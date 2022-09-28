package com.xmlmind.fo.graphic;

import com.xmlmind.fo.graphic.emf.EMFGraphicFactory;
import com.xmlmind.fo.graphic.emf.WMFGraphicFactory;

final class AutoRegisterFactories {
   public static void registerAll() {
      GraphicFactories.register(new GraphicFactoryImpl());
      GraphicFactories.register(new WMFGraphicFactory());
      GraphicFactories.register(new EMFGraphicFactory());
      String[] var0 = new String[]{"com.xmlmind.xfc_ext.SVGGraphicFactory", "com.xmlmind.xfc_ext.MathMLGraphicFactory"};

      for(int var1 = 0; var1 < var0.length; ++var1) {
         try {
            GraphicFactory var2 = (GraphicFactory)Class.forName(var0[var1]).newInstance();
            GraphicFactories.register(var2);
         } catch (Throwable var3) {
         }
      }

   }
}
