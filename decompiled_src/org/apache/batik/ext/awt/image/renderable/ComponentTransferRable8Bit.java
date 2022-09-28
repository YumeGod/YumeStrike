package org.apache.batik.ext.awt.image.renderable;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.ComponentTransferFunction;
import org.apache.batik.ext.awt.image.DiscreteTransfer;
import org.apache.batik.ext.awt.image.GammaTransfer;
import org.apache.batik.ext.awt.image.IdentityTransfer;
import org.apache.batik.ext.awt.image.LinearTransfer;
import org.apache.batik.ext.awt.image.TableTransfer;
import org.apache.batik.ext.awt.image.TransferFunction;
import org.apache.batik.ext.awt.image.rendered.ComponentTransferRed;

public class ComponentTransferRable8Bit extends AbstractColorInterpolationRable implements ComponentTransferRable {
   public static final int ALPHA = 0;
   public static final int RED = 1;
   public static final int GREEN = 2;
   public static final int BLUE = 3;
   private ComponentTransferFunction[] functions = new ComponentTransferFunction[4];
   private TransferFunction[] txfFunc = new TransferFunction[4];

   public ComponentTransferRable8Bit(Filter var1, ComponentTransferFunction var2, ComponentTransferFunction var3, ComponentTransferFunction var4, ComponentTransferFunction var5) {
      super((Filter)var1, (Map)null);
      this.setAlphaFunction(var2);
      this.setRedFunction(var3);
      this.setGreenFunction(var4);
      this.setBlueFunction(var5);
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public ComponentTransferFunction getAlphaFunction() {
      return this.functions[0];
   }

   public void setAlphaFunction(ComponentTransferFunction var1) {
      this.touch();
      this.functions[0] = var1;
      this.txfFunc[0] = null;
   }

   public ComponentTransferFunction getRedFunction() {
      return this.functions[1];
   }

   public void setRedFunction(ComponentTransferFunction var1) {
      this.touch();
      this.functions[1] = var1;
      this.txfFunc[1] = null;
   }

   public ComponentTransferFunction getGreenFunction() {
      return this.functions[2];
   }

   public void setGreenFunction(ComponentTransferFunction var1) {
      this.touch();
      this.functions[2] = var1;
      this.txfFunc[2] = null;
   }

   public ComponentTransferFunction getBlueFunction() {
      return this.functions[3];
   }

   public void setBlueFunction(ComponentTransferFunction var1) {
      this.touch();
      this.functions[3] = var1;
      this.txfFunc[3] = null;
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderedImage var2 = this.getSource().createRendering(var1);
      return var2 == null ? null : new ComponentTransferRed(this.convertSourceCS(var2), this.getTransferFunctions(), var1.getRenderingHints());
   }

   private TransferFunction[] getTransferFunctions() {
      TransferFunction[] var1 = new TransferFunction[4];
      System.arraycopy(this.txfFunc, 0, var1, 0, 4);
      ComponentTransferFunction[] var2 = new ComponentTransferFunction[4];
      System.arraycopy(this.functions, 0, var2, 0, 4);

      for(int var3 = 0; var3 < 4; ++var3) {
         if (var1[var3] == null) {
            var1[var3] = getTransferFunction(var2[var3]);
            synchronized(this.functions) {
               if (this.functions[var3] == var2[var3]) {
                  this.txfFunc[var3] = var1[var3];
               }
            }
         }
      }

      return var1;
   }

   private static TransferFunction getTransferFunction(ComponentTransferFunction var0) {
      Object var1 = null;
      if (var0 == null) {
         var1 = new IdentityTransfer();
      } else {
         switch (var0.getType()) {
            case 0:
               var1 = new IdentityTransfer();
               break;
            case 1:
               var1 = new TableTransfer(tableFloatToInt(var0.getTableValues()));
               break;
            case 2:
               var1 = new DiscreteTransfer(tableFloatToInt(var0.getTableValues()));
               break;
            case 3:
               var1 = new LinearTransfer(var0.getSlope(), var0.getIntercept());
               break;
            case 4:
               var1 = new GammaTransfer(var0.getAmplitude(), var0.getExponent(), var0.getOffset());
               break;
            default:
               throw new Error();
         }
      }

      return (TransferFunction)var1;
   }

   private static int[] tableFloatToInt(float[] var0) {
      int[] var1 = new int[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = (int)(var0[var2] * 255.0F);
      }

      return var1;
   }
}
