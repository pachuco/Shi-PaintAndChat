package paintchat_server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import paintchat.Config;
import paintchat.MgText;
import paintchat.Res;
import paintchat.admin.LocalAdmin;
import paintchat.debug.Debug;
import syi.util.Io;
import syi.util.ThreadPool;

public class Server implements Runnable {
    public static final String STR_VERSION = "(C)しぃちゃん PaintChatServer v3.57b";
    public static final String FILE_CONFIG = "cnf/paintchat.cf";
    private TalkerInstance talkerInstance;
    private boolean live = true;
    private boolean isLiveThread = true;
    private boolean isOnlyServer;
    private Config config = null;
    public Debug debug = null;
    private ServerSocket sSocket;
    private Thread tConnect = null;
    private Thread tLive = null;
    TextServer textServer;
    LineServer lineServer;

    public Server(String var1, Config var2, Debug var3, boolean var4) {
        this.config = var2;
        this.isOnlyServer = var4;
        if (var1 != null && var1.length() > 0) {
            var2.put("Admin_Password", var1);
        }

        this.debug = new Debug(var3, (Res) null);
    }

    public synchronized void exitServer() {
        if (this.live) {
            this.live = false;

            try {
                if (this.sSocket != null) {
                    try {
                        this.sSocket.close();
                        this.sSocket = null;
                    } catch (Exception var4) {
                    }
                }

                Thread var1 = Thread.currentThread();
                if (this.tConnect != null && this.tConnect != var1) {
                    try {
                        this.tConnect.interrupt();
                        this.tConnect = null;
                    } catch (Exception var3) {
                    }
                }

                if (this.tLive != null && this.tLive != var1) {
                    try {
                        this.tLive.interrupt();
                        this.tLive = null;
                    } catch (Exception var2) {
                    }
                }

                this.lineServer.mStop();
                this.textServer.mStop();
                this.debug.log("PaintChatサーバーを終了させます");
            } catch (Throwable ex) {
            }

            if (this.isOnlyServer) {
                System.exit(0);
            }

        }
    }

    public void init() {
        try {
            Thread var1 = new Thread(this, "init");
            var1.setDaemon(false);
            var1.start();
        } catch (Exception var2) {
            this.debug.log("init_thread" + var2);
        }

    }

    public void initMakeInfomation() {
        try {
            File var1 = new File(this.config.getString("File_PaintChat_Infomation", "./cnf/template/.paintchat"));
            File var2 = Io.getDirectory(var1);
            if (!var2.isDirectory()) {
                var2.mkdirs();
            }

            int var3 = this.sSocket.getLocalPort();
            int var4 = this.config.getInt("Client_Image_Width", 1200);
            int var5 = this.config.getInt("Client_Image_Height", 1200);
            String var6 = this.config.getString("Client_Sound", "false");
            int var7 = this.config.getInt("layer_count", 3);
            PrintWriter var8 = new PrintWriter(new FileWriter(var1), false);
            var8.println("Connection_Port_PaintChat=" + var3);
            var8.println("Client_Image_Width=" + var4);
            var8.println("Client_Image_Height=" + var5);
            var8.println("Client_Sound=" + var6);
            var8.println("layer_count=" + var7);
            var8.flush();
            var8.close();
            File var9 = new File("./cnf/template/.paintchat");
            if (!Io.getDirectory(var9).isDirectory()) {
                Io.getDirectory(var9).mkdirs();
            }

            if (!var9.equals(var1)) {
                Io.copyFile(var1, var9);
            }

            StringWriter var10 = new StringWriter();
            var10.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
            var10.write("<pchat_user_list port=\"" + var3 + "\" host=\"\" refresh=\"" + 15000 + "\" />\r\n");
            var10.write("<pchat_admin port=\"" + var3 + "\" host=\"\" />");
            var10.flush();
            var10.close();
            File var11 = new File(Io.getDirectory(var1), "pconfig.xml");
            var8 = new PrintWriter(new FileWriter(var11), false);
            var8.write(var10.toString());
            var8.flush();
            var8.close();
            File var12 = new File("./cnf/template/pconfig.xml");
            if (!var11.equals(var12)) {
                var8 = new PrintWriter(new FileWriter(var12));
                var8.write(var10.toString());
                var8.flush();
                var8.close();
            }
        } catch (Throwable ex) {
            this.debug.log(ex);
        }

    }

