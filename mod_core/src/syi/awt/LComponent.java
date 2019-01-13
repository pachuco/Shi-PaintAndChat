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
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

public abstract class LComponent extends Canvas {
    protected static boolean isWin;
    private String title = null;
    public boolean isUpDown = false;
    public boolean isGUI = true;
    public boolean isHide = true;
    public boolean isEscape = false;
    public boolean isBar = false;
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
        this.enableEvents(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
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

    private Cursor getCur(int x, int y) {
        byte cursor;
        switch (this.inCorner(x, y)) {
            case 1:
                cursor = Cursor.NW_RESIZE_CURSOR;
                break;
            case 2:
                cursor = Cursor.NE_RESIZE_CURSOR;
                break;
            case 3:
                cursor = Cursor.SW_RESIZE_CURSOR;
                break;
            case 4:
                cursor = Cursor.SE_RESIZE_CURSOR;
                break;
            default:
                cursor = Cursor.DEFAULT_CURSOR;
        }

        return Cursor.getPredefinedCursor(cursor);
    }

    public Graphics getG() {
        Graphics g = this.getGraphics();
        if (g == null) {
            return null;
        } else {
            g.translate(this.getGapX(), this.getGapY());
            Dimension var2 = this.getSize();
            g.clipRect(0, 0, var2.width, var2.height);
            return g;
        }
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
        this.dVisit.width -= this.getGapW();
        this.dVisit.height -= this.getGapH();
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

            if (var1 >= var3.width - var5 - 1) {
                var4 = 2;
            }
        } else if (var2 >= var3.height - var5 - 1) {
            if (var1 <= var5) {
                var4 = 3;
            }

            if (var1 >= var3.width - var5 - 1) {
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
            this.getMinimumSize();
            int var5 = var2.x;
            int var6 = var2.y;
            int var7 = var4.width;
            int var8 = var4.height;
            if (var3.width > 10 && var3.height > 10) {
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
    }

    public void paint(Graphics g) {
        if (this.isVisible()) {
            Dimension var2 = this.getSizeW();
            if (!this.isPaint && this.isMove) {
                g.drawRect(0, 0, var2.width - 1, var2.height - 1);
            } else {
                int var3 = this.iBSize;
                int var4 = this.iGap;
                int var10000 = var4 * 2;
                int var5 = var2.width;
                int var6 = var2.height;
                if (this.isFrame) {
                    g.setColor(this.clFrame);
                    g.drawRect(0, 0, var5 - 1, var6 - 1);
                }

                if (this.isGUI) {
                    g.fillRect(1, var3, var5 - 2, 1);
                    g.fillRect(var5 - var3 - 1, 1, 1, var3 - 1);
                    g.setColor(this.clLBar);
                    g.fillRect(1, 1, var5 - 2, 1);
                    g.setColor(this.clBar);
                    g.fillRect(1, 2, var5 - 2 - this.iBSize, this.iBSize - 2);
                    g.drawLine(var5 - var3 + 1, 2, var5 - 2, var3 - 1);
                    g.drawLine(var5 - var3 + 1, var3 - 1, var5 - 2, 1);
                    if (this.title != null && this.title.length() > 0) {
                        g.setClip(1, 1, var5 - 1 - this.iBSize, this.iBSize - 1);
                        g.setFont(fontBar);
                        g.setColor(this.clBarT);
                        g.drawString(this.title, var4, var3 - 1);
                        g.setClip(0, 0, var2.width, var2.height);
                    }
                }

                int var7 = this.getGapX();
                int var8 = this.getGapY();
                g.translate(var7, var8);

                try {
                    this.paint2(g);
                } catch (Throwable ex) {
                    ;
                }

                g.translate(-var7, -var8);
            }
        }
    }

    public abstract void paint2(Graphics g);

    public abstract void pMouse(MouseEvent event);

    protected void processEvent(AWTEvent awtEv) {
        try {
            Dimension var3 = this.getSizeW();
            Point var4 = this.getLocation();
            int mouseX;
            int mouseY;
            switch (awtEv.getID()) {
                case ComponentEvent.COMPONENT_MOVED:
                    var4.setLocation(super.getLocation());
                    mouseY = var4.x;
                    int var8 = var4.y;
                    this.inParent();
                    if (this.isRepaint) {
                        this.getParent().repaint(0L, mouseY, var8, var3.width, var3.height);
                    }
                    break;
                case ComponentEvent.COMPONENT_RESIZED:
                    var3.setSize(super.getSize());
                    int var5 = var3.width;
                    mouseX = var3.height;
                    this.inParent();
                    if (this.isRepaint) {
                        this.getParent().repaint(0L, var4.x, var4.y, var5, mouseX);
                    }
            }

            if (awtEv instanceof MouseEvent) {
                MouseEvent mEv = (MouseEvent) awtEv;
                mEv.consume();
                mouseX = mEv.getX();
                mouseY = mEv.getY();
                if (this.isGUI) {
                    var3 = this.getSizeW();
                    Dimension var21 = this.getSize();
                    int var9 = var21.width;
                    int var10 = var21.height;
                    int var10000 = this.iGap * 2;
                    boolean var11 = false;
                    Dimension var14;
                    switch (mEv.getID()) {
                        case MouseEvent.MOUSE_PRESSED:
                            this.oldX = mouseX;
                            this.oldY = mouseY;
                            if (this.inCorner(mouseX, mouseY) != 0) {
                                this.isMove = true;
                                this.isResize = true;
                                this.isPaint = false;
                                this.countResize = 0;
                                return;
                            }

                            Container parent = this.getParent();
                            if (mouseY <= this.iBSize) {
                                if (mouseX >= var3.width - this.iBSize) {
                                    if (this.isHide) {
                                        //this.setVisible(false);
                                        //tablet widget expects visibility change instead of removal
                                        parent.remove(this);
                                    }
                                    return;
                                }

                                if (mEv.getClickCount() % 2 != 0) {
                                    this.isMove = true;
                                    this.isResize = false;
                                    this.isPaint = false;
                                    return;
                                }

                                var14 = this.getMaximumSize();
                                Dimension var15 = this.getMinimumSize();
                                Dimension var16 = parent.getSize();
                                int var17 = Math.min(var14.width, var16.width - this.getGapW());
                                int var18 = Math.min(var14.height, var16.height - this.getGapH());
                                if (var9 >= var17 && var10 >= var18) {
                                    this.setSize(this.getPreferredSize());
                                } else {
                                    if (var9 <= var15.width && var10 >= var18) {
                                        var18 = var15.height;
                                    } else if (var10 <= var15.height && var9 >= var17) {
                                        var17 = var15.width;
                                    }

                                    this.setSize(var17, var18);
                                }

                                return;
                            }
                            break;
                        case MouseEvent.MOUSE_RELEASED:
                            if (this.isMove) {
                                this.isMove = false;
                                var11 = true;
                                this.isPaint = true;
                            }
                            break;
                        case MouseEvent.MOUSE_MOVED:
                            if (!this.getCursor().equals(this.getCur(mouseX, mouseY))) {
                                this.setCursor(this.getCur(mouseX, mouseY));
                            }

                            if (this.isUpDown) {
                                Container var12 = this.getParent();
                                if (var12.getComponent(0) != this) {
                                    var12.remove(this);
                                    var12.add(this, 0);
                                }
                            }
                        case MouseEvent.MOUSE_ENTERED:
                        case MouseEvent.MOUSE_EXITED:
                        default:
                            break;
                        case MouseEvent.MOUSE_DRAGGED:
                            if (this.isMove && ++this.countResize >= 4) {
                                var11 = true;
                                this.countResize = 0;
                            }
                    }

                    if (var11) {
                        Point var23 = this.getLocation();
                        var10000 = var23.x;
                        var10000 = var23.y;
                        var10000 = var21.width;
                        var10000 = var21.height;
                        if (this.isResize) {
                            Dimension var24 = this.dL;
                            var14 = this.dS;
                            int var26 = var21.width + (mouseX - this.oldX);
                            int var27 = var21.height + (mouseY - this.oldY);
                            this.setSize(var26 < var14.width ? var14.width : (var26 > var24.width ? var24.width : var26), var27 < var14.height ? var14.height : (var27 > var24.height ? var24.height : var27));
                            this.oldX = mouseX;
                            this.oldY = mouseY;
                        } else {
                            Point var25 = this.getLocation();
                            var14 = this.getParent().getSize();
                            mouseX = var25.x + mouseX - this.oldX;
                            mouseX = mouseX <= 0 ? 0 : (mouseX + var3.width >= var14.width ? var14.width - var3.width : mouseX);
                            mouseY = var25.y + mouseY - this.oldY;
                            mouseY = mouseY <= 0 ? 0 : (mouseY + var3.height >= var14.height ? var14.height - var3.height : mouseY);
                            this.setLocation(mouseX, mouseY);
                        }

                        if (this.isPaint) {
                            this.repaint();
                        }
                    }
                }

                mouseX = this.getGapX();
                mouseY = this.getGapY();
                if (!this.isMove) {
                    mEv.translatePoint(-mouseX, -mouseY);
                    this.pMouse(mEv);
                    mEv.translatePoint(mouseX, mouseY);
                }
            }

            super.processEvent(awtEv);
        } catch (Throwable ex) {
            ;
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
