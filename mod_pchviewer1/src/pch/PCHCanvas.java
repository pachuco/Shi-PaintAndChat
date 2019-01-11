package pch;

import jaba.applet.Applet;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import paintchat.MgLine;

public class PCHCanvas extends Canvas implements Runnable, WindowListener {
    public static final String STR_VERSION = "(C)しぃちゃん PCHCanvas v1.02";
    public static boolean isWin = true;
    private boolean live = true;
    private boolean liveDraw = false;
    private boolean liveLoad = false;
    private boolean isSuspend = false;
    private Applet applet;
    private int[][] i_off = null;
    private int imW = 0;
    private int imH = 0;
    private Image image = null;
    private Graphics primary;
    private Graphics back;
    private int[] b_bk = null;
    private int b_bk_w = 0;
    private int b_bk_h = 0;
    private int scaleX = 0;
    private int scaleY = 0;
    private int scale = 1;
    private int visit0 = 255;
    private int visit1 = 255;
    private MgLine mg_b = null;
    private int speed = 10;
    private URL urlPCH = null;
    private int seek = 0;
    private int mark = 0;
    private int lineCount = 0;
    private MgLine[] mglines = null;
    private Thread tDraw = null;
    private Thread tLoad = null;
    private PCHProgress progress = null;
    private Point oldPo = null;