    public boolean isExecute(boolean var1) {
        Object var10000 = null;
        var10000 = null;
        var10000 = null;
        boolean var2 = false;

        try {
            Res var3 = new Res();
            var3.put("password", this.config.getString("Admin_Password", ""));
            LocalAdmin var4 = new LocalAdmin(var3, InetAddress.getLocalHost(), this.config.getInt("Connection_Port_PaintChat", Config.DEF_PORT));
            String var5 = var4.getString("ping");
            var2 = var5.indexOf("version=") >= 0;
            if (var2 && var1) {
                var5 = var4.getString("terminate");
                this.debug.log("server got terminated");
                System.exit(0);
            }

            return var2;
        } catch (Throwable ex) {
            if (var2) {
                this.debug.log("server have work now.");
            }

            return var2;
        }
    }

    public static void main(String[] var0) {
        try {
            String var1 = "cnf/paintchat.cf";
            String var2 = null;
            boolean var3 = false;
            if (var0 != null) {
                if (var0.length >= 1) {
                    var1 = var0[0];
                    if (var1.trim().length() <= 0) {
                        var1 = "cnf/paintchat.cf";
                    }
                }

                if (var0.length >= 2) {
                    if (var0[1].equalsIgnoreCase("exit")) {
                        var3 = true;
                    } else {
                        var2 = new String(var0[1]);
                    }
                }
            }

            System.out.println((new File(var1)).getCanonicalPath());
            Config var4 = new Config(var1);
            Server var5 = new Server(var2, var4, (Debug) null, true);
            if (var5.isExecute(var3)) {
                throw new Exception("既に起動しています。");
            }

            if (var3) {
                throw new Exception("既にサーバーは終了しているか、検索を失敗しました。");
            }

            var5.init();
        } catch (Throwable ex) {
            System.out.println("server_main" + ex.toString());
            System.exit(0);
        }

    }

    public synchronized void rInit() throws Throwable {
        if (this.live) {
            Config var1 = this.config;
            int var2 = var1.getInt("Connection_Port_PaintChat", Config.DEF_PORT);
            int var3 = var1.getInt("Connection_Max", 255);
            InetAddress.getLocalHost().getHostAddress();

            try {
                this.sSocket = new ServerSocket(var2, var3);
            } catch (IOException var5) {
                this.debug.log("Server error:" + var5.getMessage());
                throw var5;
            }

            var2 = this.sSocket.getLocalPort();
            this.textServer = new TextServer();
            this.textServer.mInit(this, this.config, this.debug);
            this.lineServer = new LineServer();
            this.lineServer.mInit(this.config, this.debug, this);
            this.talkerInstance = new TalkerInstance(this.config, this, this.textServer, this.lineServer, this.debug);
            this.initMakeInfomation();
            this.runServer();
            this.debug.log("Run ChatServer port=" + var2);
        }
    }

