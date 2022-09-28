package org.apache.fop.area;

public class Block extends BlockParent {
   public static final int STACK = 0;
   public static final int RELATIVE = 1;
   public static final int ABSOLUTE = 2;
   public static final int FIXED = 3;
   private int stacking = 2;
   private int positioning = 0;
   protected transient boolean allowBPDUpdate = true;

   public void addBlock(Block block) {
      this.addBlock(block, true);
   }

   public void addBlock(Block block, boolean autoHeight) {
      if (autoHeight && this.allowBPDUpdate && block.isStacked()) {
         this.bpd += block.getAllocBPD();
      }

      this.addChildArea(block);
   }

   public void addLineArea(LineArea line) {
      this.bpd += line.getAllocBPD();
      this.addChildArea(line);
   }

   public void setPositioning(int pos) {
      this.positioning = pos;
   }

   public int getPositioning() {
      return this.positioning;
   }

   public boolean isStacked() {
      return this.getPositioning() == 0 || this.getPositioning() == 1;
   }

   public int getStartIndent() {
      Integer startIndent = (Integer)this.getTrait(Trait.START_INDENT);
      return startIndent != null ? startIndent : 0;
   }
}
