package org.apache.fop.area;

public class NormalFlow extends BlockParent {
   public NormalFlow(int ipd) {
      this.addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
      this.setIPD(ipd);
   }

   public void addBlock(Block block) {
      super.addBlock(block);
      if (block.isStacked()) {
         this.bpd += block.getAllocBPD();
      }

   }
}
