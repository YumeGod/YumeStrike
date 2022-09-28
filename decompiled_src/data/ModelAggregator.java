package data;

import aggressor.AggressorClient;
import common.ChangeLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class ModelAggregator implements Aggregator {
   protected ChangeLog changes;
   protected String name;
   protected Map model = new HashMap();

   public ModelAggregator(String var1) {
      this.name = var1;
      this.changes = new ChangeLog(var1);
   }

   public void extract(AggressorClient var1) {
      Map var2 = var1.getData().getDataModel(this.name);
      this.merge(var2);
   }

   public void merge(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         String var4 = (String)var3.getKey();
         Map var5 = (Map)var3.getValue();
         this.changes.update(var4, new HashMap(var5));
      }

      this.changes.applyForce(this.model);
      this.changes = new ChangeLog(this.name);
   }

   public void publish(Map var1) {
      var1.put(this.name, new LinkedList(this.model.values()));
   }
}
