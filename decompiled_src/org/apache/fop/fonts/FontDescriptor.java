package org.apache.fop.fonts;

public interface FontDescriptor extends FontMetrics {
   int getAscender();

   int getCapHeight();

   int getDescender();

   int getFlags();

   boolean isSymbolicFont();

   int[] getFontBBox();

   int getItalicAngle();

   int getStemV();

   boolean isEmbeddable();
}
