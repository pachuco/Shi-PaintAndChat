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

import static res.ResShiClient.*;

public class ToolList {
    private Tools tools;
    private Res cnf;
    boolean isField;
    boolean isClass;
    boolean isDirect;
    boolean isMask;
    boolean isEraser;
    boolean isSelect;
    boolean isDrawList;
    boolean isIm;
    String strField; // string name of the linked field in M
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

    /** Draws image g with bgColor background */
    private void dImage(Graphics g, Color bgColor, int offY, int idx) {
        int rectHeight = this.r.height;
        int rectWidth = this.r.width;
        // background
        g.setColor(bgColor);
        g.fillRect(2, offY + 2, this.r.width - 4, rectHeight - 4);

        // the mask button shows a rectangle of the current mask color
        if (this.isMask) {
            g.setColor(new Color(this.info.iColorMask));
            g.fillRect(rectWidth - this.imW - 3, offY + 3, this.imW, (rectHeight - 4) / 2);
        }

        // draw background image if available
        if (this.isIm && this.image != null && idx < this.image.getHeight(null) / this.imH) {
            int sx1 = this.imIndex * this.imW;
            int sy1 = idx * this.imH;
            int dx1 = this.r.x + 2;
            int dy1 = offY + 2;
            g.drawImage(this.image, dx1, dy1, dx1 + rectWidth - 4, dy1 + rectHeight - 4, sx1, sy1, sx1 + this.imW, sy1 + this.imH, bgColor, (ImageObserver) null);
        }
    }

    /** On mouse drag event, used for showing the alternate tool lists*/
    private void drag(int x, int y) {
        if (this.isDrag) {
            int listLength = this.len();
            int width = this.r.width;
            int height = this.r.height;
            int listIdx = y / (height - 2) - 1; // index from the drag dropdown
            this.isList = true;
            int selectedListIdx = this.iSelectList; // index of currently selected item in the list
            if (x >= 0 && x < width && listIdx >= 0 && listIdx < listLength) {
                this.iSelectList = listIdx;
            } else {
                this.iSelectList = -1;
                listIdx = -1;
            }

            if (this.isList && !this.isDrawList) {
                this.isDrawList = true;
                this.repaint();
            }

            if (selectedListIdx != listIdx && this.isList) {
                Graphics g = this.tools.primary();
                if (selectedListIdx >= 0) {
                    g.setColor(this.tools.clFrame);
                    g.drawRect(this.r.x + 1, this.r.y + (height - 3) * (selectedListIdx + 1) + 2, width - 3, height - 3);
                }

                if (listIdx >= 0) {
                    g.setColor(this.tools.clSel);
                    g.drawRect(this.r.x + 1, this.r.y + (height - 3) * (listIdx + 1) + 2, width - 3, height - 3);
                }

            }
        }
    }

