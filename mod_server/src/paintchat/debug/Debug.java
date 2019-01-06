package paintchat.debug;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import paintchat.Res;

public class Debug implements DebugListener {
    private DebugListener listener = null;
    private Res res = null;
    public BufferedWriter wFile = null;
    public boolean bool_debug = false;

    public Debug() {
    }

    public Debug(DebugListener var1, Res var2) {
        this.listener = var1;
        this.res = var2;
    }

    public Debug(Res var1) {
        this.res = var1;
    }

    protected void finalize() throws Throwable {
        this.mDestroy();
    }

    public void log(Object var1) {
        try {
            if (this.listener != null) {
                this.listener.log(var1);
            } else {
                this.logSys(var1);
            }

            this.writeLogFile(var1);
        } catch (RuntimeException var3) {
            System.out.println("debug_log:" + var3.getMessage());
        }

    }

    public void logDebug(Object var1) {
        if (this.bool_debug) {
            this.log(var1);
        }
    }

    public void logRes(String var1) {
        this.log(this.res.get(var1));
    }

    public void logSys(Object var1) {
        try {
            if (var1 == null) {
                var1 = "null";
            }

            if (var1 instanceof Throwable) {
                ((Throwable) var1).printStackTrace();
            } else {
                System.out.println(var1.toString());
            }
        } catch (RuntimeException var3) {
            var3.printStackTrace();
        }

    }

    public void newLogFile(String var1) {
        if (this.wFile != null) {
            this.setFileWriter(var1);
        }
    }

    private void writeLogFile(Object var1) {
        String var2 = var1 == null ? "null" : var1.toString();
        synchronized (this) {
            BufferedWriter var4 = this.wFile;

            try {
                if (var4 == null) {
                    return;
                }

                var4.write(var2);
                var4.newLine();
            } catch (IOException var6) {
                try {
                    var4.close();
                } catch (IOException var5) {
                }

                this.wFile = null;
            }

        }
    }

    public synchronized void setListener(DebugListener var1) {
        this.listener = var1;
    }

    public synchronized void setResource(Res var1) {
        this.res = var1;
    }

    public void setDebug(boolean var1) {
        this.bool_debug = var1;
    }

    public synchronized void setFileWriter(String var1) {
        try {
            if (this.wFile != null) {
                this.wFile.flush();
                this.wFile.close();
            }

            this.wFile = new BufferedWriter(new FileWriter(var1));
        } catch (IOException var3) {
            System.out.println("debug:" + var3);
            this.wFile = null;
        }

    }

    public void mDestroy() {
        this.listener = null;
        BufferedWriter var1 = this.wFile;
        this.wFile = null;

        try {
            var1.flush();
            var1.close();
        } catch (IOException var2) {
        }

    }
}
