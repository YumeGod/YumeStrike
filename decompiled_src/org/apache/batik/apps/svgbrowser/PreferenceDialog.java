package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.batik.ext.swing.GridBagConstants;
import org.apache.batik.ext.swing.JGridBagPanel;
import org.apache.batik.util.Platform;
import org.apache.batik.util.PreferenceManager;
import org.apache.batik.util.gui.CSSMediaPanel;
import org.apache.batik.util.gui.LanguageDialog;

public class PreferenceDialog extends JDialog implements GridBagConstants {
   public static final int OK_OPTION = 0;
   public static final int CANCEL_OPTION = 1;
   public static final String PREFERENCE_KEY_TITLE_PREFIX = "PreferenceDialog.title.";
   public static final String PREFERENCE_KEY_TITLE_DIALOG = "PreferenceDialog.title.dialog";
   public static final String PREFERENCE_KEY_LABEL_RENDERING_OPTIONS = "PreferenceDialog.label.rendering.options";
   public static final String PREFERENCE_KEY_LABEL_ANIMATION_RATE_LIMITING = "PreferenceDialog.label.animation.rate.limiting";
   public static final String PREFERENCE_KEY_LABEL_OTHER_OPTIONS = "PreferenceDialog.label.other.options";
   public static final String PREFERENCE_KEY_LABEL_ENABLE_DOUBLE_BUFFERING = "PreferenceDialog.label.enable.double.buffering";
   public static final String PREFERENCE_KEY_LABEL_SHOW_RENDERING = "PreferenceDialog.label.show.rendering";
   public static final String PREFERENCE_KEY_LABEL_AUTO_ADJUST_WINDOW = "PreferenceDialog.label.auto.adjust.window";
   public static final String PREFERENCE_KEY_LABEL_SELECTION_XOR_MODE = "PreferenceDialog.label.selection.xor.mode";
   public static final String PREFERENCE_KEY_LABEL_ANIMATION_LIMIT_CPU = "PreferenceDialog.label.animation.limit.cpu";
   public static final String PREFERENCE_KEY_LABEL_PERCENT = "PreferenceDialog.label.percent";
   public static final String PREFERENCE_KEY_LABEL_ANIMATION_LIMIT_FPS = "PreferenceDialog.label.animation.limit.fps";
   public static final String PREFERENCE_KEY_LABEL_FPS = "PreferenceDialog.label.fps";
   public static final String PREFERENCE_KEY_LABEL_ANIMATION_LIMIT_UNLIMITED = "PreferenceDialog.label.animation.limit.unlimited";
   public static final String PREFERENCE_KEY_LABEL_SHOW_DEBUG_TRACE = "PreferenceDialog.label.show.debug.trace";
   public static final String PREFERENCE_KEY_LABEL_IS_XML_PARSER_VALIDATING = "PreferenceDialog.label.is.xml.parser.validating";
   public static final String PREFERENCE_KEY_LABEL_GRANT_SCRIPTS_ACCESS_TO = "PreferenceDialog.label.grant.scripts.access.to";
   public static final String PREFERENCE_KEY_LABEL_LOAD_SCRIPTS = "PreferenceDialog.label.load.scripts";
   public static final String PREFERENCE_KEY_LABEL_ALLOWED_SCRIPT_ORIGIN = "PreferenceDialog.label.allowed.script.origin";
   public static final String PREFERENCE_KEY_LABEL_ALLOWED_RESOURCE_ORIGIN = "PreferenceDialog.label.allowed.resource.origin";
   public static final String PREFERENCE_KEY_LABEL_ENFORCE_SECURE_SCRIPTING = "PreferenceDialog.label.enforce.secure.scripting";
   public static final String PREFERENCE_KEY_LABEL_FILE_SYSTEM = "PreferenceDialog.label.file.system";
   public static final String PREFERENCE_KEY_LABEL_ALL_NETWORK = "PreferenceDialog.label.all.network";
   public static final String PREFERENCE_KEY_LABEL_JAVA_JAR_FILES = "PreferenceDialog.label.java.jar.files";
   public static final String PREFERENCE_KEY_LABEL_ECMASCRIPT = "PreferenceDialog.label.ecmascript";
   public static final String PREFERENCE_KEY_LABEL_ORIGIN_ANY = "PreferenceDialog.label.origin.any";
   public static final String PREFERENCE_KEY_LABEL_ORIGIN_DOCUMENT = "PreferenceDialog.label.origin.document";
   public static final String PREFERENCE_KEY_LABEL_ORIGIN_EMBEDDED = "PreferenceDialog.label.origin.embedded";
   public static final String PREFERENCE_KEY_LABEL_ORIGIN_NONE = "PreferenceDialog.label.origin.none";
   public static final String PREFERENCE_KEY_LABEL_USER_STYLESHEET = "PreferenceDialog.label.user.stylesheet";
   public static final String PREFERENCE_KEY_LABEL_CSS_MEDIA_TYPES = "PreferenceDialog.label.css.media.types";
   public static final String PREFERENCE_KEY_LABEL_ENABLE_USER_STYLESHEET = "PreferenceDialog.label.enable.user.stylesheet";
   public static final String PREFERENCE_KEY_LABEL_BROWSE = "PreferenceDialog.label.browse";
   public static final String PREFERENCE_KEY_LABEL_ADD = "PreferenceDialog.label.add";
   public static final String PREFERENCE_KEY_LABEL_REMOVE = "PreferenceDialog.label.remove";
   public static final String PREFERENCE_KEY_LABEL_CLEAR = "PreferenceDialog.label.clear";
   public static final String PREFERENCE_KEY_LABEL_HTTP_PROXY = "PreferenceDialog.label.http.proxy";
   public static final String PREFERENCE_KEY_LABEL_HOST = "PreferenceDialog.label.host";
   public static final String PREFERENCE_KEY_LABEL_PORT = "PreferenceDialog.label.port";
   public static final String PREFERENCE_KEY_LABEL_COLON = "PreferenceDialog.label.colon";
   public static final String PREFERENCE_KEY_BROWSE_TITLE = "PreferenceDialog.BrowseWindow.title";
   public static final String PREFERENCE_KEY_LANGUAGES = "preference.key.languages";
   public static final String PREFERENCE_KEY_IS_XML_PARSER_VALIDATING = "preference.key.is.xml.parser.validating";
   public static final String PREFERENCE_KEY_USER_STYLESHEET = "preference.key.user.stylesheet";
   public static final String PREFERENCE_KEY_USER_STYLESHEET_ENABLED = "preference.key.user.stylesheet.enabled";
   public static final String PREFERENCE_KEY_SHOW_RENDERING = "preference.key.show.rendering";
   public static final String PREFERENCE_KEY_AUTO_ADJUST_WINDOW = "preference.key.auto.adjust.window";
   public static final String PREFERENCE_KEY_ENABLE_DOUBLE_BUFFERING = "preference.key.enable.double.buffering";
   public static final String PREFERENCE_KEY_SHOW_DEBUG_TRACE = "preference.key.show.debug.trace";
   public static final String PREFERENCE_KEY_SELECTION_XOR_MODE = "preference.key.selection.xor.mode";
   public static final String PREFERENCE_KEY_PROXY_HOST = "preference.key.proxy.host";
   public static final String PREFERENCE_KEY_CSS_MEDIA = "preference.key.cssmedia";
   public static final String PREFERENCE_KEY_DEFAULT_FONT_FAMILY = "preference.key.default.font.family";
   public static final String PREFERENCE_KEY_PROXY_PORT = "preference.key.proxy.port";
   public static final String PREFERENCE_KEY_ENFORCE_SECURE_SCRIPTING = "preference.key.enforce.secure.scripting";
   public static final String PREFERENCE_KEY_GRANT_SCRIPT_FILE_ACCESS = "preference.key.grant.script.file.access";
   public static final String PREFERENCE_KEY_GRANT_SCRIPT_NETWORK_ACCESS = "preference.key.grant.script.network.access";
   public static final String PREFERENCE_KEY_LOAD_ECMASCRIPT = "preference.key.load.ecmascript";
   public static final String PREFERENCE_KEY_LOAD_JAVA = "preference.key.load.java.script";
   public static final String PREFERENCE_KEY_ALLOWED_SCRIPT_ORIGIN = "preference.key.allowed.script.origin";
   public static final String PREFERENCE_KEY_ALLOWED_EXTERNAL_RESOURCE_ORIGIN = "preference.key.allowed.external.resource.origin";
   public static final String PREFERENCE_KEY_ANIMATION_RATE_LIMITING_MODE = "preference.key.animation.rate.limiting.mode";
   public static final String PREFERENCE_KEY_ANIMATION_RATE_LIMITING_CPU = "preference.key.animation.rate.limiting.cpu";
   public static final String PREFERENCE_KEY_ANIMATION_RATE_LIMITING_FPS = "preference.key.animation.rate.limiting.fps";
   public static final String LABEL_OK = "PreferenceDialog.label.ok";
   public static final String LABEL_CANCEL = "PreferenceDialog.label.cancel";
   protected PreferenceManager model;
   protected JConfigurationPanel configurationPanel;
   protected JCheckBox userStylesheetEnabled;
   protected JLabel userStylesheetLabel;
   protected JTextField userStylesheet;
   protected JButton userStylesheetBrowse;
   protected JCheckBox showRendering;
   protected JCheckBox autoAdjustWindow;
   protected JCheckBox enableDoubleBuffering;
   protected JCheckBox showDebugTrace;
   protected JCheckBox selectionXorMode;
   protected JCheckBox isXMLParserValidating;
   protected JRadioButton animationLimitUnlimited;
   protected JRadioButton animationLimitCPU;
   protected JRadioButton animationLimitFPS;
   protected JLabel animationLimitCPULabel;
   protected JLabel animationLimitFPSLabel;
   protected JTextField animationLimitCPUAmount;
   protected JTextField animationLimitFPSAmount;
   protected JCheckBox enforceSecureScripting;
   protected JCheckBox grantScriptFileAccess;
   protected JCheckBox grantScriptNetworkAccess;
   protected JCheckBox loadJava;
   protected JCheckBox loadEcmascript;
   protected JComboBox allowedScriptOrigin;
   protected JComboBox allowedResourceOrigin;
   protected JList mediaList;
   protected JButton mediaListRemoveButton;
   protected JButton mediaListClearButton;
   protected JTextField host;
   protected JTextField port;
   protected LanguageDialog.Panel languagePanel;
   protected DefaultListModel mediaListModel = new DefaultListModel();
   protected int returnCode;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$PreferenceDialog;
   // $FF: synthetic field
   static Class class$javax$swing$AbstractButton;

