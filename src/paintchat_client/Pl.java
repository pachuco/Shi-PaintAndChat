package paintchat_client;

import jaba.applet.Applet;

import java.applet.AudioClip;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import paintchat.M;
import paintchat.MgText;
import paintchat.Res;
import paintchat.ToolBox;
import syi.awt.Awt;
import syi.awt.LButton;
import syi.awt.LComponent;
import syi.awt.TextPanel;
import syi.util.ThreadPool;

public class Pl extends Panel implements Runnable, ActionListener, IMi, KeyListener {
    private static final String STR_VERSION = "PaintChatClient v3.66";
    private static final String STR_INFO = "PaintChat";
    protected Applet applet;
    private boolean isStart = false;
    private int iScrollType = 0;
    private Data dd;
    public Res res;
    public Mi mi;
    private ToolBox tool = null;
    private Panel tPanel;
    private Panel tPanelB;
    private TextPanel tText;
    private TextField tField;
    private TextPanel tList;
    private Panel miPanel;
    private Label tLabel;
    private MgText mgText;
    private Dimension dPack = new Dimension();
    private Dimension dSize = null;
    private Dimension dMax = new Dimension();
    private int iGap = 5;
    private int iCenter = 80;
    private int iCenterOld = -1;
    private Color clInfo;
    private AudioClip[] sounds = null;
    private int iPG = 10;

    public Pl(Applet var1) {
        super((LayoutManager) null);
        this.applet = var1;
    }

