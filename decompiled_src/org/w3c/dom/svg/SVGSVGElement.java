package org.w3c.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.DocumentCSS;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.EventTarget;

public interface SVGSVGElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, SVGLocatable, SVGFitToViewBox, SVGZoomAndPan, EventTarget, DocumentEvent, ViewCSS, DocumentCSS {
   SVGAnimatedLength getX();

   SVGAnimatedLength getY();

   SVGAnimatedLength getWidth();

   SVGAnimatedLength getHeight();

   String getContentScriptType();

   void setContentScriptType(String var1) throws DOMException;

   String getContentStyleType();

   void setContentStyleType(String var1) throws DOMException;

   SVGRect getViewport();

   float getPixelUnitToMillimeterX();

   float getPixelUnitToMillimeterY();

   float getScreenPixelToMillimeterX();

   float getScreenPixelToMillimeterY();

   boolean getUseCurrentView();

   void setUseCurrentView(boolean var1) throws DOMException;

   SVGViewSpec getCurrentView();

   float getCurrentScale();

   void setCurrentScale(float var1) throws DOMException;

   SVGPoint getCurrentTranslate();

   int suspendRedraw(int var1);

   void unsuspendRedraw(int var1) throws DOMException;

   void unsuspendRedrawAll();

   void forceRedraw();

   void pauseAnimations();

   void unpauseAnimations();

   boolean animationsPaused();

   float getCurrentTime();

   void setCurrentTime(float var1);

   NodeList getIntersectionList(SVGRect var1, SVGElement var2);

   NodeList getEnclosureList(SVGRect var1, SVGElement var2);

   boolean checkIntersection(SVGElement var1, SVGRect var2);

   boolean checkEnclosure(SVGElement var1, SVGRect var2);

   void deselectAll();

   SVGNumber createSVGNumber();

   SVGLength createSVGLength();

   SVGAngle createSVGAngle();

   SVGPoint createSVGPoint();

   SVGMatrix createSVGMatrix();

   SVGRect createSVGRect();

   SVGTransform createSVGTransform();

   SVGTransform createSVGTransformFromMatrix(SVGMatrix var1);

   Element getElementById(String var1);
}
