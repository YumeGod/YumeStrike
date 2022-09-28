package org.apache.batik.gvt.filter;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.AbstractRed;
import org.apache.batik.ext.awt.image.rendered.AbstractTiledRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.gvt.GraphicsNode;

public class GraphicsNodeRed8Bit extends AbstractRed {
   private GraphicsNode node;
   private AffineTransform node2dev;
   private RenderingHints hints;
   private boolean usePrimitivePaint;
   static final boolean onMacOSX = "Mac OS X".equals(System.getProperty("os.name"));

   public GraphicsNodeRed8Bit(GraphicsNode var1, AffineTransform var2, boolean var3, RenderingHints var4) {
      this.node = var1;
      this.node2dev = var2;
      this.hints = var4;
      this.usePrimitivePaint = var3;
      AffineTransform var5 = var2;
      Object var6 = var1.getPrimitiveBounds();
      if (var6 == null) {
         var6 = new Rectangle2D.Float(0.0F, 0.0F, 1.0F, 1.0F);
      }

      if (!var3) {
         AffineTransform var7 = var1.getTransform();
         if (var7 != null) {
            var5 = (AffineTransform)var2.clone();
            var5.concatenate(var7);
         }
      }

      Rectangle var15 = var5.createTransformedShape((Shape)var6).getBounds();
      ColorModel var8 = this.createColorModel();
      int var9 = AbstractTiledRed.getDefaultTileSize();
      int var10 = var9 * (int)Math.floor((double)(var15.x / var9));
      int var11 = var9 * (int)Math.floor((double)(var15.y / var9));
      int var12 = var15.x + var15.width - var10;
      if (var12 > var9) {
         var12 = var9;
      }

      int var13 = var15.y + var15.height - var11;
      if (var13 > var9) {
         var13 = var9;
      }

      if (var12 <= 0 || var13 <= 0) {
         var12 = 1;
         var13 = 1;
      }

      SampleModel var14 = var8.createCompatibleSampleModel(var12, var13);
      this.init((CachableRed)null, var15, var8, var14, var10, var11, (Map)null);
   }

   public WritableRaster copyData(WritableRaster var1) {
      this.genRect(var1);
      return var1;
   }

   public void genRect(WritableRaster var1) {
      BufferedImage var2 = new BufferedImage(this.cm, var1.createWritableTranslatedChild(0, 0), this.cm.isAlphaPremultiplied(), (Hashtable)null);
      Graphics2D var3 = GraphicsUtil.createGraphics(var2, this.hints);
      var3.setComposite(AlphaComposite.Clear);
      var3.fillRect(0, 0, var1.getWidth(), var1.getHeight());
      var3.setComposite(AlphaComposite.SrcOver);
      var3.translate(-var1.getMinX(), -var1.getMinY());
      var3.transform(this.node2dev);
      if (this.usePrimitivePaint) {
         this.node.primitivePaint(var3);
      } else {
         this.node.paint(var3);
      }

      var3.dispose();
   }

   public ColorModel createColorModel() {
      return onMacOSX ? GraphicsUtil.sRGB_Pre : GraphicsUtil.sRGB_Unpre;
   }
}
