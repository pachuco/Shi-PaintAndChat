package paintchat_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import paintchat.MgText;
import paintchat.debug.DebugListener;
import syi.util.Io;
import syi.util.Vector2;

public class ChatLogOutputStream extends Writer {
    private boolean isValidate = true;
    BufferedWriter outLog = null;
    File fDir;
    DebugListener debug;
    long timeStart = 0L;

    public ChatLogOutputStream(File var1, DebugListener var2, boolean var3) {
        this.fDir = var1;
        this.debug = var2;
        this.isValidate = var3;
    }

    public synchronized void write(char var1) throws IOException {
        try {
            if (!this.isValidate) {
                return;
            }

            this.initFile();
            if (this.outLog != null) {
                this.outLog.write(var1);
            }
        } catch (IOException var3) {
            this.debug.log(var3);
        }

    }

    public synchronized void write(char[] var1, int var2, int var3) {
        try {
            if (!this.isValidate) {
                return;
            }

            this.initFile();
            if (this.outLog != null) {
                this.outLog.write(var1, var2, var3);
                this.outLog.newLine();
            }
        } catch (IOException var5) {
            this.debug.log(var5);
        }

    }

    public void write(String var1) {
        this.write((String) var1, 0, var1.length());
    }

    public synchronized void write(String var1, int var2, int var3) {
        try {
            if (!this.isValidate) {
                return;
            }

            this.initFile();
            if (this.outLog != null) {
                this.outLog.write(var1, var2, var3);
                this.outLog.newLine();
            }
        } catch (IOException var5) {
            this.debug.log(var5);
        }

    }

    public synchronized void write(MgText var1) {
        try {
            if (!this.isValidate) {
                return;
            }

            this.initFile();
            if (this.outLog == null) {
                return;
            }

            if (var1.bName != null) {
                this.outLog.write(var1.getUserName());
                this.outLog.write(62);
            }

            switch (var1.head) {
                case 0:
                case 6:
                case 8:
                    this.outLog.write(var1.toString());
                    this.outLog.newLine();
            }
        } catch (IOException var2) {
        }

    }

    private void initFile() {
        try {
            if (!this.isValidate) {
                return;
            }

            long var1 = System.currentTimeMillis();
            if (this.outLog == null || this.timeStart != 0L && var1 - this.timeStart >= 86400000L) {
                this.timeStart = var1;
                boolean var10000;
                if (this.outLog == null) {
                    var10000 = true;
                } else {
                    var10000 = false;
                }

                if (!this.fDir.isDirectory()) {
                    this.fDir.mkdirs();
                }

                if (this.outLog != null) {
                    try {
                        this.outLog.flush();
                        this.outLog.close();
                    } catch (IOException var4) {
                        this.debug.log(var4);
                    }

                    this.outLog = null;
                }

                File var3 = this.getLogFile();
                this.outLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var3, true), "UTF8"), 1024);
            }
        } catch (IOException var5) {
            this.debug.log(var5);
        }

    }

    public synchronized void close() {
        try {
            if (this.outLog != null) {
                this.outLog.flush();
                this.outLog.close();
                this.outLog = null;
                File var1 = this.getLogFile();
                if (var1.isFile() && var1.length() <= 0L) {
                    var1.delete();
                }
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    private File getLogFile() throws IOException {
        return new File(Io.getDateString("text_log", "txt", this.fDir.getCanonicalPath()));
    }

    public File getTmpFile() {
        return new File(this.fDir, "text_cash.tmp");
    }

    public void loadLog(Vector2 var1, int var2, boolean var3) {
        File var4 = this.getTmpFile();
        if (var4.isFile() && var4.length() > 0L) {
            BufferedReader var5 = null;

            try {
                var5 = new BufferedReader(new InputStreamReader(new FileInputStream(var4), "UTF8"));

                String var6;
                while ((var6 = var5.readLine()) != null) {
                    var1.add((Object) (new MgText(0, (byte) 6, var6)));
                    if (var1.size() >= var2) {
                        var1.remove(5);
                    }
                }
            } catch (IOException var8) {
            }

            try {
                if (var5 != null) {
                    var5.close();
                }
            } catch (IOException var7) {
            }

            if (var3) {
                var4.delete();
            }

        }
    }

    public void flush() throws IOException {
        if (this.outLog != null) {
            this.outLog.flush();
        }

    }
}
