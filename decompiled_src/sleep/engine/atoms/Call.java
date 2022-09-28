package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.CallRequest;
import sleep.engine.Step;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Call extends Step {
   String function;

   public Call(String var1) {
      this.function = var1;
   }

   public String toString(String var1) {
      return var1 + "[Function Call]: " + this.function + "\n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Function var2 = var1.getFunction(this.function);
      Block var3 = null;
      if (var2 != null) {
         CallRequest.FunctionCallRequest var4 = new CallRequest.FunctionCallRequest(var1, this.getLineNumber(), this.function, var2);
         var4.CallFunction();
      } else if ((var3 = var1.getBlock(this.function)) != null) {
         CallRequest.InlineCallRequest var5 = new CallRequest.InlineCallRequest(var1, this.getLineNumber(), this.function, var3);
         var5.CallFunction();
      } else {
         var1.getScriptInstance().fireWarning("Attempted to call non-existent function " + this.function, this.getLineNumber());
         var1.FrameResult(SleepUtils.getEmptyScalar());
      }

      return null;
   }
}
