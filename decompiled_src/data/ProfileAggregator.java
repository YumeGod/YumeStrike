package data;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProfileAggregator implements Aggregator {
   protected List samples = new LinkedList();

   public void extract(AggressorClient var1) {
      Map var2 = DataUtils.getC2Info(var1.getData());
      this.samples.add(var2);
   }

   public void publish(Map var1) {
      var1.put("c2samples", this.samples);
   }
}
