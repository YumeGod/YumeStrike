package org.apache.batik.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;

public abstract class AbstractNotation extends AbstractNode implements Notation {
   protected String nodeName;
   protected String publicId;
   protected String systemId;

   public short getNodeType() {
      return 12;
   }

   public void setNodeName(String var1) {
      this.nodeName = var1;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public void setPublicId(String var1) {
      this.publicId = var1;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public void setTextContent(String var1) throws DOMException {
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      AbstractNotation var3 = (AbstractNotation)var1;
      var3.nodeName = this.nodeName;
      var3.publicId = this.publicId;
      var3.systemId = this.systemId;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      AbstractNotation var3 = (AbstractNotation)var1;
      var3.nodeName = this.nodeName;
      var3.publicId = this.publicId;
      var3.systemId = this.systemId;
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractNotation var2 = (AbstractNotation)var1;
      var2.nodeName = this.nodeName;
      var2.publicId = this.publicId;
      var2.systemId = this.systemId;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractNotation var2 = (AbstractNotation)var1;
      var2.nodeName = this.nodeName;
      var2.publicId = this.publicId;
      var2.systemId = this.systemId;
      return var1;
   }
}
