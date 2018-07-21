package syi.awt;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class FileDlg extends FileDialog {

    File f;

    public FileDlg(Frame parent, String title, int mode, String filter) {
        super(parent, title, mode);
        //setMultipleMode(false);   //since 1.7
        //setModal(true);

        setFile(filter);
        setVisible(true);
    }

    public File getF() {
        File[] tf = getFiles();
        if(tf.length < 1) return null;
        if(!tf[0].isFile()) return null;
        return tf[0];
    }

}
