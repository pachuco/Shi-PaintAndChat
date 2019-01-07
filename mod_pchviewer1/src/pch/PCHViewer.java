package pch;

import jaba.applet.Applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.MemoryImageSource;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import paintchat.MgLine;

public class PCHViewer extends Applet implements Runnable {
   private volatile boolean isStart = false;
   private boolean live = true;
   private Thread tLoad;
   private Vector vList = new Vector();
   private String pchName = "";
   private PCHCanvas pch = null;
   private Hashtable table = new Hashtable();
   public int speed;
   private int imW;
   private int imH;
   public boolean run;
   public boolean isBufferProgress;
   public boolean isBufferCanvas;
   public boolean isProgress;
   public static final String STR_VERSION = "(C)しぃちゃん PCHViewer v1.02";
   private static boolean isJa;
   private Image imIcon = null;
   private Color clBack;
   private Color clFore;
   private Color clFrame;
   private Color clIcon;
   private Color clBar;
   private Color clSelect;

   public void clearPCH() {
      try {
         this.getPCH().clearPCH();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   public void clearPCH(String var1) {
      try {
         this.getPCH(var1).clearPCH();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   public void destroy() {
      try {
         this.removeAllPCH();
         this.live = false;
         if (this.tLoad != null) {
            this.tLoad.interrupt();
            this.tLoad = null;
         }
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   private synchronized Image getIcon() {
      try {
         if (this.imIcon == null) {
            int[] var1 = new int[]{-3297361, -16777216, -1, -8551800, -393216};
            byte[] var2 = new byte[]{-32, 1, 64, 49, 80, 1, 32, 33, 18, 17, 64, 1, 0, 17, 98, 1, 48, 33, 98, 1, 48, 33, 82, 33, 48, 1, 98, 17, 3, 1, 48, 1, 34, 65, 19, 1, 32, 17, 2, -125, 1, 32, 1, 18, -125, 1, 32, 1, 18, 83, 2, 3, 17, 32, 1, 18, 1, 51, 18, 97, 18, 113, 82, 1, 2, 48, 52, 18, 65, 2, 49, 36, 33, 82, 48, 20, -110};
            int var3 = 0;
            int[] var6 = new int[256];

            for(int var7 = 0; var7 < var2.length; ++var7) {
               int var5 = var1[var2[var7] & 15];
               int var4 = (var2[var7] >> 4 & 15) + 1;

               for(int var8 = 0; var8 < var4; ++var8) {
                  var6[var3++] = var5;
               }
            }

            this.imIcon = this.createImage(new MemoryImageSource(16, 16, var6, 0, 16));
         }
      } catch (Throwable var9) {
         var9.printStackTrace();
      }

      return this.imIcon;
   }

   public int getLineCount() {
      try {
         return this.getPCH().getLineCount();
      } catch (Throwable var1) {
         return 0;
      }
   }

   public int getLineCount(Object var1) {
      try {
         return this.getPCH(var1).getLineCount();
      } catch (Throwable var2) {
         return 0;
      }
   }

   private synchronized PCHCanvas getPCH() {
      if (this.pchName.length() > 0) {
         return this.getPCH(this.pchName);
      } else {
         if (this.pch == null) {
            this.setLayout(new BorderLayout());
            PCHCanvas var1 = new PCHCanvas(this, this.isBufferCanvas);
            var1.setSpeed(this.speed);
            var1.setBackground(this.clBack);
            var1.setForeground(this.clFore);
            if (this.isProgress) {
               this.add(this.getProgress(var1), "South");
            }

            this.add(var1, "Center");
            this.addNotify();
            this.pch = var1;
            this.validate();
         }

         return this.pch;
      }
   }

   private synchronized PCHCanvas getPCH(Object var1) {
      PCHCanvas var2 = (PCHCanvas)this.table.get(var1);
      Frame var3;
      if (var2 == null) {
         var2 = new PCHCanvas(this, this.isBufferCanvas);
         var2.setSpeed(this.speed);
         var2.setBackground(this.clBack);
         var2.setForeground(this.clFore);
         this.table.put(var1, var2);
         var3 = new Frame("(C)しぃちゃん PCHViewer v1.02");
         var3.addWindowListener(var2);
         var3.setLayout(new BorderLayout());
         var3.setBackground(this.clBack);
         var3.setForeground(this.clFore);
         Image var4 = this.getIcon();
         if (var4 != null) {
            var3.setIconImage(var4);
         }

         if (this.isProgress) {
            var3.add(this.getProgress(var2), "South");
         }

         var3.add(var2, "Center");
         var3.addNotify();
         var3.pack();
      }

      var3 = (Frame)var2.getParent();
      if (!var3.isVisible()) {
         var3.setVisible(true);
      }

      return var2;
   }

   private PCHProgress getProgress(PCHCanvas var1) {
      PCHProgress var2 = new PCHProgress(this.isBufferProgress);
      var2.setPCHCanvas(var1);
      var2.setColor(this.clBack, this.clFore, this.clIcon, this.clBar, this.clFrame, this.clSelect);
      var1.setProgress(var2);
      return var2;
   }

   public int getSeek() {
      try {
         return this.getPCH().getSeek();
      } catch (Throwable var2) {
         var2.printStackTrace();
         return 0;
      }
   }

   public int getSeek(Object var1) {
      try {
         this.getPCH(var1).getSeek();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      return 0;
   }

   private int i(String var1) throws NumberFormatException {
      return Integer.decode(var1);
   }

   public void init() {
      try {
         this.enableEvents(16L);
         isJa = Locale.getDefault().getLanguage().equalsIgnoreCase("ja");
         isJa = this.p("ja", isJa);
         PCHProgress.isJa = isJa;
         this.pchName = this.p("pch_name", "");
         this.speed = this.p("speed", 10);
         this.isProgress = this.p("progress", true);
         this.isBufferCanvas = this.p("buffer_canvas", true);
         this.isBufferProgress = this.p("buffer_progress", true);
         this.imW = this.p("image_width", this.p("width", 300));
         this.imH = this.p("image_height", this.p("height", 300));
         this.clBack = new Color(this.p("color_back", 13421823));
         this.clFore = new Color(this.p("color_text", 5263480));
         this.clIcon = new Color(this.p("color_icon", 13421823));
         this.clFrame = new Color(this.p("color_frame", 5263480));
         this.clBar = new Color(this.p("color_bar", 15592959));
         this.clSelect = new Color(this.p("color_bar_select", 4158837));
         this.setBackground(this.clBack);
         this.setForeground(this.clFore);
         this.run = this.p("run", true);

         try {
            Window var1 = PCHCanvas.getParentTop(this);
            if (var1 instanceof Frame) {
               ((Frame)var1).setIconImage(this.getIcon());
            }
         } catch (Throwable var2) {
         }
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   private boolean isStr(String var1) {
      return var1 != null && var1.length() > 0 && var1.toLowerCase().startsWith("undef");
   }

   public synchronized void loadPCH(Object var1, String var2) {
      try {
         synchronized(this.vList) {
            this.vList.addElement(var1);
            this.vList.addElement(var2);
         }

         synchronized(this.tLoad) {
            this.tLoad.notify();
         }
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

   }

   public synchronized void loadPCH(String var1) {
      try {
         synchronized(this.vList) {
            this.vList.addElement("");
            this.vList.addElement(var1);
         }

         synchronized(this.tLoad) {
            this.tLoad.notify();
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   private int p(String var1, int var2) {
      try {
         String var3 = this.getParameter(var1);
         return var3 != null && var3.length() > 0 ? Integer.decode(var3) : var2;
      } catch (Exception var4) {
         return var2;
      }
   }

   private String p(String var1, String var2) {
      try {
         String var3 = this.getParameter(var1);
         if (var3 != null && var3.length() > 0) {
            return var3;
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

      return var2;
   }

   private boolean p(String var1, boolean var2) {
      try {
         String var3 = this.getParameter(var1);
         if (var3 != null && var3.length() > 0) {
            char var4 = Character.toLowerCase(var3.charAt(0));
            return var4 == 't' || var4 == 'y' || var4 == 'o' || var4 == '1';
         }
      } catch (RuntimeException var5) {
      }

      return var2;
   }

   public synchronized void playPCH() {
      try {
         this.getPCH().playPCH();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   public synchronized void playPCH(Object var1) {
      try {
         this.getPCH(var1).playPCH();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   public synchronized void removeAllPCH() {
      try {
         if (this.pch != null) {
            this.pch.destroyPCH();
            this.pch = null;
         }

         Enumeration var3 = this.table.elements();

         while(var3.hasMoreElements()) {
            PCHCanvas var1 = (PCHCanvas)var3.nextElement();
            var1.destroyPCH();
            Window var2 = PCHCanvas.getParentTop(var1);
            if (var2 != null) {
               var2.dispose();
            }
         }

         this.table.clear();
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public synchronized void removePCH(Object var1) {
      try {
         PCHCanvas var2 = (PCHCanvas)this.table.get(var1);
         if (var2 != null) {
            this.table.remove(var1);
            var2.destroyPCH();
            Window var3 = PCHCanvas.getParentTop(var2);
            if (var3 != null) {
               var3.dispose();
            }

            if (var2 == this.pch) {
               this.remove(this.pch);
            }
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void run() {
      while(true) {
         try {
            if (this.live) {
               while(!this.vList.isEmpty()) {
                  Object var1;
                  String var2;
                  synchronized(this.vList) {
                     var1 = this.vList.firstElement();
                     this.vList.removeElementAt(0);
                     var2 = (String)this.vList.firstElement();
                     this.vList.removeElementAt(0);
                  }

                  PCHCanvas var3;
                  if (var1.equals("")) {
                     var3 = this.getPCH();
                  } else {
                     var3 = this.getPCH(var1);
                  }

                  var3.loadPCH(new URL(this.getCodeBase(), var2), this.imW, this.imH);
                  if (this.run) {
                     var3.playPCH();
                  }
               }

               synchronized(this.tLoad) {
                  this.tLoad.wait();
                  continue;
               }
            }
         } catch (InterruptedException var7) {
         } catch (Throwable var8) {
            var8.printStackTrace();
         }

         return;
      }
   }

   public void setBuffer(String var1) {
      if (var1.length() > 0) {
         char var2 = Character.toLowerCase(var1.charAt(0));
         boolean var3 = var2 == 't' || var2 == '1' || var2 == 'o';
         this.isBufferCanvas = var3;
         this.isBufferProgress = var3;
      }

   }

   public void setMark(Object var1, String var2) {
      try {
         this.getPCH(var1).setMark(this.i(var2));
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void setMark(String var1) {
      try {
         this.getPCH().setMark(this.i(var1));
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   public void setSize(Object var1, String var2, String var3) {
      try {
         int var4 = this.i(var2);
         int var5 = this.i(var3);
         PCHCanvas var6 = this.getPCH(var1);
         var6.setSize(var4, var5);
         ((Window)var6.getParent()).pack();
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

   }

   public void setSize(String var1, String var2) {
      try {
         if (this.pchName.length() > 0) {
            this.setSize(this.pchName, var1, var2);
            return;
         }

         int var3 = this.i(var1);
         int var4 = this.i(var2);
         this.getPCH().setSize(var3, var4);
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public void setSpeed(Object var1, String var2) {
      try {
         this.getPCH(var1).setSpeed(Integer.decode(var2));
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void setSpeed(String var1) {
      try {
         this.getPCH().setSpeed(Integer.decode(var1));
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   public void setVisit(Object var1, String var2, String var3) {
      try {
         this.getPCH(var1).setVisit(this.i(var2), this.i(var3) != 0);
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public void setVisit(String var1, String var2) {
      try {
         this.getPCH().setVisit(this.i(var1), this.i(var2) != 0);
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void start() {
      try {
         synchronized(this) {
            if (this.isStart) {
               return;
            }

            this.isStart = true;

            try {
               this.tLoad = new Thread(this);
               this.tLoad.setDaemon(true);
               this.tLoad.setPriority(1);
               this.tLoad.start();
               this.showStatus("(C)しぃちゃん PCHViewer v1.02");
               MgLine.setup(this, 2);
               String var2 = this.p("pch_file", "");
               if (var2.length() > 0) {
                  this.loadPCH(var2);
               }
            } catch (Throwable var3) {
               var3.printStackTrace();
            }
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public void suspendPCH() {
      try {
         this.getPCH().suspendDraw();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   public void suspendPCH(Object var1) {
      try {
         this.getPCH(var1).suspendDraw();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }
}
