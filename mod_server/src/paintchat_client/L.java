package paintchat_client;

import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;

import paintchat.LO;
import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;
import syi.awt.Awt;
import syi.awt.LComponent;

public class L extends LComponent implements ActionListener, ItemListener {
    private Mi mi;
    private ToolBox tool;
    private Res res;
    private M m;
    private int B = -1;
    private Font bFont;
    private int bH;
    private int bW;
    private int base;
    private int layer_size = -1;
    private int mouse = -1;
    private boolean isASlide = false;
    private int Y;
    private int YOFF;
    private PopupMenu popup = null;
    private String strMenu;
    private boolean is_pre = true;
    private boolean is_DIm = false;
    private Color cM;
    private Color cT;
    private String sL;

    public L(Mi var1, ToolBox var2, Res var3, Res var4) {
        this.tool = var2;
        this.bFont = Awt.getDefFont();
        this.bFont = new Font(this.bFont.getName(), 0, (int) ((float) this.bFont.getSize() * 0.8F));
        FontMetrics var5 = this.getFontMetrics(this.bFont);
        this.bH = var5.getHeight() + 6;
        this.base = this.bH - 2 - var5.getMaxDescent();
        int var6 = (int) (60.0F * LComponent.Q);
        String var7 = var3.res("Layer");
        this.sL = var7;
        this.strMenu = var3.res("MenuLayer");
        this.cM = new Color(var4.getP("l_m_color", 0));
        this.cT = new Color(var4.getP("l_m_color_text", 16777215));
        var5 = this.getFontMetrics(this.bFont);
        var6 = Math.max(var5.stringWidth(var7 + "00") + 4, var6);
        var6 = Math.max(var5.stringWidth(this.strMenu) + 4, var6);
        this.bW = var6;
        int var10000 = var6 + this.bH + 100;
        this.mi = var1;
        this.res = var3;
        this.setTitle(var7);
        super.isGUI = true;
        this.m = var1.info.m;
        Dimension var8 = new Dimension(this.bW, this.bH);
        this.setDimension(new Dimension(var8), var8, new Dimension());
        this.setSize(this.getMaximumSize());
    }

    public void actionPerformed(ActionEvent var1) {
        try {
            String var2 = var1.getActionCommand();
            int var3 = this.popup.getItemCount();

            int var4;
            for (var4 = 0; var4 < var3 && !this.popup.getItem(var4).getLabel().equals(var2); ++var4) {
            }

            M.Info var5 = this.mi.info;
            M var6 = this.mg();
            this.setA(var6);
            LO[] var7 = var5.layers;
            int var8 = var5.L;
            byte[] var9 = new byte[4];
            boolean var10 = false;
            boolean var11 = false;
            int var12 = this.mi.user.wait;
            this.mi.user.wait = -2;
            if (this.popup.getName().charAt(0) == 'm') {
                switch (var4) {
                    case 0:
                        var6.setRetouch(new int[]{1, var8 + 1}, (byte[]) null, 0, false);
                        var10 = true;
                        var11 = true;
                        break;
                    case 1:
                        if (var5.L > 1 && this.confirm(var7[var6.iLayer].name + this.res.res("DelLayerQ"))) {
                            var6.iLayerSrc = var6.iLayer;
                            var6.setRetouch(new int[]{2}, (byte[]) null, 0, false);
                            var10 = true;
                            break;
                        }

                        return;
                    case 2:
                        this.dFusion();
                        break;
                    case 3:
                        this.config(this.m.iLayer);
                }
            } else if (var4 == 0) {
                var6.iHint = 14;
                var6.setRetouch(new int[]{3}, (byte[]) null, 0, false);
                var10 = true;
            } else {
                byte var13 = (byte) var7[var6.iLayerSrc].iCopy;
                if (var13 == 1) {
                    this.dFusion();
                } else {
                    var6.iHint = 3;
                    var6.iPen = 20;
                    var9[0] = var13;
                    var6.setRetouch(new int[]{0, var5.W << 16 | var5.H}, var9, 4, false);
                    var10 = true;
                }
            }

            if (var10) {
                var6.draw();
                if (var11) {
                    var5.layers[var5.L - 1].makeName(this.sL);
                }

                this.mi.send(var6);
            }

            this.m.iLayerSrc = this.m.iLayer = Math.min(this.m.iLayer, var5.L - 1);
            this.repaint();
            this.mi.user.wait = var12;
            this.mi.repaint();
        } catch (Throwable var14) {
            var14.printStackTrace();
        }

    }

