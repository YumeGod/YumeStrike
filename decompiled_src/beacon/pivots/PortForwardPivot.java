package beacon.pivots;

import beacon.BeaconPivot;
import common.CommonUtils;

public class PortForwardPivot extends BeaconPivot {
   public void die() {
      this.client.getConnection().call("beacons.pivot_stop_port", CommonUtils.args(this.port + ""));
   }

   public void tunnel() {
   }
}
