package org.apache.batik.svggen.font.table;

public interface GlyphDescription {
   int getEndPtOfContours(int var1);

   byte getFlags(int var1);

   short getXCoordinate(int var1);

   short getYCoordinate(int var1);

   short getXMaximum();

   short getXMinimum();

   short getYMaximum();

   short getYMinimum();

   boolean isComposite();

   int getPointCount();

   int getContourCount();
}
