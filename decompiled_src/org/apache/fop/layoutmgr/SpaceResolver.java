package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.traits.MinOptMax;

public class SpaceResolver {
   protected static Log log;
   private UnresolvedListElementWithLength[] firstPart;
   private BreakElement breakPoss;
   private UnresolvedListElementWithLength[] secondPart;
   private UnresolvedListElementWithLength[] noBreak;
   private MinOptMax[] firstPartLengths;
   private MinOptMax[] secondPartLengths;
   private MinOptMax[] noBreakLengths;
   private boolean isFirst;
   private boolean isLast;

   private SpaceResolver(List first, BreakElement breakPoss, List second, boolean isFirst, boolean isLast) {
      this.isFirst = isFirst;
      this.isLast = isLast;
      int c = 0;
      if (first != null) {
         c += first.size();
      }

      if (second != null) {
         c += second.size();
      }

      this.noBreak = new UnresolvedListElementWithLength[c];
      this.noBreakLengths = new MinOptMax[c];
      int i = 0;
      ListIterator iter;
      if (first != null) {
         for(iter = first.listIterator(); iter.hasNext(); ++i) {
            this.noBreak[i] = (UnresolvedListElementWithLength)iter.next();
            this.noBreakLengths[i] = this.noBreak[i].getLength();
         }
      }

      if (second != null) {
         for(iter = second.listIterator(); iter.hasNext(); ++i) {
            this.noBreak[i] = (UnresolvedListElementWithLength)iter.next();
            this.noBreakLengths[i] = this.noBreak[i].getLength();
         }
      }

      if (breakPoss != null) {
         if (breakPoss.getPendingAfterMarks() != null) {
            if (log.isTraceEnabled()) {
               log.trace("    adding pending before break: " + breakPoss.getPendingAfterMarks());
            }

            first.addAll(0, breakPoss.getPendingAfterMarks());
         }

         if (breakPoss.getPendingBeforeMarks() != null) {
            if (log.isTraceEnabled()) {
               log.trace("    adding pending after break: " + breakPoss.getPendingBeforeMarks());
            }

            second.addAll(0, breakPoss.getPendingBeforeMarks());
         }
      }

      if (log.isTraceEnabled()) {
         log.trace("before: " + first);
         log.trace("  break: " + breakPoss);
         log.trace("after: " + second);
         log.trace("NO-BREAK: " + this.toString(this.noBreak, this.noBreakLengths));
      }

      if (first != null) {
         this.firstPart = new UnresolvedListElementWithLength[first.size()];
         this.firstPartLengths = new MinOptMax[this.firstPart.length];
         first.toArray(this.firstPart);

         for(i = 0; i < this.firstPart.length; ++i) {
            this.firstPartLengths[i] = this.firstPart[i].getLength();
         }
      }

      this.breakPoss = breakPoss;
      if (second != null) {
         this.secondPart = new UnresolvedListElementWithLength[second.size()];
         this.secondPartLengths = new MinOptMax[this.secondPart.length];
         second.toArray(this.secondPart);

         for(i = 0; i < this.secondPart.length; ++i) {
            this.secondPartLengths[i] = this.secondPart[i].getLength();
         }
      }

      this.resolve();
   }

   private String toString(Object[] arr1, Object[] arr2) {
      if (arr1.length != arr2.length) {
         new IllegalArgumentException("The length of both arrays must be equal");
      }

      StringBuffer sb = new StringBuffer("[");

      for(int i = 0; i < arr1.length; ++i) {
         if (i > 0) {
            sb.append(", ");
         }

         sb.append(String.valueOf(arr1[i]));
         sb.append("/");
         sb.append(String.valueOf(arr2[i]));
      }

      sb.append("]");
      return sb.toString();
   }

   private void removeConditionalBorderAndPadding(UnresolvedListElement[] elems, MinOptMax[] lengths, boolean reverse) {
      for(int i = 0; i < elems.length; ++i) {
         int effIndex;
         if (reverse) {
            effIndex = elems.length - 1 - i;
         } else {
            effIndex = i;
         }

         if (elems[effIndex] instanceof BorderOrPaddingElement) {
            BorderOrPaddingElement bop = (BorderOrPaddingElement)elems[effIndex];
            if (bop.isConditional() && !bop.isFirst() && !bop.isLast()) {
               if (log.isDebugEnabled()) {
                  log.debug("Nulling conditional element: " + bop);
               }

               lengths[effIndex] = null;
            }
         }
      }

      if (log.isTraceEnabled() && elems.length > 0) {
         log.trace("-->Resulting list: " + this.toString(elems, lengths));
      }

   }

