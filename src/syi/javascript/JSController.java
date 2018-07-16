package syi.javascript;

import jaba.applet.Applet;

import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

public class JSController {
    private boolean isCash = false;
    private Class cJava = null;
    private Object oJava = null;
    private Method mGet;
    private Method mCall;
    private Method mGetMember;
    private Method mSetMember;
    private Method mSetSlot;
    private Method mGetSlot;
    private Method mEval;
    private Applet applet;
    private Hashtable tables = null;
    private SimpleDateFormat dateFormat = null;
    private static final String STR_NO_SUPPORT = "No support JavaScript";
    private static final String STR_VERSION = "JavaScript Controller (C)shi-chan 2001";

    public JSController(Applet var1) throws IOException {
        System.out.println("JavaScript Controller (C)shi-chan 2001");
        this.applet = var1;
        this.setup();
    }

    public void alert(String var1) throws IOException {
        this.callFunction("alert", new Object[]{var1});
    }

    public String callFunction(String var1, Object[] var2) throws IOException {
        try {
            if (this.mCall == null) {
                this.mCall = this.cJava.getMethod("call", String.class, Object[].class);
            }

            return this.mCall.invoke(this.oJava, var1, var2).toString();
        } catch (Throwable var5) {
            throw new IOException("Can't call " + var1);
        }
    }

    public boolean confirm(String var1) throws IOException {
        return new Boolean(this.callFunction("confirm", new Object[]{var1}));
    }

    public String[][] dataLoad() throws IOException {
        Hashtable var1 = new Hashtable();
        this.dataLoad(var1);
        int var2 = var1.size();
        String[][] var3 = new String[var2][2];
        int var5 = 0;

        for (Enumeration var6 = var1.keys(); var6.hasMoreElements(); ++var5) {
            Object var4 = var6.nextElement();
            var3[var5][0] = (String) var4;
            var3[var5][1] = var1.get(var4).toString();
        }

        return var3;
    }

    public void dataLoad(Hashtable var1) throws IOException {
        String var2 = this.getProperty("document.cookie");
        int var3 = var2.length();

        for (int var4 = 0; var4 < var3; ++var4) {
            char var5;
            StringBuffer var6;
            for (var6 = new StringBuffer(); var4 < var3; ++var4) {
                var5 = var2.charAt(var4);
                if (var5 == '=') {
                    ++var4;
                    break;
                }

                var6.append(var5);
            }

            String var7 = this.unEscape(var6.toString());

            for (var6 = new StringBuffer(); var4 < var3; ++var4) {
                var5 = var2.charAt(var4);
                if (var5 == ';') {
                    ++var4;
                    break;
                }

                var6.append(var5);
            }

            String var8 = this.unEscape(var6.toString());
            if (!var7.equals("expires")) {
                var1.put(var7, var8);
            }
        }

    }

    public void dataSave(String[][] var1, int var2) throws IOException {
        try {
            Hashtable var3 = new Hashtable();

            for (int var4 = 0; var4 < var1.length; ++var4) {
                var3.put(var1[var4][0], var1[var4][1]);
            }

            this.dataSave(var3, var2);
        } catch (RuntimeException var5) {
            throw new RuntimeException("Format error String[*][2]");
        }
    }

    public void dataSave(Hashtable var1, int var2) throws IOException {
        if (var1.size() > 0) {
            StringBuffer var3 = new StringBuffer();
            Enumeration var5 = var1.keys();

            while (var5.hasMoreElements()) {
                String var4 = (String) var5.nextElement();
                var3.append(this.escape(var4));
                var3.append('=');
                var3.append(this.escape((String) var1.get(var4)));
                var3.append("; ");
            }

            if (this.dateFormat == null) {
                this.dateFormat = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' GMT'", Locale.ENGLISH);
            }

            var3.append("expires=" + this.dateFormat.format(new Date(System.currentTimeMillis() + (long) (86400000 * var2))) + "; ");
            this.setProperty("document.cookie", var3.toString());
        }
    }

    public String escape(String var1) {
        try {
            return this.callFunction("escape", new Object[]{var1});
        } catch (IOException var2) {
            return "";
        }
    }

    private final Object getArray(Object var1, Object[] var2) throws IOException {
        if (this.mGetSlot == null) {
            try {
                this.mGetSlot = this.cJava.getMethod("getSlot", Integer.TYPE);
            } catch (NoSuchMethodException var9) {
                throw new IOException("No support JavaScript");
            }
        }

        Object[] var3 = new Object[1];
        String var4 = (String) var2[0];
        int var5 = var4.indexOf(91);
        String var6 = var4.substring(0, var5);
        var3[0] = var6;
        var1 = this.getMember(var1, var3);

        try {
            var3[0] = Integer.decode(var4.substring(var5 + 1, var4.indexOf(93)));
            return this.mGetSlot.invoke(var1, var3);
        } catch (IllegalAccessException var7) {
            ;
        } catch (InvocationTargetException var8) {
            ;
        }

        throw new IOException("No support JavaScript");
    }

    private final Object getEndObject(String var1, StringBuffer var2) throws IOException {
        Object var3;
        int var5;
        if (this.isCash) {
            var5 = var1.lastIndexOf(46);
            String var4 = var5 < 0 ? var1 : var1.substring(0, var5);
            var3 = this.tables.get(var4);
            if (var3 != null) {
                return var3;
            }
        }

        var3 = this.oJava;
        int var8 = 0;
        boolean var9 = false;
        var1.length();

        for (Object[] var7 = new Object[1]; (var5 = var1.indexOf(46, var8)) >= 0; var8 = var5 + 1) {
            String var6 = var1.substring(var8, var5);
            var7[0] = var6;
            var3 = this.getMember(var3, var7);
        }

        var2.append(var1.substring(var8));
        return var3;
    }

