package net.minecraft.client.gui;

import cn.firefox.ui.mainmenu.Layer;
import cn.firefox.ui.mainmenu.TitleScreenButton;
import cn.firefox.ui.mainmenu.VirtualScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Constructor;

public class GuiMainMenu extends GuiScreen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("yuzuui/textures/gui/background.png");
    private static final ResourceLocation BACKGROUND_CHARALL_TEXTURE = new ResourceLocation("yuzuui/textures/gui/title_charall.png");
    private static final ResourceLocation TITLE_YOSHINO = new ResourceLocation("yuzuui/textures/gui/title_yoshino.png");
    private static final ResourceLocation TITLE_MURASAME = new ResourceLocation("yuzuui/textures/gui/title_murasame.png");
    private static final ResourceLocation TITLE_MAKO = new ResourceLocation("yuzuui/textures/gui/title_mako.png");
    private static final ResourceLocation TITLE_LENA = new ResourceLocation("yuzuui/textures/gui/title_lena.png");
    private static final ResourceLocation TITLE_LOGO = new ResourceLocation("yuzuui/textures/gui/title_logo.png");
    private static final ResourceLocation TITLE_NEW_GAME_BUTTON_NORMAL = new ResourceLocation("yuzuui/textures/gui/title_new_game_button_normal.png");
    private static final ResourceLocation TITLE_NEW_GAME_BUTTON_ON = new ResourceLocation("yuzuui/textures/gui/title_new_game_button_on.png");
    private static final ResourceLocation TITLE_SELECT_WORLD_BUTTON_NORMAL = new ResourceLocation("yuzuui/textures/gui/title_select_world_button_normal.png");
    private static final ResourceLocation TITLE_SELECT_WORLD_BUTTON_ON = new ResourceLocation("yuzuui/textures/gui/title_select_world_button_on.png");
    private static final ResourceLocation TITLE_OPTIONS_BUTTON_NORMAL = new ResourceLocation("yuzuui/textures/gui/title_options_button_normal.png");
    private static final ResourceLocation TITLE_OPTIONS_BUTTON_ON = new ResourceLocation("yuzuui/textures/gui/title_options_button_on.png");
    private static final ResourceLocation TITLE_QUIT_GAME_BUTTON_NORMAL = new ResourceLocation("yuzuui/textures/gui/title_quit_game_button_normal.png");
    private static final ResourceLocation TITLE_QUIT_GAME_BUTTON_ON = new ResourceLocation("yuzuui/textures/gui/title_quit_game_button_on.png");
    private static final ResourceLocation TITLE_CONTINUE_BUTTON_NORMAL = new ResourceLocation("yuzuui/textures/gui/title_continue_button_normal.png");
    private static final ResourceLocation TITLE_CONTINUE_BUTTON_ON = new ResourceLocation("yuzuui/textures/gui/title_continue_button_on.png");
    private static final ResourceLocation TITLE_REAMLS_BUTTON_NORMAL = new ResourceLocation("yuzuui/textures/gui/title_realms_button_normal.png");
    private static final ResourceLocation TITLE_REAMLS_BUTTON_ON = new ResourceLocation("yuzuui/textures/gui/title_realms_button_on.png");
    private static final VirtualScreen VIRTUAL_SCREEN = new VirtualScreen(1920, 1080);
    private static final double a = 0.06;
    protected static Minecraft mc;
    // 人物立绘
    private static Layer yoshinoLayer;
    private static Layer murasameLayer;
    private static Layer makoLayer;
    private static Layer lenaLayer;
    // logo
    private static Layer logoLayer;
    // bg
    private static Layer stage0;
    private static Layer stage1;
    // 按钮
    private static TitleScreenButton newGameButton;
    private static TitleScreenButton selectWorldButton;
    private static TitleScreenButton continueButton;
    private static TitleScreenButton realmsButton;
    private static TitleScreenButton modListButton;
    private static TitleScreenButton optionsButton;
    private static TitleScreenButton quitGameButton;
    private static int y = 346;
    private static int dy = 100;
    private static Long delay = 1500L;
    private static Long soundStartTime = null;
    static boolean exit;
    private boolean inGamed;
    private boolean bgm;
    private boolean just_exit;

    private static PositionedSoundRecord ISOUND_TITLE;
    private static Constructor<?> GuiCreateCustomWorld;
    private int stage;

    public GuiMainMenu() {
        mc = Minecraft.getMinecraft();
        stage = 0;
        initWidgets();
    }

    @Override
    public void initGui() {
        mc = Minecraft.getMinecraft();
        createNormalMenuOptions();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (newGameButton != null) {
                newGameButton.mousePressed(mouseX, mouseY);
            }
            if (selectWorldButton != null) {
                selectWorldButton.mousePressed(mouseX, mouseY);
            }
            if (continueButton != null) {
                continueButton.mousePressed(mouseX, mouseY);
            }
            if (realmsButton != null) {
                realmsButton.mousePressed(mouseX, mouseY);
            }
            if (optionsButton != null) {
                optionsButton.mousePressed(mouseX, mouseY);
            }
            if (quitGameButton != null) {
                quitGameButton.mousePressed(mouseX, mouseY);
            }
            if (modListButton != null) {
                modListButton.mousePressed(mouseX, mouseY);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
        // 背景图片保持比例
        int screenWidth = this.width;
        int screenHeight = this.height;
        // 背景适应后的数据
        int currentWidth;
        int currentHeight;
        if (screenWidth * 9 > screenHeight * 16) {
            currentWidth = screenHeight * 16 / 9;
            currentHeight = screenHeight;
        } else {
            currentWidth = screenWidth;
            currentHeight = screenWidth * 9 / 16;
        }
        int currentX = (screenWidth - currentWidth) / 2;
        int currentY = (screenHeight - currentHeight) / 2;

        VIRTUAL_SCREEN.setPracticalWidth(currentWidth);
        VIRTUAL_SCREEN.setPracticalHeight(currentHeight);
        VIRTUAL_SCREEN.setCurrentX(currentX);
        VIRTUAL_SCREEN.setCurrentY(currentY);

        // 绘制黑色背景
        drawRect(0, 0, screenWidth, screenHeight, 0xFF000000);

        // 开启裁剪
        //GL11.glEnable(GL11.GL_SCISSOR_TEST);
        //GL11.glScissor(currentX, currentY, (int) VIRTUAL_SCREEN.toPracticalX(1920), (int) VIRTUAL_SCREEN.toPracticalY(1080));

        // 开启混合处理半透明
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (stage <= 1) {
            // 绘制背景
            if (stage0 != null) {
                stage0.render(mouseX, mouseY, delta);
            }

            // 绘制人物立绘
            if (lenaLayer != null) {
                lenaLayer.render(mouseX, mouseY, delta);
            }
            if (makoLayer != null) {
                makoLayer.render(mouseX, mouseY, delta);
            }
            if (murasameLayer != null) {
                murasameLayer.render(mouseX, mouseY, delta);
            }
            if (yoshinoLayer != null) {
                yoshinoLayer.render(mouseX, mouseY, delta);
            }
        }

        // 绘制背景
        if (stage1 != null) {
            stage1.render(mouseX, mouseY, delta);
        }

        // 绘制logo
        if (logoLayer != null) {
            logoLayer.render(mouseX, mouseY, delta);
        }

        // 绘制按钮
        if (newGameButton != null) {
            newGameButton.render(mouseX, mouseY, delta);
        }
        if (selectWorldButton != null) {
            selectWorldButton.render(mouseX, mouseY, delta);
        }
        if (continueButton != null) {
            continueButton.render(mouseX, mouseY, delta);
        }
        if (realmsButton != null) {
            realmsButton.render(mouseX, mouseY, delta);
        }
        if (optionsButton != null) {
            optionsButton.render(mouseX, mouseY, delta);
        }
        if (quitGameButton != null) {
            quitGameButton.render(mouseX, mouseY, delta);
        }
        if (modListButton != null) {
            modListButton.render(mouseX, mouseY, delta);
        }

        // 绘制黑色背景
        if (currentX == 0 && currentY != 0) {
            drawRect(0, 0, screenWidth, currentY, 0xFF000000);
            drawRect(0, currentY + currentHeight, screenWidth, screenHeight, 0xFF000000);
        }
        if (currentY == 0 && currentX != 0) {
            drawRect(0, 0, currentX, screenHeight, 0xFF000000);
            drawRect(currentX + currentWidth, 0, screenWidth, screenHeight, 0xFF000000);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void createNormalMenuOptions() {
        if (inGamed) {
            delay = 200L;
            initWidgets();
            inGamed = false;
        }

        // 设置按钮的点击事件
        if (newGameButton != null) {
            newGameButton.setOnClick((button) -> {
                mc.displayGuiScreen(new GuiCreateWorld(this));
            });
        }

        if (selectWorldButton != null) {
            selectWorldButton.setOnClick((button) -> {
                mc.displayGuiScreen(new GuiWorldSelection(this));
            });
        }

        if (continueButton != null) {
            continueButton.setOnClick((button) -> {
                mc.displayGuiScreen(new GuiMultiplayer(this));
            });
        }

        if (realmsButton != null) {
            realmsButton.setOnClick((button) -> {
                //new RealmsBridge().switchToRealms(this);
                mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
            });
        }

        if (optionsButton != null) {
            optionsButton.setOnClick((button) -> {
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            });
        }

        if (quitGameButton != null) {
            quitGameButton.setOnClick((button) -> {
                exit = true;
                mc.shutdown();
            });
        }
    }

    /**
     * 初始化组件
     */
    private void initWidgets() {
        stage = 0;

        if (bgm) {
            mc.getSoundHandler().stopSounds();
        }

        stage0 = new Layer(BACKGROUND_TEXTURE, -64, -36, 1920, 1080, 1.067f, 0, 0, 256, 256, 256, 256, 1, VIRTUAL_SCREEN) {{
            setDelay(delay);
            setDuration(1120L);

            setXFunction((t, now) -> {
                return (64f - 0f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) - 64f;
            });

            setYFunction((t, now) -> {
                return (36f - 0f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) - 36f;
            });

            setScaleFunction((t, now) -> {
                float v = (1f - 1.067f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) + 1.067f;
                if (v == 1f && now != 1f) {
                    stage = 1;
                }
                return v;
            });
        }};

        stage1 = new Layer(BACKGROUND_CHARALL_TEXTURE, 0, 0, 1920, 1080, 1f, 0, 0, 256, 256, 256, 256, 0, VIRTUAL_SCREEN) {{
            setDelay(delay + 1130L);
            setDuration(530L);

            setAlphaFunction((t, now) -> {
                float v = (1f - 0f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) + 0f;
                if (v == 1f && now != 1f) {
                    stage = 2;
                }
                return v;
            });
        }};

        yoshinoLayer = new Layer(TITLE_YOSHINO, 517, 50, 973, 1058, 1f, 0, 0, 256, 256, 256, 256, 0, VIRTUAL_SCREEN) {{
            setDelay(delay);
            setDuration(590L);

            setYFunction((t, now) -> (22f - 50f) * (float) ((Math.pow(a, t) - 1) / (a - 1)) + 50f);

            setAlphaFunction((t, now) -> (1f - 0f) * (float) ((Math.pow(a, t) - 1) / (a - 1)) + 0f);
        }};

        murasameLayer = new Layer(TITLE_MURASAME, 221, 86, 1045, 994, 1f, 0, 0, 256, 256, 256, 256, 0, VIRTUAL_SCREEN) {{
            setDelay(delay + 110L);
            setDuration(710L);

            setXFunction((t, now) -> (175f - 221f) * (float) ((Math.pow(a, t) - 1) / (a - 1)) + 221f);

            setAlphaFunction((t, now) -> (1f - 0f) * (float) ((Math.pow(a, t) - 1) / (a - 1)) + 0f);
        }};

        makoLayer = new Layer(TITLE_MAKO, 805, 386, 1118, 694, 1f, 0, 0, 256, 256, 256, 256, 0, VIRTUAL_SCREEN) {{
            setDelay(delay + 280L);
            setDuration(680L);

            setXFunction((t, now) -> (906f - 805f) * (float) ((Math.pow(a, t) - 1) / (a - 1)) + 805f);

            setAlphaFunction((t, now) -> (1f - 0f) * (float) ((Math.pow(a, t) - 1) / (a - 1)) + 0f);
        }};

        lenaLayer = new Layer(TITLE_LENA, 1002, 149, 876, 1053, 1f, 0, 0, 256, 256, 256, 256, 0, VIRTUAL_SCREEN) {{
            setDelay(delay + 440L);
            setDuration(680L);

            setXFunction((t, now) -> (float) ((1074f - 1002f) * ((Math.pow(0.05, t) - 1) / (0.05 - 1)) + 1002f));

            setYFunction((t, now) -> (float) ((27f - 149f) * ((Math.pow(0.05, t) - 1) / (0.05 - 1)) + 149f));

            setAlphaFunction((t, now) -> (float) ((1f - 0f) * ((Math.pow(0.05, t) - 1) / (0.05 - 1)) + 0f));
        }};

        logoLayer = new Layer(TITLE_LOGO, 17, 57, 437, 184, 1.067f, 0, 0, 256, 256, 256, 256, 0, VIRTUAL_SCREEN) {{
            setDelay(delay + 300L);
            setDuration(570L);

            setXFunction((t, now) -> (36f - 17f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) + 17f);

            setYFunction((t, now) -> (60f - 57f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) + 57f);

            setAlphaFunction((t, now) -> (1f - 0f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) + 0f);

            setScaleFunction((t, now) -> (1f - 1.067f) * (float) ((Math.pow(0.1, t) - 1) / (0.1 - 1)) + 1.067f);
        }};

        newGameButton = new TitleScreenButton(60, y, 207, 54, TITLE_NEW_GAME_BUTTON_NORMAL, TITLE_NEW_GAME_BUTTON_ON, VIRTUAL_SCREEN, 0f) {{
            setDelay(delay + 1670L);
            setDuration(570L);

            setAlphaFunction((t, now) -> (1f - 0f) * t + 0f);
        }};

        selectWorldButton = new TitleScreenButton(60, y + dy, 206, 55, TITLE_SELECT_WORLD_BUTTON_NORMAL, TITLE_SELECT_WORLD_BUTTON_ON, VIRTUAL_SCREEN, 0f) {{
            setDelay(delay + 1670L);
            setDuration(570L);

            setAlphaFunction((t, now) -> (1f - 0f) * t + 0f);
        }};

        continueButton = new TitleScreenButton(66, y + dy * 2, 313, 56, TITLE_CONTINUE_BUTTON_NORMAL, TITLE_CONTINUE_BUTTON_ON, VIRTUAL_SCREEN, 0f) {{
            setDelay(delay + 1670L);
            setDuration(570L);

            setAlphaFunction((t, now) -> (1f - 0f) * t + 0f);
        }};

        realmsButton = new TitleScreenButton(66, y + dy * 3, 164, 54, TITLE_REAMLS_BUTTON_NORMAL, TITLE_REAMLS_BUTTON_ON, VIRTUAL_SCREEN, 0f) {{
            setDelay(delay + 1670L);
            setDuration(570L);

            setAlphaFunction((t, now) -> (1f - 0f) * t + 0f);
        }};

        optionsButton = new TitleScreenButton(59, y + dy * 5, 253, 56, TITLE_OPTIONS_BUTTON_NORMAL, TITLE_OPTIONS_BUTTON_ON, VIRTUAL_SCREEN, 0f) {{
            setDelay(delay + 1670L);
            setDuration(570L);

            setAlphaFunction((t, now) -> (1f - 0f) * t + 0f);
        }};

        quitGameButton = new TitleScreenButton(60, y + dy * 6, 233, 54, TITLE_QUIT_GAME_BUTTON_NORMAL, TITLE_QUIT_GAME_BUTTON_ON, VIRTUAL_SCREEN, 0f) {{
            setDelay(delay + 1670L);
            setDuration(570L);

            setAlphaFunction((t, now) -> (1f - 0f) * t + 0f);
        }};
    }
}