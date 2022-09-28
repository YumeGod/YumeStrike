package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

public class DefaultElement extends NodeImpl implements Element {
   public DefaultElement() {
   }

   public DefaultElement(String var1, String var2, String var3, String var4, short var5) {
      super(var1, var2, var3, var4, var5);
   }

   public String getTagName() {
      return null;
   }

   public String getAttribute(String var1) {
      return null;
   }

   public Attr getAttributeNode(String var1) {
      return null;
   }

   public NodeList getElementsByTagName(String var1) {
      return null;
   }

   public String getAttributeNS(String var1, String var2) {
      return null;
   }

   public Attr getAttributeNodeNS(String var1, String var2) {
      return null;
   }

   public NodeList getElementsByTagNameNS(String var1, String var2) {
      return null;
   }

   public boolean hasAttribute(String var1) {
      return false;
   }

   public boolean hasAttributeNS(String var1, String var2) {
      return false;
   }

   public TypeInfo getSchemaTypeInfo() {
      return null;
   }

   public void setAttribute(String var1, String var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void removeAttribute(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Attr removeAttributeNode(Attr var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Attr setAttributeNode(Attr var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void setAttributeNS(String var1, String var2, String var3) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void removeAttributeNS(String var1, String var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Attr setAttributeNodeNS(Attr var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void setIdAttributeNode(Attr var1, boolean var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void setIdAttribute(String var1, boolean var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void setIdAttributeNS(String var1, String var2, boolean var3) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }
}
