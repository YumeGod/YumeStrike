package beacon;

import aggressor.AggressorClient;
import beacon.elevators.BypassUACCMSTPLUA;
import beacon.elevators.BypassUACToken;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BeaconElevators {
   public Map descriptions = new HashMap();
   public Map exploits = new HashMap();

   public void registerDefaults(AggressorClient var1) {
      new BypassUACToken(var1);
      new BypassUACCMSTPLUA(var1);
   }

   public Elevator getCommandElevator(String var1) {
      synchronized(this) {
         return (Elevator)this.exploits.get(var1);
      }
   }

   public void register(String var1, String var2, Elevator var3) {
      this.descriptions.put(var1, var2);
      this.exploits.put(var1, var3);
   }

   public boolean isElevator(String var1) {
      synchronized(this) {
         return this.exploits.containsKey(var1);
      }
   }

   public List elevators() {
      synchronized(this) {
         LinkedList var2 = new LinkedList(this.descriptions.keySet());
         Collections.sort(var2);
         return var2;
      }
   }

   public String getDescription(String var1) {
      synchronized(this) {
         return this.descriptions.get(var1) + "";
      }
   }

   public interface Elevator {
      void runasadmin(String var1, String var2);
   }
}
