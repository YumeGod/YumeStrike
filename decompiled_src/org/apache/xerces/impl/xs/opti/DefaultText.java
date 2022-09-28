package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

public class DefaultText extends NodeImpl implements Text {
   public String getData() throws DOMException {
      return null;
   }

   public void setData(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public int getLength() {
      return 0;
   }

   public String substringData(int var1, int var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void appendData(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void insertData(int var1, String var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void deleteData(int var1, int var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void replaceData(int var1, int var2, String var3) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Text splitText(int var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public boolean isElementContentWhitespace() {
      throw new DOMException((short)9, "Method not supported");
   }

   public String getWholeText() {
      throw new DOMException((short)9, "Method not supported");
   }

   public Text replaceWholeText(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }
}
