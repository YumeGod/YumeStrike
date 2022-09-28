package org.apache.batik.swing.svg;

import org.apache.batik.bridge.ExternalResourceSecurity;
import org.apache.batik.bridge.RelaxedExternalResourceSecurity;
import org.apache.batik.bridge.RelaxedScriptSecurity;
import org.apache.batik.bridge.ScriptSecurity;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;

public class SVGUserAgentAdapter implements SVGUserAgent {
   public void displayError(String var1) {
      System.err.println(var1);
   }

   public void displayError(Exception var1) {
      var1.printStackTrace();
   }

   public void displayMessage(String var1) {
      System.out.println(var1);
   }

   public void showAlert(String var1) {
      System.err.println(var1);
   }

   public String showPrompt(String var1) {
      return "";
   }

   public String showPrompt(String var1, String var2) {
      return var2;
   }

   public boolean showConfirm(String var1) {
      return false;
   }

   public float getPixelUnitToMillimeter() {
      return 0.26458332F;
   }

   public float getPixelToMM() {
      return this.getPixelUnitToMillimeter();
   }

   public String getDefaultFontFamily() {
      return "Serif";
   }

   public float getMediumFontSize() {
      return 228.59999F / (72.0F * this.getPixelUnitToMillimeter());
   }

   public float getLighterFontWeight(float var1) {
      int var2 = (int)((var1 + 50.0F) / 100.0F) * 100;
      switch (var2) {
         case 100:
            return 100.0F;
         case 200:
            return 100.0F;
         case 300:
            return 200.0F;
         case 400:
            return 300.0F;
         case 500:
            return 400.0F;
         case 600:
            return 400.0F;
         case 700:
            return 400.0F;
         case 800:
            return 400.0F;
         case 900:
            return 400.0F;
         default:
            throw new IllegalArgumentException("Bad Font Weight: " + var1);
      }
   }

   public float getBolderFontWeight(float var1) {
      int var2 = (int)((var1 + 50.0F) / 100.0F) * 100;
      switch (var2) {
         case 100:
            return 600.0F;
         case 200:
            return 600.0F;
         case 300:
            return 600.0F;
         case 400:
            return 600.0F;
         case 500:
            return 600.0F;
         case 600:
            return 700.0F;
         case 700:
            return 800.0F;
         case 800:
            return 900.0F;
         case 900:
            return 900.0F;
         default:
            throw new IllegalArgumentException("Bad Font Weight: " + var1);
      }
   }

   public String getLanguages() {
      return "en";
   }

   public String getUserStyleSheetURI() {
      return null;
   }

   public String getXMLParserClassName() {
      return XMLResourceDescriptor.getXMLParserClassName();
   }

   public boolean isXMLParserValidating() {
      return false;
   }

   public String getMedia() {
      return "screen";
   }

   public String getAlternateStyleSheet() {
      return null;
   }

   public void openLink(String var1, boolean var2) {
   }

   public boolean supportExtension(String var1) {
      return false;
   }

   public void handleElement(Element var1, Object var2) {
   }

   public ScriptSecurity getScriptSecurity(String var1, ParsedURL var2, ParsedURL var3) {
      return new RelaxedScriptSecurity(var1, var2, var3);
   }

   public void checkLoadScript(String var1, ParsedURL var2, ParsedURL var3) throws SecurityException {
      ScriptSecurity var4 = this.getScriptSecurity(var1, var2, var3);
      if (var4 != null) {
         var4.checkLoadScript();
      }

   }

   public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL var1, ParsedURL var2) {
      return new RelaxedExternalResourceSecurity(var1, var2);
   }

   public void checkLoadExternalResource(ParsedURL var1, ParsedURL var2) throws SecurityException {
      ExternalResourceSecurity var3 = this.getExternalResourceSecurity(var1, var2);
      if (var3 != null) {
         var3.checkLoadExternalResource();
      }

   }
}
