package syi.awt;

import java.awt.*;
import java.awt.event.*;


public class AppWindow extends Frame implements WindowListener, KeyListener {
    private int mXPos, mYPos, mWidth, mHeight;
    public boolean isFullscreen = false;

    public AppWindow() {
        isFullscreen = false;

        setSize(600, 600);
        addKeyListener(this);
        addWindowListener(this);
        setVisible(true);
    }

    public boolean fullToggle() {
        return fullToggle(!isFullscreen);
    }

    public boolean fullToggle(boolean goFull) {
        //TODO: this is broken, fix it later
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        dispose();

        setResizable(!goFull);
        if (goFull) {
            mXPos = getX();
            mYPos = getY();
            mWidth = getWidth();
            mHeight = getHeight();
            setUndecorated(true);
            if (device.isFullScreenSupported()) {
                device.setFullScreenWindow(this);
            } else {
                setExtendedState(Frame.MAXIMIZED_BOTH);
            }
            setVisible(true);
            requestFocusInWindow();
        } else {
            device.setFullScreenWindow(null);
            setUndecorated(false);
            setBounds(mXPos, mYPos, mWidth, mHeight);
            setVisible(true);

            requestFocusInWindow();
        }
        isFullscreen = goFull;
        return true;
    }

    //WindowListener
    public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
    }

    public void windowOpened(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }

    //KeyListener
    public void keyPressed(KeyEvent e) {
        boolean isAltDown = e.isAltDown();
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_F11) {
            fullToggle();
        }
    }

    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }

}
