package org.apache.fop.render.afp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.fop.afp.AFPDitheredRectanglePainter;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPRectanglePainter;
import org.apache.fop.afp.AFPResourceLevelDefaults;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.afp.AFPUnitConverter;
import org.apache.fop.afp.AbstractAFPPainter;
import org.apache.fop.afp.DataStream;
import org.apache.fop.afp.fonts.AFPFontCollection;
import org.apache.fop.afp.fonts.AFPPageFonts;
import org.apache.fop.afp.util.DefaultFOPResourceAccessor;
import org.apache.fop.afp.util.ResourceAccessor;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontEventAdapter;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.render.afp.extensions.AFPIncludeFormMap;
import org.apache.fop.render.afp.extensions.AFPInvokeMediumMap;
import org.apache.fop.render.afp.extensions.AFPPageOverlay;
import org.apache.fop.render.afp.extensions.AFPPageSetup;
import org.apache.fop.render.intermediate.AbstractBinaryWritingIFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFPainter;

public class AFPDocumentHandler extends AbstractBinaryWritingIFDocumentHandler implements AFPCustomizable {
   private AFPResourceManager resourceManager;
   private final AFPPaintingState paintingState;
   private final AFPUnitConverter unitConv;
   private DataStream dataStream;
   private Map pageSegmentMap = new HashMap();
   private String lastMediumMap;
   private static final int LOC_ELSEWHERE = 0;
   private static final int LOC_FOLLOWING_PAGE_SEQUENCE = 1;
   private static final int LOC_IN_PAGE_HEADER = 2;
   private int location = 0;
   private AFPShadingMode shadingMode;

   public AFPDocumentHandler() {
      this.shadingMode = AFPShadingMode.COLOR;
      this.resourceManager = new AFPResourceManager();
      this.paintingState = new AFPPaintingState();
      this.unitConv = this.paintingState.getUnitConverter();
   }

   public boolean supportsPagesOutOfOrder() {
      return false;
   }

   public String getMimeType() {
      return "application/x-afp";
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return new AFPRendererConfigurator(this.getUserAgent());
   }

   public void setDefaultFontInfo(FontInfo fontInfo) {
      FontManager fontManager = this.getUserAgent().getFactory().getFontManager();
      FontCollection[] fontCollections = new FontCollection[]{new AFPFontCollection(this.getUserAgent().getEventBroadcaster(), (List)null)};
      FontInfo fi = fontInfo != null ? fontInfo : new FontInfo();
      fi.setEventListener(new FontEventAdapter(this.getUserAgent().getEventBroadcaster()));
      fontManager.setup(fi, fontCollections);
      this.setFontInfo(fi);
   }

   AFPPaintingState getPaintingState() {
      return this.paintingState;
   }

   DataStream getDataStream() {
      return this.dataStream;
   }

   AFPResourceManager getResourceManager() {
      return this.resourceManager;
   }

   AbstractAFPPainter createRectanglePainter() {
      return (AbstractAFPPainter)(AFPShadingMode.DITHERED.equals(this.shadingMode) ? new AFPDitheredRectanglePainter(this.getPaintingState(), this.getDataStream(), this.getResourceManager()) : new AFPRectanglePainter(this.getPaintingState(), this.getDataStream()));
   }

   public void startDocument() throws IFException {
      super.startDocument();

      try {
         this.paintingState.setColor(Color.WHITE);
         this.dataStream = this.resourceManager.createDataStream(this.paintingState, this.outputStream);
         this.dataStream.startDocument();
      } catch (IOException var2) {
         throw new IFException("I/O error in startDocument()", var2);
      }
   }

   public void endDocumentHeader() throws IFException {
   }

   public void endDocument() throws IFException {
      try {
         this.dataStream.endDocument();
         this.dataStream = null;
         this.resourceManager.writeToStream();
         this.resourceManager = null;
      } catch (IOException var2) {
         throw new IFException("I/O error in endDocument()", var2);
      }

      super.endDocument();
   }

   public void startPageSequence(String id) throws IFException {
      try {
         this.dataStream.startPageGroup();
      } catch (IOException var3) {
         throw new IFException("I/O error in startPageSequence()", var3);
      }

      this.location = 1;
   }

   public void endPageSequence() throws IFException {
   }

   private AffineTransform getBaseTransform() {
      AffineTransform baseTransform = new AffineTransform();
      double scale = (double)this.unitConv.mpt2units(1.0F);
      baseTransform.scale(scale, scale);
      return baseTransform;
   }

   public void startPage(int index, String name, String pageMasterName, Dimension size) throws IFException {
      this.location = 0;
      this.paintingState.clear();
      this.pageSegmentMap.clear();
      AffineTransform baseTransform = this.getBaseTransform();
      this.paintingState.concatenate(baseTransform);
      int pageWidth = Math.round(this.unitConv.mpt2units((float)size.width));
      this.paintingState.setPageWidth(pageWidth);
      int pageHeight = Math.round(this.unitConv.mpt2units((float)size.height));
      this.paintingState.setPageHeight(pageHeight);
      int pageRotation = this.paintingState.getPageRotation();
      int resolution = this.paintingState.getResolution();
      this.dataStream.startPage(pageWidth, pageHeight, pageRotation, resolution, resolution);
   }

