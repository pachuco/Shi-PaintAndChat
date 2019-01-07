package syi.awt;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import static java.awt.event.ComponentEvent.*;

public abstract class LComponent extends Canvas {
    protected static boolean isWin;
    private String title = null;
    public boolean isUpDown = false;
    public boolean isGUI = true;
    public boolean isHide = true;
    public boolean isEscape = false;
    protected boolean isFrame = true;
    protected boolean isPaint = true;
    protected boolean isRepaint = true;
    private Rectangle rEscape = null;
    private Dimension dSize = null;
    private Dimension dVisit = null;
    private Dimension dS = null;
    private Dimension dM = null;
    private Dimension dL = null;
    private Point pLocation = null;
    private boolean isMove = false;
    private boolean isResize = false;
    private int oldX = 0;
    private int oldY = 0;
    private int countResize = 0;
    public Color clFrame;
    public Color clLBar;
    public Color clBar;
    public Color clBarT;
    protected int iBSize;
    protected int iGap;
    private static Font fontBar = null;
    public static float Q;

    public LComponent() {
        if (Q == 0.0F) {
            this.setup();
        }

        this.iGap = Math.max((int) (4.0F * Q), 1);
        this.iBSize = Math.max((int) (12.0F * Q), 7);
        this.isRepaint = !isWin;
        Awt.getDef(this);
        this.clFrame = Awt.cF;
        this.enableEvents(COMPONENT_EVENT_MASK | KEY_EVENT_MASK | MOUSE_EVENT_MASK | MOUSE_MOTION_EVENT_MASK);
    }

    public void escape(boolean var1) {
        if (this.isEscape != var1) {
            this.isEscape = var1;
            if (var1) {
                this.rEscape = new Rectangle(this.getBounds());
                this.setBounds(0, 0, 1, 1);
            } else if (this.rEscape != null) {
                this.setBounds(this.rEscape);
                this.rEscape = null;
            }

        }
    }

    private Cursor getCur(int var1, int var2) {
        byte var3;
        switch (this.inCorner(var1, var2)) {
            case 1:
                var3 = 6;
                break;
            case 2:
                var3 = 7;
                break;
            case 3:
                var3 = 4;
                break;
            case 4:
                var3 = 5;
                break;
            default:
                var3 = 0;
        }

        return Cursor.getPredefinedCursor(var3);
    }

    public Graphics getG() {
        Graphics var1 = this.getGraphics();
        var1.translate(this.getGapX(), this.getGapY());
        Dimension var2 = this.getSize();
        var1.clipRect(0, 0, var2.width, var2.height);
        return var1;
    }

    public int getGapH() {
        return this.getGapY() + this.iGap;
    }

    public int getGapW() {
        return this.iGap * 2;
    }

    public int getGapX() {
        return this.iGap;
    }

    public int getGapY() {
        return this.iGap + (this.isGUI ? this.iBSize : 0);
    }

    public Point getLocation() {
        if (this.pLocation == null) {
            this.pLocation = super.getLocation();
        }

        return this.pLocation;
    }

    public Dimension getMaximumSize() {
        return this.dL == null ? this.getSize() : this.dL;
    }

    public Dimension getMinimumSize() {
        return this.dS == null ? this.getSize() : this.dS;
    }

    public Dimension getPreferredSize() {
        return this.dM == null ? this.getSize() : this.dM;
    }

    public Dimension getSize() {
        if (this.dVisit == null) {
            this.dVisit = new Dimension();
        }

        this.dVisit.setSize(this.getSizeW());
        Dimension var10000 = this.dVisit;
        var10000.width -= this.getGapW();
        var10000 = this.dVisit;
        var10000.height -= this.getGapH();
        return this.dVisit;
    }

    public Dimension getSizeW() {
        if (this.dSize == null) {
            this.dSize = super.getSize();
        }

        return this.dSize;
    }

    private int inCorner(int var1, int var2) {
        Dimension var3 = this.getSizeW();
        byte var4 = 0;
        int var5 = this.iGap;
        if (var2 <= var5) {
            if (var1 <= var5) {
                var4 = 1;
            }

            if (var1 >= var3.width - var5) {
                var4 = 2;
            }
        } else if (var2 >= var3.height - var5) {
            if (var1 <= var5) {
                var4 = 3;
            }

            if (var1 >= var3.width - var5) {
                var4 = 4;
            }
        }

        return var4;
    }

