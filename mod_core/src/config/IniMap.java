package config;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class IniMap implements Map<String,String> {
    public static int
        ACC_RW = 0,
        ACC_RO = 1,

        ERR_NONE = 0,
        ERR_WARN = 2;

    private HashMap<String, String> hashmap;
    private int flags;

    private IniMap(HashMap<String, String> map, int flags) {
        hashmap = map;
        this.flags = flags;
    }

    public static IniMap iniRead(File file, String section, int flags) {
        try {
            return iniRead(new FileInputStream(file), section, flags);
        } catch (FileNotFoundException e) {
            error(flags, "IniMap: Cannot read ini from File", false);
            return null;
        }
    }

    public static IniMap iniRead(URL url, String section, int flags) {
        try {
            return iniRead(url.openStream(), section, flags);
        } catch (IOException e) {
            error(flags, "IniMap: Cannot read ini from URL", false);
            return null;
        }
    }

    public static IniMap iniRead(InputStream in, String section, int flags) {
        BufferedReader bIn = new BufferedReader(new InputStreamReader(in), 512);
        HashMap<String, String> hmRet = new HashMap<String, String>();

        try {
            boolean isSkip = section!=null;
            boolean isTrim = true;
            String line;

            for (int i=1; (line = bIn.readLine()) != null; i++) {
                line = line.trim();
                if (i==1 && (line.startsWith("#") || line.startsWith(";"))) {
                    if (line.substring(1).startsWith("NOTRIM")) isTrim = false;
                }
                if (isSkip) {
                    if (line.startsWith("[") && line.equals("["+section+"]")) isSkip = false;
                } else {
                    if (line.startsWith("[") && line.endsWith("]")) {
                        break;
                    } else if (line.contains("=")) {
                        //split by first occurrence
                        String[] split = line.split("=", 2);
                        if (isTrim) {
                            split[0] = split[0].trim();
                            split[1] = split[1].trim();
                        }
                        hmRet.put(split[0], Rez.textUnescape(split[1]));
                    }
                }
            }
            return hmRet.isEmpty() ? null : new IniMap(hmRet, flags);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void populateClass(Class cls, Object _this, String prefix) {
        Field[] fields = cls.getDeclaredFields();
        Set<String> set = hashmap.keySet();

        for (Field fld : fields) {
            if (!set.contains(fld.getName().substring(prefix.length()))) {
                error(String.format("IniMap: Missing key: %s", fld.getName()), false);
            }
        }
        for (String key : set) {
            try {
                Field fld = cls.getDeclaredField(prefix+key);
                fld.set(_this, hashmap.get(key));
            } catch (NoSuchFieldException e) {
                error(String.format("IniMap: Missing field: %s", prefix+key), false);
            } catch (IllegalAccessException e) {
                error(String.format("IniMap: Cannot set field: %s", prefix + key), false);
            }
        }
    }

    public boolean validateKeysAgainst(IniMap slave) {
        HashSet<String> diffMaster = new HashSet<String>(hashmap.keySet());
        HashSet<String> diffSlave  = new HashSet<String>(slave.keySet());
        diffMaster.removeAll(slave.keySet());
        diffSlave.removeAll(hashmap.keySet());

        if (!diffMaster.isEmpty() || !diffSlave.isEmpty()) {
            String keysMaster = "["+Rez.textSet2String(diffMaster, ", ")+"]";
            String keysSlave  = "["+Rez.textSet2String(diffSlave, ", ")+"]";
            error(String.format("IniMap: Missing keys: %s. Surplus keys: %s", keysMaster, keysSlave), true);
            return false;
        }
        return true;
    }

    @Override
    public int size() {
        return hashmap!=null ? hashmap.size() : 0;
    }

    @Override
    public boolean isEmpty() {
        return hashmap == null || hashmap.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return hashmap != null && hashmap.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return hashmap != null && hashmap.containsValue(o);
    }

    @Override
    public String get(Object o) {
        String ret = hashmap!=null ? hashmap.get(o) : null;
        if (ret == null) {
            error(String.format("IniMap: attempted reading inexistent key: %s", o), false);
        }
        return ret;
    }

    @Override
    public String put(String k, String v) {
        if (0!=(flags&ACC_RW)) {
            return hashmap!=null ? hashmap.put(k, v) : null;
        } else {
            error(String.format("IniMap: attempted RO putting %s : %s", k, v), false);
            return null;
        }
    }

    @Override
    public String remove(Object o) {
        if (0!=(flags&ACC_RW)) {
            return hashmap!=null ? hashmap.remove(o) : null;
        } else {
            error(String.format("IniMap: attempted RO removing object: %s", o), false);
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        if (0!=(flags&ACC_RW)) {
            if (hashmap!=null) hashmap.putAll(map);
        } else {
            error("IniMap: attempted RO putAll!", false);
        }
    }

    @Override
    public void clear() {
        if (0!=(flags&ACC_RW)) {
            if (hashmap!=null) hashmap.clear();
        } else {
            error("IniMap: attempted RO clearing!", false);
        }
    }

    @Override
    public Set<String> keySet() {
        return hashmap!=null ? hashmap.keySet() : new HashSet<String>(); //@NotNull
    }

    @Override
    public Collection<String> values() {
        return hashmap!=null ? hashmap.values() : new Vector<String>(); //@NotNull
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return hashmap!=null ? hashmap.entrySet() : new HashSet<Entry<String, String>>(); //@NotNull
    }

    private void error(String str, boolean isForced) {
        error(flags, str, isForced);
    }

    private static void error(int flags, String str, boolean isForced) {
        if (isForced || 0!=(flags & ERR_WARN)) {
            System.err.println(str);
        }
    }
}
