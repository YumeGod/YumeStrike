package com.xmlmind.fo.graphic;

import com.xmlmind.fo.util.URLUtil;

public class GraphicImpl implements Graphic {
   public final String location;
   public final String format;
   public final int width;
   public final int height;
   public final double xResolution;
   public final double yResolution;
   public final int type;
   public final Object clientData;

   public GraphicImpl(String var1, String var2, int var3, int var4, double var5, double var7, Object var9) {
      this(var1, var2, var3, var4, var5, var7, 1, var9);
   }

   public GraphicImpl(String var1, String var2, int var3, int var4, double var5, double var7, int var9, Object var10) {
      this.location = var1;
      this.format = URLUtil.normalizeMIMEType(var2);
      this.width = var3;
      this.height = var4;
      this.xResolution = var5;
      this.yResolution = var7;
      this.type = var9;
      this.clientData = var10;
   }

   public String getLocation() {
      return this.location;
   }

   public String getFormat() {
      return this.format;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public double getXResolution() {
      return this.xResolution;
   }

   public double getYResolution() {
      return this.yResolution;
   }

   public int getType() {
      return this.type;
   }

   public Object getClientData() {
      return this.clientData;
   }
}