   private void performSpaceResolutionRule1(UnresolvedListElement[] elems, MinOptMax[] lengths, boolean reverse) {
      for(int i = 0; i < elems.length; ++i) {
         int effIndex;
         if (reverse) {
            effIndex = elems.length - 1 - i;
         } else {
            effIndex = i;
         }

         if (lengths[effIndex] != null) {
            if (elems[effIndex] instanceof BorderOrPaddingElement || !elems[effIndex].isConditional()) {
               break;
            }

            if (log.isDebugEnabled()) {
               log.debug("Nulling conditional element using 4.3.1, rule 1: " + elems[effIndex]);
            }

            lengths[effIndex] = null;
         }
      }

      if (log.isTraceEnabled() && elems.length > 0) {
         log.trace("-->Resulting list: " + this.toString(elems, lengths));
      }

   }

   private void performSpaceResolutionRules2to3(UnresolvedListElement[] elems, MinOptMax[] lengths, int start, int end) {
      if (log.isTraceEnabled()) {
         log.trace("rule 2-3: " + start + "-" + end);
      }

      boolean hasForcing = false;
      int remaining = 0;

      SpaceElement space;
      int highestPrecedence;
      for(highestPrecedence = start; highestPrecedence <= end; ++highestPrecedence) {
         if (lengths[highestPrecedence] != null) {
            ++remaining;
            space = (SpaceElement)elems[highestPrecedence];
            if (space.isForcing()) {
               hasForcing = true;
               break;
            }
         }
      }

      if (remaining != 0) {
         if (hasForcing) {
            for(highestPrecedence = start; highestPrecedence <= end; ++highestPrecedence) {
               if (lengths[highestPrecedence] != null) {
                  space = (SpaceElement)elems[highestPrecedence];
                  if (!space.isForcing()) {
                     if (log.isDebugEnabled()) {
                        log.debug("Nulling non-forcing space-specifier using 4.3.1, rule 2: " + elems[highestPrecedence]);
                     }

                     lengths[highestPrecedence] = null;
                  }
               }
            }

         } else {
            highestPrecedence = Integer.MIN_VALUE;

            int greatestOptimum;
            for(greatestOptimum = start; greatestOptimum <= end; ++greatestOptimum) {
               if (lengths[greatestOptimum] != null) {
                  space = (SpaceElement)elems[greatestOptimum];
                  highestPrecedence = Math.max(highestPrecedence, space.getPrecedence());
               }
            }

            if (highestPrecedence != 0 && log.isDebugEnabled()) {
               log.debug("Highest precedence is " + highestPrecedence);
            }

            remaining = 0;
            greatestOptimum = Integer.MIN_VALUE;

            int min;
            for(min = start; min <= end; ++min) {
               if (lengths[min] != null) {
                  space = (SpaceElement)elems[min];
                  if (space.getPrecedence() != highestPrecedence) {
                     if (log.isDebugEnabled()) {
                        log.debug("Nulling space-specifier with precedence " + space.getPrecedence() + " using 4.3.1, rule 3: " + elems[min]);
                     }

                     lengths[min] = null;
                  } else {
                     greatestOptimum = Math.max(greatestOptimum, space.getLength().getOpt());
                     ++remaining;
                  }
               }
            }

            if (log.isDebugEnabled()) {
               log.debug("Greatest optimum: " + greatestOptimum);
            }

            if (remaining > 1) {
               remaining = 0;

               for(min = start; min <= end; ++min) {
                  if (lengths[min] != null) {
                     space = (SpaceElement)elems[min];
                     if (space.getLength().getOpt() < greatestOptimum) {
                        if (log.isDebugEnabled()) {
                           log.debug("Nulling space-specifier with smaller optimum length using 4.3.1, rule 3: " + elems[min]);
                        }

                        lengths[min] = null;
                     } else {
                        ++remaining;
                     }
                  }
               }

               if (remaining > 1) {
                  min = Integer.MIN_VALUE;
                  int max = Integer.MAX_VALUE;

                  for(int i = start; i <= end; ++i) {
                     if (lengths[i] != null) {
                        space = (SpaceElement)elems[i];
                        min = Math.max(min, space.getLength().getMin());
                        max = Math.min(max, space.getLength().getMax());
                        if (remaining > 1) {
                           if (log.isDebugEnabled()) {
                              log.debug("Nulling non-last space-specifier using 4.3.1, rule 3, second part: " + elems[i]);
                           }

                           lengths[i] = null;
                           --remaining;
                        } else {
                           lengths[i] = MinOptMax.getInstance(min, lengths[i].getOpt(), max);
                        }
                     }
                  }

                  if (log.isTraceEnabled() && elems.length > 0) {
                     log.trace("Remaining spaces: " + remaining);
                     log.trace("-->Resulting list: " + this.toString(elems, lengths));
                  }

               }
            }
         }
      }
   }

