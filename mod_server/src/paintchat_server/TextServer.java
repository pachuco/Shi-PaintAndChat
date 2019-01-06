package paintchat_server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;

import paintchat.Config;
import paintchat.MgText;
import paintchat.debug.DebugListener;
import syi.util.Vector2;

public class TextServer {
    private boolean isLive = true;
    private TextTalkerListener[] talkers = new TextTalkerListener[10];
    private int countTalker = 0;
    private XMLTalker[] xmlTalkers = new XMLTalker[10];
    private int countXMLTalker = 0;
    private ChatLogOutputStream outLog;
    private DebugListener debug;
    private Config config;
    private String strPassword;
    private Vector2 mgtexts = new Vector2();
    private boolean isCash = true;
    private int iMaxCash;
    public Vector2 vKillIP = new Vector2();
    private int iUniqueLast = 1;
    private Server server;

    public void mInit(Server var1, Config var2, DebugListener var3) {
        if (this.isLive) {
            this.server = var1;
            this.config = var2;
            this.debug = var3;
            this.strPassword = var2.getString("Admin_Password", (String) null);
            boolean var4 = var2.getBool("Server_Log_Text", false);
            this.isCash = var2.getBool("Server_Cash_Text", true);
            File var5 = new File(var2.getString("Server_Log_Text_Dir", "save_server"));
            this.outLog = new ChatLogOutputStream(var5, var3, var4);
            this.iMaxCash = Math.max(var2.getInt("Server_Cash_Text_Size", 128), 5);
            if (var2.getBool("Server_Load_Text", false)) {
                this.outLog.loadLog(this.mgtexts, this.iMaxCash, true);
            }

        }
    }

