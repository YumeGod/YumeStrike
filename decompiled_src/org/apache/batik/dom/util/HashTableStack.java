package org.apache.batik.dom.util;

public class HashTableStack {
   protected Link current = new Link((Link)null);

   public void push() {
      ++this.current.pushCount;
   }

   public void pop() {
      if (this.current.pushCount-- == 0) {
         this.current = this.current.next;
      }

   }

   public String put(String var1, String var2) {
      if (this.current.pushCount != 0) {
         --this.current.pushCount;
         this.current = new Link(this.current);
      }

      if (var1.length() == 0) {
         this.current.defaultStr = var2;
      }

      return (String)this.current.table.put(var1, var2);
   }

   public String get(String var1) {
      if (var1.length() == 0) {
         return this.current.defaultStr;
      } else {
         for(Link var2 = this.current; var2 != null; var2 = var2.next) {
            String var3 = (String)var2.table.get(var1);
            if (var3 != null) {
               return var3;
            }
         }

         return null;
      }
   }

   protected static class Link {
      public HashTable table = new HashTable();
      public Link next;
      public String defaultStr;
      public int pushCount = 0;

      public Link(Link var1) {
         this.next = var1;
         if (this.next != null) {
            this.defaultStr = this.next.defaultStr;
         }

      }
   }
}
