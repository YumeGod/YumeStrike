package org.apache.fop.layoutmgr.inline;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.flow.Footnote;
import org.apache.fop.layoutmgr.FootnoteBodyLayoutManager;
import org.apache.fop.layoutmgr.InlineKnuthSequence;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthSequence;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.ListElement;
import org.apache.fop.layoutmgr.NonLeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;

public class FootnoteLayoutManager extends InlineStackingLayoutManager {
   private static Log log;
   private Footnote footnote;
   private InlineStackingLayoutManager citationLM;
   private FootnoteBodyLayoutManager bodyLM;
   private KnuthElement forcedAnchor;

   public FootnoteLayoutManager(Footnote node) {
      super(node);
      this.footnote = node;
   }

   public void initialize() {
      this.citationLM = new InlineLayoutManager(this.footnote.getFootnoteCitation());
      this.bodyLM = new FootnoteBodyLayoutManager(this.footnote.getFootnoteBody());
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      this.citationLM.setParent(this);
      this.citationLM.initialize();
      this.bodyLM.setParent(this);
      this.bodyLM.initialize();
      List returnedList = new LinkedList();

      while(!this.citationLM.isFinished()) {
         List partialList = this.citationLM.getNextKnuthElements(context, alignment);
         if (partialList != null) {
            returnedList.addAll(partialList);
         }
      }

      if (returnedList.size() == 0) {
         KnuthSequence seq = new InlineKnuthSequence();
         this.forcedAnchor = new KnuthInlineBox(0, (AlignmentContext)null, (Position)null, true);
         seq.add(this.forcedAnchor);
         returnedList.add(seq);
      }

      this.setFinished(true);
      this.addAnchor(returnedList);
      ListIterator listIterator = returnedList.listIterator();
      ListIterator elementIterator = null;
      KnuthSequence list = null;
      ListElement element = null;

      while(listIterator.hasNext()) {
         list = (KnuthSequence)listIterator.next();
         elementIterator = list.listIterator();

         while(elementIterator.hasNext()) {
            element = (KnuthElement)elementIterator.next();
            element.setPosition(this.notifyPos(new NonLeafPosition(this, element.getPosition())));
         }
      }

      return returnedList;
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      List returnedList = super.getChangedKnuthElements(oldList, alignment);
      this.addAnchor(returnedList);
      return returnedList;
   }

   public void addAreas(PositionIterator posIter, LayoutContext context) {
      LinkedList positionList = new LinkedList();
      NonLeafPosition pos = null;

      while(posIter.hasNext()) {
         pos = (NonLeafPosition)posIter.next();
         if (pos != null && pos.getPosition() != null) {
            positionList.add(pos.getPosition());
         }
      }

      this.citationLM.setParent(this.getParent());
      LayoutContext childContext = new LayoutContext(context);
      InlineStackingLayoutManager.StackingIter childPosIter = new InlineStackingLayoutManager.StackingIter(positionList.listIterator());

      LayoutManager childLM;
      while((childLM = childPosIter.getNextChildLM()) != null) {
         childLM.addAreas(childPosIter, childContext);
         childContext.setLeadingSpace(childContext.getTrailingSpace());
         childContext.setFlags(256, true);
      }

   }

   private void addAnchor(List citationList) {
      KnuthInlineBox lastBox = null;
      ListIterator citationIterator = citationList.listIterator(citationList.size());

      label46:
      while(citationIterator.hasPrevious() && lastBox == null) {
         Object obj = citationIterator.previous();
         if (obj instanceof KnuthElement) {
            KnuthElement element = (KnuthElement)obj;
            if (element instanceof KnuthInlineBox) {
               lastBox = (KnuthInlineBox)element;
            }
         } else {
            KnuthSequence seq = (KnuthSequence)obj;
            ListIterator nestedIterator = seq.listIterator(seq.size());

            while(true) {
               KnuthElement element;
               do {
                  if (!nestedIterator.hasPrevious() || lastBox != null) {
                     continue label46;
                  }

                  element = (KnuthElement)nestedIterator.previous();
               } while((!(element instanceof KnuthInlineBox) || element.isAuxiliary()) && element != this.forcedAnchor);

               lastBox = (KnuthInlineBox)element;
            }
         }
      }

      if (lastBox != null) {
         lastBox.setFootnoteBodyLM(this.bodyLM);
      }

   }

   static {
      log = LogFactory.getLog(FootnoteLayoutManager.class);
   }
}
