package org.apache.batik.dom.svg;

import org.apache.batik.parser.PathHandler;
import org.w3c.dom.svg.SVGPathSeg;
import org.w3c.dom.svg.SVGPathSegArcAbs;
import org.w3c.dom.svg.SVGPathSegArcRel;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicRel;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothRel;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticRel;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothRel;
import org.w3c.dom.svg.SVGPathSegLinetoAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalRel;
import org.w3c.dom.svg.SVGPathSegLinetoRel;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalRel;
import org.w3c.dom.svg.SVGPathSegList;
import org.w3c.dom.svg.SVGPathSegMovetoAbs;
import org.w3c.dom.svg.SVGPathSegMovetoRel;

public abstract class SVGAnimatedPathDataSupport {
   public static void handlePathSegList(SVGPathSegList var0, PathHandler var1) {
      int var2 = var0.getNumberOfItems();
      var1.startPath();

      for(int var3 = 0; var3 < var2; ++var3) {
         SVGPathSeg var4 = var0.getItem(var3);
         switch (var4.getPathSegType()) {
            case 1:
               var1.closePath();
               break;
            case 2:
               SVGPathSegMovetoAbs var22 = (SVGPathSegMovetoAbs)var4;
               var1.movetoAbs(var22.getX(), var22.getY());
               break;
            case 3:
               SVGPathSegMovetoRel var21 = (SVGPathSegMovetoRel)var4;
               var1.movetoRel(var21.getX(), var21.getY());
               break;
            case 4:
               SVGPathSegLinetoAbs var20 = (SVGPathSegLinetoAbs)var4;
               var1.linetoAbs(var20.getX(), var20.getY());
               break;
            case 5:
               SVGPathSegLinetoRel var19 = (SVGPathSegLinetoRel)var4;
               var1.linetoRel(var19.getX(), var19.getY());
               break;
            case 6:
               SVGPathSegCurvetoCubicAbs var18 = (SVGPathSegCurvetoCubicAbs)var4;
               var1.curvetoCubicAbs(var18.getX1(), var18.getY1(), var18.getX2(), var18.getY2(), var18.getX(), var18.getY());
               break;
            case 7:
               SVGPathSegCurvetoCubicRel var17 = (SVGPathSegCurvetoCubicRel)var4;
               var1.curvetoCubicRel(var17.getX1(), var17.getY1(), var17.getX2(), var17.getY2(), var17.getX(), var17.getY());
               break;
            case 8:
               SVGPathSegCurvetoQuadraticAbs var16 = (SVGPathSegCurvetoQuadraticAbs)var4;
               var1.curvetoQuadraticAbs(var16.getX1(), var16.getY1(), var16.getX(), var16.getY());
               break;
            case 9:
               SVGPathSegCurvetoQuadraticRel var15 = (SVGPathSegCurvetoQuadraticRel)var4;
               var1.curvetoQuadraticRel(var15.getX1(), var15.getY1(), var15.getX(), var15.getY());
               break;
            case 10:
               SVGPathSegArcAbs var14 = (SVGPathSegArcAbs)var4;
               var1.arcAbs(var14.getR1(), var14.getR2(), var14.getAngle(), var14.getLargeArcFlag(), var14.getSweepFlag(), var14.getX(), var14.getY());
               break;
            case 11:
               SVGPathSegArcRel var13 = (SVGPathSegArcRel)var4;
               var1.arcRel(var13.getR1(), var13.getR2(), var13.getAngle(), var13.getLargeArcFlag(), var13.getSweepFlag(), var13.getX(), var13.getY());
               break;
            case 12:
               SVGPathSegLinetoHorizontalAbs var12 = (SVGPathSegLinetoHorizontalAbs)var4;
               var1.linetoHorizontalAbs(var12.getX());
               break;
            case 13:
               SVGPathSegLinetoHorizontalRel var11 = (SVGPathSegLinetoHorizontalRel)var4;
               var1.linetoHorizontalRel(var11.getX());
               break;
            case 14:
               SVGPathSegLinetoVerticalAbs var10 = (SVGPathSegLinetoVerticalAbs)var4;
               var1.linetoVerticalAbs(var10.getY());
               break;
            case 15:
               SVGPathSegLinetoVerticalRel var9 = (SVGPathSegLinetoVerticalRel)var4;
               var1.linetoVerticalRel(var9.getY());
               break;
            case 16:
               SVGPathSegCurvetoCubicSmoothAbs var8 = (SVGPathSegCurvetoCubicSmoothAbs)var4;
               var1.curvetoCubicSmoothAbs(var8.getX2(), var8.getY2(), var8.getX(), var8.getY());
               break;
            case 17:
               SVGPathSegCurvetoCubicSmoothRel var7 = (SVGPathSegCurvetoCubicSmoothRel)var4;
               var1.curvetoCubicSmoothRel(var7.getX2(), var7.getY2(), var7.getX(), var7.getY());
               break;
            case 18:
               SVGPathSegCurvetoQuadraticSmoothAbs var6 = (SVGPathSegCurvetoQuadraticSmoothAbs)var4;
               var1.curvetoQuadraticSmoothAbs(var6.getX(), var6.getY());
               break;
            case 19:
               SVGPathSegCurvetoQuadraticSmoothRel var5 = (SVGPathSegCurvetoQuadraticSmoothRel)var4;
               var1.curvetoQuadraticSmoothRel(var5.getX(), var5.getY());
         }
      }

      var1.endPath();
   }
}
