package beacon;

import common.CommonUtils;
import common.Download;
import common.MudgeSanity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BeaconDownloads {
   protected List downloads = new LinkedList();

   public void start(String var1, int var2, String var3, String var4, long var5) {
      try {
         File var7 = CommonUtils.SafeFile("downloads", CommonUtils.garbage("file name"));
         var7.getParentFile().mkdirs();
         FileOutputStream var8 = new FileOutputStream(var7, false);
         String var9 = (new File(var4.replace("\\", "/"))).getName();
         String var10 = CommonUtils.stripRight(var4, var9);
         BeaconDownload var11 = new BeaconDownload(var9, var8, var1, var2);
         var11.flen = var5;
         var11.rpath = var10;
         var11.lpath = var7.getCanonicalPath();
         var11.host = var3;
         synchronized(this) {
            this.downloads.add(var11);
         }
      } catch (IOException var15) {
         MudgeSanity.logException("start download: " + var4, var15, false);
      }

   }

   protected List getDownloads(String var1) {
      LinkedList var2 = new LinkedList();
      synchronized(this) {
         Iterator var4 = this.downloads.iterator();

         while(var4.hasNext()) {
            BeaconDownload var5 = (BeaconDownload)var4.next();
            if (var5.bid.equals(var1)) {
               var2.add(var5.toDownload().toMap());
            }
         }

         return var2;
      }
   }

   protected BeaconDownload find(String var1, int var2) {
      synchronized(this) {
         Iterator var4 = this.downloads.iterator();

         BeaconDownload var5;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            var5 = (BeaconDownload)var4.next();
         } while(!var5.is(var1, var2));

         return var5;
      }
   }

   public void write(String var1, int var2, byte[] var3) {
      synchronized(this) {
         try {
            BeaconDownload var5 = this.find(var1, var2);
            var5.rcvd += (long)var3.length;
            var5.handle.write(var3, 0, var3.length);
         } catch (IOException var7) {
            MudgeSanity.logException("write download", var7, false);
         }

      }
   }

   public boolean exists(String var1, int var2) {
      return this.find(var1, var2) != null;
   }

   public boolean isComplete(String var1, int var2) {
      synchronized(this) {
         BeaconDownload var4 = this.find(var1, var2);
         return var4 != null && var4.flen == var4.rcvd;
      }
   }

   public boolean isActive(String var1) {
      synchronized(this) {
         Iterator var3 = this.downloads.iterator();

         BeaconDownload var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (BeaconDownload)var3.next();
         } while(!var4.bid.equals(var1));

         return true;
      }
   }

   public String getName(String var1, int var2) {
      BeaconDownload var3 = this.find(var1, var2);
      return var3 == null ? "unknown" : var3.fname;
   }

   public Download getDownload(String var1, int var2) {
      BeaconDownload var3 = this.find(var1, var2);
      return var3 == null ? null : var3.toDownload();
   }

   public void close(String var1, int var2) {
      synchronized(this) {
         Iterator var4 = this.downloads.iterator();

         while(var4.hasNext()) {
            BeaconDownload var5 = (BeaconDownload)var4.next();
            if (var5.is(var1, var2)) {
               var4.remove();

               try {
                  var5.handle.close();
               } catch (IOException var8) {
                  MudgeSanity.logException("write close", var8, false);
               }
            }
         }

      }
   }

   public static class BeaconDownload {
      public String fname;
      public OutputStream handle;
      public String bid;
      public int fid;
      public long start = System.currentTimeMillis();
      public long flen;
      public long rcvd = 0L;
      public String rpath;
      public String lpath;
      public String host;

      public BeaconDownload(String var1, OutputStream var2, String var3, int var4) {
         this.fname = var1;
         this.handle = var2;
         this.bid = var3;
         this.fid = var4;
      }

      public boolean is(String var1, int var2) {
         return this.bid.equals(var1) && this.fid == var2;
      }

      public Download toDownload() {
         return new Download(this.fid, this.bid, this.host, this.fname, this.rpath, this.lpath, this.flen);
      }
   }
}
