import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import javax.swing.JApplet;

public class iecheck extends JApplet {
   public String getJavaVersion() {
      return System.getProperty("java.version");
   }

   public String getMyIPAddress() {
      URL var1 = this.getDocumentBase();

      try {
         String var2 = var1.getHost();
         Socket var3 = new Socket(var2, var1.getPort() > 0 ? var1.getPort() : 80);
         InetAddress var4 = var3.getLocalAddress();
         return var4.getHostAddress();
      } catch (Exception var5) {
         return "unknown";
      }
   }

   public void init() {
   }
}
