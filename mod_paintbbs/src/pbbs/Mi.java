package pbbs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;

import paintchat.MgLine;

public class Mi extends Canvas implements WindowListener, ActionListener {
    private ColorModel cm = new DirectColorModel(24, 16711680, 65280, 255);
    private PaintBBS pbbs;
    private Pp pP;
    private Tools tools;
    public int[][] i_offs = null;
    public Graphics p = null;
    private TextField tF = null;
    private short[] points = new short[500];
    private int seek_points = 0;
    private byte draw_flag = 0;
    private Point old_po;
    private Point old_po2;
    private Point old_po3;
    private Point po_pre = new Point();
    private Point po_pre_old = new Point();
    private boolean isPre = true;
    private boolean isPreD = true;
    private int slayer;
    private MgLine mg_b;
    private Rectangle rect_re = null;
    public int dHint = -1;
    public boolean isSpace = false;
    private Image image_b = null;

    public void actionPerformed(ActionEvent var1) {
        try {
            MgLine var2 = new MgLine();
            var2.create(2, this.tools.lMaskHints, Tools.getDefFont(this.tools.lSize - 5).getSize(), this.tools.clLine, this.tools.clMask, this.tools.clAlpha, this.tools.lLayer);
            var2.setData(this.pP.moveScale(this.old_po), var1.getActionCommand());
            var2.draw(this, (Graphics) null, (Graphics) null, this.i_offs, this.pP.image_x, this.pP.image_y, this.pP.scale_x, this.pP.scale_y, this.pP.scale, 255, 255, -2);
            this.pP.undo_in(var2);
            this.tF.setVisible(false);
            this.m_paint();
        } catch (Throwable var3) {
        }

    }

    private void addP(Point var1) {
        this.points[this.seek_points++] = (short) var1.x;
        this.points[this.seek_points++] = (short) var1.y;
        if (this.seek_points >= this.points.length) {
            short[] var2 = new short[this.points.length * 2];
            System.arraycopy(this.points, 0, var2, 0, this.points.length);
            this.points = var2;
        }

    }

    public void cancel() {
        if (this.tools.lHints != 60) {
            this.dHint = -1;
            this.draw_flag = 0;
            this.m_paint();
        }
    }

    private void dPre(MouseEvent var1) {
        var1.getID();
        int var2 = this.pP.scale;
        int var3 = (this.tools.lSize - 1) * var2;
        var3 -= 3 - var3 % 3;
        int var4 = var3 / 2;
        Graphics var5 = this.primary();
        var5.setXORMode(Color.white);
        var5.setColor(this.tools.lMode == 19 ? Color.cyan : (this.tools.clLine == 16777215 ? Color.red : new Color(this.tools.clLine >>> 1)));
        if (this.isPre) {
            var5.drawOval(this.po_pre_old.x - var4, this.po_pre_old.y - var4, var3, var3);
            this.isPre = false;
        }

        if (this.isPreD && var3 >= 5 && this.dHint <= 0 && this.tools.lHints == 60 && this.tools.lMode != -2 && var5 != null) {
            this.isPre = true;
            Point var6 = this.po_pre;
            var6.x = var1.getX();
            var6.y = var1.getY();
            var6.x = var6.x / var2 * var2;
            var6.y = var6.y / var2 * var2;
            var5.drawOval(var6.x - var4, var6.y - var4, var3, var3);
            this.po_pre_old.setLocation(var6);
            var5.setPaintMode();
        } else {
            var5.setPaintMode();
        }
    }

    private void draw_line(Point var1) {
        Point var2 = this.old_po;

        do {
            var2 = this.mg_b.setData(new short[]{(short) var2.x, (short) var2.y, (short) var1.x, (short) var1.y}, 0);
            this.addP(var2);
            this.mg_b.draw(this, this.primary(), (Graphics) null, this.i_offs, this.pP.image_x, this.pP.image_y, this.pP.scale_x, this.pP.scale_y, this.pP.scale, this.tools.visit0, this.tools.visit1, -1);
        } while (!var1.equals(var2));

    }

