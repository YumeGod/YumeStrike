package graph;

import java.awt.Image;

public interface Refreshable {
   void start();

   Object addNode(String var1, String var2, String var3, Image var4, String var5);

   void setRoutes(Route[] var1);

   void highlightRoute(String var1, String var2);

   void deleteNodes();

   void end();
}
