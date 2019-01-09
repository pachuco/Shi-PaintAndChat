package paintchat.normal;

import jaba.applet.Applet;

import java.awt.AWTEvent;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.StringReader;

import paintchat.LO;
import paintchat.M;
import paintchat.Res;
import paintchat.SW;
import paintchat.ToolBox;
import paintchat_client.L;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

public class Tools extends LComponent implements ToolBox, ActionListener {
    Applet applet;
    Container parent;
    Mi mi;
    private L L;
    private boolean is_l;
    M.Info info;
    M mg;
    Res res;
    Res config;
    private Graphics primary = null;
    private Font fDef;
    private Font fIg;
    ToolList[] list;
    private Rectangle rPaint = new Rectangle();
    private Rectangle[] rects = null;
    private int fit_w = -1;
    private int fit_h = -1;
    private int nowButton = -1;
    private int nowColor = -1;
    private int oldPen;
    Color clFrame;
    Color clB;
    Color clBD;
    Color clBL;
    Color clText;
    Color clBar;
    Color clB2;
    Color clSel;
    private boolean isWest = false;
    private boolean isLarge;
    private boolean isVisible = true;
    private PopupMenu popup;
    private LComponent[] cs;
    private Window[] ws;
    private static int[] DEFC = new int[]{
            0x000000, 0xFFFFFF, 0xb47575, 0x888888, 0xfa9696, 0xc096c0, 0xffb6ff, 0x8080ff, 0x25c7c9,0xe7e58d, 0xe7962d, 0x99cb7b, 0xfcece2, 0xf9ddcf};
    private static int[] COLORS = new int[14];
    private static Color[][] clRGB;
    private static Color[][] clERGB;
    private final char[][] clV = new char[][]{{'H', 'S', 'B', 'A'}, {'R', 'G', 'B', 'A'}};
    private boolean isRGB = true;
    private float[] fhsb = new float[3];
    private int iColor;
    protected Image imBack = null;
    private Graphics back;
    protected int W;
    protected int H;
    protected int IMW;
    protected int IMH;

    static {
        System.arraycopy(DEFC, 0, COLORS, 0, 14);
        clRGB = new Color[][]{{Color.magenta, Color.cyan, Color.white, Color.lightGray}, {new Color(16422550), new Color(8581688), new Color(8421631), Color.lightGray}};
        clERGB = new Color[2][4];

        for (int var0 = 0; var0 < 2; ++var0) {
            for (int var1 = 0; var1 < 4; ++var1) {
                clERGB[var0][var1] = clRGB[var0][var1].darker();
            }
        }

    }

    public Tools() {
        this.setTitle("Toolbar");
        super.isHide = false;
        super.iGap = 2;
    }

    public void actionPerformed(ActionEvent evt) {
        String var2 = evt.getActionCommand();
        Menu menu = (Menu) evt.getSource();
        int penMask = 0;
        int count = menu.getItemCount();

        for (int i = 0; i < count; ++i) {
            if (menu.getItem(i).getLabel().equals(var2)) {
                penMask = i;
                break;
            }
        }

        switch (Integer.parseInt(menu.getLabel())) {
            case 2:
                this.mg.iPenM = penMask;
                this.setLineSize(0, this.mg.iSize);
                this.repaint();
            default:
        }
    }

    public void addC(Object var1) {
        int var2;
        if (var1 instanceof LComponent) {
            if (this.cs == null) {
                this.cs = new LComponent[]{(LComponent) var1};
                return;
            }

            for (var2 = 0; var2 < this.cs.length; ++var2) {
                if (this.cs[var2] == var1) {
                    return;
                }
            }

            var2 = this.cs.length;
            LComponent[] var3 = new LComponent[var2 + 1];
            System.arraycopy(this.cs, 0, var3, 0, var2);
            var3[var2] = (LComponent) var1;
            this.cs = var3;
        } else {
            if (this.ws == null) {
                this.ws = new Window[]{(Window) var1};
                return;
            }

            for (var2 = 0; var2 < this.ws.length; ++var2) {
                if (this.ws[var2] == var1) {
                    return;
                }
            }

            var2 = this.ws.length;
            Window[] var4 = new Window[var2 + 1];
            System.arraycopy(this.cs, 0, var4, 0, var2);
            var4[var2] = (Window) var1;
            this.ws = var4;
        }

    }

