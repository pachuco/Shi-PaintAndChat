package res;

import com.sun.media.sound.*;

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
        String PATH_SND  = "/res/snd/%s.au";

        langSP = Rez.loadLangFile(RFile.F_CP, "/res/lang/shiclient_%s.ini", null);

        snd_join = Rez.loadAudio(RFile.F_CP, String.format(PATH_SND, "in.au"));
        snd_leave = Rez.loadAudio(RFile.F_CP, String.format(PATH_SND, "out.au"));
        snd_talk = Rez.loadAudio(RFile.F_CP, String.format(PATH_SND, "talk.au"));
        snd_type = null; //Rez.loadAudio(RFile.F_CP, String.format(PATH_SND, "type.au"));
        if (!RFile.isEnvironmentJar()) Rez.testAllLanguages("/res/lang/", "shiclient_", "shiclient_en.ini");
    }
}
