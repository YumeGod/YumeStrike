package org.apache.xerces.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import org.apache.xerces.xni.Augmentations;

public class AugmentationsImpl implements Augmentations {
   private AugmentationsItemsContainer fAugmentationsContainer = new SmallContainer();

   public Object putItem(String var1, Object var2) {
      Object var3 = this.fAugmentationsContainer.putItem(var1, var2);
      if (var3 == null && this.fAugmentationsContainer.isFull()) {
         this.fAugmentationsContainer = this.fAugmentationsContainer.expand();
      }

      return var3;
   }

   public Object getItem(String var1) {
      return this.fAugmentationsContainer.getItem(var1);
   }

   public Object removeItem(String var1) {
      return this.fAugmentationsContainer.removeItem(var1);
   }

   public Enumeration keys() {
      return this.fAugmentationsContainer.keys();
   }

   public void removeAllItems() {
      this.fAugmentationsContainer.clear();
   }

   public String toString() {
      return this.fAugmentationsContainer.toString();
   }

   class LargeContainer extends AugmentationsItemsContainer {
      final Hashtable fAugmentations = new Hashtable();

      LargeContainer() {
         super();
      }

      public Object getItem(Object var1) {
         return this.fAugmentations.get(var1);
      }

      public Object putItem(Object var1, Object var2) {
         return this.fAugmentations.put(var1, var2);
      }

      public Object removeItem(Object var1) {
         return this.fAugmentations.remove(var1);
      }

      public Enumeration keys() {
         return this.fAugmentations.keys();
      }

      public void clear() {
         this.fAugmentations.clear();
      }

      public boolean isFull() {
         return false;
      }

      public AugmentationsItemsContainer expand() {
         return this;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("LargeContainer");
         Enumeration var2 = this.fAugmentations.keys();

         while(var2.hasMoreElements()) {
            Object var3 = var2.nextElement();
            var1.append("\nkey == ");
            var1.append(var3);
            var1.append("; value == ");
            var1.append(this.fAugmentations.get(var3));
         }

         return var1.toString();
      }
   }

   class SmallContainer extends AugmentationsItemsContainer {
      static final int SIZE_LIMIT = 10;
      final Object[] fAugmentations = new Object[20];
      int fNumEntries = 0;

      SmallContainer() {
         super();
      }

      public Enumeration keys() {
         return new SmallContainerKeyEnumeration();
      }

      public Object getItem(Object var1) {
         for(int var2 = 0; var2 < this.fNumEntries * 2; var2 += 2) {
            if (this.fAugmentations[var2].equals(var1)) {
               return this.fAugmentations[var2 + 1];
            }
         }

         return null;
      }

      public Object putItem(Object var1, Object var2) {
         for(int var3 = 0; var3 < this.fNumEntries * 2; var3 += 2) {
            if (this.fAugmentations[var3].equals(var1)) {
               Object var4 = this.fAugmentations[var3 + 1];
               this.fAugmentations[var3 + 1] = var2;
               return var4;
            }
         }

         this.fAugmentations[this.fNumEntries * 2] = var1;
         this.fAugmentations[this.fNumEntries * 2 + 1] = var2;
         ++this.fNumEntries;
         return null;
      }

      public Object removeItem(Object var1) {
         for(int var2 = 0; var2 < this.fNumEntries * 2; var2 += 2) {
            if (this.fAugmentations[var2].equals(var1)) {
               Object var3 = this.fAugmentations[var2 + 1];

               for(int var4 = var2; var4 < this.fNumEntries * 2 - 2; var4 += 2) {
                  this.fAugmentations[var4] = this.fAugmentations[var4 + 2];
                  this.fAugmentations[var4 + 1] = this.fAugmentations[var4 + 3];
               }

               this.fAugmentations[this.fNumEntries * 2 - 2] = null;
               this.fAugmentations[this.fNumEntries * 2 - 1] = null;
               --this.fNumEntries;
               return var3;
            }
         }

         return null;
      }

      public void clear() {
         for(int var1 = 0; var1 < this.fNumEntries * 2; var1 += 2) {
            this.fAugmentations[var1] = null;
            this.fAugmentations[var1 + 1] = null;
         }

         this.fNumEntries = 0;
      }

      public boolean isFull() {
         return this.fNumEntries == 10;
      }

      public AugmentationsItemsContainer expand() {
         LargeContainer var1 = AugmentationsImpl.this.new LargeContainer();

         for(int var2 = 0; var2 < this.fNumEntries * 2; var2 += 2) {
            var1.putItem(this.fAugmentations[var2], this.fAugmentations[var2 + 1]);
         }

         return var1;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("SmallContainer - fNumEntries == " + this.fNumEntries);

         for(int var2 = 0; var2 < 20; var2 += 2) {
            var1.append("\nfAugmentations[");
            var1.append(var2);
            var1.append("] == ");
            var1.append(this.fAugmentations[var2]);
            var1.append("; fAugmentations[");
            var1.append(var2 + 1);
            var1.append("] == ");
            var1.append(this.fAugmentations[var2 + 1]);
         }

         return var1.toString();
      }

      class SmallContainerKeyEnumeration implements Enumeration {
         Object[] enumArray;
         int next;

         SmallContainerKeyEnumeration() {
            this.enumArray = new Object[SmallContainer.this.fNumEntries];
            this.next = 0;

            for(int var2 = 0; var2 < SmallContainer.this.fNumEntries; ++var2) {
               this.enumArray[var2] = SmallContainer.this.fAugmentations[var2 * 2];
            }

         }

         public boolean hasMoreElements() {
            return this.next < this.enumArray.length;
         }

         public Object nextElement() {
            if (this.next >= this.enumArray.length) {
               throw new NoSuchElementException();
            } else {
               Object var1 = this.enumArray[this.next];
               this.enumArray[this.next] = null;
               ++this.next;
               return var1;
            }
         }
      }
   }

   abstract class AugmentationsItemsContainer {
      public abstract Object putItem(Object var1, Object var2);

      public abstract Object getItem(Object var1);

      public abstract Object removeItem(Object var1);

      public abstract Enumeration keys();

      public abstract void clear();

      public abstract boolean isFull();

      public abstract AugmentationsItemsContainer expand();
   }
}
