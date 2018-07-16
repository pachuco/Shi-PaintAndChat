package c;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Window;
import java.net.URL;
import java.util.Locale;
import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.util.ByteStream;

public class ShiPainter extends Applet implements Runnable {
   public int isStart = 0;
   private P p;
   private Ts ts;
   private Res res;
   private Res config;
   public String str_header = null;
   private Mi mi;
   private String[] sj;

   public void destroy() {
      try {
         if (this.p.getParent() != this) {
            ((Window)this.p.getParent().getParent()).dispose();
         }

         this.removeAll();
         this.p = null;
         this.ts = null;
      } catch (Throwable var1) {
         ;
      }

   }

   public String getColors() {
      return this.p.tool.getC();
   }

   public M.Info getInfo() {
      return this.mi.info;
   }

   public int getLSize() {
      return this.mi.info.L;
   }

   public Mi getMi() {
      return this.mi;
   }

   public ToolBox getToolBox() {
      return this.p.tool;
   }

   protected void jump(String var1, String var2) {
      try {
         this.getAppletContext().showDocument(new URL(this.getCodeBase(), var1), var2 == null ? "_self" : var2);
      } catch (Throwable var4) {
         Ts.alert(var4.getMessage());
      }

   }

   public void paint(Graphics var1) {
      try {
         if (this.isStart >= 2) {
            return;
         }

         FontMetrics var2 = var1.getFontMetrics();
         var1.drawString((String)"Wait for initialization to complete.", 10, var2.getHeight() + 10);
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   public void pExit() {
      Ts.run(this, 's', 2);
   }

   protected void processEvent(AWTEvent var1) {
      try {
         int var2 = var1.getID();
         if (var2 == 101 && this.ts != null) {
            this.ts.pack();
         }
      } catch (Throwable var3) {
         ;
      }

   }

   private void rInit() throws Throwable {
      try {
         this.isStart = 1;
         this.setLayout((LayoutManager)null);
         this.ts = new Ts();
         this.p = new P(this);
         URL var1 = this.getCodeBase();
         String var2 = this.p.p("dir_resource", "./res/");
         char var4 = var2.charAt(var2.length() - 1);
         Object var3;
         if (var4 != '&' && var4 != '?' && var4 != '=') {
            if (var2.charAt(var2.length() - 1) != '/') {
               var2 = var2 + '/';
            }

            var3 = new URL(var1, var2);
         } else {
            var3 = var2;
         }

         this.config = new Res(this, var3, (ByteStream)null);
         this.res = new Res(this, var3, (ByteStream)null);

         try {
            String var5 = this.p.p("res.zip", "res/res.zip");
            if (var5.equals("res_normal.zip")) {
               var5 = "res.zip";
            }

            if (var5.equals("res_pro.zip")) {
               var5 = "res.zip";
            }

            this.config.loadZip(var5);
         } catch (Throwable var9) {
            var9.printStackTrace();
         }

         try {
            this.config.load(new String((byte[])this.config.getRes("param_utf8.txt"), "UTF8"));
            this.config.remove("param_utf8.txt");
         } catch (Throwable var8) {
            Ts.alert(var8.getMessage());
         }

         Res var11 = this.config;
         Color var6 = new Color(var11.getP("color_bk", 13619199));
         this.setBackground(var6);
         this.p.setBackground(var6);
         var6 = new Color(var11.getP("window_color_bk", var6.getRGB()));
         Awt.cC = var6;
         Awt.cBk = var6;
         var6 = new Color(var11.getP("color_text", 5263480));
         this.setForeground(var6);
         this.p.setForeground(var6);
         Awt.cFore = new Color(var11.getP("window_color_text", var6.getRGB()));
         Awt.cFSel = new Color(var11.getP("color_iconselect", 15610675));
         Awt.cF = new Color(var11.getP("window_color_frame", 0));
         Awt.clBar = new Color(var11.getP("window_color_bar", 6711039));
         Awt.clLBar = new Color(var11.getP("window_color_bar_hl", 8947967));
         Awt.clBarT = new Color(var11.getP("window_color_bar_text", 16777215));

         try {
            Awt.setPFrame((Frame)Awt.getParent(this));
         } catch (RuntimeException var7) {
            ;
         }

         this.res.loadResource(this.config, "res", Locale.getDefault().getLanguage());
      } catch (Throwable var10) {
         var10.printStackTrace();
      }

      this.enableEvents(9L);
      this.add(this.ts);
      this.add(this.p);
      this.p.init(this.config, this.res, this.ts);
      this.ts.init(this, this.p, this.res, this.config);
      this.mi = this.p.mi;
      this.ts.layout(this.config.getP("bar_layout", 2));
      this.isStart = 2;
      this.p.repaint();
      if (this.p.p("popup_parent", (String)null) != null) {
         this.ts.isP = new Boolean(this.p.p("popup_parent", false));
      }

      if (this.p.p("popup", false)) {
         this.ts.setV(2, false);
         this.ts.w(false);
      }

   }

   public void run() {
      try {
         switch(Thread.currentThread().getName().charAt(0)) {
         case 'i':
            this.rInit();
            break;
         case 'j':
            String[] var1 = this.sj;
            synchronized(this.sj) {
               for(int var3 = 0; var3 < this.sj.length; ++var3) {
                  String var2;
                  if ((var2 = this.sj[var3]) != null) {
                     this.sj[var3] = null;
                     this.mi.send(var2);
                  }
               }

               return;
            }
         case 'p':
            this.ts.pack();
            break;
         case 's':
            this.p.rSave();
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public void send(String var1, boolean var2) {
      try {
         if (var2) {
            if (this.sj == null) {
               this.sj = new String[32];
            }

            String[] var3 = this.sj;
            synchronized(this.sj) {
               int var4 = 0;

               while(var4 < 32) {
                  if (this.sj[var4] != null) {
                     ++var4;
                  } else {
                     this.sj[var4] = var1;
                     break;
                  }
               }
            }

            Ts.run(this, 'j', 1);
         } else {
            this.mi.send(var1);
         }
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

   }

   public void setColors(String var1) {
      if (this.p != null && this.p.tool != null) {
         this.p.tool.setC(var1);
      }

   }

   public void start() {
      try {
         if (this.isStart == 0) {
            Ts.run(this, 'i', 3);
         }
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   public void update(Graphics var1) {
      this.paint(var1);
   }

   public void mPermission(String var1) {
      int var2 = 0;
      int var4 = var1.length();

      int var3;
      do {
         var3 = var1.indexOf(59, var2);
         if (var3 < 0) {
            var3 = var4;
         }

         if (var3 - var2 > 0) {
            this.p.mP(var1.substring(var2, var3));
         }

         var2 = var3 + 1;
      } while(var3 < var4);

   }
}
