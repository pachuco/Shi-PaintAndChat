package paintchat_client;

import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;

import paintchat.LO;
import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;
import syi.awt.Awt;
import syi.awt.LComponent;

/** Layer palette window */
public class L extends LComponent implements ActionListener, ItemListener {
    private Mi mi;
    private ToolBox tool;
    private Res res;
    private M m;
    private int B = -1;
    private Font bFont;
    private int bH;
    private int bW;
    private int base;
    private int layer_size = -1;
    private int mouse = -1;
    private boolean isASlide = false;
    private int Y;
    private int YOFF;
    private PopupMenu popup = null;
    private String strMenu;
    private boolean is_pre = true;
    private boolean is_DIm = false;
    private Color cM;
    private Color cT;
    private String sL;

    public L(Mi mi, ToolBox tool, Res res, Res cnf) {
        this.tool = tool;
        this.bFont = Awt.getDefFont();
        this.bFont = new Font(this.bFont.getName(), 0, (int) ((float) this.bFont.getSize() * 0.8F));
        FontMetrics fontMetrics = this.getFontMetrics(this.bFont);
        this.bH = fontMetrics.getHeight() + 6;
        this.base = this.bH - 2 - fontMetrics.getMaxDescent();
        int maxFontSize = (int) (60.0F * LComponent.Q);
        String layerName = res.res("Layer");
        this.sL = layerName;
        this.strMenu = res.res("MenuLayer");
        this.cM = new Color(cnf.getP("l_m_color", 0));
        this.cT = new Color(cnf.getP("l_m_color_text", 0xFFFFFF));
        fontMetrics = this.getFontMetrics(this.bFont);
        maxFontSize = Math.max(fontMetrics.stringWidth(layerName + "00") + 4, maxFontSize);
        maxFontSize = Math.max(fontMetrics.stringWidth(this.strMenu) + 4, maxFontSize);
        this.bW = maxFontSize;
        this.mi = mi;
        this.res = res;
        this.setTitle(layerName);
        super.isGUI = true;
        this.m = mi.info.m;
        Dimension dim = new Dimension(this.bW, this.bH);
        this.setDimension(new Dimension(dim), dim, new Dimension());
        this.setSize(this.getMaximumSize());
    }

