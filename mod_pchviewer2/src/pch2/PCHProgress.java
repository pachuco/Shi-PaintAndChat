package pch2;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

public class PCHProgress extends Canvas {
    private PCHCanvas pch;
    private boolean isBuffer;
    private int selTool = -1;
    private int putTool = -1;
    private Rectangle[] rects = null;
    private Image image = null;
    private Graphics back = null;
    private Graphics primary = null;
    private Color clBack;
    private Color clIcon;
    private Color clFore;
    private Color clBar;
    private Color clFrame;
    private Color clSelect;
    public static boolean isJa;
    private static final Font font = new Font("sansserif", 0, 12);
    private static final int I_BODY = 0;
    private static final int I_PLAY = 1;
    private static final int I_STOP = 2;
    private static final int I_RESTART = 3;
    private static final int I_SPEED = 4;
    private static final int I_ADD = 5;
    private static final int I_SUB = 6;
    private static final int I_BAR = 7;
    private static final int I_SIZE_ICON = 20;
    public static final int I_SIZE_PRE = 26;
    private static String[] EN_SPEED = {"Mx", "H", "M", "L"};
    private static String[] JA_SPEED = {"最", "早", "既", "鈍"};

    public PCHProgress(boolean isBuffer) {
        this.enableEvents(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        this.isBuffer = isBuffer;
    }

    public void action(Point point) {
        if (this.rects != null) {
            int iconNum = I_PLAY;

            int maxIcon;
            for (maxIcon = this.rects.length; iconNum < maxIcon; ++iconNum) {
                if (this.rects[iconNum].contains(point)) {
                    if (this.putTool != iconNum) {
                        this.putTool = iconNum;
                        this.drawIcon(iconNum);
                        return;
                    }
                    break;
                }
            }

            if (iconNum < maxIcon) {
                if (this.pch != null) {
                    switch (iconNum) {
                        case I_PLAY:
                            this.pch.playPCH();
                            break;
                        case I_STOP:
                            this.pch.suspendDraw();
                            break;
                        case I_RESTART:
                            this.pch.setMark(0);
                            this.pch.setMark(-1);
                            this.drawIcon(I_BAR);
                            break;
                        case I_SPEED:
                            int oldSpeed = this.pch.getSpeed();
                            byte newSpeed;
                            switch (oldSpeed) {
                                case 0:
                                    newSpeed = 10;
                                    break;
                                case 10:
                                    newSpeed = 20;
                                    break;
                                case 20:
                                    newSpeed = -1;
                                    break;
                                default:
                                    newSpeed = 0;
                            }

                            this.pch.setSpeed(newSpeed);
                            break;
                        case I_ADD:
                            this.pch.setScale(1, false);
                            break;
                        case I_SUB:
                            this.pch.setScale(-1, false);
                    }

                }
            }
        }
    }

    public void drawBar() {
        this.drawIcon(I_BAR);
    }

    private void drawIcon(int iconNum) {
        Graphics g = this.getPrimary();
        if (g != null) {
            if (this.isBuffer) {
                this.drawIcon(this.back, iconNum);
                if (this.rects != null && iconNum > 0 && iconNum < this.rects.length) {
                    Rectangle rect = this.rects[iconNum];
                    g.drawImage(this.image, rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, (ImageObserver) null);
                }
            } else {
                this.drawIcon(g, iconNum);
            }
        }
    }

    private void drawIcon(Graphics g, int iconNum) {
        if (this.rects == null || iconNum < 0 || iconNum >= this.rects.length || g == null) return;
        synchronized (g) {
            Rectangle rect = this.rects[iconNum];
            Color color = iconNum == 0 ? this.clBack : this.clIcon;
            color = iconNum == this.putTool ? color.darker() : color;
            if (iconNum != I_BAR) {
                g.setColor(color);
                g.fillRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
            }

            if (iconNum == this.selTool && iconNum != this.putTool || iconNum == 0) {
                g.setColor(color.brighter());
                g.drawRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
            }

            g.setColor(iconNum == this.selTool ? this.clSelect : this.clFrame);
            g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);

            int[] points1;
            int[] points2;
            switch (iconNum) {
                case I_PLAY: {
                    points1 = new int[]{rect.x + 7, rect.x + 13, rect.x + 7};
                    points2 = new int[]{rect.y + 4, rect.y + 10, rect.y + 16};
                    g.setColor(this.clFore);
                    g.fillPolygon(points1, points2, points1.length);
                    g.setColor(this.clFore.brighter());
                    g.drawPolygon(points1, points2, points1.length);
                } break;
                case I_STOP: {
                    g.setColor(this.clFore);
                    g.fillRect(rect.x + 5, rect.y + 6, 9, 9);
                    g.setColor(this.clFore.brighter());
                    g.drawRect(rect.x + 4, rect.y + 5, 10, 10);
                } break;
                case I_RESTART: {
                    points1 = new int[]{rect.x + 8, rect.x + 2, rect.x + 8};
                    points2 = new int[]{points1[0] + 8, points1[1] + 8, points1[2] + 8};
                    int[] points3 = new int[]{rect.y + 4, rect.y + 10, rect.y + 16};
                    g.setColor(this.clFore);
                    g.fillPolygon(points1, points3, points1.length);
                    g.fillPolygon(points2, points3, points1.length);
                    g.setColor(this.clFore.brighter());
                    g.drawPolygon(points1, points3, points1.length);
                    g.drawPolygon(points2, points3, points1.length);
                } break;
                case I_SPEED: {
                    String[] arrSpeedStr = isJa ? JA_SPEED : EN_SPEED;
                    int speed = this.pch.getSpeed();
                    speed = speed < 0 ? 0 : (speed == 0 ? 1 : (speed <= 10 ? 2 : 3));
                    g.setColor(this.clFore);
                    g.drawString(arrSpeedStr[speed], rect.x + 4, rect.y + rect.height - 4);
                } break;
                case I_ADD: {
                    int w = rect.width / 2;
                    int h = rect.height / 2;
                    g.fillRect(rect.x + w / 2, rect.y + h - 1, w, 2);
                    g.fillRect(rect.x + w - 1, rect.y + h / 2, 2, h);
                } break;
                case I_SUB: {
                    int w = rect.width / 2;
                    int h = rect.height / 2;
                    g.fillRect(rect.x + w / 2, rect.y + h - 1, w, 2);
                } break;
                case I_BAR: {
                    int lineCount = this.pch.getLineCount();
                    int seek = this.pch.getSeek();
                    int seekPos = (int) ((float) rect.width * ((float) seek / (float) lineCount)) - 5;
                    if (seekPos <= 0) seekPos = 1;

                    g.setColor(this.clBar);
                    g.fillRect(rect.x + 3, rect.y + 2, seekPos, rect.height - 4);
                    g.setColor(color);
                    g.fillRect(rect.x + 3 + seekPos, rect.y + 2, rect.width - seekPos - 5, rect.height - 4);
                    g.setColor(this.clFore);
                    g.fillRect(rect.x + Math.min(Math.max((int) ((float) this.pch.getMark() / (float) lineCount * (float) rect.width), 3), rect.width - 3), rect.y + 2, 1, rect.height - 4);
                    g.drawString(String.valueOf(seek) + '/' + lineCount, rect.x + 3, rect.y + rect.height - 3);
                }
            }
        }
    }

