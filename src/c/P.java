package c;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import paintchat.LO;
import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;
import paintchat_client.IMi;
import paintchat_client.Me;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;
import syi.jpeg.SJpegEncoder;
import syi.png.SPngEncoder;
import syi.util.ByteStream;

public class P extends Panel implements IMi {
    private ShiPainter app;
    private Res res;
    private Res config;
    protected ByteStream work = new ByteStream();
    protected Mi mi;
    protected ToolBox tool;
    private Ts ts;
    private int iScrollType = 0;
    private Dimension dSize;
    private Dimension dPack = new Dimension();
    public Dimension dMax = new Dimension();
    private boolean isLeft;
    private Color clB;
    private Image imBk = null;
    private M m;
    private M.User user;
    private static LO[] sLO;
    private static LO[][] undoLO;
    private static ByteStream[] undoMgs;
    public static ByteStream ani = new ByteStream();
    private static int aniCount;
    private static int maxAni;
    private static int maxGp;
    private static M undoMg;
    private static int nowI;
    private static int lastI;
    private static long sTime;
    private static int sCount;
    private static int LF;
    private OutputStream O;

    public P(ShiPainter var1) {
        super((LayoutManager) null);
        this.app = var1;
    }

    private void ani(OutputStream var1, Deflater var2, ByteStream var3, ByteStream var4, int var5) throws Throwable {
        this.O = var1;
        M.Info var6 = this.mi.info;
        String var7 = "layer_";
        String var8 = var7 + "count";
        this.wS(var8);
        var1.write(61);
        this.wS(String.valueOf(LF));
        this.wN();
        this.wS("image_width=");
        this.wS(String.valueOf(var6.imW));
        this.wN();
        this.wS("image_height=");
        this.wS(String.valueOf(var6.imH));
        this.wN();
        this.wS("count_lines=");
        this.wS(String.valueOf(aniCount));
        this.wN();
        this.wS("quality=");
        this.wS(String.valueOf(var5));
        this.wN();
        this.wS("version=2\r\n");
        this.wN();
        int var9 = ani.size();
        int var10 = 0;

        int var11;
        for (byte[] var13 = var3.getBuffer(); var10 < var9; var10 += var11) {
            var11 = Math.min(var9 - var10, 65000);
            var2.reset();
            var2.setInput(ani.getBuffer(), var10, var11);
            var2.finish();
            var4.reset();

            while (!var2.finished()) {
                int var12 = var2.deflate(var13, 0, var13.length);
                var4.write(var13, 0, var12);
            }

            this.w2(var4.size());
            var4.writeTo(var1);
        }

    }

    public void changeSize() {
        this.pack();
    }

