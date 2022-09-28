package org.apache.batik.ext.awt.image.rendered;

import java.awt.image.Raster;

public interface TileStore {
   void setTile(int var1, int var2, Raster var3);

   Raster getTile(int var1, int var2);

   Raster getTileNoCompute(int var1, int var2);
}
