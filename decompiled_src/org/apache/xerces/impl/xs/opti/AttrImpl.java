package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

public class AttrImpl extends NodeImpl implements Attr {
   Element element;
   String value;

   public AttrImpl() {
      super.nodeType = 2;
   }

   public AttrImpl(Element var1, String var2, String var3, String var4, String var5, String var6) {
      super(var2, var3, var4, var5, (short)2);
      this.element = var1;
      this.value = var6;
   }

   public String getName() {
      return super.rawname;
   }

   public boolean getSpecified() {
      return true;
   }

   public String getValue() {
      return this.value;
   }

   public Element getOwnerElement() {
      return this.element;
   }

   public void setValue(String var1) throws DOMException {
      this.value = var1;
   }

   public boolean isId() {
      return false;
   }

   public TypeInfo getSchemaTypeInfo() {
      return null;
   }
}
