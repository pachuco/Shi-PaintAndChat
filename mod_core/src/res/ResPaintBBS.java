package res;

import syi.util.cust.*;

import java.io.*;

public class ResPaintBBS {
    public static IniMap
            langPBBS;

    public ResPaintBBS() {
        String PATH_LANG = "/res/lang/paintbbs_%s.ini";
        String SYSLANG   = System.getProperty("user.language");
        RFile langFile = new RFile(RFile.F_CP, String.format(PATH_LANG, SYSLANG));

        try {
            langPBBS = new IniMap(langFile.getInputStream(), null, IniMap.ACC_RO, "lang_"+SYSLANG);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load langfile: " + langFile.getPath());
        }

        if (!RFile.isEnvironmentJar()) Rez.testValidateAllLanguages("/res/lang/", "paintbbs_", "paintbbs_en.ini");
    }
}
