package data;

import aggressor.AggressorClient;
import java.util.Map;

public interface Aggregator {
   void extract(AggressorClient var1);

   void publish(Map var1);
}
