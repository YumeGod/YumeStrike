package sleep.engine;

import java.util.List;
import sleep.engine.atoms.Assign;
import sleep.engine.atoms.AssignT;
import sleep.engine.atoms.Bind;
import sleep.engine.atoms.BindFilter;
import sleep.engine.atoms.BindPredicate;
import sleep.engine.atoms.Call;
import sleep.engine.atoms.Check;
import sleep.engine.atoms.CheckAnd;
import sleep.engine.atoms.CheckEval;
import sleep.engine.atoms.CheckOr;
import sleep.engine.atoms.CreateClosure;
import sleep.engine.atoms.CreateFrame;
import sleep.engine.atoms.Decide;
import sleep.engine.atoms.Get;
import sleep.engine.atoms.Goto;
import sleep.engine.atoms.Index;
import sleep.engine.atoms.Iterate;
import sleep.engine.atoms.ObjectAccess;
import sleep.engine.atoms.ObjectNew;
import sleep.engine.atoms.Operate;
import sleep.engine.atoms.PLiteral;
import sleep.engine.atoms.PopTry;
import sleep.engine.atoms.Return;
import sleep.engine.atoms.SValue;
import sleep.engine.atoms.Try;
import sleep.runtime.Scalar;

public class GeneratedSteps {
   public Step PopTry() {
      PopTry var1 = new PopTry();
      return var1;
   }

   public Step Try(Block var1, Block var2, String var3) {
      Try var4 = new Try(var1, var2, var3);
      return var4;
   }

   public Step Operate(String var1) {
      Operate var2 = new Operate(var1);
      return var2;
   }

   public Step Return(int var1) {
      Return var2 = new Return(var1);
      return var2;
   }

   public Step SValue(Scalar var1) {
      SValue var2 = new SValue(var1);
      return var2;
   }

   public Step IteratorCreate(String var1, String var2) {
      return new Iterate(var1, var2, 1);
   }

   public Step IteratorNext() {
      return new Iterate((String)null, (String)null, 3);
   }

   public Step IteratorDestroy() {
      return new Iterate((String)null, (String)null, 2);
   }

   public Check Check(String var1, Block var2) {
      CheckEval var3 = new CheckEval(var1, var2);
      return var3;
   }

   public Check CheckAnd(Check var1, Check var2) {
      CheckAnd var3 = new CheckAnd(var1, var2);
      return var3;
   }

   public Check CheckOr(Check var1, Check var2) {
      CheckOr var3 = new CheckOr(var1, var2);
      return var3;
   }

   public Step Goto(Check var1, Block var2, Block var3) {
      Goto var4 = new Goto(var1);
      var4.setChoices(var2);
      var4.setIncrement(var3);
      return var4;
   }

   public Step Decide(Check var1, Block var2, Block var3) {
      Decide var4 = new Decide(var1);
      var4.setChoices(var2, var3);
      return var4;
   }

   public Step PLiteral(List var1) {
      PLiteral var2 = new PLiteral(var1);
      return var2;
   }

   public Step Assign(Block var1) {
      Assign var2 = new Assign(var1);
      return var2;
   }

   public Step AssignAndOperate(Block var1, String var2) {
      Assign var3 = new Assign(var1, this.Operate(var2));
      return var3;
   }

   public Step AssignT() {
      AssignT var1 = new AssignT();
      return var1;
   }

   public Step AssignTupleAndOperate(String var1) {
      AssignT var2 = new AssignT(this.Operate(var1));
      return var2;
   }

   public Step CreateFrame() {
      CreateFrame var1 = new CreateFrame();
      return var1;
   }

   public Step Get(String var1) {
      Get var2 = new Get(var1);
      return var2;
   }

   public Step Index(String var1, Block var2) {
      Index var3 = new Index(var1, var2);
      return var3;
   }

   public Step Call(String var1) {
      Call var2 = new Call(var1);
      return var2;
   }

   public Step CreateClosure(Block var1) {
      CreateClosure var2 = new CreateClosure(var1);
      return var2;
   }

   public Step Bind(String var1, Block var2, Block var3) {
      Bind var4 = new Bind(var1, var2, var3);
      return var4;
   }

   public Step BindPredicate(String var1, Check var2, Block var3) {
      BindPredicate var4 = new BindPredicate(var1, var2, var3);
      return var4;
   }

   public Step BindFilter(String var1, String var2, Block var3, String var4) {
      BindFilter var5 = new BindFilter(var1, var2, var3, var4);
      return var5;
   }

   public Step ObjectNew(Class var1) {
      ObjectNew var2 = new ObjectNew(var1);
      return var2;
   }

   public Step ObjectAccess(String var1) {
      ObjectAccess var2 = new ObjectAccess(var1, (Class)null);
      return var2;
   }

   public Step ObjectAccessStatic(Class var1, String var2) {
      ObjectAccess var3 = new ObjectAccess(var2, var1);
      return var3;
   }
}
