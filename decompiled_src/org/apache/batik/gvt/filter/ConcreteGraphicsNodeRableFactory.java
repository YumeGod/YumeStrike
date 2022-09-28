package org.apache.batik.gvt.filter;

import org.apache.batik.gvt.GraphicsNode;

public class ConcreteGraphicsNodeRableFactory implements GraphicsNodeRableFactory {
   public GraphicsNodeRable createGraphicsNodeRable(GraphicsNode var1) {
      return (GraphicsNodeRable)var1.getGraphicsNodeRable(true);
   }
}
