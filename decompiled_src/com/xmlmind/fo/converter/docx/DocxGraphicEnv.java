package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.graphic.GraphicEnv;
import java.io.File;

public final class DocxGraphicEnv implements GraphicEnv {
   private final GraphicEnv env;
   public final Images images;
   public final Relationships relationships;

   public DocxGraphicEnv(GraphicEnv var1, Images var2, Relationships var3) {
      this.env = var1;
      this.images = var2;
      this.relationships = var3;
   }

   public File createTempFile(String var1) throws Exception {
      return this.env.createTempFile(var1);
   }

   public void reportWarning(String var1) {
      this.env.reportWarning(var1);
   }
}
