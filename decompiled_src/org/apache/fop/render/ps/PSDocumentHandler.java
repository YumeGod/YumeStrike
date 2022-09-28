package org.apache.fop.render.ps;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Source;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.render.intermediate.AbstractBinaryWritingIFDocumentHandler;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFPainter;
import org.apache.fop.render.ps.extensions.PSCommentAfter;
import org.apache.fop.render.ps.extensions.PSCommentBefore;
import org.apache.fop.render.ps.extensions.PSSetPageDevice;
import org.apache.fop.render.ps.extensions.PSSetupCode;
import org.apache.xmlgraphics.java2d.Dimension2DDouble;
import org.apache.xmlgraphics.ps.DSCConstants;
import org.apache.xmlgraphics.ps.PSDictionary;
import org.apache.xmlgraphics.ps.PSDictionaryFormatException;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSPageDeviceDictionary;
import org.apache.xmlgraphics.ps.PSProcSets;
import org.apache.xmlgraphics.ps.PSResource;
import org.apache.xmlgraphics.ps.dsc.DSCException;
import org.apache.xmlgraphics.ps.dsc.ResourceTracker;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentBoundingBox;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentHiResBoundingBox;

public class PSDocumentHandler extends AbstractBinaryWritingIFDocumentHandler {
   private static Log log;
   protected PSRenderingUtil psUtil;
   protected PSGenerator gen;
   private File tempFile;
   private int currentPageNumber = 0;
   private PageDefinition currentPageDefinition;
   private Rectangle2D documentBoundingBox;
   private List setupCodeList;
   private FontResourceCache fontResources;
   private Map formResources;
   private PSPageDeviceDictionary pageDeviceDictionary;
   private Collection[] comments = new Collection[3];
   private static final int COMMENT_DOCUMENT_HEADER = 0;
   private static final int COMMENT_DOCUMENT_TRAILER = 1;
   private static final int COMMENT_PAGE_TRAILER = 2;

   public boolean supportsPagesOutOfOrder() {
      return false;
   }

   public String getMimeType() {
      return "application/postscript";
   }

   public void setContext(IFContext context) {
      super.setContext(context);
      this.psUtil = new PSRenderingUtil(context.getUserAgent());
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return new PSRendererConfigurator(this.getUserAgent());
   }

   PSRenderingUtil getPSUtil() {
      return this.psUtil;
   }

   public void startDocument() throws IFException {
      super.startDocument();
      this.fontResources = new FontResourceCache(this.getFontInfo());

      try {
         Object out;
         if (this.psUtil.isOptimizeResources()) {
            this.tempFile = File.createTempFile("fop", (String)null);
            OutputStream out = new FileOutputStream(this.tempFile);
            out = new BufferedOutputStream(out);
         } else {
            out = this.outputStream;
         }

         this.gen = new PSGenerator((OutputStream)out) {
            public Source resolveURI(String uri) {
               return PSDocumentHandler.this.getUserAgent().resolveURI(uri);
            }
         };
         this.gen.setPSLevel(this.psUtil.getLanguageLevel());
         this.currentPageNumber = 0;
         this.documentBoundingBox = new Rectangle2D.Double();
         this.pageDeviceDictionary = new PSPageDeviceDictionary();
         this.pageDeviceDictionary.setFlushOnRetrieval(!this.psUtil.isDSCComplianceEnabled());
         this.pageDeviceDictionary.put("/ImagingBBox", "null");
      } catch (IOException var2) {
         throw new IFException("I/O error in startDocument()", var2);
      }
   }

