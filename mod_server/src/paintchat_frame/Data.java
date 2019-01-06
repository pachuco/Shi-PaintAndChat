package paintchat_frame;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;
import paintchat_http.HttpServer;
import paintchat_server.Server;
import syi.applet.ServerStub;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.awt.MessageBox;
import syi.util.ClassLoaderCustom;
import syi.util.ThreadPool;

public class Data implements Runnable, ActionListener {
    protected transient PropertyChangeSupport propertyChange;
    private Res res = null;
    private Config config = null;
    private Debug debug = null;
    private boolean fieldIsNativeWindows = false;
    private Server server = null;
    private HttpServer http = null;
    private Applet lobby = null;
    private LButton bChat;
    private LButton bHttp;
    private LButton bLobby;

    public Data() {
        File var1 = new File("cnf/temp.tmp");
        this.setIsNativeWindows(var1.isFile());
    }

    public void actionPerformed(ActionEvent var1) {
        try {
            Object var2 = var1.getSource();
            ThreadPool.poolStartThread(this, (char) (var2 == this.bChat ? 'c' : (var2 == this.bHttp ? 'h' : 'l')));
        } catch (Throwable var3) {
            this.debug.log(var3.getMessage());
        }

    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener var1) {
        this.getPropertyChange().addPropertyChangeListener(var1);
    }

    public boolean exitWin() {
        try {
            File var1 = new File(System.getProperty("user.dir"));
            File var2 = new File(var1, "cnf\\temp.tmp");
            if (!var2.isFile()) {
                return false;
            }

            var2 = new File(var1, "PaintChat.exe");
            if (!var2.isFile()) {
                return false;
            }

            Process var3 = Runtime.getRuntime().exec(var2.getCanonicalPath() + " 1");
            var3.waitFor();
        } catch (Throwable var4) {
            System.out.println("win" + var4);
        }

        return true;
    }

    public void firePropertyChange(String var1, Object var2, Object var3) {
        this.getPropertyChange().firePropertyChange(var1, var2, var3);
    }

    public boolean getIsNativeWindows() {
        return this.fieldIsNativeWindows;
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }

        return this.propertyChange;
    }

    public void init(Config var1, Res var2, Debug var3, LButton var4, LButton var5, LButton var6) {
        this.config = var1;
        this.res = var2;
        this.debug = var3;
        this.bChat = var4;
        this.bHttp = var5;
        this.bLobby = var6;
        var4.addActionListener(this);
        var5.addActionListener(this);
        var6.addActionListener(this);
        if (var1.getBool("App_Auto_Http")) {
            ThreadPool.poolStartThread(this, 'h');
        } else {
            var5.setText(var2.get("Http_Button_Start"));
        }

        if (var1.getBool("App_Auto_Paintchat")) {
            ThreadPool.poolStartThread(this, 'c');
        } else {
            var4.setText(var2.get("Paintchat_Button_Start"));
        }

    }

    public boolean isRunPaintChatServer() {
        return this.server != null;
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener var1) {
        this.getPropertyChange().removePropertyChangeListener(var1);
    }

    public void run() {
        switch (Thread.currentThread().getName().charAt(0)) {
            case 'c':
                this.runPaintChat();
                break;
            case 'h':
                this.runHttp();
                break;
            case 'l':
                this.runLobby();
        }

    }

    private synchronized void runHttp() {
        if (this.http == null) {
            this.http = new HttpServer(this.config, this.debug, false);
            this.http.init(this.config.getInt("Connection_Port_Http", 80));
            this.bHttp.setText(this.res.get("Http_Button_Stop"));
        } else {
            this.http.exitServer();
            this.http = null;
            this.bHttp.setText(this.res.get("Http_Button_Start"));
        }

    }

    private synchronized void runLobby() {
        String var1 = "chatIndex";
        if (this.lobby != null) {
            if (MessageBox.confirm("LobbyDisconnect", "(C)しぃちゃん PaintChatApp v3.66")) {
                Applet var5 = this.lobby;
                this.lobby = null;
                var5.stop();
                this.debug.log(this.res.get("LobbyOut"));
            }
        } else {
            if (this.config.getInt(var1) == 0) {
                try {
                    new ConfigDialog("paintchat.config.Ao", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
                } catch (Exception var4) {
                    this.debug.log(var4.getMessage());
                }

                if (this.config.getInt(var1) == 0) {
                    this.debug.log("LobbyCancel");
                    return;
                }
            }

            try {
                int var2 = "(C)しぃちゃん PaintChatApp v3.66".lastIndexOf(118) + 1;
                this.config.put("pchatVersion", "(C)しぃちゃん PaintChatApp v3.66".substring(var2, var2 + 4));
                this.lobby = (Applet) (new ClassLoaderCustom()).loadClass("lobbyusers.Users", "cnf", true).newInstance();
                this.lobby.setStub(ServerStub.getDefaultStub(this.config, this.res));
                this.lobby.init();
                this.lobby.start();
                this.debug.log(this.res.get("LobbyIn"));
                if (this.config.getBool("ao_show_html")) {
                    Gui.showDocument("http://ax.sakura.ne.jp/~aotama/pchat/LobbyRoom.html", this.config, this.res);
                }
            } catch (Exception var3) {
                this.debug.log(var3.getMessage());
            }

        }
    }

    private synchronized void runPaintChat() {
        if (this.server == null) {
            this.server = new Server(this.config.getString("Admin_Password"), this.config, this.debug, false);
            this.server.init();
            this.bChat.setText(this.res.get("Paintchat_Button_Stop"));
            if (this.config.getBool("App_Auto_Lobby", true)) {
                this.startLobby(false);
            }
        } else {
            this.server.exitServer();
            this.server = null;
            this.bChat.setText(this.res.get("Paintchat_Button_Start"));
            if (this.lobby != null) {
                this.runLobby();
            }
        }

    }

    public void setIsNativeWindows(boolean var1) {
        this.fieldIsNativeWindows = var1;
    }

    public void startHttp(boolean var1) {
        if (var1) {
            ThreadPool.poolStartThread(this, 'h');
        } else {
            this.runHttp();
        }

    }

    public void startLobby(boolean var1) {
        if (var1) {
            ThreadPool.poolStartThread(this, 'l');
        } else {
            this.runLobby();
        }

    }

    public void startPaintChat(boolean var1) {
        if (var1) {
            ThreadPool.poolStartThread(this, 'c');
        } else {
            this.runPaintChat();
        }

    }
}
