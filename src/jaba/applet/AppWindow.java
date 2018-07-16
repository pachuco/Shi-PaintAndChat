package jaba.applet;

import java.awt.*;
import java.awt.event.*;


public class AppWindow extends Frame implements WindowListener, KeyListener {
    private GraphicsDevice device;
    private int mXPos, mYPos, mWidth, mHeight;
    public boolean isFullscreen = false;

    public AppWindow() {
        isFullscreen = false;
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        setSize(600, 600);
        addKeyListener(this);
        addWindowListener(this);
        setVisible(true);
    }

    public boolean fullToggle() {
        return fullToggle(!isFullscreen);
    }

    public boolean fullToggle(boolean goFull) {
        Dimension screen;
        //TODO: this is broken, fix it later

        dispose();

        setResizable(!goFull);
        if (goFull) {
            mXPos = getX();
            mYPos = getY();
            mWidth = getWidth();
            mHeight = getHeight();
            screen = Toolkit.getDefaultToolkit().getScreenSize();
            setUndecorated(true);
            if (device.isFullScreenSupported()) {
                device.setFullScreenWindow(this);
            } else {
                setLocation(0, 0);
                setSize(screen);
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
    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    //KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        boolean isAltDown = e.isAltDown();
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_F11) {
            fullToggle();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