    public Graphics getBack() {
        if (this.imBack == null) {
            synchronized (this) {
                if (this.imBack == null) {
                    try {
                        int var2 = 0;

                        for (int var3 = 0; var3 < this.list.length; ++var3) {
                            var2 = Math.max(var2, this.list[var3].r.height);
                        }

                        var2 = Math.max(var2, 32) * 2;
                        this.imBack = this.createImage(this.W + 1, var2 + 1);
                        this.back = this.imBack.getGraphics();
                    } catch (RuntimeException var4) {
                        this.imBack = null;
                        this.back = null;
                    }
                }
            }
        }

        if (this.back != null) {
            this.back.setFont(this.fDef);
        }

        return this.back;
    }

    public String getC() {
        try {
            int[] var1 = COLORS == null ? DEFC : COLORS;
            StringBuffer var2 = new StringBuffer();

            for (int var3 = 0; var3 < var1.length; ++var3) {
                if (var3 != 0) {
                    var2.append('\n');
                }

                var2.append("#" + Integer.toHexString(0xFF000000 | var1[var3] & 0xFFFFFF).substring(2).toUpperCase());
            }

            return var2.toString();
        } catch (Throwable var4) {
            return null;
        }
    }

    public LComponent[] getCs() {
        return this.cs;
    }

    public Dimension getCSize() {
        Dimension var1 = this.parent.getSize();
        return new Dimension(var1.width - this.getSizeW().width - this.mi.getGapW(), var1.height);
    }

    private int getRGB() {
        return !this.isRGB ? Color.HSBtoRGB((float) (this.iColor >>> 16 & 255) / 255.0F, (float) (this.iColor >>> 8 & 255) / 255.0F, (float) (this.iColor & 255) / 255.0F) & 0xFFFFFF : this.iColor & 0xFFFFFF;
    }

    private int i(String var1, int var2) {
        return this.config.getP(var1, var2);
    }

    public boolean i(String var1, boolean var2) {
        return this.config.getP(var1, var2);
    }

    public void init(Container var1, Applet var2, Res var3, Res var4, Mi var5) {
        this.applet = var2;
        this.parent = var1;
        this.res = var4;
        this.config = var3;
        this.info = var5.info;
        this.mi = var5;
        this.mg = this.info.m;
        this.W = this.i("tool_width", 48) + 4;
        this.H = this.i("tool_height", 470);

        for (int var7 = 0; var7 < DEFC.length; var7 += 2) {
            DEFC[var7] = var3.getP("color_" + (var7 + 2), DEFC[var7]);
            DEFC[var7 + 1] = var3.getP("color_" + (var7 + 1), DEFC[var7 + 1]);
        }

        System.arraycopy(DEFC, 0, COLORS, 0, 14);
        this.sCMode(false);
        String var6 = "tool_color_";
        this.setBackground(new Color(this.i(var6 + "bk", this.i(var6 + "back", 10066363))));
        this.clB = new Color(this.i(var6 + "button", -1515602));
        this.clB2 = new Color(this.i(var6 + "button" + '2', 16308906));
        this.clFrame = new Color(this.i(var6 + "frame", 0));
        this.clText = new Color(this.i(var6 + "text", 7811891));
        this.clBar = new Color(this.i(var6 + "bar", 14540287));
        this.clSel = new Color(this.i(var6 + "iconselect", this.i("color_iconselect", 15610675)));
        this.clBL = new Color(this.i(var6 + "button" + "_hl", this.clB.brighter().getRGB()));
        this.clBD = new Color(this.i(var6 + "button" + "_dk", this.clB.darker().getRGB()));
        this.isWest = "left".equals(var3.getP("tool_align"));
        this.isLarge = var3.getP("icon_enlarge", true);
        this.is_l = var3.getP("tool_layer", true);
        this.setDimension(new Dimension(this.W, 42), new Dimension(this.W, this.H), new Dimension(this.W, (int) ((float) this.H * 1.25F)));
        this.list[0].select();
        this.addC(this);
        var1.add(this, 0);
    }