   private void performSpaceResolutionRules2to3(UnresolvedListElement[] elems, MinOptMax[] lengths) {
      int start = 0;

      for(int i = start; i < elems.length; start = i) {
         if (elems[i] instanceof SpaceElement) {
            while(i < elems.length && (elems[i] == null || elems[i] instanceof SpaceElement)) {
               ++i;
            }

            this.performSpaceResolutionRules2to3(elems, lengths, start, i - 1);
         }

         ++i;
      }

   }

   private boolean hasFirstPart() {
      return this.firstPart != null && this.firstPart.length > 0;
   }

   private boolean hasSecondPart() {
      return this.secondPart != null && this.secondPart.length > 0;
   }

   private void resolve() {
      if (this.breakPoss != null) {
         if (this.hasFirstPart()) {
            this.removeConditionalBorderAndPadding(this.firstPart, this.firstPartLengths, true);
            this.performSpaceResolutionRule1(this.firstPart, this.firstPartLengths, true);
            this.performSpaceResolutionRules2to3(this.firstPart, this.firstPartLengths);
         }

         if (this.hasSecondPart()) {
            this.removeConditionalBorderAndPadding(this.secondPart, this.secondPartLengths, false);
            this.performSpaceResolutionRule1(this.secondPart, this.secondPartLengths, false);
            this.performSpaceResolutionRules2to3(this.secondPart, this.secondPartLengths);
         }

         if (this.noBreak != null) {
            this.performSpaceResolutionRules2to3(this.noBreak, this.noBreakLengths);
         }
      } else {
         if (this.isFirst) {
            this.removeConditionalBorderAndPadding(this.secondPart, this.secondPartLengths, false);
            this.performSpaceResolutionRule1(this.secondPart, this.secondPartLengths, false);
         }

         if (this.isLast) {
            this.removeConditionalBorderAndPadding(this.firstPart, this.firstPartLengths, true);
            this.performSpaceResolutionRule1(this.firstPart, this.firstPartLengths, true);
         }

         if (this.hasFirstPart()) {
            log.trace("Swapping first and second parts.");
            UnresolvedListElementWithLength[] tempList = this.secondPart;
            MinOptMax[] tempLengths = this.secondPartLengths;
            this.secondPart = this.firstPart;
            this.secondPartLengths = this.firstPartLengths;
            this.firstPart = tempList;
            this.firstPartLengths = tempLengths;
            if (this.hasFirstPart()) {
               throw new IllegalStateException("Didn't expect more than one parts in ano-break condition.");
            }
         }

         this.performSpaceResolutionRules2to3(this.secondPart, this.secondPartLengths);
      }

   }

   private MinOptMax sum(MinOptMax[] lengths) {
      MinOptMax sum = MinOptMax.ZERO;

      for(int i = 0; i < lengths.length; ++i) {
         if (lengths[i] != null) {
            sum = sum.plus(lengths[i]);
         }
      }

      return sum;
   }

