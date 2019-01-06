package paintchat_http;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.util.Date;

import syi.util.ByteStream;
import syi.util.ThreadPool;

public class TalkerHttp implements Runnable {
    private HttpServer server;
    private HttpFiles files;
    private Socket socket;
    private InputStream In;
    private OutputStream Out;
    private Date date = new Date();
    private boolean isLineOut = false;
    private char[] bC = new char[350];
    private byte[] bB = new byte[350];
    private ByteStream bWork = new ByteStream();
    private String strMethod;
    private String strRequest;
    private static final byte[] strHttp = "HTTP/1.0".getBytes();
    private long sizeRequest = 0L;
    private String strIfMod = null;
    private int sizeIfMod = -1;
    private boolean isCash = true;
    private boolean isBody = true;
    private File fileRequest;
    private static final String STR_PRAGMA = "pragma";
    private static final String STR_LENGTH = "length=";
    private static final String STR_IF_MODIFIED_SINCE = "if-modified-since";
    private static final String STR_CONTENT_LENGTH = "content-length";
    private static final String STR_DEF_MIME = "application/octet-stream";
    private static final String[] strDef = new String[]{"GET", "HEAD", "/", "HTTP/1.0", "HTTP/1.1"};
    private static final String[] strDef2 = new String[]{"pragma", "if-modified-since", "content-length"};
    private static final EOFException EOF = new EOFException();
    private static final int I_OK = 0;
    private static final int I_NOT_MODIFIED = 1;
    private static final int I_MOVED_PERMANENTLY = 2;
    private static final int I_NOT_FOUND = 3;
    private static final int I_SERVER_ERROR = 4;
    private static final int I_SERVICE_UNAVALIABLE = 5;
    private static final byte[][] BYTE_RESPONCE = new byte[][]{" 200 OK".getBytes(), " 304 Not Modified".getBytes(), " 301 Moved Permanently".getBytes(), " 404 Not Found".getBytes(), " 500 Internal Server Error".getBytes(), " 503 Service Unavailable".getBytes()};
    private static final byte[] BYTE_DEF_HEADER = "Server: PaintChatHTTP/3.1".getBytes();
    private static final byte[] BYTE_CRLF = new byte[]{13, 10};
    private static final byte[] BYTE_LOCATION = "Location: ".getBytes();
    private static final byte[] BYTE_LAST_MODIFIED = "Last-Modified: ".getBytes();
    private static final byte[] BYTE_DATE = "Date: ".getBytes();
    private static final byte[] BYTE_CONTENT_LENGTH = "Content-Length: ".getBytes();
    private static final byte[] BYTE_CONTENT_TYPE = "Content-Type: ".getBytes();
    private static final byte[] BYTE_CONNECTION_CLOSE = "Connection: close".getBytes();

    public TalkerHttp(Socket var1, HttpServer var2, HttpFiles var3) {
        this.server = var2;
        this.socket = var1;
        this.files = var3;
        ThreadPool.poolStartThread(this, (String) null);
    }

    private void close() {
        if (this.socket != null) {
            try {
                this.Out.close();
                this.Out = null;
            } catch (IOException var3) {
            }

            try {
                this.In.close();
                this.In = null;
            } catch (IOException var2) {
            }

            try {
                this.socket.close();
                this.socket = null;
            } catch (IOException var1) {
            }

        }
    }

    private int getResponce() {
        try {
            boolean var1 = this.sizeIfMod != -1 && (long) this.sizeIfMod != this.fileRequest.length() || this.strIfMod != null && HttpServer.fmt.parse(this.strIfMod).getTime() == this.fileRequest.lastModified();
            return var1 && this.isCash ? 1 : 0;
        } catch (RuntimeException var2) {
        } catch (ParseException var3) {
        }

        return 0;
    }

    public byte[] getSince(long var1) {
        this.date.setTime(var1);
        return HttpServer.fmt.format(this.date).getBytes();
    }

    private String getString(char[] var1, int var2) {
        if (var2 == 0) {
            return null;
        } else {
            for (int var6 = 0; var6 < strDef.length; ++var6) {
                String var5 = strDef[var6];
                int var3 = var5.length();
                if (var2 == var3) {
                    int var4;
                    for (var4 = 0; var4 < var3 && var1[var4] == var5.charAt(var4); ++var4) {
                    }

                    if (var4 == var3) {
                        return var5;
                    }
                }
            }

            return new String(var1, 0, var2);
        }
    }

    private char r() throws IOException {
        int var1 = this.In.read();
        if (var1 == -1) {
            throw EOF;
        } else {
            return (char) var1;
        }
    }