    /** Returns id of the clicked tool or -1 if empty */
    private int isClick(int x, int y) {
        if (this.rects == null) {
            return -1;
        } else {
            int rectsCount = this.rects.length;
            int toolsCount = this.list.length;

            int i;
            for (i = 0; i < toolsCount; ++i) {
                if (this.list[i].r.contains(x, y)) {
                    return i;
                }
            }

            for (i = 0; i < rectsCount; ++i) {
                Rectangle rect = this.rects[i];
                if (rect != null && rect.contains(x, y)) {
                    return i + toolsCount;
                }
            }

            return -1;
        }
    }

    public void lift() {
        this.unSelect();
        this.nowButton = -1;
        this.repaint();
    }

    private void makeList() {
        Image var3 = null;
        int var4 = 0;
        int var5 = 0;

        try {
            String var2 = "res/s.gif";
            Image var6 = this.getToolkit().createImage((byte[]) this.config.getRes(var2));
            Awt.wait(var6);
            var3 = var6;
            this.config.remove(var2);
            int var7 = this.i("tool_icon_count", 7);
            this.list = new ToolList[var7];
            var4 = var6.getWidth((ImageObserver) null) / var7;
            var5 = this.i("tool_icon_height", var6.getHeight((ImageObserver) null) / 9);
            this.IMW = var4;
            this.IMH = var5;
        } catch (RuntimeException var8) {
            ;
        }

        for (int var9 = 0; var9 < this.list.length; ++var9) {
            ToolList var1;
            this.list[var9] = var1 = new ToolList();
            var1.init(this, this.res, this.config, this.mg, this.list, var9);
            var1.setImage(var3, var4, var5, var9);
        }

    }

    private void menu(int var1, int var2, int var3) {
        if (this.popup == null) {
            this.popup = new PopupMenu(String.valueOf(var3));
            this.popup.addActionListener(this);
        } else {
            this.remove(this.popup);
            this.popup.removeAll();
            this.popup.setLabel(String.valueOf(var3));
        }

        String var4;
        int var5;
        int var6;
        label60:
        switch (var3) {
            case 0:
                var5 = 0;

                while (true) {
                    if (var5 >= 11) {
                        break label60;
                    }

                    var4 = String.valueOf(var5 == 0 ? 5 : var5 * 10) + '%';
                    if (this.mg.iTT - 1 == var5) {
                        this.popup.add((MenuItem) (new CheckboxMenuItem(var4, true)));
                    } else {
                        this.popup.add(var4);
                    }

                    ++var5;
                }
            case 1:
                var5 = this.config.getInt("tt_size");
                var6 = 0;

                while (true) {
                    if (var6 >= var5) {
                        break label60;
                    }

                    var4 = this.res.res("t042" + var6);
                    if (this.mg.iTT - 12 == var6) {
                        this.popup.add((MenuItem) (new CheckboxMenuItem(var4, true)));
                    } else {
                        this.popup.add(var4);
                    }

                    ++var6;
                }
            case 2:
                for (var6 = 0; var6 < 16; ++var6) {
                    var4 = (String) this.res.get("penm_" + var6);
                    if (var4 == null) {
                        break;
                    }

                    if (this.mg.iPenM == var6) {
                        this.popup.add((MenuItem) (new CheckboxMenuItem(var4, true)));
                    } else {
                        this.popup.add(var4);
                    }
                }
        }

        this.add(this.popup);
        this.popup.show(this, var1, var2);
    }

    public void mPaint(int var1) {
        Rectangle var2;
        if (var1 == -1) {
            var2 = this.rPaint;
            var2.setSize(this.getSize());
            var2.setLocation(0, 0);
        } else if (var1 < this.list.length) {
            var2 = this.rPaint;
            var2.setBounds(this.list[var1].r);
        } else {
            var2 = this.rects[var1 - this.list.length];
        }

        this.mPaint(this.primary(), var2);
    }

    public void mPaint(int var1, int var2, int var3, int var4) {
        Rectangle var5 = this.rPaint;
        var5.setBounds(var1, var2, var3, var4);
        this.mPaint(this.primary(), var5);
    }

