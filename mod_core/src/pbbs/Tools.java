package pbbs;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.StringReader;

import paintchat.MgLine;

// moved from package paintchat_client because it didn't look like it belonged
public class Tools extends Canvas implements WindowListener, ActionListener {
    private Applet app;
    private Component mi;
    private boolean isD = false;
    private int v_bar;
    private int v_add;
    public int clLine = -16777216;
    public int clMask = -16777216;
    public int clAlpha = 255;
    public int lMode = 0;
    public int lHints = 60;
    public int lMaskHints = 110;
    public int lSize = 1;
    public int lLayer = 0;
    private int lSizeWhite = 5;
    public int visit0 = 255;
    public int visit1 = 255;
    public boolean is_start = false;
    private boolean isJp = false;
    private boolean is_advance;
    private boolean is_alpha;
    private boolean is_right;
    private Rectangle[] tools = new Rectangle[28];
    public int sel_button = 0;
    private int sel_tool = 0;
    private byte[][] nows;
    private int now_color = 0;
    private Image image = null;
    private Graphics back = null;
    private static int[] DEFC = new int[]{0, 16777215, 11826549, 8947848, 16422550, 12621504, 16758527, 8421631, 2475977, 15197581, 15177261, 10079099, 16575714, 16375247};
    private static int[] COLORS;
    private int[] CASHS;
    private Color cl_frame;
    private Color cl_bu;
    private Color cl_bu2;
    private Color cl_text;
    private Color cl_bar;
    private Color[] cl_rgb;
    private Color[] cl_ergb;
    private Point old_po = null;
    private static final int T_DRAW = 0;
    private static final int T_PAINT = 1;
    private static final int T_RECT = 2;
    private static final int T_RE = 3;
    private static final int T_WHITE = 4;
    private static final int T_HINT = 5;
    private static final int T_MASK = 6;
    private static final int T_R = 7;
    private static final int T_G = 8;
    private static final int T_B = 9;
    private static final int T_A = 10;
    private static final int T_LINE_SIZE = 11;
    private static final int T_CASH = 12;
    private static final int T_LAYER = 13;
    private Dimension D_ICON;
    private Dimension D_MINI;
    private Dimension D_SIZE;
    private Dimension D_RGB;
    private float size_fitw = 0.0F;
    private float size_fith = 0.0F;
    private String STR_VERSION = "";
    public Dimension D_TOOL;

