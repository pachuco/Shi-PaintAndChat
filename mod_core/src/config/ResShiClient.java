package config;

import com.sun.media.sound.*;

import java.io.*;

import static config.IniMap.*;
import static config.Rez.*;

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

        File fMaster = IOGetClasspathRes(String.format(PATH_LANG, "en"));
        File fSlave  = IOGetClasspathRes(String.format(PATH_LANG, SYSLANG));

        IniMap mapMaster = iniRead(fMaster, null, ACC_RO&ERR_WARN);
        if (fSlave==null) {
            lang = mapMaster;
        } else {
            IniMap mapSlave = iniRead(fSlave, null, ACC_RO&ERR_WARN);
            mapSlave.validateKeysAgainst(mapMaster);
            lang = mapSlave;
        }

        snd_join = audioLoad(IOGetClasspathRes(String.format(PATH_SND, "in")));
        snd_leave = audioLoad(IOGetClasspathRes(String.format(PATH_SND, "out")));
        snd_talk = audioLoad(IOGetClasspathRes(String.format(PATH_SND, "talk")));
        snd_type = null;
        //testValidateAllLanguages();
    }

    private void testValidateAllLanguages() {
        File fRoot = IOGetClasspathRes("/res/lang/");
        File fMaster = new File(fRoot, "shiclient_en.ini");
        IniMap iniMaster = iniRead(fMaster, null, ACC_RO&ERR_WARN);

        for (File f : fRoot.listFiles()) {
            if (f == null) continue;
            if (f.equals(fMaster)) continue;
            if (!f.getName().startsWith("shiclient_")) continue;
            IniMap im = iniRead(f, null, ACC_RO&ERR_WARN);
            System.err.println("Checking: "+f.getName());
            if (im.validateKeysAgainst(iniMaster)) {
                System.err.println("ok!");
            }
        }
    }
}
