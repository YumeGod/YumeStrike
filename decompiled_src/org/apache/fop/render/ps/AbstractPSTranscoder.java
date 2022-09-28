package org.apache.fop.render.ps;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.parser.UnitProcessor;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.svg.AbstractFOPTranscoder;
import org.apache.fop.svg.PDFDocumentGraphics2DConfigurator;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.AbstractPSDocumentGraphics2D;
import org.w3c.dom.Document;

public abstract class AbstractPSTranscoder extends AbstractFOPTranscoder {
   protected AbstractPSDocumentGraphics2D graphics = null;
   private FontInfo fontInfo;

   protected abstract AbstractPSDocumentGraphics2D createDocumentGraphics2D();

   protected boolean getAutoFontsDefault() {
      return false;
   }

   protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
      this.graphics = this.createDocumentGraphics2D();
      if (!this.isTextStroked()) {
         try {
            this.fontInfo = PDFDocumentGraphics2DConfigurator.createFontInfo(this.getEffectiveConfiguration());
            this.graphics.setCustomTextHandler(new NativeTextHandler(this.graphics, this.fontInfo));
         } catch (FOPException var11) {
            throw new TranscoderException(var11);
         }
      }

      super.transcode(document, uri, output);
      this.getLogger().trace("document size: " + this.width + " x " + this.height);
      UnitProcessor.Context uctx = org.apache.batik.bridge.UnitProcessor.createContext(this.ctx, document.getDocumentElement());
      float widthInPt = org.apache.batik.bridge.UnitProcessor.userSpaceToSVG(this.width, (short)9, (short)2, uctx);
      int w = (int)((double)widthInPt + 0.5);
      float heightInPt = org.apache.batik.bridge.UnitProcessor.userSpaceToSVG(this.height, (short)9, (short)2, uctx);
      int h = (int)((double)heightInPt + 0.5);
      this.getLogger().trace("document size: " + w + "pt x " + h + "pt");

      try {
         OutputStream out = output.getOutputStream();
         if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream((OutputStream)out);
         }

         this.graphics.setupDocument((OutputStream)out, w, h);
         this.graphics.setViewportDimension(this.width, this.height);
         if (this.hints.containsKey(ImageTranscoder.KEY_BACKGROUND_COLOR)) {
            this.graphics.setBackgroundColor((Color)this.hints.get(ImageTranscoder.KEY_BACKGROUND_COLOR));
         }

         this.graphics.setGraphicContext(new GraphicContext());
         this.graphics.setTransform(this.curTxf);
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
      BridgeContext ctx = new PSBridgeContext(this.userAgent, this.isTextStroked() ? null : this.fontInfo, this.getImageManager(), this.getImageSessionContext());
      return ctx;
   }
}
