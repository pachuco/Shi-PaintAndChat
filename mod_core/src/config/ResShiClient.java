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
        HashMap<String,String> mapMaster, mapSlave;
        URL url;

        mapMaster = iniRead(IOGetClasspathResource(String.format(PATH_LANG, "en")), null);
        //url = IOGetClasspathResource(String.format(PATH_LANG, "zh"));
        url = IOGetClasspathResource(String.format(PATH_LANG, langGet()));
        if (url==null) {
            lang = mapMaster;
        } else {
            mapSlave = iniRead(url, null);
            iniValidateKeys(mapMaster, mapSlave);
            lang = mapSlave;
        }
        lang = url!=null ? iniRead(url, null) : mapMaster;

        snd_join = audioLoad(IOGetClasspathResource(String.format(PATH_SND, "in")));
        snd_leave = audioLoad(IOGetClasspathResource(String.format(PATH_SND, "out")));
        snd_talk = audioLoad(IOGetClasspathResource(String.format(PATH_SND, "talk")));
        snd_type = null;
    }
}
