package org.apache.xerces.impl.xs.opti;

public class NodeImpl extends DefaultNode {
   String prefix;
   String localpart;
   String rawname;
   String uri;
   short nodeType;
   boolean hidden;

   public NodeImpl() {
   }

   public NodeImpl(String var1, String var2, String var3, String var4, short var5) {
      this.prefix = var1;
      this.localpart = var2;
      this.rawname = var3;
      this.uri = var4;
      this.nodeType = var5;
   }

   public String getNodeName() {
      return this.rawname;
   }

   public String getNamespaceURI() {
      return this.uri;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getLocalName() {
      return this.localpart;
   }

   public short getNodeType() {
      return this.nodeType;
   }

   public void setReadOnly(boolean var1, boolean var2) {
      this.hidden = var1;
   }

   public boolean getReadOnly() {
      return this.hidden;
   }
}
