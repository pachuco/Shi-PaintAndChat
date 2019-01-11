package paintchat_http;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import paintchat.Config;
import paintchat.Res;
import paintchat.Resource;
import paintchat.debug.Debug;
import syi.util.PProperties;

public class HttpServer implements Runnable {
    private boolean live = true;
    private boolean isOnlyServer = false;
    public Debug debug = null;
    private ServerSocket ssocket;
    private Thread thread;
    public File dirCurrent = null;
    public File dirResource = null;
    public String dirLog;
    public static PProperties Mime = null;
    public static Config config = null;
    public static Res res = null;
    public static SimpleDateFormat fmt = null;
    public String strHost = null;
    public EOFException EOF = new EOFException();
    private HttpFiles httpFiles;
    static final String STR_FILE_MIME = "./cnf/mime.cf";
    public static final String STR_VERSION = "PaintChatHTTP/3.1";
    private static final String STR_TIME_FORMAT = "EEEE',' dd MMM yyyy HH:mm:ss 'GMT'";

    public HttpServer(Config var1, Debug var2, boolean var3) {
        res = Resource.loadResource("Http");
        this.debug = new Debug(var2, res);
        config = var1;
        this.isOnlyServer = var3;
    }

    public synchronized void exitServer() {
        if (this.live) {
            try {
                this.live = false;
                ServerSocket var1 = this.ssocket;
                this.ssocket = null;
                var1.close();
                var1 = null;
                if (this.thread != null && Thread.currentThread() != this.thread) {
                    this.thread.interrupt();
                    this.thread.join();
                    this.thread = null;
                }

                this.debug.log(res.get("Exit"));
            } catch (Throwable ex) {
                this.debug.log(res.get("Error") + ex.getMessage());
            }

            if (this.isOnlyServer) {
                System.exit(0);
            }

        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.exitServer();
    }

    public String getLogName(String var1, String var2, String var3) {
        String var4 = null;
        File var5 = new File(var3);
        if (!var5.exists()) {
            var5.mkdirs();
        }

        try {
            GregorianCalendar var6 = new GregorianCalendar(TimeZone.getDefault());
            String var7 = var1 + var6.get(2) + '-' + var6.get(5) + '_';

            for (int var8 = 0; var8 < 32767; ++var8) {
                File var9 = new File(var3, var7 + var8 + '.' + var2);
                if (!var9.exists()) {
                    var4 = var3 + "/" + var7 + var8 + var2;
                    break;
                }
            }
        } catch (RuntimeException var10) {
            this.debug.log("getDate" + var10);
        }

        return var4;
    }

    public void init(int var1) {
        try {
            Config var2 = config;
            this.debug.setDebug(var2.getBool("Server_Debug"));
            if (var2.getBool("Http_Log")) {
                this.debug.setFileWriter(this.getLogName("phttpd", "log", "save_log"));
            }

            if (Mime == null) {
                Mime = new PProperties();
                Mime.load((InputStream) (new FileInputStream("./cnf/mime.cf")));
            }

            if (this.debug.bool_debug) {
                this.debug.logDebug("Mimeロード");
            }

            fmt = new SimpleDateFormat("EEEE',' dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
            this.dirLog = config.getString("Http_Log.Dir");
            this.dirCurrent = new File(config.getString("Http_Dir", "./www/"));
            if (!this.dirCurrent.exists()) {
                this.dirCurrent.mkdirs();
            }

            this.dirResource = new File("./cnf/template/");
            this.strHost = InetAddress.getLocalHost().getHostName();
            this.httpFiles = new HttpFiles(config, res);
            this.ssocket = new ServerSocket(var1, 50);
            this.thread = new Thread(this);
            this.thread.setDaemon(false);
            this.thread.setPriority(1);
            this.thread.start();
            this.debug.log("Run HTTPD PORT:" + var1);
        } catch (Exception var3) {
            this.debug.log(res.get("Error") + var3.getMessage());
        }
    }

    public static void main(String[] var0) {
        try {
            int var1 = 80;
            String var2 = "./cnf/paintchat.cf";
            if (var0 != null && var0.length > 0) {
                try {
                    var1 = Integer.parseInt(var0[0]);
                } catch (Exception var5) {
                    var1 = 80;
                }

                if (var0.length >= 2) {
                    var2 = var0[1];
                }
            }

            Config var3 = new Config(var2);
            HttpServer var4 = new HttpServer(var3, (Debug) null, true);
            var4.init(var1);
        } catch (Exception var6) {
            System.out.println("http_main:" + var6.toString());
        }

    }

    public void run() {
        long curTime = System.currentTimeMillis();

        while (this.live) {
            try {
                Socket sock = null;
                sock = this.ssocket.accept();
                sock.setSoTimeout(30000);
                new TalkerHttp(sock, this, this.httpFiles);
            } catch (InterruptedIOException ex) {
                if (System.currentTimeMillis() - curTime > 86400000L) {
                    curTime = System.currentTimeMillis();
                    this.debug.newLogFile(this.getLogName("phttpd", "log", this.dirLog));
                }
            } catch (Throwable ex) {
                if (!this.live) {
                    break;
                }

                this.debug.log(res.get("Error") + ex.getMessage());
            }
        }

    }
}
