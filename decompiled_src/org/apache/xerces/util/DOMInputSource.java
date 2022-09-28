package org.apache.xerces.util;

import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Node;

public final class DOMInputSource extends XMLInputSource {
   private Node fNode;

   public DOMInputSource() {
      this((Node)null);
   }

   public DOMInputSource(Node var1) {
      super((String)null, getSystemIdFromNode(var1), (String)null);
      this.fNode = var1;
   }

   public DOMInputSource(Node var1, String var2) {
      super((String)null, var2, (String)null);
      this.fNode = var1;
   }

   public Node getNode() {
      return this.fNode;
   }

   public void setNode(Node var1) {
      this.fNode = var1;
   }

   private static String getSystemIdFromNode(Node var0) {
      if (var0 != null) {
         try {
            return var0.getBaseURI();
         } catch (NoSuchMethodError var3) {
            return null;
         } catch (Exception var4) {
            return null;
         }
      } else {
         return null;
      }
   }
}
