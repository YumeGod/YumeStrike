package org.apache.xerces.util;

import org.apache.xerces.xni.XMLString;

public class XMLStringBuffer extends XMLString {
   public static final int DEFAULT_SIZE = 32;

   public XMLStringBuffer() {
      this((int)32);
   }

   public XMLStringBuffer(int var1) {
      super.ch = new char[var1];
   }

   public XMLStringBuffer(char var1) {
      this((int)1);
      this.append(var1);
   }

   public XMLStringBuffer(String var1) {
      this(var1.length());
      this.append(var1);
   }

   public XMLStringBuffer(char[] var1, int var2, int var3) {
      this(var3);
      this.append(var1, var2, var3);
   }

   public XMLStringBuffer(XMLString var1) {
      this(var1.length);
      this.append(var1);
   }

   public void clear() {
      super.offset = 0;
      super.length = 0;
   }

   public void append(char var1) {
      if (super.length + 1 > super.ch.length) {
         int var2 = super.ch.length * 2;
         if (var2 < super.ch.length + 32) {
            var2 = super.ch.length + 32;
         }

         char[] var3 = new char[var2];
         System.arraycopy(super.ch, 0, var3, 0, super.length);
         super.ch = var3;
      }

      super.ch[super.length] = var1;
      ++super.length;
   }

   public void append(String var1) {
      int var2 = var1.length();
      if (super.length + var2 > super.ch.length) {
         int var3 = super.ch.length * 2;
         if (var3 < super.length + var2 + 32) {
            var3 = super.ch.length + var2 + 32;
         }

         char[] var4 = new char[var3];
         System.arraycopy(super.ch, 0, var4, 0, super.length);
         super.ch = var4;
      }

      var1.getChars(0, var2, super.ch, super.length);
      super.length += var2;
   }

   public void append(char[] var1, int var2, int var3) {
      if (super.length + var3 > super.ch.length) {
         char[] var4 = new char[super.ch.length + var3 + 32];
         System.arraycopy(super.ch, 0, var4, 0, super.length);
         super.ch = var4;
      }

      System.arraycopy(var1, var2, super.ch, super.length, var3);
      super.length += var3;
   }

   public void append(XMLString var1) {
      this.append(var1.ch, var1.offset, var1.length);
   }
}
