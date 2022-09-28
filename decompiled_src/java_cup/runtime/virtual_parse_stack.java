package java_cup.runtime;

import java.util.Stack;

public class virtual_parse_stack {
   protected Stack real_stack;
   protected int real_next;
   protected Stack vstack;

   public virtual_parse_stack(Stack var1) throws Exception {
      if (var1 == null) {
         throw new Exception("Internal parser error: attempt to create null virtual stack");
      } else {
         this.real_stack = var1;
         this.vstack = new Stack();
         this.real_next = 0;
         this.get_from_real();
      }
   }

   public boolean empty() {
      return this.vstack.empty();
   }

   protected void get_from_real() {
      if (this.real_next < this.real_stack.size()) {
         Symbol var1 = (Symbol)this.real_stack.elementAt(this.real_stack.size() - 1 - this.real_next);
         ++this.real_next;
         this.vstack.push(new Integer(var1.parse_state));
      }
   }

   public void pop() throws Exception {
      if (this.vstack.empty()) {
         throw new Exception("Internal parser error: pop from empty virtual stack");
      } else {
         this.vstack.pop();
         if (this.vstack.empty()) {
            this.get_from_real();
         }

      }
   }

   public void push(int var1) {
      this.vstack.push(new Integer(var1));
   }

   public int top() throws Exception {
      if (this.vstack.empty()) {
         throw new Exception("Internal parser error: top() called on empty virtual stack");
      } else {
         return (Integer)this.vstack.peek();
      }
   }
}