   private void generate(ListIterator iter) {
      MinOptMax spaceBeforeBreak = this.sum(this.firstPartLengths);
      MinOptMax spaceAfterBreak = this.sum(this.secondPartLengths);
      boolean hasPrecedingNonBlock = false;
      if (this.breakPoss != null) {
         if (spaceBeforeBreak.isNonZero()) {
            iter.add(new KnuthPenalty(0, 1000, false, (Position)null, true));
            iter.add(new KnuthGlue(spaceBeforeBreak, (Position)null, true));
            if (this.breakPoss.isForcedBreak()) {
               iter.add(new KnuthBox(0, (Position)null, true));
            }
         }

         iter.add(new KnuthPenalty(this.breakPoss.getPenaltyWidth(), this.breakPoss.getPenaltyValue(), false, this.breakPoss.getBreakClass(), new SpaceHandlingBreakPosition(this, this.breakPoss), false));
         if (this.breakPoss.getPenaltyValue() <= -1000) {
            return;
         }

         MinOptMax noBreakLength = this.sum(this.noBreakLengths);
         MinOptMax spaceSum = spaceBeforeBreak.plus(spaceAfterBreak);
         int glue2width = noBreakLength.getOpt() - spaceSum.getOpt();
         int glue2stretch = noBreakLength.getStretch() - spaceSum.getStretch();
         int glue2shrink = noBreakLength.getShrink() - spaceSum.getShrink();
         if (glue2width != 0 || glue2stretch != 0 || glue2shrink != 0) {
            iter.add(new KnuthGlue(glue2width, glue2stretch, glue2shrink, (Position)null, true));
         }
      } else if (spaceBeforeBreak.isNonZero()) {
         throw new IllegalStateException("spaceBeforeBreak should be 0 in this case");
      }

      Position pos = null;
      if (this.breakPoss == null) {
         pos = new SpaceHandlingPosition(this);
      }

      if (spaceAfterBreak.isNonZero() || pos != null) {
         iter.add(new KnuthBox(0, pos, true));
      }

      if (spaceAfterBreak.isNonZero()) {
         iter.add(new KnuthPenalty(0, 1000, false, (Position)null, true));
         iter.add(new KnuthGlue(spaceAfterBreak, (Position)null, true));
         hasPrecedingNonBlock = true;
      }

      if (this.isLast && hasPrecedingNonBlock) {
         iter.add(new KnuthBox(0, (Position)null, true));
      }

   }

   public static void resolveElementList(List elems) {
      if (log.isTraceEnabled()) {
         log.trace(elems);
      }

      boolean first = true;
      boolean last = false;
      boolean skipNextElement = false;
      List unresolvedFirst = new ArrayList();
      List unresolvedSecond = new ArrayList();

      for(ListIterator iter = elems.listIterator(); iter.hasNext(); first = false) {
         ListElement el = (ListElement)iter.next();
         if (el.isUnresolvedElement()) {
            if (log.isTraceEnabled()) {
               log.trace("unresolved found: " + el + " " + first + "/" + last);
            }

            BreakElement breakPoss = null;
            unresolvedFirst.clear();
            unresolvedSecond.clear();
            ArrayList currentGroup;
            if (el instanceof BreakElement) {
               breakPoss = (BreakElement)el;
               currentGroup = unresolvedSecond;
            } else {
               currentGroup = unresolvedFirst;
               unresolvedFirst.add(el);
            }

            iter.remove();
            last = true;
            skipNextElement = true;

            while(iter.hasNext()) {
               el = (ListElement)iter.next();
               if (el instanceof BreakElement && breakPoss != null) {
                  skipNextElement = false;
                  last = false;
                  break;
               }

               if (currentGroup == unresolvedFirst && el instanceof BreakElement) {
                  breakPoss = (BreakElement)el;
                  iter.remove();
                  currentGroup = unresolvedSecond;
               } else {
                  if (!el.isUnresolvedElement()) {
                     last = false;
                     break;
                  }

                  currentGroup.add(el);
                  iter.remove();
               }
            }

            if (breakPoss == null && unresolvedSecond.size() == 0 && !last) {
               log.trace("Swap first and second parts in no-break condition, second part is empty.");
               List swapList = unresolvedSecond;
               unresolvedSecond = unresolvedFirst;
               unresolvedFirst = swapList;
            }

            log.debug("----start space resolution (first=" + first + ", last=" + last + ")...");
            SpaceResolver resolver = new SpaceResolver(unresolvedFirst, breakPoss, unresolvedSecond, first, last);
            if (!last) {
               iter.previous();
            }

            resolver.generate(iter);
            if (!last && skipNextElement) {
               iter.next();
            }

            log.debug("----end space resolution.");
         }
      }

   }

