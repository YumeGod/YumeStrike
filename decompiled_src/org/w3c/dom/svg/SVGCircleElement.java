package org.w3c.dom.svg;

import org.w3c.dom.events.EventTarget;

public interface SVGCircleElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, SVGTransformable, EventTarget {
   SVGAnimatedLength getCx();

   SVGAnimatedLength getCy();

   SVGAnimatedLength getR();
}
