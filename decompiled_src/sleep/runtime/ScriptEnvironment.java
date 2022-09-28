package sleep.runtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import sleep.engine.Block;
import sleep.engine.Step;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Environment;
import sleep.interfaces.FilterEnvironment;
import sleep.interfaces.Function;
import sleep.interfaces.Operator;
import sleep.interfaces.Predicate;
import sleep.interfaces.PredicateEnvironment;

public class ScriptEnvironment implements Serializable {
   protected ScriptInstance self;
   protected Stack environmentStack;
   protected Hashtable environment;
   protected Object errorMessage = null;
   protected Stack context = new Stack();
   protected Stack contextStack = new Stack();
   protected HashMap metadata = new HashMap();
   protected Stack metaStack = new Stack();
   protected ExceptionContext currentHandler = null;
   protected Stack exhandlers = new Stack();
   protected boolean moreHandlers = false;
   public static final int FLOW_CONTROL_NONE = 0;
   public static final int FLOW_CONTROL_RETURN = 1;
   public static final int FLOW_CONTROL_BREAK = 2;
   public static final int FLOW_CONTROL_CONTINUE = 4;
   public static final int FLOW_CONTROL_YIELD = 8;
   public static final int FLOW_CONTROL_THROW = 16;
   public static final int FLOW_CONTROL_DEBUG = 32;
   public static final int FLOW_CONTROL_CALLCC = 72;
   public static final int FLOW_CONTROL_PASS = 128;
   protected String debugString = "";
   protected Scalar rv = null;
   protected int request = 0;
   protected Stack sources = new Stack();
   protected ArrayList frames = new ArrayList(10);
   protected int findex = -1;

   public ScriptEnvironment() {
      this.self = null;
      this.environment = null;
      this.environmentStack = new Stack();
   }

   public ScriptEnvironment(Hashtable var1, ScriptInstance var2) {
      this.self = var2;
      this.environment = var1;
      this.environmentStack = new Stack();
   }

   public ScriptInstance getScriptInstance() {
      return this.self;
   }

   public void flagError(Object var1) {
      this.errorMessage = var1;
      if ((this.getScriptInstance().getDebugFlags() & 2) == 2) {
         if ((this.getScriptInstance().getDebugFlags() & 34) == 34) {
            this.flagReturn(this.checkError(), 16);
         } else {
            this.showDebugMessage("checkError(): " + var1);
         }
      }

   }

   public Scalar checkError() {
      Scalar var1 = SleepUtils.getScalar(this.errorMessage);
      this.errorMessage = null;
      return var1;
   }

   public ScriptVariables getScriptVariables() {
      return this.getScriptInstance().getScriptVariables();
   }

   public Scalar getScalar(String var1) {
      return this.getScriptVariables().getScalar(var1, this.getScriptInstance());
   }

   public void putScalar(String var1, Scalar var2) {
      this.getScriptVariables().putScalar(var1, var2);
   }

   public Block getBlock(String var1) {
      return (Block)((Block)this.getEnvironment().get("^" + var1));
   }

   public Function getFunction(String var1) {
      return (Function)((Function)this.getEnvironment().get(var1));
   }

   public Environment getFunctionEnvironment(String var1) {
      return (Environment)((Environment)this.getEnvironment().get(var1));
   }

   public PredicateEnvironment getPredicateEnvironment(String var1) {
      return (PredicateEnvironment)((PredicateEnvironment)this.getEnvironment().get(var1));
   }

   public FilterEnvironment getFilterEnvironment(String var1) {
      return (FilterEnvironment)((FilterEnvironment)this.getEnvironment().get(var1));
   }

   public Predicate getPredicate(String var1) {
      return (Predicate)((Predicate)this.getEnvironment().get(var1));
   }

   public Operator getOperator(String var1) {
      return (Operator)((Operator)this.getEnvironment().get(var1));
   }

   public Hashtable getEnvironment() {
      return this.environment;
   }

