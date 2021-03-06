package paintchat_client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import syi.awt.Awt;

import static res.ResShiClient.*;

public class DCF extends Dialog implements ItemListener, ActionListener {
    private Checkbox cbAdmin = new Checkbox();
    private TextField tPas = new TextField(10);
    private Label lPas = new Label();
    private TextField tName = new TextField(10);
    private Panel pText = new Panel(new GridLayout(0, 1));
    private String strName = "";
    private String strPas = "";
    boolean isAdmin;

    public DCF() {
        super(Awt.getPFrame(), langSP.get("handle"), true);
        this.setLayout(new BorderLayout());
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == 1) {
            this.pText.add(this.lPas);
            this.pText.add(this.tPas);
        } else {
            this.pText.remove(this.lPas);
            this.pText.remove(this.tPas);
        }

        this.pack();
    }

    public void actionPerformed(ActionEvent event) {
        String var2 = this.tName.getText().trim();
        if (var2.length() > 0) {
            this.up();
            this.dispose();
        } else {
            this.tName.setText("");
        }

    }

    public void mShow() {
        this.tName.addActionListener(this);
        this.tPas.addActionListener(this);
        Panel var1 = new Panel();
        Button var2 = new Button(langSP.get("enter"));
        var2.addActionListener(this);
        var1.add(var2);
        this.add((String) "South", (Component) var1);
        this.pText.add(new Label(this.getTitle()));
        this.pText.add(this.tName);
        this.cbAdmin.setLabel(langSP.get("admin"));
        this.cbAdmin.addItemListener(this);
        this.pText.add(this.cbAdmin);
        this.add((String) "Center", (Component) this.pText);
        this.lPas.setText(langSP.get("password"));
        this.enableEvents(WindowEvent.WINDOW_EVENT_MASK);
        Awt.getDef(this);
        Awt.setDef(this, false);
        this.pack();
        Awt.moveCenter(this);
        this.tName.requestFocus();
        this.up();
        this.setVisible(true);
    }

    public void mReset() {
        this.tPas.setText("");
        this.tName.setText("");
        this.cbAdmin.setState(false);
        this.up();
    }

    protected void processWindowEvent(WindowEvent event) {
        switch (event.getID()) {
            case WindowEvent.WINDOW_CLOSING:
                this.mReset();
                this.dispose();
            default:
        }
    }

    public String mGetHandle() {
        return this.strName;
    }

    public String mGetPass() {
        return this.strPas;
    }

    private void up() {
        this.strName = this.tName.getText();
        if (this.strName.length() > 10) {
            this.strName = this.strName.substring(0, 10);
        }

        this.isAdmin = this.cbAdmin.getState();
        this.strPas = !this.isAdmin ? "" : this.tPas.getText();
    }
}