    public Tools(Applet var1, Component var2, String var3, int var4, boolean var5, boolean var6) {
        this.enableEvents(56L);
        this.setBackground(var1.getBackground());
        this.app = var1;
        this.mi = var2;
        this.isJp = var5;
        this.is_right = var6;
        this.STR_VERSION = var3;

        try {
            this.is_advance = this.p("tool_advance", true);
            this.is_alpha = this.p("tool_alpha", true);
            this.cl_bu = new Color(this.p("tool_color_button", -1515602));
            this.cl_bu2 = new Color(this.p("tool_color_button2", 16308906));
            this.cl_frame = new Color(this.p("tool_color_frame", 0));
            this.cl_text = new Color(this.p("tool_color_text", 7811891));
            this.cl_bar = new Color(this.p("tool_color_bar", 14540287));
            this.cl_rgb = new Color[]{new Color(16422550), new Color(8581688), new Color(8421631), new Color(11184810)};
            this.cl_ergb = new Color[]{this.cl_rgb[0].darker(), this.cl_rgb[1].darker(), this.cl_rgb[2].darker(), this.cl_rgb[3].darker()};
            byte[][] var10001 = new byte[7][];
            byte[] var10004 = new byte[3];
            byte var7 = 0;
            var10004[0] = 0;
            byte var11;
            var10004[1] = var11 = (byte) (var7 + 1);
            var10004[2] = ++var11;
            var10001[0] = var10004;
            var10004 = new byte[4];
            var7 = 7;
            var10004[0] = 7;
            var10004[1] = var11 = (byte) (var7 + 1);
            var10004[2] = ++var11;
            var10004[3] = ++var11;
            var10001[1] = var10004;
            var10004 = new byte[4];
            var7 = 20;
            var10004[0] = 20;
            var10004[1] = var11 = (byte) (var7 + 1);
            var10004[2] = ++var11;
            var10004[3] = ++var11;
            var10001[2] = var10004;
            var10004 = new byte[6];
            var7 = 40;
            var10004[0] = 40;
            var10004[1] = 45;
            var10004[2] = var11 = (byte) (var7 + 1);
            var10004[3] = ++var11;
            var10004[4] = ++var11;
            var10004[5] = ++var11;
            var10001[3] = var10004;
            var10001[4] = new byte[]{19, 39, 100};
            var10004 = new byte[3];
            var7 = 60;
            var10004[0] = 60;
            var10004[1] = var11 = (byte) (var7 + 1);
            var10004[2] = ++var11;
            var10001[5] = var10004;
            var10004 = new byte[5];
            var7 = 110;
            var10004[0] = 110;
            var10004[1] = var11 = (byte) (var7 + 1);
            var10004[2] = ++var11;
            var10004[3] = ++var11;
            var10004[4] = ++var11;
            var10001[6] = var10004;
            this.nows = var10001;

            try {
                for (var11 = 0; var11 < DEFC.length; ++var11) {
                    DEFC[var11] = this.p("color_" + (var11 + 1), DEFC[var11]);
                }

                COLORS = new int[DEFC.length];
                System.arraycopy(DEFC, 0, COLORS, 0, DEFC.length);
                this.fit(1.0F, 1.0F, false);
            } catch (Throwable var9) {
                var9.printStackTrace();
            }

            this.CASHS = new int[]{0, 1, 255, 60, 0, 16777215, 5, 255, 60, 19, 16777215, 10, 255, 60, 19};
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

    }

    public void actionPerformed(ActionEvent var1) {
        try {
            this.isD = var1.getActionCommand().equals("  Yes  ") || var1.getActionCommand().equals(" OK ");
            ((Dialog) ((Component) var1.getSource()).getParent().getParent()).dispose();
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void cash(int var1, boolean var2) {
        if (var1 < 3) {
            int var3 = var1 * 5;
            if (var2) {
                this.CASHS[var3++] = this.clLine;
                this.CASHS[var3++] = this.lSize;
                this.CASHS[var3++] = this.clAlpha;
                this.CASHS[var3++] = this.lHints;
                this.CASHS[var3] = this.lMode;
            } else {
                if (this.lMode == 19) {
                    this.lSizeWhite = this.lSize;
                }

                this.clLine = this.CASHS[var3++];
                this.lSize = this.CASHS[var3++];
                this.clAlpha = this.CASHS[var3++];
                int var4 = this.CASHS[var3++];

                int var5;
                for (var5 = 0; var5 < 3 && this.nows[5][0] != var4; ++var5) {
                    this.roll(5, 1);
                }

                var4 = this.CASHS[var3];

                for (var5 = 0; var5 < this.nows.length; ++var5) {
                    for (int var6 = 0; var6 < this.nows[var5].length; ++var6) {
                        if (var4 == this.nows[var5][0]) {
                            this.sel_button = var5;
                            this.lMode = var4;
                            var5 = this.nows.length;
                            break;
                        }

                        this.roll(var5, 1);
                    }
                }

            }
        }
    }

    private void cash(Point var1, boolean var2) {
        if (var1.y > 5) {
            int var3 = Math.min(var1.x, 48) / 16;
            if (var3 < 3) {
                this.cash(var3, var2);
            }
        }
    }

    public void fit(float var1, float var2, boolean var3) {
        int var4 = (int) (50.0F * var1);
        if (var1 != this.size_fitw || var2 != this.size_fith) {
            this.size_fitw = var1;
            this.size_fith = var2;
            this.D_ICON = new Dimension(var4, (int) (19.0F * var2));
            this.D_MINI = new Dimension((int) (24.0F * var1), (int) (18.0F * var2));
            this.D_SIZE = new Dimension(var4, (int) (32.0F * var2));
            this.D_RGB = new Dimension(var4, (int) (14.0F * var2));
            this.D_TOOL = new Dimension(var4, (int) (420.0F * var2));
            if (this.image != null) {
                this.back.dispose();
                this.back = null;
                this.image.flush();
                this.image = null;
            }

            if (!this.getSize().equals(this.D_TOOL)) {
                this.setSize(this.D_TOOL);
            }

            if (var3) {
                this.m_paint((Graphics) null);
            }

        }
    }

    public static final String getC() {
        try {
            int[] var0 = COLORS == null ? DEFC : COLORS;
            StringBuffer var1 = new StringBuffer();

            for (int var2 = 0; var2 < var0.length; ++var2) {
                if (var2 != 0) {
                    var1.append('\n');
                }

                var1.append("#" + Integer.toHexString(-16777216 | var0[var2] & 16777215).substring(2).toUpperCase());
            }

            return var1.toString();
        } catch (Throwable var3) {
            return null;
        }
    }

    public static final void getDefC(Container var0) {
        try {
            Color var2 = new Color(3289680);
            Color var3 = new Color(14474495);
            Component[] var4 = var0.getComponents();
            if (var4 != null) {
                for (int var5 = 0; var5 < var4.length; ++var5) {
                    Component var1 = var4[var5];
                    if (var1 instanceof Container) {
                        getDefC((Container) var1);
                    } else {
                        var1.setBackground(var3);
                        var1.setForeground(var2);
                    }
                }
            }

            var0.setBackground(var3);
            var0.setForeground(var2);
        } catch (Throwable var6) {
            var6.printStackTrace();
        }

    }

    public static final Font getDefFont(int var0) {
        Dimension var1 = (new Frame()).getToolkit().getScreenSize();
        float var2 = (float) ((double) ((float) var1.height) / 150.0D);
        int var3 = (int) (var2 * 3.0F + (float) (var0 * 2));
        var3 += var3 % 2;
        return new Font("sansserif", 0, var3);
    }

    private final String getMgName(int var1) {
        if (this.isJp) {
            switch (var1) {
                case 0:
                    return "鉛筆";
                case 1:
                    return "水彩";
                case 2:
                    return "ﾃｷｽﾄ";
                case 7:
                    return "トーン";
                case 8:
                    return "ぼかし";
                case 9:
                    return "覆い焼き";
                case 10:
                    return "焼き込み";
                case 19:
                    return "消しペン";
                case 20:
                    return "四角";
                case 21:
                    return "線四角";
                case 22:
                    return "楕円";
                case 23:
                    return "線楕円";
                case 39:
                    return "消し四角";
                case 40:
                    return "コピー";
                case 41:
                    return "角取り";
                case 42:
                    return "左右反転";
                case 43:
                    return "上下反転";
                case 44:
                    return "傾け";
                case 45:
                    return "ﾚｲﾔ結合";
                case 60:
                    return "手書き";
                case 61:
                    return "直線";
                case 62:
                    return "BZ曲線";
                case 100:
                    return "全消し";
                case 110:
                    return "通常";
                case 111:
                    return "マスク";
                case 112:
                    return "逆ﾏｽｸ";
                case 113:
                    return "加算";
                case 114:
                    return "逆加算";
            }
        } else {
            switch (var1) {
                case 0:
                    return "Solid";
                case 1:
                    return "WaterC";
                case 2:
                    return "Text";
                case 7:
                    return "Halftone";
                case 8:
                    return "Blur";
                case 9:
                    return "Light";
                case 10:
                    return "Dark";
                case 19:
                    return "White";
                case 20:
                    return "Rect";
                case 21:
                    return "LineRect";
                case 22:
                    return "Oval";
                case 23:
                    return "LineOval";
                case 39:
                    return "WhiteRect";
                case 40:
                    return "Copy";
                case 41:
                    return "Antialias";
                case 42:
                    return "flipHorizontally";
                case 43:
                    return "flipVertically";
                case 44:
                    return "rotate";
                case 45:
                    return "layerUnify";
                case 60:
                    return "Freehand";
                case 61:
                    return "Line";
                case 62:
                    return "Bezier";
                case 100:
                    return "Clear";
                case 110:
                    return "Normal";
                case 111:
                    return "Mask";
                case 112:
                    return "ReMask";
                case 113:
                    return "And";
                case 114:
                    return "Divide";
            }
        }

        return "";
    }

    public Frame getPFrame() {
        Container var1 = this.getParent();

        for (int var2 = 0; var2 < 10 && (var1 = var1.getParent()) != null; ++var2) {
            if (var1 instanceof Frame) {
                return (Frame) var1;
            }
        }

        return new Frame();
    }

    private void hints() {
        int var1 = this.lMode;
        if (var1 < 20) {
            this.lHints = var1 == 39 ? 20 : (this.lMode == 2 ? this.lMode : this.nows[5][0]);
        } else if (var1 == 39) {
            this.lHints = 20;
        } else if (var1 <= 23) {
            this.lHints = this.lMode;
        } else {
            this.lHints = var1 == 40 ? 40 : 20;
        }
    }

    private void m_click(MouseEvent var1) {
        try {
            Point var2 = var1.getPoint();
            boolean var3 = (var1.getModifiers() & 4) != 0;
            boolean var4 = var3 || var1.isControlDown() || var1.isAltDown();
            int var5 = -1;

            for (int var6 = 0; var6 < this.tools.length; ++var6) {
                if (this.tools[var6].contains(var2)) {
                    var5 = var6;
                    break;
                }
            }

            if (var5 < 0) {
                return;
            }

            boolean var9 = var5 > 4 || this.sel_button == var5;
            if (var5 == 6 && var4) {
                var9 = false;
            }

            this.sel_tool = var5;
            if (var5 <= 4) {
                this.sel_button = var5;
            }

            this.roll(var5, !var9 ? 0 : (var4 ? -1 : 1));
            if (var5 > 4) {
                switch (var5) {
                    case 5:
                        break;
                    case 6:
                        if (var4) {
                            this.clMask = this.clLine;
                        }
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        this.setRGB(var5, var2, var1.isShiftDown() ? 256 : 0);
                        break;
                    case 11:
                        this.setLineSize(var2, 0, var1.isShiftDown());
                        break;
                    case 12:
                        this.cash(var2, var4);
                        break;
                    case 13:
                        if (!var4) {
                            this.lLayer = this.lLayer == 1 ? 0 : 1;
                        } else {
                            if (this.lLayer == 0) {
                                this.visit0 ^= 255;
                            } else {
                                this.visit1 ^= 255;
                            }

                            this.mi.repaint();
                        }
                        break;
                    default:
                        this.now_color = var5 - this.tools.length + 14;
                        if (var4) {
                            COLORS[this.now_color] = this.clLine;
                        }

                        if (var1.isShiftDown()) {
                            COLORS[this.now_color] = DEFC[this.now_color];
                        }

                        this.clLine = COLORS[this.now_color];
                        if (this.sel_button == 4) {
                            this.setButton(0);
                        }
                }
            }

            if (var5 != 12) {
                this.upmode();
            }

            this.hints();
            StringBuffer var7 = new StringBuffer();
            var7.append(this.getMgName(this.lMode));
            var7.append(" Mask=");
            var7.append(this.getMgName(this.lMaskHints));
            var7.append(" Hints=");
            var7.append(this.getMgName(this.lHints));
            this.app.showStatus(var7.toString());
            this.m_paint((Graphics) null);
        } catch (Throwable var8) {
            var8.printStackTrace();
        }

    }

    private void m_drag(MouseEvent var1) {
        if (!var1.isShiftDown() && !var1.isAltDown() && !var1.isControlDown()) {
            Point var2 = var1.getPoint();
            switch (this.sel_tool) {
                case 7:
                case 8:
                case 9:
                case 10:
                    this.setRGB(this.sel_tool, var2, 0);
                    break;
                case 11:
                    this.setLineSize(var2, 0, false);
                    break;
                default:
                    return;
            }

            this.m_paint((Graphics) null);
        }
    }

    public synchronized void m_paint(Graphics var1) {
        boolean var2 = false;
        if (var1 == null) {
            var1 = this.getGraphics();
            var2 = true;
        }

        try {
            if (this.back == null) {
                this.m_start();
            }

            int var4 = 0;
            int var6 = this.D_ICON.width;
            int var7 = this.D_ICON.height;
            Math.max(this.lSize / 10, 1);
            int var8 = var6 / 2;
            int var9 = var7 / 2;
            Graphics var10 = this.back;
            Image var11 = this.image;
            Rectangle var12 = null;
            String var13 = null;
            var10.setColor(this.cl_frame);
            var10.drawRect(0, 0, var6 - 1, var7 - 1);

            int var3;
            Color var14;
            int var15;
            int var16;
            for (var14 = new Color(this.clLine); var4 < 7; ++var4) {
                label173:
                {
                    var13 = null;
                    var12 = this.tools[var4];
                    var6 = var12.width;
                    var7 = var12.height;
                    var10.setColor(var4 > 4 ? this.cl_bu2 : this.cl_bu);
                    var10.fill3DRect(1, 1, var6 - 2, var7 - 2, this.sel_button != var4);
                    var10.setColor(var14);
                    switch (var4) {
                        case 3:
                            switch (this.nows[var4][0]) {
                                case 40:
                                case 45:
                                    var10.fillRect(10, 2, 25, var9);
                                    break;
                                case 41:
                                    var10.fillRoundRect(10, 2, 25, var9, 3, 3);
                                    break;
                                default:
                                    var10.drawRect(10, 2, 25, var9);
                            }

                            var10.setColor(Color.gray);
                            var10.fillRect(15, 4, 25, var9);
                            var13 = this.getMgName(this.nows[var4][0]);
                            if (this.nows[var4][0] == 45) {
                                var10.setColor(var14.darker());
                                var10.fillRect(15, 4, 19, var9 - 1);
                            }
                            break label173;
                        case 6:
                            var10.setColor(new Color(this.clMask));
                            var10.fillRect(3, 2, 43, var9 - 1);
                            var13 = this.getMgName(this.lMaskHints);
                            break label173;
                    }

                    label157:
                    switch (this.nows[var4][0]) {
                        case 0:
                            var10.fillRect(3, 3, 33, 1);
                            break;
                        case 1:
                            var10.fillRect(3, 3, 33, 2);
                            var10.setColor(new Color(this.clLine | 1431655765));
                            var10.drawRect(3, 2, 32, 3);
                            break;
                        case 2:
                            var10.drawString("ABC", 3, 2 + (int) (10.0F * this.size_fith));
                            break;
                        case 7:
                            var15 = this.clAlpha / 23;
                            var16 = 2;

                            while (true) {
                                if (var16 > var9 + 1) {
                                    break label157;
                                }

                                for (int var17 = 3; var17 <= var8 + 1; ++var17) {
                                    if (!MgLine.isTone(var15, var17, var16)) {
                                        var10.fillRect(var17, var16, 1, 1);
                                    }
                                }

                                ++var16;
                            }
                        case 8:
                            var10.fillRoundRect(3, 2, var8, var9, 4, 4);
                            break;
                        case 9:
                            var10.setColor(Color.lightGray);
                            var10.fillRoundRect(3, 2, var8, var9, 1, 1);
                            break;
                        case 10:
                            var10.setColor(Color.darkGray);
                            var10.fillRoundRect(3, 2, var8, var9, 1, 1);
                            break;
                        case 19:
                        case 39:
                        case 100:
                            var10.setColor(this.cl_ergb[2]);
                            var10.drawRect(8, 2, 11, var9 + 2);
                            var10.drawRect(19, 2, 18, var9 + 2);
                            var10.setColor(Color.white);
                            var10.fillRect(9, 3, 10, var9 + 1);
                            var10.setColor(this.cl_rgb[2]);
                            var10.fillRect(20, 3, 17, var9 + 1);
                            break;
                        case 20:
                            var10.fillRect(8, 3, 34, 5);
                            break;
                        case 21:
                            var10.drawRect(8, 3, 34, 5);
                            break;
                        case 22:
                            var10.fillOval(8, 3, 34, 8);
                            break;
                        case 23:
                            var10.drawOval(8, 3, 34, 8);
                            break;
                        case 60:
                            var10.drawRect(14, 2, 22, var9);
                            var10.drawRect(14, 2 + var9 / 3, 22, var9 / 2);
                            var10.fillRect(2, 2 + (int) (5.0F * this.size_fith), 3, 1);
                            var10.drawLine(14, 2, 3, 2 + (int) (4.0F * this.size_fith));
                            var10.drawLine(14, var9 + 2, 3, 2 + (int) (6.0F * this.size_fith));
                            break;
                        case 61:
                            var10.drawLine(7, var9, 43, 3);
                            break;
                        case 62:
                            var3 = (int) (4.0F * this.size_fith);
                            var10.drawPolyline(new int[]{12, 16, 20, 24, 28, 32, 36, 40}, new int[]{var3, 3 + var3, 4 + var3, 3 + var3, 1 + var3, var3, var3 + 1, var3 + 4}, 7);
                    }

                    var13 = this.getMgName(this.nows[var4][0]);
                }

                if (var13 != null) {
                    var10.setColor(this.cl_text);
                    var10.drawString(var13, 2, var7 - 3);
                }

                var1.drawImage(var11, var12.x, var12.y, var12.x + var6, var12.y + var7, 0, 0, var6, var7, (ImageObserver) null);
            }

            boolean var19 = false;
            var13 = null;
            var10.setColor(this.cl_frame);
            var10.drawRect(0, 0, this.D_RGB.width - 1, this.D_RGB.height - 1);
            var6 = this.tools[var4].width;
            var7 = this.tools[var4].height;

            int var5;
            for (var5 = 0; var5 < 4; ++var5) {
                var15 = var5 == 3 ? this.clAlpha : this.clLine >> 16 - (var5 << 3) & 255;
                var16 = Math.max(Math.min(var15 / 5 - 2, var6 - 2), 1);
                var10.setColor(this.cl_bar);
                var10.fillRect(1, 1, var6 - 2, var7 - 2);
                var10.setColor(this.cl_rgb[var5]);
                var10.fillRect(1, 1, var16, var7 - 2);
                var10.setColor(this.cl_ergb[var5]);
                var10.drawLine(var16, 1, var16, var7 - 2);
                var10.setColor(this.cl_text);
                var10.drawString((var5 == 0 ? "R" : (var5 == 1 ? "G" : (var5 == 2 ? "B" : "A"))) + var15, 2, var7 - 3);
                var12 = this.tools[var4];
                var1.drawImage(var11, var12.x, var12.y, var12.x + var6, var12.y + var7, 0, 0, var6, var7, (ImageObserver) null);
                ++var4;
            }

            var12 = this.tools[var4];
            var6 = var12.width;
            var7 = var12.height;
            var3 = (int) ((float) this.lSize * this.size_fith);
            var10.setColor(this.cl_bar);
            var10.fillRect(1, 1, var6 - 2, var7 - 2);
            var10.setColor(var14);
            var10.fillRect(1, 1, var6 - 2, var3);
            var10.setColor(var14.darker());
            var10.drawLine(1, var3, var6 - 2, var3);
            var10.setColor(this.cl_text);
            var10.drawString(this.lSize + "px", 2, var7 - 3);
            var10.setColor(this.cl_frame);
            var10.drawRect(0, 0, var6 - 1, var7 - 1);
            var1.drawImage(var11, var12.x, var12.y, var12.x + var6, var12.y + var7, 0, 0, var6, var7, (ImageObserver) null);
            ++var4;
            var12 = this.tools[var4];
            var6 = var12.width;
            var7 = var12.height;
            var15 = this.D_TOOL.width / 3 - 10;
            var10.setColor(this.cl_bar);
            var10.fillRect(1, 1, var6 - 2, var7 - 2);
            var10.setColor(this.cl_frame);
            var10.drawRect(0, 0, var6 - 1, var7 - 1);

            for (var5 = 0; var5 < 3; ++var5) {
                var10.drawRect(3 + 15 * var5, 2, 12, var9 - 1);
            }

            for (var5 = 0; var5 < 3; ++var5) {
                var10.setColor(new Color(this.CASHS[var5 * 5]));
                var10.fillRect(4 + 15 * var5, 3, 11, var9 - 2);
            }

            var1.drawImage(var11, var12.x, var12.y, var12.x + var6, var12.y + var7, 0, 0, var6, var7, (ImageObserver) null);
            ++var4;
            var12 = this.tools[var4];
            var7 = var12.height / 2;
            var10.setColor(this.cl_bar);
            var10.fillRect(1, 1, var12.width - 2, var12.height - 2);
            var10.setColor(this.cl_frame);
            var10.drawRect(0, 0, var12.width - 1, var12.height - 1);
            var10.setColor(this.cl_text);
            var10.fillRect(3, var7, var12.width - 6, 1);
            var10.drawString("Layer" + this.lLayer, 2, var12.height - 1 - this.lLayer * ((var12.height - 1) / 2));
            var10.setColor(Color.red);
            if (this.visit1 != 255) {
                var10.drawLine(1, 1, var12.width, var7);
            }

            if (this.visit0 != 255) {
                var10.drawLine(1, var7, var12.width, var12.height);
            }

            var1.drawImage(var11, var12.x, var12.y, var12.x + var12.width, var12.y + var12.height, 0, 0, var12.width, var12.height, (ImageObserver) null);
            ++var4;
            var6 = this.tools[var4].width;
            var7 = this.tools[var4].height;

            for (var5 = 0; var5 < COLORS.length; ++var5) {
                var12 = this.tools[var4];
                var10.setColor(new Color(COLORS[var5]));
                var10.draw3DRect(1, 1, var6 - 1, var7 - 1, this.now_color != var5);
                var10.fillRect(2, 2, var6 - 2, var7 - 2);
                var10.setColor(this.cl_frame);
                var10.drawRect(0, 0, this.D_MINI.width - 1, this.D_MINI.height - 1);
                var1.drawImage(var11, var12.x, var12.y, var12.x + var6, var12.y + var7, 0, 0, var6, var7, (ImageObserver) null);
                ++var4;
            }
        } catch (Exception var18) {
            var18.printStackTrace();
        }

        if (var2) {
            var1.dispose();
        }

    }

    public void m_start() {
        try {
            int var10000 = this.D_TOOL.width - 4;
            float var1 = this.size_fith;
            float var9 = this.size_fitw;
            Rectangle[] var2 = this.tools;
            int var3;
            if (var2[0] == null) {
                for (var3 = 0; var3 < var2.length; ++var3) {
                    var2[var3] = new Rectangle();
                }
            }

            this.image = this.app.createImage(this.D_TOOL.width, (int) (32.0F * var1));
            this.back = this.image.getGraphics();
            this.back.setFont(new Font("SansSerif", 0, Math.round(10.0F * var1)));
            boolean var8 = false;
            int var4 = 0;
            int var5 = 0;

            byte var6;
            for (var6 = 0; var4 < 7; ++var4) {
                var2[var4].setSize(this.D_ICON);
                var2[var4].setLocation(0, var5);
                var5 += this.D_ICON.height + 1;
            }

            var3 = 1;

            for (var4 = 14; var4 < var2.length; ++var4) {
                var2[var4].setSize(this.D_MINI);
                var2[var4].setLocation(var3 % 2 * (this.D_MINI.width + 2), var5);
                if (var3 % 2 == 0) {
                    var5 += this.D_MINI.height + 1;
                }

                ++var3;
            }

            for (var4 = 7; var4 < 11; ++var4) {
                var2[var4].setSize(this.D_RGB);
                var2[var4].setLocation(var6, var5);
                var5 += this.D_RGB.height + 1;
            }

            var2[var4].setSize(this.D_SIZE);
            var2[var4].setLocation(var6, var5);
            var5 += this.D_SIZE.height;
            ++var4;
            var5 += 2;
            var2[var4].setSize(this.D_RGB);
            var2[var4].setLocation(0, var5);
            var5 += this.D_RGB.height + 4;
            ++var4;
            var2[var4].setBounds(0, var5, this.D_TOOL.width, (int) (20.0F * var1));
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

    }

    public void message(String var1) {
        Dialog var2 = this.message(var1, this.STR_VERSION, 1);
        var2.show();
    }

    public Dialog message(String var1, String var2, int var3) {
        try {
            Dialog var4 = new Dialog(this.getPFrame(), var2, var3 != 0);
            var4.setResizable(false);
            var4.setLayout(new BorderLayout());
            var4.addWindowListener(this);
            BufferedReader var5 = new BufferedReader(new StringReader(var1), var1.length());
            Panel var7 = new Panel(new GridLayout(0, 1));

            String var6;
            while ((var6 = var5.readLine()) != null) {
                var7.add(new Label(var6, 1));
            }

            var5.close();
            var4.add(var7, "Center");
            if (var3 >= 1) {
                Panel var8 = new Panel(new FlowLayout());
                Button var9 = new Button(var3 >= 2 ? "  Yes  " : "  OK  ");
                var8.add(var9);
                var9.addActionListener(this);
                if (var3 >= 2) {
                    var9 = new Button("   No   ");
                    var8.add(var9);
                    var9.addActionListener(this);
                }

                var4.add(var8, "South");
            }

            var4.pack();
            moveC(var4);
            getDefC(var4);
            var4.addNotify();
            return var4;
        } catch (Throwable var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public boolean messageEx(String var1) {
        Dialog var2 = this.message(var1, this.STR_VERSION, 2);
        var2.show();
        return this.isD;
    }

    public static final void moveC(Window var0) {
        Dimension var1 = var0.getToolkit().getScreenSize();
        Dimension var2 = var0.getSize();
        var0.setLocation((var1.width - var2.width) / 2, (var1.height - var2.height) / 2);
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

    public boolean p(String var1, boolean var2) {
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

    public void paint(Graphics var1) {
        this.m_paint(var1);
    }

    protected void processEvent(AWTEvent var1) {
        if (this.back != null) {
            if (var1 instanceof MouseEvent) {
                MouseEvent var2 = (MouseEvent) var1;
                var2.consume();
                switch (var1.getID()) {
                    case 501:
                        this.m_click(var2);
                        break;
                    case 506:
                        this.m_drag(var2);
                }

            } else {
                if (var1 instanceof KeyEvent) {
                    this.getParent().dispatchEvent(var1);
                }

            }
        }
    }

    private void roll(int var1, int var2) {
        if (var2 != 0 && var1 >= 0 && var1 < this.nows.length) {
            byte[] var3 = this.nows[var1];
            int var4 = var3.length;
            if (var4 > 1) {
                byte var5;
                int var6;
                if (var2 < 0) {
                    var5 = var3[var4 - 1];

                    for (var6 = var4 - 2; var6 >= 0; --var6) {
                        var3[var6 + 1] = var3[var6];
                    }

                    var3[0] = var5;
                } else {
                    var5 = var3[0];

                    for (var6 = 1; var6 < var4; ++var6) {
                        var3[var6 - 1] = var3[var6];
                    }

                    var3[var4 - 1] = var5;
                }

            }
        }
    }

    public void setButton(int var1) {
        if (var1 != this.sel_button) {
            if (var1 < 0) {
                this.sel_button = var1;
            } else {
                MouseEvent var2 = new MouseEvent(this, 0, 0L, 0, this.tools[var1].x, this.tools[var1].y, 1, false);
                this.m_click(var2);
            }
        }
    }

    public void setC(String var1) {
        try {
            BufferedReader var2 = new BufferedReader(new StringReader(var1));

            for (int var3 = 0; (var1 = var2.readLine()) != null && var1.length() > 0; DEFC[var3++] = Integer.decode(var1)) {
            }

            System.arraycopy(DEFC, 0, COLORS, 0, COLORS.length);
            this.m_paint((Graphics) null);
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public void setLineSize(Point var1, int var2, boolean var3) {
        int var4 = var2 == 0 ? this.lSize : var2;
        if (var1 != null) {
            int var5 = var1.y - this.tools[11].y;
            var4 = var5;
            if (var1.x >= 0 && var1.x <= this.D_SIZE.width) {
                this.v_bar = 0;
                this.v_add = 0;
            } else {
                var4 = var5 - this.v_bar;
                var4 = var4 < 0 ? -1 : (var4 > 0 ? 1 : 0);
                if (++this.v_add > 3) {
                    this.v_add = 0;
                } else {
                    var4 = 0;
                }

                var4 += this.lSize;
            }

            if (var3) {
                var4 = this.lSize + (this.lSize < var5 ? 1 : -1);
            }

            this.v_bar = var5;
        }

        var4 = var4 <= 0 ? 1 : (var4 > 30 ? 30 : var4);
        if (this.lMode == 1 && var1 != null) {
            this.clAlpha = 255 - (7 + (var4 >>> 1) * 3) * 2;
        }

        this.lSize = (byte) var4;
    }

    public void setRGB(int var1, Point var2, int var3) {
        try {
            if (var1 < 0) {
                return;
            }

            int var4 = (9 - var1) * 8;
            int var5 = var2.x - this.tools[var1].x;
            int var6 = var1 != 10 ? this.clLine >>> var4 & 255 : this.clAlpha;
            int var7 = var5 * 5;
            if (var3 >= 255) {
                var3 = 0;
                var7 = var6 + (var6 < var7 ? 1 : -1);
            }

            int var8;
            int var9;
            if (var3 != 0) {
                int var10000 = var5 + var3;
            } else {
                var8 = var2.y - this.tools[var1].y;
                var9 = var5 - this.v_bar;
                if (var9 < 0 && this.v_add-- <= -2) {
                    this.v_add = 0;
                }

                if (var9 > 0 && this.v_add++ >= 2) {
                    this.v_add = 0;
                }

                if (var8 >= 0 && var8 <= this.D_RGB.height && var5 >= -60 && var5 <= this.D_RGB.width + 60) {
                    this.v_bar = 0;
                    this.v_add = 0;
                } else {
                    var7 = var6 + this.v_add / 2;
                }

                this.v_bar = var5;
            }

            var7 = var7 < 0 ? 0 : (var7 > 255 ? 255 : var7);
            var8 = this.clLine;
            var9 = this.clAlpha;
            if (var1 != 10) {
                var8 = (var7 & 255) << var4 | var8 & ~(255 << var4);
            } else {
                var9 = !this.is_alpha && this.lMode != 7 ? 255 : var7;
            }

            COLORS[this.now_color] = var8;
            this.clLine = var8;
            this.clAlpha = Math.max(var9, 1);
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

    }

    public void update(Graphics var1) {
        this.paint(var1);
    }

    public void upmode() {
        if (this.sel_button >= 0) {
            byte var1 = this.nows[this.sel_button][0];
            byte var2 = this.nows[6][0];
            int var3;
            if (var1 != this.lMode) {
                switch (var1) {
                    case 1:
                        this.clAlpha = 255 - (7 + this.lSize / 2 * 3) * 2;
                        break;
                    case 7:
                        this.clAlpha = 23;
                        break;
                    case 8:
                    case 9:
                    case 41:
                        this.clAlpha = 128;
                        break;
                    case 10:
                        this.clAlpha = 160;
                        break;
                    case 19:
                        var3 = this.lSize;
                        this.lSize = this.lSizeWhite;
                        this.lSizeWhite = var3;
                        break;
                    default:
                        this.clAlpha = 255;
                }
            }

            if (this.lMode == 19 && var1 != 19) {
                var3 = this.lSize;
                this.lSize = this.lSizeWhite;
                this.lSizeWhite = var3;
            }

            this.lMode = var1;
            this.lMaskHints = var2;
        }
    }

    public void windowActivated(WindowEvent var1) {
    }

    public void windowClosed(WindowEvent var1) {
    }

    public void windowClosing(WindowEvent var1) {
        var1.getWindow().dispose();
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
