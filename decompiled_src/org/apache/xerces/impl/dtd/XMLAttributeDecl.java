package org.apache.xerces.impl.dtd;

import org.apache.xerces.xni.QName;

public class XMLAttributeDecl {
   public final QName name = new QName();
   public final XMLSimpleType simpleType = new XMLSimpleType();
   public boolean optional;

   public void setValues(QName var1, XMLSimpleType var2, boolean var3) {
      this.name.setValues(var1);
      this.simpleType.setValues(var2);
      this.optional = var3;
   }

   public void clear() {
      this.name.clear();
      this.simpleType.clear();
      this.optional = false;
   }
}
