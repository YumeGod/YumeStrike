package sleep.bridges;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import sleep.engine.Block;
import sleep.engine.CallRequest;
import sleep.interfaces.Function;
import sleep.interfaces.Variable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;

public class SleepClosure implements Function, Runnable {
   private static int ccount = -1;
   private int id;
   Block code;
   ScriptInstance owner;
   Stack context;
   HashMap metadata;
   Variable variables;

   public Iterator scalarIterator() {
      return new ClosureIterator();
   }

   public void putMetadata(Object var1, Object var2) {
      this.metadata.put(var1, var2);
   }

   public Object getAndRemoveMetadata(Object var1, Object var2) {
      Object var3 = this.metadata.remove(var1);
      return var3 == null ? var2 : var3;
   }

   private void saveToplevelContext(Stack var1, LinkedList var2) {
      if (!var1.isEmpty()) {
         var1.push(var2);
         this.context.push(var1);
      } else if (var2.size() != 1) {
         throw new RuntimeException(var2.size() - 1 + " unaccounted local stack frame(s) in " + this.toString() + " (perhaps you forgot to &popl?)");
      }

   }

   private Stack getToplevelContext() {
      return this.context.isEmpty() ? new Stack() : (Stack)this.context.pop();
   }

   public String toStringGeneric() {
      return "&closure[" + this.code.getSourceLocation() + "]";
   }

   public String toString() {
      return this.toStringGeneric() + "#" + this.id;
   }

   private SleepClosure() {
   }

   public SleepClosure(ScriptInstance var1, Block var2) {
      this(var1, var2, var1.getScriptVariables().getGlobalVariables().createInternalVariableContainer());
   }

   public SleepClosure(ScriptInstance var1, Block var2, Variable var3) {
      this.code = var2;
      this.owner = var1;
      this.context = new Stack();
      this.metadata = new HashMap();
      var3.putScalar("$this", SleepUtils.getScalar((Object)this));
      this.setVariables(var3);
      ccount = (ccount + 1) % 32767;
      this.id = ccount;
   }

   public ScriptInstance getOwner() {
      return this.owner;
   }

   public Block getRunnableCode() {
      return this.code;
   }

   public Variable getVariables() {
      return this.variables;
   }

   public void setVariables(Variable var1) {
      this.variables = var1;
   }

   public void run() {
      this.callClosure("run", (ScriptInstance)null, (Stack)null);
   }

   public Scalar callClosure(String var1, ScriptInstance var2, Stack var3) {
      if (var2 == null) {
         var2 = this.getOwner();
      }

      if (var3 == null) {
         var3 = new Stack();
      }

      var2.getScriptEnvironment().pushSource("<internal>");
      var2.getScriptEnvironment().CreateFrame();
      var2.getScriptEnvironment().CreateFrame(var3);
      CallRequest.ClosureCallRequest var4 = new CallRequest.ClosureCallRequest(var2.getScriptEnvironment(), -1, SleepUtils.getScalar((Object)this), var1);
      var4.CallFunction();
      Scalar var5 = var2.getScriptEnvironment().getCurrentFrame().isEmpty() ? SleepUtils.getEmptyScalar() : (Scalar)var2.getScriptEnvironment().getCurrentFrame().pop();
      var2.getScriptEnvironment().KillFrame();
      var2.getScriptEnvironment().clearReturn();
      var2.getScriptEnvironment().popSource();
      return var5;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (this.owner == null) {
         this.owner = var2;
      }

      ScriptVariables var4 = var2.getScriptVariables();
      ScriptEnvironment var5 = var2.getScriptEnvironment();
      synchronized(var4) {
         Stack var9 = this.getToplevelContext();
         var5.loadContext(var9, this.metadata);
         var4.pushClosureLevel(this.getVariables());
         LinkedList var10;
         if (var9.isEmpty()) {
            var4.beginToplevel(new LinkedList());
            var4.pushLocalLevel();
         } else {
            var10 = (LinkedList)var9.pop();
            var4.beginToplevel(var10);
         }

         Variable var6 = var4.getLocalVariables();
         var4.setScalarLevel("$0", SleepUtils.getScalar(var1), var6);
         BridgeUtilities.initLocalScope(var4, var6, var3);
         Scalar var7;
         if (var9.isEmpty()) {
            var7 = this.code.evaluate(var5);
         } else {
            var7 = var5.evaluateOldContext();
         }

         var10 = var4.leaveToplevel();
         var4.popClosureLevel();
         if (var2.getScriptEnvironment().isCallCC()) {
            SleepClosure var11 = SleepUtils.getFunctionFromScalar(var2.getScriptEnvironment().getReturnValue(), var2);
            var11.putMetadata("continuation", SleepUtils.getScalar((Object)this));
            var11.putMetadata("sourceLine", var2.getScriptEnvironment().getCurrentFrame().pop());
            var11.putMetadata("sourceFile", var2.getScriptEnvironment().getCurrentFrame().pop());
            var2.getScriptEnvironment().flagReturn(var2.getScriptEnvironment().getReturnValue(), 128);
         }

         this.saveToplevelContext(var5.saveContext(), var10);
         return var7;
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeInt(this.id);
      var1.writeObject(this.code);
      var1.writeObject(this.context);
      var1.writeObject(this.variables);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.id = var1.readInt();
      this.code = (Block)var1.readObject();
      this.context = (Stack)var1.readObject();
      this.metadata = new HashMap();
      this.variables = (Variable)var1.readObject();
      this.owner = null;
   }

   private class ClosureIterator implements Iterator {
      protected Scalar current;
      protected Stack locals;

      private ClosureIterator() {
         this.locals = new Stack();
      }

      public boolean hasNext() {
         this.current = SleepClosure.this.callClosure("eval", (ScriptInstance)null, this.locals);
         return !SleepUtils.isEmptyScalar(this.current);
      }

      public Object next() {
         return this.current;
      }

      public void remove() {
      }

      // $FF: synthetic method
      ClosureIterator(Object var2) {
         this();
      }
   }
}
