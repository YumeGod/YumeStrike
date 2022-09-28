package org.apache.batik.apps.svgbrowser;

import org.apache.batik.swing.gvt.Overlay;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface DOMViewerController {
   void performUpdate(Runnable var1);

   ElementOverlayManager createSelectionManager();

   void removeSelectionOverlay(Overlay var1);

   Document getDocument();

   void selectNode(Node var1);

   boolean canEdit();
}
