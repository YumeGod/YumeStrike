package beacon;

import common.BeaconEntry;
import common.BeaconOutput;
import common.Download;
import common.Keystrokes;
import common.ScListener;
import common.Screenshot;
import java.io.Serializable;
import java.util.Map;

public interface CheckinListener {
   void checkin(ScListener var1, BeaconEntry var2);

   void output(BeaconOutput var1);

   void update(String var1, long var2, String var4, boolean var5);

   void screenshot(Screenshot var1);

   void keystrokes(Keystrokes var1);

   void download(Download var1);

   void push(String var1, Serializable var2);

   BeaconEntry resolve(String var1);

   BeaconEntry resolveEgress(String var1);

   Map buildBeaconModel();
}
