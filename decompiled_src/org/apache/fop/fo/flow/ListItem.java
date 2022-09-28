package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.BreakPropertySet;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.fo.properties.KeepProperty;
import org.xml.sax.Locator;

public class ListItem extends FObj implements BreakPropertySet {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private CommonMarginBlock commonMarginBlock;
   private int breakAfter;
   private int breakBefore;
   private KeepProperty keepTogether;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;
   private ListItemLabel label = null;
   private ListItemBody body = null;

   public ListItem(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.commonMarginBlock = pList.getMarginBlockProps();
      this.breakAfter = pList.get(58).getEnum();
      this.breakBefore = pList.get(59).getEnum();
      this.keepTogether = pList.get(131).getKeep();
      this.keepWithNext = pList.get(132).getKeep();
      this.keepWithPrevious = pList.get(133).getKeep();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startListItem(this);
   }

   protected void endOfNode() throws FOPException {
      if (this.label == null || this.body == null) {
         this.missingChildElementError("marker* (list-item-label,list-item-body)");
      }

      this.getFOEventHandler().endListItem(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.label != null) {
               this.nodesOutOfOrderError(loc, "fo:marker", "fo:list-item-label");
            }
         } else if (localName.equals("list-item-label")) {
            if (this.label != null) {
               this.tooManyNodesError(loc, "fo:list-item-label");
            }
         } else if (localName.equals("list-item-body")) {
            if (this.label == null) {
               this.nodesOutOfOrderError(loc, "fo:list-item-label", "fo:list-item-body");
            } else if (this.body != null) {
               this.tooManyNodesError(loc, "fo:list-item-body");
            }
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   public void addChildNode(FONode child) {
      int nameId = child.getNameId();
      if (nameId == 43) {
         this.label = (ListItemLabel)child;
      } else if (nameId == 42) {
         this.body = (ListItemBody)child;
      } else if (nameId == 44) {
         this.addMarker((Marker)child);
      }

   }

   public CommonMarginBlock getCommonMarginBlock() {
      return this.commonMarginBlock;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public int getBreakAfter() {
      return this.breakAfter;
   }

   public int getBreakBefore() {
      return this.breakBefore;
   }

   public KeepProperty getKeepWithNext() {
      return this.keepWithNext;
   }

   public KeepProperty getKeepWithPrevious() {
      return this.keepWithPrevious;
   }

   public KeepProperty getKeepTogether() {
      return this.keepTogether;
   }

   public ListItemLabel getLabel() {
      return this.label;
   }

   public ListItemBody getBody() {
      return this.body;
   }

   public String getLocalName() {
      return "list-item";
   }

   public int getNameId() {
      return 41;
   }
}
