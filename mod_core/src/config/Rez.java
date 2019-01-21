package config;

import com.sun.media.sound.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class Rez {
    protected static String FIELD_PREFIX = "_";
    protected static final int
        FAIL_ONMISSING_FIELD = 0x1,
        FAIL_ONMISSING_KEY   = 0x2;

    public static void classPopulateFromHashmap(Class cls, Object _this, HashMap<String, String> map, String prefix, int failModeOnMissing) {
        Field[] fields = cls.getDeclaredFields();
        Set<String> set = map.keySet();

        if (0!=(failModeOnMissing & FAIL_ONMISSING_KEY)) {
            for (Field fld : fields) {
                if (!set.contains(fld.getName().substring(prefix.length()))) {
                    throw new RuntimeException("Missing key: " + fld.getName());
                }
            }
        }
        for (String key : set) {
            try {
                Field fld = cls.getDeclaredField(prefix+key);
                fld.set(_this, map.get(key));
            } catch (NoSuchFieldException e) {
                if (0!=(failModeOnMissing & FAIL_ONMISSING_FIELD)) {
                    throw new RuntimeException("Missing field: " + prefix+key);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String langGet() {
        //return System.getProperty("user.language");
        return Locale.getDefault().getLanguage();
    }

    public static HashMap<String,String> iniRead(File file, String section) {
        try {
            return iniRead(new FileInputStream(file), section);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static HashMap<String,String> iniRead(URL url, String section) {
        try {
            return iniRead(url.openStream(), section);
        } catch (IOException e) {
            return null;
        }
    }

    public static HashMap<String,String> iniRead(InputStream in, String section) {
        BufferedReader bIn;
        HashMap<String,String> hmRet = new HashMap();
        bIn = new BufferedReader(new InputStreamReader(in), 512);

        try {
            String line; int i=1;
            if (section != null) {
                for (; (line = bIn.readLine()) != null; i++) {
                    line = line.trim();
                    if (line.startsWith("[") && line.equals("["+section+"]")) break;
                }
            }
            for (; (line = bIn.readLine()) != null; i++) {
                line = line.trim();
                if (line.startsWith("[") && line.endsWith("]")) {
                    break;
                } else if (line.contains("=")) {
                    String[] split = line.split("=", 2); //split by first occurrence
                    hmRet.put(split[0], textUnescape(split[1]));
                } else {
                    continue;
                }
            }
            return hmRet.isEmpty() ? null : hmRet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String textUnescape(String str) {
        //add other stuff to unescape, as needed
        str = str.replaceAll("(?<!\\\\)\\\\n", "\n");
        str = str.replaceAll("\\\\\\\\", "\\\\");
        return str;
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

    public static URL ioGetClasspathResource(String relPath) {
        return Rez.class.getResource(relPath);
    }

    public static File ioGetAppfolderResource(String relPath) {
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
