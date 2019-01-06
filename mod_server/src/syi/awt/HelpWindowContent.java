package syi.awt;

import java.awt.Image;
import java.awt.Point;
import java.util.Hashtable;

public class HelpWindowContent {
    public Point point;
    public String string = null;
    public Image image = null;
    boolean isResource = false;
    public Hashtable res;
    public int timeStart = 2000;
    public int timeEnd = 15000;
    private boolean isEnableVisited = false;
    private boolean isVisit = false;

    public HelpWindowContent(Image var1, String var2, boolean var3, Point var4, Hashtable var5) {
        this.point = var4;
        this.string = var2;
        this.res = var5;
        this.isResource = var3;
        this.image = var1;
    }

    public HelpWindowContent(String var1, boolean var2, Point var3, Hashtable var4) {
        this.point = var3;
        this.string = var1;
        this.res = var4;
        this.isResource = var2;
    }

    public String getText() {
        if (this.string != null && this.string.length() != 0) {
            if (this.isResource) {
                String var1 = (String) this.res.get(this.string + "_Com");
                return var1 == null ? this.res.get(this.string).toString() : var1;
            } else {
                return this.string;
            }
        } else {
            return "";
        }
    }

    public boolean isVisible(boolean var1) {
        return !this.isEnableVisited ? var1 : this.isVisit;
    }

    public void setText(String var1, boolean var2) {
        this.string = var1;
        this.isResource = var2;
    }

    public void setVisit(boolean var1) {
        this.isEnableVisited = true;
        this.isVisit = var1;
    }
}
