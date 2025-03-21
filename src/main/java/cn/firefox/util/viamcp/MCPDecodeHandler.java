package cn.firefox.util.viamcp;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.types.VarIntType;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.CancelDecoderException;
import com.viaversion.viaversion.protocols.v1_16_4to1_17.Protocol1_16_4To1_17;
import com.viaversion.viaversion.protocols.v1_16_4to1_17.packet.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.v1_17to1_17_1.packet.ClientboundPackets1_17_1;
import com.viaversion.viaversion.util.PipelineUtil;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.client.Minecraft;

import java.util.List;

@ChannelHandler.Sharable
public class MCPDecodeHandler extends MessageToMessageDecoder<ByteBuf> {
    private final UserConnection info;
    private boolean handledCompression;
    private boolean skipDoubleTransform;
    public static final VarIntType VAR_INT = new VarIntType();
    public static final IntType INT = new IntType();

    public MCPDecodeHandler(final UserConnection info) {
        this.info = info;
    }

    public UserConnection getInfo() {
        return info;
    }

    // https://github.com/ViaVersion/ViaVersion/blob/master/velocity/src/main/java/us/myles/ViaVersion/velocity/handlers/VelocityDecodeHandler.java
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> out) throws Exception {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            out.add(bytebuf.retain());
            return;
        }

        if (!info.checkIncomingPacket()) {
            throw CancelDecoderException.generate(null);
        }

        if (!info.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }

        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);

        ByteBuf raw = transformedBuf.copy();

        boolean cancel = false;

        // 1.17 fix by fyxar
        if (ViaLoadingBase.getInstance().getTargetVersion().getVersion() > 47) {
            try {
                if (VAR_INT.read(raw) == ClientboundPackets1_17_1.PING.getId()) {
                    int id = INT.read(raw);

                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        PacketWrapper wrapper = PacketWrapper.create(ServerboundPackets1_17.PONG, info);
                        wrapper.write(INT, id);
                        try {
                            wrapper.sendToServer(Protocol1_16_4To1_17.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    cancel = true;
                }
            } finally {
                raw.release();
            }
        }

        if(cancel) return;

        try {
            final boolean needsCompress = handleCompressionOrder(ctx, transformedBuf);

            info.transformIncoming(transformedBuf, CancelDecoderException::generate);

            if (needsCompress) {
                CommonTransformer.compress(ctx, transformedBuf);
                skipDoubleTransform = true;
            }

            out.add(transformedBuf.retain());
        } finally {
            transformedBuf.release();
        }
    }

    private boolean handleCompressionOrder(final ChannelHandlerContext ctx, final ByteBuf buf) throws Exception {
        if (handledCompression) {
            return false;
        }

        final int decoderIndex = ctx.pipeline().names().indexOf("decompress");

        if (decoderIndex == -1) {
            return false;
        }

        handledCompression = true;

        if (decoderIndex > ctx.pipeline().names().indexOf("via-decoder")) {
            // Need to decompress this packet due to bad order
            CommonTransformer.decompress(ctx, buf);
            final ChannelHandler encoder = ctx.pipeline().get("via-encoder");
            final ChannelHandler decoder = ctx.pipeline().get("via-decoder");
            ctx.pipeline().remove(encoder);
            ctx.pipeline().remove(decoder);
            ctx.pipeline().addAfter("compress", "via-encoder", encoder);
            ctx.pipeline().addAfter("decompress", "via-decoder", decoder);
            return true;
        }

        return false;
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (PipelineUtil.containsCause(cause, CancelCodecException.class)) {
            return;
        }
        super.exceptionCaught(ctx, cause);
    }
}