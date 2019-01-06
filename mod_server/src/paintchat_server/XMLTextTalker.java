package paintchat_server;

import java.io.IOException;

import paintchat.MgText;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.ByteInputStream;
import syi.util.Vector2;

public class XMLTextTalker extends XMLTalker implements TextTalkerListener {
    private Vector2 send_text = new Vector2();
    private Vector2 send_update = new Vector2();
    private ByteInputStream input = new ByteInputStream();
    private TextServer server;
    private DebugListener debug;
    private MgText mgName;
    private boolean isOnlyUserList = false;
    private boolean isGuest = false;
    private boolean isKill = false;
    private int countSpeak = 0;

    public XMLTextTalker(TextServer var1, DebugListener var2) {
        this.server = var1;
        this.debug = var2;
    }

    protected void mInit() throws IOException {
        Res var1 = this.getStatus();
        this.mgName = new MgText(0, (byte) 1, this.getStatus().get("name"));
        this.isOnlyUserList = var1.getBool("user_list_only", false);
        this.isGuest = var1.getBool("guest", false);
        this.server.addTalker(this);
    }

    protected void mDestroy() {
    }

    protected void mRead(String var1, String var2, Res var3) throws IOException {
        if (this.mgName != null) {
            byte var4 = (byte) this.strToHint(var1);
            MgText var5 = new MgText();
            var5.setData(this.mgName.ID, var4, this.toEscape(var2));
            switch (var4) {
                case 2:
                    this.server.removeTalker(this);
                    break;
                case 102:
                    try {
                        if (var2.indexOf("get:infomation") >= 0) {
                            this.send(this.server.getInfomation());
                        } else {
                            int var6 = var3.getInt("id", 0);
                            if (var6 > 0) {
                                this.server.doAdmin(var2, var6);
                            }
                        }
                    } catch (RuntimeException var7) {
                        this.debug.log(var7);
                    }
                    break;
                default:
                    if (this.isGuest) {
                        String var8 = this.server.getPassword();
                        if (var8 == null || var8.length() <= 0 || !this.getStatus().get("password").equals(var8)) {
                            return;
                        }
                    }

                    this.server.addText(this, var5);
            }

        }
    }

    protected void mWrite() throws IOException {
        MgText var1;
        int var2;
        if (this.send_update != null) {
            var2 = this.send_update.size();
            if (var2 > 0) {
                StringBuffer var3 = new StringBuffer();

                for (int var5 = 0; var5 < var2; ++var5) {
                    var1 = (MgText) this.send_update.get(var5);
                    String var4 = this.hintToString(var1.head);
                    var3.append('<');
                    var3.append(var4);
                    var3.append(" name=\"");
                    var3.append(this.toEscape(var1.getUserName()));
                    var3.append("\">");
                    var3.append(this.toEscape(var1.toString()));
                    var3.append("</");
                    var3.append(var4);
                    var3.append('>');
                }

                this.write("update", var3.toString());
                this.send_update.removeAll();
            }

            this.send_update = null;
        }

        var2 = this.send_text.size();
        if (var2 > 0) {
            for (int var6 = 0; var6 < var2; ++var6) {
                var1 = (MgText) this.send_text.get(var6);
                this.write(this.hintToString(var1.head), this.toEscape(var1.toString()), "id=\"" + var1.ID + "\" name=\"" + this.toEscape(var1.getUserName()) + "\"");
            }

            this.send_text.remove(var2);
        }
    }

    public void send(MgText var1) {
        if (this.isValidate()) {
            if (!this.isOnlyUserList || var1.head == 1 || var1.head == 2) {
                this.send_text.add((Object) var1);
            }
        }
    }

    public MgText getHandleName() {
        return this.mgName;
    }

    public void sendUpdate(Vector2 var1) {
        var1.copy(this.send_text);
    }

    public boolean isGuest() {
        return this.isGuest;
    }

    private String toEscape(String var1) {
        if (var1.indexOf(60) < 0 && var1.indexOf(62) < 0 && var1.indexOf(38) < 0 && var1.indexOf(34) < 0 && var1.indexOf(34) < 0) {
            return var1;
        } else {
            StringBuffer var2 = new StringBuffer();

            for (int var4 = 0; var4 < var1.length(); ++var4) {
                char var3 = var1.charAt(var4);
                switch (var3) {
                    case '"':
                        var2.append("&qout;");
                        break;
                    case '&':
                        var2.append("&amp;");
                        break;
                    case '<':
                        var2.append("&lt;");
                        break;
                    case '>':
                        var2.append("&gt;");
                        break;
                    default:
                        var2.append(var3);
                }
            }

            return var2.toString();
        }
    }

    private boolean equalsPassword() {
        String var1 = this.server.getPassword();
        return var1 == null || var1.length() <= 0 || !this.getStatus().get("password").equals(var1);
    }

    public void kill() {
        this.isKill = true;
    }

    public int getSpeakCount() {
        return this.countSpeak;
    }
}