    private void end_draw(MouseEvent var1) {
        try {
            if (this.draw_flag == 0) {
                return;
            }

            MgLine var6;
            Graphics var2 = this.primary();
            int var3 = this.tools.visit0;
            int var4 = this.tools.visit1;
            Point var5 = var1.getPoint();
            var6 = new MgLine();
            var6.create(this.tools.lMode, this.tools.lMaskHints, this.tools.lSize, this.tools.clLine, this.tools.clMask, this.tools.clAlpha, this.tools.lLayer);
            int var10000 = this.tools.lMode;
            int var7 = this.pP.image_x;
            int var8 = this.pP.image_y;
            int var9 = this.pP.scale_x;
            int var10 = this.pP.scale_y;
            int var11 = this.pP.scale;
            label37:
            switch (this.tools.lHints) {
                case 40:
                    if (this.draw_flag == 2) {
                        this.draw_flag = 0;
                        var6.setData(new Rectangle(this.rect_re), this.pP.moveScale(new Point(this.old_po2)), this.slayer, this.tools.lLayer);
                        var6.draw(this, var2, (Graphics) null, this.i_offs, var7, var8, var9, var10, var11, var3, var4, 0);
                        this.pP.undo_in(var6);
                        return;
                    }

                    this.rect_re = this.t_getRect(this.pP.moveScale(this.old_po), this.pP.moveScale(var5));
                    if (!this.rect_re.isEmpty()) {
                        this.draw_flag = 2;
                    } else {
                        this.draw_flag = 0;
                    }

                    return;
                case 60:
                    this.draw_flag = 0;
                    this.draw_line(this.pP.moveScale(var5));
                    var6.setData(this.points, this.seek_points);
                    break;
                case 61:
                    this.draw_flag = 0;
                    this.t_getRectT(this.old_po, var5);
                    var6.setData(var6.line(this.pP.moveScale(this.old_po), this.pP.moveScale(var5)), 0);
                    var6.draw(this, var2, (Graphics) null, this.i_offs, var7, var8, var9, var10, var11, var3, var4, -1);
                    break;
                case 62:
                    switch (this.draw_flag) {
                        case 1:
                            this.rect_re = new Rectangle(this.old_po, new Dimension(var5.x, var5.y));
                            ++this.draw_flag;
                            return;
                        case 2:
                            this.old_po3 = var5;
                            ++this.draw_flag;
                            return;
                        case 3:
                            this.draw_flag = 0;
                            var6.setData(var6.bezier(new Point[]{this.pP.moveScale(this.rect_re.getLocation()), this.pP.moveScale(this.old_po3), this.pP.moveScale(var5), this.pP.moveScale(new Point(this.rect_re.width, this.rect_re.height))}), 0);
                            var6.draw(this, var2, (Graphics) null, this.i_offs, var7, var8, var9, var10, var11, var3, var4, -2);
                            this.m_paint();
                            this.rect_re = null;
                            this.old_po3 = null;
                            break label37;
                        default:
                            return;
                    }
                default:
                    this.draw_flag = 0;
                    this.t_getRectT(this.old_po, var5);
                    this.rect_re = this.t_getRect(this.pP.moveScale(this.old_po), this.pP.moveScale(var5));
                    if (this.rect_re.isEmpty()) {
                        return;
                    }

                    var6.setData(this.rect_re);
                    var6.draw(this, var2, (Graphics) null, this.i_offs, var7, var8, var9, var10, var11, var3, var4, 0);
                    this.rect_re = null;
            }

            this.pP.undo_in(var6);
        } catch (Exception var12) {
            var12.printStackTrace();
        }

    }

