package org.w3c.dom.svg;

import org.w3c.dom.events.EventTarget;

public interface SVGPathElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, SVGTransformable, EventTarget, SVGAnimatedPathData {
   SVGAnimatedNumber getPathLength();

   float getTotalLength();

   SVGPoint getPointAtLength(float var1);

   int getPathSegAtLength(float var1);

   SVGPathSegClosePath createSVGPathSegClosePath();

   SVGPathSegMovetoAbs createSVGPathSegMovetoAbs(float var1, float var2);

   SVGPathSegMovetoRel createSVGPathSegMovetoRel(float var1, float var2);

   SVGPathSegLinetoAbs createSVGPathSegLinetoAbs(float var1, float var2);

   SVGPathSegLinetoRel createSVGPathSegLinetoRel(float var1, float var2);

   SVGPathSegCurvetoCubicAbs createSVGPathSegCurvetoCubicAbs(float var1, float var2, float var3, float var4, float var5, float var6);

   SVGPathSegCurvetoCubicRel createSVGPathSegCurvetoCubicRel(float var1, float var2, float var3, float var4, float var5, float var6);

   SVGPathSegCurvetoQuadraticAbs createSVGPathSegCurvetoQuadraticAbs(float var1, float var2, float var3, float var4);

   SVGPathSegCurvetoQuadraticRel createSVGPathSegCurvetoQuadraticRel(float var1, float var2, float var3, float var4);

   SVGPathSegArcAbs createSVGPathSegArcAbs(float var1, float var2, float var3, float var4, float var5, boolean var6, boolean var7);

   SVGPathSegArcRel createSVGPathSegArcRel(float var1, float var2, float var3, float var4, float var5, boolean var6, boolean var7);

   SVGPathSegLinetoHorizontalAbs createSVGPathSegLinetoHorizontalAbs(float var1);

   SVGPathSegLinetoHorizontalRel createSVGPathSegLinetoHorizontalRel(float var1);

   SVGPathSegLinetoVerticalAbs createSVGPathSegLinetoVerticalAbs(float var1);

   SVGPathSegLinetoVerticalRel createSVGPathSegLinetoVerticalRel(float var1);

   SVGPathSegCurvetoCubicSmoothAbs createSVGPathSegCurvetoCubicSmoothAbs(float var1, float var2, float var3, float var4);

   SVGPathSegCurvetoCubicSmoothRel createSVGPathSegCurvetoCubicSmoothRel(float var1, float var2, float var3, float var4);

   SVGPathSegCurvetoQuadraticSmoothAbs createSVGPathSegCurvetoQuadraticSmoothAbs(float var1, float var2);

   SVGPathSegCurvetoQuadraticSmoothRel createSVGPathSegCurvetoQuadraticSmoothRel(float var1, float var2);
}
