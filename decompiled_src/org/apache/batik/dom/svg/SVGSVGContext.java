package org.apache.batik.dom.svg;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGRect;

public interface SVGSVGContext extends SVGContext {
   List getIntersectionList(SVGRect var1, Element var2);

   List getEnclosureList(SVGRect var1, Element var2);

   boolean checkIntersection(Element var1, SVGRect var2);

   boolean checkEnclosure(Element var1, SVGRect var2);

   void deselectAll();

   int suspendRedraw(int var1);

   boolean unsuspendRedraw(int var1);

   void unsuspendRedrawAll();

   void forceRedraw();

   void pauseAnimations();

   void unpauseAnimations();

   boolean animationsPaused();

   float getCurrentTime();

   void setCurrentTime(float var1);
}
