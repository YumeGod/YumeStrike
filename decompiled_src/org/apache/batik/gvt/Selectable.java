package org.apache.batik.gvt;

import java.awt.Shape;

public interface Selectable {
   boolean selectAt(double var1, double var3);

   boolean selectTo(double var1, double var3);

   boolean selectAll(double var1, double var3);

   Object getSelection();

   Shape getHighlightShape();
}
