package syi.util.cust;

import com.sun.media.sound.*;

import java.io.*;
import java.net.*;

public class Audio {
    public static JavaSoundAudioClip load(RFile file) {
        try {
            return new JavaSoundAudioClip(file.getInputStream());
        } catch (IOException ex) {
            System.err.println("audioLoad: "+ex);
            return null;
        }
    }

    public static JavaSoundAudioClip load(File file) {
        try {
            return new JavaSoundAudioClip(new FileInputStream(file));
        } catch (IOException ex) {
            System.err.println("audioLoad: "+ex);
            return null;
        }
    }

    public static JavaSoundAudioClip load(URL url) {
        try {
            return new JavaSoundAudioClip(url.openStream());
        } catch (IOException ex) {
            System.err.println("audioLoad: "+ex);
            return null;
        }
    }

    public static JavaSoundAudioClip load(InputStream in) {
        try {
            JavaSoundAudioClip clip = new JavaSoundAudioClip(in);
            in.close();
            return clip;
        } catch (IOException ex) {
            System.err.println("audioLoad: "+ex);
            return null;
        }
    }

    public static boolean play(JavaSoundAudioClip clip) {
        if (clip!=null) {
            clip.play();
            return true;
        }
        return false;
    }
}
