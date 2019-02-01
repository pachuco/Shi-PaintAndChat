package syi.awt.cust;

import java.awt.*;
import java.awt.event.*;

import static res.ResShiClient.*;

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
        mFile = new Menu(lang.get("menuFile"));
        mPrefs = new Menu(lang.get("menuOptions"));
        mPrefsSaveAs = new Menu(lang.get("menuSaveAs"));

        miNew = prepMi(al, hNew, lang.get("menuNewImage"));
        miOpen = prepMi(al, hOpen, lang.get("menuOpenFile"));
        miSave = prepMi(al, hSave, lang.get("menuSave"));
        miSaveAsJPG = prepMi(al, hSvAsJPG, lang.get("menuAsJpeg"));
        miSaveAsPNG = prepMi(al, hSvAsPNG, lang.get("menuAsPng"));
        miSaveAsAni = prepMi(al, hSvAsAni, lang.get("menuAsAnim"));
        miExit = prepMi(al, hExit, lang.get("menuExit"));
        miToggleScr = prepMi(al, hToggleScr, lang.get("menuFullscr"));


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