    private int b(int var1) {
        return var1 < this.bH ? 0 : Math.max(this.mi.info.L - (var1 / this.bH - 1), 1);
    }

    private void send(int[] var1, byte[] var2) {
        M var3 = this.mg();
        this.setA(var3);
        var3.setRetouch(var1, var2, var2 != null ? var2.length : 0, false);
        int var4 = this.mi.user.wait;

        try {
            var3.draw();
            this.mi.send(var3);
        } catch (Throwable var5) {
        }

        this.repaint();
        this.mi.user.wait = var4;
        this.mi.repaint();
    }

    private void dFusion() {
        if (this.confirm(this.res.res("CombineVQ"))) {
            try {
                int var1 = this.mi.info.L;
                LO[] var2 = this.mi.info.layers;
                int var3 = 0;

                int var4;
                for (var4 = 0; var4 < var1; ++var4) {
                    if (var2[var4].iAlpha > 0.0F) {
                        ++var3;
                    }
                }

                if (var3 <= 0) {
                    return;
                }

                var4 = this.mi.user.wait;
                M var5 = this.mg();
                this.setA(var5);
                byte[] var7 = new byte[var3 * 4 + 2];
                int var8 = 2;
                var7[0] = (byte) var3;

                for (int var9 = 0; var9 < var1; ++var9) {
                    LO var6 = var2[var9];
                    if (var6.iAlpha > 0.0F) {
                        var7[var8++] = (byte) var9;
                        var7[var8++] = (byte) ((int) (var6.iAlpha * 255.0F));
                        var7[var8++] = (byte) var6.iCopy;
                        var7[var8++] = 41;
                    }
                }

                this.mi.user.wait = -2;
                var5.setRetouch(new int[]{7}, var7, var7.length, false);
                var5.draw();
                this.mi.send(var5);
                this.mi.user.wait = var4;
            } catch (Throwable var10) {
                var10.printStackTrace();
            }

        }
    }

    private boolean confirm(String var1) {
        return Me.confirm(var1, true);
    }

    private void dL(Graphics var1, int var2, int var3) {
        if (this.mi.info.L > var3) {
            this.getSize();
            int var4 = this.bW - 1;
            int var5 = this.bH - 2;
            Color var6 = this.m.iLayer == var3 ? Awt.cFSel : super.clFrame;
            LO var7 = this.mi.info.layers[var3];
            var1.setColor(var6);
            var1.drawRect(0, var2, var4, var5);
            var1.setColor(Awt.cFore);
            var1.setFont(this.bFont);
            var1.drawString((String) var7.name, 2, var2 + this.base);
            var1.setColor(var6);
            var1.drawRect(this.bW, var2, 100, var5);
            var1.setColor(this.cM);
            int var8 = (int) (100.0F * var7.iAlpha);
            var1.fillRect(this.bW + 1, var2 + 1, var8 - 1, var5 - 1);
            var1.setColor(this.cT);
            var1.drawString(var8 + "%", this.bW + 3, var2 + this.base);
            int var9 = this.bW + 100;
            var1.setColor(var6);
            var1.drawRect(var9 + 1, var2, var5 - 2, var5);
            var1.setColor(Awt.cFore);
            if (var8 == 0) {
                var1.drawLine(var9 + 2, var2 + 1, var9 + var5 - 2, var2 + var5 - 1);
                var1.drawLine(1, var2 + 1, var4 - 1, var2 + this.bH - 3);
            } else {
                var1.drawOval(var9 + 2, var2 + 2, var5 - 3, var5 - 3);
            }

        }
    }

    public Dimension getMaximumSize() {
        Dimension var1 = super.getMaximumSize();
        if (this.mi != null) {
            var1.setSize(this.bW + 100 + this.bH, this.bH * (this.mi.info.L + 1));
        }

        return var1;
    }

    public void itemStateChanged(ItemEvent var1) {
        this.is_pre = !this.is_pre;
    }

    private M mg() {
        M var1 = new M(this.mi.info, this.mi.user);
        var1.iAlpha = 255;
        var1.iHint = 14;
        var1.iLayer = this.m.iLayer;
        var1.iLayerSrc = this.m.iLayerSrc;
        return var1;
    }

