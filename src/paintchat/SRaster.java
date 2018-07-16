package paintchat;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.Hashtable;
import syi.awt.Awt;

public class SRaster implements ImageProducer {
   private int width;
   private int height;
   private ColorModel model;
   private int[] pixels;
   private ImageConsumer consumer = null;
   private static Hashtable properties = null;
   private boolean isWin;

   public SRaster(ColorModel var1, int[] var2, int var3, int var4) {
      this.model = var1;
      this.pixels = var2;
      this.width = var3;
      this.height = var4;
      if (properties == null) {
         properties = new Hashtable();
         this.isWin = Awt.isWin();
      }

   }

   public synchronized void addConsumer(ImageConsumer var1) {
      try {
         this.consumer = var1;
         var1.setDimensions(this.width, this.height);
         var1.setColorModel(this.model);
         var1.setProperties(properties);
         var1.setHints(30);
         this.sendPix();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public boolean isConsumer(ImageConsumer var1) {
      return var1 == this.consumer;
   }

   public void newPixels(Image var1, int[] var2, int var3, int var4) {
      this.pixels = var2;
      this.newPixels(var1, var3, var4);
   }

   public void newPixels(Image var1, int[] var2, int var3, int var4, int var5) {
      if (var5 != 1) {
         this.scale(var2, var3, var4, var5);
      }

      this.pixels = var2;
      this.newPixels(var1, var3 / var5, var4 / var5);
   }

   public void newPixels(Image var1, int var2, int var3) {
      if (this.width == var2 && this.height == var3 && this.consumer != null && this.isWin) {
         this.sendPix();
      } else {
         this.width = var2;
         this.height = var3;
         var1.flush();
      }

   }

   public void newPixels(Image var1, int var2, int var3, int var4) {
      if (var4 != 1) {
         this.scale(this.pixels, var2, var3, var4);
         var2 /= var4;
         var3 /= var4;
      }

      this.newPixels(var1, var2, var3);
   }

   public void removeConsumer(ImageConsumer var1) {
   }

   public void requestTopDownLeftRightResend(ImageConsumer var1) {
   }

   public final void scale(int[] var1, int var2, int var3, int var4) {
      boolean var5 = false;
      int var7 = 0;
      int var10000 = var2 * var3;
      int var15 = var4 * var4;

      for(int var16 = 0; var16 < var3; var16 += var4) {
         int var17 = var2 * var16;

         for(int var8 = 0; var8 < var2; var8 += var4) {
            int var13 = 0;
            int var12 = 0;
            int var11 = 0;
            int var6 = var17;

            for(int var10 = 0; var10 < var4; ++var10) {
               for(int var9 = 0; var9 < var4; ++var9) {
                  int var14 = var1[var6++];
                  var11 += var14 >>> 16 & 255;
                  var12 += var14 >>> 8 & 255;
                  var13 += var14 & 255;
               }

               var6 += var2 - var4;
            }

            var1[var7++] = var11 / var15 << 16 | var12 / var15 << 8 | var13 / var15;
            var17 += var4;
         }
      }

   }

   private void sendPix() {
      if (this.consumer != null) {
         this.consumer.setPixels(0, 0, this.width, this.height, this.model, (int[])this.pixels, 0, this.width);
         this.consumer.imageComplete(3);
      }

   }

   public void startProduction(ImageConsumer var1) {
      this.addConsumer(var1);
   }
}
