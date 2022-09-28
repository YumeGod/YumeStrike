package org.apache.xmlgraphics.util.dijkstra;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultEdgeDirectory implements EdgeDirectory {
   private Map edges = new HashMap();

   public void addEdge(Edge edge) {
      Map directEdges = (Map)this.edges.get(edge.getStart());
      if (directEdges == null) {
         directEdges = new HashMap();
         this.edges.put(edge.getStart(), directEdges);
      }

      ((Map)directEdges).put(edge.getEnd(), edge);
   }

   public int getPenalty(Vertex start, Vertex end) {
      Map edgeMap = (Map)this.edges.get(start);
      if (edgeMap != null) {
         Edge route = (Edge)edgeMap.get(end);
         if (route != null) {
            int penalty = route.getPenalty();
            if (penalty < 0) {
               throw new IllegalStateException("Penalty must not be negative");
            }

            return penalty;
         }
      }

      return 0;
   }

   public Iterator getDestinations(Vertex origin) {
      Map directRoutes = (Map)this.edges.get(origin);
      if (directRoutes != null) {
         Iterator iter = directRoutes.keySet().iterator();
         return iter;
      } else {
         return Collections.EMPTY_LIST.iterator();
      }
   }

   public Iterator getEdges(Vertex origin) {
      Map directRoutes = (Map)this.edges.get(origin);
      if (directRoutes != null) {
         Iterator iter = directRoutes.values().iterator();
         return iter;
      } else {
         return Collections.EMPTY_LIST.iterator();
      }
   }

   public Edge getBestEdge(Vertex start, Vertex end) {
      Edge best = null;
      Iterator iter = this.getEdges(start);

      while(true) {
         Edge edge;
         do {
            do {
               if (!iter.hasNext()) {
                  return best;
               }

               edge = (Edge)iter.next();
            } while(!edge.getEnd().equals(end));
         } while(best != null && edge.getPenalty() >= best.getPenalty());

         best = edge;
      }
   }
}
