package data;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.CommonUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArchiveAggregator implements Aggregator {
   protected List archives = new LinkedList();

   public void extract(AggressorClient var1) {
      LinkedList var2 = var1.getData().getTranscriptSafe("archives");

      HashMap var5;
      for(Iterator var3 = var2.iterator(); var3.hasNext(); this.archives.add(var5)) {
         Map var4 = (Map)var3.next();
         var5 = new HashMap(var4);
         if (var5.containsKey("when")) {
            long var6 = CommonUtils.toLongNumber(var5.get("when") + "", 0L);
            var5.put("when", DataUtils.AdjustForSkew(var1.getData(), var6));
         }
      }

   }

   public void publish(Map var1) {
      var1.put("archives", this.archives);
   }
}
