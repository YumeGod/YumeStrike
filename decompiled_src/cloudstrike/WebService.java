package cloudstrike;

import java.util.List;
import java.util.Properties;

public interface WebService {
   Response serve(String var1, String var2, Properties var3, Properties var4);

   String getType();

   List cleanupJobs();

   boolean suppressEvent(String var1);

   boolean isFuzzy();

   void setup(WebServer var1, String var2);
}
