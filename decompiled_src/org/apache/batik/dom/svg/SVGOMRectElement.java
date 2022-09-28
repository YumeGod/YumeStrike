package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGRectElement;

public class SVGOMRectElement extends SVGGraphicsElement implements SVGRectElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedLength x;
   protected SVGOMAnimatedLength y;
   protected AbstractSVGAnimatedLength rx;
   protected AbstractSVGAnimatedLength ry;
   protected SVGOMAnimatedLength width;
   protected SVGOMAnimatedLength height;

   protected SVGOMRectElement() {
   }

   public SVGOMRectElement(String var1, AbstractDocument var2) {
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
      this.width = this.createLiveAnimatedLength((String)null, "width", (String)null, (short)2, true);
      this.height = this.createLiveAnimatedLength((String)null, "height", (String)null, (short)1, true);
      this.rx = new AbstractSVGAnimatedLength(this, (String)null, "rx", 2, true) {
         protected String getDefaultValue() {
            Attr var1 = SVGOMRectElement.this.getAttributeNodeNS((String)null, "ry");
            return var1 == null ? "0" : var1.getValue();
         }

         protected void attrChanged() {
            super.attrChanged();
            AbstractSVGAnimatedLength var1 = (AbstractSVGAnimatedLength)SVGOMRectElement.this.getRy();
            if (this.isSpecified() && !var1.isSpecified()) {
               var1.attrChanged();
            }

         }
      };
      this.ry = new AbstractSVGAnimatedLength(this, (String)null, "ry", 1, true) {
         protected String getDefaultValue() {
            Attr var1 = SVGOMRectElement.this.getAttributeNodeNS((String)null, "rx");
            return var1 == null ? "0" : var1.getValue();
         }

         protected void attrChanged() {
            super.attrChanged();
            AbstractSVGAnimatedLength var1 = (AbstractSVGAnimatedLength)SVGOMRectElement.this.getRx();
            if (this.isSpecified() && !var1.isSpecified()) {
               var1.attrChanged();
            }

         }
      };
      this.liveAttributeValues.put((Object)null, "rx", this.rx);
      this.liveAttributeValues.put((Object)null, "ry", this.ry);
      AnimatedAttributeListener var1 = ((SVGOMDocument)this.ownerDocument).getAnimatedAttributeListener();
      this.rx.addAnimatedAttributeListener(var1);
      this.ry.addAnimatedAttributeListener(var1);
   }

   public String getLocalName() {
      return "rect";
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

   public SVGAnimatedLength getRx() {
      return this.rx;
   }

   public SVGAnimatedLength getRy() {
      return this.ry;
   }

   protected Node newNode() {
      return new SVGOMRectElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   public void updateAttributeValue(String var1, String var2, AnimatableValue var3) {
      if (var1 == null) {
         AbstractSVGAnimatedLength var4;
         if (var2.equals("rx")) {
            super.updateAttributeValue(var1, var2, var3);
            var4 = (AbstractSVGAnimatedLength)this.getRy();
            if (!var4.isSpecified()) {
               super.updateAttributeValue(var1, "ry", var3);
            }

            return;
         }

         if (var2.equals("ry")) {
            super.updateAttributeValue(var1, var2, var3);
            var4 = (AbstractSVGAnimatedLength)this.getRx();
            if (!var4.isSpecified()) {
               super.updateAttributeValue(var1, "rx", var3);
            }

            return;
         }
      }

      super.updateAttributeValue(var1, var2, var3);
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGGraphicsElement.xmlTraitInformation);
      var0.put((Object)null, "x", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "y", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "rx", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "ry", new TraitInformation(true, 3, (short)2));
      var0.put((Object)null, "width", new TraitInformation(true, 3, (short)1));
      var0.put((Object)null, "height", new TraitInformation(true, 3, (short)2));
      xmlTraitInformation = var0;
   }
}
