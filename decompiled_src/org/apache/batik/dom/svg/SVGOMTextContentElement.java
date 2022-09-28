package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGStringList;

public abstract class SVGOMTextContentElement extends SVGStylableElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected static final String[] LENGTH_ADJUST_VALUES;
   protected SVGOMAnimatedBoolean externalResourcesRequired;
   protected AbstractSVGAnimatedLength textLength;
   protected SVGOMAnimatedEnumeration lengthAdjust;

   protected SVGOMTextContentElement() {
   }

   protected SVGOMTextContentElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.externalResourcesRequired = this.createLiveAnimatedBoolean((String)null, "externalResourcesRequired", false);
      this.lengthAdjust = this.createLiveAnimatedEnumeration((String)null, "lengthAdjust", LENGTH_ADJUST_VALUES, (short)1);
      this.textLength = new AbstractSVGAnimatedLength(this, (String)null, "textLength", 2, true) {
         boolean usedDefault;

         protected String getDefaultValue() {
            this.usedDefault = true;
            return String.valueOf(SVGOMTextContentElement.this.getComputedTextLength());
         }

         public SVGLength getBaseVal() {
            if (this.baseVal == null) {
               this.baseVal = new null.SVGTextLength(this.direction);
            }

            return this.baseVal;
         }

         class SVGTextLength extends AbstractSVGAnimatedLength.BaseSVGLength {
            public SVGTextLength(short var2) {
               super(var2);
            }

            protected void revalidate() {
               usedDefault = false;
               super.revalidate();
               if (usedDefault) {
                  this.valid = false;
               }

            }
         }
      };
      this.liveAttributeValues.put((Object)null, "textLength", this.textLength);
      this.textLength.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
   }

   public SVGAnimatedLength getTextLength() {
      return this.textLength;
   }

   public SVGAnimatedEnumeration getLengthAdjust() {
      return this.lengthAdjust;
   }

   public int getNumberOfChars() {
      return SVGTextContentSupport.getNumberOfChars(this);
   }

   public float getComputedTextLength() {
      return SVGTextContentSupport.getComputedTextLength(this);
   }

   public float getSubStringLength(int var1, int var2) throws DOMException {
      return SVGTextContentSupport.getSubStringLength(this, var1, var2);
   }

   public SVGPoint getStartPositionOfChar(int var1) throws DOMException {
      return SVGTextContentSupport.getStartPositionOfChar(this, var1);
   }

   public SVGPoint getEndPositionOfChar(int var1) throws DOMException {
      return SVGTextContentSupport.getEndPositionOfChar(this, var1);
   }

   public SVGRect getExtentOfChar(int var1) throws DOMException {
      return SVGTextContentSupport.getExtentOfChar(this, var1);
   }

   public float getRotationOfChar(int var1) throws DOMException {
      return SVGTextContentSupport.getRotationOfChar(this, var1);
   }

   public int getCharNumAtPosition(SVGPoint var1) {
      return SVGTextContentSupport.getCharNumAtPosition(this, var1.getX(), var1.getY());
   }

   public void selectSubString(int var1, int var2) throws DOMException {
      SVGTextContentSupport.selectSubString(this, var1, var2);
   }

   public SVGAnimatedBoolean getExternalResourcesRequired() {
      return this.externalResourcesRequired;
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
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGStylableElement.xmlTraitInformation);
      var0.put((Object)null, "textLength", new TraitInformation(true, 3, (short)3));
      var0.put((Object)null, "lengthAdjust", new TraitInformation(true, 15));
      var0.put((Object)null, "externalResourcesRequired", new TraitInformation(true, 49));
      xmlTraitInformation = var0;
      LENGTH_ADJUST_VALUES = new String[]{"", "spacing", "spacingAndGlyphs"};
   }
}
