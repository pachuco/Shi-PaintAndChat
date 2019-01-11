package syi.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;

import syi.util.ThreadPool;

public class HelpWindow extends Window implements Runnable {
    private Thread rAdd = null;
    private HelpWindowContent object = null;
    private ImageCanvas imageCanvas = null;
    private TextCanvas textCanvas = null;
    private boolean isShow = false;
    private boolean isFront = true;

    public HelpWindow(Frame var1) {
        super(var1);
    }

    public boolean getIsShow() {
        return this.isShow;
    }

    public synchronized void reset() {
        this.setVisible(false);
        this.object = null;
        if (this.getComponentCount() > 0) {
            Component var1 = this.getComponent(0);
            if (var1 == this.textCanvas) {
                ((TextCanvas) var1).reset();
            } else {
                ((ImageCanvas) var1).reset();
            }
        }

        if (this.rAdd != null && this.rAdd != Thread.currentThread()) {
            synchronized (this.rAdd) {
                this.rAdd.interrupt();
                this.rAdd.notify();
                this.rAdd = null;
            }
        }

    }

    public void run() {
        try {
            Thread var1 = Thread.currentThread();
            if (this.object.timeStart > 0) {
                synchronized (var1) {
                    var1.wait((long) this.object.timeStart);
                }
            }

            if (!var1.isInterrupted()) {
                this.showHelp(this.object);
                if (this.object.timeEnd > 0) {
                    synchronized (var1) {
                        var1.wait((long) this.object.timeEnd);
                    }
                }
            }

            this.reset();
        } catch (Throwable ex) {
        }

    }

    public void setIsFront(boolean var1) {
        this.isFront = var1;
    }

    public void setIsShow(boolean var1) {
        this.isShow = var1;
    }

    private synchronized void showHelp(HelpWindowContent var1) {
        if (var1 != null) {
            Component var2;
            if (var1.image != null) {
                if (this.imageCanvas == null) {
                    this.imageCanvas = new ImageCanvas(this.getBackground(), this.getForeground());
                }

                if (this.getComponentCount() > 0) {
                    var2 = this.getComponent(0);
                    if (var2 != this.imageCanvas) {
                        this.remove(var2);
                        this.add(this.imageCanvas);
                    }
                } else {
                    this.add(this.imageCanvas);
                }

                this.imageCanvas.setImage(var1.image);
                if (var1.string != null) {
                    this.imageCanvas.setText(var1.getText());
                }
            } else {
                if (this.textCanvas == null) {
                    this.textCanvas = new TextCanvas();
                    this.textCanvas.setBackground(new Color(13421823));
                    this.textCanvas.setForeground(Color.black);
                }

                if (this.getComponentCount() > 0) {
                    var2 = this.getComponent(0);
                    if (var2 != this.textCanvas) {
                        this.remove(var2);
                        this.add(this.textCanvas);
                    }
                } else {
                    this.add(this.textCanvas);
                }

                this.textCanvas.setText(var1.getText());
            }

            this.pack();
            this.getSize();
            this.setLocation(var1.point.x, var1.point.y);
            this.setVisible(true);
            if (this.isFront) {
                this.toFront();
            }

        }
    }

    public synchronized void startHelp(HelpWindowContent var1) {
        if (var1.isVisible(this.isShow)) {
            this.reset();
            this.object = var1;
            Point var10000 = var1.point;
            var10000.y += 15;
            this.rAdd = ThreadPool.poolStartThread(this, "s");
        }
    }
}
