package res;

import syi.util.cust.*;

public class ResPaintBBS {
    public static IniMap
            langPBBS;

    public ResPaintBBS() {
        langPBBS = Rez.loadLangFile(RFile.F_CP, "/res/lang/paintbbs_%s.ini", null);
        if (!RFile.isEnvironmentJar()) Rez.testAllLanguages("/res/lang/", "paintbbs_", "paintbbs_en.ini");
    }
}
