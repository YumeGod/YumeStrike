package org.w3c.dom.svg;

public interface SVGFEGaussianBlurElement extends SVGElement, SVGFilterPrimitiveStandardAttributes {
   SVGAnimatedString getIn1();

   SVGAnimatedNumber getStdDeviationX();

   SVGAnimatedNumber getStdDeviationY();

   void setStdDeviation(float var1, float var2);
}
