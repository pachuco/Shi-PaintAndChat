package c;

import jaba.applet.Applet;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Locale;
import javax.imageio.ImageIO;

import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;
import paintchat_client.Mi;
import syi.awt.*;
import syi.awt.cust.*;
import syi.util.ByteStream;

import static syi.C.ShiPainter.*;


public class ShiPainter extends Applet implements Runnable, ActionListener, WindowListener {
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

    private File lastFile;
    private String lastType;
    private String lastMode;

    public FullScreen fs;

    public void destroy() {
        try {
            if (this.p.getParent() != this) {
                ((Window) this.p.getParent().getParent()).dispose();
            }

            this.removeAll();
            this.p = null;
            this.ts = null;
            this.mbar = null;
        } catch (Throwable ex) {
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

    protected void jump(String url, String target) {
        try {
            this.getAppletContext().showDocument(new URL(this.getCodeBase(), url), target == null ? "_self" : target);
        } catch (Throwable ex) {
            Ts.alert(ex.getMessage());
        }

    }

    public void paint(Graphics g) {
        try {
            if (this.isStart >= 2) {
                return;
            }

            FontMetrics fontMetrics = g.getFontMetrics();
            g.drawString((String) "Wait for initialization to complete.", 10, fontMetrics.getHeight() + 10);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void pExit() {
        Ts.run(this, 's', 2);
    }

    protected void processEvent(AWTEvent event) {
        try {
            int eventID = event.getID();
            if (eventID == ComponentEvent.COMPONENT_RESIZED && this.ts != null) {
                this.ts.pack();
            }
        } catch (Throwable ex) {
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
            URL basePath = this.getCodeBase();
            String dirResource = this.p.p("dir_resource", "./res/");
            char lastChar = dirResource.charAt(dirResource.length() - 1);
            Object resPath;
            if (lastChar != '&' && lastChar != '?' && lastChar != '=') {
                if (lastChar != '/') {
                    dirResource = dirResource + '/';
                }

                resPath = new URL(basePath, dirResource);
            } else {
                resPath = dirResource;
            }

            this.config = new Res(this, resPath, (ByteStream) null);
            this.res = new Res(this, resPath, (ByteStream) null);

            try {
                String fileName = this.p.p("res.zip", "res/res.zip");
                if (fileName.equals("res_normal.zip")) {
                    fileName = "res.zip";
                }

                if (fileName.equals("res_pro.zip")) {
                    fileName = "res.zip";
                }

                this.config.loadZip(fileName);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            try {
                this.config.load(new String((byte[]) this.config.getRes("param_utf8.txt"), "UTF8"));
                this.config.remove("param_utf8.txt");
            } catch (Throwable ex) {
                Ts.alert(ex.getMessage());
            }

            //Res var11 = this.config;
            Color cTemp = new Color(config.getP("color_bk", 0xcfcfff));
            this.setBackground(cTemp);
            this.p.setBackground(cTemp);
            cTemp = new Color(config.getP("window_color_bk", cTemp.getRGB()));
            Awt.cC = cTemp;
            Awt.cBk = cTemp;
            cTemp = new Color(config.getP("color_text", 0x505078));
            this.setForeground(cTemp);
            this.p.setForeground(cTemp);
            Awt.cFore = new Color(config.getP("window_color_text", cTemp.getRGB()));
            Awt.cFSel = new Color(config.getP("color_iconselect", 0xee3333));
            Awt.cF = new Color(config.getP("window_color_frame", 0x000000));
            Awt.clBar = new Color(config.getP("window_color_bar", 0x6666ff));
            Awt.clLBar = new Color(config.getP("window_color_bar_hl", 0x8888ff));
            Awt.clBarT = new Color(config.getP("window_color_bar_text", 0xFFFFFF));

            try {
                Awt.setPFrame((Frame) Awt.getParent(this));
            } catch (RuntimeException var7) {
                ;
            }

            this.res.loadResource(this.config, "res", Locale.getDefault().getLanguage());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        this.enableEvents(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
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

        if(d_isDesktop()) {
            if (lastMode == null) lastMode = config.getP("tools", GUI_NORMAL);
            frame.setMenuBar(mbar);
            frame.setBackground(Awt.cC);
            frame.setForeground(Awt.cFore);
            frame.setVisible(true);
        }
    }

    // Implementation of Runnable
    public void run() {
        try {
            switch (Thread.currentThread().getName().charAt(0)) {
                case 'i': // init - set to "i" by Ts.run()
                    this.rInit();
                    if(p.p("fullscreen", false)) fs.fullToggle(true);
                    break;
                case 'j': // sends sj somewhere... but I don't think it's being used in ShiPainter
                    synchronized (this.sj) {
                        for (int i = 0; i < this.sj.length; ++i) {
                            String var2;
                            if ((var2 = this.sj[i]) != null) {
                                this.sj[i] = null;
                                this.mi.send(var2);
                            }
                        }

                        return;
                    }
                case 'p': // happens at start and at window resize - set by pack()
                    this.ts.pack();
                    break;
                case 's': // save
                    this.p.rSave();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
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
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void setColors(String var1) {
        if (this.p != null && this.p.tool != null) {
            this.p.tool.setC(var1);
        }

    }

    public void start() {
        if(d_isDesktop()) {
            frame = d_getFrame();
            fs = new FullScreen(frame);
            frame.addWindowListener(this);
        }
        try {
            if (this.isStart == 0) {
                Ts.run(this, 'i', 3);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    public void mPermission(String permString) {
        //WARN: duplicate
        String[] permArr = permString.split(";");
        for(String perm : permArr) p.mP(perm);
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

            NewImage ni = new NewImage(frame, dim, lastMode);
            dim = ni.getDim();
            mode = ni.getMode();
            if(dim != null) {
                isStart = 0;
                destroy();
                paint(getGraphics());
                rInit(dim.width, dim.height, null, mode);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        lastMode = mode;
        lastFile = null;
    }

    public void saveFile(boolean isSaveAs, String type) {
        File f;
        if(type == null) type = "png";

        if(isSaveAs || lastFile == null){
            String fn;

            fn = lastFile == null ? "untitled" : lastFile.getName();

            FileDlg sa = new FileDlg(frame, "Save "+type.toUpperCase()+" file", FileDialog.SAVE, fn);
            f =sa.getF();
            if(f == null) return;
            fn = f.getName();

            //include files starting with dot
            if(fn.lastIndexOf('.') <= 0) {
                //silly homage to MS Paint. File extension in allcaps.
                if        (type.equals("png")) {
                    fn += ".PNG";
                } else if (type.equals("jpg")) {
                    fn += ".JPG";
                } else if (type.equals("pch")) {
                    fn += ".SPCH";
                } else {
                    fn += ".PNG";
                }
                f = new File(f.getParent(), fn);
            } else {
                type = getType(f);
            }
        }else{
            f = lastFile;
        }


        try {
            byte[] bs = p.export(type);
            if(bs == null) return;

            FileOutputStream fos = new FileOutputStream(f);
            //BufferedOutputStream bos = new BufferedOutputStream(fos);
            fos.write(bs);
            fos.close();

        } catch (FileNotFoundException ex) {
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        lastFile = f;
        lastType = getType(f);
    }

    public void openFile() {
        FileDlg fileBr = new FileDlg(frame, "Open SPCH/Image", FileDialog.LOAD, "*.spch;*.jpg;*.png;*.gif;*.bmp");
        File f =fileBr.getF();
        if(f == null || !f.isFile()) return;

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

            lastType = getType(f);
            lastFile = f;
            rInit(imW, imH, path, lastMode);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void exit() {
        //TODO: ask to save modified document
        frame.dispose();
        System.exit(0);
    }

    private String getType(File f) {
        if (f == null) {
            return null;
        } else {
            String tfl = f.getName().toLowerCase();
            if (tfl.endsWith(".png")) {
                return "png";
            } else if (tfl.endsWith(".jpg") || tfl.endsWith(".jpeg")) {
                return "jpg";
            } else if (tfl.endsWith(".spch")) {
                return "pch";
            } else {
                return "png";
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        if        (com.equals(MBar.hNew)) {
            newImg();
        } else if (com.equals(MBar.hOpen)) {
            openFile();
        } else if (com.equals(MBar.hSave)) {
            saveFile(false, lastType);
        } else if (com.equals(MBar.hSvAsJPG)) {
            saveFile(true, "jpg");
        } else if (com.equals(MBar.hSvAsPNG)) {
            saveFile(true, "png");
        } else if (com.equals(MBar.hSvAsAni)) {
            saveFile(true, "pch");
        } else if (com.equals(MBar.hExit)) {
            exit();
        } else if (com.equals(MBar.hToggleScr)) {
            fs.fullToggle();
        }
    }

    //WindowListener
    public void windowClosing(WindowEvent e) {
        exit();
    }

    public void windowOpened(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }

}
