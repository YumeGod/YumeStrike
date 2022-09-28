package org.apache.batik.ext.awt.image.rendered;

import java.awt.RenderingHints;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.TransferFunction;

public class ComponentTransferRed extends AbstractRed {
   LookupOp operation;

   public ComponentTransferRed(CachableRed var1, TransferFunction[] var2, RenderingHints var3) {
      super((CachableRed)var1, var1.getBounds(), GraphicsUtil.coerceColorModel(var1.getColorModel(), false), var1.getSampleModel(), (Map)null);
      byte[][] var4 = new byte[][]{var2[1].getLookupTable(), var2[2].getLookupTable(), var2[3].getLookupTable(), var2[0].getLookupTable()};
      this.operation = new LookupOp(new ByteLookupTable(0, var4), var3) {
      };
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      var1 = var2.copyData(var1);
      GraphicsUtil.coerceData(var1, var2.getColorModel(), false);
      WritableRaster var3 = var1.createWritableTranslatedChild(0, 0);
      this.operation.filter(var3, var3);
      return var1;
   }
}