    private void getPixel(Point var1) {
        int var2 = var1.x <= 0 ? 0 : (var1.x >= this.pP.image_x ? this.pP.image_x - 1 : var1.x);
        int var3 = var1.y <= 0 ? 0 : (var1.y >= this.pP.image_y ? this.pP.image_y - 1 : var1.y);
        int var4 = this.pP.image_x * var3 + var2;
        int var5 = this.i_offs[0][var4];
        int var6 = this.i_offs[1][var4];
        int var7 = var6 >>> 24 & this.tools.visit1;
        int var8 = Math.min(255 - var7, var5 >>> 24 & this.tools.visit0);
        int var9 = Math.max(255 - var8 - var7, 0);
        int var10 = Math.min((int) ((float) (var6 >>> 16 & 255) / 255.0F * (float) var7) + (int) ((float) (var5 >>> 16 & 255) / 255.0F * (float) var8) + var9, 255) << 16;
        int var11 = Math.min((int) ((float) (var6 >>> 8 & 255) / 255.0F * (float) var7) + (int) ((float) (var5 >>> 8 & 255) / 255.0F * (float) var8) + var9, 255) << 8;
        int var12 = Math.min((int) ((float) (var6 & 255) / 255.0F * (float) var7) + (int) ((float) (var5 & 255) / 255.0F * (float) var8) + var9, 255);
        int var13 = this.i_offs[this.tools.lLayer][var4] & -16777216 | var10 | var11 | var12;
        boolean var14 = false;
        if (this.tools.lLayer == 1) {
            boolean var15 = var13 == 16777215;
            int var10000 = var13 >>> 24;
            if (this.tools.sel_button == 4) {
                if (!var15) {
                    this.tools.setButton(0);
                    var14 = true;
                }
            } else if (var15) {
                this.tools.setButton(4);
                var14 = true;
            }
        }

        var13 &= 16777215;
        if (var13 != this.tools.clLine) {
            this.tools.clLine = var13;
            var14 = true;
        }

        if (var14) {
            this.tools.m_paint((Graphics) null);
        }

    }

    public void init(PaintBBS var1, Pp var2, Tools var3) {
        this.enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        this.tools = var3;
        this.pbbs = var1;
        this.pP = var2;
        this.setBackground(var2.cl_back);
        this.mg_b = new MgLine();
        MgLine.setup(this, 1);
        int var4 = var2.cl_back.getRGB() & 16777215;
        int var5 = var2.image_x;
        int var6 = var2.image_y;
        int var7 = var5 * var6;
        this.i_offs = new int[2][var7];

        int var8;
        for (var8 = 0; var8 < var5; ++var8) {
            this.i_offs[0][var8] = var4;
        }

        for (var8 = var5; var8 < var7; var8 += var5) {
            System.arraycopy(this.i_offs[0], 0, this.i_offs[0], var8, var5);
        }

        var2.copy(this.i_offs[0], this.i_offs[1]);
    }

    public void m_paint() {
        this.m_paint((Graphics) null, (Rectangle) null);
    }

    public void m_paint(Graphics var1, Rectangle var2) {
        try {
            this.isPre = false;
            if (this.i_offs == null) {
                return;
            }

            if (var1 == null && (var1 = this.primary()) == null) {
                return;
            }

            if (var2 == null) {
                var2 = new Rectangle(this.getSize());
            }

            int var3 = this.pP.scale;
            int var4 = this.pP.scale_x;
            int var5 = this.pP.scale_y;
            int var6 = this.pP.image_x;
            int var7 = this.pP.image_y;
            this.mg_b.setVisit(this.tools.visit0, this.tools.visit1);
            int var8 = var2.x / var3 + var4 - var3;
            int var9 = var2.y / var3 + var5 - var3;
            int var10 = var8 + var2.width / var3 + var3;
            int var11 = var9 + var2.height / var3 + var3;
            if (var8 < 0) {
                var8 = 0;
            }

            if (var9 < 0) {
                var9 = 0;
            }

            if (var10 > var6) {
                var10 = var6 - var9;
            }

            if (var11 > var7) {
                var11 = var7 - var9;
            }

            if (var10 <= 0 || var11 <= 0) {
                return;
            }

            int var12 = (var8 - var4) * var3;
            int var13 = var10 - var8;
            int var14 = 16384 / var13;

            do {
                var14 = Math.min(var14, var11 - var9);
                var1.drawImage(this.mg_b.makeLPic((int[]) null, this.i_offs, var6, var7, var8, var9, var13, var14), var12, (var9 - var5) * var3, var13 * var3, var14 * var3, Color.white, (ImageObserver) null);
            } while ((var9 += var14) < var11);
        } catch (Throwable var15) {
            var15.printStackTrace();
        }

    }

