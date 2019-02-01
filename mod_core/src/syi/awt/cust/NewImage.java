package syi.awt.cust;

import java.awt.*;
import java.awt.event.*;

import syi.awt.*;

import static syi.C.ShiPainter.*;
import static res.ResShiClient.*;

public class NewImage extends Dialog implements WindowListener, ActionListener {
    private final static String hnd_cancel = "hnd_cancel";
    private final static String hnd_ok = "hnd_ok";

    private KeyAdapter kaFld = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            boolean p = true;

            p|= c == KeyEvent.VK_BACK_SPACE;
            p|= c == KeyEvent.VK_DELETE;
            p|= c == KeyEvent.VK_ENTER;
            p|= c == KeyEvent.VK_TAB;
            p|= Character.isDigit(c);
            if (!p) e.consume();
        }
    };

    private KeyAdapter kaAny = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();

            if       (c == KeyEvent.VK_ENTER) {
                submit();
            }else if (c == KeyEvent.VK_ESCAPE) {
                cancel();
            }

        }
    };

    private static int GAP = 5;
    private static int PAD = 30;

    private CheckboxGroup group;
    private Checkbox radNormal;
    private Checkbox radPro;
    private TextField fieldW;
    private TextField fieldH;
    private Dimension dim;
    private String mode;


    public NewImage(Frame owner, Dimension dim, String m) {
        super(owner, lang.get("newTitle"));
        if(m == null) m = "";

        Container conAll;
        Container conSize, conMode, conButt;
        Container conW, conH;
        Button buttOk, buttCan;
        Label labW, labH;

        setLayout(new FlowLayout(FlowLayout.CENTER, PAD, PAD));
        setBackground(Awt.cBk);
        setFont(Awt.getDefFont());

        conAll = new Container();
        conAll.setLayout(new GridLayout(3, 1, 0, GAP));

        //Size fields
        conSize = new Container();
        conSize.setLayout(new GridLayout(1,2, GAP, 0));

        conW = new Container();
        conW.setLayout(new FlowLayout(FlowLayout.CENTER));
        labW = new Label(lang.get("newWidth"));
        fieldW = new TextField();
        fieldW.setColumns(5);
        fieldW.addKeyListener(kaFld);
        fieldW.addKeyListener(kaAny);
        fieldW.setText(String.valueOf(dim.width));
        conW.add(labW);
        conW.add(fieldW);
        conH = new Container();
        conH.setLayout(new FlowLayout(FlowLayout.CENTER));
        labH = new Label(lang.get("newHeight"));
        fieldH = new TextField();
        fieldH.setColumns(5);
        fieldH.addKeyListener(kaFld);
        fieldH.addKeyListener(kaAny);
        fieldH.setText(String.valueOf(dim.height));
        conH.add(labH);
        conH.add(fieldH);

        conSize.add(conW);
        conSize.add(conH);

        //Mode radio buttons
        conMode = new Container();
        conMode.setLayout(new GridLayout(1,2, GAP, 0));
        group = new CheckboxGroup();

        radNormal = new Checkbox();
        radNormal.setLabel(lang.get("newModeNormal"));
        radNormal.setState(m.equals(GUI_NORMAL));
        radNormal.addKeyListener(kaAny);
        radNormal.setCheckboxGroup(group);
        radPro = new Checkbox();
        radPro.setLabel(lang.get("newModePro"));
        radPro.setState(m.equals(GUI_PRO));
        radPro.addKeyListener(kaAny);
        radPro.setCheckboxGroup(group);

        conMode.add(radNormal);
        conMode.add(radPro);

        //Confirmation buttons
        conButt = new Container();
        conButt.setLayout(new GridLayout(1,2, GAP, 0));

        buttOk = new Button();
        buttOk.setLabel(lang.get("ok"));
        buttOk.setActionCommand(hnd_ok);
        buttOk.addActionListener(this);
        buttCan = new Button();
        buttCan.setLabel(lang.get("cancel"));
        buttCan.setActionCommand(hnd_cancel);
        buttCan.addActionListener(this);

        conButt.add(buttOk);
        conButt.add(buttCan);

        conAll.add(conSize);
        conAll.add(conMode);
        conAll.add(conButt);
        add(conAll);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);
        addWindowListener(this);
        pack();
        Awt.moveCenter(this);
        setVisible(true);
    }

    public void submit() {
        if (fieldW.getText().equals("") || fieldH.getText().equals("")) return;

        int w = Integer.parseInt(fieldW.getText());
        int h = Integer.parseInt(fieldH.getText());
        dim = new Dimension(w, h);

        Checkbox c = group.getSelectedCheckbox();
        if (c == radNormal) mode = GUI_NORMAL;
        if (c == radPro)    mode = GUI_PRO;

        dispose();
    }

    public void cancel() {
        dim = null;
        mode = null;
        dispose();
    }


    //WindowListener
    public void windowClosing(WindowEvent e) {
        cancel();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    //ActionListener
    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        if (com.equals(hnd_ok)) {
            submit();
        }
        if (com.equals(hnd_cancel)) {
            cancel();
        }
    }

    public Dimension getDim() {
        return dim;
    }

    public String getMode() {
        return mode;
    }
}