    private final Object getMember(Object var1, Object[] var2) throws IOException {
        String var3 = (String) var2[0];
        if (this.mGetMember == null) {
            try {
                this.mGetMember = this.cJava.getMethod("getMember", String.class);
            } catch (NoSuchMethodException var6) {
                throw new IOException("No support JavaScript");
            }
        }

        try {
            return var3.indexOf(91) >= 0 ? this.getArray(var1, var2) : this.mGetMember.invoke(var1, var2);
        } catch (Throwable var4) {
            throw new IOException("can't get " + var2[0]);
        }
    }

    public String getProperty(String var1) throws IOException {
        StringBuffer var2 = new StringBuffer();
        Object var3 = this.getEndObject(var1, var2);
        return this.getMember(var3, new Object[]{var2.toString()}).toString();
    }

    public void openWindow(String var1, String var2, Rectangle var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8) throws IOException {
        StringBuffer var9 = new StringBuffer();
        if (var1 == null) {
            var1 = "";
        }

        if (var2 == null || var2.length() == 0) {
            var2 = "sub";
        }

        if (!var8 && var3 != null) {
            var9.append("left=" + var3.x + ",top=" + var3.y + ",width=" + var3.width + ",height=" + var3.height + ",");
        }

        int var10 = var4 ? 1 : 0;
        var9.append("toolbar=" + var10 + ",location" + var10 + ",directories=" + var10 + ",status=" + var10 + ",menubar=" + var10 + ',');
        var10 = var5 ? 1 : 0;
        var9.append("titlebar=" + var10 + ',');
        var10 = var6 ? 1 : 0;
        var9.append("scrollbars=" + var10 + ',');
        var10 = var7 ? 1 : 0;
        var9.append("resizable=" + var10 + ',');
        var10 = var8 ? 1 : 0;
        var9.append("fullscreen=" + var10);
        this.runScript(var2 + "=window.open('" + var1 + "','" + var2 + "','" + var9.toString() + "');");
    }

    public final String runScript(String var1) throws IOException {
        if (this.mEval == null) {
            try {
                this.mEval = this.cJava.getMethod("eval", String.class);
            } catch (NoSuchMethodException var5) {
                throw new IOException("No support JavaScript");
            }
        }

        try {
            Object var2 = this.mEval.invoke(this.oJava, var1);
            return var2 == null ? "" : var2.toString();
        } catch (Throwable var3) {
            throw new IOException("ScriptError");
        }
    }

    protected String scriptString(String var1) {
        StringBuffer var2 = new StringBuffer();
        int var3 = var1.length();

        for (int var5 = 0; var5 < var3; ++var5) {
            char var4 = var1.charAt(var5);
            switch (var4) {
                case '\n':
                    var2.append('\\');
                    var4 = 'n';
                    break;
                case '\r':
                    var2.append('\\');
                    var4 = 'r';
                    break;
                case '"':
                    var2.append('\\');
                    break;
                case '\'':
                    var2.append('\\');
                    break;
                case '\\':
                    var2.append('\\');
            }

            var2.append(var4);
        }

        return var2.toString();
    }

    public synchronized void setCash(boolean var1) {
        this.isCash = var1;
        if (var1) {
            this.tables = new Hashtable();
        } else {
            this.tables = null;
        }

    }

    public void setProperty(String var1, Object var2) throws IOException {
        if (this.mSetMember == null) {
            try {
                this.mSetMember = this.cJava.getMethod("setMember", String.class, Object.class);
                this.mSetSlot = this.cJava.getMethod("setSlot", Integer.TYPE, Object.class);
            } catch (NoSuchMethodException var10) {
                throw new IOException("Not found Java script");
            }
        }

        StringBuffer var3 = new StringBuffer();
        Object var4 = this.getEndObject(var1, var3);
        String var5 = var3.toString();
        var3 = null;

        try {
            if (var5.indexOf(91) >= 0) {
                this.mSetSlot.invoke(var4, var5, var2);
            } else {
                this.mSetMember.invoke(var4, var5, var2);
            }

        } catch (Throwable var6) {
            throw new IOException("can't set " + var5);
        }
    }

    public final synchronized void setup() throws IOException {
        try {
            if (this.cJava == null) {
                this.cJava = Class.forName("netscape.javascript.JSObject");
                this.mGet = this.cJava.getMethod("getWindow", Applet.class);
            }

            this.oJava = this.mGet.invoke((Object) null, this.applet);
        } catch (Throwable var2) {
            this.oJava = null;
            throw new IOException("No support JavaScript");
        }
    }

    public void showDocument(String var1, String var2, String var3) throws IOException {
        if (var1 == null || var1.equals("_self") || var1.equals("_parent")) {
            var1 = "";
        }

        if (!var1.endsWith("document")) {
            if (var1.length() > 0) {
                var1 = var1.concat(".");
            }

            var1 = var1.concat("document");
        }

        if (var2 == null || var2.length() == 0) {
            var2 = "text/plain";
        }

        this.runScript("var doc=" + var1 + ".open('" + var2 + "');\ndoc.write('" + this.scriptString(var3) + "');\ndoc.close();");
    }

    public String unEscape(String var1) {
        try {
            return this.callFunction("unescape", new Object[]{var1});
        } catch (IOException var2) {
            return "";
        }
    }
}
