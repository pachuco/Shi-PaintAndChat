package pbbs;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Vector;

import paintchat.MgLine;
import syi.util.ByteStream;

import static res.ResPaintBBS.*;

public class Pp extends Panel {
    private PaintBBS pbbs;
    private Mi mI;
    protected Tools tools;
    public static final String STR_VER = "PaintBBSv2.22_8";
    private static final boolean bLicense = false;
    private Dimension dPack = new Dimension();
    public boolean isPack = true;
    public static int count_click;
    public static long count_timer;
    public ByteStream b_ani = null;
    int maxAni;
    private boolean bool_mouse_tool = false;
    private Point old_point = new Point();
    protected volatile boolean isSend = false;
    protected int image_x;
    protected int image_y;
    public Color cl_back;
    private Color cl_app_back;
    private Color cl_app_back2;
    private Color cl_app_fore;
    private Color cl_icon;
    private Color cl_frame;
    private Color cl_select;
    private Color cl_bar;
    private Color cl_bar_hl;
    private Color cl_bar_sh;
    private Color cl_bar_bhl;
    private Image image_brush = null;
    Image image_applet = null;
    public Graphics back = null;
    private Graphics primary = null;
    private Graphics gCanvas = null;
    private FontMetrics font_m = null;
    private int size_bar;
    public int scale_x = 0;
    public int scale_y = 0;
    public int scale = 1;
    private volatile boolean bool_paint = true;
    private boolean bool_back = true;
    private Rectangle[] r_tools = new Rectangle[10];
    private String[] str_tools = null;
    private byte select_now = -1;
    private static final byte T_REDO = 0;
    private static final byte T_UNDO = 1;
    private static final byte T_PASTE = 2;
    private static final byte T_FLOAT = 3;
    private static final byte T_SAVE = 4;
    private static final byte T_VERSION = 5;
    private static final byte T_ZOOM_IN = 6;
    private static final byte T_ZOOM_OUT = 7;
    private static final byte T_SCROLLW = 8;
    private static final byte T_SCROLLH = 9;
    private int mouse_now = -1;
    private MgLine[][] uline;
    protected int[][][] uimage;
    private int seek_line = 0;
    private int seek_box = 0;
    public static final String C_IM = "image_";
    public static final String C_W = "width";
    public static final String C_H = "height";
    public static final String C_CANVAS = "canvas";
    public static final String C_UNDO = "undo";
    public static final String C_UNDOIN = "undo_in_mg";

    final void copy(int[][] var1, int[][] var2) {
        for (int var3 = 0; var3 < var1.length; ++var3) {
            this.copy(var1[var3], var2[var3]);
        }

    }

    final void copy(int[] var1, int[] var2) {
        System.arraycopy(var1, 0, var2, 0, var1.length);
    }

    private void drawBack() {
        try {
            if (!this.bool_back) {
                return;
            }

            Dimension var1;
            int var2;
            if (this.image_brush == null) {
                this.mI.getLocation();
                var1 = this.getSize();
                this.back.setColor(this.cl_app_back);
                this.back.fillRect(0, 0, var1.width, var1.height);
                this.back.setColor(this.cl_app_back2);

                for (var2 = 16; var2 < var1.width; var2 += 16) {
                    this.back.drawLine(var2, 0, var2, var1.height);
                }

                for (var2 = 16; var2 < var1.height; var2 += 16) {
                    this.back.drawLine(0, var2, var1.width, var2);
                }

                this.bool_back = this.image_brush == null;
            } else {
                var1 = this.getSize();
                var2 = this.image_brush.getWidth(this);
                int var3 = this.image_brush.getHeight(this);
                int var4 = var1.width / var2 + (var1.width % var2 > 0 ? 1 : 0);
                int var5 = var1.height / var3 + (var1.height % var3 > 0 ? 1 : 0);

                for (int var6 = 0; var6 < var5; ++var6) {
                    for (int var7 = 0; var7 < var4; ++var7) {
                        this.back.drawImage(this.image_brush, var7 * var2, var6 * var3, this.cl_app_back, this);
                    }
                }

                this.bool_back = false;
            }
        } catch (RuntimeException var8) {
        }

    }

