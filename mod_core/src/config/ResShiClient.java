package config;

import com.sun.media.sound.*;
import java.net.*;
import java.util.*;

public final class ResShiClient extends Rez{
    public static HashMap<String, String>
            lang;
    public static JavaSoundAudioClip
            snd_join,
            snd_leave,
            snd_talk,
            snd_type;

    public ResShiClient() {
        String PATH_LANG = "/res/lang/shiclient_%s.ini";
        String PATH_SND  = "/res/snd/%s.au";
        URL url;

        url = ioGetClasspathResource(String.format(PATH_LANG, langGet()));
        if (url==null) url = ioGetClasspathResource(String.format(PATH_LANG, "en"));
        lang = iniRead(url, null);

        snd_join = audioLoad(ioGetClasspathResource(String.format(PATH_SND, "in")));
        snd_leave = audioLoad(ioGetClasspathResource(String.format(PATH_SND, "out")));
        snd_talk = audioLoad(ioGetClasspathResource(String.format(PATH_SND, "talk")));
        snd_type = null;
    }
}
