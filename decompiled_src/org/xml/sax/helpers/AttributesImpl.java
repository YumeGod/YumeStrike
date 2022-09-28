package org.xml.sax.helpers;

import org.xml.sax.Attributes;

public class AttributesImpl implements Attributes {
   int length;
   String[] data;

   public AttributesImpl() {
      this.length = 0;
      this.data = null;
   }

   public AttributesImpl(Attributes var1) {
      this.setAttributes(var1);
   }

   public int getLength() {
      return this.length;
   }

   public String getURI(int var1) {
      return var1 >= 0 && var1 < this.length ? this.data[var1 * 5] : null;
   }

   public String getLocalName(int var1) {
      return var1 >= 0 && var1 < this.length ? this.data[var1 * 5 + 1] : null;
   }

   public String getQName(int var1) {
      return var1 >= 0 && var1 < this.length ? this.data[var1 * 5 + 2] : null;
   }

   public String getType(int var1) {
      return var1 >= 0 && var1 < this.length ? this.data[var1 * 5 + 3] : null;
   }

   public String getValue(int var1) {
      return var1 >= 0 && var1 < this.length ? this.data[var1 * 5 + 4] : null;
   }

   public int getIndex(String var1, String var2) {
      int var3 = this.length * 5;

      for(int var4 = 0; var4 < var3; var4 += 5) {
         if (this.data[var4].equals(var1) && this.data[var4 + 1].equals(var2)) {
            return var4 / 5;
         }
      }

      return -1;
   }

   public int getIndex(String var1) {
      int var2 = this.length * 5;

      for(int var3 = 0; var3 < var2; var3 += 5) {
         if (this.data[var3 + 2].equals(var1)) {
            return var3 / 5;
         }
      }

      return -1;
   }

   public String getType(String var1, String var2) {
      int var3 = this.length * 5;

      for(int var4 = 0; var4 < var3; var4 += 5) {
         if (this.data[var4].equals(var1) && this.data[var4 + 1].equals(var2)) {
            return this.data[var4 + 3];
         }
      }

      return null;
   }

   public String getType(String var1) {
      int var2 = this.length * 5;

      for(int var3 = 0; var3 < var2; var3 += 5) {
         if (this.data[var3 + 2].equals(var1)) {
            return this.data[var3 + 3];
         }
      }

      return null;
   }

   public String getValue(String var1, String var2) {
      int var3 = this.length * 5;

      for(int var4 = 0; var4 < var3; var4 += 5) {
         if (this.data[var4].equals(var1) && this.data[var4 + 1].equals(var2)) {
            return this.data[var4 + 4];
         }
      }

      return null;
   }

   public String getValue(String var1) {
      int var2 = this.length * 5;

      for(int var3 = 0; var3 < var2; var3 += 5) {
         if (this.data[var3 + 2].equals(var1)) {
            return this.data[var3 + 4];
         }
      }

      return null;
   }

   public void clear() {
      if (this.data != null) {
         for(int var1 = 0; var1 < this.length * 5; ++var1) {
            this.data[var1] = null;
         }
      }

      this.length = 0;
   }

   public void setAttributes(Attributes var1) {
      this.clear();
      this.length = var1.getLength();
      if (this.length > 0) {
         this.data = new String[this.length * 5];

         for(int var2 = 0; var2 < this.length; ++var2) {
            this.data[var2 * 5] = var1.getURI(var2);
            this.data[var2 * 5 + 1] = var1.getLocalName(var2);
            this.data[var2 * 5 + 2] = var1.getQName(var2);
            this.data[var2 * 5 + 3] = var1.getType(var2);
            this.data[var2 * 5 + 4] = var1.getValue(var2);
         }
      }

   }

   public void addAttribute(String var1, String var2, String var3, String var4, String var5) {
      this.ensureCapacity(this.length + 1);
      this.data[this.length * 5] = var1;
      this.data[this.length * 5 + 1] = var2;
      this.data[this.length * 5 + 2] = var3;
      this.data[this.length * 5 + 3] = var4;
      this.data[this.length * 5 + 4] = var5;
      ++this.length;
   }

   public void setAttribute(int var1, String var2, String var3, String var4, String var5, String var6) {
      if (var1 >= 0 && var1 < this.length) {
         this.data[var1 * 5] = var2;
         this.data[var1 * 5 + 1] = var3;
         this.data[var1 * 5 + 2] = var4;
         this.data[var1 * 5 + 3] = var5;
         this.data[var1 * 5 + 4] = var6;
      } else {
         this.badIndex(var1);
      }

   }

   public void removeAttribute(int var1) {
      if (var1 >= 0 && var1 < this.length) {
         if (var1 < this.length - 1) {
            System.arraycopy(this.data, (var1 + 1) * 5, this.data, var1 * 5, (this.length - var1 - 1) * 5);
         }

         var1 = (this.length - 1) * 5;
         this.data[var1++] = null;
         this.data[var1++] = null;
         this.data[var1++] = null;
         this.data[var1++] = null;
         this.data[var1] = null;
         --this.length;
      } else {
         this.badIndex(var1);
      }

   }

   public void setURI(int var1, String var2) {
      if (var1 >= 0 && var1 < this.length) {
         this.data[var1 * 5] = var2;
      } else {
         this.badIndex(var1);
      }

   }

   public void setLocalName(int var1, String var2) {
      if (var1 >= 0 && var1 < this.length) {
         this.data[var1 * 5 + 1] = var2;
      } else {
         this.badIndex(var1);
      }

   }

   public void setQName(int var1, String var2) {
      if (var1 >= 0 && var1 < this.length) {
         this.data[var1 * 5 + 2] = var2;
      } else {
         this.badIndex(var1);
      }

   }

   public void setType(int var1, String var2) {
      if (var1 >= 0 && var1 < this.length) {
         this.data[var1 * 5 + 3] = var2;
      } else {
         this.badIndex(var1);
      }

   }

   public void setValue(int var1, String var2) {
      if (var1 >= 0 && var1 < this.length) {
         this.data[var1 * 5 + 4] = var2;
      } else {
         this.badIndex(var1);
      }

   }

   private void ensureCapacity(int var1) {
      if (var1 > 0) {
         int var2;
         if (this.data != null && this.data.length != 0) {
            if (this.data.length >= var1 * 5) {
               return;
            }

            var2 = this.data.length;
         } else {
            var2 = 25;
         }

         while(var2 < var1 * 5) {
            var2 *= 2;
         }

         String[] var3 = new String[var2];
         if (this.length > 0) {
            System.arraycopy(this.data, 0, var3, 0, this.length * 5);
         }

         this.data = var3;
      }
   }

   private void badIndex(int var1) throws ArrayIndexOutOfBoundsException {
      String var2 = "Attempt to modify attribute at illegal index: " + var1;
      throw new ArrayIndexOutOfBoundsException(var2);
   }
}
