package org.apache.batik.ext.awt.image.rendered;

import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.ColorSpaceHintKey;

public class FilterAlphaRed extends AbstractRed {
   public FilterAlphaRed(CachableRed var1) {
      super((CachableRed)var1, var1.getBounds(), var1.getColorModel(), var1.getSampleModel(), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
      this.props.put("org.apache.batik.gvt.filter.Colorspace", ColorSpaceHintKey.VALUE_COLORSPACE_ALPHA);
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      SampleModel var3 = var2.getSampleModel();
      if (var3.getNumBands() == 1) {
         return var2.copyData(var1);
      } else {
         PadRed.ZeroRecter.zeroRect(var1);
         Raster var4 = var2.getData(var1.getBounds());
         AbstractRed.copyBand(var4, var4.getNumBands() - 1, var1, var1.getNumBands() - 1);
         return var1;
      }
   }
}
