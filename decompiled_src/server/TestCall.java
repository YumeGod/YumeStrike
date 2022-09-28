package server;

import common.Request;
import java.util.Map;

public class TestCall implements ServerHook {
   public void register(Map var1) {
      var1.put("test.a", this);
      var1.put("test.b", this);
      var1.put("test.beep", this);
   }

   public void call(Request var1, ManageUser var2) {
      System.err.println("Received : " + var1);
      var2.writeNow(var1.reply("Thanks for: " + var1.getCall()));
   }
}
