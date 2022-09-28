package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Authenticator;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.util.ApplicationSecurityEnforcer;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.Platform;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.util.resources.ResourceManager;

public class Main implements Application {
   public static final String UNKNOWN_SCRIPT_TYPE_LOAD_KEY_EXTENSION = ".load";
   public static final String PROPERTY_USER_HOME = "user.home";
   public static final String PROPERTY_JAVA_SECURITY_POLICY = "java.security.policy";
   public static final String BATIK_CONFIGURATION_SUBDIRECTORY = ".batik";
   public static final String SQUIGGLE_CONFIGURATION_FILE = "preferences.xml";
   public static final String SQUIGGLE_POLICY_FILE = "__svgbrowser.policy";
   public static final String POLICY_GRANT_SCRIPT_NETWORK_ACCESS = "grant {\n  permission java.net.SocketPermission \"*\", \"listen, connect, resolve, accept\";\n};\n\n";
   public static final String POLICY_GRANT_SCRIPT_FILE_ACCESS = "grant {\n  permission java.io.FilePermission \"<<ALL FILES>>\", \"read\";\n};\n\n";
   public static final String PREFERENCE_KEY_VISITED_URI_LIST = "preference.key.visited.uri.list";
   public static final String PREFERENCE_KEY_VISITED_URI_LIST_LENGTH = "preference.key.visited.uri.list.length";
   public static final String URI_SEPARATOR = " ";
   public static final String DEFAULT_DEFAULT_FONT_FAMILY = "Arial, Helvetica, sans-serif";
   public static final String SVG_INITIALIZATION = "resources/init.svg";
   protected String svgInitializationURI;
   public static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.Main";
   public static final String SQUIGGLE_SECURITY_POLICY = "org/apache/batik/apps/svgbrowser/resources/svgbrowser.policy";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.Main", Locale.getDefault());
   protected static ResourceManager resources;
   protected static ImageIcon frameIcon;
   protected XMLPreferenceManager preferenceManager;
   public static final int MAX_VISITED_URIS = 10;
   protected Vector lastVisited = new Vector();
   protected int maxVisitedURIs = 10;
   protected String[] arguments;
   protected boolean overrideSecurityPolicy = false;
   protected ApplicationSecurityEnforcer securityEnforcer;
   protected Map handlers = new HashMap();
   protected List viewerFrames;
   protected PreferenceDialog preferenceDialog;
   protected String uiSpecialization;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$Main;

   public static void main(String[] var0) {
      new Main(var0);
   }

