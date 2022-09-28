package org.apache.fop.render.pdf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.extensions.xmp.XMPMetadata;
import org.apache.fop.pdf.PDFAnnotList;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFReference;
import org.apache.fop.pdf.PDFResourceContext;
import org.apache.fop.pdf.PDFResources;
import org.apache.fop.render.extensions.prepress.PageBoundaries;
import org.apache.fop.render.extensions.prepress.PageScale;
import org.apache.fop.render.intermediate.AbstractBinaryWritingIFDocumentHandler;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.intermediate.IFDocumentNavigationHandler;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFPainter;
import org.apache.fop.util.XMLUtil;
import org.apache.xmlgraphics.xmp.Metadata;
import org.w3c.dom.NodeList;

public class PDFDocumentHandler extends AbstractBinaryWritingIFDocumentHandler {
   private static Log log;
   private int pageSequenceIndex;
   private boolean accessEnabled;
   private PDFLogicalStructureHandler logicalStructureHandler;
   protected PDFDocument pdfDoc;
   protected PDFRenderingUtil pdfUtil;
   protected PDFResources pdfResources;
   protected PDFContentGenerator generator;
   protected PDFResourceContext currentContext;
   protected PDFPage currentPage;
   protected PageReference currentPageRef;
   protected Map pageReferences = new HashMap();
   private final PDFDocumentNavigationHandler documentNavigationHandler = new PDFDocumentNavigationHandler(this);

   public boolean supportsPagesOutOfOrder() {
      return !this.accessEnabled;
   }

   public String getMimeType() {
      return "application/pdf";
   }

   public void setContext(IFContext context) {
      super.setContext(context);
      this.pdfUtil = new PDFRenderingUtil(context.getUserAgent());
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return new PDFRendererConfigurator(this.getUserAgent());
   }

   public IFDocumentNavigationHandler getDocumentNavigationHandler() {
      return this.documentNavigationHandler;
   }

   PDFRenderingUtil getPDFUtil() {
      return this.pdfUtil;
   }

   PDFLogicalStructureHandler getLogicalStructureHandler() {
      return this.logicalStructureHandler;
   }

   public void startDocument() throws IFException {
      super.startDocument();

      try {
         this.pdfDoc = this.pdfUtil.setupPDFDocument(this.outputStream);
         this.accessEnabled = this.getUserAgent().isAccessibilityEnabled();
         if (this.accessEnabled) {
            this.pdfDoc.getRoot().makeTagged();
            this.logicalStructureHandler = new PDFLogicalStructureHandler(this.pdfDoc, this.getUserAgent().getEventBroadcaster());
         }

      } catch (IOException var2) {
         throw new IFException("I/O error in startDocument()", var2);
      }
   }

   public void endDocumentHeader() throws IFException {
      this.pdfUtil.generateDefaultXMPMetadata();
   }

   public void endDocument() throws IFException {
      try {
         this.pdfDoc.getResources().addFonts(this.pdfDoc, this.fontInfo);
         this.pdfDoc.outputTrailer(this.outputStream);
         this.pdfDoc = null;
         this.pdfResources = null;
         this.generator = null;
         this.currentContext = null;
         this.currentPage = null;
      } catch (IOException var2) {
         throw new IFException("I/O error in endDocument()", var2);
      }

      super.endDocument();
   }

   public void startPageSequence(String id) throws IFException {
      if (this.pdfDoc.getRoot().getLanguage() == null && this.getContext().getLanguage() != null) {
         this.pdfDoc.getRoot().setLanguage(XMLUtil.toRFC3066(this.getContext().getLanguage()));
      }

      if (this.accessEnabled) {
         NodeList nodes = this.getUserAgent().getStructureTree().getPageSequence(this.pageSequenceIndex++);
         this.logicalStructureHandler.processStructureTree(nodes, this.getContext().getLanguage());
      }

   }

   public void endPageSequence() throws IFException {
   }

