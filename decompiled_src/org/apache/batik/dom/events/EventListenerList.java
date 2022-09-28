package org.apache.batik.dom.events;

import org.apache.batik.dom.util.HashTable;
import org.apache.batik.dom.util.IntTable;
import org.w3c.dom.events.EventListener;

public class EventListenerList {
   protected int n;
   protected Entry head;
   protected IntTable counts = new IntTable();
   protected Entry[] listeners;
   protected HashTable listenersNS = new HashTable();

   public void addListener(String var1, Object var2, EventListener var3) {
      for(Entry var4 = this.head; var4 != null; var4 = var4.next) {
         if ((var1 != null && var1.equals(var4.namespaceURI) || var1 == null && var4.namespaceURI == null) && var4.listener == var3) {
            return;
         }
      }

      this.head = new Entry(var3, var1, var2, this.head);
      this.counts.inc(var1);
      ++this.n;
      this.listeners = null;
      this.listenersNS.remove(var1);
   }

   public void removeListener(String var1, EventListener var2) {
      if (this.head != null) {
         if (this.head != null && (var1 != null && var1.equals(this.head.namespaceURI) || var1 == null && this.head.namespaceURI == null) && var2 == this.head.listener) {
            this.head = this.head.next;
         } else {
            Entry var4 = this.head;

            Entry var3;
            for(var3 = this.head.next; var3 != null; var3 = var3.next) {
               if ((var1 != null && var1.equals(var3.namespaceURI) || var1 == null && var3.namespaceURI == null) && var3.listener == var2) {
                  var4.next = var3.next;
                  break;
               }

               var4 = var3;
            }

            if (var3 == null) {
               return;
            }
         }

         this.counts.dec(var1);
         --this.n;
         this.listeners = null;
         this.listenersNS.remove(var1);
      }
   }

   public Entry[] getEventListeners() {
      if (this.listeners != null) {
         return this.listeners;
      } else {
         this.listeners = new Entry[this.n];
         int var1 = 0;

         for(Entry var2 = this.head; var2 != null; var2 = var2.next) {
            this.listeners[var1++] = var2;
         }

         return this.listeners;
      }
   }

   public Entry[] getEventListeners(String var1) {
      if (var1 == null) {
         return this.getEventListeners();
      } else {
         Entry[] var2 = (Entry[])this.listenersNS.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            int var3 = this.counts.get(var1);
            if (var3 == 0) {
               return null;
            } else {
               var2 = new Entry[var3];
               this.listenersNS.put(var1, var2);
               int var4 = 0;

               for(Entry var5 = this.head; var4 < var3; var5 = var5.next) {
                  if (var1.equals(var5.namespaceURI)) {
                     var2[var4++] = var5;
                  }
               }

               return var2;
            }
         }
      }
   }

   public boolean hasEventListener(String var1) {
      if (var1 == null) {
         return this.n != 0;
      } else {
         return this.counts.get(var1) != 0;
      }
   }

   public int size() {
      return this.n;
   }

   public class Entry {
      protected EventListener listener;
      protected String namespaceURI;
      protected Object group;
      protected boolean mark;
      protected Entry next;

      public Entry(EventListener var2, String var3, Object var4, Entry var5) {
         this.listener = var2;
         this.namespaceURI = var3;
         this.group = var4;
         this.next = var5;
      }

      public EventListener getListener() {
         return this.listener;
      }

      public Object getGroup() {
         return this.group;
      }

      public String getNamespaceURI() {
         return this.namespaceURI;
      }
   }
}
