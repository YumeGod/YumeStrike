package org.apache.fop.render.afp.extensions;

import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPUnitConverter;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.xmlgraphics.util.UnitConv;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class AFPPageOverlayElement extends AbstractAFPExtensionObject {
   private static final String ATT_X = "x";
   private static final String ATT_Y = "y";

   public AFPPageOverlayElement(FONode parent, String name) {
      super(parent, name);
   }

   private AFPPageOverlay getPageSetupAttachment() {
      return (AFPPageOverlay)this.getExtensionAttachment();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      if ("include-page-overlay".equals(this.getLocalName())) {
         if (this.parent.getNameId() != 68 && this.parent.getNameId() != 53) {
            this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfPageSequenceOrSPM");
         }
      } else if (this.parent.getNameId() != 68) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfSPM");
      }

   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
      super.processNode(elementName, locator, attlist, propertyList);
      AFPPageOverlay pageOverlay = this.getPageSetupAttachment();
      if ("include-page-overlay".equals(elementName)) {
         AFPPaintingState paintingState = new AFPPaintingState();
         AFPUnitConverter unitConverter = new AFPUnitConverter(paintingState);
         int x = (int)unitConverter.mpt2units((float)UnitConv.convert(attlist.getValue("x")));
         int y = (int)unitConverter.mpt2units((float)UnitConv.convert(attlist.getValue("y")));
         pageOverlay.setX(x);
         pageOverlay.setY(y);
      }

   }

   protected ExtensionAttachment instantiateExtensionAttachment() {
      return new AFPPageOverlay();
   }
}