   public void startPage(int index, String name, String pageMasterName, Dimension size) throws IFException {
      this.pdfResources = this.pdfDoc.getResources();
      PageBoundaries boundaries = new PageBoundaries(size, this.getContext().getForeignAttributes());
      Rectangle trimBox = boundaries.getTrimBox();
      Rectangle bleedBox = boundaries.getBleedBox();
      Rectangle mediaBox = boundaries.getMediaBox();
      Rectangle cropBox = boundaries.getCropBox();
      double scaleX = 1.0;
      double scaleY = 1.0;
      String scale = (String)this.getContext().getForeignAttribute(PageScale.EXT_PAGE_SCALE);
      Point2D scales = PageScale.getScale(scale);
      if (scales != null) {
         scaleX = scales.getX();
         scaleY = scales.getY();
      }

      AffineTransform boxTransform = new AffineTransform(scaleX / 1000.0, 0.0, 0.0, -scaleY / 1000.0, 0.0, scaleY * size.getHeight() / 1000.0);
      this.currentPage = this.pdfDoc.getFactory().makePage(this.pdfResources, index, this.toPDFCoordSystem(mediaBox, boxTransform), this.toPDFCoordSystem(cropBox, boxTransform), this.toPDFCoordSystem(bleedBox, boxTransform), this.toPDFCoordSystem(trimBox, boxTransform));
      if (this.accessEnabled) {
         this.logicalStructureHandler.startPage(this.currentPage);
      }

      this.pdfUtil.generatePageLabel(index, name);
      this.currentPageRef = new PageReference(this.currentPage, size);
      this.pageReferences.put(new Integer(index), this.currentPageRef);
      this.generator = new PDFContentGenerator(this.pdfDoc, this.outputStream, this.currentPage);
      AffineTransform basicPageTransform = new AffineTransform(1.0, 0.0, 0.0, -1.0, 0.0, scaleY * (double)size.height / 1000.0);
      basicPageTransform.scale(scaleX, scaleY);
      this.generator.saveGraphicsState();
      this.generator.concatenate(basicPageTransform);
   }

   private Rectangle2D toPDFCoordSystem(Rectangle box, AffineTransform transform) {
      return transform.createTransformedShape(box).getBounds2D();
   }

   public IFPainter startPageContent() throws IFException {
      return new PDFPainter(this, this.logicalStructureHandler);
   }

   public void endPageContent() throws IFException {
      this.generator.restoreGraphicsState();
   }

   public void endPage() throws IFException {
      if (this.accessEnabled) {
         this.logicalStructureHandler.endPage();
      }

      try {
         this.documentNavigationHandler.commit();
         this.pdfDoc.registerObject(this.generator.getStream());
         this.currentPage.setContents(this.generator.getStream());
         PDFAnnotList annots = this.currentPage.getAnnotations();
         if (annots != null) {
            this.pdfDoc.addObject(annots);
         }

         this.pdfDoc.addObject(this.currentPage);
         this.generator.flushPDFDoc();
         this.generator = null;
      } catch (IOException var2) {
         throw new IFException("I/O error in endPage()", var2);
      }
   }

   public void handleExtensionObject(Object extension) throws IFException {
      if (extension instanceof XMPMetadata) {
         this.pdfUtil.renderXMPMetadata((XMPMetadata)extension);
      } else if (extension instanceof Metadata) {
         XMPMetadata wrapper = new XMPMetadata((Metadata)extension);
         this.pdfUtil.renderXMPMetadata(wrapper);
      } else {
         log.debug("Don't know how to handle extension object. Ignoring: " + extension + " (" + extension.getClass().getName() + ")");
      }

   }

   PageReference getPageReference(int pageIndex) {
      return (PageReference)this.pageReferences.get(new Integer(pageIndex));
   }

   static {
      log = LogFactory.getLog(PDFDocumentHandler.class);
   }

   static final class PageReference {
      private final PDFReference pageRef;
      private final Dimension pageDimension;

      private PageReference(PDFPage page, Dimension dim) {
         this.pageRef = page.makeReference();
         this.pageDimension = new Dimension(dim);
      }

      public PDFReference getPageRef() {
         return this.pageRef;
      }

      public Dimension getPageDimension() {
         return this.pageDimension;
      }

      // $FF: synthetic method
      PageReference(PDFPage x0, Dimension x1, Object x2) {
         this(x0, x1);
      }
   }
}
