package paintchat;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MgText {
    public byte head = 100;
    public int ID = 0;
    public byte[] bName = null;
    private byte[] data = null;
    private int seekMax = 0;
    private String strData = null;
    public static final String ENCODE = "UTF8";
    private static final String EMPTY = "";
    public static final byte M_TEXT = 0;
    public static final byte M_IN = 1;
    public static final byte M_OUT = 2;
    public static final byte M_UPDATE = 3;
    public static final byte M_VERSION = 4;
    public static final byte M_EXIT = 5;
    public static final byte M_INFOMATION = 6;
    public static final byte M_SCRIPT = 8;
    public static final byte M_EMPTY = 100;
    public static final byte M_SERVER = 102;

    public MgText() {
    }

    public MgText(int var1, byte var2, byte[] var3) {
        this.setData(var1, var2, var3);
    }

    public MgText(int var1, byte var2, String var3) {
        this.setData(var1, var2, var3);
    }

    public MgText(MgText var1) {
        this.setData(var1);
    }

    public void getData(OutputStream out, boolean saveName) throws IOException {
        if (this.head != -1) {
            this.toBin(false);
            int nameLength = this.bName != null && saveName ? this.bName.length : 0;
            int chunkSize = this.seekMax + 4 + nameLength;
            if (nameLength >= 255) {
                throw new IOException("longer name");
            } else {
                this.w2(out, chunkSize);
                out.write(this.head);
                this.w2(out, this.ID);
                if (saveName) {
                    out.write(nameLength);
                    if (nameLength > 0) {
                        out.write(this.bName);
                    }
                } else {
                    out.write(0);
                }

                if (this.seekMax > 0) {
                    out.write(this.data, 0, this.seekMax);
                }

            }
        }
    }

    public String getUserName() {
        try {
            if (this.bName != null) {
                return new String(this.bName, "UTF8");
            }
        } catch (UnsupportedEncodingException var1) {
            ;
        }

        return "";
    }

    public void setUserName(Object var1) {
        try {
            if (var1 == null) {
                this.bName = null;
            } else if (var1 instanceof String) {
                String var2 = (String) var1;
                this.bName = var2.length() <= 0 ? null : var2.getBytes("UTF8");
            } else {
                this.bName = (byte[]) var1;
            }
        } catch (UnsupportedEncodingException var3) {
            this.bName = null;
        }

    }

    public int getValueSize() {
        this.toBin(false);
        return this.seekMax;
    }

    private final int r(InputStream in) throws IOException {
        int var2 = in.read();
        if (var2 == -1) {
            throw new EOFException();
        } else {
            return var2;
        }
    }

    private final int r2(InputStream in) throws IOException {
        return this.r(in) << 8 | this.r(in);
    }

    private final void r(InputStream in, byte[] var2, int var3) throws IOException {
        int var5;
        for (int var4 = 0; var4 < var3; var4 += var5) {
            var5 = in.read(var2, var4, var3 - var4);
            if (var5 == -1) {
                throw new EOFException();
            }
        }

    }

    private final void w2(OutputStream var1, int var2) throws IOException {
        var1.write(var2 >>> 8 & 255);
        var1.write(var2 & 255);
    }

    public void setData(int var1, byte var2, byte[] var3) {
        int var4 = var3.length;
        this.head = var2;
        this.ID = var1;
        this.bName = null;
        if (this.data != null && this.data.length >= var4) {
            System.arraycopy(var3, 0, this.data, 0, var4);
        } else {
            this.data = var3;
        }

        this.seekMax = var4;
        this.strData = null;
    }

    public void setData(int var1, byte var2, String var3) {
        this.head = var2;
        this.ID = var1;
        this.bName = null;
        this.strData = var3;
        this.seekMax = 0;
    }

    public int setData(InputStream var1) throws IOException {
        try {
            this.strData = null;
            int var2 = this.r2(var1);
            this.head = (byte) this.r(var1);
            this.ID = this.r2(var1);
            int var3 = this.r(var1);
            if (var3 <= 0) {
                this.bName = null;
            } else {
                if (var3 >= 255) {
                    throw new IOException("abnormal");
                }

                this.bName = new byte[var3];
                this.r(var1, this.bName, var3);
            }

            int var4 = var2 - 1 - 2 - 1 - var3;
            if (var4 > 0) {
                if (var4 >= 1024) {
                    throw new IOException("abnormal");
                }

                if (this.data == null || this.data.length < var4) {
                    this.data = new byte[var4];
                }

                this.r(var1, this.data, var4);
                this.seekMax = var4;
            } else {
                this.seekMax = 0;
            }

            return var2 + 2;
        } catch (RuntimeException var5) {
            var5.printStackTrace();
            this.head = 100;
            this.ID = 0;
            this.bName = null;
            this.seekMax = 0;
            return 2;
        }
    }

    public void setData(MgText var1) {
        this.head = var1.head;
        this.ID = var1.ID;
        this.bName = var1.bName;
        this.seekMax = var1.seekMax;
        if (this.seekMax > 0) {
            if (this.data == null || this.data.length < this.seekMax) {
                this.data = new byte[this.seekMax];
            }

            System.arraycopy(var1.data, 0, this.data, 0, this.seekMax);
        }

        this.strData = var1.strData;
    }

    public final void toBin(boolean var1) {
        if (this.seekMax == 0 && this.strData != null && this.strData.length() > 0) {
            try {
                byte[] var2 = this.strData.getBytes("UTF8");
                int var3 = var2.length;
                if (this.data != null && this.data.length >= var3) {
                    System.arraycopy(var2, 0, this.data, 0, var3);
                } else {
                    this.data = var2;
                }

                this.seekMax = var3;
            } catch (UnsupportedEncodingException var4) {
                this.strData = null;
                this.seekMax = 0;
            }
        }

        if (var1) {
            this.strData = null;
        }

    }

    public String toString() {
        try {
            if (this.seekMax > 0 && this.strData == null) {
                this.strData = new String(this.data, 0, this.seekMax, "UTF8");
            }

            if (this.strData != null) {
                return this.strData;
            }
        } catch (RuntimeException var1) {
            ;
        } catch (UnsupportedEncodingException var2) {
            ;
        }

        this.strData = null;
        this.seekMax = 0;
        return "";
    }
}
