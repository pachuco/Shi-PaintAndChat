package paintchat_server;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import paintchat.Res;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.ThreadPool;

public abstract class XMLTalker extends DefaultHandler implements Runnable {
    private boolean isLive = true;
    private boolean isInit = false;
    private boolean canWrite = true;
    private boolean doWrite = false;
    protected int iSendInterval = 2000;
    private BufferedWriter Out;
    private BufferedInputStream In;
    private Socket socket;
    private ByteStream stm_buffer = new ByteStream();
    private Res status;
    long lTime;
    private SAXParser saxParser = null;
    private String strTag;
    private Res res_att = new Res();
    private final String[] strTags = new String[]{"talk", "in", "leave", "infomation", "script", "admin"};
    private final String[] strStartTags = new String[]{"<talk", "<in", "<leave", "<infomation", "<script", "<admin"};
    private final int[] strHints = new int[]{0, 1, 2, 6, 8, 102};

    private synchronized void mInitInside() throws Throwable {
        if (!this.isInit) {
            this.isInit = true;
            if (this.In == null) {
                this.In = new BufferedInputStream(this.socket.getInputStream());
            }

            if (this.Out == null) {
                this.Out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF8"));
            }

            this.updateTimer();
        }
    }

    public void updateTimer() {
        this.lTime = System.currentTimeMillis();
    }

    public synchronized void mStart(Socket var1, InputStream var2, OutputStream var3, Res var4) {
        try {
            this.socket = var1;
            if (var2 != null) {
                this.In = var2 instanceof BufferedInputStream ? (BufferedInputStream) var2 : new BufferedInputStream(var2);
            }

            if (var3 != null) {
                this.Out = new BufferedWriter(new OutputStreamWriter(var3));
            }

            this.status = var4 == null ? new Res() : var4;
            ThreadPool.poolStartThread(this, 'l');
        } catch (Throwable ex) {
            this.mStop();
        }

    }

    public synchronized void mStop() {
        if (this.isLive) {
            this.isLive = false;
            this.canWrite = true;
            this.mDestroy();
            this.canWrite = false;
            this.mDestroyInside();
        }
    }

    private synchronized void mDestroyInside() {
        try {
            if (this.Out != null) {
                this.Out.close();
            }
        } catch (IOException var3) {
        }

        this.Out = null;

        try {
            if (this.In != null) {
                this.In.close();
            }
        } catch (IOException var2) {
        }

        this.In = null;

        try {
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException var1) {
        }

        this.socket = null;
    }

    private void mParseXML(InputStream var1) {
        try {
            if (this.saxParser == null) {
                this.saxParser = SAXParserFactory.newInstance().newSAXParser();
            }

            this.saxParser.parse((InputStream) var1, (DefaultHandler) this);
        } catch (Exception var2) {
            this.mStop();
        }

    }

    protected abstract void mInit() throws IOException;

    protected abstract void mDestroy();

    protected abstract void mRead(String var1, String var2, Res var3) throws IOException;

    protected abstract void mWrite() throws IOException;