    public Dimension getMinimumSize() {
        return new Dimension(I_SIZE_PRE, I_SIZE_PRE);
    }

    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return parent == null ? this.getMinimumSize() : new Dimension(parent.getSize().width, I_SIZE_PRE);
    }

    private Graphics getPrimary() {
        if (this.primary == null) {
            if (this.getParent() == null) {
                return null;
            }

            this.primary = this.getGraphics();
            if (this.primary == null) {
                return null;
            }

            this.primary.setFont(font);
        }

        return this.primary;
    }

    private void iPaint(Graphics g) {
        if (g != null) {
            for (int i = 0; i < this.rects.length; ++i) {
                this.drawIcon(g, i);
            }

        }
    }

    public void paint(Graphics g) {
        try {
            if (this.rects == null) {
                this.upRect();
                this.iPaint(this.back);
            }

            g = this.getPrimary();
            if (this.isBuffer) {
                Dimension dim = this.getSize();
                g.drawImage(this.image, 0, 0, dim.width, dim.height, 0, 0, dim.width, dim.height, null);
            } else {
                this.iPaint(g);
            }
        } catch (Throwable ex) {
        }

    }

    protected void processComponentEvent(ComponentEvent cEv) {
        try {
            int id = cEv.getID();
            if (this.rects == null) {
                return;
            }

            if (id == ComponentEvent.COMPONENT_RESIZED) {
                this.upRect();
                this.iPaint(this.back);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    protected void processMouseEvent(MouseEvent mEv) {
        try {
            int id = mEv.getID();
            Point point = mEv.getPoint();
            switch (id) {
                case MouseEvent.MOUSE_PRESSED:
                    this.action(point);
                    if (this.putTool == I_BAR) {
                        this.setMark(point);
                    }
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    this.action(point);
                    this.releaseIcon();
                    break;
                case MouseEvent.MOUSE_MOVED:
                    this.selIcon(point);
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                default:
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    if (this.putTool == I_BAR) {
                        this.setMark(point);
                    }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    protected void processMouseMotionEvent(MouseEvent mEv) {
        this.processMouseEvent(mEv);
    }

    private void releaseIcon() {
        int iconNum = this.putTool;
        this.putTool = -1;
        this.drawIcon(iconNum);
    }

    private void selIcon(Point point) {
        if (this.rects != null) {
            int oldTool = this.selTool;
            int maxTool = this.rects.length;

            int newTool;
            for (newTool = 1; newTool < maxTool && !this.rects[newTool].contains(point); ++newTool) {
            }

            if (newTool >= maxTool)  newTool = -1;

            if (this.selTool != newTool) {
                this.selTool = newTool;
                if (oldTool != -1) this.drawIcon(oldTool);
                if (newTool != -1) this.drawIcon(newTool);

            }
        }
    }

    public void setColor(Color cBack, Color cFore, Color cIcon, Color cBar, Color cFrame, Color cSel) {
        this.clBack = cBack;
        this.clFore = cFore;
        this.clIcon = cIcon;
        this.clBar = cBar;
        this.clFrame = cFrame;
        this.clSelect = cSel;
        this.setBackground(cBack);
        this.setForeground(cFore);
    }

    private synchronized void setMark(Point point) {
        try {
            int pos = point.x - this.rects[I_BAR].x;
            int barWidth = this.rects[I_BAR].width;
            int lineCount = this.pch.getLineCount();
            pos = pos <= 0 ? 0 : (pos >= barWidth ? lineCount : (int) ((float) pos / (float) barWidth * (float) lineCount));
            this.pch.setMark(pos);
            this.drawIcon(I_BAR);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

    }

    public void setPCHCanvas(PCHCanvas pch) {
        this.pch = pch;
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    private synchronized void upRect() {
        if (this.rects == null) {
            this.rects = new Rectangle[8];

            for (int i = 0; i < this.rects.length; ++i) {
                this.rects[i] = new Rectangle();
            }
        }

        Dimension dim = this.getSize();
        int x = 3;
        int y = 3;

        for (int i = 1; i < this.rects.length; ++i) {
            this.rects[i].setSize(I_SIZE_ICON, I_SIZE_ICON);
        }

        for (int i = 1; i < this.rects.length; ++i) {
            Rectangle rect = this.rects[i];
            rect.setLocation(x, y);
            x += 23;
        }

        this.rects[I_BAR].setSize(dim.width - this.rects[I_BODY].x - 3, I_SIZE_ICON);
        this.rects[I_BODY].setBounds(0, 0, dim.width, dim.height);
        if (this.isBuffer && (this.image == null || this.image.getWidth(null) < dim.width || this.image.getHeight(null) < dim.height)) {
            if (this.image != null) {
                this.image.flush();
                this.back.dispose();
            }

            this.image = this.createImage(dim.width, dim.height);
            this.back = this.image.getGraphics();
            this.back.setFont(font);
        }
    }
}
