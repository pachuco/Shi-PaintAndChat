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
    private int lenColor = 0xFF;
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
    private static int[] DEFC = new int[]{
        0x000000, 0xFFFFFF, 0xB47575, 0x888888, 0xFA9696, 0xC096C0, 0xFFB6FF, 0x8080FF, 0x25C7C9, 0xE7E58D,
        0xE7962D, 0x99CB7B, 0xFCECE2, 0xF9DDCF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF
    };

    public TPalette() {
        if (clRGB == null) {
            clRGB = new Color[][]{
                    {Color.magenta, Color.cyan, Color.white, Color.lightGray},
                    {new Color(0xfa9696), new Color(0x82f238), new Color(0x8080ff), Color.lightGray}
            };
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
                var2.append("#" + Integer.toHexString(0xFF000000 | var1 & 0xFFFFFF).substring(2).toUpperCase());
            }

            return var2.toString();
        } catch (Throwable var4) {
            return null;
        }
    }

    private int getRGB() {
        return this.isRGB == 1 ? this.iColor : Color.HSBtoRGB((float) (this.iColor >>> 16 & 0xFF) / 255.0F, (float) (this.iColor >>> 8 & 0xFF) / 255.0F, (float) (this.iColor & 0xFF) / 255.0F) & 0xFFFFFF;
    }

    public void init(Tools var1, M.Info var2, Res var3, Res var4) {
        this.info = var2;
        this.mg = var2.m;
        this.res = var4;
        this.config = var3;
        this.tools = var1;
        this.setDimension(new Dimension((int) (42.0F * LComponent.Q), (int) (42.0F * LComponent.Q)), new Dimension((int) (112.0F * LComponent.Q), (int) (202.0F * LComponent.Q)), new Dimension((int) (300.0F * LComponent.Q), (int) (300.0F * LComponent.Q)));
    }

    public void paint2(Graphics g) {
        try {
            this.initP();
            Dimension dim = this.getSize();
            int var3 = Math.min((dim.height - 1) / 10, 64);
            int var4 = (int) ((float) var3 * 1.5F);
            int var5 = var3 <= 12 ? 0 : 2;
            int var6 = this.cls.length;
            int var7 = 0;
            int var8 = 0;

            for (int i = 0; i < var6; ++i) {
                g.setColor(this.cls[i]);
                g.fillRect(var7 + 1, var8 + 1, var4 - 1 - var5, var3 - 1 - var5);
                g.setColor(Awt.cF);
                g.drawRect(var7, var8, var4 - var5, var3 - var5);
                if (this.selPalette == i) {
                    g.setColor(Awt.cFSel);
                    g.drawRect(var7 + 1, var8 + 1, var4 - var5 - 2, var3 - var5 - 2);
                }

                if (var7 == 0) {
                    var7 += var4;
                } else {
                    var7 = 0;
                    var8 += var3;
                }
            }

            int var9 = var4 * 2;
            var8 = this.pBar(g, var9, 0, var3);
            g.setColor(this.getBackground());
            g.fillRect(var7 + var9, var8, dim.width - var9, dim.height - var8);
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

    }

    private int pBar(Graphics g, int var2, int var3, int var4) {
        Dimension dim = this.getSize();
        int var6 = dim.width - var2 - 1;
        int var10000 = dim.height - var3;
        Color colBckg = this.getBackground();
        Color colFore = Awt.cFore;
        boolean var9 = this.mg.isText();
        int var10 = var9 ? 0xFF : this.info.getPenMask()[this.mg.iPenM].length;
        int var11 = Math.min(var4 * 6, var10 * 8 + 1);
        int var12 = this.mg.iSize;
        var12 = var12 <= 0 ? 0 : (var12 >= var10 ? var10 - 1 : var12);
        this.mg.iSize = var12;
        String var13 = var9 ? this.mg.iSize + "pt" : (int) Math.sqrt((double) this.info.getPenMask()[this.mg.iPenM][this.mg.iSize].length) + "px";
        g.setColor(Awt.cF);
        g.drawRect(var2, var3, var6, var11);
        int var14 = (int) ((float) var11 * ((float) (var12 + 1) / (float) var10));
        g.setColor(this.cls[this.selPalette]);
        g.fillRect(var2 + 1, var3 + 1, var6 - 1, var14 - 1);
        g.setColor(colBckg);
        g.fillRect(var2 + 1, var3 + 1 + var14, var6 - 1, var11 - var14 - 1);
        g.setColor(colFore);
        g.setFont(Awt.getDefFont());
        g.setXORMode(colBckg);
        g.drawString(var13, var2 + 2, var3 + var11 - 2);
        g.setPaintMode();
        if (clFont == null || clFont.getSize() != Math.max(var4 - 2, 1)) {
            clFont = new Font("sansserif", 0, Math.max(var4 - 4, 1));
        }

        g.setFont(clFont);
        int var15 = this.iColor << 8 | this.mg.iAlpha;
        int var16 = 24;
        var3 += var11;

        for (int i = 0; i < 4; ++i) {
            g.setColor(Awt.cF);
            g.drawRect(var2, var3 + 1, var6, var4 - 2);
            g.setColor(Color.white);
            g.fillRect(var2 + 1, var3 + 2, var6 - 1, 1);
            g.fillRect(var2 + 1, var3 + 3, 1, var4 - 4);
            var14 = (int) ((float) (var6 - 2) * ((float) (var15 >>> var16 & 0xFF) / 255.0F));
            g.setColor(clRGB[this.isRGB][i]);
            g.fillRect(var2 + 2, var3 + 3, var14, var4 - 4);
            g.setColor(Color.gray);
            g.fillRect(var2 + 1 + var14, var3 + 3, 1, var4 - 4);
            g.setColor(colBckg);
            g.fillRect(var2 + 2 + var14, var3 + 3, var6 - var14 - 2, var4 - 4);
            g.setColor(colFore);
            g.drawString(String.valueOf(clValue[this.isRGB][i]) + (var15 >>> var16 & 0xFF), var2 + 2, var3 + var4 - 2);
            var3 += var4;
            var16 -= 8;
        }

        return var3;
    }

    public void pMouse(MouseEvent event) {
        int eventID = event.getID();
        int mouseX = event.getX();
        int mouseY = event.getY();
        Dimension var5 = this.getSize();
        int var6 = (var5.height - 1) / 10;
        int var7 = (int) ((float) var6 * 1.5F);
        int var8 = var7 * 2 + 1;
        boolean var9 = Awt.isR(event);
        boolean var10 = this.mg.isText();
        int var11 = var10 ? 0xFF : this.info.getPenMask()[this.mg.iPenM].length;
        int var12 = Math.min(var6 * 6, var11 * 8 + 1);
        if (mouseX <= var8 && eventID == MouseEvent.MOUSE_PRESSED) {
            this.iDrag = -1;
            int var18 = Math.min(mouseY / var6 * 2 + mouseX / var7, 19);
            if (var9) {
                this.cls[var18] = new Color(this.mg.iColor);
            } else if (event.isShiftDown()) {
                this.cls[var18] = new Color(DEFC[var18]);
            } else {
                this.selPalette = var18;
                this.tools.setRGB(this.cls[var18].getRGB());
            }

            this.repaint();
        } else {
            boolean var13 = false;
            switch (eventID) {
                case MouseEvent.MOUSE_PRESSED:
                    if (mouseY < var12) {
                        if (var9) {
                            this.tools.setField(this, "iPenM", "penm_", mouseX, mouseY);
                            return;
                        }

                        this.iDrag = 0;
                    } else {
                        if (var9) {
                            this.changeRH();
                            return;
                        }

                        this.iDrag = (mouseY - var12) / var6;
                        this.iDrag = (this.iDrag <= 0 ? 0 : (this.iDrag >= 3 ? 3 : this.iDrag)) + 1;
                    }

                    var13 = true;
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    this.iDrag = -1;
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                default:
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    if (this.iDrag >= 0) {
                        var13 = true;
                    }
            }

            if (var13) {
                if (this.iDrag == 0) {
                    this.setLineSize((int) ((float) mouseY / (float) var12 * (float) var11));
                } else {
                    int var14 = (int) ((float) (mouseX - var8) / (float) (var5.width - var8) * 255.0F);
                    int var15 = 24 - 8 * (this.iDrag - 1);
                    int var16 = this.iColor << 8 | this.mg.iAlpha;
                    var16 = var16 & ~(0xFF << var15) | Math.max(Math.min(var14, 0xFF), 0) << var15;
                    this.iColor = var16 >>> 8;
                    this.mg.iAlpha = Math.max(var16 & 0xFF, 1);
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
                    DEFC[var3++] = this.cls[var3].getRGB() & 0xFFFFFF;
                }
            }

            this.repaint();
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public void setColor(int var1) {
        var1 &= 0xFFFFFF;
        boolean var2 = this.getRGB() != var1;
        if (this.isRGB == 1) {
            this.iColor = var1;
        } else {
            Color.RGBtoHSB(var1 >>> 16, var1 >>> 8 & 0xFF, var1 & 0xFF, this.fhsb);
            this.iColor = (int) (this.fhsb[0] * 255.0F) << 16 | (int) (this.fhsb[1] * 255.0F) << 8 | (int) (this.fhsb[2] * 255.0F);
        }

        if ((this.cls[this.selPalette].getRGB() & 0xFFFFFF) != var1) {
            this.cls[this.selPalette] = new Color(this.mg.iColor);
            var2 = true;
        }

        if (var2) {
            this.repaint();
        }

    }

    public void setLineSize(int var1) {
        int var2 = this.mg.isText() ? 0xFF : this.info.getPenMask()[this.mg.iPenM].length;
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