    void copyLO(LO[] var1, LO[] var2) {
        for (int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3].setLayer(var1[var3]);
        }

    }

    private String getCode() {
        String var1 = "UTF8";
        String var2 = "send_";
        String var3 = this.p(var2 + "language", var1);
        String[] var4 = new String[]{var3, var3.toUpperCase(), var1};
        int var5 = 0;

        while (var5 < var4.length) {
            try {
                var1.getBytes(var4[var5]);
                return var4[var5];
            } catch (UnsupportedEncodingException var6) {
                ++var5;
            }
        }

        return var1;
    }

    private byte[] getHeader(boolean var1) {
        String var2 = "header";
        String var3 = "send_";
        String var4 = "uencode";
        String var5 = "image_type";
        String var6 = "timer";
        byte[] var7 = (byte[]) null;
        String var8 = this.app.str_header;
        StringBuffer var9 = new StringBuffer();
        if (this.p(var3 + var2 + '_' + var5, false)) {
            var9.append(var5 + '=' + (var1 ? "jpeg" : "png") + '&');
        }

        if (this.p(var3 + var2 + '_' + var6, false)) {
            var9.append(var6 + "=" + (System.currentTimeMillis() - sTime) + "&");
        }

        if (this.p(var3 + var2 + "_count", false)) {
            var9.append("count=" + sCount + "&");
        }

        var9.append(var8 != null && var8.length() > 0 ? var8 : this.config.getP(var3 + var2, ""));
        if (this.p(var3 + var4, false)) {
            var9 = new StringBuffer();
        }

        try {
            var7 = var9.toString().getBytes(this.getCode());
        } catch (Throwable var10) {
            ;
        }

        return var7;
    }

    public Dimension getSize() {
        if (this.dSize == null) {
            this.dSize = super.getSize();
        }

        return this.dSize;
    }

    public int[] getThumbnail(int[] var1, int var2, int var3, boolean var4, int var5) throws Throwable {
        int var6 = this.mi.info.imW;
        int var7 = this.mi.info.imH;
        float var8 = (float) var6 / (float) var2;
        float var9 = (float) var7 / (float) var3;
        int var20 = var6 * var7 - 1;
        int var22 = 0;
        int var19;
        if (var6 != var2 || var7 != var3) {
            int[] var23 = new int[var2 * var3];

            for (int var24 = 0; var24 < var3; ++var24) {
                for (int var25 = 0; var25 < var2; ++var25) {
                    float var15 = 0.0F;
                    float var16 = 0.0F;
                    float var17 = 0.0F;
                    float var18 = 0.0F;

                    for (float var11 = 0.0F; var11 < var9; ++var11) {
                        float var14 = var9 - var11;
                        if (var14 > 1.0F) {
                            var14 = 1.0F;
                        }

                        for (float var10 = 0.0F; var10 < var8; ++var10) {
                            float var13 = var8 - var10;
                            if (var13 > 1.0F) {
                                var13 = 1.0F;
                            }

                            var19 = Math.min(((int) ((float) var24 * var9) + (int) var11) * var6 + (int) ((float) var25 * var8) + (int) var10, var20);
                            int var21 = var1[var19];
                            float var12 = var13 * var14;
                            var16 += (float) (var21 >> 16 & 255) * var12;
                            var17 += (float) (var21 >> 8 & 255) * var12;
                            var18 += (float) (var21 & 255) * var12;
                            var15 += var12;
                        }
                    }

                    var16 /= var15;
                    var16 = var16 > 255.0F ? 255.0F : var16;
                    var17 /= var15;
                    var17 = var17 > 255.0F ? 255.0F : var17;
                    var18 /= var15;
                    var18 = var18 > 255.0F ? 255.0F : var18;
                    var23[var22++] = (int) var16 << 16 | (int) var17 << 8 | (int) var18;
                }
            }

            var1 = var23;
        }

        if (var4) {
            var5 = var5 <= 0 ? 1 : var5;
            float var28 = (float) var5;
            float var29 = (float) ((int) ((double) var5 * 1.5D));
            var19 = var2 * var3;
            int[] var30 = new int[3];
            int[] var26 = new int[3];

            for (int var27 = 0; var27 < var19; ++var27) {
                var26[2] = var1[var27] >>> 16 & 255;
                var26[1] = var1[var27] >>> 8 & 255;
                var26[0] = var1[var27] & 255;
                var30[2] = (int) ((float) var26[2] * 0.299F + (float) var26[1] * 0.587F + (float) var26[0] * 0.114F);
                var30[1] = (int) (-((float) var26[2] * 0.1687F) - (float) var26[1] * 0.3313F + (float) var26[0] * 0.5F + 128.0F);
                var30[0] = (int) ((float) var26[2] * 0.5F - (float) var26[1] * 0.4187F - (float) var26[0] * 0.0813F + 128.0F);
                var30[2] = (int) ((float) Math.round((float) var30[2] / var29) * var29);
                var30[1] = (int) ((float) Math.round((float) var30[1] / var28) * var28);
                var30[0] = (int) ((float) Math.round((float) var30[0] / var28) * var28);
                var26[2] = (int) ((float) var30[2] + 1.402F * (float) (var30[0] - 128));
                var26[1] = (int) ((float) var30[2] - 0.34414F * (float) (var30[1] - 128) - 0.71414F * (float) (var30[0] - 128));
                var26[0] = (int) ((float) var30[2] + 1.772F * (float) (var30[1] - 128));
                var26[2] -= var26[2] % 2;
                var26[1] -= var26[1] % 2;
                var26[0] -= var26[0] % 2;
                var26[2] = var26[2] > 255 ? 255 : (var26[2] < 0 ? 0 : var26[2]);
                var26[1] = var26[1] > 255 ? 255 : (var26[1] < 0 ? 0 : var26[1]);
                var26[0] = var26[0] > 255 ? 255 : (var26[0] < 0 ? 0 : var26[0]);
                var1[var27] = var26[2] << 16 | var26[1] << 8 | var26[0];
            }
        }

        return var1;
    }

    public void init(Res var1, Res var2, Ts var3) throws Throwable {
        this.enableEvents(49L);
        this.config = var1;
        this.res = var2;
        String var5 = "cursor_";
        ShiPainter var6 = this.app;
        this.ts = var3;
        this.isLeft = var1.getP("isLeft", false);
        int var7 = var1.getP("image_width", 300);
        int var8 = var1.getP("image_height", 300);
        int var9 = var1.getP("quality", 1);
        int var10 = var1.getP("layer_count", 2);
        this.clB = new Color(var1.getP("color_bk2", 12303359));
        Cursor[] var11 = new Cursor[4];
        int[] var12 = new int[]{0, 13, 0, 0};

        int var4;
        for (var4 = 0; var4 < 4; ++var4) {
            var11[var4] = this.loadCursor(var6.getParameter(var5 + (var4 + 1)), var12[var4]);
        }

        this.mi = new Mi(this, var2);
        this.mi.init(var6, var1, var7, var8, var9, var10, var11);

        try {
            String var13 = var1.getP("tools", "normal");
            this.tool = (ToolBox) Class.forName("paintchat." + var13 + ".Tools").newInstance();
            this.tool.init(this, var6, var1, this.res, this.mi);
        } catch (Throwable var24) {
            var24.printStackTrace();
        }

        this.mi.tBox = this.tool;
        this.add(this.mi);
        M.Info var25 = this.mi.info;
        M.User var14 = this.user = this.mi.user;
        var6.mPermission(var1.getP("permission", "layer_edit:t;fill:t;clean:t;layer:all;"));
        String var15 = var1.getP("mg_init");
        if (var15 != null && var15.length() > 0) {
            var25.m.set(var15);
        }

        this.m = new M(var25, var14);
        int var16;
        if (undoLO != null && Ts.confirm("Restore")) {
            var25.setLayers(sLO);
            var25.W = sLO[0].W;
            var25.H = sLO[0].H;
            var25.setSize(var7, var8, var9);

            for (var4 = 0; var4 < undoLO.length; ++var4) {
                for (var16 = 0; var16 < undoLO[var4].length; ++var16) {
                    undoLO[var4][var16].setSize(var25.W, var25.H);
                }
            }
        } else {
            var14.wait = -2;
            this.reset();
            LF = var10;
            LO.iL = 0;
            this.setLName();
            var16 = var1.getP("undo", 24);
            int var17 = Math.min(var1.getP("undo_in_mg", 12), var16);
            var16 = Math.max(var16 / var17, 2);
            undoLO = new LO[var16][];
            undoMgs = new ByteStream[var16];

            for (var4 = 0; var4 < var16; ++var4) {
                undoMgs[var4] = new ByteStream();
            }

            maxGp = var17;
            maxAni = var1.getP("animation_max", 0) * 1024 * 2;
            String var18 = this.p("pch_file", (String) null);
            String var19 = this.p("image_canvas", (String) null);
            if (var19 != null && var19.toLowerCase().endsWith("pch")) {
                var18 = var19;
                var19 = null;
            }

            sTime = System.currentTimeMillis();
            if (var19 != null) {
                Image var20 = var6.getImage(var6.getCodeBase(), var19);
                Awt.wait(var20);
                int var21 = var20.getWidth((ImageObserver) null);
                int var22 = var20.getHeight((ImageObserver) null);
                if (var9 > 1) {
                    var21 *= var9;
                    var22 *= var9;
                    Image var23 = var20.getScaledInstance(var21, var22, 16);
                    var20.flush();
                    var20 = var23;
                }

                int[] var28 = Awt.getPix(var20);
                var20.flush();
                var25.layers[0].toCopy(var21, var22, var28, 0, 0);
                this.setL(var7, var8, var25.L);
                this.copyLO(var25.layers, undoLO[0]);
            }

            if (var18 != null) {
                this.r(var18);
            }

            this.setL(var7, var8, var25.L);
            var14.wait = 0;
            int var26 = 0;

            while ((var15 = var1.getP("mg_" + var26)) != null && var15.length() > 0) {
                ++var26;
                M var27 = new M(var25, var14);
                var27.set(var15);
                var27.draw();
                this.send(var27);
            }
        }

        this.mi.isEnable = true;
    }

    private void l(int var1) throws Throwable {
        String var2 = String.valueOf(var1);

        for (int var3 = 0; var3 < 8 - var2.length(); ++var3) {
            this.O.write(48);
        }

        this.wS(var2);
    }

    private Cursor loadCursor(String var1, int var2) {
        try {
            if (var1 != null && var1.length() > 0) {
                boolean var3 = var1.equals("none");
                int var4 = 8;
                int var5 = 8;
                int var6 = 16;
                int var7 = 16;
                Image var8 = null;
                Toolkit var9 = Toolkit.getDefaultToolkit();
                if (!var3) {
                    var8 = var9.createImage((byte[]) this.config.getRes(var1));
                    if (var8 == null) {
                        return Cursor.getPredefinedCursor(var2);
                    }

                    Awt.wait(var8);
                    var6 = var8.getWidth((ImageObserver) null);
                    var7 = var8.getHeight((ImageObserver) null);
                    var4 = var1.indexOf(120);
                    var4 = var4 == -1 ? var6 / 2 - 1 : Integer.parseInt(var1.substring(var4 + 1, var1.indexOf(120, var4 + 1)));
                    var5 = var1.indexOf(121);
                    var5 = var5 == -1 ? var7 / 2 - 1 : Integer.parseInt(var1.substring(var5 + 1, var1.indexOf(121, var5 + 1)));
                }

                try {
                    Method var20 = Toolkit.class.getMethod("createCustomCursor", Image.class, Point.class, String.class);
                    Method var11 = Toolkit.class.getMethod("getBestCursorSize", Integer.TYPE, Integer.TYPE);
                    Dimension var12;
                    if (var8 == null) {
                        var12 = new Dimension();
                    } else {
                        var12 = (Dimension) var11.invoke(var9, new Integer(var6), new Integer(var7));
                    }

                    return (Cursor) var20.invoke(var9, var8, new Point((int) ((float) var12.width / (float) var6 * (float) var4), (int) ((float) var12.height / (float) var7 * (float) var5)), var1);
                } catch (Throwable var18) {
                    if (var8 == null) {
                        var8 = this.createImage(new MemoryImageSource(var6, var7, new int[var6 * var7], 0, var6));
                    }

                    Cursor var10 = (Cursor) Class.forName("com.ms.awt.CursorX").getConstructors()[0].newInstance(var8, new Integer(var4), new Integer(var5));
                    if (var10 != null) {
                        return var10;
                    }
                }
            }
        } catch (Throwable var19) {
            var19.printStackTrace();
        }

        return Cursor.getPredefinedCursor(var2);
    }

    public String p(String var1) {
        return this.p(var1, "");
    }

    public int p(String var1, int var2) {
        try {
            String var3 = this.app.getParameter(var1);
            if (var3 != null && var3.length() > 0) {
                if (var3.charAt(var3.length() - 1) == '%') {
                    int var4 = Integer.decode(var3.substring(0, var3.length() - 1));
                    return (int) Math.round((double) var2 / 100.0D * (double) var4);
                } else {
                    return Integer.decode(var3);
                }
            } else {
                return var2;
            }
        } catch (Throwable var5) {
            return var2;
        }
    }

    public String p(String var1, String var2) {
        try {
            String var3 = this.app.getParameter(var1);
            return var3 != null && var3.length() > 0 ? var3 : var2;
        } catch (Throwable var4) {
            return var2;
        }
    }

    public final boolean p(String var1, boolean var2) {
        try {
            String var3 = this.app.getParameter(var1);
            if (var3 != null && var3.length() > 0) {
                char var4 = Character.toLowerCase(var3.charAt(0));
                return var4 == 't' || var4 == 'y' || var4 == '1';
            } else {
                return var2;
            }
        } catch (Throwable var5) {
            return var2;
        }
    }

    public void pack() {
        try {
            if (this.tool == null || this.mi == null) {
                return;
            }

            this.tool.pack();
            this.mi.resetGraphics();
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void paint(Graphics var1) {
        try {
            if (this.app.isStart <= 1) {
                return;
            }

            Dimension var2 = this.getSize();
            int var3;
            if (this.imBk == null) {
                var1.setColor(this.clB);

                for (var3 = 16; var3 < var2.width; var3 += 20) {
                    var1.fillRect(var3, 0, 1, var2.height);
                }

                for (var3 = 16; var3 < var2.height; var3 += 20) {
                    var1.fillRect(0, var3, var2.width, 1);
                }
            } else {
                var3 = this.imBk.getWidth((ImageObserver) null);
                int var4 = this.imBk.getHeight((ImageObserver) null);
                int var5 = var2.width / var3 + (var2.width % var3 > 0 ? 1 : 0);
                int var6 = var2.height / var4 + (var2.height % var4 > 0 ? 1 : 0);

                for (int var7 = 0; var7 < var6; ++var7) {
                    for (int var8 = 0; var8 < var5; ++var8) {
                        var1.drawImage(this.imBk, var8 * var3, var7 * var4, this);
                    }
                }
            }
        } catch (RuntimeException var9) {
            ;
        }

    }

    private String post(URL var1, ByteStream var2, ByteStream var3, Label var4, boolean var5) throws Throwable {
        String var7 = this.getCode();
        char var8 = '2';
        Socket var10 = null;
        BufferedReader var11 = null;
        int var12 = var2.size();
        byte[] var13 = var2.getBuffer();
        String var14 = "KB/ All data size=" + var12 / 1024 + "KB";
        OutputStream var9;
        int var17;
        if (var5) {
            var3.reset();
            this.O = var3;
            this.wS("POST ");
            this.wS(var1.getFile());
            this.wS(" HTTP/1.0\r\nConnection: close\r\nUser-Agent: Shi-Painter/1.x (");
            this.wS(System.getProperty("os.name") + ';' + System.getProperty("os.version"));
            this.wS(")\r\nReferer: ");
            this.wS(this.app.getDocumentBase().toExternalForm());
            this.wS("\r\nHost: ");
            this.wS(var1.getHost());
            this.wS("\r\nAccept-Language: ");
            this.wS(Locale.getDefault().getLanguage());
            this.wS("\r\nContent-Length: ");
            this.wS(String.valueOf(var12));
            this.wN();
            this.wS("Content-type");
            this.wS(": ");
            this.wS("application/octet-stream");
            this.wN();
            this.wN();
            int var15 = var1.getPort();
            var10 = new Socket(var1.getHost(), var15 > 0 ? var15 : 80);
            var9 = var10.getOutputStream();
            var3.writeTo(var9);
            var9.flush();
            int var16 = 0;

            while (var16 < var12) {
                var17 = Math.min(var12 - var16, 5000);
                var9.write(var13, var16, var17);
                var16 += var17;
                var4.setText(var16 / 1024 + var14);
            }

            var9.flush();

            try {
                var11 = new BufferedReader(new InputStreamReader(var10.getInputStream(), var7));
                String var6 = var11.readLine();
                if (var6 != null) {
                    var6 = var6.substring(Math.max(var6.indexOf(32), 0)).trim() + '2';
                    var8 = var6.charAt(0);
                }

                while ((var6 = var11.readLine()) != null && var6.length() > 0) {
                    ;
                }
            } catch (Throwable var20) {
                ;
            }
        } else {
            var4.setText(this.res.res("SendJava"));
            URLConnection var21 = var1.openConnection();
            var21.setDoOutput(true);
            var21.setDoInput(true);
            var21.setUseCaches(false);
            var21.setRequestProperty("Content-type", "application/octet-stream");
            var9 = var21.getOutputStream();
            var2.writeTo(var9);
            var9.flush();
            var9.close();
            var11 = new BufferedReader(new InputStreamReader(var21.getInputStream(), var7));
        }

        StringBuffer var22 = new StringBuffer();
        String var23 = null;

        try {
            while ((var17 = var11.read()) != -1) {
                if (var17 != 13 && (var22.length() != 0 || !Character.isWhitespace((char) var17))) {
                    var22.append((char) var17);
                    if (var17 == 10 && var23 == null) {
                        var23 = var22.toString();
                        var22 = new StringBuffer();
                    }
                }
            }

            if (var23 == null) {
                var23 = var22.toString();
                var22.setLength(0);
            }
        } catch (Throwable var19) {
            ;
        }

        try {
            var11.close();
            if (var10 != null) {
                var10.close();
            }
        } catch (Throwable var18) {
            ;
        }

        if (var8 != '2' && var8 != '3') {
            var22.insert(0, (String) (var23 + '\n'));
        } else {
            if (var23 == null || var23.length() <= 0) {
                return null;
            }

            if (var23.indexOf("error") < 0) {
                var17 = var23.indexOf("URL:");
                return var17 >= 0 ? var23.substring(var17 + 4, var23.length()) : null;
            }
        }

        throw new InterruptedException(var22.toString());
    }

    protected void processEvent(AWTEvent var1) {
        try {
            int var2 = var1.getID();
            if (this.mi != null && var1 instanceof MouseEvent) {
                MouseEvent var6 = (MouseEvent) var1;
                Point var4 = this.mi.getLocation();
                var6.translatePoint(-var4.x, -var4.y);
                this.mi.dispatchEvent(var1);
                var6.translatePoint(var4.x, var4.y);
                return;
            }

            if (var2 == 101) {
                Dimension var3 = this.getSize();
                var3.setSize(super.getSize());
                if (!var3.equals(this.dPack)) {
                    this.dPack.setSize(this.getSize());
                    this.pack();
                }

                return;
            }

            super.processEvent(var1);
        } catch (Throwable var5) {
            ;
        }

    }

    private int r(InputStream var1) throws Throwable {
        int var2 = var1.read();
        if (var2 < 0) {
            throw new Throwable();
        } else {
            return var2;
        }
    }

    private void r(String var1) throws Throwable {
        int var3 = 0;
        StringBuffer var5 = new StringBuffer();
        byte[] var6 = new byte[512];
        ByteStream var7 = new ByteStream();
        M.Info var8 = this.mi.info;
        Inflater var9 = new Inflater(false);
        this.work.reset();
        BufferedInputStream var10 = null;

        int var4;
        try {
            var10 = new BufferedInputStream((new URL(this.app.getCodeBase(), var1)).openStream());

            while (true) {
                while (true) {
                    int var2 = this.r((InputStream) var10);
                    if (var2 != 13) {
                        if (var2 == 10) {
                            ++var3;
                            if (var3 >= 2) {
                                Res var16 = new Res();
                                var16.load(var5.toString());
                                LF = var16.getInt("layer_count", 2);
                                var8.setSize(var16.getInt("image_width", 200), var16.getInt("image_height", 200), var16.getInt("quality", 1));
                                this.setL(var8.imW, var8.imH, LF);

                                while (true) {
                                    var7.reset();
                                    var4 = this.r((InputStream) var10) << 8 | this.r((InputStream) var10);

                                    for (int var17 = 0; var17 < var4; ++var17) {
                                        var7.write(this.r((InputStream) var10));
                                    }

                                    var9.reset();
                                    var9.setInput(var7.getBuffer(), 0, var7.size());

                                    while (!var9.needsInput()) {
                                        if ((var3 = var9.inflate(var6, 0, var6.length)) > 0) {
                                            this.work.write(var6, 0, var3);
                                        }
                                    }
                                }
                            }
                        } else {
                            var3 = 0;
                        }

                        var5.append((char) var2);
                    }
                }
            }
        } catch (Throwable var14) {
            if (var10 != null) {
                var10.close();
            }

            this.mi.user.wait = -2;
            int var11 = 0;
            var4 = this.work.size();
            boolean var15 = false;
            M var12 = new M(var8, this.user);

            try {
                while (var11 < var4) {
                    var11 += var12.set(this.work.getBuffer(), var11);
                    var12.draw();
                    this.send(var12);
                }
            } catch (RuntimeException var13) {
                ;
            }

            this.work.reset();
            this.mi.user.wait = 0;
            if (undoLO[0] == null) {
                this.setL(var8.imW, var8.imH, var8.L);
            }

        }
    }

    public void reset() {
        nowI = 0;
        lastI = 0;
        maxGp = 0;
        maxAni = 0;
        aniCount = 0;
        sCount = 0;
        sTime = (long) 0;
        undoLO = null;
        undoMgs = null;
        ani.reset();
        undoMg = null;
        sLO = null;
    }

    public void rSave() {
        if (!this.ts.isMe()) {
            this.mi.isEnable = false;
            this.ts.setEnabled(false);
            this.setEnabled(false);
            if (!Ts.confirm("IsSave")) {
                this.mi.isEnable = true;
                this.ts.setEnabled(true);
                this.setEnabled(true);
            } else {
                try {
                    long var2 = (long) (this.p("security_timer", 0) * 1000);
                    int var4 = this.p("security_click", 0);
                    if ((var2 <= 0L || System.currentTimeMillis() - sTime > var2) && (var4 <= 0 || sCount > var4)) {
                        String var6 = this.save();
                        this.reset();
                        this.app.jump(var6, this.p("url_target", (String) null));
                    } else {
                        this.app.jump(this.p("security_url"), (String) null);
                    }
                } catch (Throwable var5) {
                    var5.printStackTrace();
                    Ts.alert(var5.getMessage() + '\n' + this.res.res("SendE"));

                    int var1;
                    for (var1 = 0; var1 < undoMgs.length; ++var1) {
                        undoMgs[var1].reset();
                    }

                    nowI = 0;
                    lastI = 0;

                    for (var1 = 0; var1 < this.mi.info.L; ++var1) {
                        this.mi.info.layers[var1].isDraw = false;
                    }

                    this.copyLO(this.mi.info.layers, undoLO[0]);
                    this.ts.setEnabled(true);
                    this.setEnabled(true);
                    this.mi.isEnable = true;
                }
            }
        }
    }

    private String save() throws Throwable {
        M.Info var2 = this.mi.info;
        String var3 = null;
        this.wMg(-1);
        Deflater var5 = new Deflater(9, false);
        ByteStream var6 = this.work;
        this.work.reset();
        ByteStream var7 = undoMgs[0];
        ByteStream var8 = undoMgs[1];
        SJpegEncoder var9 = new SJpegEncoder();
        SPngEncoder var10 = new SPngEncoder(var7, var8, var5);
        boolean var11 = false;
        Me var12 = new Me();

        try {
            int var13 = this.p("quality", 1);
            var2.setQuality(var13);
            int var14 = var2.imW;
            int var15 = var2.imH;
            URL var16 = new URL(this.app.getCodeBase(), this.p("url_save", "getpic.cgi"));
            boolean var17 = this.p("poo", false);
            boolean var18 = this.p("send_advance", true);
            boolean var19 = this.p("image_jpeg", false);
            int var20 = this.p("compress_level", 10);
            int var21 = this.p("thumbnail_compress_level", 20);
            int var22 = this.p("image_size", 0);
            int var23 = Math.min(this.p("thumbnail_width", var14), var14);
            int var24 = Math.min(this.p("thumbnail_height", var15), var15);
            var10.setInterlace(this.p("image_interlace", false));
            var12.setModal(false);
            var12.ad(this.res.res("Send0"));
            Label var25 = var12.ad(this.res.res("Send1"));
            var12.pack();
            Awt.moveCenter(var12);
            var12.setVisible(true);
            int[] var26 = new int[var14 * var13 * var15 * var13];
            M var27 = new M(var2, this.mi.user);
            var27.mkLPic(var26, 0, 0, var14, var15, var13);
            var27 = null;
            boolean var28 = false;
            if (var22 != 1) {
                var10.fencode(var6, var26, var14, var15);
            }

            int var32 = var6.size();
            if (var32 == 0 || var22 > 0 && var32 >= var22 * 1024) {
                var6.reset();
                if (var19) {
                    var25.setText(this.res.res("EncJpeg"));
                    var11 = true;
                    var9.encode(var6, var26, var14, var15, var20);
                    if (var22 > 1 && var6.size() >= (int) ((double) var22 * 1.5D) * 1024) {
                        var6.reset();
                        var9.encode(var6, var26, var14, var15, (int) ((double) var20 * 1.5D));
                    }
                } else {
                    var25.setText(this.res.res("CompPNG"));
                    var10.fencode(var6, this.getThumbnail(var26, var14, var15, true, var20 / 4), var14, var15);
                }

                if (var32 == 0 || var6.size() < var32) {
                    var11 = var19;
                }
            }

            var7.reset();
            var6.writeTo(var7);
            var22 = var7.size();
            var6.reset();
            this.O = var6;
            if (!var17) {
                this.wS(this.p("header_magic", "S"));
                byte[] var29 = this.getHeader(var11);
                this.l(var29.length);
                var6.write(var29);
                this.l(var22);
            } else {
                this.w2(0);
                this.w2(0);
            }

            this.wN();
            var7.writeTo(var6);
            if (var23 != var14 || var24 != var15) {
                var26 = this.getThumbnail(var26, var23, var24, false, 0);
            }

            for (int var1 = 0; var1 < 2; ++var1) {
                String var4 = this.p("thumbnail_type" + (var1 > 0 ? "2" : ""));
                if (var1 == 0 && (var23 != var14 || var24 != var15) || var4.length() > 0) {
                    char var33 = var4.length() > 0 ? Character.toLowerCase(var4.charAt(0)) : 112;
                    var32 = var6.size();
                    this.O = var6;
                    this.l(0);
                    switch (var33) {
                        case 'a':
                            this.ani(var6, var5, var7, var8, var13);
                            break;
                        case 'j':
                            var9.encode(var6, var26, var23, var24, var21);
                            break;
                        default:
                            var10.encode(var6, var26, var23, var24, 0);
                    }

                    this.O = var8;
                    var8.reset();
                    this.l(var6.size() - var32 - 8);
                    System.arraycopy(var8.getBuffer(), 0, var6.getBuffer(), var32, 8);
                }
            }

            var5.end();
            var26 = (int[]) null;
            var9 = null;
            var10 = null;
            if (var18) {
                try {
                    var3 = this.post(var16, var6, var7, var25, true);
                } catch (Throwable var30) {
                    var18 = false;
                }
            }

            if (!var18) {
                var3 = this.post(var16, var6, var7, var25, false);
            }
        } catch (Throwable var31) {
            var12.dispose();
            throw var31;
        }

        var12.dispose();
        return var3 == null ? this.p("url_exit", "finish.cgi") : var3;
    }

    public void scroll(boolean var1, int var2, int var3) {
        LComponent[] var4 = this.tool.getCs();
        int var5 = var4.length;
        Point var13 = this.mi.getLocation();
        int var14 = var13.x + this.mi.getGapX();
        int var15 = var13.y + this.mi.getGapY();
        Dimension var16 = this.mi.getSizeW();

        for (int var17 = 0; var17 < var5; ++var17) {
            LComponent var6 = var4[var17];
            if (var6 != null) {
                Point var8 = var6.getLocation();
                Dimension var7 = var6.getSizeW();
                if ((var8.x + var7.width > var13.x && var8.y + var7.height > var13.y && var8.x < var13.x + var16.width && var8.y < var13.y + var16.height || var6.isEscape) && var6.isVisible()) {
                    if (this.iScrollType == 0) {
                        int var10 = var8.x - var14;
                        int var11 = var8.y - var15;
                        int var12 = var7.width;
                        if (var2 > 0) {
                            this.mi.m_paint(var10 - var2, var11, var2, var7.height);
                        }

                        if (var2 < 0) {
                            this.mi.m_paint(var10 + var12, var11, -var2, var7.height);
                        }

                        var12 += Math.abs(var2);
                        if (var3 < 0) {
                            this.mi.m_paint(var10 - var2, var11 + var7.height, var12, -var3);
                        }

                        if (var3 > 0) {
                            this.mi.m_paint(var10 - var2, var11 - var3, var12, var3);
                        }
                    } else {
                        boolean var9 = var6.isEscape;
                        var6.escape(var1);
                        if (!var9) {
                            this.mi.m_paint(var8.x - var13.x, var8.y - var13.y, var7.width, var7.height);
                        }
                    }
                }
            }
        }

    }

    public void send(M var1) {
        int var3 = maxGp;
        int var4 = undoMgs.length;
        int var5 = this.mi.info.L;
        int var6 = nowI / var3;
        int var7 = nowI % var3;
        ByteStream var8 = undoMgs[var6];
        int var2;
        if (nowI < lastI) {
            int var9 = 0;
            byte[] var10 = var8.getBuffer();

            for (var2 = 0; var2 < var7; ++var2) {
                var9 += ((var10[var9] & 255) << 8 | var10[var9 + 1] & 255) + 2;
            }

            var8.seek(var9);
        }

        var1.get(var8, this.work, (M) null);
        if (var1.iHint != 14 || !this.setL(this.mi.info.imW, this.mi.info.imH, var5)) {
            try {
                ++nowI;
                lastI = nowI;
                ++sCount;
                if (var7 + 1 >= var3) {
                    LO[] var14 = this.mi.info.layers;
                    ++var6;
                    if (var6 >= var4) {
                        this.wMg(1);
                        nowI -= var3;
                        lastI = nowI;
                        --var6;
                        LO[] var15 = undoLO[0];
                        ByteStream var11 = undoMgs[0];

                        for (var2 = 1; var2 < var4; ++var2) {
                            undoMgs[var2 - 1] = undoMgs[var2];
                            undoLO[var2 - 1] = undoLO[var2];
                        }

                        for (var2 = 0; var2 < var5; ++var2) {
                            if (undoLO[0][var2].offset == null) {
                                undoLO[0][var2].offset = var15[var2].offset;
                                var15[var2].offset = null;
                            }
                        }

                        undoMgs[var6] = var11;
                        undoLO[var6] = var15;
                    }

                    LO[] var17 = undoLO[var6];

                    for (var2 = 0; var2 < var5; ++var2) {
                        LO var12 = var14[var2];
                        boolean var16 = var12.isDraw;
                        var17[var2].setField(var14[var2]);
                        var12.isDraw = false;
                        if (!var16) {
                            var17[var2].offset = null;
                        } else {
                            var17[var2].reserve();
                            var17[var2].setImage(var14[var2]);
                        }
                    }

                    undoMgs[var6].reset();
                }
            } catch (Throwable var13) {
                var13.printStackTrace();
            }

        }
    }

    public void setARGB(int var1) {
        this.tool.selPix(var1 == 16777215);
        if (this.mi.info.m.iPen != 4 && this.mi.info.m.iPen != 5) {
            this.tool.setARGB(var1);
        }

    }

    public boolean setL(int var1, int var2, int var3) {
        M.Info var4 = this.mi.info;
        boolean var5 = undoLO[0] != null;
        boolean var6 = var1 == var4.imW && var2 == var4.imH && var3 == var4.L;
        if (var5) {
            var6 = undoLO[0].length == var3;
        }

        if (var6 && var5) {
            return false;
        } else {
            try {
                if (var5) {
                    this.wMg(-1);
                }
            } catch (Throwable var10) {
                ;
            }

            var4.setSize(var1, var2, var4.Q);
            var4.setL(var3);
            LO[] var7 = var4.layers;
            this.setLName();

            int var8;
            for (var8 = 0; var8 < var3; ++var8) {
                var7[var8].isDraw = false;
            }

            for (var8 = 0; var8 < undoMgs.length; ++var8) {
                undoLO[var8] = new LO[var3];

                for (int var9 = 0; var9 < var3; ++var9) {
                    undoLO[var8][var9] = new LO(var1 * var4.Q, var2 * var4.Q);
                }

                undoMgs[var8].reset();
            }

            nowI = 0;
            lastI = 0;
            this.copyLO(var7, undoLO[0]);
            sLO = var7;
            return true;
        }
    }

    public void setLineSize(int var1) {
        this.tool.setLineSize(var1);
    }

    public void undo(boolean var1) {
        try {
            int var2 = this.mi.info.L;
            int var3 = lastI;
            LO[] var4 = this.mi.info.layers;
            if (var1) {
                if (nowI <= 0) {
                    return;
                }

                --nowI;
            } else {
                if (nowI >= var3) {
                    return;
                }

                ++nowI;
            }

            this.user.wait = -2;
            int var6 = nowI / maxGp;
            int var7 = nowI % maxGp;
            byte[] var8 = undoMgs[var6].getBuffer();

            int var10;
            for (var10 = 0; var10 < var2; ++var10) {
                LO var5 = var4[var10];

                int var9;
                for (var9 = var6; var9 > 0 && !undoLO[var9][var10].isDraw; --var9) {
                    ;
                }

                var5.setField(undoLO[var6][var10]);
                var5.setImage(undoLO[var9][var10]);
            }

            var10 = 0;

            for (int var11 = 0; var11 < var7; ++var11) {
                var10 += this.m.set(var8, var10);
                this.m.draw();
            }

            this.user.wait = 0;
            this.mi.m_paint((Rectangle) null);
            this.tool.up();
        } catch (Throwable var12) {
            var12.printStackTrace();
        }

    }

    public void update(Graphics var1) {
        this.paint(var1);
    }

    private void w2(int var1) throws Throwable {
        for (int var2 = 8; var2 >= 0; var2 -= 8) {
            this.O.write(var1 >>> var2 & 255);
        }

    }

    private void wMg(int var1) throws Throwable {
        ByteStream var2 = ani;
        synchronized (ani) {
            if (maxAni == 0 || ani.size() < maxAni) {
                if (var1 < 0) {
                    var1 = undoMgs.length;
                }

                for (int var6 = 0; var6 < var1; ++var6) {
                    ByteStream var3 = undoMgs[var6];
                    int var5 = var3.size();

                    for (int var4 = 0; var4 < var5; ++aniCount) {
                        var4 += this.m.set(var3.getBuffer(), var4);
                        this.m.get(ani, this.work, undoMg);
                        if (undoMg == null) {
                            undoMg = new M();
                        }

                        undoMg.set(this.m);
                    }
                }

            }
        }
    }

    private void wN() throws Throwable {
        this.w2(3338);
    }

    private void wS(String var1) throws Throwable {
        for (int var3 = 0; var3 < var1.length(); ++var3) {
            char var2 = var1.charAt(var3);
            if (var2 > 255) {
                this.O.write(37);
                this.O.write(Character.forDigit(var2 >>> 8 & 255, 16));
                this.O.write(Character.forDigit(var2 & 255, 16));
            } else {
                this.O.write(var2);
            }
        }

    }

    protected void mP(String var1) {
        try {
            int var2 = var1.indexOf(58);
            if (var2 <= 0) {
                return;
            }

            M.Info var3 = this.mi.info;
            String var4 = var1.substring(0, var2).trim();
            String var5 = var1.substring(var2 + 1).trim();
            boolean var6 = false;
            if (var5.length() > 0) {
                var6 = var5.charAt(0) == 't';
            }

            if (var4.equals("layer")) {
                var3.permission = var5.equals("all") ? -1L : Long.parseLong(var5);
                return;
            }

            if (var4.equals("layer_edit")) {
                var3.isLEdit = var6;
                return;
            }

            if (var4.equals("canvas")) {
                this.mi.isEnable = var6;
                return;
            }

            if (var4.equals("fill")) {
                var3.isFill = var6;
                return;
            }

            if (var4.equals("clean")) {
                var3.isClean = var6;
                return;
            }

            if (var4.equals("unlayer")) {
                var3.unpermission = (long) Integer.parseInt(var5);
            }
        } catch (RuntimeException var7) {
            var7.printStackTrace();
        }

    }

    private void setLName() {
        LO[] var1 = this.mi.info.layers;
        String var2 = this.res.res("Layer");

        for (int var3 = 0; var3 < this.mi.info.L; ++var3) {
            if (var1[var3].name == null) {
                var1[var3].makeName(var2);
            }
        }

    }
}
