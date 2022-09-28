package org.apache.bcel.generic;

public final class SWITCH implements CompoundInstruction {
   private int[] match;
   private InstructionHandle[] targets;
   private Select instruction;
   private int match_length;

   public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target, int max_gap) {
      this.match = (int[])match.clone();
      this.targets = (InstructionHandle[])targets.clone();
      if ((this.match_length = match.length) < 2) {
         this.instruction = new TABLESWITCH(match, targets, target);
      } else {
         this.sort(0, this.match_length - 1);
         if (this.matchIsOrdered(max_gap)) {
            this.fillup(max_gap, target);
            this.instruction = new TABLESWITCH(this.match, this.targets, target);
         } else {
            this.instruction = new LOOKUPSWITCH(this.match, this.targets, target);
         }
      }

   }

   public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
      this(match, targets, target, 1);
   }

   private final void fillup(int max_gap, InstructionHandle target) {
      int max_size = this.match_length + this.match_length * max_gap;
      int[] m_vec = new int[max_size];
      InstructionHandle[] t_vec = new InstructionHandle[max_size];
      int count = 1;
      m_vec[0] = this.match[0];
      t_vec[0] = this.targets[0];

      for(int i = 1; i < this.match_length; ++i) {
         int prev = this.match[i - 1];
         int gap = this.match[i] - prev;

         for(int j = 1; j < gap; ++j) {
            m_vec[count] = prev + j;
            t_vec[count] = target;
            ++count;
         }

         m_vec[count] = this.match[i];
         t_vec[count] = this.targets[i];
         ++count;
      }

      this.match = new int[count];
      this.targets = new InstructionHandle[count];
      System.arraycopy(m_vec, 0, this.match, 0, count);
      System.arraycopy(t_vec, 0, this.targets, 0, count);
   }

   private final void sort(int l, int r) {
      int i = l;
      int j = r;
      int m = this.match[(l + r) / 2];

      do {
         while(this.match[i] < m) {
            ++i;
         }

         while(m < this.match[j]) {
            --j;
         }

         if (i <= j) {
            int h = this.match[i];
            this.match[i] = this.match[j];
            this.match[j] = h;
            InstructionHandle h2 = this.targets[i];
            this.targets[i] = this.targets[j];
            this.targets[j] = h2;
            ++i;
            --j;
         }
      } while(i <= j);

      if (l < j) {
         this.sort(l, j);
      }

      if (i < r) {
         this.sort(i, r);
      }

   }

   private final boolean matchIsOrdered(int max_gap) {
      for(int i = 1; i < this.match_length; ++i) {
         if (this.match[i] - this.match[i - 1] > max_gap) {
            return false;
         }
      }

      return true;
   }

   public final InstructionList getInstructionList() {
      return new InstructionList(this.instruction);
   }

   public final Instruction getInstruction() {
      return this.instruction;
   }
}
