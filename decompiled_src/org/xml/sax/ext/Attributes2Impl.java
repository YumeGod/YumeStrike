package org.xml.sax.ext;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class Attributes2Impl extends AttributesImpl implements Attributes2 {
   private boolean[] declared;
   private boolean[] specified;

   public Attributes2Impl() {
   }

   public Attributes2Impl(Attributes var1) {
      super(var1);
   }

   public boolean isDeclared(int var1) {
      if (var1 >= 0 && var1 < this.getLength()) {
         return this.declared[var1];
      } else {
         throw new ArrayIndexOutOfBoundsException("No attribute at index: " + var1);
      }
   }

   public boolean isDeclared(String var1, String var2) {
      int var3 = this.getIndex(var1, var2);
      if (var3 < 0) {
         throw new IllegalArgumentException("No such attribute: local=" + var2 + ", namespace=" + var1);
      } else {
         return this.declared[var3];
      }
   }

   public boolean isDeclared(String var1) {
      int var2 = this.getIndex(var1);
      if (var2 < 0) {
         throw new IllegalArgumentException("No such attribute: " + var1);
      } else {
         return this.declared[var2];
      }
   }

   public boolean isSpecified(int var1) {
      if (var1 >= 0 && var1 < this.getLength()) {
         return this.specified[var1];
      } else {
         throw new ArrayIndexOutOfBoundsException("No attribute at index: " + var1);
      }
   }

   public boolean isSpecified(String var1, String var2) {
      int var3 = this.getIndex(var1, var2);
      if (var3 < 0) {
         throw new IllegalArgumentException("No such attribute: local=" + var2 + ", namespace=" + var1);
      } else {
         return this.specified[var3];
      }
   }

   public boolean isSpecified(String var1) {
      int var2 = this.getIndex(var1);
      if (var2 < 0) {
         throw new IllegalArgumentException("No such attribute: " + var1);
      } else {
         return this.specified[var2];
      }
   }

   public void setAttributes(Attributes var1) {
      int var2 = var1.getLength();
      super.setAttributes(var1);
      this.declared = new boolean[var2];
      this.specified = new boolean[var2];
      if (var1 instanceof Attributes2) {
         Attributes2 var3 = (Attributes2)var1;

         for(int var4 = 0; var4 < var2; ++var4) {
            this.declared[var4] = var3.isDeclared(var4);
            this.specified[var4] = var3.isSpecified(var4);
         }
      } else {
         for(int var5 = 0; var5 < var2; ++var5) {
            this.declared[var5] = !"CDATA".equals(var1.getType(var5));
            this.specified[var5] = true;
         }
      }

   }

   public void addAttribute(String var1, String var2, String var3, String var4, String var5) {
      super.addAttribute(var1, var2, var3, var4, var5);
      int var6 = this.getLength();
      if (var6 < this.specified.length) {
         boolean[] var7 = new boolean[var6];
         System.arraycopy(this.declared, 0, var7, 0, this.declared.length);
         this.declared = var7;
         var7 = new boolean[var6];
         System.arraycopy(this.specified, 0, var7, 0, this.specified.length);
         this.specified = var7;
      }

      this.specified[var6 - 1] = true;
      this.declared[var6 - 1] = !"CDATA".equals(var4);
   }

   public void removeAttribute(int var1) {
      int var2 = this.getLength() - 1;
      super.removeAttribute(var1);
      if (var1 != var2) {
         System.arraycopy(this.declared, var1 + 1, this.declared, var1, var2 - var1);
         System.arraycopy(this.specified, var1 + 1, this.specified, var1, var2 - var1);
      }

   }

   public void setDeclared(int var1, boolean var2) {
      if (var1 >= 0 && var1 < this.getLength()) {
         this.declared[var1] = var2;
      } else {
         throw new ArrayIndexOutOfBoundsException("No attribute at index: " + var1);
      }
   }

   public void setSpecified(int var1, boolean var2) {
      if (var1 >= 0 && var1 < this.getLength()) {
         this.specified[var1] = var2;
      } else {
         throw new ArrayIndexOutOfBoundsException("No attribute at index: " + var1);
      }
   }
}
