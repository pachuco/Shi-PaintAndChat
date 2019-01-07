package paintchat_client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import paintchat.Res;
import syi.awt.Awt;

/** Dialog for prompts, modals, alerts */
public class Me extends Dialog implements ActionListener {
    private static boolean isD = false;
    public static Res res;
    public static Res conf;
    private Button bOk;
    private Button bNo;
    private TextField tText;
    private Panel pBotton;
    private Panel pText;
    public boolean isOk;

    public Me() {
        super(Awt.getPFrame());
        this.enableEvents(64L);
        this.setModal(true);
        this.setLayout(new BorderLayout(5, 5));
        String var1 = "yes";
        String var2 = "no";
        this.pText = new Panel(new GridLayout(0, 1));
        this.add((Component) this.pText, (Object) "North");
        this.bOk = new Button(p(var1));
        this.bOk.addActionListener(this);
        this.bNo = new Button(p(var2));
        this.bNo.addActionListener(this);
        this.pBotton = new Panel(new FlowLayout(1, 10, 4));
        this.pBotton.add(this.bOk);
        this.add((Component) this.pBotton, (Object) "South");
    }

    public void actionPerformed(ActionEvent var1) {
        this.isOk = var1.getSource() == this.bOk || var1.getSource() instanceof TextField;
        this.dispose();
    }

    public void init(String var1, boolean var2) {
        var1 = p(var1);
        this.setConfirm(var2);
        int var3 = 0;

        while (var3 < var1.length()) {
            String var4 = r(var1, var3);
            ++var3;
            if (var4 != null) {
                this.ad(var4);
                var3 += var4.length();
            }
        }

        Awt.getDef(this);
        this.setBackground(new Color(conf.getP("dlg_color_bk", Awt.cBk.getRGB())));
        this.setForeground(new Color(conf.getP("dlg_color_text", Awt.cFore.getRGB())));
        Awt.setDef(this, false);
        this.pack();
        Awt.moveCenter(this);
    }

    public Label ad(String var1) {
        Label var2 = new Label(var1);
        this.pText.add(var2);
        return var2;
    }

    public static void alert(String var0) {
        confirm(var0, false);
    }

    public static boolean confirm(String var0, boolean var1) {
        isD = true;
        Me var2 = getMe();

        try {
            var2.init(var0, var1);
            var2.setVisible(true);
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

        isD = false;
        return var2.isOk;
    }

    public static String getString(String var0, String var1) {
        isD = true;
        Me var2 = getMe();

        try {
            var2.init(var0, true);
            if (var1 == null) {
                var1 = "";
            }

            if (var2.tText == null) {
                var2.tText = new TextField(var1);
            } else {
                var2.tText.setText(var1);
            }

            var2.add((Component) var2.tText, (Object) "Center");
            var2.pack();
            var2.setVisible(true);
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

        isD = false;
        return var2.isOk ? var2.tText.getText() : null;
    }

    public static Me getMe() {
        Me var0 = new Me();
        return var0;
    }

    public static boolean isDialog() {
        return isD;
    }

    protected void processWindowEvent(WindowEvent var1) {
        if (var1.getID() == WindowEvent.WINDOW_CLOSING) {
            var1.getWindow().dispose();
        }

    }

    private static String r(String var0, int var1) {
        int var3;
        for (var3 = var1; var3 < var0.length(); ++var3) {
            char var2 = var0.charAt(var3);
            if (var2 == '\r' || var2 == '\n') {
                break;
            }
        }

        return var1 == var3 ? null : var0.substring(var1, var3);
    }

    private static String p(String var0) {
        return res == null ? var0 : res.res(var0);
    }

    private void setConfirm(boolean canCancel) {
        int var2 = this.pBotton.getComponentCount();
        if (canCancel) {
            if (var2 <= 1) {
                this.pBotton.add(this.bNo);
            }
        } else if (var2 >= 2) {
            this.pBotton.remove(this.bNo);
        }

    }
}
