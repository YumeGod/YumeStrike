package common;

import java.awt.Component;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class Requirements {
   public static void recommended() {
   }

   public static Set arguments() {
      RuntimeMXBean var0 = ManagementFactory.getRuntimeMXBean();
      List var1 = var0.getInputArguments();
      HashSet var2 = new HashSet(var1);
      return var2;
   }

   public static String requirements(boolean var0) {
      if ("1.6".equals(System.getProperty("java.specification.version"))) {
         return "Java 1.6 is not supported. Please upgrade to Java 1.7 or later.";
      } else if (var0 && "1.8".equals(System.getProperty("java.specification.version")) && CommonUtils.isin("OpenJDK", System.getProperty("java.runtime.name"))) {
         return "OpenJDK 1.8 is not supported. Use Oracle Java 8 or OpenJDK 11 (or later.)";
      } else {
         Set var1 = arguments();
         if (!var1.contains("-XX:+AggressiveHeap")) {
            return "Java -XX:+AggressiveHeap option not set. Use the Cobalt Strike launcher. Don't click the .jar file!";
         } else {
            return !var1.contains("-XX:+UseParallelGC") ? "Java -XX:+UseParallelGC option not set. Use the Cobalt Strike launcher. Don't click the .jar file!" : null;
         }
      }
   }

   public static void checkGUI() {
      recommended();
      String var0 = requirements(true);
      if (var0 != null) {
         JOptionPane.showMessageDialog((Component)null, var0, (String)null, 0);
         CommonUtils.print_error(var0);
         System.exit(0);
      }

      String var1 = System.getenv("XDG_SESSION_TYPE");
      if ("wayland".equals(var1)) {
         CommonUtils.print_warn("You are using a Wayland desktop and not X11. Graphical Java applications run on Wayland are known to crash. You should use X11. See: https://www.cobaltstrike.com/help-wayland");
         JOptionPane.showInputDialog((Component)null, "The Wayland desktop is not supported with Cobalt Strike.\nMore information:", (String)null, 2, (Icon)null, (Object[])null, "https://www.cobaltstrike.com/help-wayland");
      }

   }

   public static void checkConsole() {
      recommended();
      String var0 = requirements(false);
      if (var0 != null) {
         CommonUtils.print_error(var0);
         System.exit(0);
      }

   }
}
