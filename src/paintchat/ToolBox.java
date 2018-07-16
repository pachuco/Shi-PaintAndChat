package paintchat;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Dimension;
import paintchat_client.Mi;
import syi.awt.LComponent;

public interface ToolBox {
   String getC();

   LComponent[] getCs();

   Dimension getCSize();

   void init(Container var1, Applet var2, Res var3, Res var4, Mi var5);

   void lift();

   void pack();

   void selPix(boolean var1);

   void setARGB(int var1);

   void setC(String var1);

   void setLineSize(int var1);

   void up();

   void mVisible(boolean var1);
}