   private void writeHeader() throws IOException {
      this.gen.writeln("%!PS-Adobe-3.0");
      this.gen.writeDSCComment("Creator", (Object[])(new String[]{this.getUserAgent().getProducer()}));
      this.gen.writeDSCComment("CreationDate", new Object[]{new Date()});
      this.gen.writeDSCComment("LanguageLevel", (Object)(new Integer(this.gen.getPSLevel())));
      this.gen.writeDSCComment("Pages", new Object[]{DSCConstants.ATEND});
      this.gen.writeDSCComment("BoundingBox", DSCConstants.ATEND);
      this.gen.writeDSCComment("HiResBoundingBox", DSCConstants.ATEND);
      this.gen.writeDSCComment("DocumentSuppliedResources", new Object[]{DSCConstants.ATEND});
      this.writeExtensions(0);
      this.gen.writeDSCComment("EndComments");
      this.gen.writeDSCComment("BeginDefaults");
      this.gen.writeDSCComment("EndDefaults");
      this.gen.writeDSCComment("BeginProlog");
      PSProcSets.writeStdProcSet(this.gen);
      PSProcSets.writeEPSProcSet(this.gen);
      FOPProcSet.INSTANCE.writeTo(this.gen);
      this.gen.writeDSCComment("EndProlog");
      this.gen.writeDSCComment("BeginSetup");
      PSRenderingUtil.writeSetupCodeList(this.gen, this.setupCodeList, "SetupCode");
      if (!this.psUtil.isOptimizeResources()) {
         this.fontResources.addAll(PSFontUtils.writeFontDict(this.gen, this.fontInfo));
      } else {
         this.gen.commentln("%FOPFontSetup");
      }

      this.gen.writeDSCComment("EndSetup");
   }

   public void endDocumentHeader() throws IFException {
      try {
         this.writeHeader();
      } catch (IOException var2) {
         throw new IFException("I/O error writing the PostScript header", var2);
      }
   }

   public void endDocument() throws IFException {
      try {
         this.gen.writeDSCComment("Trailer");
         this.writeExtensions(1);
         this.gen.writeDSCComment("Pages", (Object)(new Integer(this.currentPageNumber)));
         (new DSCCommentBoundingBox(this.documentBoundingBox)).generate(this.gen);
         (new DSCCommentHiResBoundingBox(this.documentBoundingBox)).generate(this.gen);
         this.gen.getResourceTracker().writeResources(false, this.gen);
         this.gen.writeDSCComment("EOF");
         this.gen.flush();
         log.debug("Rendering to PostScript complete.");
         if (this.psUtil.isOptimizeResources()) {
            IOUtils.closeQuietly(this.gen.getOutputStream());
            this.rewritePostScriptFile();
         }

         if (this.pageDeviceDictionary != null) {
            this.pageDeviceDictionary.clear();
         }
      } catch (IOException var2) {
         throw new IFException("I/O error in endDocument()", var2);
      }

      super.endDocument();
   }

   private void rewritePostScriptFile() throws IOException {
      log.debug("Processing PostScript resources...");
      long startTime = System.currentTimeMillis();
      ResourceTracker resTracker = this.gen.getResourceTracker();
      InputStream in = new FileInputStream(this.tempFile);
      InputStream in = new BufferedInputStream(in);

      try {
         ResourceHandler handler = new ResourceHandler(this.getUserAgent(), this.fontInfo, resTracker, this.formResources);
         handler.process(in, this.outputStream, this.currentPageNumber, this.documentBoundingBox);
         this.outputStream.flush();
      } catch (DSCException var10) {
         throw new RuntimeException(var10.getMessage());
      } finally {
         IOUtils.closeQuietly((InputStream)in);
         if (!this.tempFile.delete()) {
            this.tempFile.deleteOnExit();
            log.warn("Could not delete temporary file: " + this.tempFile);
         }

      }

      if (log.isDebugEnabled()) {
         long duration = System.currentTimeMillis() - startTime;
         log.debug("Resource Processing complete in " + duration + " ms.");
      }

   }

   public void startPageSequence(String id) throws IFException {
   }

   public void endPageSequence() throws IFException {
   }

