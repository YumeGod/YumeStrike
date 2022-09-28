package sleep.runtime;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Stack;
import sleep.bridges.DefaultVariable;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Variable;

public class ScriptVariables implements Serializable {
   protected Variable global;
   protected LinkedList closure;
   protected LinkedList locals;
   protected Stack marks;

   public void beginToplevel(LinkedList var1) {
      this.marks.push(this.locals);
      this.locals = var1;
   }

   public LinkedList leaveToplevel() {
      LinkedList var1 = this.locals;
      this.locals = (LinkedList)this.marks.pop();
      return var1;
   }

   public boolean haveMoreLocals() {
      return this.locals.size() > 1;
   }

   public ScriptVariables() {
      this(new DefaultVariable());
   }

   public ScriptVariables(Variable var1) {
      this.global = var1;
      this.closure = new LinkedList();
      this.locals = new LinkedList();
      this.marks = new Stack();
   }

   public void putScalar(String var1, Scalar var2) {
      this.global.putScalar(var1, var2);
   }

   public Scalar getScalar(String var1) {
      return this.getScalar(var1, (ScriptInstance)null);
   }

   public Variable getScalarLevel(String var1, ScriptInstance var2) {
      Variable var3 = this.getLocalVariables();
      if (var3 != null && var3.scalarExists(var1)) {
         return var3;
      } else {
         var3 = this.getClosureVariables();
         if (var3 != null && var3.scalarExists(var1)) {
            return var3;
         } else {
            var3 = this.getGlobalVariables();
            return var3.scalarExists(var1) ? var3 : null;
         }
      }
   }

   public Scalar getScalar(String var1, ScriptInstance var2) {
      Variable var3 = this.getScalarLevel(var1, var2);
      return var3 != null ? var3.getScalar(var1) : null;
   }

   public void setScalarLevel(String var1, Scalar var2, Variable var3) {
      var3.putScalar(var1, var2);
   }

   public Variable getLocalVariables() {
      return this.locals.size() == 0 ? null : (Variable)this.locals.getFirst();
   }

   public Variable getClosureVariables() {
      return this.closure.size() == 0 ? null : (Variable)this.closure.getFirst();
   }

   public Variable getGlobalVariables() {
      return this.global;
   }

   public Variable getClosureVariables(SleepClosure var1) {
      return var1.getVariables();
   }

   public void setClosureVariables(SleepClosure var1, Variable var2) {
      var1.setVariables(var2);
   }

   public void pushClosureLevel(Variable var1) {
      this.closure.addFirst(var1);
   }

   public void popClosureLevel() {
      this.closure.removeFirst();
   }

   public void pushLocalLevel(Variable var1) {
      this.locals.addFirst(var1);
   }

   public void pushLocalLevel() {
      this.locals.addFirst(this.global.createLocalVariableContainer());
   }

   public void popLocalLevel() {
      this.locals.removeFirst();
   }
}
