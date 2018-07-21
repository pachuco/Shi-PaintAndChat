package c;

import jaba.applet.Applet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Locale;

import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.FileDlg;
import syi.awt.NewImage;
import syi.util.ByteStream;

import javax.imageio.ImageIO;

public class ShiPainter extends Applet implements Runnable, ActionListener {
    public int isStart = 0;
    private P p;
    private MBar mbar;
    private Ts ts;
    private Res res;
    private Res config;
    public String str_header = null;
    private Mi mi;
    private String[] sj;
    private Frame frame;
    private String lastMode;
    private File lastFile;
    private String lastType;
    private String lastFName;

    public void destroy() {
        try {
            if (this.p.getParent() != this) {
                ((Window) this.p.getParent().getParent()).dispose();
            }

            this.removeAll();
            this.p = null;
            this.ts = null;
            this.mbar = null;
        } catch (Throwable var1) {
            ;
        }

    }

    public String getColors() {
        return this.p.tool.getC();
    }

    public M.Info getInfo() {
        return this.mi.info;
    }

    public int getLSize() {
        return this.mi.info.L;
    }

    public Mi getMi() {
        return this.mi;
    }

    public ToolBox getToolBox() {
        return this.p.tool;
    }

    protected void jump(String var1, String var2) {
        try {
            this.getAppletContext().showDocument(new URL(this.getCodeBase(), var1), var2 == null ? "_self" : var2);
        } catch (Throwable var4) {
            Ts.alert(var4.getMessage());
        }

    }

