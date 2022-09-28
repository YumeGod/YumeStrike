package sleep.error;

import java.io.File;
import sleep.runtime.ScriptInstance;

public class ScriptWarning {
   protected ScriptInstance script;
   protected String message;
   protected int line;
   protected boolean trace;
   protected String source;

   public ScriptWarning(ScriptInstance var1, String var2, int var3) {
      this(var1, var2, var3, false);
   }

   public ScriptWarning(ScriptInstance var1, String var2, int var3, boolean var4) {
      this.script = var1;
      this.message = var2;
      this.line = var3;
      this.trace = var4;
      this.source = this.script.getScriptEnvironment().getCurrentSource();
   }

   public boolean isDebugTrace() {
      return this.trace;
   }

   public ScriptInstance getSource() {
      return this.script;
   }

   public String toString() {
      return this.isDebugTrace() ? "Trace: " + this.getMessage() + " at " + this.getNameShort() + ":" + this.getLineNumber() : "Warning: " + this.getMessage() + " at " + this.getNameShort() + ":" + this.getLineNumber();
   }

   public String getMessage() {
      return this.message;
   }

   public int getLineNumber() {
      return this.line;
   }

   public String getScriptName() {
      return this.source;
   }

   public String getNameShort() {
      return (new File(this.getScriptName())).getName();
   }
}
