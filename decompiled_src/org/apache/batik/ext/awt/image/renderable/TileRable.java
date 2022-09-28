package org.apache.batik.ext.awt.image.renderable;

import java.awt.geom.Rectangle2D;

public interface TileRable extends FilterColorInterpolation {
   Rectangle2D getTileRegion();

   void setTileRegion(Rectangle2D var1);

   Rectangle2D getTiledRegion();

   void setTiledRegion(Rectangle2D var1);

   boolean isOverflow();

   void setOverflow(boolean var1);

   void setSource(Filter var1);

   Filter getSource();
}