   public void startPage(int index, String name, String pageMasterName, Dimension size) throws IFException {
      try {
         if (this.currentPageNumber == 0) {
         }

         ++this.currentPageNumber;
         this.gen.getResourceTracker().notifyStartNewPage();
         this.gen.getResourceTracker().notifyResourceUsageOnPage(PSProcSets.STD_PROCSET);
         this.gen.writeDSCComment("Page", new Object[]{name, new Integer(this.currentPageNumber)});
         double pageWidth = (double)size.width / 1000.0;
         double pageHeight = (double)size.height / 1000.0;
         boolean rotate = false;
         List pageSizes = new ArrayList();
         if (this.psUtil.isAutoRotateLandscape() && pageHeight < pageWidth) {
            rotate = true;
            pageSizes.add(new Long(Math.round(pageHeight)));
            pageSizes.add(new Long(Math.round(pageWidth)));
         } else {
            pageSizes.add(new Long(Math.round(pageWidth)));
            pageSizes.add(new Long(Math.round(pageHeight)));
         }

         this.pageDeviceDictionary.put("/PageSize", pageSizes);
         this.currentPageDefinition = new PageDefinition(new Dimension2DDouble(pageWidth, pageHeight), rotate);
         Integer zero = new Integer(0);
         Rectangle2D pageBoundingBox = new Rectangle2D.Double();
         if (rotate) {
            pageBoundingBox.setRect(0.0, 0.0, pageHeight, pageWidth);
            this.gen.writeDSCComment("PageBoundingBox", new Object[]{zero, zero, new Long(Math.round(pageHeight)), new Long(Math.round(pageWidth))});
            this.gen.writeDSCComment("PageHiResBoundingBox", new Object[]{zero, zero, new Double(pageHeight), new Double(pageWidth)});
            this.gen.writeDSCComment("PageOrientation", (Object)"Landscape");
         } else {
            pageBoundingBox.setRect(0.0, 0.0, pageWidth, pageHeight);
            this.gen.writeDSCComment("PageBoundingBox", new Object[]{zero, zero, new Long(Math.round(pageWidth)), new Long(Math.round(pageHeight))});
            this.gen.writeDSCComment("PageHiResBoundingBox", new Object[]{zero, zero, new Double(pageWidth), new Double(pageHeight)});
            if (this.psUtil.isAutoRotateLandscape()) {
               this.gen.writeDSCComment("PageOrientation", (Object)"Portrait");
            }
         }

         this.documentBoundingBox.add(pageBoundingBox);
         this.gen.writeDSCComment("PageResources", new Object[]{DSCConstants.ATEND});
         this.gen.commentln("%FOPSimplePageMaster: " + pageMasterName);
      } catch (IOException var13) {
         throw new IFException("I/O error in startPage()", var13);
      }
   }

   public void startPageHeader() throws IFException {
      super.startPageHeader();

      try {
         this.gen.writeDSCComment("BeginPageSetup");
      } catch (IOException var2) {
         throw new IFException("I/O error in startPageHeader()", var2);
      }
   }

   public void endPageHeader() throws IFException {
      try {
         if (!this.pageDeviceDictionary.isEmpty()) {
            String content = this.pageDeviceDictionary.getContent();
            if (this.psUtil.isSafeSetPageDevice()) {
               content = content + " SSPD";
            } else {
               content = content + " setpagedevice";
            }

            PSRenderingUtil.writeEnclosedExtensionAttachment(this.gen, new PSSetPageDevice(content));
         }

         double pageHeight = this.currentPageDefinition.dimensions.getHeight();
         if (this.currentPageDefinition.rotate) {
            this.gen.writeln(this.gen.formatDouble(pageHeight) + " 0 translate");
            this.gen.writeln("90 rotate");
         }

         this.gen.concatMatrix(1.0, 0.0, 0.0, -1.0, 0.0, pageHeight);
         this.gen.writeDSCComment("EndPageSetup");
      } catch (IOException var3) {
         throw new IFException("I/O error in endPageHeader()", var3);
      }

      super.endPageHeader();
   }

   private void writeExtensions(int which) throws IOException {
      Collection extensions = this.comments[which];
      if (extensions != null) {
         PSRenderingUtil.writeEnclosedExtensionAttachments(this.gen, extensions);
         extensions.clear();
      }

   }