   public void startPageHeader() throws IFException {
      super.startPageHeader();
      this.location = 2;
   }

   public void endPageHeader() throws IFException {
      this.location = 0;
      super.endPageHeader();
   }

   public IFPainter startPageContent() throws IFException {
      return new AFPPainter(this);
   }

   public void endPageContent() throws IFException {
   }

   public void endPage() throws IFException {
      try {
         AFPPageFonts pageFonts = this.paintingState.getPageFonts();
         if (pageFonts != null && !pageFonts.isEmpty()) {
            this.dataStream.addFontsToCurrentPage(pageFonts);
         }

         this.dataStream.endPage();
      } catch (IOException var2) {
         throw new IFException("I/O error in endPage()", var2);
      }
   }

   public void handleExtensionObject(Object extension) throws IFException {
      String mediumMap;
      if (extension instanceof AFPPageSetup) {
         AFPPageSetup aps = (AFPPageSetup)extension;
         mediumMap = aps.getElementName();
         String content;
         String source;
         if ("tag-logical-element".equals(mediumMap)) {
            if (this.location != 2 && this.location != 1) {
               throw new IFException("TLE extension must be in the page header or between page-sequence and the first page: " + aps, (Exception)null);
            }

            content = aps.getName();
            source = aps.getValue();
            this.dataStream.createTagLogicalElement(content, source);
         } else {
            if (this.location != 2) {
               throw new IFException("AFP page setup extension encountered outside the page header: " + aps, (Exception)null);
            }

            if ("include-page-segment".equals(mediumMap)) {
               content = aps.getName();
               source = aps.getValue();
               this.pageSegmentMap.put(source, content);
            } else if ("no-operation".equals(mediumMap)) {
               content = aps.getContent();
               if (content != null) {
                  this.dataStream.createNoOperation(content);
               }
            }
         }
      } else if (extension instanceof AFPPageOverlay) {
         AFPPageOverlay ipo = (AFPPageOverlay)extension;
         if (this.location != 2) {
            throw new IFException("AFP page overlay extension encountered outside the page header: " + ipo, (Exception)null);
         }

         mediumMap = ipo.getName();
         if (mediumMap != null) {
            this.dataStream.createIncludePageOverlay(mediumMap, ipo.getX(), ipo.getY());
         }
      } else if (extension instanceof AFPInvokeMediumMap) {
         if (this.location != 1 && this.location != 2) {
            throw new IFException("AFP IMM extension must be between page-sequence and the first page or child of page-header: " + extension, (Exception)null);
         }

         AFPInvokeMediumMap imm = (AFPInvokeMediumMap)extension;
         mediumMap = imm.getName();
         if (mediumMap != null && !mediumMap.equals(this.lastMediumMap)) {
            this.dataStream.createInvokeMediumMap(mediumMap);
            this.lastMediumMap = mediumMap;
         }
      } else if (extension instanceof AFPIncludeFormMap) {
         AFPIncludeFormMap formMap = (AFPIncludeFormMap)extension;
         ResourceAccessor accessor = new DefaultFOPResourceAccessor(this.getUserAgent(), (String)null, (URI)null);

         try {
            this.getResourceManager().createIncludedResource(formMap.getName(), formMap.getSrc(), accessor, (byte)-2);
         } catch (IOException var6) {
            throw new IFException("I/O error while embedding form map resource: " + formMap.getName(), var6);
         }
      }

   }

   public void setBitsPerPixel(int bitsPerPixel) {
      this.paintingState.setBitsPerPixel(bitsPerPixel);
   }

   public void setColorImages(boolean colorImages) {
      this.paintingState.setColorImages(colorImages);
   }

   public void setNativeImagesSupported(boolean nativeImages) {
      this.paintingState.setNativeImagesSupported(nativeImages);
   }

   public void setCMYKImagesSupported(boolean value) {
      this.paintingState.setCMYKImagesSupported(value);
   }

   public void setDitheringQuality(float quality) {
      this.paintingState.setDitheringQuality(quality);
   }

   public void setShadingMode(AFPShadingMode shadingMode) {
      this.shadingMode = shadingMode;
   }

   public void setResolution(int resolution) {
      this.paintingState.setResolution(resolution);
   }

   public int getResolution() {
      return this.paintingState.getResolution();
   }

   public void setDefaultResourceGroupFilePath(String filePath) {
      this.resourceManager.setDefaultResourceGroupFilePath(filePath);
   }

   public void setResourceLevelDefaults(AFPResourceLevelDefaults defaults) {
      this.resourceManager.setResourceLevelDefaults(defaults);
   }

   String getPageSegmentNameFor(String uri) {
      return (String)this.pageSegmentMap.get(uri);
   }
}