    public void actionPerformed(ActionEvent var1) {
        try {
            Object var2 = var1.getSource();
            if (var2 instanceof LButton) {
                switch (Integer.parseInt(((Component) var2).getName())) {
                    case 0:
                        this.f(this.tPanel, true);
                        break;
                        //case 1: f(this, false);
                    case 1:
                        this.mExit();
                }
            } else {
                this.typed();
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void mExit() {
        try {
            this.applet.getAppletContext().showDocument(new URL(this.applet.getDocumentBase(), this.dd.config.getP("exit", "../index.html")));
        } catch (Throwable var1) {
            ;
        }

    }

    protected void addInOut(String var1, boolean var2) {
        if (var2) {
            this.tList.addText(var1);
            var1 = var1 + this.res.res("entered");
            this.dSound(2);
        } else {
            this.tList.remove(var1);
            var1 = var1 + this.res.res("leaved");
            this.dSound(3);
        }

        this.addTextInfo(var1, false);
    }

    protected void addSText(String var1) {
        this.tText.decode(var1);
    }

    protected void addText(String var1, String var2, boolean var3) {
        if (var2 == null) {
            this.tText.repaint();
        } else {
            this.tText.addText(var1 == null ? var2 : "<" + var1 + "> " + var2, var3);
        }

    }

    protected void addTextInfo(String var1, boolean var2) {
        Color var3 = this.tText.getForeground();
        this.tText.setForeground(Color.red);
        this.addText((String) null, "PaintChat: " + var1, var2);
        this.tText.setForeground(var3);
    }

    public void changeSize() {
        this.mi.resetGraphics();
        this.pack();
    }

    public void destroy() {
        try {
            if (this.dd != null) {
                this.dd.destroy();
            }

            this.dd = null;
            this.tool = null;
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    protected void dSound(int var1) {
        try {
            if (this.sounds == null || this.sounds[var1] == null) {
                return;
            }

            this.sounds[var1].play();
        } catch (RuntimeException var2) {
            this.sounds = null;
        }

    }

    private void f(final Component var1, boolean var2) {
        try {
            boolean var3 = false;
            Object var4 = var2 ? this : this.applet;
            Component[] var5 = ((Container) var4).getComponents();

            for (int var6 = 0; var6 < var5.length; ++var6) {
                if (var5[var6] == var1) {
                    var3 = true;
                    break;
                }
            }

            Container var8 = var1.getParent();
            var8.remove(var1);
            if (var3) {
                if (var2) {
                    this.iCenter = 100;
                }

                this.pack();
                Frame var9 = new Frame("PaintChatClient v3.66");
                var9.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent we) { f(var1, true); }
                });
                var9.setLayout(new BorderLayout());
                var9.add((Component) var1, (Object) "Center");
                var9.pack();
                var9.setVisible(true);
            } else {
                ((Window) var8).dispose();
                if (var2) {
                    this.iCenter = 70;
                    this.add(var1);
                } else {
                    this.applet.add((Component) var1, (Object) "Center");
                    this.applet.validate();
                }

                this.pack();
            }
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

    }

    public Dimension getSize() {
        if (this.dSize == null) {
            this.dSize = super.getSize();
        }

        return this.dSize;
    }

    public void iPG(boolean var1) {
        if (var1) {
            this.iPG = Math.min(100, this.iPG + 10);
        }

        if (!this.isStart) {
            try {
                Graphics var2 = this.getGraphics();
                if (var2 == null) {
                    return;
                }

                String var3 = String.valueOf(this.iPG) + '%';
                FontMetrics var4 = var2.getFontMetrics();
                int var5 = var4.getHeight() + 2;
                var2.setColor(this.getBackground());
                var2.fillRect(5, 5 + var5, var4.stringWidth(var3) + 15, var5 + 10);
                var2.setColor(this.getForeground());
                var2.drawString((String) "PaintChatClient v3.66", 10, 10 + var5);
                var2.drawString((String) var3, 10, 10 + var5 * 2);
                var2.dispose();
            } catch (Throwable var6) {
                ;
            }

        }
    }

    public void keyPressed(KeyEvent var1) {
        try {
            boolean var2 = var1.isAltDown() || var1.isControlDown();
            int var3 = var1.getKeyCode();
            if (var2) {
                if (var3 == 38) {
                    var1.consume();
                    this.iCenter = Math.max(this.iCenter - 4, 0);
                    this.pack();
                }

                if (var3 == 40) {
                    var1.consume();
                    this.iCenter = Math.min(this.iCenter + 4, 100);
                    this.pack();
                }

                if (var3 == 83) {
                    var1.consume();
                    this.typed();
                }
            } else {
                switch (var3) {
                    case 10:
                        break;
                    case 117:
                        this.f(this, false);
                        break;
                    default:
                        this.dSound(0);
                }
            }
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public void keyReleased(KeyEvent var1) {
    }

    public void keyTyped(KeyEvent var1) {
    }

    private Cursor loadCursor(String var1, int var2) {
        try {
            if (var1 != null && var1.length() > 0) {
                boolean var3 = var1.equals("none");
                int var4;
                int var5;
                int var6;
                int var7;
                Image var8;
                if (!var3) {
                    var8 = this.getToolkit().createImage((byte[]) this.dd.config.getRes(var1));
                    if (var8 == null) {
                        return Cursor.getPredefinedCursor(var2);
                    }

                    Awt.wait(var8);
                    var6 = var8.getWidth((ImageObserver) null);
                    var7 = var8.getHeight((ImageObserver) null);
                    var4 = var1.indexOf(120);
                    if (var4 == -1) {
                        var4 = var4 == -1 ? var6 / 2 - 1 : Integer.parseInt(var1.substring(var4 + 1, var1.indexOf(120, var4 + 1)));
                    }

                    var5 = var1.indexOf(121);
                    if (var5 == -1) {
                        var5 = var5 == -1 ? var7 / 2 - 1 : Integer.parseInt(var1.substring(var5 + 1, var1.indexOf(121, var5 + 1)));
                    }
                } else {
                    var5 = 7;
                    var4 = 7;
                    var7 = 16;
                    var6 = 16;
                    var8 = null;
                }

                try {
                    if (var8 == null) {
                        IndexColorModel var9 = new IndexColorModel(8, 2, new byte[2], new byte[2], new byte[2], 0);
                        var8 = this.createImage(new MemoryImageSource(var6, var7, var9, new byte[var6 * var7], 0, var6));
                    }

                    Toolkit var20 = this.getToolkit();
                    var20.getClass();
                    Method var10 = Toolkit.class.getMethod("createCustomCursor", Image.class, Point.class, String.class);
                    Method var11 = Toolkit.class.getMethod("getBestCursorSize", Integer.TYPE, Integer.TYPE);
                    Dimension var12 = (Dimension) var11.invoke(var20, new Integer(var6), new Integer(var7));
                    if (var12.width != 0 && var12.height != 0) {
                        return (Cursor) var10.invoke(var20, var8, new Point((int) ((float) var6 / (float) var12.width * (float) var4), (int) ((float) var7 / (float) var12.height * (float) var5)), "custum");
                    }
                } catch (NoSuchMethodException var18) {
                    if (var8 == null) {
                        var8 = this.createImage(new MemoryImageSource(var6, var7, new int[var6 * var7], 0, var6));
                    }

                    return (Cursor) Class.forName("com.ms.awt.CursorX").getConstructors()[0].newInstance(var8, new Integer(var4), new Integer(var5));
                }
            }
        } catch (Throwable var19) {
            var19.printStackTrace();
        }

        return Cursor.getPredefinedCursor(var2);
    }

    private void loadSound() {
        if (this.sounds != null) {
            this.sounds = null;
        } else {
            this.sounds = new AudioClip[4];
            String[] var1 = new String[]{"tp.au", "talk.au", "in.au", "out.au"};

            for (int i = 0; i < 4; ++i) {
                String var2 = this.dd.config.res(var1[i]);
                if (var2 != null && var2.length() > 0 && var2.charAt(0) != '_') {
                    URL url = getClass().getResource("/snd/" + var1[i]);
                    AudioClip snd = this.applet.getAudioClip(url);
                    if (snd == null) {
                        try {
                            snd = this.applet.getAudioClip(new URL(this.applet.getCodeBase(), var2));
                        } catch (MalformedURLException e) {
                            snd = null;
                        }
                    }
                    sounds[i] = snd;
                }
            }
        }
    }

    private synchronized void mkTextPanel() {
        if (this.tField == null) {
            String var1 = "Center";
            String var2 = "East";
            String var3 = "West";
            Panel var4 = new Panel(new BorderLayout());
            this.tField = new TextField();
            this.tField.addActionListener(this);
            var4.add((Component) this.tField, (Object) var1);
            this.tLabel = new Label(this.dd.res.res("input"));
            var4.add((Component) this.tLabel, (Object) var3);
            String[] var5 = new String[]{"F", "leave"};
            Panel var6 = new Panel(new FlowLayout(0, 2, 1));
            this.tPanelB = var6;

            for (int var8 = 0; var8 < var5.length; ++var8) {
                LButton var7 = new LButton(this.res.res(var5[var8]));
                var7.addActionListener(this);
                var7.setName(String.valueOf(var8));
                var6.add(var7);
            }

            var4.add((Component) var6, (Object) var2);
            Color var10 = this.getBackground();
            Color var9 = this.getForeground();
            this.tText = new TextPanel(this.applet, 100, var10, var9, this.tField);
            this.tList = new TextPanel(this.applet, 20, var10, var9, this.tField);
            this.tPanel = new Panel(new BorderLayout());
            this.tPanel.add((Component) this.tText, (Object) var1);
            this.tPanel.add((Component) this.tList, (Object) var2);
            this.tPanel.add((Component) var4, (Object) "South");
            Awt.getDef(this.tPanel);
            Awt.setDef(this.tPanel, false);
        }
    }

    private void pack() {
        this.dSize = super.getSize();
        if (this.tool != null && this.mi != null && this.dPack != null) {
            this.getSize();
            ThreadPool.poolStartThread(this, 'p');
        }
    }

    public void paint(Graphics var1) {
        if (!this.isStart) {
            this.iPG(false);
        }

        Dimension var2 = this.getSize();
        var1.drawRect(0, 0, var2.width - 1, var2.height - 1);
    }

    protected void processEvent(AWTEvent var1) {
        int var2 = var1.getID();
        if (var2 == 101) {
            this.dSize = super.getSize();
            Dimension var3 = this.getSize();
            this.setSize(var3.getSize());
            if (this.dPack != null && !this.dPack.equals(var3)) {
                this.pack();
            }
        } else if (this.mi != null && var1 instanceof MouseEvent) {
            Point var4 = this.mi.getLocation();
            ((MouseEvent) var1).translatePoint(-var4.x, -var4.y);
            this.mi.dispatchEvent(var1);
        }

        super.processEvent(var1);
    }

    public void repaint(long var1, int var3, int var4, int var5, int var6) {
        this.repaint(this, var3, var4, var5, var6);
    }

    private void repaint(Component var1, int var2, int var3, int var4, int var5) {
        int var7;
        if (var1 instanceof Container) {
            Component[] var6 = ((Container) var1).getComponents();

            for (var7 = 0; var7 < var6.length; ++var7) {
                Point var8 = var6[var7].getLocation();
                this.repaint(var6[var7], var2 - var8.x, var3 - var8.y, var4, var5);
            }
        } else {
            Point var9 = var1.getLocation();
            var7 = var2 - var9.x;
            int var10 = var3 - var9.y;
            if (var7 + var4 <= 0 || var10 + var5 <= 0) {
                return;
            }

            var1.repaint(var7, var10, var4, var5);
        }

    }

    private void rInit() {
        String var1 = "cursor_";
        String var2 = "window_color_";

        try {
            this.getSize();
            this.dd = new Data(this);
            this.mgText = new MgText();
            this.mi = new Mi(this, this.res);
            this.iPG(true);
            this.dd.mi = this.mi;
            this.dd.init();
            this.res = this.dd.res;
            Res var4 = this.dd.config;
            int var5 = var4.getP("layer_count", 2);
            int var6 = var4.getP("quality", 1);

            try {
                Color var7 = new Color(var4.getP("color_bk", 13619199));
                this.applet.setBackground(var7);
                this.setBackground(var7);
                var7 = new Color(var4.getP(var2 + "_bk", var7.getRGB()));
                Awt.cC = var7;
                Awt.cBk = var7;
                var7 = new Color(var4.getP("color_text", 5263480));
                this.applet.setForeground(var7);
                this.setForeground(var7);
                Awt.cFore = new Color(var4.getP(var2 + "_text", var7.getRGB()));
                Awt.cFSel = new Color(var4.getP("color_iconselect", 15610675));
                Awt.cF = new Color(var4.getP(var2 + "_frame", 0));
                Awt.clBar = new Color(var4.getP(var2 + "_bar", 6711039));
                Awt.clLBar = new Color(var4.getP(var2 + "_bar_hl", 8947967));
                Awt.clBarT = new Color(var4.getP(var2 + "_bar_text", 0xFFFFFF));
                Awt.getDef(this);
                Awt.setPFrame((Frame) Awt.getParent(this));
            } catch (Throwable var13) {
                ;
            }

            this.iPG(true);
            Cursor[] var16 = new Cursor[4];
            byte var3 = 0;
            int[] var8 = new int[]{var3, 13, var3, var3};

            for (int var15 = 0; var15 < 4; ++var15) {
                var16[var15] = this.loadCursor(this.applet.getParameter(var1 + (var15 + 1)), var8[var15]);
            }

            this.iPG(true);
            this.miPanel = new Panel((LayoutManager) null);
            this.mi.init(this.applet, this.dd.config, this.dd.imW, this.dd.imH, var6, var5, var16);
            this.miPanel.add(this.mi);
            this.iPG(true);
            String var9 = var4.getP("tools", "normal");

            try {
                this.tool = (ToolBox) Class.forName("paintchat." + var9 + ".Tools").newInstance();
                this.tool.init(this.miPanel, this.applet, this.dd.config, this.res, this.mi);
            } catch (Throwable var12) {
                var12.printStackTrace();
            }

            this.mkTextPanel();
            this.tField.addKeyListener(this);
            this.enableEvents(9L);
            this.isStart = true;
            this.add(this.tPanel);
            this.add(this.miPanel);
            this.tField.requestFocus();
            this.iPG(true);
            this.pack();
            if (this.dd.config.getP("Client_Sound", false)) {
                this.loadSound();
            }

            DCF var10 = new DCF(this.res);
            var10.mShow();
            String var11 = var10.mGetHandle();
            if (var11.length() <= 0) {
                this.mExit();
                return;
            }

            this.dd.strName = var11;
            this.dd.config.put("chat_password", var10.mGetPass());
            this.dd.start();
            this.addInOut(var11, true);
        } catch (Throwable var14) {
            var14.printStackTrace();
        }

    }

    private synchronized void rPack() {
        Dimension var1 = this.getSize();
        this.dPack.setSize(var1);
        this.setVisible(false);
        int var2 = this.iGap;
        int var3 = (int) ((float) var1.height * ((float) this.iCenter / 100.0F));
        if (this.miPanel != null) {
            this.miPanel.setBounds(0, 0, var1.width, var3);
        }

        if (this.tool != null) {
            this.tool.pack();
            if (this.tPanel != null && this.tPanel.getParent() == this) {
                byte var4 = 0;
                this.tPanel.setBounds(var4, var3 + var2, var1.width - var4, var1.height - (var3 + var2));
                this.validate();
            }
        }

        this.mi.resetGraphics();
        this.setVisible(true);
    }

    public void run() {
        try {
            switch (Thread.currentThread().getName().charAt(0)) {
                case 'i':
                    this.rInit();
                    break;
                case 'p':
                    this.rPack();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void scroll(boolean var1, int var2, int var3) {
        LComponent[] var4 = this.tool.getCs();
        int var5 = var4.length;
        Point var13 = this.mi.getLocation();
        int var14 = var13.x + this.mi.getGapX();
        int var15 = var13.y + this.mi.getGapY();
        Dimension var16 = this.mi.getSizeW();

        for (int var17 = 0; var17 < var5; ++var17) {
            LComponent var6 = var4[var17];
            Point var8 = var6.getLocation();
            Dimension var7 = var6.getSizeW();
            if ((var8.x + var7.width > var13.x && var8.y + var7.height > var13.y && var8.x < var13.x + var16.width && var8.y < var13.y + var16.height || var6.isEscape) && var6.isVisible()) {
                if (this.iScrollType == 0) {
                    int var10 = var8.x - var14;
                    int var11 = var8.y - var15;
                    int var12 = var7.width;
                    int var10000 = var7.height;
                    if (var2 > 0) {
                        this.mi.m_paint(var10 - var2, var11, var2, var7.height);
                    }

                    if (var2 < 0) {
                        this.mi.m_paint(var10 + var12, var11, -var2, var7.height);
                    }

                    var12 += Math.abs(var2);
                    if (var3 < 0) {
                        this.mi.m_paint(var10 - var2, var11 + var7.height, var12, -var3);
                    }

                    if (var3 > 0) {
                        this.mi.m_paint(var10 - var2, var11 - var3, var12, var3);
                    }
                } else {
                    boolean var9 = var6.isEscape;
                    var6.escape(var1);
                    if (!var9) {
                        this.mi.m_paint(var8.x - var13.x, var8.y - var13.y, var7.width, var7.height);
                    }
                }
            }
        }

    }

    public void send(M var1) {
        this.dd.send(var1);
    }

    public void setARGB(int var1) {
        var1 &= 0xFFFFFF;
        this.tool.selPix(this.mi.info.m.iLayer != 0 && var1 == 0xFFFFFF);
        if (this.mi.info.m.iPen != 4 && this.mi.info.m.iPen != 5) {
            this.tool.setARGB(this.mi.info.m.iAlpha << 24 | var1);
        }

    }

    private void setDefComponent(Container var1) {
        try {
            if (var1 == null) {
                return;
            }

            Color var3 = var1.getForeground();
            Color var4 = var1.getBackground();
            Component[] var5 = var1.getComponents();
            if (var5 != null) {
                for (int var6 = 0; var6 < var5.length; ++var6) {
                    Component var2 = var5[var6];
                    var2.setBackground(var4);
                    var2.setForeground(var3);
                    if (var2 instanceof Container) {
                        this.setDefComponent((Container) var2);
                    }
                }
            }
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

    }

    public void setLineSize(int var1) {
        this.tool.setLineSize(var1);
    }

    private void typed() {
        try {
            String msg = this.tField.getText();
            if (msg == null || msg.length() <= 0) {
                return;
            }

            this.tField.setText("");
            if (msg.length() > 256) {
                this.mi.alert("longer it", false);
                return;
            }

            if (this.mi.info.m.isText()) {
                this.mi.addText(msg);
            } else {
                this.mgText.setData(0, (byte) 0, (String) msg);
                this.dd.send(this.mgText);
                msg = '<' + this.dd.strName + "> " + msg;
                this.tText.addText(msg, true);
                this.dSound(1);
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void undo(boolean var1) {
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
