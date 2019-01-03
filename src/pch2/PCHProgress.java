package pch2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

public class PCHProgress extends Canvas {
   private PCHCanvas pch;
   private boolean isBuffer;
   private int selTool = -1;
   private int putTool = -1;
   private Rectangle[] rects = null;
   private Image image = null;
   private Graphics back = null;
   private Graphics primary = null;
   private Color clBack;
   private Color clIcon;
   private Color clFore;
   private Color clBar;
   private Color clFrame;
   private Color clSelect;
   public static boolean isJa;
   private static final Font font = new Font("sansserif", 0, 12);
   private static final int I_BODY = 0;
   private static final int I_PLAY = 1;
   private static final int I_STOP = 2;
   private static final int I_RESTART = 3;
   private static final int I_SPEED = 4;
   private static final int I_ADD = 5;
   private static final int I_SUB = 6;
   private static final int I_BAR = 7;
   private static final int I_SIZE_ICON = 20;
   public static final int I_SIZE_PRE = 26;

   public PCHProgress(boolean var1) {
      this.enableEvents(49L);
      this.isBuffer = var1;
   }

   public void action(Point var1) {
      if (this.rects != null) {
         int var2 = 1;

         int var3;
         for(var3 = this.rects.length; var2 < var3; ++var2) {
            if (this.rects[var2].contains(var1)) {
               if (this.putTool != var2) {
                  this.putTool = var2;
                  this.drawIcon(var2);
                  return;
               }
               break;
            }
         }

         if (var2 < var3) {
            if (this.pch != null) {
               switch(var2) {
               case 1:
                  this.pch.playPCH();
                  break;
               case 2:
                  this.pch.suspendDraw();
                  break;
               case 3:
                  this.pch.setMark(0);
                  this.pch.setMark(-1);
                  this.drawIcon(7);
                  break;
               case 4:
                  int var4 = this.pch.getSpeed();
                  byte var5;
                  switch(var4) {
                  case 0:
                     var5 = 10;
                     break;
                  case 10:
                     var5 = 20;
                     break;
                  case 20:
                     var5 = -1;
                     break;
                  default:
                     var5 = 0;
                  }

                  this.pch.setSpeed(var5);
                  break;
               case 5:
                  this.pch.setScale(1, false);
                  break;
               case 6:
                  this.pch.setScale(-1, false);
               }

            }
         }
      }
   }

   public void drawBar() {
      this.drawIcon(7);
   }

   private void drawIcon(int var1) {
      Graphics var2 = this.getPrimary();
      if (var2 != null) {
         if (this.isBuffer) {
            this.drawIcon(this.back, var1);
            if (this.rects != null && var1 > 0 && var1 < this.rects.length) {
               Rectangle var3 = this.rects[var1];
               var2.drawImage(this.image, var3.x, var3.y, var3.x + var3.width, var3.y + var3.height, var3.x, var3.y, var3.x + var3.width, var3.y + var3.height, (ImageObserver)null);
            }
         } else {
            this.drawIcon(var2, var1);
         }

      }
   }

