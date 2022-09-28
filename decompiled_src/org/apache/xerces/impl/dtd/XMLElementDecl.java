package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.xni.QName;

public class XMLElementDecl {
   public static final short TYPE_ANY = 0;
   public static final short TYPE_EMPTY = 1;
   public static final short TYPE_MIXED = 2;
   public static final short TYPE_CHILDREN = 3;
   public static final short TYPE_SIMPLE = 4;
   public final QName name = new QName();
   public int scope = -1;
   public short type = -1;
   public ContentModelValidator contentModelValidator;
   public final XMLSimpleType simpleType = new XMLSimpleType();

   public void setValues(QName var1, int var2, short var3, ContentModelValidator var4, XMLSimpleType var5) {
      this.name.setValues(var1);
      this.scope = var2;
      this.type = var3;
      this.contentModelValidator = var4;
      this.simpleType.setValues(var5);
   }

   public void clear() {
      this.name.clear();
      this.type = -1;
      this.scope = -1;
      this.contentModelValidator = null;
      this.simpleType.clear();
   }
}
