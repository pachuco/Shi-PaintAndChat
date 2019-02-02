package res;

import com.sun.media.sound.*;
import java.io.*;

import syi.util.cust.*;

public final class ResShiClient{
    public static IniMap
            langSP;
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
            langSP = new IniMap(langFile.getInputStream(), null, IniMap.ACC_RO, "lang_"+SYSLANG);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load langfile: " + langFile.getPath());
        }

        snd_join = Rez.loadAudio(new RFile(RFile.F_CP, String.format(PATH_SND, "in")));
        snd_leave = Rez.loadAudio(new RFile(RFile.F_CP, String.format(PATH_SND, "out")));
        snd_talk = Rez.loadAudio(new RFile(RFile.F_CP, String.format(PATH_SND, "talk")));
        snd_type = null;
        if (!RFile.isEnvironmentJar()) Rez.testValidateAllLanguages("/res/lang/", "shiclient_", "shiclient_en.ini");
    }
}
