package org.apache.fop.util.bitmap;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.ColorCube;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;

public class JAIMonochromeBitmapConverter implements MonochromeBitmapConverter {
   private boolean isErrorDiffusion = false;

   public void setHint(String name, String value) {
      if ("quality".equalsIgnoreCase(name)) {
         this.isErrorDiffusion = "true".equalsIgnoreCase(value);
      }

   }

   public RenderedImage convertToMonochrome(BufferedImage img) {
      return this.convertToMonochromePlanarImage(img);
   }

   private PlanarImage convertToMonochromePlanarImage(BufferedImage img) {
      if (img.getColorModel().getColorSpace().getNumComponents() != 1) {
         img = BitmapImageUtil.convertToGrayscale(img, (Dimension)null);
      }

      ParameterBlock pb = new ParameterBlock();
      pb.addSource(img);
      String opName = null;
      if (this.isErrorDiffusion) {
         opName = "errordiffusion";
         LookupTableJAI lut = new LookupTableJAI(new byte[]{0, -1});
         pb.add(lut);
         pb.add(KernelJAI.ERROR_FILTER_FLOYD_STEINBERG);
      } else {
         opName = "ordereddither";
         ColorCube colorMap = ColorCube.createColorCube(0, 0, new int[]{2});
         pb.add(colorMap);
         pb.add(KernelJAI.DITHER_MASK_441);
      }

      ImageLayout layout = new ImageLayout();
      byte[] map = new byte[]{0, -1};
      ColorModel cm = new IndexColorModel(1, 2, map, map, map);
      layout.setColorModel(cm);
      RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
      PlanarImage dst = JAI.create(opName, pb, hints);
      return dst;
   }
}
