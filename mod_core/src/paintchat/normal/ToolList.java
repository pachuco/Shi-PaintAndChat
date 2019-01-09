package paintchat.normal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import paintchat.M;
import paintchat.Res;

public class ToolList {
    private Tools tools;
    private Res res;
    private Res cnf;
    boolean isField;
    boolean isClass;
    boolean isDirect;
    boolean isMask;
    boolean isEraser;
    boolean isSelect;
    boolean isDrawList;
    boolean isIm;
    String strField;
    private int quality = 1;
    private boolean isDrag = false;
    public int iSelect;
    public int iSelectList;
    public boolean isList;
    private M info = null;
    private M[] mgs = null;
    private int[] items = null;
    private String[] strs = null;
    private String[] strings;
    private int length;
    private ToolList[] lists;
    private Font font;
    private int base = 0;
    private Image image;
    private int imW;
    private int imH;
    private int imIndex;
    public Rectangle r = new Rectangle();

    private void dImage(Graphics var1, Color var2, int var3, int var4) {
        int var5 = this.r.height;
        int var6 = this.r.width;
        var1.setColor(var2);
        var1.fillRect(2, var3 + 2, this.r.width - 4, var5 - 4);
        if (this.isMask) {
            var1.setColor(new Color(this.info.iColorMask));
            var1.fillRect(var6 - this.imW - 3, var3 + 3, this.imW, (var5 - 4) / 2);
        }

        if (this.isIm && this.image != null && var4 < this.image.getHeight((ImageObserver) null) / this.imH) {
            int var7 = this.imIndex * this.imW;
            int var8 = var4 * this.imH;
            int var9 = this.r.x + 2;
            int var10 = var3 + 2;
            var1.drawImage(this.image, var9, var10, var9 + var6 - 4, var10 + var5 - 4, var7, var8, var7 + this.imW, var8 + this.imH, var2, (ImageObserver) null);
        }
    }

    private void drag(int var1, int var2) {
        if (this.isDrag) {
            int var3 = this.len();
            int var4 = this.r.width;
            int var5 = this.r.height;
            int var6 = var2 / (var5 - 2) - 1;
            this.isList = true;
            int var7 = this.iSelectList;
            if (var1 >= 0 && var1 < var4 && var6 >= 0 && var6 < var3) {
                this.iSelectList = var6;
            } else {
                this.iSelectList = -1;
                var6 = -1;
            }

            if (this.isList && !this.isDrawList) {
                this.isDrawList = true;
                this.repaint();
            }

            if (var7 != var6 && this.isList) {
                Graphics var8 = this.tools.primary();
                if (var7 >= 0) {
                    var8.setColor(this.tools.clFrame);
                    var8.drawRect(this.r.x + 1, this.r.y + (var5 - 3) * (var7 + 1) + 2, var4 - 3, var5 - 3);
                }

                if (var6 >= 0) {
                    var8.setColor(this.tools.clSel);
                    var8.drawRect(this.r.x + 1, this.r.y + (var5 - 3) * (var6 + 1) + 2, var4 - 3, var5 - 3);
                }

            }
        }
    }

    private int getValue() {
        try {
            return this.isField ? M.class.getField(this.strField).getInt(this.info) : this.iSelect;
        } catch (Throwable var3) {
            var3.printStackTrace();
            return 0;
        }
    }

