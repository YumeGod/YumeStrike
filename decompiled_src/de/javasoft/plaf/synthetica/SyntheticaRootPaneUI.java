package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.RootPanePainter;
import de.javasoft.plaf.synthetica.painter.SyntheticaPainter;
import de.javasoft.util.JavaVersion;
import de.javasoft.util.OS;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;

public class SyntheticaRootPaneUI extends BasicRootPaneUI {
   private Window window;
   private JRootPane rootPane;
   private LayoutManager layoutManager;
   private LayoutManager oldLayoutManager;
   private AWTEventListener awtMouseListener;
   private MouseInputListener mouseInputListener;
   private WindowListener windowListener;
   private ComponentListener windowShapeResizeListener;
   private ComponentListener windowMenuBarResizeListener;
   private ComponentListener rootPaneResizeListener;
   private MouseEventProcessor mouseEventProcessor;
   private JComponent titlePane;
   private Insets resizeInsets;
   public static final boolean EVAL_COPY = false;
   public static final int EVAL_HEIGHT = 16;
   public static final String EVAL_TEXT = "Synthetica - Unregistered Evaluation Copy!";
   private int snapThreshold = 12;
   private int snapHeightThreshold = 24;
   private int snapCornerSize = 24;
   private SnapState snapState;
   private Rectangle preSnapBounds;
   private WeakReference headerShadowComponent;
   private JComponent headerShadow;

   public static ComponentUI createUI(JComponent c) {
      return new SyntheticaRootPaneUI();
   }

   public static final boolean isEvalCopy() {
      return false;
   }

   public void installUI(JComponent c) {
      super.installUI(c);
      this.rootPane = (JRootPane)c;
      if (this.isDecorated(this.rootPane)) {
         this.installClientDecorations(this.rootPane);
      }

      if (this.isHeaderShadowEnabled()) {
         this.installHeaderListener(this.rootPane);
      }

   }

   public void uninstallUI(JComponent c) {
      super.uninstallUI(c);
      this.uninstallClientDecorations(this.rootPane);
      this.uninstallHeaderListener(this.rootPane);
      this.rootPane = null;
   }

   private void installClientDecorations(JRootPane root) {
      JComponent titlePane = new SyntheticaTitlePane(root, this);
      this.setTitlePane(root, titlePane);
      this.installBorder(root);
      this.installWindowListeners(root, root.getParent());
      this.installLayout(root);
      String styleName;
      Container contentPane;
      if (this.window instanceof Dialog && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.dialogStyle.enabled", this.window, false) && SyntheticaLookAndFeel.getClientProperty("Synthetica.style", this.rootPane, (Object)null) == null) {
         styleName = SyntheticaLookAndFeel.getString("Synthetica.rootPane.dialogStyle.defaultName", this.window, "Dialog");
         this.rootPane.putClientProperty("Synthetica.style", styleName);
         contentPane = this.rootPane.getContentPane();
         if (contentPane != null && contentPane.getName().startsWith("null.")) {
         }

         contentPane.setName(styleName + ".contentPane");
      } else if (this.window instanceof Frame && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.frameStyle.enabled", this.window, false) && SyntheticaLookAndFeel.getClientProperty("Synthetica.style", this.rootPane, (Object)null) == null) {
         styleName = SyntheticaLookAndFeel.getString("Synthetica.rootPane.frameStyle.defaultName", this.window, "Frame");
         this.rootPane.putClientProperty("Synthetica.style", styleName);
         contentPane = this.rootPane.getContentPane();
         if (contentPane != null && contentPane.getName().startsWith("null.")) {
         }

         contentPane.setName(styleName + ".contentPane");
      }

   }

   private void uninstallClientDecorations(JRootPane root) {
      if (this.titlePane != null && this.titlePane instanceof SyntheticaTitlePane) {
         ((SyntheticaTitlePane)this.titlePane).uninstallListeners(root);
      }

      this.setTitlePane(root, (JComponent)null);
      this.uninstallBorder(root);
      this.uninstallWindowListeners(root);
      this.uninstallLayout(root);
   }

   public JComponent getTitlePane() {
      return this.titlePane;
   }

