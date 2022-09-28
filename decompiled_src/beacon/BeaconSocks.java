package beacon;

import common.BeaconOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import socks.BeaconProxyListener;
import socks.Mortal;
import socks.PortForward;
import socks.ReversePortForward;
import socks.SocksProxy;
import socks.SocksProxyServer;

public class BeaconSocks {
   protected Map socks = new HashMap();
   protected Map servers = new HashMap();
   protected BeaconC2 controller;

   public BeaconSocks(BeaconC2 var1) {
      this.controller = var1;
   }

   public void notifyClients() {
      LinkedList var1 = new LinkedList();
      synchronized(this) {
         Iterator var3 = this.servers.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            List var6 = (List)var4.getValue();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Mortal var8 = (Mortal)var7.next();
               Map var9 = var8.toMap();
               var9.put("bid", var5);
               var1.add(var9);
            }
         }
      }

      this.controller.getCheckinListener().push("socks", var1);
   }

   public SocksProxy getBroker(String var1) {
      synchronized(this) {
         if (this.socks.containsKey(var1)) {
            return (SocksProxy)this.socks.get(var1);
         } else {
            SocksProxy var3 = new SocksProxy();
            var3.addProxyListener(new BeaconProxyListener());
            this.socks.put(var1, var3);
            return var3;
         }
      }
   }

   public void track(String var1, Mortal var2) {
      synchronized(this) {
         if (!this.servers.containsKey(var1)) {
            this.servers.put(var1, new LinkedList());
         }

         LinkedList var4 = (LinkedList)this.servers.get(var1);
         var4.add(var2);
      }

      this.notifyClients();
   }

   public void pivot(String var1, int var2) {
      synchronized(this) {
         SocksProxyServer var4 = new SocksProxyServer(this.getBroker(var1));

         try {
            var4.go(var2);
            this.track(var1, var4);
            this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "started SOCKS4a server on: " + var2));
         } catch (IOException var7) {
            this.controller.getCheckinListener().output(BeaconOutput.Error(var1, "Could not start SOCKS4a server on " + var2 + ": " + var7.getMessage()));
         }

      }
   }

   protected ReversePortForward findPortForward(String var1, int var2) {
      synchronized(this) {
         if (this.servers.containsKey(var1)) {
            Iterator var4 = ((LinkedList)this.servers.get(var1)).iterator();

            while(var4.hasNext()) {
               Mortal var5 = (Mortal)var4.next();
               if (var5 instanceof ReversePortForward) {
                  ReversePortForward var6 = (ReversePortForward)var5;
                  if (var6.getPort() == var2) {
                     return var6;
                  }
               }
            }
         }

         return null;
      }
   }

   public void accept(String var1, int var2, int var3) {
      synchronized(this) {
         ReversePortForward var5 = this.findPortForward(var1, var2);
         if (var5 != null) {
            var5.accept(var3);
         }
      }
   }

   public void portfwd(String var1, int var2, String var3, int var4) {
      synchronized(this) {
         PortForward var6 = new PortForward(this.getBroker(var1), var3, var4);

         try {
            var6.go(var2);
            this.track(var1, var6);
            this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "started port forward on " + var2 + " to " + var3 + ":" + var4));
         } catch (IOException var9) {
            this.controller.getCheckinListener().output(BeaconOutput.Error(var1, "Could not start port forward on " + var2 + ": " + var9.getMessage()));
         }

      }
   }

   public void rportfwd(String var1, int var2, String var3, int var4) {
      synchronized(this) {
         ReversePortForward var6 = new ReversePortForward(this.getBroker(var1), var2, var3, var4);
         this.track(var1, var6);
         this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "started reverse port forward on " + var2 + " to " + var3 + ":" + var4));
      }
   }

   public void stop_port(int var1) {
      synchronized(this) {
         Iterator var3 = this.servers.entrySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            LinkedList var6 = (LinkedList)var4.getValue();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Mortal var8 = (Mortal)var7.next();
               if (var8.getPort() == var1) {
                  this.controller.getCheckinListener().output(BeaconOutput.Output(var5, "stopped proxy pivot on " + var1));
                  var8.die();
                  var7.remove();
               }
            }

            if (var6.size() == 0) {
               var3.remove();
            }
         }
      }

      this.notifyClients();
   }

   public void stop(String var1) {
      synchronized(this) {
         if (this.servers.containsKey(var1)) {
            Iterator var3 = ((LinkedList)this.servers.get(var1)).iterator();

            while(var3.hasNext()) {
               Mortal var4 = (Mortal)var3.next();
               var4.die();
            }

            this.servers.remove(var1);
         }
      }

      this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "stopped SOCKS4a servers"));
      this.notifyClients();
   }

   public boolean isActive(String var1) {
      synchronized(this) {
         return this.servers.containsKey(var1);
      }
   }

   public void die(String var1, int var2) {
      synchronized(this) {
         SocksProxy var4 = (SocksProxy)this.socks.get(var1);
         if (var4 != null) {
            var4.die(var2);
         }
      }
   }

   public void write(String var1, int var2, byte[] var3) {
      synchronized(this) {
         SocksProxy var5 = (SocksProxy)this.socks.get(var1);
         if (var5 != null) {
            var5.write(var2, var3, 0, var3.length);
         }
      }
   }

   public void resume(String var1, int var2) {
      synchronized(this) {
         SocksProxy var4 = (SocksProxy)this.socks.get(var1);
         if (var4 != null) {
            var4.resume(var2);
         }
      }
   }

   public byte[] dump(String var1, int var2) {
      synchronized(this) {
         SocksProxy var4 = (SocksProxy)this.socks.get(var1);
         return var4 == null ? new byte[0] : var4.grab(var2);
      }
   }
}
