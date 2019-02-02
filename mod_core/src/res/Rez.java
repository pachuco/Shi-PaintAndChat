package res;

import com.sun.media.sound.*;
import syi.util.cust.*;

import java.io.*;

public class Rez {
    public static void testValidateAllLanguages(String root, String prefix, String master) {
        RFile fRoot = new RFile(RFile.F_CP, root);
        RFile fMaster = new RFile(fRoot, master);
        IniMap iniMaster = null;
        try {
            iniMaster = new IniMap(fMaster.getInputStream(), null, IniMap.ACC_RO);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (RFile f : fRoot.listFiles()) {
            if (f.equals(fMaster)) continue;
            if (!f.getName().startsWith(prefix)) continue;
            try {
                IniMap im = new IniMap(f.getInputStream(), null, IniMap.ACC_RO);
                System.err.println("Checking: "+f.getName());
                if (im.validateKeysAgainst(iniMaster)) {
                    System.err.println("ok!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("oops!");
            }
        }
    }

    public static JavaSoundAudioClip loadAudio(RFile file) {
        try {
            return new JavaSoundAudioClip(file.getInputStream());
        } catch (IOException ex) {
            System.err.println("audioLoad: " + file.getPath());
            return null;
        }
    }
}