   public Main(String[] var1) {
      this.handlers.put("-font-size", new FontSizeHandler());
      this.viewerFrames = new LinkedList();
      this.arguments = var1;
      if (Platform.isOSX) {
         this.uiSpecialization = "OSX";
         System.setProperty("apple.laf.useScreenMenuBar", "true");

         try {
            Class var2 = Class.forName("com.apple.eawt.Application");
            Class var3 = Class.forName("com.apple.eawt.ApplicationListener");
            Class var4 = Class.forName("com.apple.eawt.ApplicationEvent");
            Method var5 = var2.getMethod("getApplication");
            Method var6 = var2.getMethod("addApplicationListener", var3);
            final Method var7 = var4.getMethod("setHandled", Boolean.TYPE);
            Method var8 = var2.getMethod("setEnabledPreferencesMenu", Boolean.TYPE);
            InvocationHandler var9 = new InvocationHandler() {
               public Object invoke(Object var1, Method var2, Object[] var3) {
                  String var4 = var2.getName();
                  JSVGViewerFrame var5;
                  if (var4.equals("handleAbout")) {
                     var5 = Main.this.viewerFrames.isEmpty() ? null : (JSVGViewerFrame)Main.this.viewerFrames.get(0);
                     AboutDialog var6 = new AboutDialog(var5);
                     var6.setSize(var6.getPreferredSize());
                     var6.setLocationRelativeTo(var5);
                     var6.setVisible(true);
                     var6.toFront();
                  } else if (var4.equals("handlePreferences")) {
                     var5 = Main.this.viewerFrames.isEmpty() ? null : (JSVGViewerFrame)Main.this.viewerFrames.get(0);
                     Main.this.showPreferenceDialog(var5);
                  } else if (!var4.equals("handleQuit")) {
                     return null;
                  }

                  try {
                     var7.invoke(var3[0], Boolean.TRUE);
                  } catch (Exception var7x) {
                  }

                  return null;
               }
            };
            Object var10 = var5.invoke((Object)null, (Object[])null);
            var8.invoke(var10, Boolean.TRUE);
            Object var11 = Proxy.newProxyInstance((class$org$apache$batik$apps$svgbrowser$Main == null ? (class$org$apache$batik$apps$svgbrowser$Main = class$("org.apache.batik.apps.svgbrowser.Main")) : class$org$apache$batik$apps$svgbrowser$Main).getClassLoader(), new Class[]{var3}, var9);
            var6.invoke(var10, var11);
         } catch (Exception var13) {
            var13.printStackTrace();
            this.uiSpecialization = null;
         }
      }

      HashMap var14 = new HashMap(11);
      var14.put("preference.key.languages", Locale.getDefault().getLanguage());
      var14.put("preference.key.show.rendering", Boolean.FALSE);
      var14.put("preference.key.auto.adjust.window", Boolean.TRUE);
      var14.put("preference.key.selection.xor.mode", Boolean.FALSE);
      var14.put("preference.key.enable.double.buffering", Boolean.TRUE);
      var14.put("preference.key.show.debug.trace", Boolean.FALSE);
      var14.put("preference.key.proxy.host", "");
      var14.put("preference.key.proxy.port", "");
      var14.put("preference.key.cssmedia", "screen");
      var14.put("preference.key.default.font.family", "Arial, Helvetica, sans-serif");
      var14.put("preference.key.is.xml.parser.validating", Boolean.FALSE);
      var14.put("preference.key.enforce.secure.scripting", Boolean.TRUE);
      var14.put("preference.key.grant.script.file.access", Boolean.FALSE);
      var14.put("preference.key.grant.script.network.access", Boolean.FALSE);
      var14.put("preference.key.load.java.script", Boolean.TRUE);
      var14.put("preference.key.load.ecmascript", Boolean.TRUE);
      var14.put("preference.key.allowed.script.origin", new Integer(2));
      var14.put("preference.key.allowed.external.resource.origin", new Integer(1));
      var14.put("preference.key.visited.uri.list", "");
      var14.put("preference.key.visited.uri.list.length", new Integer(10));
      var14.put("preference.key.animation.rate.limiting.mode", new Integer(1));
      var14.put("preference.key.animation.rate.limiting.cpu", new Float(0.75F));
      var14.put("preference.key.animation.rate.limiting.fps", new Float(10.0F));
      var14.put("preference.key.user.stylesheet.enabled", Boolean.TRUE);
      this.securityEnforcer = new ApplicationSecurityEnforcer(this.getClass(), "org/apache/batik/apps/svgbrowser/resources/svgbrowser.policy");

      try {
         this.preferenceManager = new XMLPreferenceManager("preferences.xml", var14);
         String var15 = System.getProperty("user.home");
         File var17 = new File(var15, ".batik");
         var17.mkdir();
         XMLPreferenceManager.setPreferenceDirectory(var17.getCanonicalPath());
         this.preferenceManager.load();
         this.setPreferences();
         this.initializeLastVisited();
         Authenticator.setDefault(new JAuthenticator());
      } catch (Exception var12) {
         var12.printStackTrace();
      }

      final AboutDialog var16 = new AboutDialog();
      ((BorderLayout)var16.getContentPane().getLayout()).setVgap(8);
      final JProgressBar var18 = new JProgressBar(0, 3);
      var16.getContentPane().add(var18, "South");
      Dimension var19 = var16.getToolkit().getScreenSize();
      Dimension var20 = var16.getPreferredSize();
      var16.setLocation((var19.width - var20.width) / 2, (var19.height - var20.height) / 2);
      var16.setSize(var20);
      var16.setVisible(true);
      final JSVGViewerFrame var21 = new JSVGViewerFrame(this);
      JSVGCanvas var22 = var21.getJSVGCanvas();
      var22.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
         public void documentLoadingStarted(SVGDocumentLoaderEvent var1) {
            var18.setValue(1);
         }

         public void documentLoadingCompleted(SVGDocumentLoaderEvent var1) {
            var18.setValue(2);
         }
      });
      var22.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
         public void gvtBuildCompleted(GVTTreeBuilderEvent var1) {
            var18.setValue(3);
         }
      });
      var22.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
         public void gvtRenderingCompleted(GVTTreeRendererEvent var1) {
            var16.dispose();
            var21.dispose();
            System.gc();
            Main.this.run();
         }
      });
      var22.setSize(100, 100);
      this.svgInitializationURI = (class$org$apache$batik$apps$svgbrowser$Main == null ? (class$org$apache$batik$apps$svgbrowser$Main = class$("org.apache.batik.apps.svgbrowser.Main")) : class$org$apache$batik$apps$svgbrowser$Main).getResource("resources/init.svg").toString();
      var22.loadSVGDocument(this.svgInitializationURI);
   }

   public void installCustomPolicyFile() throws IOException {
      String var1 = System.getProperty("java.security.policy");
      if (this.overrideSecurityPolicy || var1 == null || "".equals(var1)) {
         ParsedURL var2 = new ParsedURL(this.securityEnforcer.getPolicyURL());
         String var3 = System.getProperty("user.home");
         File var4 = new File(var3, ".batik");
         File var5 = new File(var4, "__svgbrowser.policy");
         BufferedReader var6 = new BufferedReader(new InputStreamReader(var2.openStream()));
         FileWriter var7 = new FileWriter(var5);
         char[] var8 = new char[1024];
         boolean var9 = false;

         int var12;
         while((var12 = var6.read(var8, 0, var8.length)) != -1) {
            var7.write(var8, 0, var12);
         }

         var6.close();
         boolean var10 = this.preferenceManager.getBoolean("preference.key.grant.script.network.access");
         boolean var11 = this.preferenceManager.getBoolean("preference.key.grant.script.file.access");
         if (var10) {
            var7.write("grant {\n  permission java.net.SocketPermission \"*\", \"listen, connect, resolve, accept\";\n};\n\n");
         }

         if (var11) {
            var7.write("grant {\n  permission java.io.FilePermission \"<<ALL FILES>>\", \"read\";\n};\n\n");
         }

         var7.close();
         this.overrideSecurityPolicy = true;
         System.setProperty("java.security.policy", var5.toURL().toString());
      }

   }

   public void run() {
      try {
         int var1;
         for(var1 = 0; var1 < this.arguments.length; ++var1) {
            OptionHandler var2 = (OptionHandler)this.handlers.get(this.arguments[var1]);
            if (var2 == null) {
               break;
            }

            var1 = var2.handleOption(var1);
         }

         JSVGViewerFrame var8 = this.createAndShowJSVGViewerFrame();

         while(var1 < this.arguments.length) {
            if (this.arguments[var1].length() == 0) {
               ++var1;
            } else {
               File var3 = new File(this.arguments[var1]);
               String var4 = null;

               try {
                  if (var3.canRead()) {
                     var4 = var3.toURL().toString();
                  }
               } catch (SecurityException var6) {
               }

               if (var4 == null) {
                  var4 = this.arguments[var1];
                  ParsedURL var5 = null;
                  var5 = new ParsedURL(this.arguments[var1]);
                  if (!var5.complete()) {
                     var4 = null;
                  }
               }

               if (var4 != null) {
                  if (var8 == null) {
                     var8 = this.createAndShowJSVGViewerFrame();
                  }

                  var8.showSVGDocument(var4);
                  var8 = null;
               } else {
                  JOptionPane.showMessageDialog(var8, resources.getString("Error.skipping.file") + this.arguments[var1]);
               }

               ++var1;
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         this.printUsage();
      }

   }

   protected void printUsage() {
      System.out.println();
      System.out.println(resources.getString("Command.header"));
      System.out.println(resources.getString("Command.syntax"));
      System.out.println();
      System.out.println(resources.getString("Command.options"));
      Iterator var1 = this.handlers.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         System.out.println(((OptionHandler)this.handlers.get(var2)).getDescription());
      }

   }

   public JSVGViewerFrame createAndShowJSVGViewerFrame() {
      JSVGViewerFrame var1 = new JSVGViewerFrame(this);
      var1.setSize(resources.getInteger("Frame.width"), resources.getInteger("Frame.height"));
      var1.setIconImage(frameIcon.getImage());
      var1.setTitle(resources.getString("Frame.title"));
      var1.setVisible(true);
      this.viewerFrames.add(var1);
      this.setPreferences(var1);
      return var1;
   }

   public void closeJSVGViewerFrame(JSVGViewerFrame var1) {
      var1.getJSVGCanvas().stopProcessing();
      this.viewerFrames.remove(var1);
      if (this.viewerFrames.size() == 0) {
         System.exit(0);
      }

      var1.dispose();
   }

   public Action createExitAction(JSVGViewerFrame var1) {
      return new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            System.exit(0);
         }
      };
   }

   public void openLink(String var1) {
      JSVGViewerFrame var2 = this.createAndShowJSVGViewerFrame();
      var2.getJSVGCanvas().loadSVGDocument(var1);
   }

   public String getXMLParserClassName() {
      return XMLResourceDescriptor.getXMLParserClassName();
   }

   public boolean isXMLParserValidating() {
      return this.preferenceManager.getBoolean("preference.key.is.xml.parser.validating");
   }

   public void showPreferenceDialog(JSVGViewerFrame var1) {
      if (this.preferenceDialog == null) {
         this.preferenceDialog = new PreferenceDialog(var1, this.preferenceManager);
      }

      if (this.preferenceDialog.showDialog() == 0) {
         try {
            this.preferenceManager.save();
            this.setPreferences();
         } catch (Exception var3) {
         }
      }

   }

   private void setPreferences() throws IOException {
      Iterator var1 = this.viewerFrames.iterator();

      while(var1.hasNext()) {
         this.setPreferences((JSVGViewerFrame)var1.next());
      }

      System.setProperty("proxyHost", this.preferenceManager.getString("preference.key.proxy.host"));
      System.setProperty("proxyPort", this.preferenceManager.getString("preference.key.proxy.port"));
      this.installCustomPolicyFile();
      this.securityEnforcer.enforceSecurity(this.preferenceManager.getBoolean("preference.key.enforce.secure.scripting"));
   }

   private void setPreferences(JSVGViewerFrame var1) {
      boolean var2 = this.preferenceManager.getBoolean("preference.key.enable.double.buffering");
      var1.getJSVGCanvas().setDoubleBufferedRendering(var2);
      boolean var3 = this.preferenceManager.getBoolean("preference.key.show.rendering");
      var1.getJSVGCanvas().setProgressivePaint(var3);
      boolean var4 = this.preferenceManager.getBoolean("preference.key.show.debug.trace");
      var1.setDebug(var4);
      boolean var5 = this.preferenceManager.getBoolean("preference.key.auto.adjust.window");
      var1.setAutoAdjust(var5);
      boolean var6 = this.preferenceManager.getBoolean("preference.key.selection.xor.mode");
      var1.getJSVGCanvas().setSelectionOverlayXORMode(var6);
      int var7 = this.preferenceManager.getInteger("preference.key.animation.rate.limiting.mode");
      if (var7 < 0 || var7 > 2) {
         var7 = 1;
      }

      float var8;
      switch (var7) {
         case 0:
            var1.getJSVGCanvas().setAnimationLimitingNone();
            break;
         case 1:
            var8 = this.preferenceManager.getFloat("preference.key.animation.rate.limiting.cpu");
            if (var8 <= 0.0F || var8 > 1.0F) {
               var8 = 0.75F;
            }

            var1.getJSVGCanvas().setAnimationLimitingCPU(var8);
            break;
         case 2:
            var8 = this.preferenceManager.getFloat("preference.key.animation.rate.limiting.fps");
            if (var8 <= 0.0F) {
               var8 = 10.0F;
            }

            var1.getJSVGCanvas().setAnimationLimitingFPS(var8);
      }

   }

   public String getLanguages() {
      String var1 = this.preferenceManager.getString("preference.key.languages");
      return var1 == null ? Locale.getDefault().getLanguage() : var1;
   }

   public String getUserStyleSheetURI() {
      boolean var1 = this.preferenceManager.getBoolean("preference.key.user.stylesheet.enabled");
      String var2 = this.preferenceManager.getString("preference.key.user.stylesheet");
      if (var1 && var2.length() != 0) {
         try {
            File var3 = new File(var2);
            if (var3.exists()) {
               return var3.toURL().toString();
            }
         } catch (IOException var4) {
         }

         return var2;
      } else {
         return null;
      }
   }

   public String getDefaultFontFamily() {
      return this.preferenceManager.getString("preference.key.default.font.family");
   }

   public String getMedia() {
      String var1 = this.preferenceManager.getString("preference.key.cssmedia");
      return var1 == null ? "screen" : var1;
   }

   public boolean isSelectionOverlayXORMode() {
      return this.preferenceManager.getBoolean("preference.key.selection.xor.mode");
   }

   public boolean canLoadScriptType(String var1) {
      if (!"text/ecmascript".equals(var1) && !"application/ecmascript".equals(var1) && !"text/javascript".equals(var1) && !"application/javascript".equals(var1)) {
         return "application/java-archive".equals(var1) ? this.preferenceManager.getBoolean("preference.key.load.java.script") : this.preferenceManager.getBoolean(var1 + ".load");
      } else {
         return this.preferenceManager.getBoolean("preference.key.load.ecmascript");
      }
   }

   public int getAllowedScriptOrigin() {
      int var1 = this.preferenceManager.getInteger("preference.key.allowed.script.origin");
      return var1;
   }

   public int getAllowedExternalResourceOrigin() {
      int var1 = this.preferenceManager.getInteger("preference.key.allowed.external.resource.origin");
      return var1;
   }

   public void addVisitedURI(String var1) {
      if (!this.svgInitializationURI.equals(var1)) {
         int var2 = this.preferenceManager.getInteger("preference.key.visited.uri.list.length");
         if (var2 < 0) {
            var2 = 0;
         }

         if (this.lastVisited.contains(var1)) {
            this.lastVisited.removeElement(var1);
         }

         while(this.lastVisited.size() > 0 && this.lastVisited.size() > var2 - 1) {
            this.lastVisited.removeElementAt(0);
         }

         if (var2 > 0) {
            this.lastVisited.addElement(var1);
         }

         StringBuffer var3 = new StringBuffer(this.lastVisited.size() * 8);

         for(int var4 = 0; var4 < this.lastVisited.size(); ++var4) {
            var3.append(URLEncoder.encode(this.lastVisited.get(var4).toString()));
            var3.append(" ");
         }

         this.preferenceManager.setString("preference.key.visited.uri.list", var3.toString());

         try {
            this.preferenceManager.save();
         } catch (Exception var5) {
         }

      }
   }

   public String[] getVisitedURIs() {
      String[] var1 = new String[this.lastVisited.size()];
      this.lastVisited.toArray(var1);
      return var1;
   }

   public String getUISpecialization() {
      return this.uiSpecialization;
   }

   protected void initializeLastVisited() {
      String var1 = this.preferenceManager.getString("preference.key.visited.uri.list");
      StringTokenizer var2 = new StringTokenizer(var1, " ");
      int var3 = var2.countTokens();
      int var4 = this.preferenceManager.getInteger("preference.key.visited.uri.list.length");
      if (var3 > var4) {
         var3 = var4;
      }

      for(int var5 = 0; var5 < var3; ++var5) {
         this.lastVisited.addElement(URLDecoder.decode(var2.nextToken()));
      }

   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      resources = new ResourceManager(bundle);
      frameIcon = new ImageIcon((class$org$apache$batik$apps$svgbrowser$Main == null ? (class$org$apache$batik$apps$svgbrowser$Main = class$("org.apache.batik.apps.svgbrowser.Main")) : class$org$apache$batik$apps$svgbrowser$Main).getResource(resources.getString("Frame.icon")));
   }

   protected class FontSizeHandler implements OptionHandler {
      public int handleOption(int var1) {
         ++var1;
         int var2 = Integer.parseInt(Main.this.arguments[var1]);
         Font var3 = new Font("Dialog", 0, var2);
         FontUIResource var4 = new FontUIResource(var3);
         UIManager.put("CheckBox.font", var4);
         UIManager.put("PopupMenu.font", var4);
         UIManager.put("TextPane.font", var4);
         UIManager.put("MenuItem.font", var4);
         UIManager.put("ComboBox.font", var4);
         UIManager.put("Button.font", var4);
         UIManager.put("Tree.font", var4);
         UIManager.put("ScrollPane.font", var4);
         UIManager.put("TabbedPane.font", var4);
         UIManager.put("EditorPane.font", var4);
         UIManager.put("TitledBorder.font", var4);
         UIManager.put("Menu.font", var4);
         UIManager.put("TextArea.font", var4);
         UIManager.put("OptionPane.font", var4);
         UIManager.put("DesktopIcon.font", var4);
         UIManager.put("MenuBar.font", var4);
         UIManager.put("ToolBar.font", var4);
         UIManager.put("RadioButton.font", var4);
         UIManager.put("RadioButtonMenuItem.font", var4);
         UIManager.put("ToggleButton.font", var4);
         UIManager.put("ToolTip.font", var4);
         UIManager.put("ProgressBar.font", var4);
         UIManager.put("TableHeader.font", var4);
         UIManager.put("Panel.font", var4);
         UIManager.put("List.font", var4);
         UIManager.put("ColorChooser.font", var4);
         UIManager.put("PasswordField.font", var4);
         UIManager.put("TextField.font", var4);
         UIManager.put("Table.font", var4);
         UIManager.put("Label.font", var4);
         UIManager.put("InternalFrameTitlePane.font", var4);
         UIManager.put("CheckBoxMenuItem.font", var4);
         return var1;
      }

      public String getDescription() {
         return Main.resources.getString("Command.font-size");
      }
   }

   protected interface OptionHandler {
      int handleOption(int var1);

      String getDescription();
   }
}
