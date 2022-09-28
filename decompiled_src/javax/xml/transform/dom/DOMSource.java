package javax.xml.transform.dom;

import javax.xml.transform.Source;
import org.w3c.dom.Node;

public class DOMSource implements Source {
   private Node node;
   private String systemID;
   public static final String FEATURE = "http://javax.xml.transform.dom.DOMSource/feature";

   public DOMSource() {
   }

   public DOMSource(Node var1) {
      this.setNode(var1);
   }

   public DOMSource(Node var1, String var2) {
      this.setNode(var1);
      this.setSystemId(var2);
   }

   public void setNode(Node var1) {
      this.node = var1;
   }

   public Node getNode() {
      return this.node;
   }

   public void setSystemId(String var1) {
      this.systemID = var1;
   }

   public String getSystemId() {
      return this.systemID;
   }
}
