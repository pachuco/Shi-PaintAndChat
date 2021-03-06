package paintchat.pro;

import jaba.applet.Applet;

import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;

import paintchat.M;
import paintchat.PP;
import paintchat.Res;
import paintchat.SRaster;
import paintchat.ToolBox;
import paintchat_client.L;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

import static res.ResShiClient.*;

public class Tools implements ToolBox, ActionListener {
    private Applet applet;
    private Component parent;
    protected Mi mi;
    M.Info info;
    M mg;
    private LComponent[] components;
    private TPic tPic;
    private TPalette tPalette;
    private TBar tOption;
    public PP tP;
    protected int[] iBuffer;
    private Image image = null;
    private SRaster raster;

    public void actionPerformed(ActionEvent event) {
        try {
            PopupMenu var2 = (PopupMenu) event.getSource();
            int itemCount = var2.getItemCount();
            String actCom = event.getActionCommand();

            for (int i = 0; i < itemCount; ++i) {
                if (var2.getItem(i).getLabel().equals(actCom)) {
                    this.mg.set(var2.getName() + '=' + i);
                    this.repaint();
                    break;
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public String getC() {
        return this.tPalette.getC();
    }

    public LComponent[] getCs() {
        return this.components;
    }

    public Dimension getCSize() {
        return null;
    }

    public void init(Container var1, Applet var2, Res config, Mi var5) {
        this.applet = var2;
        this.info = var5.info;
        this.mg = this.info.m;
        this.iBuffer = var5.user.getBuffer();
        this.mi = var5;
        this.parent = var1;
        Dimension var6 = var1.getSize();
        LComponent[] var7 = new LComponent[10];
        TPen var8 = new TPen(this, this.info, config, null, var7);
        var8.init(0);
        var7[0] = var8;
        TPen var9 = new TPen(this, this.info, config, var8, var7);
        var9.init(1);
        var7[1] = var9;
        TPalette var10 = new TPalette();
        var10.setLocation((int) ((float) var8.getSizeW().width * Awt.q()) + 10, 0);
        var10.init(this, this.info, config);
        var7[2] = var10;
        this.tPalette = var10;
        TPen var11 = new TPen(this, this.info, config, null, var7);
        var11.initTT();
        var7[3] = var11;
        TPic var12 = new TPic(this);
        var7[4] = var12;
        this.tPic = var12;
        TPen var13 = new TPen(this, this.info, config, null, var7);
        var13.setLocation(var10.getLocation().x + var10.getSizeW().width, 0);
        var13.initHint();
        var7[5] = var13;
        L var14 = new L(var5, this, config);
        var7[6] = var14;
        TBar var15 = new TBar(this, config, var7);
        this.tOption = var15;
        var7[7] = var15;
        PP var16 = new PP();
        this.tP = var16;
        var16.mSetup(this, this.info, var5.user, this.mg, config);
        var7[8] = var16;
        TBar var17 = new TBar(this, config, var7);
        var7[9] = var17;
        var15.initOption(var2.getCodeBase(), var5);
        var17.init();
        var11.setLocation(var13.getLocation().x + var13.getSizeW().width, 0);
        var9.setLocation(0, var8.getSizeW().height);
        var12.setLocation(0, var9.getLocation().y + var9.getSizeW().height);
        var17.setLocation(var6.width - var17.getSizeW().width, 0);
        this.components = var7;
        var8.setItem(0, null);
        var1.add(var17, 0);
    }

    public void lift() {
        ((TPen) this.components[0]).setItem(-1, (M) null);
    }

    protected Image mkImage(int var1, int var2) {
        if (this.raster == null) {
            this.raster = new SRaster(ColorModel.getRGBdefault(), this.iBuffer, var1, var2);
            this.image = this.applet.createImage(this.raster);
        } else {
            this.raster.newPixels(this.image, this.iBuffer, var1, var2);
        }

        return this.image;
    }

    public void pack() {
        if (this.components != null) {
            for (int var1 = 0; var1 < this.components.length; ++var1) {
                if (this.components[var1] != null) {
                    this.components[var1].inParent();
                }
            }

            this.mi.setVisible(false);
            Dimension var3 = this.parent.getSize();
            this.mi.setDimension((Dimension) null, new Dimension(var3), new Dimension(var3));
            Dimension var2 = this.mi.getSize();
            this.mi.setLocation((var3.width - var2.width) / 2, (var3.height - var2.height) / 2);
            this.mi.setVisible(true);
        }

    }

    void repaint() {
        for (int var1 = 0; var1 < this.components.length; ++var1) {
            this.components[var1].repaint();
        }

    }

    public void selPix(boolean var1) {
        ((TPen) this.components[0]).undo(var1);
    }

    public void setARGB(int col) {
        int var2 = this.mg.iAlpha << 24 | this.mg.iColor;
        this.mg.iAlpha = col >>> 24;
        this.mg.iColor = col & 0xFFFFFF;
        if (var2 != col) {
            this.tPic.setColor(col);
            this.tPalette.setColor(col);
            this.tP.repaint();
        }

    }

    public void setC(String var1) {
        this.tPalette.setC(var1);
    }

    void setField(Component var1, String var2, String var3, int var4, int var5) {
        try {
            PopupMenu var6 = new PopupMenu();
            var6.setName(var2);
            var6.addActionListener(this);
            int var8 = M.class.getField(var2).getInt(this.mg);

            for (int var9 = 0; var9 < 16; ++var9) {
                Object var7 = langSP.get((Object)(var3 + var9));
                if (var7 != null) {
                    if (var8 == var9) {
                        var6.add((MenuItem) (new CheckboxMenuItem(var7.toString(), true)));
                    } else {
                        var6.add(var7.toString());
                    }
                }
            }

            var1.add(var6);
            var6.show(var1, var4, var5);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void setLineSize(int var1) {
        this.tPalette.setLineSize(var1);
        this.tP.up();
    }

    public void setMask(Component var1, int var2, int var3, int var4, boolean var5) {
        if (var5) {
            this.setField(var1, "iMask", "mask_", var3, var4);
        } else {
            this.mg.iColorMask = var2 & 0xFFFFFF;
            this.components[4].repaint();
        }

    }

    public void setRGB(int col) {
        this.setARGB(this.mg.iAlpha << 24 | col & 0xFFFFFF);
    }

    public void up() {
        this.tPic.repaint();
        this.tPalette.repaint();
        this.tP.up();
        if (this.components != null && this.components[6] != null) {
            this.components[6].repaint();
        }

    }

    public void mVisible(boolean var1) {
        for (int var2 = 0; var2 < this.components.length; ++var2) {
            this.components[var2].setVisible(var1);
        }

    }
}
