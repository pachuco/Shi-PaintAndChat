package pch;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import static java.awt.event.MouseEvent.*;

public class PCHProgress extends Canvas {
    private PCHCanvas pch;
    private boolean isBuffer;
    private int selTool = -1;
    private int putTool = -1;
    private Rectangle[] rects = null;
    private Image image = null;
    private Graphics back = null;
    private Graphics primary = null;
    private Color clBack;
    private Color clIcon;
    private Color clFore;
    private Color clBar;
    private Color clFrame;
    private Color clSelect;
    public static boolean isJa;
    private static final Font font = new Font("sansserif", 0, 12);
    private static final int I_BODY = 0;
    private static final int I_PLAY = 1;
    private static final int I_STOP = 2;
    private static final int I_RESTART = 3;
    private static final int I_SPEED = 4;
    private static final int I_ADD = 5;
    private static final int I_SUB = 6;
    private static final int I_BAR = 7;
    private static final int I_SIZE_ICON = 20;
    public static final int I_SIZE_PRE = 26;

    public PCHProgress(boolean var1) {
        this.enableEvents(49L);
        this.isBuffer = var1;
    }

    public void action(Point var1) {
        if (this.rects != null) {
            int var2 = 1;

            int var3;
            for (var3 = this.rects.length; var2 < var3; ++var2) {
                if (this.rects[var2].contains(var1)) {
                    if (this.putTool != var2) {
                        this.putTool = var2;
                        this.drawIcon(var2);
                        return;
                    }
                    break;
                }
            }

            if (var2 < var3) {
                if (this.pch != null) {
                    switch (var2) {
                        case I_PLAY:
                            this.pch.playPCH();
                            break;
                        case I_STOP:
                            this.pch.suspendDraw();
                            break;
                        case I_RESTART:
                            this.pch.setMark(0);
                            this.pch.setMark(-1);
                            this.drawIcon(7);
                            break;
                        case I_SPEED:
                            int var4 = this.pch.getSpeed();
                            byte var5;
                            switch (var4) {
                                case 0:
                                    var5 = 10;
                                    break;
                                case 10:
                                    var5 = 20;
                                    break;
                                case 20:
                                    var5 = -1;
                                    break;
                                default:
                                    var5 = 0;
                            }

                            this.pch.setSpeed(var5);
                            break;
                        case I_ADD:
                            this.pch.setScale(1, false);
                            break;
                        case I_SUB:
                            this.pch.setScale(-1, false);
                    }

                }
            }
        }
    }

    public void drawBar() {
        this.drawIcon(I_BAR);
    }

    private void drawIcon(int iconNum) {
        if (this.isBuffer) {
            this.drawIcon(this.back, iconNum);
            if (this.rects != null && iconNum > 0 && iconNum < this.rects.length) {
                Rectangle rect = this.rects[iconNum];
                this.getPrimary().drawImage(this.image, rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, (ImageObserver) null);
            }
        } else {
            this.drawIcon(this.getPrimary(), iconNum);
        }

    }