    private void move_draw(MouseEvent var1) {
        try {
            if (this.draw_flag == 0) {
                return;
            }

            Graphics var2 = this.primary();
            Point var3 = var1.getPoint();
            int var4 = this.pP.image_x;
            int var5 = this.pP.image_y;
            int var10000 = this.tools.lLayer;
            int var6 = this.pP.scale;
            int var7 = this.pP.scale_x;
            int var8 = this.pP.scale_y;
            int var9 = this.tools.clLine;
            Rectangle var11;
            int var15;
            int var19;
            switch (this.tools.lHints) {
                case 40:
                    if (this.draw_flag == 2) {
                        for (var19 = 0; var19 < 2; ++var19) {
                            var11 = new Rectangle(this.old_po2.x / var6 + var7, this.old_po2.y / var6 + var8, this.rect_re.width, this.rect_re.height);
                            var11 = (new Rectangle(var7, var8, var4 - var7, var5 - var8)).intersection(var11);
                            int var20 = Math.max(this.old_po2.x / var6 * var6, 0);
                            int var21 = Math.max(this.old_po2.y / var6 * var6, 0);
                            if (var11.width > 0 && var11.height > 0) {
                                if (var19 == 0) {
                                    int var22 = this.rect_re.width * var6;
                                    var15 = this.rect_re.height * var6;
                                    Rectangle var24 = this.getBounds();
                                    var2.drawImage(this.pP.image_applet, var20, var21, var20 + var22, var21 + var15, var24.x + var20, var24.y + var21, var24.x + var20 + var22, var24.y + var21 + var15, (ImageObserver) null);
                                    this.old_po2.translate(-(this.old_po.x - var3.x), -(this.old_po.y - var3.y));
                                } else {
                                    Image var23 = this.createImage(new MemoryImageSource(var11.width, var11.height, this.cm, this.i_offs[this.slayer], (this.rect_re.y + (var21 <= 0 ? this.rect_re.height - var11.height : 0)) * var4 + this.rect_re.x + (var20 <= 0 ? this.rect_re.width - var11.width : 0), var4));
                                    var2.drawImage(var23, var20, var21, var11.width * var6, var11.height * var6, Color.white, (ImageObserver) null);
                                }
                            }
                        }

                        this.old_po = var3;
                    } else {
                        var2.setXORMode(new Color(~var9));
                        var2.setColor(new Color(var9));

                        for (var19 = 0; var19 < 2; ++var19) {
                            var11 = new Rectangle(this.t_getRectT(this.old_po, this.old_po2));
                            var2.drawRect(var11.x, var11.y, var11.width, var11.height);
                            this.old_po2 = var3;
                        }

                        var2.setPaintMode();
                    }

                    return;
                case 60:
                    var3 = this.pP.moveScale(var3);
                    if (!var3.equals(this.old_po)) {
                        this.draw_line(var3);
                        this.old_po = var3;
                    }
                    break;
                case 62:
                    if (this.draw_flag > 1) {
                        this.pP.scale_x = 0;
                        this.pP.scale_y = 0;
                        Rectangle var10 = this.getBounds();
                        var11 = new Rectangle(0, 0, var10.width / var6, var10.height / var6);
                        Color var12 = new Color(var9);
                        Graphics var13 = this.pP.getCanvas(false);
                        Point[] var14 = new Point[]{this.pP.moveScale(this.rect_re.getLocation()), this.pP.moveScale(this.draw_flag == 2 ? new Point(var3) : new Point(this.old_po3)), this.pP.moveScale(var3), this.pP.moveScale(new Point(this.rect_re.width, this.rect_re.height))};
                        var13.drawImage(this.image_b, 0, 0, Color.white, (ImageObserver) null);
                        var13.setXORMode(this.pP.cl_back);
                        var13.setColor(new Color(~this.pP.cl_back.getRGB()));

                        for (var15 = 0; var15 < (this.draw_flag - 2 + 1) * 2; var15 += 2) {
                            var13.drawLine(var14[var15].x, var14[var15].y, var14[var15 + 1].x, var14[var15 + 1].y);
                        }

                        var15 = (4 - this.pP.scale) * 2 + 1;
                        int var16 = var15 / 2;

                        for (int var17 = 0; var17 < this.draw_flag; ++var17) {
                            var13.drawOval(var14[var17].x - var16, var14[var17].y - var16, var15, var15);
                        }

                        var13.setPaintMode();
                        var13.setColor(var12);
                        this.mg_b.setPreData(this.mg_b.bezier(var14), 0, this.tools.lSize);
                        this.mg_b.draw(this, var13, (Graphics) null, this.i_offs, 0, 0, 0, 0, 1, this.tools.visit0, this.tools.visit1, -2);
                        this.primary().drawImage(this.pP.image_applet, 0, 0, var11.width * var6, var11.height * var6, var10.x, var10.y, var10.x + var11.width, var10.y + var11.height, Color.white, (ImageObserver) null);
                        this.pP.scale_x = var7;
                        this.pP.scale_y = var8;
                        return;
                    }
                case 61:
                    var2.setXORMode(new Color(~var9));
                    var2.setColor(new Color(var9));
                    var2.drawLine(this.old_po.x, this.old_po.y, this.old_po2.x, this.old_po2.y);
                    var2.drawLine(this.old_po.x, this.old_po.y, var3.x, var3.y);
                    var2.setPaintMode();
                    this.old_po2 = var3;
                    return;
                default:
                    var2.setXORMode(new Color(~var9));
                    var2.setColor(new Color(this.tools.clLine));

                    for (var19 = 0; var19 < 2; ++var19) {
                        var11 = new Rectangle(this.t_getRectT(this.old_po, this.old_po2));
                        switch (this.tools.lMode) {
                            case 20:
                                var2.fillRect(var11.x, var11.y, var11.width, var11.height);
                                break;
                            case 22:
                                var2.fillOval(var11.x, var11.y, var11.width, var11.height);
                                break;
                            case 23:
                                var2.drawOval(var11.x, var11.y, var11.width, var11.height);
                                break;
                            default:
                                var2.drawRect(var11.x, var11.y, var11.width, var11.height);
                        }

                        this.old_po2 = var3;
                    }

                    var2.setPaintMode();
            }
        } catch (Exception var18) {
            var18.printStackTrace();
        }

    }

