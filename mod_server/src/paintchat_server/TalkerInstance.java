package paintchat_server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import paintchat.Config;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.ByteInputStream;
import syi.util.Io;
import syi.util.ThreadPool;
import syi.util.Vector2;

public class TalkerInstance implements Runnable {
    private Socket socket;
    private InputStream In;
    private OutputStream Out;
    private TextServer textServer;
    private LineServer lineServer;
    private DebugListener debug;
    private Config serverStatus;
    private Server server;
    private String strPassword;
    boolean isAscii = false;

    public TalkerInstance(Config var1, Server var2, TextServer var3, LineServer var4, DebugListener var5) {
        this.server = var2;
        this.textServer = var3;
        this.lineServer = var4;
        this.serverStatus = var1;
        this.debug = var5;
        this.strPassword = var1.getString("Admin_Password", "");
    }

    public void run() {
        try {
            this.switchConnection(this.socket);
        } catch (Throwable ex) {
            ex.printStackTrace();
            this.closeSocket();
        }

    }

    public void newTalker(Socket var1) {
        TalkerInstance var2 = new TalkerInstance(this.serverStatus, this.server, this.textServer, this.lineServer, this.debug);
        var2.socket = var1;
        ThreadPool.poolStartThread(var2, "sock_switch");
    }

    private boolean isKillAddress(Socket socket) {
        try {
            InetAddress var2 = socket.getInetAddress();
            Vector2 var3 = this.textServer.vKillIP;

            for (int var5 = 0; var5 < var3.size(); ++var5) {
                InetAddress var4 = ((PaintChatTalker) var3.get(var5)).getAddress();
                if (var4.equals(var2)) {
                    return true;
                }
            }
        } catch (RuntimeException var6) {
            this.debug.log(var6);
        }

        return false;
    }

    private void switchConnection(Socket socket) throws IOException {
        ByteInputStream byteInStream = new ByteInputStream();
        InputStream inStream = socket.getInputStream();
        this.In = inStream;
        this.isAscii = Io.r(inStream) != 98;
        if (this.isAscii) {
            this.switchAsciiConnection();
        } else {
            int var4 = Io.readUShort(inStream);
            if (var4 <= 0) {
                throw new IOException("protocol unknown");
            } else {
                byte[] var5 = new byte[var4];
                Io.rFull(inStream, var5, 0, var4);
                Res var6 = new Res();
                byteInStream.setBuffer(var5, 0, var4);
                var6.load((InputStream) byteInStream);
                if (var6.getBool("local_admin")) {
                    this.switchLocalAdmin(socket, var6);
                } else {
                    Object var7 = null;
                    String var8 = var6.get("protocol", "");
                    if (var8.equals("paintchat.text")) {
                        var7 = new TextTalker(this.textServer, this.debug);
                    }

                    if (var8.equals("paintchat.line")) {
                        var7 = new LineTalker(this.lineServer, this.debug);
                    }

                    var8.equals("paintchat.infomation");
                    if (var7 != null) {
                        if (this.strPassword.length() > 0 && this.strPassword.equals(var6.get("password", ""))) {
                            var6.put("permission", "layer:all;canvas:true;talk:true;layer_edit:true;fill:true;clean:true;");
                        } else {
                            var6.put("permission", this.serverStatus.get("Client_Permission"));
                        }

                        ((PaintChatTalker) var7).mStart(this.socket, inStream, (OutputStream) null, var6);
                    }

                }
            }
        }
    }

    private void switchAsciiConnection() throws IOException {
        BufferedInputStream var1 = new BufferedInputStream(this.In);
        StringBuffer var2 = new StringBuffer();
        int var4 = 0;

        int var3;
        while ((var3 = Io.r(var1)) != -1) {
            var2.append((char) var3);
            if (var3 == 0) {
                break;
            }

            ++var4;
            if (var4 >= 255) {
                throw new IOException("abnormal");
            }
        }

        String var5 = var2.toString();
        String var6 = null;
        int var7 = var5.indexOf("type=");
        if (var7 <= 0) {
            var6 = "paintchat.text";
        } else {
            var7 += 6;
            int var8 = var5.indexOf(34, var7);
            if (var8 == -1) {
                var8 = var5.indexOf(39, var7);
            }

            var6 = var8 == -1 ? "paintchat.text" : var5.substring(var7, var8);
        }

        if (var6.equals("paintchat.infomation")) {
            this.switchLocalAdminXML(var5);
        } else {
            XMLTextTalker var9 = new XMLTextTalker(this.textServer, this.debug);
            if (var9 != null) {
                var9.mStart(this.socket, var1, (OutputStream) null, (Res) null);
            }

        }
    }

    private void switchLocalAdmin(Socket var1, Res var2) {
        try {
            String var3 = this.serverStatus.getString("Admin_Password");
            if (var1.getInetAddress().equals(InetAddress.getLocalHost()) || var3 != null && var3.length() > 0 && var3.equals(var2.get("password", ""))) {
                String var4 = var2.get("request", "ping");
                StringBuffer var5 = new StringBuffer();
                byte[] var6 = (byte[]) null;
                if (var4.equals("ping")) {
                    var5.append("response=ping ok\n");
                    var5.append("version=");
                    var5.append("(C)しぃちゃん PaintChatServer v3.57b");
                    var5.append("\n");
                    var6 = var5.toString().getBytes("UTF8");
                }

                if (var4.equals("exit")) {
                    var5.append("response=exit ok\n");
                    byte[] var7 = var5.toString().getBytes("UTF8");
                    this.Out = this.socket.getOutputStream();
                    Io.wShort(this.Out, var7.length);
                    this.Out.write(var7);
                    this.Out.flush();
                    this.closeSocket();
                    this.server.exitServer();
                    return;
                }

                if (var6 != null) {
                    this.Out = this.socket.getOutputStream();
                    Io.wShort(this.Out, var6.length);
                    this.Out.write(var6);
                    this.Out.flush();
                }
            }
        } catch (IOException var8) {
            this.debug.log(var8);
        }

        this.closeSocket();
    }

    private void switchLocalAdminXML(String var1) throws IOException {
        int var2 = var1.indexOf("request=");
        if (var2 < 0) {
            this.closeSocket();
        } else {
            var2 += 9;
            int var3 = var1.indexOf(34, var2);
            if (var3 < 0) {
                var3 = var1.indexOf(39, var2);
                if (var3 < 0) {
                    this.closeSocket();
                    return;
                }
            }

            if (var2 != var3) {
                String var4 = var1.substring(var2, var3);
                if (var4.equals("userlist")) {
                    this.Out = this.socket.getOutputStream();
                    StringBuffer var5 = new StringBuffer();
                    this.textServer.getUserListXML(var5);
                    var5.append('\u0000');
                    this.Out.write(var5.toString().getBytes("UTF8"));
                    this.closeSocket();
                } else if (var4.equals("infomation")) {
                    this.Out = this.socket.getOutputStream();
                    this.closeSocket();
                } else {
                    this.closeSocket();
                }
            }
        }
    }

    private void closeSocket() {
        try {
            if (this.In != null) {
                this.In.close();
                this.In = null;
            }

            if (this.Out != null) {
                this.Out.close();
                this.Out = null;
            }

            if (this.socket != null) {
                this.socket.close();
                this.socket = null;
            }
        } catch (IOException var2) {
            this.debug.log(var2);
        }

    }
}
