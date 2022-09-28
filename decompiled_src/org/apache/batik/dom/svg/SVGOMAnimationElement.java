package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimationElement;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGStringList;

public abstract class SVGOMAnimationElement extends SVGOMElement implements SVGAnimationElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedBoolean externalResourcesRequired;

   protected SVGOMAnimationElement() {
   }

   protected SVGOMAnimationElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
   }

   public SVGElement getTargetElement() {
      return ((SVGAnimationContext)this.getSVGContext()).getTargetElement();
   }

   public float getStartTime() {
      return ((SVGAnimationContext)this.getSVGContext()).getStartTime();
   }

   public float getCurrentTime() {
      return ((SVGAnimationContext)this.getSVGContext()).getCurrentTime();
   }

   public float getSimpleDuration() throws DOMException {
      float var1 = ((SVGAnimationContext)this.getSVGContext()).getSimpleDuration();
      if (var1 == Float.POSITIVE_INFINITY) {
         throw this.createDOMException((short)9, "animation.dur.indefinite", (Object[])null);
      } else {
         return var1;
      }
   }

   public float getHyperlinkBeginTime() {
      return ((SVGAnimationContext)this.getSVGContext()).getHyperlinkBeginTime();
   }

   public boolean beginElement() throws DOMException {
      return ((SVGAnimationContext)this.getSVGContext()).beginElement();
   }

   public boolean beginElementAt(float var1) throws DOMException {
      return ((SVGAnimationContext)this.getSVGContext()).beginElementAt(var1);
   }

   public boolean endElement() throws DOMException {
      return ((SVGAnimationContext)this.getSVGContext()).endElement();
   }

   public boolean endElementAt(float var1) throws DOMException {
      return ((SVGAnimationContext)this.getSVGContext()).endElementAt(var1);
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

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMElement.xmlTraitInformation);
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      xmlTraitInformation = var0;
   }
}
