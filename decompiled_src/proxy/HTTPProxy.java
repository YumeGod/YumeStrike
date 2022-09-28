package proxy;

import common.CommonUtils;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLHandshakeException;
import ssl.SecureProxySocket;

public class HTTPProxy implements Runnable {
   protected String server = "";
   protected int port = 0;
   protected int sport = 0;
   protected List listeners = new LinkedList();
   protected ServerSocket pserver = null;
   protected boolean alive = true;
   protected long requests = 0L;
   protected long rx = 0L;
   protected long fails = 0L;

   public void addProxyListener(HTTPProxyEventListener var1) {
      this.listeners.add(var1);
   }

   public void fireEvent(int var1, String var2) {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((HTTPProxyEventListener)var3.next()).proxyEvent(var1, var2);
      }

   }

   public void stop() {
      this.alive = false;

      try {
         this.pserver.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public HTTPProxy(int var1, String var2, int var3) throws IOException {
      this.server = var2;
      this.port = var3;
      this.sport = var1;
      this.pserver = new ServerSocket(var1, 128);
   }

   public int getPort() {
      return this.port;
   }

   public void start() {
      (new Thread(this, "Browser Pivot HTTP Proxy Server (port " + this.sport + ")")).start();
   }

   private static final int checkLen(String var0, int var1, StringBuffer var2) {
      var2.append(var0 + "\r\n");
      if (var0.toLowerCase().startsWith("content-length: ")) {
         try {
            return Integer.parseInt(var0.substring(16).trim());
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return var1;
   }

   private static final int checkLenServer(String var0, int var1, StringBuffer var2) {
      Set var3 = CommonUtils.toSet("strict-transport-security, expect-ct, alt-svc");
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = var4.next() + ": ";
         if (var0.toLowerCase().startsWith(var5)) {
            return var1;
         }
      }

      var2.append(var0 + "\r\n");
      if (var0.toLowerCase().startsWith("content-length: ")) {
         try {
            return Integer.parseInt(var0.substring(16).trim());
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      return var1;
   }

   private static String readLine(DataInputStream var0) throws IOException {
      StringBuffer var1 = new StringBuffer();

      while(true) {
         int var2 = var0.readUnsignedByte();
         if ((char)var2 == '\n') {
            return var1.toString();
         }

         if ((char)var2 == '\r') {
            boolean var3 = false;
         } else {
            var1.append((char)var2);
         }
      }
   }

   public void run() {
      while(true) {
         try {
            if (this.alive) {
               Socket var1 = this.pserver.accept();
               var1.setSoTimeout(60000);
               new ProxyClient(var1);
               continue;
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }

         return;
      }
   }

   public class ProxyClient implements Runnable {
      protected Socket socket = null;
      protected Socket proxy = null;

      public ProxyClient(Socket var2) {
         this.socket = var2;
         (new Thread(this, "Browser Pivot HTTP Request")).start();
      }

      public void run() {
         String var1 = "";
         DataOutputStream var2 = null;
         boolean var3 = false;
         String var4 = "";
         boolean var32 = false;

         label445: {
            String[] var47;
            label444: {
               label443: {
                  label442: {
                     try {
                        var32 = true;
                        this.proxy = new Socket(HTTPProxy.this.server, HTTPProxy.this.port);
                        this.proxy.setSoTimeout(60000);
                        StringBuffer var5 = new StringBuffer(8192);
                        InputStream var6 = this.socket.getInputStream();
                        DataInputStream var7 = new DataInputStream(var6);
                        var2 = new DataOutputStream(this.socket.getOutputStream());
                        var4 = HTTPProxy.readLine(var7);
                        var5.append(var4 + "\r\n");
                        int var8 = 0;
                        String var9 = HTTPProxy.readLine(var7);

                        for(var8 = HTTPProxy.checkLen(var9, var8, var5); var9.length() > 0; var8 = HTTPProxy.checkLen(var9, var8, var5)) {
                           var9 = HTTPProxy.readLine(var7);
                        }

                        byte var48;
                        if (var4.startsWith("CONNECT")) {
                           var2.writeBytes("HTTP/1.1 200 Connection established\r\n\r\n");
                           String[] var10 = var4.split(" ");
                           String var11 = var10[1];
                           if (var11.endsWith(":443")) {
                              var11 = var11.substring(0, var11.length() - 4);
                           }

                           var5 = new StringBuffer(8192);
                           this.socket = (new SecureProxySocket(this.socket)).getSocket();
                           var7 = new DataInputStream(this.socket.getInputStream());
                           var2 = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
                           var4 = HTTPProxy.readLine(var7);
                           if (var4.startsWith("GET ")) {
                              var5.append("GET https://" + var11 + var4.substring(4) + "\r\n");
                           } else if (var4.startsWith("POST ")) {
                              var5.append("POST https://" + var11 + var4.substring(5) + "\r\n");
                           }

                           var48 = 0;
                           var9 = HTTPProxy.readLine(var7);

                           for(var8 = HTTPProxy.checkLen(var9, var48, var5); var9.length() > 0; var8 = HTTPProxy.checkLen(var9, var8, var5)) {
                              var9 = HTTPProxy.readLine(var7);
                           }
                        }

                        DataOutputStream var49 = new DataOutputStream(new BufferedOutputStream(this.proxy.getOutputStream()));
                        var49.writeBytes(var5.toString());
                        var49.flush();
                        if (var8 > 0) {
                           byte[] var50 = new byte[var8];

                           int var51;
                           for(boolean var12 = false; var8 > 0; var8 -= var51) {
                              var51 = var7.read(var50);
                              var49.write(var50, 0, var51);
                              var49.flush();
                           }
                        }

                        DataInputStream var52 = new DataInputStream(this.proxy.getInputStream());
                        var5 = new StringBuffer(8192);
                        var9 = HTTPProxy.readLine(var52);
                        var48 = 0;

                        for(var8 = HTTPProxy.checkLenServer(var9, var48, var5); var9.length() > 0; var8 = HTTPProxy.checkLenServer(var9, var8, var5)) {
                           var9 = HTTPProxy.readLine(var52);
                        }

                        HTTPProxy var10000 = HTTPProxy.this;
                        var10000.rx += (long)var8;
                        if (var8 == 0) {
                           var2.writeBytes(var5.toString());
                           var2.flush();
                           var3 = true;
                        } else {
                           byte[] var53 = new byte[var8];
                           boolean var13 = false;
                           byte var14 = 0;

                           int var54;
                           for(int var15 = 0; var8 > 0; var15 += var54) {
                              var54 = var52.read(var53);
                              if (var54 <= 0) {
                                 throw new IOException("incomplete read " + var14 + ", need: " + var8 + " bytes, read: " + var15 + " bytes");
                              }

                              if (!var3) {
                                 var2.writeBytes(var5.toString());
                                 var2.flush();
                                 var3 = true;
                              }

                              var2.write(var53, 0, var54);
                              var2.flush();
                              var8 -= var54;
                           }
                        }

                        ++HTTPProxy.this.requests;
                        var32 = false;
                        break label444;
                     } catch (SSLHandshakeException var43) {
                        HTTPProxy.this.fireEvent(0, "add to trusted hosts: " + var1);
                        ++HTTPProxy.this.fails;
                        var3 = true;
                        var32 = false;
                     } catch (SocketException var44) {
                        HTTPProxy.this.fireEvent(1, "browser proxy refused connection.");
                        ++HTTPProxy.this.fails;
                        var32 = false;
                        break label443;
                     } catch (Exception var45) {
                        ++HTTPProxy.this.fails;
                        var32 = false;
                        break label442;
                     } finally {
                        if (var32) {
                           try {
                              try {
                                 if (!var3 && var2 != null && !var4.startsWith("CONNECT") && (var4.trim() + "").length() > 0) {
                                    String[] var17 = var4.split(" ");
                                    var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var17[1] + "\r\n\r\n");
                                    var2.flush();
                                 }
                              } catch (Exception var33) {
                              }

                              if (this.socket != null) {
                                 this.socket.close();
                              }

                              if (this.proxy != null) {
                                 this.proxy.close();
                              }
                           } catch (Exception var34) {
                           }

                        }
                     }

                     try {
                        try {
                           if (!var3 && var2 != null && !var4.startsWith("CONNECT") && (var4.trim() + "").length() > 0) {
                              var47 = var4.split(" ");
                              var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47[1] + "\r\n\r\n");
                              var2.flush();
                           }
                        } catch (Exception var39) {
                        }

                        if (this.socket != null) {
                           this.socket.close();
                        }

                        if (this.proxy != null) {
                           this.proxy.close();
                        }
                     } catch (Exception var40) {
                     }
                     break label445;
                  }

                  try {
                     try {
                        if (!var3 && var2 != null && !var4.startsWith("CONNECT") && (var4.trim() + "").length() > 0) {
                           var47 = var4.split(" ");
                           var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47[1] + "\r\n\r\n");
                           var2.flush();
                        }
                     } catch (Exception var35) {
                     }

                     if (this.socket != null) {
                        this.socket.close();
                     }

                     if (this.proxy != null) {
                        this.proxy.close();
                     }
                  } catch (Exception var36) {
                  }
                  break label445;
               }

               try {
                  try {
                     if (!var3 && var2 != null && !var4.startsWith("CONNECT") && (var4.trim() + "").length() > 0) {
                        var47 = var4.split(" ");
                        var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47[1] + "\r\n\r\n");
                        var2.flush();
                     }
                  } catch (Exception var37) {
                  }

                  if (this.socket != null) {
                     this.socket.close();
                  }

                  if (this.proxy != null) {
                     this.proxy.close();
                  }
               } catch (Exception var38) {
               }
               break label445;
            }

            try {
               try {
                  if (!var3 && var2 != null && !var4.startsWith("CONNECT") && (var4.trim() + "").length() > 0) {
                     var47 = var4.split(" ");
                     var2.writeBytes("HTTP/1.1 302\r\nLocation: " + var47[1] + "\r\n\r\n");
                     var2.flush();
                  }
               } catch (Exception var41) {
               }

               if (this.socket != null) {
                  this.socket.close();
               }

               if (this.proxy != null) {
                  this.proxy.close();
               }
            } catch (Exception var42) {
            }
         }

         HTTPProxy.this.fireEvent(3, HTTPProxy.this.requests + " " + HTTPProxy.this.fails + " " + HTTPProxy.this.rx);
      }
   }
}