    public void init(Tools var1, Res var2, Res var3, M var4, ToolList[] var5, int var6) {
        try {
            this.tools = var1;
            this.res = var2;
            this.cnf = var3;
            this.lists = var5;
            this.info = var4;
            String var7 = "t0" + var6 + "_";
            this.isDirect = var3.getP(var7 + "direct", false);
            this.isClass = var3.getP(var7 + "class", false);
            this.isEraser = var3.getP(var7 + "iseraser", false);
            this.isIm = var3.getP(var7 + "image", true);
            this.strField = var3.getP(var7 + "field", (String) null);
            this.isField = this.strField != null;
            if (this.isField && this.strField.equals("iMask")) {
                this.isMask = true;
            }

            var7 = "t0" + var6;

            int var9;
            for (var9 = 0; var3.getP(var7 + var9) != null; ++var9) {
                ;
            }

            this.strings = new String[var9];

            for (int var10 = 0; var10 < var9; ++var10) {
                String var8 = var7 + var10;
                if (this.isField) {
                    if (this.items == null) {
                        this.items = new int[var9];
                    }

                    this.items[var10] = var3.getP(var8, 0);
                } else if (this.isClass) {
                    if (this.strs == null) {
                        this.strs = new String[var9];
                    }

                    this.strs[var10] = var3.getP(var8);
                } else {
                    if (this.mgs == null) {
                        this.mgs = new M[var9];
                    }

                    (this.mgs[var10] = new M()).set(var3.getP(var8));
                }

                this.strings[var10] = var2.res(var8);
                var3.remove(var8);
                var2.remove(var8);
            }
        } catch (Throwable var11) {
            var11.printStackTrace();
        }

    }

    private int len() {
        return this.mgs == null ? (this.items == null ? (this.strs == null ? 0 : this.strs.length) : this.items.length) : this.mgs.length;
    }

    public void paint(Graphics var1, Graphics var2) {
        try {
            if (var1 == null || var2 == null) {
                return;
            }

            int var4 = this.r.width;
            int var5 = this.r.height;
            int var6 = this.r.x;
            int var7 = this.r.y;
            int var8 = this.len();
            int var9 = var5 - 2;
            int var10;
            if (this.isList) {
                var10 = var7 + var5 - 2;
                Color var11 = this.isDirect ? this.tools.clB2 : this.tools.clB;

                int var12;
                for (var12 = 0; var12 < var8; ++var12) {
                    this.dImage(var1, var11, var10, var12);
                    var1.setColor(this.tools.clText);
                    if (var12 < this.strings.length) {
                        var1.drawString(this.strings[var12], var6 + 4, var10 + this.base);
                    }

                    var10 += var9 - 1;
                }

                var10 = var7 + var9;
                var1.setColor(this.tools.clFrame);
                var1.drawRect(var6, var10, var4 - 1, (var9 - 1) * var8 + 2);

                for (var12 = 0; var12 < var8; ++var12) {
                    var1.drawRect(var6 + 1, var10 + 1, var4 - 3, var5 - 3);
                    var10 += var9 - 1;
                }
            }

            int var3 = this.getValue();
            if (this.isField) {
                var10 = this.items.length;

                for (int var14 = 0; var14 < var10; ++var14) {
                    if (this.items[var14] == var3) {
                        var3 = var14;
                        break;
                    }
                }
            }

            this.dImage(var2, this.isDirect ? this.tools.clB2 : this.tools.clB, 0, var3);
            var2.setColor(this.tools.clFrame);
            var2.drawRect(0, 0, var4 - 1, var5 - 1);
            if (this.isSelect) {
                var2.setColor(this.tools.clSel);
                var2.drawRect(1, 1, var4 - 3, var5 - 3);
            } else {
                var2.setColor(this.tools.clBL);
                var2.fillRect(1, 1, var4 - 2, 1);
                var2.fillRect(1, 1, 1, var5 - 2);
                var2.setColor(this.tools.clBD);
                var2.fillRect(var4 - 2, 2, 1, var5 - 4);
                var2.fillRect(2, var5 - 2, var4 - 3, 1);
            }

            if (var3 >= 0 && var3 < this.strings.length) {
                var2.setColor(this.tools.clText);
                var2.drawString((String) this.strings[var3], 3, this.base);
            }

            var1.drawImage(this.tools.imBack, this.r.x, this.r.y, this.r.x + var4, this.r.y + var5, 0, 0, var4, var5, this.tools.clB, (ImageObserver) null);
        } catch (Throwable var13) {
            var13.printStackTrace();
        }

    }

