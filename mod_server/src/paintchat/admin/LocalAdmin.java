package paintchat.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;

import paintchat.Res;
import syi.util.Io;

public class LocalAdmin {
    private Res status;
    private InetAddress addr;
    private int iPort;
    private byte[] bRes;

    public LocalAdmin(Res var1, InetAddress var2, int var3) {
        this.status = var1;
        if (var1 != null) {
            var1.put("local_admin", "t");
        }

        this.addr = var2;
        this.iPort = var3 <= 0 ? 'ꇃ' : var3;
    }

    private void doConnect(String var1) {
        InputStream var2 = null;
        OutputStream var3 = null;
        Socket var4 = null;
        this.bRes = null;

        try {
            StringBuffer var5 = new StringBuffer();
            Enumeration var7 = this.status.keys();

            while (var7.hasMoreElements()) {
                String var6 = var7.nextElement().toString();
                if (var6.length() > 0) {
                    var5.append(var6);
                    var5.append('=');
                    var5.append(this.status.get(var6));
                    var5.append('\n');
                }
            }

            var5.append("request=");
            var5.append(var1);
            byte[] var11 = var5.toString().getBytes("UTF8");
            var5 = null;
            var4 = new Socket(this.addr, this.iPort);
            var2 = var4.getInputStream();
            var3 = var4.getOutputStream();
            var3.write(98);
            Io.wShort(var3, var11.length);
            var3.write(var11);
            var3.flush();
            var11 = (byte[]) null;
            int var8 = Io.readUShort(var2);
            if (var8 > 0) {
                var11 = new byte[var8];
                Io.rFull(var2, var11, 0, var8);
            }

            try {
                var2.close();
                var3.close();
                var4.close();
            } catch (IOException var9) {
            }

            this.bRes = var11;
        } catch (IOException var10) {
        }

    }

    public String getString(String var1) {
        try {
            this.doConnect(var1);
            return this.bRes != null && this.bRes.length > 0 ? new String(this.bRes, "UTF8") : "";
        } catch (Exception var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public byte[] getBytes(String var1) {
        try {
            this.doConnect(var1);
            return this.bRes;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
