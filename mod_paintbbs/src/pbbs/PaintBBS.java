package pbbs;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import jaba.applet.*;
import paintchat.*;
import syi.jpeg.*;
import syi.png.*;
import syi.util.*;

import static res.ResPaintBBS.*;

public class PaintBBS extends Applet implements Runnable {
    private Mi mI;
    private Pp pP;
    private Tools tools;
    protected static int[][] c_ioff = null;
    protected static ByteStream c_ani = null;
    private static final String EMP = "";
    protected Thread t_ani = null;
    protected volatile Vector v_ani = null;
    private Image imAni;
    public volatile int speed;
    private boolean isJs = true;
    private Method o_call;
    public String remoteWidth = "";
    public String remoteHeight = "";
    public String str_header = "";
    private String sEnc = null;
    private String sJump = null;
    private OutputStream O;
    private boolean isF = false;
    protected boolean isW;
    protected Window fwindow;
    private volatile boolean isStart = false;
    static Class class$array1$$java$lang$Object;
    static Class class$java$applet$Applet;
    static Class class$java$lang$String;

    private void ani(ByteStream var1, ByteStream var2, byte[] var3) throws IOException {
        if (this.pP.b_ani != null) {
            int var4 = this.pP.maxAni / 2;
            byte[] var5 = this.pP.b_ani.getBuffer();
            int var6 = this.pP.b_ani.size();
            CRC32 var7 = new CRC32();
            Deflater var10 = new Deflater(9, true);

            for (int var11 = 0; var11 < 2; ++var11) {
                var2.reset();
                var7.reset();
                var7.update(var5, 0, var6);
                var2.write(31);
                var2.write(139);
                var2.write(8);

                int var12;
                for (var12 = 0; var12 < 7; ++var12) {
                    var2.write(0);
                }

                var10.reset();
                var10.setInput(var5, 0, var6);

                int var8;
                while (!var10.needsInput()) {
                    var8 = var10.deflate(var3, 0, var3.length);
                    var2.write(var3, 0, var8);
                }

                var10.finish();

                while (!var10.finished()) {
                    var8 = var10.deflate(var3, 0, var3.length);
                    var2.write(var3, 0, var8);
                }

                var8 = (int) var7.getValue();

                for (var12 = 0; var12 < 2; ++var12) {
                    for (int var13 = 0; var13 <= 24; var13 += 8) {
                        var2.write(var8 >>> var13 & 255);
                    }

                    var8 = var10.getTotalIn();
                }

                int var9 = var2.size();
                if (var4 == 0 || var9 <= var4) {
                    break;
                }

                var6 -= (int) ((float) (var9 - var4) * ((float) var6 / (float) var9));
            }

            var10.end();
            var2.writeTo(var1);
        }
    }

    protected String call(String var1) {
        try {
            if (!this.isJs) {
                return null;
            }

            Class var2 = Class.forName("netscape.javascript.JSObject");
            Class[] var10002 = new Class[1];
            Class var10005 = class$java$applet$Applet;
            if (var10005 == null) {
                try {
                    var10005 = Class.forName("java.applet.Applet");
                } catch (ClassNotFoundException var8) {
                    throw new NoClassDefFoundError(var8.getMessage());
                }

                class$java$applet$Applet = var10005;
            }

            var10002[0] = var10005;
            Method var3 = var2.getMethod("getWindow", var10002);
            Object var4 = var3.invoke((Object) null, this);
            if (this.o_call == null) {
                Class[] var10003 = new Class[2];
                Class var10006 = class$java$lang$String;
                if (var10006 == null) {
                    try {
                        var10006 = Class.forName("java.lang.String");
                    } catch (ClassNotFoundException var7) {
                        throw new NoClassDefFoundError(var7.getMessage());
                    }

                    class$java$lang$String = var10006;
                }

                var10003[0] = var10006;
                var10006 = class$array1$$java$lang$Object;
                if (var10006 == null) {
                    try {
                        var10006 = Class.forName("[Ljava.lang.Object;");
                    } catch (ClassNotFoundException var6) {
                        throw new NoClassDefFoundError(var6.getMessage());
                    }

                    class$array1$$java$lang$Object = var10006;
                }

                var10003[1] = var10006;
                this.o_call = var2.getMethod("call", var10003);
            }

            String var5 = this.o_call.invoke(var4, "paintBBSCallback", new Object[]{var1}).toString();
            if (var5 != null) {
                return var5.toLowerCase().startsWith("undef") ? null : var5;
            }
        } catch (Throwable ex) {
            this.isJs = false;
        }

        return null;
    }