   public void setEnvironment(Hashtable var1) {
      this.environment = var1;
   }

   public Stack getEnvironmentStack() {
      return this.environmentStack;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ScriptInstance -- " + this.getScriptInstance());
      var1.append("Misc Environment:\n");
      var1.append(this.getEnvironment().toString());
      var1.append("\nEnvironment Stack:\n");
      var1.append(this.getEnvironmentStack().toString());
      var1.append("Return Stuff: " + this.rv);
      return var1.toString();
   }

   public void loadContext(Stack var1, HashMap var2) {
      this.contextStack.push(this.context);
      this.metaStack.push(this.metadata);
      this.context = var1;
      this.metadata = var2;
   }

   public void setContextMetadata(Object var1, Object var2) {
      if (var2 == null) {
         this.metadata.remove(var1);
      } else {
         this.metadata.put(var1, var2);
      }

   }

   public Object getContextMetadata(Object var1) {
      return this.metadata.get(var1);
   }

   public Object getContextMetadata(Object var1, Object var2) {
      Object var3 = this.metadata.get(var1);
      return var3 == null ? var2 : this.metadata.get(var1);
   }

   public void addToContext(Block var1, Step var2) {
      Context var3 = new Context();
      var3.block = var1;
      var3.last = var2;
      if (this.isResponsible(var1)) {
         var3.handler = this.popExceptionContext();

         Context var5;
         for(Iterator var4 = this.context.iterator(); var4.hasNext(); var5.moreHandlers = true) {
            var5 = (Context)var4.next();
         }
      } else {
         var3.moreHandlers = this.moreHandlers;
      }

      this.context.add(var3);
   }

   public Scalar evaluateOldContext() {
      Scalar var1 = SleepUtils.getEmptyScalar();
      Stack var2 = this.context;
      this.context = new Stack();
      Iterator var3 = var2.iterator();

      while(true) {
         do {
            do {
               if (!var3.hasNext()) {
                  this.moreHandlers = false;
                  return var1;
               }

               Context var4 = (Context)var3.next();
               if (var4.handler != null) {
                  this.installExceptionHandler(var4.handler);
               }

               this.moreHandlers = var4.moreHandlers;
               var1 = var4.block.evaluate(this, var4.last);
            } while(!this.isReturn());
         } while(!this.isYield());

         while(var3.hasNext()) {
            this.context.add(var3.next());
         }
      }
   }

   public Stack saveContext() {
      Stack var1 = this.context;
      this.context = (Stack)((Stack)this.contextStack.pop());
      this.metadata = (HashMap)((HashMap)this.metaStack.pop());
      return var1;
   }

   public boolean isExceptionHandlerInstalled() {
      return this.currentHandler != null || this.moreHandlers;
   }

   public boolean isResponsible(Block var1) {
      return this.currentHandler != null && this.currentHandler.owner == var1;
   }

   public void installExceptionHandler(ExceptionContext var1) {
      if (this.currentHandler != null) {
         this.exhandlers.push(this.currentHandler);
      }

      this.currentHandler = var1;
   }

   public void installExceptionHandler(Block var1, Block var2, String var3) {
      ExceptionContext var4 = new ExceptionContext();
      var4.owner = var1;
      var4.handler = var2;
      var4.varname = var3;
      this.installExceptionHandler(var4);
   }

   public Scalar getExceptionMessage() {
      this.request &= -17;
      Scalar var1 = this.rv;
      this.rv = null;
      return var1;
   }

   public Block getExceptionHandler() {
      this.request &= -17;
      Block var1 = this.currentHandler.handler;
      Scalar var2 = this.getScriptVariables().getScalar(this.currentHandler.varname, this.getScriptInstance());
      if (var2 != null) {
         var2.setValue(this.rv);
      } else {
         this.putScalar(this.currentHandler.varname, this.rv);
      }

      this.rv = null;
      return var1;
   }

