package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import org.apache.batik.anim.values.AnimatableMotionPointValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedTransformList;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGTextElement;

public class SVGOMTextElement extends SVGOMTextPositioningElement implements SVGTextElement, SVGMotionAnimatableElement {
   protected static final String X_DEFAULT_VALUE = "0";
   protected static final String Y_DEFAULT_VALUE = "0";
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedTransformList transform;
   protected AffineTransform motionTransform;

   protected SVGOMTextElement() {
   }

   public SVGOMTextElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.transform = this.createLiveAnimatedTransformList((String)null, "transform", "");
   }

   public String getLocalName() {
      return "text";
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

   public SVGAnimatedTransformList getTransform() {
      return this.transform;
   }

   protected String getDefaultXValue() {
      return "0";
   }

   protected String getDefaultYValue() {
      return "0";
   }

   protected Node newNode() {
      return new SVGOMTextElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   public AffineTransform getMotionTransform() {
      return this.motionTransform;
   }

   public void updateOtherValue(String var1, AnimatableValue var2) {
      if (var1.equals("motion")) {
         if (this.motionTransform == null) {
            this.motionTransform = new AffineTransform();
         }

         if (var2 == null) {
            this.motionTransform.setToIdentity();
         } else {
            AnimatableMotionPointValue var3 = (AnimatableMotionPointValue)var2;
            this.motionTransform.setToTranslation((double)var3.getX(), (double)var3.getY());
            this.motionTransform.rotate((double)var3.getAngle());
         }

         SVGOMDocument var4 = (SVGOMDocument)this.ownerDocument;
         var4.getAnimatedAttributeListener().otherAnimationChanged(this, var1);
      } else {
         super.updateOtherValue(var1, var2);
      }

   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMTextPositioningElement.xmlTraitInformation);
      var0.put((Object)null, "transform", new TraitInformation(true, 9));
      xmlTraitInformation = var0;
   }
}
