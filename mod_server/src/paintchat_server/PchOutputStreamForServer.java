package paintchat_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

import paintchat.Config;
import syi.util.Io;
import syi.util.VectorBin;

public class PchOutputStreamForServer extends OutputStream {
    private Config config;
    private PchOutputStream out = null;
    private long lTimer = 0L;
    private boolean isLogEnable = false;
    private boolean isLogLoad = false;
    private int iCashMax;
    private int iLogMax;
    private Deflater deflater;
    private byte[] bDeflate = new byte[70000];
    private VectorBin vAnime;

    public PchOutputStreamForServer(Config var1) {
        this.config = var1;
        this.mInit();
    }

    private void mInit() {
        this.iCashMax = this.config.getInt("Server_Cash_Line_Size", 500000);
        this.iLogMax = 100000000;
        this.isLogEnable = this.config.getBool("Server_Log_Line", false);
        this.isLogLoad = this.config.getBool("Server_Load_Line", false);
        boolean var1 = this.config.getBool("Server_Cash_Line", true);
        if ((this.isLogLoad || this.isLogEnable || var1) && this.deflater == null) {
            this.deflater = new Deflater(9, false);
        }

        if (this.isLogLoad) {
            File var2 = this.getTmpFile();
            if (var2.isFile() && var2.length() > 0L) {
                try {
                    PchInputStream var3 = new PchInputStream(new BufferedInputStream(new FileInputStream(var2)), this.iCashMax);
                    this.vAnime = var3.getLines();
                } catch (IOException var4) {
                    var4.printStackTrace();
                }
            }
        }

        if (this.vAnime == null) {
            this.vAnime = new VectorBin();
        }

    }

    public void mFinalize() {
        this.close();
        this.deflater.end();
    }

    public void write(int var1) {
        this.write(new byte[]{(byte) var1});
    }

    public void write(byte[] var1) {
        this.write(var1, 0, var1.length);
    }

    public void write(byte[] var1, int var2, int var3) {
        try {
            if (this.checkTimeout()) {
                this.newLog();
            }

            this.setupStream();
            this.deflater.reset();
            this.deflater.setInput(var1, var2, var3);
            this.deflater.finish();

            int var4;
            for (var4 = 0; !this.deflater.finished(); var4 += this.deflater.deflate(this.bDeflate, var4, var3 - var4)) {
            }

            byte[] var5 = new byte[var4];
            System.arraycopy(this.bDeflate, 0, var5, 0, var4);
            this.vAnime.add(var5);
            if (this.vAnime.getSizeBytes() >= this.iCashMax) {
                this.vAnime.remove(1);
            }

            if (this.out != null) {
                this.out.write(this.bDeflate, 0, var4);
                if (this.out.size() >= this.iLogMax) {
                    this.close();
                    this.setupStream();
                }
            }
        } catch (IOException var6) {
            var6.printStackTrace();
            this.close();
            this.isLogEnable = false;
        }

    }

    public void flush() {
        try {
            if (this.out != null) {
                this.out.flush();
            }
        } catch (IOException var1) {
            this.close();
        }

    }

    public void close() {
        try {
            if (this.out != null) {
                this.out.flush();
                this.out.close();
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        this.out = null;
        if (this.isLogEnable) {
            try {
                File var1 = this.getTmpFile();
                if (var1.isFile() && var1.length() > 0L) {
                    File var2 = new File(Io.getDateString("pch", "spch", Io.getDirectory(var1.getCanonicalPath())));
                    Io.copyFile(var1, var2);
                }
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

    }

    public boolean checkTimeout() {
        return this.lTimer != 0L && System.currentTimeMillis() - this.lTimer >= 86400000L;
    }

    public void setupStream() throws IOException {
        if (this.out == null && (this.isLogEnable || this.isLogLoad)) {
            File var1 = this.getTmpFile();
            boolean var2 = var1.isFile() && var1.length() > 0L;
            this.out = new PchOutputStream(new BufferedOutputStream(new FileOutputStream(var1, var2)), var2);
            if (!var2) {
                this.out.writeHeader("Client_Image_Width", this.config.getString("Client_Image_Width"));
                this.out.writeHeader("Client_Image_Height", this.config.getString("Client_Image_Height"));
                this.out.writeHeader("version", "2");
            }

            this.lTimer = System.currentTimeMillis();
        }
    }

    public void getLog(VectorBin var1) {
        this.vAnime.copy(var1);
    }

    public void newLog() {
        this.vAnime.removeAll();
        this.flush();
        this.close();
        File var1 = this.getTmpFile();
        if (var1.isFile()) {
            var1.delete();
        }

    }

    private File getTmpFile() {
        File var1 = new File(this.config.getString("Server_Log_Line_Dir", "./save_server/"));
        if (!var1.isDirectory()) {
            var1.mkdirs();
        }

        return new File(var1, "line_cash.tmp");
    }
}
