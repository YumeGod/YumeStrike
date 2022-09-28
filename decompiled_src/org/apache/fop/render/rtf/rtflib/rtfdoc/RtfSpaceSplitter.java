package org.apache.fop.render.rtf.rtflib.rtfdoc;

public class RtfSpaceSplitter {
   private RtfAttributes commonAttributes;
   private int spaceBefore;
   private int spaceAfter;
   private boolean updatingSpaceBefore;
   private RtfAttributes spaceBeforeCandidate;
   private RtfAttributes spaceAfterCandidate;

   public RtfSpaceSplitter(RtfAttributes attrs, int previousSpace) {
      this.commonAttributes = attrs;
      this.updatingSpaceBefore = true;
      this.spaceBeforeCandidate = null;
      this.spaceAfterCandidate = null;
      this.spaceBefore = this.split("sb") + previousSpace;
      this.spaceAfter = this.split("sa");
   }

   public int split(String key) {
      Integer i = (Integer)this.commonAttributes.getValue(key);
      if (i == null) {
         i = new Integer(0);
      }

      this.commonAttributes.unset(key);
      return i;
   }

   public RtfAttributes getCommonAttributes() {
      return this.commonAttributes;
   }

   public int getSpaceBefore() {
      return this.spaceBefore;
   }

   public void setSpaceBeforeCandidate(RtfAttributes candidate) {
      if (this.updatingSpaceBefore) {
         this.spaceBeforeCandidate = candidate;
      }

   }

   public void setSpaceAfterCandidate(RtfAttributes candidate) {
      this.spaceAfterCandidate = candidate;
   }

   public boolean isBeforeCadidateSet() {
      return this.spaceBeforeCandidate != null;
   }

   public boolean isAfterCadidateSet() {
      return this.spaceAfterCandidate != null;
   }

   public void stopUpdatingSpaceBefore() {
      this.updatingSpaceBefore = false;
   }

   public int flush() {
      int accumulatingSpace = 0;
      if (!this.isBeforeCadidateSet()) {
         accumulatingSpace += this.spaceBefore;
      } else {
         this.spaceBeforeCandidate.addIntegerValue(this.spaceBefore, "sb");
      }

      if (!this.isAfterCadidateSet()) {
         accumulatingSpace += this.spaceAfter;
      } else {
         this.spaceAfterCandidate.addIntegerValue(this.spaceAfter, "sa");
      }

      return accumulatingSpace;
   }
}