    public PCHCanvas(Applet var1, boolean var2) {
        this.enableEvents(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.applet = var1;
    }

    public synchronized void addMg(MgLine var1) {
        if (this.mglines == null) {
            this.mglines = new MgLine[100];
        }

        this.mglines[this.lineCount++] = var1;
        ++this.mark;
        if (this.progress != null) {
            this.progress.drawBar();
        }

        if (this.lineCount >= this.mglines.length) {
            MgLine[] var2 = new MgLine[(int) ((double) this.lineCount * 1.5D)];
            System.arraycopy(this.mglines, 0, var2, 0, this.mglines.length);
            this.mglines = var2;
        }

    }

    private synchronized void clearCanvas() {
        try {
            int var1 = 0xFFFFFF;
            if (this.i_off != null) {
                int var2 = this.i_off[0].length;
                if (var2 > 0) {
                    int var3 = 0;

                    while (true) {
                        if (var3 >= var2) {
                            System.arraycopy(this.i_off[0], 0, this.i_off[1], 0, var2);
                            break;
                        }

                        this.i_off[0][var3] = var1;
                        ++var3;
                    }
                }

                String var9 = this.applet == null ? null : this.applet.getParameter("image_canvas");
                if (var9 != null && var9.length() > 0) {
                    if (this.b_bk == null) {
                        Image var4 = this.applet.getImage(new URL(this.applet.getCodeBase(), var9));
                        MediaTracker var5 = new MediaTracker(this);
                        var5.addImage(var4, 0);
                        var5.waitForID(0);
                        this.b_bk_w = var4.getWidth((ImageObserver) null);
                        this.b_bk_h = Math.min(var4.getHeight((ImageObserver) null), this.imH);
                        PixelGrabber var6 = new PixelGrabber(var4, 0, 0, this.b_bk_w, var4.getHeight((ImageObserver) null), true);
                        var6.grabPixels();
                        this.b_bk = (int[]) var6.getPixels();
                        var4.flush();
                    }

                    for (int var11 = 0; var11 < this.b_bk_h; ++var11) {
                        System.arraycopy(this.b_bk, var11 * this.b_bk_w, this.i_off[0], var11 * this.imW, Math.min(this.b_bk_w, this.imW));
                    }
                }
            }

            Color var8 = new Color(var1);
            if (this.back != null) {
                this.back.clearRect(0, 0, this.imW, this.imH);
                this.back.setColor(var8);
                this.back.fillRect(0, 0, this.imW, this.imH);
            }

            if (this.primary != null) {
                Dimension var10 = this.getSize();
                this.primary.clearRect(0, 0, var10.width, var10.height);
                this.primary.setColor(var8);
                this.primary.fillRect(0, 0, this.imW, this.imH);
            }

            this.repaint();
        } catch (Throwable var7) {
            var7.printStackTrace();
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
        if (this.tDraw != null) {
            this.liveDraw = false;
            this.tDraw.interrupt();
            if (this.isSuspend) {
                synchronized (this.tDraw) {
                    this.tDraw.notify();
                }
            }
        }

    }

    public void error(String var1) {
        try {
            if (this.image == null) {
                return;
            }

            new Font("Sans", 0, 16);
            this.back.setColor(new Color(13421823));
            this.back.fillRect(0, 0, this.imW, this.imH);
            this.back.setColor(new Color(5263480));
            this.back.drawString(STR_VERSION, 5, 32);
            this.back.drawString(var1, 5, 64);
            this.repaint();
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public int getLineCount() {
        return this.lineCount;
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
        return this.imW != 0 && this.imH != 0 ? new Dimension(this.imW, this.imH) : new Dimension(300, 300);
    }

    public int getSeek() {
        return this.seek;
    }

    public int getSpeed() {
        return this.speed;
    }

    public synchronized void loadPCH(URL var1, int var2, int var3) throws InterruptedException {
        if (var1 != null) {
            this.setSize(var2, var3);
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

    private void m_paint(Graphics var1) {
        try {
            if (var1 == null) {
                var1 = this.primary;
            }

            if (var1 == null) {
                return;
            }

            this.m_po(var1, var1.getClipBounds());
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    private void m_po(Graphics var1, Rectangle var2) {
        if (this.mg_b == null) {
            this.mg_b = new MgLine(0);
        }

        this.mg_b.setVisit(this.visit0, this.visit1);
        Dimension var3 = this.getSize();
        if (var2 == null) {
            var2 = new Rectangle(var3);
        }

        int var4 = this.scaleX * this.scale;
        int var5 = this.scaleY * this.scale;
        int var6 = var2.x;
        int var7 = var2.y;
        int var8 = var2.width;
        int var9 = var2.width;
        int var12 = var4 + this.imW * this.scale;
        int var13 = var5 + this.imH * this.scale;
        int var14 = Math.max(var6, var4);
        int var15 = Math.max(var7, var5);
        int var16 = Math.min(var8, var12);
        int var17 = Math.min(var9, var13);
        if (var16 - var14 > 0 && var17 - var15 > 0) {
            int var18 = 16384 / this.imW;
            int var20 = 0;
            int var22 = Math.max((var14 - var4) / this.scale, 0);
            int var23 = Math.max((var15 - var5) / this.scale, 0);
            int var24 = (var16 - var14) / this.scale;

            while (true) {
                int var19 = Math.min(var18, (var17 - var15) / this.scale - var20);
                if (var19 <= 0) {
                    return;
                }

                Image var21 = this.mg_b.makeLPic((int[]) null, this.i_off, this.imW, this.imH, var22, var23 + var20, var24, var19);
                var1.drawImage(var21, var14, var15 + var20 * this.scale, var16 - var14, var19 * this.scale, Color.white, (ImageObserver) null);
                var21.flush();
                var20 += var18;
            }
        }
    }

    public void paint(Graphics var1) {
        try {
            if (this.primary == null) {
                this.primary = this.getGraphics();
            }

            synchronized (var1) {
                this.m_paint(var1);
            }
        } catch (Throwable var4) {
            var4.printStackTrace();
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

    protected void processComponentEvent(ComponentEvent event) {
        try {
            if (event.getID() == 101 && this.primary == null) {
                this.primary = this.getGraphics();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    protected void processMouseEvent(MouseEvent event) {
        this.processMouseMotionEvent(event);
    }

    protected void processMouseMotionEvent(MouseEvent event) {
        try {
            switch (event.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                    this.oldPo = event.getPoint();
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    this.oldPo = null;
                    this.setCursor(Cursor.getDefaultCursor());
                    this.repaint();
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                default:
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    this.scroll(event.getPoint(), true);
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    private void rDraw() throws InterruptedException {
        if (this.seek == 0) {
            this.clearCanvas();
        }

        while (this.liveDraw) {
            if (this.seek < this.mark) {
                MgLine var1 = this.mglines[this.seek++];
                if (this.progress != null) {
                    this.progress.drawBar();
                }

                synchronized (this.primary) {
                    var1.draw(this, this.primary, this.back, this.i_off, this.imW, this.imH, -this.scaleX, -this.scaleY, this.scale, this.visit0, this.visit1, this.speed);
                }
            } else {
                Thread.sleep(1000L);
            }

            if (this.isSuspend) {
                this.m_paint((Graphics) null);
                synchronized (this.tDraw) {
                    this.tDraw.wait();
                    this.isSuspend = false;
                }
            }
        }

    }

    private void rLoad() throws InterruptedException {
        DataInputStream var2 = null;

        try {
            var2 = new DataInputStream(new GZIPInputStream(this.urlPCH.openStream(), 4000));

            while (this.liveLoad) {
                MgLine var1 = new MgLine(1);
                var1.setData(var2);
                this.addMg(var1);
            }
        } catch (EOFException var5) {
        } catch (IOException var6) {
            this.error(var6.getMessage());
        }

        if (var2 != null) {
            try {
                var2.close();
            } catch (IOException var4) {
            }
        }

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

    private synchronized void scroll(Point var1, boolean var2) {
        if (this.oldPo != null && this.primary != null && !this.oldPo.equals(var1)) {
            if (this.tDraw != null && !this.isSuspend) {
                this.suspendDraw();
            }

            Graphics var3 = this.primary;
            Dimension var4 = this.getSize();
            int var5 = this.scaleX;
            int var6 = this.scaleY;
            int var7 = var1.x - this.oldPo.x;
            int var8 = var1.y - this.oldPo.y;
            if (var2) {
                var7 = -var7;
                var8 = -var8;
            }

            this.oldPo.setLocation(var1);
            this.scaleX -= var7;
            this.scaleY -= var8;
            var3.setColor(this.getBackground());
            var3.fillRect(var5 * this.scale, var6 * this.scale, var4.width * this.scale, var4.height * this.scale);
            var3.setColor(this.getForeground());
            var3.drawRect(this.scaleX * this.scale, this.scaleY * this.scale, (this.imW - 1) * this.scale, (this.imH - 1) * this.scale);
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

    public synchronized void setSize(int var1, int var2) {
        if (var1 > 0 && var2 > 0 && (var1 != this.imW || var2 != this.imH)) {
            this.imW = var1;
            this.imH = var2;
            int var3 = var1 * (var2 + 1);
            if (this.i_off == null || this.i_off[0].length < var3) {
                this.i_off = new int[2][var3];
            }

            this.seek = 0;
            this.clearCanvas();
        }
    }

    public void setSpeed(int var1) {
        try {
            this.speed = var1;
            if (this.progress != null) {
                this.progress.repaint();
            }
        } catch (RuntimeException var3) {
            var3.printStackTrace();
        }

    }

    public synchronized void setVisit(int var1, boolean var2) {
        if (var1 > -1 && var1 < 2) {
            int var3 = var2 ? 255 : 0;
            if (var1 == 0) {
                this.visit0 = var3;
            } else {
                this.visit1 = var3;
            }

            this.repaint();
        }
    }

    public synchronized void stopLoad() {
        try {
            if (this.tLoad != null) {
                this.liveLoad = false;
                this.tLoad.join();
                this.tLoad = null;
            }
        } catch (InterruptedException var1) {
        }

    }

    public void suspendDraw() {
        this.isSuspend = true;
    }

    public void setScale(int var1, boolean var2) {
        var1 = var2 ? var1 : this.scale + var1;
        if (var1 > 0) {
            if (this.tDraw != null && !this.isSuspend) {
                this.suspendDraw();
            }

            this.scale = var1;
            Dimension var3 = this.getSize();
            this.scaleX = (var3.width - this.imW * this.scale) / 2 / this.scale;
            this.scaleY = (var3.height - this.imH * this.scale) / 2 / this.scale;
            this.repaint(0L);
        }
    }

    public void update_(Graphics var1) {
        this.paint(var1);
    }

    public void windowActivated(WindowEvent event) {
    }

    public void windowClosed(WindowEvent event) {
    }

    public void windowClosing(WindowEvent event) {
        try {
            this.destroyPCH();
            Component[] var2 = event.getWindow().getComponents();

            for (int var3 = 0; var3 < var2.length; ++var3) {
                if (var2[var3] instanceof PCHCanvas) {
                    ((PCHCanvas) var2[var3]).destroyPCH();
                }
            }

            event.getWindow().dispose();
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public void windowDeactivated(WindowEvent event) {
    }

    public void windowDeiconified(WindowEvent event) {
    }

    public void windowIconified(WindowEvent event) {
    }

    public void windowOpened(WindowEvent event) {
    }
}