    private void readMethod() throws IOException {
        try {
            while (true) {
                String var1;
                if ((var1 = this.readSpace()) == null) {
                    if (!this.isLineOut) {
                        continue;
                    }

                    throw EOF;
                }

                this.strMethod = var1;

                while ((var1 = this.readSpace()) == null) {
                    if (this.isLineOut) {
                        throw EOF;
                    }
                }

                this.strRequest = var1;

                while (this.readSpace() == null) {
                    if (this.isLineOut) {
                        throw EOF;
                    }
                }

                if (!this.isLineOut) {
                    for (int var2 = 0; var2 < 4096; ++var2) {
                        if (this.r() == '\n') {
                            return;
                        }
                    }
                }

                return;
            }
        } catch (IOException var3) {
            this.close();
            throw var3;
        }
    }

    private boolean readOption() throws IOException {
        boolean var2 = true;
        char[] var3 = this.bC;
        int var4 = 0;
        int var5 = var3.length;

        char var1;
        int var6;
        for (var6 = 0; var6 < var5; ++var6) {
            var1 = this.r();
            if (var1 == '\n') {
                var2 = false;
                break;
            }

            if (!Character.isWhitespace(var1)) {
                if (var1 == ':') {
                    var2 = false;
                    break;
                }

                if (var1 == '+') {
                    var1 = ' ';
                }

                if (var1 == '%') {
                    var1 = (char) (Character.digit((char) this.r(), 16) << 4 | Character.digit((char) this.r(), 16));
                }

                var3[var4++] = Character.toLowerCase(var1);
            }
        }

        if (var2) {
            for (var6 = 0; var6 < 4096 && this.r() != '\n'; ++var6) {
            }

            if (var6 >= 4096) {
                throw EOF;
            } else {
                return true;
            }
        } else if (var4 == 0) {
            return false;
        } else {
            String var8 = this.getString(var3, var4);
            var4 = 0;
            var2 = true;

            int var7;
            for (var7 = 0; var7 < var5; ++var7) {
                var1 = this.r();
                if (var1 == '\n') {
                    var2 = false;
                    break;
                }

                if ((var4 != 0 || !Character.isWhitespace(var1)) && var1 != '\r') {
                    if (var1 == '+') {
                        var1 = ' ';
                    }

                    if (var1 == '%') {
                        var1 = (char) (Character.digit((char) this.r(), 16) << 4 | Character.digit((char) this.r(), 16));
                    }

                    var3[var4++] = var1;
                }
            }

            if (var2) {
                for (var7 = 0; var7 < 4096 && this.r() != '\n'; ++var7) {
                }

                if (var7 >= 4096) {
                    throw EOF;
                }
            }

            this.switchOption(var8, var4 <= 0 ? null : new String(var3, 0, var4));
            return true;
        }
    }

    private String readSpace() throws IOException {
        boolean var1 = false;
        int var2 = -1;
        int var3 = 0;
        short var4 = 1024;
        this.bWork.reset();

        for (; var3 < var4; ++var3) {
            var2 = this.In.read();
            if (var2 != 13) {
                if (var2 == -1 || Character.isWhitespace((char) var2)) {
                    break;
                }

                switch (var2) {
                    case 37:
                        int var5 = this.In.read();
                        int var6 = this.In.read();
                        if (var5 == -1 || var6 == -1) {
                            throw EOF;
                        }

                        this.bWork.write(Character.digit((char) ((char) var5), 16) << 4 | Character.digit((char) ((char) var6), 16));
                        break;
                    default:
                        this.bWork.write(var2);
                }
            }
        }

        this.isLineOut = var2 == -1 || var2 == 10;
        if ((var1 || var2 != -1) && var3 < 1024) {
            return new String(this.bWork.getBuffer(), 0, this.bWork.size(), "UTF8");
        } else {
            throw EOF;
        }
    }

    private void rRun() throws Throwable {
        this.In = this.socket.getInputStream();
        this.Out = this.socket.getOutputStream();

        try {
            this.readMethod();

            while (this.readOption()) {
            }

            this.server.debug.log(this.socket.getLocalAddress().getHostName() + ' ' + this.strRequest);
            String var1 = this.files.uriToPath(this.strRequest);
            if (this.files.needMove(var1)) {
                this.sendMessage(2, this.files.addIndex(var1));
            } else {
                this.fileRequest = this.files.getFile(var1);
                this.sendMessage(this.getResponce(), var1);
            }
        } catch (EOFException var2) {
        } catch (FileNotFoundException var3) {
            this.sendMessage(3, this.strRequest);
        } catch (Throwable var4) {
            this.server.debug.log(var4.getMessage());
        }

        this.close();
    }

    public void run() {
        try {
            this.rRun();
        } catch (Throwable var1) {
        }

    }

