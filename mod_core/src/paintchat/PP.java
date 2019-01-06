package paintchat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import syi.awt.Awt;
import syi.awt.LComponent;

import static java.awt.event.MouseEvent.*;

public class PP extends LComponent implements SW {
    private ToolBox tools;
    private int Len;
    private int iW;
    private int iH;
    private M[] ms;
    private M m;
    private M.Info info;

    public void paint2(Graphics g) {
        this.mPaint(g);
    }

    public void mPaint(Graphics g) {
        boolean hasGraphics = g == null;
        if (hasGraphics) {
            g = this.getG();
        }

        Dimension dim = this.getSize();
        int width = dim.width / this.iW;
        int height = Math.max(dim.height / this.iH, 1);
        int y = 0;
        int x = 0;
        Color colorBlack = Color.black;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                colorBlack = new Color(this.ms[x].iColor);
                Color color = this.ms[x].eq(this.m) ? Awt.cFSel : null;
                Awt.fillFrame(g, false, j * (this.iW + 1), y, this.iW, this.iH, colorBlack, colorBlack, color, color);
                ++x;
                if (x >= this.Len) {
                    return;
                }
            }

            y += this.iH + 1;
        }

        if (hasGraphics) {
            g.dispose();
        }

    }

    private int b(int x, int y) {
        Dimension dim = this.getSize();
        if (x > 0 && y > 0 && x < dim.width - 1 && y < dim.height - 1) {
            int var4 = y / (this.iH + 1) * (dim.width / (this.iW + 1)) + x / (this.iW + 1);
            return var4 >= this.Len ? -1 : var4;
        } else {
            return -1;
        }
    }

    public void pMouse(MouseEvent evt) {
        int var2 = this.b(evt.getX(), evt.getY());
        if (var2 >= 0) {
            switch (evt.getID()) {
                case MOUSE_PRESSED:
                    if (Awt.isR(evt)) {
                        this.ms[var2].set(this.m);
                    } else {
                        this.tools.lift();
                        int layer = this.m.iLayer;
                        int layerSrc = this.m.iLayerSrc;
                        int mask = this.m.iColorMask;
                        this.m.set(this.ms[var2]);
                        this.m.iLayer = layer;
                        this.m.iLayerSrc = layerSrc;
                        this.m.iColorMask = mask;
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

    public void mSetup(ToolBox tools, M.Info info, M.User user, M mg, Res res, Res config) {
        this.tools = tools;
        this.info = info;
        this.iW = config.getP("tp_width", 18);
        this.iH = config.getP("tp_height", 18);
        int width = this.iW + 1;
        int height = this.iH + 1;
        int len = config.getP("tp_len", 12);
        this.Len = len;
        this.ms = new M[len];

        for (int i = 0; i < len; ++i) {
            this.ms[i] = new M();
            this.ms[i].set(config.getP("tp" + i, ""));
        }

        this.m = mg;
        super.isBar = true;
        this.setTitle(config.get("PPTitle"));
        Dimension dim = new Dimension(width, height * len);
        this.setDimension(new Dimension(width, height), dim, new Dimension(width * len, height * len));
    }

    public void up() {
        this.repaint();
    }
}
