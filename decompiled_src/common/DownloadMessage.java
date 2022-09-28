package common;

import java.io.Serializable;

public class DownloadMessage implements Serializable {
   public static final int DOWNLOAD_START = 0;
   public static final int DOWNLOAD_CHUNK = 1;
   public static final int DOWNLOAD_DONE = 2;
   public static final int DOWNLOAD_ERROR = 3;
   protected String id = null;
   protected long size = 0L;
   protected byte[] data = null;
   protected String message = null;
   protected int type = 0;

   protected DownloadMessage(int var1, String var2) {
      this.type = var1;
      this.id = var2;
   }

   public static DownloadMessage Error(String var0, String var1) {
      DownloadMessage var2 = new DownloadMessage(3, var0);
      var2.message = var1;
      return var2;
   }

   public static DownloadMessage Chunk(String var0, byte[] var1) {
      DownloadMessage var2 = new DownloadMessage(1, var0);
      var2.data = var1;
      return var2;
   }

   public static DownloadMessage Done(String var0) {
      return new DownloadMessage(2, var0);
   }

   public static DownloadMessage Start(String var0, long var1) {
      DownloadMessage var3 = new DownloadMessage(0, var0);
      var3.size = var1;
      return var3;
   }

   public String getError() {
      if (this.type != 3) {
         throw new RuntimeException("Wrong message type for that info");
      } else {
         return this.message;
      }
   }

   public byte[] getData() {
      if (this.type != 1) {
         throw new RuntimeException("Wrong message type for that info");
      } else {
         return this.data;
      }
   }

   public long getSize() {
      if (this.type != 0) {
         throw new RuntimeException("Wrong message type for that info");
      } else {
         return this.size;
      }
   }

   public int getType() {
      return this.type;
   }

   public String id() {
      return this.id;
   }
}