    private void p() {
        this.repaint();
        this.tool.up();
    }

    public void paint2(Graphics var1) {
        try {
            int var2 = this.mi.info.L;

            for (int var4 = 0; var4 < var2; ++var4) {
                LO var3 = this.mi.info.layers[var4];
                if (var3.name == null) {
                    var3.makeName(this.sL);
                }
            }

            if (this.layer_size != var2) {
                this.layer_size = var2;
                this.setSize(this.getMaximumSize());
                return;
            }

            Dimension var8 = this.getSize();
            int var5 = var2 - 1;
            int var6 = this.bH;
            var1.setFont(this.bFont);
            var1.setColor(Awt.cBk);
            var1.fillRect(0, 0, var8.width, var8.height);

            while (var6 < var8.height) {
                if (this.isASlide || var5 != this.mouse - 1) {
                    this.dL(var1, var6, var5);
                }

                --var5;
                if (var5 < 0) {
                    break;
                }

                var6 += this.bH;
            }

            if (!this.isASlide && this.mouse > 0) {
                this.dL(var1, this.Y - this.YOFF, this.mouse - 1);
            }

            Awt.drawFrame(var1, this.mouse == 0, 0, 0, this.bW, this.bH - 2);
            var1.setColor(Awt.cFore);
            var1.drawString((String) this.strMenu, 3, this.bH - 6);
        } catch (Throwable var7) {
        }

    }

    public void pMouse(MouseEvent var1) {
        try {
            int var2 = this.Y = var1.getY();
            int var3 = var1.getX();
            M.Info var4 = this.mi.info;
            boolean var5 = Awt.isR(var1);
            int var8;
            int var17;
            switch (var1.getID()) {
                case 501:
                    if (this.mouse < 0) {
                        int var6 = this.b(var2);
                        int var7 = var6 - 1;
                        if (var7 >= 0) {
                            if (var3 > this.bW + 100 + 1) {
                                var8 = this.mi.user.wait;
                                this.mi.user.wait = -2;
                                if (var5) {
                                    for (var17 = 0; var17 < var4.L; ++var17) {
                                        this.setAlpha(var17, var17 == var7 ? 255 : 0, true);
                                    }
                                } else {
                                    this.setAlpha(var7, var4.layers[var7].iAlpha == 0.0F ? 255 : 0, true);
                                }

                                this.mi.user.wait = var8;
                                this.mi.repaint();
                                this.p();
                            } else if (var1.getClickCount() < 2 && !var5) {
                                this.isASlide = var3 >= this.bW;
                                this.mouse = var6;
                                this.m.iLayer = this.m.iLayerSrc = var7;
                                this.YOFF = var2 % this.bH;
                                if (this.isASlide) {
                                    this.setAlpha(var7, (int) ((float) (var3 - this.bW) / 100.0F * 255.0F), false);
                                } else {
                                    this.p();
                                }
                            } else {
                                this.config(var7);
                                this.mi.repaint();
                            }
                        } else {
                            this.m.iLayerSrc = this.m.iLayer;
                            if (var3 < this.bW && var2 > 2) {
                                this.popup(new String[]{"AddLayer", "DelLayer", "CombineV", "PropertyLayer"}, var3, var2, true);
                            }
                        }
                    }
                    break;
                case 502:
                    if (!var5) {
                        if (this.isASlide) {
                            this.setAlpha(this.m.iLayer, (int) ((float) (var3 - this.bW) / 100.0F * 255.0F), true);
                            this.mouse = -1;
                            this.isASlide = false;
                        } else {
                            var8 = this.mouse - 1;
                            var17 = this.b(this.Y) - 1;
                            if (var8 >= 0 && var17 >= 0 && var8 != var17) {
                                this.m.iLayer = var17;
                                this.m.iLayerSrc = var8;
                                this.popup(new String[]{this.res.res("Shift"), this.res.res("Combine")}, var3, var2, false);
                            }

                            this.mouse = -1;
                            this.repaint();
                        }
                    }
                    break;
                case 503:
                    var8 = this.b(var2) - 1;
                    if (!this.is_pre || var8 < 0 || var3 >= this.bW) {
                        if (this.is_DIm) {
                            this.is_DIm = false;
                            this.repaint();
                        }

                        return;
                    }

                    this.is_DIm = true;
                    Dimension var9 = this.getSize();
                    int var10 = this.mi.info.W;
                    int var11 = this.mi.info.H;
                    int[] var12 = this.mi.info.layers[var8].offset;
                    Graphics var13 = this.getG();
                    int var14 = Math.min(var9.width - this.bW - 1, var9.height - 1);
                    if (var12 == null) {
                        var13.setColor(Color.white);
                        var13.fillRect(this.bW + 1, 1, var14 - 1, var14 - 1);
                    } else {
                        Image var15 = this.getToolkit().createImage((ImageProducer) (new MemoryImageSource(var10, var11, new DirectColorModel(24, 16711680, 65280, 255), var12, 0, var10)));
                        var13.drawImage(var15, this.bW + 1, 1, var14 - 1, var14 - 1, (ImageObserver) null);
                        var15.flush();
                    }

                    var13.setColor(Color.black);
                    var13.drawRect(this.bW, 0, var14, var14);
                    var13.dispose();
                case 504:
                case 505:
                default:
                    break;
                case 506:
                    if (this.mouse > 0) {
                        if (this.isASlide) {
                            this.setAlpha(this.m.iLayer, (int) ((float) (var3 - this.bW) / 100.0F * 255.0F), false);
                        } else {
                            this.m.iLayer = this.b(this.Y) - 1;
                            this.repaint();
                        }
                    }
            }
        } catch (Throwable var16) {
            var16.printStackTrace();
        }

    }

