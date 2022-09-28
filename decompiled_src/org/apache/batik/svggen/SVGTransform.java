package org.apache.batik.svggen;

import java.util.Stack;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.batik.ext.awt.g2d.TransformStackElement;

public class SVGTransform extends AbstractSVGConverter {
   private static double radiansToDegrees = 57.29577951308232;

   public SVGTransform(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return new SVGTransformDescriptor(this.toSVGTransform(var1));
   }

   public final String toSVGTransform(GraphicContext var1) {
      return this.toSVGTransform(var1.getTransformStack());
   }

   public final String toSVGTransform(TransformStackElement[] var1) {
      int var2 = var1.length;
      Stack var3 = new Stack() {
         public Object push(Object var1) {
            Object var2;
            if (((TransformStackElement)var1).isIdentity()) {
               var2 = this.pop();
            } else {
               super.push(var1);
               var2 = null;
            }

            return var2;
         }

         public Object pop() {
            Object var1 = null;
            if (!super.empty()) {
               var1 = super.pop();
            }

            return var1;
         }
      };
      boolean var4 = false;
      int var5 = 0;
      boolean var6 = false;
      boolean var7 = false;

      TransformStackElement var8;
      for(var8 = null; var5 < var2; var8 = (TransformStackElement)var3.push(var8)) {
         int var13 = var5;
         if (var8 == null) {
            var8 = (TransformStackElement)var1[var5].clone();
            var13 = var5 + 1;
         }

         var4 = true;

         int var12;
         for(var12 = var13; var12 < var2; ++var12) {
            var4 = var8.concatenate(var1[var12]);
            if (!var4) {
               break;
            }
         }

         var5 = var12;
      }

      if (var8 != null) {
         var3.push(var8);
      }

      int var9 = var3.size();
      StringBuffer var10 = new StringBuffer(var9 * 8);

      for(var5 = 0; var5 < var9; ++var5) {
         var10.append(this.convertTransform((TransformStackElement)var3.get(var5)));
         var10.append(" ");
      }

      String var11 = var10.toString().trim();
      return var11;
   }

   final String convertTransform(TransformStackElement var1) {
      StringBuffer var2 = new StringBuffer();
      double[] var3 = var1.getTransformParameters();
      switch (var1.getType().toInt()) {
         case 0:
            if (!var1.isIdentity()) {
               var2.append("translate");
               var2.append("(");
               var2.append(this.doubleString(var3[0]));
               var2.append(",");
               var2.append(this.doubleString(var3[1]));
               var2.append(")");
            }
            break;
         case 1:
            if (!var1.isIdentity()) {
               var2.append("rotate");
               var2.append("(");
               var2.append(this.doubleString(radiansToDegrees * var3[0]));
               var2.append(")");
            }
            break;
         case 2:
            if (!var1.isIdentity()) {
               var2.append("scale");
               var2.append("(");
               var2.append(this.doubleString(var3[0]));
               var2.append(",");
               var2.append(this.doubleString(var3[1]));
               var2.append(")");
            }
            break;
         case 3:
            if (!var1.isIdentity()) {
               var2.append("matrix");
               var2.append("(");
               var2.append(1);
               var2.append(",");
               var2.append(this.doubleString(var3[1]));
               var2.append(",");
               var2.append(this.doubleString(var3[0]));
               var2.append(",");
               var2.append(1);
               var2.append(",");
               var2.append(0);
               var2.append(",");
               var2.append(0);
               var2.append(")");
            }
            break;
         case 4:
            if (!var1.isIdentity()) {
               var2.append("matrix");
               var2.append("(");
               var2.append(this.doubleString(var3[0]));
               var2.append(",");
               var2.append(this.doubleString(var3[1]));
               var2.append(",");
               var2.append(this.doubleString(var3[2]));
               var2.append(",");
               var2.append(this.doubleString(var3[3]));
               var2.append(",");
               var2.append(this.doubleString(var3[4]));
               var2.append(",");
               var2.append(this.doubleString(var3[5]));
               var2.append(")");
            }
            break;
         default:
            throw new Error();
      }

      return var2.toString();
   }
}
