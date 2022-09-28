package org.apache.batik.ext.awt.image.renderable;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.SVGComposite;

public class FilterChainRable8Bit extends AbstractRable implements FilterChainRable, PaintRable {
   private int filterResolutionX;
   private int filterResolutionY;
   private Filter chainSource;
   private FilterResRable filterRes;
   private PadRable crop;
   private Rectangle2D filterRegion;

   public FilterChainRable8Bit(Filter var1, Rectangle2D var2) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else if (var2 == null) {
         throw new IllegalArgumentException();
      } else {
         Rectangle2D var3 = (Rectangle2D)var2.clone();
         this.crop = new PadRable8Bit(var1, var3, PadMode.ZERO_PAD);
         this.chainSource = var1;
         this.filterRegion = var2;
         this.init(this.crop);
      }
   }

   public int getFilterResolutionX() {
      return this.filterResolutionX;
   }

   public void setFilterResolutionX(int var1) {
      this.touch();
      this.filterResolutionX = var1;
      this.setupFilterRes();
   }

   public int getFilterResolutionY() {
      return this.filterResolutionY;
   }

   public void setFilterResolutionY(int var1) {
      this.touch();
      this.filterResolutionY = var1;
      this.setupFilterRes();
   }

   private void setupFilterRes() {
      if (this.filterResolutionX >= 0) {
         if (this.filterRes == null) {
            this.filterRes = new FilterResRable8Bit();
            this.filterRes.setSource(this.chainSource);
         }

         this.filterRes.setFilterResolutionX(this.filterResolutionX);
         this.filterRes.setFilterResolutionY(this.filterResolutionY);
      } else {
         this.filterRes = null;
      }

      if (this.filterRes != null) {
         this.crop.setSource(this.filterRes);
      } else {
         this.crop.setSource(this.chainSource);
      }

   }

   public void setFilterRegion(Rectangle2D var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.filterRegion = var1;
      }
   }

   public Rectangle2D getFilterRegion() {
      return this.filterRegion;
   }

   public Filter getSource() {
      return this.crop;
   }

   public void setSource(Filter var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Null Source for Filter Chain");
      } else {
         this.touch();
         this.chainSource = var1;
         if (this.filterRes == null) {
            this.crop.setSource(var1);
         } else {
            this.filterRes.setSource(var1);
         }

      }
   }

   public Rectangle2D getBounds2D() {
      return (Rectangle2D)this.filterRegion.clone();
   }

   public boolean paintRable(Graphics2D var1) {
      Composite var2 = var1.getComposite();
      if (!SVGComposite.OVER.equals(var2)) {
         return false;
      } else {
         GraphicsUtil.drawImage(var1, (RenderableImage)this.getSource());
         return true;
      }
   }

   public RenderedImage createRendering(RenderContext var1) {
      return this.crop.createRendering(var1);
   }
}
