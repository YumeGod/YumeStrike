package org.w3c.dom.svg;

import org.w3c.dom.events.EventTarget;

public interface SVGEllipseElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, SVGTransformable, EventTarget {
   SVGAnimatedLength getCx();

   SVGAnimatedLength getCy();

   SVGAnimatedLength getRx();

   SVGAnimatedLength getRy();
}
