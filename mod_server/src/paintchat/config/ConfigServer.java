package paintchat.config;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
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

import paintchat.Config;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.awt.LTextField;

public class ConfigServer extends ConfigApplet implements ActionListener {
    private Panel ivjPanel1 = null;
    private GridLayout ivjPanel1GridLayout = null;
    private Panel ivjPanel2 = null;
    private GridLayout ivjPanel2GridLayout = null;
    private Checkbox ivjServer_Load_Line = null;
    private Checkbox ivjServer_Log_Text = null;
    private Button ivjButton1 = null;
    private LButton ivjCancel = null;
    private LButton ivjOk = null;
    private Panel ivjPanel3 = null;
    private Checkbox ivjServer_Cash_Line = null;
    private LTextField ivjServer_Cash_Line_Size = null;
    private Checkbox ivjServer_Cash_Text = null;
    private LTextField ivjServer_Cash_Text_Size = null;
    private Checkbox ivjServer_Log_Line = null;
    private Checkbox ivjServer_Load_Text = null;
    private TextField ivjClient_Image_Height = null;
    private TextField ivjClient_Image_Width = null;
    private Label ivjLabel1 = null;
    private Label ivjLabel2 = null;
    private Panel ivjPanel4 = null;
    private Checkbox ivjClient_Sound = null;
    private LTextField textPermission;

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.getButton1()) {
            this.connEtoM1(event);
        }

        if (event.getSource() == this.getOk()) {
            this.mSave();
            this.mDestroy();
        }

        if (event.getSource() == this.getCancel()) {
            this.mDestroy();
        }

    }

    public void button1_ActionPerformed(ActionEvent event) {
    }

    private void connEtoC1() {
        try {
            this.initValue();
        } catch (Throwable ex) {
            this.handleException(ex);
        }

    }

    private void connEtoM1(ActionEvent event) {
        try {
            this.stop();
        } catch (Throwable ex) {
            this.handleException(ex);
        }

    }

    public String getAppletInfo() {
        return "paintchat.config.ConfigServer は VisualAge for Java を使用して作成されました。";
    }

    private Button getButton1() {
        if (this.ivjButton1 == null) {
            try {
                this.ivjButton1 = new Button();
                this.ivjButton1.setName("Button1");
                this.ivjButton1.setBounds(134, 283, 56, 20);
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

    private TextField getClient_Image_Height() {
        if (this.ivjClient_Image_Height == null) {
            try {
                this.ivjClient_Image_Height = new TextField();
                this.ivjClient_Image_Height.setName("Client_Image_Height");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjClient_Image_Height;
    }

    private TextField getClient_Image_Width() {
        if (this.ivjClient_Image_Width == null) {
            try {
                this.ivjClient_Image_Width = new TextField();
                this.ivjClient_Image_Width.setName("Client_Image_Width");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjClient_Image_Width;
    }

    private Checkbox getClient_Sound() {
        if (this.ivjClient_Sound == null) {
            try {
                this.ivjClient_Sound = new Checkbox();
                this.ivjClient_Sound.setName("Client_Sound");
                this.ivjClient_Sound.setLabel("Client_Sound");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjClient_Sound;
    }

    private Label getLabel1() {
        if (this.ivjLabel1 == null) {
            try {
                this.ivjLabel1 = new Label();
                this.ivjLabel1.setName("Label1");
                this.ivjLabel1.setAlignment(2);
                this.ivjLabel1.setText("Client_Image_Width");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjLabel1;
    }

    private Label getLabel2() {
        if (this.ivjLabel2 == null) {
            try {
                this.ivjLabel2 = new Label();
                this.ivjLabel2.setName("Label2");
                this.ivjLabel2.setAlignment(2);
                this.ivjLabel2.setText("Client_Image_Height");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjLabel2;
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
                this.getPanel1().add((Component) this.getServer_Cash_Text(), (Object) this.getServer_Cash_Text().getName());
                this.getPanel1().add((Component) this.getServer_Cash_Line(), (Object) this.getServer_Cash_Line().getName());
                this.getPanel1().add((Component) this.getServer_Log_Text(), (Object) this.getServer_Log_Text().getName());
                this.getPanel1().add((Component) this.getServer_Log_Line(), (Object) this.getServer_Log_Line().getName());
                this.getPanel1().add((Component) this.getServer_Load_Text(), (Object) this.getServer_Load_Text().getName());
                this.getPanel1().add((Component) this.getServer_Load_Line(), (Object) this.getServer_Load_Line().getName());
                this.getPanel1().add((Component) this.getClient_Sound(), (Object) this.getClient_Sound().getName());
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
                this.ivjPanel2.add(this.getPanel4());
                this.getPanel2().add((Component) this.getServer_Cash_Text_Size(), (Object) this.getServer_Cash_Text_Size().getName());
                this.getPanel2().add((Component) this.getServer_Cash_Line_Size(), (Object) this.getServer_Cash_Line_Size().getName());
                LTextField var1 = new LTextField();
                this.textPermission = var1;
                var1.setText("Client_Permission");
                var1.setName(var1.getText());
                this.getPanel2().add((Component) var1, (Object) var1.getName());
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

    private Panel getPanel4() {
        if (this.ivjPanel4 == null) {
            try {
                this.ivjPanel4 = new Panel();
                this.ivjPanel4.setName("Panel4");
                this.ivjPanel4.setLayout(new GridLayout());
                this.getPanel4().add((Component) this.getLabel1(), (Object) this.getLabel1().getName());
                this.getPanel4().add((Component) this.getClient_Image_Width(), (Object) this.getClient_Image_Width().getName());
                this.getPanel4().add((Component) this.getLabel2(), (Object) this.getLabel2().getName());
                this.getPanel4().add((Component) this.getClient_Image_Height(), (Object) this.getClient_Image_Height().getName());
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjPanel4;
    }

    private Checkbox getServer_Cash_Line() {
        if (this.ivjServer_Cash_Line == null) {
            try {
                this.ivjServer_Cash_Line = new Checkbox();
                this.ivjServer_Cash_Line.setName("Server_Cash_Line");
                this.ivjServer_Cash_Line.setLabel("Server_Cash_Line");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Cash_Line;
    }

    private LTextField getServer_Cash_Line_Size() {
        if (this.ivjServer_Cash_Line_Size == null) {
            try {
                this.ivjServer_Cash_Line_Size = new LTextField();
                this.ivjServer_Cash_Line_Size.setName("Server_Cash_Line_Size");
                this.ivjServer_Cash_Line_Size.setText("Server_Cash_Text_Size");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Cash_Line_Size;
    }

    private Checkbox getServer_Cash_Text() {
        if (this.ivjServer_Cash_Text == null) {
            try {
                this.ivjServer_Cash_Text = new Checkbox();
                this.ivjServer_Cash_Text.setName("Server_Cash_Text");
                this.ivjServer_Cash_Text.setLabel("Server_Cash_Text");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Cash_Text;
    }

    private LTextField getServer_Cash_Text_Size() {
        if (this.ivjServer_Cash_Text_Size == null) {
            try {
                this.ivjServer_Cash_Text_Size = new LTextField();
                this.ivjServer_Cash_Text_Size.setName("Server_Cash_Text_Size");
                this.ivjServer_Cash_Text_Size.setText("Server_Cash_Text_Size");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Cash_Text_Size;
    }

    private Checkbox getServer_Load_Line() {
        if (this.ivjServer_Load_Line == null) {
            try {
                this.ivjServer_Load_Line = new Checkbox();
                this.ivjServer_Load_Line.setName("Server_Load_Line");
                this.ivjServer_Load_Line.setLabel("Server_Load_Line");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Load_Line;
    }

    private Checkbox getServer_Load_Text() {
        if (this.ivjServer_Load_Text == null) {
            try {
                this.ivjServer_Load_Text = new Checkbox();
                this.ivjServer_Load_Text.setName("Server_Load_Text");
                this.ivjServer_Load_Text.setLabel("Server_Load_Text");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Load_Text;
    }

    private Checkbox getServer_Log_Line() {
        if (this.ivjServer_Log_Line == null) {
            try {
                this.ivjServer_Log_Line = new Checkbox();
                this.ivjServer_Log_Line.setName("Server_Log_Line");
                this.ivjServer_Log_Line.setLabel("Server_Log_Line");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Log_Line;
    }

    private Checkbox getServer_Log_Text() {
        if (this.ivjServer_Log_Text == null) {
            try {
                this.ivjServer_Log_Text = new Checkbox();
                this.ivjServer_Log_Text.setName("Server_Log_Text");
                this.ivjServer_Log_Text.setLabel("Server_Log_Text");
            } catch (Throwable ex) {
                this.handleException(ex);
            }
        }

        return this.ivjServer_Log_Text;
    }

    private void handleException(Throwable ex) {
    }

    public void init() {
        try {
            this.setName("ConfigServer");
            this.setLayout(new BorderLayout());
            this.setSize(359, 226);
            this.add((Component) this.getPanel1(), (Object) "North");
            this.add((Component) this.getPanel2(), (Object) "Center");
            this.add((Component) this.getPanel3(), (Object) "South");
            this.initConnections();
            this.connEtoC1();
            Gui.giveDef(this);
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
        this.getClient_Image_Width().setText(this.getParameter(this.getClient_Image_Width().getName()));
        this.getClient_Image_Height().setText(this.getParameter(this.getClient_Image_Height().getName()));
        this.textPermission.setText(this.getParameter(this.textPermission.getName()));
    }

    public static void main(String[] var0) {
        try {
            Frame var1 = new Frame();
            Class var3 = Class.forName("paintchat.config.ConfigServer");
            ClassLoader var4 = var3.getClassLoader();
            ConfigServer var2 = (ConfigServer) Beans.instantiate(var4, "paintchat.config.ConfigServer");
            var1.add((String) "Center", (Component) var2);
            var1.setSize(var2.getSize());
            var1.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent event) {
                    System.exit(0);
                }
            });
            var1.setVisible(true);
        } catch (Throwable ex) {
            System.err.println("paintchat.config.ConfigApplet の main() で例外が発生しました");
            ex.printStackTrace(System.out);
        }

    }

    public void mDestroy() {
        try {
            this.getHelp().reset();
            ((Window) this.getParent()).dispose();
        } catch (RuntimeException var2) {
            var2.printStackTrace();
        }

    }

    public void mSave() {
        try {
            this.setParameter(this);
            Config var1 = (Config) ((ServerStub) this.getAppletContext()).getHashTable();
            var1.save(new FileOutputStream(var1.getString("File_Config", "cnf/paintchat.cf")), Resource.loadResource("Config"));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }
}
