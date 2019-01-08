package syi.awt;

import jaba.applet.Applet;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import static java.awt.event.MouseEvent.*;
import static java.awt.event.ComponentEvent.*;

import paintchat.Res;
import syi.javascript.JSController;

public class TextPanel extends Canvas implements ActionListener, ItemListener {
    public boolean isView = true;
    private boolean isPress = false;
    private Font font;
    private Object lock = new Object();
    private Applet applet;
    private boolean isSScroll = false;
    private boolean isGetFSize = false;
    private boolean isGetSize = false;
    private boolean isVisitScroll = true;
    private int H = 15;
    private int WS = 12;
    private int As = 0;
    private int Ds = 0;
    private int Gap = 1;
    private int scrollPos;
    private int scrollMax;
    private int iSeek = 0;
    private TextField textField;
    private Res config;
    private Color nowColor = null;
    private Color beColor = null;
    private String[] strings = null;
    private Color[] colors = null;
    private Graphics primary = null;
    private static PopupMenu popup = null;
    private int Y;
    private Dimension size = new Dimension();
    private static final String strEmpty = "";

    public TextPanel() {
        this.nowColor = Color.black;
    }

    public TextPanel(Applet var1, int var2, Color var3, Color var4, TextField var5) {
        this.init(var1, var2, var3, var4, var5);
    }

    public void actionPerformed(ActionEvent var1) {
        try {
            String var2 = var1.getActionCommand();
            if (var2 == null || var2.length() <= 0) {
                return;
            }

            PopupMenu var3 = popup;
            if (var3.getItem(0).getLabel().equals(var2)) {
                if ((var2 = this.getLine(this.Y)) != null) {
                    this.textField.setText(var2);
                }

                return;
            }

            int var6;
            String var9;
            if (var3.getItem(2).getLabel().equals(var2)) {
                var9 = this.getLine(this.Y);
                int var10 = var9.indexOf("http://");
                var6 = var9.indexOf(32, var10);
                var6 = var6 < 0 ? var9.length() : var6;
                this.applet.getAppletContext().showDocument(new URL(var9.substring(var10, var6)), "jump_url");
                return;
            }

            if (var3.getItem(3).getLabel().equals(var2)) {
                var9 = System.getProperty("line.separator");
                StringBuffer var5 = new StringBuffer();
                var5.append("<html><body>");
                var5.append(var9);

                for (var6 = 0; var6 < this.iSeek; ++var6) {
                    var2 = this.strings[var6];
                    if (var2 != null) {
                        var2 = Awt.replaceText(var2, "&lt;", "<");
                        var2 = Awt.replaceText(var2, "&gt;", ">");
                        var5.append(var2);
                        var5.append("<br>");
                        var5.append(var9);
                    }
                }

                var5.append("<div align=\"right\"> <a href=\"http://www.gt.sakura.ne.jp/~ocosama/\">shi-chan site</a></div>");
                var5.append("</body></html>");
                String var11 = "text_html";
                JSController var7 = new JSController(this.applet);
                var7.openWindow((String) null, var11, (Rectangle) null, true, false, true, true, false);
                var7.showDocument(var11, "text/html", var5.toString());
                return;
            }

            if (var3.getItem(7).getLabel().equals(var2)) {
                this.clear();
                this.repaint();
                return;
            }

            if (var2.charAt(0) == '+') {
                var2 = var2.substring(1);
            }

            int var4 = Math.min(Math.max(this.font.getSize() + Integer.parseInt(var2), 4), 256);
            this.setFont(new Font(this.font.getName(), this.font.getStyle(), var4));
            this.repaint();
        } catch (Throwable var8) {
            var8.printStackTrace();
        }

    }

    public void addText(String var1) {
        this.addText(var1, true);
    }

    public void addText(String var1, boolean var2) {
        Object var3 = this.lock;
        synchronized (this.lock) {
            if (this.iSeek > 0) {
                System.arraycopy(this.strings, 0, this.strings, 1, this.iSeek);
                System.arraycopy(this.colors, 0, this.colors, 1, this.iSeek);
            }
        }

        this.strings[0] = var1;
        this.colors[0] = this.nowColor;
        if (this.iSeek < this.strings.length - 2) {
            ++this.iSeek;
        }

        if (var2 && this.isGetFSize) {
            if (this.primary == null) {
                this.primary = this.getGraphics();
            }

            this.paint(this.primary);
        }

    }

