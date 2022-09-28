package org.apache.batik.swing.svg;

public interface GVTTreeBuilderListener {
   void gvtBuildStarted(GVTTreeBuilderEvent var1);

   void gvtBuildCompleted(GVTTreeBuilderEvent var1);

   void gvtBuildCancelled(GVTTreeBuilderEvent var1);

   void gvtBuildFailed(GVTTreeBuilderEvent var1);
}
