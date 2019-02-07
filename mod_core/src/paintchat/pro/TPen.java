package paintchat.pro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;

import paintchat.M;
import paintchat.Res;
import syi.awt.Awt;
import syi.awt.LComponent;

import static syi.C.EngineM.*;

public class TPen extends LComponent implements Runnable {
    private Tools tools;
    private int iType = 0;
    private boolean isRun = false;
    private LComponent[] cs;
    private TPen tPen;
    private M.Info info;
    private M mg;
    private Res config;
    private Image image = null;
    private Image[] images = null;
    private boolean isDrag = false;
    private int selButton = 0;
    private int selWhite;
    private int selPen = 0;
    private int imW = 0;
    private int imH = 0;
    private int imCount;
    private int selItem = -1;
    private M[] mgs = null;
    private ColorModel cmDef;
    private int sizeTT = 0;

    public TPen(Tools var1, M.Info var2, Res config, TPen var4, LComponent[] var5) {
        this.tools = var1;
        this.info = var2;
        this.mg = this.info.m;
        this.config = config;
        this.tPen = var4;
        this.cs = var5;
        super.isBar = true;
    }

    private int getIndex(int x, int y, int var3) {
        Dimension dim = this.getSize();
        int width = this.imW;
        int height = this.imH;
        if (this.iType != 2) {
            width += 3;
            height += 3;
        }

        x -= var3;
        int var7 = (dim.width - var3) / width;
        return y / height * var7 + Math.min(x / width, var7);
    }

    public void init(int var1) {
        this.iType = var1;
        Res var2 = this.config;
        ++var1;
        int var4 = 30;
        int var5 = 30;
        String var6 = String.valueOf(var1);

        int var7;
        for (var7 = 0; var2.get((Object)(String.valueOf('t') + var6 + var7)) != null; ++var7) {
            ;
        }
        if (var7 != 0) {
            this.mgs = new M[var7];

            int var8;
            for (var8 = 0; var8 < var7; ++var8) {
                M var3 = new M();
                var3.set(var2.get(String.valueOf('t') + var6 + var8));
                this.mgs[var8] = var3;
            }

            for (var8 = var7 - 1; var8 >= 0; --var8) {
                if (this.mgs[var8].iPen == P_WHITE || this.mgs[var8].iPen == P_SWHITE) {
                    this.selWhite = var8;
                }
            }

            String var9 = "res/" + var1 + ".gif";
            this.image = this.getToolkit().createImage((byte[]) var2.getRes(var9));
            Awt.wait(this.image);
            var2.remove(var9);
            var4 = (int) ((float) this.image.getWidth((ImageObserver) null) * LComponent.Q);
            var5 = (int) ((float) (this.image.getHeight((ImageObserver) null) / var7) * LComponent.Q);
            if (LComponent.Q != 1.0F) {
                this.image = Awt.toMin(this.image, var4, var5 * var7);
            }

            this.imW = var4;
            this.imH = var5;
            this.imCount = var7;
        } else {
            this.imCount = 0;
            this.imW = 20;
            this.imH = 20;
        }

        var4 += 3;
        var5 += 3;
        this.selItem = -1;
        this.setItem(0, (M) null);
        this.setDimension(new Dimension(var4 + 1, var5 + 1), new Dimension(var4 + 1, var5 * var7 + 1), new Dimension(var4 * var7 + 1, var5 * var7 + 1));
    }

    public void initHint() {
        try {
            String var1 = "res/3.gif";
            this.iType = 3;
            this.imCount = 7;
            int var2 = this.imCount;
            this.image = this.getToolkit().createImage((byte[]) this.config.getRes(var1));
            Awt.wait(this.image);
            this.config.remove(var1);
            int var3 = this.image.getWidth((ImageObserver) null);
            int var4 = this.image.getHeight((ImageObserver) null);
            if (LComponent.Q != 1.0F) {
                var3 = (int) ((float) var3 * LComponent.Q);
                var4 = (int) ((float) var4 * LComponent.Q) / var2 * var2;
                this.image = Awt.toMin(this.image, var3, var4);
            }

            var4 /= var2;
            this.imW = var3;
            this.imH = var4;
            var3 += 3;
            var4 += 3;
            this.setDimension(new Dimension(var3 + 1, var4 + 1), new Dimension(var3 + 1, var4 * var2 + 1), new Dimension(var3 * var2 + 1, var4 * var2 + 1));
        } catch (RuntimeException var5) {
            var5.printStackTrace();
        }

    }

