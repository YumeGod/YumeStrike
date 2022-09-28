package org.apache.batik.swing.gvt;

public interface GVTTreeRendererListener {
   void gvtRenderingPrepare(GVTTreeRendererEvent var1);

   void gvtRenderingStarted(GVTTreeRendererEvent var1);

   void gvtRenderingCompleted(GVTTreeRendererEvent var1);

   void gvtRenderingCancelled(GVTTreeRendererEvent var1);

   void gvtRenderingFailed(GVTTreeRendererEvent var1);
}