    private void drawButton(Graphics var1, int var2) {
        try {
            if (var2 >= 8) {
                return;
            }

            if ((var1 = this.primary(var1)) == null) {
                return;
            }

            int var4;
            int var5;
            if (var2 < 0) {
                var4 = 0;
                var5 = 8;
            } else {
                var4 = var2;
                var5 = var2 + 1;
            }

            Graphics var7 = this.back;
            var7.setColor(this.cl_icon);

            int var3;
            for (var3 = var4; var3 < var5; ++var3) {
                if (this.mouse_now != var3) {
                    var7.fillRect(this.r_tools[var3].x, this.r_tools[var3].y, this.r_tools[var3].width, this.r_tools[var3].height);
                }
            }

            Rectangle var6;
            if (this.mouse_now >= var4 && this.mouse_now <= var5) {
                var7.setColor(this.cl_icon.darker());
                var6 = this.r_tools[this.mouse_now];
                var7.fillRect(var6.x, var6.y, var6.width, var6.height);
            }

            var7.setColor(this.cl_icon.brighter());

            for (var3 = var4; var3 < var5; ++var3) {
                var6 = this.r_tools[var3];
                if (this.select_now != var3) {
                    var7.drawRect(var6.x, var6.y, var6.width, var6.height);
                }
            }

            if (this.select_now != 5 && this.select_now >= var4 && this.select_now <= var5 && this.select_now <= 7) {
                var6 = this.r_tools[this.select_now];
                if (this.select_now != this.mouse_now) {
                    var7.drawRect(var6.x + 2, var6.y + 2, var6.width - 3, var6.height - 3);
                }

                var7.setColor(this.cl_select);
                var7.drawRect(var6.x + 1, var6.y + 1, var6.width - 2, var6.height - 2);
            }

            var7.setColor(this.cl_app_fore);
            int var8 = this.font_m.getMaxDescent();

            for (var3 = var4; var3 < var5; ++var3) {
                var6 = this.r_tools[var3];
                if (var6.height >= 16) {
                    var7.drawString(this.str_tools[var3], var6.x + 5, var6.y + var6.height - var8 - 2);
                }
            }

            var7.setColor(this.cl_frame);

            for (var3 = var4; var3 < var5; ++var3) {
                var6 = this.r_tools[var3];
                var7.drawRect(var6.x, var6.y, var6.width, var6.height);
            }

            if (var2 < 0 || var2 > 7) {
                this.drawScroll(this.back);
            }

            if (var1 != this.back) {
                var1.drawImage(this.image_applet, 0, 0, Color.white, (ImageObserver) null);
            }
        } catch (RuntimeException var9) {
            var9.printStackTrace();
        }

    }

