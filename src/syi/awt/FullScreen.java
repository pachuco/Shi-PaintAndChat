package syi.awt;

import java.awt.*;
import java.awt.event.*;


public class FullScreen {
    private Frame frame;
    private int mXPos, mYPos, mWidth, mHeight;
    public boolean isFullscreen = false;

    public FullScreen(Frame f) {
        isFullscreen = false;
        frame = f;
    }

    public boolean fullToggle() {
        return fullToggle(!isFullscreen);
    }

    public boolean fullToggle(boolean goFull) {
        //TODO: this is broken, fix it later
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        frame.dispose();

        frame.setResizable(!goFull);
        if (goFull) {
            mXPos = frame.getX();
            mYPos = frame.getY();
            mWidth = frame.getWidth();
            mHeight = frame.getHeight();
            frame.setUndecorated(true);
            if (device.isFullScreenSupported()) {
                device.setFullScreenWindow(frame);
            } else {
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            }
            frame.setVisible(true);
            frame.requestFocusInWindow();
        } else {
            if (device.isFullScreenSupported()) {
                device.setFullScreenWindow(null);
            } else {
                frame.setExtendedState(Frame.NORMAL);
            }
            frame.setUndecorated(false);
            frame.setBounds(mXPos, mYPos, mWidth, mHeight);
            frame.setVisible(true);

            frame.requestFocusInWindow();
        }
        isFullscreen = goFull;
        return true;
    }
}
