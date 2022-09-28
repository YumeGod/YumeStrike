package org.apache.fop.text.linebreak;

public class LineBreakStatus {
   public static final byte DIRECT_BREAK = 0;
   public static final byte INDIRECT_BREAK = 1;
   public static final byte COMBINING_INDIRECT_BREAK = 2;
   public static final byte COMBINING_PROHIBITED_BREAK = 3;
   public static final byte PROHIBITED_BREAK = 4;
   public static final byte EXPLICIT_BREAK = 5;
   private byte leftClass;
   private boolean hadSpace;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public LineBreakStatus() {
      this.reset();
   }

   public void reset() {
      this.leftClass = -1;
      this.hadSpace = false;
   }

   public byte nextChar(char var1) {
      // $FF: Couldn't be decompiled
   }

   static {
      $assertionsDisabled = !LineBreakStatus.class.desiredAssertionStatus();
   }
}