    public void paint(Graphics var1) {
        this.m_paint(var1, (Rectangle) null);
    }

    private Graphics primary() {
        if (this.p == null) {
            this.p = this.getGraphics();
        }

        return this.p;
    }

    protected void processKeyEvent(KeyEvent var1) {
        this.pP.processKeyEvent(var1);
    }

    protected void processMouseEvent(MouseEvent var1) {
        this.processMouseMotionEvent(var1);
    }

    protected void processMouseMotionEvent(MouseEvent var1) {
        try {
            int var2 = var1.getID();
            var1.consume();
            switch (var2) {
                case MouseEvent.MOUSE_PRESSED:
                    boolean var9 = var1.isControlDown();
                    boolean var11 = var1.isAltDown();
                    boolean var12 = var1.getModifiers() == 4;
                    if (this.dHint == 1) {
                        if (this.draw_flag > 0) {
                            this.cancel();
                            this.isPreD = false;
                        }

                        return;
                    }

                    if (this.dHint == 2) {
                        this.isPreD = false;
                        return;
                    }

                    this.dHint = var9 && var11 ? 3 : (!var12 && !var9 && !var11 ? (this.isSpace ? 2 : 0) : 1);
                    if (this.tF != null && this.tF.isVisible()) {
                        this.tF.setVisible(false);
                    }

                    if (this.dHint == 0) {
                        this.old_po = var1.getPoint();
                        switch (this.tools.lMode) {
                            case -2:
                            case 100:
                                Point var13 = this.pP.moveScale(var1.getPoint());
                                MgLine var7 = new MgLine();
                                var7.create(this.tools.lMode, this.tools.lMaskHints, this.tools.lSize, this.tools.clLine, this.tools.clMask, this.tools.clAlpha, this.tools.lLayer);
                                var7.setData(new Rectangle(var13));
                                var7.draw(this, this.primary(), (Graphics) null, this.i_offs, this.pP.image_x, this.pP.image_y, this.pP.scale_x, this.pP.scale_y, this.pP.scale, this.tools.visit0, this.tools.visit1, -1);
                                this.pP.undo_in(var7);
                                return;
                            case 2:
                                return;
                            default:
                                this.start_draw(var1);
                                this.isPreD = false;
                                this.dPre(var1);
                                this.isPreD = true;
                        }
                    }
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    if (this.dHint == 0) {
                        if (this.tools.lMode == 2) {
                            this.dHint = -1;
                            this.draw_flag = 0;
                            this.isPre = false;
                            if (this.tF == null) {
                                this.tF = new TextField(15);
                                this.tF.setBackground(Color.white);
                                this.tF.setForeground(Color.blue);
                                this.tF.addActionListener(this);
                                this.tF.setVisible(false);
                                this.getParent().add(this.tF, 0);
                            }

                            Font var3 = Tools.getDefFont(this.tools.lSize - 5);
                            this.tF.setFont(var3);
                            int var4 = this.getFontMetrics(var3).getHeight() + 2;
                            Point var5 = this.getLocation();
                            Dimension var6 = this.tF.getPreferredSize();
                            this.tF.setBounds(var5.x + this.old_po.x - 4, var5.y + this.old_po.y - var4 + 2, var6.width, var6.height);
                            this.tF.setVisible(true);
                            break;
                        }

                        this.end_draw(var1);
                        this.isPre = false;
                        this.dPre(var1);
                    }

                    this.dHint = -1;
                    this.isPreD = true;
                    break;
                case MouseEvent.MOUSE_MOVED:
                    if (this.tools.lHints != 60 && this.tools.lMode != 40) {
                        this.move_draw(var1);
                    }

                    if (this.dHint == -1) {
                        this.dPre(var1);
                    }
                    break;
                case MouseEvent.MOUSE_ENTERED:
                    this.isPreD = true;
                    this.requestFocus();
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    if (this.dHint == 0) {
                        this.move_draw(var1);
                        this.isPre = false;
                        this.dPre(var1);
                        break;
                    }
                case MouseEvent.MOUSE_EXITED:
                    this.isPreD = false;
            }

            switch (this.dHint) {
                case 1:
                    this.isPreD = false;
                    this.dPre(var1);
                    this.getPixel(this.pP.moveScale(var1.getPoint()));
                    break;
                case 2:
                    this.pP.scroll(var1, this.old_po, true);
                    break;
                case 3:
                    Point var10 = var1.getPoint();
                    this.tools.setLineSize((Point) null, this.tools.lSize - (this.old_po.y - var10.y), var1.isShiftDown());
                    this.tools.m_paint((Graphics) null);
                    this.old_po = var10;
            }
        } catch (Throwable var8) {
            var8.printStackTrace();
        }

    }

