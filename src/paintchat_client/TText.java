package paintchat_client;

import java.io.IOException;
import java.io.InputStream;
import paintchat.MgText;
import paintchat.Res;
import paintchat_server.PaintChatTalker;
import syi.util.ByteInputStream;
import syi.util.ByteStream;

public class TText extends PaintChatTalker {
   private Pl pl;
   private Data data;
   private MgText mg = new MgText();
   private ByteInputStream bin = new ByteInputStream();
   private ByteStream stm = new ByteStream();
   private Res names = new Res();

   public TText(Pl var1, Data var2) {
      this.pl = var1;
      this.data = var2;
      super.iSendInterval = 1000;
   }

   public void mInit() {
   }

   protected synchronized void mDestroy() {
      try {
         MgText var1 = new MgText(0, (byte)2, (String)null);
         this.stm.reset();
         var1.getData(this.stm, false);
         this.write(this.stm);
         this.stm.reset();
         this.flush();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   protected void mRead(ByteStream var1) throws IOException {
      int var2 = 0;
      int var3 = var1.size();
      this.bin.setByteStream(var1);

      while(var2 < var3) {
         var2 += this.mg.setData((InputStream)this.bin);
         String var5 = this.mg.toString();
         Integer var4 = new Integer(this.mg.ID);
         switch(this.mg.head) {
         case 0:
            this.pl.addText(this.mg.bName != null ? this.mg.getUserName() : (String)this.names.get(var4), var5, true);
            this.pl.dSound(1);
            break;
         case 1:
            this.names.put(var4, var5);
            this.pl.addInOut(var5, true);
            break;
         case 2:
            this.names.remove(var4);
            this.pl.addInOut(var5, false);
            break;
         case 102:
            this.data.mPermission(var5);
            break;
         default:
            this.pl.addText((String)null, var5, true);
            this.pl.dSound(1);
         }
      }

   }

   protected void mIdle(long var1) throws IOException {
   }

   protected synchronized void mWrite() throws IOException {
      if (this.stm.size() > 0) {
         this.write(this.stm);
         this.stm.reset();
      }
   }

   public synchronized void send(MgText var1) {
      try {
         var1.getData(this.stm, false);
      } catch (IOException var2) {
         ;
      }

   }

   public synchronized void mRStop() {
      try {
         super.canWrite = true;
         this.stm.reset();
         (new MgText(0, (byte)2, (String)null)).getData(this.stm, false);
         this.write(this.stm);
         this.flush();
         this.mStop();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }
}
