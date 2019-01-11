package paintchat_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;

import paintchat.Res;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.ThreadPool;

public abstract class PaintChatTalker implements Runnable {
    private boolean isLive = true;
    protected boolean canWrite = true;
    private boolean doWrite = false;
    protected int iSendInterval = 2000;
    private OutputStream Out;
    private InputStream In;
    private Socket socket;
    private ByteStream stm_buffer = new ByteStream();
    private Res status = null;
    long lTime;
    private boolean isConnect = false;

    private void mInitInside() throws IOException {
        if (this.In == null) {
            this.In = this.socket.getInputStream();
        }

        if (this.Out == null) {
            this.Out = this.socket.getOutputStream();
        }

        this.updateTimer();
        if (this.isConnect) {
            this.w(98);
            this.write(this.getStatus());
            this.flush();
            ByteStream var1 = this.getWriteBuffer();
            this.read(var1);
            if (var1.size() > 0) {
                ByteInputStream var2 = new ByteInputStream();
                var2.setByteStream(var1);
                this.getStatus().load((InputStream) var2);
            }
        } else {
            this.write(this.getStatus());
            this.flush();
        }

        this.mInit();
    }

    public void updateTimer() {
        this.lTime = System.currentTimeMillis();
    }

    public synchronized void mStart(Socket var1, InputStream var2, OutputStream var3, Res var4) {
        this.socket = var1;
        this.In = var2;
        this.Out = var3;
        this.status = var4 == null ? new Res() : var4;
        var1.getInetAddress().getHostName();
        ThreadPool.poolStartThread(this, 'l');
    }

    public synchronized void mConnect(Socket var1, Res var2) throws IOException {
        this.isConnect = true;
        this.mStart(var1, (InputStream) null, (OutputStream) null, var2);
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

    private void mDestroyInside() {
        try {
            if (this.Out != null) {
                this.Out.close();
            }
        } catch (IOException var3) {
            ;
        }

        this.Out = null;

        try {
            if (this.In != null) {
                this.In.close();
            }
        } catch (IOException var2) {
            ;
        }

        this.In = null;

        try {
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException var1) {
            ;
        }

        this.socket = null;
    }

    protected abstract void mInit() throws IOException;

    protected abstract void mDestroy();

    protected abstract void mRead(ByteStream var1) throws IOException;

    protected abstract void mIdle(long var1) throws IOException;

    protected abstract void mWrite() throws IOException;

    public void run() {
        long var1 = 0L;
        long var3 = 0L;

        try {
            this.mInitInside();
            this.canWrite = false;

            while (this.isLive) {
                if (this.canRead(this.In)) {
                    this.stm_buffer.reset();
                    this.read(this.stm_buffer);
                    if (this.stm_buffer.size() > 0) {
                        this.mRead(this.stm_buffer);
                    }

                    this.stm_buffer.reset();
                } else {
                    long var5 = System.currentTimeMillis();
                    var1 = var5 - this.lTime;
                    if (var5 - var3 >= (long) this.iSendInterval) {
                        var3 = var5;
                        this.canWrite = true;
                        this.mWrite();
                        if (var1 >= 60000L) {
                            this.stm_buffer.reset();
                            this.write(this.stm_buffer);
                        }

                        this.canWrite = false;
                        this.flush();
                    }

                    Thread.currentThread();
                    Thread.sleep((long) (var1 < 1000L ? 200 : (var1 < 5000L ? 400 : (var1 < 10000L ? 600 : (var1 < 20000L ? 1200 : 2400)))));
                }
            }
        } catch (InterruptedException ex) {
            ;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        this.mStop();
    }

    protected void read(ByteStream var1) throws IOException {
        int var2 = Io.readUShort(this.In);
        if (var2 > 0) {
            var1.write(this.In, var2);
            this.updateTimer();
        }
    }

    protected void write(ByteStream var1) throws IOException {
        this.write(var1.getBuffer(), var1.size());
    }

    protected void write(Res var1) throws IOException {
        if (var1 == null) {
            this.write((byte[]) null, 0);
        } else {
            StringBuffer var2 = new StringBuffer();
            Enumeration var4 = var1.keys();

            while (var4.hasMoreElements()) {
                String var3 = (String) var4.nextElement();
                if (var2.length() > 0) {
                    var2.append('\n');
                }

                var2.append(var3);
                var2.append('=');
                var2.append(var1.get(var3));
            }

            byte[] var5 = var2.toString().getBytes("UTF8");
            this.write(var5, var5.length);
        }
    }

    protected void write(byte[] var1, int var2) throws IOException {
        if (this.isLive) {
            if (!this.canWrite) {
                throw new IOException("write() bad timing");
            } else {
                Io.wShort(this.Out, var2);
                if (var2 > 0) {
                    this.Out.write(var1, 0, var2);
                }

                this.doWrite = true;
                this.updateTimer();
            }
        }
    }

    protected void w(int var1) throws IOException {
        if (this.isLive) {
            this.Out.write(var1);
            this.doWrite = true;
            this.updateTimer();
        }
    }

    protected void flush() throws IOException {
        if (this.Out != null && this.doWrite) {
            this.Out.flush();
        }

        this.doWrite = false;
    }

    public boolean canRead(InputStream var1) throws IOException {
        return var1 != null && var1.available() >= 2;
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
            this.stm_buffer.reset();
            return this.stm_buffer;
        }
    }

    public synchronized InetAddress getAddress() {
        return this.isLive && this.socket != null ? this.socket.getInetAddress() : null;
    }
}
