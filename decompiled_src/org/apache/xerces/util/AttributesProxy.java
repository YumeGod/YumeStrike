package org.apache.xerces.util;

import org.apache.xerces.xni.XMLAttributes;
import org.xml.sax.AttributeList;
import org.xml.sax.ext.Attributes2;

public final class AttributesProxy implements AttributeList, Attributes2 {
   private XMLAttributes fAttributes;

   public AttributesProxy(XMLAttributes var1) {
      this.fAttributes = var1;
   }

   public void setAttributes(XMLAttributes var1) {
      this.fAttributes = var1;
   }

   public XMLAttributes getAttributes() {
      return this.fAttributes;
   }

   public int getLength() {
      return this.fAttributes.getLength();
   }

   public String getQName(int var1) {
      return this.fAttributes.getQName(var1);
   }

   public String getURI(int var1) {
      String var2 = this.fAttributes.getURI(var1);
      return var2 != null ? var2 : XMLSymbols.EMPTY_STRING;
   }

   public String getLocalName(int var1) {
      return this.fAttributes.getLocalName(var1);
   }

   public String getType(int var1) {
      return this.fAttributes.getType(var1);
   }

   public String getType(String var1) {
      return this.fAttributes.getType(var1);
   }

   public String getType(String var1, String var2) {
      return var1.equals(XMLSymbols.EMPTY_STRING) ? this.fAttributes.getType((String)null, var2) : this.fAttributes.getType(var1, var2);
   }

   public String getValue(int var1) {
      return this.fAttributes.getValue(var1);
   }

   public String getValue(String var1) {
      return this.fAttributes.getValue(var1);
   }

   public String getValue(String var1, String var2) {
      return var1.equals(XMLSymbols.EMPTY_STRING) ? this.fAttributes.getValue((String)null, var2) : this.fAttributes.getValue(var1, var2);
   }

   public int getIndex(String var1) {
      return this.fAttributes.getIndex(var1);
   }

   public int getIndex(String var1, String var2) {
      return var1.equals(XMLSymbols.EMPTY_STRING) ? this.fAttributes.getIndex((String)null, var2) : this.fAttributes.getIndex(var1, var2);
   }

   public boolean isDeclared(int var1) {
      if (var1 >= 0 && var1 < this.fAttributes.getLength()) {
         return Boolean.TRUE.equals(this.fAttributes.getAugmentations(var1).getItem("ATTRIBUTE_DECLARED"));
      } else {
         throw new ArrayIndexOutOfBoundsException(var1);
      }
   }

   public boolean isDeclared(String var1) {
      int var2 = this.getIndex(var1);
      if (var2 == -1) {
         throw new IllegalArgumentException(var1);
      } else {
         return Boolean.TRUE.equals(this.fAttributes.getAugmentations(var2).getItem("ATTRIBUTE_DECLARED"));
      }
   }

   public boolean isDeclared(String var1, String var2) {
      int var3 = this.getIndex(var1, var2);
      if (var3 == -1) {
         throw new IllegalArgumentException(var2);
      } else {
         return Boolean.TRUE.equals(this.fAttributes.getAugmentations(var3).getItem("ATTRIBUTE_DECLARED"));
      }
   }

   public boolean isSpecified(int var1) {
      if (var1 >= 0 && var1 < this.fAttributes.getLength()) {
         return this.fAttributes.isSpecified(var1);
      } else {
         throw new ArrayIndexOutOfBoundsException(var1);
      }
   }

   public boolean isSpecified(String var1) {
      int var2 = this.getIndex(var1);
      if (var2 == -1) {
         throw new IllegalArgumentException(var1);
      } else {
         return this.fAttributes.isSpecified(var2);
      }
   }

   public boolean isSpecified(String var1, String var2) {
      int var3 = this.getIndex(var1, var2);
      if (var3 == -1) {
         throw new IllegalArgumentException(var2);
      } else {
         return this.fAttributes.isSpecified(var3);
      }
   }

   public String getName(int var1) {
      return this.fAttributes.getQName(var1);
   }
}
