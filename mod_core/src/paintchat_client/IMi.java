package paintchat_client;

import paintchat.M;

public interface IMi {
    void changeSize();

    void scroll(boolean var1, int var2, int var3);

    void send(M mg);

    void setARGB(int argb);

    void setLineSize(int size);

    void undo(boolean isUndo);
}
