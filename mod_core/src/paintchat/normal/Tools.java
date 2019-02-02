package paintchat.normal;

import jaba.applet.Applet;

import java.awt.AWTEvent;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.StringReader;

import paintchat.LO;
import paintchat.M;
import paintchat.Res;
import paintchat.SW;
import paintchat.ToolBox;
import paintchat_client.L;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

import static res.ResShiClient.*;

public class Tools extends LComponent implements ToolBox, ActionListener {
    Applet applet;
    Container parent;
    Mi mi;
    private L L;
    private boolean is_l;
    M.Info info;
    M mg;
    Res config;
    private Graphics primary = null;
    private Font fDef;
    private Font fIg;
    ToolList[] list;
    private Rectangle rPaint = new Rectangle();
    private Rectangle[] rects = null;
    private int fit_w = -1;
    private int fit_h = -1;
    private int nowButton = -1; // current GUI button index
    private int nowColor = -1; // current palette index
    private int oldPen; // previous pen
    Color clFrame;
    Color clB;
    Color clBD;
    Color clBL;
    Color clText;
    Color clBar;
    Color clB2;
    Color clSel;
    private boolean isWest = false; // floats the toolbar to the left of the canvas
    private boolean isLarge;
    private boolean isVisible = true;
    private PopupMenu popup; // popup menu, currently only used to change pen mask by right clicking the line size slider
    private LComponent[] cs; // ui components
    private Window[] ws;
    private static int[] DEFC = new int[]{
            0x000000, 0xFFFFFF, 0xB47575, 0x888888, 0xFA9696, 0xC096C0, 0xFFB6FF, 0x8080FF, 0x25C7C9,0xE7E58D, 0xE7962D, 0x99CB7B, 0xFCECE2, 0xF9DDCF};
    private static int[] COLORS = new int[14];
    private static Color[][] clRGB;
    private static Color[][] clERGB;
    private final char[][] clV = new char[][]{{'H', 'S', 'B', 'A'}, {'R', 'G', 'B', 'A'}};
    private boolean isRGB = true;
    private float[] fhsb = new float[3];
    private int iColor; // current color
    protected Image imBack = null;
    private Graphics back;
    protected int W;
    protected int H;
    protected int IMW; // toolbar icons width
    protected int IMH; // toolbar icons height