    private void mPaint(Graphics var1, Rectangle var2) {
        if (this.rects != null && var1 != null && this.list != null) {
            Graphics var3 = this.getBack();
            if (var2 == null) {
                var2 = var1.getClipBounds();
                if (var2 == null || var2.isEmpty()) {
                    var2 = new Rectangle(this.getSize());
                }
            }

            if (!var2.isEmpty()) {
                int var6 = this.list.length;
                Dimension var7 = this.getSize();
                var3.setFont(this.fDef);

                int var4;
                for (var4 = 0; var4 < var6; ++var4) {
                    if (this.list[var4].r.intersects(var2)) {
                        this.list[var4].paint(var1, var3);
                    }
                }

                var3.setFont(this.fIg);
                int var9 = this.isRGB ? 1 : 0;

                for (var4 = 0; var4 < this.rects.length; ++var4) {
                    Rectangle var8 = this.rects[var4];
                    int var5 = var4 + var6;
                    if (var8.intersects(var2)) {
                        if (var4 < 14) {
                            Color var10 = new Color(COLORS[var4]);
                            var3.setColor(var4 == this.nowColor ? var10.darker() : var10.brighter());
                            var3.drawRect(1, 1, var8.width - 2, var8.height - 2);
                            var3.setColor(var10);
                            var3.fillRect(2, 2, var8.width - 3, var8.height - 3);
                            var3.setColor(this.nowColor == var4 ? this.clSel : this.clFrame);
                        } else {
                            int var12;
                            int var13;
                            int var14;
                            switch (var4) {
                                case 18:
                                    boolean var17 = this.mg.isText();
                                    Color var11 = new Color(this.getRGB());
                                    var12 = var17 ? 255 : this.info.getPMMax();
                                    var13 = var8.width - 10;
                                    var14 = var8.height - 2;
                                    var3.setColor(this.clB2);
                                    var3.fillRect(1, 1, var8.width - 2, var14);
                                    if (this.mg.iSize >= var12) {
                                        this.mg.iSize = var12 - 1;
                                    }

                                    var3.setColor(var11);
                                    var3.fillRect(1, 1, var13, (int) ((float) (this.mg.iSize + 1) / (float) var12 * (float) var14));
                                    if (this.info.getPenMask() == null) {
                                        return;
                                    }

                                    var3.setColor(this.clText);
                                    var3.drawString((String) String.valueOf(this.mg.iSize), 6, var14 - 1);
                                    var3.setColor(this.clFrame);
                                    var3.fillRect(var13, 1, 1, var14);
                                    var3.fillRect(var13 + 1, var14 / 2, 8, 1);
                                    var3.setColor(var11);

                                    for (int var15 = 3; var15 >= 1; --var15) {
                                        var3.fillRect(var8.width - 5 - var15, var15 + 2, var15 << 1, 1);
                                        var3.fillRect(var8.width - 5 - var15, var14 - 2 - var15, var15 << 1, 1);
                                    }

                                    var3.fillRect(var8.width - 6, 5, 2, 8);
                                    var3.fillRect(var8.width - 6, var14 - 11, 2, 8);
                                    break;
                                case 19:
                                    var3.setColor(this.clBar);
                                    var3.fillRect(1, 1, var8.width - 1, var8.height - 2);
                                    if (this.info.layers != null && this.info.layers.length > this.mg.iLayer) {
                                        LO var16 = this.info.layers[this.mg.iLayer];
                                        var3.setColor(this.clText);
                                        if (var16.name != null) {
                                            var3.drawString((String) var16.name, 2, var8.height - var3.getFontMetrics().getMaxDescent() - 1);
                                        }

                                        if (var16.iAlpha == 0.0F) {
                                            var3.setColor(Color.red);
                                            var3.drawLine(1, 1, var8.width - 3, var8.height - 3);
                                        }
                                    }
                                    break;
                                default:
                                    int var18 = var4 - 14;
                                    int var19 = var8.height;
                                    var12 = var4 == 17 ? this.mg.iAlpha : this.iColor >>> (2 - var18) * 8 & 255;
                                    var13 = (int) ((float) (var7.width - 10 - 2) / 255.0F * (float) var12);
                                    var3.setColor(this.clB2);
                                    var3.fillRect(0, 0, 5, var19 - 1);
                                    var3.fillRect(var8.width - 5, 1, 5, var19 - 1);
                                    var3.setColor(this.clFrame);
                                    var3.fillRect(5, 1, 1, var19 - 1);
                                    var3.fillRect(var8.width - 5 - 1, 1, 1, var19 - 1);
                                    if (var13 > 0) {
                                        var3.setColor(clRGB[var9][var18]);
                                        var3.fillRect(6, 1, var13, var8.height - 2);
                                    }

                                    var14 = var8.width - 10 - var13 - 2;
                                    if (var14 > 0) {
                                        var3.setColor(this.clBar);
                                        var3.fillRect(var13 + 5 + 1, 1, var14, var8.height - 2);
                                        var3.setColor(clERGB[var9][var18]);
                                        var3.fillRect(var13 + 5 + 1, 1, 1, var8.height - 2);
                                    }

                                    var3.setColor(this.clText);
                                    var3.drawString((String) (String.valueOf(this.clV[var9][var18]) + var12), 8, var8.height - 2);
                            }

                            var3.setColor(this.nowButton == var5 ? this.clSel : this.clFrame);
                        }

                        var3.drawRect(0, 0, var8.width - 1, var8.height - 1);
                        var1.drawImage(this.imBack, var8.x, var8.y, var8.x + var8.width, var8.y + var8.height, 0, 0, var8.width, var8.height, Color.white, (ImageObserver) null);
                    }
                }

            }
        }
    }

