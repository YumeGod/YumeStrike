package common;

import java.io.Serializable;
import java.util.Map;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class ProfilerEvent implements Serializable, Transcript, Scriptable {
   public String external;
   public String internal;
   public String useragent;
   public Map applications;
   public String id;

   public ProfilerEvent(String var1, String var2, String var3, Map var4, String var5) {
      this.external = var1;
      this.internal = var2;
      this.useragent = var3;
      this.applications = var4;
      this.id = var5;
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      var1.push(SleepUtils.getScalar(this.id));
      var1.push(SleepUtils.getHashWrapper(this.applications));
      var1.push(SleepUtils.getScalar(this.useragent));
      var1.push(SleepUtils.getScalar(this.internal));
      var1.push(SleepUtils.getScalar(this.external));
      return var1;
   }

   public String eventName() {
      return "profiler_hit";
   }
}
