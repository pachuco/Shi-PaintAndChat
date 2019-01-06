package paintchat_frame;

import jaba.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import paintchat.Config;
import paintchat.Res;
import paintchat.Resource;
import paintchat.debug.Debug;
import syi.applet.AppletWatcher;
import syi.applet.ServerStub;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.awt.HelpWindow;
import syi.awt.HelpWindowContent;
import syi.awt.LButton;
import syi.awt.LTextField;
import syi.awt.MessageBox;
import syi.util.Io;
import syi.util.ThreadPool;

public class PFrame extends Frame implements ActionListener, ItemListener, MouseListener, WindowListener, Runnable {
    public static final String STR_VERSION = "(C)しぃちゃん PaintChatApp v3.66";
    private Config config;
    private Res res;
    private Debug debug = null;
    private Panel ivjPanel3 = null;
    private Panel ivjPanelLeft = null;
    private GridLayout ivjPanelLeftGridLayout = null;
    private Menu ivjMenu1 = null;
    private FlowLayout ivjPanel3FlowLayout = null;
    private Menu ivjMenu2 = null;
    private Menu ivjMenu3 = null;
    private MenuItem ivjMenuItem1 = null;
    private MenuItem ivjMenuItem2 = null;
    private MenuItem ivjMenuItem3 = null;
    private MenuItem ivjMenuItem4 = null;
    private BorderLayout ivjPFrameBorderLayout = null;
    private MenuBar ivjPFrameMenuBar = null;
    private HelpWindow ivjHelp = null;
    private Console ivjConsole = null;
    private MenuItem ivjMenuItem6 = null;
    private MenuItem ivjMenuHelpDocument = null;
    private CheckboxMenuItem ivjMenuShowConsole = null;
    private CheckboxMenuItem ivjMenuShowHelp = null;
    private Data ivjData = null;
    private LTextField ivjHttp_Port = null;
    private LTextField ivjIp = null;
    private LButton ivjLobby_Button = null;
    private LTextField ivjPaintchat_Port = null;
    private LButton ivjHttp_Button = null;
    private LButton ivjPaintchat_Button = null;
    private MenuItem ivjMenu_Help_Update = null;
    private MenuItem ivjMenuItem9 = null;
    private MenuItem ivjMenuItem10 = null;
    private MenuItem ivjMenu_FilesCopy = null;

    public PFrame() {
        this.initialize();
    }

    public PFrame(String var1) {
        super(var1);
    }

    public void actionPerformed(ActionEvent var1) {
        if (var1.getSource() == this.getMenuItem6()) {
            this.connEtoC2(var1);
        }

        if (var1.getSource() == this.getMenuItem4()) {
            this.connEtoC6(var1);
        }

        if (var1.getSource() == this.getMenuHelpDocument()) {
            this.connEtoC7(var1);
        }

        if (var1.getSource() == this.getMenuItem1()) {
            this.connEtoC11(var1);
        }

        if (var1.getSource() == this.getMenuItem2()) {
            this.connEtoC12(var1);
        }

        if (var1.getSource() == this.getMenuItem9()) {
            this.connEtoC16(var1);
        }

        if (var1.getSource() == this.getMenuItem10()) {
            this.connEtoC18(var1);
        }

        if (var1.getSource() == this.getMenuItem3()) {
            this.connEtoC9(var1);
        }

        if (var1.getSource() == this.getMenu_FilesCopy()) {
            this.connEtoC13(var1);
        }

        String var2;
        if (var1.getSource() == this.getPaintchat_Port()) {
            try {
                var2 = var1.getActionCommand();
                Integer.parseInt(var2);
            } catch (NumberFormatException var4) {
                var2 = "41411";
            }

            this.getPaintchat_Port().setText(var2);
            this.config.put("Connection_Port_PaintChat", var2);
            this.config.saveConfig((File) null, (String) null);
        }

        if (var1.getSource() == this.getHttp_Port()) {
            try {
                var2 = var1.getActionCommand();
                Integer.parseInt(var2);
            } catch (NumberFormatException var3) {
                var2 = "80";
            }

            this.getHttp_Port().setText(var2);
            this.config.put("Connection_Port_Http", var2);
            this.config.saveConfig((File) null, (String) null);
        }

    }