   protected static boolean isMetalSteel() {
      if (!UIManager.getLookAndFeel().getName().equals("Metal")) {
         return false;
      } else {
         try {
            LookAndFeel var0 = UIManager.getLookAndFeel();
            var0.getClass().getMethod("getCurrentTheme");
            return false;
         } catch (Exception var1) {
            return true;
         }
      }
   }

   public PreferenceDialog(Frame var1, PreferenceManager var2) {
      super(var1, true);
      if (var2 == null) {
         throw new IllegalArgumentException();
      } else {
         this.model = var2;
         this.buildGUI();
         this.initializeGUI();
         this.pack();
         this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
               if (Platform.isOSX) {
                  PreferenceDialog.this.savePreferences();
               }

            }
         });
      }
   }

   public PreferenceManager getPreferenceManager() {
      return this.model;
   }

   protected void initializeGUI() {
      this.enableDoubleBuffering.setSelected(this.model.getBoolean("preference.key.enable.double.buffering"));
      this.showRendering.setSelected(this.model.getBoolean("preference.key.show.rendering"));
      this.autoAdjustWindow.setSelected(this.model.getBoolean("preference.key.auto.adjust.window"));
      this.selectionXorMode.setSelected(this.model.getBoolean("preference.key.selection.xor.mode"));
      switch (this.model.getInteger("preference.key.animation.rate.limiting.mode")) {
         case 0:
            this.animationLimitUnlimited.setSelected(true);
            break;
         case 2:
            this.animationLimitFPS.setSelected(true);
            break;
         default:
            this.animationLimitCPU.setSelected(true);
      }

      float var2 = this.model.getFloat("preference.key.animation.rate.limiting.cpu");
      if (!(var2 <= 0.0F) && !(var2 > 100.0F)) {
         var2 *= 100.0F;
      } else {
         var2 = 85.0F;
      }

      if ((float)((int)var2) == var2) {
         this.animationLimitCPUAmount.setText(Integer.toString((int)var2));
      } else {
         this.animationLimitCPUAmount.setText(Float.toString(var2));
      }

      var2 = this.model.getFloat("preference.key.animation.rate.limiting.fps");
      if (var2 <= 0.0F) {
         var2 = 10.0F;
      }

      if ((float)((int)var2) == var2) {
         this.animationLimitFPSAmount.setText(Integer.toString((int)var2));
      } else {
         this.animationLimitFPSAmount.setText(Float.toString(var2));
      }

      this.showDebugTrace.setSelected(this.model.getBoolean("preference.key.show.debug.trace"));
      this.isXMLParserValidating.setSelected(this.model.getBoolean("preference.key.is.xml.parser.validating"));
      this.enforceSecureScripting.setSelected(this.model.getBoolean("preference.key.enforce.secure.scripting"));
      this.grantScriptFileAccess.setSelected(this.model.getBoolean("preference.key.grant.script.file.access"));
      this.grantScriptNetworkAccess.setSelected(this.model.getBoolean("preference.key.grant.script.network.access"));
      this.loadJava.setSelected(this.model.getBoolean("preference.key.load.java.script"));
      this.loadEcmascript.setSelected(this.model.getBoolean("preference.key.load.ecmascript"));
      int var3 = this.model.getInteger("preference.key.allowed.script.origin");
      switch (var3) {
         case 1:
            this.allowedScriptOrigin.setSelectedIndex(0);
            break;
         case 2:
            this.allowedScriptOrigin.setSelectedIndex(1);
            break;
         case 3:
         default:
            this.allowedScriptOrigin.setSelectedIndex(3);
            break;
         case 4:
            this.allowedScriptOrigin.setSelectedIndex(2);
      }

      var3 = this.model.getInteger("preference.key.allowed.external.resource.origin");
      switch (var3) {
         case 1:
            this.allowedResourceOrigin.setSelectedIndex(0);
            break;
         case 2:
            this.allowedResourceOrigin.setSelectedIndex(1);
            break;
         case 3:
         default:
            this.allowedResourceOrigin.setSelectedIndex(3);
            break;
         case 4:
            this.allowedResourceOrigin.setSelectedIndex(2);
      }

      this.languagePanel.setLanguages(this.model.getString("preference.key.languages"));
      String var4 = this.model.getString("preference.key.cssmedia");
      this.mediaListModel.removeAllElements();
      StringTokenizer var5 = new StringTokenizer(var4, " ");

      while(var5.hasMoreTokens()) {
         this.mediaListModel.addElement(var5.nextToken());
      }

      this.userStylesheet.setText(this.model.getString("preference.key.user.stylesheet"));
      boolean var1 = this.model.getBoolean("preference.key.user.stylesheet.enabled");
      this.userStylesheetEnabled.setSelected(var1);
      this.host.setText(this.model.getString("preference.key.proxy.host"));
      this.port.setText(this.model.getString("preference.key.proxy.port"));
      var1 = this.enableDoubleBuffering.isSelected();
      this.showRendering.setEnabled(var1);
      var1 = this.animationLimitCPU.isSelected();
      this.animationLimitCPUAmount.setEnabled(var1);
      this.animationLimitCPULabel.setEnabled(var1);
      var1 = this.animationLimitFPS.isSelected();
      this.animationLimitFPSAmount.setEnabled(var1);
      this.animationLimitFPSLabel.setEnabled(var1);
      var1 = this.enforceSecureScripting.isSelected();
      this.grantScriptFileAccess.setEnabled(var1);
      this.grantScriptNetworkAccess.setEnabled(var1);
      var1 = this.userStylesheetEnabled.isSelected();
      this.userStylesheetLabel.setEnabled(var1);
      this.userStylesheet.setEnabled(var1);
      this.userStylesheetBrowse.setEnabled(var1);
      this.mediaListRemoveButton.setEnabled(!this.mediaList.isSelectionEmpty());
      this.mediaListClearButton.setEnabled(!this.mediaListModel.isEmpty());
   }

   protected void savePreferences() {
      this.model.setString("preference.key.languages", this.languagePanel.getLanguages());
      this.model.setString("preference.key.user.stylesheet", this.userStylesheet.getText());
      this.model.setBoolean("preference.key.user.stylesheet.enabled", this.userStylesheetEnabled.isSelected());
      this.model.setBoolean("preference.key.show.rendering", this.showRendering.isSelected());
      this.model.setBoolean("preference.key.auto.adjust.window", this.autoAdjustWindow.isSelected());
      this.model.setBoolean("preference.key.enable.double.buffering", this.enableDoubleBuffering.isSelected());
      this.model.setBoolean("preference.key.show.debug.trace", this.showDebugTrace.isSelected());
      this.model.setBoolean("preference.key.selection.xor.mode", this.selectionXorMode.isSelected());
      this.model.setBoolean("preference.key.is.xml.parser.validating", this.isXMLParserValidating.isSelected());
      this.model.setBoolean("preference.key.enforce.secure.scripting", this.enforceSecureScripting.isSelected());
      this.model.setBoolean("preference.key.grant.script.file.access", this.grantScriptFileAccess.isSelected());
      this.model.setBoolean("preference.key.grant.script.network.access", this.grantScriptNetworkAccess.isSelected());
      this.model.setBoolean("preference.key.load.java.script", this.loadJava.isSelected());
      this.model.setBoolean("preference.key.load.ecmascript", this.loadEcmascript.isSelected());
      byte var1;
      switch (this.allowedScriptOrigin.getSelectedIndex()) {
         case 0:
            var1 = 1;
            break;
         case 1:
            var1 = 2;
            break;
         case 2:
            var1 = 4;
            break;
         default:
            var1 = 8;
      }

      this.model.setInteger("preference.key.allowed.script.origin", var1);
      switch (this.allowedResourceOrigin.getSelectedIndex()) {
         case 0:
            var1 = 1;
            break;
         case 1:
            var1 = 2;
            break;
         case 2:
            var1 = 4;
            break;
         default:
            var1 = 8;
      }

      this.model.setInteger("preference.key.allowed.external.resource.origin", var1);
      var1 = 1;
      if (this.animationLimitFPS.isSelected()) {
         var1 = 2;
      } else if (this.animationLimitUnlimited.isSelected()) {
         var1 = 0;
      }

      this.model.setInteger("preference.key.animation.rate.limiting.mode", var1);

      float var2;
      try {
         var2 = Float.parseFloat(this.animationLimitCPUAmount.getText()) / 100.0F;
         if (var2 <= 0.0F || var2 >= 1.0F) {
            var2 = 0.85F;
         }
      } catch (NumberFormatException var6) {
         var2 = 0.85F;
      }

      this.model.setFloat("preference.key.animation.rate.limiting.cpu", var2);

      try {
         var2 = Float.parseFloat(this.animationLimitFPSAmount.getText());
         if (var2 <= 0.0F) {
            var2 = 15.0F;
         }
      } catch (NumberFormatException var5) {
         var2 = 15.0F;
      }

      this.model.setFloat("preference.key.animation.rate.limiting.fps", var2);
      this.model.setString("preference.key.proxy.host", this.host.getText());
      this.model.setString("preference.key.proxy.port", this.port.getText());
      StringBuffer var3 = new StringBuffer();
      Enumeration var4 = this.mediaListModel.elements();

      while(var4.hasMoreElements()) {
         var3.append((String)var4.nextElement());
         var3.append(' ');
      }

      this.model.setString("preference.key.cssmedia", var3.toString());
   }

   protected void buildGUI() {
      JPanel var1 = new JPanel(new BorderLayout());
      this.configurationPanel = new JConfigurationPanel();
      this.addConfigPanel("general", this.buildGeneralPanel());
      this.addConfigPanel("security", this.buildSecurityPanel());
      this.addConfigPanel("language", this.buildLanguagePanel());
      this.addConfigPanel("stylesheet", this.buildStylesheetPanel());
      this.addConfigPanel("network", this.buildNetworkPanel());
      var1.add(this.configurationPanel);
      if (!Platform.isOSX) {
         this.setTitle(Resources.getString("PreferenceDialog.title.dialog"));
         var1.add(this.buildButtonsPanel(), "South");
      }

      this.setResizable(false);
      this.getContentPane().add(var1);
   }

   protected void addConfigPanel(String var1, JPanel var2) {
      String var3 = Resources.getString("PreferenceDialog.title." + var1);
      ImageIcon var4 = new ImageIcon((class$org$apache$batik$apps$svgbrowser$PreferenceDialog == null ? (class$org$apache$batik$apps$svgbrowser$PreferenceDialog = class$("org.apache.batik.apps.svgbrowser.PreferenceDialog")) : class$org$apache$batik$apps$svgbrowser$PreferenceDialog).getResource("resources/icon-" + var1 + ".png"));
      ImageIcon var5 = new ImageIcon((class$org$apache$batik$apps$svgbrowser$PreferenceDialog == null ? (class$org$apache$batik$apps$svgbrowser$PreferenceDialog = class$("org.apache.batik.apps.svgbrowser.PreferenceDialog")) : class$org$apache$batik$apps$svgbrowser$PreferenceDialog).getResource("resources/icon-" + var1 + "-dark.png"));
      this.configurationPanel.addPanel(var3, var4, var5, var2);
   }

   protected JPanel buildButtonsPanel() {
      JPanel var1 = new JPanel(new FlowLayout(2));
      JButton var2 = new JButton(Resources.getString("PreferenceDialog.label.ok"));
      JButton var3 = new JButton(Resources.getString("PreferenceDialog.label.cancel"));
      var1.add(var2);
      var1.add(var3);
      var2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PreferenceDialog.this.setVisible(false);
            PreferenceDialog.this.returnCode = 0;
            PreferenceDialog.this.savePreferences();
            PreferenceDialog.this.dispose();
         }
      });
      var3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PreferenceDialog.this.setVisible(false);
            PreferenceDialog.this.returnCode = 1;
            PreferenceDialog.this.dispose();
         }
      });
      this.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            switch (var1.getKeyCode()) {
               case 10:
                  PreferenceDialog.this.returnCode = 0;
                  break;
               case 27:
                  PreferenceDialog.this.returnCode = 1;
                  break;
               default:
                  return;
            }

            PreferenceDialog.this.setVisible(false);
            PreferenceDialog.this.dispose();
         }
      });
      return var1;
   }

   protected JPanel buildGeneralPanel() {
      JGridBagPanel.InsetsManager var1 = new JGridBagPanel.InsetsManager() {
         protected Insets i1 = new Insets(5, 5, 0, 0);
         protected Insets i2 = new Insets(5, 0, 0, 0);
         protected Insets i3 = new Insets(0, 5, 0, 0);
         protected Insets i4 = new Insets(0, 0, 0, 0);

         public Insets getInsets(int var1, int var2) {
            if (var2 != 4 && var2 != 9) {
               return var1 == 0 ? this.i4 : this.i3;
            } else {
               return var1 == 0 ? this.i2 : this.i1;
            }
         }
      };
      JGridBagPanel var2 = new JGridBagPanel(var1);
      var2.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
      JLabel var3 = new JLabel(Resources.getString("PreferenceDialog.label.rendering.options"));
      this.enableDoubleBuffering = new JCheckBox(Resources.getString("PreferenceDialog.label.enable.double.buffering"));
      this.enableDoubleBuffering.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PreferenceDialog.this.showRendering.setEnabled(PreferenceDialog.this.enableDoubleBuffering.isSelected());
         }
      });
      this.showRendering = new JCheckBox(Resources.getString("PreferenceDialog.label.show.rendering"));
      Insets var4 = this.showRendering.getMargin();
      this.showRendering.setMargin(new Insets(var4.top, var4.left + 24, var4.bottom, var4.right));
      this.selectionXorMode = new JCheckBox(Resources.getString("PreferenceDialog.label.selection.xor.mode"));
      this.autoAdjustWindow = new JCheckBox(Resources.getString("PreferenceDialog.label.auto.adjust.window"));
      JLabel var5 = new JLabel(Resources.getString("PreferenceDialog.label.animation.rate.limiting"));
      this.animationLimitCPU = new JRadioButton(Resources.getString("PreferenceDialog.label.animation.limit.cpu"));
      JPanel var6 = new JPanel();
      var6.setLayout(new FlowLayout(3, 3, 0));
      var6.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));
      this.animationLimitCPUAmount = new JTextField();
      this.animationLimitCPUAmount.setPreferredSize(new Dimension(40, 20));
      var6.add(this.animationLimitCPUAmount);
      this.animationLimitCPULabel = new JLabel(Resources.getString("PreferenceDialog.label.percent"));
      var6.add(this.animationLimitCPULabel);
      this.animationLimitFPS = new JRadioButton(Resources.getString("PreferenceDialog.label.animation.limit.fps"));
      JPanel var7 = new JPanel();
      var7.setLayout(new FlowLayout(3, 3, 0));
      var7.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));
      this.animationLimitFPSAmount = new JTextField();
      this.animationLimitFPSAmount.setPreferredSize(new Dimension(40, 20));
      var7.add(this.animationLimitFPSAmount);
      this.animationLimitFPSLabel = new JLabel(Resources.getString("PreferenceDialog.label.fps"));
      var7.add(this.animationLimitFPSLabel);
      this.animationLimitUnlimited = new JRadioButton(Resources.getString("PreferenceDialog.label.animation.limit.unlimited"));
      ButtonGroup var8 = new ButtonGroup();
      var8.add(this.animationLimitCPU);
      var8.add(this.animationLimitFPS);
      var8.add(this.animationLimitUnlimited);
      ActionListener var9 = new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            boolean var2 = PreferenceDialog.this.animationLimitCPU.isSelected();
            PreferenceDialog.this.animationLimitCPUAmount.setEnabled(var2);
            PreferenceDialog.this.animationLimitCPULabel.setEnabled(var2);
            var2 = PreferenceDialog.this.animationLimitFPS.isSelected();
            PreferenceDialog.this.animationLimitFPSAmount.setEnabled(var2);
            PreferenceDialog.this.animationLimitFPSLabel.setEnabled(var2);
         }
      };
      this.animationLimitCPU.addActionListener(var9);
      this.animationLimitFPS.addActionListener(var9);
      this.animationLimitUnlimited.addActionListener(var9);
      JLabel var10 = new JLabel(Resources.getString("PreferenceDialog.label.other.options"));
      this.showDebugTrace = new JCheckBox(Resources.getString("PreferenceDialog.label.show.debug.trace"));
      this.isXMLParserValidating = new JCheckBox(Resources.getString("PreferenceDialog.label.is.xml.parser.validating"));
      var2.add(var3, 0, 0, 1, 1, 13, 0, 0.0, 0.0);
      var2.add(this.enableDoubleBuffering, 1, 0, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(this.showRendering, 1, 1, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(this.autoAdjustWindow, 1, 2, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(this.selectionXorMode, 1, 3, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(var5, 0, 4, 1, 1, 13, 0, 0.0, 0.0);
      var2.add(this.animationLimitCPU, 1, 4, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(var6, 1, 5, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(this.animationLimitFPS, 1, 6, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(var7, 1, 7, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(this.animationLimitUnlimited, 1, 8, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(var10, 0, 9, 1, 1, 13, 0, 0.0, 0.0);
      var2.add(this.showDebugTrace, 1, 9, 1, 1, 17, 0, 0.0, 0.0);
      var2.add(this.isXMLParserValidating, 1, 10, 1, 1, 17, 0, 0.0, 0.0);
      return var2;
   }

   protected JPanel buildSecurityPanel() {
      JGridBagPanel.InsetsManager var1 = new JGridBagPanel.InsetsManager() {
         protected Insets i1 = new Insets(5, 5, 0, 0);
         protected Insets i2 = new Insets(5, 0, 0, 0);
         protected Insets i3 = new Insets(0, 5, 0, 0);
         protected Insets i4 = new Insets(0, 0, 0, 0);

         public Insets getInsets(int var1, int var2) {
            if (var2 != 1 && var2 != 3 && var2 != 5 && var2 != 6) {
               return var1 == 0 ? this.i4 : this.i3;
            } else {
               return var1 == 0 ? this.i2 : this.i1;
            }
         }
      };
      JGridBagPanel var2 = new JGridBagPanel(var1);
      var2.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
      this.enforceSecureScripting = new JCheckBox(Resources.getString("PreferenceDialog.label.enforce.secure.scripting"));
      this.enforceSecureScripting.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            boolean var2 = PreferenceDialog.this.enforceSecureScripting.isSelected();
            PreferenceDialog.this.grantScriptFileAccess.setEnabled(var2);
            PreferenceDialog.this.grantScriptNetworkAccess.setEnabled(var2);
         }
      });
      JLabel var3 = new JLabel(Resources.getString("PreferenceDialog.label.grant.scripts.access.to"));
      var3.setVerticalAlignment(1);
      var3.setOpaque(true);
      this.grantScriptFileAccess = new JCheckBox(Resources.getString("PreferenceDialog.label.file.system"));
      this.grantScriptNetworkAccess = new JCheckBox(Resources.getString("PreferenceDialog.label.all.network"));
      JLabel var4 = new JLabel(Resources.getString("PreferenceDialog.label.load.scripts"));
      var4.setVerticalAlignment(1);
      this.loadJava = new JCheckBox(Resources.getString("PreferenceDialog.label.java.jar.files"));
      this.loadEcmascript = new JCheckBox(Resources.getString("PreferenceDialog.label.ecmascript"));
      String[] var5 = new String[]{Resources.getString("PreferenceDialog.label.origin.any"), Resources.getString("PreferenceDialog.label.origin.document"), Resources.getString("PreferenceDialog.label.origin.embedded"), Resources.getString("PreferenceDialog.label.origin.none")};
      JLabel var6 = new JLabel(Resources.getString("PreferenceDialog.label.allowed.script.origin"));
      this.allowedScriptOrigin = new JComboBox(var5);
      JLabel var7 = new JLabel(Resources.getString("PreferenceDialog.label.allowed.resource.origin"));
      this.allowedResourceOrigin = new JComboBox(var5);
      var2.add(this.enforceSecureScripting, 1, 0, 1, 1, 17, 0, 1.0, 0.0);
      var2.add(var3, 0, 1, 1, 1, 13, 0, 1.0, 0.0);
      var2.add(this.grantScriptFileAccess, 1, 1, 1, 1, 17, 0, 1.0, 0.0);
      var2.add(this.grantScriptNetworkAccess, 1, 2, 1, 1, 17, 0, 1.0, 0.0);
      var2.add(var4, 0, 3, 1, 1, 13, 0, 1.0, 0.0);
      var2.add(this.loadJava, 1, 3, 1, 1, 17, 0, 1.0, 0.0);
      var2.add(this.loadEcmascript, 1, 4, 1, 1, 17, 0, 1.0, 0.0);
      var2.add(var6, 0, 5, 1, 1, 13, 0, 1.0, 0.0);
      var2.add(this.allowedScriptOrigin, 1, 5, 1, 1, 17, 0, 1.0, 0.0);
      var2.add(var7, 0, 6, 1, 1, 13, 0, 1.0, 0.0);
      var2.add(this.allowedResourceOrigin, 1, 6, 1, 1, 17, 0, 1.0, 0.0);
      return var2;
   }

   protected JPanel buildLanguagePanel() {
      JPanel var1 = new JPanel();
      var1.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
      this.languagePanel = new LanguageDialog.Panel();
      this.languagePanel.setBorder(BorderFactory.createEmptyBorder());
      Color var2 = UIManager.getColor("Window.background");
      this.languagePanel.getComponent(0).setBackground(var2);
      this.languagePanel.getComponent(1).setBackground(var2);
      var1.add(this.languagePanel);
      return var1;
   }

   protected JPanel buildStylesheetPanel() {
      JGridBagPanel.InsetsManager var1 = new JGridBagPanel.InsetsManager() {
         protected Insets i1 = new Insets(5, 5, 0, 0);
         protected Insets i2 = new Insets(5, 0, 0, 0);
         protected Insets i3 = new Insets(0, 5, 0, 0);
         protected Insets i4 = new Insets(0, 0, 0, 0);

         public Insets getInsets(int var1, int var2) {
            if (var2 >= 1 && var2 <= 5) {
               return var1 == 0 ? this.i2 : this.i1;
            } else {
               return var1 == 0 ? this.i4 : this.i3;
            }
         }
      };
      JGridBagPanel var2 = new JGridBagPanel(var1);
      var2.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
      this.userStylesheetEnabled = new JCheckBox(Resources.getString("PreferenceDialog.label.enable.user.stylesheet"));
      this.userStylesheetEnabled.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            boolean var2 = PreferenceDialog.this.userStylesheetEnabled.isSelected();
            PreferenceDialog.this.userStylesheetLabel.setEnabled(var2);
            PreferenceDialog.this.userStylesheet.setEnabled(var2);
            PreferenceDialog.this.userStylesheetBrowse.setEnabled(var2);
         }
      });
      this.userStylesheetLabel = new JLabel(Resources.getString("PreferenceDialog.label.user.stylesheet"));
      this.userStylesheet = new JTextField();
      this.userStylesheetBrowse = new JButton(Resources.getString("PreferenceDialog.label.browse"));
      this.userStylesheetBrowse.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            File var2 = null;
            if (Platform.isOSX) {
               FileDialog var3 = new FileDialog((Frame)PreferenceDialog.this.getOwner(), Resources.getString("PreferenceDialog.BrowseWindow.title"));
               var3.setVisible(true);
               String var4 = var3.getFile();
               if (var4 != null) {
                  String var5 = var3.getDirectory();
                  var2 = new File(var5, var4);
               }
            } else {
               JFileChooser var7 = new JFileChooser(new File("."));
               var7.setDialogTitle(Resources.getString("PreferenceDialog.BrowseWindow.title"));
               var7.setFileHidingEnabled(false);
               int var8 = var7.showOpenDialog(PreferenceDialog.this);
               if (var8 == 0) {
                  var2 = var7.getSelectedFile();
               }
            }

            if (var2 != null) {
               try {
                  PreferenceDialog.this.userStylesheet.setText(var2.getCanonicalPath());
               } catch (IOException var6) {
               }
            }

         }
      });
      JLabel var3 = new JLabel(Resources.getString("PreferenceDialog.label.css.media.types"));
      var3.setVerticalAlignment(1);
      this.mediaList = new JList();
      this.mediaList.setSelectionMode(0);
      this.mediaList.setModel(this.mediaListModel);
      this.mediaList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent var1) {
            PreferenceDialog.this.updateMediaListButtons();
         }
      });
      this.mediaListModel.addListDataListener(new ListDataListener() {
         public void contentsChanged(ListDataEvent var1) {
            PreferenceDialog.this.updateMediaListButtons();
         }

         public void intervalAdded(ListDataEvent var1) {
            PreferenceDialog.this.updateMediaListButtons();
         }

         public void intervalRemoved(ListDataEvent var1) {
            PreferenceDialog.this.updateMediaListButtons();
         }
      });
      JScrollPane var4 = new JScrollPane();
      var4.setBorder(BorderFactory.createLoweredBevelBorder());
      var4.getViewport().add(this.mediaList);
      JButton var5 = new JButton(Resources.getString("PreferenceDialog.label.add"));
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CSSMediaPanel.AddMediumDialog var2 = new CSSMediaPanel.AddMediumDialog(PreferenceDialog.this);
            var2.pack();
            var2.setVisible(true);
            if (var2.getReturnCode() != 1 && var2.getMedium() != null) {
               String var3 = var2.getMedium().trim();
               if (var3.length() != 0 && !PreferenceDialog.this.mediaListModel.contains(var3)) {
                  for(int var4 = 0; var4 < PreferenceDialog.this.mediaListModel.size() && var3 != null; ++var4) {
                     String var5 = (String)PreferenceDialog.this.mediaListModel.getElementAt(var4);
                     int var6 = var3.compareTo(var5);
                     if (var6 == 0) {
                        var3 = null;
                     } else if (var6 < 0) {
                        PreferenceDialog.this.mediaListModel.insertElementAt(var3, var4);
                        var3 = null;
                     }
                  }

                  if (var3 != null) {
                     PreferenceDialog.this.mediaListModel.addElement(var3);
                  }

               }
            }
         }
      });
      this.mediaListRemoveButton = new JButton(Resources.getString("PreferenceDialog.label.remove"));
      this.mediaListRemoveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            int var2 = PreferenceDialog.this.mediaList.getSelectedIndex();
            PreferenceDialog.this.mediaList.clearSelection();
            if (var2 >= 0) {
               PreferenceDialog.this.mediaListModel.removeElementAt(var2);
            }

         }
      });
      this.mediaListClearButton = new JButton(Resources.getString("PreferenceDialog.label.clear"));
      this.mediaListClearButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            PreferenceDialog.this.mediaList.clearSelection();
            PreferenceDialog.this.mediaListModel.removeAllElements();
         }
      });
      var2.add(this.userStylesheetEnabled, 1, 0, 2, 1, 17, 0, 0.0, 0.0);
      var2.add(this.userStylesheetLabel, 0, 1, 1, 1, 13, 0, 0.0, 0.0);
      var2.add(this.userStylesheet, 1, 1, 1, 1, 17, 2, 1.0, 0.0);
      var2.add(this.userStylesheetBrowse, 2, 1, 1, 1, 17, 2, 0.0, 0.0);
      var2.add(var3, 0, 2, 1, 1, 13, 3, 0.0, 0.0);
      var2.add(var4, 1, 2, 1, 4, 17, 1, 1.0, 1.0);
      var2.add(new JPanel(), 2, 2, 1, 1, 17, 1, 0.0, 1.0);
      var2.add(var5, 2, 3, 1, 1, 16, 2, 0.0, 0.0);
      var2.add(this.mediaListRemoveButton, 2, 4, 1, 1, 16, 2, 0.0, 0.0);
      var2.add(this.mediaListClearButton, 2, 5, 1, 1, 16, 2, 0.0, 0.0);
      return var2;
   }

   protected void updateMediaListButtons() {
      this.mediaListRemoveButton.setEnabled(!this.mediaList.isSelectionEmpty());
      this.mediaListClearButton.setEnabled(!this.mediaListModel.isEmpty());
   }

   protected JPanel buildNetworkPanel() {
      JGridBagPanel var1 = new JGridBagPanel();
      var1.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
      JLabel var2 = new JLabel(Resources.getString("PreferenceDialog.label.http.proxy"));
      JLabel var3 = new JLabel(Resources.getString("PreferenceDialog.label.host"));
      JLabel var4 = new JLabel(Resources.getString("PreferenceDialog.label.port"));
      JLabel var5 = new JLabel(Resources.getString("PreferenceDialog.label.colon"));
      Font var6 = var3.getFont();
      float var7 = var6.getSize2D() * 0.85F;
      var6 = var6.deriveFont(var7);
      var3.setFont(var6);
      var4.setFont(var6);
      this.host = new JTextField();
      this.host.setPreferredSize(new Dimension(200, 20));
      this.port = new JTextField();
      this.port.setPreferredSize(new Dimension(40, 20));
      var1.add(var2, 0, 0, 1, 1, 13, 0, 0.0, 0.0);
      var1.add(this.host, 1, 0, 1, 1, 17, 2, 0.0, 0.0);
      var1.add(var5, 2, 0, 1, 1, 17, 0, 0.0, 0.0);
      var1.add(this.port, 3, 0, 1, 1, 17, 2, 0.0, 0.0);
      var1.add(var3, 1, 1, 1, 1, 17, 0, 0.0, 0.0);
      var1.add(var4, 3, 1, 1, 1, 17, 0, 0.0, 0.0);
      return var1;
   }

   public int showDialog() {
      if (Platform.isOSX) {
         this.returnCode = 0;
      } else {
         this.returnCode = 1;
      }

      this.pack();
      this.setVisible(true);
      return this.returnCode;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   protected class JConfigurationPanel extends JPanel {
      protected JToolBar toolbar = new JToolBar();
      protected JPanel panel;
      protected CardLayout layout;
      protected ButtonGroup group;
      protected int page = -1;

      public JConfigurationPanel() {
         this.toolbar.setFloatable(false);
         this.toolbar.setLayout(new FlowLayout(3, 0, 0));
         this.toolbar.add(new JToolBar.Separator(new Dimension(8, 8)));
         if (Platform.isOSX || PreferenceDialog.isMetalSteel()) {
            this.toolbar.setBackground(new Color(248, 248, 248));
         }

         this.toolbar.setOpaque(true);
         this.panel = new JPanel();
         this.layout = (CardLayout)(Platform.isOSX ? new ResizingCardLayout() : new CardLayout());
         this.group = new ButtonGroup();
         this.setLayout(new BorderLayout());
         this.panel.setLayout(this.layout);
         this.add(this.toolbar, "North");
         this.add(this.panel);
      }

      public void addPanel(String var1, Icon var2, Icon var3, JPanel var4) {
         JToggleButton var5 = new JToggleButton(var1, var2);
         var5.setVerticalTextPosition(3);
         var5.setHorizontalTextPosition(0);
         var5.setContentAreaFilled(false);

         try {
            (PreferenceDialog.class$javax$swing$AbstractButton == null ? (PreferenceDialog.class$javax$swing$AbstractButton = PreferenceDialog.class$("javax.swing.AbstractButton")) : PreferenceDialog.class$javax$swing$AbstractButton).getMethod("setIconTextGap", Integer.TYPE).invoke(var5, new Integer(0));
         } catch (Exception var7) {
         }

         var5.setPressedIcon(var3);
         this.group.add(var5);
         this.toolbar.add(var5);
         this.toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
         var5.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent var1) {
               JToggleButton var2 = (JToggleButton)var1.getSource();
               switch (var1.getStateChange()) {
                  case 1:
                     JConfigurationPanel.this.select(var2);
                     break;
                  case 2:
                     JConfigurationPanel.this.unselect(var2);
               }

            }
         });
         if (this.panel.getComponentCount() == 0) {
            var5.setSelected(true);
            this.page = 0;
         } else {
            this.unselect(var5);
         }

         this.panel.add(var4, var1.intern());
      }

      protected int getComponentIndex(Component var1) {
         Container var2 = var1.getParent();
         int var3 = var2.getComponentCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var2.getComponent(var4) == var1) {
               return var4;
            }
         }

         return -1;
      }

      protected void select(JToggleButton var1) {
         var1.setOpaque(true);
         var1.setBackground(Platform.isOSX ? new Color(216, 216, 216) : UIManager.getColor("List.selectionBackground"));
         var1.setForeground(UIManager.getColor("List.selectionForeground"));
         var1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, new Color(160, 160, 160)), BorderFactory.createEmptyBorder(4, 3, 4, 3)));
         this.layout.show(this.panel, var1.getText().intern());
         this.page = this.getComponentIndex(var1) - 1;
         if (Platform.isOSX) {
            PreferenceDialog.this.setTitle(var1.getText());
         }

         PreferenceDialog.this.pack();
         this.panel.grabFocus();
      }

      protected void unselect(JToggleButton var1) {
         var1.setOpaque(false);
         var1.setBackground((Color)null);
         var1.setForeground(UIManager.getColor("Button.foreground"));
         var1.setBorder(BorderFactory.createEmptyBorder(5, 4, 5, 4));
      }

      protected class ResizingCardLayout extends CardLayout {
         public ResizingCardLayout() {
            super(0, 0);
         }

         public Dimension preferredLayoutSize(Container var1) {
            Dimension var2 = super.preferredLayoutSize(var1);
            if (JConfigurationPanel.this.page != -1) {
               Dimension var3 = JConfigurationPanel.this.panel.getComponent(JConfigurationPanel.this.page).getPreferredSize();
               var2 = new Dimension((int)var2.getWidth(), (int)var3.getHeight());
            }

            return var2;
         }
      }
   }
}
