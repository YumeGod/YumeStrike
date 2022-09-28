package org.apache.fop.traits;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.fonts.Font;

public class SpaceVal {
   private final MinOptMax space;
   private final boolean conditional;
   private final boolean forcing;
   private final int precedence;

   public SpaceVal(SpaceProperty spaceprop, PercentBaseContext context) {
      this.space = createSpaceProperty(spaceprop, context);
      this.conditional = spaceprop.getConditionality().getEnum() == 32;
      Property precProp = spaceprop.getPrecedence();
      if (precProp.getNumber() != null) {
         this.precedence = precProp.getNumber().intValue();
         this.forcing = false;
      } else {
         this.forcing = precProp.getEnum() == 53;
         this.precedence = 0;
      }

   }

   private static MinOptMax createSpaceProperty(SpaceProperty spaceprop, PercentBaseContext context) {
      int min = spaceprop.getMinimum(context).getLength().getValue(context);
      int opt = spaceprop.getOptimum(context).getLength().getValue(context);
      int max = spaceprop.getMaximum(context).getLength().getValue(context);
      return MinOptMax.getInstance(min, opt, max);
   }

   public SpaceVal(MinOptMax space, boolean conditional, boolean forcing, int precedence) {
      this.space = space;
      this.conditional = conditional;
      this.forcing = forcing;
      this.precedence = precedence;
   }

   public static SpaceVal makeWordSpacing(Property wordSpacing, SpaceVal letterSpacing, Font fs) {
      if (wordSpacing.getEnum() == 97) {
         int spaceCharIPD = fs.getCharWidth(' ');
         MinOptMax space = MinOptMax.getInstance(-spaceCharIPD / 3, 0, spaceCharIPD / 2);
         return new SpaceVal(space.plus(letterSpacing.getSpace().mult(2)), true, true, 0);
      } else {
         return new SpaceVal(wordSpacing.getSpace(), (PercentBaseContext)null);
      }
   }

   public static SpaceVal makeLetterSpacing(Property letterSpacing) {
      return letterSpacing.getEnum() == 97 ? new SpaceVal(MinOptMax.ZERO, true, true, 0) : new SpaceVal(letterSpacing.getSpace(), (PercentBaseContext)null);
   }

   public boolean isConditional() {
      return this.conditional;
   }

   public boolean isForcing() {
      return this.forcing;
   }

   public int getPrecedence() {
      return this.precedence;
   }

   public MinOptMax getSpace() {
      return this.space;
   }

   public String toString() {
      return "SpaceVal: " + this.getSpace().toString();
   }
}
