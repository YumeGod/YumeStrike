package ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class SecureProxySocket {
   protected SSLSocket socket = null;
   private static byte[] buffer = null;

   public static SSLSocketFactory getMyFactory() throws Exception {
      SSLContext var0 = SSLContext.getInstance("SSL");
      var0.init((KeyManager[])null, new TrustManager[]{new ITrustYouDude()}, new SecureRandom());
      SSLSocketFactory var1 = var0.getSocketFactory();
      return var1;
   }

   public SecureProxySocket(String var1, int var2) throws Exception {
      SSLSocketFactory var3 = getMyFactory();
      this.socket = (SSLSocket)var3.createSocket(var1, var2);
      this.socket.setSoTimeout(4048);
      this.socket.startHandshake();
   }

   public SecureProxySocket(Socket var1) throws Exception {
      KeyStore var2 = KeyStore.getInstance("JKS");
      InputStream var3 = this.getClass().getClassLoader().getResourceAsStream("resources/proxy.store");
      var2.load(var3, "123456".toCharArray());
      var3.close();
      KeyManagerFactory var4 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      var4.init(var2, "123456".toCharArray());
      SSLContext var5 = SSLContext.getInstance("SSL");
      var5.init(var4.getKeyManagers(), new TrustManager[]{new ITrustYouDude()}, new SecureRandom());
      SSLSocketFactory var6 = var5.getSocketFactory();
      this.socket = (SSLSocket)var6.createSocket(var1, var1.getInetAddress().getHostName(), var1.getPort(), true);
      this.socket.setUseClientMode(false);
      this.socket.setSoTimeout(8192);
      this.socket.startHandshake();
   }

   public static byte[] readbytes(InputStream var0) throws IOException {
      Class var1 = SecureSocket.class;
      synchronized(SecureSocket.class) {
         if (buffer == null) {
            buffer = new byte[1048576];
         }

         int var2 = var0.read(buffer);
         return var2 > 0 ? Arrays.copyOf(buffer, var2) : new byte[0];
      }
   }

   public Socket getSocket() {
      return this.socket;
   }
}
