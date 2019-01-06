package paintchat_server;

import java.io.IOException;

import paintchat.M;
import paintchat.debug.DebugListener;
import syi.util.ByteStream;
import syi.util.VectorBin;

public class LineTalker extends PaintChatTalker {
    ByteStream lines_send = new ByteStream();
    VectorBin lines_log = new VectorBin();
    LineServer server;
    DebugListener debug;
    private M mgRead = new M();
    private ByteStream workReceive = new ByteStream();
    private ByteStream workReceive2 = new ByteStream();
    private M mgSend = null;
    private int countDraw = 0;

    public LineTalker(LineServer var1, DebugListener var2) {
        this.server = var1;
        this.debug = var2;
    }

    public void send(ByteStream var1, M var2, ByteStream var3) {
        if (this.isValidate()) {
            try {
                if (this.lines_send.size() >= 65535) {
                    this.lines_send = null;
                    throw new IOException("client error");
                }

                int var4 = 0;
                int var5 = var1.size();
                byte[] var6 = var1.getBuffer();
                synchronized (this.lines_send) {
                    for (; var4 + 1 < var5; this.mgSend.set(var2)) {
                        var4 += var2.set(var6, var4);
                        var2.get(this.lines_send, var3, this.mgSend);
                        if (this.mgSend == null) {
                            this.mgSend = new M();
                        }
                    }
                }
            } catch (IOException var9) {
                this.debug.log(var9);
                this.mStop();
            }

        }
    }

    protected void mInit() throws IOException {
        super.iSendInterval = 4000;
        this.server.addTalker(this);
        synchronized (this.lines_send) {
            this.lines_send.w2(1);
            this.lines_send.write(0);
        }
    }

    protected void mRead(ByteStream var1) throws IOException {
        int var2 = var1.size();
        if (var2 > 0) {
            if (var2 <= 2 && var1.getBuffer()[0] == 0) {
                this.server.removeTalker(this);
            } else {
                this.workReceive.reset();

                while (var1.size() >= 2) {
                    var2 = this.mgRead.set(var1);
                    if (var2 < 0) {
                        return;
                    }

                    ++this.countDraw;
                    var1.reset(var2);
                    this.mgRead.get(this.workReceive, this.workReceive2, (M) null);
                    if (this.mgRead.iHint == 10) {
                        this.server.addClear(this, this.workReceive);
                        this.workReceive.reset();
                    }
                }

                if (this.workReceive.size() > 0) {
                    this.server.addLine(this, this.workReceive);
                }

            }
        }
    }

    protected void mIdle(long var1) throws IOException {
    }

    protected void mWrite() throws IOException {
        if (this.lines_log != null) {
            if (this.lines_log.size() <= 0) {
                this.lines_log = null;
            } else {
                this.mSendFlag(1);
                byte[] var4 = this.lines_log.get(0);
                this.write(var4, var4.length);
                this.lines_log.remove(1);
            }
        } else if (this.lines_send.size() > 2) {
            ByteStream var1 = this.getWriteBuffer();
            synchronized (this.lines_send) {
                this.lines_send.writeTo(var1);
                this.lines_send.reset();
            }

            this.write(var1);
            var1.reset();
        }
    }

    protected void mDestroy() {
        this.lines_send = null;
        this.lines_log = null;
        this.server = null;
        this.debug = null;
    }

    public VectorBin getLogArray() {
        return this.lines_log;
    }

    private void mSendFlag(int var1) throws IOException {
        ByteStream var2 = this.getWriteBuffer();
        var2.write(var1);
        this.write(var2);
    }

    public int getDrawCount() {
        return this.countDraw;
    }
}