    private void start_draw(MouseEvent var1) {
        try {
            int var2 = this.pP.scale;
            this.old_po = var1.getPoint();
            this.old_po2 = var1.getPoint();
            this.mg_b.create(this.tools.lMode, this.tools.lMaskHints, this.tools.lSize, this.tools.clLine, this.tools.clMask, this.tools.clAlpha, this.tools.lLayer);
            switch (this.tools.lHints) {
                case 2:
                    this.draw_flag = 0;
                    this.m_paint();
                    return;
                case 40:
                    if (this.draw_flag >= 2) {
                        this.m_paint();
                        this.old_po2.setLocation(this.rect_re.x * var2 - this.pP.scale_x * var2, this.rect_re.y * var2 - this.pP.scale_y * var2);
                        this.m_paint(this.pP.getCanvas(false), (Rectangle) null);
                    } else {
                        this.pP.getCanvas(true);
                        this.slayer = this.tools.lLayer;
                        this.draw_flag = 1;
                    }

                    return;
                case 60:
                    this.draw_flag = 1;
                    this.old_po = this.pP.moveScale(this.old_po);
                    this.old_po2 = this.old_po.getLocation();
                    this.seek_points = 0;
                    this.draw_line(this.old_po2);
                    break;
                case 61:
                    this.draw_flag = 1;
                    this.pP.getCanvas(true);
                    return;
                case 62:
                    if (this.draw_flag == 0) {
                        if (this.image_b == null) {
                            this.image_b = this.createImage(this.pP.image_x, this.pP.image_y);
                        }

                        Rectangle var3 = this.getBounds();
                        Graphics var4 = this.image_b.getGraphics();
                        Rectangle var5 = new Rectangle(0, 0, var3.width / var2, var3.height / var2);
                        this.pP.scale = 1;
                        this.m_paint(var4, var5);
                        this.pP.scale = var2;
                        var4.dispose();
                        this.draw_flag = 1;
                        this.pP.getCanvas(true);
                    }

                    return;
                default:
                    this.draw_flag = 1;
                    return;
            }
        } catch (RuntimeException var6) {
            var6.printStackTrace();
        }

    }

