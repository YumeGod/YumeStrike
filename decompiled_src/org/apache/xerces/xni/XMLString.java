package org.apache.xerces.xni;

public class XMLString {
   public char[] ch;
   public int offset;
   public int length;

   public XMLString() {
   }

   public XMLString(char[] var1, int var2, int var3) {
      this.setValues(var1, var2, var3);
   }

   public XMLString(XMLString var1) {
      this.setValues(var1);
   }

   public void setValues(char[] var1, int var2, int var3) {
      this.ch = var1;
      this.offset = var2;
      this.length = var3;
   }

   public void setValues(XMLString var1) {
      this.setValues(var1.ch, var1.offset, var1.length);
   }

   public void clear() {
      this.ch = null;
      this.offset = 0;
      this.length = -1;
   }

   public boolean equals(char[] var1, int var2, int var3) {
      if (var1 == null) {
         return false;
      } else if (this.length != var3) {
         return false;
      } else {
         for(int var4 = 0; var4 < var3; ++var4) {
            if (this.ch[this.offset + var4] != var1[var2 + var4]) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean equals(String var1) {
      if (var1 == null) {
         return false;
      } else if (this.length != var1.length()) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.length; ++var2) {
            if (this.ch[this.offset + var2] != var1.charAt(var2)) {
               return false;
            }
         }

         return true;
      }
   }

   public String toString() {
      return this.length > 0 ? new String(this.ch, this.offset, this.length) : "";
   }
}
