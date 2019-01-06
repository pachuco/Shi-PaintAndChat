package syi.awt;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

public class LTextField extends Component {
    private String Title;
    private String Text;
    private int Gap;
    private boolean edit;
    private Color Bk;
    private Color BkD;
    private Color Fr;
    private boolean isPress;
    private Dimension size;
    private ActionListener actionListener;

    public LTextField() {
        this("", "");
    }

    public LTextField(String var1) {
        this(var1, "");
    }

    public LTextField(String var1, String var2) {
        this.Gap = 3;
        this.edit = true;
        this.isPress = false;
        this.size = null;
        this.actionListener = null;
        this.enableEvents(17L);
        this.setBk(Color.white);
        this.setFr(new Color(5263480));
        this.setText(var1);
        this.setTitle(var2);
    }

    public void addActionListener(ActionListener var1) {
        this.actionListener = var1;
    }

    public boolean getEdit() {
        return this.edit;
    }

    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    public Dimension getPreferredSize() {
        Font var1 = this.getFont();
        if (var1 == null) {
            return this.size;
        } else {
            FontMetrics var2 = this.getFontMetrics(var1);
            if (var2 == null) {
                return this.size;
            } else {
                int var3 = this.Gap * 4;
                if (this.Text != null) {
                    var3 += var2.stringWidth(this.Text);
                } else {
                    var3 += 10;
                }

                if (this.Title != null) {
                    var3 += var2.stringWidth(this.Title);
                } else {
                    var3 += 10;
                }

                return new Dimension(var3, var2.getMaxAscent() + var2.getMaxDescent() + this.Gap * 2);
            }
        }
    }

    public Dimension getSize() {
        if (this.size == null) {
            this.size = super.getSize();
        }

        return this.size;
    }

    public String getText() {
        return this.Text == null ? "" : this.Text;
    }

    public String getTitle() {
        return this.Title == null ? "" : this.Title;
    }

    public void paint(Graphics var1) {
        try {
            FontMetrics var2 = var1.getFontMetrics();
            int var3 = this.Gap;
            int var4 = var2.getMaxAscent() + 1;
            if (this.Title.length() > 0) {
                int var5 = var2.stringWidth(this.Title);
                var1.drawString(this.Title, this.Gap, this.Gap + var4);
                var3 += this.Gap + var5;
            }

            Dimension var7 = this.getSize();
            Awt.fillFrame(var1, this.isPress, var3, 0, var7.width - var3, var7.height);
            if (this.Text.length() > 0) {
                var1.setColor(this.Fr);
                var1.drawString(this.Text, var3 + this.Gap, this.Gap + var4);
            }
        } catch (Throwable var6) {
            var6.printStackTrace();
        }

    }

    protected void processEvent(AWTEvent var1) {
        try {
            int var2 = var1.getID();
            if (var1 instanceof MouseEvent) {
                MouseEvent var3 = (MouseEvent) var1;
                var3.consume();
                if (this.edit && var2 == 501) {
                    this.isPress = true;
                    this.repaint();
                }

                if (this.edit && var2 == 502) {
                    var3.consume();
                    this.repaint();
                    this.isPress = false;
                    Point var4 = var3.getPoint();
                    if (this.contains(var4)) {
                        String var5 = this.getText();
                        Point var6 = this.getLocationOnScreen();
                        var4.translate(var6.x, var6.y);
                        this.setText(MessageBox.getString(this.getText(), this.getTitle()));
                        if (!var5.equals(this.getText()) && this.actionListener != null) {
                            this.actionListener.actionPerformed(new ActionEvent(this, 1001, this.getText()));
                        }
                    }
                }
            }

            if (var1 instanceof ComponentEvent && (var2 == 101 || var2 == 102)) {
                this.size = null;
            }

            super.processEvent(var1);
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

    }

    public void pMouse(MouseEvent var1) {
    }

    public void setBk(Color var1) {
        this.Bk = var1;
        this.BkD = var1.darker();
    }

    public void setEdit(boolean var1) {
        this.edit = var1;
    }

    public void setFr(Color var1) {
        this.Fr = var1;
    }

    public void setGap(int var1) {
        this.Gap = var1;
    }

    public void setText(String var1) {
        this.Text = var1;
        this.invalidate();
        Container var2 = this.getParent();
        if (var2 != null) {
            var2.validate();
        }

        if (this.isShowing()) {
            this.repaint();
        }

    }

    public void setTitle(String var1) {
        this.Title = var1;
        if (this.isShowing()) {
            this.repaint();
        }

    }

    public void update(Graphics var1) {
        try {
            this.paint(var1);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }
}
