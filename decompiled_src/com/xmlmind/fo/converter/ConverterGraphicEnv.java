package com.xmlmind.fo.converter;

import com.xmlmind.fo.graphic.GraphicEnvImpl;

final class ConverterGraphicEnv extends GraphicEnvImpl {
   public final ErrorHandler errorHandler;

   public ConverterGraphicEnv(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public void reportWarning(String var1) {
      try {
         this.errorHandler.warning(new Exception(var1));
      } catch (Exception var3) {
      }

   }
}