    public void actionPerformed(ActionEvent event) {
        try {
            String actionCommand = event.getActionCommand();
            int popupItemCount = this.popup.getItemCount();

            int selectedPopupIdx;
            for (selectedPopupIdx = 0; selectedPopupIdx < popupItemCount && !this.popup.getItem(selectedPopupIdx).getLabel().equals(actionCommand); ++selectedPopupIdx) {
                ;
            }

            M.Info info = this.mi.info;
            M mg = this.mg();
            this.setA(mg);
            LO[] layers = info.layers;
            int layerCount = info.L;
            byte[] var9 = new byte[4];
            boolean wasUpdated = false;
            boolean wasLayerAdded = false;
            int userWait = this.mi.user.wait;
            this.mi.user.wait = -2;

            if (this.popup.getName().charAt(0) == 'm') {
                // clicked a voice from the menu popup
                switch (selectedPopupIdx) {
                    case 0: // add new layer
                        mg.setRetouch(new int[]{1, layerCount + 1}, (byte[]) null, 0, false);
                        wasUpdated = true;
                        wasLayerAdded = true;
                        break;
                    case 1: // delete layer
                        if (info.L > 1 && this.confirm(layers[mg.iLayer].name + this.res.res("DelLayerQ"))) {
                            mg.iLayerSrc = mg.iLayer;
                            mg.setRetouch(new int[]{2}, (byte[]) null, 0, false);
                            wasUpdated = true;
                            break;
                        }

                        return;
                    case 2: // merge visible layers
                        this.dFusion();
                        break;
                    case 3: // open layer property window
                        this.config(this.m.iLayer);
                }
            } else if (selectedPopupIdx == 0) {
                mg.iHint = M.H_L;
                mg.setRetouch(new int[]{3}, (byte[]) null, 0, false);
                wasUpdated = true;
            } else {
                byte blendMode = (byte) layers[mg.iLayerSrc].iCopy;
                if (blendMode == 1) {
                    this.dFusion();
                } else {
                    mg.iHint = M.H_RECT;
                    mg.iPen = M.P_FUSION;
                    var9[0] = blendMode;
                    mg.setRetouch(new int[]{0, info.W << 16 | info.H}, var9, 4, false);
                    wasUpdated = true;
                }
            }

            if (wasUpdated) {
                mg.draw();
                if (wasLayerAdded) {
                    info.layers[info.L - 1].makeName(this.sL);
                }

                this.mi.send(mg);
            }

            this.m.iLayerSrc = this.m.iLayer = Math.min(this.m.iLayer, info.L - 1);
            this.repaint();
            this.mi.user.wait = userWait;
            this.mi.repaint();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    /** Returns the index of the layer under the mouse */
    private int b(int y) {
        return y < this.bH ? 0 : Math.max(this.mi.info.L - (y / this.bH - 1), 1);
    }

    private void send(int[] var1, byte[] var2) {
        M mg = this.mg();
        M.Info info = this.mi.info;
        this.setA(mg);
        mg.setRetouch(var1, var2, var2 != null ? var2.length : 0, false);
        int userWait = this.mi.user.wait;

        try {
            mg.draw();
            //MAGIC: layer alpha and mix mode are local if layer editing is disabled
            if (info.isLEdit) this.mi.send(mg);
        } catch (Throwable ex) {
            ;
        }

        this.repaint();
        this.mi.user.wait = userWait;
        this.mi.repaint();
    }

    /** Merge visible layers */
    private void dFusion() {
        if (this.confirm(this.res.res("CombineVQ"))) {
            try {
                int layerCount = this.mi.info.L;
                LO[] layers = this.mi.info.layers;
                int visibleCount = 0;


                for (int i = 0; i < layerCount; ++i) {
                    if (layers[i].iAlpha > 0.0F) {
                        ++visibleCount;
                    }
                }

                if (visibleCount <= 0) {
                    return; // nothing to merge
                }

                int userWait = this.mi.user.wait;
                M mg = this.mg();
                this.setA(mg);
                byte[] buffer = new byte[visibleCount * 4 + 2];
                int offset = 2;
                buffer[0] = (byte) visibleCount;

                for (int i = 0; i < layerCount; ++i) {
                    LO layer = layers[i];
                    if (layer.iAlpha > 0.0F) {
                        buffer[offset++] = (byte) i;
                        buffer[offset++] = (byte) ((int) (layer.iAlpha * 255.0F));
                        buffer[offset++] = (byte) layer.iCopy;
                        buffer[offset++] = 41;
                    }
                }

                this.mi.user.wait = -2;
                mg.setRetouch(new int[]{7}, buffer, buffer.length, false);
                mg.draw();
                this.mi.send(mg);
                this.mi.user.wait = userWait;
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

        }
    }

    private boolean confirm(String text) {
        return Me.confirm(text, true);
    }

    /** Draws layer items */
    private void dL(Graphics g, int y, int layerIndex) {
        if (this.mi.info.L > layerIndex) {
            this.getSize();
            int borderWidth = this.bW - 1;
            int borderHeight = this.bH - 2;
            Color borderColor = this.m.iLayer == layerIndex ? Awt.cFSel : super.clFrame;
            LO layer = this.mi.info.layers[layerIndex];

            // layer name border
            g.setColor(borderColor);
            g.drawRect(0, y, borderWidth, borderHeight);

            // layer name
            g.setColor(Awt.cFore);
            g.setFont(this.bFont);
            g.drawString((String) layer.name, 2, y + this.base);

            // slider alpha border
            g.setColor(borderColor);
            g.drawRect(this.bW, y, 100, borderHeight);

            // slider alpha rect
            g.setColor(this.cM);
            int alpha100 = (int) (100.0F * layer.iAlpha);
            g.fillRect(this.bW + 1, y + 1, alpha100 - 1, borderHeight - 1);

            // slider alpha percentage
            g.setColor(this.cT);
            g.drawString(alpha100 + "%", this.bW + 3, y + this.base);

            // layer show/hide border
            int sliderX = this.bW + 100;
            g.setColor(borderColor);
            g.drawRect(sliderX + 1, y, borderHeight - 2, borderHeight);

            g.setColor(Awt.cFore);
            if (alpha100 == 0) {
                // diagonal lines over name a show button
                g.drawLine(sliderX + 2, y + 1, sliderX + borderHeight - 2, y + borderHeight - 1);
                g.drawLine(1, y + 1, borderWidth - 1, y + this.bH - 3);
            } else {
                // circle show/hide button
                g.drawOval(sliderX + 2, y + 2, borderHeight - 3, borderHeight - 3);
            }

        }
    }

    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        if (this.mi != null) {
            size.setSize(this.bW + 100 + this.bH, this.bH * (this.mi.info.L + 1));
        }

        return size;
    }

    public void itemStateChanged(ItemEvent event) {
        this.is_pre = !this.is_pre;
    }

    private M mg() {
        M var1 = new M(this.mi.info, this.mi.user);
        var1.iAlpha = 255;
        var1.iHint = M.H_L;
        var1.iLayer = this.m.iLayer;
        var1.iLayerSrc = this.m.iLayerSrc;
        return var1;
    }

    private void p() {
        this.repaint();
        this.tool.up();
    }

    public void paint2(Graphics g) {
        try {
            int layerCount = this.mi.info.L;

            for (int i = 0; i < layerCount; ++i) {
                LO layer = this.mi.info.layers[i];
                if (layer.name == null) {
                    layer.makeName(this.sL);
                }
            }

            if (this.layer_size != layerCount) {
                this.layer_size = layerCount;
                this.setSize(this.getMaximumSize());
                return;
            }

            Dimension windowSize = this.getSize();
            int lastIndex = layerCount - 1;
            int offY = this.bH;
            g.setFont(this.bFont);
            g.setColor(Awt.cBk);
            g.fillRect(0, 0, windowSize.width, windowSize.height);

            while (offY < windowSize.height) {
                if (this.isASlide || lastIndex != this.mouse - 1) {
                    this.dL(g, offY, lastIndex);
                }

                --lastIndex;
                if (lastIndex < 0) {
                    break;
                }

                offY += this.bH;
            }

            if (!this.isASlide && this.mouse > 0) {
                this.dL(g, this.Y - this.YOFF, this.mouse - 1);
            }

            Awt.drawFrame(g, this.mouse == 0, 0, 0, this.bW, this.bH - 2);
            g.setColor(Awt.cFore);
            g.drawString((String) this.strMenu, 3, this.bH - 6);
        } catch (Throwable ex) {
            ;
        }

    }

    public void pMouse(MouseEvent event) {
        try {
            int mouseY = this.Y = event.getY();
            int mouseX = event.getX();
            M.Info info = this.mi.info;
            boolean isModifier = Awt.isR(event);
            int userWait;
            switch (event.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                    if (this.mouse < 0) {
                        int btnIndex = this.b(mouseY);
                        int layerIndex = btnIndex - 1; // first row is reserved for the menu button, the rest are layers
                        if (layerIndex >= 0) {
                            if (mouseX > this.bW + 100 + 1) {
                                userWait = this.mi.user.wait;
                                this.mi.user.wait = -2;
                                if (isModifier) {
                                    for (int i = 0; i < info.L; ++i) {
                                        this.setAlpha(i, i == layerIndex ? 255 : 0, true);
                                    }
                                } else {
                                    this.setAlpha(layerIndex, info.layers[layerIndex].iAlpha == 0.0F ? 255 : 0, true);
                                }

                                this.mi.user.wait = userWait;
                                this.mi.repaint();
                                this.p();
                            } else if (event.getClickCount() < 2 && !isModifier) {
                                this.isASlide = mouseX >= this.bW;
                                this.mouse = btnIndex;
                                this.m.iLayer = this.m.iLayerSrc = layerIndex;
                                this.YOFF = mouseY % this.bH;
                                if (this.isASlide) {
                                    this.setAlpha(layerIndex, (int) ((float) (mouseX - this.bW) / 100.0F * 255.0F), false);
                                } else {
                                    this.p();
                                }
                            } else {
                                this.config(layerIndex);
                                this.mi.repaint();
                            }
                        } else {
                            this.m.iLayerSrc = this.m.iLayer;
                            if (mouseX < this.bW && mouseY > 2) {
                                this.popup(new String[]{"AddLayer", "DelLayer", "CombineV", "PropertyLayer"}, mouseX, mouseY, true);
                            }
                        }
                    }
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    if (!isModifier) {
                        if (this.isASlide) {
                            this.setAlpha(this.m.iLayer, (int) ((float) (mouseX - this.bW) / 100.0F * 255.0F), true);
                            this.mouse = -1;
                            this.isASlide = false;
                        } else {
                            userWait = this.mouse - 1;
                            int layerIndex = this.b(this.Y) - 1;
                            if (userWait >= 0 && layerIndex >= 0 && userWait != layerIndex) {
                                this.m.iLayer = layerIndex;
                                this.m.iLayerSrc = userWait;
                                this.popup(new String[]{this.res.res("Shift"), this.res.res("Combine")}, mouseX, mouseY, false);
                            }

                            this.mouse = -1;
                            this.repaint();
                        }
                    }
                    break;
                case MouseEvent.MOUSE_MOVED:
                    userWait = this.b(mouseY) - 1;
                    if (!this.is_pre || userWait < 0 || mouseX >= this.bW) {
                        if (this.is_DIm) {
                            this.is_DIm = false;
                            this.repaint();
                        }

                        return;
                    }

                    this.is_DIm = true;
                    Dimension var9 = this.getSize();
                    int var10 = this.mi.info.W;
                    int var11 = this.mi.info.H;
                    int[] var12 = this.mi.info.layers[userWait].offset;
                    Graphics var13 = this.getG();
                    int var14 = Math.min(var9.width - this.bW - 1, var9.height - 1);
                    if (var12 == null) {
                        var13.setColor(Color.white);
                        var13.fillRect(this.bW + 1, 1, var14 - 1, var14 - 1);
                    } else {
                        Image var15 = this.getToolkit().createImage((ImageProducer) (new MemoryImageSource(var10, var11, new DirectColorModel(24, 16711680, 65280, 255), var12, 0, var10)));
                        var13.drawImage(var15, this.bW + 1, 1, var14 - 1, var14 - 1, (ImageObserver) null);
                        var15.flush();
                    }

                    var13.setColor(Color.black);
                    var13.drawRect(this.bW, 0, var14, var14);
                    var13.dispose();
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                default:
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    if (this.mouse > 0) {
                        if (this.isASlide) {
                            this.setAlpha(this.m.iLayer, (int) ((float) (mouseX - this.bW) / 100.0F * 255.0F), false);
                        } else {
                            this.m.iLayer = this.b(this.Y) - 1;
                            this.repaint();
                        }
                    }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void popup(String[] var1, int var2, int var3, boolean var4) {
        if (this.mi.info.isLEdit) {
            if (this.popup == null) {
                this.popup = new PopupMenu();
                this.popup.addActionListener(this);
                this.add(this.popup);
            } else {
                this.popup.removeAll();
            }

            for (int var5 = 0; var5 < var1.length; ++var5) {
                this.popup.add(this.res.res(var1[var5]));
            }

            if (var4) {
                this.popup.addSeparator();
                CheckboxMenuItem var6 = new CheckboxMenuItem(this.res.res("IsPreview"), this.is_pre);
                var6.addItemListener(this);
                this.popup.add((MenuItem) var6);
                this.popup.setName("m");
            } else {
                this.popup.setName("l");
            }

            this.popup.show(this, var2, var3);
        }
    }

    private void setA(M mg) {
        // bit 1-8: layer.alpha - bit 9-16: layerSrc.alpha
        mg.iAlpha2 = (int) (this.mi.info.layers[mg.iLayer].iAlpha * 255.0F) << 8 | (int) (this.mi.info.layers[mg.iLayerSrc].iAlpha * 255.0F);
    }

    public void setAlpha(int layerIndex, int alpha, boolean doSend) throws Throwable {
        alpha = alpha <= 0 ? 0 : (alpha >= 255 ? 255 : alpha);
        if ((float) alpha != this.mi.info.layers[layerIndex].iAlpha) {
            if (doSend) {
                int currentLayerIndex = this.m.iLayer;
                this.m.iLayer = layerIndex;
                this.send(new int[]{8}, new byte[]{(byte) alpha});
                this.m.iLayer = currentLayerIndex;
            } else {
                this.mi.info.layers[layerIndex].iAlpha = (float) alpha / 255.0F;
                this.mi.repaint();
                this.repaint();
            }

        }
    }

    public void config(int layerIndex) {
        LO layer = this.mi.info.layers[layerIndex];
        Choice choice = new Choice();
        choice.add(this.res.res("Normal"));
        choice.add(this.res.res("Multiply"));
        choice.add(this.res.res("Reverse"));
        choice.select(layer.iCopy);
        TextField textField = new TextField(layer.name);
        Me me = Me.getMe();
        Panel panel = new Panel(new GridLayout(0, 1));
        panel.add(textField);
        panel.add(choice);
        textField.addActionListener(me);
        me.add((Component) panel, (Object) "Center");
        me.pack();
        Awt.moveCenter(me);
        me.setVisible(true);
        if (me.isOk) {
            String inputText = textField.getText();
            if (!inputText.equals(layer.name)) {
                try {
                    this.send(new int[]{10}, inputText.getBytes("UTF8"));
                } catch (Throwable ex) {
                    ;
                }
            }

            int var8 = choice.getSelectedIndex();
            if (layer.iCopy != var8) {
                this.send(new int[]{9}, new byte[]{(byte) var8});
            }

            this.repaint();
        }

    }
}
