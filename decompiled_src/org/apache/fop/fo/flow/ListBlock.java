package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.BreakPropertySet;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.fo.properties.KeepProperty;
import org.xml.sax.Locator;

public class ListBlock extends FObj implements BreakPropertySet {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private CommonMarginBlock commonMarginBlock;
   private int breakAfter;
   private int breakBefore;
   private KeepProperty keepTogether;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;
   private Length widowContentLimit;
   private Length orphanContentLimit;
   private boolean hasListItem = false;

   public ListBlock(FONode parent) {
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
      this.widowContentLimit = pList.get(271).getLength();
      this.orphanContentLimit = pList.get(272).getLength();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startList(this);
   }

   protected void endOfNode() throws FOPException {
      if (!this.hasListItem) {
         this.missingChildElementError("marker* (list-item)+");
      }

      this.getFOEventHandler().endList(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.hasListItem) {
               this.nodesOutOfOrderError(loc, "fo:marker", "fo:list-item");
            }
         } else if (localName.equals("list-item")) {
            this.hasListItem = true;
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
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

   public Length getWidowContentLimit() {
      return this.widowContentLimit;
   }

   public Length getOrphanContentLimit() {
      return this.orphanContentLimit;
   }

   public String getLocalName() {
      return "list-block";
   }

   public int getNameId() {
      return 40;
   }
}