    private synchronized void drawIcon(Graphics g, int iconNum) {
        try {
            if (this.rects == null || iconNum < 0 || iconNum >= this.rects.length || g == null) {
                return;
            }

            Rectangle rect = this.rects[iconNum];
            Color var4 = iconNum == 0 ? this.clBack : this.clIcon;
            var4 = iconNum == this.putTool ? var4.darker() : var4;
            if (iconNum != 7) {
                g.setColor(var4);
                g.fillRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
            }

            if (iconNum == this.selTool && iconNum != this.putTool || iconNum == 0) {
                g.setColor(var4.brighter());
                g.drawRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
            }

            g.setColor(iconNum == this.selTool ? this.clSelect : this.clFrame);
            g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
            int var5;
            int var6;
            int var7;
            int[] var10;
            int[] var12;
            switch (iconNum) {
                case I_PLAY:
                    var10 = new int[]{rect.x + 7, rect.x + 13, rect.x + 7};
                    var12 = new int[]{rect.y + 4, rect.y + 10, rect.y + 16};
                    g.setColor(this.clFore);
                    g.fillPolygon(var10, var12, var10.length);
                    g.setColor(this.clFore.brighter());
                    g.drawPolygon(var10, var12, var10.length);
                    break;
                case I_STOP:
                    g.setColor(this.clFore);
                    g.fillRect(rect.x + 5, rect.y + 6, 9, 9);
                    g.setColor(this.clFore.brighter());
                    g.drawRect(rect.x + 4, rect.y + 5, 10, 10);
                    break;
                case I_RESTART:
                    var10 = new int[]{rect.x + 8, rect.x + 2, rect.x + 8};
                    var12 = new int[]{var10[0] + 8, var10[1] + 8, var10[2] + 8};
                    int[] var13 = new int[]{rect.y + 4, rect.y + 10, rect.y + 16};
                    g.setColor(this.clFore);
                    g.fillPolygon(var10, var13, var10.length);
                    g.fillPolygon(var12, var13, var10.length);
                    g.setColor(this.clFore.brighter());
                    g.drawPolygon(var10, var13, var10.length);
                    g.drawPolygon(var12, var13, var10.length);
                    break;
                case I_SPEED:
                    String[][] var9 = new String[][]{{"Mx", "H", "M", "L"}, {"最", "早", "既", "鈍"}};
                    String[] var11 = isJa ? var9[1] : var9[0];
                    var7 = this.pch.getSpeed();
                    var7 = var7 < 0 ? 0 : (var7 == 0 ? 1 : (var7 <= 10 ? 2 : 3));
                    g.setColor(this.clFore);
                    g.drawString(var11[var7], rect.x + 4, rect.y + rect.height - 4);
                    break;
                case I_ADD:
                    var5 = rect.width / 2;
                    var6 = rect.height / 2;
                    g.fillRect(rect.x + var5 / 2, rect.y + var6 - 1, var5, 2);
                    g.fillRect(rect.x + var5 - 1, rect.y + var6 / 2, 2, var6);
                    break;
                case I_SUB:
                    var5 = rect.width / 2;
                    var6 = rect.height / 2;
                    g.fillRect(rect.x + var5 / 2, rect.y + var6 - 1, var5, 2);
                    break;
                case I_BAR:
                    var5 = this.pch.getLineCount();
                    var6 = this.pch.getSeek();
                    var7 = (int) ((float) rect.width * ((float) var6 / (float) var5)) - 5;
                    if (var7 <= 0) {
                        var7 = 1;
                    }

                    g.setColor(this.clBar);
                    g.fillRect(rect.x + 3, rect.y + 2, var7, rect.height - 4);
                    g.setColor(var4);
                    g.fillRect(rect.x + 3 + var7, rect.y + 2, rect.width - var7 - 5, rect.height - 4);
                    g.setColor(this.clFore);
                    g.fillRect(rect.x + Math.min(Math.max((int) ((float) this.pch.getMark() / (float) var5 * (float) rect.width), 3), rect.width - 3), rect.y + 2, 1, rect.height - 4);
                    g.drawString(String.valueOf(var6) + '/' + var5, rect.x + 3, rect.y + rect.height - 3);
            }
        } catch (RuntimeException var8) {
            var8.printStackTrace();
        }

    }

    protected void finalize() throws Throwable {
        try {
            if (this.image != null) {
                this.image.flush();
                this.back.dispose();
            }

            if (this.primary != null) {
                this.primary.dispose();
            }
        } catch (Throwable var1) {
        }

    }

    public Dimension getMinimumSize() {
        return new Dimension(I_SIZE_PRE, I_SIZE_PRE);
    }

    public Dimension getPreferredSize() {
        Container var1 = this.getParent();
        return var1 == null ? this.getMinimumSize() : new Dimension(var1.getSize().width, I_SIZE_PRE);
    }

    private Graphics getPrimary() {
        if (this.primary == null) {
            this.primary = this.getGraphics();
            if (this.primary == null) {
                return null;
            }

            this.primary.setFont(font);
        }

        return this.primary;
    }

    private void iPaint(Graphics g) {
        if (g != null) {
            for (int i = 0; i < this.rects.length; ++i) {
                this.drawIcon(g, i);
            }

        }
    }