   public ExceptionContext popExceptionContext() {
      ExceptionContext var1 = this.currentHandler;
      if (this.exhandlers.isEmpty()) {
         this.currentHandler = null;
      } else {
         this.currentHandler = (ExceptionContext)this.exhandlers.pop();
      }

      return var1;
   }

   public boolean isThrownValue() {
      return (this.request & 16) == 16;
   }

   public boolean isDebugInterrupt() {
      return (this.request & 32) == 32;
   }

   public boolean isYield() {
      return (this.request & 8) == 8;
   }

   public boolean isCallCC() {
      return (this.request & 72) == 72;
   }

   public boolean isPassControl() {
      return (this.request & 128) == 128;
   }

   public Scalar getReturnValue() {
      return this.rv;
   }

   public boolean isReturn() {
      return this.request != 0;
   }

   public int getFlowControlRequest() {
      return this.request;
   }

   public String getDebugString() {
      this.request &= -33;
      return this.debugString;
   }

   public void showDebugMessage(String var1) {
      this.request |= 32;
      this.debugString = var1;
   }

   public void flagReturn(Scalar var1, int var2) {
      if (var1 == null) {
         var1 = SleepUtils.getEmptyScalar();
      }

      this.rv = var1;
      this.request = var2;
   }

   public void resetEnvironment() {
      this.errorMessage = null;
      this.request = 0;
      this.rv = null;
      this.getScriptInstance().clearStackTrace();
   }

   public void clearReturn() {
      this.request = 0 | this.request & 176;
      if (!this.isThrownValue() && !this.isPassControl()) {
         this.rv = null;
      }

   }

   public void pushSource(String var1) {
      this.sources.push(var1);
   }

   public String getCurrentSource() {
      return !this.sources.isEmpty() ? this.sources.peek() + "" : "unknown";
   }

   public void popSource() {
      this.sources.pop();
   }

   public int markFrame() {
      return this.findex;
   }

   public void cleanFrame(int var1) {
      while(this.findex > var1) {
         this.KillFrame();
      }

   }

   public Stack getCurrentFrame() {
      return (Stack)this.frames.get(this.findex);
   }

   public void FrameResult(Scalar var1) {
      this.KillFrame();
      if (this.findex >= 0) {
         this.getCurrentFrame().push(var1);
      }

   }

   public boolean hasFrame() {
      return this.findex >= 0;
   }

   public void KillFrame() {
      this.getCurrentFrame().clear();
      --this.findex;
   }

   public void CreateFrame(Stack var1) {
      if (var1 == null) {
         var1 = new Stack();
      }

      if (this.findex + 1 >= this.frames.size()) {
         this.frames.add(var1);
      } else {
         this.frames.set(this.findex + 1, var1);
      }

      ++this.findex;
   }

   public void CreateFrame() {
      if (this.findex + 1 >= this.frames.size()) {
         this.frames.add(new Stack());
      }

      ++this.findex;
   }

   public Scalar evaluateStatement(String var1) throws YourCodeSucksException {
      return SleepUtils.runCode(SleepUtils.ParseCode(var1), this);
   }

   public boolean evaluatePredicate(String var1) throws YourCodeSucksException {
      var1 = "if (" + var1 + ") { return 1; } else { return $null; }";
      return SleepUtils.runCode(SleepUtils.ParseCode(var1), this).intValue() == 1;
   }

   public Scalar evaluateExpression(String var1) throws YourCodeSucksException {
      var1 = "return (" + var1 + ");";
      return SleepUtils.runCode(SleepUtils.ParseCode(var1), this);
   }

   public Scalar evaluateParsedLiteral(String var1) throws YourCodeSucksException {
      var1 = "return \"" + var1 + "\";";
      return SleepUtils.runCode(SleepUtils.ParseCode(var1), this);
   }

   protected static class ExceptionContext implements Serializable {
      public Block owner;
      public String varname;
      public Block handler;
   }

   protected static class Context implements Serializable {
      public Block block;
      public Step last;
      public ExceptionContext handler;
      public boolean moreHandlers;
   }
}
