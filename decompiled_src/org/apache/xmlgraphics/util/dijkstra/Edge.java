package org.apache.xmlgraphics.util.dijkstra;

public interface Edge {
   Vertex getStart();

   Vertex getEnd();

   int getPenalty();
}
