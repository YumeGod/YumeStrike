package javax.xml.transform.dom;

import javax.xml.transform.Result;
import org.w3c.dom.Node;

public class DOMResult implements Result {
   public static final String FEATURE = "http://javax.xml.transform.dom.DOMResult/feature";
   private Node node = null;
   private Node nextSibling = null;
   private String systemId = null;

   public DOMResult() {
      this.setNode((Node)null);
      this.setNextSibling((Node)null);
      this.setSystemId((String)null);
   }

   public DOMResult(Node var1) {
      this.setNode(var1);
      this.setNextSibling((Node)null);
      this.setSystemId((String)null);
   }

   public DOMResult(Node var1, String var2) {
      this.setNode(var1);
      this.setNextSibling((Node)null);
      this.setSystemId(var2);
   }

   public DOMResult(Node var1, Node var2) {
      if (var2 != null) {
         if (var1 == null) {
            throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
         }

         if ((var1.compareDocumentPosition(var2) & 16) == 0) {
            throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
         }
      }

      this.setNode(var1);
      this.setNextSibling(var2);
      this.setSystemId((String)null);
   }

   public DOMResult(Node var1, Node var2, String var3) {
      if (var2 != null) {
         if (var1 == null) {
            throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
         }

         if ((var1.compareDocumentPosition(var2) & 16) == 0) {
            throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
         }
      }

      this.setNode(var1);
      this.setNextSibling(var2);
      this.setSystemId(var3);
   }

   public void setNode(Node var1) {
      if (this.nextSibling != null) {
         if (var1 == null) {
            throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
         }

         if ((var1.compareDocumentPosition(this.nextSibling) & 16) == 0) {
            throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
         }
      }

      this.node = var1;
   }

   public Node getNode() {
      return this.node;
   }

   public void setNextSibling(Node var1) {
      if (var1 != null) {
         if (this.node == null) {
            throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
         }

         if ((this.node.compareDocumentPosition(var1) & 16) == 0) {
            throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
         }
      }

      this.nextSibling = var1;
   }

   public Node getNextSibling() {
      return this.nextSibling;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public String getSystemId() {
      return this.systemId;
   }
}
