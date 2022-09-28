package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.apache.batik.gvt.text.Mark;

public interface TextPainter {
   void paint(TextNode var1, Graphics2D var2);

   Mark selectAt(double var1, double var3, TextNode var5);

   Mark selectTo(double var1, double var3, Mark var5);

   Mark selectFirst(TextNode var1);

   Mark selectLast(TextNode var1);

   Mark getMark(TextNode var1, int var2, boolean var3);

   int[] getSelected(Mark var1, Mark var2);

   Shape getHighlightShape(Mark var1, Mark var2);

   Shape getOutline(TextNode var1);

   Rectangle2D getBounds2D(TextNode var1);

   Rectangle2D getGeometryBounds(TextNode var1);
}