    private void sendMessage(int var1, String var2) throws IOException {
        if (this.server.debug.bool_debug) {
            this.server.debug.log(var2 + ' ' + var1);
        }

        this.Out.write(strHttp);
        this.Out.write(BYTE_RESPONCE[var1]);
        this.Out.write(BYTE_CRLF);
        this.Out.write(BYTE_DEF_HEADER);
        this.Out.write(BYTE_CRLF);
        byte[] var3 = this.getSince(System.currentTimeMillis());
        this.Out.write(BYTE_DATE);
        this.Out.write(var3);
        this.Out.write(BYTE_CRLF);
        switch (var1) {
            case 0:
                var3 = this.getSince(this.fileRequest.lastModified());
                this.w(BYTE_LAST_MODIFIED);
                this.w(var3);
                this.w(BYTE_CRLF);
                this.w(BYTE_CONTENT_TYPE);
                this.writeMime(var2);
                this.w(BYTE_CRLF);
                this.w(BYTE_CONTENT_LENGTH);
                this.w(String.valueOf(this.fileRequest.length()));
                this.w(BYTE_CRLF);
                this.w(BYTE_CONNECTION_CLOSE);
                this.w(BYTE_CRLF);
                this.w(BYTE_CRLF);
                if (this.isBody) {
                    this.wFile(this.fileRequest);
                }
                break;
            case 1:
                this.w(BYTE_CONNECTION_CLOSE);
                this.w(BYTE_CRLF);
                this.w(BYTE_CRLF);
                break;
            case 2:
                this.w(BYTE_LOCATION);
                this.w(var2);
                this.w(BYTE_CONNECTION_CLOSE);
                this.w(BYTE_CRLF);
                this.w(BYTE_CRLF);
                break;
            default:
                byte[] var4 = this.strRequest.getBytes();
                this.w(BYTE_CONTENT_TYPE);
                this.w("text/html");
                this.w(BYTE_CRLF);
                this.w(BYTE_CONTENT_LENGTH);
                this.w(String.valueOf(this.files.getErrorMessageSize(var1) + var4.length));
                this.w(BYTE_CRLF);
                this.w(BYTE_CONNECTION_CLOSE);
                this.w(BYTE_CRLF);
                this.w(BYTE_CRLF);
                if (this.isBody) {
                    this.files.getErrorMessage(this.Out, var1, var4, 0, var4.length);
                }
        }

        this.Out.flush();
    }

    private void switchOption(String var1, String var2) throws RuntimeException {
        if (this.server.debug.bool_debug) {
            this.server.debug.log(var1 + ": " + var2);
        }

        if (var1 != null && var2 != null && var1.length() > 0 && var2.length() > 0) {
            if (var1.equals("pragma")) {
                this.isCash = var2.indexOf("no-cache") < 0;
            } else {
                int var3;
                if (var1.equals("content-length")) {
                    try {
                        var3 = var2.length();

                        int var4;
                        for (var4 = 0; var4 < var3 && Character.isDigit(var2.charAt(var4)); ++var4) {
                        }

                        if (var3 != var4) {
                            var2 = var2.substring(0, var4);
                        }

                        this.sizeRequest = (long) Integer.parseInt(var2);
                    } catch (NumberFormatException var6) {
                        this.sizeRequest = 0L;
                    }

                } else if (var1.equals("if-modified-since")) {
                    try {
                        var3 = var2.indexOf(59);
                        this.strIfMod = var3 != -1 ? var2.substring(0, var3) : var2;
                        if (var3 >= 0) {
                            var3 = var2.indexOf("length=", var3 + 1);
                            if (var3 != -1) {
                                var3 += "length=".length();
                                if (var3 < var2.length()) {
                                    this.sizeIfMod = Integer.parseInt(var2.substring(var3), 10);
                                }
                            }
                        }
                    } catch (Exception var5) {
                        var5.printStackTrace();
                        this.strIfMod = null;
                        this.sizeIfMod = -1;
                    }

                }
            }
        }
    }

    private void w(byte[] var1) throws IOException {
        this.Out.write(var1);
    }

    private void w(String var1) throws IOException {
        int var2 = var1.length();
        var1.getChars(0, var1.length(), this.bC, 0);

        for (int var3 = 0; var3 < var2; ++var3) {
            this.bB[var3] = (byte) this.bC[var3];
        }

        this.Out.write(this.bB, 0, var2);
    }

    private void wFile(File var1) throws IOException {
        if (var1.length() > 0L) {
            FileInputStream var3 = new FileInputStream(var1);

            int var2;
            while ((var2 = var3.read(this.bB)) != -1) {
                this.Out.write(this.bB, 0, var2);
            }

            var3.close();
        }
    }

    private void wln(byte[] var1) throws IOException {
        this.w(var1);
        this.w(BYTE_CRLF);
    }

    private void writeMime(String var1) throws IOException {
        int var2 = var1.lastIndexOf(46);
        String var3;
        if (var2 < 0) {
            var3 = "application/octet-stream";
        } else {
            var3 = HttpServer.Mime.getString(var1.substring(var2 + 1), "application/octet-stream");
        }

        this.w(var3);
    }
}
