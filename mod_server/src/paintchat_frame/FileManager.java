package paintchat_frame;

import java.io.File;
import java.io.IOException;

import paintchat.Config;
import syi.util.Io;

public class FileManager {
    Config config;

    public FileManager(Config var1) {
        this.config = var1;
    }

    public static void copyFile(File var0, File var1) throws IOException {
        Io.copyFile(var0, var1);
    }

    public static void copyFile(String var0, String var1) {
        copyFile(var0, var1);
    }

    public static void copyFiles(String[] var0, String var1) {
        Io.copyFiles(var0, var1);
    }

    public void templateToWWW() {
        try {
            String var3 = Io.getCurrent();
            String var4 = "cnf" + File.separatorChar + "template" + File.separatorChar;
            String var5 = "index.html";
            new File(var4);
            File var6 = new File(this.config.getString("File_PaintChatClient_Dir", "www"));
            if (!var6.isDirectory()) {
                var6.mkdirs();
            }

            String[] var2 = new String[]{"pchat.jar", "pchat_user_list.swf", "entrance_normal.html", "entrance_pro.html", var5};

            for (int var7 = 0; var7 < var2.length; ++var7) {
                if (var2[var7] != var5 || this.config.getBool("App_Get_Index", true)) {
                    String var1 = var2[var7];
                    Io.copyFile(Io.makeFile(var3, var4 + var1), new File(var6, var1));
                }
            }
        } catch (Throwable var8) {
            var8.printStackTrace();
        }

    }
}
