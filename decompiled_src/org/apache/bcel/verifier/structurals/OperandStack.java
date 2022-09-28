package org.apache.bcel.verifier.structurals;

import java.util.ArrayList;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;

public class OperandStack {
   private ArrayList stack = new ArrayList();
   private int maxStack;

   public OperandStack(int maxStack) {
      this.maxStack = maxStack;
   }

   public OperandStack(int maxStack, ObjectType obj) {
      this.maxStack = maxStack;
      this.push(obj);
   }

   protected Object clone() {
      OperandStack newstack = new OperandStack(this.maxStack);
      newstack.stack = (ArrayList)this.stack.clone();
      return newstack;
   }

   public void clear() {
      this.stack = new ArrayList();
   }

   public boolean equals(Object o) {
      if (!(o instanceof OperandStack)) {
         return false;
      } else {
         OperandStack s = (OperandStack)o;
         return this.stack.equals(s.stack);
      }
   }

   public OperandStack getClone() {
      return (OperandStack)this.clone();
   }

   public boolean isEmpty() {
      return this.stack.isEmpty();
   }

   public int maxStack() {
      return this.maxStack;
   }

   public Type peek() {
      return this.peek(0);
   }

   public Type peek(int i) {
      return (Type)this.stack.get(this.size() - i - 1);
   }

   public Type pop() {
      Type e = (Type)this.stack.remove(this.size() - 1);
      return e;
   }

   public Type pop(int i) {
      for(int j = 0; j < i; ++j) {
         this.pop();
      }

      return null;
   }

   public void push(Type type) {
      if (type == null) {
         throw new AssertionViolatedException("Cannot push NULL onto OperandStack.");
      } else if (type != Type.BOOLEAN && type != Type.CHAR && type != Type.BYTE && type != Type.SHORT) {
         if (this.slotsUsed() >= this.maxStack) {
            throw new AssertionViolatedException("OperandStack too small, should have thrown proper Exception elsewhere. Stack: " + this);
         } else {
            this.stack.add(type);
         }
      } else {
         throw new AssertionViolatedException("The OperandStack does not know about '" + type + "'; use Type.INT instead.");
      }
   }

   int size() {
      return this.stack.size();
   }

   public int slotsUsed() {
      int slots = 0;

      for(int i = 0; i < this.stack.size(); ++i) {
         slots += this.peek(i).getSize();
      }

      return slots;
   }

   public String toString() {
      String s = "Slots used: " + this.slotsUsed() + " MaxStack: " + this.maxStack + ".\n";

      for(int i = 0; i < this.size(); ++i) {
         s = s + this.peek(i) + " (Size: " + this.peek(i).getSize() + ")\n";
      }

      return s;
   }

   public void merge(OperandStack s) {
      if (this.slotsUsed() == s.slotsUsed() && this.size() == s.size()) {
         for(int i = 0; i < this.size(); ++i) {
            if (!(this.stack.get(i) instanceof UninitializedObjectType) && s.stack.get(i) instanceof UninitializedObjectType) {
               throw new StructuralCodeConstraintException("Backwards branch with an uninitialized object on the stack detected.");
            }

            if (!this.stack.get(i).equals(s.stack.get(i)) && this.stack.get(i) instanceof UninitializedObjectType && !(s.stack.get(i) instanceof UninitializedObjectType)) {
               throw new StructuralCodeConstraintException("Backwards branch with an uninitialized object on the stack detected.");
            }

            if (this.stack.get(i) instanceof UninitializedObjectType && !(s.stack.get(i) instanceof UninitializedObjectType)) {
               this.stack.set(i, ((UninitializedObjectType)this.stack.get(i)).getInitialized());
            }

            if (!this.stack.get(i).equals(s.stack.get(i))) {
               if (!(this.stack.get(i) instanceof ReferenceType) || !(s.stack.get(i) instanceof ReferenceType)) {
                  throw new StructuralCodeConstraintException("Cannot merge stacks of different types:\nStack A:\n" + this + "\nStack B:\n" + s);
               }

               this.stack.set(i, ((ReferenceType)this.stack.get(i)).firstCommonSuperclass((ReferenceType)s.stack.get(i)));
            }
         }

      } else {
         throw new StructuralCodeConstraintException("Cannot merge stacks of different size:\nOperandStack A:\n" + this + "\nOperandStack B:\n" + s);
      }
   }

   public void initializeObject(UninitializedObjectType u) {
      for(int i = 0; i < this.stack.size(); ++i) {
         if (this.stack.get(i) == u) {
            this.stack.set(i, u.getInitialized());
         }
      }

   }
}