    void drawScroll(Graphics var1) {
        try {
            if ((var1 = this.primary(var1)) == null) {
                return;
            }

            int var10000 = this.size_bar;
            boolean var2 = false;
            Rectangle var4 = this.mI.getBounds();
            var10000 = var4.x;
            var10000 = var4.y;
            int var5 = var4.width;
            int var6 = var4.height;
            int var7 = var5 / this.scale;
            int var8 = var6 / this.scale;
            if (this.scale_x + var7 >= this.image_x) {
                this.scale_x = Math.max(0, this.image_x - var7);
            }

            if (this.scale_y + var8 - 1 >= this.image_y) {
                this.scale_y = Math.max(0, this.image_y - var8);
            }

            Rectangle[] var9 = new Rectangle[]{this.r_tools[8], this.r_tools[9]};
            int var10 = Math.min((int) ((float) var7 / (float) this.image_x * (float) var5), var5);
            int var11 = Math.min((int) ((float) var8 / (float) this.image_y * (float) var6), var6) + 1;
            int var12 = (int) ((float) this.scale_x / (float) this.image_x * (float) var5);
            int var13 = (int) ((float) this.scale_y / (float) this.image_y * (float) var6);
            this.back.setColor(this.cl_frame);

            Rectangle var3;
            int var15;
            for (var15 = 0; var15 < 2; ++var15) {
                var3 = var9[var15];
                this.back.drawRect(var3.x, var3.y, var3.width, var3.height);
            }

            this.back.setColor(this.cl_icon);

            for (var15 = 0; var15 < 2; ++var15) {
                var3 = var9[var15];
                this.back.fillRect(var3.x + 2, var3.y + 2, var3.width - 2, var3.height - 2);
            }

            this.back.setColor(this.cl_bar_bhl);

            for (var15 = 0; var15 < 2; ++var15) {
                var3 = var9[var15];
                this.back.drawRect(var3.x + 1, var3.y + 1, var3.width - 2, var3.height - 2);
            }

            this.back.setColor(this.cl_bar_hl);
            var3 = var9[0];
            this.back.drawRect(var3.x + var12 + 2, var3.y + 2, var10 - 3, var3.height - 3);
            var3 = var9[1];
            this.back.drawRect(var3.x + 2, var3.y + var13 + 2, var3.width - 3, var11 - 3);
            this.back.setColor(this.cl_bar_sh);
            var3 = var9[0];
            this.back.drawRect(var3.x + var12 + 1, var3.y + 1, var10 - 3, var3.height - 3);
            var3 = var9[1];
            this.back.drawRect(var3.x + 1, var3.y + var13 + 1, var3.width - 3, var11 - 3);

            for (var15 = 0; var15 < 2; ++var15) {
                var3 = var9[var15];
                this.back.setColor(8 + var15 == this.select_now ? this.cl_select : this.cl_bar.brighter());
                if (var15 == 0) {
                    this.back.drawRect(var3.x + var12 + 1, var3.y + 1, var10 - 2, var3.height - 2);
                } else {
                    this.back.drawRect(var3.x + 1, var3.y + var13 + 1, var3.width - 2, var11 - 2);
                }
            }

            this.back.setColor(this.cl_bar);
            var3 = var9[0];
            this.back.fillRect(var3.x + var12 + 3, var3.y + 3, var10 - 5, var3.height - 5);
            var3 = var9[1];
            this.back.fillRect(var3.x + 3, var3.y + var13 + 3, var3.width - 5, var11 - 5);

            for (var15 = 0; var15 < 2; ++var15) {
                var1.drawImage(this.image_applet, var9[var15].x, var9[var15].y, var9[var15].x + var9[var15].width, var9[var15].y + var9[var15].height, var9[var15].x, var9[var15].y, var9[var15].x + var9[var15].width, var9[var15].y + var9[var15].height, Color.white, (ImageObserver) null);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public Graphics getCanvas(boolean var1) {
        if (this.gCanvas != null && var1) {
            this.gCanvas.dispose();
            this.gCanvas = null;
        }

        if (this.gCanvas == null) {
            Rectangle var2 = this.mI.getBounds();
            this.gCanvas = this.back.create(var2.x, var2.y, var2.width, var2.height);
        }

        return this.gCanvas;
    }

    public int[] getThumbnail(int[] var1, int var2, int var3, boolean var4, int var5) throws InterruptedException, IOException {
        float var6 = (float) this.image_x / (float) var2;
        float var7 = (float) this.image_y / (float) var3;
        int var18 = this.image_x * this.image_y - 1;
        int var20 = 0;
        int var17;
        if (this.image_x != var2 || this.image_y != var3) {
            int[] var21 = new int[var2 * var3];

            for (int var22 = 0; var22 < var3; ++var22) {
                for (int var23 = 0; var23 < var2; ++var23) {
                    float var13 = 0.0F;
                    float var14 = 0.0F;
                    float var15 = 0.0F;
                    float var16 = 0.0F;

                    for (float var9 = 0.0F; var9 < var7; ++var9) {
                        float var12 = var7 - var9;
                        if (var12 > 1.0F) {
                            var12 = 1.0F;
                        }

                        for (float var8 = 0.0F; var8 < var6; ++var8) {
                            float var11 = var6 - var8;
                            if (var11 > 1.0F) {
                                var11 = 1.0F;
                            }

                            var17 = Math.min(((int) ((float) var22 * var7) + (int) var9) * this.image_x + (int) ((float) var23 * var6) + (int) var8, var18);
                            int var19 = var1[var17];
                            float var10 = var11 * var12;
                            var14 += (float) (var19 >> 16 & 255) * var10;
                            var15 += (float) (var19 >> 8 & 255) * var10;
                            var16 += (float) (var19 & 255) * var10;
                            var13 += var10;
                        }
                    }

                    var14 /= var13;
                    var14 = var14 > 255.0F ? 255.0F : var14;
                    var15 /= var13;
                    var15 = var15 > 255.0F ? 255.0F : var15;
                    var16 /= var13;
                    var16 = var16 > 255.0F ? 255.0F : var16;
                    var21[var20++] = (int) var14 << 16 | (int) var15 << 8 | (int) var16;
                }
            }

            var1 = var21;
        }

        if (var4) {
            var5 = var5 <= 0 ? 1 : var5;
            float var26 = (float) var5;
            float var27 = (float) ((int) ((double) var5 * 1.5D));
            var17 = var2 * var3;
            float[] var10000 = new float[3];
            int[] var28 = new int[3];
            int[] var24 = new int[3];

            for (int var25 = 0; var25 < var17; ++var25) {
                var24[2] = var1[var25] >> 16 & 255;
                var24[1] = var1[var25] >> 8 & 255;
                var24[0] = var1[var25] & 255;
                var28[2] = (int) ((double) var24[2] * 0.299D + (double) var24[1] * 0.587D + (double) var24[0] * 0.114D);
                var28[1] = (int) (-((double) var24[2] * 0.1687D) - (double) var24[1] * 0.3313D + (double) var24[0] * 0.5D + 128.0D);
                var28[0] = (int) ((double) var24[2] * 0.5D - (double) var24[1] * 0.4187D - (double) var24[0] * 0.0813D + 128.0D);
                var28[2] = (int) ((float) Math.round((float) var28[2] / var27) * var27);
                var28[1] = (int) ((float) Math.round((float) var28[1] / var26) * var26);
                var28[0] = (int) ((float) Math.round((float) var28[0] / var26) * var26);
                var24[2] = (int) ((double) var28[2] + 1.402D * (double) (var28[0] - 128));
                var24[1] = (int) ((double) var28[2] - 0.34414D * (double) (var28[1] - 128) - 0.71414D * (double) (var28[0] - 128));
                var24[0] = (int) ((double) var28[2] + 1.772D * (double) (var28[1] - 128));
                var24[2] -= var24[2] % 2;
                var24[1] -= var24[1] % 2;
                var24[0] -= var24[0] % 2;
                var24[2] = var24[2] > 255 ? 255 : (var24[2] < 0 ? 0 : var24[2]);
                var24[1] = var24[1] > 255 ? 255 : (var24[1] < 0 ? 0 : var24[1]);
                var24[0] = var24[0] > 255 ? 255 : (var24[0] < 0 ? 0 : var24[0]);
                var1[var25] = var24[2] << 16 | var24[1] << 8 | var24[0];
            }
        }

        return var1;
    }

    public void init(PaintBBS var1, Tools var2, Mi var3, Vector var4) {
        this.setLayout(null);
        this.pbbs = var1;
        this.tools = var2;
        this.mI = var3;
        String var5 = "bkcolor";
        String var6 = "bk";
        String var7 = "bk2";
        String var8 = "size";
        String var9 = "color_";
        String var10 = "text";
        String var11 = "icon";
        String var12 = "iconselect";
        String var13 = "bar";
        String var14 = "frame";
        String var15 = "_hl";
        String var16 = "_shadow";
        String var17 = "thumbnail_type";
        String var18 = null;

        str_tools = new String[8];
        for (int i=0; i<6; i++) {
            str_tools[i] = langPBBS.get(String.format("b%02d", i));
        }
        str_tools[5] += STR_VER;
        str_tools[6] = "+";
        str_tools[7] = "-";

        this.cl_app_fore = new Color(var1.p(var9 + var10, 6710937));
        this.cl_app_back = new Color(var1.p(var9 + var6, 13421823));
        this.cl_app_back2 = new Color(var1.p(var9 + var7, 12303359));
        this.cl_icon = new Color(var1.p(var9 + var11, 13421823));
        this.cl_select = new Color(var1.p(var9 + var12, 16755370));
        this.cl_bar = new Color(var1.p(var9 + var13, 7303086));
        this.cl_frame = new Color(var1.p(var9 + var14, this.cl_app_fore.getRGB()));
        this.cl_bar_hl = new Color(var1.p(var9 + var13 + '_' + var14 + var15, 15658751));
        this.cl_bar_sh = new Color(var1.p(var9 + var13 + '_' + var14 + var16, this.cl_bar.darker().getRGB()));
        this.cl_bar_bhl = new Color(var1.p(var9 + var13 + var15, 0xFFFFFF));
        this.cl_back = new Color(var1.p("image_" + var5, 0xFFFFFF));
        this.size_bar = var1.p(var13 + '_' + var8, 20);
        this.image_x = var1.p("image_width", 300);
        this.image_y = var1.p("image_height", 300);
        this.maxAni = var1.p("animation_max", 0) * 1024 * 2;
        this.setBackground(this.cl_app_back);
        var1.setBackground(this.cl_app_back);
        var2.setBackground(this.cl_app_back);
        var18 = this.pbbs.getParameter("image_" + var6);

        try {
            if (var18 != null && var18.length() > 0) {
                Image var19 = this.pbbs.getImage(new URL(this.pbbs.getDocumentBase(), var18));
                MediaTracker var20 = new MediaTracker(this);
                var20.addImage(var19, 0);
                var20.waitForID(0);
                this.image_brush = var19;
            }
        } catch (Throwable ex) {
        }

        for (int var35 = 0; var35 < this.r_tools.length; ++var35) {
            this.r_tools[var35] = new Rectangle(this.size_bar, this.size_bar);
        }

        this.setBackground(this.cl_app_back);
        this.image_applet = this.createImage(1, 1);
        this.back = this.image_applet.getGraphics();
        Font var36 = Tools.getDefFont(-1);
        if (var36.getSize() > 16) {
            var36 = new Font(var36.getName(), var36.getStyle(), 16);
        }

        this.back.setFont(var36);
        this.font_m = this.back.getFontMetrics();
        this.tools.is_start = true;
        this.add(this.tools);
        this.add(this.mI);
        this.addNotify();
        this.mI.init(this.pbbs, this, this.tools);
        boolean var37 = false;
        if (PaintBBS.c_ioff != null && (var37 = this.tools.messageEx(langPBBS.get("restore")))) {
            int[][] var21 = PaintBBS.c_ioff;
            this.copy(var21, this.mI.i_offs);
            this.b_ani = PaintBBS.c_ani;
            var4 = null;
        } else {
            count_click = 0;
            count_timer = 0L;
        }

        if (this.pbbs.p(var17).indexOf(97) < 0 && this.pbbs.p(var17 + '2').indexOf(97) < 0) {
            this.b_ani = null;
        } else {
            this.b_ani = this.b_ani != null ? this.b_ani : new ByteStream();
        }

        PaintBBS.c_ani = this.b_ani;
        PaintBBS.c_ioff = this.mI.i_offs;
        int var38 = Math.max(this.pbbs.p("undo_in_mg", 15), 3);
        int var22 = Math.max(this.pbbs.p("undo", 90), var38) / var38;
        this.uline = new MgLine[var22][var38];
        int var23 = this.image_x * this.image_y;
        this.uimage = new int[var22][2][var23];

        int var24;
        for (var24 = 0; var24 < var22; ++var24) {
            this.copy(this.mI.i_offs, this.uimage[var24]);
        }

        if (!var37) {
            try {
                String var25 = var1.getParameter("image_canvas");
                if (var25 != null && var25.length() > 0 && !var25.toLowerCase().endsWith(".pch")) {
                    Image var26 = this.pbbs.getImage(this.pbbs.getCodeBase(), var25);
                    MediaTracker var27 = new MediaTracker(this);
                    var27.addImage(var26, 0);
                    var27.waitForID(0);
                    PixelGrabber var28 = new PixelGrabber(var26, 0, 0, this.image_x, this.image_y, true);
                    var28.grabPixels();
                    int[] var29 = (int[]) var28.getPixels();
                    var26.flush();
                    int var30 = Math.min(var26.getWidth((ImageObserver) null), this.image_x);
                    int var31 = Math.min(var26.getHeight((ImageObserver) null), this.image_y);

                    for (var24 = 0; var24 < var31; ++var24) {
                        System.arraycopy(var29, var30 * var24, this.mI.i_offs[0], var30 * var24, var30);
                    }

                    Object var40 = null;
                    this.copy(this.mI.i_offs, this.uimage[0]);
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            if (var4 != null) {
                while (true) {
                    while (var4.isEmpty()) {
                        try {
                            Thread.currentThread();
                            Thread.sleep(3000L);
                        } catch (InterruptedException var33) {
                            break;
                        }
                    }

                    MgLine var39 = (MgLine) var4.firstElement();
                    var4.removeElementAt(0);
                    if (var39.head == 101) {
                        var4 = null;
                        break;
                    }

                    var39.draw(this.mI, (Graphics) null, (Graphics) null, this.mI.i_offs, this.image_x, this.image_y, 0, 0, 0, 255, 255, -2);
                    this.undo_in(var39);
                }
            }
        }

        this.up_rect();
        this.enableEvents(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        if (count_timer == 0L) {
            count_timer = System.currentTimeMillis();
        }

    }

    public void m_paint(Graphics var1) {
        try {
            if (this.mI == null) {
                return;
            }

            if ((var1 = this.primary(var1)) == null) {
                return;
            }

            Rectangle var2 = this.mI.getBounds();
            Dimension var3 = this.getSize();
            int var4 = var2.x + var2.width;
            int var5 = var2.y + var2.height;
            var1.drawImage(this.image_applet, 0, 0, var3.width, var2.y, 0, 0, var3.width, var2.y, Color.white, (ImageObserver) null);
            var1.drawImage(this.image_applet, 0, var2.y, var2.x, var5, 0, var2.y, var2.x, var5, Color.white, (ImageObserver) null);
            var1.drawImage(this.image_applet, var4, var2.y, var3.width, var5, var4, var2.y, var3.width, var5, Color.white, (ImageObserver) null);
            var1.drawImage(this.image_applet, 0, var5, var3.width, var3.height, 0, var5, var3.width, var3.height, Color.white, (ImageObserver) null);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void makeBack() {
        this.drawBack();
        Rectangle var1 = this.mI.getBounds();
        this.getSize();
        this.back.setColor(this.cl_frame);
        this.back.drawRect(var1.x - 1, var1.y - 1, var1.width + 1, var1.height);
        this.drawButton(this.back, -1);
    }

    Point moveRescale(Point var1) {
        var1.setLocation((var1.x + this.scale_x) * this.scale, (var1.y + this.scale_y) * this.scale);
        return var1;
    }

    Point moveScale(Point var1) {
        var1.move(this.scale_x + var1.x / this.scale, this.scale_y + var1.y / this.scale);
        return var1;
    }

    protected void out(MgLine[] var1, int var2) {
        try {
            if (this.b_ani != null && (this.maxAni <= 0 || this.b_ani.size() <= this.maxAni)) {
                for (int var3 = 0; var3 < var2; ++var3) {
                    var1[var3].getData(this.b_ani);
                }
            }
        } catch (Throwable ex) {
        }

    }

    private void pack(boolean var1) {
        if (this.image_applet != null && this.mI != null) {
            Dimension var2 = this.getSize();
            if (var1 || !var2.equals(this.dPack)) {
                this.dPack.setSize(var2);
                this.setVisible(false);
                this.up_rect();
                this.makeBack();
                this.setVisible(true);
            }

        }
    }

    public void paint(Graphics var1) {
        try {
            if (this.image_applet != null && this.back == null) {
                var1.drawImage(this.image_applet, 0, 0, Color.white, (ImageObserver) null);
                return;
            }

            this.m_paint((Graphics) null);
        } catch (RuntimeException var3) {
            var3.printStackTrace();
        }

    }

    public Graphics primary(Graphics var1) {
        if (var1 != null) {
            return var1;
        } else {
            if (this.primary == null) {
                this.primary = this.getGraphics();
            }

            return this.primary;
        }
    }

    protected void processComponentEvent(ComponentEvent event) {
        if (this.image_applet != null && count_timer != 0L) {
            int id = event.getID();
            if (this.isPack && id == ComponentEvent.COMPONENT_SHOWN) {
                this.isPack = false;
                Container cont = this.getParent();
                cont.invalidate();
                cont.validate();
            } else if (event.getID() == ComponentEvent.COMPONENT_RESIZED) {
                this.primary = null;
                this.mI.p = null;
                this.pack(false);
            }
        }
    }

    public void processKeyEvent(KeyEvent event) {
        try {
            int id = event.getID();
            label34:
            switch (id) {
                case 401:
                    switch (event.getKeyCode()) {
                        case 27:
                            this.mI.cancel();
                            break label34;
                        case 32:
                            this.mI.isSpace = true;
                            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            break label34;
                        case 85:
                        case 90:
                            if (event.isControlDown()) {
                                if (event.isAltDown()) {
                                    this.undo(false);
                                } else {
                                    this.undo(true);
                                }
                            }

                            this.mI.m_paint();
                            break label34;
                        case 89:
                            if (event.isControlDown()) {
                                this.undo(false);
                            }

                            this.mI.m_paint();
                            break label34;
                        case 107:
                            this.scaleChange(1);
                            this.mI.m_paint();
                            break label34;
                        case 109:
                            this.scaleChange(-1);
                            this.mI.m_paint();
                        default:
                            break label34;
                    }
                case 402:
                    this.mI.isSpace = false;
                    this.setCursor(Cursor.getDefaultCursor());
            }

            event.consume();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    protected void processMouseEvent(MouseEvent event) {
        this.processMouseMotionEvent(event);
    }

    protected void processMouseMotionEvent(MouseEvent event) {
        try {
            if (this.isSend) {
                return;
            }

            int var2 = 0;
            int eventID = event.getID();
            Point mousePos = event.getPoint();
            Point var5;
            switch (eventID) {
                case MouseEvent.MOUSE_PRESSED:
                    label93:
                    for (this.old_point = new Point(mousePos); var2 < this.r_tools.length; ++var2) {
                        if (this.r_tools[var2].contains(mousePos)) {
                            this.mouse_now = var2;
                            this.drawButton((Graphics) null, this.mouse_now);
                            switch (var2) {
                                case 0:
                                    this.undo(false);
                                    break label93;
                                case 1:
                                    this.undo(true);
                                    break label93;
                                case 2:
                                    this.tools.lMode = -2;
                                    this.tools.setButton(-1);
                                    this.tools.m_paint((Graphics) null);
                                    this.pbbs.showStatus("Mode PaintBrush");
                                    break label93;
                                case 3:
                                    this.pbbs.popup(true);
                                    break label93;
                                case 4: // exit
                                    this.pbbs.pExit();
                                    break label93;
                                case 5: // open website
                                    String var9 = "http://shichan.jp/";
                                    if (this.tools.messageEx(var9 + (langPBBS.get("visitSite")))) {
                                        this.pbbs.getAppletContext().showDocument(new URL(var9), "_blank");
                                    }
                                    break label93;
                                case 6: // zoom in
                                    this.scaleChange(1);
                                    break label93;
                                case 7: // zoom out
                                    this.scaleChange(-1);
                                default:
                                    break label93;
                            }
                        }
                    }

                    if (var2 >= this.r_tools.length) {
                        this.mouse_now = -1;
                        var5 = this.mI.getLocation();
                        event.translatePoint(-var5.x, -var5.y);
                        this.mI.dispatchEvent(event);
                    }
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    this.bool_mouse_tool = false;
                    if (this.mouse_now == -1) {
                        var5 = this.mI.getLocation();
                        event.translatePoint(-var5.x, -var5.y);
                        this.mI.dispatchEvent(event);
                    } else {
                        int var8 = this.mouse_now;
                        this.mouse_now = -1;
                        if (var8 != 9 && var8 != 9) {
                            this.drawButton((Graphics) null, var8);
                            break;
                        }

                        this.mI.m_paint();
                    }
                    break;
                case MouseEvent.MOUSE_MOVED:
                    while (var2 < this.r_tools.length) {
                        if (var2 != this.select_now && this.r_tools[var2].contains(mousePos)) {
                            this.select_now = (byte) var2;
                            this.drawButton(this.back, -1);
                            this.m_paint((Graphics) null);
                            break;
                        }

                        ++var2;
                    }

                    if (this.mouse_now == -1) {
                        Point var6 = this.mI.getLocation();
                        event.translatePoint(-var6.x, -var6.y);
                        this.mI.dispatchEvent(event);
                    }
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    switch (this.mouse_now) {
                        case -1:
                            var5 = this.mI.getLocation();
                            event.translatePoint(-var5.x, -var5.y);
                            this.mI.dispatchEvent(event);
                            break;
                        case 8:
                        case 9:
                            this.scroll(event, this.old_point, this.mI.isSpace);
                            this.old_point = mousePos;
                    }
            }

            if (eventID == MouseEvent.MOUSE_RELEASED) {
                this.mouse_now = -1;
            }

            event.consume();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    protected void putUMg() {
        try {
            if (this.b_ani == null) {
                return;
            }

            int var1;
            for (var1 = 0; var1 < this.seek_box; ++var1) {
                this.out(this.uline[var1], this.uline[0].length);
            }

            this.out(this.uline[var1], this.seek_line);
            this.seek_box = 0;
            this.seek_line = 0;
            this.uline[0][0] = null;
            this.copy(this.mI.i_offs, this.uimage[0]);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private synchronized void scaleChange(int var1) {
        try {
            this.bool_back = true;
            int var2 = this.scale + var1;
            if (var2 < 1 || var2 > 12) {
                return;
            }

            this.scale = var2;
            int var3 = this.image_x / var2;
            int var4 = this.image_y / var2;
            int var5 = this.image_x - var3;
            int var6 = this.image_y - var4;
            if (var2 == 2) {
                this.scale_x = var3 / 2;
                this.scale_y = var4 / 2;
            } else {
                this.scale_x = (int) ((double) this.scale_x + (double) var3 / 2.0D / (double) var2 * (double) var1);
                this.scale_y = (int) ((double) this.scale_y + (double) var4 / 2.0D / (double) var2 * (double) var1);
            }

            this.scale_x = this.scale_x < 0 ? 0 : (this.scale_x > var5 ? var5 : this.scale_x);
            this.scale_y = this.scale_y < 0 ? 0 : (this.scale_y > var6 ? var6 : this.scale_y);
            this.pbbs.showStatus("1/" + this.scale);
            this.pack(true);
        } catch (Exception var7) {
        }

    }

    void scaleLocation(int var1, int var2) {
        Rectangle var3 = this.mI.getBounds();
        int var4 = this.scale_x + var1;
        int var5 = this.scale_y + var2;
        int var6 = var3.width / this.scale;
        int var7 = var3.height / this.scale;
        var4 = var4 < 0 ? 0 : (var4 + var6 >= this.image_x ? Math.max(0, this.image_x - var6) : var4);
        var5 = var5 < 0 ? 0 : (var5 + var7 - 1 >= this.image_y ? Math.max(0, this.image_y - var7) : var5);
        this.scale_x = var4;
        this.scale_y = var5;
    }

    public synchronized void scroll(MouseEvent event, Point var2, boolean var3) {
        Point mousePos = event.getPoint();
        Dimension var5 = this.mI.getSize();
        Graphics var6 = this.mI.p;
        int var7 = this.scale_x;
        int var8 = this.scale_y;
        float var9 = (float) (mousePos.x - var2.x) * ((float) this.image_x / (float) var5.width);
        if (var9 != 0.0F) {
            var9 = var9 > 0.0F && var9 < 1.0F ? 1.0F : (var9 < 0.0F && var9 > -1.0F ? -1.0F : var9);
        }

        int var10 = (int) var9;
        var9 = (float) (mousePos.y - var2.y) * ((float) this.image_y / (float) var5.height);
        if (var9 != 0.0F) {
            var9 = var9 > 0.0F && var9 < 1.0F ? 1.0F : (var9 < 0.0F && var9 > -1.0F ? -1.0F : var9);
        }

        int var11 = (int) var9;
        if (var3) {
            var10 = -var10;
            var11 = -var11;
        }

        var2.setLocation(mousePos);
        this.scale_x = Math.max(var7 + var10, 0);
        this.scale_y = Math.max(var8 + var11, 0);
        this.drawScroll((Graphics) null);
        int var12 = (this.scale_x - var7) * this.scale;
        int var13 = (this.scale_y - var8) * this.scale;
        var10 = var5.width - Math.abs(var12);
        var11 = var5.height - Math.abs(var13);

        try {
            var6.copyArea(Math.max(var12, 0), Math.max(var13, 0), var10, var11, -var12, -var13);
            Rectangle var14;
            if (var12 != 0) {
                var14 = new Rectangle();
                if (var12 > 0) {
                    var14.setBounds(var5.width - var12, 0, var12, var5.height);
                } else {
                    var14.setBounds(0, 0, -var12, var5.height);
                }

                this.mI.m_paint(var6, var14);
            }

            if (var13 != 0) {
                var14 = new Rectangle();
                if (var13 > 0) {
                    var14.setBounds(0, var5.height - var13, var5.width, var13);
                } else {
                    var14.setBounds(0, 0, var5.width, -var13);
                }

                this.mI.m_paint(var6, var14);
            }
        } catch (Throwable ex) {
            System.out.println(ex);
        }

    }

    synchronized void undo(boolean var1) {
        try {
            int var3 = this.uline.length - 1;
            int var4 = this.uline[0].length - 1;
            if (var1) {
                if (this.seek_line == 0 && this.seek_box == 0) {
                    return;
                }

                if (--this.seek_line < 0) {
                    this.seek_line = var4;
                    --this.seek_box;
                }
            } else {
                if (this.seek_line >= var4 && this.seek_box >= var3) {
                    return;
                }

                if (this.uline[this.seek_box][this.seek_line] == null) {
                    return;
                }

                if (++this.seek_line > var4) {
                    this.seek_line = 0;
                    ++this.seek_box;
                }
            }

            this.copy(this.uimage[this.seek_box], this.mI.i_offs);

            for (int var5 = 0; var5 < this.seek_line; ++var5) {
                MgLine var2 = this.uline[this.seek_box][var5];
                if (var2 == null) {
                    break;
                }

                var2.draw(null, null, null, this.mI.i_offs, this.image_x, this.image_y, 0, 0, 1, 255, 255, -2);
            }

            this.mI.m_paint();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    synchronized void undo_in(MgLine var1) {
        try {
            int var10000 = this.tools.lLayer;
            int var2 = this.uline[0].length;
            int var3 = this.uline.length;
            int var4 = this.uimage.length;
            ++count_click;
            this.uline[this.seek_box][this.seek_line] = var1;
            ++this.seek_line;
            if (this.seek_line >= var2) {
                this.seek_line = 0;
                if (++this.seek_box >= var3) {
                    MgLine[] var6 = this.uline[0];
                    this.out(var6, var2);
                    --this.seek_box;

                    int var5;
                    for (var5 = 1; var5 < var3; ++var5) {
                        this.uline[var5 - 1] = this.uline[var5];
                    }

                    for (var5 = 0; var5 < var2; ++var5) {
                        var6[var5] = null;
                    }

                    this.uline[var3 - 1] = var6;
                    int[][] var7 = this.uimage[0];

                    for (var5 = 1; var5 < var4; ++var5) {
                        this.uimage[var5 - 1] = this.uimage[var5];
                    }

                    this.uimage[var4 - 1] = var7;
                }

                this.copy(this.mI.i_offs, this.uimage[this.seek_box]);
            }

            this.uline[this.seek_box][this.seek_line] = null;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private synchronized void up_rect() {
        try {
            int var1 = this.size_bar;
            FontMetrics var2 = this.font_m;
            int var3 = var2.getAscent() + var2.getDescent();
            Dimension var4 = this.getSize();
            Rectangle var5 = new Rectangle(var4.width - this.tools.D_TOOL.width, var4.height);
            Rectangle var6 = this.mI.getBounds();
            int var8;
            if (this.image_applet != null) {
                int var7 = var4.width;
                var8 = var4.height;
                if (var7 <= this.image_applet.getWidth((ImageObserver) null) && var8 <= this.image_applet.getHeight((ImageObserver) null)) {
                    this.mI.repaint();
                } else {
                    Font var9 = this.back.getFont();
                    this.back.dispose();
                    this.image_applet.flush();
                    Image var10 = null;

                    try {
                        Method var11 = this.getClass().getMethod("createVolatileImage", Integer.TYPE, Integer.TYPE);
                        if (var11 != null) {
                            var10 = (Image) var11.invoke(this, new Integer(var7), new Integer(var8));
                        }
                    } catch (Throwable ex) {
                    }

                    if (var10 == null) {
                        var10 = this.createImage(var7, var8);
                    }

                    this.image_applet = var10;
                    this.back = var10.getGraphics();
                    this.back.setFont(var9);
                }
            }

            Rectangle var17 = new Rectangle();
            var17.setLocation(var5.x + 5 + this.size_bar, var5.y + this.r_tools[0].height + 5 + this.size_bar);
            var17.setSize(var5.width - (var17.x + 5) - this.size_bar, var4.height - this.r_tools[4].height - this.size_bar - 10 - var17.y);
            var8 = this.scale * this.image_x;
            int var18 = this.scale * this.image_y;
            var6 = var17.intersection(new Rectangle(var5.x + (var5.width - var8) / 2, var5.y + (var5.height - var18) / 2, var8, var18));
            if (!var6.equals(this.mI.getBounds())) {
                this.mI.p = null;
                this.mI.setBounds(var6);
            }

            byte var20 = 6;
            int var19 = var6.x;
            int var12 = var6.y;
            var8 = var6.x + var6.width;
            var18 = var6.y + var6.height;
            int var21 = var20 + 1;
            this.r_tools[var20].setLocation(var19 - 1 - var1, var18);
            this.r_tools[var21++].setLocation(var8, var18);
            this.r_tools[var21++].setBounds(var19, var18, var6.width, var1);
            this.r_tools[var21].setBounds(var8, var12 - 1, var1, var6.height);
            if (this.tools != null) {
                this.tools.fit(1.0F, (float) Math.min(var4.height, 520) / 420.0F, false);
                Rectangle var13 = this.tools.getBounds();
                var8 = this.r_tools[var21].x + var1 + 5;
                var18 = Math.max((var4.height - var13.height) / 2, 0);
                if (var13.x != var8 || var13.y != var18) {
                    this.tools.setLocation(var8, var18);
                }
            }

            boolean var23 = false;
            var19 = var6.x + var6.width;
            var12 = var6.y - var3 - 12 - var1;
            String[] var24 = this.str_tools;
            Rectangle[] var14 = this.r_tools;
            var18 = var3 + 8;

            for (var21 = 2; var21 >= 0; --var21) {
                var8 = var2.stringWidth(var24[var21]) + 10;
                var19 -= var8;
                var14[var21].setBounds(var19, var12, var8, var18);
                var19 -= var21 == 2 ? 14 : 7;
            }

            var20 = 3;
            var21 = var20 + 1;
            var14[var20].setBounds(3, 3, var2.stringWidth(var24[var21]), var18);
            byte var22 = 5;
            var12 = var4.height - 5 - var18;
            var8 = var2.stringWidth(var24[var21]) + 10;
            var14[var21++].setBounds(var22, var12, var8, var18);
            var19 = var22 + var8 + 15;
            var8 = var2.stringWidth(var24[5]) + 10;
            var14[var21].setBounds(var19, var12, var8, var18);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
