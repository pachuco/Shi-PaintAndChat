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

    public LButton(String text) {
        this.size = null;
        this.BackImage = null;
        this.Text = null;
        this.listener = null;
        this.Gap = 3;
        this.textWidth = -1;
        this.isPress = false;
        this.dkBackColor = Color.darkGray;
        this.enableEvents(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        this.setBackground(new Color(0xCFCFCF));
        this.setText(text);
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
        int txtWidth = 50;
        int txtHeight = 10;
        if (this.BackImage != null) {
            txtWidth = this.BackImage.getWidth((ImageObserver) null);
            txtHeight = this.BackImage.getHeight((ImageObserver) null);
        } else {
            Font font = this.getFont();
            int var4 = this.Gap * 2;
            if (font != null) {
                FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
                if (fontMetrics != null) {
                    txtHeight = fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
                }

                if (this.Text != null) {
                    txtWidth = fontMetrics.stringWidth(this.Text) + var4;
                }
            }

            txtHeight += var4;
        }

        return new Dimension(txtWidth, txtHeight);
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

    public void paint(Graphics g) {
        try {
            Dimension var2 = this.getSize();
            if (this.BackImage != null) {
                Awt.drawFrame(g, this.isPress, 0, 0, var2.width, var2.height);
                g.drawImage(this.BackImage, 1, 1, (ImageObserver) null);
            } else {
                Awt.fillFrame(g, this.isPress, 0, 0, var2.width, var2.height);
            }

            if (this.Text == null) {
                return;
            }

            FontMetrics fontMetrics = g.getFontMetrics();
            if (this.textWidth == -1) {
                this.textWidth = fontMetrics.stringWidth(this.Text);
            }

            g.setColor(this.getForeground());
            g.drawString(this.Text, (this.size.width - this.textWidth) / 2, fontMetrics.getMaxAscent() + this.Gap + 1);
        } catch (Throwable ex) {
            ;
        }

    }

    protected void processEvent(AWTEvent awtEvent) {
        try {
            int id = awtEvent.getID();
            if (awtEvent instanceof MouseEvent) {
                MouseEvent mouseEvent = (MouseEvent) awtEvent;
                mouseEvent.consume();
                if (id == MouseEvent.MOUSE_PRESSED) {
                    this.isPress = true;
                    this.repaint();
                }

                if (id == MouseEvent.MOUSE_RELEASED) {
                    if (this.contains(((MouseEvent) awtEvent).getPoint())) {
                        this.doAction();
                    }

                    this.isPress = false;
                    this.repaint();
                }

                return;
            }

            if (awtEvent instanceof ComponentEvent) {
                if (id == ComponentEvent.COMPONENT_RESIZED || id == ComponentEvent.COMPONENT_SHOWN) {
                    this.size = null;
                    this.repaint();
                }

                return;
            }

            super.processEvent(awtEvent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void resetSize() {
        super.setSize(this.getPreferredSize());
    }

    public void setBackground(Color color) {
        if (color != null) {
            this.dkBackColor = color.darker();
            super.setBackground(color);
        }
    }

    public void setBackImage(Image img) {
        this.BackImage = img;
        if (this.isShowing()) {
            this.repaint();
        }

    }

    public void setGap(int gap) {
        this.Gap = gap;
        this.resetSize();
    }

    public void setText(String text) {
        this.Text = text;
        this.textWidth = -1;
        this.invalidate();
        Component parent = Awt.getParent(this);
        if (parent != null) {
            parent.validate();
        }

        if (this.isShowing()) {
            this.repaint();
        }

    }

    public void update(Graphics g) {
        this.paint(g);
    }
}
