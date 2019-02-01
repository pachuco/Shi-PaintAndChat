package res;

import com.sun.media.sound.*;
import java.io.*;

import syi.util.cust.*;

public final class ResShiClient{
    public static IniMap
            lang;
    public static JavaSoundAudioClip
            snd_join,
            snd_leave,
            snd_talk,
            snd_type;

    public ResShiClient() {
        String PATH_LANG = "/res/lang/shiclient_%s.ini";
        String PATH_SND  = "/res/snd/%s.au";
        String SYSLANG   = System.getProperty("user.language");
        RFile langFile = new RFile(RFile.F_CP, String.format(PATH_LANG, SYSLANG));

        try {
            lang = new IniMap(langFile.getInputStream(), null, IniMap.ACC_RO, "langFile");
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load langfile: " + langFile.getPath());
        }

        snd_join = Audio.load(new RFile(RFile.F_CP, String.format(PATH_SND, "in")));
        snd_leave = Audio.load(new RFile(RFile.F_CP, String.format(PATH_SND, "out")));
        snd_talk = Audio.load(new RFile(RFile.F_CP, String.format(PATH_SND, "talk")));
        snd_type = null;
        if (!RFile.isEnvironmentJar()) testValidateAllLanguages();
    }

    private void testValidateAllLanguages() {
        RFile fRoot = new RFile(RFile.F_CP, "/res/lang/");
        RFile fMaster = new RFile(fRoot, "shiclient_en.ini");
        IniMap iniMaster = null;
        try {
            iniMaster = new IniMap(fMaster.getInputStream(), null, IniMap.ACC_RO);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (RFile f : fRoot.listFiles()) {
            if (f.equals(fMaster)) continue;
            if (!f.getName().startsWith("shiclient_")) continue;
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
}
