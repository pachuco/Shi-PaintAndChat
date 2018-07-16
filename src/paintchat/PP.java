package paintchat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import syi.awt.Awt;
import syi.awt.LComponent;

public class PP extends LComponent implements SW {
    private ToolBox tools;
    private int Len;
    private int iW;
    private int iH;
    private M[] ms;
    private M m;
    private M.Info info;

    public void paint2(Graphics var1) {
        this.mPaint(var1);
    }

    public void mPaint(Graphics var1) {
        boolean var2 = var1 == null;
        if (var2) {
            var1 = this.getG();
        }

        Dimension var3 = this.getSize();
        int var4 = var3.width / this.iW;
        int var5 = Math.max(var3.height / this.iH, 1);
        int var6 = 0;
        int var7 = 0;
        Color var8 = Color.black;

        for (int var10 = 0; var10 < var5; ++var10) {
            for (int var11 = 0; var11 < var4; ++var11) {
                var8 = new Color(this.ms[var7].iColor);
                Color var9 = this.ms[var7].eq(this.m) ? Awt.cFSel : null;
                Awt.fillFrame(var1, false, var11 * (this.iW + 1), var6, this.iW, this.iH, var8, var8, var9, var9);
                ++var7;
                if (var7 >= this.Len) {
                    return;
                }
            }

            var6 += this.iH + 1;
        }

        if (var2) {
            var1.dispose();
        }

    }

    private int b(int var1, int var2) {
        Dimension var3 = this.getSize();
        if (var1 > 0 && var2 > 0 && var1 < var3.width - 1 && var2 < var3.height - 1) {
            int var4 = var2 / (this.iH + 1) * (var3.width / (this.iW + 1)) + var1 / (this.iW + 1);
            return var4 >= this.Len ? -1 : var4;
        } else {
            return -1;
        }
    }

    public void pMouse(MouseEvent var1) {
        int var2 = this.b(var1.getX(), var1.getY());
        if (var2 >= 0) {
            switch (var1.getID()) {
                case 501:
                    if (Awt.isR(var1)) {
                        this.ms[var2].set(this.m);
                    } else {
                        this.tools.lift();
                        int var3 = this.m.iLayer;
                        int var4 = this.m.iLayerSrc;
                        int var5 = this.m.iColorMask;
                        this.m.set(this.ms[var2]);
                        this.m.iLayer = var3;
                        this.m.iLayerSrc = var4;
                        this.m.iColorMask = var5;
                        this.tools.up();
                    }

                    this.mPaint((Graphics) null);
                default:
            }
        }
    }

    public void lift() {
    }

    public void mPack() {
        this.inParent();
    }

    public void mSetup(ToolBox var1, M.Info var2, M.User var3, M var4, Res var5, Res var6) {
        this.tools = var1;
        this.info = var2;
        this.iW = var6.getP("tp_width", 18);
        this.iH = var6.getP("tp_height", 18);
        int var7 = this.iW + 1;
        int var8 = this.iH + 1;
        int var9 = var6.getP("tp_len", 12);
        this.Len = var9;
        this.ms = new M[var9];

        for (int var10 = 0; var10 < var9; ++var10) {
            this.ms[var10] = new M();
            this.ms[var10].set(var6.getP("tp" + var10, ""));
        }

        this.m = var4;
        super.isBar = true;
        this.setTitle(var6.get("PPTitle"));
        Dimension var11 = new Dimension(var7, var8 * var9);
        this.setDimension(new Dimension(var7, var8), var11, new Dimension(var7 * var9, var8 * var9));
    }

    public void up() {
        this.repaint();
    }
}
