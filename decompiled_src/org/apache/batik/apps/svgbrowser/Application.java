package org.apache.batik.apps.svgbrowser;

import javax.swing.Action;

public interface Application {
   JSVGViewerFrame createAndShowJSVGViewerFrame();

   void closeJSVGViewerFrame(JSVGViewerFrame var1);

   Action createExitAction(JSVGViewerFrame var1);

   void openLink(String var1);

   String getXMLParserClassName();

   boolean isXMLParserValidating();

   void showPreferenceDialog(JSVGViewerFrame var1);

   String getLanguages();

   String getUserStyleSheetURI();

   String getDefaultFontFamily();

   String getMedia();

   boolean isSelectionOverlayXORMode();

   boolean canLoadScriptType(String var1);

   int getAllowedScriptOrigin();

   int getAllowedExternalResourceOrigin();

   void addVisitedURI(String var1);

   String[] getVisitedURIs();

   String getUISpecialization();
}
