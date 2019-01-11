package syi.awt;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class LPopup extends Component {
    private String[] strs = null;
    private int seek = 0;
    private ActionListener listener = null;
    private int minWidth = 10;

    public LPopup() {
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        this.setVisible(false);
    }

    public void add(String var1) {
        if (this.strs == null || this.seek >= this.strs.length) {
            String[] var2 = new String[(int) ((double) (this.seek + 1) * 1.5D)];
            if (this.strs != null) {
                for (int var3 = 0; var3 < this.strs.length; ++var3) {
                    var2[var3] = this.strs[var3];
                }
            }

            this.strs = var2;
        }

        this.strs[this.seek++] = var1;

        try {
            FontMetrics var5 = this.getFontMetrics(this.getFont());
            this.minWidth = Math.max(var5.stringWidth(var1), this.minWidth);
        } catch (RuntimeException var4) {
        }

    }

    public void add(String var1, int var2) {
    }

    public void addActionListener(ActionListener var1) {
    }

    public Dimension getPreferredSize() {
        try {
            FontMetrics var1 = this.getFontMetrics(this.getFont());
            return new Dimension(this.minWidth, var1.getMaxDescent() + var1.getMaxAscent());
        } catch (RuntimeException var2) {
            return new Dimension(10, 10);
        }
    }

    public void paint(Graphics var1) {
        try {
            Dimension var2 = this.getSize();
            var1.setColor(this.getForeground());
            var1.drawRect(0, 0, var2.width - 1, var2.height - 1);
            var1.setColor(Color.white);
            var1.fillRect(1, 1, var2.width - 2, var2.height - 2);
        } catch (Throwable ex) {
        }

    }

    protected void processMouseEvent(MouseEvent event) {
        try {
            int var2 = event.getID();
        } catch (Throwable ex) {
        }

    }

    public void removeAt(int var1) {
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