    static {
        // copies default palette DEFC to user palette COLORS
        System.arraycopy(DEFC, 0, COLORS, 0, 14);
        clRGB = new Color[][]{{Color.magenta, Color.cyan, Color.white, Color.lightGray}, {new Color(0xFA9696), new Color(0x82F238), new Color(0x8080FF), Color.lightGray}};
        clERGB = new Color[2][4];

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 4; ++j) {
                clERGB[i][j] = clRGB[i][j].darker();
            }
        }

    }

    public Tools() {
        this.setTitle("Toolbar");
        super.isHide = false;
        super.iGap = 2; // padding between tool list border and content
    }

    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        Menu menu = (Menu) event.getSource();
        int penMask = 0;
        int count = menu.getItemCount();

        for (int i = 0; i < count; ++i) {
            if (menu.getItem(i).getLabel().equals(actionCommand)) {
                penMask = i;
                break;
            }
        }

        switch (Integer.parseInt(menu.getLabel())) {
            case 2: // pen mask change from the menu from menu()
                this.mg.iPenM = penMask;
                this.setLineSize(0, this.mg.iSize);
                this.repaint();
            default:
        }
    }

    /** Adds a component or window to their respective lists */
    public void addC(Object obj) {

        if (obj instanceof LComponent) {
            if (this.cs == null) {
                this.cs = new LComponent[]{(LComponent) obj};
                return;
            }

            for (int i = 0; i < this.cs.length; ++i) {
                if (this.cs[i] == obj) {
                    return;
                }
            }

            int componentCount = this.cs.length;
            LComponent[] componentList = new LComponent[componentCount + 1];
            System.arraycopy(this.cs, 0, componentList, 0, componentCount);
            componentList[componentCount] = (LComponent) obj;
            this.cs = componentList;
        } else {
            if (this.ws == null) {
                this.ws = new Window[]{(Window) obj};
                return;
            }

            for (int i = 0; i < this.ws.length; ++i) {
                if (this.ws[i] == obj) {
                    return;
                }
            }

            int windowCount = this.ws.length;
            Window[] windowList = new Window[windowCount + 1];
            System.arraycopy(this.cs, 0, windowList, 0, windowCount);
            windowList[windowCount] = (Window) obj;
            this.ws = windowList;
        }

    }

    /** Loads the background images for the tools */
    public Graphics getBack() {
        if (this.imBack == null) {
            synchronized (this) {
                if (this.imBack == null) {
                    try {
                        int height = 0;

                        for (int i = 0; i < this.list.length; ++i) {
                            height = Math.max(height, this.list[i].r.height);
                        }

                        height = Math.max(height, 32) * 2;
                        this.imBack = this.createImage(this.W + 1, height + 1);
                        this.back = this.imBack.getGraphics();
                    } catch (RuntimeException ex) {
                        this.imBack = null;
                        this.back = null;
                    }
                }
            }
        }

        if (this.back != null) {
            this.back.setFont(this.fDef);
        }

        return this.back;
    }

    /** Returns a list of the current palette colors in hexadecimal format */
    public String getC() {
        try {
            int[] colors = COLORS == null ? DEFC : COLORS; // either from custom palette or default colors
            StringBuffer colorList = new StringBuffer();

            for (int i = 0; i < colors.length; ++i) {
                if (i != 0) {
                    colorList.append('\n');
                }

                colorList.append("#" + Integer.toHexString(0xFF000000 | colors[i] & 0xFFFFFF).substring(2).toUpperCase());
            }

            return colorList.toString();
        } catch (Throwable ex) {
            return null;
        }
    }

    public LComponent[] getCs() {
        return this.cs;
    }

    public Dimension getCSize() {
        Dimension parentSize = this.parent.getSize();
        return new Dimension(parentSize.width - this.getSizeW().width - this.mi.getGapW(), parentSize.height);
    }

    private int getRGB() {
        return !this.isRGB ? Color.HSBtoRGB((float) (this.iColor >>> 16 & 255) / 255.0F, (float) (this.iColor >>> 8 & 255) / 255.0F, (float) (this.iColor & 255) / 255.0F) & 0xFFFFFF : this.iColor & 0xFFFFFF;
    }


    //FIXME: Why is this called "i"?
    private int i(String key, int fallback) {
        return this.config.getP(key, fallback);
    }

    //FIXME: Why is this called "i"?
    public boolean i(String key, boolean fallback) {
        return this.config.getP(key, fallback);
    }

    public void init(Container parent, Applet app, Res config, Mi mi) {
        this.applet = app;
        this.parent = parent;
        this.config = config;
        this.info = mi.info;
        this.mi = mi;
        this.mg = this.info.m;
        this.W = this.i("tool_width", 48) + 4;
        this.H = this.i("tool_height", 470);

        for (int i = 0; i < DEFC.length; i += 2) {
            DEFC[i] = config.getP("color_" + (i + 2), DEFC[i]);
            DEFC[i + 1] = config.getP("color_" + (i + 1), DEFC[i + 1]);
        }

        System.arraycopy(DEFC, 0, COLORS, 0, 14);
        this.sCMode(false);
        String toolColorPrefix = "tool_color_";
        this.setBackground(new Color(this.i(toolColorPrefix + "bk", this.i(toolColorPrefix + "back", 0x9999BB))));
        this.clB = new Color(this.i(toolColorPrefix + "button", 0xFFE8DFAE));
        this.clB2 = new Color(this.i(toolColorPrefix + "button" + '2', 0xF8DAAA));
        this.clFrame = new Color(this.i(toolColorPrefix + "frame", 0x000000));
        this.clText = new Color(this.i(toolColorPrefix + "text", 0x773333));
        this.clBar = new Color(this.i(toolColorPrefix + "bar", 0xDDDDFF));
        this.clSel = new Color(this.i(toolColorPrefix + "iconselect", this.i("color_iconselect", 0xEE3333)));
        this.clBL = new Color(this.i(toolColorPrefix + "button" + "_hl", this.clB.brighter().getRGB()));
        this.clBD = new Color(this.i(toolColorPrefix + "button" + "_dk", this.clB.darker().getRGB()));
        this.isWest = "left".equals(config.getP("tool_align"));
        this.isLarge = config.getP("icon_enlarge", true);
        this.is_l = config.getP("tool_layer", true);
        this.setDimension(new Dimension(this.W, 42), new Dimension(this.W, this.H), new Dimension(this.W, (int) ((float) this.H * 1.25F)));
        this.list[0].select();
        this.addC(this);
        parent.add(this, 0);
    }

    /** Returns id of the clicked tool or -1 if empty */
    private int isClick(int x, int y) {
        if (this.rects == null) {
            return -1;
        } else {
            int rectCount = this.rects.length;
            int toolCount = this.list.length;

            int i;
            for (i = 0; i < toolCount; ++i) {
                if (this.list[i].r.contains(x, y)) {
                    return i;
                }
            }

            for (i = 0; i < rectCount; ++i) {
                Rectangle rect = this.rects[i];
                if (rect != null && rect.contains(x, y)) {
                    return i + toolCount;
                }
            }

            return -1;
        }
    }

    public void lift() {
        this.unSelect();
        this.nowButton = -1;
        this.repaint();
    }

    private void makeList() {
        Image imgIcon = null;
        int iconWidth = 0;
        int iconHeight = 0;

        try {
            String filePath = "res/s.gif";
            Image spritesheet = this.getToolkit().createImage((byte[]) this.config.getRes(filePath));
            Awt.wait(spritesheet);
            imgIcon = spritesheet;
            this.config.remove(filePath);
            int iconCount = this.i("tool_icon_count", 7);
            this.list = new ToolList[iconCount];
            iconWidth = spritesheet.getWidth((ImageObserver) null) / iconCount;
            iconHeight = this.i("tool_icon_height", spritesheet.getHeight((ImageObserver) null) / 9);
            this.IMW = iconWidth;
            this.IMH = iconHeight;
        } catch (RuntimeException ex) {
            ;
        }

        for (int i = 0; i < this.list.length; ++i) {
            ToolList toolList;
            this.list[i] = toolList = new ToolList();
            toolList.init(this, this.config, this.mg, this.list, i);
            toolList.setImage(imgIcon, iconWidth, iconHeight, i);
        }

    }

    /** A menu to change the brush set for this tool? */
    private void menu(int x, int y, int menuType) {
        // Note: menuType can only be 2, so the other two menus are probably deprecated
        if (this.popup == null) {
            this.popup = new PopupMenu(String.valueOf(menuType));
            this.popup.addActionListener(this);
        } else {
            this.remove(this.popup);
            this.popup.removeAll();
            this.popup.setLabel(String.valueOf(menuType));
        }

        String menuItem;
        int ttSize;
        int i;
        label60:
        switch (menuType) {
            case 0: // unused - shows percentages from 5% to 100%
                ttSize = 0;

                while (true) {
                    if (ttSize >= 11) {
                        break label60;
                    }

                    menuItem = String.valueOf(ttSize == 0 ? 5 : ttSize * 10) + '%';
                    if (this.mg.iTT - 1 == ttSize) {
                        this.popup.add((MenuItem) (new CheckboxMenuItem(menuItem, true)));
                    } else {
                        this.popup.add(menuItem);
                    }

                    ++ttSize;
                }
            case 1: // unused - list of textures?
                ttSize = this.config.getInt("tt_size");
                i = 0;

                while (true) {
                    if (i >= ttSize) {
                        break label60;
                    }

                    menuItem = langSP.get("t042" + i);
                    if (this.mg.iTT - 12 == i) {
                        this.popup.add((MenuItem) (new CheckboxMenuItem(menuItem, true)));
                    } else {
                        this.popup.add(menuItem);
                    }

                    ++i;
                }
            case 2: // Pen Masks
                for (i = 0; i < 4; ++i) {
                    menuItem = (String) langSP.get("penm_" + i);
                    if (menuItem == null) {
                        break;
                    }

                    if (this.mg.iPenM == i) {
                        this.popup.add((MenuItem) (new CheckboxMenuItem(menuItem, true)));
                    } else {
                        this.popup.add(menuItem);
                    }
                }
        }

        this.add(this.popup);
        this.popup.show(this, x, y);
    }

    public void mPaint(int toolID) {
        Rectangle rect;
        if (toolID == -1) {
            rect = this.rPaint;
            rect.setSize(this.getSize());
            rect.setLocation(0, 0);
        } else if (toolID < this.list.length) {
            rect = this.rPaint;
            rect.setBounds(this.list[toolID].r);
        } else {
            rect = this.rects[toolID - this.list.length];
        }

        this.mPaint(this.primary(), rect);
    }

    public void mPaint(int x, int y, int width, int height) {
        Rectangle rect = this.rPaint;
        rect.setBounds(x, y, width, height);
        this.mPaint(this.primary(), rect);
    }

    private void mPaint(Graphics g, Rectangle rect) {
        if (this.rects != null && g != null && this.list != null) {
            Graphics background = this.getBack();
            if (rect == null) {
                rect = g.getClipBounds();
                if (rect == null || rect.isEmpty()) {
                    rect = new Rectangle(this.getSize());
                }
            }

            if (!rect.isEmpty()) {
                int toolCount = this.list.length;
                Dimension size = this.getSize();
                background.setFont(this.fDef);

                int toolID;
                for (toolID = 0; toolID < toolCount; ++toolID) {
                    if (this.list[toolID].r.intersects(rect)) {
                        this.list[toolID].paint(g, background);
                    }
                }

                background.setFont(this.fIg);
                int colorMode = this.isRGB ? 1 : 0;

                for (toolID = 0; toolID < this.rects.length; ++toolID) {
                    Rectangle toolRect = this.rects[toolID];
                    int currentSlider = toolID + toolCount;
                    if (toolRect.intersects(rect)) {
                        if (toolID < 14) {
                            Color color = new Color(COLORS[toolID]);
                            background.setColor(toolID == this.nowColor ? color.darker() : color.brighter());
                            background.drawRect(1, 1, toolRect.width - 2, toolRect.height - 2);
                            background.setColor(color);
                            background.fillRect(2, 2, toolRect.width - 3, toolRect.height - 3);
                            background.setColor(this.nowColor == toolID ? this.clSel : this.clFrame);
                        } else {
                            int maxLineSize;
                            int toolWidth;
                            int toolHeight;
                            switch (toolID) {
                                case 18: // line size
                                    boolean isTextTool = this.mg.isText();
                                    Color color = new Color(this.getRGB());
                                    maxLineSize = isTextTool ? 255 : this.info.getPMMax();
                                    toolWidth = toolRect.width - 10;
                                    toolHeight = toolRect.height - 2;
                                    background.setColor(this.clB2);
                                    background.fillRect(1, 1, toolRect.width - 2, toolHeight);
                                    if (this.mg.iSize >= maxLineSize) {
                                        this.mg.iSize = maxLineSize - 1;
                                    }

                                    // vertical slider
                                    background.setColor(color);
                                    background.fillRect(1, 1, toolWidth, (int) ((float) (this.mg.iSize + 1) / (float) maxLineSize * (float) toolHeight));
                                    if (this.info.getPenMask() == null) {
                                        return;
                                    }

                                    // size text
                                    background.setColor(this.clText);
                                    background.drawString((String) String.valueOf(this.mg.iSize), 6, toolHeight - 1);

                                    // increase/decrease buttons
                                    background.setColor(this.clFrame);
                                    background.fillRect(toolWidth, 1, 1, toolHeight);
                                    background.fillRect(toolWidth + 1, toolHeight / 2, 8, 1); // horizontal divider
                                    background.setColor(color);

                                    for (int arrowWidth = 3; arrowWidth >= 1; --arrowWidth) {
                                        background.fillRect(toolRect.width - 5 - arrowWidth, arrowWidth + 2, arrowWidth << 1, 1);
                                        background.fillRect(toolRect.width - 5 - arrowWidth, toolHeight - 2 - arrowWidth, arrowWidth << 1, 1);
                                    }

                                    background.fillRect(toolRect.width - 6, 5, 2, 8);
                                    background.fillRect(toolRect.width - 6, toolHeight - 11, 2, 8);
                                    break;
                                case 19: // layer
                                    background.setColor(this.clBar);
                                    background.fillRect(1, 1, toolRect.width - 1, toolRect.height - 2);
                                    if (this.info.layers != null && this.info.layers.length > this.mg.iLayer) {
                                        LO layer = this.info.layers[this.mg.iLayer];
                                        background.setColor(this.clText);
                                        if (layer.name != null) {
                                            background.drawString((String) layer.name, 2, toolRect.height - background.getFontMetrics().getMaxDescent() - 1);
                                        }

                                        if (layer.iAlpha == 0.0F) {
                                            background.setColor(Color.red);
                                            background.drawLine(1, 1, toolRect.width - 3, toolRect.height - 3);
                                        }
                                    }
                                    break;
                                default:
                                    // color sliders
                                    int channel = toolID - 14;
                                    int colorSliderHeight = toolRect.height;
                                    maxLineSize = toolID == 17 ? this.mg.iAlpha : this.iColor >>> (2 - channel) * 8 & 255;
                                    toolWidth = (int) ((float) (size.width - 10 - 2) / 255.0F * (float) maxLineSize);
                                    background.setColor(this.clB2);
                                    background.fillRect(0, 0, 5, colorSliderHeight - 1);
                                    background.fillRect(toolRect.width - 5, 1, 5, colorSliderHeight - 1);
                                    background.setColor(this.clFrame);
                                    background.fillRect(5, 1, 1, colorSliderHeight - 1);
                                    background.fillRect(toolRect.width - 5 - 1, 1, 1, colorSliderHeight - 1);
                                    if (toolWidth > 0) {
                                        background.setColor(clRGB[colorMode][channel]);
                                        background.fillRect(6, 1, toolWidth, toolRect.height - 2);
                                    }

                                    toolHeight = toolRect.width - 10 - toolWidth - 2;
                                    if (toolHeight > 0) {
                                        background.setColor(this.clBar);
                                        background.fillRect(toolWidth + 5 + 1, 1, toolHeight, toolRect.height - 2);
                                        background.setColor(clERGB[colorMode][channel]);
                                        background.fillRect(toolWidth + 5 + 1, 1, 1, toolRect.height - 2);
                                    }

                                    background.setColor(this.clText);
                                    background.drawString((String) (String.valueOf(this.clV[colorMode][channel]) + maxLineSize), 8, toolRect.height - 2);
                            }

                            background.setColor(this.nowButton == currentSlider ? this.clSel : this.clFrame);
                        }

                        background.drawRect(0, 0, toolRect.width - 1, toolRect.height - 1);
                        g.drawImage(this.imBack, toolRect.x, toolRect.y, toolRect.x + toolRect.width, toolRect.y + toolRect.height, 0, 0, toolRect.width, toolRect.height, Color.white, (ImageObserver) null);
                    }
                }

            }
        }
    }

    private void mPress(MouseEvent event) {
        int mouseX = event.getX();
        int mouseY = event.getY();
        int id = this.isClick(mouseX, mouseY); // -1 if nothing
        int toolID = id; // id value is changed during computations thus the original must be saved
        boolean isModifier = Awt.isR(event);
        this.nowButton = id;
        if (id >= 0) {
            if (id - this.list.length < 0) { // tool buttons
                if (isModifier) {
                    if (this.list[id].isField && this.list[id].isMask) {
                        this.mg.iColorMask = this.mg.iColor;
                        this.mPaint(id);
                    }

                    this.nowButton = -1;
                }

            } else {
                id -= this.list.length;
                Rectangle toolRect = this.rects[id];
                if (id - 14 < 0) { // palette buttons
                    if (isModifier) {
                        COLORS[id] = this.mg.iColor; // replaces palette color
                    } else if (event.isShiftDown()) {
                        COLORS[id] = DEFC[id]; // restores default color
                    } else { // select color
                        this.nowColor = id;
                        this.mg.iColor = COLORS[this.nowColor];
                        this.selPix(false);
                        this.toColor(this.mg.iColor);
                    }

                    this.up();
                } else { // color sliders
                    id -= 14;
                    int currentLayerIndex;
                    if (id - 4 < 0) {
                        currentLayerIndex = mouseX <= 5 ? -1 : (mouseX >= toolRect.width - 5 ? 1 : 0);
                        if (currentLayerIndex != 0) {
                            this.nowButton = -1;
                            if (isModifier) {
                                currentLayerIndex *= 5;
                            }
                        } else if (isModifier) { // switch between RGB and HSB
                            this.sCMode(this.isRGB);
                            this.nowButton = -1;
                            this.mPaint(-1);
                            return;
                        }

                        this.setRGB(id, currentLayerIndex, mouseX);
                    } else { // line size slider
                        id -= 4;
                        if (id - 1 < 0) {
                            if (isModifier) {
                                this.nowButton = -1;
                                this.menu(mouseX, mouseY, 2);
                            } else {
                                if (mouseX >= toolRect.x + toolRect.width - 10) {
                                    this.setLineSize(0, Math.max(this.mg.iSize + ((toolRect.y + toolRect.height - mouseY) / 2 >= 10 ? -1 : 1), 0));
                                    this.nowButton = -1;
                                } else {
                                    this.setLineSize(mouseY, -1);
                                }

                                this.mPaint(toolID);
                            }
                        } else { // layer
                            --id;
                            if (id - 1 < 0) {
                                currentLayerIndex = this.mg.iLayer;
                                if (isModifier) { // hide/show current layer
                                    LO currentLayer = this.info.layers[currentLayerIndex];
                                    currentLayer.iAlpha = (float) (currentLayer.iAlpha == 0.0F ? 1 : 0);
                                    this.mi.repaint();
                                } else { // open layers window or cycle between layers if already open
                                    if (this.L == null) {
                                        this.L = new L(this.mi, this, this.config);
                                        this.addC(this.L);
                                    }

                                    if (this.L.getParent() == null) {
                                        this.L.setVisible(this.isVisible);
                                        this.parent.add(this.L, 0);
                                    } else {
                                        if (++this.mg.iLayer >= this.info.L) {
                                            this.mg.iLayer = 0;
                                        }

                                        this.mg.iLayerSrc = this.mg.iLayer;
                                    }
                                }

                                if (this.L != null) {
                                    this.L.repaint();
                                }

                                this.mPaint(toolID);
                            }
                        }
                    }
                }
            }
        }
    }

    public void pack() {
        try {
            Container parent = this.parent;
            Dimension parentSize = parent.getSize();
            this.setSize(Math.min(this.W, parentSize.width), Math.min(this.H, parentSize.height));
            if (this.L != null) {
                this.L.inParent();
            }

            Dimension cSize = this.getCSize();
            Dimension sizeW = this.getSizeW();
            if (!this.mi.isGUI) {
                this.mi.setDimension((Dimension) null, cSize, cSize);
                cSize = this.mi.getSizeW();
                this.mi.setLocation((parentSize.width - sizeW.width - cSize.width) / 2 + (this.isWest ? sizeW.width : 0), (parentSize.height - cSize.height) / 2);
            }

            cSize = this.mi.getSizeW();
            Point location = this.mi.getLocation();
            this.setLocation(this.isWest ? Math.max(0, location.x - sizeW.width - 10) : Math.min(location.x + cSize.width + 10, parentSize.width - sizeW.width), (parentSize.height - sizeW.height) / 2);
            if (this.cs != null) {
                for (int i = 2; i < this.cs.length; ++i) {
                    ((SW) this.cs[i]).mPack();
                }
            }
        } catch (Throwable ex) {
            ;
        }

    }

    public void paint2(Graphics g) {
        try {
            g.setFont(this.fDef);
            this.mPaint(g, g.getClipBounds());
            if (this.primary != null) {
                this.primary.dispose();
                this.primary = null;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void pMouse(MouseEvent event) {
        try {
            if (this.rects == null) {
                return;
            }

            int mouseX = event.getX();
            int mouseY = event.getY();
            boolean isModifier = Awt.isR(event);

            if (this.list != null) {
                for (int i = 0; i < this.list.length; ++i) {
                    if (!this.list[i].isMask || !isModifier) {
                        this.list[i].pMouse(event);
                    }
                }
            }

            int eventID = event.getID();
            switch (eventID) {
                case MouseEvent.MOUSE_PRESSED:
                    this.mPress(event);
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    this.nowButton = -1;
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                default:
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    int id = this.nowButton;
                    // tool buttons
                    if (id < this.list.length) {
                        return;
                    }

                    // palette buttons
                    id -= this.list.length;
                    if (id - 14 < 0) {
                        return;
                    }

                    // color sliders
                    id -= 14;
                    if (id - 4 < 0) {
                        this.setRGB(id, 0, mouseX);
                        this.mPaint(-1);
                        this.upCS();
                        return;
                    }

                    // line size slider
                    id -= 4;
                    if (id - 1 < 0) {
                        this.setLineSize(mouseY, -1);
                        this.mPaint(this.nowButton);
                        return;
                    }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public Graphics primary() {
        if (this.primary == null) {
            this.primary = this.getG();
        }

        return this.primary;
    }

    protected void processEvent(AWTEvent event) {
        switch (event.getID()) {
            case ComponentEvent.COMPONENT_MOVED:
                int locationX = this.getLocation().x;
                int center = this.getParent().getSize().width / 2 - this.getSize().width / 2;
                if (locationX < center && !this.isWest) {
                    this.isWest = true;
                    this.pack();
                } else if (locationX >= center && this.isWest) {
                    this.isWest = false;
                    this.pack();
                }
                break;
            case ComponentEvent.COMPONENT_RESIZED:
            case ComponentEvent.COMPONENT_SHOWN:
                if (this.primary != null) {
                    this.primary.dispose();
                    this.primary = null;
                }
        }

        super.processEvent(event);
    }

    /** Alternates between RGB and HSB if true */
    private void sCMode(boolean switchColorMode) {
        this.isRGB = !switchColorMode;
        this.toColor(this.mg.iColor);
    }

    /** Switches between tool and eraser without losing the tool settings */
    public void selPix(boolean selectEraser) {
        if (this.list != null) {
            int toolCount = this.list.length;
            ToolList selected = null;
            ToolList eraser = null;

            for (int i = 0; i < toolCount; ++i) {
                ToolList tool = this.list[i];
                if (tool.isEraser) {
                    eraser = tool;
                }

                if (tool.isSelect) {
                    selected = tool;
                }
            }

            if (selectEraser) {
                if (selected != eraser) {
                    this.unSelect();
                    eraser.select();
                    this.mPaint(-1);
                }
            } else if (selected == eraser) {
                this.unSelect();
                this.list[this.oldPen].select();
                this.mPaint(-1);
            }

        }
    }

    public void setARGB(int argb) {
        this.mg.iAlpha = argb >>> 24;
        argb &= 0xFFFFFF;
        this.mg.iColor = argb;
        this.toColor(argb);
        this.mPaint(-1);
        this.upCS();
    }

    /** Overrides default palette from a list of hex colors separated by newlines */
    public void setC(String colorList) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(colorList));

            for (int i = 0; (colorList = in.readLine()) != null && colorList.length() > 0; DEFC[i++] = Integer.decode(colorList)) {
                ;
            }

            System.arraycopy(DEFC, 0, COLORS, 0, COLORS.length);
            this.repaint();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    /** Sets line size according to the maximum allowed by the current pen mask */
    public void setLineSize(int size) {
        this.setLineSize(0, Math.max(size, 0));
        this.mPaint(this.list.length + 5);
    }

    /** Sets line size according to the maximum allowed by the current pen mask and mouse position on slider */
    public void setLineSize(int mouseY, int size) {
        int maxSize = this.info.getPMMax();
        Rectangle sliderRect = this.rects[18]; //TODO: magic number
        if (size == -1) {
            size = (int) ((float) (mouseY - sliderRect.y) / (float) sliderRect.height * (float) maxSize);
        }

        size = size <= 0 ? 0 : (size >= maxSize ? maxSize - 1 : size);
        this.mg.iSize = size;
        this.upCS();
    }

    /** Changes the current color, either by increment/decrement or by mouse position on the slider rect */
    private void setRGB(int channel, int increment, int mouseX) {
        int bitShift = channel == 3 ? 24 : (2 - channel) * 8; // a=24, r=16, g=8, b=0
        int color = this.mg.iAlpha << 24 | this.iColor;
        int channelValue;
        if (increment != 0) {
            channelValue = color >>> bitShift & 255;
            channelValue += increment;
        } else {
            Rectangle sliderRect = this.rects[14 + channel];
            mouseX = mouseX - sliderRect.x - 4;
            channelValue = mouseX > 0 ? (int) ((float) mouseX / (float) (this.W - 8) * 255.0F) : 0;
        }

        channelValue = channelValue <= 0 ? 0 : (channelValue >= 255 ? 255 : channelValue);
        int otherChannelsMask = (255 << bitShift);
        otherChannelsMask = ~otherChannelsMask;
        color = color & otherChannelsMask | channelValue << bitShift;
        this.iColor = color & 0xFFFFFF;
        this.mg.iColor = this.getRGB();
        this.mg.iAlpha = Math.max(color >>> 24, 1); // it doesn't make sense to draw with 0 opacity
        this.mPaint(channel);
        // update current palette if selected
        if (this.nowColor >= 0) {
            COLORS[this.nowColor] = this.mg.iColor;
        }

        this.mPaint(-1);
        this.upCS();
    }

    /** Sets size of the toolbar */
    public void setSize(int width, int height) {
        if (this.applet == null) {
            super.setSize(width, height);
        } else if (width != this.fit_w || height != this.fit_h) {
            synchronized (this) {
                this.fit_w = width;
                this.fit_h = height;
                if (this.list == null) {
                    this.makeList();
                }

                if (this.rects == null) {
                    this.rects = new Rectangle[20];

                    for (int i = 0; i < this.rects.length; ++i) {
                        this.rects[i] = new Rectangle();
                    }
                }

                Rectangle[] rects = this.rects;
                float btnScaling = (float) height / (float) this.H;
                int toolHeight = (int) ((float) (this.IMH + 4) * btnScaling);
                if (!this.isLarge) {
                    toolHeight = Math.min(this.IMH + 4, toolHeight);
                }

                int paletteHeight = Math.min((height - (toolHeight + 1) * this.list.length - (int) (16.0F * btnScaling * 4.0F) - (int) (33.0F * btnScaling) - 3) / 8, (width - 1) / 2);
                this.fIg = new Font("sansserif", 0, (int) ((float) paletteHeight * 0.475F));
                this.fDef = new Font("sansserif", 0, (int) ((float) toolHeight * 0.43F));
                FontMetrics fontMetrics = this.getFontMetrics(this.fDef);
                int base = toolHeight - fontMetrics.getMaxDescent() - 2;
                int offY = 0;

                // tool buttons
                for (int i = 0; i < this.list.length; ++i) {
                    this.list[i].r.setLocation(0, offY);
                    this.list[i].setSize(this.W, toolHeight, base);
                    offY += toolHeight + 1;
                }

                int halfWidth = (width - 1) / 2;

                int i;
                Rectangle btnRect;
                // palette buttons
                for (i = 0; i < 14; ++i) {
                    btnRect = rects[i];
                    btnRect.setBounds(i % 2 == 1 ? halfWidth + 1 : 0, offY, i % 2 == 1 ? width - halfWidth - 1 : halfWidth, paletteHeight);
                    if (i % 2 == 1) {
                        offY += paletteHeight + 1;
                    }
                }

                // color sliders
                for (halfWidth = (int) (15.0F * btnScaling); i < 18; ++i) {
                    btnRect = rects[i];
                    btnRect.setBounds(0, offY, width, halfWidth);
                    offY += halfWidth + 1;
                }

                // line size slider
                halfWidth = (int) (32.0F * btnScaling);
                btnRect = rects[i++];
                btnRect.setBounds(0, offY, width, halfWidth);

                // layer
                offY += halfWidth + 1;
                halfWidth = Math.min(height - offY, paletteHeight);
                btnRect = rects[i++];
                btnRect.setBounds(0, height - halfWidth, width, halfWidth);

                super.setSize(width, height);
            }
        }
    }

    /** Shows a window by class name */
    public void showW(String className) {
        if (this.isVisible) {
            int maxY = 0;
            if (this.cs != null) {
                for (int i = 0; i < this.cs.length; ++i) {
                    LComponent component = this.cs[i];
                    if (component.getClass().getName().equals(className)) {
                        if (component.getParent() == null) {
                            component.setVisible(this.isVisible);
                            this.parent.add(component, 0);
                        }

                        return;
                    }

                    maxY = Math.max(maxY, component.getLocation().y);
                }
            }

            if (this.ws != null) {
                for (int i = 0; i < this.ws.length; ++i) {
                    if (this.ws[i].getClass().getName().equals(className)) {
                        if (!this.ws[i].isVisible()) {
                            this.ws[i].setVisible(true);
                        }

                        return;
                    }
                }
            }

            try {
                SW window = (SW) Class.forName(className).newInstance();
                if (window instanceof Window) {
                    this.addC(window);
                    window.mSetup(this, this.info, this.mi.user, this.mg, this.config);
                } else {
                    LComponent component = (LComponent) window;
                    this.addC(component);
                    component.setVisible(false);
                    window.mSetup(this, this.info, this.mi.user, this.mg, this.config);
                    this.parent.add(component, 0);
                    component.setLocation(0, maxY);
                    component.inParent();
                    window.mPack();
                    component.setVisible(this.isVisible);
                }
            } catch (Throwable ex) {
                this.mi.alert(ex.getLocalizedMessage(), false);
            }

        }
    }

    /** Changes the current RGB/HSB color applying the current alpha */
    private void toColor(int rgb) {
        if (!this.isRGB) {
            Color.RGBtoHSB(rgb >>> 16 & 255, rgb >>> 8 & 255, rgb & 255, this.fhsb);
            this.iColor = this.mg.iAlpha << 24 | (int) (this.fhsb[0] * 255.0F) << 16 | (int) (this.fhsb[1] * 255.0F) << 8 | (int) (this.fhsb[2] * 255.0F);
        } else {
            this.iColor = this.mg.iAlpha << 24 | rgb;
        }

    }

    public void unSelect() {
        for (int i = 0; i < this.list.length; ++i) {
            ToolList toolList = this.list[i];
            if (toolList.isSelect) {
                if (!toolList.isEraser) {
                    this.oldPen = i;
                }

                toolList.unSelect();
            }
        }

    }

    public void up() {
        try {
            this.mPaint(-1);
            this.mi.up();
            this.upCS();
            if (this.L != null) {
                this.L.repaint();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void upCS() {
        if (this.cs != null) {
            for (int i = 2; i < this.cs.length; ++i) {
                if (this.cs[i] instanceof SW) {
                    ((SW) this.cs[i]).up();
                }
            }

            if (this.L != null) {
                this.L.repaint();
            }

        }
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    /** Shows/hides various tool windows */
    public void mVisible(boolean isVisible) {
        this.isVisible = isVisible;
        if (this.cs != null) {
            for (int i = 0; i < this.cs.length; ++i) {
                if (this.cs[i] != this) {
                    this.cs[i].setVisible(isVisible);
                }
            }
        }

    }
}
