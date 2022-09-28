package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.dv.DatatypeValidator;

public class XMLSimpleType {
   public static final short TYPE_CDATA = 0;
   public static final short TYPE_ENTITY = 1;
   public static final short TYPE_ENUMERATION = 2;
   public static final short TYPE_ID = 3;
   public static final short TYPE_IDREF = 4;
   public static final short TYPE_NMTOKEN = 5;
   public static final short TYPE_NOTATION = 6;
   public static final short TYPE_NAMED = 7;
   public static final short DEFAULT_TYPE_DEFAULT = 3;
   public static final short DEFAULT_TYPE_FIXED = 1;
   public static final short DEFAULT_TYPE_IMPLIED = 0;
   public static final short DEFAULT_TYPE_REQUIRED = 2;
   public short type;
   public String name;
   public String[] enumeration;
   public boolean list;
   public short defaultType;
   public String defaultValue;
   public String nonNormalizedDefaultValue;
   public DatatypeValidator datatypeValidator;

   public void setValues(short var1, String var2, String[] var3, boolean var4, short var5, String var6, String var7, DatatypeValidator var8) {
      this.type = var1;
      this.name = var2;
      if (var3 != null && var3.length > 0) {
         this.enumeration = new String[var3.length];
         System.arraycopy(var3, 0, this.enumeration, 0, this.enumeration.length);
      } else {
         this.enumeration = null;
      }

      this.list = var4;
      this.defaultType = var5;
      this.defaultValue = var6;
      this.nonNormalizedDefaultValue = var7;
      this.datatypeValidator = var8;
   }

   public void setValues(XMLSimpleType var1) {
      this.type = var1.type;
      this.name = var1.name;
      if (var1.enumeration != null && var1.enumeration.length > 0) {
         this.enumeration = new String[var1.enumeration.length];
         System.arraycopy(var1.enumeration, 0, this.enumeration, 0, this.enumeration.length);
      } else {
         this.enumeration = null;
      }

      this.list = var1.list;
      this.defaultType = var1.defaultType;
      this.defaultValue = var1.defaultValue;
      this.nonNormalizedDefaultValue = var1.nonNormalizedDefaultValue;
      this.datatypeValidator = var1.datatypeValidator;
   }

   public void clear() {
      this.type = -1;
      this.name = null;
      this.enumeration = null;
      this.list = false;
      this.defaultType = -1;
      this.defaultValue = null;
      this.nonNormalizedDefaultValue = null;
      this.datatypeValidator = null;
   }
}
