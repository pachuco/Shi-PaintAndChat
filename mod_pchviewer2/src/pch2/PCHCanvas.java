package pch2;

import jaba.applet.Applet;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.Inflater;

import static java.awt.event.ComponentEvent.*;

import paintchat.LO;
import paintchat.M;
import paintchat.Mg;
import paintchat.Res;
import syi.awt.Awt;
import syi.util.ByteStream;

public class PCHCanvas extends Canvas implements Runnable, WindowListener {
    public static final String STR_VERSION = "(C)しぃちゃん PCHCanvas v1.14";
    private Object oSync = new Object();
    public static boolean isWin = true;
    private boolean live = true;
    private boolean liveDraw = false;
    private boolean liveLoad = false;
    private boolean isSuspend = false;
    private boolean isOldVersion = false;
    private Res cf;
    private Image image = null;
    private Applet applet;
    private Graphics primary;
    private Graphics back;
    private Mg mgLine = new Mg();
    private M mgLine2 = new M();
    private Mg.Info info;
    private Mg.User user;
    private M.Info info2;
    private M.User user2;
    private boolean is_init = false;
    private int[] b_bk = null;
    private int b_bk_w = 0;
    private int b_bk_h = 0;
    private URL urlPCH = null;
    private int seek = 0;
    private int mark = 0;
    private int lineCount = 0;
    private int lineMax = 0;
    private byte[][] mglines = null;
    private Thread tDraw = null;
    private Thread tLoad = null;
    private PCHProgress progress = null;
    private Point pressPo = null;

    public PCHCanvas(Applet var1, Object var2, boolean var3, Res var4) {
        this.enableEvents(COMPONENT_EVENT_MASK | MOUSE_EVENT_MASK | MOUSE_MOTION_EVENT_MASK);
        this.setCursor(Cursor.getPredefinedCursor(12));
        this.cf = new Res(var1, var2, new ByteStream());
        Enumeration var6 = var4.keys();

        while (var6.hasMoreElements()) {
            Object var5 = var6.nextElement();
            this.cf.put(var5, var4.get(var5));
        }

        this.applet = var1;
    }

    public synchronized void addMg(byte[] var1) {
        if (this.mglines == null) {
            this.mglines = new byte[this.lineMax][];
        }

        if (this.lineCount >= this.lineMax) {
            this.lineMax = (int) ((float) this.lineMax * 1.25F);
            byte[][] var2 = new byte[this.lineMax][];
            System.arraycopy(this.mglines, 0, var2, 0, this.mglines.length);
            this.mglines = var2;
        }

        this.mglines[this.lineCount++] = var1;
        ++this.mark;
        if (this.progress != null) {
            this.progress.drawBar();
        }

    }

    private synchronized void clearCanvas() {
        try {
            int var1 = this.cf.getP("layer_count", 2);
            int var2;
            int var3;
            int var4;
            int var7;
            if (this.isOldVersion) {
                if (this.info == null) {
                    return;
                }

                this.info.setL(var1);
                int[][] var5 = this.info.getOffset();
                int var10 = var5[0].length;
                int[] var12 = var5[0];
                this.mgLine.memset(var12, 16777215);
                int var8 = 1;

                while (true) {
                    if (var8 >= var5.length) {
                        var4 = this.info.Q;
                        var2 = this.info.W;
                        var3 = this.info.H;
                        break;
                    }

                    System.arraycopy(var12, 0, var5[var8], 0, var10);
                    ++var8;
                }
            } else {
                if (this.info2 == null) {
                    return;
                }

                this.info2.setL(var1);
                LO[] var6 = this.info2.layers;

                for (var7 = 0; var7 < this.info2.L; ++var7) {
                    var6[var7].clear();
                }

                var4 = this.info2.Q;
                var2 = this.info2.W;
                var3 = this.info2.H;
            }

            String var11 = this.applet.getParameter("image_canvas");
            if (var11 != null && var11.length() > 0) {
                if (this.b_bk == null) {
                    Image var13 = this.applet.getImage(new URL(this.applet.getCodeBase(), var11));
                    Awt.wait(var13);
                    if (var4 != 1) {
                        Image var14 = var13.getScaledInstance(var13.getWidth((ImageObserver) null) * var4, var13.getHeight((ImageObserver) null) * var4, 16);
                        Awt.wait(var14);
                        var13.flush();
                        var13 = var14;
                    }

                    this.b_bk_w = var13.getWidth((ImageObserver) null);
                    this.b_bk_h = Math.min(var13.getHeight((ImageObserver) null), var3);
                    PixelGrabber var15 = new PixelGrabber(var13, 0, 0, this.b_bk_w, this.b_bk_h, true);
                    var15.grabPixels();
                    this.b_bk = (int[]) var15.getPixels();
                    var13.flush();
                }

                this.info2.layers[0].reserve();

                for (var7 = 0; var7 < this.b_bk_h; ++var7) {
                    System.arraycopy(this.b_bk, var7 * this.b_bk_w, this.info2.layers[0].offset, var7 * var2, Math.min(this.b_bk_w, var2));
                }
            }

            if (this.back != null) {
                this.back.setColor(Color.white);
                this.back.fillRect(0, 0, this.image.getWidth((ImageObserver) null), this.image.getHeight((ImageObserver) null));
            }

            this.repaint();
        } catch (Throwable var9) {
            var9.printStackTrace();
        }

    }

