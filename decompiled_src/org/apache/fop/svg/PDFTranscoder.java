package org.apache.fop.svg;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.parser.UnitProcessor;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.fop.Version;
import org.apache.fop.fonts.FontInfo;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.w3c.dom.Document;

public class PDFTranscoder extends AbstractFOPTranscoder implements Configurable {
   protected PDFDocumentGraphics2D graphics = null;

   public PDFTranscoder() {
      this.handler = new AbstractFOPTranscoder.FOPErrorHandler();
   }

   protected UserAgent createUserAgent() {
      return new AbstractFOPTranscoder.FOPTranscoderUserAgent() {
         public float getPixelUnitToMillimeter() {
            return super.getPixelUnitToMillimeter();
         }
      };
   }

   protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
      this.graphics = new PDFDocumentGraphics2D(this.isTextStroked());
      this.graphics.getPDFDocument().getInfo().setProducer("Apache FOP Version " + Version.getVersion() + ": PDF Transcoder for Batik");
      if (this.hints.containsKey(KEY_DEVICE_RESOLUTION)) {
         this.graphics.setDeviceDPI(this.getDeviceResolution());
      }

      this.setupImageInfrastructure(uri);

      try {
         Configuration effCfg = this.getEffectiveConfiguration();
         if (effCfg != null) {
            PDFDocumentGraphics2DConfigurator configurator = new PDFDocumentGraphics2DConfigurator();
            configurator.configure(this.graphics, effCfg);
         } else {
            this.graphics.setupDefaultFontInfo();
         }
      } catch (Exception var11) {
         throw new TranscoderException("Error while setting up PDFDocumentGraphics2D", var11);
      }

      super.transcode(document, uri, output);
      if (this.getLogger().isTraceEnabled()) {
         this.getLogger().trace("document size: " + this.width + " x " + this.height);
      }

      UnitProcessor.Context uctx = org.apache.batik.bridge.UnitProcessor.createContext(this.ctx, document.getDocumentElement());
      float widthInPt = org.apache.batik.bridge.UnitProcessor.userSpaceToSVG(this.width, (short)9, (short)2, uctx);
      int w = (int)((double)widthInPt + 0.5);
      float heightInPt = org.apache.batik.bridge.UnitProcessor.userSpaceToSVG(this.height, (short)9, (short)2, uctx);
      int h = (int)((double)heightInPt + 0.5);
      if (this.getLogger().isTraceEnabled()) {
         this.getLogger().trace("document size: " + w + "pt x " + h + "pt");
      }

      try {
         OutputStream out = output.getOutputStream();
         if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream((OutputStream)out);
         }

         this.graphics.setupDocument((OutputStream)out, w, h);
         this.graphics.setSVGDimension(this.width, this.height);
         if (this.hints.containsKey(ImageTranscoder.KEY_BACKGROUND_COLOR)) {
            this.graphics.setBackgroundColor((Color)this.hints.get(ImageTranscoder.KEY_BACKGROUND_COLOR));
         }

         this.graphics.setGraphicContext(new GraphicContext());
         this.graphics.preparePainting();
         this.graphics.transform(this.curTxf);
         this.graphics.setRenderingHint(RenderingHintsKeyExt.KEY_TRANSCODING, "Vector");
         this.root.paint(this.graphics);
         this.graphics.finish();
      } catch (IOException var10) {
         throw new TranscoderException(var10);
      }
   }

   protected BridgeContext createBridgeContext() {
      return this.createBridgeContext("1.x");
   }

   public BridgeContext createBridgeContext(String version) {
      FontInfo fontInfo = this.graphics.getFontInfo();
      if (this.isTextStroked()) {
         fontInfo = null;
      }

      BridgeContext ctx = new PDFBridgeContext(this.userAgent, fontInfo, this.getImageManager(), this.getImageSessionContext());
      return ctx;
   }
}
