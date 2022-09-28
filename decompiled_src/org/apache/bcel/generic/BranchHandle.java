package org.apache.bcel.generic;

public final class BranchHandle extends InstructionHandle {
   private BranchInstruction bi;
   private static BranchHandle bh_list = null;

   private BranchHandle(BranchInstruction i) {
      super(i);
      this.bi = i;
   }

   static final BranchHandle getBranchHandle(BranchInstruction i) {
      if (bh_list == null) {
         return new BranchHandle(i);
      } else {
         BranchHandle bh = bh_list;
         bh_list = (BranchHandle)bh.next;
         bh.setInstruction(i);
         return bh;
      }
   }

   protected void addHandle() {
      super.next = bh_list;
      bh_list = this;
   }

   public int getPosition() {
      return this.bi.position;
   }

   void setPosition(int pos) {
      super.i_position = this.bi.position = pos;
   }

   protected int updatePosition(int offset, int max_offset) {
      int x = this.bi.updatePosition(offset, max_offset);
      super.i_position = this.bi.position;
      return x;
   }

   public void setTarget(InstructionHandle ih) {
      this.bi.setTarget(ih);
   }

   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
      this.bi.updateTarget(old_ih, new_ih);
   }

   public InstructionHandle getTarget() {
      return this.bi.getTarget();
   }

   public void setInstruction(Instruction i) {
      super.setInstruction(i);
      if (!(i instanceof BranchInstruction)) {
         throw new ClassGenException("Assigning " + i + " to branch handle which is not a branch instruction");
      } else {
         this.bi = (BranchInstruction)i;
      }
   }
}
