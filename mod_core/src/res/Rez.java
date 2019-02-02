package res;

import com.sun.media.sound.*;
import syi.util.cust.*;

import java.io.*;

public class Rez {
    public static void testAllLanguages(String root, String prefix, String master) {
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

    public static IniMap loadLangFile(int flags, String formattedPath, String overrideLang) {
        IniMap ret;
        String lang = overrideLang!=null ? overrideLang : System.getProperty("user.language");
        RFile langMain = new RFile(flags, String.format(formattedPath, lang));
        RFile langBack = new RFile(flags, String.format(formattedPath, "en"));
        try {
            ret = new IniMap(langMain.getInputStream(), null, IniMap.ACC_RO, "lang_"+lang);
        } catch (IOException ex) {
            System.err.println("Cannot load main langfile: " + langMain.getPath());
            try {
                ret = new IniMap(langBack.getInputStream(), null, IniMap.ACC_RO, "lang_en");
            } catch (IOException ex2) {
                throw new RuntimeException("Cannot load backup langfile: " + langBack.getPath());
            }
        }
        return ret;
    }

    public static JavaSoundAudioClip loadAudio(int flags, String relPath) {
        RFile file = new RFile(flags, relPath);
        try {
            return new JavaSoundAudioClip(file.getInputStream());
        } catch (IOException ex) {
            System.err.println("Cannot load audio: " + file.getPath());
            return null;
        }
    }
}
