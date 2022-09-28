package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import org.apache.batik.bridge.DefaultExternalResourceSecurity;
import org.apache.batik.bridge.DefaultScriptSecurity;
import org.apache.batik.bridge.EmbededExternalResourceSecurity;
import org.apache.batik.bridge.EmbededScriptSecurity;
import org.apache.batik.bridge.ExternalResourceSecurity;
import org.apache.batik.bridge.NoLoadExternalResourceSecurity;
import org.apache.batik.bridge.NoLoadScriptSecurity;
import org.apache.batik.bridge.RelaxedExternalResourceSecurity;
import org.apache.batik.bridge.RelaxedScriptSecurity;
import org.apache.batik.bridge.ScriptSecurity;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.dom.StyleSheetProcessingInstruction;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.ext.swing.JAffineTransformChooser;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.gvt.Overlay;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;
import org.apache.batik.swing.svg.LinkActivationEvent;
import org.apache.batik.swing.svg.LinkActivationListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.apache.batik.swing.svg.SVGFileFilter;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherEvent;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherListener;
import org.apache.batik.swing.svg.SVGUserAgent;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.apache.batik.transcoder.print.PrintTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.Platform;
import org.apache.batik.util.Service;
import org.apache.batik.util.gui.JErrorPane;
import org.apache.batik.util.gui.LocationBar;
import org.apache.batik.util.gui.MemoryMonitor;
import org.apache.batik.util.gui.URIChooser;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.JComponentModifier;
import org.apache.batik.util.gui.resource.MenuFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.gui.resource.ToolBarFactory;
import org.apache.batik.util.gui.xmleditor.XMLDocument;
import org.apache.batik.util.gui.xmleditor.XMLTextEditor;
import org.apache.batik.util.resources.ResourceManager;
import org.apache.batik.xml.XMLUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class JSVGViewerFrame extends JFrame implements ActionMap, SVGDocumentLoaderListener, GVTTreeBuilderListener, SVGLoadEventDispatcherListener, GVTTreeRendererListener, LinkActivationListener, UpdateManagerListener {
   private static String EOL;
   protected static boolean priorJDK1_4;
   protected static final String JDK_1_4_PRESENCE_TEST_CLASS = "java.util.logging.LoggingPermission";
   public static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.GUI";
   public static final String ABOUT_ACTION = "AboutAction";
   public static final String OPEN_ACTION = "OpenAction";
   public static final String OPEN_LOCATION_ACTION = "OpenLocationAction";
   public static final String NEW_WINDOW_ACTION = "NewWindowAction";
   public static final String RELOAD_ACTION = "ReloadAction";
   public static final String SAVE_AS_ACTION = "SaveAsAction";
   public static final String BACK_ACTION = "BackAction";
   public static final String FORWARD_ACTION = "ForwardAction";
   public static final String FULL_SCREEN_ACTION = "FullScreenAction";
   public static final String PRINT_ACTION = "PrintAction";
   public static final String EXPORT_AS_JPG_ACTION = "ExportAsJPGAction";
   public static final String EXPORT_AS_PNG_ACTION = "ExportAsPNGAction";
   public static final String EXPORT_AS_TIFF_ACTION = "ExportAsTIFFAction";
   public static final String PREFERENCES_ACTION = "PreferencesAction";
   public static final String CLOSE_ACTION = "CloseAction";
   public static final String VIEW_SOURCE_ACTION = "ViewSourceAction";
   public static final String EXIT_ACTION = "ExitAction";
   public static final String RESET_TRANSFORM_ACTION = "ResetTransformAction";
   public static final String ZOOM_IN_ACTION = "ZoomInAction";
   public static final String ZOOM_OUT_ACTION = "ZoomOutAction";
   public static final String PREVIOUS_TRANSFORM_ACTION = "PreviousTransformAction";
   public static final String NEXT_TRANSFORM_ACTION = "NextTransformAction";
   public static final String USE_STYLESHEET_ACTION = "UseStylesheetAction";
   public static final String PLAY_ACTION = "PlayAction";
   public static final String PAUSE_ACTION = "PauseAction";
   public static final String STOP_ACTION = "StopAction";
   public static final String MONITOR_ACTION = "MonitorAction";
   public static final String DOM_VIEWER_ACTION = "DOMViewerAction";
   public static final String SET_TRANSFORM_ACTION = "SetTransformAction";
   public static final String FIND_DIALOG_ACTION = "FindDialogAction";
   public static final String THUMBNAIL_DIALOG_ACTION = "ThumbnailDialogAction";
   public static final String FLUSH_ACTION = "FlushAction";
   public static final String TOGGLE_DEBUGGER_ACTION = "ToggleDebuggerAction";
   public static final Cursor WAIT_CURSOR;
   public static final Cursor DEFAULT_CURSOR;
   public static final String PROPERTY_OS_NAME;
   public static final String PROPERTY_OS_NAME_DEFAULT;
   public static final String PROPERTY_OS_WINDOWS_PREFIX;
   protected static final String OPEN_TITLE = "Open.title";
   protected static Vector handlers;
   protected static SquiggleInputHandler defaultHandler;
   protected static ResourceBundle bundle;
   protected static ResourceManager resources;
   protected Application application;
   protected Canvas svgCanvas;
   protected JPanel svgCanvasPanel;
   protected JWindow window;
   protected static JFrame memoryMonitorFrame;
   protected File currentPath = new File("");
   protected File currentSavePath = new File("");
   protected BackAction backAction = new BackAction();
   protected ForwardAction forwardAction = new ForwardAction();
   protected PlayAction playAction = new PlayAction();
   protected PauseAction pauseAction = new PauseAction();
   protected StopAction stopAction = new StopAction();
   protected PreviousTransformAction previousTransformAction = new PreviousTransformAction();
   protected NextTransformAction nextTransformAction = new NextTransformAction();
   protected UseStylesheetAction useStylesheetAction = new UseStylesheetAction();
   protected boolean debug;
   protected boolean autoAdjust = true;
   protected boolean managerStopped;
   protected SVGUserAgent userAgent = new UserAgent();
   protected SVGDocument svgDocument;
   protected URIChooser uriChooser;
   protected DOMViewer domViewer;
   protected FindDialog findDialog;
   protected ThumbnailDialog thumbnailDialog;
   protected JAffineTransformChooser.Dialog transformDialog;
   protected LocationBar locationBar;
   protected StatusBar statusBar;
   protected String title;
   protected LocalHistory localHistory;
   protected TransformHistory transformHistory = new TransformHistory();
   protected String alternateStyleSheet;
   protected Debugger debugger;
   protected Map listeners = new HashMap();
   long time;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Runnable;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$SquiggleInputHandler;

   public JSVGViewerFrame(Application var1) {
      this.application = var1;
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            JSVGViewerFrame.this.application.closeJSVGViewerFrame(JSVGViewerFrame.this);
         }
      });
      this.svgCanvas = new Canvas(this.userAgent, true, true) {
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

         {
            this.setMaximumSize(this.screenSize);
         }

         public Dimension getPreferredSize() {
            Dimension var1 = super.getPreferredSize();
            if (var1.width > this.screenSize.width) {
               var1.width = this.screenSize.width;
            }

            if (var1.height > this.screenSize.height) {
               var1.height = this.screenSize.height;
            }

            return var1;
         }

         public void setMySize(Dimension var1) {
            this.setPreferredSize(var1);
            this.invalidate();
            if (JSVGViewerFrame.this.autoAdjust) {
               Platform.unmaximize(JSVGViewerFrame.this);
               JSVGViewerFrame.this.pack();
            }

         }

         public void setDisableInteractions(boolean var1) {
            super.setDisableInteractions(var1);
            ((Action)JSVGViewerFrame.this.listeners.get("SetTransformAction")).setEnabled(!var1);
            if (JSVGViewerFrame.this.thumbnailDialog != null) {
               JSVGViewerFrame.this.thumbnailDialog.setInteractionEnabled(!var1);
            }

         }
      };
      javax.swing.ActionMap var2 = this.svgCanvas.getActionMap();
      var2.put("FullScreenAction", new FullScreenAction());
      InputMap var3 = this.svgCanvas.getInputMap(0);
      KeyStroke var4 = KeyStroke.getKeyStroke(122, 0);
      var3.put(var4, "FullScreenAction");
      this.svgCanvas.setDoubleBufferedRendering(true);
      this.listeners.put("AboutAction", new AboutAction());
      this.listeners.put("OpenAction", new OpenAction());
      this.listeners.put("OpenLocationAction", new OpenLocationAction());
      this.listeners.put("NewWindowAction", new NewWindowAction());
      this.listeners.put("ReloadAction", new ReloadAction());
      this.listeners.put("SaveAsAction", new SaveAsAction());
      this.listeners.put("BackAction", this.backAction);
      this.listeners.put("ForwardAction", this.forwardAction);
      this.listeners.put("PrintAction", new PrintAction());
      this.listeners.put("ExportAsJPGAction", new ExportAsJPGAction());
      this.listeners.put("ExportAsPNGAction", new ExportAsPNGAction());
      this.listeners.put("ExportAsTIFFAction", new ExportAsTIFFAction());
      this.listeners.put("PreferencesAction", new PreferencesAction());
      this.listeners.put("CloseAction", new CloseAction());
      this.listeners.put("ExitAction", this.application.createExitAction(this));
      this.listeners.put("ViewSourceAction", new ViewSourceAction());
      javax.swing.ActionMap var5 = this.svgCanvas.getActionMap();
      this.listeners.put("ResetTransformAction", var5.get("ResetTransform"));
      this.listeners.put("ZoomInAction", var5.get("ZoomIn"));
      this.listeners.put("ZoomOutAction", var5.get("ZoomOut"));
      this.listeners.put("PreviousTransformAction", this.previousTransformAction);
      var4 = KeyStroke.getKeyStroke(75, 2);
      var3.put(var4, this.previousTransformAction);
      this.listeners.put("NextTransformAction", this.nextTransformAction);
      var4 = KeyStroke.getKeyStroke(76, 2);
      var3.put(var4, this.nextTransformAction);
      this.listeners.put("UseStylesheetAction", this.useStylesheetAction);
      this.listeners.put("PlayAction", this.playAction);
      this.listeners.put("PauseAction", this.pauseAction);
      this.listeners.put("StopAction", this.stopAction);
      this.listeners.put("MonitorAction", new MonitorAction());
      this.listeners.put("DOMViewerAction", new DOMViewerAction());
      this.listeners.put("SetTransformAction", new SetTransformAction());
      this.listeners.put("FindDialogAction", new FindDialogAction());
      this.listeners.put("ThumbnailDialogAction", new ThumbnailDialogAction());
      this.listeners.put("FlushAction", new FlushAction());
      this.listeners.put("ToggleDebuggerAction", new ToggleDebuggerAction());
      JPanel var6 = null;

      try {
         MenuFactory var7 = new MenuFactory(bundle, this);
         JMenuBar var8 = var7.createJMenuBar("MenuBar", this.application.getUISpecialization());
         this.setJMenuBar(var8);
         this.localHistory = new LocalHistory(var8, this);
         String[] var9 = this.application.getVisitedURIs();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            if (var9[var10] != null && !"".equals(var9[var10])) {
               this.localHistory.update(var9[var10]);
            }
         }

         var6 = new JPanel(new BorderLayout());
         ToolBarFactory var13 = new ToolBarFactory(bundle, this);
         JToolBar var11 = var13.createJToolBar("ToolBar");
         var11.setFloatable(false);
         this.getContentPane().add(var6, "North");
         var6.add(var11, "North");
         var6.add(new JSeparator(), "Center");
         var6.add(this.locationBar = new LocationBar(), "South");
         this.locationBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      } catch (MissingResourceException var12) {
         System.out.println(var12.getMessage());
         System.exit(0);
      }

      this.svgCanvasPanel = new JPanel(new BorderLayout());
      this.svgCanvasPanel.setBorder(BorderFactory.createEtchedBorder());
      this.svgCanvasPanel.add(this.svgCanvas, "Center");
      var6 = new JPanel(new BorderLayout());
      var6.add(this.svgCanvasPanel, "Center");
      var6.add(this.statusBar = new StatusBar(), "South");
      this.getContentPane().add(var6, "Center");
      this.svgCanvas.addSVGDocumentLoaderListener(this);
      this.svgCanvas.addGVTTreeBuilderListener(this);
      this.svgCanvas.addSVGLoadEventDispatcherListener(this);
      this.svgCanvas.addGVTTreeRendererListener(this);
      this.svgCanvas.addLinkActivationListener(this);
      this.svgCanvas.addUpdateManagerListener(this);
      this.svgCanvas.addMouseMotionListener(new MouseMotionAdapter() {
         public void mouseMoved(MouseEvent var1) {
            if (JSVGViewerFrame.this.svgDocument == null) {
               JSVGViewerFrame.this.statusBar.setXPosition((float)var1.getX());
               JSVGViewerFrame.this.statusBar.setYPosition((float)var1.getY());
            } else {
               try {
                  AffineTransform var2 = JSVGViewerFrame.this.svgCanvas.getViewBoxTransform();
                  if (var2 != null) {
                     var2 = var2.createInverse();
                     Point2D var3 = var2.transform(new Point2D.Float((float)var1.getX(), (float)var1.getY()), (Point2D)null);
                     JSVGViewerFrame.this.statusBar.setXPosition((float)var3.getX());
                     JSVGViewerFrame.this.statusBar.setYPosition((float)var3.getY());
                     return;
                  }
               } catch (NoninvertibleTransformException var4) {
               }

               JSVGViewerFrame.this.statusBar.setXPosition((float)var1.getX());
               JSVGViewerFrame.this.statusBar.setYPosition((float)var1.getY());
            }

         }
      });
      this.svgCanvas.addMouseListener(new MouseAdapter() {
         public void mouseExited(MouseEvent var1) {
            Dimension var2 = JSVGViewerFrame.this.svgCanvas.getSize();
            if (JSVGViewerFrame.this.svgDocument == null) {
               JSVGViewerFrame.this.statusBar.setWidth((float)var2.width);
               JSVGViewerFrame.this.statusBar.setHeight((float)var2.height);
            } else {
               try {
                  AffineTransform var3 = JSVGViewerFrame.this.svgCanvas.getViewBoxTransform();
                  if (var3 != null) {
                     var3 = var3.createInverse();
                     Point2D var4 = var3.transform(new Point2D.Float(0.0F, 0.0F), (Point2D)null);
                     Point2D var5 = var3.transform(new Point2D.Float((float)var2.width, (float)var2.height), (Point2D)null);
                     JSVGViewerFrame.this.statusBar.setWidth((float)(var5.getX() - var4.getX()));
                     JSVGViewerFrame.this.statusBar.setHeight((float)(var5.getY() - var4.getY()));
                     return;
                  }
               } catch (NoninvertibleTransformException var6) {
               }

               JSVGViewerFrame.this.statusBar.setWidth((float)var2.width);
               JSVGViewerFrame.this.statusBar.setHeight((float)var2.height);
            }

         }
      });
      this.svgCanvas.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent var1) {
            Dimension var2 = JSVGViewerFrame.this.svgCanvas.getSize();
            if (JSVGViewerFrame.this.svgDocument == null) {
               JSVGViewerFrame.this.statusBar.setWidth((float)var2.width);
               JSVGViewerFrame.this.statusBar.setHeight((float)var2.height);
            } else {
               try {
                  AffineTransform var3 = JSVGViewerFrame.this.svgCanvas.getViewBoxTransform();
                  if (var3 != null) {
                     var3 = var3.createInverse();
                     Point2D var4 = var3.transform(new Point2D.Float(0.0F, 0.0F), (Point2D)null);
                     Point2D var5 = var3.transform(new Point2D.Float((float)var2.width, (float)var2.height), (Point2D)null);
                     JSVGViewerFrame.this.statusBar.setWidth((float)(var5.getX() - var4.getX()));
                     JSVGViewerFrame.this.statusBar.setHeight((float)(var5.getY() - var4.getY()));
                     return;
                  }
               } catch (NoninvertibleTransformException var6) {
               }

               JSVGViewerFrame.this.statusBar.setWidth((float)var2.width);
               JSVGViewerFrame.this.statusBar.setHeight((float)var2.height);
            }

         }
      });
      this.locationBar.addActionListener(new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            String var2 = JSVGViewerFrame.this.locationBar.getText().trim();
            int var3 = var2.indexOf(35);
            String var4 = "";
            if (var3 != -1) {
               var4 = var2.substring(var3 + 1);
               var2 = var2.substring(0, var3);
            }

            if (!var2.equals("")) {
               try {
                  File var5 = new File(var2);
                  if (var5.exists()) {
                     if (var5.isDirectory()) {
                        return;
                     }

                     try {
                        var2 = var5.getCanonicalPath();
                        if (var2.startsWith("/")) {
                           var2 = "file:" + var2;
                        } else {
                           var2 = "file:/" + var2;
                        }
                     } catch (IOException var8) {
                     }
                  }
               } catch (SecurityException var9) {
               }

               String var10 = JSVGViewerFrame.this.svgCanvas.getFragmentIdentifier();
               if (JSVGViewerFrame.this.svgDocument != null) {
                  ParsedURL var6 = new ParsedURL(JSVGViewerFrame.this.svgDocument.getURL());
                  ParsedURL var7 = new ParsedURL(var6, var2);
                  var10 = var10 == null ? "" : var10;
                  if (var6.equals(var7) && var4.equals(var10)) {
                     return;
                  }
               }

               if (var4.length() != 0) {
                  var2 = var2 + '#' + var4;
               }

               JSVGViewerFrame.this.locationBar.setText(var2);
               JSVGViewerFrame.this.locationBar.addToHistory(var2);
               JSVGViewerFrame.this.showSVGDocument(var2);
            }
         }
      });
   }

   public void dispose() {
      this.hideDebugger();
      this.svgCanvas.dispose();
      super.dispose();
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public void setAutoAdjust(boolean var1) {
      this.autoAdjust = var1;
   }

   public JSVGCanvas getJSVGCanvas() {
      return this.svgCanvas;
   }

   private static File makeAbsolute(File var0) {
      return !var0.isAbsolute() ? var0.getAbsoluteFile() : var0;
   }

   public void showDebugger() {
      if (this.debugger == null && JSVGViewerFrame.Debugger.isPresent) {
         this.debugger = new Debugger(this, this.locationBar.getText());
         this.debugger.initialize();
      }

   }

   public void hideDebugger() {
      if (this.debugger != null) {
         this.debugger.clearAllBreakpoints();
         this.debugger.go();
         this.debugger.dispose();
         this.debugger = null;
      }

   }

   public void showSVGDocument(String var1) {
      try {
         ParsedURL var2 = new ParsedURL(var1);
         SquiggleInputHandler var3 = this.getInputHandler(var2);
         var3.handle(var2, this);
      } catch (Exception var4) {
         if (this.userAgent != null) {
            this.userAgent.displayError(var4);
         }
      }

   }

   public SquiggleInputHandler getInputHandler(ParsedURL var1) throws IOException {
      Iterator var2 = getHandlers().iterator();
      SquiggleInputHandler var3 = null;

      while(var2.hasNext()) {
         SquiggleInputHandler var4 = (SquiggleInputHandler)var2.next();
         if (var4.accept(var1)) {
            var3 = var4;
            break;
         }
      }

      if (var3 == null) {
         var3 = defaultHandler;
      }

      return var3;
   }

   protected static Vector getHandlers() {
      if (handlers != null) {
         return handlers;
      } else {
         handlers = new Vector();
         registerHandler(new SVGInputHandler());
         Iterator var0 = Service.providers(class$org$apache$batik$apps$svgbrowser$SquiggleInputHandler == null ? (class$org$apache$batik$apps$svgbrowser$SquiggleInputHandler = class$("org.apache.batik.apps.svgbrowser.SquiggleInputHandler")) : class$org$apache$batik$apps$svgbrowser$SquiggleInputHandler);

         while(var0.hasNext()) {
            SquiggleInputHandler var1 = (SquiggleInputHandler)var0.next();
            registerHandler(var1);
         }

         return handlers;
      }
   }

   public static synchronized void registerHandler(SquiggleInputHandler var0) {
      Vector var1 = getHandlers();
      var1.addElement(var0);
   }

   public Action getAction(String var1) throws MissingListenerException {
      Action var2 = (Action)this.listeners.get(var1);
      if (var2 == null) {
         throw new MissingListenerException("Can't find action.", "org.apache.batik.apps.svgbrowser.resources.GUI", var1);
      } else {
         return var2;
      }
   }

   public void documentLoadingStarted(SVGDocumentLoaderEvent var1) {
      String var2 = resources.getString("Message.documentLoad");
      if (this.debug) {
         System.out.println(var2);
         this.time = System.currentTimeMillis();
      }

      this.statusBar.setMainMessage(var2);
      this.stopAction.update(true);
      this.svgCanvas.setCursor(WAIT_CURSOR);
   }

   public void documentLoadingCompleted(SVGDocumentLoaderEvent var1) {
      if (this.debug) {
         System.out.print(resources.getString("Message.documentLoadTime"));
         System.out.println(System.currentTimeMillis() - this.time + " ms");
      }

      this.setSVGDocument(var1.getSVGDocument(), var1.getSVGDocument().getURL(), var1.getSVGDocument().getTitle());
   }

   public void setSVGDocument(SVGDocument var1, String var2, String var3) {
      this.svgDocument = var1;
      if (this.domViewer != null) {
         if (this.domViewer.isVisible() && var1 != null) {
            this.domViewer.setDocument(var1, (ViewCSS)var1.getDocumentElement());
         } else {
            this.domViewer.dispose();
            this.domViewer = null;
         }
      }

      this.stopAction.update(false);
      this.svgCanvas.setCursor(DEFAULT_CURSOR);
      this.locationBar.setText(var2);
      if (this.debugger != null) {
         this.debugger.detach();
         this.debugger.setDocumentURL(var2);
      }

      if (this.title == null) {
         this.title = this.getTitle();
      }

      if (var3.length() != 0) {
         this.setTitle(this.title + ": " + var3);
      } else {
         int var6 = var2.lastIndexOf("/");
         if (var6 == -1) {
            var6 = var2.lastIndexOf("\\");
         }

         if (var6 == -1) {
            this.setTitle(this.title + ": " + var2);
         } else {
            this.setTitle(this.title + ": " + var2.substring(var6 + 1));
         }
      }

      this.localHistory.update(var2);
      this.application.addVisitedURI(var2);
      this.backAction.update();
      this.forwardAction.update();
      this.transformHistory = new TransformHistory();
      this.previousTransformAction.update();
      this.nextTransformAction.update();
      this.useStylesheetAction.update();
   }

   public void documentLoadingCancelled(SVGDocumentLoaderEvent var1) {
      String var2 = resources.getString("Message.documentCancelled");
      if (this.debug) {
         System.out.println(var2);
      }

      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
      this.stopAction.update(false);
      this.svgCanvas.setCursor(DEFAULT_CURSOR);
   }

   public void documentLoadingFailed(SVGDocumentLoaderEvent var1) {
      String var2 = resources.getString("Message.documentFailed");
      if (this.debug) {
         System.out.println(var2);
      }

      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
      this.stopAction.update(false);
      this.svgCanvas.setCursor(DEFAULT_CURSOR);
   }

   public void gvtBuildStarted(GVTTreeBuilderEvent var1) {
      String var2 = resources.getString("Message.treeBuild");
      if (this.debug) {
         System.out.println(var2);
         this.time = System.currentTimeMillis();
      }

      this.statusBar.setMainMessage(var2);
      this.stopAction.update(true);
      this.svgCanvas.setCursor(WAIT_CURSOR);
   }

   public void gvtBuildCompleted(GVTTreeBuilderEvent var1) {
      if (this.debug) {
         System.out.print(resources.getString("Message.treeBuildTime"));
         System.out.println(System.currentTimeMillis() - this.time + " ms");
      }

      if (this.findDialog != null) {
         if (this.findDialog.isVisible()) {
            this.findDialog.setGraphicsNode(this.svgCanvas.getGraphicsNode());
         } else {
            this.findDialog.dispose();
            this.findDialog = null;
         }
      }

      this.stopAction.update(false);
      this.svgCanvas.setCursor(DEFAULT_CURSOR);
      this.svgCanvas.setSelectionOverlayXORMode(this.application.isSelectionOverlayXORMode());
      this.svgCanvas.requestFocus();
      if (this.debugger != null) {
         this.debugger.attach();
      }

   }

   public void gvtBuildCancelled(GVTTreeBuilderEvent var1) {
      String var2 = resources.getString("Message.treeCancelled");
      if (this.debug) {
         System.out.println(var2);
      }

      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
      this.stopAction.update(false);
      this.svgCanvas.setCursor(DEFAULT_CURSOR);
      this.svgCanvas.setSelectionOverlayXORMode(this.application.isSelectionOverlayXORMode());
   }

   public void gvtBuildFailed(GVTTreeBuilderEvent var1) {
      String var2 = resources.getString("Message.treeFailed");
      if (this.debug) {
         System.out.println(var2);
      }

      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
      this.stopAction.update(false);
      this.svgCanvas.setCursor(DEFAULT_CURSOR);
      this.svgCanvas.setSelectionOverlayXORMode(this.application.isSelectionOverlayXORMode());
      if (this.autoAdjust) {
         this.pack();
      }

   }

   public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent var1) {
      String var2 = resources.getString("Message.onload");
      if (this.debug) {
         System.out.println(var2);
         this.time = System.currentTimeMillis();
      }

      this.stopAction.update(true);
      this.statusBar.setMainMessage(var2);
   }

   public void svgLoadEventDispatchCompleted(SVGLoadEventDispatcherEvent var1) {
      if (this.debug) {
         System.out.print(resources.getString("Message.onloadTime"));
         System.out.println(System.currentTimeMillis() - this.time + " ms");
      }

      this.stopAction.update(false);
      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(resources.getString("Message.done"));
   }

   public void svgLoadEventDispatchCancelled(SVGLoadEventDispatcherEvent var1) {
      String var2 = resources.getString("Message.onloadCancelled");
      if (this.debug) {
         System.out.println(var2);
      }

      this.stopAction.update(false);
      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
   }

   public void svgLoadEventDispatchFailed(SVGLoadEventDispatcherEvent var1) {
      String var2 = resources.getString("Message.onloadFailed");
      if (this.debug) {
         System.out.println(var2);
      }

      this.stopAction.update(false);
      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
   }

   public void gvtRenderingPrepare(GVTTreeRendererEvent var1) {
      if (this.debug) {
         String var2 = resources.getString("Message.treeRenderingPrep");
         System.out.println(var2);
         this.time = System.currentTimeMillis();
      }

      this.stopAction.update(true);
      this.svgCanvas.setCursor(WAIT_CURSOR);
      this.statusBar.setMainMessage(resources.getString("Message.treeRendering"));
   }

   public void gvtRenderingStarted(GVTTreeRendererEvent var1) {
      if (this.debug) {
         String var2 = resources.getString("Message.treeRenderingPrepTime");
         System.out.print(var2);
         System.out.println(System.currentTimeMillis() - this.time + " ms");
         this.time = System.currentTimeMillis();
         var2 = resources.getString("Message.treeRenderingStart");
         System.out.println(var2);
      }

   }

   public void gvtRenderingCompleted(GVTTreeRendererEvent var1) {
      if (this.debug) {
         String var2 = resources.getString("Message.treeRenderingTime");
         System.out.print(var2);
         System.out.println(System.currentTimeMillis() - this.time + " ms");
      }

      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(resources.getString("Message.done"));
      if (!this.svgCanvas.isDynamic() || this.managerStopped) {
         this.stopAction.update(false);
      }

      this.svgCanvas.setCursor(DEFAULT_CURSOR);
      this.transformHistory.update(this.svgCanvas.getRenderingTransform());
      this.previousTransformAction.update();
      this.nextTransformAction.update();
   }

   public void gvtRenderingCancelled(GVTTreeRendererEvent var1) {
      String var2 = resources.getString("Message.treeRenderingCancelled");
      if (this.debug) {
         System.out.println(var2);
      }

      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
      if (!this.svgCanvas.isDynamic()) {
         this.stopAction.update(false);
      }

      this.svgCanvas.setCursor(DEFAULT_CURSOR);
   }

   public void gvtRenderingFailed(GVTTreeRendererEvent var1) {
      String var2 = resources.getString("Message.treeRenderingFailed");
      if (this.debug) {
         System.out.println(var2);
      }

      this.statusBar.setMainMessage("");
      this.statusBar.setMessage(var2);
      if (!this.svgCanvas.isDynamic()) {
         this.stopAction.update(false);
      }

      this.svgCanvas.setCursor(DEFAULT_CURSOR);
   }

   public void linkActivated(LinkActivationEvent var1) {
      String var2 = var1.getReferencedURI();
      if (this.svgDocument != null) {
         ParsedURL var3 = new ParsedURL(this.svgDocument.getURL());
         ParsedURL var4 = new ParsedURL(var3, var2);
         if (!var4.sameFile(var3)) {
            return;
         }

         if (var2.indexOf(35) != -1) {
            this.localHistory.update(var2);
            this.locationBar.setText(var2);
            if (this.debugger != null) {
               this.debugger.detach();
               this.debugger.setDocumentURL(var2);
            }

            this.application.addVisitedURI(var2);
            this.backAction.update();
            this.forwardAction.update();
            this.transformHistory = new TransformHistory();
            this.previousTransformAction.update();
            this.nextTransformAction.update();
         }
      }

   }

   public void managerStarted(UpdateManagerEvent var1) {
      if (this.debug) {
         String var2 = resources.getString("Message.updateManagerStarted");
         System.out.println(var2);
      }

      this.managerStopped = false;
      this.playAction.update(false);
      this.pauseAction.update(true);
      this.stopAction.update(true);
   }

   public void managerSuspended(UpdateManagerEvent var1) {
      if (this.debug) {
         String var2 = resources.getString("Message.updateManagerSuspended");
         System.out.println(var2);
      }

      this.playAction.update(true);
      this.pauseAction.update(false);
   }

   public void managerResumed(UpdateManagerEvent var1) {
      if (this.debug) {
         String var2 = resources.getString("Message.updateManagerResumed");
         System.out.println(var2);
      }

      this.playAction.update(false);
      this.pauseAction.update(true);
   }

   public void managerStopped(UpdateManagerEvent var1) {
      if (this.debug) {
         String var2 = resources.getString("Message.updateManagerStopped");
         System.out.println(var2);
      }

      this.managerStopped = true;
      this.playAction.update(false);
      this.pauseAction.update(false);
      this.stopAction.update(false);
   }

   public void updateStarted(UpdateManagerEvent var1) {
   }

   public void updateCompleted(UpdateManagerEvent var1) {
   }

   public void updateFailed(UpdateManagerEvent var1) {
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
      try {
         EOL = System.getProperty("line.separator", "\n");
      } catch (SecurityException var2) {
         EOL = "\n";
      }

      priorJDK1_4 = true;

      try {
         Class.forName("java.util.logging.LoggingPermission");
         priorJDK1_4 = false;
      } catch (ClassNotFoundException var1) {
      }

      WAIT_CURSOR = new Cursor(3);
      DEFAULT_CURSOR = new Cursor(0);
      PROPERTY_OS_NAME = Resources.getString("JSVGViewerFrame.property.os.name");
      PROPERTY_OS_NAME_DEFAULT = Resources.getString("JSVGViewerFrame.property.os.name.default");
      PROPERTY_OS_WINDOWS_PREFIX = Resources.getString("JSVGViewerFrame.property.os.windows.prefix");
      defaultHandler = new SVGInputHandler();
      bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.GUI", Locale.getDefault());
      resources = new ResourceManager(bundle);
   }

   protected static class ImageFileFilter extends FileFilter {
      protected String extension;

      public ImageFileFilter(String var1) {
         this.extension = var1;
      }

      public boolean accept(File var1) {
         boolean var2 = false;
         String var3 = null;
         if (var1 != null) {
            if (var1.isDirectory()) {
               var2 = true;
            } else {
               var3 = var1.getPath().toLowerCase();
               if (var3.endsWith(this.extension)) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public String getDescription() {
         return this.extension;
      }
   }

   protected class UserAgent implements SVGUserAgent {
      public void displayError(String var1) {
         if (JSVGViewerFrame.this.debug) {
            System.err.println(var1);
         }

         JOptionPane var2 = new JOptionPane(var1, 0);
         JDialog var3 = var2.createDialog(JSVGViewerFrame.this, "ERROR");
         var3.setModal(false);
         var3.setVisible(true);
      }

      public void displayError(Exception var1) {
         if (JSVGViewerFrame.this.debug) {
            var1.printStackTrace();
         }

         JErrorPane var2 = new JErrorPane(var1, 0);
         JDialog var3 = var2.createDialog(JSVGViewerFrame.this, "ERROR");
         var3.setModal(false);
         var3.setVisible(true);
      }

      public void displayMessage(String var1) {
         JSVGViewerFrame.this.statusBar.setMessage(var1);
      }

      public void showAlert(String var1) {
         JSVGViewerFrame.this.svgCanvas.showAlert(var1);
      }

      public String showPrompt(String var1) {
         return JSVGViewerFrame.this.svgCanvas.showPrompt(var1);
      }

      public String showPrompt(String var1, String var2) {
         return JSVGViewerFrame.this.svgCanvas.showPrompt(var1, var2);
      }

      public boolean showConfirm(String var1) {
         return JSVGViewerFrame.this.svgCanvas.showConfirm(var1);
      }

      public float getPixelUnitToMillimeter() {
         return 0.26458332F;
      }

      public float getPixelToMM() {
         return this.getPixelUnitToMillimeter();
      }

      public String getDefaultFontFamily() {
         return JSVGViewerFrame.this.application.getDefaultFontFamily();
      }

      public float getMediumFontSize() {
         return 228.59999F / (72.0F * this.getPixelUnitToMillimeter());
      }

      public float getLighterFontWeight(float var1) {
         int var2 = (int)((var1 + 50.0F) / 100.0F) * 100;
         switch (var2) {
            case 100:
               return 100.0F;
            case 200:
               return 100.0F;
            case 300:
               return 200.0F;
            case 400:
               return 300.0F;
            case 500:
               return 400.0F;
            case 600:
               return 400.0F;
            case 700:
               return 400.0F;
            case 800:
               return 400.0F;
            case 900:
               return 400.0F;
            default:
               throw new IllegalArgumentException("Bad Font Weight: " + var1);
         }
      }

      public float getBolderFontWeight(float var1) {
         int var2 = (int)((var1 + 50.0F) / 100.0F) * 100;
         switch (var2) {
            case 100:
               return 600.0F;
            case 200:
               return 600.0F;
            case 300:
               return 600.0F;
            case 400:
               return 600.0F;
            case 500:
               return 600.0F;
            case 600:
               return 700.0F;
            case 700:
               return 800.0F;
            case 800:
               return 900.0F;
            case 900:
               return 900.0F;
            default:
               throw new IllegalArgumentException("Bad Font Weight: " + var1);
         }
      }

      public String getLanguages() {
         return JSVGViewerFrame.this.application.getLanguages();
      }

      public String getUserStyleSheetURI() {
         return JSVGViewerFrame.this.application.getUserStyleSheetURI();
      }

      public String getXMLParserClassName() {
         return JSVGViewerFrame.this.application.getXMLParserClassName();
      }

      public boolean isXMLParserValidating() {
         return JSVGViewerFrame.this.application.isXMLParserValidating();
      }

      public String getMedia() {
         return JSVGViewerFrame.this.application.getMedia();
      }

      public String getAlternateStyleSheet() {
         return JSVGViewerFrame.this.alternateStyleSheet;
      }

      public void openLink(String var1, boolean var2) {
         if (var2) {
            JSVGViewerFrame.this.application.openLink(var1);
         } else {
            JSVGViewerFrame.this.showSVGDocument(var1);
         }

      }

      public boolean supportExtension(String var1) {
         return false;
      }

      public void handleElement(Element var1, Object var2) {
      }

      public ScriptSecurity getScriptSecurity(String var1, ParsedURL var2, ParsedURL var3) {
         if (!JSVGViewerFrame.this.application.canLoadScriptType(var1)) {
            return new NoLoadScriptSecurity(var1);
         } else {
            switch (JSVGViewerFrame.this.application.getAllowedScriptOrigin()) {
               case 1:
                  return new RelaxedScriptSecurity(var1, var2, var3);
               case 2:
                  return new DefaultScriptSecurity(var1, var2, var3);
               case 3:
               default:
                  return new NoLoadScriptSecurity(var1);
               case 4:
                  return new EmbededScriptSecurity(var1, var2, var3);
            }
         }
      }

      public void checkLoadScript(String var1, ParsedURL var2, ParsedURL var3) throws SecurityException {
         ScriptSecurity var4 = this.getScriptSecurity(var1, var2, var3);
         if (var4 != null) {
            var4.checkLoadScript();
         }

      }

      public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL var1, ParsedURL var2) {
         switch (JSVGViewerFrame.this.application.getAllowedExternalResourceOrigin()) {
            case 1:
               return new RelaxedExternalResourceSecurity(var1, var2);
            case 2:
               return new DefaultExternalResourceSecurity(var1, var2);
            case 3:
            default:
               return new NoLoadExternalResourceSecurity();
            case 4:
               return new EmbededExternalResourceSecurity(var1);
         }
      }

      public void checkLoadExternalResource(ParsedURL var1, ParsedURL var2) throws SecurityException {
         ExternalResourceSecurity var3 = this.getExternalResourceSecurity(var1, var2);
         if (var3 != null) {
            var3.checkLoadExternalResource();
         }

      }
   }

   public class DOMViewerAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         this.openDOMViewer();
      }

      public void openDOMViewer() {
         if (JSVGViewerFrame.this.domViewer == null || JSVGViewerFrame.this.domViewer.isDisplayable()) {
            JSVGViewerFrame.this.domViewer = new DOMViewer(JSVGViewerFrame.this.svgCanvas.new JSVGViewerDOMViewerController());
            Rectangle var1 = JSVGViewerFrame.this.getBounds();
            Dimension var2 = JSVGViewerFrame.this.domViewer.getSize();
            JSVGViewerFrame.this.domViewer.setLocation(var1.x + (var1.width - var2.width) / 2, var1.y + (var1.height - var2.height) / 2);
         }

         JSVGViewerFrame.this.domViewer.setVisible(true);
      }

      public DOMViewer getDOMViewer() {
         return JSVGViewerFrame.this.domViewer;
      }
   }

   public class FullScreenAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.window != null && JSVGViewerFrame.this.window.isVisible()) {
            JSVGViewerFrame.this.svgCanvas.getParent().remove(JSVGViewerFrame.this.svgCanvas);
            JSVGViewerFrame.this.svgCanvasPanel.add(JSVGViewerFrame.this.svgCanvas, "Center");
            JSVGViewerFrame.this.window.setVisible(false);
         } else {
            if (JSVGViewerFrame.this.window == null) {
               JSVGViewerFrame.this.window = new JWindow(JSVGViewerFrame.this);
               Dimension var2 = Toolkit.getDefaultToolkit().getScreenSize();
               JSVGViewerFrame.this.window.setSize(var2);
            }

            JSVGViewerFrame.this.svgCanvas.getParent().remove(JSVGViewerFrame.this.svgCanvas);
            JSVGViewerFrame.this.window.getContentPane().add(JSVGViewerFrame.this.svgCanvas);
            JSVGViewerFrame.this.window.setVisible(true);
            JSVGViewerFrame.this.window.toFront();
            JSVGViewerFrame.this.svgCanvas.requestFocus();
         }

      }
   }

   public class ThumbnailDialogAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.thumbnailDialog == null) {
            JSVGViewerFrame.this.thumbnailDialog = new ThumbnailDialog(JSVGViewerFrame.this, JSVGViewerFrame.this.svgCanvas);
            JSVGViewerFrame.this.thumbnailDialog.pack();
            Rectangle var2 = JSVGViewerFrame.this.getBounds();
            Dimension var3 = JSVGViewerFrame.this.thumbnailDialog.getSize();
            JSVGViewerFrame.this.thumbnailDialog.setLocation(var2.x + (var2.width - var3.width) / 2, var2.y + (var2.height - var3.height) / 2);
         }

         JSVGViewerFrame.this.thumbnailDialog.setInteractionEnabled(!JSVGViewerFrame.this.svgCanvas.getDisableInteractions());
         JSVGViewerFrame.this.thumbnailDialog.setVisible(true);
      }
   }

   public class FindDialogAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.findDialog == null) {
            JSVGViewerFrame.this.findDialog = new FindDialog(JSVGViewerFrame.this, JSVGViewerFrame.this.svgCanvas);
            JSVGViewerFrame.this.findDialog.setGraphicsNode(JSVGViewerFrame.this.svgCanvas.getGraphicsNode());
            JSVGViewerFrame.this.findDialog.pack();
            Rectangle var2 = JSVGViewerFrame.this.getBounds();
            Dimension var3 = JSVGViewerFrame.this.findDialog.getSize();
            JSVGViewerFrame.this.findDialog.setLocation(var2.x + (var2.width - var3.width) / 2, var2.y + (var2.height - var3.height) / 2);
         }

         JSVGViewerFrame.this.findDialog.setVisible(true);
      }
   }

   public class MonitorAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.memoryMonitorFrame == null) {
            JSVGViewerFrame.memoryMonitorFrame = new MemoryMonitor();
            Rectangle var2 = JSVGViewerFrame.this.getBounds();
            Dimension var3 = JSVGViewerFrame.memoryMonitorFrame.getSize();
            JSVGViewerFrame.memoryMonitorFrame.setLocation(var2.x + (var2.width - var3.width) / 2, var2.y + (var2.height - var3.height) / 2);
         }

         JSVGViewerFrame.memoryMonitorFrame.setVisible(true);
      }
   }

   public class SetTransformAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.transformDialog == null) {
            JSVGViewerFrame.this.transformDialog = JAffineTransformChooser.createDialog(JSVGViewerFrame.this, JSVGViewerFrame.resources.getString("SetTransform.title"));
         }

         AffineTransform var2 = JSVGViewerFrame.this.transformDialog.showDialog();
         if (var2 != null) {
            AffineTransform var3 = JSVGViewerFrame.this.svgCanvas.getRenderingTransform();
            if (var3 == null) {
               var3 = new AffineTransform();
            }

            var2.concatenate(var3);
            JSVGViewerFrame.this.svgCanvas.setRenderingTransform(var2);
         }

      }
   }

   public class StopAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
         JSVGViewerFrame.this.svgCanvas.stopProcessing();
      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      public void update(boolean var1) {
         Iterator var2 = this.components.iterator();

         while(var2.hasNext()) {
            ((JComponent)var2.next()).setEnabled(var1);
         }

      }
   }

   public class PauseAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
         JSVGViewerFrame.this.svgCanvas.suspendProcessing();
      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      public void update(boolean var1) {
         Iterator var2 = this.components.iterator();

         while(var2.hasNext()) {
            ((JComponent)var2.next()).setEnabled(var1);
         }

      }
   }

   public class PlayAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
         JSVGViewerFrame.this.svgCanvas.resumeProcessing();
      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      public void update(boolean var1) {
         Iterator var2 = this.components.iterator();

         while(var2.hasNext()) {
            ((JComponent)var2.next()).setEnabled(var1);
         }

      }
   }

   public class UseStylesheetAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      protected void update() {
         JSVGViewerFrame.this.alternateStyleSheet = null;
         Iterator var1 = this.components.iterator();
         SVGDocument var2 = JSVGViewerFrame.this.svgCanvas.getSVGDocument();

         while(var1.hasNext()) {
            JComponent var3 = (JComponent)var1.next();
            var3.removeAll();
            var3.setEnabled(false);
            ButtonGroup var4 = new ButtonGroup();

            for(Node var5 = var2.getFirstChild(); var5 != null && var5.getNodeType() != 1; var5 = var5.getNextSibling()) {
               if (var5 instanceof StyleSheetProcessingInstruction) {
                  StyleSheetProcessingInstruction var6 = (StyleSheetProcessingInstruction)var5;
                  HashTable var7 = var6.getPseudoAttributes();
                  final String var8 = (String)var7.get("title");
                  String var9 = (String)var7.get("alternate");
                  if (var8 != null && "yes".equals(var9)) {
                     JRadioButtonMenuItem var10 = new JRadioButtonMenuItem(var8);
                     var10.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent var1) {
                           SVGOMDocument var2 = (SVGOMDocument)JSVGViewerFrame.this.svgCanvas.getSVGDocument();
                           var2.clearViewCSS();
                           JSVGViewerFrame.this.alternateStyleSheet = var8;
                           JSVGViewerFrame.this.svgCanvas.setSVGDocument(var2);
                        }
                     });
                     var4.add(var10);
                     var3.add(var10);
                     var3.setEnabled(true);
                  }
               }
            }
         }

      }
   }

   public class NextTransformAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.transformHistory.canGoForward()) {
            JSVGViewerFrame.this.transformHistory.forward();
            this.update();
            JSVGViewerFrame.this.previousTransformAction.update();
            JSVGViewerFrame.this.svgCanvas.setRenderingTransform(JSVGViewerFrame.this.transformHistory.currentTransform());
         }

      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      protected void update() {
         boolean var1 = JSVGViewerFrame.this.transformHistory.canGoForward();
         Iterator var2 = this.components.iterator();

         while(var2.hasNext()) {
            ((JComponent)var2.next()).setEnabled(var1);
         }

      }
   }

   public class PreviousTransformAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.transformHistory.canGoBack()) {
            JSVGViewerFrame.this.transformHistory.back();
            this.update();
            JSVGViewerFrame.this.nextTransformAction.update();
            JSVGViewerFrame.this.svgCanvas.setRenderingTransform(JSVGViewerFrame.this.transformHistory.currentTransform());
         }

      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      protected void update() {
         boolean var1 = JSVGViewerFrame.this.transformHistory.canGoBack();
         Iterator var2 = this.components.iterator();

         while(var2.hasNext()) {
            ((JComponent)var2.next()).setEnabled(var1);
         }

      }
   }

   public class ToggleDebuggerAction extends AbstractAction {
      public ToggleDebuggerAction() {
         super("Toggle Debugger Action");
      }

      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.debugger == null) {
            JSVGViewerFrame.this.showDebugger();
         } else {
            JSVGViewerFrame.this.hideDebugger();
         }

      }
   }

   public class FlushAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JSVGViewerFrame.this.svgCanvas.flush();
         JSVGViewerFrame.this.svgCanvas.setRenderingTransform(JSVGViewerFrame.this.svgCanvas.getRenderingTransform());
      }
   }

   public class ViewSourceAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.svgDocument != null) {
            final ParsedURL var2 = new ParsedURL(JSVGViewerFrame.this.svgDocument.getURL());
            final JFrame var3 = new JFrame(var2.toString());
            var3.setSize(JSVGViewerFrame.resources.getInteger("ViewSource.width"), JSVGViewerFrame.resources.getInteger("ViewSource.height"));
            final XMLTextEditor var4 = new XMLTextEditor();
            var4.setFont(new Font("monospaced", 0, 12));
            JScrollPane var5 = new JScrollPane();
            var5.getViewport().add(var4);
            var5.setVerticalScrollBarPolicy(22);
            var3.getContentPane().add(var5, "Center");
            (new Thread() {
               public void run() {
                  char[] var1 = new char[4096];

                  try {
                     XMLDocument var2x = new XMLDocument();
                     ParsedURL var3x = new ParsedURL(JSVGViewerFrame.this.svgDocument.getURL());
                     InputStream var4x = var2.openStream(JSVGViewerFrame.this.getInputHandler(var3x).getHandledMimeTypes());
                     Reader var5 = XMLUtilities.createXMLDocumentReader(var4x);

                     int var6;
                     while((var6 = var5.read(var1, 0, var1.length)) != -1) {
                        var2x.insertString(var2x.getLength(), new String(var1, 0, var6), (AttributeSet)null);
                     }

                     var4.setDocument(var2x);
                     var4.setEditable(false);
                     var3.setVisible(true);
                  } catch (Exception var7) {
                     JSVGViewerFrame.this.userAgent.displayError(var7);
                  }

               }
            }).start();
         }
      }
   }

   public class ExportAsTIFFAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JFileChooser var2 = new JFileChooser(JSVGViewerFrame.makeAbsolute(JSVGViewerFrame.this.currentSavePath));
         var2.setDialogTitle(JSVGViewerFrame.resources.getString("ExportAsTIFF.title"));
         var2.setFileHidingEnabled(false);
         var2.setFileSelectionMode(0);
         var2.addChoosableFileFilter(new ImageFileFilter(".tiff"));
         int var3 = var2.showSaveDialog(JSVGViewerFrame.this);
         if (var3 == 0) {
            final File var4 = var2.getSelectedFile();
            BufferedImage var5 = JSVGViewerFrame.this.svgCanvas.getOffScreen();
            if (var5 != null) {
               JSVGViewerFrame.this.statusBar.setMessage(JSVGViewerFrame.resources.getString("Message.exportAsTIFF"));
               int var6 = var5.getWidth();
               int var7 = var5.getHeight();
               final TIFFTranscoder var8 = new TIFFTranscoder();
               if (JSVGViewerFrame.this.application.getXMLParserClassName() != null) {
                  var8.addTranscodingHint(JPEGTranscoder.KEY_XML_PARSER_CLASSNAME, JSVGViewerFrame.this.application.getXMLParserClassName());
               }

               final BufferedImage var9 = var8.createImage(var6, var7);
               Graphics2D var10 = var9.createGraphics();
               var10.drawImage(var5, (BufferedImageOp)null, 0, 0);
               (new Thread() {
                  public void run() {
                     try {
                        JSVGViewerFrame.this.currentSavePath = var4;
                        BufferedOutputStream var1 = new BufferedOutputStream(new FileOutputStream(var4));
                        var8.writeImage(var9, new TranscoderOutput(var1));
                        var1.close();
                     } catch (Exception var2) {
                     }

                     JSVGViewerFrame.this.statusBar.setMessage(JSVGViewerFrame.resources.getString("Message.done"));
                  }
               }).start();
            }
         }

      }
   }

   public class ExportAsPNGAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JFileChooser var2 = new JFileChooser(JSVGViewerFrame.makeAbsolute(JSVGViewerFrame.this.currentSavePath));
         var2.setDialogTitle(JSVGViewerFrame.resources.getString("ExportAsPNG.title"));
         var2.setFileHidingEnabled(false);
         var2.setFileSelectionMode(0);
         var2.addChoosableFileFilter(new ImageFileFilter(".png"));
         int var3 = var2.showSaveDialog(JSVGViewerFrame.this);
         if (var3 == 0) {
            boolean var4 = PNGOptionPanel.showDialog(JSVGViewerFrame.this);
            final File var5 = var2.getSelectedFile();
            BufferedImage var6 = JSVGViewerFrame.this.svgCanvas.getOffScreen();
            if (var6 != null) {
               JSVGViewerFrame.this.statusBar.setMessage(JSVGViewerFrame.resources.getString("Message.exportAsPNG"));
               int var7 = var6.getWidth();
               int var8 = var6.getHeight();
               final PNGTranscoder var9 = new PNGTranscoder();
               if (JSVGViewerFrame.this.application.getXMLParserClassName() != null) {
                  var9.addTranscodingHint(JPEGTranscoder.KEY_XML_PARSER_CLASSNAME, JSVGViewerFrame.this.application.getXMLParserClassName());
               }

               var9.addTranscodingHint(PNGTranscoder.KEY_FORCE_TRANSPARENT_WHITE, Boolean.TRUE);
               if (var4) {
                  var9.addTranscodingHint(PNGTranscoder.KEY_INDEXED, new Integer(256));
               }

               final BufferedImage var10 = var9.createImage(var7, var8);
               Graphics2D var11 = var10.createGraphics();
               var11.drawImage(var6, (BufferedImageOp)null, 0, 0);
               (new Thread() {
                  public void run() {
                     try {
                        JSVGViewerFrame.this.currentSavePath = var5;
                        BufferedOutputStream var1 = new BufferedOutputStream(new FileOutputStream(var5));
                        var9.writeImage(var10, new TranscoderOutput(var1));
                        var1.close();
                     } catch (Exception var2) {
                     }

                     JSVGViewerFrame.this.statusBar.setMessage(JSVGViewerFrame.resources.getString("Message.done"));
                  }
               }).start();
            }
         }

      }
   }

   public class ExportAsJPGAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JFileChooser var2 = new JFileChooser(JSVGViewerFrame.makeAbsolute(JSVGViewerFrame.this.currentSavePath));
         var2.setDialogTitle(JSVGViewerFrame.resources.getString("ExportAsJPG.title"));
         var2.setFileHidingEnabled(false);
         var2.setFileSelectionMode(0);
         var2.addChoosableFileFilter(new ImageFileFilter(".jpg"));
         int var3 = var2.showSaveDialog(JSVGViewerFrame.this);
         if (var3 == 0) {
            float var4 = JPEGOptionPanel.showDialog(JSVGViewerFrame.this);
            final File var5 = var2.getSelectedFile();
            BufferedImage var6 = JSVGViewerFrame.this.svgCanvas.getOffScreen();
            if (var6 != null) {
               JSVGViewerFrame.this.statusBar.setMessage(JSVGViewerFrame.resources.getString("Message.exportAsJPG"));
               int var7 = var6.getWidth();
               int var8 = var6.getHeight();
               final JPEGTranscoder var9 = new JPEGTranscoder();
               if (JSVGViewerFrame.this.application.getXMLParserClassName() != null) {
                  var9.addTranscodingHint(JPEGTranscoder.KEY_XML_PARSER_CLASSNAME, JSVGViewerFrame.this.application.getXMLParserClassName());
               }

               var9.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(var4));
               final BufferedImage var10 = var9.createImage(var7, var8);
               Graphics2D var11 = var10.createGraphics();
               var11.setColor(Color.white);
               var11.fillRect(0, 0, var7, var8);
               var11.drawImage(var6, (BufferedImageOp)null, 0, 0);
               (new Thread() {
                  public void run() {
                     try {
                        JSVGViewerFrame.this.currentSavePath = var5;
                        BufferedOutputStream var1 = new BufferedOutputStream(new FileOutputStream(var5));
                        var9.writeImage(var10, new TranscoderOutput(var1));
                        var1.close();
                     } catch (Exception var2) {
                     }

                     JSVGViewerFrame.this.statusBar.setMessage(JSVGViewerFrame.resources.getString("Message.done"));
                  }
               }).start();
            }
         }

      }
   }

   public class SaveAsAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JFileChooser var2 = new JFileChooser(JSVGViewerFrame.makeAbsolute(JSVGViewerFrame.this.currentSavePath));
         var2.setDialogTitle(JSVGViewerFrame.resources.getString("SaveAs.title"));
         var2.setFileHidingEnabled(false);
         var2.setFileSelectionMode(0);
         var2.addChoosableFileFilter(new ImageFileFilter(".svg"));
         int var3 = var2.showSaveDialog(JSVGViewerFrame.this);
         if (var3 == 0) {
            File var4 = var2.getSelectedFile();
            SVGOptionPanel var5 = SVGOptionPanel.showDialog(JSVGViewerFrame.this);
            final boolean var6 = var5.getUseXMLBase();
            final boolean var7 = var5.getPrettyPrint();
            var5 = null;
            final SVGDocument var8 = JSVGViewerFrame.this.svgCanvas.getSVGDocument();
            if (var8 != null) {
               JSVGViewerFrame.this.statusBar.setMessage(JSVGViewerFrame.resources.getString("Message.saveAs"));
               JSVGViewerFrame.this.currentSavePath = var4;
               final OutputStreamWriter var9 = null;

               try {
                  FileOutputStream var10 = null;
                  var10 = new FileOutputStream(var4);
                  BufferedOutputStream var15 = new BufferedOutputStream(var10);
                  var9 = new OutputStreamWriter(var15, "utf-8");
               } catch (Exception var14) {
                  JSVGViewerFrame.this.userAgent.displayError(var14);
                  return;
               }

               final Runnable var11 = new Runnable() {
                  public void run() {
                     String var1 = JSVGViewerFrame.resources.getString("Message.done");
                     JSVGViewerFrame.this.statusBar.setMessage(var1);
                  }
               };
               Runnable var12 = new Runnable() {
                  public void run() {
                     try {
                        var9.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        var9.write(JSVGViewerFrame.EOL);
                        Node var1 = var8.getFirstChild();
                        if (var1.getNodeType() != 10) {
                           var9.write("<!DOCTYPE svg PUBLIC '");
                           var9.write("-//W3C//DTD SVG 1.0//EN");
                           var9.write("' '");
                           var9.write("http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
                           var9.write("'>");
                           var9.write(JSVGViewerFrame.EOL);
                           var9.write(JSVGViewerFrame.EOL);
                        }

                        SVGSVGElement var2 = var8.getRootElement();
                        boolean var3 = var6;
                        if (var2.hasAttributeNS("http://www.w3.org/XML/1998/namespace", "base")) {
                           var3 = false;
                        }

                        if (var3) {
                           var2.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", var8.getURL());
                        }

                        if (var7) {
                           SVGTranscoder var4 = new SVGTranscoder();
                           var4.transcode(new TranscoderInput(var8), new TranscoderOutput(var9));
                        } else {
                           DOMUtilities.writeDocument(var8, var9);
                        }

                        var9.close();
                        if (var3) {
                           var2.removeAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base");
                        }

                        if (EventQueue.isDispatchThread()) {
                           var11.run();
                        } else {
                           EventQueue.invokeLater(var11);
                        }
                     } catch (Exception var5) {
                        JSVGViewerFrame.this.userAgent.displayError(var5);
                     }

                  }
               };
               UpdateManager var13 = JSVGViewerFrame.this.svgCanvas.getUpdateManager();
               if (var13 != null && var13.isRunning()) {
                  var13.getUpdateRunnableQueue().invokeLater(var12);
               } else {
                  var12.run();
               }

            }
         }
      }
   }

   public class PrintAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.svgDocument != null) {
            final SVGDocument var2 = JSVGViewerFrame.this.svgDocument;
            (new Thread() {
               public void run() {
                  String var1 = var2.getURL();
                  String var2x = JSVGViewerFrame.this.svgCanvas.getFragmentIdentifier();
                  if (var2x != null) {
                     var1 = var1 + '#' + var2x;
                  }

                  PrintTranscoder var3 = new PrintTranscoder();
                  if (JSVGViewerFrame.this.application.getXMLParserClassName() != null) {
                     var3.addTranscodingHint(JPEGTranscoder.KEY_XML_PARSER_CLASSNAME, JSVGViewerFrame.this.application.getXMLParserClassName());
                  }

                  var3.addTranscodingHint(PrintTranscoder.KEY_SHOW_PAGE_DIALOG, Boolean.TRUE);
                  var3.addTranscodingHint(PrintTranscoder.KEY_SHOW_PRINTER_DIALOG, Boolean.TRUE);
                  var3.transcode(new TranscoderInput(var1), (TranscoderOutput)null);

                  try {
                     var3.print();
                  } catch (PrinterException var5) {
                     JSVGViewerFrame.this.userAgent.displayError((Exception)var5);
                  }

               }
            }).start();
         }

      }
   }

   public class ForwardAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.localHistory.canGoForward()) {
            JSVGViewerFrame.this.localHistory.forward();
         }

      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      protected void update() {
         boolean var1 = JSVGViewerFrame.this.localHistory.canGoForward();
         Iterator var2 = this.components.iterator();

         while(var2.hasNext()) {
            ((JComponent)var2.next()).setEnabled(var1);
         }

      }
   }

   public class BackAction extends AbstractAction implements JComponentModifier {
      List components = new LinkedList();

      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.localHistory.canGoBack()) {
            JSVGViewerFrame.this.localHistory.back();
         }

      }

      public void addJComponent(JComponent var1) {
         this.components.add(var1);
         var1.setEnabled(false);
      }

      protected void update() {
         boolean var1 = JSVGViewerFrame.this.localHistory.canGoBack();
         Iterator var2 = this.components.iterator();

         while(var2.hasNext()) {
            ((JComponent)var2.next()).setEnabled(var1);
         }

      }
   }

   public class ReloadAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if ((var1.getModifiers() & 1) == 1) {
            JSVGViewerFrame.this.svgCanvas.flushImageCache();
         }

         if (JSVGViewerFrame.this.svgDocument != null) {
            JSVGViewerFrame.this.localHistory.reload();
         }

      }
   }

   public class CloseAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JSVGViewerFrame.this.application.closeJSVGViewerFrame(JSVGViewerFrame.this);
      }
   }

   public class PreferencesAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JSVGViewerFrame.this.application.showPreferenceDialog(JSVGViewerFrame.this);
      }
   }

   public class NewWindowAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JSVGViewerFrame var2 = JSVGViewerFrame.this.application.createAndShowJSVGViewerFrame();
         var2.autoAdjust = JSVGViewerFrame.this.autoAdjust;
         var2.debug = JSVGViewerFrame.this.debug;
         var2.svgCanvas.setProgressivePaint(JSVGViewerFrame.this.svgCanvas.getProgressivePaint());
         var2.svgCanvas.setDoubleBufferedRendering(JSVGViewerFrame.this.svgCanvas.getDoubleBufferedRendering());
      }
   }

   public class OpenLocationAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (JSVGViewerFrame.this.uriChooser == null) {
            JSVGViewerFrame.this.uriChooser = new URIChooser(JSVGViewerFrame.this);
            JSVGViewerFrame.this.uriChooser.setFileFilter(new SVGFileFilter());
            JSVGViewerFrame.this.uriChooser.pack();
            Rectangle var2 = JSVGViewerFrame.this.getBounds();
            Dimension var3 = JSVGViewerFrame.this.uriChooser.getSize();
            JSVGViewerFrame.this.uriChooser.setLocation(var2.x + (var2.width - var3.width) / 2, var2.y + (var2.height - var3.height) / 2);
         }

         if (JSVGViewerFrame.this.uriChooser.showDialog() == 0) {
            String var10 = JSVGViewerFrame.this.uriChooser.getText();
            if (var10 == null) {
               return;
            }

            int var11 = var10.indexOf(35);
            String var4 = "";
            if (var11 != -1) {
               var4 = var10.substring(var11 + 1);
               var10 = var10.substring(0, var11);
            }

            if (!var10.equals("")) {
               File var5 = new File(var10);
               if (var5.exists()) {
                  if (var5.isDirectory()) {
                     var10 = null;
                  } else {
                     try {
                        var10 = var5.getCanonicalPath();
                        if (var10.startsWith("/")) {
                           var10 = "file:" + var10;
                        } else {
                           var10 = "file:/" + var10;
                        }
                     } catch (IOException var9) {
                     }
                  }
               }

               if (var10 != null) {
                  if (JSVGViewerFrame.this.svgDocument != null) {
                     ParsedURL var6 = new ParsedURL(JSVGViewerFrame.this.svgDocument.getURL());
                     ParsedURL var7 = new ParsedURL(var6, var10);
                     String var8 = JSVGViewerFrame.this.svgCanvas.getFragmentIdentifier();
                     if (var6.equals(var7) && var4.equals(var8)) {
                        return;
                     }
                  }

                  if (var4.length() != 0) {
                     var10 = var10 + '#' + var4;
                  }

                  JSVGViewerFrame.this.showSVGDocument(var10);
               }
            }
         }

      }
   }

   public class OpenAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         File var2 = null;
         FileDialog var3;
         String var4;
         if (Platform.isOSX) {
            var3 = new FileDialog(JSVGViewerFrame.this, Resources.getString("Open.title"));
            var3.setFilenameFilter(new FilenameFilter() {
               public boolean accept(File var1, String var2) {
                  Iterator var3 = JSVGViewerFrame.getHandlers().iterator();

                  SquiggleInputHandler var4;
                  do {
                     if (!var3.hasNext()) {
                        return false;
                     }

                     var4 = (SquiggleInputHandler)var3.next();
                  } while(!var4.accept(new File(var1, var2)));

                  return true;
               }
            });
            var3.setVisible(true);
            var4 = var3.getFile();
            if (var3 != null) {
               String var5 = var3.getDirectory();
               var2 = new File(var5, var4);
            }
         } else {
            var3 = null;
            var4 = System.getProperty(JSVGViewerFrame.PROPERTY_OS_NAME, JSVGViewerFrame.PROPERTY_OS_NAME_DEFAULT);
            SecurityManager var11 = System.getSecurityManager();
            JFileChooser var9;
            if (JSVGViewerFrame.priorJDK1_4 && var11 != null && var4.indexOf(JSVGViewerFrame.PROPERTY_OS_WINDOWS_PREFIX) != -1) {
               var9 = new JFileChooser(JSVGViewerFrame.makeAbsolute(JSVGViewerFrame.this.currentPath), new WindowsAltFileSystemView());
            } else {
               var9 = new JFileChooser(JSVGViewerFrame.makeAbsolute(JSVGViewerFrame.this.currentPath));
            }

            var9.setFileHidingEnabled(false);
            var9.setFileSelectionMode(0);
            Iterator var6 = JSVGViewerFrame.getHandlers().iterator();

            while(var6.hasNext()) {
               SquiggleInputHandler var7 = (SquiggleInputHandler)var6.next();
               var9.addChoosableFileFilter(new SquiggleInputHandlerFilter(var7));
            }

            int var12 = var9.showOpenDialog(JSVGViewerFrame.this);
            if (var12 == 0) {
               var2 = var9.getSelectedFile();
               JSVGViewerFrame.this.currentPath = var2;
            }
         }

         if (var2 != null) {
            try {
               String var10 = var2.toURL().toString();
               JSVGViewerFrame.this.showSVGDocument(var10);
            } catch (MalformedURLException var8) {
               if (JSVGViewerFrame.this.userAgent != null) {
                  JSVGViewerFrame.this.userAgent.displayError((Exception)var8);
               }
            }
         }

      }
   }

   public class AboutAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         AboutDialog var2 = new AboutDialog(JSVGViewerFrame.this);
         var2.setSize(var2.getPreferredSize());
         var2.setLocationRelativeTo(JSVGViewerFrame.this);
         var2.setVisible(true);
         var2.toFront();
      }
   }

   protected static class Debugger {
      protected static boolean isPresent;
      protected static Class debuggerClass;
      protected static Class contextFactoryClass;
      protected static final int CLEAR_ALL_BREAKPOINTS_METHOD = 0;
      protected static final int GO_METHOD = 1;
      protected static final int SET_EXIT_ACTION_METHOD = 2;
      protected static final int ATTACH_TO_METHOD = 3;
      protected static final int DETACH_METHOD = 4;
      protected static final int DISPOSE_METHOD = 5;
      protected static final int GET_DEBUG_FRAME_METHOD = 6;
      protected static Constructor debuggerConstructor;
      protected static Method[] debuggerMethods;
      protected static Class rhinoInterpreterClass;
      protected static Method getContextFactoryMethod;
      protected Object debuggerInstance;
      protected JSVGViewerFrame svgFrame;

      public Debugger(JSVGViewerFrame var1, String var2) {
         this.svgFrame = var1;

         try {
            this.debuggerInstance = debuggerConstructor.newInstance("JavaScript Debugger - " + var2);
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4.getMessage());
         } catch (InvocationTargetException var5) {
            var5.printStackTrace();
            throw new RuntimeException(var5.getMessage());
         } catch (InstantiationException var6) {
            throw new RuntimeException(var6.getMessage());
         }
      }

      public void setDocumentURL(String var1) {
         this.getDebugFrame().setTitle("JavaScript Debugger - " + var1);
      }

      public void initialize() {
         JFrame var1 = this.getDebugFrame();
         JMenuBar var2 = var1.getJMenuBar();
         JMenu var3 = var2.getMenu(0);
         var3.getItem(0).setEnabled(false);
         var3.getItem(1).setEnabled(false);
         var3.getItem(3).setText(Resources.getString("Close.text"));
         var3.getItem(3).setAccelerator(KeyStroke.getKeyStroke(87, 2));
         var1.setSize(600, 460);
         var1.pack();
         this.setExitAction(new Runnable() {
            public void run() {
               Debugger.this.svgFrame.hideDebugger();
            }
         });
         WindowAdapter var4 = new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
               Debugger.this.svgFrame.hideDebugger();
            }
         };
         var1.addWindowListener(var4);
         var1.setVisible(true);
         this.attach();
      }

      public void attach() {
         Object var1 = this.svgFrame.svgCanvas.getRhinoInterpreter();
         if (var1 != null) {
            this.attachTo(this.getContextFactory(var1));
         }

      }

      protected JFrame getDebugFrame() {
         try {
            return (JFrame)debuggerMethods[6].invoke(this.debuggerInstance, (Object[])null);
         } catch (InvocationTargetException var2) {
            throw new RuntimeException(var2.getMessage());
         } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      protected void setExitAction(Runnable var1) {
         try {
            debuggerMethods[2].invoke(this.debuggerInstance, var1);
         } catch (InvocationTargetException var3) {
            throw new RuntimeException(var3.getMessage());
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4.getMessage());
         }
      }

      public void attachTo(Object var1) {
         try {
            debuggerMethods[3].invoke(this.debuggerInstance, var1);
         } catch (InvocationTargetException var3) {
            throw new RuntimeException(var3.getMessage());
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4.getMessage());
         }
      }

      public void detach() {
         try {
            debuggerMethods[4].invoke(this.debuggerInstance, (Object[])null);
         } catch (InvocationTargetException var2) {
            throw new RuntimeException(var2.getMessage());
         } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      public void go() {
         try {
            debuggerMethods[1].invoke(this.debuggerInstance, (Object[])null);
         } catch (InvocationTargetException var2) {
            throw new RuntimeException(var2.getMessage());
         } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      public void clearAllBreakpoints() {
         try {
            debuggerMethods[0].invoke(this.debuggerInstance, (Object[])null);
         } catch (InvocationTargetException var2) {
            throw new RuntimeException(var2.getMessage());
         } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      public void dispose() {
         try {
            debuggerMethods[5].invoke(this.debuggerInstance, (Object[])null);
         } catch (InvocationTargetException var2) {
            throw new RuntimeException(var2.getMessage());
         } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      protected Object getContextFactory(Object var1) {
         try {
            return getContextFactoryMethod.invoke(var1, (Object[])null);
         } catch (InvocationTargetException var3) {
            throw new RuntimeException(var3.getMessage());
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4.getMessage());
         }
      }

      static {
         try {
            Class var0 = Class.forName("org.mozilla.javascript.tools.debugger.Main");
            Class var1 = Class.forName("org.mozilla.javascript.ContextFactory");
            rhinoInterpreterClass = Class.forName("org.apache.batik.script.rhino.RhinoInterpreter");
            debuggerConstructor = var0.getConstructor(JSVGViewerFrame.class$java$lang$String == null ? (JSVGViewerFrame.class$java$lang$String = JSVGViewerFrame.class$("java.lang.String")) : JSVGViewerFrame.class$java$lang$String);
            debuggerMethods = new Method[]{var0.getMethod("clearAllBreakpoints", (Class[])null), var0.getMethod("go", (Class[])null), var0.getMethod("setExitAction", JSVGViewerFrame.class$java$lang$Runnable == null ? (JSVGViewerFrame.class$java$lang$Runnable = JSVGViewerFrame.class$("java.lang.Runnable")) : JSVGViewerFrame.class$java$lang$Runnable), var0.getMethod("attachTo", var1), var0.getMethod("detach", (Class[])null), var0.getMethod("dispose", (Class[])null), var0.getMethod("getDebugFrame", (Class[])null)};
            getContextFactoryMethod = rhinoInterpreterClass.getMethod("getContextFactory", (Class[])null);
            debuggerClass = var0;
            isPresent = true;
         } catch (ClassNotFoundException var2) {
         } catch (NoSuchMethodException var3) {
         } catch (SecurityException var4) {
         }

      }
   }

   protected class Canvas extends JSVGCanvas {
      public Canvas(SVGUserAgent var2, boolean var3, boolean var4) {
         super(var2, var3, var4);
      }

      public Object getRhinoInterpreter() {
         return this.bridgeContext == null ? null : this.bridgeContext.getInterpreter("text/ecmascript");
      }

      protected class JSVGViewerDOMViewerController implements DOMViewerController {
         public boolean canEdit() {
            return Canvas.this.getUpdateManager() != null;
         }

         public ElementOverlayManager createSelectionManager() {
            return this.canEdit() ? new ElementOverlayManager(Canvas.this) : null;
         }

         public Document getDocument() {
            return Canvas.this.svgDocument;
         }

         public void performUpdate(Runnable var1) {
            if (this.canEdit()) {
               Canvas.this.getUpdateManager().getUpdateRunnableQueue().invokeLater(var1);
            } else {
               var1.run();
            }

         }

         public void removeSelectionOverlay(Overlay var1) {
            Canvas.this.getOverlays().remove(var1);
         }

         public void selectNode(Node var1) {
            DOMViewerAction var2 = (DOMViewerAction)JSVGViewerFrame.this.getAction("DOMViewerAction");
            var2.openDOMViewer();
            JSVGViewerFrame.this.domViewer.selectNode(var1);
         }
      }
   }
}
