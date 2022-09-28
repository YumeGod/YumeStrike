package org.apache.fop.render.intermediate.util;

import java.awt.Dimension;
import javax.xml.transform.Result;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.intermediate.IFDocumentNavigationHandler;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFPainter;

public class IFDocumentHandlerProxy implements IFDocumentHandler {
   protected IFDocumentHandler delegate;

   public IFDocumentHandlerProxy(IFDocumentHandler delegate) {
      this.delegate = delegate;
   }

   public boolean supportsPagesOutOfOrder() {
      return this.delegate.supportsPagesOutOfOrder();
   }

   public String getMimeType() {
      return this.delegate.getMimeType();
   }

   public void setContext(IFContext context) {
      this.delegate.setContext(context);
   }

   public IFContext getContext() {
      return this.delegate.getContext();
   }

   public FontInfo getFontInfo() {
      return this.delegate.getFontInfo();
   }

   public void setFontInfo(FontInfo fontInfo) {
      this.delegate.setFontInfo(fontInfo);
   }

   public void setDefaultFontInfo(FontInfo fontInfo) {
      this.delegate.setDefaultFontInfo(fontInfo);
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return this.delegate.getConfigurator();
   }

   public IFDocumentNavigationHandler getDocumentNavigationHandler() {
      return this.delegate.getDocumentNavigationHandler();
   }

   public void setResult(Result result) throws IFException {
      this.delegate.setResult(result);
   }

   public void startDocument() throws IFException {
      this.delegate.startDocument();
   }

   public void startDocumentHeader() throws IFException {
      this.delegate.startDocumentHeader();
   }

   public void endDocumentHeader() throws IFException {
      this.delegate.endDocumentHeader();
   }

   public void startPageSequence(String id) throws IFException {
      this.delegate.startPageSequence(id);
   }

   public void startPage(int index, String name, String pageMasterName, Dimension size) throws IFException {
      this.delegate.startPage(index, name, pageMasterName, size);
   }

   public void startPageHeader() throws IFException {
      this.delegate.startPageHeader();
   }

   public void endPageHeader() throws IFException {
      this.delegate.endPageHeader();
   }

   public IFPainter startPageContent() throws IFException {
      return this.delegate.startPageContent();
   }

   public void endPageContent() throws IFException {
      this.delegate.endPageContent();
   }

   public void startPageTrailer() throws IFException {
      this.delegate.startPageTrailer();
   }

   public void endPageTrailer() throws IFException {
      this.delegate.endPageTrailer();
   }

   public void endPage() throws IFException {
      this.delegate.endPage();
   }

   public void endPageSequence() throws IFException {
      this.delegate.endPageSequence();
   }

   public void startDocumentTrailer() throws IFException {
      this.delegate.startDocumentTrailer();
   }

   public void endDocumentTrailer() throws IFException {
      this.delegate.endDocumentTrailer();
   }

   public void endDocument() throws IFException {
      this.delegate.endDocument();
   }

   public void handleExtensionObject(Object extension) throws IFException {
      this.delegate.handleExtensionObject(extension);
   }
}
