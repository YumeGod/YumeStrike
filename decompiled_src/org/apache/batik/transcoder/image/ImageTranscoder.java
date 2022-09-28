package org.apache.batik.transcoder.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.SinglePixelPackedSampleModel;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.gvt.renderer.ConcreteImageRendererFactory;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.transcoder.keys.PaintKey;
import org.w3c.dom.Document;

public abstract class ImageTranscoder extends SVGAbstractTranscoder {
   public static final TranscodingHints.Key KEY_BACKGROUND_COLOR = new PaintKey();
   public static final TranscodingHints.Key KEY_FORCE_TRANSPARENT_WHITE = new BooleanKey();

   protected ImageTranscoder() {
   }

   protected void transcode(Document var1, String var2, TranscoderOutput var3) throws TranscoderException {
      super.transcode(var1, var2, var3);
      int var4 = (int)((double)this.width + 0.5);
      int var5 = (int)((double)this.height + 0.5);
      ImageRenderer var6 = this.createRenderer();
      var6.updateOffScreen(var4, var5);
      var6.setTransform(this.curTxf);
      var6.setTree(this.root);
      this.root = null;

      try {
         Rectangle2D.Float var7 = new Rectangle2D.Float(0.0F, 0.0F, this.width, this.height);
         var6.repaint(this.curTxf.createInverse().createTransformedShape(var7));
         BufferedImage var8 = var6.getOffScreen();
         var6 = null;
         BufferedImage var9 = this.createImage(var4, var5);
         Graphics2D var10 = GraphicsUtil.createGraphics(var9);
         if (this.hints.containsKey(KEY_BACKGROUND_COLOR)) {
            Paint var11 = (Paint)this.hints.get(KEY_BACKGROUND_COLOR);
            var10.setComposite(AlphaComposite.SrcOver);
            var10.setPaint(var11);
            var10.fillRect(0, 0, var4, var5);
         }

         if (var8 != null) {
            var10.drawRenderedImage(var8, new AffineTransform());
         }

         var10.dispose();
         var8 = null;
         this.writeImage(var9, var3);
      } catch (Exception var12) {
         throw new TranscoderException(var12);
      }
   }

   protected ImageRenderer createRenderer() {
      ConcreteImageRendererFactory var1 = new ConcreteImageRendererFactory();
      return var1.createStaticImageRenderer();
   }

   protected void forceTransparentWhite(BufferedImage var1, SinglePixelPackedSampleModel var2) {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      DataBufferInt var5 = (DataBufferInt)var1.getRaster().getDataBuffer();
      int var6 = var2.getScanlineStride();
      int var7 = var5.getOffset();
      int[] var8 = var5.getBankData()[0];
      int var9 = var7;
      int var10 = var6 - var3;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      boolean var14 = false;
      boolean var15 = false;

      for(int var16 = 0; var16 < var4; ++var16) {
         for(int var17 = 0; var17 < var3; ++var17) {
            int var22 = var8[var9];
            int var18 = var22 >> 24 & 255;
            int var19 = var22 >> 16 & 255;
            int var20 = var22 >> 8 & 255;
            int var21 = var22 & 255;
            var19 = (255 * (255 - var18) + var18 * var19) / 255;
            var20 = (255 * (255 - var18) + var18 * var20) / 255;
            var21 = (255 * (255 - var18) + var18 * var21) / 255;
            var8[var9++] = var18 << 24 & -16777216 | var19 << 16 & 16711680 | var20 << 8 & '\uff00' | var21 & 255;
         }

         var9 += var10;
      }

   }

   public abstract BufferedImage createImage(int var1, int var2);

   public abstract void writeImage(BufferedImage var1, TranscoderOutput var2) throws TranscoderException;
}
