package graph;

import common.CommonUtils;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RefreshGraph implements Runnable {
   protected List nodes = new LinkedList();
   protected List highlights = new LinkedList();
   protected List routes = new LinkedList();
   protected Refreshable graph = null;

   public RefreshGraph(Refreshable var1) {
      this.graph = var1;
   }

   public void go() {
      CommonUtils.runSafe(this);
   }

   public void addRoute(Route var1) {
      this.routes.add(var1);
   }

   public void addNode(String var1, String var2, String var3, Image var4, String var5) {
      Node var6 = new Node();
      var6.id = var1;
      var6.label = var2;
      var6.description = var3;
      var6.iconz = var4;
      var6.tooltip = var5;
      this.nodes.add(var6);
   }

   public void addHighlight(String var1, String var2) {
      Highlight var3 = new Highlight();
      var3.gateway = var1;
      var3.host = var2;
      this.highlights.add(var3);
   }

   public void run() {
      this.graph.start();
      Iterator var1 = this.nodes.iterator();

      while(var1.hasNext()) {
         Node var2 = (Node)var1.next();
         this.graph.addNode(var2.id, var2.label, var2.description, var2.iconz, var2.tooltip);
      }

      this.graph.setRoutes((Route[])((Route[])this.routes.toArray(new Route[0])));
      var1 = this.highlights.iterator();

      while(var1.hasNext()) {
         Highlight var3 = (Highlight)var1.next();
         this.graph.highlightRoute(var3.gateway, var3.host);
      }

      this.graph.deleteNodes();
      this.graph.end();
   }

   private static class Node {
      public String id;
      public String label;
      public String description;
      public Image iconz;
      public String tooltip;

      private Node() {
         this.id = "";
         this.label = "";
         this.description = "";
         this.iconz = null;
         this.tooltip = "";
      }

      // $FF: synthetic method
      Node(Object var1) {
         this();
      }
   }

   private static class Highlight {
      public String gateway;
      public String host;

      private Highlight() {
         this.gateway = "";
         this.host = "";
      }

      // $FF: synthetic method
      Highlight(Object var1) {
         this();
      }
   }
}
