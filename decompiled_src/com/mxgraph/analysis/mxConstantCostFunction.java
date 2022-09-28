package com.mxgraph.analysis;

import com.mxgraph.view.mxCellState;

public class mxConstantCostFunction implements mxICostFunction {
   protected double cost = 0.0;

   public mxConstantCostFunction(double var1) {
      this.cost = var1;
   }

   public double getCost(mxCellState var1) {
      return this.cost;
   }
}
