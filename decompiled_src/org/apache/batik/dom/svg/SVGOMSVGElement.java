package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import java.util.List;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.ListNodeList;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.DocumentCSS;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.stylesheets.DocumentStyle;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.svg.SVGAngle;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGNumber;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.svg.SVGStringList;
import org.w3c.dom.svg.SVGTransform;
import org.w3c.dom.svg.SVGViewSpec;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class SVGOMSVGElement extends SVGStylableElement implements SVGSVGElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final AttributeInitializer attributeInitializer;
   protected SVGOMAnimatedLength x;
   protected SVGOMAnimatedLength y;
   protected SVGOMAnimatedLength width;
   protected SVGOMAnimatedLength height;
   protected SVGOMAnimatedBoolean externalResourcesRequired;
   protected SVGOMAnimatedPreserveAspectRatio preserveAspectRatio;
   protected SVGOMAnimatedRect viewBox;

   protected SVGOMSVGElement() {
   }

   public SVGOMSVGElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.x = this.createLiveAnimatedLength((String)null, "x", "0", (short)2, false);
      this.y = this.createLiveAnimatedLength((String)null, "y", "0", (short)1, false);
      this.width = this.createLiveAnimatedLength((String)null, "width", "100%", (short)2, true);
      this.height = this.createLiveAnimatedLength((String)null, "height", "100%", (short)1, true);
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
      this.preserveAspectRatio = this.createLiveAnimatedPreserveAspectRatio();
      this.viewBox = this.createLiveAnimatedRect((String)null, "viewBox", (String)null);
   }

   public String getLocalName() {
      return "svg";
   }

   public SVGAnimatedLength getX() {
      return this.x;
   }

   public SVGAnimatedLength getY() {
      return this.y;
   }

   public SVGAnimatedLength getWidth() {
      return this.width;
   }

   public SVGAnimatedLength getHeight() {
      return this.height;
   }

   public String getContentScriptType() {
      return this.getAttributeNS((String)null, "contentScriptType");
   }

   public void setContentScriptType(String var1) {
      this.setAttributeNS((String)null, "contentScriptType", var1);
   }

   public String getContentStyleType() {
      return this.getAttributeNS((String)null, "contentStyleType");
   }

   public void setContentStyleType(String var1) {
      this.setAttributeNS((String)null, "contentStyleType", var1);
   }

   public SVGRect getViewport() {
      SVGContext var1 = this.getSVGContext();
      return new SVGOMRect(0.0F, 0.0F, var1.getViewportWidth(), var1.getViewportHeight());
   }

   public float getPixelUnitToMillimeterX() {
      return this.getSVGContext().getPixelUnitToMillimeter();
   }

   public float getPixelUnitToMillimeterY() {
      return this.getSVGContext().getPixelUnitToMillimeter();
   }

   public float getScreenPixelToMillimeterX() {
      return this.getSVGContext().getPixelUnitToMillimeter();
   }

   public float getScreenPixelToMillimeterY() {
      return this.getSVGContext().getPixelUnitToMillimeter();
   }

   public boolean getUseCurrentView() {
      throw new UnsupportedOperationException("SVGSVGElement.getUseCurrentView is not implemented");
   }

   public void setUseCurrentView(boolean var1) throws DOMException {
      throw new UnsupportedOperationException("SVGSVGElement.setUseCurrentView is not implemented");
   }

   public SVGViewSpec getCurrentView() {
      throw new UnsupportedOperationException("SVGSVGElement.getCurrentView is not implemented");
   }

   public float getCurrentScale() {
      AffineTransform var1 = this.getSVGContext().getScreenTransform();
      return var1 != null ? (float)Math.sqrt(var1.getDeterminant()) : 1.0F;
   }

   public void setCurrentScale(float var1) throws DOMException {
      SVGContext var2 = this.getSVGContext();
      AffineTransform var3 = var2.getScreenTransform();
      float var4 = 1.0F;
      if (var3 != null) {
         var4 = (float)Math.sqrt(var3.getDeterminant());
      }

      float var5 = var1 / var4;
      var3 = new AffineTransform(var3.getScaleX() * (double)var5, var3.getShearY() * (double)var5, var3.getShearX() * (double)var5, var3.getScaleY() * (double)var5, var3.getTranslateX(), var3.getTranslateY());
      var2.setScreenTransform(var3);
   }

   public SVGPoint getCurrentTranslate() {
      return new SVGPoint() {
         protected AffineTransform getScreenTransform() {
            SVGContext var1 = SVGOMSVGElement.this.getSVGContext();
            return var1.getScreenTransform();
         }

         public float getX() {
            AffineTransform var1 = this.getScreenTransform();
            return (float)var1.getTranslateX();
         }

         public float getY() {
            AffineTransform var1 = this.getScreenTransform();
            return (float)var1.getTranslateY();
         }

         public void setX(float var1) {
            SVGContext var2 = SVGOMSVGElement.this.getSVGContext();
            AffineTransform var3 = var2.getScreenTransform();
            var3 = new AffineTransform(var3.getScaleX(), var3.getShearY(), var3.getShearX(), var3.getScaleY(), (double)var1, var3.getTranslateY());
            var2.setScreenTransform(var3);
         }

         public void setY(float var1) {
            SVGContext var2 = SVGOMSVGElement.this.getSVGContext();
            AffineTransform var3 = var2.getScreenTransform();
            var3 = new AffineTransform(var3.getScaleX(), var3.getShearY(), var3.getShearX(), var3.getScaleY(), var3.getTranslateX(), (double)var1);
            var2.setScreenTransform(var3);
         }

         public SVGPoint matrixTransform(SVGMatrix var1) {
            AffineTransform var2 = this.getScreenTransform();
            float var3 = (float)var2.getTranslateX();
            float var4 = (float)var2.getTranslateY();
            float var5 = var1.getA() * var3 + var1.getC() * var4 + var1.getE();
            float var6 = var1.getB() * var3 + var1.getD() * var4 + var1.getF();
            return new SVGOMPoint(var5, var6);
         }
      };
   }

   public int suspendRedraw(int var1) {
      if (var1 > 60000) {
         var1 = 60000;
      } else if (var1 < 0) {
         var1 = 0;
      }

      SVGSVGContext var2 = (SVGSVGContext)this.getSVGContext();
      return var2.suspendRedraw(var1);
   }

   public void unsuspendRedraw(int var1) throws DOMException {
      SVGSVGContext var2 = (SVGSVGContext)this.getSVGContext();
      if (!var2.unsuspendRedraw(var1)) {
         throw this.createDOMException((short)8, "invalid.suspend.handle", new Object[]{new Integer(var1)});
      }
   }

   public void unsuspendRedrawAll() {
      SVGSVGContext var1 = (SVGSVGContext)this.getSVGContext();
      var1.unsuspendRedrawAll();
   }

   public void forceRedraw() {
      SVGSVGContext var1 = (SVGSVGContext)this.getSVGContext();
      var1.forceRedraw();
   }

   public void pauseAnimations() {
      SVGSVGContext var1 = (SVGSVGContext)this.getSVGContext();
      var1.pauseAnimations();
   }

   public void unpauseAnimations() {
      SVGSVGContext var1 = (SVGSVGContext)this.getSVGContext();
      var1.unpauseAnimations();
   }

   public boolean animationsPaused() {
      SVGSVGContext var1 = (SVGSVGContext)this.getSVGContext();
      return var1.animationsPaused();
   }

   public float getCurrentTime() {
      SVGSVGContext var1 = (SVGSVGContext)this.getSVGContext();
      return var1.getCurrentTime();
   }

   public void setCurrentTime(float var1) {
      SVGSVGContext var2 = (SVGSVGContext)this.getSVGContext();
      var2.setCurrentTime(var1);
   }

   public NodeList getIntersectionList(SVGRect var1, SVGElement var2) {
      SVGSVGContext var3 = (SVGSVGContext)this.getSVGContext();
      List var4 = var3.getIntersectionList(var1, var2);
      return new ListNodeList(var4);
   }

   public NodeList getEnclosureList(SVGRect var1, SVGElement var2) {
      SVGSVGContext var3 = (SVGSVGContext)this.getSVGContext();
      List var4 = var3.getEnclosureList(var1, var2);
      return new ListNodeList(var4);
   }

   public boolean checkIntersection(SVGElement var1, SVGRect var2) {
      SVGSVGContext var3 = (SVGSVGContext)this.getSVGContext();
      return var3.checkIntersection(var1, var2);
   }

   public boolean checkEnclosure(SVGElement var1, SVGRect var2) {
      SVGSVGContext var3 = (SVGSVGContext)this.getSVGContext();
      return var3.checkEnclosure(var1, var2);
   }

   public void deselectAll() {
      ((SVGSVGContext)this.getSVGContext()).deselectAll();
   }

   public SVGNumber createSVGNumber() {
      return new SVGNumber() {
         protected float value;

         public float getValue() {
            return this.value;
         }

         public void setValue(float var1) {
            this.value = var1;
         }
      };
   }

   public SVGLength createSVGLength() {
      return new SVGOMLength(this);
   }

   public SVGAngle createSVGAngle() {
      return new SVGOMAngle();
   }

   public SVGPoint createSVGPoint() {
      return new SVGOMPoint(0.0F, 0.0F);
   }

   public SVGMatrix createSVGMatrix() {
      return new AbstractSVGMatrix() {
         protected AffineTransform at = new AffineTransform();

         protected AffineTransform getAffineTransform() {
            return this.at;
         }
      };
   }

   public SVGRect createSVGRect() {
      return new SVGOMRect(0.0F, 0.0F, 0.0F, 0.0F);
   }

   public SVGTransform createSVGTransform() {
      SVGOMTransform var1 = new SVGOMTransform();
      var1.setType((short)1);
      return var1;
   }

   public SVGTransform createSVGTransformFromMatrix(SVGMatrix var1) {
      SVGOMTransform var2 = new SVGOMTransform();
      var2.setMatrix(var1);
      return var2;
   }

   public Element getElementById(String var1) {
      return this.ownerDocument.getChildElementById(this, var1);
   }

   public SVGElement getNearestViewportElement() {
      return SVGLocatableSupport.getNearestViewportElement(this);
   }

   public SVGElement getFarthestViewportElement() {
      return SVGLocatableSupport.getFarthestViewportElement(this);
   }

   public SVGRect getBBox() {
      return SVGLocatableSupport.getBBox(this);
   }

   public SVGMatrix getCTM() {
      return SVGLocatableSupport.getCTM(this);
   }

   public SVGMatrix getScreenCTM() {
      return SVGLocatableSupport.getScreenCTM(this);
   }

   public SVGMatrix getTransformToElement(SVGElement var1) throws SVGException {
      return SVGLocatableSupport.getTransformToElement(this, var1);
   }

   public DocumentView getDocument() {
      return (DocumentView)this.getOwnerDocument();
   }

   public CSSStyleDeclaration getComputedStyle(Element var1, String var2) {
      AbstractView var3 = ((DocumentView)this.getOwnerDocument()).getDefaultView();
      return ((ViewCSS)var3).getComputedStyle(var1, var2);
   }

   public Event createEvent(String var1) throws DOMException {
      return ((DocumentEvent)this.getOwnerDocument()).createEvent(var1);
   }

   public boolean canDispatch(String var1, String var2) throws DOMException {
      AbstractDocument var3 = (AbstractDocument)this.getOwnerDocument();
      return var3.canDispatch(var1, var2);
   }

   public StyleSheetList getStyleSheets() {
      return ((DocumentStyle)this.getOwnerDocument()).getStyleSheets();
   }

   public CSSStyleDeclaration getOverrideStyle(Element var1, String var2) {
      return ((DocumentCSS)this.getOwnerDocument()).getOverrideStyle(var1, var2);
   }

   public String getXMLlang() {
      return XMLSupport.getXMLLang(this);
   }

   public void setXMLlang(String var1) {
      this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", var1);
   }

   public String getXMLspace() {
      return XMLSupport.getXMLSpace(this);
   }

   public void setXMLspace(String var1) {
      this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:space", var1);
   }

   public short getZoomAndPan() {
      return SVGZoomAndPanSupport.getZoomAndPan(this);
   }

   public void setZoomAndPan(short var1) {
      SVGZoomAndPanSupport.setZoomAndPan(this, var1);
   }

   public SVGAnimatedRect getViewBox() {
      return this.viewBox;
   }

   public SVGAnimatedPreserveAspectRatio getPreserveAspectRatio() {
      return this.preserveAspectRatio;
   }

   public SVGAnimatedBoolean getExternalResourcesRequired() {
      return this.externalResourcesRequired;
   }

   public SVGStringList getRequiredFeatures() {
      return SVGTestsSupport.getRequiredFeatures(this);
   }

   public SVGStringList getRequiredExtensions() {
      return SVGTestsSupport.getRequiredExtensions(this);
   }

   public SVGStringList getSystemLanguage() {
      return SVGTestsSupport.getSystemLanguage(this);
   }

   public boolean hasExtension(String var1) {
      return SVGTestsSupport.hasExtension(this, var1);
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMSVGElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "width", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "height", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "preserveAspectRatio", new TraitInformation(true, 32));
      var0.put((Object)null, "viewBox", new TraitInformation(true, 50));
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      xmlTraitInformation = var0;
      attributeInitializer = new AttributeInitializer(7);
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", (String)null, "xmlns", "http://www.w3.org/2000/svg");
      attributeInitializer.addAttribute("http://www.w3.org/2000/xmlns/", "xmlns", "xlink", "http://www.w3.org/1999/xlink");
      attributeInitializer.addAttribute((String)null, (String)null, "preserveAspectRatio", "xMidYMid meet");
      attributeInitializer.addAttribute((String)null, (String)null, "zoomAndPan", "magnify");
      attributeInitializer.addAttribute((String)null, (String)null, "version", "1.0");
      attributeInitializer.addAttribute((String)null, (String)null, "contentScriptType", "text/ecmascript");
      attributeInitializer.addAttribute((String)null, (String)null, "contentStyleType", "text/css");
   }
}