    public void inParent() {
        Container var1 = this.getParent();
        if (var1 != null) {
            Point var2 = this.getLocation();
            Dimension var3 = var1.getSize();
            Dimension var4 = this.getSizeW();
            int var5 = var2.x;
            int var6 = var2.y;
            int var7 = var4.width;
            int var8 = var4.height;
            var5 = var5 <= 0 ? 0 : (var5 + var7 >= var3.width ? var3.width - var7 : var5);
            var5 = var5 <= 0 ? 0 : var5;
            var6 = var6 <= 0 ? 0 : (var6 + var8 >= var3.height ? var3.height - var8 : var6);
            var6 = var6 <= 0 ? 0 : var6;
            if (var5 != var2.x || var6 != var2.y) {
                this.setLocation(var5, var6);
            }

            var7 = Math.min(var7, var3.width);
            var8 = Math.min(var8, var3.height);
            if (var7 != var4.width || var8 != var4.height) {
                this.setSize(var7 - this.getGapW(), var8 - this.getGapH());
            }

        }
    }

    public void paint(Graphics var1) {
        if (this.isVisible()) {
            Dimension var2 = this.getSizeW();
            if (!this.isPaint && this.isMove) {
                var1.drawRect(0, 0, var2.width - 1, var2.height - 1);
            } else {
                int var3 = this.iBSize;
                int var4 = this.iGap;
                int var10000 = var4 * 2;
                int var5 = var2.width;
                int var6 = var2.height;
                if (this.isFrame) {
                    var1.setColor(this.clFrame);
                    var1.drawRect(0, 0, var5 - 1, var6 - 1);
                }

                if (this.isGUI) {
                    var1.fillRect(1, var3, var5 - 2, 1);
                    var1.fillRect(var5 - var3 - 1, 1, 1, var3 - 1);
                    var1.setColor(this.clLBar);
                    var1.fillRect(1, 1, var5 - 2, 1);
                    var1.setColor(this.clBar);
                    var1.fillRect(1, 2, var5 - 2 - this.iBSize, this.iBSize - 2);
                    var1.drawLine(var5 - var3 + 1, 2, var5 - 2, var3 - 1);
                    var1.drawLine(var5 - var3 + 1, var3 - 1, var5 - 2, 1);
                    if (this.title != null && this.title.length() > 0) {
                        var1.setClip(1, 1, var5 - 1 - this.iBSize, this.iBSize - 1);
                        var1.setFont(fontBar);
                        var1.setColor(this.clBarT);
                        var1.drawString(this.title, var4, var3 - 1);
                        var1.setClip(0, 0, var2.width, var2.height);
                    }
                }

                int var7 = this.getGapX();
                int var8 = this.getGapY();
                var1.translate(var7, var8);

                try {
                    this.paint2(var1);
                } catch (Throwable var9) {
                }

                var1.translate(-var7, -var8);
            }
        }
    }

    public abstract void paint2(Graphics var1);

    public abstract void pMouse(MouseEvent var1);

