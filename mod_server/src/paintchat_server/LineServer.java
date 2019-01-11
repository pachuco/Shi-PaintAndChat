package paintchat_server;

import java.net.InetAddress;

import paintchat.Config;
import paintchat.M;
import paintchat.debug.Debug;
import syi.util.ByteStream;

public class LineServer {
    private boolean isLive = true;
    private LineTalker[] talkers = new LineTalker[15];
    private int countTalker = 0;
    private boolean isCash = true;
    private ByteStream bCash = new ByteStream(100000);
    private ByteStream bWork = new ByteStream();
    private M mgCash = null;
    private M mgWork = new M();
    private PchOutputStreamForServer bLog;
    private Debug debug;
    private Server server;
    public Config config;

    public synchronized void mInit(Config var1, Debug var2, Server var3) {
        this.debug = var2;
        this.config = var1;
        this.server = var3;
        this.bLog = new PchOutputStreamForServer(this.config);
        this.isCash = var1.getBool("Server_Cash_Line", true);
    }

    public synchronized void mStop() {
        if (this.isLive) {
            this.isLive = false;

            for (int var1 = 0; var1 < this.countTalker; ++var1) {
                LineTalker var2 = this.talkers[var1];
                if (var2 != null) {
                    var2.mStop();
                }
            }

            if (this.bLog != null) {
                this.writeLog();
                this.bLog.close();
            }

        }
    }

    protected void finalize() throws Throwable {
        this.isLive = false;
    }

    public synchronized void addTalker(LineTalker var1) {
        LineTalker[] var2 = this.talkers;
        if (this.countTalker >= this.talkers.length) {
            LineTalker[] var3 = new LineTalker[this.countTalker + 1];
            System.arraycopy(var2, 0, var3, 0, this.countTalker);
            var2 = var3;
        }

        var2[this.countTalker] = var1;
        ++this.countTalker;
        if (this.talkers != var2) {
            this.talkers = var2;
        }

        var1.send(this.bCash, this.mgWork, this.bWork);
        this.bLog.getLog(var1.getLogArray());
    }

    public synchronized void removeTalker(LineTalker var1) {
        int var2 = this.countTalker;

        for (int var3 = 0; var3 < var2; ++var3) {
            if (var1 == this.talkers[var3]) {
                this.removeTalker(var3);
                break;
            }
        }

    }

    private void removeTalker(int var1) {
        LineTalker var2 = this.talkers[var1];
        this.talkers[var1] = null;
        if (var2 != null) {
            var2.mStop();
        }

        if (var1 + 1 < this.countTalker) {
            System.arraycopy(this.talkers, var1 + 1, this.talkers, var1, this.countTalker - (var1 + 1));
            this.talkers[this.countTalker - 1] = null;
        }

        --this.countTalker;
    }

    public synchronized void addLine(LineTalker var1, ByteStream var2) {
        try {
            int var4 = 0;

            while (true) {
                while (var4 < this.countTalker) {
                    LineTalker var3 = this.talkers[var4];
                    if (var3 != null && var3.isValidate()) {
                        if (var3 != var1) {
                            var3.send(var2, this.mgWork, this.bWork);
                        }

                        ++var4;
                    } else {
                        this.removeTalker(var4);
                        var4 = 0;
                    }
                }

                this.addCash(var2, false);
                break;
            }
        } catch (Throwable ex) {
            this.debug.log(ex.getMessage());
        }

    }

    public void addClear(LineTalker var1, ByteStream var2) {
        this.addLine(var1, var2);
        this.server.sendClearMessage(var1.getAddress());
        this.newLog();
    }

    private void addCash(ByteStream var1, boolean var2) {
        if (this.isCash && var1 != null) {
            int var3 = var1.size();
            if (var3 > 0) {
                if (var2 || var3 + this.bCash.size() > 60000) {
                    this.writeLog();
                }

                try {
                    int var4 = 0;

                    for (byte[] var5 = var1.getBuffer(); var4 < var3; this.mgCash.set(this.mgWork)) {
                        var4 += this.mgWork.set(var5, var4);
                        this.mgWork.get(this.bCash, this.bWork, this.mgCash);
                        if (this.mgCash == null) {
                            this.mgCash = new M();
                        }
                    }
                } catch (RuntimeException var6) {
                    this.debug.log(var6);
                }

            }
        }
    }

    private void writeLog() {
        if (this.bCash.size() > 0) {
            this.bLog.write(this.bCash.getBuffer(), 0, this.bCash.size());
            this.bCash.reset();
            this.mgCash = null;
        }
    }

    public int getUserCount() {
        return this.countTalker;
    }

    public synchronized LineTalker getTalker(InetAddress var1) {
        for (int var3 = 0; var3 < this.countTalker; ++var3) {
            LineTalker var2 = this.talkers[var3];
            if (var2 != null && var1.equals(var2.getAddress())) {
                return var2;
            }
        }

        return null;
    }

    public synchronized LineTalker getTalkerAt(int var1) {
        return var1 >= 0 && var1 < this.countTalker ? this.talkers[var1] : null;
    }

    public synchronized void newLog() {
        this.writeLog();
        this.bLog.newLog();
    }
}
