package syi.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import syi.util.Io;

public class ImageCanvas extends Canvas {
    private Image image = null;
    private String string = null;
    private int imW = 0;
    private int imH = 0;

    public ImageCanvas(Color var1, Color var2) {
        this.setBackground(var1);
    }

    public Dimension getPreferredSize() {
        return new Dimension(this.imW + 2, this.imH + 2);
    }

    public void paint(Graphics var1) {
        try {
            Dimension var2 = this.getSize();
            var1.drawRect(0, 0, var2.width - 1, var2.height - 1);
            if (this.image != null) {
                var1.drawImage(this.image, 1, 1, (ImageObserver) null);
            } else {
                var1.clearRect(1, 1, var2.width - 2, var2.height - 2);
            }

            if (this.string != null) {
                FontMetrics var3 = var1.getFontMetrics();
                var1.drawString((String) this.string, 10, var3.getMaxAscent() + 10);
            }
        } catch (RuntimeException var4) {
        }

    }

    public synchronized void reset() {
        if (this.image != null) {
            this.image.flush();
            this.image = null;
            this.imW = 0;
            this.imH = 0;
        }

        this.string = null;
    }

    public void setImage(Image var1) {
        this.image = var1;
        this.imW = var1.getWidth((ImageObserver) null);
        this.imH = var1.getHeight((ImageObserver) null);
        this.setSize(this.imW + 2, this.imH + 2);
    }

    public void setImage(String var1) {
        if (var1 != null && var1.length() > 0) {
            this.setImage(Io.loadImageNow(this, var1));
        }
    }

    public void setText(String var1) {
        this.string = var1;
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