    public void call(String var1) {
        try {
            JSController var2 = new JSController(this.applet);
            var2.runScript(var1);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public synchronized void clear() {
        for (int var1 = 0; var1 < this.strings.length; ++var1) {
            this.strings[var1] = null;
            this.colors[var1] = null;
        }

        this.iSeek = 0;
    }

    public void decode(String var1) {
        if (var1.length() > 0) {
            if (var1.charAt(0) == '$') {
                int var2 = 0;
                boolean var3 = false;
                if (var1.startsWith("$js:")) {
                    this.call(var1.substring(4, var1.length()));
                    return;
                }

                while (var2 < var1.length()) {
                    int var4 = var1.indexOf(59, var2);
                    if (var4 < 0) {
                        var4 = var1.length();
                    }

                    this.decode(var1, var2, var4);
                    var2 = var4 + 1;
                }
            } else {
                this.decode(var1, 0, var1.length());
            }

        }
    }

    private void decode(String var1, int var2, int var3) {
        if (var3 - var2 > 0) {
            try {
                if (var1.charAt(var2) != '$') {
                    this.addText(var1.substring(var2, var3));
                    return;
                }

                int var4 = var1.indexOf(58, var2);
                String var5 = "";
                String var6 = "";
                if (var4 > var2 && var4 <= var3) {
                    var5 = var1.substring(var2, var4);
                } else {
                    var5 = var1;
                }

                if (var4 >= 0 && var4 < var3 - 1) {
                    var6 = var1.substring(var4 + 1, var3);
                }

                int var8;
                if (var5.indexOf("$clear") >= 0) {
                    Object var7 = this.lock;
                    synchronized (this.lock) {
                        for (var8 = 0; var8 < this.strings.length; ++var8) {
                            this.strings[var8] = null;
                        }
                    }
                } else if (var5.indexOf("$bkcolor") >= 0) {
                    this.setBackground(new Color(Res.parseInt(var6)));
                } else if (var5.indexOf("$color") >= 0) {
                    this.setForeground(var6.charAt(0) == '/' ? this.beColor : new Color(Res.parseInt(var6)));
                } else {
                    Font var11;
                    if (var5.indexOf("$font_size") >= 0) {
                        var11 = this.getFont();
                        var4 = Res.parseInt(var6);
                        this.setFont(new Font(var11.getName(), 0, var11.getSize() + var4));
                        this.repaint();
                    } else if (var5.indexOf("$font") >= 0) {
                        var11 = this.getFont();
                        var8 = var6.equals("bold") ? 1 : 0;
                        this.setFont(new Font(var11.getName(), var8, var11.getSize()));
                        this.repaint();
                    } else if (var5.indexOf("$js") >= 0) {
                        this.call(var6);
                    } else {
                        this.addText(var1.substring(var2, var3));
                    }
                }
            } catch (RuntimeException var10) {
                ;
            }

        }
    }

    public String getLine(int var1) {
        int var2 = (this.scrollPos + var1) / (this.H + this.Gap * 2);
        return this.strings[var2 <= 0 ? 0 : (var2 >= this.iSeek ? this.iSeek - 1 : var2)];
    }

    public Dimension getPreferredSize() {
        Dimension var1 = this.getToolkit().getScreenSize();
        int var2 = this.Gap * 2;
        Font var3 = this.getFont();
        FontMetrics var4 = null;
        if (var3 != null) {
            var4 = this.getFontMetrics(var3);
        }

        if (var4 == null) {
            var1.setSize(300, 120);
            return var1;
        } else {
            this.H = var4.getMaxAscent() + var4.getMaxDescent() + 1;
            int var5 = 0;
            if (this.strings != null) {
                for (int var6 = 0; var6 < this.iSeek; ++var6) {
                    if (this.strings[var6] != null) {
                        var5 = Math.max(var4.stringWidth(this.strings[var6]) + var2, var5);
                    }
                }
            }

            var5 += var2 + this.WS;
            var1.setSize(var5 <= 100 ? 100 : (var5 >= var1.width / 2 ? var1.width / 2 : var5), (this.H + var2) * (this.iSeek <= 3 ? 3 : (this.iSeek >= 12 ? 12 : this.iSeek)));
            return var1;
        }
    }

    public Dimension getSize() {
        if (this.size.width + this.size.height == 0 || !this.isGetSize) {
            this.size.setSize(super.getSize());
        }

        return this.size;
    }

    public void init(Applet var1, int var2, Color var3, Color var4, TextField var5) {
        this.enableEvents(COMPONENT_EVENT_MASK | MOUSE_EVENT_MASK | MOUSE_MOTION_EVENT_MASK);
        this.textField = var5;
        this.applet = var1;
        this.setBackground(var3);
        this.setForeground(var4);
        this.nowColor = this.beColor = var4;
        this.setMaxLabel(var2);
    }

    public void itemStateChanged(ItemEvent var1) {
        try {
            this.isSScroll = var1.getStateChange() == 1;
            this.scrollPos = 0;
            this.paint(this.primary);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void paint(Graphics var1) {
        try {
            Dimension var2 = this.getSize();
            if (!this.isView) {
                var1.setColor(this.getBackground());
                var1.fillRect(0, 0, var2.width, var2.height);
                return;
            }

            if (this.strings == null) {
                return;
            }

            if (var1 == null) {
                return;
            }

            int var3 = this.Gap * 2;
            int var10000 = this.strings.length;
            if (!this.isGetFSize) {
                this.isGetFSize = true;
                if (this.font == null) {
                    this.font = super.getFont();
                }

                FontMetrics var5 = var1.getFontMetrics();
                this.As = var5.getMaxAscent();
                this.Ds = var5.getMaxDescent();
                this.H = this.As + this.Ds;
                this.scrollPos = 0;
            }

            var1.setFont(this.font);
            int var4 = this.H + var3;
            this.scrollMax = var4 * this.iSeek;
            if (var2.height <= 0 || var2.width <= 0) {
                return;
            }

            int var14 = this.WS;
            int var6 = var2.width - 2 - var14;
            int var7 = Math.max(this.scrollPos / var4, 0);
            int var8 = var7 + var2.height / var4 + 2;
            int var9 = var7 * var4 - this.scrollPos;
            var1.setClip(1, 1, var6, var2.height - 2);
            Color var10 = this.getBackground();
            var1.setColor(var10);

            int var11;
            for (var11 = var7; var11 < var8; ++var11) {
                if (var11 < this.strings.length && this.strings[var11] != null) {
                    var1.fillRect(1, var9, var6, this.H + var3);
                    var1.setColor(this.colors[var11]);
                    var1.drawString((String) this.strings[var11], 1, var9 + this.Gap + this.H - this.Ds);
                    var1.setColor(var10);
                } else {
                    var1.fillRect(1, var9, var6, this.H + var3);
                }

                var9 += var4;
            }

            if (var9 > 0) {
                var1.setColor(Color.black);
                var1.fillRect(1, var9, var6 - 1, var2.height - var9 - 1);
            }

            var1.setClip(0, 0, var2.width, var2.height);
            if (this.isVisitScroll) {
                var11 = var2.height / (this.H + var3);
                int var12 = (int) ((float) var2.height * ((float) var11 / (float) (this.iSeek + var11)));
                var9 = (int) ((float) this.scrollPos / (float) this.scrollMax * (float) (var2.height - var12 - 1));
                ++var6;
                --var14;
                var1.setColor(this.getForeground());
                var1.fillRect(var6, 1, 1, var2.height - 2);
                var1.fillRect(var6 + 1, var9, var14, var12);
                var1.setColor(var10);
                var1.setColor(this.getBackground());
                var1.fillRect(var6 + 1, 1, var14, var9);
                var1.fillRect(var6 + 1, var9 + var12 + 1, var14, var2.height - var12 - var9 - 1);
                var1.setColor(this.getForeground());
                var1.drawRect(0, 0, var2.width - 1, var2.height - 1);
            }
        } catch (Throwable var13) {
            var13.printStackTrace();
        }

    }

    private void popup(int var1, int var2) {
        if (popup == null) {
            Menu var3 = new Menu("Font size");
            var3.addActionListener(this);

            for (int var5 = 6; var5 > -6; --var5) {
                if (var5 != 0) {
                    String var4 = String.valueOf(var5);
                    if (var5 > 0) {
                        var4 = '+' + var4;
                    }

                    var3.add(var4);
                }
            }

            popup = new PopupMenu();
            popup.add("CopyString");
            popup.addSeparator();
            popup.add("GotoURL");
            popup.add("ToHTML");
            popup.addSeparator();
            popup.add((MenuItem) var3);
            popup.addSeparator();
            popup.add("Erase");
            CheckboxMenuItem var6 = new CheckboxMenuItem("Smooth scroll", this.isSScroll);
            var6.addItemListener(this);
            popup.add((MenuItem) var6);
        }

        this.add(popup);
        popup.addActionListener(this);
        popup.getItem(2).setEnabled(isStringUrl(this.getLine(var2)));
        popup.show(this, var1, var2);
    }

    private boolean isStringUrl(String str) {
        str = str.substring(str.indexOf('>')+1);
        try {
            new URL(str);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    protected void processEvent(AWTEvent var1) {
        try {
            if (var1 instanceof MouseEvent) {
                MouseEvent var2 = (MouseEvent) var1;
                int var3 = var2.getX();
                int var4 = var2.getY();
                switch (var1.getID()) {
                    case MOUSE_PRESSED:
                        this.Y = var4;
                        this.isPress = !Awt.isR(var2);
                        if (!this.isPress) {
                            this.popup(var3, var4);
                        }
                        break;
                    case MOUSE_RELEASED:
                        this.isPress = false;
                    case MOUSE_MOVED:
                    case MOUSE_ENTERED:
                    case MOUSE_EXITED:
                    default:
                        break;
                    case MOUSE_DRAGGED:
                        if (this.isPress && this.Y != var4) {
                            int var5 = -(this.Y - var4);
                            if (!this.isSScroll) {
                                var5 *= this.H + this.Gap * 2;
                            }

                            this.scrollPos = Math.max(Math.min(this.scrollPos + var5, this.scrollMax), 0);
                            this.Y = var4;
                            if (this.primary == null) {
                                this.primary = this.getGraphics();
                            }

                            this.paint(this.primary);
                        }
                }

                return;
            }

            if (var1 instanceof ComponentEvent) {
                switch (var1.getID()) {
                    case 101:
                    case 102:
                        this.isGetSize = false;
                        if (this.primary != null) {
                            this.primary.dispose();
                            this.primary = null;
                        }

                        if (this.isGetFSize) {
                            this.repaint();
                        }
                    default:
                        return;
                }
            }

            super.processEvent(var1);
        } catch (Throwable var6) {
            var6.printStackTrace();
        }

    }

    public void remove(String var1) {
        try {
            Object var2 = this.lock;
            synchronized (this.lock) {
                int var3 = this.iSeek;

                for (int var4 = 0; var4 < var3; ++var4) {
                    String var5;
                    if ((var5 = this.strings[var4]) != null && var5.equals(var1)) {
                        if (var4 != var3 - 1) {
                            System.arraycopy(this.strings, var4 + 1, this.strings, var4, var3 - var4 - 1);
                        }

                        this.strings[var3 - 1] = null;
                        --this.iSeek;
                        break;
                    }
                }
            }

            this.repaint();
        } catch (RuntimeException var7) {
            var7.printStackTrace();
        }

    }

    public void setFont(Font var1) {
        this.font = var1;
        this.isGetFSize = false;
    }

    public void setForeground(Color var1) {
        this.beColor = this.nowColor;
        this.nowColor = var1;
        super.setForeground(var1);
    }

    public void setMaxLabel(int var1) {
        if (var1 > 0) {
            String[] var2 = new String[var1];
            Color[] var3 = new Color[var1];
            this.scrollPos = 0;
            if (this.strings != null) {
                System.arraycopy(this.strings, 0, var2, 0, this.strings.length);
            }

            if (this.colors != null) {
                System.arraycopy(this.colors, 0, var3, 0, this.colors.length);
            }

            this.strings = var2;
            this.colors = var3;
        }
    }

    public void setRText(String var1) {
        StringBuffer var3 = new StringBuffer();

        try {
            BufferedReader var4 = new BufferedReader(new StringReader(var1));

            String var2;
            while ((var2 = var4.readLine()) != null) {
                var3.insert(0, (String) var2);
                var3.insert(0, (char) '\n');
            }

            var4.close();
        } catch (IOException var5) {
            ;
        }

        this.setText(var3.toString());
    }

    public void setText(String var1) {
        int var3 = var1.length();
        CharArrayWriter var4 = new CharArrayWriter();

        for (int var5 = 0; var5 < var3; ++var5) {
            char var2 = var1.charAt(var5);
            if (var2 != '\r' && var2 != '\n') {
                var4.write(var2);
            } else if (var4.size() > 0) {
                this.decode(var4.toString());
                var4.reset();
            }
        }

        if (var4.size() > 0) {
            this.decode(var4.toString());
        }

    }

    public void setVisitScroll(boolean var1) {
        this.isVisitScroll = var1;
        this.WS = var1 ? 12 : 0;
    }

    public void update(Graphics var1) {
        this.paint(var1);
    }
}