   private void drawIcon(Graphics var1, int var2) {
      if (var1 != null) {
         synchronized(var1) {
            try {
               if (this.rects == null || var2 < 0 || var2 >= this.rects.length || var1 == null) {
                  return;
               }

               Rectangle var4 = this.rects[var2];
               Color var5 = var2 == 0 ? this.clBack : this.clIcon;
               var5 = var2 == this.putTool ? var5.darker() : var5;
               if (var2 != 7) {
                  var1.setColor(var5);
                  var1.fillRect(var4.x + 1, var4.y + 1, var4.width - 2, var4.height - 2);
               }

               if (var2 == this.selTool && var2 != this.putTool || var2 == 0) {
                  var1.setColor(var5.brighter());
                  var1.drawRect(var4.x + 1, var4.y + 1, var4.width - 2, var4.height - 2);
               }

               var1.setColor(var2 == this.selTool ? this.clSelect : this.clFrame);
               var1.drawRect(var4.x, var4.y, var4.width - 1, var4.height - 1);
               int var6;
               int var7;
               int var8;
               int[] var12;
               int[] var14;
               switch(var2) {
               case 1:
                  var12 = new int[]{var4.x + 7, var4.x + 13, var4.x + 7};
                  var14 = new int[]{var4.y + 4, var4.y + 10, var4.y + 16};
                  var1.setColor(this.clFore);
                  var1.fillPolygon(var12, var14, var12.length);
                  var1.setColor(this.clFore.brighter());
                  var1.drawPolygon(var12, var14, var12.length);
                  break;
               case 2:
                  var1.setColor(this.clFore);
                  var1.fillRect(var4.x + 5, var4.y + 6, 9, 9);
                  var1.setColor(this.clFore.brighter());
                  var1.drawRect(var4.x + 4, var4.y + 5, 10, 10);
                  break;
               case 3:
                  var12 = new int[]{var4.x + 8, var4.x + 2, var4.x + 8};
                  var14 = new int[]{var12[0] + 8, var12[1] + 8, var12[2] + 8};
                  int[] var15 = new int[]{var4.y + 4, var4.y + 10, var4.y + 16};
                  var1.setColor(this.clFore);
                  var1.fillPolygon(var12, var15, var12.length);
                  var1.fillPolygon(var14, var15, var12.length);
                  var1.setColor(this.clFore.brighter());
                  var1.drawPolygon(var12, var15, var12.length);
                  var1.drawPolygon(var14, var15, var12.length);
                  break;
               case 4:
                  String[][] var11 = new String[][]{{"Mx", "H", "M", "L"}, {"最", "早", "既", "鈍"}};
                  String[] var13 = isJa ? var11[1] : var11[0];
                  var8 = this.pch.getSpeed();
                  var8 = var8 < 0 ? 0 : (var8 == 0 ? 1 : (var8 <= 10 ? 2 : 3));
                  var1.setColor(this.clFore);
                  var1.drawString(var13[var8], var4.x + 4, var4.y + var4.height - 4);
                  break;
               case 5:
                  var6 = var4.width / 2;
                  var7 = var4.height / 2;
                  var1.fillRect(var4.x + var6 / 2, var4.y + var7 - 1, var6, 2);
                  var1.fillRect(var4.x + var6 - 1, var4.y + var7 / 2, 2, var7);
                  break;
               case 6:
                  var6 = var4.width / 2;
                  var7 = var4.height / 2;
                  var1.fillRect(var4.x + var6 / 2, var4.y + var7 - 1, var6, 2);
                  break;
               case 7:
                  var6 = this.pch.getLineCount();
                  var7 = this.pch.getSeek();
                  var8 = (int)((float)var4.width * ((float)var7 / (float)var6)) - 5;
                  if (var8 <= 0) {
                     var8 = 1;
                  }

                  var1.setColor(this.clBar);
                  var1.fillRect(var4.x + 3, var4.y + 2, var8, var4.height - 4);
                  var1.setColor(var5);
                  var1.fillRect(var4.x + 3 + var8, var4.y + 2, var4.width - var8 - 5, var4.height - 4);
                  var1.setColor(this.clFore);
                  var1.fillRect(var4.x + Math.min(Math.max((int)((float)this.pch.getMark() / (float)var6 * (float)var4.width), 3), var4.width - 3), var4.y + 2, 1, var4.height - 4);
                  var1.drawString(String.valueOf(var7) + '/' + var6, var4.x + 3, var4.y + var4.height - 3);
               }
            } catch (RuntimeException var9) {
               var9.printStackTrace();
            }

         }
      }
   }

   public Dimension getMinimumSize() {
      return new Dimension(26, 26);
   }

   public Dimension getPreferredSize() {
      Container var1 = this.getParent();
      return var1 == null ? this.getMinimumSize() : new Dimension(var1.getSize().width, 26);
   }

   private Graphics getPrimary() {
      if (this.primary == null) {
         if (this.getParent() == null) {
            return null;
         }

         this.primary = this.getGraphics();
         if (this.primary == null) {
            return null;
         }

         this.primary.setFont(font);
      }

      return this.primary;
   }

