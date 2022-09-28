package server;

import common.Request;
import java.util.Map;

public interface ServerHook {
   void call(Request var1, ManageUser var2);

   void register(Map var1);
}
