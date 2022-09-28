package aggressor.dialogs;

import aggressor.AggressorClient;
import java.util.Stack;

public class JavaSmartAppletDialog extends JavaAppletDialog {
   public JavaSmartAppletDialog(AggressorClient var1) {
      super(var1);
   }

   public String getResourceName() {
      String var1 = this.client.getScriptEngine().format("SMART_APPLET_RESOURCE", new Stack());
      return var1 != null ? var1 : "resources/applet_rhino.jar";
   }

   public String getMainClass() {
      String var1 = this.client.getScriptEngine().format("SMART_APPLET_MAINCLASS", new Stack());
      return var1 != null ? var1 : "JavaApplet.class";
   }

   public String getShortDescription() {
      return "smart applet";
   }

   public String getTitle() {
      return "Smart Applet Attack";
   }

   public String getURL() {
      return "https://www.cobaltstrike.com/help-java-smart-applet-attack";
   }

   public String getDescription() {
      return "<html><body>The Smart Applet detects the Java version and uses an embedded exploit to disable the Java security sandbox. This attack is cross-platform and cross-browser.<p><b>Vulnerable Java Versions</b></p><ul><li>Java 1.6.0_45 and below</li><li>Java 1.7.0_21 and below</li></ul></body></html>";
   }

   public String getDefaultURL() {
      return "/SiteLoader";
   }
}
