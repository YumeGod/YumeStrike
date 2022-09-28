package org.apache.fop.render.intermediate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;

public abstract class AbstractIFDocumentHandler implements IFDocumentHandler {
   private static Log log;
   private IFContext ifContext;

   public void setContext(IFContext context) {
      this.ifContext = context;
   }

   public IFContext getContext() {
      return this.ifContext;
   }

   public FOUserAgent getUserAgent() {
      return this.getContext().getUserAgent();
   }

   public IFDocumentNavigationHandler getDocumentNavigationHandler() {
      return null;
   }

   public void startDocument() throws IFException {
      if (this.getUserAgent() == null) {
         throw new IllegalStateException("User agent must be set before starting document generation");
      }
   }

   public void startDocumentHeader() throws IFException {
   }

   public void endDocumentHeader() throws IFException {
   }

   public void startDocumentTrailer() throws IFException {
   }

   public void endDocumentTrailer() throws IFException {
   }

   public void startPageHeader() throws IFException {
   }

   public void endPageHeader() throws IFException {
   }

   public void startPageTrailer() throws IFException {
   }

   public void endPageTrailer() throws IFException {
   }

   static {
      log = LogFactory.getLog(AbstractIFDocumentHandler.class);
   }
}
