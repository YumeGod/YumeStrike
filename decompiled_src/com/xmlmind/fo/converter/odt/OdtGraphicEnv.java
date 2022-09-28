package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.graphic.GraphicEnv;
import java.io.File;
import java.util.Vector;

public final class OdtGraphicEnv implements GraphicEnv {
   private final GraphicEnv env;
   public final Vector fileEntries;
   public final StyleTable styleTable;

   public OdtGraphicEnv(GraphicEnv var1, Vector var2, StyleTable var3) {
      this.env = var1;
      this.fileEntries = var2;
      this.styleTable = var3;
   }

   public File createTempFile(String var1) throws Exception {
      return this.env.createTempFile(var1);
   }

   public void reportWarning(String var1) {
      this.env.reportWarning(var1);
   }
}
