package org.apache.batik.bridge;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.batik.gvt.event.EventDispatcher;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGFeatureStrings;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAElement;
import org.w3c.dom.svg.SVGDocument;

public class UserAgentAdapter implements UserAgent {
   protected Set FEATURES = new HashSet();
   protected Set extensions = new HashSet();
   protected BridgeContext ctx;

   public void setBridgeContext(BridgeContext var1) {
      this.ctx = var1;
   }

   public void addStdFeatures() {
      SVGFeatureStrings.addSupportedFeatureStrings(this.FEATURES);
   }

   public Dimension2D getViewportSize() {
      return new Dimension(1, 1);
   }

   public void displayMessage(String var1) {
   }

   public void displayError(String var1) {
      this.displayMessage(var1);
   }

   public void displayError(Exception var1) {
      this.displayError(var1.getMessage());
   }

   public void showAlert(String var1) {
   }

   public String showPrompt(String var1) {
      return null;
   }

   public String showPrompt(String var1, String var2) {
      return null;
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
      return "Arial, Helvetica, sans-serif";
   }

   public float getMediumFontSize() {
      return 228.59999F / (72.0F * this.getPixelUnitToMillimeter());
   }

   public float getLighterFontWeight(float var1) {
      return getStandardLighterFontWeight(var1);
   }

   public float getBolderFontWeight(float var1) {
      return getStandardBolderFontWeight(var1);
   }

   public String getLanguages() {
      return "en";
   }

   public String getMedia() {
      return "all";
   }

   public String getAlternateStyleSheet() {
      return null;
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

   public EventDispatcher getEventDispatcher() {
      return null;
   }

   public void openLink(SVGAElement var1) {
   }

   public void setSVGCursor(Cursor var1) {
   }

   public void setTextSelection(Mark var1, Mark var2) {
   }

   public void deselectAll() {
   }

   public void runThread(Thread var1) {
   }

   public AffineTransform getTransform() {
      return null;
   }

   public void setTransform(AffineTransform var1) {
   }

   public Point getClientAreaLocationOnScreen() {
      return new Point();
   }

   public boolean hasFeature(String var1) {
      return this.FEATURES.contains(var1);
   }

   public boolean supportExtension(String var1) {
      return this.extensions.contains(var1);
   }

   public void registerExtension(BridgeExtension var1) {
      Iterator var2 = var1.getImplementedExtensions();

      while(var2.hasNext()) {
         this.extensions.add(var2.next());
      }

   }

   public void handleElement(Element var1, Object var2) {
   }

   public ScriptSecurity getScriptSecurity(String var1, ParsedURL var2, ParsedURL var3) {
      return new DefaultScriptSecurity(var1, var2, var3);
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

   public static float getStandardLighterFontWeight(float var0) {
      int var1 = (int)((var0 + 50.0F) / 100.0F) * 100;
      switch (var1) {
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
            throw new IllegalArgumentException("Bad Font Weight: " + var0);
      }
   }

   public static float getStandardBolderFontWeight(float var0) {
      int var1 = (int)((var0 + 50.0F) / 100.0F) * 100;
      switch (var1) {
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
            throw new IllegalArgumentException("Bad Font Weight: " + var0);
      }
   }

   public SVGDocument getBrokenLinkDocument(Element var1, String var2, String var3) {
      throw new BridgeException(this.ctx, var1, "uri.image.broken", new Object[]{var2, var3});
   }
}