    public void destroy() {
        try {
            if (this.mI == null) {
                return;
            }

            this.pP.putUMg();
            if (this.t_ani != null) {
                Thread var1 = this.t_ani;
                this.t_ani = null;
                var1.interrupt();
                var1.join();
            }

            if (this.fwindow != null) {
                this.fwindow.setVisible(false);
                this.fwindow.dispose();
                this.fwindow = null;
            }

            this.mI = null;
            this.tools = null;
            this.pP = null;
        } catch (Throwable ex) {
        }

    }

    private byte[] getBytes(String var1) {
        String var2 = "UTF8";
        String var3 = "send_";
        if (var1 == null) {
            var1 = var2;
        }

        String var4 = this.sEnc != null ? this.sEnc : this.p(var3 + "language", var2);
        int var5 = 0;

        while (var5 < 3) {
            try {
                this.sEnc = var4;
                return var1.getBytes(var4);
            } catch (UnsupportedEncodingException var6) {
                var4 = var5 == 0 ? var4.toUpperCase() : "UTF8";
                ++var5;
            }
        }

        return var1.getBytes();
    }

    public String getColors() {
        return Tools.getC();
    }

    private byte[] getHeader(boolean var1) {
        String var2 = "header";
        String var3 = "send_";
        String var4 = "uencode";
        String var5 = "image_type";
        String var6 = null;
        StringBuffer var7 = new StringBuffer();
        String var8 = var3 + var2 + '_';
        if (this.p(var8 + var5, false)) {
            var7.append(var5 + '=' + (var1 ? "jpeg" : "png") + '&');
        }

        if (this.p(var8 + "timer", false)) {
            var7.append("timer=" + (System.currentTimeMillis() - Pp.count_timer) + "&");
        }

        if (this.p(var8 + "count", false)) {
            var7.append("count=" + Pp.count_click + "&");
        }

        var6 = this.call(var2);
        if (var6 == null || var6.length() <= 0) {
            var6 = this.str_header != null && this.str_header.length() > 0 ? this.str_header : this.p(var3 + var2, (String) null);
        }

        if (var6 != null) {
            if (this.p(var3 + var4, false)) {
                var6 = URLEncoder.encode(var6);
            }

            var7.append(var6);
        }

        return this.getBytes(var7.toString());
    }

    private int i(String var1) {
        int var2 = var1.length();
        if (var2 <= 0) {
            return 0;
        } else {
            byte var3 = 0;
            if (var1.charAt(0) == '0') {
                var3 = 2;
            } else if (var1.charAt(0) == '#') {
                var3 = 1;
            }

            if (var3 == 0) {
                return Integer.parseInt(var1);
            } else {
                int var4 = 0;
                var2 -= var3;

                for (int var5 = 0; var5 < var2; ++var5) {
                    var4 |= Character.digit(var1.charAt(var5 + var3), 16) << (var2 - 1 - var5) * 4;
                }

                return var4;
            }
        }
    }

    private void l(OutputStream var1, int var2) throws IOException {
        String var3 = String.valueOf(var2);
        int var4 = var3.length();

        int var5;
        for (var5 = 0; var5 < 8 - var4; ++var5) {
            var1.write(48);
        }

        for (var5 = 0; var5 < var4; ++var5) {
            var1.write(var3.charAt(var5));
        }

    }

