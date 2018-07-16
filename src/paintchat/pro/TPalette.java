package paintchat.pro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;

import paintchat.M;
import paintchat.Res;
import syi.awt.Awt;
import syi.awt.LComponent;

public class TPalette extends LComponent {
    private int lenColor = 255;
    private int iDrag = -1;
    private M.Info info;
    private M mg;
    private Tools tools;
    private Res res;
    private Res config;
    private int sizePalette = 20;
    private int selPalette = 0;
    private int oldColor = 0;
    private Color[] cls;
    private int isRGB = 1;
    private float[] fhsb = new float[3];
    private int iColor;
    private static Font clFont = null;
    private static final char[][] clValue = new char[][]{{'H', 'S', 'B', 'A'}, {'R', 'G', 'B', 'A'}};
    private static Color[][] clRGB = null;
    private static int[] DEFC = new int[]{0, 16777215, 11826549, 8947848, 16422550, 12621504, 16758527, 8421631, 2475977, 15197581, 15177261, 10079099, 16575714, 16375247, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215};

    public TPalette() {
        if (clRGB == null) {
            clRGB = new Color[][]{{Color.magenta, Color.cyan, Color.white, Color.lightGray}, {new Color(16422550), new Color(8581688), new Color(8421631), Color.lightGray}};
        }

    }

    public void changeRH() {
        int var1 = this.getRGB();
        this.isRGB = this.isRGB == 0 ? 1 : 0;
        this.setColor(var1);
    }

    public String getC() {
        try {
            StringBuffer var2 = new StringBuffer();

            for (int var3 = 0; var3 < this.cls.length; ++var3) {
                if (var3 != 0) {
                    var2.append('\n');
                }

                int var1 = this.cls[var3].getRGB();
                var2.append("#" + Integer.toHexString(-16777216 | var1 & 16777215).substring(2).toUpperCase());
            }

            return var2.toString();
        } catch (Throwable var4) {
            return null;
        }
    }

    private int getRGB() {
        return this.isRGB == 1 ? this.iColor : Color.HSBtoRGB((float) (this.iColor >>> 16 & 255) / 255.0F, (float) (this.iColor >>> 8 & 255) / 255.0F, (float) (this.iColor & 255) / 255.0F) & 16777215;
    }

    public void init(Tools var1, M.Info var2, Res var3, Res var4) {
        this.info = var2;
        this.mg = var2.m;
        this.res = var4;
        this.config = var3;
        this.tools = var1;
        this.setDimension(new Dimension((int) (42.0F * LComponent.Q), (int) (42.0F * LComponent.Q)), new Dimension((int) (112.0F * LComponent.Q), (int) (202.0F * LComponent.Q)), new Dimension((int) (300.0F * LComponent.Q), (int) (300.0F * LComponent.Q)));
    }

