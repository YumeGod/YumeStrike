package org.apache.batik.bridge;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import org.apache.batik.gvt.event.EventDispatcher;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAElement;
import org.w3c.dom.svg.SVGDocument;

public interface UserAgent {
   EventDispatcher getEventDispatcher();

   Dimension2D getViewportSize();

   void displayError(Exception var1);

   void displayMessage(String var1);

   void showAlert(String var1);

   String showPrompt(String var1);

   String showPrompt(String var1, String var2);

   boolean showConfirm(String var1);

   float getPixelUnitToMillimeter();

   float getPixelToMM();

   float getMediumFontSize();

   float getLighterFontWeight(float var1);

   float getBolderFontWeight(float var1);

   String getDefaultFontFamily();

   String getLanguages();

   String getUserStyleSheetURI();

   void openLink(SVGAElement var1);

   void setSVGCursor(Cursor var1);

   void setTextSelection(Mark var1, Mark var2);

   void deselectAll();

   String getXMLParserClassName();

   boolean isXMLParserValidating();

   AffineTransform getTransform();

   void setTransform(AffineTransform var1);

   String getMedia();

   String getAlternateStyleSheet();

   Point getClientAreaLocationOnScreen();

   boolean hasFeature(String var1);

   boolean supportExtension(String var1);

   void registerExtension(BridgeExtension var1);

   void handleElement(Element var1, Object var2);

   ScriptSecurity getScriptSecurity(String var1, ParsedURL var2, ParsedURL var3);

   void checkLoadScript(String var1, ParsedURL var2, ParsedURL var3) throws SecurityException;

   ExternalResourceSecurity getExternalResourceSecurity(ParsedURL var1, ParsedURL var2);

   void checkLoadExternalResource(ParsedURL var1, ParsedURL var2) throws SecurityException;

   SVGDocument getBrokenLinkDocument(Element var1, String var2, String var3);
}
