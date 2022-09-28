package server;

import common.CommonUtils;
import common.DownloadMessage;
import common.MudgeSanity;
import common.Reply;
import common.Request;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DownloadCalls implements ServerHook {
   protected Resources resources = null;
   protected long ids = 0L;
   protected Map sessions = new HashMap();

   public void register(Map var1) {
      var1.put("download.start", this);
      var1.put("download.get", this);
   }

   public DownloadCalls(Resources var1) {
      this.resources = var1;
   }

   public String makeSession(Request var1, ManageUser var2, File var3) {
      try {
         FileInputStream var4 = new FileInputStream(var3);
         synchronized(this) {
            ++this.ids;
            this.sessions.put(this.ids + "", var4);
         }

         return this.ids + "";
      } catch (IOException var8) {
         MudgeSanity.logException("makeSession", var8, false);
         var2.writeNow(var1.reply(DownloadMessage.Error((String)null, var8.getMessage())));
         return null;
      }
   }

   public void getChunk(Request var1, ManageUser var2, String var3) {
      FileInputStream var4;
      synchronized(this) {
         var4 = (FileInputStream)this.sessions.get(var3);
      }

      if (var4 == null) {
         var2.writeNow(var1.reply(DownloadMessage.Error(var3, "invalid download ID")));
      } else {
         try {
            byte[] var5 = new byte[262144];
            int var6 = var4.read(var5);
            if (var6 > 0) {
               byte[] var7 = new byte[var6];
               System.arraycopy(var5, 0, var7, 0, var6);
               var2.writeNow(var1.reply(DownloadMessage.Chunk(var3, var7)));
            } else {
               synchronized(this) {
                  this.sessions.remove(var3);
                  var4.close();
               }

               var2.writeNow(var1.reply(DownloadMessage.Done(var3)));
            }
         } catch (IOException var10) {
            MudgeSanity.logException("getChunk", var10, false);
            var2.writeNow(var1.reply(DownloadMessage.Error(var3, var10.getMessage())));
         }

      }
   }

   public void call(Request var1, ManageUser var2) {
      if (var1.is("download.start", 1)) {
         File var3 = new File(var1.arg(0) + "");
         if (!CommonUtils.isSafeFile(new File("downloads"), var3)) {
            CommonUtils.print_error(var2.getNick() + " attempted to sync '" + var1.arg(0) + "'. Rejected: not in the downloads/ folder.");
            var2.writeNow(var1.reply(DownloadMessage.Error((String)null, "argument is not in downloads/ folder")));
            return;
         }

         if (!var3.exists()) {
            var2.writeNow(var1.reply(DownloadMessage.Error((String)null, "File does not exist")));
            return;
         }

         String var4 = this.makeSession(var1, var2, var3);
         if (var4 == null) {
            return;
         }

         var2.writeNow(var1.reply(DownloadMessage.Start(var4, var3.length())));
      } else if (var1.is("download.get", 1)) {
         this.getChunk(var1, var2, var1.arg(0) + "");
      } else {
         var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
      }

   }
}
