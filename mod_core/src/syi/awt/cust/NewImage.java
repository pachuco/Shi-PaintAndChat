package syi.awt.cust;

import paintchat.Res;
import syi.awt.*;

import java.awt.*;
import java.awt.event.*;

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


    CheckboxGroup group;
    Checkbox radNormal;
    Checkbox radPro;
    Button okBut;
    Button canBut;
    Label wLabel;
    Label hLabel;
    TextField wField;
    TextField hField;

    private Dimension dim;
    private String mode;


    public NewImage(Frame owner, Dimension dim, String m) {
        super(owner, "New image");
        if(m == null) m = "";

        setLayout(null);
        setBackground(Awt.cBk);
        setFont(Awt.getDefFont());

        //TODO: rewrite this crap to not use absolute positions
        wLabel = new Label();
        wLabel.setLocation(14, 35);
        wLabel.setSize(45, 25);
        wLabel.setText("Width" + ":");
        add(wLabel);

        hLabel = new Label();
        hLabel.setLocation(146, 35);
        hLabel.setSize(45, 25);
        hLabel.setText("Height" + ":");
        add(hLabel);

        wField = new TextField();
        wField.setLocation(63, 35);
        wField.setSize(60, 25);
        wField.setBackground(new Color(-1));
        wField.setText(String.valueOf(dim.width));
        wField.setColumns(10);
        wField.addKeyListener(kaFld);
        wField.addKeyListener(kaAny);
        add(wField);

        hField = new TextField();
        hField.setLocation(195, 35);
        hField.setSize(54, 25);
        hField.setBackground(new Color(-1));
        hField.setText(String.valueOf(dim.height));
        hField.setColumns(10);
        hField.addKeyListener(kaFld);
        hField.addKeyListener(kaAny);
        add(hField);

        group = new CheckboxGroup();

        radNormal = new Checkbox();
        radNormal.setLocation(20, 70);
        radNormal.setSize(120, 30);
        radNormal.setLabel("Normal mode");
        radNormal.setState(m.equals("normal"));
        radNormal.addKeyListener(kaAny);
        radNormal.setCheckboxGroup(group);
        add(radNormal);


        radPro = new Checkbox();
        radPro.setLocation(150, 70);
        radPro.setSize(120, 30);
        radPro.setLabel("Pro mode");
        radPro.setState(m.equals("pro"));
        radPro.addKeyListener(kaAny);
        radPro.setCheckboxGroup(group);
        add(radPro);


        okBut = new Button();
        okBut.setLocation(67, 100);
        okBut.setSize(70, 28);
        okBut.setLabel("Ok");
        okBut.addKeyListener(kaAny);
        add(okBut);

        canBut = new Button();
        canBut.setLocation(150, 100);
        canBut.setSize(70, 28);
        canBut.setLabel("Cancel");
        canBut.addKeyListener(kaAny);
        add(canBut);

        setSize(280, 160);
        Awt.moveCenter(this);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);


        okBut.setActionCommand(hnd_ok);
        okBut.addActionListener(this);
        canBut.setActionCommand(hnd_cancel);
        canBut.addActionListener(this);
        addWindowListener(this);

        setVisible(true);
    }

    public void submit() {
        if (wField.getText().equals("") || hField.getText().equals("")) return;

        int w = Integer.parseInt(wField.getText());
        int h = Integer.parseInt(hField.getText());
        dim = new Dimension(w, h);

        Checkbox c = group.getSelectedCheckbox();
        if (c == radNormal) mode = "normal";
        if (c == radPro) mode = "pro";

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