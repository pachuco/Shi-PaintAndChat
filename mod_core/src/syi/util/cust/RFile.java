package syi.util.cust;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/** Class for handling files relative to either app folder
 *   or classpath(may be packed jar or folder).
 *  API imitates java.io.File.
 *  Folder paths MUST end with slash character.
 */

public class RFile {
    private String relativePath;
    private String basePath;
    private boolean isJar;
    private int fileType;

    private ZipFile zip;
    private File file;

    public static final int
            F_FS = 0,   //file from appfolder
            F_CP = 1;   //file from classpath

    public RFile(RFile rf, String ... relPathArgs) {
        this(rf.fileType, rf.relativePath, Text.pathJoin(relPathArgs));
    }

    public RFile(int fileType, String ... relPathArgs) {
        this.relativePath = Text.pathJoin(relPathArgs);
        this.isJar = isEnvironmentJar();
        this.fileType = fileType;
        if (fileType == F_FS) {
            if (isJar) {
                //parent folder from jar
                basePath = new File(RFile.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
            } else {
                //current dir environment var;
                basePath = System.getProperty("user.dir");
            }
            file = new File(basePath, relativePath);
        } else if (fileType == F_CP) {
            basePath = Text.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            if (isJar) {
                try {
                    zip = new ZipFile(basePath);
                } catch (IOException ex) {
                    ex.printStackTrace(); //should not happen
                }
            } else {
                file = new File(basePath, relativePath);
            }
        }
    }

    public String getParent() {
        return Text.pathParent(relativePath);
    }

    public RFile getParentFile() {
        return new RFile(fileType, Text.pathParent(relativePath));
    }

    public String[] list() {
        if (fileType == F_CP && isJar) {
            ArrayList<String> tmp = new ArrayList<String>();
            ZipEntry baseEntry = zip.getEntry(relativePath);
            Enumeration<? extends ZipEntry> entries;

            if (baseEntry==null || !baseEntry.isDirectory()) return null;
            entries = zip.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (Text.pathIsDirectChild(relativePath, name)) tmp.add(name);
            }
            return tmp.toArray(new String[tmp.size()]);
        } else {
            String[] nameList = file.list();
            for (int i=0; i<nameList.length; i++) {
                nameList[i] = Text.pathStripAbsolute(basePath, Text.pathJoin(relativePath,nameList[i]));
            }
            return nameList;
        }
    }

    public RFile[] listFiles() {
        String[] nameList = list();
        RFile[] fileList;
        if (nameList==null) return null;
        fileList = new RFile[nameList.length];
        for (int i=0; i<fileList.length; i++) fileList[i] = new RFile(fileType, nameList[i]);
        return fileList;
    }

    public InputStream getInputStream() throws IOException {
        if (fileType == F_CP && isJar) {
            ZipEntry zipEntry = zip.getEntry(relativePath);
            if (zipEntry== null) throw new IOException("Null zipentry: " + relativePath);
            return zip.getInputStream(zipEntry);
        } else {
            return new FileInputStream(file);
        }
    }

    public OutputStream getOutputStream() throws IOException {
        if (fileType == F_CP) {
            throw new IOException("RFile: Classpath resources are read-only!");
        } else {
            return new FileOutputStream(file);
        }
    }

    public boolean isDirectory() {
        if (fileType == F_CP && isJar) {
            return zip.getEntry(relativePath).isDirectory();
        } else {
            return file.isDirectory();
        }
    }

    public String getName() {
        return Text.pathName(relativePath);
    }

    public String getPath() {
        return relativePath;
    }

    public boolean equals(RFile rf) {
        if (fileType != rf.fileType) return false;
        if (!basePath.equals(rf.basePath)) return false;
        if (!relativePath.equals(rf.relativePath)) return false;
        return true;
    }

    //add additional File methods as needed.

    public static boolean isEnvironmentJar() {
        return RFile.class.getResource("").getProtocol().equals("jar");
    }
}
