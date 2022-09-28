package com.xmlmind.fo.graphic;

public interface GraphicFactory {
   String[] getInputFormats();

   String[] getOutputFormats();

   Graphic createGraphic(String var1, String var2, Object var3, GraphicEnv var4) throws Exception;

   Graphic convertGraphic(Graphic var1, String var2, double var3, double var5, Object var7, GraphicEnv var8) throws Exception;
}
