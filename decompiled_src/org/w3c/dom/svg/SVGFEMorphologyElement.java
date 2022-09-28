package org.w3c.dom.svg;

public interface SVGFEMorphologyElement extends SVGElement, SVGFilterPrimitiveStandardAttributes {
   short SVG_MORPHOLOGY_OPERATOR_UNKNOWN = 0;
   short SVG_MORPHOLOGY_OPERATOR_ERODE = 1;
   short SVG_MORPHOLOGY_OPERATOR_DILATE = 2;

   SVGAnimatedString getIn1();

   SVGAnimatedEnumeration getOperator();

   SVGAnimatedNumber getRadiusX();

   SVGAnimatedNumber getRadiusY();
}
