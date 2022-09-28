package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.SyntheticaPainter;
import de.javasoft.util.JavaVersion;
import de.javasoft.util.OS;
import de.javasoft.util.java2d.DropShadow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class SyntheticaTitlePane extends JPanel {
   private static final long serialVersionUID = -6164225719742333375L;
   private static final String BUTTON_ID = "Synthetica.buttonID";
   private JRootPane rootPane;
   private SyntheticaRootPaneUI rootPaneUI;
   private Window window;
   private Frame frame;
   private Dialog dialog;
   private JButton menuButton;
   private JLabel titleLabel;
   private JLabel menuBarLabel;
   private Container userComponentContainer;
   private JButton toggleButton;
   private JButton iconifyButton;
   private JButton closeButton;
   private JPopupMenu systemMenu;
   private Action closeAction;
   private Action iconifyAction;
   private Action restoreAction;
   private Action maximizeAction;
   private WindowListener windowListener;
   private PropertyChangeListener propertyChangeListener;
   private PropertyChangeListener rootPanePropertyChangeListener;
   private ContainerListener layeredPaneContainerListener;
   private ActionListener menuActionListener;
   private boolean selected = true;
   private boolean useMACStyle = false;
   private JInternalFrame iFrameDelegate = new JInternalFrame();

   public SyntheticaTitlePane(JRootPane var1, BasicRootPaneUI var2) {
      this.rootPane = var1;
      this.rootPaneUI = (SyntheticaRootPaneUI)var2;
      Container var3 = this.rootPane.getParent();
      this.window = var3 instanceof Window ? (Window)var3 : SwingUtilities.getWindowAncestor(var3);
      if (this.window instanceof Frame) {
         this.frame = (Frame)this.window;
      } else if (this.window instanceof Dialog) {
         this.dialog = (Dialog)this.window;
      }

      this.useMACStyle = SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.useMACStyleOnMAC", this.window, true);
      this.setOpaque(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.opaque", this.window, true));
      if (!this.isOpaque() && !SyntheticaLookAndFeel.isWindowOpacityEnabled(this.window)) {
         this.setDoubleBuffered(false);
      }

      this.setLayout(new GridBagLayout());
      this.setName("RootPane.titlePane");
      this.closeAction = new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            SyntheticaTitlePane.this.close();
         }
      };
      int var4 = this.rootPane.getWindowDecorationStyle();
      boolean var5 = SyntheticaLookAndFeel.getBoolean("Synthetica.dialog.icon.enabled", this.window);
      if (var4 == 1 || var4 == 3 || var4 == 7 || var4 == 8 || var4 == 2 || var5) {
         this.iconifyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               SyntheticaTitlePane.this.iconify();
            }
         };
         this.restoreAction = new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               SyntheticaTitlePane.this.restore();
            }
         };
         this.maximizeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
               SyntheticaTitlePane.this.maximize();
            }
         };
         this.menuButton = this.createTitlePaneButton("RootPane.titlePane.menuButton");
         this.menuButton.putClientProperty("doNotCancelPopup", (new JComboBox()).getClientProperty("doNotCancelPopup"));
         this.menuButton.setIcon(this.getIcon(this.rootPane));
         this.menuButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1) {
               String var2 = System.getProperty("os.name").toLowerCase();
               if (var1.getClickCount() == 2 && var2.contains("windows")) {
                  SyntheticaTitlePane.this.closeAction.actionPerformed(new ActionEvent(var1.getSource(), var1.getID(), "doubleClick"));
               }

            }
         });
         this.systemMenu = new JPopupMenu();
         this.systemMenu.putClientProperty("Synthetica.popupMenu.toplevel", false);
         this.addMenuItems(this.systemMenu);
      }

      this.titleLabel = new JLabel(" ");
      this.titleLabel.setName("RootPane.title");
      this.titleLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
      this.titleLabel.setFont(this.titleLabel.getFont().deriveFont(1));
      this.menuBarLabel = new JLabel(" ");
      this.menuBarLabel.setName("RootPane.menuBar");
      this.menuBarLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
      boolean var6 = this.showMenuBarInTitlePane();
      this.menuBarLabel.setVisible(var6);
      if (var6) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               SyntheticaTitlePane.this.menuBarLabel.setPreferredSize(SyntheticaTitlePane.this.rootPane.getJMenuBar().getPreferredSize());
            }
         });
      }

      this.createButtons();
      this.updateLayoutStyle(var4, var5);
      this.installListeners(this.rootPane);
      this.setComponentsActiveState(this.window.isActive());
      this.updateState();
   }

   public void updateLayoutStyle() {
      int var1 = this.rootPane.getWindowDecorationStyle();
      boolean var2 = SyntheticaLookAndFeel.getBoolean("Synthetica.dialog.icon.enabled", this.window);
      this.updateLayoutStyle(var1, var2);
   }

   private void updateLayoutStyle(int var1, boolean var2) {
      this.removeAll();
      LayoutStyle var3 = this.getLayoutStyle();
      if (var3 == SyntheticaTitlePane.LayoutStyle.REGULAR) {
         this.addControls(var1, var2);
      } else if (var3 == SyntheticaTitlePane.LayoutStyle.INLINEMENU) {
         this.addControls_inlineMenuLayout(var1, var2);
      } else if (var3 == SyntheticaTitlePane.LayoutStyle.INLINEMENU_LEADING) {
         this.addControls_inlineMenuLeadingLayout(var1, var2);
      } else if (var3 == SyntheticaTitlePane.LayoutStyle.SECONDARYMENU) {
         this.menuBarLabel.setVisible(true);
         if (this.userComponentContainer == null) {
            this.userComponentContainer = new UserComponentContainer();
         }

         this.addControls_secondaryMenuLayout(var1, var2);
      }

   }

   private void addControls(int var1, boolean var2) {
      int var3 = this.getXGap();
      if (this.useMACStyle && OS.getCurrentOS() == OS.Mac) {
         this.add(this.closeButton, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
         if (var1 == 1 && this.dialog == null) {
            this.add(this.iconifyButton, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.titleLabel, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, this.getTitleLabelAlignment(), 2, this.getTitleLabelInsets(var3), 0, 0));
         this.add(this.menuBarLabel, new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0, 10, 2, this.getMenuBarLabelInsets(var3), 0, 0));
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }
      } else {
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }

         this.add(this.titleLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, this.getTitleLabelAlignment(), 2, this.getTitleLabelInsets(var3), 0, 0));
         this.add(this.menuBarLabel, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, 10, 2, this.getMenuBarLabelInsets(var3), 0, 0));
         if (var1 == 1 && this.dialog == null) {
            this.add(this.iconifyButton, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(3, 0, 1, 2, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.closeButton, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
      }

   }

   private void addControls_inlineMenuLayout(int var1, boolean var2) {
      int var3 = this.getXGap();
      if (this.useMACStyle && OS.getCurrentOS() == OS.Mac) {
         this.add(this.closeButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
         if (var1 == 1 && this.dialog == null) {
            this.add(this.iconifyButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.titleLabel, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, this.getTitleLabelAlignment(), 2, this.getTitleLabelInsets(var3), 0, 0));
         this.add(this.menuBarLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, 10, 0, this.getMenuBarLabelInsets(var3), 0, 0));
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }
      } else {
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }

         this.add(this.titleLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, this.getTitleLabelAlignment(), 2, this.getTitleLabelInsets(var3), 0, 0));
         this.add(this.menuBarLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 10, 0, this.getMenuBarLabelInsets(var3), 0, 0));
         if (var1 == 1 && this.dialog == null) {
            this.add(this.iconifyButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.closeButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
      }

   }

   private void addControls_inlineMenuLeadingLayout(int var1, boolean var2) {
      int var3 = this.getXGap();
      if (this.useMACStyle && OS.getCurrentOS() == OS.Mac) {
         this.add(this.closeButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
         if (var1 == 1 && this.dialog == null) {
            this.add(this.iconifyButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.menuBarLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, 10, 0, this.getMenuBarLabelInsets(var3), 0, 0));
         this.add(this.titleLabel, new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0, this.getTitleLabelAlignment(), 2, this.getTitleLabelInsets(var3), 0, 0));
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }
      } else {
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }

         this.add(this.menuBarLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0, this.getMenuBarLabelInsets(var3), 0, 0));
         this.add(this.titleLabel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, this.getTitleLabelAlignment(), 2, this.getTitleLabelInsets(var3), 0, 0));
         if (var1 == 1 && this.dialog == null) {
            this.add(this.iconifyButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.closeButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
      }

   }

   private void addControls_secondaryMenuLayout(int var1, boolean var2) {
      int var3 = this.getXGap();
      if (this.useMACStyle && OS.getCurrentOS() == OS.Mac) {
         this.add(this.closeButton, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
         if (var1 == 1 && this.dialog == null) {
            this.add(this.iconifyButton, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.menuBarLabel, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, 10, 2, this.getMenuBarLabelInsets(var3), 0, 0));
         this.add(this.userComponentContainer, new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0, 10, 2, this.getUserComponentInsets(var3), 0, 0));
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }
      } else {
         if (var1 == 1 || var2) {
            this.add(this.menuButton, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, this.getMenuButtonAlignment(), 0, this.getMenuButtonInsets(var3), 0, 0));
         }

         this.add(this.menuBarLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 10, 2, this.getMenuBarLabelInsets(var3), 0, 0));
         boolean var4 = var1 == 1 && this.dialog == null;
         this.add(this.userComponentContainer, new GridBagConstraints(1, 1, var4 ? 4 : 2, 1, 1.0, 1.0, 10, 2, this.getUserComponentInsets(var3), 0, 0));
         if (var4) {
            this.add(this.iconifyButton, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, this.getIconifyButtonAlignment(), 0, this.getIconifyButtonInsets(var3), 0, 0));
            this.add(this.toggleButton, new GridBagConstraints(3, 0, 1, 2, 0.0, 0.0, this.getToggleButtonAlignment(), 0, this.getToggleButtonInsets(var3), 0, 0));
         }

         this.add(this.closeButton, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0, this.getCloseButtonAlignment(), 0, this.getCloseButtonInsets(var3), 0, 0));
      }

   }

   public void setUserComponent(JComponent var1) {
      if (this.userComponentContainer != null) {
         this.userComponentContainer.removeAll();
         this.userComponentContainer.add(var1);
      }

   }

   public JComponent getUserComponent() {
      return this.userComponentContainer != null && this.userComponentContainer.getComponentCount() != 0 ? (JComponent)this.userComponentContainer.getComponent(0) : null;
   }

   public LayoutStyle getLayoutStyle() {
      String var1 = SyntheticaLookAndFeel.getString("Synthetica.rootPane.titlePane.layoutStyle", this.window);
      return var1 == null ? SyntheticaTitlePane.LayoutStyle.REGULAR : SyntheticaTitlePane.LayoutStyle.valueOf(var1);
   }

   private void installListeners(JRootPane var1) {
      this.windowListener = new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            if (OS.getCurrentOS() == OS.Linux && SyntheticaTitlePane.this.dialog != null && SyntheticaTitlePane.this.dialog.isModal()) {
               SyntheticaTitlePane.this.dialog.setAlwaysOnTop(true);
            }

            if (SyntheticaTitlePane.this.rootPane.getWindowDecorationStyle() == 1 && SyntheticaTitlePane.this.dialog == null) {
               SyntheticaTitlePane.this.updateToggleButton();
               SyntheticaTitlePane.this.updateState();
            }

         }

         public void windowStateChanged(WindowEvent var1) {
            SyntheticaTitlePane.this.updateToggleButton();
            SyntheticaTitlePane.this.updateState();
         }

         public void windowActivated(WindowEvent var1) {
            SyntheticaTitlePane.this.setActive(true);
            SyntheticaTitlePane.this.selected = true;
         }

         public void windowDeactivated(WindowEvent var1) {
            SyntheticaTitlePane.this.setActive(false);
            SyntheticaTitlePane.this.selected = false;
         }
      };
      this.window.addWindowListener(this.windowListener);
      this.window.addWindowStateListener((WindowStateListener)this.windowListener);
      this.propertyChangeListener = new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1) {
            String var2 = var1.getPropertyName();
            if ("resizable".equals(var2)) {
               boolean var3 = (Boolean)var1.getNewValue();
               SyntheticaTitlePane.this.toggleButton.setEnabled(var3);
               SyntheticaTitlePane.this.systemMenu.removeAll();
               SyntheticaTitlePane.this.addMenuItems(SyntheticaTitlePane.this.systemMenu);
            } else if ("iconImage".equals(var2) && SyntheticaTitlePane.this.menuButton != null) {
               SyntheticaTitlePane.this.menuButton.setIcon(SyntheticaTitlePane.this.getIcon(SyntheticaTitlePane.this.rootPane));
            } else if ("title".equals(var2)) {
               SyntheticaTitlePane.this.repaint();
            } else if ("name".equals(var2)) {
               SyntheticaTitlePane.this.updateDefaults();
               SyntheticaTitlePane.this.updateLayoutStyle();
            }

         }
      };
      this.window.addPropertyChangeListener(this.propertyChangeListener);
      this.rootPanePropertyChangeListener = new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1) {
            if ("Synthetica.dialog.iconImage".equals(var1.getPropertyName()) && SyntheticaTitlePane.this.menuButton != null) {
               SyntheticaTitlePane.this.menuButton.setIcon(SyntheticaTitlePane.this.getIcon((JRootPane)var1.getSource()));
            }

         }
      };
      this.rootPane.addPropertyChangeListener(this.rootPanePropertyChangeListener);
      if (this.menuButton != null) {
         this.menuActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (SyntheticaTitlePane.this.systemMenu.isVisible()) {
                  SyntheticaTitlePane.this.systemMenu.setVisible(false);
               } else {
                  int var2 = SyntheticaTitlePane.this.getXGap();
                  int var3 = SyntheticaTitlePane.this.menuButton.getY() + SyntheticaTitlePane.this.menuButton.getHeight() + SyntheticaTitlePane.this.getTitleLabelInsets(var2).bottom;
                  boolean var4 = SyntheticaTitlePane.this.useMACStyle && OS.getCurrentOS() == OS.Mac;
                  if ((!SyntheticaTitlePane.this.isRTL() || var4) && (SyntheticaTitlePane.this.isRTL() || !var4)) {
                     SyntheticaTitlePane.this.systemMenu.show(SyntheticaTitlePane.this, SyntheticaTitlePane.this.menuButton.getX() - var2, var3);
                  } else {
                     SyntheticaTitlePane.this.systemMenu.show(SyntheticaTitlePane.this, SyntheticaTitlePane.this.menuButton.getX() + SyntheticaTitlePane.this.menuButton.getWidth() - SyntheticaTitlePane.this.systemMenu.getPreferredSize().width + var2, var3);
                  }

               }
            }
         };
         this.menuButton.addActionListener(this.menuActionListener);
      }

      this.layeredPaneContainerListener = new ContainerListener() {
         private ContainerListener menuBarContainerListener;
         private MouseInputListener menuBarMouseListener;

         public void componentAdded(ContainerEvent var1) {
            if (var1.getChild() instanceof JMenuBar && SyntheticaTitlePane.this.showMenuBarInTitlePane()) {
               JMenuBar var2 = (JMenuBar)var1.getChild();
               this.updatePlaceHolder(var2);
               SyntheticaTitlePane.this.menuBarLabel.setVisible(true);
               ((JLayeredPane)var1.getComponent()).setComponentZOrder(var2, 1);
               this.menuBarMouseListener = SyntheticaTitlePane.this.rootPaneUI.getMouseInputListener();
               var2.addMouseListener(this.menuBarMouseListener);
               var2.addMouseMotionListener(this.menuBarMouseListener);
               this.menuBarContainerListener = new ContainerListener() {
                  public void componentRemoved(ContainerEvent var1) {
                     updatePlaceHolder(var1.getComponent());
                  }

                  public void componentAdded(ContainerEvent var1) {
                     updatePlaceHolder(var1.getComponent());
                  }
               };
               var2.addContainerListener(this.menuBarContainerListener);
            }

         }

         private void updatePlaceHolder(Component var1) {
            SyntheticaTitlePane.this.menuBarLabel.setPreferredSize(var1.getPreferredSize());
            SyntheticaTitlePane.this.menuBarLabel.setMinimumSize(var1.getPreferredSize());
         }

         public void componentRemoved(ContainerEvent var1) {
            if (var1.getChild() instanceof JMenuBar) {
               JMenuBar var2 = (JMenuBar)var1.getChild();
               var2.removeMouseListener(this.menuBarMouseListener);
               var2.removeMouseMotionListener(this.menuBarMouseListener);
               var2.removeContainerListener(this.menuBarContainerListener);
               SyntheticaTitlePane.this.menuBarLabel.setVisible(false);
               this.menuBarMouseListener = null;
               this.menuBarContainerListener = null;
            }

         }
      };
      this.rootPane.getLayeredPane().addContainerListener(this.layeredPaneContainerListener);
   }

   void uninstallListeners(JRootPane var1) {
      this.window.removeWindowListener(this.windowListener);
      this.window.removeWindowStateListener((WindowStateListener)this.windowListener);
      this.window.removePropertyChangeListener(this.propertyChangeListener);
      var1.removePropertyChangeListener(this.rootPanePropertyChangeListener);
      var1.getLayeredPane().removeContainerListener(this.layeredPaneContainerListener);
      if (this.menuButton != null) {
         this.menuButton.removeActionListener(this.menuActionListener);
      }

   }

   private void updateDefaults() {
      this.setOpaque(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.opaque", this.window, true));
      int var1 = this.getXGap();
      GridBagLayout var2 = (GridBagLayout)this.getLayout();
      GridBagConstraints var3 = var2.getConstraints(this.titleLabel);
      var3.insets = this.getTitleLabelInsets(var1);
      var2.setConstraints(this.titleLabel, var3);
      this.titleLabel.revalidate();
      var3 = var2.getConstraints(this.menuBarLabel);
      var3.insets = this.getMenuBarLabelInsets(var1);
      var2.setConstraints(this.menuBarLabel, var3);
      this.menuBarLabel.revalidate();
      if (this.menuButton != null) {
         this.menuButton.setIcon(this.getIcon(this.rootPane));
         var3 = var2.getConstraints(this.menuButton);
         var3.insets = this.getMenuButtonInsets(var1);
         var3.anchor = this.getMenuButtonAlignment();
         var2.setConstraints(this.menuButton, var3);
         this.menuButton.revalidate();
      }

      if (this.iconifyButton != null) {
         var3 = var2.getConstraints(this.iconifyButton);
         var3.insets = this.getIconifyButtonInsets(var1);
         var3.anchor = this.getIconifyButtonAlignment();
         var2.setConstraints(this.iconifyButton, var3);
         this.iconifyButton.revalidate();
      }

      if (this.toggleButton != null) {
         var3 = var2.getConstraints(this.toggleButton);
         var3.insets = this.getToggleButtonInsets(var1);
         var3.anchor = this.getToggleButtonAlignment();
         var2.setConstraints(this.toggleButton, var3);
         this.toggleButton.revalidate();
      }

      var3 = var2.getConstraints(this.closeButton);
      var3.insets = this.getCloseButtonInsets(var1);
      var3.anchor = this.getCloseButtonAlignment();
      var2.setConstraints(this.closeButton, var3);
      this.closeButton.revalidate();
   }

   boolean showMenuBarInTitlePane() {
      return this.rootPane.getJMenuBar() != null && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.showMenuBarInTitlePane", this.window, false);
   }

   boolean clipMenuBarWidth() {
      return this.rootPane.getJMenuBar() != null && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.clipMenuBarWidth", this.window, false);
   }

   private int getXGap() {
      return SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.gap", this.window, 4);
   }

   private Insets getTitleLabelInsets(int var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.title.insets", this.window, new Insets(3, 0, 4, 0));
      return new Insets(var2.top, var2.left + var1, var2.bottom, var2.right + var1);
   }

   private Insets getMenuBarLabelInsets(int var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.menuBar.insets", this.window, false);
      return new Insets(var2.top, var2.left + var1, var2.bottom, var2.right + var1);
   }

   private Insets getUserComponentInsets(int var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.userComponent.insets", this.window, false);
      return new Insets(var2.top, var2.left + var1, var2.bottom, var2.right + var1);
   }

   private Insets getMenuButtonInsets(int var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.menuButton.insets", this.window, false);
      return this.useMACStyle && OS.getCurrentOS() == OS.Mac ? new Insets(var2.top, var2.right, var2.bottom, var2.left + var1) : new Insets(var2.top, var2.left + var1, var2.bottom, var2.right);
   }

   private Insets getIconifyButtonInsets(int var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.iconifyButton.insets", this.window, false);
      int var3;
      if (this.useMACStyle && OS.getCurrentOS() == OS.Mac) {
         var3 = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.iconifyButton.gap", this.window, 0);
         return new Insets(var2.top, var2.left + var3, var2.bottom, var2.right);
      } else {
         var3 = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.iconifyButton.gap", this.window, 0);
         return new Insets(var2.top, var2.left, var2.bottom, var2.right + var3);
      }
   }

   private Insets getToggleButtonInsets(int var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.toggleButton.insets", this.window, false);
      int var3;
      if (this.useMACStyle && OS.getCurrentOS() == OS.Mac) {
         var3 = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.toggleButton.gap", this.window, 0);
         return new Insets(var2.top, var2.left + var3, var2.bottom, var2.right);
      } else {
         var3 = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.toggleButton.gap", this.window, var1);
         return new Insets(var2.top, var2.left, var2.bottom, var2.right + var3);
      }
   }

   private Insets getCloseButtonInsets(int var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.closeButton.insets", this.window, false);
      return this.useMACStyle && OS.getCurrentOS() == OS.Mac ? new Insets(var2.top, var2.right + var1, var2.bottom, var2.left) : new Insets(var2.top, var2.left, var2.bottom, var2.right + var1);
   }

   private int getMenuButtonAlignment() {
      return SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.menuButton.alignment", this.window, 10);
   }

   private int getTitleLabelAlignment() {
      return SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.title.alignment", this.window, 10);
   }

   private int getIconifyButtonAlignment() {
      return SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.iconifyButton.alignment", this.window, 10);
   }

   private int getToggleButtonAlignment() {
      return SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.toggleButton.alignment", this.window, 10);
   }

   private int getCloseButtonAlignment() {
      return SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.closeButton.alignment", this.window, 10);
   }

   public JRootPane getRootPane() {
      return this.rootPane;
   }

   public Dimension getPreferredSize() {
      return this.isTitlePaneEnabled() ? super.getPreferredSize() : new Dimension(0, 0);
   }

   public Dimension getMinimumSize() {
      return this.isTitlePaneEnabled() ? super.getMinimumSize() : new Dimension(0, 0);
   }

   boolean isTitlePaneEnabled() {
      boolean var1 = this.rootPane.getClientProperty("Synthetica.titlePane.enabled") == null ? false : !(Boolean)this.rootPane.getClientProperty("Synthetica.titlePane.enabled");
      String var2 = this.rootPane.getParent().getClass().getName() + ".titlePane.enabled";
      return (UIManager.get(var2) == null || UIManager.getBoolean(var2)) && !var1;
   }

   private String getTitle() {
      if (this.frame != null) {
         return this.frame.getTitle();
      } else {
         return this.dialog != null ? this.dialog.getTitle() : null;
      }
   }

   private Icon getFrameIcon() {
      Image var1 = this.frame != null ? this.frame.getIconImage() : null;
      Icon var2 = null;
      if (var1 != null) {
         var2 = this.image2Icon(var1);
      } else {
         SynthStyle var3 = SynthLookAndFeel.getStyle(this.rootPane, Region.ROOT_PANE);
         SynthContext var4 = new SynthContext(this.rootPane, Region.ROOT_PANE, var3, 0);
         var2 = var3.getIcon(var4, "RootPane.icon");
      }

      return var2;
   }

   private Icon getIcon(JRootPane var1) {
      Icon var2 = this.getFrameIcon();
      if (var1.getWindowDecorationStyle() != 1 && var1 != null) {
         if (var1.getClientProperty("Synthetica.dialog.iconImage") != null) {
            Image var4 = (Image)var1.getClientProperty("Synthetica.dialog.iconImage");
            return this.image2Icon(var4);
         } else if (this.window instanceof JDialog && !JavaVersion.JAVA5 && ((JDialog)this.window).getIconImages().size() > 0) {
            return this.image2Icon((Image)((JDialog)var1.getParent()).getIconImages().get(0));
         } else {
            if (this.window != null && this.window.getOwner() != null) {
               Window var3 = this.window.getOwner();
               if (var3 instanceof JFrame) {
                  var1 = ((JFrame)var3).getRootPane();
               } else if (var3 instanceof JDialog) {
                  var1 = ((JDialog)var3).getRootPane();
               } else {
                  var1 = null;
               }

               if (var1 != null && var1.getUI() instanceof SyntheticaRootPaneUI && ((SyntheticaRootPaneUI)var1.getUI()).getTitlePane() != null) {
                  return ((SyntheticaTitlePane)((SyntheticaRootPaneUI)var1.getUI()).getTitlePane()).getIcon(var1);
               }
            }

            return var2;
         }
      } else {
         return var2;
      }
   }

   private Icon image2Icon(Image var1) {
      Object var2 = var1.getProperty("comment", (ImageObserver)null);
      return SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.menuButton.useOriginalImageSize", this.window) ? new ImageIcon(var1, var2 instanceof String ? (String)var2 : "") : new ImageIcon(var1.getScaledInstance(16, 16, 4), var2 instanceof String ? (String)var2 : "");
   }

   private void setActive(boolean var1) {
      this.setComponentsActiveState(var1);
      this.getRootPane().repaint();
   }

   private void setComponentsActiveState(boolean var1) {
      JInternalFrame var2 = this.iFrameDelegate;
      var2.setName(this.window.getName());
      SynthStyle var3 = SynthLookAndFeel.getStyleFactory().getStyle(var2, Region.INTERNAL_FRAME_TITLE_PANE);
      SynthContext var4 = new SynthContext(var2, Region.INTERNAL_FRAME_TITLE_PANE, var3, var1 ? 512 : 0);
      Font var5 = this.getFont();
      if (var5 == null || var5 instanceof UIResource) {
         var5 = var3.getFont(var4);
      }

      float var6 = (float)SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.title.fontSize", this.window);
      if (var6 == 0.0F) {
         var6 = (float)var5.getSize();
      } else {
         var6 = SyntheticaLookAndFeel.scaleFontSize(var6);
      }

      var5 = var5.deriveFont(var5.getStyle(), var6);
      this.titleLabel.setFont(var5);
      Color var7 = SyntheticaLookAndFeel.getColor("Synthetica.rootPane.titlePane.foreground" + (var1 ? ".selected" : ""), this.window, this.getForeground());
      if (var7 == null || var7 instanceof UIResource) {
         var7 = var3.getColor(var4, ColorType.FOREGROUND);
      }

      var7 = new Color(var7.getRGB());
      this.titleLabel.setForeground(var7);
      this.closeButton.putClientProperty("paintActive", var1);
      if (this.rootPane.getWindowDecorationStyle() == 1 && this.dialog == null) {
         this.iconifyButton.putClientProperty("paintActive", var1);
         this.toggleButton.putClientProperty("paintActive", var1);
      }

      this.updateToggleButton();
      if (this.iconifyButton != null) {
         this.iconifyButton.setIcon(var1 ? (Icon)this.iconifyButton.getClientProperty("Synthetica.iconifyIcon") : (Icon)this.iconifyButton.getClientProperty("Synthetica.iconifyIcon.inactive"));
      }

      if (this.closeButton != null) {
         this.closeButton.setIcon(var1 ? (Icon)this.closeButton.getClientProperty("Synthetica.closeIcon") : (Icon)this.closeButton.getClientProperty("Synthetica.closeIcon.inactive"));
      }

   }

   private boolean isFrameResizable() {
      return this.frame != null && this.frame.isResizable();
   }

   private boolean isFrameMaximized() {
      return this.frame != null && (this.frame.getExtendedState() & 6) == 6;
   }

   private void addMenuItems(JPopupMenu var1) {
      JMenuItem var2 = var1.add(this.restoreAction);
      var2.setText(SyntheticaLookAndFeel.getString("InternalFrameTitlePane.restoreButtonText", this.window));
      var2.setMnemonic('R');
      var2.setEnabled(this.isFrameResizable());
      var2 = var1.add(this.iconifyAction);
      var2.setText(SyntheticaLookAndFeel.getString("InternalFrameTitlePane.minimizeButtonText", this.window));
      var2.setMnemonic('n');
      var2.setEnabled(this.frame != null);
      if (Toolkit.getDefaultToolkit().isFrameStateSupported(6)) {
         var2 = var1.add(this.maximizeAction);
         var2.setText(SyntheticaLookAndFeel.getString("InternalFrameTitlePane.maximizeButtonText", this.window));
         var2.setMnemonic('x');
         var2.setEnabled(this.isFrameResizable());
      }

      var1.addSeparator();
      var2 = var1.add(this.closeAction);
      var2.setText(SyntheticaLookAndFeel.getString("InternalFrameTitlePane.closeButtonText", this.window));
      var2.setMnemonic('C');
      if (this.closeButton != null) {
         var2.setEnabled(this.closeButton.isVisible() & this.closeButton.isEnabled());
      }

   }

   private JButton createTitlePaneButton(String var1) {
      JButton var2 = new JButton();
      var2.setName(var1);
      var2.setFocusPainted(false);
      var2.setFocusable(false);
      var2.setOpaque(false);
      var2.setBorder(BorderFactory.createEmptyBorder());
      return var2;
   }

   private void createButtons() {
      MouseAdapter var1 = new MouseAdapter() {
         public void mouseEntered(MouseEvent var1) {
            JButton var2 = (JButton)var1.getSource();
            String var3 = "Synthetica." + var2.getClientProperty("Synthetica.buttonID") + "Icon.hover";
            var2.setIcon((Icon)var2.getClientProperty(var3));
         }

         public void mouseExited(MouseEvent var1) {
            JButton var2 = (JButton)var1.getSource();
            String var3 = "Synthetica." + var2.getClientProperty("Synthetica.buttonID") + "Icon";
            if (!SyntheticaTitlePane.this.window.isActive()) {
               var3 = var3 + ".inactive";
            }

            var2.setIcon((Icon)var2.getClientProperty(var3));
         }
      };
      SynthStyle var2 = SynthLookAndFeel.getStyle(this.rootPane, Region.ROOT_PANE);
      SynthContext var3 = new SynthContext(this.rootPane, Region.ROOT_PANE, var2, 0);
      Icon var4 = var2.getIcon(var3, "RootPane.closeIcon");
      Icon var5 = var2.getIcon(var3, "RootPane.iconifyIcon");
      Icon var6 = var2.getIcon(var3, "RootPane.maximizeIcon");
      Icon var7 = var2.getIcon(var3, "RootPane.minimizeIcon");
      Icon var8 = var2.getIcon(var3, "RootPane.closeIcon.inactive") == null ? var4 : var2.getIcon(var3, "RootPane.closeIcon.inactive");
      Icon var9 = var2.getIcon(var3, "RootPane.iconifyIcon.inactive") == null ? var5 : var2.getIcon(var3, "RootPane.iconifyIcon.inactive");
      Icon var10 = var2.getIcon(var3, "RootPane.maximizeIcon.inactive") == null ? var6 : var2.getIcon(var3, "RootPane.maximizeIcon.inactive");
      Icon var11 = var2.getIcon(var3, "RootPane.minimizeIcon.inactive") == null ? var7 : var2.getIcon(var3, "RootPane.minimizeIcon.inactive");
      var3 = new SynthContext(this.rootPane, Region.ROOT_PANE, var2, 2);
      Icon var12 = var2.getIcon(var3, "RootPane.closeIcon");
      Icon var13 = var2.getIcon(var3, "RootPane.iconifyIcon");
      Icon var14 = var2.getIcon(var3, "RootPane.maximizeIcon");
      Icon var15 = var2.getIcon(var3, "RootPane.minimizeIcon");
      var3 = new SynthContext(this.rootPane, Region.ROOT_PANE, var2, 4);
      Icon var16 = var2.getIcon(var3, "RootPane.closeIcon") == var4 ? null : var2.getIcon(var3, "RootPane.closeIcon");
      Icon var17 = var2.getIcon(var3, "RootPane.iconifyIcon") == var5 ? null : var2.getIcon(var3, "RootPane.iconifyIcon");
      Icon var18 = var2.getIcon(var3, "RootPane.maximizeIcon") == var6 ? null : var2.getIcon(var3, "RootPane.maximizeIcon");
      Icon var19 = var2.getIcon(var3, "RootPane.minimizeIcon") == var7 ? null : var2.getIcon(var3, "RootPane.minimizeIcon");
      this.closeButton = this.createTitlePaneButton("RootPane.titlePane.closeButton");
      this.closeButton.putClientProperty("Synthetica.buttonID", "close");
      this.closeButton.putClientProperty("Synthetica.closeIcon", var4);
      this.closeButton.putClientProperty("Synthetica.closeIcon.hover", var12);
      this.closeButton.putClientProperty("Synthetica.closeIcon.inactive", var8);
      this.closeButton.setAction(this.closeAction);
      this.closeButton.getAccessibleContext().setAccessibleName("Close");
      this.closeButton.setIcon(var4);
      this.closeButton.setPressedIcon(var16);
      this.closeButton.addMouseListener(var1);
      CloseButtonStateListener var20 = new CloseButtonStateListener((CloseButtonStateListener)null);
      this.closeButton.addPropertyChangeListener(var20);
      this.closeButton.addComponentListener(var20);
      if (this.rootPane.getWindowDecorationStyle() == 1 && this.dialog == null) {
         this.iconifyButton = this.createTitlePaneButton("RootPane.titlePane.iconifyButton");
         this.iconifyButton.putClientProperty("Synthetica.buttonID", "iconify");
         this.iconifyButton.putClientProperty("Synthetica.iconifyIcon", var5);
         this.iconifyButton.putClientProperty("Synthetica.iconifyIcon.hover", var13);
         this.iconifyButton.putClientProperty("Synthetica.iconifyIcon.inactive", var9);
         this.iconifyButton.setAction(this.iconifyAction);
         this.iconifyButton.getAccessibleContext().setAccessibleName("Iconify");
         this.iconifyButton.setIcon(var5);
         this.iconifyButton.setPressedIcon(var17);
         this.iconifyButton.addMouseListener(var1);
         this.toggleButton = this.createTitlePaneButton("RootPane.titlePane.toggleButton");
         this.toggleButton.putClientProperty("Synthetica.maximizeIcon", var6);
         this.toggleButton.putClientProperty("Synthetica.maximizeIcon.hover", var14);
         this.toggleButton.putClientProperty("Synthetica.maximizeIcon.pressed", var18);
         this.toggleButton.putClientProperty("Synthetica.maximizeIcon.inactive", var10);
         this.toggleButton.putClientProperty("Synthetica.minimizeIcon", var7);
         this.toggleButton.putClientProperty("Synthetica.minimizeIcon.hover", var15);
         this.toggleButton.putClientProperty("Synthetica.minimizeIcon.pressed", var19);
         this.toggleButton.putClientProperty("Synthetica.minimizeIcon.inactive", var11);
         this.updateToggleButton();
         this.toggleButton.addMouseListener(var1);
      }

   }

   private void updateToggleButton() {
      if (this.toggleButton != null) {
         boolean var1 = this.window.isActive();
         Icon var2 = null;
         Icon var3 = null;
         if (!this.isFrameMaximized()) {
            this.toggleButton.setAction(this.maximizeAction);
            this.toggleButton.getAccessibleContext().setAccessibleName("Maximize");
            this.toggleButton.putClientProperty("Synthetica.buttonID", "maximize");
            if (!var1) {
               var2 = (Icon)this.toggleButton.getClientProperty("Synthetica.maximizeIcon.inactive");
            } else {
               var2 = (Icon)this.toggleButton.getClientProperty("Synthetica.maximizeIcon");
            }

            var3 = (Icon)this.toggleButton.getClientProperty("Synthetica.maximizeIcon.pressed");
         } else {
            this.toggleButton.setAction(this.restoreAction);
            this.toggleButton.getAccessibleContext().setAccessibleName("Restore");
            this.toggleButton.putClientProperty("Synthetica.buttonID", "minimize");
            if (!var1) {
               var2 = (Icon)this.toggleButton.getClientProperty("Synthetica.minimizeIcon.inactive");
            } else {
               var2 = (Icon)this.toggleButton.getClientProperty("Synthetica.minimizeIcon");
            }

            var3 = (Icon)this.toggleButton.getClientProperty("Synthetica.minimizeIcon.pressed");
         }

         this.toggleButton.setIcon(var2);
         this.toggleButton.setPressedIcon(var3);
         this.toggleButton.setEnabled(this.isFrameResizable());
      }
   }

   public void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      SynthContext var2 = SyntheticaLookAndFeel.createContext(this.rootPane, Region.ROOT_PANE, this.selected ? 512 : 0);
      ((SyntheticaPainter)SyntheticaPainter.getInstance()).paintRootPaneTitlePaneBackground(var2, var1, 0, 0, this.getWidth(), this.getHeight());
      Rectangle var3 = this.getControlButtonsBounds();
      if (var3.width > 0 && var3.height > 0) {
         ((SyntheticaPainter)SyntheticaPainter.getInstance()).paintRootPaneButtonAreaBackground(var2, var1, var3.x, var3.y, var3.width, var3.height);
      }

      String var4 = this.getTitle();
      if (var4 != null && var4.length() != 0) {
         GridBagLayout var5 = (GridBagLayout)this.getLayout();
         Insets var6 = var5.getConstraints(this.titleLabel).insets;
         FontMetrics var7 = this.getFontMetrics(this.titleLabel.getFont());
         boolean var8 = OS.getCurrentOS() == OS.Mac && this.useMACStyle;
         boolean var9 = SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.title.center", this.window) || var8;
         int var10 = this.getXGap();
         Rectangle var11 = this.getMenuButtonBounds();
         if (this.menuButton != null) {
            var11.width += var5.getConstraints(this.menuButton).insets.right;
         }

         Rectangle var12 = this.getControlButtonsBounds();
         boolean var13 = this.isRTL();
         int var14 = var7.stringWidth(var4);
         int var15 = var7.getHeight();
         int var16 = this.getTitleLabelAlignment() == 10 ? (this.getHeight() - var15 + var6.top - var6.bottom) / 2 : this.titleLabel.getY();
         if (SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.showMenuBarInTitlePane", this.window, false)) {
            var16 = this.titleLabel.getY() + this.titleLabel.getInsets().top;
         }

         int var17 = !var8 && !var13 ? var11.x + var11.width + var6.left : var12.x + var12.width + var6.right;
         int var18 = !var8 && !var13 ? var12.x - var14 - var6.right : var11.x - var14 - var6.left;
         var18 = Math.max(var17, var18);
         int var19 = var13 ? var18 : var17;
         var19 = var9 ? (var13 ? Math.min(var19, this.getWidth() / 2 - var7.stringWidth(var4) / 2) : Math.max(var19, this.getWidth() / 2 - var7.stringWidth(var4) / 2)) : var19;
         if (!var8) {
            var19 = var13 ? Math.min(var19, var18) : Math.max(var19, var17);
            var19 = var13 ? Math.max(var19, var17) : Math.min(var19, var18);
         }

         Rectangle var20 = this.getBounds();
         SynthStyle var21 = SynthLookAndFeel.getStyle(this.iFrameDelegate, Region.INTERNAL_FRAME_TITLE_PANE);
         var2 = new SynthContext(this.iFrameDelegate, Region.INTERNAL_FRAME_TITLE_PANE, var21, this.selected ? 512 : 0);
         var20.width -= var11.width + var12.width + var10 * 2 + var6.left + var6.right;
         double var22;
         if (this.showMenuBarInTitlePane() && this.getLayoutStyle() == SyntheticaTitlePane.LayoutStyle.INLINEMENU) {
            if (var13) {
               var22 = Math.max(0.0, this.menuBarLabel.getBounds().getX() + this.menuBarLabel.getBounds().getWidth() + (double)var6.right - (double)var19);
               var19 = (int)((double)var19 + var22);
               var20.width = (int)((double)var20.width - this.menuBarLabel.getBounds().getWidth());
            } else {
               var20.width = (int)((double)var20.width - (this.menuBarLabel.getBounds().getWidth() + (double)(var9 ? var19 - var11.width - var10 - var6.left : 0)));
            }
         } else if (this.showMenuBarInTitlePane() && this.getLayoutStyle() == SyntheticaTitlePane.LayoutStyle.INLINEMENU_LEADING) {
            if (!var13) {
               var22 = Math.max(0.0, this.menuBarLabel.getBounds().getX() + this.menuBarLabel.getBounds().getWidth() + (double)var6.right - (double)var19);
               var19 = (int)((double)var19 + var22);
               var20.width = (int)((double)var20.width - this.menuBarLabel.getBounds().getWidth());
            } else {
               var20.width = (int)((double)var20.width - (this.menuBarLabel.getBounds().getWidth() + (double)(var9 ? var19 - var11.width - var10 - var6.left : 0)));
            }
         }

         String var30 = var21.getGraphicsUtils(var2).layoutText(var2, var7, var4, (Icon)null, 0, 0, 0, 0, var20, new Rectangle(0, 0), new Rectangle(0, 0), 0);
         if ("...".equals(var30)) {
            var30 = "";
         }

         boolean var23 = SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.title.visible", this.window, true);
         boolean var24 = this.rootPane.getClientProperty("Synthetica.logoRenderer") != null;
         if (var23 && (!var24 || (!var24 || this.rootPane.getClientProperty("Synthetica.paintTitle") != null) && (Boolean)this.rootPane.getClientProperty("Synthetica.paintTitle"))) {
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.dropShadow", this.window) && this.selected) {
               BufferedImage var25 = new BufferedImage(var14, var15, 2);
               Graphics2D var26 = var25.createGraphics();
               var26.setFont(this.titleLabel.getFont());
               var26.drawString(var30, 0, var7.getAscent());
               var26.dispose();
               DropShadow var27 = new DropShadow(var25);
               var27.setDistance(SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.dropShadow.distance", this.window, -5));
               var27.setShadowColor(SyntheticaLookAndFeel.getColor("Synthetica.rootPane.titlePane.dropShadow.color", this.window, var27.getShadowColor()));
               var27.setQuality(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.dropShadow.highQuality", this.window, var27.getHighQuality()));
               var27.setShadowOpacity((float)SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.dropShadow.opacity", this.window, (int)(var27.getShadowOpacity() * 100.0F)) / 100.0F);
               var27.setShadowSize(SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.dropShadow.size", this.window, var27.getShadowSize()));
               int var28 = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.dropShadow.xOffset", this.window, 0);
               int var29 = SyntheticaLookAndFeel.getInt("Synthetica.rootPane.titlePane.dropShadow.yOffset", this.window, 0);
               var27.paintShadow(var1, var19 + var28, var16 + var29);
            }

            var1.setFont(this.titleLabel.getFont());
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.title.etchedTop", this.window)) {
               var1.setColor(Color.BLACK);
               var21.getGraphicsUtils(var2).paintText(var2, var1, var30, var19, var16 - 1, -2);
            }

            if (SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.title.etchedBottom", this.window)) {
               var1.setColor(Color.WHITE);
               var21.getGraphicsUtils(var2).paintText(var2, var1, var30, var19, var16 + 1, -2);
            }

            Color var31 = SyntheticaLookAndFeel.getColor("Synthetica.rootPane.titlePane.foreground" + (this.window.isActive() ? ".selected" : ""), this.window, var21.getColor(var2, ColorType.FOREGROUND));
            var1.setColor(var31);
            var21.getGraphicsUtils(var2).paintText(var2, var1, var30, var19, var16, -2);
         }

      }
   }

   public Rectangle getControlButtonsBounds() {
      Rectangle var1 = this.closeButton.getBounds();
      if (this.useMACStyle && OS.getCurrentOS() == OS.Mac) {
         if (this.toggleButton != null) {
            var1.width = this.toggleButton.getBounds().x + this.toggleButton.getBounds().width - var1.x;
         }
      } else if (this.iconifyButton != null) {
         Rectangle var2 = this.iconifyButton.getBounds();
         if (this.isRTL()) {
            var1.width = var2.x + var2.width - var1.x;
         } else {
            var1.width = var1.x + var1.width - var2.x;
            var1.x = var2.x;
         }
      }

      return var1;
   }

   public Rectangle getMenuButtonBounds() {
      Rectangle var1 = this.menuButton == null ? new Rectangle(0, 0, 0, 0) : this.menuButton.getBounds();
      if (this.menuButton == null) {
         var1.x = this.isRTL() ? this.getBounds().width - 1 : 0;
         var1.y = this.getBounds().y;
      }

      return var1;
   }

   private void close() {
      this.window.dispatchEvent(new WindowEvent(this.window, 201));
   }

   void iconify() {
      int var1 = this.frame.getExtendedState();
      this.frame.setExtendedState(var1 | 1);
      this.updateState();
   }

   void maximize() {
      int var1 = this.frame.getExtendedState();
      this.rootPaneUI.setMaximizedBounds(this.frame);
      this.frame.setExtendedState(var1 | 6);
      this.updateState();
      if (OS.getCurrentOS() == OS.Mac && isFullScreenOnMacSupported(this.frame)) {
         this.frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(this.frame);
      }

   }

   void restore() {
      if (OS.getCurrentOS() == OS.Mac && isFullScreenOnMacSupported(this.frame)) {
         this.frame.getGraphicsConfiguration().getDevice().setFullScreenWindow((Window)null);
      }

      int var1 = this.frame.getExtendedState();
      if ((var1 & 1) == 1) {
         this.frame.setExtendedState(var1 ^ 1);
      } else if ((var1 & 6) == 6) {
         this.frame.setMaximizedBounds((Rectangle)null);
         this.frame.setExtendedState(var1 ^ 6);
      }

      this.updateState();
   }

   private void updateState() {
      if (this.frame != null) {
         if (!this.isFrameResizable()) {
            this.restoreAction.setEnabled(false);
            this.maximizeAction.setEnabled(false);
            this.iconifyAction.setEnabled(true);
         } else {
            switch (this.frame.getExtendedState()) {
               case 1:
                  this.restoreAction.setEnabled(true);
                  this.maximizeAction.setEnabled(true);
                  this.iconifyAction.setEnabled(false);
                  break;
               case 6:
                  this.restoreAction.setEnabled(true);
                  this.maximizeAction.setEnabled(false);
                  this.iconifyAction.setEnabled(true);
                  break;
               default:
                  this.restoreAction.setEnabled(false);
                  this.maximizeAction.setEnabled(true);
                  this.iconifyAction.setEnabled(true);
            }

            if ((this.frame.getExtendedState() & 1) != 0) {
               ExtKeyEventProcessor.resetWinMetaKey();
            }

         }
      }
   }

   static boolean isFullScreenOnMacSupported(Frame var0) {
      GraphicsDevice var1 = var0.getGraphicsConfiguration().getDevice();
      return var1.isFullScreenSupported() && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.fullScreenOnMaximizeMacOnly.enabled", var0, false);
   }

   public ComponentOrientation getComponentOrientation() {
      return this.window == null ? this.getRootPane().getComponentOrientation() : this.window.getComponentOrientation();
   }

   private boolean isRTL() {
      return !this.getComponentOrientation().isLeftToRight();
   }

   private class CloseButtonStateListener implements PropertyChangeListener, ComponentListener {
      private CloseButtonStateListener() {
      }

      public void componentResized(ComponentEvent var1) {
      }

      public void componentMoved(ComponentEvent var1) {
      }

      public void componentShown(ComponentEvent var1) {
         this.updateSystemMenu();
      }

      public void componentHidden(ComponentEvent var1) {
         this.updateSystemMenu();
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("enabled".equals(var2)) {
            this.updateSystemMenu();
         }

      }

      private void updateSystemMenu() {
         if (SyntheticaTitlePane.this.systemMenu != null) {
            SyntheticaTitlePane.this.systemMenu.removeAll();
            SyntheticaTitlePane.this.addMenuItems(SyntheticaTitlePane.this.systemMenu);
         }

      }

      // $FF: synthetic method
      CloseButtonStateListener(CloseButtonStateListener var2) {
         this();
      }
   }

   public static enum LayoutStyle {
      REGULAR,
      INLINEMENU,
      INLINEMENU_LEADING,
      SECONDARYMENU;
   }

   private static class UserComponentContainer extends JComponent {
      public UserComponentContainer() {
         this.setLayout(new BorderLayout());
      }
   }
}