    public void run() {
        try {
            switch (Thread.currentThread().getName().charAt(0)) {
                case 'c':
                    this.runConnect();
                case 'd':
                case 'e':
                case 'f':
                case 'h':
                default:
                    break;
                case 'g':
                    this.runLive();
                    break;
                case 'i':
                    this.rInit();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void runConnect() throws InterruptedException {
        while (this.live) {
            try {
                Socket var1 = this.sSocket.accept();
                var1.setSoTimeout(180000);
                if (!this.isLiveThread) {
                    synchronized (this.tLive) {
                        this.tLive.notify();
                    }
                }

                this.talkerInstance.newTalker(var1);
            } catch (Throwable ex1) {
                try {
                    Thread.currentThread();
                    Thread.sleep(5000L);
                } catch (Throwable ex2) {
                }
            }
        }

        this.exitServer();
    }

    private void runLive() {
        long var1 = System.currentTimeMillis();
        long var3 = var1 + 86400000L;
        long var5 = var1 + 3600000L;

        while (this.live) {
            try {
                if (this.tLive == null) {
                    break;
                }

                Thread.currentThread();
                Thread.sleep(60000L);
                if (!this.live) {
                    break;
                }

                if (this.debug.bool_debug) {
                    int var12 = this.textServer.getUserCount();
                    int var8 = this.lineServer.getUserCount();
                    this.debug.logSys("pchats_gc:connect t=" + var12 + " l=" + var8);
                    this.debug.logSys("ThreadSleep=" + ThreadPool.getCountOfSleeping() + " ThreadWork=" + ThreadPool.getCountOfWorking());
                }

                this.textServer.clearDeadTalker();
                if (this.textServer.getUserCount() <= 0 && this.lineServer.getUserCount() <= 0) {
                    synchronized (this.tLive) {
                        this.debug.log("pchat_gc:suspend");
                        this.isLiveThread = false;
                        this.tLive.wait();
                        this.isLiveThread = true;
                    }

                    this.debug.log("pchat_gc:resume");
                }

                var1 = System.currentTimeMillis();
                if (var1 >= var5) {
                    var5 = var1 + 3600000L;
                    this.debug.log((new Date()).toString());
                }

                if (var1 >= var3) {
                    var3 += var1 + 86400000L;
                    this.debug.newLogFile(Io.getDateString("pserv_", "log", this.config.getString("Server_Log_Server_Dir", "save_server")));
                    this.textServer.clearKillIP();
                }
            } catch (Throwable ex1) {
                try {
                    this.debug.log(ex1);
                    Thread.currentThread();
                    Thread.sleep(1000L);
                } catch (Throwable ex2) {
                    break;
                }
            }
        }

        this.exitServer();
    }

    private void runServer() {
        this.tConnect = new Thread(this);
        this.tConnect.setDaemon(true);
        this.tConnect.setPriority(1);
        this.tConnect.setName("connection");
        this.tLive = new Thread(this);
        this.tLive.setDaemon(false);
        this.tLive.setPriority(1);
        this.tLive.setName("gc");
        this.tConnect.start();
        this.tLive.start();
    }

    public MgText getInfomation() {
        StringWriter var1 = new StringWriter();
        int var4 = 0;
        boolean var5 = true;
        var1.write("---------------<br><b>");

        TextTalkerListener var2;
        while ((var2 = this.textServer.getTalkerAt(var4)) != null) {
            ++var4;
            MgText var6 = var2.getHandleName();
            InetAddress var7 = var2.getAddress();
            LineTalker var3 = this.lineServer.getTalker(var7);
            if (var2.isValidate()) {
                if (!var5) {
                    var1.write("<br>");
                }

                var5 = true;
                if (var6 != null) {
                    var1.write(var6.toString() + "<br>");
                }

                if (var7 != null) {
                    var1.write("host=" + var7.toString() + "<br>");
                }

                var1.write("speak=" + var2.getSpeakCount() + "<br>");
                if (var3 != null && var3.isValidate()) {
                    var1.write("draw=" + var3.getDrawCount() + "<br>");
                }

                var1.write("-----<br>");
            }
        }

        var1.write("</b>---------------");
        var1.toString();
        return var4 <= 0 ? null : new MgText(0, (byte) 8, var1.toString());
    }

    public void sendClearMessage(InetAddress var1) {
        TextTalkerListener var2 = this.textServer.getTalker(var1);
        if (var2 != null) {
            MgText var3 = new MgText(0, (byte) 0, var2.getHandleName().getUserName() + " erased canvas.");
            var3.setUserName("PaintChat");
            this.textServer.addText(var2, var3);
        }

    }
}
