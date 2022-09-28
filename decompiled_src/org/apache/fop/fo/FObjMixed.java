package org.apache.fop.fo;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.flow.Block;
import org.xml.sax.Locator;

public abstract class FObjMixed extends FObj {
   private FOText ft = null;
   protected FONode currentTextNode;
   protected FOText lastFOTextProcessed = null;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   protected FObjMixed(FONode parent) {
      super(parent);
   }

   protected void characters(char[] data, int start, int length, PropertyList pList, Locator locator) throws FOPException {
      if (this.ft == null) {
         this.ft = new FOText(this);
         this.ft.setLocator(locator);
         if (!this.inMarker()) {
            this.ft.bind(pList);
         }
      }

      this.ft.characters(data, start, length, (PropertyList)null, (Locator)null);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      if (!this.inMarker() || this.getNameId() == 44) {
         this.sendCharacters();
      }

   }

   protected static void handleWhiteSpaceFor(FObjMixed fobj, FONode nextChild) {
      fobj.getBuilderContext().getXMLWhiteSpaceHandler().handleWhiteSpace(fobj, fobj.currentTextNode, nextChild);
   }

   private void flushText() throws FOPException {
      if (this.ft != null) {
         FOText lft = this.ft;
         this.ft = null;
         if (this.getNameId() == 3) {
            lft.createBlockPointers((Block)this);
            this.lastFOTextProcessed = lft;
         } else if (this.getNameId() != 44 && this.getNameId() != 80 && this.getNameId() != 7) {
            FONode fo = this.parent;

            int foNameId;
            for(foNameId = fo.getNameId(); foNameId != 3 && foNameId != 44 && foNameId != 80 && foNameId != 7 && foNameId != 53; foNameId = fo.getNameId()) {
               fo = fo.getParent();
            }

            if (foNameId == 3) {
               lft.createBlockPointers((Block)fo);
               ((FObjMixed)fo).lastFOTextProcessed = lft;
            } else if (foNameId == 53 && lft.willCreateArea()) {
               log.error("Could not create block pointers. FOText w/o Block ancestor.");
            }
         }

         this.addChildNode(lft);
      }

   }

   private void sendCharacters() throws FOPException {
      FONode node;
      if (this.currentTextNode != null) {
         for(FONode.FONodeIterator nodeIter = this.getChildNodes(this.currentTextNode); nodeIter.hasNext(); node.endOfNode()) {
            node = nodeIter.nextNode();
            if (!$assertionsDisabled && !(node instanceof FOText) && node.getNameId() != 10) {
               throw new AssertionError();
            }

            if (node.getNameId() == 10) {
               node.startOfNode();
            }
         }
      }

      this.currentTextNode = null;
   }

   protected void addChildNode(FONode child) throws FOPException {
      this.flushText();
      if (!this.inMarker()) {
         if (!(child instanceof FOText) && child.getNameId() != 10) {
            handleWhiteSpaceFor(this, child);
            this.sendCharacters();
         } else if (this.currentTextNode == null) {
            this.currentTextNode = child;
         }
      }

      super.addChildNode(child);
   }

   public void finalizeNode() throws FOPException {
      this.flushText();
      if (!this.inMarker() || this.getNameId() == 44) {
         handleWhiteSpaceFor(this, (FONode)null);
      }

   }

   public CharIterator charIterator() {
      return new RecursiveCharIterator(this);
   }

   static {
      $assertionsDisabled = !FObjMixed.class.desiredAssertionStatus();
   }
}
