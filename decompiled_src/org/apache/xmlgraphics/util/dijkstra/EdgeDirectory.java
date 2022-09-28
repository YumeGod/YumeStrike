package org.apache.xmlgraphics.util.dijkstra;

import java.util.Iterator;

public interface EdgeDirectory {
   int getPenalty(Vertex var1, Vertex var2);

   Iterator getDestinations(Vertex var1);
}
