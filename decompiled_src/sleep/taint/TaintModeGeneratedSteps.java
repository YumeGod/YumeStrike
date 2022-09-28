package sleep.taint;

import java.util.List;
import sleep.engine.GeneratedSteps;
import sleep.engine.Step;

public class TaintModeGeneratedSteps extends GeneratedSteps {
   public Step Call(String var1) {
      return new TaintCall(var1, super.Call(var1));
   }

   public Step PLiteral(List var1) {
      return new PermeableStep(super.PLiteral(var1));
   }

   public Step Operate(String var1) {
      return new TaintOperate(var1, super.Operate(var1));
   }

   public Step ObjectNew(Class var1) {
      return new PermeableStep(super.ObjectNew(var1));
   }

   public Step ObjectAccess(String var1) {
      return new TaintObjectAccess(super.ObjectAccess(var1), var1, (Class)null);
   }

   public Step ObjectAccessStatic(Class var1, String var2) {
      return new TaintObjectAccess(super.ObjectAccessStatic(var1, var2), var2, var1);
   }
}