   private void iPaint(Graphics var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < this.rects.length; ++var2) {
            this.drawIcon(var1, var2);
         }

      }
   }

   public void paint(Graphics var1) {
      try {
         if (this.rects == null) {
            this.upRect();
            this.iPaint(this.back);
         }

         var1 = this.getPrimary();
         if (this.isBuffer) {
            Dimension var2 = this.getSize();
            var1.drawImage(this.image, 0, 0, var2.width, var2.height, 0, 0, var2.width, var2.height, (ImageObserver)null);
         } else {
            this.iPaint(var1);
         }
      } catch (Throwable var3) {
      }

   }

   protected void processComponentEvent(ComponentEvent var1) {
      try {
         int var2 = var1.getID();
         if (this.rects == null) {
            return;
         }

         if (var2 == 101) {
            this.upRect();
            this.iPaint(this.back);
         }
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   protected void processMouseEvent(MouseEvent var1) {
      try {
         int var2 = var1.getID();
         Point var3 = var1.getPoint();
         switch(var2) {
         case 501:
            this.action(var3);
            if (this.putTool == 7) {
               this.setMark(var3);
            }
            break;
         case 502:
            this.action(var3);
            this.releaseIcon();
            break;
         case 503:
            this.selIcon(var3);
         case 504:
         case 505:
         default:
            break;
         case 506:
            if (this.putTool == 7) {
               this.setMark(var3);
            }
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   protected void processMouseMotionEvent(MouseEvent var1) {
      this.processMouseEvent(var1);
   }

   private void releaseIcon() {
      int var1 = this.putTool;
      this.putTool = -1;
      this.drawIcon(var1);
   }

   private void selIcon(Point var1) {
      if (this.rects != null) {
         int var2 = this.selTool;
         int var3 = this.rects.length;

         int var4;
         for(var4 = 1; var4 < var3 && !this.rects[var4].contains(var1); ++var4) {
         }

         if (var4 >= var3) {
            var4 = -1;
         }

         if (this.selTool != var4) {
            this.selTool = var4;
            if (var2 != -1) {
               this.drawIcon(var2);
            }

            if (var4 != -1) {
               this.drawIcon(var4);
            }

         }
      }
   }

   public void setColor(Color var1, Color var2, Color var3, Color var4, Color var5, Color var6) {
      this.clBack = var1;
      this.clFore = var2;
      this.clIcon = var3;
      this.clBar = var4;
      this.clFrame = var5;
      this.clSelect = var6;
      this.setBackground(var1);
      this.setForeground(var2);
   }

   private synchronized void setMark(Point var1) {
      try {
         int var2 = var1.x - this.rects[7].x;
         int var3 = this.rects[7].width;
         int var4 = this.pch.getLineCount();
         var2 = var2 <= 0 ? 0 : (var2 >= var3 ? var4 : (int)((float)var2 / (float)var3 * (float)var4));
         this.pch.setMark(var2);
         this.drawIcon(7);
      } catch (RuntimeException var5) {
         var5.printStackTrace();
      }

   }

   public void setPCHCanvas(PCHCanvas var1) {
      this.pch = var1;
   }

   public void update(Graphics var1) {
      this.paint(var1);
   }

   private synchronized void upRect() {
      int var1;
      if (this.rects == null) {
         this.rects = new Rectangle[8];

         for(var1 = 0; var1 < this.rects.length; ++var1) {
            this.rects[var1] = new Rectangle();
         }
      }

      Dimension var2 = this.getSize();
      int var4 = 3;
      byte var5 = 3;

      for(var1 = 1; var1 < this.rects.length; ++var1) {
         this.rects[var1].setSize(20, 20);
      }

      for(var1 = 1; var1 < this.rects.length; ++var1) {
         Rectangle var3 = this.rects[var1];
         var3.setLocation(var4, var5);
         var4 += 23;
      }

      this.rects[7].setSize(var2.width - this.rects[7].x - 3, 20);
      this.rects[0].setBounds(0, 0, var2.width, var2.height);
      if (this.isBuffer && (this.image == null || this.image.getWidth((ImageObserver)null) < var2.width || this.image.getHeight((ImageObserver)null) < var2.height)) {
         if (this.image != null) {
            this.image.flush();
            this.back.dispose();
         }

         this.image = this.createImage(var2.width, var2.height);
         this.back = this.image.getGraphics();
         this.back.setFont(font);
      }

   }
}
