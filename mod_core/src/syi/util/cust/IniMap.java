package syi.util.cust;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;


/** Class for reading INI sections from files.
 *  Writing #NOTRIM in the first line prevents
 *   default trimming of lines, making this
 *   suitable for language files.
 *  Initiating IniMap with a debugIdentifier
 *   enables error messages.
 */

public class IniMap implements Map<String,String> {
    public static int
        ACC_RW = 0,     //keys can be read and written
        ACC_RO = 1;     //keys can be read only

    private HashMap<String, String> hashmap;
    private int flags;
    private String debugIdentifier;

    public IniMap(InputStream in, String section, int flags, String debugIdentifier) {
        this(in, section, flags);
        this.debugIdentifier = debugIdentifier;
    }

    public IniMap(InputStream in, String section, int flags) {
        BufferedReader bIn = new BufferedReader(new InputStreamReader(in), 512);
        HashMap<String, String> hmRet = new HashMap<String, String>();
        this.flags = flags;

        try {
            boolean isSkip = section!=null;
            boolean isTrim = true;
            String line;

            try {
                for (int i = 1; (line = bIn.readLine()) != null; i++) {
                    if (i == 1 && (line.startsWith("#") || line.startsWith(";"))) {
                        if (line.substring(1).startsWith("NOTRIM")) isTrim = false;
                    }
                    if (isTrim) line = line.trim();
                    if (isSkip) {
                        if (line.startsWith("[") && line.equals("[" + section + "]")) isSkip = false;
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
                            hmRet.put(split[0], Text.unescape(split[1]));
                        }
                    }
                }
                hashmap = hmRet;
            } finally {
                bIn.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void populateClass(Class cls, Object _this, String prefix) {
        Field[] fields = cls.getDeclaredFields();
        Set<String> set = hashmap.keySet();

        for (Field fld : fields) {
            if (!set.contains(fld.getName().substring(prefix.length()))) {
                error(String.format("Missing key: %s", fld.getName()), false);
            }
        }
        for (String key : set) {
            try {
                Field fld = cls.getDeclaredField(prefix+key);
                fld.set(_this, hashmap.get(key));
            } catch (NoSuchFieldException ex) {
                error(String.format("Missing field: %s", prefix+key), false);
            } catch (IllegalAccessException ex) {
                error(String.format("Cannot set field: %s", prefix + key), false);
            }
        }
    }

    public boolean validateKeysAgainst(IniMap slave) {
        HashSet<String> diffMaster = new HashSet<String>(hashmap.keySet());
        HashSet<String> diffSlave  = new HashSet<String>(slave.keySet());
        diffMaster.removeAll(slave.keySet());
        diffSlave.removeAll(hashmap.keySet());

        if (!diffMaster.isEmpty() || !diffSlave.isEmpty()) {
            String keysMaster = "["+ Text.set2String(diffMaster, ", ")+"]";
            String keysSlave  = "["+ Text.set2String(diffSlave, ", ")+"]";
            error(String.format("Missing keys: %s. Surplus keys: %s", keysSlave, keysMaster), true);
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
            error(String.format("Attempted reading of inexistent key: %s", o), false);
        }
        return ret;
    }

    @Override
    public String put(String k, String v) {
        if (0!=(flags & ACC_RW)) {
            return hashmap!=null ? hashmap.put(k, v) : null;
        } else {
            error(String.format("Attempted RO putting key and value %s : %s", k, v), false);
            return null;
        }
    }

    @Override
    public String remove(Object o) {
        if (0!=(flags & ACC_RW)) {
            return hashmap!=null ? hashmap.remove(o) : null;
        } else {
            error(String.format("Attempted RO removal of key: %s", o), false);
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        if (0!=(flags & ACC_RW)) {
            if (hashmap!=null) hashmap.putAll(map);
        } else {
            error("Attempted RO putAll!", false);
        }
    }

    @Override
    public void clear() {
        if (0!=(flags & ACC_RW)) {
            if (hashmap!=null) hashmap.clear();
        } else {
            error("Attempted RO clearing!", false);
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
        if (debugIdentifier!=null) System.err.print("IniMap " + debugIdentifier + ": ");
        if (isForced || debugIdentifier!=null) {
            System.err.println(str);
        }
    }
}
