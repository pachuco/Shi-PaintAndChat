package jaba.applet;

import java.io.*;
import java.util.ArrayList;

public class IniFile {

    public static String DEFAULT_SECTION = null;

    private ArrayList strvec; //non-threadsafe, faster
    private int[] sectLocs;
    private int sectnum;
    private boolean isLoaded;


    public IniFile() {
        strvec = new ArrayList();
        sectLocs = new int[256]; //assumption
        reset();
    }

    public void openIni(File f) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;

        reset();
        try {
            for (int i = 0; ; i++) {
                line = br.readLine();
                if (line == null) break;
                line = line.trim();

                if (validBlank(line)) {
                    //decoration blank line
                } else if (validKeyValue(line)) {
                    //key=value
                } else if (validSection(line)) {
                    //[section]
                    sectLocs[sectnum++] = i;
                } else if (validComment(line)) {
                    //;comment
                } else {
                    //invalid crud
                    continue;
                }
                strvec.add(line);
            }

            isLoaded = true;
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeIni(File f) {

    }

    public void reset() {
        strvec.clear();
        for (int i = 0; i < sectLocs.length; i++) sectLocs[i] = -1;
        sectnum = 0;
        isLoaded = false;
    }

    public boolean isIniLoaded() {
        return isLoaded;
    }

    public boolean hasSection(String sect) {
        if (sect == null) return false;
        sect = "[" + sect.trim().toLowerCase() + "]";

        for (int i=0; i<sectnum; i++) {
            String line = strvec.get(sectLocs[i]).toString().trim().toLowerCase();
            if (line == sect) return true;
        }
        return false;
    }

    public boolean delParameter(String sect, String key) {
        return false;
        //same as below, but in reverse for sectLoc
        //maybe...
    }

    public boolean setParameter(String sect, String key, String value) {
        return false;
        //if section does not exist, create it at end of file
        //if key does not exist, create at end of section
        //walking is done in reverse for creation, and the first row after a valid non-empty line is used
        //an empty line may be added after
        //for adding a new key, sectLoc items above current section are incremented
        //for adding a new section, same is done, but contents are shifted up the array
    }

    public String getParameter(String sect, String key) {
        int sectStart;

        key = key.trim().toLowerCase();
        sect = sect == null ? null : "[" + sect.trim().toLowerCase() + "]";

        if (sect == null) {
            sectStart = 0;
            return procSection(sectStart, key);
        } else {
            for (int i = 0; i < sectnum; i++) {
                sectStart = sectLocs[i];
                String tsect = strvec.get(sectStart).toString().trim().toLowerCase();
                if (sect.equals(tsect)) {
                    return procSection(sectStart, key);
                }
            }
        }
        return null;
    }

    private String procSection(int sectStart, String key) {
        int vSize = strvec.size();

        for (int j = sectStart + 1; j < vSize; j++) {
            String line = strvec.get(j).toString().trim();
            if (validSection(line)) {
                break;
            } else if (validKeyValue(line)) {
                int eqLoc = line.indexOf('=');
                String tkey = line.substring(0, eqLoc).trim().toLowerCase();
                if (key.equals(tkey)) {
                    return line.substring(eqLoc + 1).trim().toLowerCase().split(";")[0];
                }
            }
        }
        return null;
    }

    private final boolean validKeyValue(String str) {
        return str.contains("=");
    }

    private final boolean validSection(String str) {
        return str.startsWith("[") && str.endsWith("]");
    }

    private final boolean validComment(String str) {
        return str.startsWith(";");
    }

    private final boolean validBlank(String str) {
        return str.equals("");
    }

}
