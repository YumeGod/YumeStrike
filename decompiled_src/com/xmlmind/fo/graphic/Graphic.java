package com.xmlmind.fo.graphic;

public interface Graphic {
   int TYPE_VECTOR = 0;
   int TYPE_RASTER = 1;
   int TYPE_RASTERIZABLE = 2;

   String getLocation();

   String getFormat();

   int getWidth();

   int getHeight();

   double getXResolution();

   double getYResolution();

   int getType();

   Object getClientData();
}
