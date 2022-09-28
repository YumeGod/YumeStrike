package org.apache.batik.bridge;

import org.apache.batik.gvt.GraphicsNode;

public class StyleReference {
   private GraphicsNode node;
   private String styleAttribute;

   public StyleReference(GraphicsNode var1, String var2) {
      this.node = var1;
      this.styleAttribute = var2;
   }

   public GraphicsNode getGraphicsNode() {
      return this.node;
   }

   public String getStyleAttribute() {
      return this.styleAttribute;
   }
}
