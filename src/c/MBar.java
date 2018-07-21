package c;

import java.awt.*;
import java.awt.event.ActionListener;

public class MBar extends MenuBar {
    public static final String hNew        = "hNew";
    public static final String hOpen       = "hOpen";
    public static final String hSave       = "hSave";
    public static final String hSvAsJPG    = "hSvAsJPG";
    public static final String hSvAsPNG    = "hSvAsPNG";
    public static final String hSvAsAni    = "hSvAsAni";
    public static final String hExit       = "hExit";
    public static final String hToggleScr  = "hToggle";

    private Menu mFile, mPrefs, mPrefsSaveAs;
    private MenuItem miSaveAsJPG, miSaveAsPNG, miSaveAsAni;
    private MenuItem miNew, miOpen, miSave, miExit;
    private MenuItem miToggleScr;

    private MenuItem prepMi(ActionListener al, String action, String label) {
        MenuItem ret = new MenuItem(label);
        ret.addActionListener(al);
        ret.setActionCommand(action);
        return ret;
    }

    public MBar(ActionListener al) {
        mFile = new Menu("File");
        mPrefs = new Menu("Options");
        mPrefsSaveAs = new Menu("Save as...");

        miNew = prepMi(al, hNew, "New image");
        miOpen = prepMi(al, hOpen, "Open file");
        miSave = prepMi(al, hSave, "Save");
        miSaveAsJPG = prepMi(al, hSvAsJPG, "JPEG");
        miSaveAsPNG = prepMi(al, hSvAsPNG, "PNG");
        miSaveAsAni = prepMi(al, hSvAsAni, "Animation");
        miExit = prepMi(al, hExit, "Exit");
        miToggleScr = prepMi(al, hToggleScr, "Toggle fullscreen");


        add(mFile);
        mFile.add(miNew);
        mFile.add(miOpen);
        mFile.add(miSave);
        mFile.add(mPrefsSaveAs);
        mPrefsSaveAs.add(miSaveAsJPG);
        mPrefsSaveAs.add(miSaveAsPNG);
        mPrefsSaveAs.add(miSaveAsAni);
        mFile.addSeparator();
        mFile.add(miExit);

        add(mPrefs);
        mPrefs.add(miToggleScr);
    }
}
