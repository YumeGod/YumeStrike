package common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChangeLog implements Serializable {
   public static final int CHANGE_ADD = 1;
   public static final int CHANGE_ADDNEW = 2;
   public static final int CHANGE_UPDATE = 3;
   public static final int CHANGE_DELETE = 4;
   protected List changes = new LinkedList();
   protected long preid = 0L;
   protected long postid = 0L;
   protected String name;

   public int size() {
      return this.changes.size();
   }

   public boolean isDifferent() {
      return this.changes.size() > 0;
   }

   public ChangeLog(String var1) {
      this.name = var1;
   }

   public void add(String var1, Map var2) {
      this.changes.add(new ChangeEntry(1, var1, var2));
   }

   public void addnew(String var1, Map var2) {
      this.changes.add(new ChangeEntry(2, var1, var2));
   }

   public void update(String var1, Map var2) {
      this.changes.add(new ChangeEntry(3, var1, var2));
   }

   public void delete(String var1) {
      this.changes.add(new ChangeEntry(4, var1, (Map)null));
   }

   protected long pre(Map var1) {
      long var2 = 0L;
      if (this.preid == 0L) {
         this.preid = var2;
      } else if (var2 != this.preid) {
      }

      return var2;
   }

   protected void post(Map var1, long var2) {
   }

   public void applyOptimize(Map var1) {
      long var2 = this.pre(var1);
      Iterator var4 = this.changes.iterator();

      while(var4.hasNext()) {
         ChangeEntry var5 = (ChangeEntry)var4.next();
         this.actOptimize(var5, var1);
         if (!var5.isNeccessary()) {
            var4.remove();
         }
      }

      this.post(var1, var2);
   }

   public void applyForce(Map var1) {
      long var2 = this.pre(var1);
      Iterator var4 = this.changes.iterator();

      while(var4.hasNext()) {
         ChangeEntry var5 = (ChangeEntry)var4.next();
         this.actForce(var5, var1);
      }

      this.post(var1, var2);
   }

   protected boolean same(Map var1, Map var2) {
      if (var1.size() != var2.size()) {
         return false;
      } else {
         Iterator var3 = var1.entrySet().iterator();

         Map.Entry var4;
         Object var5;
         do {
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               var4 = (Map.Entry)var3.next();
               var5 = var2.get(var4.getKey());
            } while(var5 == null && var4.getValue() == null);

            if (var5 == null && var4.getValue() != null) {
               return false;
            }

            if (var5 != null && var4.getValue() == null) {
               return false;
            }
         } while(var5 == null || var4.getValue() == null || var5.toString().equals(var4.getValue().toString()));

         return false;
      }
   }

   protected void actForce(ChangeEntry var1, Map var2) {
      switch (var1.type()) {
         case 1:
            var2.put(var1.key(), var1.entry());
            break;
         case 2:
            if (!var2.containsKey(var1.key())) {
               var2.put(var1.key(), var1.entry());
            }
            break;
         case 3:
            if (!var2.containsKey(var1.key())) {
               var2.put(var1.key(), var1.entry());
               break;
            } else {
               Map var3 = (Map)var2.get(var1.key());
               Iterator var4 = var1.entry().entrySet().iterator();

               while(var4.hasNext()) {
                  Map.Entry var5 = (Map.Entry)var4.next();
                  var3.put(var5.getKey(), var5.getValue());
               }

               return;
            }
         case 4:
            var2.remove(var1.key());
      }

   }

   protected void actOptimize(ChangeEntry var1, Map var2) {
      Map var3;
      switch (var1.type()) {
         case 1:
            if (var2.containsKey(var1.key())) {
               var3 = (Map)var2.get(var1.key());
               if (!this.same(var3, var1.entry())) {
                  var2.put(var1.key(), var1.entry());
               } else {
                  var1.kill();
               }
            } else {
               var2.put(var1.key(), var1.entry());
            }
            break;
         case 2:
            if (!var2.containsKey(var1.key())) {
               var2.put(var1.key(), var1.entry());
            } else {
               var1.kill();
            }
            break;
         case 3:
            if (!var2.containsKey(var1.key())) {
               var2.put(var1.key(), var1.entry());
               break;
            } else {
               var3 = (Map)var2.get(var1.key());
               boolean var4 = false;
               Iterator var5 = var1.entry().entrySet().iterator();

               while(true) {
                  while(true) {
                     Map.Entry var6;
                     Object var7;
                     do {
                        if (!var5.hasNext()) {
                           if (!var4) {
                              var1.kill();
                           }

                           return;
                        }

                        var6 = (Map.Entry)var5.next();
                        var7 = var3.get(var6.getKey());
                     } while(var7 == null && var6.getValue() == null);

                     if (var7 == null && var6.getValue() != null) {
                        var3.put(var6.getKey(), var6.getValue());
                        var4 = true;
                     } else if (var7 != null && var6.getValue() != null) {
                        if (!var6.getValue().toString().equals(var7.toString())) {
                           var3.put(var6.getKey(), var6.getValue());
                           var4 = true;
                        }
                     } else if (var7 != null && var6.getValue() == null) {
                        var3.put(var6.getKey(), var6.getValue());
                        var4 = true;
                     }
                  }
               }
            }
         case 4:
            if (var2.containsKey(var1.key())) {
               var2.remove(var1.key());
            } else {
               var1.kill();
            }
      }

   }

   public void print() {
      CommonUtils.print_info("Change Log...");
      Iterator var1 = this.changes.iterator();

      while(var1.hasNext()) {
         ChangeEntry var2 = (ChangeEntry)var1.next();
         var2.print();
      }

   }

   public class ChangeEntry implements Serializable {
      protected int type;
      protected String key;
      protected Map entry;
      protected boolean needed = true;

      public ChangeEntry(int var2, String var3, Map var4) {
         this.type = var2;
         this.key = var3;
         this.entry = var4;
      }

      public void kill() {
         this.needed = false;
      }

      public boolean isNeccessary() {
         return this.needed;
      }

      public String key() {
         return this.key;
      }

      public int type() {
         return this.type;
      }

      public Map entry() {
         return this.entry;
      }

      public void print() {
         switch (this.type) {
            case 1:
               CommonUtils.print_info("\tAdd:\n\t\t" + this.key + "\n\t\t" + this.entry);
               break;
            case 2:
               CommonUtils.print_info("\tAddNew:\n\t\t" + this.key + "\n\t\t" + this.entry);
               break;
            case 3:
               CommonUtils.print_info("\tUpdate:\n\t\t" + this.key + "\n\t\t" + this.entry);
               break;
            case 4:
               CommonUtils.print_info("\tDelete:\n\t\t" + this.key);
         }

      }
   }
}
