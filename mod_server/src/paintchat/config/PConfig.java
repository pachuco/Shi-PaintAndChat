package paintchat.config;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.File;

import paintchat.Config;
import syi.applet.ServerStub;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.awt.LTextField;

public class PConfig extends ConfigApplet implements ActionListener {
    private Panel ivjPanel1 = null;
    private GridLayout ivjPanel1GridLayout = null;
    private LTextField ivjAdmin_Password = null;
    private Checkbox ivjApp_Auto_Http = null;
    private Checkbox ivjApp_Auto_Lobby = null;
    private Checkbox ivjApp_Auto_Paintchat = null;
    private LTextField ivjApp_BrowserPath = null;
    private LTextField ivjApp_Cgi = null;
    private LButton ivjCancel = null;
    private Checkbox ivjConnection_GrobalAddress = null;
    private LButton ivjOk = null;
    private Panel ivjPanel2 = null;
    private Panel ivjPanel3 = null;
    private GridLayout ivjPanel2GridLayout = null;
    private Checkbox ivjApp_ShowStartHelp = null;
    private Button ivjButton1 = null;
    private Checkbox ivjAdmin_ChatMaster = null;
    private Checkbox ivjApp_Get_Index = null;

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.getButton1()) {
            this.connEtoM1(event);
        }

        if (event.getSource() == this.getOk()) {
            this.m_save();
            this.m_dispose();
        }

        if (event.getSource() == this.getCancel()) {
            this.m_dispose();
        }

    }

    public void addsMouseListener(Container var1) {
        Component[] var2 = var1.getComponents();
        if (var2 != null) {
            for (int var3 = 0; var3 < var2.length; ++var3) {
                if (var2[var3] instanceof Container) {
                    this.addsMouseListener((Container) var2[var3]);
                } else {
                    var2[var3].addMouseListener(this);
                }
            }
        }

        var1.addMouseListener(this);
    }

    public void browse(TextField var1, boolean var2) {
        String var3 = var1.getText();
        File var4 = Gui.fileDialog(Awt.getPFrame(), var3, var2);
        var1.setText(var4.getAbsolutePath());
    }

    private void connEtoM1(ActionEvent event) {
        try {
            this.stop();
        } catch (Throwable ex) {
            this.handleException(ex);
        }

    }

    private Checkbox getAdmin_ChatMaster() {
        if (this.ivjAdmin_ChatMaster == null) {
            try {
                this.ivjAdmin_ChatMaster = new Checkbox();
                this.ivjAdmin_ChatMaster.setName("Admin_ChatMaster");
                this.ivjAdmin_ChatMaster.setLabel("Admin_ChatMaster");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjAdmin_ChatMaster;
    }

    private LTextField getAdmin_Password() {
        if (this.ivjAdmin_Password == null) {
            try {
                this.ivjAdmin_Password = new LTextField();
                this.ivjAdmin_Password.setName("Admin_Password");
                this.ivjAdmin_Password.setTitle("Admin_Password");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjAdmin_Password;
    }

    private Checkbox getApp_Auto_Http() {
        if (this.ivjApp_Auto_Http == null) {
            try {
                this.ivjApp_Auto_Http = new Checkbox();
                this.ivjApp_Auto_Http.setName("App_Auto_Http");
                this.ivjApp_Auto_Http.setLabel("App_Auto_Http");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjApp_Auto_Http;
    }

    private Checkbox getApp_Auto_Lobby() {
        if (this.ivjApp_Auto_Lobby == null) {
            try {
                this.ivjApp_Auto_Lobby = new Checkbox();
                this.ivjApp_Auto_Lobby.setName("App_Auto_Lobby");
                this.ivjApp_Auto_Lobby.setLabel("App_Auto_Lobby");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjApp_Auto_Lobby;
    }

    private Checkbox getApp_Auto_Paintchat() {
        if (this.ivjApp_Auto_Paintchat == null) {
            try {
                this.ivjApp_Auto_Paintchat = new Checkbox();
                this.ivjApp_Auto_Paintchat.setName("App_Auto_Paintchat");
                this.ivjApp_Auto_Paintchat.setLabel("App_Auto_Paintchat");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjApp_Auto_Paintchat;
    }

    private LTextField getApp_BrowserPath() {
        if (this.ivjApp_BrowserPath == null) {
            try {
                this.ivjApp_BrowserPath = new LTextField();
                this.ivjApp_BrowserPath.setName("App_BrowserPath");
                this.ivjApp_BrowserPath.setTitle("App_BrowserPath");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjApp_BrowserPath;
    }

    private LTextField getApp_Cgi() {
        if (this.ivjApp_Cgi == null) {
            try {
                this.ivjApp_Cgi = new LTextField();
                this.ivjApp_Cgi.setName("App_Cgi");
                this.ivjApp_Cgi.setTitle("App_Cgi");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjApp_Cgi;
    }

    private Checkbox getApp_Get_Index() {
        if (this.ivjApp_Get_Index == null) {
            try {
                this.ivjApp_Get_Index = new Checkbox();
                this.ivjApp_Get_Index.setName("App_Get_Index");
                this.ivjApp_Get_Index.setLabel("App_Get_Index");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjApp_Get_Index;
    }

    private Checkbox getApp_ShowStartHelp() {
        if (this.ivjApp_ShowStartHelp == null) {
            try {
                this.ivjApp_ShowStartHelp = new Checkbox();
                this.ivjApp_ShowStartHelp.setName("App_ShowStartHelp");
                this.ivjApp_ShowStartHelp.setLabel("App_ShowStartHelp");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjApp_ShowStartHelp;
    }

    public String getAppletInfo() {
        return "paintchat.config.Ao は VisualAge for Java を使用して作成されました。";
    }

    private Button getButton1() {
        if (this.ivjButton1 == null) {
            try {
                this.ivjButton1 = new Button();
                this.ivjButton1.setName("Button1");
                this.ivjButton1.setBounds(135, 339, 56, 20);
                this.ivjButton1.setLabel("Button1");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjButton1;
    }

    private LButton getCancel() {
        if (this.ivjCancel == null) {
            try {
                this.ivjCancel = new LButton();
                this.ivjCancel.setName("Cancel");
                this.ivjCancel.setText("Cancel");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjCancel;
    }

    private Checkbox getConnection_GrobalAddress() {
        if (this.ivjConnection_GrobalAddress == null) {
            try {
                this.ivjConnection_GrobalAddress = new Checkbox();
                this.ivjConnection_GrobalAddress.setName("Connection_GrobalAddress");
                this.ivjConnection_GrobalAddress.setLabel("Connection_GrobalAddress");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjConnection_GrobalAddress;
    }

    private LButton getOk() {
        if (this.ivjOk == null) {
            try {
                this.ivjOk = new LButton();
                this.ivjOk.setName("Ok");
                this.ivjOk.setText("Ok");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjOk;
    }

    private Panel getPanel1() {
        if (this.ivjPanel1 == null) {
            try {
                this.ivjPanel1 = new Panel();
                this.ivjPanel1.setName("Panel1");
                this.ivjPanel1.setLayout(this.getPanel1GridLayout());
                this.getPanel1().add((Component) this.getApp_Auto_Lobby(), (Object) this.getApp_Auto_Lobby().getName());
                this.getPanel1().add((Component) this.getApp_Auto_Paintchat(), (Object) this.getApp_Auto_Paintchat().getName());
                this.getPanel1().add((Component) this.getApp_Auto_Http(), (Object) this.getApp_Auto_Http().getName());
                this.getPanel1().add((Component) this.getConnection_GrobalAddress(), (Object) this.getConnection_GrobalAddress().getName());
                this.getPanel1().add((Component) this.getApp_ShowStartHelp(), (Object) this.getApp_ShowStartHelp().getName());
                this.getPanel1().add((Component) this.getAdmin_ChatMaster(), (Object) this.getAdmin_ChatMaster().getName());
                this.getPanel1().add((Component) this.getApp_Get_Index(), (Object) this.getApp_Get_Index().getName());
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjPanel1;
    }

    private GridLayout getPanel1GridLayout() {
        GridLayout var1 = null;

        try {
            var1 = new GridLayout(0, 2);
        } catch (Throwable ex) {
            this.handleException(ex);
        }

        return var1;
    }

    private Panel getPanel2() {
        if (this.ivjPanel2 == null) {
            try {
                this.ivjPanel2 = new Panel();
                this.ivjPanel2.setName("Panel2");
                this.ivjPanel2.setLayout(this.getPanel2GridLayout());
                this.getPanel2().add((Component) this.getAdmin_Password(), (Object) this.getAdmin_Password().getName());
                this.getPanel2().add((Component) this.getApp_Cgi(), (Object) this.getApp_Cgi().getName());
                this.getPanel2().add((Component) this.getApp_BrowserPath(), (Object) this.getApp_BrowserPath().getName());
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjPanel2;
    }

    private GridLayout getPanel2GridLayout() {
        GridLayout var1 = null;

        try {
            var1 = new GridLayout(0, 1);
        } catch (Throwable ex) {
            this.handleException(ex);
        }

        return var1;
    }

    private Panel getPanel3() {
        if (this.ivjPanel3 == null) {
            try {
                this.ivjPanel3 = new Panel();
                this.ivjPanel3.setName("Panel3");
                this.ivjPanel3.setLayout(new FlowLayout());
                this.getPanel3().add((Component) this.getOk(), (Object) this.getOk().getName());
                this.getPanel3().add((Component) this.getCancel(), (Object) this.getCancel().getName());
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjPanel3;
    }

    private void handleException(Throwable ex) {
    }

    public void init() {
        try {
            this.setName("Ao");
            this.setLayout(new BorderLayout());
            this.setBackground(new Color(204, 204, 204));
            this.setSize(446, 266);
            this.add((Component) this.getPanel1(), (Object) "North");
            this.add((Component) this.getPanel2(), (Object) "Center");
            this.add((Component) this.getPanel3(), (Object) "South");
            this.initConnections();
            Gui.giveDef(this);
            this.initValue();
        } catch (Throwable ex) {
            this.handleException(ex);
        }

    }

    private void initConnections() throws Exception {
        this.getOk().addActionListener(this);
        this.getCancel().addActionListener(this);
        this.setMouseListener(this, this);
        this.getButton1().addActionListener(this);
    }

    public void initValue() {
        this.getResource(super.res, this);
        this.getParameter(this);
        Gui.giveDef(this);
    }

    public void m_dispose() {
        try {
            this.getHelp().reset();
            ((Window) this.getParent()).dispose();
        } catch (RuntimeException var2) {
            var2.printStackTrace();
        }

    }

    public void m_save() {
        try {
            this.setParameter(this);
            Config var1 = (Config) ((ServerStub) this.getAppletContext()).getHashTable();
            var1.saveConfig((File) null, (String) null);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] var0) {
        try {
            Frame var1 = new Frame();
            Class var3 = Class.forName("paintchat.config.PConfig");
            ClassLoader var4 = var3.getClassLoader();
            PConfig var2 = (PConfig) Beans.instantiate(var4, "paintchat.config.PConfig");
            var1.add((String) "Center", (Component) var2);
            var1.setSize(var2.getSize());
            var1.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent event) {
                    System.exit(0);
                }
            });
            var1.setVisible(true);
        } catch (Throwable ex) {
            System.err.println("java.applet.Applet の main() で例外が発生しました");
            ex.printStackTrace(System.out);
        }

    }

    public void reset() {
        this.getHelp().reset();
    }
}
