package org.apache.bcel.verifier.structurals;

public class Frame {
   protected static UninitializedObjectType _this;
   private LocalVariables locals;
   private OperandStack stack;

   public Frame(int maxLocals, int maxStack) {
      this.locals = new LocalVariables(maxLocals);
      this.stack = new OperandStack(maxStack);
   }

   public Frame(LocalVariables locals, OperandStack stack) {
      this.locals = locals;
      this.stack = stack;
   }

   protected Object clone() {
      Frame f = new Frame(this.locals.getClone(), this.stack.getClone());
      return f;
   }

   public Frame getClone() {
      return (Frame)this.clone();
   }

   public LocalVariables getLocals() {
      return this.locals;
   }

   public OperandStack getStack() {
      return this.stack;
   }

   public boolean equals(Object o) {
      if (!(o instanceof Frame)) {
         return false;
      } else {
         Frame f = (Frame)o;
         return this.stack.equals(f.stack) && this.locals.equals(f.locals);
      }
   }

   public String toString() {
      String s = "Local Variables:\n";
      s = s + this.locals;
      s = s + "OperandStack:\n";
      s = s + this.stack;
      return s;
   }
}
