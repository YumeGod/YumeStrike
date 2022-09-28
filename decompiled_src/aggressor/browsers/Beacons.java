package aggressor.browsers;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.AdjustData;
import common.CommonUtils;
import dialog.DialogUtils;
import filter.DataFilter;
import java.util.LinkedList;
import java.util.Map;

public class Beacons extends Sessions implements AdjustData {
   protected DataFilter filter = new DataFilter();

   public Beacons(AggressorClient var1, boolean var2) {
      super(var1, var2);
      this.filter.checkBeacon("id", false);
   }

   public Map format(String var1, Object var2) {
      return !this.filter.test((Map)var2) ? null : (Map)var2;
   }

   public void result(String var1, Object var2) {
      if (this.table.isShowing()) {
         LinkedList var3 = new LinkedList(DataUtils.getBeaconModelFromResult(var2));
         var3 = CommonUtils.apply(var1, var3, this);
         DialogUtils.setTable(this.table, this.model, var3);
      }
   }
}