    public void paint2(Graphics var1) {
        try {
            this.initP();
            Dimension var2 = this.getSize();
            int var3 = Math.min((var2.height - 1) / 10, 64);
            int var4 = (int) ((float) var3 * 1.5F);
            int var5 = var3 <= 12 ? 0 : 2;
            int var6 = this.cls.length;
            int var7 = 0;
            int var8 = 0;

            int var9;
            for (var9 = 0; var9 < var6; ++var9) {
                var1.setColor(this.cls[var9]);
                var1.fillRect(var7 + 1, var8 + 1, var4 - 1 - var5, var3 - 1 - var5);
                var1.setColor(Awt.cF);
                var1.drawRect(var7, var8, var4 - var5, var3 - var5);
                if (this.selPalette == var9) {
                    var1.setColor(Awt.cFSel);
                    var1.drawRect(var7 + 1, var8 + 1, var4 - var5 - 2, var3 - var5 - 2);
                }

                if (var7 == 0) {
                    var7 += var4;
                } else {
                    var7 = 0;
                    var8 += var3;
                }
            }

            var9 = var4 * 2;
            var8 = this.pBar(var1, var9, 0, var3);
            var1.setColor(this.getBackground());
            var1.fillRect(var7 + var9, var8, var2.width - var9, var2.height - var8);
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

    }

    private int pBar(Graphics var1, int var2, int var3, int var4) {
        Dimension var5 = this.getSize();
        int var6 = var5.width - var2 - 1;
        int var10000 = var5.height - var3;
        Color var7 = this.getBackground();
        Color var8 = Awt.cFore;
        boolean var9 = this.mg.isText();
        int var10 = var9 ? 255 : this.info.getPenMask()[this.mg.iPenM].length;
        int var11 = Math.min(var4 * 6, var10 * 8 + 1);
        int var12 = this.mg.iSize;
        var12 = var12 <= 0 ? 0 : (var12 >= var10 ? var10 - 1 : var12);
        this.mg.iSize = var12;
        String var13 = var9 ? this.mg.iSize + "pt" : (int) Math.sqrt((double) this.info.getPenMask()[this.mg.iPenM][this.mg.iSize].length) + "px";
        var1.setColor(Awt.cF);
        var1.drawRect(var2, var3, var6, var11);
        int var14 = (int) ((float) var11 * ((float) (var12 + 1) / (float) var10));
        var1.setColor(this.cls[this.selPalette]);
        var1.fillRect(var2 + 1, var3 + 1, var6 - 1, var14 - 1);
        var1.setColor(var7);
        var1.fillRect(var2 + 1, var3 + 1 + var14, var6 - 1, var11 - var14 - 1);
        var1.setColor(var8);
        var1.setFont(Awt.getDefFont());
        var1.setXORMode(var7);
        var1.drawString(var13, var2 + 2, var3 + var11 - 2);
        var1.setPaintMode();
        if (clFont == null || clFont.getSize() != Math.max(var4 - 2, 1)) {
            clFont = new Font("sansserif", 0, Math.max(var4 - 4, 1));
        }

        var1.setFont(clFont);
        int var15 = this.iColor << 8 | this.mg.iAlpha;
        int var16 = 24;
        var3 += var11;

        for (int var17 = 0; var17 < 4; ++var17) {
            var1.setColor(Awt.cF);
            var1.drawRect(var2, var3 + 1, var6, var4 - 2);
            var1.setColor(Color.white);
            var1.fillRect(var2 + 1, var3 + 2, var6 - 1, 1);
            var1.fillRect(var2 + 1, var3 + 3, 1, var4 - 4);
            var14 = (int) ((float) (var6 - 2) * ((float) (var15 >>> var16 & 255) / 255.0F));
            var1.setColor(clRGB[this.isRGB][var17]);
            var1.fillRect(var2 + 2, var3 + 3, var14, var4 - 4);
            var1.setColor(Color.gray);
            var1.fillRect(var2 + 1 + var14, var3 + 3, 1, var4 - 4);
            var1.setColor(var7);
            var1.fillRect(var2 + 2 + var14, var3 + 3, var6 - var14 - 2, var4 - 4);
            var1.setColor(var8);
            var1.drawString(String.valueOf(clValue[this.isRGB][var17]) + (var15 >>> var16 & 255), var2 + 2, var3 + var4 - 2);
            var3 += var4;
            var16 -= 8;
        }

        return var3;
    }

    public void pMouse(MouseEvent var1) {
        int var2 = var1.getID();
        int var3 = var1.getX();
        int var4 = var1.getY();
        Dimension var5 = this.getSize();
        int var6 = (var5.height - 1) / 10;
        int var7 = (int) ((float) var6 * 1.5F);
        int var8 = var7 * 2 + 1;
        boolean var9 = Awt.isR(var1);
        boolean var10 = this.mg.isText();
        int var11 = var10 ? 255 : this.info.getPenMask()[this.mg.iPenM].length;
        int var12 = Math.min(var6 * 6, var11 * 8 + 1);
        if (var3 <= var8 && var2 == 501) {
            this.iDrag = -1;
            int var18 = Math.min(var4 / var6 * 2 + var3 / var7, 19);
            if (var9) {
                this.cls[var18] = new Color(this.mg.iColor);
            } else if (var1.isShiftDown()) {
                this.cls[var18] = new Color(DEFC[var18]);
            } else {
                this.selPalette = var18;
                this.tools.setRGB(this.cls[var18].getRGB());
            }

            this.repaint();
        } else {
            boolean var13 = false;
            switch (var2) {
                case 501:
                    if (var4 < var12) {
                        if (var9) {
                            this.tools.setField(this, "iPenM", "penm_", var3, var4);
                            return;
                        }

                        this.iDrag = 0;
                    } else {
                        if (var9) {
                            this.changeRH();
                            return;
                        }

                        this.iDrag = (var4 - var12) / var6;
                        this.iDrag = (this.iDrag <= 0 ? 0 : (this.iDrag >= 3 ? 3 : this.iDrag)) + 1;
                    }

                    var13 = true;
                    break;
                case 502:
                    this.iDrag = -1;
                case 503:
                case 504:
                case 505:
                default:
                    break;
                case 506:
                    if (this.iDrag >= 0) {
                        var13 = true;
                    }
            }

            if (var13) {
                if (this.iDrag == 0) {
                    this.setLineSize((int) ((float) var4 / (float) var12 * (float) var11));
                } else {
                    int var14 = (int) ((float) (var3 - var8) / (float) (var5.width - var8) * 255.0F);
                    int var15 = 24 - 8 * (this.iDrag - 1);
                    int var16 = this.iColor << 8 | this.mg.iAlpha;
                    var16 = var16 & ~(255 << var15) | Math.max(Math.min(var14, 255), 0) << var15;
                    this.iColor = var16 >>> 8;
                    this.mg.iAlpha = Math.max(var16 & 255, 1);
                    var16 = this.iColor;
                    this.tools.setRGB(this.getRGB());
                    boolean var17 = this.iColor == var16;
                    this.iColor = var16;
                    if (var17) {
                        this.repaint();
                    }
                }
            }

        }
    }

    public void setC(String var1) {
        try {
            this.initP();
            BufferedReader var2 = new BufferedReader(new StringReader(var1));
            int var3 = 0;

            while ((var1 = var2.readLine()) != null) {
                if (var3 < this.cls.length) {
                    this.cls[var3] = Color.decode(var1);
                }

                if (var3 < DEFC.length) {
                    DEFC[var3++] = this.cls[var3].getRGB() & 16777215;
                }
            }

            this.repaint();
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public void setColor(int var1) {
        var1 &= 16777215;
        boolean var2 = this.getRGB() != var1;
        if (this.isRGB == 1) {
            this.iColor = var1;
        } else {
            Color.RGBtoHSB(var1 >>> 16, var1 >>> 8 & 255, var1 & 255, this.fhsb);
            this.iColor = (int) (this.fhsb[0] * 255.0F) << 16 | (int) (this.fhsb[1] * 255.0F) << 8 | (int) (this.fhsb[2] * 255.0F);
        }

        if ((this.cls[this.selPalette].getRGB() & 16777215) != var1) {
            this.cls[this.selPalette] = new Color(this.mg.iColor);
            var2 = true;
        }

        if (var2) {
            this.repaint();
        }

    }

    public void setLineSize(int var1) {
        int var2 = this.mg.isText() ? 255 : this.info.getPenMask()[this.mg.iPenM].length;
        int var3 = this.mg.iSize;
        this.mg.iSize = Math.min(Math.max(0, var1), var2);
        if (var3 != this.mg.iSize) {
            this.repaint();
            this.tools.tP.repaint();
        }

    }

    private void initP() {
        if (this.cls == null) {
            this.cls = new Color[this.sizePalette];

            for (int var1 = 0; var1 < this.sizePalette; ++var1) {
                this.cls[var1] = new Color(this.config.getP("color_" + (var1 + 1), DEFC[var1]));
            }

        }
    }
}
