package paintchat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;

import syi.awt.Awt;
import syi.awt.LComponent;

/** Texture toolbar for normal UI */
public class TT extends LComponent implements SW, Runnable {
    private ToolBox ts;
    private M.Info info;
    private M.User user;
    private M mg;
    private boolean isRun = false;
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
    private int sizeTT = 0;
    private int iLast = -1;

    private int getIndex(int var1, int var2, int var3) {
        Dimension var4 = this.getSize();
        int var5 = this.imW;
        int var6 = this.imH;
        var1 -= var3;
        int var7 = (var4.width - var3) / var5;
        return var2 / var6 * var7 + Math.min(var1 / var5, var7);
    }

    public void lift() {
    }

    public void mPack() {
        this.inParent();
        Container var1 = this.getParent();
        Dimension var2 = this.getMaximumSize();
        var2.height = var1.getSize().height - this.getGapH();
    }

    public void mSetup(ToolBox var1, M.Info var2, M.User var3, M var4, Res var6) {
        this.ts = var1;
        this.info = var2;
        this.user = var3;
        this.mg = var4;
        this.setTitle(var6.getP("window_3"));
        this.getToolkit();
        this.imW = this.imH = (int) (34.0F * LComponent.Q);

        try {
            String var7 = "tt_size";
            this.images = new Image[Integer.parseInt(var6.get(var7))];
            var6.remove(var7);
            int var8 = this.imW * 5 + 1;
            int var9 = ((this.images.length + 12) / 5 + 1) * this.imW + 1;
            this.setDimension(new Dimension(this.imW + 1, this.imW + 1), new Dimension(var8, var9), new Dimension(var8, var9));
        } catch (RuntimeException var10) {
            ;
        }

    }

    public void paint2(Graphics g) {
        if (this.images != null) {
            if (!this.isRun) {
                Thread var2 = new Thread(this);
                var2.setPriority(1);
                var2.setDaemon(true);
                var2.start();
                this.isRun = true;
            }

            int var21 = this.images.length + 11;
            int var3 = 0;
            int var4 = 0;
            int var5 = this.imW;
            int var6 = this.imH;
            int var7 = var5 - 3;
            M var10 = this.mg;
            int[] var11 = this.user.getBuffer();
            Color var12 = this.getBackground();
            short var13 = 255;
            //var12.getRGB();
            Dimension var14 = this.getSize();
            //this.getToolkit();
            this.iLast = var10.iTT;

            for (int var16 = -1; var16 < var21; ++var16) {
                g.setColor(var16 + 1 == var10.iTT ? Awt.cFSel : Awt.cF);
                g.drawRect(var3 + 1, var4 + 1, var5 - 2, var6 - 2);
                if (var16 == -1) {
                    g.setColor(Color.blue);
                    g.fillRect(var3 + 2, var4 + 2, var5 - 3, var6 - 3);
                } else if (var16 < 11) {
                    synchronized (var11) {
                        int var18 = 0;
                        int var19 = var16;

                        for (int var9 = 0; var9 < var7; ++var9) {
                            for (int var8 = 0; var8 < var7; ++var8) {
                                var11[var18++] = M.isTone(var19, var8, var9) ? -1 : var13;
                            }
                        }

                        g.drawImage(this.user.mkImage(var7, var7), var3 + 2, var4 + 2, var12, (ImageObserver) null);
                    }
                } else {
                    Image var15 = this.images[var16 - 11];
                    if (var15 == null) {
                        g.setColor(Color.blue);
                        g.fillRect(var3 + 2, var4 + 2, var5 - 3, var6 - 3);
                    } else {
                        g.drawImage(var15, var3 + 2, var4 + 2, var12, (ImageObserver) null);
                    }
                }

                var3 += var5;
                if (var3 + var5 >= var14.width) {
                    var3 = 0;
                    var4 += var6;
                    if (var4 + var6 >= var14.height) {
                        break;
                    }
                }
            }

        }
    }

    public void pMouse(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            this.getSize();
            int indexTT = this.getIndex(event.getX(), event.getY(), 0);
            if (indexTT >= this.images.length + 12) {
                return;
            }

            this.mg.iTT = indexTT;
            this.repaint();
        }

    }

    public void run() {
        try {
            DirectColorModel var1 = new DirectColorModel(24, 65280, 65280, 255);
            int var2 = this.imW;
            int var3 = this.imH;

            for (int var4 = 0; var4 < this.images.length; ++var4) {
                if (this.images[var4] == null) {
                    float[] var5 = this.info.getTT(var4 + 12);
                    int[] var6 = new int[var5.length];

                    int var7;
                    for (var7 = 0; var7 < var6.length; ++var7) {
                        var6[var7] = (int) ((1.0F - var5[var7]) * 255.0F) << 8 | 255;
                    }

                    var7 = (int) Math.sqrt((double) var6.length);
                    this.images[var4] = Awt.toMin(this.createImage(new MemoryImageSource(var7, var7, var1, var6, 0, var7)), var2 - 3, var3 - 3);
                    if (var4 % 5 == 2) {
                        this.repaint();
                    }
                }
            }

            this.repaint();
        } catch (Throwable ex) {
            ;
        }

    }

    public void up() {
        if (this.iLast != this.mg.iTT) {
            this.repaint();
        }

    }
}