   public static void performConditionalsNotification(List effectiveList, int startElementIndex, int endElementIndex, int prevBreak) {
      KnuthElement el = null;
      if (prevBreak > 0) {
         el = (KnuthElement)effectiveList.get(prevBreak);
      }

      SpaceHandlingBreakPosition beforeBreak = null;
      SpaceHandlingBreakPosition afterBreak = null;
      Position pos;
      if (el != null && el.isPenalty()) {
         pos = el.getPosition();
         if (pos instanceof SpaceHandlingBreakPosition) {
            beforeBreak = (SpaceHandlingBreakPosition)pos;
            beforeBreak.notifyBreakSituation(true, RelSide.BEFORE);
         }
      }

      el = (KnuthElement)effectiveList.get(endElementIndex);
      if (el != null && el.isPenalty()) {
         pos = el.getPosition();
         if (pos instanceof SpaceHandlingBreakPosition) {
            afterBreak = (SpaceHandlingBreakPosition)pos;
            afterBreak.notifyBreakSituation(true, RelSide.AFTER);
         }
      }

      for(int i = startElementIndex; i <= endElementIndex; ++i) {
         Position pos = ((KnuthElement)effectiveList.get(i)).getPosition();
         if (pos instanceof SpaceHandlingPosition) {
            ((SpaceHandlingPosition)pos).notifySpaceSituation();
         } else if (pos instanceof SpaceHandlingBreakPosition) {
            SpaceHandlingBreakPosition noBreak = (SpaceHandlingBreakPosition)pos;
            if (noBreak != beforeBreak && noBreak != afterBreak) {
               noBreak.notifyBreakSituation(false, (RelSide)null);
            }
         }
      }

   }

   static {
      log = LogFactory.getLog(SpaceResolver.class);
   }

   public static class SpaceHandlingPosition extends Position {
      private SpaceResolver resolver;

      public SpaceHandlingPosition(SpaceResolver resolver) {
         super((LayoutManager)null);
         this.resolver = resolver;
      }

      public SpaceResolver getSpaceResolver() {
         return this.resolver;
      }

      public void notifySpaceSituation() {
         if (this.resolver.breakPoss != null) {
            throw new IllegalStateException("Only applicable to no-break situations");
         } else {
            for(int i = 0; i < this.resolver.secondPart.length; ++i) {
               this.resolver.secondPart[i].notifyLayoutManager(this.resolver.secondPartLengths[i]);
            }

         }
      }

      public String toString() {
         return "SpaceHandlingPosition";
      }
   }

   public static class SpaceHandlingBreakPosition extends Position {
      private SpaceResolver resolver;
      private Position originalPosition;

      public SpaceHandlingBreakPosition(SpaceResolver resolver, BreakElement breakPoss) {
         super((LayoutManager)null);
         this.resolver = resolver;

         for(this.originalPosition = breakPoss.getPosition(); this.originalPosition instanceof NonLeafPosition; this.originalPosition = this.originalPosition.getPosition()) {
         }

      }

      public SpaceResolver getSpaceResolver() {
         return this.resolver;
      }

      public void notifyBreakSituation(boolean isBreakSituation, RelSide side) {
         int i;
         if (isBreakSituation) {
            if (RelSide.BEFORE == side) {
               for(i = 0; i < this.resolver.secondPart.length; ++i) {
                  this.resolver.secondPart[i].notifyLayoutManager(this.resolver.secondPartLengths[i]);
               }
            } else {
               for(i = 0; i < this.resolver.firstPart.length; ++i) {
                  this.resolver.firstPart[i].notifyLayoutManager(this.resolver.firstPartLengths[i]);
               }
            }
         } else {
            for(i = 0; i < this.resolver.noBreak.length; ++i) {
               this.resolver.noBreak[i].notifyLayoutManager(this.resolver.noBreakLengths[i]);
            }
         }

      }

      public String toString() {
         StringBuffer sb = new StringBuffer();
         sb.append("SpaceHandlingBreakPosition(");
         sb.append(this.originalPosition);
         sb.append(")");
         return sb.toString();
      }

      public Position getOriginalBreakPosition() {
         return this.originalPosition;
      }

      public Position getPosition() {
         return this.originalPosition;
      }
   }
}
