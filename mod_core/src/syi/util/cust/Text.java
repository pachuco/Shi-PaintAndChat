package syi.util.cust;
import java.util.*;

public class Text {

    public static String unescape(String str) {
        //add other stuff to unescape, as needed
        str = str.replaceAll("(?<!\\\\)\\\\n", "\n");
        str = str.replaceAll("\\\\\\\\", "\\\\");
        return str;
    }

    public static String set2String(HashSet<String> set, String separator) {
        return String.join(separator, set.toArray(new String[set.size()]));
    }

    public static String pathJoin(String ... args) {
        String ret = "";
        for (String str : args) {
            if (ret.endsWith("/") && str.startsWith("/")) {
                str = str.substring(1);
            } else if (!ret.endsWith("/") && !str.startsWith("/")) {
                ret += "/";
            }
            ret += str;
        }
        if (ret.startsWith("/")) ret = ret.substring(1);
        if (ret.isEmpty()) ret = "/";
        return ret;
    }

    public static int pathDepth(String relPath) {
        if (relPath.startsWith("/")) relPath = relPath.substring(1);
        if (relPath.endsWith("/")) relPath = relPath.substring(0, relPath.length()-1);
        String[] split = relPath.split("/");
        if (split.length==1 && split[0].isEmpty()) return 0;
        return split.length;
    }

    public static String pathParent(String relPath) {
        int index;
        if (relPath.endsWith("/")) relPath = relPath.substring(0, relPath.length()-1);
        index = relPath.lastIndexOf('/');
        if (index>=0 && index<relPath.length()) {
            relPath = relPath.substring(0, index+1);
        } else {
            return "/";
        }
        if (relPath.startsWith("/")) relPath = relPath.substring(1);
        if (relPath.isEmpty()) relPath = "/";
        return relPath;
    }

    public static boolean pathIsChild(String master, String child) {
        return child.startsWith(master);
    }

    public static boolean pathIsDirectChild(String master, String child) {
        return pathIsChild(master, child) && pathDepth(child)==(pathDepth(master)+1);
    }

    public static String pathName(String relPath) {
        if (relPath.endsWith("/")) relPath.substring(0, relPath.length()-1);
        String[] split = relPath.split("/");
        return split[split.length-1];
    }

    public static String pathStripAbsolute(String absPath, String relPath) {
        if (relPath.startsWith(absPath)) {
            relPath = relPath.substring(absPath.length(), relPath.length());
        }
        return relPath;
    }
}
