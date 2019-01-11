package paintchat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import syi.util.PProperties;

public class Config extends PProperties {
    private static String strSep = null;
    public static final String CF_CHATMASTER = "Admin_ChatMaster";
    private final String CF_CHATMASTER2 = "ChatMaster";
    public static final String CF_PASSWORD = "Admin_Password";
    private final String CF_PASSWORD2 = "password";
    public static final String CF_MAX_USER = "Server_User_Max";
    public static final String CF_GUEST = "Server_User_Guest";
    private final String CF_GUEST2 = "guest";
    public static final String CF_CONNECT = "Connection_GrobalAddress";
    public final String CF_CONNECT2 = "co";
    public static final String CF_PORT = "Connection_Port_PaintChat";
    public final String CF_PORT2 = "k";
    public static final String CF_PORT_HTTP = "Connection_Port_Http";
    public static final String CF_CONNECTION_MAX = "Connection_Max";
    public static final String CF_CONNECTION_TIMEOUT = "Connection_Timeout";
    public static final String CF_HOST = "Connection_Host";
    private final String CF_HOST2 = "h";
    public static final String CF_INFOMATION = "Server_Infomation";
    private final String CF_INFOMATION2 = "i";
    public static final String CF_LOG_LINE = "Server_Log_Line";
    public final String CF_LOG_LINE2 = "ss";
    public static final String CF_LOG_SERVER = "Server_Log_Server";
    private final String CF_LOG_SERVER2 = "Log";
    public static final String CF_LOG_HTTP = "Http_Log";
    public static final String CF_SERVER_DEBUG = "Server_Debug";
    public static final String CF_LOG_TEXT = "Server_Log_Text";
    private final String CF_LOG_TEXT2 = "Log_Text";
    public static final String CF_LOAD_LINE = "Server_Load_Line";
    private final String CF_LOAD_LINE2 = "Load";
    public static final String CF_LOAD_TEXT = "Server_Load_Text";
    public static final String CF_CASH_TEXT = "Server_Cash_Text";
    private final String CF_CASH_TEXT2 = "Caches_Text";
    public final String CF_MAX_TEXT2 = "ht";
    public final String CF_MAX_TEXT3 = "Cash_Text_Size";
    public static final String CF_CASH_LINE = "Server_Cash_Line";
    private final String CF_CASH_LINE2 = "Caches_Line";
    public static final String CF_CASH_LINE_SIZE = "Server_Cash_Line_Size";
    public static final String CF_CASH_TEXT_SIZE = "Server_Cash_Text_Size";
    public static final String CF_LOG_LINE_DIR = "Server_Log_Line_Dir";
    public static final String CF_LOG_TEXT_DIR = "Server_Log_Text_Dir";
    public final String CF_MAX_LINE2 = "hl";
    public final String CF_MAX_LINE3 = "Max_Line";
    public static final String CF_WIDTH = "Client_Image_Width";
    public final String CF_CANVAS_SIZE_X2 = "cx";
    public static final String CF_HEIGHT = "Client_Image_Height";
    public final String CF_CANVAS_SIZE_Y2 = "cy";
    public static final String CF_SOUND = "Client_Sound";
    public static final String CF_SOUND2 = "so";
    public static final String CF_FILE_CONFIG = "File_Config";
    public static final String CF_FILE_PAINTCHAT_CLIENT_DIR = "File_PaintChatClient_Dir";
    public static final String CF_FILE_PAINTCHAT_SERVER_DIR = "File_PaintChatServer_Dir";
    public static final String CF_FILE_HTTP_DIR = "File_Http_Dir";
    public static final String CF_FILE_PAINTCHAT_INFOMATION = "File_PaintChat_Infomation";
    public static int DEF_PORT = 41411;
    public static final String CF_APP_VERSION = "App_Version";
    public static final String CF_APP_IS_CONSOLE = "App_IsConsole";
    public static final String CF_APP_SHOW_STARTHELP = "App_ShowStartHelp";
    public static final String CF_APP_SHOW_HELP = "App_ShowHelp";
    public static final String CF_APP_AUTO_HTTP = "App_Auto_Http";
    public static final String CF_APP_AUTO_CHAT = "App_Auto_Paintchat";
    public static final String CF_APP_AUTO_LOBBY = "App_Auto_Lobby";
    public static final String CF_APP_CGI = "App_Cgi";
    public static final String CF_APP_JVM_PATH = "App_JvmPath";
    public static final String CF_APP_GET_INDEX = "App_Get_Index";
    public static final String CF_HTTP_DIR = "Http_Dir";
    public static final String CF_CLIENT_IMAGE_WIDTH = "Client_Image_Width";
    public static final String CF_CLIENT_IMAGE_HEIGHT = "Client_Image_Height";
    public static final String CF_CLIENT_PERMISSION = "Client_Permission";

