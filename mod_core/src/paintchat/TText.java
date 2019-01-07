package paintchat;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
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

import static java.awt.event.ComponentEvent.*;

import syi.awt.Awt;

public class TText extends Dialog implements SW, ActionListener, ItemListener {
    ToolBox ts;
    M mg;
    private Choice cName;
    private Checkbox cIT;
    private Checkbox cBL;
    private Checkbox cV;
    private TextField cSize;
    private TextField cSpace;
    private TextField cFill;

    public TText() {
        super(Awt.getPFrame());
    }

    public void actionPerformed(ActionEvent var1) {
        try {
            this.ts.lift();
            this.mg.iPen = Integer.parseInt(this.cFill.getText());
            this.mg.iSize = Integer.parseInt(this.cSize.getText());
            this.mg.iHint = this.cV.getState() ? M.H_VTEXT : M.H_TEXT;
            this.mg.strHint = (this.cName.getSelectedItem() + '-' + (this.cBL.getState() ? "BOLD" : "") + (this.cIT.getState() ? "ITALIC" : "") + '-').getBytes("UTF8");
            this.mg.iCount = Integer.parseInt(this.cSpace.getText());
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void lift() {
    }

    public void mPack() {
    }

    public void mSetup(ToolBox var1, M.Info var2, M.User var3, M var4, Res var5, Res var6) {
        this.ts = var1;
        this.mg = var4;
        this.setTitle(var5.res("Font"));
        String[] var7 = (String[]) null;

        try {
            Class var8 = Class.forName("java.awt.GraphicsEnvironment");
            Object var9 = var8.getMethod("getLocalGraphicsEnvironment", (Class[]) null).invoke((Object) null, (Object[]) null);
            var7 = (String[]) var8.getMethod("getAvailableFontFamilyNames", (Class[]) null).invoke(var9, (Object[]) null);
        } catch (Throwable var13) {
            ;
        }

        Choice var14 = new Choice();
        this.cName = var14;
        if (var7 != null) {
            for (int var15 = 0; var15 < var7.length; ++var15) {
                var14.addItem(var7[var15]);
            }
        }

        var7 = (String[]) null;
        byte var16 = 0;
        this.setLayout(new GridLayout(0, 1));
        TextField var10 = new TextField("20");
        this.cSize = var10;
        this.cSpace = new TextField("-5");
        this.add(new Label(var5.res("Font"), var16));
        this.add(var14);
        this.add(new Label(var5.res("Size"), var16));
        this.add(var10);
        this.add(new Label(var5.res("WSpace"), var16));
        this.add(this.cSpace);
        Panel var11 = new Panel();
        var11.add(this.cBL = new Checkbox(var5.res("Bold")));
        var11.add(this.cIT = new Checkbox(var5.res("Italic")));
        this.add(var11);
        var11 = new Panel();
        var11.add(this.cV = new Checkbox(var5.res("VText")));
        var11.add(this.cFill = new TextField("1"));
        this.add(var11);
        var11 = new Panel();
        Button var12 = new Button(var5.res("Apply"));
        var12.addActionListener(this);
        var11.add(var12);
        this.add((Component) var11, (Object) "Center");
        this.cFill.addActionListener(this);
        var10.addActionListener(this);
        this.cSpace.addActionListener(this);
        var14.addItemListener(this);
        this.cBL.addItemListener(this);
        this.cIT.addItemListener(this);
        this.cV.addItemListener(this);
        this.pack();
        this.enableEvents(WINDOW_EVENT_MASK);
        this.setVisible(true);
    }

    protected void processWindowEvent(WindowEvent var1) {
        if (var1.getID() == 201) {
            //this.setVisible(false);
            this.dispose();
        }

    }

    public void up() {
    }

    public void itemStateChanged(ItemEvent var1) {
        this.actionPerformed((ActionEvent) null);
    }
}