    private void mPress(MouseEvent event) {
        int mouseX = event.getX();
        int mouseY = event.getY();
        int var4 = this.isClick(mouseX, mouseY);
        int var5 = var4;
        boolean var6 = Awt.isR(event);
        this.nowButton = var4;
        if (var4 >= 0) {
            if (var4 - this.list.length < 0) {
                if (var6) {
                    if (this.list[var4].isField && this.list[var4].isMask) {
                        this.mg.iColorMask = this.mg.iColor;
                        this.mPaint(var4);
                    }

                    this.nowButton = -1;
                }

            } else {
                var4 -= this.list.length;
                Rectangle var7 = this.rects[var4];
                if (var4 - 14 < 0) {
                    if (var6) {
                        COLORS[var4] = this.mg.iColor;
                    } else if (event.isShiftDown()) {
                        COLORS[var4] = DEFC[var4];
                    } else {
                        this.nowColor = var4;
                        this.mg.iColor = COLORS[this.nowColor];
                        this.selPix(false);
                        this.toColor(this.mg.iColor);
                    }

                    this.up();
                } else {
                    var4 -= 14;
                    int var8;
                    if (var4 - 4 < 0) {
                        var8 = mouseX <= 5 ? -1 : (mouseX >= var7.width - 5 ? 1 : 0);
                        if (var8 != 0) {
                            this.nowButton = -1;
                            if (var6) {
                                var8 *= 5;
                            }
                        } else if (var6) {
                            this.sCMode(this.isRGB);
                            this.nowButton = -1;
                            this.mPaint(-1);
                            return;
                        }

                        this.setRGB(var4, var8, mouseX);
                    } else {
                        var4 -= 4;
                        if (var4 - 1 < 0) {
                            if (var6) {
                                this.nowButton = -1;
                                this.menu(mouseX, mouseY, 2);
                            } else {
                                if (mouseX >= var7.x + var7.width - 10) {
                                    this.setLineSize(0, Math.max(this.mg.iSize + ((var7.y + var7.height - mouseY) / 2 >= 10 ? -1 : 1), 0));
                                    this.nowButton = -1;
                                } else {
                                    this.setLineSize(mouseY, -1);
                                }

                                this.mPaint(var5);
                            }
                        } else {
                            --var4;
                            if (var4 - 1 < 0) {
                                var8 = this.mg.iLayer;
                                if (var6) {
                                    LO var9 = this.info.layers[var8];
                                    var9.iAlpha = (float) (var9.iAlpha == 0.0F ? 1 : 0);
                                    this.mi.repaint();
                                } else {
                                    if (this.L == null) {
                                        this.L = new L(this.mi, this, this.res, this.config);
                                        this.addC(this.L);
                                    }

                                    if (this.L.getParent() == null) {
                                        this.L.setVisible(this.isVisible);
                                        this.parent.add(this.L, 0);
                                    } else {
                                        if (++this.mg.iLayer >= this.info.L) {
                                            this.mg.iLayer = 0;
                                        }

                                        this.mg.iLayerSrc = this.mg.iLayer;
                                    }
                                }

                                if (this.L != null) {
                                    this.L.repaint();
                                }

                                this.mPaint(var5);
                            }
                        }
                    }
                }
            }
        }
    }

