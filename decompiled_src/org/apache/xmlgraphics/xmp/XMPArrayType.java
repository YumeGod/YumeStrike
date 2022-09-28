package org.apache.xmlgraphics.xmp;

public final class XMPArrayType {
   public static final XMPArrayType BAG = new XMPArrayType("Bag");
   public static final XMPArrayType SEQ = new XMPArrayType("Seq");
   public static final XMPArrayType ALT = new XMPArrayType("Alt");
   private String name;

   private XMPArrayType(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return "rdf:" + this.name;
   }
}