    protected String p(String var1) {
        return this.p(var1, "");
    }

    protected int p(String var1, int var2) {
        try {
            String var3 = this.getParameter(var1);
            if (var3 != null && var3.length() > 0) {
                return var3.charAt(var3.length() - 1) == '%' ? (int) ((double) var2 / 100.0D * (double) this.i(var3.substring(0, var3.length() - 1))) : this.i(var3);
            } else {
                return var2;
            }
        } catch (Throwable ex) {
            return var2;
        }
    }

    protected String p(String var1, String var2) {
        try {
            String var3 = this.getParameter(var1);
            return var3 != null && var3.length() > 0 ? var3 : var2;
        } catch (Throwable ex) {
            return var2;
        }
    }

    protected boolean p(String var1, boolean var2) {
        try {
            String var3 = this.getParameter(var1);
            if (var3 != null && var3.length() > 0) {
                char var4 = Character.toLowerCase(var3.charAt(0));
                return var4 == 't' || var4 == 'y' || var4 == '1';
            } else {
                return var2;
            }
        } catch (Throwable ex) {
            return var2;
        }
    }

    public void paint(Graphics var1) {
        try {
            if (this.imAni != null) {
                var1.drawImage(this.imAni, 0, 0, Color.white, null);
            } else {
                var1.drawString(Pp.STR_VER, 10, var1.getFontMetrics().getHeight() * 2);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void pExit() {
        this.r(this, 's', 1);
    }

    public synchronized void popup(boolean var1) {
        try {
            Frame var3 = null;
            if (this.isW && this.isF) {
                if (this.tools.messageEx(langPBBS.get("closeWindow"))) {
                    this.fwindow.dispose();
                }

                return;
            }

            if (var1 && !this.tools.messageEx(this.isF ? langPBBS.get("toPage") : langPBBS.get("toFloat"))) {
                return;
            }

            boolean var4 = false;
            if (!this.isF) {
                String var5 = "popup_parent";
                var3 = this.tools.getPFrame();
                var4 = this.getParameter(var5) != null ? this.p(var5, false) : this.tools.messageEx(langPBBS.get("isParentWind"));
            }

            Container var10 = this.pP.getParent();
            if (var10 != null) {
                var10.remove(this.pP);
            }

            if (this.fwindow != null) {
                this.fwindow.dispose();
            }

            this.pP.setVisible(false);
            this.pP.isPack = true;
            String var7 = "Center";
            if (!this.isF) {
                Object var8 = var4 ? new Frame(Pp.STR_VER) : new Dialog(var3, Pp.STR_VER, false);
                ((Container) var8).setLayout(new BorderLayout());
                ((Component) var8).setLocation(0, 0);
                ((Component) var8).setSize(this.getToolkit().getScreenSize());
                ((Window) var8).addWindowListener(this.mI);
                ((Container) var8).add(this.pP, var7);
                this.fwindow = (Window) var8;
                ((Window) var8).show();
            } else {
                this.add(this.pP, var7);
                Dimension var6 = this.getSize();
                this.pP.setBounds(0, 0, var6.width, var6.height);
            }

            this.isF = !this.isF;
            this.pP.setVisible(true);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void post(URL var1, ByteStream var2, ByteStream var3, Label var4, boolean var5) throws IOException, InterruptedException {
        String var6 = "Content-type";
        String var7 = "application/octet-stream";
        var3.reset();
        this.getBytes((String) null);
        String var9 = this.sEnc;
        char var10 = '2';
        Socket var12 = null;
        BufferedReader var13 = null;
        int var14 = var2.size();
        byte[] var15 = var2.getBuffer();
        String var16 = "KB/ All data size=" + var14 / 1024 + "KB";
        OutputStream var11;
        String var18;
        int var19;
        if (var5) {
            int var17 = var1.getPort();
            var12 = new Socket(var1.getHost(), var17 > 0 ? var17 : 80);
            var11 = var12.getOutputStream();
            this.O = var3;
            var18 = "\r\n";
            this.wS("POST ");
            this.wS(var1.getFile());
            this.wS(" HTTP/1.0\r\nConnection: close\r\nUser-Agent: PaintBBS/2.x (");
            this.wS(System.getProperty("os.name"));
            this.w(59);
            this.wS(System.getProperty("os.version"));
            this.wS(")\r\nReferer: ");
            this.wS(this.getDocumentBase().toExternalForm());
            this.wS("\r\nHost: ");
            this.wS(var1.getHost());
            this.wS("\r\nAccept-Language: ");
            this.wS(Locale.getDefault().getLanguage());
            this.wS("\r\nContent-Length: ");
            this.wS(String.valueOf(var14));
            this.wS(var18);
            this.wS(var6);
            this.w(58);
            this.w(32);
            this.wS(var7);
            this.wS(var18);
            this.wS(var18);
            var3.writeTo(var11);
            var11.flush();
            var19 = 0;

            while (var19 < var14) {
                int var20 = Math.min(var14 - var19, 5000);
                var11.write(var15, var19, var20);
                var19 += var20;
                var4.setText(var19 / 1024 + var16);
            }

            var11.flush();

            try {
                var13 = new BufferedReader(new InputStreamReader(var12.getInputStream(), var9));
                String var8 = var13.readLine();
                if (var8 != null) {
                    var8 = var8.substring(Math.max(var8.indexOf(32), 0)).trim() + '2';
                    var10 = var8.charAt(0);
                }

                while ((var8 = var13.readLine()) != null && var8.length() > 0) {
                }
            } catch (EOFException var23) {
            }
        } else {
            var4.setText(langPBBS.get("apiWait"));
            URLConnection var24 = var1.openConnection();
            var24.setDoOutput(true);
            var24.setDoInput(true);
            var24.setUseCaches(false);
            var24.setRequestProperty(var6, var7);
            var11 = var24.getOutputStream();
            var11.write(var15, 0, var14);
            var11.flush();
            var11.close();
            var13 = new BufferedReader(new InputStreamReader(var24.getInputStream(), var9));
        }

        StringBuffer var25 = new StringBuffer();
        var18 = null;

        try {
            while ((var19 = var13.read()) != -1) {
                if (var19 != 13 && (var25.length() != 0 || !Character.isWhitespace((char) var19))) {
                    var25.append((char) var19);
                    if (var19 == 10 && var18 == null) {
                        var18 = var25.toString();
                        var25 = new StringBuffer();
                    }
                }
            }

            if (var18 == null) {
                var18 = var25.toString();
                var25.setLength(0);
            }
        } catch (EOFException var22) {
        }

        try {
            var13.close();
            if (var12 != null) {
                var12.close();
            }
        } catch (Throwable ex) {
        }

        if (var10 != '2' && var10 != '3') {
            var25.insert(0, var18 + '\n');
        } else {
            if (var18 == null || var18.length() <= 0) {
                return;
            }

            if (var18.indexOf("error") < 0) {
                var19 = var18.indexOf("URL:");
                this.sJump = var19 >= 0 ? var18.substring(var19 + 4, var18.length()) : null;
                return;
            }
        }

        this.sJump = null;
        throw new InterruptedException(var25.toString());
    }

    private Thread r(Runnable var1, char var2, int var3) {
        Thread var4 = new Thread(var1, String.valueOf(var2));
        var4.setPriority(var3);
        var4.start();
        return var4;
    }

    private void rAni() throws Throwable {
        DataInputStream var1 = null;

        MgLine var2;
        try {
            var1 = new DataInputStream(new GZIPInputStream((new URL(this.getCodeBase(), this.p("pch_file", this.p("image_canvas")))).openStream(), 4000));

            while (this.t_ani != null) {
                var2 = new MgLine();
                var2.setData(var1);
                if (var2.head != 101) {
                    this.v_ani.addElement(var2);
                }
            }
        } catch (Throwable ex) {
        }

        var2 = new MgLine();
        var2.setData(101);
        this.v_ani.addElement(var2);
        if (var1 != null) {
            var1.close();
        }

    }

    private void rAniD() {
        MgLine var1 = new MgLine();
        Graphics var2 = null;
        Graphics var3 = null;
        this.speed = this.p("speed", -1);

        try {
            MgLine.setup(this, 1);
            Dimension var4 = this.getSize();
            int var5 = var4.width * var4.height;
            var2 = this.getGraphics();
            this.imAni = this.createImage(var4.width, var4.height + 1);
            var3 = this.imAni.getGraphics();
            var3.setColor(Color.white);
            var3.fillRect(0, 0, var4.width, var4.height);
            var2.setColor(Color.white);
            var2.fillRect(0, 0, var4.width, var4.height);
            int[][] var6 = new int[2][var5 + var4.width];

            for (int var7 = 0; var7 < 2; ++var7) {
                for (int var8 = 0; var8 < var5; ++var8) {
                    var6[var7][var8] = 0xFFFFFF;
                }
            }

            while (this.t_ani != null) {
                while (!this.v_ani.isEmpty()) {
                    var1 = (MgLine) this.v_ani.firstElement();
                    this.v_ani.removeElementAt(0);
                    if (var1.head == 101) {
                        break;
                    }

                    var1.draw(this, var2, var3, var6, var4.width, var4.height, 0, 0, 1, 255, 255, this.speed);
                    if (this.speed > 0) {
                        Thread.sleep((long) this.speed);
                    }
                }

                if (var1.head == 101) {
                    break;
                }

                Thread.sleep(3000L);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        this.showStatus("Done.");
        if (var2 != null) {
            var2.dispose();
        }

        if (var3 != null) {
            var3.dispose();
        }

    }

    private void rInit() {
        this.setLayout(new BorderLayout());
        String var1 = "ja";
        this.pP = new Pp();
        this.mI = new Mi();
        this.tools = new Tools(this, this.mI, Pp.STR_VER, 0xFFFFFF, false);
        this.pP.setVisible(false);
        this.add(this.pP, "Center");
        this.pP.setLocation(0, 0);
        this.pP.init(this, this.tools, this.mI, this.v_ani);
        c_ioff = this.mI.i_offs;
        if (this.isW = this.p("popup", false)) {
            this.popup(false);
        }

        if (!this.pP.isVisible()) {
            this.pP.setVisible(true);
        }

        this.call("start");
    }

    private void rSave() {
        try {
            String var1 = "url_";
            this.pP.isSend = true;
            boolean var2 = false;
            this.pP.setEnabled(false);
            String var3 = this.call("check");
            if (var3 != null && var3.length() > 0) {
                char var4 = Character.toLowerCase(var3.charAt(0));
                var2 = var4 == 't' || var4 == 'y' || var4 == '1';
            }

            var3 = null;
            if ((var2 || this.tools.messageEx(langPBBS.get("isDrawDone"))) && this.saveImage()) {
                c_ioff = null;
                c_ani = null;
                Pp.count_click = 0;
                Pp.count_timer = 0L;
                this.pP.setVisible(false);
                this.getAppletContext().showDocument(new URL(this.getCodeBase(), this.sJump != null ? this.sJump : this.p(var1 + "exit", "finish.cgi")), this.p(var1 + "target", "_self"));
                return;
            }
        } catch (Throwable ex) {
            this.tools.message(ex.getMessage() + langPBBS.get("doRetry"));
        }

        this.pP.copy(this.mI.i_offs, this.pP.uimage[0]);
        this.pP.isSend = false;
        this.pP.setEnabled(true);
    }

    public void run() {
        try {
            switch (Thread.currentThread().getName().charAt(0)) {
                case 'a':
                    this.rAni();
                    break;
                case 'd':
                    this.rAniD();
                    break;
                case 'i':
                    this.rInit();
                    break;
                case 's':
                    this.rSave();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private boolean saveImage() throws Throwable {
        String var1 = "security_";
        String var2 = "thumbnail_";
        String var3 = "send_";
        String var4 = "compress_";
        String var5 = "save";
        String var6 = "poo";
        String var7 = "advance";
        String var8 = "level";
        String var9 = "jpeg";
        String var10 = "url_";
        String var11 = "image_";
        String var12 = "width";
        String var13 = "height";
        int var14 = this.pP.image_x;
        int var15 = this.pP.image_y;
        this.pP.putUMg();
        ByteStream var17 = new ByteStream(100000);
        ByteStream var18 = new ByteStream(100000);
        ByteStream var19 = new ByteStream(100000);
        Deflater var20 = new Deflater(9, false);
        SJpegEncoder var21 = new SJpegEncoder();
        SPngEncoder var22 = new SPngEncoder(var18, var19, var20);
        boolean var23 = true;
        boolean var24 = false;
        Dialog var25 = null;

        try {
            URL var26 = new URL(this.getCodeBase(), this.p(var10 + var5, "getpic.cgi"));
            boolean var27 = this.p(var6, true);
            boolean var28 = this.p(var3 + var7, true);
            boolean var29 = this.p(var11 + var9, false);
            int var30 = this.p(var4 + var8, 10);
            int var31 = this.p(var2 + var4 + var8, 20);
            int var32 = this.p(var11 + "size", 0);
            int var33 = Math.min(this.p(var2 + var12, var14), var14);
            int var34 = Math.min(this.p(var2 + var13, var15), var15);
            boolean var10000;
            if (var33 == var14 && var34 == var15 && this.pP.b_ani == null) {
                var10000 = false;
            } else {
                var10000 = true;
            }

            var22.setInterlace(this.p(var11 + "interlace", false));
            long var35 = (long) (this.p(var1 + "timer", 0) * 1000);
            if (var35 > 0L && System.currentTimeMillis() - Pp.count_timer <= var35) {
                var23 = false;
            }

            int var37 = this.p(var1 + "click", 0);
            if (var37 > 0 && Pp.count_click <= var37) {
                var23 = false;
            }

            if (!var23) {
                URL var38 = new URL(this.getCodeBase(), this.p(var1 + "url", var26.toString()));
                if (!this.p(var1 + "post", false)) {
                    this.getAppletContext().showDocument(var38);
                    return false;
                }

                var26 = var38;
            }

            var25 = this.tools.message(langPBBS.get("progress"), Pp.STR_VER, 0);
            Label var47 = new Label(langPBBS.get("pngEncode"), 1);
            var25.add(var47, "South");
            var25.pack();
            Tools.moveC(var25);
            var25.show();
            int[] var39 = this.pP.uimage[0][0];
            int[][] var40 = this.mI.i_offs;
            MgLine var41 = new MgLine();
            var41.setVisit(this.tools.visit0, this.tools.visit1);
            var41.makeLPic(var39, var40, var14, var15, 0, 0, var14, var15);
            var41 = null;
            boolean var42 = false;
            if (var32 != 1) {
                var22.fencode(var17, var39, var14, var15);
            }

            int var49 = var17.size();
            if (var49 == 0 || var32 > 0 && var49 >= var32 * 1024) {
                var17.reset();
                if (var29) {
                    var47.setText("JPEG Encoding");
                    var24 = true;
                    var21.encode(var17, var39, var14, var15, var30);
                    if (var32 > 1 && var17.size() >= (int) ((double) var32 * 1.5D) * 1024) {
                        var17.reset();
                        var21.encode(var17, var39, var14, var15, (int) ((float) var30 * 1.25F));
                    }
                } else {
                    var47.setText("PNG Compress");
                    var22.fencode(var17, this.pP.getThumbnail(var39, var14, var15, true, var30 / 4), var14, var15);
                }

                if (var49 == 0 || var17.size() < var49) {
                    var24 = var29;
                }
            }

            var18.reset();
            var17.writeTo(var18);
            var32 = var18.size();
            var17.reset();
            this.O = var17;
            if (!var27) {
                byte[] var43 = this.getHeader(var24);
                this.wS(this.p("header_magic", "P"));
                this.l(var17, var43.length);
                var17.write(var43);
                this.l(var17, var32);
            } else {
                for (var37 = 0; var37 < 4; ++var37) {
                    var17.write(0);
                }
            }

            this.w(13);
            this.w(10);
            var18.writeTo(var17);
            if (var33 != var14 || var34 != var15) {
                var39 = this.pP.getThumbnail(var39, var33, var34, false, 0);
            }

            for (var37 = 0; var37 < 2; ++var37) {
                String var16 = this.p(var2 + "type" + (var37 > 0 ? "2" : ""));
                if (var37 == 0 && (var33 != var14 || var34 != var15) || var16.length() > 0) {
                    char var50 = var16.length() > 0 ? Character.toLowerCase(var16.charAt(0)) : 112;
                    var49 = var17.size();
                    this.l(var17, 0);
                    switch (var50) {
                        case 'a':
                            this.ani(var17, var19, var18.getBuffer());
                            break;
                        case 'j':
                            var21.encode(var17, var39, var33, var34, var31);
                            break;
                        default:
                            var22.encode(var17, var39, var33, var34, 0);
                    }

                    var19.reset();
                    this.l(var19, var17.size() - var49 - 8);
                    System.arraycopy(var19.getBuffer(), 0, var17.getBuffer(), var49, 8);
                }
            }

            var20.end();
            Object var48 = null;
            var21 = null;
            var22 = null;
            if (var28) {
                try {
                    this.post(var26, var17, var18, var47, true);
                } catch (IOException var44) {
                    var28 = false;
                }
            }

            if (!var28) {
                this.post(var26, var17, var18, var47, false);
            }
        } catch (InterruptedException var45) {
            if (var25 != null) {
                var25.dispose();
            }

            this.tools.message(var45.getMessage());
            return false;
        } catch (Throwable ex) {
            if (var25 != null) {
                var25.dispose();
            }

            throw ex;
        }

        var25.dispose();
        return true;
    }

    public void setColors(String var1) {
        if (this.tools != null) {
            this.tools.setC(var1);
        }

    }

    public void setSpeed(String var1) {
        this.speed = Integer.parseInt(var1);
    }

    public void start() {
        String var1 = "";

        try {
            if (this.isStart) {
                if (this.remoteWidth.length() > 0 && this.remoteHeight.length() > 0) {
                    Dimension var2 = new Dimension(this.i(this.remoteWidth), this.i(this.remoteHeight));
                    if (var2.width > 0 && var2.height > 0) {
                        this.setSize(var2);
                    }

                    this.remoteWidth = this.remoteHeight = var1;
                }
            } else {
                this.isStart = true;
                String var4 = this.p("pch_file", var1);
                if (var4.length() <= 0) {
                    var4 = this.p("image_canvas", var1);
                    if (!var4.toLowerCase().endsWith(".pch")) {
                        var4 = var1;
                    }
                }

                if (var4.length() > 0) {
                    this.v_ani = new Vector();
                    this.t_ani = this.r(this, 'a', 1);
                }

                if (this.p("viewer", false)) {
                    this.t_ani = this.r(this, 'd', 1);
                } else {
                    this.r(this, 'i', 2);
                }
            }
        } catch (Throwable ex) {
        }

    }

    private void w(int var1) throws IOException {
        this.O.write(var1);
    }

    private void wS(String var1) throws IOException {
        for (int var3 = 0; var3 < var1.length(); ++var3) {
            int var2 = var1.charAt(var3) & 255;
            if (var2 > 255) {
                this.O.write(var2 >>> 8);
            }

            this.O.write(var2 & 255);
        }

    }
}
