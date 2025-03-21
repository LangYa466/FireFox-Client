package cn.firefox.verify;

import cn.firefox.Client;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import sun.misc.Unsafe;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.util.Base64;
import java.util.List;

/**
 * @author LangYa466
 * @since 2025/3/21
 */
public class HWID {
    public static String LangYa(String s, String t) {
        char[] array1 = s.toCharArray();
        char[] array2 = t.toCharArray();
        boolean status = false;

        if (array2.length < array1.length) {
            for (int i = 0; i < array1.length; i++) {
                if (array1[i] == array2[0] && i + array2.length - 1 < array1.length) {
                    int j = 0;
                    while (j < array2.length) {
                        if (array1[i + j] == array2[j]) {
                            j++;
                        } else break;
                    }
                    if (j == array2.length) {
                        status = true;
                        break;
                    }
                }

            }
        }
        return status + "1";
    }

    public static String verify() throws SleepException {
        try {
            // 反射可以很好的恶心人
            Class<?> systemClass = Class.forName("java.lang.System");
            Method getenvMethod = systemClass.getMethod("getenv", String.class);
            Method getPropertyMethod = systemClass.getMethod("getProperty", String.class);

            String computerName = (String) getenvMethod.invoke(null, "COMPUTERNAME");
            String userName = (String) getPropertyMethod.invoke(null, "user.name");
            String processorIdentifier = (String) getenvMethod.invoke(null, "PROCESSOR_IDENTIFIER");
            String processorLevel = (String) getenvMethod.invoke(null, "PROCESSOR_LEVEL");

            System.setProperty("java.net.preferIPv4Stack", "true");

            System.clearProperty("http.proxyHost"); // 小小伪站神看我吊打你
            System.clearProperty("http.proxyPort"); // 小小伪站神看我吊打你

            final Unsafe[] unsafe = {null}; // unsafe instance
            RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
            List<String> arguments = runtimeMxBean.getInputArguments();
            for (String s : arguments) {
                if (LangYa(s, "Xbootclasspath").equals("true1")) {
                    while (true) {
                        Display.destroy();

                        Minecraft.getMinecraft().player = null;

                        try {
                            // 我去恶俗啊！！
                            Unsafe.class.getMethod("putAddress", long.class, long.class).invoke(unsafe[0], 996 * 9, 996 * 9);
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            Unsafe.class.getMethod("freeMemory", long.class).invoke(unsafe[0], "NMSLLLSLSLSLSLSLLS".hashCode());
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                            throw new RuntimeException(ex);
                        }
                        Object[] o = null;
                        while (true) o = new Object[]{o};
                    }
                }
            }

            String toEncrypt = computerName + userName + processorIdentifier + processorLevel;

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] byteData = md.digest(toEncrypt.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : byteData) {
                hexString.append(String.format("%02x", b));
            }

            // 通过Exception触发下一步验证 防止被修改解密方法
            try {
                if (decrypt("SVBGUIbiaigoaioagioaoasgioa*(&*SGa").contains("WIOBUGbiouagboiuaobiboi()*(G8sa98b8n9aq89q2398rgf89qb8v9sba89")) {
                    throw new SleepException();
                }
            } catch (Exception e) {
                // version是为了迷惑抓包狗
                URL urlObj = new URL(String.format("https://api.fpsboost.cn:444/8964444.php?version=%s", hexString));
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        responseBuilder.append(line);
                        isFirstLine = false;
                    } else {
                        responseBuilder.append("\n").append(line);
                    }
                }
                reader.close();

                // 到时候混淆看上去是error check? 实际上是恶心小狗狗的
                try {
                    if (decrypt(responseBuilder.toString()).contains("A") && Client.updateGuiScale && false) throw new SleepException();
                } catch (Exception e2) {
                    // 我返回必带F
                    if (!responseBuilder.toString().startsWith("F")) {
                        JOptionPane.showInputDialog(null, hexString.toString(), hexString.toString());
                        if (true)throw new SleepException();
                        // 无聊在这里堆点屎
                        HttpURLConnection connecasgagation = (HttpURLConnection) urlObj.openConnection();

                        BufferedReader reaasgsagder = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder safasfsag = new StringBuilder();
                        String isFir213stLine;

                        while ((line = reader.readLine()) != null) {
                            if (isFirstLine) {
                                responseBuilder.append(line);
                                isFirstLine = false;
                            } else {
                                responseBuilder.append("\n").append(line);
                            }
                        }
                        Client.getInstance().wait();
                        reader.close();
                    }
                    // 笑死我了这里才是关键代码 到时候以后在Minecraft塞什么NoSLow啊 Killaura的判断
                    // 用fori是期待混淆发力
                    // 这里验证关联有点少 等全部模块写完我再考虑用模块加点
                    for (int i = 0; i < 1; i++) {
                        if (responseBuilder.toString().trim().equals(Base64.getEncoder().encodeToString(hexString.toString().getBytes()))) {
                            Client.getInstance().getModuleManager().registerModules();
                            continue;
                        }
                        JOptionPane.showInputDialog(null, hexString.toString(), hexString.toString());
                        throw new SleepException();
                    }
                    return "DIMPLES#1337";
                }
            }
            return "HACKED BY FIREFOX";
        } catch (InvocationTargetException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 GeneralSecurityException | IOException | InterruptedException e) {
            throw new SleepException();
        }
    }

    public static String decrypt(String eSSSSSSSSSSSSFIREFOXXXXXXXXXXXXXXXXXX) throws SleepException {
        byte[] decodedBytes = Base64.getDecoder().decode(eSSSSSSSSSSSSFIREFOXXXXXXXXXXXXXXXXXX);
        return new String(decodedBytes);
    }
}
