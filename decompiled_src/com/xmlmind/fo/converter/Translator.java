package com.xmlmind.fo.converter;

import com.xmlmind.fo.font.GenericFamilies;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.objects.Flow;
import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.properties.Value;
import java.util.Enumeration;
import java.util.Properties;
import org.xml.sax.Attributes;

public abstract class Translator implements EventHandler, ErrorHandler {
   protected OutputDestination output;
   protected Properties properties = new Properties();
   protected ErrorHandler errorHandler = this;
   protected UriResolver uriResolver;

   protected Translator() {
   }

   public void setOutput(OutputDestination var1) {
      this.output = var1;
   }

   public void setProperty(String var1, String var2) {
      this.properties.put(var1, var2);
   }

   public void setProperties(Properties var1) {
      Enumeration var2 = var1.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         this.setProperty(var3, var1.getProperty(var3));
      }

   }

   public void setErrorHandler(ErrorHandler var1) {
      if (var1 != null) {
         this.errorHandler = var1;
      } else {
         this.errorHandler = this;
      }

   }

   public void setUriResolver(UriResolver var1) {
      this.uriResolver = var1;
   }

   public static String[] listEncodings() {
      return new String[]{"ASCII"};
   }

   public void abort() {
   }

   protected boolean singleSided() {
      String var1 = this.properties.getProperty("singleSidedLayout", "");
      return var1.toLowerCase().equals("true");
   }

   protected boolean prescaleImages() {
      String var1 = this.properties.getProperty("prescaleImages", "");
      return !var1.toLowerCase().equals("false");
   }

   protected int imageResolution() {
      return GraphicLayout.getResolutionProperty(this.properties, "imageResolution", 96);
   }

   protected int imageRendererResolution() {
      return GraphicLayout.getResolutionProperty(this.properties, "imageRendererResolution", this.getImageRendererResolution());
   }

   protected boolean alwaysSaveAsPNG() {
      String var1 = this.properties.getProperty("alwaysSaveAsPNG", "");
      return var1.toLowerCase().equals("true");
   }

   protected GenericFamilies genericFontFamilies(String var1, String var2, String var3) throws Exception {
      GenericFamilies var4 = new GenericFamilies();
      String var5 = this.properties.getProperty("genericFontFamilies", (String)null);
      if (var5 != null) {
         try {
            GenericFamilies.parse(var5, var4);
         } catch (IllegalArgumentException var7) {
            this.errorHandler.warning(var7);
         }
      }

      if (var4.serif == null) {
         if (var1 == null) {
            var1 = "Times New Roman";
         }

         var4.serif = var1;
      }

      if (var4.sansSerif == null) {
         if (var2 == null) {
            var2 = "Arial";
         }

         var4.sansSerif = var2;
      }

      if (var4.monospace == null) {
         if (var3 == null) {
            var3 = "Courier New";
         }

         var4.monospace = var3;
      }

      return var4;
   }

   public int getImageRendererResolution() {
      return 96;
   }

   public String systemFont(int var1) {
      return "10pt serif";
   }

   public double fontSize(int var1, double var2) {
      double var4 = 10.0;
      switch (var1) {
         case 95:
            var4 = 14.0;
            break;
         case 96:
            var4 = 2.0 * (double)Math.round(var2 / 2.0 + 1.0);
            break;
         case 118:
            var4 = 12.0;
            break;
         case 182:
            var4 = 10.0;
            break;
         case 183:
            var4 = 2.0 * (double)Math.round(var2 / 2.0 - 1.0);
            if (var4 < 2.0) {
               var4 = 2.0;
            }
            break;
         case 235:
            var4 = 18.0;
            break;
         case 236:
            var4 = 6.0;
            break;
         case 239:
            var4 = 16.0;
            break;
         case 243:
            var4 = 8.0;
      }

      return var4;
   }

   public double referenceWidth() {
      return 0.0;
   }

   public void startDocument() throws Exception {
   }

   public void endDocument() throws Exception {
   }

   public void characters(String var1, Context var2) throws Exception {
   }

   public void startRoot(Value[] var1, Context var2) throws Exception {
   }

   public void endRoot(Context var1) throws Exception {
   }

   public void startDeclarations(Value[] var1, Context var2) throws Exception {
   }

   public void endDeclarations(Context var1) throws Exception {
   }

   public void startColorProfile(Value[] var1, Context var2) throws Exception {
   }

   public void endColorProfile(Context var1) throws Exception {
   }

   public void startPageSequence(PageSequence var1, Value[] var2, Context var3) throws Exception {
   }

   public void endPageSequence(Context var1) throws Exception {
   }

   public void startLayoutMasterSet(Value[] var1, Context var2) throws Exception {
   }

   public void endLayoutMasterSet(Context var1) throws Exception {
   }

   public void startPageSequenceMaster(Value[] var1, Context var2) throws Exception {
   }

   public void endPageSequenceMaster(Context var1) throws Exception {
   }

   public void startSinglePageMasterReference(Value[] var1, Context var2) throws Exception {
   }

   public void endSinglePageMasterReference(Context var1) throws Exception {
   }

   public void startRepeatablePageMasterReference(Value[] var1, Context var2) throws Exception {
   }

   public void endRepeatablePageMasterReference(Context var1) throws Exception {
   }

   public void startRepeatablePageMasterAlternatives(Value[] var1, Context var2) throws Exception {
   }

   public void endRepeatablePageMasterAlternatives(Context var1) throws Exception {
   }

   public void startConditionalPageMasterReference(Value[] var1, Context var2) throws Exception {
   }

   public void endConditionalPageMasterReference(Context var1) throws Exception {
   }

   public void startSimplePageMaster(Value[] var1, Context var2) throws Exception {
   }

   public void endSimplePageMaster(Context var1) throws Exception {
   }

   public void startRegionBody(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionBody(Context var1) throws Exception {
   }

   public void startRegionBefore(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionBefore(Context var1) throws Exception {
   }

   public void startRegionAfter(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionAfter(Context var1) throws Exception {
   }

   public void startRegionStart(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionStart(Context var1) throws Exception {
   }

   public void startRegionEnd(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionEnd(Context var1) throws Exception {
   }

   public void startFlow(Flow var1, Value[] var2, Context var3) throws Exception {
   }

   public void endFlow(Context var1) throws Exception {
   }

   public void startStaticContent(Flow var1, Value[] var2, Context var3) throws Exception {
   }

   public void endStaticContent(Context var1) throws Exception {
   }

   public void startTitle(Value[] var1, Context var2) throws Exception {
   }

   public void endTitle(Context var1) throws Exception {
   }

   public void startBlock(Value[] var1, Context var2) throws Exception {
   }

   public void endBlock(Context var1) throws Exception {
   }

   public void startBlockContainer(Value[] var1, Context var2) throws Exception {
   }

   public void endBlockContainer(Context var1) throws Exception {
   }

   public void startBidiOverride(Value[] var1, Context var2) throws Exception {
   }

   public void endBidiOverride(Context var1) throws Exception {
   }

   public void startCharacter(Value[] var1, Context var2) throws Exception {
   }

   public void endCharacter(Context var1) throws Exception {
   }

   public void startInitialPropertySet(Value[] var1, Context var2) throws Exception {
   }

   public void endInitialPropertySet(Context var1) throws Exception {
   }

   public void startExternalGraphic(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception {
   }

   public void endExternalGraphic(Context var1) throws Exception {
   }

   public void startInstreamForeignObject(Value[] var1, Context var2) throws Exception {
   }

   public void endInstreamForeignObject(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception {
   }

   public void startInline(Value[] var1, Context var2) throws Exception {
   }

   public void endInline(Context var1) throws Exception {
   }

   public void startInlineContainer(Value[] var1, Context var2) throws Exception {
   }

   public void endInlineContainer(Context var1) throws Exception {
   }

   public void startLeader(Value[] var1, Context var2) throws Exception {
   }

   public void endLeader(Context var1) throws Exception {
   }

   public void startPageNumber(Value[] var1, Context var2) throws Exception {
   }

   public void endPageNumber(Context var1) throws Exception {
   }

   public void startPageNumberCitation(Value[] var1, Context var2) throws Exception {
   }

   public void endPageNumberCitation(Context var1) throws Exception {
   }

   public void startTableAndCaption(Value[] var1, Context var2) throws Exception {
   }

   public void endTableAndCaption(Context var1) throws Exception {
   }

   public void startTable(Value[] var1, Context var2) throws Exception {
   }

   public void endTable(Context var1) throws Exception {
   }

   public void startTableColumn(Value[] var1, Context var2) throws Exception {
   }

   public void endTableColumn(Context var1) throws Exception {
   }

   public void startTableCaption(Value[] var1, Context var2) throws Exception {
   }

   public void endTableCaption(Context var1) throws Exception {
   }

   public void startTableHeader(Value[] var1, Context var2) throws Exception {
   }

   public void endTableHeader(Context var1) throws Exception {
   }

   public void startTableFooter(Value[] var1, Context var2) throws Exception {
   }

   public void endTableFooter(Context var1) throws Exception {
   }

   public void startTableBody(Value[] var1, Context var2) throws Exception {
   }

   public void endTableBody(Context var1) throws Exception {
   }

   public void startTableRow(Value[] var1, Context var2) throws Exception {
   }

   public void endTableRow(Context var1) throws Exception {
   }

   public void startTableCell(Value[] var1, Context var2) throws Exception {
   }

   public void endTableCell(Context var1) throws Exception {
   }

   public void startListBlock(Value[] var1, Context var2) throws Exception {
   }

   public void endListBlock(Context var1) throws Exception {
   }

   public void startListItem(Value[] var1, Context var2) throws Exception {
   }

   public void endListItem(Context var1) throws Exception {
   }

   public void startListItemBody(Value[] var1, Context var2) throws Exception {
   }

   public void endListItemBody(Context var1) throws Exception {
   }

   public void startListItemLabel(Value[] var1, Context var2) throws Exception {
   }

   public void endListItemLabel(Context var1) throws Exception {
   }

   public void startBasicLink(Value[] var1, Context var2) throws Exception {
   }

   public void endBasicLink(Context var1) throws Exception {
   }

   public void startMultiSwitch(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiSwitch(Context var1) throws Exception {
   }

   public void startMultiCase(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiCase(Context var1) throws Exception {
   }

   public void startMultiToggle(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiToggle(Context var1) throws Exception {
   }

   public void startMultiProperties(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiProperties(Context var1) throws Exception {
   }

   public void startMultiPropertySet(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiPropertySet(Context var1) throws Exception {
   }

   public void startFloat(Value[] var1, Context var2) throws Exception {
   }

   public void endFloat(Context var1) throws Exception {
   }

   public void startFootnote(Value[] var1, Context var2) throws Exception {
   }

   public void endFootnote(Context var1) throws Exception {
   }

   public void startFootnoteBody(Value[] var1, Context var2) throws Exception {
   }

   public void endFootnoteBody(Context var1) throws Exception {
   }

   public void startWrapper(Value[] var1, Context var2) throws Exception {
   }

   public void endWrapper(Context var1) throws Exception {
   }

   public void startMarker(Value[] var1, Context var2) throws Exception {
   }

   public void endMarker(Context var1) throws Exception {
   }

   public void startRetrieveMarker(Value[] var1, Context var2) throws Exception {
   }

   public void endRetrieveMarker(Context var1) throws Exception {
   }

   public boolean supportsExtension(String var1) {
      return false;
   }

   public void startForeignObject(String var1, String var2, Attributes var3, Context var4) throws Exception {
   }

   public void endForeignObject(String var1, String var2, Context var3) throws Exception {
   }

   protected String resolve(String var1) throws Exception {
      if (this.uriResolver != null) {
         String var2 = null;

         try {
            var2 = this.uriResolver.resolve(var1);
         } catch (Exception var4) {
            this.warning("URI resolver error: " + var4.getMessage());
         }

         return var2;
      } else {
         return var1;
      }
   }

   protected void warning(String var1) throws Exception {
      this.errorHandler.warning(new Exception(var1));
   }

   public void error(Exception var1) throws Exception {
      throw var1;
   }

   public void warning(Exception var1) throws Exception {
      String var2 = var1.getMessage();
      System.err.println("warning: " + var2);
   }

   public interface UriResolver {
      String resolve(String var1) throws Exception;
   }
}