    public void pack() {
        try {
            Container var1 = this.parent;
            Dimension var2 = var1.getSize();
            this.setSize(Math.min(this.W, var2.width), Math.min(this.H, var2.height));
            if (this.L != null) {
                this.L.inParent();
            }

            Dimension var3 = this.getCSize();
            Dimension var4 = this.getSizeW();
            if (!this.mi.isGUI) {
                this.mi.setDimension((Dimension) null, var3, var3);
                var3 = this.mi.getSizeW();
                this.mi.setLocation((var2.width - var4.width - var3.width) / 2 + (this.isWest ? var4.width : 0), (var2.height - var3.height) / 2);
            }

            var3 = this.mi.getSizeW();
            Point var5 = this.mi.getLocation();
            this.setLocation(this.isWest ? Math.max(0, var5.x - var4.width - 10) : Math.min(var5.x + var3.width + 10, var2.width - var4.width), (var2.height - var4.height) / 2);
            if (this.cs != null) {
                for (int var6 = 2; var6 < this.cs.length; ++var6) {
                    ((SW) this.cs[var6]).mPack();
                }
            }
        } catch (Throwable var7) {
            ;
        }

    }

    public void paint2(Graphics var1) {
        try {
            var1.setFont(this.fDef);
            this.mPaint(var1, var1.getClipBounds());
            if (this.primary != null) {
                this.primary.dispose();
                this.primary = null;
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void pMouse(MouseEvent var1) {
        try {
            if (this.rects == null) {
                return;
            }

            int var2 = var1.getX();
            int var3 = var1.getY();
            boolean var4 = Awt.isR(var1);
            int var5;
            if (this.list != null) {
                for (var5 = 0; var5 < this.list.length; ++var5) {
                    if (!this.list[var5].isMask || !var4) {
                        this.list[var5].pMouse(var1);
                    }
                }
            }

            var5 = var1.getID();
            switch (var5) {
                case MouseEvent.MOUSE_PRESSED:
                    this.mPress(var1);
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    this.nowButton = -1;
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                default:
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    int var6 = this.nowButton;
                    if (var6 < this.list.length) {
                        return;
                    }

                    var6 -= this.list.length;
                    if (var6 - 14 < 0) {
                        return;
                    }

                    var6 -= 14;
                    if (var6 - 4 < 0) {
                        this.setRGB(var6, 0, var2);
                        this.mPaint(-1);
                        this.upCS();
                        return;
                    }

                    var6 -= 4;
                    if (var6 - 1 < 0) {
                        this.setLineSize(var3, -1);
                        this.mPaint(this.nowButton);
                        return;
                    }
            }
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

    }

    public Graphics primary() {
        if (this.primary == null) {
            this.primary = this.getG();
        }

        return this.primary;
    }

    protected void processEvent(AWTEvent var1) {
        switch (var1.getID()) {
            case ComponentEvent.COMPONENT_MOVED:
                int var2 = this.getLocation().x;
                int var3 = this.getParent().getSize().width / 2 - this.getSize().width / 2;
                if (var2 < var3 && !this.isWest) {
                    this.isWest = true;
                    this.pack();
                } else if (var2 >= var3 && this.isWest) {
                    this.isWest = false;
                    this.pack();
                }
                break;
            case ComponentEvent.COMPONENT_RESIZED:
            case ComponentEvent.COMPONENT_SHOWN:
                if (this.primary != null) {
                    this.primary.dispose();
                    this.primary = null;
                }
        }

        super.processEvent(var1);
    }

    private void sCMode(boolean var1) {
        this.isRGB = !var1;
        this.toColor(this.mg.iColor);
    }

    public void selPix(boolean var1) {
        if (this.list != null) {
            int var2 = this.list.length;
            ToolList var4 = null;
            ToolList var5 = null;

            for (int var6 = 0; var6 < var2; ++var6) {
                ToolList var3 = this.list[var6];
                if (var3.isEraser) {
                    var5 = var3;
                }

                if (var3.isSelect) {
                    var4 = var3;
                }
            }

            if (var1) {
                if (var4 != var5) {
                    this.unSelect();
                    var5.select();
                    this.mPaint(-1);
                }
            } else if (var4 == var5) {
                this.unSelect();
                this.list[this.oldPen].select();
                this.mPaint(-1);
            }

        }
    }

    public void setARGB(int var1) {
        this.mg.iAlpha = var1 >>> 24;
        var1 &= 0xFFFFFF;
        this.mg.iColor = var1;
        this.toColor(var1);
        this.mPaint(-1);
        this.upCS();
    }

    public void setC(String var1) {
        try {
            BufferedReader var2 = new BufferedReader(new StringReader(var1));

            for (int var3 = 0; (var1 = var2.readLine()) != null && var1.length() > 0; DEFC[var3++] = Integer.decode(var1)) {
                ;
            }

            System.arraycopy(DEFC, 0, COLORS, 0, COLORS.length);
            this.repaint();
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public void setLineSize(int var1) {
        this.setLineSize(0, Math.max(var1, 0));
        this.mPaint(this.list.length + 5);
    }

    public void setLineSize(int var1, int var2) {
        int var3 = this.info.getPMMax();
        Rectangle var4 = this.rects[18];
        if (var2 == -1) {
            var2 = (int) ((float) (var1 - var4.y) / (float) var4.height * (float) var3);
        }

        var2 = var2 <= 0 ? 0 : (var2 >= var3 ? var3 - 1 : var2);
        this.mg.iSize = var2;
        this.upCS();
    }

    private void setRGB(int var1, int var2, int var3) {
        int var5 = var1 == 3 ? 24 : (2 - var1) * 8;
        int var6 = this.mg.iAlpha << 24 | this.iColor;
        int var4;
        if (var2 != 0) {
            var4 = var6 >>> var5 & 255;
            var4 += var2;
        } else {
            Rectangle var7 = this.rects[14 + var1];
            var3 = var3 - var7.x - 4;
            var4 = var3 > 0 ? (int) ((float) var3 / (float) (this.W - 8) * 255.0F) : 0;
        }

        var4 = var4 <= 0 ? 0 : (var4 >= 255 ? 255 : var4);
        int var8 = 255 << var5;
        var8 = ~var8;
        var6 = var6 & var8 | var4 << var5;
        this.iColor = var6 & 0xFFFFFF;
        this.mg.iColor = this.getRGB();
        this.mg.iAlpha = Math.max(var6 >>> 24, 1);
        this.mPaint(var1);
        if (this.nowColor >= 0) {
            COLORS[this.nowColor] = this.mg.iColor;
        }

        this.mPaint(-1);
        this.upCS();
    }

    public void setSize(int var1, int var2) {
        if (this.applet == null) {
            super.setSize(var1, var2);
        } else if (var1 != this.fit_w || var2 != this.fit_h) {
            synchronized (this) {
                this.fit_w = var1;
                this.fit_h = var2;
                if (this.list == null) {
                    this.makeList();
                }

                int var3;
                if (this.rects == null) {
                    this.rects = new Rectangle[20];

                    for (var3 = 0; var3 < this.rects.length; ++var3) {
                        this.rects[var3] = new Rectangle();
                    }
                }

                Rectangle[] var5 = this.rects;
                float var6 = (float) var2 / (float) this.H;
                int var7 = (int) ((float) (this.IMH + 4) * var6);
                if (!this.isLarge) {
                    var7 = Math.min(this.IMH + 4, var7);
                }

                int var8 = Math.min((var2 - (var7 + 1) * this.list.length - (int) (16.0F * var6 * 4.0F) - (int) (33.0F * var6) - 3) / 8, (var1 - 1) / 2);
                this.fIg = new Font("sansserif", 0, (int) ((float) var8 * 0.475F));
                this.fDef = new Font("sansserif", 0, (int) ((float) var7 * 0.43F));
                FontMetrics var9 = this.getFontMetrics(this.fDef);
                int var10 = var7 - var9.getMaxDescent() - 2;
                boolean var11 = false;
                int var12 = 0;

                for (var3 = 0; var3 < this.list.length; ++var3) {
                    this.list[var3].r.setLocation(0, var12);
                    this.list[var3].setSize(this.W, var7, var10);
                    var12 += var7 + 1;
                }

                int var15 = (var1 - 1) / 2;

                Rectangle var13;
                for (var3 = 0; var3 < 14; ++var3) {
                    var13 = var5[var3];
                    var13.setBounds(var3 % 2 == 1 ? var15 + 1 : 0, var12, var3 % 2 == 1 ? var1 - var15 - 1 : var15, var8);
                    if (var3 % 2 == 1) {
                        var12 += var8 + 1;
                    }
                }

                for (var15 = (int) (15.0F * var6); var3 < 18; ++var3) {
                    var13 = var5[var3];
                    var13.setBounds(0, var12, var1, var15);
                    var12 += var15 + 1;
                }

                var15 = (int) (32.0F * var6);
                var13 = var5[var3++];
                var13.setBounds(0, var12, var1, var15);
                var12 += var15 + 1;
                var15 = Math.min(var2 - var12, var8);
                var13 = var5[var3++];
                var13.setBounds(0, var2 - var15, var1, var15);
                int var10000 = var12 + var15 + 1;
                super.setSize(var1, var2);
            }
        }
    }

    public void showW(String var1) {
        if (this.isVisible) {
            int var2 = 0;
            if (this.cs != null) {
                for (int var4 = 0; var4 < this.cs.length; ++var4) {
                    LComponent var3 = this.cs[var4];
                    if (var3.getClass().getName().equals(var1)) {
                        if (var3.getParent() == null) {
                            var3.setVisible(this.isVisible);
                            this.parent.add(var3, 0);
                        }

                        return;
                    }

                    var2 = Math.max(var2, var3.getLocation().y);
                }
            }

            if (this.ws != null) {
                for (int var6 = 0; var6 < this.ws.length; ++var6) {
                    if (this.ws[var6].getClass().getName().equals(var1)) {
                        if (!this.ws[var6].isVisible()) {
                            this.ws[var6].setVisible(true);
                        }

                        return;
                    }
                }
            }

            try {
                SW var7 = (SW) Class.forName(var1).newInstance();
                if (var7 instanceof Window) {
                    this.addC(var7);
                    var7.mSetup(this, this.info, this.mi.user, this.mg, this.res, this.config);
                } else {
                    LComponent var8 = (LComponent) var7;
                    this.addC(var8);
                    var8.setVisible(false);
                    var7.mSetup(this, this.info, this.mi.user, this.mg, this.res, this.config);
                    this.parent.add(var8, 0);
                    var8.setLocation(0, var2);
                    var8.inParent();
                    var7.mPack();
                    var8.setVisible(this.isVisible);
                }
            } catch (Throwable var5) {
                this.mi.alert(var5.getLocalizedMessage(), false);
            }

        }
    }

    private void toColor(int var1) {
        if (!this.isRGB) {
            Color.RGBtoHSB(var1 >>> 16 & 255, var1 >>> 8 & 255, var1 & 255, this.fhsb);
            this.iColor = this.mg.iAlpha << 24 | (int) (this.fhsb[0] * 255.0F) << 16 | (int) (this.fhsb[1] * 255.0F) << 8 | (int) (this.fhsb[2] * 255.0F);
        } else {
            this.iColor = this.mg.iAlpha << 24 | var1;
        }

    }

    public void unSelect() {
        for (int var2 = 0; var2 < this.list.length; ++var2) {
            ToolList var1 = this.list[var2];
            if (var1.isSelect) {
                if (!var1.isEraser) {
                    this.oldPen = var2;
                }

                var1.unSelect();
            }
        }

    }

    public void up() {
        try {
            this.mPaint(-1);
            this.mi.up();
            this.upCS();
            if (this.L != null) {
                this.L.repaint();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void upCS() {
        if (this.cs != null) {
            for (int var1 = 2; var1 < this.cs.length; ++var1) {
                if (this.cs[var1] instanceof SW) {
                    ((SW) this.cs[var1]).up();
                }
            }

            if (this.L != null) {
                this.L.repaint();
            }

        }
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }

    public void mVisible(boolean var1) {
        this.isVisible = var1;
        if (this.cs != null) {
            for (int var2 = 0; var2 < this.cs.length; ++var2) {
                if (this.cs[var2] != this) {
                    this.cs[var2].setVisible(var1);
                }
            }
        }

    }
}
