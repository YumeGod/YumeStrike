package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class PhishEvent implements Serializable, Scriptable, Loggable, Informant {
   protected LinkedList variables = new LinkedList();
   protected long when = System.currentTimeMillis();
   protected String evname;
   protected String sid;
   protected String desc;
   protected Map info = null;

   public PhishEvent(String var1, String var2, LinkedList var3, String var4, Map var5) {
      this.variables = new LinkedList(var3);
      this.sid = var1;
      this.evname = var2;
      this.desc = var4;
      this.info = var5;
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      Iterator var2 = this.variables.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 == null) {
            var1.add(0, SleepUtils.getEmptyScalar());
         } else if (var3 instanceof Map) {
            var1.add(0, SleepUtils.getHashWrapper((Map)var3));
         } else if (var3 instanceof Long) {
            var1.add(0, SleepUtils.getScalar((Long)var3));
         } else {
            var1.add(0, SleepUtils.getScalar(var3.toString()));
         }
      }

      return var1;
   }

   public String eventName() {
      return this.evname;
   }

   public String getBeaconId() {
      return null;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.writeBytes(CommonUtils.formatLogDate(this.when));
      var1.writeBytes(" ");
      var1.writeBytes(this.desc + "\n");
   }

   public String getLogFile() {
      return "campaign_" + this.sid + ".log";
   }

   public String getLogFolder() {
      return "phishes";
   }

   public boolean hasInformation() {
      return this.info != null;
   }

   public Map archive() {
      return this.info;
   }
}
