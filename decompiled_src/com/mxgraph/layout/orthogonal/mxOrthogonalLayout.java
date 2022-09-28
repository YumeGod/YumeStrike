package com.mxgraph.layout.orthogonal;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.orthogonal.model.mxOrthogonalModel;
import com.mxgraph.view.mxGraph;

public class mxOrthogonalLayout extends mxGraphLayout {
   protected mxOrthogonalModel orthModel;
   protected boolean routeToGrid = false;

   public mxOrthogonalLayout(mxGraph var1) {
      super(var1);
      this.orthModel = new mxOrthogonalModel(var1);
   }

   public void execute(Object var1) {
   }
}
