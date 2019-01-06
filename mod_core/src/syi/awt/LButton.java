package syi.awt;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import static java.awt.event.ComponentEvent.*;
import static java.awt.event.MouseEvent.*;

public class LButton extends Canvas {
    private Dimension size;
    private Image BackImage;
    private String Text;
    private ActionListener listener;
    int Gap;
    int textWidth;
    boolean isPress;
    Color dkBackColor;

    public LButton() {
        this((String) null);
    }

    public LButton(String var1) {
        this.size = null;
        this.BackImage = null;
        this.Text = null;
        this.listener = null;
        this.Gap = 3;
        this.textWidth = -1;
        this.isPress = false;
        this.dkBackColor = Color.darkGray;
        this.enableEvents(17L);
        this.setBackground(new Color(13619151));
        this.setText(var1);
    }

    public void addActionListener(ActionListener var1) {
        this.listener = var1;
    }

    private void doAction() {
        if (this.listener != null) {
            this.listener.actionPerformed(new ActionEvent(this, 1001, this.getText()));
        }

    }

    public Image getBackImage() {
        return this.BackImage;
    }

    public int getGap() {
        return this.Gap;
    }

    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    public Dimension getPreferredSize() {
        int var1 = 50;
        int var2 = 10;
        if (this.BackImage != null) {
            var1 = this.BackImage.getWidth((ImageObserver) null);
            var2 = this.BackImage.getHeight((ImageObserver) null);
        } else {
            Font var3 = this.getFont();
            int var4 = this.Gap * 2;
            if (var3 != null) {
                FontMetrics var5 = this.getFontMetrics(this.getFont());
                if (var5 != null) {
                    var2 = var5.getMaxAscent() + var5.getMaxDescent();
                }

                if (this.Text != null) {
                    var1 = var5.stringWidth(this.Text) + var4;
                }
            }

            var2 += var4;
        }

        return new Dimension(var1, var2);
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

    public void paint(Graphics var1) {
        try {
            Dimension var2 = this.getSize();
            if (this.BackImage != null) {
                Awt.drawFrame(var1, this.isPress, 0, 0, var2.width, var2.height);
                var1.drawImage(this.BackImage, 1, 1, (ImageObserver) null);
            } else {
                Awt.fillFrame(var1, this.isPress, 0, 0, var2.width, var2.height);
            }

            if (this.Text == null) {
                return;
            }

            FontMetrics var3 = var1.getFontMetrics();
            if (this.textWidth == -1) {
                this.textWidth = var3.stringWidth(this.Text);
            }

            var1.setColor(this.getForeground());
            var1.drawString(this.Text, (this.size.width - this.textWidth) / 2, var3.getMaxAscent() + this.Gap + 1);
        } catch (Throwable var4) {
            ;
        }

    }

    protected void processEvent(AWTEvent ev) {
        try {
            int id = ev.getID();
            if (ev instanceof MouseEvent) {
                MouseEvent mev = (MouseEvent) ev;
                mev.consume();
                if (id == MOUSE_PRESSED) {
                    this.isPress = true;
                    this.repaint();
                }

                if (id == MOUSE_RELEASED) {
                    if (this.contains(((MouseEvent) ev).getPoint())) {
                        this.doAction();
                    }

                    this.isPress = false;
                    this.repaint();
                }

                return;
            }

            if (ev instanceof ComponentEvent) {
                if (id == COMPONENT_RESIZED || id == COMPONENT_SHOWN) {
                    this.size = null;
                    this.repaint();
                }

                return;
            }

            super.processEvent(ev);
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    private void resetSize() {
        super.setSize(this.getPreferredSize());
    }

    public void setBackground(Color var1) {
        if (var1 != null) {
            this.dkBackColor = var1.darker();
            super.setBackground(var1);
        }
    }

    public void setBackImage(Image var1) {
        this.BackImage = var1;
        if (this.isShowing()) {
            this.repaint();
        }

    }

    public void setGap(int var1) {
        this.Gap = var1;
        this.resetSize();
    }

    public void setText(String var1) {
        this.Text = var1;
        this.textWidth = -1;
        this.invalidate();
        Component var2 = Awt.getParent(this);
        if (var2 != null) {
            var2.validate();
        }

        if (this.isShowing()) {
            this.repaint();
        }

    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
