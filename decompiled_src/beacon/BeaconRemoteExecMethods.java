package beacon;

import aggressor.AggressorClient;
import beacon.methods.PsExec;
import beacon.methods.WMI;
import beacon.methods.WinRM;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BeaconRemoteExecMethods {
   public Map descriptions = new HashMap();
   public Map exploits = new HashMap();

   public void registerDefaults(AggressorClient var1) {
      new WMI(var1);
      new WinRM(var1);
      new PsExec(var1);
   }

   public RemoteExecMethod getRemoteExecMethod(String var1) {
      synchronized(this) {
         return (RemoteExecMethod)this.exploits.get(var1);
      }
   }

   public void register(String var1, String var2, RemoteExecMethod var3) {
      this.descriptions.put(var1, var2);
      this.exploits.put(var1, var3);
   }

   public boolean isRemoteExecMethod(String var1) {
      synchronized(this) {
         return this.exploits.containsKey(var1);
      }
   }

   public List methods() {
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

   public interface RemoteExecMethod {
      void remoteexec(String var1, String var2, String var3);
   }
}
