package paintchat_client;

import java.awt.Component;
import java.io.IOException;
import java.util.zip.Inflater;

import paintchat.M;
import paintchat_server.PaintChatTalker;
import syi.util.ByteStream;
import syi.util.ThreadPool;

public class TLine extends PaintChatTalker {
    public Data data;
    private ByteStream bSendCash = new ByteStream();
    private ByteStream stmIn = new ByteStream();
    private ByteStream workSend = new ByteStream();
    private M mgOut = null;
    private M mgDraw = null;
    private boolean isCompress = false;
    private boolean isRunDraw = false;
    Inflater inflater = new Inflater(false);

    public TLine(Data var1, M var2) {
        this.data = var1;
        this.mgDraw = var2;
    }

    protected void mDestroy() {
        this.isRunDraw = false;
        super.iSendInterval = 0;
    }

    protected void mRead(ByteStream var1) throws IOException {
        if (var1.size() <= 1) {
            switch (var1.getBuffer()[0]) {
                case 0:
                    synchronized (this.stmIn) {
                        this.stmIn.w2(0);
                        break;
                    }
                case 1:
                    this.isCompress = true;
            }

        } else {
            try {
                int var2 = var1.size();
                if (this.isCompress) {
                    this.isCompress = false;
                    if (this.inflater == null) {
                        this.inflater = new Inflater(false);
                    }

                    this.inflater.reset();
                    this.inflater.setInput(var1.getBuffer(), 0, var2);
                    synchronized (this) {
                        byte[] var5 = this.workSend.getBuffer();
                        synchronized (this.stmIn) {
                            while (!this.inflater.needsInput()) {
                                int var3 = this.inflater.inflate(var5, 0, var5.length);
                                this.stmIn.write(var5, 0, var3);
                            }
                        }
                    }
                } else {
                    if (this.inflater != null) {
                        this.inflater.end();
                        this.inflater = null;
                    }

                    synchronized (this.stmIn) {
                        var1.writeTo(this.stmIn);
                    }
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }

        }
    }

    public void mInit() {
    }

    protected void mIdle(long var1) throws IOException {
    }

    protected void mWrite() throws IOException {
        if (this.bSendCash.size() > 0) {
            synchronized (this.bSendCash) {
                this.write(this.bSendCash);
                this.bSendCash.reset();
            }
        }
    }

    public void send(M var1) {
        synchronized (this.bSendCash) {
            if (var1 == null) {
                try {
                    super.canWrite = true;
                    this.bSendCash.reset();
                    this.bSendCash.w2(2);
                    this.write(this.bSendCash);
                    this.flush();
                } catch (IOException var3) {
                }

            } else {
                var1.get(this.bSendCash, this.workSend, this.mgOut);
                if (this.mgOut == null) {
                    this.mgOut = new M();
                }

                this.mgOut.set(var1);
            }
        }
    }

    public void run() {
        if (!this.isRunDraw) {
            this.isRunDraw = true;
            ThreadPool.poolStartThread(this, 'd');
            super.run();
        } else {
            try {
                while (this.isRunDraw) {
                    if (this.stmIn.size() >= 2) {
                        synchronized (this.stmIn) {
                            byte[] var3 = this.stmIn.getBuffer();
                            int var1 = (var3[0] & 255) << 8 | var3[1] & 255;
                            if (var1 <= 2) {
                                this.mgDraw.newUser((Component) null).wait = 0;
                                this.data.addTextComp();
                                this.stmIn.reset(var1 + 2);
                                continue;
                            }

                            var1 = this.mgDraw.set(this.stmIn.getBuffer(), 0);
                            this.stmIn.reset(var1);
                        }

                        if (this.mgDraw.iLayer >= this.data.info.L) {
                            this.data.info.setL(this.mgDraw.iLayer + 1);
                        }

                        this.mgDraw.draw();
                    } else {
                        Thread.currentThread();
                        Thread.sleep(3000L);
                    }
                }
            } catch (InterruptedException var5) {
            }

        }
    }

    public synchronized void mRStop() {
        this.send((M) null);
        this.mStop();
    }
}