    private void connEtoC1(WindowEvent var1) {
        try {
            this.destroy();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC11(ActionEvent var1) {
        try {
            this.startHttp();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC12(ActionEvent var1) {
        try {
            this.startServer();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC13(ActionEvent var1) {
        try {
            this.setupWWWFolder();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC16(ActionEvent var1) {
        try {
            this.startClient();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC17(WindowEvent var1) {
        try {
            this.pFrame_WindowClosed(var1);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC18(ActionEvent var1) {
        try {
            this.menuItem10_ActionPerformed1();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    public void connEtoC18_NormalResult(boolean var1) {
        try {
            if (var1) {
                new ConfigDialog("paintchat.config.PConfig", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private void connEtoC2(ActionEvent var1) {
        try {
            this.menuItem6_ActionPerformed();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC3(MouseEvent var1) {
        try {
            this.iP_MouseClicked();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC4(ItemEvent var1) {
        try {
            this.showConsole();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC5(ItemEvent var1) {
        try {
            this.showHelp();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC6(ActionEvent var1) {
        try {
            this.menuItem4_ActionPerformed();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC7(ActionEvent var1) {
        try {
            this.menuHelpDocument_ActionPerformed();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC8(WindowEvent var1) {
        try {
            this.pFrame_WindowIconified(var1);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC9(ActionEvent var1) {
        try {
            this.menuItem3_ActionPerformed(var1);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoM1(MouseEvent var1) {
        try {
            this.getHelp().startHelp(new HelpWindowContent(var1.getComponent().getName(), true, Gui.getScreenPos(var1), this.res));
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoM2(MouseEvent var1) {
        try {
            this.getHelp().startHelp(new HelpWindowContent(var1.getComponent().getName(), true, Gui.getScreenPos(var1), this.res));
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoM3(MouseEvent var1) {
        try {
            this.getHelp().startHelp(new HelpWindowContent(var1.getComponent().getName(), true, Gui.getScreenPos(var1), this.res));
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoM4(MouseEvent var1) {
        try {
            this.getHelp().startHelp(new HelpWindowContent(var1.getComponent().getName(), true, Gui.getScreenPos(var1), this.res));
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoM5(MouseEvent var1) {
        try {
            this.getHelp().startHelp(new HelpWindowContent(var1.getComponent().getName(), true, Gui.getScreenPos(var1), this.res));
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoM6(MouseEvent var1) {
        try {
            this.getHelp().startHelp(new HelpWindowContent(var1.getComponent().getName(), true, Gui.getScreenPos(var1), this.res));
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void destroy() {
        Thread var1 = new Thread(this, "destroy");
        var1.start();
    }

    public Config getConfig() throws IOException {
        if (this.config == null) {
            this.config = new Config("cnf/paintchat.cf");
        }

        return this.config;
    }

    private Console getConsole() {
        if (this.ivjConsole == null) {
            try {
                this.ivjConsole = new Console();
                this.ivjConsole.setName("Console");
                this.ivjConsole.setBackground(Color.white);
                this.ivjConsole.setBounds(861, 328, 159, 60);
                this.ivjConsole.setForeground(Color.black);
                Applet var1 = new Applet();
                var1.setStub(ServerStub.getDefaultStub(this.getConfig(), this.getResource()));
                this.ivjConsole.init(var1, 400, this.ivjConsole.getBackground(), this.ivjConsole.getForeground(), (TextField) null);
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjConsole;
    }

    private Data getData() {
        if (this.ivjData == null) {
            try {
                this.ivjData = new Data();
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjData;
    }

    private HelpWindow getHelp() {
        if (this.ivjHelp == null) {
            try {
                this.ivjHelp = new HelpWindow(this);
                this.ivjHelp.setName("Help");
                this.ivjHelp.setBounds(275, 283, 76, 75);
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjHelp;
    }

    private LButton getHttp_Button() {
        if (this.ivjHttp_Button == null) {
            try {
                this.ivjHttp_Button = new LButton();
                this.ivjHttp_Button.setName("Http_Button");
                this.ivjHttp_Button.setForeground(new Color(80, 80, 120));
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjHttp_Button;
    }

    private LTextField getHttp_Port() {
        if (this.ivjHttp_Port == null) {
            try {
                this.ivjHttp_Port = new LTextField();
                this.ivjHttp_Port.setName("Http_Port");
                this.ivjHttp_Port.setText("80");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjHttp_Port;
    }

    private LTextField getIp() {
        if (this.ivjIp == null) {
            try {
                this.ivjIp = new LTextField();
                this.ivjIp.setName("Ip");
                this.ivjIp.setText("127.0.0.1");
                this.ivjIp.setEdit(false);
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjIp;
    }

    private LButton getLobby_Button() {
        if (this.ivjLobby_Button == null) {
            try {
                this.ivjLobby_Button = new LButton();
                this.ivjLobby_Button.setName("Lobby_Button");
                this.ivjLobby_Button.setForeground(new Color(80, 80, 120));
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLobby_Button;
    }

    private MenuItem getMenu_FilesCopy() {
        if (this.ivjMenu_FilesCopy == null) {
            try {
                this.ivjMenu_FilesCopy = new MenuItem();
                this.ivjMenu_FilesCopy.setLabel("Menu_FilesCopy");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenu_FilesCopy;
    }

    private MenuItem getMenu_Help_Update() {
        if (this.ivjMenu_Help_Update == null) {
            try {
                this.ivjMenu_Help_Update = new MenuItem();
                this.ivjMenu_Help_Update.setLabel("Menu_Help_Update");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenu_Help_Update;
    }

    private Menu getMenu1() {
        if (this.ivjMenu1 == null) {
            try {
                this.ivjMenu1 = new Menu();
                this.ivjMenu1.setFont(new Font("dialog", 0, 14));
                this.ivjMenu1.setActionCommand("Menu.Server");
                this.ivjMenu1.setLabel("Menu_Action");
                this.ivjMenu1.add(this.getMenuItem1());
                this.ivjMenu1.add(this.getMenuItem2());
                this.ivjMenu1.add(this.getMenuItem9());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenu1;
    }

    private Menu getMenu2() {
        if (this.ivjMenu2 == null) {
            try {
                this.ivjMenu2 = new Menu();
                this.ivjMenu2.setFont(new Font("dialog", 0, 14));
                this.ivjMenu2.setActionCommand("Menu.Option");
                this.ivjMenu2.setLabel("Menu_Option");
                this.ivjMenu2.add(this.getMenuItem3());
                this.ivjMenu2.add(this.getMenuItem10());
                this.ivjMenu2.add(this.getMenuItem4());
                this.ivjMenu2.add(this.getMenu_FilesCopy());
                this.ivjMenu2.add((MenuItem) this.getMenuShowConsole());
                this.ivjMenu2.add((MenuItem) this.getMenuShowHelp());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenu2;
    }

    private Menu getMenu3() {
        if (this.ivjMenu3 == null) {
            try {
                this.ivjMenu3 = new Menu();
                this.ivjMenu3.setFont(new Font("dialog", 0, 14));
                this.ivjMenu3.setActionCommand("Menu3");
                this.ivjMenu3.setLabel("Menu_Help");
                this.ivjMenu3.add(this.getMenu_Help_Update());
                this.ivjMenu3.add(this.getMenuHelpDocument());
                this.ivjMenu3.add(this.getMenuItem6());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenu3;
    }

    private MenuItem getMenuHelpDocument() {
        if (this.ivjMenuHelpDocument == null) {
            try {
                this.ivjMenuHelpDocument = new MenuItem();
                this.ivjMenuHelpDocument.setLabel("Menu_Help_Document");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuHelpDocument;
    }

    private MenuItem getMenuItem1() {
        if (this.ivjMenuItem1 == null) {
            try {
                this.ivjMenuItem1 = new MenuItem();
                this.ivjMenuItem1.setActionCommand("Menu.Server.HTTP");
                this.ivjMenuItem1.setLabel("Menu_Action_HTTP");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuItem1;
    }

    private MenuItem getMenuItem10() {
        if (this.ivjMenuItem10 == null) {
            try {
                this.ivjMenuItem10 = new MenuItem();
                this.ivjMenuItem10.setLabel("Menu_Option_Server");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuItem10;
    }

    private MenuItem getMenuItem2() {
        if (this.ivjMenuItem2 == null) {
            try {
                this.ivjMenuItem2 = new MenuItem();
                this.ivjMenuItem2.setActionCommand("Menu.Server.PaintChat");
                this.ivjMenuItem2.setLabel("Menu_Action_PaintChat");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuItem2;
    }

    private MenuItem getMenuItem3() {
        if (this.ivjMenuItem3 == null) {
            try {
                this.ivjMenuItem3 = new MenuItem();
                this.ivjMenuItem3.setActionCommand("Menu.Option.Config");
                this.ivjMenuItem3.setLabel("Menu_Option_Config");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuItem3;
    }

    private MenuItem getMenuItem4() {
        if (this.ivjMenuItem4 == null) {
            try {
                this.ivjMenuItem4 = new MenuItem();
                this.ivjMenuItem4.setActionCommand("Menu.Option.Lobby");
                this.ivjMenuItem4.setLabel("Menu_Option_Lobby");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuItem4;
    }

    private MenuItem getMenuItem6() {
        if (this.ivjMenuItem6 == null) {
            try {
                this.ivjMenuItem6 = new MenuItem();
                this.ivjMenuItem6.setLabel("Menu_Help_About");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuItem6;
    }

    private MenuItem getMenuItem9() {
        if (this.ivjMenuItem9 == null) {
            try {
                this.ivjMenuItem9 = new MenuItem();
                this.ivjMenuItem9.setLabel("Menu_Action_Client");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuItem9;
    }

    private CheckboxMenuItem getMenuShowConsole() {
        if (this.ivjMenuShowConsole == null) {
            try {
                this.ivjMenuShowConsole = new CheckboxMenuItem();
                this.ivjMenuShowConsole.setLabel("Menu_Option_ShowConsole");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuShowConsole;
    }

    private CheckboxMenuItem getMenuShowHelp() {
        if (this.ivjMenuShowHelp == null) {
            try {
                this.ivjMenuShowHelp = new CheckboxMenuItem();
                this.ivjMenuShowHelp.setLabel("Menu_Option_ShowHelp");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjMenuShowHelp;
    }

    private LButton getPaintchat_Button() {
        if (this.ivjPaintchat_Button == null) {
            try {
                this.ivjPaintchat_Button = new LButton();
                this.ivjPaintchat_Button.setName("Paintchat_Button");
                this.ivjPaintchat_Button.setForeground(new Color(80, 80, 120));
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjPaintchat_Button;
    }

    private LTextField getPaintchat_Port() {
        if (this.ivjPaintchat_Port == null) {
            try {
                this.ivjPaintchat_Port = new LTextField();
                this.ivjPaintchat_Port.setName("Paintchat_Port");
                this.ivjPaintchat_Port.setText("0");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjPaintchat_Port;
    }

    private Panel getPanel3() {
        if (this.ivjPanel3 == null) {
            try {
                this.ivjPanel3 = new Panel();
                this.ivjPanel3.setName("Panel3");
                this.ivjPanel3.setLayout(this.getPanel3FlowLayout());
                this.ivjPanel3.setBackground(new Color(204, 204, 204));
                this.ivjPanel3.setForeground(new Color(80, 80, 120));
                this.getPanel3().add((Component) this.getPanelLeft(), (Object) this.getPanelLeft().getName());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjPanel3;
    }

    private FlowLayout getPanel3FlowLayout() {
        FlowLayout var1 = null;

        try {
            var1 = new FlowLayout();
            var1.setAlignment(1);
            var1.setVgap(5);
            var1.setHgap(5);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

        return var1;
    }

    private Panel getPanelLeft() {
        if (this.ivjPanelLeft == null) {
            try {
                this.ivjPanelLeft = new Panel();
                this.ivjPanelLeft.setName("PanelLeft");
                this.ivjPanelLeft.setLayout(this.getPanelLeftGridLayout());
                this.ivjPanelLeft.setBackground(new Color(204, 204, 204));
                this.ivjPanelLeft.setForeground(new Color(80, 80, 120));
                this.getPanelLeft().add((Component) this.getIp(), (Object) this.getIp().getName());
                this.getPanelLeft().add((Component) this.getHttp_Port(), (Object) this.getHttp_Port().getName());
                this.getPanelLeft().add((Component) this.getPaintchat_Port(), (Object) this.getPaintchat_Port().getName());
                this.getPanelLeft().add((Component) this.getPaintchat_Button(), (Object) this.getPaintchat_Button().getName());
                this.getPanelLeft().add((Component) this.getHttp_Button(), (Object) this.getHttp_Button().getName());
                this.getPanelLeft().add((Component) this.getLobby_Button(), (Object) this.getLobby_Button().getName());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjPanelLeft;
    }

    private GridLayout getPanelLeftGridLayout() {
        GridLayout var1 = null;

        try {
            var1 = new GridLayout(0, 1);
            var1.setVgap(3);
            var1.setHgap(0);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

        return var1;
    }

    private BorderLayout getPFrameBorderLayout() {
        BorderLayout var1 = null;

        try {
            var1 = new BorderLayout();
            var1.setVgap(0);
            var1.setHgap(0);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

        return var1;
    }

    private MenuBar getPFrameMenuBar() {
        if (this.ivjPFrameMenuBar == null) {
            try {
                this.ivjPFrameMenuBar = new MenuBar();
                this.ivjPFrameMenuBar.setHelpMenu(this.getMenu3());
                this.ivjPFrameMenuBar.add(this.getMenu2());
                this.ivjPFrameMenuBar.add(this.getMenu1());
                this.ivjPFrameMenuBar.add(this.getMenu3());
                MenuBar var1 = this.ivjPFrameMenuBar;
                int var2 = var1.getMenuCount();

                for (int var3 = 0; var3 < var2; ++var3) {
                    this.setResource(var1.getMenu(var3));
                }
            } catch (Throwable var4) {
                this.handleException(var4);
            }
        }

        return this.ivjPFrameMenuBar;
    }

    private Res getResource() {
        if (this.res == null) {
            Resource.loadResource();
            this.res = Resource.loadResource("Application");
        }

        return this.res;
    }

    private void handleException(Throwable var1) {
    }

    public void init() {
        Thread var1 = new Thread(this, "init");
        var1.setPriority(1);
        var1.setDaemon(false);
        var1.start();
    }

    private void initConnections() throws Exception {
        this.getPaintchat_Port().addActionListener(this);
        this.getHttp_Port().addActionListener(this);
        this.getIp().addMouseListener(this);
        this.getHttp_Port().addMouseListener(this);
        this.getPaintchat_Port().addMouseListener(this);
        this.getPaintchat_Button().addMouseListener(this);
        this.getHttp_Button().addMouseListener(this);
        this.getLobby_Button().addMouseListener(this);
        this.getMenuItem6().addActionListener(this);
        this.addWindowListener(this);
        this.getMenuShowConsole().addItemListener(this);
        this.getMenuShowHelp().addItemListener(this);
        this.getMenuItem4().addActionListener(this);
        this.getMenuHelpDocument().addActionListener(this);
        this.getMenu_Help_Update().addActionListener(this);
        this.getMenuItem1().addActionListener(this);
        this.getMenuItem2().addActionListener(this);
        this.getMenuItem9().addActionListener(this);
        this.getMenuItem10().addActionListener(this);
        this.getMenuItem3().addActionListener(this);
        this.getMenu_FilesCopy().addActionListener(this);
    }

    private void initialize() {
        try {
            this.setName("PFrame");
            this.setLayout(this.getPFrameBorderLayout());
            this.setBackground(new Color(204, 204, 204));
            this.setForeground(new Color(80, 80, 120));
            this.setMenuBar(this.getPFrameMenuBar());
            this.setBounds(new Rectangle(0, 0, 391, 172));
            this.setSize(391, 172);
            this.setTitle("PaintChat");
            this.add((Component) this.getPanel3(), (Object) "West");
            this.initConnections();
        } catch (Throwable var2) {
            this.handleException(var2);
        }

    }

    public void iP_MouseClicked() {
        PopupMenuPaintChat var1 = new PopupMenuPaintChat(this.debug, this.config, this.res);
        var1.show(this, this.ivjIp, 0, 0);
    }

    public void itemStateChanged(ItemEvent var1) {
        if (var1.getSource() == this.getMenuShowConsole()) {
            this.connEtoC4(var1);
        }

        if (var1.getSource() == this.getMenuShowHelp()) {
            this.connEtoC5(var1);
        }

    }

    public static void main(String[] var0) {
        try {
            PFrame var1 = new PFrame();
            var1.init();
        } catch (Throwable var2) {
            var2.printStackTrace(System.out);
            System.exit(0);
        }

    }

    public void menuHelpDocument_ActionPerformed() {
        Gui.showDocument("Help.html", this.config, this.res);
    }

    public void menuItem1_ActionPerformed1() {
        ThreadPool.poolStartThread(this.getData(), 'h');
    }

    public void menuItem10_ActionPerformed(ActionEvent var1) {
        try {
            new ConfigDialog("paintchat.config.ConfigServer", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void menuItem10_ActionPerformed1() {
        if (this.getData().isRunPaintChatServer() && MessageBox.confirm("ConfirmMayServerStopNow", "(C)しぃちゃん PaintChatApp v3.66")) {
            this.getData().startPaintChat(false);
        }

        try {
            new ConfigDialog("paintchat.config.ConfigServer", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
        } catch (Throwable var1) {
        }

    }

    public void menuItem3_ActionPerformed(ActionEvent var1) {
        try {
            new ConfigDialog("paintchat.config.PConfig", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
        } catch (Throwable var3) {
            this.debug.log(var3.getMessage());
        }

    }

    public void menuItem4_ActionPerformed() {
        try {
            new ConfigDialog("paintchat.config.Ao", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
        } catch (Throwable var2) {
            this.debug.log(var2.getMessage());
        }

    }

    public void menuItem6_ActionPerformed() {
        StringBuffer var1 = new StringBuffer();
        var1.append("(C)しぃちゃん PaintChatApp v3.66");
        var1.append('\n');
        var1.append('\n');
        var1.append("JavaVirtualMachine(JVM):");
        var1.append(System.getProperty("java.vendor"));
        var1.append("\nJVM Version:");
        var1.append(System.getProperty("java.version"));
        var1.append('\n');
        var1.append("\n立ち上げ画面のCGはuzukiさんが作成しました。\nuzukiさんのHP http://www19.freeweb.ne.jp/play/m_uzuki/top.htm\n効果音はあややさんが作成しました。\nロビープログラムは藍珠さんが作成、管理しています。");
        MessageBox.alert(var1.toString(), "(C)しぃちゃん PaintChatApp v3.66");
    }

    public void mouseClicked(MouseEvent var1) {
        if (var1.getSource() == this.getIp()) {
            this.connEtoC3(var1);
        }

    }

    public void mouseEntered(MouseEvent var1) {
        if (var1.getSource() == this.getIp()) {
            this.connEtoM1(var1);
        }

        if (var1.getSource() == this.getHttp_Port()) {
            this.connEtoM2(var1);
        }

        if (var1.getSource() == this.getPaintchat_Port()) {
            this.connEtoM3(var1);
        }

        if (var1.getSource() == this.getPaintchat_Button()) {
            this.connEtoM4(var1);
        }

        if (var1.getSource() == this.getHttp_Button()) {
            this.connEtoM5(var1);
        }

        if (var1.getSource() == this.getLobby_Button()) {
            this.connEtoM6(var1);
        }

    }

    public void mouseExited(MouseEvent var1) {
        this.getHelp().reset();
    }

    public void mousePressed(MouseEvent var1) {
    }

    public void mouseReleased(MouseEvent var1) {
    }

    public void panel3_MouseReleased(MouseEvent var1) {
        MenuBar var2 = this.getMenuBar();
        if (!this.ivjMenuShowConsole.getState() && var2 == null) {
            var2 = this.getPFrameMenuBar();
            var2.remove(0);
            var2.remove(0);
            var2.remove(0);
            PopupMenu var3 = new PopupMenu();
            var3.addActionListener(this);
            var3.add((MenuItem) this.getMenu1());
            var3.add((MenuItem) this.getMenu2());
            var3.add((MenuItem) this.getMenu3());
            this.add(var3);
            var3.show(var1.getComponent(), var1.getX(), var1.getY());
            var3.removeAll();
            var2.add(this.getMenu1());
            var2.add(this.getMenu2());
            var2.add(this.getMenu3());
        }
    }

    public void pFrame_WindowClosed(WindowEvent var1) {
        System.exit(0);
    }

    public void pFrame_WindowIconified(WindowEvent var1) {
        if (this.getData().getIsNativeWindows()) {
            this.setVisible(false);
        }

    }

    private void rDestroy() {
        try {
            this.config.put("Connection_Port_Http", this.getHttp_Port().getText().trim());
            this.config.put("Connection_Port_PaintChat", this.getPaintchat_Port().getText().trim());
            this.config.saveConfig((File) null, (String) null);
            if (this.getData().getIsNativeWindows()) {
                this.getData().exitWin();
            }

            this.dispose();
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    private void rInit() {
        try {
            System.currentTimeMillis();
            this.getResource();
            this.setResource(this.res, this);
            Awt.setPFrame(this);
            Awt.cBk = this.getBackground();
            Awt.cFore = this.getForeground();
            this.getConfig();
            this.getHttp_Port().setText(this.config.getString("Connection_Port_Http", "80"));
            this.getPaintchat_Port().setText(this.config.getString("Connection_Port_PaintChat", "0"));
            Image var1 = Io.loadImageNow(this, "cnf/template/top.jpg");
            Dimension var2 = this.getToolkit().getScreenSize();
            Point var3 = new Point((var2.width - var1.getWidth((ImageObserver) null)) / 2, (var2.height - var1.getHeight((ImageObserver) null)) / 2);
            this.getHelp().setForeground(Color.black);
            HelpWindowContent var4 = new HelpWindowContent(var1, "(C)しぃちゃん PaintChatApp v3.66", false, var3, (Hashtable) null);
            var4.timeStart = 0;
            var4.timeEnd = 3500;
            var4.setVisit(true);
            this.getHelp().startHelp(var4);
            MessageBox.setResource(this.res);
            this.debug = new Debug(this.res);
            this.setIconImage(Io.loadImageNow(this, "cnf/icon.gif"));
            this.setTitle("PaintChat_MainWindow");
            if (this.config.getBool("App_IsConsole")) {
                this.showConsole();
                this.ivjConsole.addText("(C)しぃちゃん PaintChatApp v3.66");
                this.ivjConsole.addText("http://shichan.jp/");
                this.ivjConsole.addText(" ");
                if (this.config.getBool("App_ShowStartHelp", true)) {
                    this.ivjConsole.setRText(this.res.get("StartHelp"));
                    this.ivjConsole.addText(" ");
                }
            }

            this.res.remove("StartHelp");
            if (this.config.getBool("App_ShowHelp", true)) {
                this.showHelp();
            }

            if (!this.config.getString("App_Version").equals("(C)しぃちゃん PaintChatApp v3.66")) {
                (new FileManager(this.config)).templateToWWW();
                this.config.put("App_Version", "(C)しぃちゃん PaintChatApp v3.66");
                this.debug.log("Client update.");
            }

            this.getIp().setText(PopupMenuPaintChat.getAddress(this.config, this.debug));
            this.getData().init(this.config, this.res, this.debug, this.getPaintchat_Button(), this.getHttp_Button(), this.getLobby_Button());
            this.pack();
            Awt.moveCenter(this);
            this.setVisible(true);
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }

    public void run() {
        try {
            switch (Thread.currentThread().getName().charAt(0)) {
                case 'd':
                    this.rDestroy();
                    break;
                case 'i':
                    this.rInit();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void setResource(MenuItem var1) {
        var1.setLabel(this.getResource().get(var1.getLabel(), var1.getLabel()));
        if (var1 instanceof Menu) {
            Menu var2 = (Menu) var1;
            int var3 = var2.getItemCount();

            for (int var4 = 0; var4 < var3; ++var4) {
                this.setResource(var2.getItem(var4));
            }
        }

    }

    public void setResource(Res var1, Component var2) {
        if (var2 instanceof Container) {
            Container var3 = (Container) var2;
            int var4 = var3.getComponentCount();

            for (int var5 = 0; var5 < var4; ++var5) {
                this.setResource(var1, var3.getComponent(var5));
            }
        } else {
            String var6 = var1.get(var2.getName(), var2.getName());
            if (var2 instanceof LTextField) {
                LTextField var9 = (LTextField) var2;
                var9.setTitle(var6);
                return;
            }

            if (var2 instanceof LButton) {
                LButton var8 = (LButton) var2;
                var8.setText(var6);
                return;
            }

            if (var2 instanceof Checkbox) {
                Checkbox var7 = (Checkbox) var2;
                var7.setLabel(var6);
                return;
            }
        }

    }

    public void setupWWWFolder() {
        (new FileManager(this.config)).templateToWWW();
    }

    public void showConsole() {
        Console var1 = this.getConsole();
        boolean var2 = var1.getParent() == this;
        MenuBar var3;
        if (var2) {
            this.remove(var1);
            var1.stop();
            var3 = this.getMenuBar();
            var3.remove(this.getMenu1());
            var3.remove(this.getMenu3());
        } else {
            var1.start(this.debug);
            this.add((Component) var1, (Object) "Center");
            var3 = this.getPFrameMenuBar();
            var3.add(this.getMenu1());
            var3.add(this.getMenu3());
        }

        this.config.put("App_IsConsole", String.valueOf(!var2));
        this.ivjMenuShowConsole.setState(!var2);
        this.pack();
    }

    public void showHelp() {
        boolean var1 = this.getHelp().getIsShow();
        this.getHelp().setIsShow(!var1);
        this.getMenuShowHelp().setState(!var1);
        this.config.put("App_ShowHelp", String.valueOf(!var1));
    }

    public void startClient() {
        try {
            AppletWatcher var1 = new AppletWatcher("paintchat_client.Client", "(C)しぃちゃん PaintChatApp v3.66", this.config, this.res, false);
            var1.setIconImage(this.getIconImage());
            var1.show();
        } catch (Throwable var2) {
            this.debug.log(var2.getMessage());
        }

    }

    public void startHttp() {
        this.getData().startHttp(true);
    }

    public void startServer() {
        this.getData().startPaintChat(true);
    }

    public void startViewer() {
    }

    public void windowActivated(WindowEvent var1) {
    }

    public void windowClosed(WindowEvent var1) {
        if (var1.getSource() == this) {
            this.connEtoC17(var1);
        }

    }

    public void windowClosing(WindowEvent var1) {
        if (var1.getSource() == this) {
            this.connEtoC1(var1);
        }

    }

    public void windowDeactivated(WindowEvent var1) {
    }

    public void windowDeiconified(WindowEvent var1) {
        this.setVisible(true);
    }

    public void windowIconified(WindowEvent var1) {
        if (var1.getSource() == this) {
            this.connEtoC8(var1);
        }

    }

    public void windowOpened(WindowEvent var1) {
    }
}
