package org.apache.batik.swing.svg;

import org.apache.batik.bridge.ExternalResourceSecurity;
import org.apache.batik.bridge.ScriptSecurity;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;

public interface SVGUserAgent {
   void displayError(String var1);

   void displayError(Exception var1);

   void displayMessage(String var1);

   void showAlert(String var1);

   String showPrompt(String var1);

   String showPrompt(String var1, String var2);

   boolean showConfirm(String var1);

   float getPixelUnitToMillimeter();

   float getPixelToMM();

   String getDefaultFontFamily();

   float getMediumFontSize();

   float getLighterFontWeight(float var1);

   float getBolderFontWeight(float var1);

   String getLanguages();

   String getUserStyleSheetURI();

   String getXMLParserClassName();

   boolean isXMLParserValidating();

   String getMedia();

   String getAlternateStyleSheet();

   void openLink(String var1, boolean var2);

   boolean supportExtension(String var1);

   void handleElement(Element var1, Object var2);

   ScriptSecurity getScriptSecurity(String var1, ParsedURL var2, ParsedURL var3);

   void checkLoadScript(String var1, ParsedURL var2, ParsedURL var3) throws SecurityException;

   ExternalResourceSecurity getExternalResourceSecurity(ParsedURL var1, ParsedURL var2);

   void checkLoadExternalResource(ParsedURL var1, ParsedURL var2) throws SecurityException;
}
