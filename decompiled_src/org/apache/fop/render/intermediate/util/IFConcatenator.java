package org.apache.fop.render.intermediate.util;

import java.awt.Dimension;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentNavigationHandler;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFParser;
import org.apache.xmlgraphics.xmp.Metadata;

public class IFConcatenator {
   private IFDocumentHandler targetHandler;
   private int nextPageIndex = 0;

   public IFConcatenator(IFDocumentHandler targetHandler, Metadata metadata) throws IFException {
      this.targetHandler = targetHandler;
      this.startDocument(metadata);
   }

   private void startDocument(Metadata metadata) throws IFException {
      this.targetHandler.startDocument();
      this.targetHandler.startDocumentHeader();
      if (metadata != null) {
         this.targetHandler.handleExtensionObject(metadata);
      }

      this.targetHandler.endDocumentHeader();
   }

   private void endDocument() throws IFException {
      this.targetHandler.startPageTrailer();
      this.targetHandler.endPageTrailer();
      this.targetHandler.endDocument();
   }

   protected IFDocumentHandler getTargetHandler() {
      return this.targetHandler;
   }

   public void finish() throws IFException {
      this.endDocument();
   }

   public void appendDocument(Source src) throws TransformerException, IFException {
      IFParser parser = new IFParser();
      parser.parse(src, new IFPageSequenceFilter(this.getTargetHandler()), this.getTargetHandler().getContext().getUserAgent());
   }

   private class IFPageSequenceFilter extends IFDocumentHandlerProxy {
      private boolean inPageSequence = false;

      public IFPageSequenceFilter(IFDocumentHandler delegate) {
         super(delegate);
      }

      public void startDocument() throws IFException {
      }

      public void startDocumentHeader() throws IFException {
      }

      public void endDocumentHeader() throws IFException {
      }

      public void startPageSequence(String id) throws IFException {
         assert !this.inPageSequence;

         this.inPageSequence = true;
         super.startPageSequence(id);
      }

      public void startPage(int index, String name, String pageMasterName, Dimension size) throws IFException {
         super.startPage(IFConcatenator.this.nextPageIndex, name, pageMasterName, size);
         IFConcatenator.this.nextPageIndex++;
      }

      public void endPageSequence() throws IFException {
         super.endPageSequence();

         assert this.inPageSequence;

         this.inPageSequence = false;
      }

      public void startDocumentTrailer() throws IFException {
      }

      public void endDocumentTrailer() throws IFException {
      }

      public void endDocument() throws IFException {
      }

      public void handleExtensionObject(Object extension) throws IFException {
         if (this.inPageSequence) {
            super.handleExtensionObject(extension);
         }

      }

      public IFDocumentNavigationHandler getDocumentNavigationHandler() {
         return null;
      }
   }
}
