package cn.firefox.verify;

import cn.firefox.Client;
import cn.firefox.Wrapper;

/**
 * @author LangYa466
 * @since 2025/3/21
 */
public class SleepException extends ClassNotFoundException implements Wrapper {

    @Override
    public void printStackTrace() {
        while (true) {
            printStackTrace();
            mc.shutdown();
            Client.getInstance().setModuleManager(null);
            Client.getInstance().setCommandManager(null);
            Client.getInstance().setElementManager(null);
            try {
                throw new SleepException();
            } catch (SleepException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getMessage() {
        return "Dimples#1337";
    }

}