   void installBorder(JRootPane root) {
      if (this.isDecorated(root)) {
         root.setBorder(new Border() {
            public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
               SynthContext sc = SyntheticaLookAndFeel.createContext((JComponent)c, Region.ROOT_PANE, 0);
               SyntheticaPainter.getInstance().paintRootPaneBorder(sc, g, x, y, w, h);
            }

            public Insets getBorderInsets(Component c) {
               if (SyntheticaRootPaneUI.this.isMaximized(SyntheticaRootPaneUI.this.window)) {
                  return new Insets(0, 0, 0, 0);
               } else {
                  Insets insets = SyntheticaRootPaneUI.this.getDefaultBorderInsets();
                  if (SyntheticaRootPaneUI.this.getRootPaneBorderText() != null) {
                     insets = (Insets)insets.clone();
                     insets.bottom += 16;
                     insets.left += SyntheticaLookAndFeel.getInt("Synthetica.rootPane.margin.left", SyntheticaRootPaneUI.this.window, 0);
                  }

                  return insets;
               }
            }

            public boolean isBorderOpaque() {
               return false;
            }
         });
      }

   }

   private Insets getDefaultBorderInsets() {
      Insets insets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.size", this.window);
      if (insets == null) {
         insets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.insets", this.window);
      }

      return insets;
   }

   public String getRootPaneBorderText() {
      return SyntheticaLookAndFeel.getString("Synthetica.rootPane.borderText", this.window);
   }

   private void uninstallBorder(JRootPane root) {
      root.setBorder((Border)null);
   }

   private void installWindowListeners(JRootPane root, Component parent) {
      this.window = parent instanceof Window ? (Window)parent : SwingUtilities.getWindowAncestor(parent);
      this.resizeInsets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.resizeInsets", this.window, this.getDefaultBorderInsets());
      if (this.window != null) {
         if (this.getMouseInputListener() == null) {
            this.setMouseInputListener(new MouseInputHandler());
         }

         if (this.getAWTMouseInputListener() == null) {
            this.setAWTMouseInputListener(new AWTMouseInputHandler());
         }

         if (this.windowListener == null) {
            if (!SyntheticaLookAndFeel.isWindowOpacityEnabled(this.window)) {
               if (OS.getCurrentOS() == OS.Mac) {
                  SyntheticaLookAndFeel.setWindowOpaque(this.window, false);
               } else {
                  try {
                     SyntheticaLookAndFeel.setWindowOpaque(this.window, false);
                  } catch (Exception var4) {
                  }
               }
            }

            boolean opaque = SyntheticaLookAndFeel.getBoolean("Synthetica.window.contentPane.opaque", this.window, true);
            this.setContentPaneOpaque(this.window, opaque);
            this.windowListener = new WindowAdapter() {
               public void windowOpened(WindowEvent evt) {
                  Window w = evt.getWindow();
                  if (!SyntheticaLookAndFeel.isWindowOpacityEnabled(SyntheticaRootPaneUI.this.window)) {
                     SyntheticaLookAndFeel.setWindowOpaque(w, false);
                     if (SyntheticaLookAndFeel.getBoolean("Synthetica.window.contentPane.opaque", SyntheticaRootPaneUI.this.window, true)) {
                        SyntheticaRootPaneUI.this.setContentPaneOpaque(w, true);
                     }
                  }

                  if (SyntheticaRootPaneUI.this.snapState == null && SyntheticaRootPaneUI.this.preSnapBounds == null) {
                     SyntheticaRootPaneUI.this.preSnapBounds = evt.getWindow().getBounds();
                  }

               }

               public void windowDeactivated(WindowEvent evt) {
                  if (SyntheticaRootPaneUI.this.mouseInputListener instanceof MouseInputHandler) {
                     ((MouseInputHandler)SyntheticaRootPaneUI.this.mouseInputListener).cancelDragResize();
                     ((MouseInputHandler)SyntheticaRootPaneUI.this.mouseInputListener).cancelSnap();
                  }

               }
            };
            this.window.addWindowListener(this.windowListener);
         }

         if (SyntheticaLookAndFeel.isWindowShapeEnabled(this.window)) {
            if (this.windowShapeResizeListener == null && SyntheticaLookAndFeel.isWindowShapeSupported(this.window)) {
               this.windowShapeResizeListener = new ComponentAdapter() {
                  public void componentResized(ComponentEvent evt) {
                     Window w = (Window)evt.getComponent();
                     SyntheticaLookAndFeel.updateWindowShape(w);
                  }
               };
               this.window.addComponentListener(this.windowShapeResizeListener);
            } else if (OS.getCurrentOS() == OS.Mac) {
               SyntheticaLookAndFeel.updateWindowShape(this.window);
            }
         }

         if (this.titlePane instanceof SyntheticaTitlePane && ((SyntheticaTitlePane)this.titlePane).showMenuBarInTitlePane() && this.windowMenuBarResizeListener == null) {
            this.windowMenuBarResizeListener = new ComponentAdapter() {
               public void componentResized(ComponentEvent evt) {
                  SyntheticaRootPaneUI.this.rootPane.doLayout();
               }
            };
            this.window.addComponentListener(this.windowMenuBarResizeListener);
         }
      }

   }

   private void installHeaderListener(JRootPane root) {
      if (this.rootPaneResizeListener == null) {
         this.rootPaneResizeListener = new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
               if (SyntheticaRootPaneUI.this.getHeaderShadowComponent() == null) {
                  SyntheticaRootPaneUI.this.updateHeaderShadow();
               }

            }
         };
         root.addComponentListener(this.rootPaneResizeListener);
      }

   }

   private void uninstallHeaderListener(JRootPane root) {
      if (this.rootPaneResizeListener != null) {
         root.removeComponentListener(this.rootPaneResizeListener);
         this.rootPaneResizeListener = null;
      }

   }

   private void setContentPaneOpaque(Window w, boolean opaque) {
      if (w instanceof JDialog && ((JDialog)w).getContentPane() instanceof JComponent) {
         ((JComponent)((JDialog)w).getContentPane()).setOpaque(opaque);
      } else if (w instanceof JFrame && ((JFrame)w).getContentPane() instanceof JComponent) {
         ((JComponent)((JFrame)w).getContentPane()).setOpaque(opaque);
      } else if (w instanceof JWindow && ((JWindow)w).getContentPane() instanceof JComponent) {
         ((JComponent)((JWindow)w).getContentPane()).setOpaque(opaque);
      }

   }

   private void uninstallWindowListeners(JRootPane root) {
      if (this.window != null) {
         if (this.awtMouseListener != null) {
            this.removeAWTMouseListener(this.awtMouseListener);
         }

         this.window.removeWindowListener(this.windowListener);
         this.window.removeComponentListener(this.windowShapeResizeListener);
         this.window.removeComponentListener(this.windowMenuBarResizeListener);
      }

      this.awtMouseListener = null;
      if (this.mouseInputListener instanceof MouseInputHandler) {
         this.mouseInputListener = null;
      }

      this.windowListener = null;
      this.windowShapeResizeListener = null;
      this.windowMenuBarResizeListener = null;
      this.window = null;
   }

   private void installLayout(JRootPane root) {
      if (this.layoutManager == null) {
         this.layoutManager = new SyntheticaRootLayout();
      }

      this.oldLayoutManager = root.getLayout();
      root.setLayout(this.layoutManager);
   }

   private void uninstallLayout(JRootPane root) {
      if (this.oldLayoutManager != null) {
         root.setLayout(this.oldLayoutManager);
      }

      this.oldLayoutManager = null;
      this.layoutManager = null;
   }

   private void setTitlePane(JRootPane root, JComponent titlePane) {
      JLayeredPane layeredPane = root.getLayeredPane();
      if (this.titlePane != null) {
         this.titlePane.setVisible(false);
         layeredPane.remove(this.titlePane);
      }

      if (titlePane != null) {
         layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
         titlePane.setVisible(true);
      }

      this.titlePane = titlePane;
   }

   public void propertyChange(PropertyChangeEvent e) {
      super.propertyChange(e);
      String propertyName = e.getPropertyName();
      if (propertyName != null) {
         if (propertyName.equals("windowDecorationStyle")) {
            this.uninstallClientDecorations(this.rootPane);
            if (this.isDecorated(this.rootPane)) {
               this.installClientDecorations(this.rootPane);
            }
         } else if (propertyName.equals("ancestor")) {
            this.uninstallWindowListeners(this.rootPane);
            if (this.isDecorated(this.rootPane) && e.getNewValue() != null) {
               this.installWindowListeners(this.rootPane, this.rootPane.getParent());
            }
         } else if (propertyName.equals("Synthetica.style") && this.titlePane instanceof SyntheticaTitlePane) {
            ((SyntheticaTitlePane)this.titlePane).updateLayoutStyle();
         }

      }
   }

   public void setMaximizedBounds(Frame frame) {
      if (!SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.fullscreen", SyntheticaLookAndFeel.isUIScalingEnabled())) {
         frame.setMaximizedBounds(this.getMaximizedBounds(frame.getGraphicsConfiguration(), SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.respectScreenBoundsX"), SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.respectScreenBoundsY")));
      }
   }

   private Rectangle getMaximizedBounds(GraphicsConfiguration gc, boolean respectScreenBoundsX, boolean respectScreenBoundsY) {
      Rectangle screenBounds = gc.getBounds();
      if (!respectScreenBoundsX) {
         screenBounds.x = 0;
      }

      if (!respectScreenBoundsY) {
         screenBounds.y = 0;
      }

      Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
      if (SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.ignoreScreenInsets")) {
         return screenBounds;
      } else {
         if (!SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.disableAutoHideTaskBarCorrection") && screenInsets.bottom == 0) {
            ++screenInsets.bottom;
         }

         Rectangle maxBounds = new Rectangle(screenBounds.x + screenInsets.left, screenBounds.y + screenInsets.top, screenBounds.width - (screenInsets.left + screenInsets.right), screenBounds.height - (screenInsets.top + screenInsets.bottom));
         return maxBounds;
      }
   }

   private boolean isDecorated(JRootPane root) {
      return root.getWindowDecorationStyle() != 0;
   }

   public MouseInputListener getMouseInputListener() {
      return this.mouseInputListener;
   }

   public void setMouseInputListener(MouseInputListener listener) {
      this.mouseInputListener = listener;
   }

   private AWTEventListener getAWTMouseInputListener() {
      return this.awtMouseListener;
   }

   private void setAWTMouseInputListener(AWTEventListener listener) {
      if (this.awtMouseListener != null && this.window != null) {
         this.removeAWTMouseListener(this.awtMouseListener);
      }

      this.awtMouseListener = listener;
      if (this.awtMouseListener != null && this.window != null) {
         this.addAWTMouseListener(this.awtMouseListener);
      }

      if (this.getMouseEventProcesor() == null) {
         this.mouseEventProcessor = new MouseEventProcessor();
      }

   }

   private void addAWTMouseListener(AWTEventListener listener) {
      Toolkit.getDefaultToolkit().addAWTEventListener(listener, 48L);
   }

   private void removeAWTMouseListener(AWTEventListener listener) {
      Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
   }

   public MouseEventProcessor getMouseEventProcesor() {
      return this.mouseEventProcessor;
   }

   public void setMouseEventProcessor(MouseEventProcessor processor) {
      this.mouseEventProcessor = processor;
   }

   public void setHeaderShadowComponent(JComponent c) {
      if (c == null) {
         this.uninstallHeaderShadowComponent();
      } else if (c != this.getHeaderShadowComponent()) {
         this.installHeaderShadowComponent(c);
      }

      this.updateHeaderShadow();
   }

   public JComponent getHeaderShadowComponent() {
      return this.headerShadowComponent == null ? null : (JComponent)this.headerShadowComponent.get();
   }

   public boolean isHeaderShadowEnabled() {
      return !this.getHeaderShadowType().equals(SyntheticaRootPaneUI.HeaderShadowType.NONE);
   }

   private void installHeaderShadowComponent(JComponent c) {
      PropertyChangeListener pcl = (PropertyChangeListener)c.getClientProperty("Synthetica.headerShadow.ancestorListener");
      if (pcl == null) {
         pcl = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
               if ("ancestor".equals(evt.getPropertyName())) {
                  if (evt.getNewValue() == null) {
                     SyntheticaRootPaneUI.this.uninstallHeaderShadowComponent();
                  }

                  SyntheticaRootPaneUI.this.updateHeaderShadow();
               }

            }
         };
         c.putClientProperty("Synthetica.headerShadow.ancestorListener", pcl);
         c.addPropertyChangeListener(pcl);
      }

      ComponentListener cl = (ComponentListener)c.getClientProperty("Synthetica.headerShadow.componentListener");
      if (cl == null) {
         ComponentListener cl = new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
               SyntheticaRootPaneUI.this.updateHeaderShadow();
            }
         };
         c.putClientProperty("Synthetica.headerShadow.componentListener", cl);
         c.addComponentListener(cl);
      }

      JComponent hsc = this.getHeaderShadowComponent();
      if (hsc == null || c == null || c != hsc && c.getLocationOnScreen().y + c.getHeight() > hsc.getLocationOnScreen().y + hsc.getHeight()) {
         this.headerShadowComponent = new WeakReference(c);
      }

   }

   private void uninstallHeaderShadowComponent() {
      this.headerShadowComponent = null;
   }

   private void updateHeaderShadow() {
      if (this.headerShadow == null && !"ToolBar.FloatingWindow".equals(this.rootPane.getName())) {
         this.headerShadow = new JComponent() {
            protected void paintComponent(Graphics g) {
               JComponent hsc = SyntheticaRootPaneUI.this.getProperHeaderShadowComponent();
               if (hsc != null) {
                  super.paintComponent(g);
                  SyntheticaRootPaneUI.this.paintHeaderShadowBackground(SyntheticaRootPaneUI.this.rootPane, g, 0, 0, this.getWidth(), this.getHeight());
               }

            }
         };
         this.headerShadow.setName("HeaderShadow");
         this.headerShadow.setOpaque(false);
         this.rootPane.getLayeredPane().add(this.headerShadow, new Integer(JLayeredPane.PALETTE_LAYER + 1));
      }

      JComponent hsc = this.getProperHeaderShadowComponent();
      if (this.headerShadow != null && hsc != null) {
         Point p = hsc.getLocation();
         Point p_ = SwingUtilities.convertPoint(hsc.getParent(), p, this.headerShadow.getParent());
         this.headerShadow.setBounds(p_.x, p_.y + hsc.getHeight(), hsc.getWidth(), SyntheticaLookAndFeel.getInt("Synthetica.rootPane.headerShadow.size", this.rootPane, 6));
      }

   }

   private JComponent getProperHeaderShadowComponent() {
      if (this.rootPane == null) {
         return null;
      } else {
         HeaderShadowType type = this.getHeaderShadowType();
         if (type == SyntheticaRootPaneUI.HeaderShadowType.TITLEPANE_ONLY) {
            return this.getTitlePane();
         } else if (this.isHeaderShadowComponentValid()) {
            return this.getHeaderShadowComponent();
         } else if (type != SyntheticaRootPaneUI.HeaderShadowType.NORMAL && type != SyntheticaRootPaneUI.HeaderShadowType.SHADOW_COMPONENT_MENUBAR_ONLY) {
            return null;
         } else {
            JComponent c = this.rootPane.getJMenuBar() == null ? (type == SyntheticaRootPaneUI.HeaderShadowType.SHADOW_COMPONENT_MENUBAR_ONLY ? null : this.getTitlePane()) : this.rootPane.getJMenuBar();
            return (JComponent)(c instanceof SyntheticaTitlePane && !((SyntheticaTitlePane)c).isTitlePaneEnabled() ? null : c);
         }
      }
   }

   private boolean isHeaderShadowComponentValid() {
      JComponent hsc = this.getHeaderShadowComponent();
      if (this.headerShadow != null && hsc != null && hsc.getRootPane() == this.rootPane) {
         Point p = hsc.getLocation();
         p = SwingUtilities.convertPoint(hsc.getParent(), p, this.headerShadow.getParent());
         return p.y + hsc.getHeight() < this.headerShadow.getParent().getHeight();
      } else {
         return false;
      }
   }

   private HeaderShadowType getHeaderShadowType() {
      String val = SyntheticaLookAndFeel.getString("Synthetica.rootPane.headerShadow.type", this.window);
      return val == null ? SyntheticaRootPaneUI.HeaderShadowType.NONE : SyntheticaRootPaneUI.HeaderShadowType.valueOf(val);
   }

   private void paintHeaderShadowBackground(JRootPane rootPane, Graphics g, int x, int y, int w, int h) {
      RootPanePainter.getInstance().paintHeaderShadowBackground(rootPane, (SynthContext)null, g, x, y, w, h);
   }

   void snapNext(Window win) {
      if (this.snapState == null && !this.isMaximized(win)) {
         this.preSnapBounds = win.getBounds();
      } else if (this.isMaximized(win)) {
         ((SyntheticaTitlePane)this.titlePane).restore();
         this.snapState = null;
      }

      GraphicsConfiguration wgc = win.getGraphicsConfiguration();
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] arr$ = ge.getScreenDevices();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         GraphicsDevice gd = arr$[i$];
         GraphicsConfiguration gc = gd.getDefaultConfiguration();
         if (wgc == gc && this.snapState == SyntheticaRootPaneUI.SnapState.SNAP_LEFT) {
            this.restoreSnappedWindow(win, gc, true);
            break;
         }

         if (this.snapState == SyntheticaRootPaneUI.SnapState.SNAP_RIGHT) {
            Rectangle multiBounds = this.getMultiScreenBounds();
            boolean rightmost = wgc.getBounds().x + wgc.getBounds().width == multiBounds.x + multiBounds.width;
            boolean leftmostGC = gc.getBounds().x == multiBounds.x;
            if (rightmost && leftmostGC || gc.getBounds().x > wgc.getBounds().x + wgc.getBounds().width - 1) {
               this.snapLeft(gc, win);
               break;
            }
         } else if (wgc == gc) {
            this.snapRight(gc, win);
            break;
         }
      }

   }

   void snapPrev(Window win) {
      if (this.snapState == null && !this.isMaximized(win)) {
         this.preSnapBounds = win.getBounds();
      } else if (this.isMaximized(win)) {
         ((SyntheticaTitlePane)this.titlePane).restore();
         this.snapState = null;
      }

      GraphicsConfiguration wgc = win.getGraphicsConfiguration();
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] arr$ = ge.getScreenDevices();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         GraphicsDevice gd = arr$[i$];
         GraphicsConfiguration gc = gd.getDefaultConfiguration();
         if (wgc == gc && this.snapState == SyntheticaRootPaneUI.SnapState.SNAP_RIGHT) {
            this.restoreSnappedWindow(win, gc, true);
            break;
         }

         if (this.snapState == SyntheticaRootPaneUI.SnapState.SNAP_LEFT) {
            Rectangle multiBounds = this.getMultiScreenBounds();
            boolean leftmost = wgc.getBounds().x == multiBounds.x;
            boolean rightmostGC = gc.getBounds().x + gc.getBounds().width == multiBounds.x + multiBounds.width;
            if (leftmost && rightmostGC || gc.getBounds().x + gc.getBounds().width - 1 < wgc.getBounds().x) {
               this.snapRight(gc, win);
               break;
            }
         } else if (wgc == gc) {
            this.snapLeft(gc, win);
            break;
         }
      }

   }

   private void snapLeft(GraphicsConfiguration gc, Window win) {
      this.snapState = SyntheticaRootPaneUI.SnapState.SNAP_LEFT;
      Rectangle r = this.getMaximizedBounds(gc, true, true);
      r.width /= 2;
      win.setBounds(this.justifySnapBounds(r, this.snapState));
   }

   private void snapRight(GraphicsConfiguration gc, Window win) {
      this.snapState = SyntheticaRootPaneUI.SnapState.SNAP_RIGHT;
      Rectangle r = this.getMaximizedBounds(gc, true, true);
      r.width /= 2;
      r.x += r.width;
      win.setBounds(this.justifySnapBounds(r, this.snapState));
   }

   void restoreSnappedWindow(Window win, GraphicsConfiguration gc, boolean restoreLocation) {
      if (this.preSnapBounds != null) {
         if (restoreLocation) {
            Rectangle bounds = this.getMaximizedBounds(gc, true, true);
            if (bounds.intersects(this.preSnapBounds)) {
               win.setLocation(this.preSnapBounds.x, this.preSnapBounds.y);
            } else {
               Rectangle r = this.toRelativeScreenBounds(this.preSnapBounds);
               win.setLocation(Math.max(bounds.x, Math.min(bounds.x + bounds.width - r.width, bounds.x + r.x)), Math.max(bounds.y, Math.min(bounds.y + bounds.height - r.height, bounds.y + r.y)));
            }
         }

         win.setSize(this.preSnapBounds.width, this.preSnapBounds.height);
      }

      this.snapState = null;
   }

   private Rectangle toRelativeScreenBounds(Rectangle bounds) {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] arr$ = ge.getScreenDevices();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         GraphicsDevice gd = arr$[i$];
         GraphicsConfiguration gc = gd.getDefaultConfiguration();
         Rectangle rect = gc.getBounds();
         Rectangle r = new Rectangle(bounds.x - rect.x, bounds.y - rect.y, bounds.width, bounds.height);
         if (rect.intersects(bounds)) {
            return r;
         }
      }

      return null;
   }

   private Rectangle getMultiScreenBounds() {
      Rectangle r = new Rectangle();
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] arr$ = ge.getScreenDevices();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         GraphicsDevice gd = arr$[i$];
         r = r.union(gd.getDefaultConfiguration().getBounds());
      }

      return r;
   }

   private Rectangle justifySnapBounds(Rectangle bounds, SnapState state) {
      Insets insets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.snap.insets", this.window, false);
      Rectangle r = new Rectangle(bounds);
      switch (state) {
         case SNAP_LEFT:
            r.x -= insets.left;
            r.y -= insets.top;
            r.width += insets.left;
            r.height += insets.top + insets.bottom;
            break;
         case SNAP_RIGHT:
            r.y -= insets.top;
            r.width += insets.right;
            r.height += insets.top + insets.bottom;
            break;
         case SNAP_HEIGHT:
            r.y -= insets.top;
            r.height += insets.top + insets.bottom;
      }

      return r;
   }

   boolean isSnapEnabled(Window win) {
      boolean translucencyCapable = true;
      if (JavaVersion.JAVA7_OR_ABOVE) {
         GraphicsConfiguration gc = win.getGraphicsConfiguration();

         try {
            Method m = GraphicsConfiguration.class.getMethod("isTranslucencyCapable");
            Object result = m.invoke(gc, (Object[])null);
            translucencyCapable = result instanceof Boolean ? (Boolean)result : false;
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      return translucencyCapable && win instanceof Frame && ((Frame)win).isResizable() && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.snap.enabled", this.window, true);
   }

   private boolean isMaximized(Window win) {
      return win instanceof Frame && (((Frame)win).getExtendedState() & 6) == 6;
   }

   public static class MouseEventProcessor {
      public void process(JRootPane root, MouseEvent evt) {
         Component c = evt.getComponent();
         int id = evt.getID();
         boolean isTitlePaneComponent = SwingUtilities.getAncestorOfClass(SyntheticaTitlePane.class, c) != null;
         if (c.getCursor() != Cursor.getDefaultCursor() && (isTitlePaneComponent || c instanceof MenuElement)) {
            Window window = SwingUtilities.getWindowAncestor(root);
            window.setCursor(Cursor.getDefaultCursor());
         }

         boolean doNotHandle = isTitlePaneComponent || c instanceof MenuElement;
         if (!doNotHandle) {
            MouseInputListener mouseInputListener = ((SyntheticaRootPaneUI)root.getUI()).getMouseInputListener();
            if (mouseInputListener != null) {
               if (id == 500) {
                  mouseInputListener.mouseClicked(evt);
               } else if (id == 506) {
                  mouseInputListener.mouseDragged(evt);
               } else if (id == 504) {
                  mouseInputListener.mouseEntered(evt);
               } else if (id == 505) {
                  mouseInputListener.mouseExited(evt);
               } else if (id == 503) {
                  mouseInputListener.mouseMoved(evt);
               } else if (id == 501) {
                  mouseInputListener.mousePressed(evt);
               } else if (id == 502) {
                  mouseInputListener.mouseReleased(evt);
               }

               if (mouseInputListener instanceof MouseInputHandler && ((MouseInputHandler)mouseInputListener).isResizing()) {
                  evt.consume();
               }

            }
         }
      }
   }

   private class MouseInputHandler implements MouseInputListener {
      private static final int WINDOW_MOVE = 1;
      private static final int WINDOW_RESIZE = 2;
      private int windowAction;
      private int dragXOffset;
      private int dragYOffset;
      private Dimension dragDimension;
      private int resizeType;
      private int minimumYPos;
      private int maximumYPos;
      private boolean liveResize;
      private boolean liveDrag;
      private Rectangle resizeBounds;
      private JWindow dragResizeWindow;
      private JWindow snapWindow;
      private SnapState tmpSnapState;
      private int preSnapXOffset;
      private Point dragStart;
      private Point dragStartOnScreen;
      private boolean windowMenuKeyEmulationRunning;
      private final PrivilegedExceptionAction locationAction;
      private Frame frame;
      private Dialog dialog;

      private MouseInputHandler() {
         this.liveResize = SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.liveResizeEnabled", SyntheticaRootPaneUI.this.window, true);
         this.liveDrag = SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.liveDragEnabled", SyntheticaRootPaneUI.this.window, true);
         this.locationAction = new PrivilegedExceptionAction() {
            public Object run() throws HeadlessException {
               PointerInfo info = MouseInfo.getPointerInfo();
               return info == null ? null : info.getLocation();
            }
         };
         this.frame = null;
         this.dialog = null;
         if (SyntheticaRootPaneUI.this.window instanceof Frame) {
            this.frame = (Frame)SyntheticaRootPaneUI.this.window;
         } else if (SyntheticaRootPaneUI.this.window instanceof Dialog) {
            this.dialog = (Dialog)SyntheticaRootPaneUI.this.window;
         }

      }

      private Point getPoint(MouseEvent evt) {
         return SwingUtilities.convertPoint(evt.getComponent(), evt.getPoint(), SyntheticaRootPaneUI.this.window);
      }

      private boolean isInTitlePane(MouseEvent evt) {
         Point titlePanePoint = SwingUtilities.convertPoint(SyntheticaRootPaneUI.this.window, this.getPoint(evt), SyntheticaRootPaneUI.this.titlePane);
         return SyntheticaRootPaneUI.this.titlePane.contains(titlePanePoint);
      }

      public void mousePressed(MouseEvent evt) {
         if (SyntheticaRootPaneUI.this.isDecorated(SyntheticaRootPaneUI.this.rootPane) && !this.windowMenuKeyEmulationRunning) {
            SyntheticaRootPaneUI.this.window.toFront();
            Rectangle bounds = SyntheticaRootPaneUI.this.window.getGraphicsConfiguration().getBounds();
            this.minimumYPos = bounds.y;
            this.maximumYPos = bounds.y + bounds.height;
            this.dragStart = this.getPoint(evt);
            this.dragStartOnScreen = new Point(this.dragStart);
            SwingUtilities.convertPointToScreen(this.dragStartOnScreen, SyntheticaRootPaneUI.this.window);
            Point titlePanePoint = SwingUtilities.convertPoint(SyntheticaRootPaneUI.this.window, this.dragStart, SyntheticaRootPaneUI.this.titlePane);
            int cursor = this.position2Cursor(SyntheticaRootPaneUI.this.window, this.dragStart.x, this.dragStart.y);
            if (cursor == 0 && SyntheticaRootPaneUI.this.titlePane != null && SyntheticaRootPaneUI.this.titlePane.contains(titlePanePoint) && (this.dialog != null || this.frame != null && this.frame.getExtendedState() != 6)) {
               this.windowAction = 1;
               this.dragXOffset = this.dragStart.x;
               this.dragYOffset = this.dragStart.y;
               if (!this.liveDrag && this.dragResizeWindow == null) {
                  this.dragResizeWindow = this.createDragResizeWindow();
                  this.dragResizeWindow.setBounds(SyntheticaRootPaneUI.this.window.getBounds());
                  this.dragResizeWindow.setVisible(true);
               }
            } else if (this.isWindowResizable()) {
               this.dragXOffset = this.dragStart.x;
               this.dragYOffset = this.dragStart.y;
               this.dragDimension = new Dimension(SyntheticaRootPaneUI.this.window.getWidth(), SyntheticaRootPaneUI.this.window.getHeight());
               this.resizeType = this.position2Cursor(SyntheticaRootPaneUI.this.window, this.dragStart.x, this.dragStart.y);
               if (this.resizeType != 0) {
                  this.windowAction = 2;
               }

               if (!this.liveResize && this.resizeType != 0 && this.dragResizeWindow == null) {
                  this.dragResizeWindow = this.createDragResizeWindow();
                  this.dragResizeWindow.setCursor(Cursor.getPredefinedCursor(cursor));
                  this.dragResizeWindow.setBounds(SyntheticaRootPaneUI.this.window.getBounds());
                  this.dragResizeWindow.setVisible(true);
               }
            }

            if (SyntheticaRootPaneUI.this.isSnapEnabled(SyntheticaRootPaneUI.this.window) && SyntheticaRootPaneUI.this.snapState == null) {
               SyntheticaRootPaneUI.this.preSnapBounds = SyntheticaRootPaneUI.this.window.getBounds();
               this.preSnapXOffset = this.dragXOffset;
            }

         }
      }

      public void mouseReleased(MouseEvent evt) {
         if (!this.windowMenuKeyEmulationRunning) {
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.nativeWindowMenuSupport.enabled", SyntheticaRootPaneUI.this.window, false) && evt.getButton() == 3 && this.isInTitlePane(evt) && (OS.getCurrentOS() == OS.Windows || OS.getCurrentOS() == OS.Linux) && this.snapWindow == null) {
               this.windowMenuKeyEmulationRunning = true;

               try {
                  final Robot robot = new Robot();
                  if (OS.getCurrentOS() == OS.Windows) {
                     robot.keyPress(18);
                     robot.keyPress(32);
                  } else if (OS.getCurrentOS() == OS.Linux) {
                     robot.keyPress(18);
                     robot.mousePress(4);
                     robot.mouseRelease(4);
                  }

                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        try {
                           Thread.sleep(100L);
                        } catch (InterruptedException var2) {
                        }

                        if (OS.getCurrentOS() == OS.Windows) {
                           robot.keyRelease(32);
                        }

                        robot.keyRelease(18);
                        SwingUtilities.invokeLater(new Runnable() {
                           public void run() {
                              MouseInputHandler.this.windowMenuKeyEmulationRunning = false;
                           }
                        });
                     }
                  });
               } catch (AWTException var3) {
                  var3.printStackTrace();
               }

            } else {
               Rectangle bounds;
               if (this.dragResizeWindow != null && (this.windowAction == 2 && !this.liveResize || this.windowAction == 1 && !this.liveDrag)) {
                  bounds = this.dragResizeWindow.getBounds();
                  this.dragResizeWindow.setVisible(false);
                  this.dragResizeWindow.dispose();
                  if (this.windowAction == 2 && this.resizeBounds != null) {
                     SyntheticaRootPaneUI.this.window.setBounds(this.resizeBounds);
                  } else {
                     SyntheticaRootPaneUI.this.window.setBounds(bounds);
                  }

                  this.dragResizeWindow = null;
                  this.resizeBounds = null;
               }

               if (this.snapWindow != null) {
                  bounds = this.snapWindow.getBounds();
                  if (this.tmpSnapState == SyntheticaRootPaneUI.SnapState.SNAP_MAXIMIZED) {
                     ((SyntheticaTitlePane)SyntheticaRootPaneUI.this.titlePane).maximize();
                  } else {
                     SyntheticaRootPaneUI.this.window.setBounds(SyntheticaRootPaneUI.this.justifySnapBounds(bounds, this.tmpSnapState));
                  }

                  this.disposeSnapWindow();
                  SyntheticaRootPaneUI.this.snapState = this.tmpSnapState;
                  this.tmpSnapState = null;
               }

               if (this.windowAction == 2 && !SyntheticaRootPaneUI.this.window.isValid()) {
                  SyntheticaRootPaneUI.this.window.validate();
                  SyntheticaRootPaneUI.this.rootPane.repaint();
               }

               this.windowAction = -1;
               this.resizeType = -1;
               SyntheticaRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
            }
         }
      }

      private void cancelDragResize() {
         if (this.dragResizeWindow != null && (this.windowAction == 2 && !this.liveResize || this.windowAction == 1 && !this.liveDrag)) {
            this.dragResizeWindow.setVisible(false);
            this.dragResizeWindow.dispose();
            this.dragResizeWindow = null;
            this.resizeBounds = null;
         }

      }

      public void mouseMoved(MouseEvent evt) {
         if (SyntheticaRootPaneUI.this.isDecorated(SyntheticaRootPaneUI.this.rootPane) && !this.windowMenuKeyEmulationRunning) {
            Point p = this.getPoint(evt);
            int cursor = this.position2Cursor(SyntheticaRootPaneUI.this.window, p.x, p.y);
            if (cursor != 0 && this.isWindowResizable()) {
               SyntheticaRootPaneUI.this.window.setCursor(Cursor.getPredefinedCursor(cursor));
            } else {
               SyntheticaRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
            }

         }
      }

      public void mouseEntered(MouseEvent evt) {
         this.mouseMoved(evt);
      }

      public void mouseExited(MouseEvent evt) {
         SyntheticaRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
      }

      private void cancelSnap() {
         if (this.windowAction == 1 || this.windowAction == 2) {
            this.resetSnapState();
         }

      }

      private void resetSnapState() {
         if (this.snapWindow != null) {
            this.disposeSnapWindow();
         }

         this.tmpSnapState = null;
         SyntheticaRootPaneUI.this.snapState = null;
      }

      private void disposeSnapWindow() {
         this.snapWindow.setVisible(false);
         this.snapWindow.dispose();
         this.snapWindow = null;
      }

      private void unsnap(Window w) {
         SyntheticaRootPaneUI.this.restoreSnappedWindow(w, w.getGraphicsConfiguration(), false);
         this.dragXOffset = this.preSnapXOffset;
      }

      public void mouseDragged(MouseEvent evt) {
         if (!this.windowMenuKeyEmulationRunning) {
            GraphicsConfiguration gc = !this.liveDrag && this.dragResizeWindow != null ? this.dragResizeWindow.getGraphicsConfiguration() : SyntheticaRootPaneUI.this.window.getGraphicsConfiguration();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            this.minimumYPos = gc.getBounds().y + screenInsets.top;
            this.maximumYPos = gc.getBounds().y + gc.getBounds().height - screenInsets.bottom - SyntheticaRootPaneUI.this.titlePane.getHeight() - SyntheticaRootPaneUI.this.rootPane.getInsets().top;
            Rectangle multiRect = SyntheticaRootPaneUI.this.getMultiScreenBounds();
            int minimumXPos = multiRect.x + screenInsets.left;
            int maximumXPos = multiRect.x + multiRect.width - screenInsets.left - screenInsets.right;
            boolean respectMinimumYPos = SyntheticaLookAndFeel.isSystemPropertySet("synthetica.window.respectMinimumYPos");
            boolean respectMaximumYPos = SyntheticaLookAndFeel.isSystemPropertySet("synthetica.window.respectMaximumYPos");
            boolean snapEnabled;
            if (SyntheticaRootPaneUI.this.isMaximized(SyntheticaRootPaneUI.this.window) && evt.getComponent() instanceof Window && this.isInTitlePane(evt) && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.restoreByDragFromMaximized.enabled", SyntheticaRootPaneUI.this.window, true)) {
               int threshold = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.restoreByDragFromMaximized.threshold", SyntheticaRootPaneUI.this.window, 4);
               snapEnabled = Math.abs(this.dragStart.x - evt.getX()) > threshold || Math.abs(this.dragStart.y - evt.getY()) > threshold;
               if (snapEnabled) {
                  ((SyntheticaTitlePane)SyntheticaRootPaneUI.this.titlePane).restore();
                  this.windowAction = 1;
                  if (this.dragXOffset <= 0) {
                     this.dragXOffset = SyntheticaRootPaneUI.this.window.getWidth() / 2;
                  }

                  if (this.dragYOffset <= 0) {
                     this.dragYOffset = SyntheticaRootPaneUI.this.titlePane.getHeight() / 2;
                  }

                  if (!this.liveDrag) {
                     this.dragResizeWindow = this.createDragResizeWindow();
                     this.dragResizeWindow.setBounds(SyntheticaRootPaneUI.this.window.getBounds());
                     this.dragResizeWindow.setVisible(true);
                  }
               }
            }

            Point p;
            if (this.windowAction == 1) {
               try {
                  p = (Point)AccessController.doPrivileged(this.locationAction);
                  if (p == null) {
                     p = this.getPoint(evt);
                     SwingUtilities.convertPointToScreen(p, (Component)evt.getSource());
                  }

                  p.x -= this.dragXOffset;
                  p.y -= this.dragYOffset;
                  if (respectMinimumYPos && p.y < this.minimumYPos) {
                     p.y = this.minimumYPos;
                  }

                  if (respectMaximumYPos && p.y > this.maximumYPos) {
                     p.y = this.maximumYPos;
                  }

                  snapEnabled = SyntheticaRootPaneUI.this.isSnapEnabled(SyntheticaRootPaneUI.this.window);
                  if (snapEnabled && SyntheticaRootPaneUI.this.snapState == SyntheticaRootPaneUI.SnapState.SNAP_HEIGHT && p.y < this.minimumYPos + SyntheticaRootPaneUI.this.snapHeightThreshold) {
                     p.y = this.minimumYPos - SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.snap.insets", SyntheticaRootPaneUI.this.window, false).top;
                  }

                  if (this.liveDrag) {
                     SyntheticaRootPaneUI.this.window.setLocation(p);
                  } else {
                     this.dragResizeWindow.setLocation(p);
                  }

                  if (snapEnabled) {
                     GraphicsConfiguration wgc = this.liveDrag ? SyntheticaRootPaneUI.this.window.getGraphicsConfiguration() : this.dragResizeWindow.getGraphicsConfiguration();
                     Point p_ = new Point(p.x + this.dragXOffset, p.y + this.dragYOffset);
                     if (p_.y <= this.minimumYPos) {
                        this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_MAXIMIZED;
                        if (this.snapWindow == null) {
                           this.snapWindow = this.createSnapWindow();
                        }

                        this.snapWindow.setBounds(SyntheticaRootPaneUI.this.getMaximizedBounds(wgc, true, true));
                        this.snapWindow.setVisible(true);
                     } else {
                        Rectangle r;
                        if (p_.x <= minimumXPos) {
                           this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_LEFT;
                           if (this.snapWindow == null) {
                              this.snapWindow = this.createSnapWindow();
                           }

                           r = SyntheticaRootPaneUI.this.getMaximizedBounds(wgc, true, true);
                           r.width /= 2;
                           this.snapWindow.setBounds(r);
                           this.snapWindow.setVisible(true);
                        } else if (p_.x >= maximumXPos - 1) {
                           this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_RIGHT;
                           if (this.snapWindow == null) {
                              this.snapWindow = this.createSnapWindow();
                           }

                           r = SyntheticaRootPaneUI.this.getMaximizedBounds(wgc, true, true);
                           r.width /= 2;
                           r.x += r.width;
                           this.snapWindow.setBounds(r);
                           this.snapWindow.setVisible(true);
                        } else if (p_.y <= this.minimumYPos + SyntheticaRootPaneUI.this.snapCornerSize && p_.x <= minimumXPos + SyntheticaRootPaneUI.this.snapCornerSize) {
                           this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_TOP_LEFT;
                           if (this.snapWindow == null) {
                              this.snapWindow = this.createSnapWindow();
                           }

                           r = SyntheticaRootPaneUI.this.getMaximizedBounds(wgc, true, true);
                           r.width /= 2;
                           r.height /= 2;
                           this.snapWindow.setBounds(r);
                           this.snapWindow.setVisible(true);
                        } else if (p_.y <= this.minimumYPos + SyntheticaRootPaneUI.this.snapCornerSize && p_.x >= maximumXPos - 1 - SyntheticaRootPaneUI.this.snapCornerSize) {
                           this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_TOP_RIGHT;
                           if (this.snapWindow == null) {
                              this.snapWindow = this.createSnapWindow();
                           }

                           r = SyntheticaRootPaneUI.this.getMaximizedBounds(wgc, true, true);
                           r.width /= 2;
                           r.height /= 2;
                           r.x += r.width;
                           this.snapWindow.setBounds(r);
                           this.snapWindow.setVisible(true);
                        } else if (p_.y >= this.maximumYPos - 1 - SyntheticaRootPaneUI.this.snapCornerSize && p_.x <= minimumXPos + SyntheticaRootPaneUI.this.snapCornerSize) {
                           this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_BOTTOM_LEFT;
                           if (this.snapWindow == null) {
                              this.snapWindow = this.createSnapWindow();
                           }

                           r = SyntheticaRootPaneUI.this.getMaximizedBounds(wgc, true, true);
                           ++r.height;
                           r.width /= 2;
                           r.height /= 2;
                           r.y += r.height - 1;
                           this.snapWindow.setBounds(r);
                           this.snapWindow.setVisible(true);
                        } else if (p_.y >= this.maximumYPos - 1 - SyntheticaRootPaneUI.this.snapCornerSize && p_.x >= maximumXPos - 1 - SyntheticaRootPaneUI.this.snapCornerSize) {
                           this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_BOTTOM_RIGHT;
                           if (this.snapWindow == null) {
                              this.snapWindow = this.createSnapWindow();
                           }

                           r = SyntheticaRootPaneUI.this.getMaximizedBounds(wgc, true, true);
                           ++r.height;
                           r.width /= 2;
                           r.height /= 2;
                           r.x += r.width;
                           r.y += r.height - 1;
                           this.snapWindow.setBounds(r);
                           this.snapWindow.setVisible(true);
                        } else if (this.snapWindow != null && p_.y > this.minimumYPos + SyntheticaRootPaneUI.this.snapThreshold && p_.x > minimumXPos + SyntheticaRootPaneUI.this.snapThreshold && p_.x < maximumXPos - SyntheticaRootPaneUI.this.snapThreshold) {
                           this.disposeSnapWindow();
                        }
                     }

                     if (SyntheticaRootPaneUI.this.snapState != null && this.snapWindow == null && (SyntheticaRootPaneUI.this.snapState != SyntheticaRootPaneUI.SnapState.SNAP_HEIGHT || p.y >= this.minimumYPos + SyntheticaRootPaneUI.this.snapHeightThreshold)) {
                        int thresholdx = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.snap.restoreThreshold", SyntheticaRootPaneUI.this.window, 0);
                        boolean thresholdReached = Math.abs(this.dragStartOnScreen.x - evt.getXOnScreen()) > thresholdx || Math.abs(this.dragStartOnScreen.y - evt.getYOnScreen()) > thresholdx;
                        if (thresholdReached) {
                           this.unsnap((Window)(!this.liveDrag && this.dragResizeWindow != null ? this.dragResizeWindow : SyntheticaRootPaneUI.this.window));
                           if (this.dragXOffset <= 0) {
                              this.dragXOffset = SyntheticaRootPaneUI.this.window.getWidth() / 2;
                           }

                           if (this.dragYOffset <= 0) {
                              this.dragYOffset = SyntheticaRootPaneUI.this.titlePane.getHeight() / 2;
                           }
                        }
                     }
                  }
               } catch (PrivilegedActionException var15) {
               }
            } else if (this.windowAction == 2) {
               if (SyntheticaRootPaneUI.this.isSnapEnabled(SyntheticaRootPaneUI.this.window) && (this.resizeType == 9 || this.resizeType == 8) && SyntheticaRootPaneUI.this.snapState != null) {
                  this.resetSnapState();
               }

               p = this.getPoint(evt);
               Dimension min = (Dimension)SyntheticaLookAndFeel.get("Synthetica.rootPane.minimumWindowSize", (Component)SyntheticaRootPaneUI.this.window);
               if (SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.fallBelowMinimumWindowSize", SyntheticaRootPaneUI.this.window, true) && (evt.getModifiers() & 2) != 0) {
                  min = new Dimension(4, 4);
               }

               if (min == null) {
                  min = SyntheticaRootPaneUI.this.window.getMinimumSize();
               }

               this.resizeBounds = SyntheticaRootPaneUI.this.window.getBounds();
               Rectangle startBounds = new Rectangle(this.resizeBounds);
               if (this.resizeType == 11 || this.resizeType == 7 || this.resizeType == 5) {
                  this.resizeBounds.width = Math.max(min.width, this.dragDimension.width + p.x - this.dragXOffset);
               }

               if (this.resizeType == 9 || this.resizeType == 4 || this.resizeType == 5) {
                  this.resizeBounds.height = Math.max(min.height, this.dragDimension.height + p.y - this.dragYOffset);
                  if (SyntheticaRootPaneUI.this.isSnapEnabled(SyntheticaRootPaneUI.this.window) && this.resizeType == 9) {
                     this.setSnapHeightState();
                  }
               }

               Rectangle var10000;
               int dx;
               if (this.resizeType == 8 || this.resizeType == 6 || this.resizeType == 7) {
                  dx = p.y - this.dragYOffset;
                  var10000 = this.resizeBounds;
                  var10000.y += dx;
                  var10000 = this.resizeBounds;
                  var10000.height -= dx;
                  if (this.resizeBounds.height < min.height) {
                     var10000 = this.resizeBounds;
                     var10000.y += this.resizeBounds.height - min.height;
                     this.resizeBounds.height = min.height;
                  }

                  if (SyntheticaRootPaneUI.this.isSnapEnabled(SyntheticaRootPaneUI.this.window) && this.resizeType == 8) {
                     this.setSnapHeightState();
                  }
               }

               if (this.resizeType == 10 || this.resizeType == 6 || this.resizeType == 4) {
                  dx = p.x - this.dragXOffset;
                  var10000 = this.resizeBounds;
                  var10000.x += dx;
                  var10000 = this.resizeBounds;
                  var10000.width -= dx;
                  if (this.resizeBounds.width < min.width) {
                     var10000 = this.resizeBounds;
                     var10000.x += this.resizeBounds.width - min.width;
                     this.resizeBounds.width = min.width;
                  }
               }

               if (respectMinimumYPos && this.resizeBounds.y < this.minimumYPos) {
                  this.resizeBounds.y = this.minimumYPos;
               }

               if (!this.resizeBounds.equals(startBounds)) {
                  if (this.liveResize) {
                     SyntheticaRootPaneUI.this.window.setBounds(this.resizeBounds);
                  } else {
                     this.dragResizeWindow.setBounds(this.resizeBounds);
                  }
               }
            }

         }
      }

      private void setSnapHeightState() {
         try {
            Point p = (Point)AccessController.doPrivileged(this.locationAction);
            int maxYPos = this.maximumYPos + SyntheticaRootPaneUI.this.rootPane.getInsets().top + SyntheticaRootPaneUI.this.titlePane.getHeight() - 1;
            if ((p.y <= this.minimumYPos || p.y >= maxYPos) && this.snapWindow == null) {
               this.tmpSnapState = SyntheticaRootPaneUI.SnapState.SNAP_HEIGHT;
               this.snapWindow = this.createSnapWindow();
               this.snapWindow.setCursor(Cursor.getPredefinedCursor(8));
               Rectangle r = SyntheticaRootPaneUI.this.getMaximizedBounds(SyntheticaRootPaneUI.this.window.getGraphicsConfiguration(), true, true);
               Rectangle bounds = SyntheticaRootPaneUI.this.window.getBounds();
               r.x = bounds.x;
               r.width = bounds.width;
               this.snapWindow.setBounds(r);
               this.snapWindow.setVisible(true);
            } else if (this.snapWindow != null && p.y > this.minimumYPos + SyntheticaRootPaneUI.this.snapThreshold && p.y < maxYPos - SyntheticaRootPaneUI.this.snapThreshold) {
               this.disposeSnapWindow();
            }
         } catch (PrivilegedActionException var5) {
         }

      }

      public void mouseClicked(MouseEvent evt) {
         if (this.frame != null && !this.windowMenuKeyEmulationRunning) {
            Point convertedPoint = SwingUtilities.convertPoint(SyntheticaRootPaneUI.this.window, this.getPoint(evt), SyntheticaRootPaneUI.this.titlePane);
            if (SyntheticaRootPaneUI.this.titlePane != null && SyntheticaRootPaneUI.this.titlePane.contains(convertedPoint) && evt.getClickCount() == 2 && (evt.getModifiers() & 16) == 16) {
               if (SyntheticaRootPaneUI.this.snapState != null) {
                  if (SyntheticaRootPaneUI.this.isMaximized(this.frame)) {
                     ((SyntheticaTitlePane)SyntheticaRootPaneUI.this.titlePane).restore();
                  }

                  SyntheticaRootPaneUI.this.restoreSnappedWindow(this.frame, this.frame.getGraphicsConfiguration(), true);
               } else if (this.frame.isResizable() && this.isFrameResizable()) {
                  ((SyntheticaTitlePane)SyntheticaRootPaneUI.this.titlePane).maximize();
               } else if (this.frame.isResizable() && !this.isFrameResizable()) {
                  ((SyntheticaTitlePane)SyntheticaRootPaneUI.this.titlePane).restore();
               }
            }

         }
      }

      private int position2Cursor(Window w, int x, int y) {
         int ww = w.getWidth();
         int wh = w.getHeight();
         if (x < SyntheticaRootPaneUI.this.resizeInsets.left && y < SyntheticaRootPaneUI.this.resizeInsets.top) {
            return 6;
         } else if (x > ww - SyntheticaRootPaneUI.this.resizeInsets.right && y < SyntheticaRootPaneUI.this.resizeInsets.top) {
            return 7;
         } else if (x < SyntheticaRootPaneUI.this.resizeInsets.left && y > wh - SyntheticaRootPaneUI.this.resizeInsets.bottom) {
            return 4;
         } else if (x > ww - SyntheticaRootPaneUI.this.resizeInsets.right && y > wh - SyntheticaRootPaneUI.this.resizeInsets.bottom) {
            return 5;
         } else if (x < SyntheticaRootPaneUI.this.resizeInsets.left) {
            return 10;
         } else if (x > ww - SyntheticaRootPaneUI.this.resizeInsets.right) {
            return 11;
         } else if (y < SyntheticaRootPaneUI.this.resizeInsets.top) {
            return 8;
         } else {
            return y > wh - SyntheticaRootPaneUI.this.resizeInsets.bottom ? 9 : 0;
         }
      }

      private JWindow createDragResizeWindow() {
         JWindow w = new JWindow(SyntheticaRootPaneUI.this.window);
         if (OS.getCurrentOS() == OS.Mac) {
            w.getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
         }

         w.getRootPane().setContentPane(new JComponent() {
            public void paintComponent(Graphics g) {
               super.paintComponents(g);
               MouseInputHandler.this.paintResizeBackground(SyntheticaRootPaneUI.this.rootPane, g, 0, 0, this.getWidth(), this.getHeight());
            }
         });
         SyntheticaLookAndFeel.setWindowOpaque(w, false);
         return w;
      }

      private JWindow createSnapWindow() {
         JWindow w = new JWindow();
         if (OS.getCurrentOS() == OS.Mac) {
            w.getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
         }

         w.getRootPane().setContentPane(new JComponent() {
            public void paintComponent(Graphics g) {
               super.paintComponents(g);
               MouseInputHandler.this.paintSnapBackground(SyntheticaRootPaneUI.this.rootPane, g, 0, 0, this.getWidth(), this.getHeight());
            }
         });
         SyntheticaLookAndFeel.setWindowOpaque(w, false);
         return w;
      }

      private void paintResizeBackground(JRootPane rootPane, Graphics g, int x, int y, int w, int h) {
         RootPanePainter.getInstance().paintResizeBackground(rootPane, (SynthContext)null, g, x, y, w, h);
      }

      private void paintSnapBackground(JRootPane rootPane, Graphics g, int x, int y, int w, int h) {
         RootPanePainter.getInstance().paintSnapBackground(rootPane, (SynthContext)null, g, x, y, w, h);
      }

      private boolean isFrameResizable() {
         return this.frame != null && this.frame.isResizable() && (this.frame.getExtendedState() & 6) == 0;
      }

      private boolean isDialogResizable() {
         return this.dialog != null && this.dialog.isResizable();
      }

      private boolean isWindowResizable() {
         return this.isFrameResizable() || this.isDialogResizable();
      }

      public boolean isResizing() {
         return this.windowAction == 2;
      }

      // $FF: synthetic method
      MouseInputHandler(Object x1) {
         this();
      }
   }

   private class AWTMouseInputHandler implements AWTEventListener {
      private AWTMouseInputHandler() {
      }

      public void eventDispatched(AWTEvent evt) {
         Object o = evt.getSource();
         if (o instanceof Component) {
            Component c = (Component)o;
            Window w = c instanceof Window ? (Window)c : SwingUtilities.getWindowAncestor(c);
            if (SyntheticaRootPaneUI.this.window != w || !(evt instanceof MouseEvent) || SyntheticaRootPaneUI.this.rootPane == null) {
               return;
            }

            SyntheticaRootPaneUI.this.mouseEventProcessor.process(SyntheticaRootPaneUI.this.rootPane, (MouseEvent)evt);
         }

      }

      // $FF: synthetic method
      AWTMouseInputHandler(Object x1) {
         this();
      }
   }

   private static class SyntheticaRootLayout implements LayoutManager2 {
      private SyntheticaRootLayout() {
      }

      public void addLayoutComponent(String name, Component comp) {
      }

      public void removeLayoutComponent(Component comp) {
      }

      public void addLayoutComponent(Component comp, Object constraints) {
      }

      public float getLayoutAlignmentX(Container target) {
         return 0.0F;
      }

      public float getLayoutAlignmentY(Container target) {
         return 0.0F;
      }

      public void invalidateLayout(Container target) {
      }

      public Dimension preferredLayoutSize(Container parent) {
         Insets insets = parent.getInsets();
         JRootPane root = (JRootPane)parent;
         JComponent titlePane = ((SyntheticaRootPaneUI)root.getUI()).titlePane;
         new Dimension(0, 0);
         Dimension dimC;
         if (root.getContentPane() != null) {
            dimC = root.getContentPane().getPreferredSize();
         } else {
            dimC = root.getSize();
         }

         dimC = dimC == null ? new Dimension(0, 0) : dimC;
         Dimension dimM = new Dimension(0, 0);
         if (root.getJMenuBar() != null) {
            dimM = root.getJMenuBar().getPreferredSize();
         }

         dimM = dimM == null ? new Dimension(0, 0) : dimM;
         Dimension dimT = titlePane.getPreferredSize();
         dimT = dimT == null ? new Dimension(0, 0) : dimT;
         int width = Math.max(dimC.width, Math.max(dimM.width, dimT.width)) + insets.left + insets.right;
         int height = dimC.height + dimM.height + dimT.height + insets.top + insets.bottom;
         return new Dimension(width, height);
      }

      public Dimension minimumLayoutSize(Container parent) {
         Insets insets = parent.getInsets();
         JRootPane root = (JRootPane)parent;
         JComponent titlePane = ((SyntheticaRootPaneUI)root.getUI()).titlePane;
         new Dimension(0, 0);
         Dimension dimC;
         if (root.getContentPane() != null) {
            dimC = root.getContentPane().getMinimumSize();
         } else {
            dimC = root.getSize();
         }

         dimC = dimC == null ? new Dimension(0, 0) : dimC;
         Dimension dimM = new Dimension(0, 0);
         if (root.getJMenuBar() != null) {
            dimM = root.getJMenuBar().getMinimumSize();
         }

         dimM = dimM == null ? new Dimension(0, 0) : dimM;
         Dimension dimT = titlePane.getMinimumSize();
         dimT = dimT == null ? new Dimension(0, 0) : dimT;
         int width = Math.max(dimC.width, Math.max(dimM.width, dimT.width)) + insets.left + insets.right;
         int height = dimC.height + dimM.height + dimT.height + insets.top + insets.bottom;
         return new Dimension(width, height);
      }

      public Dimension maximumLayoutSize(Container target) {
         Insets insets = target.getInsets();
         JRootPane root = (JRootPane)target;
         JComponent titlePane = ((SyntheticaRootPaneUI)root.getUI()).titlePane;
         new Dimension(0, 0);
         Dimension dimC;
         if (root.getContentPane() != null) {
            dimC = root.getContentPane().getMaximumSize();
         } else {
            dimC = root.getSize();
         }

         dimC = dimC == null ? new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE) : dimC;
         Dimension dimM = new Dimension(0, 0);
         if (root.getJMenuBar() != null) {
            dimM = root.getJMenuBar().getMaximumSize();
         }

         dimM = dimM == null ? new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE) : dimM;
         Dimension dimT = titlePane.getMaximumSize();
         dimT = dimT == null ? new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE) : dimT;
         int width = Math.max(dimC.width, Math.max(dimM.width, dimT.width));
         if (width != Integer.MAX_VALUE) {
            width += insets.left + insets.right;
         }

         int height = Math.max(dimC.height, Math.max(dimM.height, dimT.height));
         if (height != Integer.MAX_VALUE) {
            height += insets.top + insets.bottom;
         }

         return new Dimension(width, height);
      }

      public void layoutContainer(Container parent) {
         JRootPane rootPane = (JRootPane)parent;
         Rectangle bounds = rootPane.getBounds();
         Insets insets = rootPane.getInsets() != null ? rootPane.getInsets() : new Insets(0, 0, 0, 0);
         int width = bounds.width - insets.right - insets.left;
         int height = bounds.height - insets.top - insets.bottom;
         int nextY = 0;
         if (rootPane.getLayeredPane() != null) {
            rootPane.getLayeredPane().setBounds(insets.left, insets.top, width, height);
         }

         if (rootPane.getGlassPane() != null) {
            rootPane.getGlassPane().setBounds(insets.left, insets.top, width, height);
         }

         JComponent titlePane = ((SyntheticaRootPaneUI)rootPane.getUI()).titlePane;
         Dimension dimT = null;
         if (titlePane.isEnabled()) {
            dimT = titlePane.getPreferredSize();
            if (dimT != null) {
               titlePane.setBounds(0, 0, width, dimT.height);
               nextY += dimT.height;
            }
         }

         JMenuBar mBar = rootPane.getJMenuBar();
         if (mBar != null && titlePane instanceof SyntheticaTitlePane) {
            SyntheticaTitlePane titlePane_ = (SyntheticaTitlePane)titlePane;
            Dimension dimM = mBar.getPreferredSize();
            if (titlePane_.showMenuBarInTitlePane()) {
               boolean ltr = mBar.getComponentOrientation().isLeftToRight();
               JComponent mb = (JComponent)SyntheticaLookAndFeel.findComponent((String)"RootPane.menuBar", titlePane);
               if (mb != null) {
                  Insets mbInsets = mb.getInsets();
                  nextY = mb.getY() + mbInsets.top;
                  int mBarX = titlePane_.clipMenuBarWidth() ? mb.getX() : (ltr ? mb.getX() : 0);
                  int mBarWidth = titlePane_.clipMenuBarWidth() ? mb.getWidth() - mbInsets.left - mbInsets.right : (ltr ? width - mb.getX() : mb.getX() + mb.getWidth());
                  mBar.setBounds(mBarX + mbInsets.left, nextY, mBarWidth, dimM.height);
                  nextY += mbInsets.top + mbInsets.bottom + dimM.height;
                  JComponent userComponent = titlePane_.getUserComponent();
                  if (userComponent != null && titlePane_.getLayoutStyle() == SyntheticaTitlePane.LayoutStyle.SECONDARYMENU) {
                     nextY += userComponent.getPreferredSize().height;
                  }

                  nextY = Math.max(nextY, dimT == null ? 0 : dimT.height);
               }
            } else {
               mBar.setBounds(0, nextY, width, dimM.height);
               nextY += dimM.height;
            }
         }

         Container cPane = rootPane.getContentPane();
         if (cPane != null) {
            cPane.setBounds(0, nextY, width, height < nextY ? 0 : height - nextY);
         }

      }

      // $FF: synthetic method
      SyntheticaRootLayout(Object x0) {
         this();
      }
   }

   private static enum HeaderShadowType {
      NONE,
      NORMAL,
      SHADOW_COMPONENT_MENUBAR_ONLY,
      SHADOW_COMPONENT_ONLY,
      TITLEPANE_ONLY;
   }

   private static enum SnapState {
      SNAP_LEFT,
      SNAP_RIGHT,
      SNAP_HEIGHT,
      SNAP_MAXIMIZED,
      SNAP_TOP_LEFT,
      SNAP_TOP_RIGHT,
      SNAP_BOTTOM_LEFT,
      SNAP_BOTTOM_RIGHT;
   }
}
