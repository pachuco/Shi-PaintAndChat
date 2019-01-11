package syi.awt;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.CharArrayWriter;

public class TextCanvas extends Canvas {
    String[] strs = null;
    int seek = 0;
    CharArrayWriter buffer = new CharArrayWriter();
    Dimension d = new Dimension();
    private int Gap = 3;
    public boolean isBorder = true;

    public TextCanvas() {
    }

    public TextCanvas(String var1) {
        this.setText(var1);
    }

    public synchronized void addText(String var1) {
        if (this.strs == null) {
            this.strs = new String[6];
        } else if (this.seek >= this.strs.length) {
            String[] var2 = new String[this.strs.length * 2];
            System.arraycopy(this.strs, 0, var2, 0, this.strs.length);
            this.strs = var2;
        }

        this.strs[this.seek++] = var1;
    }

    public Dimension getPreferredSize() {
        if (this.strs != null && this.seek != 0) {
            try {
                FontMetrics var1 = this.getFontMetrics(this.getFont());
                int var2 = 0;
                int var3 = this.Gap * 2;

                for (int var4 = 0; var4 < this.seek; ++var4) {
                    var2 = Math.max(var2, var1.stringWidth(this.strs[var4]));
                }

                return new Dimension(var2 + var3 + 4, (var1.getMaxDescent() + var1.getMaxAscent() + this.Gap) * this.seek + 4);
            } catch (RuntimeException var5) {
                return new Dimension(50, 10);
            }
        } else {
            return new Dimension(50, 10);
        }
    }

    public void paint(Graphics var1) {
        try {
            Dimension var2 = this.getSize();
            var1.clearRect(1, 1, var2.width - 2, var2.height - 2);
            if (this.isBorder) {
                var1.drawRect(0, 0, var2.width - 1, var2.height - 1);
            }

            FontMetrics var3 = var1.getFontMetrics();
            int var4 = var3.getMaxAscent() + var3.getMaxDescent() + this.Gap;
            int var5 = var3.getMaxAscent();

            for (int var6 = 0; var6 < this.seek; ++var6) {
                var1.drawString(this.strs[var6], this.Gap + 2, var4 * var6 + var5 + this.Gap + 2);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void reset() {
        if (this.strs != null) {
            for (int var1 = 0; var1 < this.seek; ++var1) {
                this.strs[var1] = null;
            }

            this.seek = 0;
        }
    }

    public final void setText(String var1) {
        if (var1 != null) {
            CharArrayWriter var2 = this.buffer;
            synchronized (var2) {
                var2.reset();
                this.reset();
                int var4 = var1.length();
                int var6 = 0;

                while (true) {
                    if (var6 >= var4) {
                        if (var2.size() > 0) {
                            this.addText(var2.toString());
                        }
                        break;
                    }

                    char var5 = var1.charAt(var6);
                    if (var5 != '\r' && var5 != '\n') {
                        var2.write(var5);
                    } else if (var2.size() > 0) {
                        this.addText(var2.toString());
                        var2.reset();
                    }

                    ++var6;
                }
            }

            this.setSize(this.getPreferredSize());
        }
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
