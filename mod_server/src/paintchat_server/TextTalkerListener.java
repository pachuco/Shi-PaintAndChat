package paintchat_server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import paintchat.MgText;
import paintchat.Res;
import syi.util.Vector2;

public interface TextTalkerListener {
    void mStart(Socket var1, InputStream var2, OutputStream var3, Res var4);

    void mStop();

    void send(MgText var1);

    MgText getHandleName();

    void sendUpdate(Vector2 var1);

    boolean isValidate();

    InetAddress getAddress();

    boolean isGuest();

    void kill();

    int getSpeakCount();
}
