package paintchat.config;

import jaba.applet.Applet;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import paintchat.Res;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.HelpWindow;
import syi.awt.HelpWindowContent;
import syi.awt.LButton;
import syi.awt.LTextField;
import syi.util.PProperties;

public class ConfigApplet extends Applet implements MouseListener, FocusListener {
    Res res = Resource.loadResource("Config");
    private HelpWindow helpWindow = null;

    public void focusGained(FocusEvent var1) {
    }

    public void focusLost(FocusEvent var1) {
        this.getHelp().reset();
    }

    protected boolean getBool(String var1) {
        if (var1 != null && var1.length() > 0) {
            char var2 = Character.toLowerCase(var1.charAt(0));
            switch (var2) {
                case '1':
                case 'o':
                case 't':
                case 'y':
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    protected boolean getBool(String var1, boolean var2) {
        if (var1 != null && var1.length() > 0) {
            char var3 = Character.toLowerCase(var1.charAt(0));
            switch (var3) {
                case '0':
                case 'c':
                case 'f':
                case 'n':
                    return false;
                case '1':
                case 'o':
                case 't':
                case 'y':
                    return true;
                default:
                    return var2;
            }
        } else {
            return var2;
        }
    }

    protected HelpWindow getHelp() {
        if (this.helpWindow == null) {
            this.helpWindow = new HelpWindow((Frame) this.getParent().getParent());
            this.helpWindow.setIsShow(this.getBool(this.getParameter("App_IsConsole"), true));
        }

        return this.helpWindow;
    }

    public void getParameter(Component var1) {
        if (var1 instanceof Container) {
            Component[] var2 = ((Container) var1).getComponents();

            for (int var3 = 0; var3 < var2.length; ++var3) {
                this.getParameter(var2[var3]);
            }
        } else {
            if (!(var1 instanceof LTextField) && !(var1 instanceof Checkbox)) {
                return;
            }

            String var4 = this.getParameter(var1.getName());
            if (var4 == null) {
                return;
            }

            if (var1 instanceof LTextField) {
                ((LTextField) var1).setText(var4);
            }

            if (var1 instanceof Checkbox) {
                ((Checkbox) var1).setState(this.getBool(var4));
            }
        }

    }

    public void getResource(Res var1, Component var2) {
        if (var2 instanceof Container) {
            Container var3 = (Container) var2;
            int var4 = var3.getComponentCount();

            for (int var5 = 0; var5 < var4; ++var5) {
                this.getResource(var1, var3.getComponent(var5));
            }
        } else {
            String var6 = var2.getName();
            var6 = var1.get(var6, var6);
            if (var2 instanceof LTextField) {
                ((LTextField) var2).setTitle(var6);
                return;
            }

            if (var2 instanceof LButton) {
                ((LButton) var2).setText(var6);
                return;
            }

            if (var2 instanceof Checkbox) {
                ((Checkbox) var2).setLabel(var6);
                return;
            }

            if (var2 instanceof Label) {
                Label var7 = (Label) var2;
                var7.setText(var1.get(var7.getText(), var7.getText()));
                return;
            }
        }

    }

    public void mouseClicked(MouseEvent var1) {
    }

    public void mouseEntered(MouseEvent var1) {
        Component var2 = var1.getComponent();
        if (!(var2 instanceof Container)) {
            String var3 = var2.getName();
            if (var3 != null && var3.length() > 0) {
                Point var4 = var2.getLocationOnScreen();
                Point var5 = var1.getPoint();
                var4.translate(var5.x + 10, var5.y);
                this.getHelp().startHelp(new HelpWindowContent(var3, true, var4, this.res));
            }

        }
    }

    public void mouseExited(MouseEvent var1) {
        this.getHelp().reset();
    }

    public void mousePressed(MouseEvent var1) {
    }

    public void mouseReleased(MouseEvent var1) {
    }

    public void setMouseListener(Component var1, MouseListener var2) {
        if (var1 instanceof Container) {
            Component[] var3 = ((Container) var1).getComponents();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                this.setMouseListener(var3[var5], var2);
            }
        } else {
            var1.addMouseListener(var2);
        }

    }

    protected void setParam(Component var1, Hashtable var2) {
        if (var1 instanceof Container) {
            Component[] var3 = ((Container) var1).getComponents();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                this.setParam(var3[var5], var2);
            }
        } else {
            if (var1 instanceof LTextField) {
                var2.put(var1.getName(), ((LTextField) var1).getText());
                return;
            }

            if (var1 instanceof Checkbox) {
                var2.put(var1.getName(), String.valueOf(((Checkbox) var1).getState()));
                return;
            }

            if (var1 instanceof TextField) {
                var2.put(var1.getName(), ((TextField) var1).getText());
                return;
            }
        }

    }

    public void setParameter(Component var1) {
        PProperties var2 = ((ServerStub) this.getAppletContext()).getHashTable();
        this.setParam(this, var2);
    }
}
