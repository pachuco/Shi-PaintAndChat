package syi.util;

import java.io.IOException;
import java.io.InputStream;

public class ByteInputStream extends InputStream {
   private byte[] buffer;
   private int iSeekStart = 0;
   private int iSeek = 0;
   private int iLen = 0;

   public int available() {
      return this.iLen - this.iSeek;
   }

   public int read() {
      return this.iSeek >= this.iLen ? -1 : this.buffer[this.iSeek++] & 255;
   }

   public int read(byte[] var1, int var2, int var3) {
      if (this.iSeek >= this.iLen) {
         return -1;
      } else {
         var3 = Math.min(this.iLen - this.iSeek, var3);
         System.arraycopy(this.buffer, this.iSeek, var1, var2, var3);
         this.iSeek += var3;
         return var3;
      }
   }

   public void reset() {
      this.iSeek = this.iSeekStart;
   }

   public void setBuffer(byte[] var1, int var2, int var3) {
      this.buffer = var1;
      this.iLen = var2 + var3;
      this.iSeekStart = this.iSeek = var2;
   }

   public void setByteStream(ByteStream var1) {
      this.setBuffer(var1.getBuffer(), 0, var1.size());
   }

   public void close() throws IOException {
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public long skip(long var1) throws IOException {
      var1 = Math.min(var1, (long)(this.iLen - this.iSeek));
      this.iSeek = (int)((long)this.iSeek + var1);
      return var1;
   }

   public boolean markSupported() {
      return true;
   }

   public void mark(int var1) {
      this.iSeekStart = this.iSeek;
   }
}