    protected void processEvent(AWTEvent var1) {
        try {
            int var2 = var1.getID();
            Dimension var3 = this.getSizeW();
            Point var4 = this.getLocation();
            int var6;
            int var7;
            switch (var2) {
                case 100:
                    var4.setLocation(super.getLocation());
                    var7 = var4.x;
                    int var8 = var4.y;
                    this.inParent();
                    if (this.isRepaint) {
                        this.getParent().repaint(0L, var7, var8, var3.width, var3.height);
                    }
                    break;
                case 101:
                    var3.setSize(super.getSize());
                    int var5 = var3.width;
                    var6 = var3.height;
                    this.inParent();
                    if (this.isRepaint) {
                        this.getParent().repaint(0L, var4.x, var4.y, var5, var6);
                    }
            }

            if (var1 instanceof MouseEvent) {
                MouseEvent var16 = (MouseEvent) var1;
                var16.consume();
                var6 = var16.getX();
                var7 = var16.getY();
                if (this.isGUI) {
                    var3 = this.getSizeW();
                    Dimension var17 = this.getSize();
                    int var10000 = this.iGap * 2;
                    boolean var9 = false;
                    Dimension var11;
                    switch (var16.getID()) {
                        case 501:
                            this.oldX = var6;
                            this.oldY = var7;
                            int var18 = this.inCorner(var6, var7);
                            if (var18 != 0) {
                                this.isMove = true;
                                this.isResize = true;
                                this.isPaint = false;
                                this.countResize = 0;
                                return;
                            }

                            if (var7 <= this.iBSize) {
                                if (var6 >= var3.width - this.iBSize) {
                                    if (this.isHide) {
                                        this.setVisible(false);
                                    }

                                    return;
                                }

                                if (var16.getClickCount() % 2 == 0) {
                                    var11 = this.getMaximumSize();
                                    if (var17.equals(var11)) {
                                        this.setSize(this.getPreferredSize());
                                    } else {
                                        this.setSize(var11);
                                    }

                                    return;
                                }

                                this.isMove = true;
                                this.isResize = false;
                                this.isPaint = false;
                                return;
                            }
                            break;
                        case 502:
                            if (this.isMove) {
                                this.isMove = false;
                                var9 = true;
                                this.isPaint = true;
                            }
                            break;
                        case 503:
                            if (!this.getCursor().equals(this.getCur(var6, var7))) {
                                this.setCursor(this.getCur(var6, var7));
                            }

                            if (this.isUpDown) {
                                Container var10 = this.getParent();
                                if (var10.getComponent(0) != this) {
                                    var10.remove(this);
                                    var10.add(this, 0);
                                }
                            }
                        case 504:
                        case 505:
                        default:
                            break;
                        case 506:
                            if (this.isMove && ++this.countResize >= 4) {
                                var9 = true;
                                this.countResize = 0;
                            }
                    }

                    if (var9) {
                        Point var19 = this.getLocation();
                        var10000 = var19.x;
                        var10000 = var19.y;
                        var10000 = var17.width;
                        var10000 = var17.height;
                        Dimension var12;
                        if (this.isResize) {
                            var11 = this.dL;
                            var12 = this.dS;
                            int var13 = var17.width + (var6 - this.oldX);
                            int var14 = var17.height + (var7 - this.oldY);
                            this.setSize(var13 < var12.width ? var12.width : (var13 > var11.width ? var11.width : var13), var14 < var12.height ? var12.height : (var14 > var11.height ? var11.height : var14));
                            this.oldX = var6;
                            this.oldY = var7;
                        } else {
                            Point var20 = this.getLocation();
                            var12 = this.getParent().getSize();
                            var6 = var20.x + var6 - this.oldX;
                            var6 = var6 <= 0 ? 0 : (var6 + var3.width >= var12.width ? var12.width - var3.width : var6);
                            var7 = var20.y + var7 - this.oldY;
                            var7 = var7 <= 0 ? 0 : (var7 + var3.height >= var12.height ? var12.height - var3.height : var7);
                            this.setLocation(var6, var7);
                        }

                        if (this.isPaint) {
                            this.repaint();
                        }
                    }
                }

                var6 = this.getGapX();
                var7 = this.getGapY();
                if (!this.isMove) {
                    var16.translatePoint(-var6, -var7);
                    this.pMouse(var16);
                    var16.translatePoint(var6, var7);
                }
            }

            super.processEvent(var1);
        } catch (Throwable var15) {
        }

    }

    public void setDimension(Dimension var1, Dimension var2, Dimension var3) {
        var2 = var2 == null ? this.getSize() : var2;
        this.dS = var1 == null ? (this.dS == null ? new Dimension() : this.dS) : var1;
        this.dL = var3 == null ? (this.dL == null ? new Dimension(9999, 9999) : this.dL) : var3;
        this.dM = new Dimension(var2);
        this.setSize(var2.width, var2.height);
    }

    public void setLocation(int var1, int var2) {
        this.getLocation().setLocation(var1, var2);
        super.setLocation(var1, var2);
    }

    public void setSize(int var1, int var2) {
        Dimension var3 = this.getMaximumSize();
        Dimension var4 = this.getMinimumSize();
        var1 = var1 > var3.width ? var3.width : (var1 < var4.width ? var4.width : var1);
        var2 = var2 > var3.height ? var3.height : (var2 < var4.height ? var4.height : var2);
        Dimension var5 = this.getSize();
        if (var5.width != var1 || var5.height != var2) {
            var1 += this.getGapW();
            var2 += this.getGapH();
            this.getSizeW().setSize(var1, var2);
            super.setSize(var1, var2);
        }
    }

    public void setSize(Dimension var1) {
        this.setSize(var1.width, var1.height);
    }

    public void setTitle(String var1) {
        this.title = var1;
    }

    private void setup() {
        isWin = Awt.isWin();
        this.clFrame = Color.black;
        this.clBar = new Color(5592575);
        this.clLBar = this.clBar.brighter();
        this.clBarT = Color.white;
        Q = Awt.q();
        fontBar = new Font("sansserif", 0, (int) (10.0F * Q));
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
