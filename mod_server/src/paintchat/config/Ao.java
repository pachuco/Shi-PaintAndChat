package paintchat.config;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.FileOutputStream;
import java.util.Random;

import paintchat.Config;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.util.PProperties;

public class Ao extends ConfigApplet implements ActionListener {
    private String CF_AO_CHATINDEX = "chatIndex";
    private String CF_AO_SHOW_HTML = "ao_show_html";
    private Checkbox ivjao_show_html = null;
    private Button ivjbu_ok = null;
    private Button ivjButton2 = null;
    private GridLayout ivjAo2GridLayout = null;
    private Panel ivjPanel3 = null;
    private LButton ivjCancel = null;
    private LButton ivjOk = null;
    private Panel ivjPanelConfig = null;
    private Panel ivjPanelContent = null;
    private TextField ivjadministratorName = null;
    private TextField ivjchatName = null;
    private TextField ivjchatUrl = null;
    private TextField ivjcommentString = null;
    private TextField ivjhomepageName = null;
    private TextField ivjhpUrl = null;
    private TextField ivjinformtionServerAddress = null;
    private Label ivjLabel1 = null;
    private Label ivjLabel2 = null;
    private Label ivjLabel3 = null;
    private Label ivjLabel4 = null;
    private Label ivjLabel5 = null;
    private Label ivjLabel6 = null;
    private Label ivjLabel7 = null;
    private Panel ivjleftPanel = null;
    private GridLayout ivjleftPanelGridLayout = null;
    private Panel ivjpanelRight = null;
    private GridLayout ivjpanelRightGridLayout = null;
    private Checkbox ivjApp_Auto_Lobby = null;
    private Panel ivjpanelBottom = null;
    private Label ivjlobby_setup = null;
    private FlowLayout ivjpanelBottomFlowLayout = null;

    public void actionPerformed(ActionEvent var1) {
        if (var1.getSource() == this.getButton2()) {
            this.connEtoC1(var1);
        }

        if (var1.getSource() == this.getbu_ok()) {
            this.connEtoC2(var1);
        }

        if (var1.getSource() == this.getOk()) {
            this.save();
            this.m_destroy();
        }

        if (var1.getSource() == this.getCancel()) {
            this.m_destroy();
        }

    }

    public void ao2_Init() {
    }

    public void ao2_Start() {
        Awt.setPFrame(Awt.getPFrame());
        this.getCancel().addActionListener(this);
        this.getOk().addActionListener(this);
    }

