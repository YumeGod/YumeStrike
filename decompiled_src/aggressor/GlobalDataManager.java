package aggressor;

import common.Callback;
import common.CommonUtils;
import common.MudgeSanity;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlobalDataManager implements Runnable, GenericDataManager {
   protected Map store = new HashMap();
   protected Map subs = new HashMap();
   protected LinkedList reports = new LinkedList();
   protected Set globals = new HashSet();
   protected static GlobalDataManager manager = new GlobalDataManager();

   public static GlobalDataManager getGlobalDataManager() {
      return manager;
   }

   public WindowCleanup unsubOnClose(String var1, Callback var2) {
      return new WindowCleanup(this, var1, var2);
   }

   public boolean isGlobal(String var1) {
      return this.globals.contains(var1);
   }

   public GlobalDataManager() {
      this.globals.add("listeners");
      this.globals.add("sites");
      this.globals.add("tokens");
      (new Thread(this, "Global Data Manager")).start();
   }

   public void unsub(String var1, Callback var2) {
      synchronized(this) {
         List var4 = (List)this.subs.get(var1);
         var4.remove(var2);
      }
   }

   protected List getSubs(String var1) {
      synchronized(this) {
         Object var3 = (List)this.subs.get(var1);
         if (var3 == null) {
            var3 = new LinkedList();
            this.subs.put(var1, var3);
         }

         return (List)var3;
      }
   }

   protected List getSubsSafe(String var1) {
      synchronized(this) {
         return new LinkedList(this.getSubs(var1));
      }
   }

   public void subscribe(String var1, Callback var2) {
      synchronized(this) {
         this.getSubs(var1).add(var2);
      }
   }

   public void cleanup() {
      synchronized(this) {
         Iterator var2 = this.store.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            DataManager var4 = (DataManager)var3.getKey();
            if (!var4.isAlive()) {
               var2.remove();
               CommonUtils.print_stat("Released data manager: " + var4);
            }
         }

      }
   }

   public void wait(DataManager var1) {
      for(long var2 = System.currentTimeMillis(); System.currentTimeMillis() - var2 < 5000L; Thread.yield()) {
         synchronized(this) {
            if (this.store.containsKey(var1)) {
               break;
            }
         }
      }

   }

   public void put(DataManager var1, String var2, Object var3) {
      synchronized(this) {
         if (!this.store.containsKey(var1)) {
            this.store.put(var1, new HashMap());
         }

         Map var5 = (Map)this.store.get(var1);
         var5.put(var2, var3);
      }
   }

   public Map getStore(String var1) {
      HashMap var2 = new HashMap();
      synchronized(this) {
         this.cleanup();
         Iterator var4 = this.store.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            DataManager var6 = (DataManager)var5.getKey();
            Map var7 = (Map)var5.getValue();
            if (var7.containsKey(var1)) {
               var2.put(var6, new HashMap((Map)var7.get(var1)));
            }
         }

         return var2;
      }
   }

   protected Map getMap(String var1) {
      HashMap var2 = new HashMap();
      synchronized(this) {
         this.cleanup();
         Iterator var4 = this.store.values().iterator();

         while(var4.hasNext()) {
            Map var5 = (Map)var4.next();
            Map var6 = (Map)var5.get(var1);
            if (var2 != null) {
               var2.putAll(var6);
            }
         }

         return var2;
      }
   }

   protected List getList(String var1) {
      LinkedList var2 = new LinkedList();
      synchronized(this) {
         this.cleanup();
         Iterator var4 = this.store.values().iterator();

         while(var4.hasNext()) {
            Map var5 = (Map)var4.next();
            List var6 = (List)var5.get(var1);
            if (var6 != null) {
               var2.addAll(var6);
            }
         }

         return var2;
      }
   }

   public Map getMapSafe(String var1) {
      return (Map)this.get(var1, Collections.emptyMap());
   }

   public List getListSafe(String var1) {
      return (List)this.get(var1, new LinkedList());
   }

   public Object get(String var1, Object var2) {
      if (var1.equals("listeners")) {
         return this.getMap("listeners");
      } else if (var1.equals("sites")) {
         return this.getList("sites");
      } else if (var1.equals("tokens")) {
         return this.getList("tokens");
      } else {
         CommonUtils.print_error("Value: " + var1 + " is not a global data value! [BUG!!]");
         return var2;
      }
   }

   public void process(DataManager var1, String var2, Object var3) {
      Object var4 = null;
      synchronized(this) {
         this.put(var1, var2, var3);
         var4 = this.get(var2, var3);
      }

      Iterator var5 = this.getSubsSafe(var2).iterator();

      while(var5.hasNext()) {
         Callback var6 = (Callback)var5.next();
         var6.result(var2, var4);
      }

   }

   public void report(DataManager var1, String var2, Object var3) {
      synchronized(this) {
         this.reports.add(new TripleZ(var1, var2, var3));
      }
   }

   protected TripleZ grab() {
      synchronized(this) {
         return (TripleZ)this.reports.pollFirst();
      }
   }

   public void run() {
      long var1 = System.currentTimeMillis() + 10000L;
      TripleZ var3 = null;

      try {
         while(true) {
            if (System.currentTimeMillis() > var1) {
               var1 = System.currentTimeMillis() + 10000L;
               this.cleanup();
            }

            var3 = this.grab();
            if (var3 == null) {
               Thread.sleep(1000L);
            } else {
               this.process(var3.dmgr, var3.key, var3.data);
               Thread.yield();
            }
         }
      } catch (Exception var5) {
         MudgeSanity.logException("GDM Loop: " + var3, var5, false);
      }
   }

   private static class TripleZ {
      public DataManager dmgr;
      public String key;
      public Object data;

      public TripleZ(DataManager var1, String var2, Object var3) {
         this.dmgr = var1;
         this.key = var2;
         this.data = var3;
      }

      public String toString() {
         return this.dmgr + "; " + this.key + " => " + this.data;
      }
   }
}
