package com.mxgraph.canvas;

import com.mxgraph.view.mxCellState;
import java.awt.Point;

public interface mxICanvas {
   void setTranslate(int var1, int var2);

   Point getTranslate();

   void setScale(double var1);

   double getScale();

   Object drawCell(mxCellState var1);

   Object drawLabel(String var1, mxCellState var2, boolean var3);
}
