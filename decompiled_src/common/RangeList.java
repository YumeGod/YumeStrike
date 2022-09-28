package common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RangeList {
   protected List results = null;
   protected String targets;
   protected boolean hasError = false;
   protected String description = "";
   public static final int ENTRY_BARE = 1;
   public static final int ENTRY_RANGE = 2;

   public boolean hasError() {
      return this.hasError;
   }

   public String getError() {
      return this.description;
   }

   public Entry Bare(String var1) {
      Entry var2 = new Entry();
      var2.type = 1;
      var2.value = (long)CommonUtils.toNumber(var1, 0);
      return var2;
   }

   public Entry Range(long var1, long var3) {
      Entry var5 = new Entry();
      var5.type = 2;
      var5.start = var1;
      var5.end = var3;
      return var5;
   }

   public LinkedList parse() {
      LinkedList var1 = new LinkedList();
      String[] var2 = this.targets.split(",");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var2[var3].trim();
         String[] var4;
         long var5;
         long var7;
         if (var2[var3].matches("\\d+-\\d+")) {
            var4 = var2[var3].split("-");
            var5 = (long)CommonUtils.toNumber(var4[0], 0);
            var7 = (long)CommonUtils.toNumber(var4[1], 0);
            var1.add(this.Range(var5, var7));
         } else if (var2[var3].matches("\\d++\\d+")) {
            var4 = var2[var3].split("+");
            var5 = (long)CommonUtils.toNumber(var4[0], 0);
            var7 = (long)CommonUtils.toNumber(var4[1], 0);
            var1.add(this.Range(var5, var5 + var7));
         } else {
            var1.add(this.Bare(var2[var3]));
         }
      }

      return var1;
   }

   public RangeList(String var1) {
      this.targets = var1;
      this.results = this.parse();
   }

   public Iterator iterator() {
      return this.results.iterator();
   }

   public List toList() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.iterator();

      while(true) {
         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            if (var3.type == 1) {
               var1.add(new Long(var3.value));
            } else if (var3.type == 2) {
               for(long var4 = var3.start; var4 < var3.end; ++var4) {
                  var1.add(new Long(var4));
               }
            }
         }

         return var1;
      }
   }

   public int random() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (var3.type == 1) {
            var1.add(new Integer((int)var3.value));
         } else if (var3.type == 2) {
            var1.add(new Integer((int)var3.start + CommonUtils.rand((int)var3.end - (int)var3.start)));
         }
      }

      return (Integer)CommonUtils.pick((List)var1);
   }

   public boolean hit(long var1) {
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (var4.type == 1) {
            if (var4.value == var1) {
               return true;
            }
         } else if (var4.type == 2 && var1 >= var4.start && var1 < var4.end) {
            return true;
         }
      }

      return false;
   }

   private static class Entry {
      public int type;
      public long value;
      public long start;
      public long end;

      private Entry() {
      }

      // $FF: synthetic method
      Entry(Object var1) {
         this();
      }
   }
}