    public void paint(Graphics var1) {
        try {
            if (this.isStart >= 2) {
                return;
            }

            FontMetrics var2 = var1.getFontMetrics();
            var1.drawString((String) "Wait for initialization to complete.", 10, var2.getHeight() + 10);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void pExit() {
        Ts.run(this, 's', 2);
    }

    protected void processEvent(AWTEvent var1) {
        try {
            int var2 = var1.getID();
            if (var2 == 101 && this.ts != null) {
                this.ts.pack();
            }
        } catch (Throwable var3) {
            ;
        }

    }

    private void rInit() throws Throwable {
        rInit(-1, -1, null, null);
    }

    private void rInit(int width, int height, String imgPath, String mode) throws Throwable {
        try {
            this.isStart = 1;
            this.setLayout((LayoutManager) null);
            this.ts = new Ts();
            this.p = new P(this);
            this.mbar = new MBar(this);
            URL var1 = this.getCodeBase();
            String var2 = this.p.p("dir_resource", "./res/");
            char var4 = var2.charAt(var2.length() - 1);
            Object var3;
            if (var4 != '&' && var4 != '?' && var4 != '=') {
                if (var2.charAt(var2.length() - 1) != '/') {
                    var2 = var2 + '/';
                }

                var3 = new URL(var1, var2);
            } else {
                var3 = var2;
            }

            this.config = new Res(this, var3, (ByteStream) null);
            this.res = new Res(this, var3, (ByteStream) null);

            try {
                String var5 = this.p.p("res.zip", "res/res.zip");
                if (var5.equals("res_normal.zip")) {
                    var5 = "res.zip";
                }

                if (var5.equals("res_pro.zip")) {
                    var5 = "res.zip";
                }

                this.config.loadZip(var5);
            } catch (Throwable var9) {
                var9.printStackTrace();
            }

            try {
                this.config.load(new String((byte[]) this.config.getRes("param_utf8.txt"), "UTF8"));
                this.config.remove("param_utf8.txt");
            } catch (Throwable var8) {
                Ts.alert(var8.getMessage());
            }

            Res var11 = this.config;
            Color var6 = new Color(var11.getP("color_bk", 0xcfcfff));
            this.setBackground(var6);
            this.p.setBackground(var6);
            var6 = new Color(var11.getP("window_color_bk", var6.getRGB()));
            Awt.cC = var6;
            Awt.cBk = var6;
            var6 = new Color(var11.getP("color_text", 0x505078));
            this.setForeground(var6);
            this.p.setForeground(var6);
            Awt.cFore = new Color(var11.getP("window_color_text", var6.getRGB()));
            Awt.cFSel = new Color(var11.getP("color_iconselect", 0xee3333));
            Awt.cF = new Color(var11.getP("window_color_frame", 0x000000));
            Awt.clBar = new Color(var11.getP("window_color_bar", 0x6666ff));
            Awt.clLBar = new Color(var11.getP("window_color_bar_hl", 0x8888ff));
            Awt.clBarT = new Color(var11.getP("window_color_bar_text", 0xFFFFFF));

            try {
                Awt.setPFrame((Frame) Awt.getParent(this));
            } catch (RuntimeException var7) {
                ;
            }

            this.res.loadResource(this.config, "res", Locale.getDefault().getLanguage());
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

        this.enableEvents(9L);
        this.add(this.ts);
        this.add(this.p);
        p.loadImW = width;
        p.loadImH = height;
        p.loadPath = imgPath;
        p.loadMode = mode;
        this.p.init(this.config, this.res, this.ts);
        this.ts.init(this, this.p, this.res, this.config);
        this.mi = this.p.mi;
        this.ts.layout(this.config.getP("bar_layout", 2));
        this.isStart = 2;
        this.p.repaint();
        if (this.p.p("popup_parent", (String) null) != null) {
            this.ts.isP = new Boolean(this.p.p("popup_parent", false));
        }

        if (this.p.p("popup", false)) {
            this.ts.setV(2, false);
            this.ts.w(false);
        }

        if (this.d_isDesktop()) {
            if (lastMode == null) lastMode = config.getP("tools", "normal");
            frame = d_getFrame();
            frame.setMenuBar(mbar);
        }
    }

    public void run() {
        try {
            switch (Thread.currentThread().getName().charAt(0)) {
                case 'i':
                    this.rInit();
                    break;
                case 'j':
                    String[] var1 = this.sj;
                    synchronized (this.sj) {
                        for (int var3 = 0; var3 < this.sj.length; ++var3) {
                            String var2;
                            if ((var2 = this.sj[var3]) != null) {
                                this.sj[var3] = null;
                                this.mi.send(var2);
                            }
                        }

                        return;
                    }
                case 'p':
                    this.ts.pack();
                    break;
                case 's':
                    this.p.rSave();
            }
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }

    public void send(String var1, boolean var2) {
        try {
            if (var2) {
                if (this.sj == null) {
                    this.sj = new String[32];
                }

                String[] var3 = this.sj;
                synchronized (this.sj) {
                    int var4 = 0;

                    while (var4 < 32) {
                        if (this.sj[var4] != null) {
                            ++var4;
                        } else {
                            this.sj[var4] = var1;
                            break;
                        }
                    }
                }

                Ts.run(this, 'j', 1);
            } else {
                this.mi.send(var1);
            }
        } catch (Throwable var6) {
            var6.printStackTrace();
        }

    }

    public void setColors(String var1) {
        if (this.p != null && this.p.tool != null) {
            this.p.tool.setC(var1);
        }

    }

    public void start() {
        try {
            if (this.isStart == 0) {
                Ts.run(this, 'i', 3);
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void update(Graphics var1) {
        this.paint(var1);
    }

    public void mPermission(String var1) {
        int var2 = 0;
        int var4 = var1.length();

        int var3;
        do {
            var3 = var1.indexOf(59, var2);
            if (var3 < 0) {
                var3 = var4;
            }

            if (var3 - var2 > 0) {
                this.p.mP(var1.substring(var2, var3));
            }

            var2 = var3 + 1;
        } while (var3 < var4);

    }

    public void newImg() {
        Dimension dim;
        String mode = lastMode;
        try {

            if(mi == null || mi.info == null){
                dim = new Dimension(300, 300);
            }else {
                dim = mi.info.getCanvasSize();
            }

            NewImage ni = new NewImage(d_getFrame(), dim, lastMode);
            dim = ni.getDim();
            mode = ni.getMode();
            if(dim != null) {
                isStart = 0;
                destroy();
                paint(getGraphics());
                rInit(dim.width, dim.height, null, mode);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        lastMode = mode;
        setLastFile(null);
    }

    public void openImg() {
        FileDlg fileBr = new FileDlg(frame, "Open SPCH/Image", FileDialog.LOAD, "*.spch;*.jpg;*.png;*.gif");
        File f =fileBr.getF();
        if(f == null) return;

        String fnl = f.getName().toLowerCase();
        String path = "file:///" + f.getPath();

        //get image size and initiate canvas with it if it's not a PCH
        int imW = 300;
        int imH = 300;
        try {
            isStart = 0;
            destroy();
            paint(getGraphics());

            Image img = ImageIO.read(f.toURI().toURL());
            Awt.wait(img);

            if(img != null) {
                imW = img.getWidth(null);
                imH = img.getHeight(null);
            }

            setLastFile(f);
            rInit(imW, imH, path, lastMode);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    void setLastFile(File f) {
        if (f == null) {
            lastFile = null;
            lastFName = null;
        } else {
            String tf = f.getName();
            String tfl = tf.toLowerCase();
            lastFile = f;
            lastFName = tf.substring(0, tf.lastIndexOf('.'));
            if (tfl.endsWith(".png")) {
                lastType = "png";
            } else if (tfl.endsWith(".jpg") || tfl.endsWith(".jpeg")) {
                lastType = "jpg";
            } else if (tfl.endsWith(".spch")) {
                lastType = "pch";
            } else {
                lastType = "png";
            }
        }
        System.out.println(lastFName);
        System.out.println(lastType);
    }

    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        if        (com.equals(MBar.hNew)) {
            newImg();
        } else if (com.equals(MBar.hOpen)) {
            openImg();
        } else if (com.equals(MBar.hSave)) {
        } else if (com.equals(MBar.hSvAsJPG)) {
        } else if (com.equals(MBar.hSvAsPNG)) {
        } else if (com.equals(MBar.hSvAsAni)) {
        } else if (com.equals(MBar.hExit)) {
            System.exit(0);
        } else if (com.equals(MBar.hToggleScr)) {
        }
    }
}
