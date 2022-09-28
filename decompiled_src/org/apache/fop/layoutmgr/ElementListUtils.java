package org.apache.fop.layoutmgr;

import java.util.List;
import java.util.ListIterator;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.ListUtil;

public final class ElementListUtils {
   private ElementListUtils() {
   }

   public static boolean removeLegalBreaks(List elements, MinOptMax constraint) {
      return removeLegalBreaks(elements, constraint.getOpt());
   }

   public static boolean removeLegalBreaks(List elements, int constraint) {
      int len = 0;
      ListIterator iter = elements.listIterator();

      do {
         if (!iter.hasNext()) {
            return true;
         }

         ListElement el = (ListElement)iter.next();
         if (el.isPenalty()) {
            KnuthPenalty penalty = (KnuthPenalty)el;
            if (penalty.getPenalty() < 1000) {
               iter.set(new KnuthPenalty(penalty.getWidth(), 1000, penalty.isPenaltyFlagged(), penalty.getPosition(), penalty.isAuxiliary()));
            }
         } else if (el.isGlue()) {
            KnuthGlue glue = (KnuthGlue)el;
            len += glue.getWidth();
            iter.previous();
            el = (ListElement)iter.previous();
            iter.next();
            if (el.isBox()) {
               iter.add(new KnuthPenalty(0, 1000, false, (Position)null, false));
            }

            iter.next();
         } else if (el instanceof BreakElement) {
            BreakElement breakEl = (BreakElement)el;
            if (breakEl.getPenaltyValue() < 1000) {
               breakEl.setPenaltyValue(1000);
            }
         } else {
            KnuthElement kel = (KnuthElement)el;
            len += kel.getWidth();
         }
      } while(len < constraint);

      return false;
   }

   public static boolean removeLegalBreaksFromEnd(List elements, int constraint) {
      int len = 0;
      ListIterator i = elements.listIterator(elements.size());

      do {
         if (!i.hasPrevious()) {
            return true;
         }

         ListElement el = (ListElement)i.previous();
         if (el.isPenalty()) {
            KnuthPenalty penalty = (KnuthPenalty)el;
            if (penalty.getPenalty() < 1000) {
               i.set(new KnuthPenalty(penalty.getWidth(), 1000, penalty.isPenaltyFlagged(), penalty.getPosition(), penalty.isAuxiliary()));
            }
         } else if (el.isGlue()) {
            KnuthGlue glue = (KnuthGlue)el;
            len += glue.getWidth();
            el = (ListElement)i.previous();
            i.next();
            if (el.isBox()) {
               i.add(new KnuthPenalty(0, 1000, false, (Position)null, false));
            }
         } else if (el.isUnresolvedElement()) {
            if (el instanceof BreakElement) {
               BreakElement breakEl = (BreakElement)el;
               if (breakEl.getPenaltyValue() < 1000) {
                  breakEl.setPenaltyValue(1000);
               }
            } else if (el instanceof UnresolvedListElementWithLength) {
               UnresolvedListElementWithLength uel = (UnresolvedListElementWithLength)el;
               len += uel.getLength().getOpt();
            }
         } else {
            KnuthElement kel = (KnuthElement)el;
            len += kel.getWidth();
         }
      } while(len < constraint);

      return false;
   }

   public static int calcContentLength(List elems, int start, int end) {
      ListIterator iter = elems.listIterator(start);
      int count = end - start + 1;
      int len = 0;

      while(iter.hasNext()) {
         ListElement el = (ListElement)iter.next();
         if (el.isBox()) {
            len += ((KnuthElement)el).getWidth();
         } else if (el.isGlue()) {
            len += ((KnuthElement)el).getWidth();
         }

         --count;
         if (count == 0) {
            break;
         }
      }

      return len;
   }

   public static int calcContentLength(List elems) {
      return calcContentLength(elems, 0, elems.size() - 1);
   }

   public static boolean endsWithForcedBreak(List elems) {
      ListElement last = (ListElement)ListUtil.getLast(elems);
      return last.isForcedBreak();
   }

   public static boolean startsWithForcedBreak(List elems) {
      return !elems.isEmpty() && ((ListElement)elems.get(0)).isForcedBreak();
   }

   public static boolean endsWithNonInfinitePenalty(List elems) {
      ListElement last = (ListElement)ListUtil.getLast(elems);
      if (last.isPenalty() && ((KnuthPenalty)last).getPenalty() < 1000) {
         return true;
      } else {
         return last instanceof BreakElement && ((BreakElement)last).getPenaltyValue() < 1000;
      }
   }

   public static int determinePreviousBreak(List elems, int startIndex) {
      int prevBreak;
      for(prevBreak = startIndex - 1; prevBreak >= 0; --prevBreak) {
         KnuthElement el = (KnuthElement)elems.get(prevBreak);
         if (el.isPenalty() && el.getPenalty() < 1000) {
            break;
         }
      }

      return prevBreak;
   }
}