    public Config(Object var1) throws IOException {
        this.loadConfig(var1);
    }

    public void appendParam(StringBuffer var1, String var2) {
        var1.append("<param name=\"");
        var1.append(var2);
        var1.append("\" value=\"");
        var1.append(this.getString("Connection_Port_PaintChat"));
        var1.append("\">");
        var1.append(this.getSeparator());
    }

    public String getSeparator() {
        if (strSep == null) {
            strSep = System.getProperty("line.separator");
        }

        return strSep;
    }

    public void loadConfig(Object var1) {
        try {
            if (var1 instanceof Hashtable) {
                Hashtable var2 = (Hashtable) var1;
                Enumeration var4 = var2.keys();

                while (var4.hasMoreElements()) {
                    Object var3 = var4.nextElement();
                    this.put(var3, var2.get(var3));
                }
            } else if (var1 instanceof String) {
                File var6 = new File((String) var1);
                this.loadPut((InputStream) (new FileInputStream(var6)));
                this.put("File_Config", var6.getCanonicalPath());
            } else if (var1 instanceof File) {
                this.loadPut((InputStream) (new FileInputStream((File) var1)));
                this.put("File_Config", ((File) var1).getCanonicalPath());
            }
        } catch (IOException var5) {
            this.put("File_Config", var1.toString());
        }

        this.replaceOldKeys();
        this.setDefault();
    }

    private void putDef(String var1, String var2) {
        this.put(var1, this.getString(var1, var2));
    }

    private void replaceOldKeys() {
        String[][] var1 = new String[][]{{"Admin_ChatMaster", "ChatMaster"}, {"Admin_Password", "password"}, {"Server_User_Guest", "guest"}, {"Connection_GrobalAddress", "co"}, {"Connection_Port_PaintChat", "k"}, {"Connection_Host", "h"}, {"Server_Infomation", "i"}, {"Server_Log_Line", "ss"}, {"Server_Log_Server", "Log"}, {"Server_Log_Text", "Log_Text"}, {"Server_Load_Line", "Load"}, {"Server_Cash_Text", "Caches_Text"}, {"Server_Cash_Line", "Caches_Line"}, {"Server_Cash_Line_Size", "hl", "Max_Line"}, {"Client_Image_Width", "cx"}, {"Client_Image_Height", "cy"}, {"Client_Sound", "so"}};

        for (int var3 = 0; var3 < var1.length; ++var3) {
            for (int var4 = 1; var4 < var1[var3].length; ++var4) {
                Object var2 = this.get(var1[var3][var4]);
                if (var2 != null) {
                    this.put(var1[var3][0], var2);
                    this.remove(var1[var3][var4]);
                }
            }
        }

    }

    public void saveConfig(File var1, String var2) {
        try {
            this.save(new FileOutputStream(var1 == null ? new File(this.getString("File_Config", "./cnf/paintchat.cf")) : var1), Resource.loadResource(var2 != null && var2.length() > 0 ? var2 : "Config"));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void setDefault() {
        String var1 = "true";
        String var2 = "false";
        this.putDef("App_IsConsole", var1);
        this.putDef("App_Auto_Paintchat", var1);
        this.putDef("App_Auto_Http", var1);
        this.putDef("App_Auto_Lobby", var1);
        this.putDef("App_ShowStartHelp", var1);
        this.putDef("App_Get_Index", var1);
        this.putDef("Connection_GrobalAddress", var1);
        this.putDef("File_PaintChat_Infomation", "www/.paintchat");
        this.putDef("Server_Cash_Line", var1);
        this.putDef("Server_Cash_Text", var1);
        this.putDef("Server_Log_Line", var2);
        this.putDef("Server_Log_Text", var2);
        this.putDef("Server_Load_Line", var1);
        this.putDef("Server_Load_Text", var1);
        this.putDef("Server_Cash_Line_Size", "512000");
        this.putDef("Server_Cash_Text_Size", "200");
        this.putDef("Client_Image_Width", "1200");
        this.putDef("Client_Image_Height", "900");
        this.putDef("Client_Permission", "layer:all;layer_edit:true;canvas:true;talk:true;fill:false;clean:true;");
    }

    public static String setParams(String var0, String var1, String var2) {
        int var3 = var0.indexOf("<!--" + var1 + "-->");
        int var4 = var0.indexOf("<!--/" + var1 + "-->");
        if (var3 >= 0 && var4 >= 0) {
            var3 += var1.length() + 7;
            StringBuffer var5 = new StringBuffer();
            var5.append(var0.substring(0, var3));
            var5.append(var2);
            var5.append(var0.substring(var4, var0.length()));
            return var5.toString();
        } else {
            return var0;
        }
    }
}
