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
import paintchat.Res;
import syi.awt.Awt;

public class DCF extends Dialog implements ItemListener, ActionListener {
   private Res res;
   private Checkbox cbAdmin = new Checkbox();
   private TextField tPas = new TextField(10);
   private Label lPas = new Label();
   private TextField tName = new TextField(10);
   private Panel pText = new Panel(new GridLayout(0, 1));
   private String strName = "";
   private String strPas = "";
   boolean isAdmin;

   public DCF(Res var1) {
      super(Awt.getPFrame(), var1.res("handle"), true);
      this.setLayout(new BorderLayout());
      this.res = var1;
   }

   public void itemStateChanged(ItemEvent var1) {
      if (var1.getStateChange() == 1) {
         this.pText.add(this.lPas);
         this.pText.add(this.tPas);
      } else {
         this.pText.remove(this.lPas);
         this.pText.remove(this.tPas);
      }

      this.pack();
   }

   public void actionPerformed(ActionEvent var1) {
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
      Button var2 = new Button(this.res.res("enter"));
      var2.addActionListener(this);
      var1.add(var2);
      this.add((String)"South", (Component)var1);
      this.pText.add(new Label(this.getTitle()));
      this.pText.add(this.tName);
      this.cbAdmin.setLabel(this.res.res("admin"));
      this.cbAdmin.addItemListener(this);
      this.pText.add(this.cbAdmin);
      this.add((String)"Center", (Component)this.pText);
      this.lPas.setText(this.res.res("password"));
      this.enableEvents(64L);
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

   protected void processWindowEvent(WindowEvent var1) {
      switch(var1.getID()) {
      case 201:
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