    public synchronized void mStop() {
        if (this.isLive) {
            this.isLive = false;

            for (int var1 = 0; var1 < this.countTalker; ++var1) {
                TextTalkerListener var2 = this.talkers[var1];
                if (var2 != null) {
                    var2.mStop();
                }
            }

            if (this.outLog != null) {
                this.outLog.close();
            }

            if (this.mgtexts.size() > 0) {
                try {
                    BufferedWriter var6 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outLog.getTmpFile()), "UTF8"));
                    int var7 = this.mgtexts.size();

                    for (int var4 = 0; var4 < var7; ++var4) {
                        MgText var3 = (MgText) this.mgtexts.get(var4);
                        switch (var3.head) {
                            case 1:
                                var6.write("Login name=" + var3.toString());
                                break;
                            case 2:
                                var6.write("Logout name=" + var3.getUserName());
                                break;
                            default:
                                var6.write(var3.getUserName() + '>' + var3.toString());
                        }

                        var6.newLine();
                    }

                    var6.flush();
                    var6.close();
                } catch (IOException var5) {
                }
            }

        }
    }

    public synchronized void addTalker(TextTalkerListener var1) {
        if (this.isLive) {
            MgText var2 = var1.getHandleName();
            String var3 = var2.toString();
            if (var3.length() > 0 && var1.isValidate()) {
                var2.ID = this.getUniqueID();
                String var4 = this.getUniqueName(var3);
                if (var3 != var4) {
                    var2.setData(var2.ID, var2.head, this.getUniqueName(var3));
                    var3 = var2.toString();
                    var2.setUserName(var3);
                }

                var2.toBin(false);
                int var5 = this.countTalker;

                for (int var6 = 0; var6 < var5; ++var6) {
                    if (!this.talkers[var6].isGuest()) {
                        var1.send(this.talkers[var6].getHandleName());
                    }
                }

                if (this.isCash) {
                    var1.sendUpdate(this.mgtexts);
                }

                if (!var1.isGuest()) {
                    this.addText(var1, new MgText(var2.ID, (byte) 1, var3));
                }

                if (this.countTalker + 1 >= this.talkers.length) {
                    TextTalkerListener[] var7 = new TextTalkerListener[this.talkers.length + 1];
                    System.arraycopy(this.talkers, 0, var7, 0, this.talkers.length);
                    this.talkers = var7;
                }

                this.talkers[this.countTalker] = var1;
                ++this.countTalker;
            } else {
                var1.mStop();
            }
        }
    }

    public void doAdmin(String var1, int var2) {
        if (var1.indexOf("kill") >= 0) {
            this.killTalker(var2);
        } else {
            TextTalkerListener var3 = this.getTalker(var2);
            var3.send(new MgText(var2, (byte) 102, var1));
        }

    }

    public synchronized void removeTalker(TextTalkerListener var1) {
        if (this.isLive) {
            boolean var2 = var1.isGuest();
            var1.mStop();
            MgText var3 = new MgText();
            var3.setData(var1.getHandleName());
            var3.head = 2;
            var3.toBin(false);
            int var4 = this.countTalker;

            for (int var5 = 0; var5 < var4; ++var5) {
                if (this.talkers[var5] == var1) {
                    this.talkers[var5] = null;
                    if (var5 + 1 < var4) {
                        System.arraycopy(this.talkers, var5 + 1, this.talkers, var5, var4 - (var5 + 1));
                        this.talkers[var4 - 1] = null;
                    }

                    --this.countTalker;
                    --var4;
                    --var5;
                } else if (!var2) {
                    this.talkers[var5].send(var3);
                }
            }

        }
    }

    public synchronized void killTalker(int var1) {
        TextTalkerListener var2 = this.getTalker(var1);
        if (var2 != null) {
            MgText var3 = var2.getHandleName();
            this.vKillIP.add((Object) var2.getAddress());
            var2.kill();
            this.addText(var2, new MgText(0, (byte) 6, "kill done name=+" + var3.toString() + " address=" + var2.getAddress()));
        }
    }

    public synchronized void addText(TextTalkerListener var1, MgText var2) {
        if (this.isLive) {
            for (int var3 = 0; var3 < this.countTalker; ++var3) {
                TextTalkerListener var4 = this.talkers[var3];
                if (var4 != var1 && var4 != null) {
                    if (var4.isValidate()) {
                        var4.send(var2);
                    } else {
                        this.removeTalker(var4);
                        --var3;
                    }
                }
            }

            if (this.isCash) {
                this.mgtexts.add((Object) var2);
                if (this.mgtexts.size() >= this.iMaxCash) {
                    this.mgtexts.remove(5);
                }
            }

            if (this.outLog != null) {
                this.outLog.write(var2);
            }

        }
    }

    public int getUserCount() {
        return this.countTalker;
    }

    public void clearKillIP() {
        this.vKillIP.removeAll();
    }

    public synchronized void clearDeadTalker() {
        for (int var2 = 0; var2 < this.countTalker; ++var2) {
            TextTalkerListener var1 = this.talkers[var2];
            if (var1 == null || !var1.isValidate()) {
                if (var1 != null) {
                    this.removeTalker(var1);
                } else {
                    if (var2 < this.countTalker - 1) {
                        System.arraycopy(this.talkers, var2 + 1, this.talkers, var2, this.countTalker - (var2 + 1));
                    }

                    --this.countTalker;
                }

                var2 = -1;
            }
        }

    }

    public synchronized TextTalkerListener getTalker(InetAddress var1) {
        if (var1 == null) {
            return null;
        } else {
            for (int var2 = 0; var2 < this.countTalker; ++var2) {
                TextTalkerListener var3 = this.talkers[var2];
                if (var3 != null && var3.getAddress().equals(var1)) {
                    return var3;
                }
            }

            return null;
        }
    }

    public synchronized TextTalkerListener getTalker(int var1) {
        if (var1 <= 0) {
            return null;
        } else {
            for (int var2 = 0; var2 < this.countTalker; ++var2) {
                TextTalkerListener var3 = this.talkers[var2];
                if (var3 != null && var3.getHandleName().ID == var1) {
                    return var3;
                }
            }

            return null;
        }
    }

    public synchronized TextTalkerListener getTalkerAt(int var1) {
        return var1 >= 0 && var1 < this.countTalker ? this.talkers[var1] : null;
    }

    private int getUniqueID() {
        int var1 = this.iUniqueLast;
        int var3 = 0;

        while (var3 < this.countTalker) {
            TextTalkerListener var2;
            if ((var2 = this.talkers[var3++]) != null && var2.getHandleName().ID == var1) {
                ++var1;
                if (var1 >= 65535) {
                    var1 = 1;
                }

                var3 = 0;
            }
        }

        this.iUniqueLast = var1 >= 65535 ? 1 : var1 + 1;
        return var1;
    }

    private String getUniqueName(String var1) {
        int var2 = 2;
        String var3 = var1;
        int var5 = 0;

        while (var5 < this.countTalker) {
            TextTalkerListener var4;
            if ((var4 = this.talkers[var5++]) != null && var3.equals(var4.getHandleName().toString())) {
                var3 = var1 + var2;
                ++var2;
                var5 = 0;
            }
        }

        return var3;
    }

    public String getPassword() {
        return this.strPassword;
    }

    public synchronized void getUserListXML(StringBuffer var1) {
        int var5 = this.countTalker;

        for (int var6 = 0; var6 < var5; ++var6) {
            TextTalkerListener var2 = this.talkers[var6];
            if (var2 != null && !var2.isGuest()) {
                MgText var4 = var2.getHandleName();
                String var3 = var4.toString();
                if (var3 != null && var3.length() > 0) {
                    var1.append("<in id=\"" + var4.ID + "\">" + var3 + "</in>");
                }
            }
        }

    }

    public synchronized MgText getInfomation() {
        return this.server.getInfomation();
    }

    public void writeLog(String var1) {
        if (this.outLog != null) {
            this.outLog.write(var1);
        }

    }
}
