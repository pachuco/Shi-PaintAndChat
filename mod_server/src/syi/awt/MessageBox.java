package syi.awt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Hashtable;

public class MessageBox extends Dialog implements ActionListener {
    private static MessageBox message = null;
    public boolean bool = false;
    private static Hashtable res;
    private Panel panelUnder;
    private Panel panelUpper;
    private Panel panelCenter;
    private LButton b_ok;
    private LButton b_cancel;
    private TextField textField = new TextField();
    private TextCanvas textCanvas = new TextCanvas();

    public MessageBox(Frame var1) {
        super(var1);
        this.setForeground(var1.getForeground());
        this.setBackground(var1.getBackground());
        super.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        this.setLayout(new BorderLayout());
        this.setModal(true);
        this.setResizable(false);
        Awt.getDef(this);
        this.textCanvas.isBorder = false;
        this.textField.setColumns(64);
        this.panelUnder = new Panel();
        this.panelUnder.setLayout(new FlowLayout());
        String var2 = "Ok";
        String var3 = "Cancel";
        this.b_ok = new LButton(var2);
        this.b_cancel = new LButton(var3);
        if (res != null) {
            String var4 = (String) res.get(var2);
            if (var4 != null) {
                this.b_ok.setText(var4);
            }

            var4 = (String) res.get(var3);
            if (var4 != null) {
                this.b_cancel.setText(var4);
            }
        }

        this.panelUnder.add(this.b_ok);
        this.b_ok.addActionListener(this);
        this.b_cancel.addActionListener(this);
        this.textField.addActionListener(this);
        this.add((Component) this.textCanvas, (Object) "North");
        this.add((Component) this.panelUnder, (Object) "South");
        Awt.setDef(this, false);
    }

    public void actionPerformed(ActionEvent var1) {
        Object var2 = var1.getSource();
        this.bool = var2 == this.b_ok || var2 == this.textField;

        try {
            Object var3 = (Component) var1.getSource();

            for (int var4 = 0; var4 < 10 && var3 != null && var3 != this; ++var4) {
                var3 = ((Component) var3).getParent();
            }

            ((Window) var3).dispose();
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }

    public static void alert(String var0, String var1) {
        messageBox(var0, var1, 1);
    }

    public static boolean confirm(String var0, String var1) {
        return messageBox(var0, var1, 2);
    }

    public void error(String var1) {
        String var2 = "TitleOfError";
        String var3 = (String) res.get(var2);
        if (var3 == null) {
            var3 = var2;
        }

        alert(var1, var3);
    }

    private static String getRes(String var0) {
        if (res == null) {
            return var0;
        } else if (var0 == null) {
            return "";
        } else {
            String var1 = (String) res.get(var0);
            return var1 == null ? var0 : var1;
        }
    }

    public static String getString(String var0, String var1) {
        return messageBox(var0, var1, 3) ? message.getText() : var0;
    }

    public static String getString(String var0, String var1, Point var2) {
        return messageBox(var0, var1, var2, 3) ? message.getText() : var0;
    }

    public String getText() {
        return this.textField.getText();
    }

    public static synchronized boolean messageBox(String var0, String var1, int var2) {
        return messageBox(var0, var1, (Point) null, var2);
    }

    public static synchronized boolean messageBox(String var0, String var1, Point var2, int var3) {
        boolean var4 = false;

        try {
            MessageBox var5 = message;
            if (var5 == null) {
                message = new MessageBox(Awt.getPFrame());
                var5 = message;
            } else if (var5.isShowing()) {
                var5.dispose();
            }

            Awt.getDef(var5);
            var5.resetMessage();
            var5.setOkCancel(var3 >= 2);
            if (var3 == 3) {
                var5.textField.setText(var0);
                var5.textCanvas.setText(getRes("EorBInput"));
            } else {
                var5.setText(getRes(var0));
            }

            var5.setTextField(var3 == 3);
            var5.setTitle(getRes(var1));
            var5.pack();
            if (var2 != null) {
                var5.setLocation(var2);
            } else {
                Awt.moveCenter(var5);
            }

            var5.setVisible(true);
            var4 = var5.bool;
        } catch (Throwable var6) {
            System.out.println("message" + var6);
        }

        return var4;
    }

    protected void processWindowEvent(WindowEvent event) {
        try {
            int eventID = event.getID();
            if (eventID == WindowEvent.WINDOW_CLOSING) {
                this.dispose();
            }
        } catch (Throwable ex) {
        }

    }

    private void resetMessage() {
        if (this.textField != null) {
            this.textField.setText("");
        }

        this.bool = false;
    }

    private void setOkCancel(boolean var1) {
        if (var1) {
            this.panelUnder.add(this.b_cancel);
        } else {
            this.panelUnder.remove(this.b_cancel);
        }

    }

    public static void setResource(Hashtable var0) {
        res = var0;
    }

    public void setText(String var1) throws IOException {
        this.textCanvas.setText(var1);
    }

    private void setTextField(boolean var1) {
        if (var1) {
            this.add((Component) this.textField, (Object) "Center");
            this.textField.requestFocus();
            this.textField.selectAll();
        } else {
            this.remove(this.textField);
        }

    }
}