    private Rectangle t_getRect(Point var1, Point var2) {
        int var3 = Math.min(var1.x, var2.x);
        int var4 = Math.min(var1.y, var2.y);
        int var5 = Math.max(var1.x, var2.x);
        int var6 = Math.max(var1.y, var2.y);
        var3 = var3 < 0 ? 0 : var3;
        var4 = var4 < 0 ? 0 : var4;
        var5 = var5 > this.pP.image_x ? this.pP.image_x : var5;
        var6 = var6 > this.pP.image_y ? this.pP.image_y : var6;
        return var3 < this.pP.image_x && var4 < this.pP.image_y ? new Rectangle(var3, var4, var5 - var3, var6 - var4) : new Rectangle();
    }

    private Rectangle t_getRectT(Point var1, Point var2) {
        int var3 = Math.min(var1.x, var2.x);
        int var4 = Math.min(var1.y, var2.y);
        int var5 = Math.max(var1.x, var2.x);
        int var6 = Math.max(var1.y, var2.y);
        var3 = var3 < 0 ? 0 : var3;
        var4 = var4 < 0 ? 0 : var4;
        Dimension var7 = this.getSize();
        var5 = var5 > var7.width ? var7.width : var5;
        var6 = var6 > var7.height ? var7.height : var6;
        return var3 < var7.width && var4 < var7.height ? new Rectangle(var3, var4, var5 - var3, var6 - var4) : new Rectangle();
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
            this.pbbs.popup(true);
        } catch (Throwable var2) {
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
