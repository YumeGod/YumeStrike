package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.LengthRangeProperty;

public class Leader extends InlineLevel {
   private Length alignmentAdjust;
   private int alignmentBaseline;
   private Length baselineShift;
   private int dominantBaseline;
   private int leaderAlignment;
   private LengthRangeProperty leaderLength;
   private int leaderPattern;
   private Length leaderPatternWidth;
   private int ruleStyle;
   private Length ruleThickness;

   public Leader(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.alignmentAdjust = pList.get(3).getLength();
      this.alignmentBaseline = pList.get(4).getEnum();
      this.baselineShift = pList.get(15).getLength();
      this.dominantBaseline = pList.get(88).getEnum();
      this.leaderAlignment = pList.get(136).getEnum();
      this.leaderLength = pList.get(137).getLengthRange();
      this.leaderPattern = pList.get(138).getEnum();
      this.leaderPatternWidth = pList.get(139).getLength();
      this.ruleThickness = pList.get(214).getLength();
      switch (this.leaderPattern) {
         case 123:
            this.ruleStyle = pList.get(213).getEnum();
         case 35:
         case 134:
         case 158:
            return;
         default:
            throw new RuntimeException("Invalid leader pattern: " + this.leaderPattern);
      }
   }

   public int getRuleStyle() {
      return this.ruleStyle;
   }

   public Length getRuleThickness() {
      return this.ruleThickness;
   }

   public int getLeaderAlignment() {
      return this.leaderAlignment;
   }

   public LengthRangeProperty getLeaderLength() {
      return this.leaderLength;
   }

   public int getLeaderPattern() {
      return this.leaderPattern;
   }

   public Length getLeaderPatternWidth() {
      return this.leaderPatternWidth;
   }

   public Length getAlignmentAdjust() {
      return this.alignmentAdjust;
   }

   public int getAlignmentBaseline() {
      return this.alignmentBaseline;
   }

   public Length getBaselineShift() {
      return this.baselineShift;
   }

   public int getDominantBaseline() {
      return this.dominantBaseline;
   }

   public String getLocalName() {
      return "leader";
   }

   public int getNameId() {
      return 39;
   }
}
