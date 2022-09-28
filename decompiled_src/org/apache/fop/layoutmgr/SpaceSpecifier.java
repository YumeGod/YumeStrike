package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.traits.SpaceVal;

public class SpaceSpecifier implements Cloneable {
   private boolean startsReferenceArea;
   private boolean hasForcing = false;
   private List spaceVals = new ArrayList();

   public SpaceSpecifier(boolean startsReferenceArea) {
      this.startsReferenceArea = startsReferenceArea;
   }

   public Object clone() {
      try {
         SpaceSpecifier ss = (SpaceSpecifier)super.clone();
         ss.startsReferenceArea = this.startsReferenceArea;
         ss.hasForcing = this.hasForcing;
         ss.spaceVals = new ArrayList();
         ss.spaceVals.addAll(this.spaceVals);
         return ss;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public void clear() {
      this.hasForcing = false;
      this.spaceVals.clear();
   }

   public boolean hasSpaces() {
      return !this.spaceVals.isEmpty();
   }

   public void addSpace(SpaceVal space) {
      if (!this.startsReferenceArea || !space.isConditional() || this.hasSpaces()) {
         if (space.isForcing()) {
            if (!this.hasForcing) {
               this.spaceVals.clear();
               this.hasForcing = true;
            }

            this.spaceVals.add(space);
         } else if (!this.hasForcing && space.getSpace().isNonZero()) {
            this.spaceVals.add(space);
         }
      }

   }

   public MinOptMax resolve(boolean endsReferenceArea) {
      int lastIndex = this.spaceVals.size();
      if (endsReferenceArea) {
         while(lastIndex > 0) {
            SpaceVal spaceVal = (SpaceVal)this.spaceVals.get(lastIndex - 1);
            if (!spaceVal.isConditional()) {
               break;
            }

            --lastIndex;
         }
      }

      MinOptMax resolvedSpace = MinOptMax.ZERO;
      int maxPrecedence = -1;

      for(int index = 0; index < lastIndex; ++index) {
         SpaceVal spaceVal = (SpaceVal)this.spaceVals.get(index);
         MinOptMax space = spaceVal.getSpace();
         if (this.hasForcing) {
            resolvedSpace = resolvedSpace.plus(space);
         } else {
            int precedence = spaceVal.getPrecedence();
            if (precedence > maxPrecedence) {
               maxPrecedence = precedence;
               resolvedSpace = space;
            } else if (precedence == maxPrecedence) {
               if (space.getOpt() > resolvedSpace.getOpt()) {
                  resolvedSpace = space;
               } else if (space.getOpt() == resolvedSpace.getOpt()) {
                  if (resolvedSpace.getMin() < space.getMin()) {
                     resolvedSpace = MinOptMax.getInstance(space.getMin(), resolvedSpace.getOpt(), resolvedSpace.getMax());
                  }

                  if (resolvedSpace.getMax() > space.getMax()) {
                     resolvedSpace = MinOptMax.getInstance(resolvedSpace.getMin(), resolvedSpace.getOpt(), space.getMax());
                  }
               }
            }
         }
      }

      return resolvedSpace;
   }

   public String toString() {
      return "Space Specifier (resolved at begin/end of ref. area:):\n" + this.resolve(false) + "\n" + this.resolve(true);
   }
}