    public void run() {
        try {
            this.mInitInside();
            ByteInputStream var1 = new ByteInputStream();
            long var2 = 0L;
            long var4 = 0L;
            this.canWrite = false;

            while (this.isLive) {
                if (this.canRead(this.In)) {
                    this.stm_buffer.reset();
                    this.read(this.stm_buffer);
                    var1.setByteStream(this.stm_buffer);
                    this.mParseXML(var1);
                    this.stm_buffer.reset();
                } else {
                    long var6 = System.currentTimeMillis();
                    var2 = var6 - this.lTime;
                    if (var6 - var4 >= (long) this.iSendInterval) {
                        var4 = var6;
                        this.canWrite = true;
                        this.mWrite();
                        if (var2 >= 60000L) {
                            this.stm_buffer.reset();
                            this.write("ping", (String) null);
                        }

                        this.canWrite = false;
                        this.flush();
                    }

                    Thread.currentThread();
                    Thread.sleep((long) (var2 < 1000L ? 200 : (var2 < 5000L ? 400 : (var2 < 10000L ? 600 : (var2 < 20000L ? 1200 : 2400)))));
                }
            }
        } catch (InterruptedException ex) {
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        this.mStop();
    }

    private void setAtt(Attributes var1) {
        int var2 = var1.getLength();
        this.res_att.clear();

        try {
            for (int var3 = 0; var3 < var2; ++var3) {
                this.res_att.put(var1.getQName(var3), var1.getValue(var3));
            }

        } catch (RuntimeException var4) {
            this.res_att.clear();
        }
    }

    protected void write(Res var1) throws IOException {
        StringBuffer var2 = new StringBuffer();

        String var3;
        for (Enumeration var4 = var1.keys(); var4.hasMoreElements(); this.write(var3, var1.get(var3))) {
            var3 = (String) var4.nextElement();
            if (var2.length() > 0) {
                var2.append('\n');
            }
        }

    }

    protected void write(String var1, String var2) throws IOException {
        this.write(var1, var2, (String) null);
    }

    protected void write(String var1, String var2, String var3) throws IOException {
        if (this.isLive && var1 != null && var1.length() > 0) {
            if (!this.canWrite) {
                throw new IOException("write() bad timing");
            } else {
                if (var2 != null && var2.length() > 0) {
                    if (var3 != null && var3.length() > 0) {
                        System.out.println('<' + var1 + ' ' + var3 + '>');
                        this.Out.write('<' + var1 + ' ' + var3 + '>');
                    } else {
                        this.Out.write('<' + var1 + '>');
                    }

                    this.Out.write(var2);
                    this.Out.write("</" + var1 + '>');
                } else if (var3 != null && var3.length() > 0) {
                    this.Out.write('<' + var1 + ' ' + var3 + "/>");
                } else {
                    this.Out.write('<' + var1 + "/>");
                }

                this.doWrite = true;
                this.updateTimer();
            }
        }
    }

    protected void read(ByteStream var1) throws IOException {
        while (true) {
            if (this.isLive) {
                int var2 = Io.r(this.In);
                if (var2 != 0) {
                    var1.write(var2);
                    continue;
                }
            }

            return;
        }
    }

    protected void flush() throws IOException {
        if (this.Out != null && this.doWrite) {
            this.Out.write(0);
            this.Out.flush();
        }

        this.doWrite = false;
    }

    public boolean canRead(InputStream var1) throws IOException {
        return var1 != null && var1.available() >= 1;
    }

    public Res getStatus() {
        return this.status;
    }

    public boolean isValidate() {
        return this.isLive;
    }

    public ByteStream getWriteBuffer() throws IOException {
        if (!this.canWrite) {
            throw new IOException("getWriteBuffer() bad timing");
        } else {
            return this.stm_buffer;
        }
    }

    public synchronized InetAddress getAddress() {
        return this.isLive && this.socket != null ? this.socket.getInetAddress() : null;
    }

    public void characters(char[] var1, int var2, int var3) throws SAXException {
        if (this.strTag != null) {
            try {
                this.mRead(this.strTag, new String(var1, var2, var3), this.res_att);
            } catch (IOException var4) {
                this.mStop();
            }

        }
    }

    public void endElement(String var1, String var2, String var3) throws SAXException {
        this.strTag = null;
    }

    public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
        try {
            this.strTag = var3;
            if (var3 == null || var3.length() <= 0) {
                return;
            }

            if (var3.equals("ping")) {
                this.strTag = null;
                return;
            }

            if (var3.equals("talk")) {
                return;
            }

            this.setAtt(var4);
            if (var3.equals("initialize")) {
                this.setStatus(var4);
                this.mInit();
                return;
            }

            if (var3.equals("leave")) {
                this.mRead(var3, null, null);
                this.mStop();
                return;
            }
        } catch (Throwable ex) {
            this.mStop();
        }

    }

    public void setStatus(Attributes var1) {
        if (var1 != null) {
            Res var2 = this.getStatus();
            int var3 = var1.getLength();

            for (int var6 = 0; var6 < var3; ++var6) {
                String var4 = var1.getQName(var6);
                String var5 = var1.getValue(var6);
                if (var4 != null && var4.length() > 0 && var5 != null) {
                    var2.put(var4, var5);
                }
            }

        }
    }

    protected String hintToString(int var1) {
        for (int var2 = 0; var2 < this.strHints.length; ++var2) {
            if (this.strHints[var2] == var1) {
                return this.strTags[var2];
            }
        }

        return this.strTags[0];
    }

    protected int strToHint(String var1) {
        if (var1 != null && var1.length() > 0 && !var1.equals("talk")) {
            int var2 = this.strTags.length;

            for (int var3 = 1; var3 < var2; ++var3) {
                if (this.strTags[var3].equals(var1)) {
                    return this.strHints[var3];
                }
            }

            return 0;
        } else {
            return 0;
        }
    }
}
