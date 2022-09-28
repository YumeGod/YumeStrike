package data;

import aggressor.AggressorClient;
import common.Keys;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataAggregate {
   protected AggressorClient client;
   protected List aggregators = new LinkedList();

   protected DataAggregate(AggressorClient var1) {
      this.client = var1;
   }

   public void register(Aggregator var1) {
      this.aggregators.add(var1);
   }

   public Map aggregate() {
      Map var1 = this.client.getWindow().getClients();
      Iterator var2 = var1.values().iterator();

      Iterator var4;
      Aggregator var5;
      while(var2.hasNext()) {
         AggressorClient var3 = (AggressorClient)var2.next();
         var4 = this.aggregators.iterator();

         while(var4.hasNext()) {
            var5 = (Aggregator)var4.next();
            var5.extract(var3);
         }
      }

      HashMap var6 = new HashMap();
      var4 = this.aggregators.iterator();

      while(var4.hasNext()) {
         var5 = (Aggregator)var4.next();
         var5.publish(var6);
      }

      return var6;
   }

   public static Map AllModels(AggressorClient var0) {
      DataAggregate var1 = new DataAggregate(var0);
      Iterator var2 = Keys.getDataModelIterator();

      while(var2.hasNext()) {
         var1.register(new ModelAggregator((String)var2.next()));
      }

      var1.register(new ArchiveAggregator());
      var1.register(new ProfileAggregator());
      return var1.aggregate();
   }
}
