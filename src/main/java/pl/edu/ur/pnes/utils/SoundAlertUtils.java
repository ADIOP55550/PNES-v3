package pl.edu.ur.pnes.utils;

import java.awt.*;

public class SoundAlertUtils {
    public static void playWarning() {
//        final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
//        if (runnable != null) {
//            runnable.run();
//        }

        // this works on GNU/Linux, if it does not work on Windows, then restore above code and add platform check
        Toolkit.getDefaultToolkit().beep();
    }
}
