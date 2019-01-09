package syi.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import syi.util.Io;
import syi.util.PProperties;

public class Gui extends Awt {
    private static PProperties resource = null;

    public static File fileDialog(Window var0, String var1, boolean var2) {
        String var3;
        String var4;
        try {
            String var5;
            if (resource == null) {
                var5 = (var2 ? "書き込む" : "読みこむ") + "ファイルを選択してください";
            } else {
                var5 = resource.getString("Dialog." + (var2 ? "Save" : "Load"));
            }

            Frame var6 = var0 instanceof Frame ? (Frame) var0 : Awt.getPFrame();
            FileDialog var7 = new FileDialog(var6, var5, var2 ? 1 : 0);
            if (var1 != null) {
                var7.setFile(var1);
            }

            var7.setModal(true);
            var7.setVisible(true);
            var3 = var7.getDirectory();
            var4 = var7.getFile();
            if (var4.equals("null") || var4.equals("null")) {
                return null;
            }
        } catch (RuntimeException var8) {
            var8.printStackTrace();
            return null;
        }

        return new File(var3, var4);
    }

    public static String getClipboard() {
        String var0 = null;

        try {
            StringSelection var1 = new StringSelection("");
            Transferable var2 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(var1);
            if (var2 != null) {
                var0 = (String) var2.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception var3) {
            var0 = null;
        }

        return var0 == null ? "" : var0;
    }

    public static void getDefSize(Component var0) {
        Dimension var1 = Toolkit.getDefaultToolkit().getScreenSize();
        var0.setSize(var1.width / 2, var1.height / 2);
    }

    public static Point getScreenPos(Component var0, Point var1) {
        Point var2 = var0.getLocationOnScreen();
        var2.translate(var1.x, var1.y);
        return var2;
    }

    public static Point getScreenPos(MouseEvent event) {
        Point var1 = event.getComponent().getLocationOnScreen();
        var1.translate(event.getX(), event.getY());
        return var1;
    }

    public static void giveDef(Component var0) {
        if (var0 instanceof Container) {
            Component[] var1 = ((Container) var0).getComponents();

            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2] != null) {
                    giveDef(var1[var2]);
                }
            }
        }

        Awt.getDef(var0);
    }

    public static void pack(Container var0) {
        Component[] var1 = var0.getComponents();
        if (var1 != null) {
            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2] instanceof Container) {
                    pack((Container) var1[var2]);
                } else if (!var1[var2].isValid()) {
                    var1[var2].validate();
                }
            }
        }

        if (!var0.isValid()) {
            var0.validate();
        }

    }

    public static boolean showDocument(String var0, PProperties var1, Hashtable var2) {
        Runtime var3 = Runtime.getRuntime();

        try {
            File var4;
            if (var0.startsWith("http://")) {
                var4 = new File(Io.getCurrent(), "cnf/dummy.html");
                FileOutputStream var5 = new FileOutputStream(var4);
                var5.write(("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;URL=" + var0 + "\"></head></html>").getBytes());
                var5.flush();
                var5.close();
            } else {
                var4 = new File(var0);
            }

            String var10 = var4.getCanonicalPath();
            String var6 = "App.BrowserPath";
            if (Awt.isWin()) {
                try {
                    var3.exec(new String[]{var1.getString(var6, "explorer"), var10});
                    return true;
                } catch (IOException var8) {
                }
            }

            String var7 = var1.getString(var6);
            if (var7.length() <= 0) {
                if (MessageBox.confirm("NeedBrowser", "Option")) {
                    var7 = fileDialog(Awt.getPFrame(), "", false).getCanonicalPath();
                } else {
                    var7 = "false";
                }

                var1.put(var6, var7);
            }

            if (var7 != null && var7.length() > 0 && !var7.equalsIgnoreCase("false")) {
                var3.exec(new String[]{var7, var10});
                return true;
            } else {
                return false;
            }
        } catch (Throwable var9) {
            System.out.println((Object) var9);
            return false;
        }
    }
}
