package org.apache.xalan.xsltc.runtime;

import java.util.Enumeration;

public class Hashtable {
   private transient HashtableEntry[] table;
   private transient int count;
   private int threshold;
   private float loadFactor;

   public Hashtable(int initialCapacity, float loadFactor) {
      if (initialCapacity <= 0) {
         initialCapacity = 11;
      }

      if ((double)loadFactor <= 0.0) {
         loadFactor = 0.75F;
      }

      this.loadFactor = loadFactor;
      this.table = new HashtableEntry[initialCapacity];
      this.threshold = (int)((float)initialCapacity * loadFactor);
   }

   public Hashtable(int initialCapacity) {
      this(initialCapacity, 0.75F);
   }

   public Hashtable() {
      this(101, 0.75F);
   }

   public int size() {
      return this.count;
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public Enumeration keys() {
      return new HashtableEnumerator(this.table, true);
   }

   public Enumeration elements() {
      return new HashtableEnumerator(this.table, false);
   }

   public boolean contains(Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         HashtableEntry[] tab = this.table;
         int i = tab.length;

         while(i-- > 0) {
            for(HashtableEntry e = tab[i]; e != null; e = e.next) {
               if (e.value.equals(value)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean containsKey(Object key) {
      HashtableEntry[] tab = this.table;
      int hash = key.hashCode();
      int index = (hash & Integer.MAX_VALUE) % tab.length;

      for(HashtableEntry e = tab[index]; e != null; e = e.next) {
         if (e.hash == hash && e.key.equals(key)) {
            return true;
         }
      }

      return false;
   }

   public Object get(Object key) {
      HashtableEntry[] tab = this.table;
      int hash = key.hashCode();
      int index = (hash & Integer.MAX_VALUE) % tab.length;

      for(HashtableEntry e = tab[index]; e != null; e = e.next) {
         if (e.hash == hash && e.key.equals(key)) {
            return e.value;
         }
      }

      return null;
   }

   protected void rehash() {
      int oldCapacity = this.table.length;
      HashtableEntry[] oldTable = this.table;
      int newCapacity = oldCapacity * 2 + 1;
      HashtableEntry[] newTable = new HashtableEntry[newCapacity];
      this.threshold = (int)((float)newCapacity * this.loadFactor);
      this.table = newTable;
      int i = oldCapacity;

      HashtableEntry e;
      int index;
      while(i-- > 0) {
         for(HashtableEntry old = oldTable[i]; old != null; newTable[index] = e) {
            e = old;
            old = old.next;
            index = (e.hash & Integer.MAX_VALUE) % newCapacity;
            e.next = newTable[index];
         }
      }

   }

   public Object put(Object key, Object value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         HashtableEntry[] tab = this.table;
         int hash = key.hashCode();
         int index = (hash & Integer.MAX_VALUE) % tab.length;

         HashtableEntry e;
         for(e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
               Object old = e.value;
               e.value = value;
               return old;
            }
         }

         if (this.count >= this.threshold) {
            this.rehash();
            return this.put(key, value);
         } else {
            e = new HashtableEntry();
            e.hash = hash;
            e.key = key;
            e.value = value;
            e.next = tab[index];
            tab[index] = e;
            ++this.count;
            return null;
         }
      }
   }

   public Object remove(Object key) {
      HashtableEntry[] tab = this.table;
      int hash = key.hashCode();
      int index = (hash & Integer.MAX_VALUE) % tab.length;
      HashtableEntry e = tab[index];

      for(HashtableEntry prev = null; e != null; e = e.next) {
         if (e.hash == hash && e.key.equals(key)) {
            if (prev != null) {
               prev.next = e.next;
            } else {
               tab[index] = e.next;
            }

            --this.count;
            return e.value;
         }

         prev = e;
      }

      return null;
   }

   public void clear() {
      HashtableEntry[] tab = this.table;
      int index = tab.length;

      while(true) {
         --index;
         if (index < 0) {
            this.count = 0;
            return;
         }

         tab[index] = null;
      }
   }

   public String toString() {
      int max = this.size() - 1;
      StringBuffer buf = new StringBuffer();
      Enumeration k = this.keys();
      Enumeration e = this.elements();
      buf.append("{");

      for(int i = 0; i <= max; ++i) {
         String s1 = k.nextElement().toString();
         String s2 = e.nextElement().toString();
         buf.append(s1 + "=" + s2);
         if (i < max) {
            buf.append(", ");
         }
      }

      buf.append("}");
      return buf.toString();
   }

   class HashtableEnumerator implements Enumeration {
      boolean keys;
      int index;
      HashtableEntry[] table;
      HashtableEntry entry;

      HashtableEnumerator(HashtableEntry[] table, boolean keys) {
         this.table = table;
         this.keys = keys;
         this.index = table.length;
      }

      public boolean hasMoreElements() {
         if (this.entry != null) {
            return true;
         } else {
            while(this.index-- > 0) {
               if ((this.entry = this.table[this.index]) != null) {
                  return true;
               }
            }

            return false;
         }
      }

      public Object nextElement() {
         if (this.entry == null) {
            while(this.index-- > 0 && (this.entry = this.table[this.index]) == null) {
            }
         }

         if (this.entry != null) {
            HashtableEntry e = this.entry;
            this.entry = e.next;
            return this.keys ? e.key : e.value;
         } else {
            return null;
         }
      }
   }
}
