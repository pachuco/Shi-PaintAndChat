package config;

import com.sun.media.sound.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class Rez {

    public static String textUnescape(String str) {
        //add other stuff to unescape, as needed
        str = str.replaceAll("(?<!\\\\)\\\\n", "\n");
        str = str.replaceAll("\\\\\\\\", "\\\\");
        return str;
    }

    public static String textSet2String(HashSet<String> set, String separator) {
        return String.join(separator, set.toArray(new String[set.size()]));
    }

    public static JavaSoundAudioClip audioLoad(File file) {
        try {
            return new JavaSoundAudioClip(new FileInputStream(file));
        } catch (IOException e) {
            return null;
        }
    }

    public static JavaSoundAudioClip audioLoad(URL url) {
        try {
            return new JavaSoundAudioClip(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }

    public static JavaSoundAudioClip audioLoad(InputStream in) {
        try {
            return new JavaSoundAudioClip(in);
        } catch (IOException e) {
            return null;
        }
    }

    public static File IOGetClasspathRes(String relPath) {
        return new File(Rez.class.getResource(relPath).getPath());
    }

    public static File IOGetAppfolderRes(String relPath) {
        String base = "";

        String protocol = Rez.class.getResource("").getProtocol();
        if(protocol.equals("jar")){
            base = Rez.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            base = new File(base).getParent();
        } else if(protocol.equals("file")) {
            base = System.getProperty("user.dir");
        }
        return new File(base, relPath);
    }
}
