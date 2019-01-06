package syi.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

class CommentCutter {
    private Reader In = null;
    private Writer Out = null;
    private int[][] flagLine = null;
    private int[][] flagWhole = null;
    private int[][] flagWholeEnd = null;
    private int[] countLine = null;
    private int[] countWhole = null;
    private int[] cash = new int[3];
    private int countCash = 0;
    private int oldCh = 32;

    public CommentCutter() {
    }

    public CommentCutter(Reader var1, Writer var2) {
        this.In = var1;
        this.Out = var2;
    }

    private void addDefault() {
        this.addLineFlag(new int[]{47, 47});
        this.addWholeFlag(new int[]{47, 37}, new int[]{37, 47});
    }

    public void addLineFlag(int[] var1) {
        synchronized (this) {
            if (this.flagLine == null) {
                this.flagLine = new int[0][];
                this.countLine = new int[0];
            }

            int[][] var3 = new int[this.flagLine.length + 1][];
            int[] var4 = new int[this.flagLine.length + 1];

            int var5;
            for (var5 = 0; var5 < this.flagLine.length - 1; ++var5) {
                var3[var5] = this.flagLine[var5];
            }

            var3[var5] = var1;
            this.flagLine = var3;
            this.countLine = var4;
        }
    }

    public void addWholeFlag(int[] var1, int[] var2) {
        synchronized (this) {
            if (this.flagWhole == null) {
                this.flagWhole = new int[0][];
                this.flagWholeEnd = new int[0][];
                this.countWhole = new int[0];
            }

            int[][] var4 = new int[this.flagWhole.length + 1][];
            int[][] var5 = new int[this.flagWholeEnd.length + 1][];
            int[] var6 = new int[this.countWhole.length + 1];

            int var7;
            for (var7 = 0; var7 < this.flagLine.length - 1; ++var7) {
                var4[var7] = this.flagWhole[var7];
                var5[var7] = this.flagWholeEnd[var7];
            }

            var4[var7] = var1;
            var5[var7] = var2;
            this.flagWhole = var4;
            this.flagWholeEnd = var5;
            this.countWhole = var6;
        }
    }

    private void cleanCounter() {
        int var1;
        for (var1 = 0; var1 < this.countLine.length; ++var1) {
            this.countLine[var1] = 0;
        }

        for (var1 = 0; var1 < this.countWhole.length; ++var1) {
            this.countWhole[var1] = 0;
        }

        this.countCash = 0;
    }

    public void cut() throws IOException {
        synchronized (this) {
            try {
                if (this.flagLine == null && this.flagWhole == null) {
                    this.addDefault();
                }

                this.cleanCounter();
                this.findFlags();
            } catch (EOFException var2) {
            }

            this.In.close();
            this.Out.flush();
            this.Out.close();
        }
    }

    private void cutArray(int[] var1) throws IOException {
        int var3 = 0;
        int var4 = var1.length;

        int var2;
        while ((var2 = this.In.read()) != -1) {
            var3 = var1[var3] == var2 ? var3 + 1 : 0;
            if (var3 == var4) {
                break;
            }
        }

    }

    private void cutCRLF() throws IOException {
        int var1;
        while ((var1 = this.In.read()) != -1 && var1 != 13 && var1 != 10) {
        }

    }

    private void findFlags() throws IOException {
        int var1;
        while ((var1 = this.In.read()) != -1) {
            if (Character.isWhitespace((char) var1)) {
                var1 = 32;
            }

            if (this.switchFlag(var1)) {
                this.out(var1);
            }
        }

    }

    private void inCash(int var1) {
        if (this.countCash >= this.cash.length) {
            int[] var2 = new int[this.cash.length];
            System.arraycopy(this.cash, 0, var2, 0, this.cash.length);
            this.cash = var2;
        }

        this.cash[this.countCash++] = var1;
    }

    public static void main(String[] var0) {
        try {
            String var3 = "c:\\Windows\\ﾃﾞｽｸﾄｯﾌﾟ\\";
            String var4 = "";
            FileDialog var5 = new FileDialog(new Frame(), "コメントをカットするファイルを指定", 0);
            var5.setDirectory(var3);
            var5.setFile(var4);
            var5.show();
            var3 = var5.getDirectory();
            var4 = var5.getFile();
            if (var3 == null || var3.equals("null") || var4 == null || var4.equals((Object) null)) {
                System.exit(0);
            }

            File var1 = new File(var3, var4);
            var5.setMode(1);
            var5.setTitle("カットしたファイルを保存する場所を指定");
            var5.show();
            var3 = var5.getDirectory();
            var4 = var5.getFile();
            if (var3 == null || var3.equals("null") || var4 == null || var4.equals((Object) null)) {
                System.exit(0);
            }

            File var2 = new File(var3, var4);
            CommentCutter var6 = new CommentCutter(new BufferedReader(new InputStreamReader(new FileInputStream(var1), "JISAutoDetect")), new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2))));
            var6.cut();
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

        System.exit(0);
    }

    private void out(int var1) throws IOException {
        if (var1 != 32 || this.oldCh != 32) {
            this.Out.write(var1);
            this.oldCh = var1;
        }
    }

    private void outCash() throws IOException {
        for (int var1 = 0; var1 < this.countCash; ++var1) {
            this.out(this.cash[var1]);
        }

        this.countCash = 0;
    }

    public void setInOut(Reader var1, Writer var2) {
        synchronized (this) {
            this.Out = var2;
            this.In = var1;
        }
    }

    private boolean switchFlag(int var1) throws IOException {
        boolean var2 = true;

        for (int var3 = 0; var3 < this.flagLine.length; ++var3) {
            if (this.flagLine[var3][this.countLine[var3]] == var1) {
                var2 = false;
                int var10002 = this.countLine[var3]++;
                if (this.countLine[var3] >= this.flagLine[var3].length) {
                    this.cleanCounter();
                    this.cutCRLF();
                    return false;
                }
            } else {
                this.countLine[var3] = 0;
            }
        }

        if (!var2) {
            this.inCash(var1);
        } else {
            this.outCash();
        }

        return var2;
    }
}
