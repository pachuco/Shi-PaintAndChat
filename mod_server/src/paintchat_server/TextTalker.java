package paintchat_server;

import java.io.IOException;
import java.io.InputStream;

import paintchat.MgText;
import paintchat.debug.DebugListener;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Vector2;

public class TextTalker extends PaintChatTalker implements TextTalkerListener {
    private Vector2 sendText = new Vector2();
    private MgText[] sendUpdate = null;
    private TextServer server;
    private DebugListener debug;
    private MgText mgName;
    private MgText mgRead = new MgText();
    private ByteInputStream bin = new ByteInputStream();
    private int countSpeak = 0;
    private boolean isGuest = false;
    private boolean isKill = false;

    public TextTalker(TextServer var1, DebugListener var2) {
        this.debug = var2;
        this.server = var1;
    }

    protected void mInit() throws IOException {
        this.mgName = new MgText(0, (byte) 1, this.getStatus().get("name"));
        this.mgName.setUserName(this.mgName.toString());
        this.isGuest = this.getStatus().getBool("guest", false);
        this.server.addTalker(this);
        String var1 = "User login name=" + this.mgName.getUserName() + " host=" + this.getAddress();
        this.server.writeLog(var1);
        this.debug.log(var1);
    }

    protected void mDestroy() {
        if (this.mgName.getUserName().length() > 0) {
            String var1 = "User logout name=" + this.mgName.getUserName() + " host=" + this.getAddress();
            this.server.writeLog(var1);
            this.debug.log(var1);
        }

    }

    protected void mRead(ByteStream var1) throws IOException {
        this.bin.setByteStream(var1);
        int var2 = var1.size();
        int var3 = 0;

        while (var3 < var2 - 1) {
            int var4 = this.mgRead.setData((InputStream) this.bin);
            this.mgRead.ID = this.mgName.ID;
            this.mgRead.bName = this.mgName.bName;
            var3 += var4;
            if (var4 <= 0) {
                throw new IOException("broken");
            }

            this.switchMessage(this.mgRead);
        }

    }

    protected void mIdle(long var1) throws IOException {
    }

    protected void mWrite() throws IOException {
        ByteStream var1 = this.getWriteBuffer();
        int var2;
        if (this.sendUpdate != null) {
            var2 = this.sendUpdate.length;

            for (int var4 = 0; var4 < var2; ++var4) {
                byte var3 = this.sendUpdate[var4].head;
                if (var3 == 0 || var3 == 6 || var3 == 8) {
                    this.sendUpdate[var4].getData(var1, true);
                }
            }

            this.sendUpdate = null;
            this.write(var1);
            var1.reset();
        }

        var2 = this.sendText.size();
        if (var2 > 0) {
            for (int var5 = 0; var5 < var2; ++var5) {
                ((MgText) this.sendText.get(var5)).getData(var1, false);
            }

            this.sendText.remove(var2);
            this.write(var1);
        }
    }

    private void switchMessage(MgText var1) throws IOException {
        switch (var1.head) {
            case 0:
                ++this.countSpeak;
            case 1:
            default:
                if (!this.isKill) {
                    this.server.addText(this, new MgText(var1));
                }
                break;
            case 2:
                this.server.removeTalker(this);
        }

    }

    public void send(MgText var1) {
        try {
            if (!this.isValidate() || this.isKill) {
                return;
            }

            this.sendText.add((Object) var1);
        } catch (RuntimeException var3) {
            this.debug.log(this.getHandleName() + ":" + var3.getMessage());
            this.mStop();
        }

    }

    public synchronized void sendUpdate(Vector2 var1) {
        int var2 = var1.size();
        if (var2 > 0) {
            this.sendUpdate = new MgText[var2];
            var1.copy(this.sendUpdate, 0, 0, var2);
        }

    }

    public MgText getHandleName() {
        return this.mgName;
    }

    public boolean isGuest() {
        return this.isGuest;
    }

    public synchronized void kill() {
        this.send(new MgText(this.mgName.ID, (byte) 102, "canvas:false;chat:false;"));
        this.isKill = true;
    }

    public int getSpeakCount() {
        return this.countSpeak;
    }
}