    private void popup(String[] var1, int var2, int var3, boolean var4) {
        if (this.mi.info.isLEdit) {
            if (this.popup == null) {
                this.popup = new PopupMenu();
                this.popup.addActionListener(this);
                this.add(this.popup);
            } else {
                this.popup.removeAll();
            }

            for (int var5 = 0; var5 < var1.length; ++var5) {
                this.popup.add(this.res.res(var1[var5]));
            }

            if (var4) {
                this.popup.addSeparator();
                CheckboxMenuItem var6 = new CheckboxMenuItem(this.res.res("IsPreview"), this.is_pre);
                var6.addItemListener(this);
                this.popup.add((MenuItem) var6);
                this.popup.setName("m");
            } else {
                this.popup.setName("l");
            }

            this.popup.show(this, var2, var3);
        }
    }

    private void setA(M var1) {
        var1.iAlpha2 = (int) (this.mi.info.layers[var1.iLayer].iAlpha * 255.0F) << 8 | (int) (this.mi.info.layers[var1.iLayerSrc].iAlpha * 255.0F);
    }

    public void setAlpha(int var1, int var2, boolean var3) throws Throwable {
        var2 = var2 <= 0 ? 0 : (var2 >= 255 ? 255 : var2);
        if ((float) var2 != this.mi.info.layers[var1].iAlpha) {
            if (var3) {
                int var4 = this.m.iLayer;
                this.m.iLayer = var1;
                this.send(new int[]{8}, new byte[]{(byte) var2});
                this.m.iLayer = var4;
            } else {
                this.mi.info.layers[var1].iAlpha = (float) var2 / 255.0F;
                this.mi.repaint();
                this.repaint();
            }

        }
    }

    public void config(int var1) {
        LO var2 = this.mi.info.layers[var1];
        Choice var3 = new Choice();
        var3.add(this.res.res("Normal"));
        var3.add(this.res.res("Multiply"));
        var3.add(this.res.res("Reverse"));
        var3.select(var2.iCopy);
        TextField var4 = new TextField(var2.name);
        Me var5 = Me.getMe();
        Panel var6 = new Panel(new GridLayout(0, 1));
        var6.add(var4);
        var6.add(var3);
        var4.addActionListener(var5);
        var5.add((Component) var6, (Object) "Center");
        var5.pack();
        Awt.moveCenter(var5);
        var5.setVisible(true);
        if (var5.isOk) {
            String var7 = var4.getText();
            if (!var7.equals(var2.name)) {
                try {
                    this.send(new int[]{10}, var7.getBytes("UTF8"));
                } catch (Throwable var9) {
                }
            }

            int var8 = var3.getSelectedIndex();
            if (var2.iCopy != var8) {
                this.send(new int[]{9}, new byte[]{(byte) var8});
            }

            this.repaint();
        }

    }
}