    public void initTT() {
        this.iType = 2;
        Res var1 = this.config;
        this.getToolkit();
        this.cmDef = new DirectColorModel(24, 65280, 65280, 255);
        this.imW = this.imH = (int) (34.0F * LComponent.Q);

        try {
            String var2 = "tt_size";
            this.images = new Image[Integer.parseInt(var1.get(var2))];
            var1.remove(var2);
            int var3 = this.imW * 5 + 1;
            int var4 = ((this.images.length + 12) / 5 + 1) * this.imW + 1;
            this.setDimension(new Dimension(this.imW + 1, this.imW + 1), new Dimension(var3, var4), new Dimension(var3 * 2, var4 * 2));
        } catch (RuntimeException var5) {
            ;
        }

    }

    private void mouseH(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            int var2 = this.getIndex(event.getX(), event.getY(), 0);
            if (var2 < 7) {
                this.mg.iHint = var2;
                this.repaint();
            }
        }
    }

    private void mousePen(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            int var2 = this.getIndex(event.getX(), event.getY(), 0);
            if (var2 >= this.imCount) {
                return;
            }

            this.setItem(var2, (M) null);
        }

    }

    private void mouseTT(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            this.getSize();
            int var2 = this.getIndex(event.getX(), event.getY(), 0);
            if (var2 >= this.images.length + 12) {
                return;
            }

            this.mg.iTT = var2;
            this.repaint();
        }

    }

    public void paint2(Graphics g) {
        switch (this.iType) {
            case 2:
                this.paintTT(g);
                break;
            case 3:
                this.selItem = this.mg.iHint;
            default:
                this.paintPen(g);
        }

    }

    private void paintPen(Graphics g) {
        if (this.image != null) {
            int var2 = 0;
            int var3 = 0;
            int w = this.imW;
            int h = this.imH;
            int wPadded = this.imW + 3;
            int hPadded = this.imH + 3;
            Dimension dim = this.getSize();

            for (int i = 0; i < this.imCount; ++i) {
                g.setColor(this.selItem == i ? Awt.cFSel : Awt.cF);
                g.drawRect(var2 + 1, var3 + 1, w + 1, h + 1);
                g.drawImage(this.image, var2 + 2, var3 + 2, var2 + w + 2, var3 + h + 2, 0, i * h, w, (i + 1) * h, null);
                if (this.selItem == i) {
                    g.setColor(Color.black);
                    g.fillRect(var2 + 2, var3 + 2, w, 1);
                    g.fillRect(var2 + 2, var3 + 3, 1, h - 1);
                }

                var2 = var2 + wPadded * 2 >= dim.width ? 0 : var2 + wPadded;
                var3 = var2 == 0 ? var3 + hPadded : var3;
                if (var3 + hPadded >= dim.height) break;
            }

        }
    }

    private void paintTT(Graphics g) {
        if (this.images != null) {
            if (!this.isRun) {
                Thread var2 = new Thread(this);
                var2.setPriority(1);
                var2.setDaemon(true);
                var2.start();
                this.isRun = true;
            }

            int var19 = this.images.length + 11;
            int var3 = 0;
            int var4 = 0;
            int w = this.imW;
            int h = this.imH;
            int var7 = w - 3;
            int[] var10 = this.tools.iBuffer;
            Dimension var11 = this.getSize();
            this.getToolkit();
            int var12 = this.getBackground().getRGB();

            for (int var14 = -1; var14 < var19; ++var14) {
                g.setColor(var14 + 1 == this.mg.iTT ? Awt.cFSel : Awt.cF);
                g.drawRect(var3 + 1, var4 + 1, w - 2, h - 2);
                if (var14 == -1) {
                    g.setColor(Color.blue);
                    g.fillRect(var3 + 2, var4 + 2, w - 3, h - 3);
                } else if (var14 < 11) {
                    synchronized (var10) {
                        int var16 = 0;
                        int var17 = var14;

                        for (int var9 = 0; var9 < var7; ++var9) {
                            for (int var8 = 0; var8 < var7; ++var8) {
                                var10[var16++] = M.isTone(var17, var8, var9) ? var12 : -16776961;
                            }
                        }

                        g.drawImage(this.tools.mkImage(var7, var7), var3 + 2, var4 + 2, this.getBackground(), null);
                    }
                } else {
                    Image var13 = this.images[var14 - 11];
                    if (var13 == null) {
                        g.setColor(Color.blue);
                        g.fillRect(var3 + 2, var4 + 2, w - 3, h - 3);
                    } else {
                        g.drawImage(var13, var3 + 2, var4 + 2, this.getBackground(), null);
                    }
                }

                var3 += w;
                if (var3 + w >= var11.width) {
                    var3 = 0;
                    var4 += h;
                    if (var4 + h >= var11.height) {
                        break;
                    }
                }
            }

        }
    }

    public void pMouse(MouseEvent event) {
        switch (this.iType) {
            case 2:
                this.mouseTT(event);
                break;
            case 3:
                this.mouseH(event);
                break;
            default:
                this.mousePen(event);
        }

    }

    public void run() {
        try {
            int var1 = this.imW;
            int var2 = this.imH;

            for (int var3 = 0; var3 < this.images.length; ++var3) {
                if (this.images[var3] == null) {
                    float[] var4 = this.info.getTT(var3 + 12);
                    int[] var5 = new int[var4.length];

                    int var6;
                    for (var6 = 0; var6 < var5.length; ++var6) {
                        var5[var6] = (int) ((1.0F - var4[var6]) * 255.0F) << 8 | 255;
                    }

                    var6 = (int) Math.sqrt((double) var5.length);
                    this.images[var3] = Awt.toMin(this.createImage(new MemoryImageSource(var6, var6, this.cmDef, var5, 0, var6)), var1 - 3, var2 - 3);
                    if (var3 % 5 == 2) {
                        this.repaint();
                    }
                }
            }

            this.repaint();
        } catch (Throwable ex) {
            ;
        }

    }

    public void setItem(int var1, M var2) {
        int var3;
        if (this.iType == 1) {
            this.tPen.setItem(-1, var2);
        } else {
            if (this.selItem >= 0 && this.selItem < this.imCount) {
                this.mgs[this.selItem].set(this.mg);
            }

            if (var1 >= 0) {
                var3 = this.mgs[var1].iPen;
                if (var3 != 4 && var3 != 5) {
                    this.selPen = var1;
                } else {
                    this.selWhite = var1;
                }
            }
        }

        this.selItem = var1;
        if (var1 < 0 && var2 == null) {
            this.repaint();
        } else {
            var3 = this.mg.iColor;
            int var4 = this.mg.iColorMask;
            int var5 = this.mg.iMask;
            int var6 = this.mg.iLayer;
            this.mg.set(var2 != null ? var2 : this.mgs[var1]);
            this.mg.iColor = var3;
            this.mg.iColorMask = var4;
            this.mg.iMask = var5;
            this.mg.iLayer = var6;
            if (this.tPen != null) {
                this.tPen.repaint();
            }

            for (int var7 = 0; var7 < this.cs.length; ++var7) {
                if (this.cs[var7] != null) {
                    this.cs[var7].repaint();
                }
            }
        }

    }

    public void undo(boolean var1) {
        if (var1) {
            if (this.selWhite != this.selItem) {
                this.setItem(this.selWhite, (M) null);
            }
        } else if (this.selPen != this.selItem) {
            this.setItem(this.selPen, (M) null);
        }

    }
}
