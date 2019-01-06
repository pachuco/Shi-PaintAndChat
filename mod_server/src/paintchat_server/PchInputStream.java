package paintchat_server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import paintchat.Res;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.VectorBin;

public class PchInputStream {
    private VectorBin lines = new VectorBin();
    private Res res = new Res();
    private InputStream in;
    boolean isRead = false;
    private int iMax = 10000000;

    public PchInputStream(InputStream var1, int var2) {
        this.in = var1;
        this.iMax = Math.max(var2, 0);
    }

    public VectorBin getLines() {
        this.getPch();
        return this.lines;
    }

    public Res getStatus() {
        this.getPch();
        return this.res;
    }

    private void getPch() {
        if (!this.isRead) {
            this.isRead = true;
            ByteStream var1 = new ByteStream();
            byte[] var2 = new byte[]{13, 10, 13, 10};
            int var4 = 0;

            try {
                label63:
                while (true) {
                    int var3 = Io.r(this.in);
                    if (var3 < 0) {
                        throw new EOFException();
                    }

                    if (var2[var4] == var3) {
                        ++var4;
                        if (var4 >= var2.length) {
                            if (var1.size() > 0) {
                                ByteInputStream var5 = new ByteInputStream();
                                var5.setByteStream(var1);
                                this.res.load((InputStream) var5);
                            }

                            while (true) {
                                do {
                                    var4 = Io.readUShort(this.in);
                                    if (var4 < 0) {
                                        break label63;
                                    }
                                } while (var4 < 2);

                                var2 = new byte[var4];
                                Io.rFull(this.in, var2, 0, var4);
                                this.lines.add(var2);

                                while (this.lines.size() > 0 && this.lines.getSizeBytes() > this.iMax) {
                                    this.lines.remove(1);
                                }
                            }
                        }
                    } else {
                        var4 = 0;
                    }

                    var1.write(var3);
                }
            } catch (EOFException var7) {
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            try {
                this.in.close();
                this.in = null;
            } catch (IOException var6) {
            }

        }
    }
}