    private void connEtoC1(ActionEvent var1) {
        try {
            this.m_destroy();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC2(ActionEvent var1) {
        try {
            this.save();
            this.connEtoC3();
        } catch (Throwable var3) {
            this.handleException(var3);
        }

    }

    private void connEtoC3() {
        try {
            this.m_destroy();
        } catch (Throwable var2) {
            this.handleException(var2);
        }

    }

    private void connEtoC4() {
        try {
            this.ao2_Start();
        } catch (Throwable var2) {
            this.handleException(var2);
        }

    }

    private TextField getadministratorName() {
        if (this.ivjadministratorName == null) {
            try {
                this.ivjadministratorName = new TextField();
                this.ivjadministratorName.setName("administratorName");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjadministratorName;
    }

    private Checkbox getao_show_html() {
        if (this.ivjao_show_html == null) {
            try {
                this.ivjao_show_html = new Checkbox();
                this.ivjao_show_html.setName("ao_show_html");
                this.ivjao_show_html.setLabel("ao_show_html");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjao_show_html;
    }

    private GridLayout getAo2GridLayout() {
        GridLayout var1 = null;

        try {
            var1 = new GridLayout(0, 1);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

        return var1;
    }

    private Checkbox getApp_Auto_Lobby() {
        if (this.ivjApp_Auto_Lobby == null) {
            try {
                this.ivjApp_Auto_Lobby = new Checkbox();
                this.ivjApp_Auto_Lobby.setName("App_Auto_Lobby");
                this.ivjApp_Auto_Lobby.setLabel("App_Auto_Lobby");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjApp_Auto_Lobby;
    }

    public String getAppletInfo() {
        return "paintchat.config.Ao2 は VisualAge for Java を使用して作成されました。";
    }

    private Button getbu_ok() {
        if (this.ivjbu_ok == null) {
            try {
                this.ivjbu_ok = new Button();
                this.ivjbu_ok.setName("bu_ok");
                this.ivjbu_ok.setBounds(190, 333, 38, 20);
                this.ivjbu_ok.setLabel(" OK ");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjbu_ok;
    }

    private Button getButton2() {
        if (this.ivjButton2 == null) {
            try {
                this.ivjButton2 = new Button();
                this.ivjButton2.setName("Button2");
                this.ivjButton2.setBounds(235, 359, 50, 20);
                this.ivjButton2.setLabel("CANCEL");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjButton2;
    }

    private LButton getCancel() {
        if (this.ivjCancel == null) {
            try {
                this.ivjCancel = new LButton();
                this.ivjCancel.setName("Cancel");
                this.ivjCancel.setText("Cancel");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjCancel;
    }

    private TextField getchatName() {
        if (this.ivjchatName == null) {
            try {
                this.ivjchatName = new TextField();
                this.ivjchatName.setName("chatName");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjchatName;
    }

    private TextField getchatUrl() {
        if (this.ivjchatUrl == null) {
            try {
                this.ivjchatUrl = new TextField();
                this.ivjchatUrl.setName("chatUrl");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjchatUrl;
    }

    private TextField getcommentString() {
        if (this.ivjcommentString == null) {
            try {
                this.ivjcommentString = new TextField();
                this.ivjcommentString.setName("commentString");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjcommentString;
    }

    private TextField gethomepageName() {
        if (this.ivjhomepageName == null) {
            try {
                this.ivjhomepageName = new TextField();
                this.ivjhomepageName.setName("homepageName");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjhomepageName;
    }

    private TextField gethpUrl() {
        if (this.ivjhpUrl == null) {
            try {
                this.ivjhpUrl = new TextField();
                this.ivjhpUrl.setName("hpUrl");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjhpUrl;
    }

    private TextField getinformtionServerAddress() {
        if (this.ivjinformtionServerAddress == null) {
            try {
                this.ivjinformtionServerAddress = new TextField();
                this.ivjinformtionServerAddress.setName("informtionServerAddress");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjinformtionServerAddress;
    }

    private Label getLabel1() {
        if (this.ivjLabel1 == null) {
            try {
                this.ivjLabel1 = new Label();
                this.ivjLabel1.setName("Label1");
                this.ivjLabel1.setText("chatUrl");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLabel1;
    }

    private Label getLabel2() {
        if (this.ivjLabel2 == null) {
            try {
                this.ivjLabel2 = new Label();
                this.ivjLabel2.setName("Label2");
                this.ivjLabel2.setText("administratorName");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLabel2;
    }

    private Label getLabel3() {
        if (this.ivjLabel3 == null) {
            try {
                this.ivjLabel3 = new Label();
                this.ivjLabel3.setName("Label3");
                this.ivjLabel3.setText("chatName");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLabel3;
    }

    private Label getLabel4() {
        if (this.ivjLabel4 == null) {
            try {
                this.ivjLabel4 = new Label();
                this.ivjLabel4.setName("Label4");
                this.ivjLabel4.setText("commentString");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLabel4;
    }

    private Label getLabel5() {
        if (this.ivjLabel5 == null) {
            try {
                this.ivjLabel5 = new Label();
                this.ivjLabel5.setName("Label5");
                this.ivjLabel5.setText("hpUrl");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLabel5;
    }

    private Label getLabel6() {
        if (this.ivjLabel6 == null) {
            try {
                this.ivjLabel6 = new Label();
                this.ivjLabel6.setName("Label6");
                this.ivjLabel6.setText("homepageName");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLabel6;
    }

    private Label getLabel7() {
        if (this.ivjLabel7 == null) {
            try {
                this.ivjLabel7 = new Label();
                this.ivjLabel7.setName("Label7");
                this.ivjLabel7.setText("informtionServerAddress");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjLabel7;
    }

    private Panel getleftPanel() {
        if (this.ivjleftPanel == null) {
            try {
                this.ivjleftPanel = new Panel();
                this.ivjleftPanel.setName("leftPanel");
                this.ivjleftPanel.setLayout(this.getleftPanelGridLayout());
                this.getleftPanel().add((Component) this.getLabel2(), (Object) this.getLabel2().getName());
                this.ivjleftPanel.add(this.getLabel1());
                this.ivjleftPanel.add(this.getLabel3());
                this.ivjleftPanel.add(this.getLabel4());
                this.ivjleftPanel.add(this.getLabel5());
                this.ivjleftPanel.add(this.getLabel6());
                this.ivjleftPanel.add(this.getLabel7());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjleftPanel;
    }

    private GridLayout getleftPanelGridLayout() {
        GridLayout var1 = null;

        try {
            var1 = new GridLayout(0, 1);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

        return var1;
    }

    private Label getlobby_setup() {
        if (this.ivjlobby_setup == null) {
            try {
                this.ivjlobby_setup = new Label();
                this.ivjlobby_setup.setName("lobby_setup");
                this.ivjlobby_setup.setAlignment(1);
                this.ivjlobby_setup.setText("lobby_setup");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjlobby_setup;
    }

    private LButton getOk() {
        if (this.ivjOk == null) {
            try {
                this.ivjOk = new LButton();
                this.ivjOk.setName("Ok");
                this.ivjOk.setText("   OK   ");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjOk;
    }

    private Panel getPanel3() {
        if (this.ivjPanel3 == null) {
            try {
                this.ivjPanel3 = new Panel();
                this.ivjPanel3.setName("Panel3");
                this.ivjPanel3.setLayout(new FlowLayout());
                this.getPanel3().add((Component) this.getOk(), (Object) this.getOk().getName());
                this.ivjPanel3.add(this.getCancel());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjPanel3;
    }

    private Panel getpanelBottom() {
        if (this.ivjpanelBottom == null) {
            try {
                this.ivjpanelBottom = new Panel();
                this.ivjpanelBottom.setName("panelBottom");
                this.ivjpanelBottom.setLayout(this.getpanelBottomFlowLayout());
                this.getpanelBottom().add((Component) this.getao_show_html(), (Object) this.getao_show_html().getName());
                this.getpanelBottom().add((Component) this.getApp_Auto_Lobby(), (Object) this.getApp_Auto_Lobby().getName());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjpanelBottom;
    }

    private FlowLayout getpanelBottomFlowLayout() {
        FlowLayout var1 = null;

        try {
            var1 = new FlowLayout();
            var1.setAlignment(0);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

        return var1;
    }

    private Panel getPanelConfig() {
        if (this.ivjPanelConfig == null) {
            try {
                this.ivjPanelConfig = new Panel();
                this.ivjPanelConfig.setName("PanelConfig");
                this.ivjPanelConfig.setLayout(new BorderLayout());
                this.getPanelConfig().add((Component) this.getPanelContent(), (Object) "Center");
                this.getPanelConfig().add((Component) this.getPanel3(), (Object) "South");
                this.getPanelConfig().add((Component) this.getlobby_setup(), (Object) "North");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjPanelConfig;
    }

    private Panel getPanelContent() {
        if (this.ivjPanelContent == null) {
            try {
                this.ivjPanelContent = new Panel();
                this.ivjPanelContent.setName("PanelContent");
                this.ivjPanelContent.setLayout(new BorderLayout());
                this.getPanelContent().add((Component) this.getleftPanel(), (Object) "West");
                this.getPanelContent().add((Component) this.getpanelRight(), (Object) "Center");
                this.getPanelContent().add((Component) this.getpanelBottom(), (Object) "South");
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjPanelContent;
    }

    private Panel getpanelRight() {
        if (this.ivjpanelRight == null) {
            try {
                this.ivjpanelRight = new Panel();
                this.ivjpanelRight.setName("panelRight");
                this.ivjpanelRight.setLayout(this.getpanelRightGridLayout());
                this.getpanelRight().add((Component) this.getadministratorName(), (Object) this.getadministratorName().getName());
                this.getpanelRight().add((Component) this.getchatUrl(), (Object) this.getchatUrl().getName());
                this.getpanelRight().add((Component) this.getchatName(), (Object) this.getchatName().getName());
                this.getpanelRight().add((Component) this.getcommentString(), (Object) this.getcommentString().getName());
                this.getpanelRight().add((Component) this.gethpUrl(), (Object) this.gethpUrl().getName());
                this.getpanelRight().add((Component) this.gethomepageName(), (Object) this.gethomepageName().getName());
                this.getpanelRight().add((Component) this.getinformtionServerAddress(), (Object) this.getinformtionServerAddress().getName());
            } catch (Throwable var2) {
                this.handleException(var2);
            }
        }

        return this.ivjpanelRight;
    }

    private GridLayout getpanelRightGridLayout() {
        GridLayout var1 = null;

        try {
            var1 = new GridLayout(0, 1);
        } catch (Throwable var3) {
            this.handleException(var3);
        }

        return var1;
    }

    private void handleException(Throwable var1) {
    }

    public void init() {
        try {
            this.setName("Ao2");
            this.setLayout(this.getAo2GridLayout());
            this.setBackground(new Color(204, 204, 204));
            this.setSize(426, 240);
            this.setForeground(Color.black);
            this.add(this.getPanelConfig());
            this.initConnections();
            Gui.giveDef(this);
            this.load();
            this.initValue();
            ((Dialog) this.getParent()).pack();
        } catch (Throwable var2) {
            this.handleException(var2);
        }

    }

    private void initConnections() throws Exception {
        this.getOk().addActionListener(this);
        this.getCancel().addActionListener(this);
        this.setMouseListener(this, this);
        this.getButton2().addActionListener(this);
        this.getbu_ok().addActionListener(this);
    }

    private void initValue() {
        try {
            super.res = Resource.loadResource("Config");
            this.getResource(super.res, this);
            String var1 = this.getParameter("App_ShowHelp");
            boolean var2 = true;
            if (var1 != null || var1.length() > 0) {
                switch (Character.toLowerCase(var1.charAt(0))) {
                    case '0':
                    case 'f':
                    case 'n':
                        var2 = false;
                        break;
                    default:
                        var2 = true;
                }
            }

            this.getHelp().setIsShow(var2);
        } catch (Throwable var3) {
        }

    }

    private void load() {
        try {
            PProperties var1 = ((ServerStub) this.getAppletContext()).getHashTable();
            this.ivjao_show_html.setState(var1.getBool(this.CF_AO_SHOW_HTML));
            String var2 = System.getProperty("user.name", "");
            this.getadministratorName().setText(var1.getString(this.getadministratorName().getName(), var2));
            this.getchatName().setText(var1.getString(this.getchatName().getName(), var2 + "'s chat room"));
            this.getchatUrl().setText(var1.getString(this.getchatUrl().getName()));
            this.getcommentString().setText(var1.getString(this.getcommentString().getName(), "test room"));
            this.gethpUrl().setText(var1.getString(this.gethpUrl().getName()));
            this.gethomepageName().setText(var1.getString(this.gethomepageName().getName()));
            this.getinformtionServerAddress().setText(var1.getString(this.getinformtionServerAddress().getName(), "http://www.ax.sakura.ne.jp/~aotama/paintchat/paintchatexcheange.conf"));
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void m_destroy() {
        try {
            this.getHelp().reset();
            ((Window) Awt.getParent(this)).dispose();
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public static void main(String[] var0) {
        try {
            Frame var1 = new Frame();
            Class var3 = Class.forName("paintchat.config.Ao");
            ClassLoader var4 = var3.getClassLoader();
            Ao var2 = (Ao) Beans.instantiate(var4, "paintchat.config.Ao");
            var1.add((String) "Center", (Component) var2);
            var1.setSize(var2.getSize());
            var1.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent var1) {
                    System.exit(0);
                }
            });
            var1.setVisible(true);
        } catch (Throwable var5) {
            System.err.println("java.applet.Applet の main() で例外が発生しました");
            var5.printStackTrace(System.out);
        }

    }

    private void save() {
        try {
            Config var1 = (Config) ((ServerStub) this.getAppletContext()).getHashTable();
            if (var1.getInt(this.CF_AO_CHATINDEX) == 0) {
                Random var2 = new Random();
                long var3 = 0L;

                while (true) {
                    if (var3 != 0L) {
                        var1.put(this.CF_AO_CHATINDEX, String.valueOf((int) var3));
                        break;
                    }

                    var3 = (long) var2.nextInt();

                    for (int var5 = 0; var5 < 100; ++var5) {
                        var3 += (long) (var2.nextInt() % 6);
                    }
                }
            }

            this.setParameter(this);
            var1.save(new FileOutputStream(var1.getString("File_Config", "cnf/paintchat.cf")), Resource.loadResource("Config"));
        } catch (Throwable var6) {
            var6.printStackTrace();
        }

    }

    public void start() {
        this.connEtoC4();
    }
}
