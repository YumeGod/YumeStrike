package org.apache.xmlgraphics.util.dijkstra;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DijkstraAlgorithm {
   public static final int INFINITE = Integer.MAX_VALUE;
   private final Comparator penaltyComparator = new Comparator() {
      public int compare(Object left, Object right) {
         int leftPenalty = DijkstraAlgorithm.this.getLowestPenalty((Vertex)left);
         int rightPenalty = DijkstraAlgorithm.this.getLowestPenalty((Vertex)right);
         if (leftPenalty < rightPenalty) {
            return -1;
         } else {
            return leftPenalty == rightPenalty ? ((Comparable)left).compareTo(right) : 1;
         }
      }
   };
   private EdgeDirectory edgeDirectory;
   private TreeSet priorityQueue;
   private Set finishedVertices;
   private Map lowestPenalties;
   private Map predecessors;

   public DijkstraAlgorithm(EdgeDirectory edgeDirectory) {
      this.priorityQueue = new TreeSet(this.penaltyComparator);
      this.finishedVertices = new HashSet();
      this.lowestPenalties = new HashMap();
      this.predecessors = new HashMap();
      this.edgeDirectory = edgeDirectory;
   }

   protected int getPenalty(Vertex start, Vertex end) {
      return this.edgeDirectory.getPenalty(start, end);
   }

   protected Iterator getDestinations(Vertex origin) {
      return this.edgeDirectory.getDestinations(origin);
   }

   private void reset() {
      this.finishedVertices.clear();
      this.priorityQueue.clear();
      this.lowestPenalties.clear();
      this.predecessors.clear();
   }

   public void execute(Vertex start, Vertex destination) {
      if (start != null && destination != null) {
         this.reset();
         this.setShortestDistance(start, 0);
         this.priorityQueue.add(start);

         while(this.priorityQueue.size() > 0) {
            Vertex u = (Vertex)this.priorityQueue.first();
            this.priorityQueue.remove(u);
            if (destination.equals(u)) {
               break;
            }

            this.finishedVertices.add(u);
            this.relax(u);
         }

      } else {
         throw new NullPointerException("start and destination may not be null");
      }
   }

   private void relax(Vertex u) {
      Iterator iter = this.getDestinations(u);

      while(iter.hasNext()) {
         Vertex v = (Vertex)iter.next();
         if (!this.isFinished(v)) {
            int shortDist = this.getLowestPenalty(u) + this.getPenalty(u, v);
            if (shortDist < this.getLowestPenalty(v)) {
               this.setShortestDistance(v, shortDist);
               this.setPredecessor(v, u);
            }
         }
      }

   }

   private void setPredecessor(Vertex a, Vertex b) {
      this.predecessors.put(a, b);
   }

   private boolean isFinished(Vertex v) {
      return this.finishedVertices.contains(v);
   }

   private void setShortestDistance(Vertex vertex, int distance) {
      this.priorityQueue.remove(vertex);
      this.lowestPenalties.put(vertex, new Integer(distance));
      this.priorityQueue.add(vertex);
   }

   public int getLowestPenalty(Vertex vertex) {
      Integer d = (Integer)this.lowestPenalties.get(vertex);
      return d == null ? Integer.MAX_VALUE : d;
   }

   public Vertex getPredecessor(Vertex vertex) {
      return (Vertex)this.predecessors.get(vertex);
   }
}
