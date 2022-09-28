package common;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TeamSocket {
   protected String from;
   protected boolean connected = true;
   protected List listeners = new LinkedList();
   protected Socket client;
   protected OutputStream bout = null;

   public TeamSocket(Socket var1) throws Exception {
      this.client = var1;
      var1.setSoTimeout(0);
      this.from = var1.getInetAddress().getHostAddress();
   }

   public void addDisconnectListener(DisconnectListener var1) {
      synchronized(this) {
         this.listeners.add(var1);
      }
   }

   public void fireDisconnectEvent() {
      synchronized(this) {
         Iterator var2 = this.listeners.iterator();

         while(var2.hasNext()) {
            DisconnectListener var3 = (DisconnectListener)var2.next();
            var3.disconnected(this);
         }

         this.listeners.clear();
      }
   }

   public boolean isConnected() {
      synchronized(this) {
         return this.connected;
      }
   }

   public Object readObject() {
      try {
         if (this.isConnected()) {
            ObjectInputStream var1 = new ObjectInputStream(this.client.getInputStream());
            return var1.readUnshared();
         }
      } catch (IOException var2) {
         MudgeSanity.logException("client (" + this.from + ") read", var2, true);
         this.close();
      } catch (ClassNotFoundException var3) {
         MudgeSanity.logException("class not found", var3, false);
         this.close();
      } catch (Exception var4) {
         MudgeSanity.logException("client (" + this.from + ") read", var4, false);
         this.close();
      }

      return null;
   }

   public void close() {
      if (this.isConnected()) {
         synchronized(this) {
            try {
               this.connected = false;
               if (this.bout != null) {
                  this.bout.close();
               }

               if (this.client != null) {
                  this.client.close();
               }
            } catch (Exception var4) {
               MudgeSanity.logException("client (" + this.from + ") close", var4, false);
            }

            this.fireDisconnectEvent();
         }
      }
   }

   public void writeObject(Object var1) {
      if (this.isConnected()) {
         try {
            synchronized(this.client) {
               if (this.bout == null) {
                  this.bout = new BufferedOutputStream(this.client.getOutputStream(), 262144);
               }

               ObjectOutputStream var3 = new ObjectOutputStream(this.bout);
               var3.writeUnshared(var1);
               var3.flush();
            }
         } catch (IOException var6) {
            MudgeSanity.logException("client (" + this.from + ") write", var6, true);
            this.close();
         } catch (Exception var7) {
            MudgeSanity.logException("client (" + this.from + ") write", var7, false);
            this.close();
         }

      }
   }
}
