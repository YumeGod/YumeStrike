package aggressor;

import common.Callback;
import java.util.List;
import java.util.Map;

public interface GenericDataManager {
   void unsub(String var1, Callback var2);

   void subscribe(String var1, Callback var2);

   WindowCleanup unsubOnClose(String var1, Callback var2);

   Object get(String var1, Object var2);

   Map getMapSafe(String var1);

   List getListSafe(String var1);
}