    public synchronized void clearPCH() {
        this.stopLoad();
        this.setMark(0);
        this.lineCount = 0;
        int var1 = this.mglines.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            this.mglines[var2] = null;
        }

    }

    public synchronized void destroyPCH() {
        Container var1 = this.getParent();
        if (var1 instanceof Window) {
            ((Window) var1).dispose();
        } else {
            var1.remove(this);
        }

        this.stopLoad();

        try {
            if (this.tDraw != null) {
                this.liveDraw = false;
                this.tDraw.interrupt();
                if (this.isSuspend) {
                    synchronized (this.tDraw) {
                        this.tDraw.notify();
                        this.tDraw.join();
                    }
                }
            }
        } catch (Throwable var4) {
        }

    }

    public void error(String var1) {
        try {
            if (this.image == null) {
                return;
            }

            Dimension var2 = this.getSize();
            new Font("Sans", 0, 16);
            this.back.setColor(new Color(13421823));
            this.back.fillRect(0, 0, var2.width, var2.height);
            this.back.setColor(new Color(5263480));
            this.back.drawString(STR_VERSION, 5, 32);
            this.back.drawString(var1, 5, 64);
            this.repaint();
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public int getLineCount() {
        return this.lineMax;
    }

    public int getMark() {
        return this.mark;
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    public static Window getParentTop(Component var0) {
        try {
            Container var1;
            for (var1 = var0.getParent(); !(var1 instanceof Window); var1 = var1.getParent()) {
            }

            return (Window) var1;
        } catch (Throwable var2) {
            return null;
        }
    }

    public Dimension getPreferredSize() {
        return this.getSize();
    }

    public int getSeek() {
        return this.seek;
    }

    public int getSpeed() {
        this.initUser();
        return this.user != null ? this.user.wait : (this.user2 != null ? this.user2.wait : 0);
    }

    private void initUser() {
        if (this.isOldVersion) {
            if (this.info != null) {
                return;
            }

            synchronized (this.mgLine) {
                if (this.user == null) {
                    this.user = this.mgLine.newUser(this);
                }
            }
        } else {
            if (this.info2 != null) {
                return;
            }

            synchronized (this.mgLine2) {
                if (this.user2 == null) {
                    this.user2 = this.mgLine2.newUser(this);
                }
            }
        }

    }

    private void loadConfig(InputStream var1, ByteStream var2) throws IOException {
        var2.reset();
        int var4 = 0;

        int var3;
        while ((var3 = var1.read()) != -1) {
            if (var3 != 13) {
                if (var3 == 10) {
                    ++var4;
                    if (var4 >= 2) {
                        this.cf.load(new String(var2.getBuffer(), 0, var2.size(), "UTF8"));
                        this.isOldVersion = this.cf.getP("version", 1) <= 1;
                        this.mglines = null;
                        this.lineMax = this.cf.getP("count_lines", 5);
                        this.initUser();
                        Dimension var5 = this.getSize();
                        int var6 = Math.max(this.cf.getP("Client_Image_Width", this.cf.getP("image_width", var5.width)), 1);
                        int var7 = Math.max(this.cf.getP("Client_Image_Height", this.cf.getP("image_height", var5.height)), 1);
                        int var8 = this.cf.getP("layer_count", 2);
                        int var9 = this.cf.getP("quality", 1);
                        if (this.isOldVersion) {
                            this.info = this.mgLine.newInfo(this.applet, this, this.cf);
                            this.info.setSize(var6, var7, var9);
                            this.info.setL(var8);
                        } else {
                            this.info2 = this.mgLine2.newInfo(this.applet, this, this.cf);
                            this.info2.setSize(var6, var7, var9);
                            this.info2.setL(var8);
                        }

                        this.setCanvasSize(var6, var7);
                        if (var6 > var5.width || var7 > var5.height) {
                            Container var10 = this.getParent();
                            if (var10 instanceof Window) {
                                ((Window) var10).pack();
                            }
                        }

                        this.updateInfo();
                        this.moveCenter();
                        this.is_init = true;
                        return;
                    }
                } else {
                    var4 = 0;
                }

                var2.write(var3);
            }
        }

        throw new EOFException();
    }

    public synchronized void loadPCH(URL var1, int var2, int var3) throws InterruptedException {
        if (var1 != null) {
            if (this.urlPCH != null && var1.equals(this.urlPCH)) {
                this.setMark(0);
                this.setMark(-1);
            } else {
                this.urlPCH = var1;
                this.stopLoad();
                this.liveLoad = true;
                Thread var4 = new Thread(this, "l");
                var4.setDaemon(true);
                var4.setPriority(1);
                this.tLoad = var4;
                var4.start();
            }
        }
    }

    private void m_paint(Graphics var1, Rectangle var2) {
        try {
            synchronized (this.oSync) {
                if (var1 == null) {
                    var1 = this.primary;
                }

                if (var1 == null) {
                    return;
                }

                Dimension var4 = this.getSize();
                int var5;
                int var6;
                int var7;
                int var8;
                if (var2 == null) {
                    var5 = 0;
                    var6 = 0;
                    var7 = var4.width;
                    var8 = var4.height;
                } else {
                    var5 = var2.x < 0 ? 0 : var2.x;
                    var6 = var2.y < 0 ? 0 : var2.y;
                    var7 = var2.x + var2.width > var4.width ? var4.width - var2.x : var4.width;
                    var8 = var2.y + var2.height > var4.height ? var4.height - var2.y : var4.height;
                }

                if (var7 <= 0 || var8 <= 0) {
                    return;
                }

                if (this.isOldVersion) {
                    if (this.mgLine == null) {
                        return;
                    }

                    synchronized (this.mgLine) {
                        this.mgLine.m_paint(var5, var6, var7, var8);
                    }
                } else {
                    if (this.mgLine2 == null) {
                        return;
                    }

                    synchronized (this.mgLine2) {
                        this.mgLine2.m_paint(var5, var6, var7, var8);
                    }
                }
            }
        } catch (Throwable var13) {
            var13.printStackTrace();
        }

    }

    private void m_po(Graphics var1, Rectangle var2) {
        int var3 = var2.x;
        int var4 = var2.y;
        int var5 = var2.width;
        int var6 = var2.height;
        if (var3 < 0) {
            var3 = 0;
        }

        if (var4 < 0) {
            var4 = 0;
        }

        if (var5 > 0 && var6 > 0 && this.is_init) {
            if (this.isOldVersion) {
                if (this.mgLine == null) {
                    return;
                }

                synchronized (this.mgLine) {
                    this.mgLine.m_paint(var3, var4, var5, var6);
                }
            } else {
                this.getSize();
                if (this.mgLine2 == null) {
                    return;
                }

                synchronized (this.mgLine2) {
                    this.mgLine2.m_paint(var3, var4, var5, var6);
                }
            }

        }
    }

    public void paint(Graphics var1) {
        try {
            this.m_paint(var1, var1.getClipBounds());
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public synchronized void playPCH() {
        if (this.tDraw != null && this.liveDraw) {
            if (this.isSuspend) {
                synchronized (this.tDraw) {
                    this.tDraw.notify();
                }
            }
        } else {
            this.tDraw = new Thread(this, "d");
            this.liveDraw = true;
            this.tDraw.setDaemon(true);
            this.tDraw.setPriority(1);
            this.tDraw.start();
        }

    }

    protected void processComponentEvent(ComponentEvent var1) {
        try {
            if (var1.getID() == 102 || var1.getID() == 101) {
                this.updateInfo();
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    protected void processMouseEvent(MouseEvent var1) {
        this.processMouseMotionEvent(var1);
    }

    protected void processMouseMotionEvent(MouseEvent var1) {
        try {
            switch (var1.getID()) {
                case 501:
                    this.pressPo = var1.getPoint();
                    this.setCursor(Cursor.getPredefinedCursor(12));
                    break;
                case 502:
                    this.pressPo = null;
                    this.setCursor(Cursor.getDefaultCursor());
                    this.repaint();
                case 503:
                case 504:
                case 505:
                default:
                    break;
                case 506:
                    this.scroll(var1.getPoint(), true, false);
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    private int r(InputStream var1) throws IOException {
        int var2 = var1.read();
        if (var2 == -1) {
            throw new EOFException();
        } else {
            return var2;
        }
    }

    private void rDraw() throws InterruptedException {
        if (this.seek == 0) {
            this.clearCanvas();
        }

        while (this.liveDraw) {
            if (this.seek < this.mark) {
                if (this.seek == 0) {
                    this.clearCanvas();
                }

                if (this.isOldVersion) {
                    this.mgLine.set(this.mglines[this.seek++], 0);
                    if (this.progress != null) {
                        this.progress.drawBar();
                    }

                    if (this.mgLine.iLayer >= this.info.L) {
                        this.info.setL(this.mgLine.iLayer + 1);
                    }

                    synchronized (this.oSync) {
                        this.mgLine.draw();
                    }
                } else {
                    this.mgLine2.set(this.mglines[this.seek++], 0);
                    if (this.progress != null) {
                        this.progress.drawBar();
                    }

                    if (this.mgLine2.iLayer >= this.info2.L) {
                        this.info2.setL(this.mgLine2.iLayer + 1);
                    }

                    synchronized (this.oSync) {
                        this.mgLine2.draw();
                    }
                }
            } else {
                Thread.sleep(1000L);
            }

            if (this.isSuspend) {
                this.m_paint((Graphics) null, (Rectangle) null);
                synchronized (this.tDraw) {
                    if (this.isSuspend) {
                        this.tDraw.wait();
                    }

                    this.isSuspend = false;
                }
            }
        }

    }

    private void rLoad() throws InterruptedException {
        Inflater var1 = new Inflater(false);
        ByteStream var2 = new ByteStream();
        DataInputStream var3 = null;

        try {
            var3 = new DataInputStream(this.urlPCH.openStream());
            this.loadConfig(var3, var2);
            var2.reset();
            byte[] var4 = new byte[1024];
            byte[] var5 = new byte[512];

            int var9;
            while (this.liveLoad && (var9 = var3.readUnsignedShort()) >= 0) {
                var1.reset();
                int var8 = 0;

                label77:
                while (this.liveLoad && var8 < var9) {
                    int var10 = Math.min(var9 - var8, var4.length);
                    var3.readFully(var4, 0, var10);
                    var8 += var10;
                    var1.setInput(var4, 0, var10);

                    while (true) {
                        while (true) {
                            if (!this.liveLoad) {
                                continue label77;
                            }

                            int var11 = var1.inflate(var5, 0, var5.length);
                            if (var11 <= 0) {
                                if (var1.needsInput()) {
                                    continue label77;
                                }
                            } else {
                                var2.write(var5, 0, var11);
                                if (var2.size() >= 2) {
                                    var11 = 0;

                                    while (var2.size() - var11 >= 2) {
                                        byte[] var6 = var2.getBuffer();
                                        var10 = ((var6[var11] & 255) << 8 | var6[var11 + 1] & 255) + 2;
                                        if (var2.size() - var11 < var10) {
                                            break;
                                        }

                                        byte[] var7 = new byte[var10];
                                        System.arraycopy(var6, var11, var7, 0, var10);
                                        var11 += var10;
                                        this.addMg(var7);
                                        if (!this.liveLoad) {
                                            break;
                                        }
                                    }

                                    if (var11 > 0) {
                                        var2.reset(var11);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (EOFException var13) {
        } catch (Throwable var14) {
            var14.printStackTrace();
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            this.error(var14.getMessage());
        }

        if (var3 != null) {
            try {
                var3.close();
            } catch (IOException var12) {
            }
        }

        var1.end();
    }

    public void run() {
        try {
            switch (Thread.currentThread().getName().charAt(0)) {
                case 'd':
                    this.rDraw();
                    break;
                case 'l':
                    this.rLoad();
            }
        } catch (InterruptedException var2) {
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    private synchronized void scroll(Point var1, boolean var2, boolean var3) {
        if (var3) {
            this.setLocation(var1);
            var2 = true;
        } else if (this.pressPo == null || this.primary == null || this.pressPo.equals(var1)) {
            return;
        }

        synchronized (this.oSync) {
            int var5 = var1.x - this.pressPo.x;
            int var6 = var1.y - this.pressPo.y;
            if (var2) {
                var5 = -var5;
                var6 = -var6;
            }

            Point var7 = this.getLocation();
            this.setLocation(var7.x - var5, var7.y - var6);
        }
    }

    public synchronized void setMark(int var1) {
        var1 = var1 >= 0 && var1 < this.lineCount ? var1 : this.lineCount;
        if (var1 < this.seek) {
            this.seek = 0;
            this.clearCanvas();
        }

        this.mark = var1;
    }

    public void setProgress(PCHProgress var1) {
        this.progress = var1;
    }

    public void setSpeed(int var1) {
        try {
            this.initUser();
            if (this.user != null) {
                this.user.wait = var1;
            }

            if (this.user2 != null) {
                this.user2.wait = var1;
            }

            if (this.progress != null) {
                this.progress.repaint();
            }
        } catch (RuntimeException var3) {
            var3.printStackTrace();
        }

    }

    private void updateInfo() {
        synchronized (this.oSync) {
            if (this.primary != null) {
                this.primary.dispose();
            }

            this.primary = this.getGraphics();
            Dimension var2 = this.getSize();
            int var3 = var2.width;
            int var4 = var2.height;
            if (this.back != null) {
                var3 = this.image.getWidth((ImageObserver) null);
                var4 = this.image.getHeight((ImageObserver) null);
            }

            if (this.info != null) {
                this.info.setComponent(this, this.back != null ? this.back : this.primary, var3, var4);
                var3 = this.info.imW;
                var4 = this.info.imH;
            }

            if (this.info2 != null) {
                this.info2.setComponent(this, this.back != null ? this.back : this.primary, var3, var4);
                var3 = this.info2.imW;
                var4 = this.info2.imH;
            }

        }
    }

    public synchronized void setVisit(int var1, boolean var2) {
        int var3 = this.isOldVersion ? this.info.visit.length : this.info2.L;
        if (var1 > -1 && var1 < var3) {
            if (this.isOldVersion) {
                this.info.visit[var1] = (float) (var2 ? 255 : 0);
            } else {
                this.info2.layers[var1].iAlpha = (float) (var2 ? 1 : 0);
            }

            this.repaint();
        }
    }

    public synchronized void stopLoad() {
        if (this.tLoad != null) {
            this.liveLoad = false;
            this.tLoad.interrupt();
            this.tLoad = null;
        }

    }

    public void suspendDraw() {
        this.isSuspend = true;
    }

    public void setScale(int var1, boolean var2) {
        synchronized (this.oSync) {
            if (this.isOldVersion) {
                if (this.info != null) {
                    this.info.addScale(var1, var2);
                    this.setCanvasSize(this.info.W, this.info.H);
                }
            } else if (this.info2 != null) {
                this.info2.addScale(var1, var2);
                this.setCanvasSize(this.info2.W, this.info2.H);
            }
        }

        this.repaint();
    }

    public float getScale() {
        if (this.info != null) {
            return (float) this.info.scale / (float) this.info.Q;
        } else {
            return this.info2 != null ? (float) this.info2.scale / (float) this.info2.Q : 1.0F;
        }
    }

    public void moveCenter() {
        Dimension var1 = this.getParent().getSize();
        Dimension var2 = this.getSize();
        this.setLocation((var1.width - var2.width) / 2, (var1.height - var2.height) / 2);
    }

    private void setCanvasSize(int var1, int var2) {
        synchronized (this.oSync) {
            if (this.isOldVersion) {
                var1 = (int) ((float) (this.info.W * this.info.scale) / (float) this.info.Q);
                var2 = (int) ((float) (this.info.H * this.info.scale) / (float) this.info.Q);
            } else {
                var1 = (int) ((float) (this.info2.W * this.info2.scale) / (float) this.info2.Q);
                var2 = (int) ((float) (this.info2.H * this.info2.scale) / (float) this.info2.Q);
            }

            this.setSize(var1, var2);

            try {
                Container var4 = this.getParent().getParent();
                if (var4 instanceof Window) {
                    var4.setVisible(false);
                    var4.setSize(var1, var2 + 60);
                    var4.setVisible(true);
                }
            } catch (Throwable var5) {
            }

        }
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }

    public void windowActivated(WindowEvent var1) {
    }

    public void windowClosed(WindowEvent var1) {
    }

    public void windowClosing(WindowEvent var1) {
        try {
            this.suspendDraw();
            this.stopLoad();
            var1.getWindow().dispose();
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void windowDeactivated(WindowEvent var1) {
    }

    public void windowDeiconified(WindowEvent var1) {
    }

    public void windowIconified(WindowEvent var1) {
    }

    public void windowOpened(WindowEvent var1) {
    }
}
