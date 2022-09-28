package org.apache.xerces.impl.dtd;

public class XMLContentSpec {
   public static final short CONTENTSPECNODE_LEAF = 0;
   public static final short CONTENTSPECNODE_ZERO_OR_ONE = 1;
   public static final short CONTENTSPECNODE_ZERO_OR_MORE = 2;
   public static final short CONTENTSPECNODE_ONE_OR_MORE = 3;
   public static final short CONTENTSPECNODE_CHOICE = 4;
   public static final short CONTENTSPECNODE_SEQ = 5;
   public static final short CONTENTSPECNODE_ANY = 6;
   public static final short CONTENTSPECNODE_ANY_OTHER = 7;
   public static final short CONTENTSPECNODE_ANY_LOCAL = 8;
   public static final short CONTENTSPECNODE_ANY_LAX = 22;
   public static final short CONTENTSPECNODE_ANY_OTHER_LAX = 23;
   public static final short CONTENTSPECNODE_ANY_LOCAL_LAX = 24;
   public static final short CONTENTSPECNODE_ANY_SKIP = 38;
   public static final short CONTENTSPECNODE_ANY_OTHER_SKIP = 39;
   public static final short CONTENTSPECNODE_ANY_LOCAL_SKIP = 40;
   public short type;
   public Object value;
   public Object otherValue;

   public XMLContentSpec() {
      this.clear();
   }

   public XMLContentSpec(short var1, Object var2, Object var3) {
      this.setValues(var1, var2, var3);
   }

   public XMLContentSpec(XMLContentSpec var1) {
      this.setValues(var1);
   }

   public XMLContentSpec(Provider var1, int var2) {
      this.setValues(var1, var2);
   }

   public void clear() {
      this.type = -1;
      this.value = null;
      this.otherValue = null;
   }

   public void setValues(short var1, Object var2, Object var3) {
      this.type = var1;
      this.value = var2;
      this.otherValue = var3;
   }

   public void setValues(XMLContentSpec var1) {
      this.type = var1.type;
      this.value = var1.value;
      this.otherValue = var1.otherValue;
   }

   public void setValues(Provider var1, int var2) {
      if (!var1.getContentSpec(var2, this)) {
         this.clear();
      }

   }

   public int hashCode() {
      return this.type << 16 | this.value.hashCode() << 8 | this.otherValue.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof XMLContentSpec) {
         XMLContentSpec var2 = (XMLContentSpec)var1;
         return this.type == var2.type && this.value == var2.value && this.otherValue == var2.otherValue;
      } else {
         return false;
      }
   }

   public interface Provider {
      boolean getContentSpec(int var1, XMLContentSpec var2);
   }
}
