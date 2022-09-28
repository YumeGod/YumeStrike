package org.apache.batik.dom.svg;

import java.util.Iterator;
import java.util.LinkedList;
import org.apache.batik.anim.values.AnimatableNumberOptionalNumberValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSNavigableNode;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.anim.AnimationTargetListener;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.parser.UnitProcessor;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedInteger;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGFitToViewBox;
import org.w3c.dom.svg.SVGSVGElement;

public abstract class SVGOMElement extends AbstractElement implements SVGElement, ExtendedTraitAccess, AnimationTarget {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected transient boolean readonly;
   protected String prefix;
   protected transient SVGContext svgContext;
   protected DoublyIndexedTable targetListeners;
   protected UnitProcessor.Context unitContext;

   protected SVGOMElement() {
   }

   protected SVGOMElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   protected void initializeAllLiveAttributes() {
   }

   public String getId() {
      if (((SVGOMDocument)this.ownerDocument).isSVG12) {
         Attr var1 = this.getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "id");
         if (var1 != null) {
            return var1.getNodeValue();
         }
      }

      return this.getAttributeNS((String)null, "id");
   }

   public void setId(String var1) {
      if (((SVGOMDocument)this.ownerDocument).isSVG12) {
         this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "id", var1);
         Attr var2 = this.getAttributeNodeNS((String)null, "id");
         if (var2 != null) {
            var2.setNodeValue(var1);
         }
      } else {
         this.setAttributeNS((String)null, "id", var1);
      }

   }

   public String getXMLbase() {
      return this.getAttributeNS("http://www.w3.org/XML/1998/namespace", "base");
   }

   public void setXMLbase(String var1) throws DOMException {
      this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", var1);
   }

   public SVGSVGElement getOwnerSVGElement() {
      for(CSSStylableElement var1 = CSSEngine.getParentCSSStylableElement(this); var1 != null; var1 = CSSEngine.getParentCSSStylableElement(var1)) {
         if (var1 instanceof SVGSVGElement) {
            return (SVGSVGElement)var1;
         }
      }

      return null;
   }

   public SVGElement getViewportElement() {
      for(CSSStylableElement var1 = CSSEngine.getParentCSSStylableElement(this); var1 != null; var1 = CSSEngine.getParentCSSStylableElement(var1)) {
         if (var1 instanceof SVGFitToViewBox) {
            return (SVGElement)var1;
         }
      }

      return null;
   }

   public String getNodeName() {
      return this.prefix != null && !this.prefix.equals("") ? this.prefix + ':' + this.getLocalName() : this.getLocalName();
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2000/svg";
   }

   public void setPrefix(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else if (var1 != null && !var1.equals("") && !DOMUtilities.isValidName(var1)) {
         throw this.createDOMException((short)5, "prefix", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var1});
      } else {
         this.prefix = var1;
      }
   }

   protected String getCascadedXMLBase(Node var1) {
      String var2 = null;
      Node var3 = var1.getParentNode();

      while(var3 != null) {
         if (var3.getNodeType() == 1) {
            var2 = this.getCascadedXMLBase(var3);
            break;
         }

         if (var3 instanceof CSSNavigableNode) {
            var3 = ((CSSNavigableNode)var3).getCSSParentNode();
         } else {
            var3 = var3.getParentNode();
         }
      }

      if (var2 == null) {
         AbstractDocument var4;
         if (var1.getNodeType() == 9) {
            var4 = (AbstractDocument)var1;
         } else {
            var4 = (AbstractDocument)var1.getOwnerDocument();
         }

         var2 = var4.getDocumentURI();
      }

      while(var1 != null && var1.getNodeType() != 1) {
         var1 = var1.getParentNode();
      }

      if (var1 == null) {
         return var2;
      } else {
         Element var6 = (Element)var1;
         Attr var5 = var6.getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "base");
         if (var5 != null) {
            if (var2 == null) {
               var2 = var5.getNodeValue();
            } else {
               var2 = (new ParsedURL(var2, var5.getNodeValue())).toString();
            }
         }

         return var2;
      }
   }

   public void setSVGContext(SVGContext var1) {
      this.svgContext = var1;
   }

   public SVGContext getSVGContext() {
      return this.svgContext;
   }

   public SVGException createSVGException(short var1, String var2, Object[] var3) {
      try {
         return new SVGOMException(var1, this.getCurrentDocument().formatMessage(var2, var3));
      } catch (Exception var5) {
         return new SVGOMException(var1, var2);
      }
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   protected SVGOMAnimatedTransformList createLiveAnimatedTransformList(String var1, String var2, String var3) {
      SVGOMAnimatedTransformList var4 = new SVGOMAnimatedTransformList(this, var1, var2, var3);
      this.liveAttributeValues.put(var1, var2, var4);
      var4.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var4;
   }

   protected SVGOMAnimatedBoolean createLiveAnimatedBoolean(String var1, String var2, boolean var3) {
      SVGOMAnimatedBoolean var4 = new SVGOMAnimatedBoolean(this, var1, var2, var3);
      this.liveAttributeValues.put(var1, var2, var4);
      var4.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var4;
   }

   protected SVGOMAnimatedString createLiveAnimatedString(String var1, String var2) {
      SVGOMAnimatedString var3 = new SVGOMAnimatedString(this, var1, var2);
      this.liveAttributeValues.put(var1, var2, var3);
      var3.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var3;
   }

   protected SVGOMAnimatedPreserveAspectRatio createLiveAnimatedPreserveAspectRatio() {
      SVGOMAnimatedPreserveAspectRatio var1 = new SVGOMAnimatedPreserveAspectRatio(this);
      this.liveAttributeValues.put((Object)null, "preserveAspectRatio", var1);
      var1.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var1;
   }

   protected SVGOMAnimatedMarkerOrientValue createLiveAnimatedMarkerOrientValue(String var1, String var2) {
      SVGOMAnimatedMarkerOrientValue var3 = new SVGOMAnimatedMarkerOrientValue(this, var1, var2);
      this.liveAttributeValues.put(var1, var2, var3);
      var3.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var3;
   }

   protected SVGOMAnimatedPathData createLiveAnimatedPathData(String var1, String var2, String var3) {
      SVGOMAnimatedPathData var4 = new SVGOMAnimatedPathData(this, var1, var2, var3);
      this.liveAttributeValues.put(var1, var2, var4);
      var4.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var4;
   }

   protected SVGOMAnimatedNumber createLiveAnimatedNumber(String var1, String var2, float var3) {
      return this.createLiveAnimatedNumber(var1, var2, var3, false);
   }

   protected SVGOMAnimatedNumber createLiveAnimatedNumber(String var1, String var2, float var3, boolean var4) {
      SVGOMAnimatedNumber var5 = new SVGOMAnimatedNumber(this, var1, var2, var3, var4);
      this.liveAttributeValues.put(var1, var2, var5);
      var5.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var5;
   }

   protected SVGOMAnimatedNumberList createLiveAnimatedNumberList(String var1, String var2, String var3, boolean var4) {
      SVGOMAnimatedNumberList var5 = new SVGOMAnimatedNumberList(this, var1, var2, var3, var4);
      this.liveAttributeValues.put(var1, var2, var5);
      var5.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var5;
   }

   protected SVGOMAnimatedPoints createLiveAnimatedPoints(String var1, String var2, String var3) {
      SVGOMAnimatedPoints var4 = new SVGOMAnimatedPoints(this, var1, var2, var3);
      this.liveAttributeValues.put(var1, var2, var4);
      var4.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var4;
   }

   protected SVGOMAnimatedLengthList createLiveAnimatedLengthList(String var1, String var2, String var3, boolean var4, short var5) {
      SVGOMAnimatedLengthList var6 = new SVGOMAnimatedLengthList(this, var1, var2, var3, var4, var5);
      this.liveAttributeValues.put(var1, var2, var6);
      var6.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var6;
   }

   protected SVGOMAnimatedInteger createLiveAnimatedInteger(String var1, String var2, int var3) {
      SVGOMAnimatedInteger var4 = new SVGOMAnimatedInteger(this, var1, var2, var3);
      this.liveAttributeValues.put(var1, var2, var4);
      var4.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var4;
   }

   protected SVGOMAnimatedEnumeration createLiveAnimatedEnumeration(String var1, String var2, String[] var3, short var4) {
      SVGOMAnimatedEnumeration var5 = new SVGOMAnimatedEnumeration(this, var1, var2, var3, var4);
      this.liveAttributeValues.put(var1, var2, var5);
      var5.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var5;
   }

   protected SVGOMAnimatedLength createLiveAnimatedLength(String var1, String var2, String var3, short var4, boolean var5) {
      SVGOMAnimatedLength var6 = new SVGOMAnimatedLength(this, var1, var2, var3, var4, var5);
      this.liveAttributeValues.put(var1, var2, var6);
      var6.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var6;
   }

   protected SVGOMAnimatedRect createLiveAnimatedRect(String var1, String var2, String var3) {
      SVGOMAnimatedRect var4 = new SVGOMAnimatedRect(this, var1, var2, var3);
      this.liveAttributeValues.put(var1, var2, var4);
      var4.addAnimatedAttributeListener(((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener());
      return var4;
   }

   public boolean hasProperty(String var1) {
      AbstractStylableDocument var2 = (AbstractStylableDocument)this.ownerDocument;
      CSSEngine var3 = var2.getCSSEngine();
      return var3.getPropertyIndex(var1) != -1 || var3.getShorthandIndex(var1) != -1;
   }

   public boolean hasTrait(String var1, String var2) {
      return false;
   }

   public boolean isPropertyAnimatable(String var1) {
      AbstractStylableDocument var2 = (AbstractStylableDocument)this.ownerDocument;
      CSSEngine var3 = var2.getCSSEngine();
      int var4 = var3.getPropertyIndex(var1);
      if (var4 != -1) {
         ValueManager[] var6 = var3.getValueManagers();
         return var6[var4].isAnimatableProperty();
      } else {
         var4 = var3.getShorthandIndex(var1);
         if (var4 != -1) {
            ShorthandManager[] var5 = var3.getShorthandManagers();
            return var5[var4].isAnimatableProperty();
         } else {
            return false;
         }
      }
   }

   public final boolean isAttributeAnimatable(String var1, String var2) {
      DoublyIndexedTable var3 = this.getTraitInformationTable();
      TraitInformation var4 = (TraitInformation)var3.get(var1, var2);
      return var4 != null ? var4.isAnimatable() : false;
   }

   public boolean isPropertyAdditive(String var1) {
      AbstractStylableDocument var2 = (AbstractStylableDocument)this.ownerDocument;
      CSSEngine var3 = var2.getCSSEngine();
      int var4 = var3.getPropertyIndex(var1);
      if (var4 != -1) {
         ValueManager[] var6 = var3.getValueManagers();
         return var6[var4].isAdditiveProperty();
      } else {
         var4 = var3.getShorthandIndex(var1);
         if (var4 != -1) {
            ShorthandManager[] var5 = var3.getShorthandManagers();
            return var5[var4].isAdditiveProperty();
         } else {
            return false;
         }
      }
   }

   public boolean isAttributeAdditive(String var1, String var2) {
      return true;
   }

   public boolean isTraitAnimatable(String var1, String var2) {
      return false;
   }

   public boolean isTraitAdditive(String var1, String var2) {
      return false;
   }

   public int getPropertyType(String var1) {
      AbstractStylableDocument var2 = (AbstractStylableDocument)this.ownerDocument;
      CSSEngine var3 = var2.getCSSEngine();
      int var4 = var3.getPropertyIndex(var1);
      if (var4 != -1) {
         ValueManager[] var5 = var3.getValueManagers();
         return var5[var4].getPropertyType();
      } else {
         return 0;
      }
   }

   public final int getAttributeType(String var1, String var2) {
      DoublyIndexedTable var3 = this.getTraitInformationTable();
      TraitInformation var4 = (TraitInformation)var3.get(var1, var2);
      return var4 != null ? var4.getType() : 0;
   }

   public Element getElement() {
      return this;
   }

   public void updatePropertyValue(String var1, AnimatableValue var2) {
   }

   public void updateAttributeValue(String var1, String var2, AnimatableValue var3) {
      LiveAttributeValue var4 = this.getLiveAttributeValue(var1, var2);
      ((AbstractSVGAnimatedValue)var4).updateAnimatedValue(var3);
   }

   public void updateOtherValue(String var1, AnimatableValue var2) {
   }

   public AnimatableValue getUnderlyingValue(String var1, String var2) {
      LiveAttributeValue var3 = this.getLiveAttributeValue(var1, var2);
      return !(var3 instanceof AnimatedLiveAttributeValue) ? null : ((AnimatedLiveAttributeValue)var3).getUnderlyingValue(this);
   }

   protected AnimatableValue getBaseValue(SVGAnimatedInteger var1, SVGAnimatedInteger var2) {
      return new AnimatableNumberOptionalNumberValue(this, (float)var1.getBaseVal(), (float)var2.getBaseVal());
   }

   protected AnimatableValue getBaseValue(SVGAnimatedNumber var1, SVGAnimatedNumber var2) {
      return new AnimatableNumberOptionalNumberValue(this, var1.getBaseVal(), var2.getBaseVal());
   }

   public short getPercentageInterpretation(String var1, String var2, boolean var3) {
      if ((var3 || var1 == null) && (var2.equals("baseline-shift") || var2.equals("font-size"))) {
         return 0;
      } else if (!var3) {
         DoublyIndexedTable var4 = this.getTraitInformationTable();
         TraitInformation var5 = (TraitInformation)var4.get(var1, var2);
         return var5 != null ? var5.getPercentageInterpretation() : 3;
      } else {
         return 3;
      }
   }

   protected final short getAttributePercentageInterpretation(String var1, String var2) {
      return 3;
   }

   public boolean useLinearRGBColorInterpolation() {
      return false;
   }

   public float svgToUserSpace(float var1, short var2, short var3) {
      if (this.unitContext == null) {
         this.unitContext = new UnitContext();
      }

      return var3 == 0 && var2 == 2 ? 0.0F : UnitProcessor.svgToUserSpace(var1, var2, (short)(3 - var3), this.unitContext);
   }

   public void addTargetListener(String var1, String var2, boolean var3, AnimationTargetListener var4) {
      if (!var3) {
         if (this.targetListeners == null) {
            this.targetListeners = new DoublyIndexedTable();
         }

         LinkedList var5 = (LinkedList)this.targetListeners.get(var1, var2);
         if (var5 == null) {
            var5 = new LinkedList();
            this.targetListeners.put(var1, var2, var5);
         }

         var5.add(var4);
      }

   }

   public void removeTargetListener(String var1, String var2, boolean var3, AnimationTargetListener var4) {
      if (!var3) {
         LinkedList var5 = (LinkedList)this.targetListeners.get(var1, var2);
         var5.remove(var4);
      }

   }

   void fireBaseAttributeListeners(String var1, String var2) {
      if (this.targetListeners != null) {
         LinkedList var3 = (LinkedList)this.targetListeners.get(var1, var2);
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            AnimationTargetListener var5 = (AnimationTargetListener)var4.next();
            var5.baseValueChanged(this, var1, var2, false);
         }
      }

   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      SVGOMElement var3 = (SVGOMElement)var1;
      var3.prefix = this.prefix;
      var3.initializeAllLiveAttributes();
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      SVGOMElement var3 = (SVGOMElement)var1;
      var3.prefix = this.prefix;
      var3.initializeAllLiveAttributes();
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      SVGOMElement var2 = (SVGOMElement)var1;
      var2.prefix = this.prefix;
      var2.initializeAllLiveAttributes();
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      SVGOMElement var2 = (SVGOMElement)var1;
      var2.prefix = this.prefix;
      var2.initializeAllLiveAttributes();
      return var1;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable();
      var0.put((Object)null, "id", new TraitInformation(false, 16));
      var0.put("http://www.w3.org/XML/1998/namespace", "base", new TraitInformation(false, 10));
      var0.put("http://www.w3.org/XML/1998/namespace", "space", new TraitInformation(false, 15));
      var0.put("http://www.w3.org/XML/1998/namespace", "id", new TraitInformation(false, 16));
      var0.put("http://www.w3.org/XML/1998/namespace", "lang", new TraitInformation(false, 45));
      xmlTraitInformation = var0;
   }

   protected class UnitContext implements UnitProcessor.Context {
      public Element getElement() {
         return SVGOMElement.this;
      }

      public float getPixelUnitToMillimeter() {
         return SVGOMElement.this.getSVGContext().getPixelUnitToMillimeter();
      }

      public float getPixelToMM() {
         return this.getPixelUnitToMillimeter();
      }

      public float getFontSize() {
         return SVGOMElement.this.getSVGContext().getFontSize();
      }

      public float getXHeight() {
         return 0.5F;
      }

      public float getViewportWidth() {
         return SVGOMElement.this.getSVGContext().getViewportWidth();
      }

      public float getViewportHeight() {
         return SVGOMElement.this.getSVGContext().getViewportHeight();
      }
   }
}
