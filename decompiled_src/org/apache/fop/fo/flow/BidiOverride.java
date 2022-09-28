package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObjMixed;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.SpaceProperty;
import org.xml.sax.Locator;

public class BidiOverride extends FObjMixed {
   private boolean blockOrInlineItemFound = false;
   private boolean canHaveBlockLevelChildren = true;
   private SpaceProperty lineHeight;

   public BidiOverride(FONode parent) {
      super(parent);
      int lvlLeader = this.findAncestor(39);
      int lvlInCntr = this.findAncestor(36);
      int lvlInline = this.findAncestor(35);
      int lvlFootnote = this.findAncestor(24);
      if (lvlLeader > 0) {
         if (lvlInCntr < 0 || lvlInCntr > 0 && lvlInCntr > lvlLeader) {
            this.canHaveBlockLevelChildren = false;
         }
      } else if (lvlInline > 0 && lvlFootnote == lvlInline + 1 && (lvlInCntr < 0 || lvlInCntr > 0 && lvlInCntr > lvlInline)) {
         this.canHaveBlockLevelChildren = false;
      }

   }

   public void bind(PropertyList pList) throws FOPException {
      this.lineHeight = pList.get(144).getSpace();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.blockOrInlineItemFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(#PCDATA|%inline;|%block;)");
            }
         } else if (!this.isBlockOrInlineItem(nsURI, localName)) {
            this.invalidChildError(loc, nsURI, localName);
         } else if (!this.canHaveBlockLevelChildren && this.isBlockItem(nsURI, localName)) {
            this.invalidChildError(loc, this.getParent().getName(), nsURI, this.getName(), "rule.bidiOverrideContent");
         } else {
            this.blockOrInlineItemFound = true;
         }
      }

   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public String getLocalName() {
      return "bidi-override";
   }

   public int getNameId() {
      return 2;
   }
}