    private int getValue() {
        try {
            return this.isField ? M.class.getField(this.strField).getInt(this.info) : this.iSelect;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public void init(Tools tools, Res cnf, M mg, ToolList[] toolList, int toolID) {
        try {
            this.tools = tools;
            this.cnf = cnf;
            this.lists = toolList;
            this.info = mg;
            // loads up tools configuration from parameters (param_utf8.txt)
            String toolIdPrefix = "t0" + toolID + "_";
            this.isDirect = cnf.getP(toolIdPrefix + "direct", false);
            this.isClass = cnf.getP(toolIdPrefix + "class", false);
            this.isEraser = cnf.getP(toolIdPrefix + "iseraser", false);
            this.isIm = cnf.getP(toolIdPrefix + "image", true);
            this.strField = cnf.getP(toolIdPrefix + "field", (String) null);
            this.isField = this.strField != null;
            if (this.isField && this.strField.equals("iMask")) {
                this.isMask = true;
            }

            toolIdPrefix = "t0" + toolID;

            int paramCount;
            for (paramCount = 0; cnf.getP(toolIdPrefix + paramCount) != null; ++paramCount) {
                ;
            }

            this.strings = new String[paramCount];

            for (int i = 0; i < paramCount; ++i) {
                String key = toolIdPrefix + i;
                if (this.isField) {
                    if (this.items == null) {
                        this.items = new int[paramCount];
                    }

                    this.items[i] = cnf.getP(key, 0);
                } else if (this.isClass) {
                    if (this.strs == null) {
                        this.strs = new String[paramCount];
                    }

                    this.strs[i] = cnf.getP(key);
                } else {
                    if (this.mgs == null) {
                        this.mgs = new M[paramCount];
                    }

                    (this.mgs[i] = new M()).set(cnf.getP(key));
                }

                this.strings[i] = langSP.get(key);
                cnf.remove(key);
                langSP.remove(key);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    /** Returns how many sub-tools are linked */
    private int len() {
        return this.mgs == null ? (this.items == null ? (this.strs == null ? 0 : this.strs.length) : this.items.length) : this.mgs.length;
    }

    public void paint(Graphics g, Graphics background) {
        try {
            if (g == null || background == null) {
                return;
            }

            int width = this.r.width;
            int height = this.r.height;
            int rectX = this.r.x;
            int rectY = this.r.y;
            int itemCount = this.len();
            int drawHeight = height - 2;
            int offY;
            if (this.isList) {
                offY = rectY + height - 2;
                Color bgColor = this.isDirect ? this.tools.clB2 : this.tools.clB;

                int idx;
                for (idx = 0; idx < itemCount; ++idx) {
                    this.dImage(g, bgColor, offY, idx);
                    g.setColor(this.tools.clText);
                    if (idx < this.strings.length) {
                        g.drawString(this.strings[idx], rectX + 4, offY + this.base);
                    }

                    offY += drawHeight - 1;
                }

                offY = rectY + drawHeight;
                g.setColor(this.tools.clFrame);
                g.drawRect(rectX, offY, width - 1, (drawHeight - 1) * itemCount + 2);

                for (idx = 0; idx < itemCount; ++idx) {
                    g.drawRect(rectX + 1, offY + 1, width - 3, height - 3);
                    offY += drawHeight - 1;
                }
            }

            int currentTool = this.getValue();
            if (this.isField) {
                int toolCount = this.items.length;

                for (int i = 0; i < toolCount; ++i) {
                    if (this.items[i] == currentTool) {
                        currentTool = i;
                        break;
                    }
                }
            }

            this.dImage(background, this.isDirect ? this.tools.clB2 : this.tools.clB, 0, currentTool);
            background.setColor(this.tools.clFrame);
            background.drawRect(0, 0, width - 1, height - 1);
            if (this.isSelect) {
                // draw active border
                background.setColor(this.tools.clSel);
                background.drawRect(1, 1, width - 3, height - 3);
            } else {
                // normal border
                background.setColor(this.tools.clBL);
                background.fillRect(1, 1, width - 2, 1);
                background.fillRect(1, 1, 1, height - 2);
                background.setColor(this.tools.clBD);
                background.fillRect(width - 2, 2, 1, height - 4);
                background.fillRect(2, height - 2, width - 3, 1);
            }

            if (currentTool >= 0 && currentTool < this.strings.length) {
                background.setColor(this.tools.clText);
                background.drawString((String) this.strings[currentTool], 3, this.base);
            }

            g.drawImage(this.tools.imBack, this.r.x, this.r.y, this.r.x + width, this.r.y + height, 0, 0, width, height, this.tools.clB, (ImageObserver) null);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    /** Mouse action event */
    public void pMouse(MouseEvent event) {
        int mouseX = event.getX();
        int mouseY = event.getY();
        int x = mouseX - this.r.x; // relative mouse position to element
        int y = mouseY - this.r.y;
        switch (event.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                if (this.r.contains(mouseX, mouseY)) {
                    this.press();
                }
                break;
            case MouseEvent.MOUSE_RELEASED:
                this.release(x, y, this.r.contains(mouseX, mouseY));
            case MouseEvent.MOUSE_MOVED:
            case MouseEvent.MOUSE_ENTERED:
            case MouseEvent.MOUSE_EXITED:
            default:
                break;
            case MouseEvent.MOUSE_DRAGGED:
                this.drag(x, y);
        }

    }

    /** On mouse pressed */
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

    /** On mouse released */
    private void release(int x, int y, boolean isInBound) {
        if (this.isDrag) {
            int rectHeight = this.r.height;
            int rectWidth = this.r.width;
            int listIdx = y / (rectHeight - 2) - 1; // index from the drag dropdown
            boolean isDrawList = this.isDrawList;
            boolean doSelectPrimary = false;
            this.isDrag = false;
            this.isList = false;
            if (listIdx < 0 || listIdx >= this.len() || x < 0 || x >= rectWidth) {
                listIdx = -1;
            }

            if (listIdx == -1) {
                if (isInBound) {
                    if (this.isSelect) {
                        int listLength = this.len();
                        this.unSelect();
                        if (++this.iSelect >= listLength) {
                            this.iSelect = 0;
                        }

                        this.select();
                    } else {
                        this.select();
                    }

                    doSelectPrimary = true;
                }
            } else {
                if (this.isSelect) {
                    this.unSelect();
                }

                this.iSelect = listIdx;
                this.select();
            }

            this.iSelectList = -1;
            this.isDrawList = false;
            if (isDrawList) {
                Graphics g = this.tools.primary();
                g.setColor(this.tools.getBackground());
                g.fillRect(this.r.x - 1, this.r.y - 1, rectWidth + 2, (rectHeight - 2) * (this.len() + 1) + 2);
            }

            if (doSelectPrimary || isDrawList) {
                this.tools.mPaint(-1);
            }

        }
    }

    public void repaint() {
        this.paint(this.tools.primary(), this.tools.getBack());
    }

    /** Applies tool selection */
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

            int color = this.info.iColor;
            int mask = this.info.iMask;
            int colorMask = this.info.iColorMask;
            int layer = this.info.iLayer;
            int layerSrc = this.info.iLayerSrc;
            int texture = this.info.iTT;
            int sa = this.info.iSA;
            int ss = this.info.iSS;
            this.info.set(this.mgs[this.iSelect]);
            this.info.iColor = color;
            this.info.iMask = mask;
            this.info.iColorMask = colorMask;
            this.info.iLayer = layer;
            this.info.iLayerSrc = layerSrc;
            this.info.iTT = texture;
            if (sa != this.info.iSA || ss != this.info.iSS) {
                this.tools.mi.up();
            }

            if (!this.isDirect) {
                this.isSelect = true;
            }

            this.tools.upCS();
        } catch (Throwable ex) {
            ;
        }

    }

    public void setImage(Image img, int width, int height, int idx) {
        this.image = img;
        this.imW = width;
        this.imH = height;
        this.imIndex = idx;
    }

    /** Set size of the tool button */
    public void setSize(int width, int height, int base) {
        this.r.setSize(width, height);
        this.base = base;
    }

    /** Deselect the current tool */
    public void unSelect() {
        if (this.isSelect && !this.isDirect) {
            this.mgs[this.iSelect].set(this.info);
        }

        this.isSelect = false;
    }
}
