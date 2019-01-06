package paintchat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;

public class Resource {
    private static Hashtable table = new Hashtable();
    public static final String RESOURCE = "Resource";
    public static final String CONFIG = "Config";
    public static final String APP = "Application";
    public static final String SERVER = "Server";
    public static final String HTTP = "Http";
    public static final String CLIENT = "Client";
    public static final String R_ERROR = "error";

    private static void addResource(String var0, String var1) {
        Res var2 = new Res();
        var2.load(getContent(var0, var1));
        table.put(var1, var2);
    }

    private static String getContent(String var0, String var1) {
        int var2 = var0.indexOf('<' + var1 + '>');
        if (var2 == -1) {
            return null;
        } else {
            int var3 = var0.indexOf("</" + var1 + '>', var2);
            if (var3 == -1) {
                var3 = var0.length();
            }

            return var0.substring(var2, var3);
        }
    }

    private static File getResourceFile() {
        String var0 = "resource";
        String var1 = ".properties";
        Locale var2 = Locale.getDefault();
        File var3 = new File(System.getProperty("user.dir"), "cnf");
        File var4 = null;
        var4 = new File(var3, var0 + var2.getLanguage());
        if (!var4.exists()) {
            var4 = new File(var3, var0 + '_' + var2.getLanguage() + var1);
            if (!var4.exists()) {
                var4 = new File(var3, var0 + var1);
            }
        }

        return var4;
    }

    public static synchronized void loadResource() {
        try {
            if (table.size() >= 4) {
                return;
            }

            File var0 = getResourceFile();
            StringBuffer var1 = new StringBuffer();
            BufferedReader var2 = new BufferedReader(new FileReader(var0));

            int var3;
            while ((var3 = var2.read()) != -1) {
                var1.append((char) var3);
            }

            var2.close();
            String var4 = getContent(var1.toString(), "Resource");
            addResource(var4, "Config");
            addResource(var4, "Application");
            addResource(var4, "Server");
            addResource(var4, "Http");
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static synchronized Res loadResource(String var0) {
        try {
            Res var1 = (Res) table.get(var0);
            if (var1 != null) {
                return var1;
            } else {
                File var2 = getResourceFile();
                StringBuffer var3 = new StringBuffer();
                BufferedReader var4 = new BufferedReader(new FileReader(var2));

                int var5;
                while ((var5 = var4.read()) != -1) {
                    var3.append((char) var5);
                }

                var4.close();
                String var6 = getContent(var3.toString(), "Resource");
                addResource(var6, var0);
                return (Res) table.get(var0);
            }
        } catch (IOException var7) {
            var7.printStackTrace();
            return null;
        }
    }
}