    public void pMouse(MouseEvent event) {
        int mouseX = event.getX();
        int mouseY = event.getY();
        int var4 = mouseX - this.r.x;
        int var5 = mouseY - this.r.y;
        switch (event.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                if (this.r.contains(mouseX, mouseY)) {
                    this.press();
                }
                break;
            case MouseEvent.MOUSE_RELEASED:
                this.release(var4, var5, this.r.contains(mouseX, mouseY));
            case MouseEvent.MOUSE_MOVED:
            case MouseEvent.MOUSE_ENTERED:
            case MouseEvent.MOUSE_EXITED:
            default:
                break;
            case MouseEvent.MOUSE_DRAGGED:
                this.drag(var4, var5);
        }

    }

    private void press() {
        if (!this.isDrag) {
            this.isDrag = true;
            this.iSelectList = -1;
            if (this.isDirect) {
                this.isList = true;
                this.isDrawList = true;
                this.repaint();
            }

        }
    }

    private void release(int var1, int var2, boolean var3) {
        if (this.isDrag) {
            int var4 = this.r.height;
            int var5 = this.r.width;
            int var6 = var2 / (var4 - 2) - 1;
            boolean var7 = this.isDrawList;
            boolean var8 = false;
            this.isDrag = false;
            this.isList = false;
            if (var6 < 0 || var6 >= this.len() || var1 < 0 || var1 >= var5) {
                var6 = -1;
            }

            if (var6 == -1) {
                if (var3) {
                    if (this.isSelect) {
                        int var9 = this.len();
                        this.unSelect();
                        if (++this.iSelect >= var9) {
                            this.iSelect = 0;
                        }

                        this.select();
                    } else {
                        this.select();
                    }

                    var8 = true;
                }
            } else {
                if (this.isSelect) {
                    this.unSelect();
                }

                this.iSelect = var6;
                this.select();
            }

            this.iSelectList = -1;
            this.isDrawList = false;
            if (var7) {
                Graphics var10 = this.tools.primary();
                var10.setColor(this.tools.getBackground());
                var10.fillRect(this.r.x - 1, this.r.y - 1, var5 + 2, (var4 - 2) * (this.len() + 1) + 2);
            }

            if (var8 || var7) {
                this.tools.mPaint(-1);
            }

        }
    }

    public void repaint() {
        this.paint(this.tools.primary(), this.tools.getBack());
    }

    public void select() {
        try {
            if (this.isField) {
                M.class.getField(this.strField).setInt(this.info, this.items[this.iSelect]);
                this.tools.upCS();
                return;
            }

            if (this.isClass) {
                this.tools.showW(this.strs[this.iSelect]);
                return;
            }

            if (!this.isDirect) {
                this.tools.unSelect();
            }

            int var1 = this.info.iColor;
            int var2 = this.info.iMask;
            int var3 = this.info.iColorMask;
            int var4 = this.info.iLayer;
            int var5 = this.info.iLayerSrc;
            int var6 = this.info.iTT;
            int var7 = this.info.iSA;
            int var8 = this.info.iSS;
            this.info.set(this.mgs[this.iSelect]);
            this.info.iColor = var1;
            this.info.iMask = var2;
            this.info.iColorMask = var3;
            this.info.iLayer = var4;
            this.info.iLayerSrc = var5;
            this.info.iTT = var6;
            if (var7 != this.info.iSA || var8 != this.info.iSS) {
                this.tools.mi.up();
            }

            if (!this.isDirect) {
                this.isSelect = true;
            }

            this.tools.upCS();
        } catch (Throwable var10) {
            ;
        }

    }

    public void setImage(Image var1, int var2, int var3, int var4) {
        this.image = var1;
        this.imW = var2;
        this.imH = var3;
        this.imIndex = var4;
    }

    public void setSize(int var1, int var2, int var3) {
        this.r.setSize(var1, var2);
        this.base = var3;
    }

    public void unSelect() {
        if (this.isSelect && !this.isDirect) {
            this.mgs[this.iSelect].set(this.info);
        }

        this.isSelect = false;
    }
}