   public IFPainter startPageContent() throws IFException {
      return new PSPainter(this);
   }

   public void endPageContent() throws IFException {
      try {
         this.gen.showPage();
      } catch (IOException var2) {
         throw new IFException("I/O error in endPageContent()", var2);
      }
   }

   public void startPageTrailer() throws IFException {
      super.startPageTrailer();

      try {
         this.gen.writeDSCComment("PageTrailer");
      } catch (IOException var2) {
         throw new IFException("I/O error in startPageTrailer()", var2);
      }
   }

   public void endPageTrailer() throws IFException {
      try {
         this.writeExtensions(2);
      } catch (IOException var2) {
         throw new IFException("I/O error in endPageTrailer()", var2);
      }

      super.endPageTrailer();
   }

   public void endPage() throws IFException {
      try {
         this.gen.getResourceTracker().writeResources(true, this.gen);
      } catch (IOException var2) {
         throw new IFException("I/O error in endPage()", var2);
      }

      this.currentPageDefinition = null;
   }

   private boolean inPage() {
      return this.currentPageDefinition != null;
   }

   public void handleExtensionObject(Object extension) throws IFException {
      try {
         if (extension instanceof PSSetupCode) {
            if (this.inPage()) {
               PSRenderingUtil.writeEnclosedExtensionAttachment(this.gen, (PSSetupCode)extension);
            } else {
               if (this.setupCodeList == null) {
                  this.setupCodeList = new ArrayList();
               }

               if (!this.setupCodeList.contains(extension)) {
                  this.setupCodeList.add(extension);
               }
            }
         } else if (extension instanceof PSSetPageDevice) {
            PSSetPageDevice setPageDevice = (PSSetPageDevice)extension;
            String content = setPageDevice.getContent();
            if (content != null) {
               try {
                  this.pageDeviceDictionary.putAll(PSDictionary.valueOf(content));
               } catch (PSDictionaryFormatException var6) {
                  PSEventProducer eventProducer = PSEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
                  eventProducer.postscriptDictionaryParseError(this, content, var6);
               }
            }
         } else if (extension instanceof PSCommentBefore) {
            if (this.inPage()) {
               PSRenderingUtil.writeEnclosedExtensionAttachment(this.gen, (PSCommentBefore)extension);
            } else {
               if (this.comments[0] == null) {
                  this.comments[0] = new ArrayList();
               }

               this.comments[0].add(extension);
            }
         } else if (extension instanceof PSCommentAfter) {
            int targetCollection = this.inPage() ? 2 : 1;
            if (this.comments[targetCollection] == null) {
               this.comments[targetCollection] = new ArrayList();
            }

            this.comments[targetCollection].add(extension);
         }

      } catch (IOException var7) {
         throw new IFException("I/O error in handleExtensionObject()", var7);
      }
   }

   protected PSResource getPSResourceForFontKey(String key) {
      return this.fontResources.getPSResourceForFontKey(key);
   }

   protected PSResource getFormForImage(String uri) {
      if (uri != null && !"".equals(uri)) {
         if (this.formResources == null) {
            this.formResources = new HashMap();
         }

         PSResource form = (PSResource)this.formResources.get(uri);
         if (form == null) {
            form = new PSImageFormResource(this.formResources.size() + 1, uri);
            this.formResources.put(uri, form);
         }

         return (PSResource)form;
      } else {
         throw new IllegalArgumentException("uri must not be empty or null");
      }
   }

   static {
      log = LogFactory.getLog(PSDocumentHandler.class);
   }

   private static final class PageDefinition {
      private Dimension2D dimensions;
      private boolean rotate;

      private PageDefinition(Dimension2D dimensions, boolean rotate) {
         this.dimensions = dimensions;
         this.rotate = rotate;
      }

      // $FF: synthetic method
      PageDefinition(Dimension2D x0, boolean x1, Object x2) {
         this(x0, x1);
      }
   }
}
