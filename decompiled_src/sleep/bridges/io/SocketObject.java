package sleep.bridges.io;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.SleepClosure;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class SocketObject extends IOObject {
   protected Socket socket;
   private static Map servers;
   public static final int LISTEN_FUNCTION = 1;
   public static final int CONNECT_FUNCTION = 2;

   public Object getSource() {
      return this.socket;
   }

   public void open(SocketHandler var1, ScriptEnvironment var2) {
      try {
         this.socket = new Socket();
         if (var1.laddr != null) {
            this.socket.bind(new InetSocketAddress(var1.laddr, var1.lport));
         }

         this.socket.connect(new InetSocketAddress(var1.host, var1.port), var1.timeout);
         this.socket.setSoLinger(true, var1.linger);
         this.openRead(this.socket.getInputStream());
         this.openWrite(this.socket.getOutputStream());
      } catch (Exception var4) {
         var2.flagError(var4);
      }

   }

   public static void release(int var0) {
      String var1 = var0 + "";
      ServerSocket var2 = null;
      if (servers != null && servers.containsKey(var1)) {
         var2 = (ServerSocket)servers.get(var1);
         servers.remove(var1);

         try {
            var2.close();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   private static ServerSocket getServerSocket(int var0, SocketHandler var1) throws Exception {
      String var2 = var0 + "";
      if (servers == null) {
         servers = Collections.synchronizedMap(new HashMap());
      }

      ServerSocket var3 = null;
      if (servers.containsKey(var2)) {
         var3 = (ServerSocket)servers.get(var2);
      } else {
         var3 = new ServerSocket(var0, var1.backlog, var1.laddr != null ? InetAddress.getByName(var1.laddr) : null);
         servers.put(var2, var3);
      }

      return var3;
   }

   public void listen(SocketHandler var1, ScriptEnvironment var2) {
      ServerSocket var3 = null;

      try {
         var3 = getServerSocket(var1.port, var1);
         var3.setSoTimeout(var1.timeout);
         this.socket = var3.accept();
         this.socket.setSoLinger(true, var1.linger);
         var1.callback.setValue(SleepUtils.getScalar(this.socket.getInetAddress().getHostAddress()));
         this.openRead(this.socket.getInputStream());
         this.openWrite(this.socket.getOutputStream());
      } catch (Exception var5) {
         var2.flagError(var5);
      }
   }

   public void close() {
      try {
         this.socket.close();
      } catch (Exception var2) {
      }

      super.close();
   }

   public static class SocketHandler implements Runnable {
      public ScriptInstance script;
      public SleepClosure function;
      public SocketObject socket;
      public int port;
      public int timeout;
      public String host;
      public Scalar callback;
      public int type;
      public String laddr;
      public int lport;
      public int linger;
      public int backlog;

      public void start() {
         if (this.function != null) {
            this.socket.setThread(new Thread(this));
            this.socket.getThread().start();
         } else {
            this.run();
         }

      }

      public void run() {
         if (this.type == 1) {
            this.socket.listen(this, this.script.getScriptEnvironment());
         } else {
            this.socket.open(this, this.script.getScriptEnvironment());
         }

         if (this.function != null) {
            Stack var1 = new Stack();
            var1.push(SleepUtils.getScalar((Object)this.socket));
            this.function.callClosure("&callback", this.script, var1);
         }

      }
   }
}