    public void paint(Graphics g) {
        try {
            if (this.rects == null) {
                this.upRect();
                this.iPaint(this.back);
            }

            if (this.isBuffer) {
                Dimension var2 = this.getSize();
                g.drawImage(this.image, 0, 0, var2.width, var2.height, 0, 0, var2.width, var2.height, (ImageObserver) null);
            } else {
                g.setFont(font);
                this.iPaint(g);
            }
        } catch (Throwable var3) {
        }

    }

    protected void processComponentEvent(ComponentEvent cEv) {
        try {
            int var2 = cEv.getID();
            if (this.rects == null) {
                return;
            }

            if (var2 == COMPONENT_RESIZED) {
                this.upRect();
                this.iPaint(this.back);
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    protected void processMouseEvent(MouseEvent mEv) {
        try {
            int var2 = mEv.getID();
            Point var3 = mEv.getPoint();
            switch (var2) {
                case 501:
                    this.action(var3);
                    if (this.putTool == 7) {
                        this.setMark(var3);
                    }
                    break;
                case 502:
                    this.action(var3);
                    this.releaseIcon();
                    break;
                case 503:
                    this.selIcon(var3);
                case 504:
                case 505:
                default:
                    break;
                case 506:
                    if (this.putTool == 7) {
                        this.setMark(var3);
                    }
            }
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    protected void processMouseMotionEvent(MouseEvent var1) {
        this.processMouseEvent(var1);
    }

    private void releaseIcon() {
        int var1 = this.putTool;
        this.putTool = -1;
        this.drawIcon(var1);
    }

    private void selIcon(Point var1) {
        if (this.rects != null) {
            int var2 = this.selTool;
            int var3 = this.rects.length;

            int var4;
            for (var4 = 1; var4 < var3 && !this.rects[var4].contains(var1); ++var4) {
            }

            if (var4 >= var3) {
                var4 = -1;
            }

            if (this.selTool != var4) {
                this.selTool = var4;
                if (var2 != -1) {
                    this.drawIcon(var2);
                }

                if (var4 != -1) {
                    this.drawIcon(var4);
                }

            }
        }
    }

    public void setColor(Color var1, Color var2, Color var3, Color var4, Color var5, Color var6) {
        this.clBack = var1;
        this.clFore = var2;
        this.clIcon = var3;
        this.clBar = var4;
        this.clFrame = var5;
        this.clSelect = var6;
        this.setBackground(var1);
        this.setForeground(var2);
    }

    private synchronized void setMark(Point var1) {
        try {
            int var2 = var1.x - this.rects[7].x;
            int var3 = this.rects[7].width;
            int var4 = this.pch.getLineCount();
            var2 = var2 <= 0 ? 0 : (var2 >= var3 ? var4 : (int) ((float) var2 / (float) var3 * (float) var4));
            this.pch.setMark(var2);
            this.drawIcon(7);
        } catch (RuntimeException var5) {
            var5.printStackTrace();
        }

    }

    public void setPCHCanvas(PCHCanvas var1) {
        this.pch = var1;
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }

    private synchronized void upRect() {
        int var1;
        if (this.rects == null) {
            this.rects = new Rectangle[8];

            for (var1 = 0; var1 < this.rects.length; ++var1) {
                this.rects[var1] = new Rectangle();
            }
        }

        Dimension var2 = this.getSize();
        int var4 = 3;
        byte var5 = 3;

        for (var1 = 1; var1 < this.rects.length; ++var1) {
            this.rects[var1].setSize(I_SIZE_ICON, I_SIZE_ICON);
        }

        for (var1 = 1; var1 < this.rects.length; ++var1) {
            Rectangle var3 = this.rects[var1];
            var3.setLocation(var4, var5);
            var4 += 23;
        }

        this.rects[7].setSize(var2.width - this.rects[7].x - 3, I_SIZE_ICON);
        this.rects[0].setBounds(0, 0, var2.width, var2.height);
        if (this.isBuffer && (this.image == null || this.image.getWidth((ImageObserver) null) < var2.width || this.image.getHeight((ImageObserver) null) < var2.height)) {
            if (this.image != null) {
                this.image.flush();
                this.back.dispose();
            }

            this.image = this.createImage(var2.width, var2.height);
            this.back = this.image.getGraphics();
            this.back.setFont(font);
        }

    }
}
