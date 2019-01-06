package paintchat_frame;

import java.awt.Container;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;
import syi.awt.LTextField;
import syi.awt.MessageBox;
import syi.util.Io;

public class PopupMenuPaintChat implements ActionListener {
    private Debug debug;
    private Res res;
    private Config config;
    private LTextField textField;
    public String strSelected;
    private String strAuto;
    private String strCgi;

    public PopupMenuPaintChat(Debug var1, Config var2, Res var3) {
        this.debug = var1;
        this.config = var2;
        this.res = var3;
    }

    public void actionPerformed(ActionEvent var1) {
        try {
            this.doAction(var1.getActionCommand());
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public static String addressToString(byte[] var0) {
        StringBuffer var1 = new StringBuffer();

        for (int var2 = 0; var2 < 4; ++var2) {
            if (var2 > 0) {
                var1.append('.');
            }

            var1.append(var0[var2] & 255);
        }

        return var1.toString();
    }

    public void copyURL(String var1) {
        StringBuffer var2 = new StringBuffer();
        var2.append("http://");
        var2.append(var1);
        int var3 = this.config.getInt("Connection_Port_Http", 80);
        if (var3 != 80) {
            var2.append(':');
            var2.append(var3);
        }

        var2.append('/');
        StringSelection var4 = new StringSelection(var2.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var4, var4);
    }

    private void doAction(String var1) {
        try {
            if (var1.equals(this.strAuto)) {
                var1 = addressToString(getGlobalAddress().getAddress());
            } else if (var1.equals(this.strCgi)) {
                var1 = this.getAddressFromCGI();
                if (var1 == null || var1.length() <= 0) {
                    MessageBox.alert("NotfoundCGI", "TitleOfError");
                    return;
                }
            }

            this.textField.setText(var1);
            this.copyURL(var1);
        } catch (Throwable var2) {
        }

    }

    public static String getAddress(Config var0, Debug var1) {
        InetAddress var2 = null;

        try {
            boolean var3 = var0.getBool("Connection_GrobalAddress");
            if (var3) {
                var2 = selectGlobalAddress(getGlobalAddress());
            } else {
                var2 = InetAddress.getLocalHost();
            }
        } catch (IOException var5) {
            var2 = null;
        }

        if (var2 == null) {
            try {
                var2 = InetAddress.getLocalHost();
            } catch (IOException var4) {
                var2 = null;
            }
        }

        if (var2 == null) {
            var1.logRes("BadAddress");
            return "127.0.0.1";
        } else {
            byte[] var6 = var2.getAddress();
            if (!isGlobalIP(var6)) {
                var1.logRes("LocalAddress");
            }

            return addressToString(var6);
        }
    }

    public String getAddressFromCGI() throws IOException {
        String var1 = this.config.getString("App_Cgi");
        if (var1 != null && var1.length() > 0) {
            URL var2 = new URL(var1);
            BufferedInputStream var3 = new BufferedInputStream(var2.openStream());
            StringBuffer var4 = new StringBuffer();

            try {
                while (true) {
                    while (true) {
                        int var5 = Io.r(var3);
                        if (var5 == 37) {
                            var4.append((char) (Character.digit((char) ((char) Io.r(var3)), 16) << 4 | Character.digit((char) ((char) Io.r(var3)), 16)));
                        } else {
                            var4.append((char) var5);
                        }
                    }
                }
            } catch (EOFException var6) {
                var3.close();
                return var4.toString();
            }
        } else {
            return null;
        }
    }

    private static InetAddress getGlobalAddress() throws IOException {
        InetAddress var0 = InetAddress.getLocalHost();

        InetAddress var1;
        try {
            Socket var2 = new Socket((new URL(System.getProperty("java.vendor.url"))).getHost(), 80);
            var1 = var2.getLocalAddress();
            var2.close();
            var2 = null;
        } catch (IOException var3) {
            var1 = var0;
        }

        return selectGlobalAddress(var1);
    }

    public static boolean isGlobalIP(byte[] var0) {
        if (var0[0] == 127 && var0[1] == 0 && var0[2] == 0 && var0[3] == 1) {
            return false;
        } else if (var0[0] == 10) {
            return false;
        } else if (var0[0] == 172 && var0[1] >= 16 && var0[1] < 31) {
            return false;
        } else {
            return var0[0] != 192 || var0[1] != 168;
        }
    }

    private static InetAddress selectGlobalAddress(InetAddress var0) {
        try {
            byte[] var1 = var0.getAddress();
            if (!isGlobalIP(var1)) {
                InetAddress[] var2 = InetAddress.getAllByName(var0.getHostAddress());

                for (int var3 = 0; var3 < var2.length; ++var3) {
                    var1 = var2[var3].getAddress();
                    if (isGlobalIP(var1)) {
                        return var2[var3];
                    }
                }
            }
        } catch (IOException var4) {
        } catch (RuntimeException var5) {
        }

        return var0;
    }

    public void show(Container var1, LTextField var2, int var3, int var4) {
        PopupMenu var5 = new PopupMenu();
        var5.addActionListener(this);
        this.strAuto = this.res.get("ConnectAuto");
        this.strCgi = this.res.get("ConnectCGI");
        this.textField = var2;
        var5.add(getAddress(this.config, this.debug));
        var5.addSeparator();
        var5.add(this.strAuto);
        var5.add(this.strCgi);
        var1.add(var5);
        var5.show(var1, var3, var4);
    }
}
