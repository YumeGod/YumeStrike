package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.styles.ButtonStyle;
import de.javasoft.plaf.synthetica.styles.CheckBoxStyle;
import de.javasoft.plaf.synthetica.styles.ComboBoxStyle;
import de.javasoft.plaf.synthetica.styles.EditorPaneStyle;
import de.javasoft.plaf.synthetica.styles.FormattedTextFieldStyle;
import de.javasoft.plaf.synthetica.styles.LabelStyle;
import de.javasoft.plaf.synthetica.styles.ListStyle;
import de.javasoft.plaf.synthetica.styles.MenuItemStyle;
import de.javasoft.plaf.synthetica.styles.MenuStyle;
import de.javasoft.plaf.synthetica.styles.PasswordFieldStyle;
import de.javasoft.plaf.synthetica.styles.PopupMenuStyle;
import de.javasoft.plaf.synthetica.styles.RadioButtonStyle;
import de.javasoft.plaf.synthetica.styles.ScrollPaneStyle;
import de.javasoft.plaf.synthetica.styles.SpinnerStyle;
import de.javasoft.plaf.synthetica.styles.TabbedPaneContentStyle;
import de.javasoft.plaf.synthetica.styles.TabbedPaneStyle;
import de.javasoft.plaf.synthetica.styles.TableStyle;
import de.javasoft.plaf.synthetica.styles.TextAreaStyle;
import de.javasoft.plaf.synthetica.styles.TextFieldStyle;
import de.javasoft.plaf.synthetica.styles.TextPaneStyle;
import de.javasoft.plaf.synthetica.styles.ToggleButtonStyle;
import de.javasoft.plaf.synthetica.styles.ToolBarButtonStyle;
import de.javasoft.plaf.synthetica.styles.ToolBarSeparatorStyle;
import de.javasoft.plaf.synthetica.styles.ToolBarStyle;
import de.javasoft.plaf.synthetica.styles.ToolTipStyle;
import de.javasoft.plaf.synthetica.styles.ViewportStyle;
import de.javasoft.util.JavaVersion;
import de.javasoft.util.OS;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DesktopManager;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeCellRenderer;
import sun.swing.DefaultLookup;
import sun.swing.plaf.synth.DefaultSynthStyle;

public class StyleFactory extends SynthStyleFactory {
   private SynthStyleFactory synthStyleFactory;
   private SyntheticaStyleFactory syntheticaStyleFactory;
   private PropertyChangeListener styleUpdater;
   private Boolean styleNameSupportEnabled;
   private ComponentPropertyStore componentPropertyStore;
   private boolean prepareMetalLAFSwitch = false;
   private CellRendererHoverListener cellRendererHoverListener;
   private static boolean menuToolTipEnabled;
   private static boolean menuItemToolTipEnabled;
   private static boolean componentPropertyStoreEnabled;
   private static int cleanerThreadDelay;

   static {
      String var0;
      try {
         var0 = System.getProperty("synthetica.componentPropertyStoreEnabled");
         componentPropertyStoreEnabled = var0 == null ? true : Boolean.parseBoolean(var0);
      } catch (AccessControlException var2) {
         componentPropertyStoreEnabled = true;
      }

      try {
         var0 = System.getProperty("synthetica.cleanerThreadDelay");
         cleanerThreadDelay = var0 == null ? 30000 : Integer.parseInt(var0);
      } catch (AccessControlException var1) {
         cleanerThreadDelay = 30000;
      }

   }

   public StyleFactory(SynthStyleFactory var1) {
      this.synthStyleFactory = var1;
      this.componentPropertyStore = new ComponentPropertyStore();
   }

   void uninitialize() {
      this.componentPropertyStore.enabled = false;
      this.restoreAllComponentProperties();
      this.componentPropertyStore.stop();
   }

   public ComponentPropertyStore getComponentPropertyStore() {
      return this.componentPropertyStore;
   }

   public SynthStyle getStyle(JComponent var1, Region var2) {
      String var3 = SyntheticaLookAndFeel.getStyleName(var1);
      this.installStyleUpdater(var1);
      SynthStyle var4 = null;
      if (var3 != null && (var2.equals(Region.TABBED_PANE_CONTENT) || var2.equals(Region.TABBED_PANE_TAB) || var2.equals(Region.TABBED_PANE_TAB_AREA) || var2.equals(Region.INTERNAL_FRAME_TITLE_PANE))) {
         PropertyChangeListener[] var5 = var1.getPropertyChangeListeners();
         PropertyChangeListener[] var9 = var5;
         int var8 = var5.length;

         PropertyChangeListener var6;
         int var7;
         for(var7 = 0; var7 < var8; ++var7) {
            var6 = var9[var7];
            var1.removePropertyChangeListener(var6);
         }

         var1.setName(var2.getName() + "." + var3);
         if (var1.getClientProperty("Synthetica.style") != null) {
            var1.putClientProperty("Synthetica.style", var2.getName() + "." + var3);
         }

         var4 = this.getStyle(var1, var2, this.getSynthStyle(var1, var2), true);
         var1.setName(var3);
         if (var1.getClientProperty("Synthetica.style") != null) {
            var1.putClientProperty("Synthetica.style", var3);
         }

         var9 = var5;
         var8 = var5.length;

         for(var7 = 0; var7 < var8; ++var7) {
            var6 = var9[var7];
            var1.addPropertyChangeListener(var6);
         }
      } else {
         var4 = this.getStyle(var1, var2, this.getSynthStyle(var1, var2), false);
      }

      return var4;
   }

   private void installStyleUpdater(JComponent var1) {
      if (this.isSyntheticaStyleNameSupportEnabled() && JavaVersion.JAVA6 && !this.eventListenerExists(this.styleUpdater, var1.getPropertyChangeListeners())) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
         var1.addPropertyChangeListener(this.styleUpdater);
      }

   }

   private SynthStyle getSynthStyle(JComponent var1, Region var2) {
      if (this.isSyntheticaStyleNameSupportEnabled()) {
         if (this.syntheticaStyleFactory == null) {
            this.syntheticaStyleFactory = new SyntheticaStyleFactory(this.synthStyleFactory);
         }

         return this.syntheticaStyleFactory.getStyle(var1, var2);
      } else {
         return this.synthStyleFactory.getStyle(var1, var2);
      }
   }

   boolean isSyntheticaStyleNameSupportEnabled() {
      if (this.styleNameSupportEnabled == null) {
         this.styleNameSupportEnabled = false;
         if (JavaVersion.JAVA6U10_OR_ABOVE && SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
            this.styleNameSupportEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.styleNameSupportEnabled", (Component)null, true);
            if (JavaVersion.JAVA6) {
               this.styleUpdater = new PropertyChangeListener() {
                  public void propertyChange(PropertyChangeEvent var1) {
                     if ("Synthetica.style".equals(var1.getPropertyName())) {
                        JComponent var2 = (JComponent)var1.getSource();
                        var2.putClientProperty("Nimbus.Overrides", "" + var1.getNewValue());
                     }

                  }
               };
            }
         }
      }

      return this.styleNameSupportEnabled;
   }

   private SynthStyle getStyle(JComponent var1, Region var2, SynthStyle var3, boolean var4) {
      String var5 = var1.getName();
      if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
         if (SyntheticaLookAndFeel.getFont() != null) {
            boolean var6 = !SyntheticaLookAndFeel.getBoolean("Synthetica.font.disabled", var1) & SyntheticaLookAndFeel.getBoolean("Synthetica.font.enabled", var1, true);
            if (var6) {
               Font var7 = ((DefaultSynthStyle)var3).getStateInfo(0).getFont();
               Font var8 = SyntheticaLookAndFeel.getFont();
               FontUIResource var17 = new FontUIResource(var8.deriveFont(var7 == null ? 0 : var7.getStyle()));
               ((DefaultSynthStyle)var3).getStateInfo(0).setFont(var17);
               if (var1.getBorder() instanceof TitledBorder) {
                  ((TitledBorder)var1.getBorder()).setTitleFont(var17);
               }
            }
         } else if (SyntheticaLookAndFeel.getFont() == null) {
            Font var12 = ((DefaultSynthStyle)var3).getStateInfo(0).getFont();
            Object var13;
            if (var12 == null) {
               var13 = new FontUIResource("Tahoma", 0, 11);
            } else {
               var13 = var12.deriveFont(0);
            }

            String var14 = UIManager.getString("Synthetica.font.resource");
            if (var14 != null) {
               try {
                  float var20 = (float)((Font)var13).getSize();
                  var12 = Font.createFont(0, this.getClass().getResourceAsStream(var14));
                  var13 = var12.deriveFont(var20);
               } catch (Exception var11) {
                  throw new RuntimeException(var11);
               }
            }

            SyntheticaLookAndFeel.setFont((Font)var13, true);
         }
      }

      if (var5 != null && (var5.startsWith("ComboBox.arrowButton") || var5.startsWith("ComboBox.textField")) && SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.hoverAndPressed.enabled", var1) && !this.eventListenerExists(var1.getMouseListeners())) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
         var1.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent var1) {
               Component var2 = (Component)var1.getSource();
               JComponent var3 = (JComponent)var2.getParent();
               var3.putClientProperty("Synthetica.MOUSE_OVER", true);
               var3.dispatchEvent(var1);
            }

            public void mouseExited(MouseEvent var1) {
               Component var2 = (Component)var1.getSource();
               JComponent var3 = (JComponent)var2.getParent();
               var3.putClientProperty("Synthetica.MOUSE_OVER", false);
               var3.dispatchEvent(var1);
            }

            public void mousePressed(MouseEvent var1) {
               Component var2 = (Component)var1.getSource();
               JComponent var3 = (JComponent)var2.getParent();
               if (SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.hoverAndPressed.enabled", var3)) {
                  var3.repaint();
               }

            }

            public void mouseReleased(MouseEvent var1) {
               Component var2 = (Component)var1.getSource();
               JComponent var3 = (JComponent)var2.getParent();
               if (SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.hoverAndPressed.enabled", var3)) {
                  var3.repaint();
               }

            }
         });
      }

      if (var5 != null && (var5.startsWith("Spinner.nextButton") || var5.startsWith("Spinner.previousButton") || var5.startsWith("Spinner.formattedTextField")) && SyntheticaLookAndFeel.getBoolean("Synthetica.spinner.hoverAndPressed.enabled", var1) && !this.eventListenerExists(var1.getMouseListeners())) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
         var1.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent var1) {
               JSpinner var2 = this.getSpinner(var1);
               var2.putClientProperty("Synthetica.MOUSE_OVER", true);
               var2.dispatchEvent(var1);
            }

            public void mouseExited(MouseEvent var1) {
               JSpinner var2 = this.getSpinner(var1);
               var2.putClientProperty("Synthetica.MOUSE_OVER", false);
               var2.dispatchEvent(var1);
            }

            public void mousePressed(MouseEvent var1) {
            }

            public void mouseReleased(MouseEvent var1) {
            }

            private JSpinner getSpinner(MouseEvent var1) {
               Component var2 = (Component)var1.getSource();
               String var3 = var2.getName();
               return var3 != null && var3.startsWith("Spinner.formattedTextField") ? (JSpinner)var2.getParent().getParent() : (JSpinner)var2.getParent();
            }
         });
      }

      Dimension var19;
      int var22;
      if (var2 == Region.ARROW_BUTTON) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
         if (var1.getClass().getName().endsWith("SynthScrollableTabButton")) {
            var19 = SyntheticaLookAndFeel.getDim("Synthetica.tabbedPane.arrowButton.size", var1);
            if (var19 != null) {
               var1.setPreferredSize(var19);
            }
         }

         if (var5 != null && var5.startsWith("Spinner.")) {
            var22 = SyntheticaLookAndFeel.getInt("Synthetica.spinner.arrowButton.width", var1);
            if (var22 > 0 && var1.getPreferredSize() != null) {
               var1.setPreferredSize(new Dimension(var22, var1.getPreferredSize().height));
            }
         } else if (var5 != null && var5.startsWith("ScrollBar.button") && SyntheticaLookAndFeel.getBoolean("Synthetica.scrollBarTrack.hoverOnButtons.enabled", var1) && !this.eventListenerExists(var1.getMouseListeners())) {
            this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
            var1.addMouseListener(new MouseAdapter() {
               public void mouseEntered(MouseEvent var1) {
                  JButton var2 = (JButton)var1.getSource();
                  var2.getParent().repaint();
               }

               public void mouseExited(MouseEvent var1) {
                  JButton var2 = (JButton)var1.getSource();
                  var2.getParent().repaint();
               }
            });
         }
      } else {
         int var15;
         if (var2 == Region.BUTTON) {
            this.componentPropertyStore.storeComponentProperty(var1, "SYCP_BUTTON_HALIGN");
            var22 = SyntheticaLookAndFeel.getInt("Synthetica.button.horizontalAlignment", var1, -1);
            if (var22 >= 0) {
               ((AbstractButton)var1).setHorizontalAlignment(var22);
            }

            this.componentPropertyStore.storeComponentProperty(var1, "SYCP_BUTTON_VALIGN");
            var15 = SyntheticaLookAndFeel.getInt("Synthetica.button.verticalAlignment", var1, -1);
            if (var15 >= 0) {
               ((AbstractButton)var1).setVerticalAlignment(var15);
            }

            this.componentPropertyStore.storeComponentProperty(var1, "SYCP_BUTTON_MARGIN");
            if (var1.getParent() instanceof JToolBar) {
               var3 = ToolBarButtonStyle.getStyle(var3, var1, var2);
            } else if (!SyntheticaLookAndFeel.getBoolean("Synthetica.button.useSynthStyle", var1, true)) {
               var3 = ButtonStyle.getStyle(var3, var1, var2);
            }

            LookAndFeel.installProperty(var1, "iconTextGap", SyntheticaLookAndFeel.getInt("Synthetica.button.iconTextGap", var1, 4));
            if (var5 != null && var5.startsWith("InternalFrameTitlePane.") && OS.getCurrentOS() == OS.Mac) {
               var1.putClientProperty("Synthetica.popupType", 2);
            }
         } else if (var2 == Region.INTERNAL_FRAME_TITLE_PANE) {
            if (!this.eventListenerExists(var1.getPropertyChangeListeners()) && !var4) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
               var1.addPropertyChangeListener(new PropertyChangeListener() {
                  public void propertyChange(PropertyChangeEvent var1) {
                     if ("ancestor".equals(var1.getPropertyName())) {
                        final JComponent var2 = (JComponent)var1.getSource();
                        if (JavaVersion.JAVA5 && var2 instanceof BasicInternalFrameTitlePane) {
                           boolean var3 = SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrameTitlePane.java5LayoutManager.enabled", var2, true);
                           BasicInternalFrameTitlePane var4 = (BasicInternalFrameTitlePane)var2;
                           if (!(var4.getLayout() instanceof SyntheticaInternalFrameTitlePaneLayout5) && var3) {
                              var4.setLayout(new SyntheticaInternalFrameTitlePaneLayout5(var4));
                           }
                        }

                        Component[] var10 = var2.getComponents();

                        for(int var11 = 0; var11 < var10.length; ++var11) {
                           Component var5 = var10[var11];
                           if (var5 instanceof JButton) {
                              final String var6 = SyntheticaLookAndFeel.getStyleName(var5);
                              boolean var7 = true;
                              MouseListener[] var8 = var5.getMouseListeners();

                              for(int var9 = 0; var9 < var8.length; ++var9) {
                                 if (var8[var9].getClass().getName().contains("synthetica")) {
                                    var7 = false;
                                    break;
                                 }
                              }

                              if (var7) {
                                 StyleFactory.this.componentPropertyStore.storeComponentProperty(var5, "SYCP_MOUSE_LISTENERS");
                                 var5.addMouseListener(new MouseAdapter() {
                                    public void mouseEntered(MouseEvent var1) {
                                       var2.putClientProperty("Synthetica.MOUSE_OVER", var6);
                                    }

                                    public void mouseExited(MouseEvent var1) {
                                       var2.putClientProperty("Synthetica.MOUSE_OVER", (Object)null);
                                    }

                                    public void mousePressed(MouseEvent var1) {
                                       var2.putClientProperty("Synthetica.MOUSE_PRESSED", var6);
                                    }

                                    public void mouseReleased(final MouseEvent var1) {
                                       var2.putClientProperty("Synthetica.MOUSE_PRESSED", (Object)null);
                                       if (var2.getClientProperty("Synthetica.MOUSE_OVER") != null) {
                                          (new Thread() {
                                             public void run() {
                                                try {
                                                   sleep(100L);
                                                } catch (InterruptedException var2x) {
                                                }

                                                EventQueue.invokeLater(new Runnable() {
                                                   public void run() {
                                                      JButton var1x = (JButton)var1.getSource();
                                                      var1x.dispatchEvent(new MouseEvent(var1x, 505, var1.getWhen(), var1.getModifiers(), var1.getX(), var1.getY(), var1.getClickCount(), var1.isPopupTrigger()));
                                                   }
                                                });
                                             }
                                          }).start();
                                       }
                                    }
                                 });
                              }
                           }
                        }
                     }

                  }
               });
            }
         } else if (var2 == Region.CHECK_BOX) {
            if (!this.eventListenerExists(var1.getMouseListeners())) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
               final JCheckBox var27 = (JCheckBox)var1;
               var1.addMouseListener(new MouseAdapter() {
                  public void mouseEntered(MouseEvent var1) {
                     var27.putClientProperty("Synthetica.MOUSE_OVER", true);
                  }

                  public void mouseExited(MouseEvent var1) {
                     var27.putClientProperty("Synthetica.MOUSE_OVER", false);
                  }
               });
            }

            LookAndFeel.installProperty(var1, "iconTextGap", SyntheticaLookAndFeel.getInt("Synthetica.checkBox.iconTextGap", var1, 4));
            var3 = CheckBoxStyle.getStyle(var3, var1, var2);
         } else if (var2 == Region.COMBO_BOX) {
            if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
               this.setOpaque(var1, false);
            }

            final JComboBox var29;
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.hoverAndPressed.enabled", var1) && !this.eventListenerExists(var1.getMouseListeners())) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
               var29 = (JComboBox)var1;
               var1.addMouseListener(new MouseAdapter() {
                  public void mouseEntered(MouseEvent var1) {
                     var29.putClientProperty("Synthetica.MOUSE_OVER", true);
                     var29.repaint();
                  }

                  public void mouseExited(MouseEvent var1) {
                     var29.putClientProperty("Synthetica.MOUSE_OVER", false);
                     var29.repaint();
                  }

                  public void mousePressed(MouseEvent var1) {
                     var29.putClientProperty("Synthetica.MOUSE_PRESSED", true);
                     var29.repaint();
                  }

                  public void mouseReleased(MouseEvent var1) {
                     var29.putClientProperty("Synthetica.MOUSE_PRESSED", false);
                     var29.repaint();
                  }
               });
            }

            if (SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.openedStateSupport.enabled", var1) && !this.eventListenerExists(((JComboBox)var1).getPopupMenuListeners())) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_COMBOBOX_POPUPMENU_LISTENERS");
               var29 = (JComboBox)var1;
               var29.addPopupMenuListener(new PopupMenuListener() {
                  public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
                     var29.repaint();
                  }

                  public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
                     var29.repaint();
                  }

                  public void popupMenuCanceled(PopupMenuEvent var1) {
                  }
               });
            }

            if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
               var1.addPropertyChangeListener(new PropertyChangeListener() {
                  public void propertyChange(PropertyChangeEvent var1) {
                     JComboBox var2 = (JComboBox)var1.getSource();
                     String var3 = var1.getPropertyName();
                     String var4;
                     if (!"renderer".equals(var3) && !"name".equals(var3)) {
                        if ("UI".equals(var3)) {
                           var4 = SyntheticaLookAndFeel.getString("Synthetica.comboBox.layoutManager.className", var2);
                           if (var4 != null) {
                              StyleFactory.this.componentPropertyStore.storeComponentProperty(var2, "SYCP_COMBOBOX_DEFAULT_LAYOUT");

                              try {
                                 var2.setLayout((LayoutManager)Class.forName(var4).newInstance());
                              } catch (Exception var6) {
                                 throw new RuntimeException(var6);
                              }
                           }
                        } else if ("editable".equals(var3)) {
                           var2.repaint();
                        }
                     } else {
                        var4 = (String)SyntheticaLookAndFeel.get("Synthetica.comboBox.defaultRenderer.className", (Component)var2);
                        if (StyleFactory.this.replaceDefaultComboRenderer(var2.getRenderer(), var4)) {
                           StyleFactory.this.componentPropertyStore.storeComponentProperty(var2, "SYCP_COMBOBOX_DEFAULT_RENDERER");

                           try {
                              var2.setRenderer((DefaultListCellRenderer)Class.forName(var4).newInstance());
                           } catch (Exception var7) {
                              throw new RuntimeException(var7);
                           }
                        }
                     }

                  }
               });
            }

            var3 = ComboBoxStyle.getStyle(var3, var1, var2);
         } else if (var2 == Region.EDITOR_PANE) {
            this.installFocusListener(var1);
            this.installTextComponentPropertyChangeListener(var1);
            var3 = EditorPaneStyle.getStyle(var3, var1, var2);
            if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
               this.setOpaque(var1, false);
            }

            if (SyntheticaLookAndFeel.getBoolean("Synthetica.editorPane.hoverSupport.enabled", var1)) {
               this.installTextComponentHoverSupport(var1);
            }
         } else if (var2 == Region.FORMATTED_TEXT_FIELD) {
            this.installFocusListener(var1);
            var3 = FormattedTextFieldStyle.getStyle(var3, var1, var2);
            if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
               this.setOpaque(var1, false);
            }

            if (SyntheticaLookAndFeel.getBoolean("Synthetica.formattedTextField.hoverSupport.enabled", var1)) {
               this.installTextComponentHoverSupport(var1);
            }
         } else if (var2 == Region.INTERNAL_FRAME) {
            SynthContext var30 = new SynthContext(var1, Region.INTERNAL_FRAME, var3, 1);
            if (!var3.isOpaque(var30)) {
               this.setOpaque(var1, false);
            }

            if (SyntheticaLookAndFeel.get("Synthetica.internalFrame.opaque", (Component)var1) != null) {
               this.setOpaque(var1, SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.opaque", var1, true));
            }

            if (JavaVersion.JAVA5 && !this.eventListenerExists(var1.getPropertyChangeListeners())) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
               var1.addPropertyChangeListener(new PropertyChangeListener() {
                  public void propertyChange(PropertyChangeEvent var1) {
                     if (var1.getPropertyName().equals("frameIcon")) {
                        JInternalFrame var2 = (JInternalFrame)var1.getSource();
                        if (SyntheticaLookAndFeel.findComponent((String)"InternalFrame.northPane", var2) != null) {
                           ((JButton)SyntheticaLookAndFeel.findComponent((String)"InternalFrameTitlePane.menuButton", var2)).setIcon(var2.getFrameIcon());
                        }
                     }

                  }
               });
            }
         } else if (var2 == Region.DESKTOP_ICON) {
            if (JavaVersion.JAVA5 && !this.eventListenerExists(var1.getPropertyChangeListeners())) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
               var1.addPropertyChangeListener(new PropertyChangeListener() {
                  public void propertyChange(PropertyChangeEvent var1) {
                     if (var1.getPropertyName().equals("ancestor")) {
                        JInternalFrame.JDesktopIcon var2 = (JInternalFrame.JDesktopIcon)var1.getSource();
                        ((JButton)SyntheticaLookAndFeel.findComponent((String)"InternalFrameTitlePane.menuButton", var2)).setIcon(var2.getInternalFrame().getFrameIcon());
                     }

                  }
               });
            }

            var19 = SyntheticaLookAndFeel.getDim("Synthetica.internalFrame.desktopIcon.size", var1, (Dimension)null);
            if (var19 != null) {
               var1.setPreferredSize(var19);
            }
         } else {
            String var31;
            if (var2 == Region.DESKTOP_PANE) {
               var31 = SyntheticaLookAndFeel.getString("Synthetica.desktopPane.desktopManager.className", var1);
               if (var31 != null) {
                  JDesktopPane var16 = (JDesktopPane)var1;
                  this.componentPropertyStore.storeComponentProperty(var16, "SYCP_DESKTOP_MANAGER");

                  try {
                     var16.setDesktopManager((DesktopManager)Class.forName(var31).newInstance());
                  } catch (Exception var10) {
                     throw new RuntimeException(var10);
                  }
               }
            } else if (var2 == Region.LABEL) {
               var3 = LabelStyle.getStyle(var3, var1, var2);
            } else if (var2 == Region.LIST) {
               this.installFocusListener(var1);
               var3 = ListStyle.getStyle(var3, var1, var2);
               if (SyntheticaLookAndFeel.get("Synthetica.list.opaque", (Component)var1) != null) {
                  this.setOpaque(var1, SyntheticaLookAndFeel.getBoolean("Synthetica.list.opaque", var1, true));
               }

               if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
                  var1.addPropertyChangeListener(new PropertyChangeListener() {
                     public void propertyChange(PropertyChangeEvent var1) {
                        if (var1.getSource() instanceof JList) {
                           JList var2 = (JList)var1.getSource();
                           ListCellRenderer var3 = var2.getCellRenderer();
                           String var4 = SyntheticaLookAndFeel.getString("Synthetica.list.defaultCellRenderer.className", var2);
                           if (var4 != null && var3 != null && var3.getClass().getName().startsWith("javax.swing.plaf.synth.SynthListUI")) {
                              try {
                                 ListCellRenderer var5 = (ListCellRenderer)Class.forName(var4).newInstance();
                                 var2.setCellRenderer(var5);
                              } catch (Exception var6) {
                                 throw new RuntimeException(var6);
                              }
                           }
                        }

                     }
                  });
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.cellRenderer.hoverSupport.enabled", var1, false) && !this.eventListenerExists(var1.getMouseListeners())) {
                  this.installCellRendererHoverListener(var1);
               }

               if (OS.getCurrentOS() == OS.Mac && !this.eventListenerExists(var1.getMouseListeners()) && SyntheticaLookAndFeel.getBoolean("Synthetica.metaKeySupportOnMacEnabled", var1, true) && SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
                  this.installMetaKeySupport(var1);
               }
            } else if (var2 == Region.PASSWORD_FIELD) {
               this.installFocusListener(var1);
               var3 = PasswordFieldStyle.getStyle(var3, var1, var2);
               if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
                  this.setOpaque(var1, false);
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.passwordField.hoverSupport.enabled", var1)) {
                  this.installTextComponentHoverSupport(var1);
               }
            } else if (var2 == Region.ROOT_PANE) {
               Color var33 = UIManager.getColor("control");
               Color var18 = var1.getBackground();
               if (var18 instanceof SystemColor && !var18.equals(var33) && SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.replaceSystemColor", var1)) {
                  var1.setBackground(var33);
               }
            } else if (var2 == Region.SPINNER) {
               var3 = SpinnerStyle.getStyle(var3, var1, var2);
               if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
                  this.setOpaque(var1, false);
               }

               var31 = SyntheticaLookAndFeel.getString("Synthetica.spinner.layoutManager.className", var1);
               LayoutManager var21 = var1.getLayout();
               if (var31 != null && var21 instanceof UIResource && !var21.getClass().getName().equals(var31)) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_SPINNER_DEFAULT_LAYOUT");

                  try {
                     var1.setLayout((LayoutManager)Class.forName(var31).newInstance());
                  } catch (Exception var9) {
                     throw new RuntimeException(var9);
                  }
               }

               if (JavaVersion.JAVA5 && !this.eventListenerExists(var1.getPropertyChangeListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
                  var1.addPropertyChangeListener(new PropertyChangeListener() {
                     public void propertyChange(PropertyChangeEvent var1) {
                        if ("ToolTipText".equals(var1.getPropertyName())) {
                           StyleFactory.this.updateToolTipTextForChildren((JComponent)var1.getSource());
                        }

                     }
                  });
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.spinner.hoverAndPressed.enabled", var1) && !this.eventListenerExists(var1.getMouseListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
                  final JSpinner var24 = (JSpinner)var1;
                  var1.addMouseListener(new MouseAdapter() {
                     public void mouseEntered(MouseEvent var1) {
                        var24.putClientProperty("Synthetica.MOUSE_OVER", true);
                        var24.repaint();
                     }

                     public void mouseExited(MouseEvent var1) {
                        var24.putClientProperty("Synthetica.MOUSE_OVER", false);
                        var24.repaint();
                     }

                     public void mousePressed(MouseEvent var1) {
                        var24.putClientProperty("Synthetica.MOUSE_PRESSED", true);
                        var24.repaint();
                     }

                     public void mouseReleased(MouseEvent var1) {
                        var24.putClientProperty("Synthetica.MOUSE_PRESSED", false);
                        var24.repaint();
                     }
                  });
               }
            } else if (var2 == Region.SCROLL_BAR) {
               if (SyntheticaLookAndFeel.getBoolean("Synthetica.scrollBarTrack.hoverAndPressed.enabled", var1) && !this.eventListenerExists(var1.getMouseListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
                  var1.addMouseListener(new MouseAdapter() {
                     public void mouseEntered(MouseEvent var1) {
                        JScrollBar var2 = (JScrollBar)var1.getSource();
                        Boolean var3 = (Boolean)var2.getClientProperty("Synthetica.MOUSE_OVER");
                        var2.putClientProperty("Synthetica.MOUSE_OVER", true);
                        if (var3 == null || !var3) {
                           var2.repaint();
                        }

                     }

                     public void mouseExited(MouseEvent var1) {
                        boolean var2 = (var1.getModifiersEx() & 1024) == 1024;
                        if (!var2) {
                           JScrollBar var3 = (JScrollBar)var1.getSource();
                           Boolean var4 = (Boolean)var3.getClientProperty("Synthetica.MOUSE_OVER");
                           var3.putClientProperty("Synthetica.MOUSE_OVER", false);
                           if (var4 == null || var4) {
                              var3.repaint();
                           }
                        }

                     }

                     public void mousePressed(MouseEvent var1) {
                        JScrollBar var2 = (JScrollBar)var1.getSource();
                        Boolean var3 = (Boolean)var2.getClientProperty("Synthetica.MOUSE_PRESSED");
                        var2.putClientProperty("Synthetica.MOUSE_PRESSED", true);
                        if (var3 == null || !var3) {
                           var2.repaint();
                        }

                     }

                     public void mouseReleased(MouseEvent var1) {
                        JScrollBar var2 = (JScrollBar)var1.getSource();
                        Boolean var3 = (Boolean)var2.getClientProperty("Synthetica.MOUSE_PRESSED");
                        var2.putClientProperty("Synthetica.MOUSE_PRESSED", false);
                        boolean var4 = var2.getBounds().contains(SwingUtilities.convertPoint(var2, var1.getPoint(), var2.getParent()));
                        if (!var4) {
                           var2.putClientProperty("Synthetica.MOUSE_OVER", false);
                        }

                        if (var3 == null || var3) {
                           var2.repaint();
                        }

                     }
                  });
               }
            } else if (var2 == Region.SCROLL_PANE) {
               var3 = ScrollPaneStyle.getStyle(var3, var1, var2);
               JViewport var36 = ((JScrollPane)var1).getViewport();
               if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1) && (var36 != null && var36.getView() instanceof JTextComponent || SyntheticaLookAndFeel.getBoolean("Synthetica.scrollPane.nonOpaque", var1))) {
                  this.setOpaque(var1, false);
               }

               this.applyTitledBorderStyle(var1);
               if (SyntheticaLookAndFeel.getBoolean("Synthetica.scrollPane.cornerPainter.enabled", var1)) {
                  JScrollPane var23 = (JScrollPane)var1;
                  this.installScrollPaneCorner(var23, "LOWER_RIGHT_CORNER");
                  this.installScrollPaneCorner(var23, "LOWER_LEFT_CORNER");
                  this.installScrollPaneCorner(var23, "UPPER_LEFT_CORNER");
                  this.installScrollPaneCorner(var23, "UPPER_RIGHT_CORNER");
               }
            } else if (var2 == Region.SLIDER && SyntheticaLookAndFeel.getBoolean("Synthetica.slider.hoverAndPressed.enabled", var1) && !this.eventListenerExists(var1.getMouseListeners())) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
               var1.addMouseListener(new MouseAdapter() {
                  public void mouseEntered(MouseEvent var1) {
                     JComponent var2 = (JComponent)var1.getSource();
                     var2.putClientProperty("Synthetica.MOUSE_OVER", true);
                     var2.repaint();
                  }

                  public void mouseExited(MouseEvent var1) {
                     JComponent var2 = (JComponent)var1.getSource();
                     var2.putClientProperty("Synthetica.MOUSE_OVER", false);
                     var2.repaint();
                  }

                  public void mousePressed(MouseEvent var1) {
                     JComponent var2 = (JComponent)var1.getSource();
                     var2.putClientProperty("Synthetica.MOUSE_PRESSED", true);
                     var2.repaint();
                  }

                  public void mouseReleased(MouseEvent var1) {
                     JComponent var2 = (JComponent)var1.getSource();
                     var2.putClientProperty("Synthetica.MOUSE_PRESSED", false);
                     var2.repaint();
                  }
               });
            } else if (var2 == Region.SPLIT_PANE_DIVIDER) {
               if (SyntheticaLookAndFeel.getBoolean("Syntetica.splitPane.centerOneTouchButtons", var1)) {
                  if (!this.eventListenerExists(var1.getComponentListeners())) {
                     this.componentPropertyStore.storeComponentProperty(var1, "SYCP_COMPONENT_LISTENERS");
                     var1.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent var1) {
                           StyleFactory.this.updateSplitDivider((JSplitPane)var1.getComponent());
                        }
                     });
                  }

                  if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
                     this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
                     var1.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent var1) {
                           String var2 = var1.getPropertyName();
                           if ("orientation".equals(var2) || "dividerSize".equals(var2)) {
                              StyleFactory.this.updateSplitDivider((JSplitPane)var1.getSource());
                           }

                        }
                     });
                  }
               }
            } else if (var2 == Region.MENU) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
               final JMenu var37 = (JMenu)var1;
               if (!menuToolTipEnabled && var1.getToolTipText() != null) {
                  ToolTipManager.sharedInstance().unregisterComponent(var1);
               }

               final MouseAdapter var25 = null;
               if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
                  final DefaultSynthStyle var26 = (DefaultSynthStyle)var3;
                  var25 = new MouseAdapter() {
                     public void mouseEntered(MouseEvent var1) {
                        JMenu var2 = (JMenu)var1.getSource();
                        if (var2.isTopLevelMenu()) {
                           var2.putClientProperty("Synthetica.MOUSE_OVER", Boolean.TRUE);
                           Color var3 = var26.getColor(var2, Region.MENU, 512, ColorType.TEXT_FOREGROUND);
                           var2.setForeground(new Color(var3.getRGB()));
                           var2.repaint();
                        }

                     }

                     public void mouseExited(MouseEvent var1) {
                        JMenu var2 = (JMenu)var1.getSource();
                        if (var2.isTopLevelMenu()) {
                           var2.putClientProperty("Synthetica.MOUSE_OVER", Boolean.FALSE);
                           Color var3 = var26.getColor(var2, Region.MENU, 0, ColorType.TEXT_FOREGROUND);
                           var2.setForeground(var3);
                           var2.repaint();
                        }

                     }
                  };
               }

               if (var37.isEnabled() && !this.eventListenerExists(var37.getMouseListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
                  if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
                     var37.addMouseListener(var25);
                  }
               } else if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
                  var1.addPropertyChangeListener(new PropertyChangeListener() {
                     public void propertyChange(PropertyChangeEvent var1) {
                        if (var1.getPropertyName().equals("ancestor")) {
                           if (!var37.isEnabled() || !var37.isTopLevelMenu()) {
                              return;
                           }

                           StyleFactory.this.componentPropertyStore.storeComponentProperty(var37, "SYCP_MOUSE_LISTENERS");
                           if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
                              var37.addMouseListener(var25);
                           }
                        }

                     }
                  });
               }

               var3 = MenuStyle.getStyle(var3, var1, var2);
            } else if (var2 == Region.MENU_ITEM) {
               var3 = MenuItemStyle.getStyle(var3, var1, var2);
               if (!menuItemToolTipEnabled && var1.getToolTipText() != null) {
                  ToolTipManager.sharedInstance().unregisterComponent(var1);
               }
            } else if (var2 == Region.RADIO_BUTTON_MENU_ITEM) {
               var3 = MenuItemStyle.getStyle(var3, var1, var2);
               if (!menuItemToolTipEnabled && var1.getToolTipText() != null) {
                  ToolTipManager.sharedInstance().unregisterComponent(var1);
               }
            } else if (var2 == Region.CHECK_BOX_MENU_ITEM) {
               var3 = MenuItemStyle.getStyle(var3, var1, var2);
               if (!menuItemToolTipEnabled && var1.getToolTipText() != null) {
                  ToolTipManager.sharedInstance().unregisterComponent(var1);
               }
            } else if (var2 == Region.TABLE) {
               this.installFocusListener(var1);
               JTable var38 = (JTable)var1;
               if (SyntheticaLookAndFeel.get("Synthetica.table.opaque", (Component)var1) != null) {
                  this.setOpaque(var1, SyntheticaLookAndFeel.getBoolean("Synthetica.table.opaque", var1, true));
               }

               if (SyntheticaLookAndFeel.get("Synthetica.table.columnMargin", (Component)var38) != null) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TABLE_COLUMN_MARGIN");
                  var15 = SyntheticaLookAndFeel.getInt("Synthetica.table.columnMargin", var38);
                  if (var15 != var38.getColumnModel().getColumnMargin()) {
                     var38.getColumnModel().setColumnMargin(var15);
                  }
               }

               if (SyntheticaLookAndFeel.get("Synthetica.table.rowMargin", (Component)var38) != null) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TABLE_ROW_MARGIN");
                  var15 = SyntheticaLookAndFeel.getInt("Synthetica.table.rowMargin", var38);
                  if (var15 != var38.getRowMargin()) {
                     var38.setRowMargin(var15);
                  }
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.table.installCellRenderers", var1, true)) {
                  this.installDefaultTableRenderers(var38);
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.table.installCellEditors", var1, true)) {
                  this.installDefaultTableEditors(var38);
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.cellRenderer.hoverSupport.enabled", var1, false) && !this.eventListenerExists(var1.getMouseListeners())) {
                  this.installCellRendererHoverListener(var1);
               }

               if (OS.getCurrentOS() == OS.Mac && !this.eventListenerExists(var1.getMouseListeners()) && SyntheticaLookAndFeel.getBoolean("Synthetica.metaKeySupportOnMacEnabled", var1, true) && SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
                  this.installMetaKeySupport(var1);
               }

               if (var38.getDefaultRenderer(Icon.class) == null) {
                  var38.setDefaultRenderer(Icon.class, var38.getDefaultRenderer(ImageIcon.class));
               }

               var3 = TableStyle.getStyle(var3, var1, var2);
            } else if (var2 == Region.TABLE_HEADER) {
               if (var1 instanceof JTableHeader) {
                  JTableHeader var39 = (JTableHeader)var1;
                  if (SyntheticaLookAndFeel.get("Synthetica.tableHeader.opaque", (Component)var1) != null) {
                     this.setOpaque(var1, SyntheticaLookAndFeel.getBoolean("Synthetica.tableHeader.opaque", var1, true));
                  }

                  TableCellRenderer var32 = var39.getDefaultRenderer();
                  if (var32 instanceof UIResource && !(var32 instanceof SyntheticaHeaderRenderer5) && !var32.getClass().getName().equals("de.javasoft.plaf.synthetica.SyntheticaHeaderRenderer") && !var32.getClass().getName().equals("org.netbeans.modules.autoupdate.ui.SortColumnHeaderRenderer") && !SyntheticaLookAndFeel.getBoolean("Synthetica.table.useSynthHeaderRenderer", var1)) {
                     if (JavaVersion.JAVA5) {
                        var39.setDefaultRenderer(new SyntheticaHeaderRenderer5());
                     } else if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
                        var39.setDefaultRenderer(new SyntheticaHeaderRenderer());
                     }
                  }
               }
            } else if (var2 == Region.TABBED_PANE) {
               if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
                  var1.addPropertyChangeListener(new PropertyChangeListener() {
                     public void propertyChange(PropertyChangeEvent var1) {
                        if ("ancestor".equals(var1.getPropertyName())) {
                           Iterator var3 = SyntheticaLookAndFeel.findComponents((Class)AbstractButton.class, (JComponent)var1.getSource(), new ArrayList()).iterator();

                           while(true) {
                              AbstractButton var2;
                              int var5;
                              Dimension var6;
                              do {
                                 if (!var3.hasNext()) {
                                    return;
                                 }

                                 var2 = (AbstractButton)var3.next();
                                 JTabbedPane var4 = (JTabbedPane)var1.getSource();
                                 var5 = var4.getTabPlacement();
                                 var6 = SyntheticaLookAndFeel.getDim("Synthetica.tabbedPane.arrowButton.size", var4);
                              } while(var6 == null);

                              var2.setPreferredSize(var5 != 1 && var5 != 3 ? new Dimension(var6.height, var6.width) : var6);
                           }
                        }
                     }
                  });
               }
            } else if (var2 == Region.TABBED_PANE_TAB) {
               var3 = TabbedPaneStyle.getStyle(var3, var1, var2);
               if (!this.eventListenerExists(var1.getMouseMotionListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_MOTION_LISTENERS");
                  var1.addMouseMotionListener(new MouseMotionAdapter() {
                     public void mouseMoved(MouseEvent var1) {
                        StyleFactory.this.tabHoverSupport(var1);
                     }
                  });
               }

               if (!this.eventListenerExists(var1.getMouseListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
                  var1.addMouseListener(new MouseAdapter() {
                     public void mouseEntered(MouseEvent var1) {
                        StyleFactory.this.tabHoverSupport(var1);
                     }

                     public void mouseExited(MouseEvent var1) {
                        JTabbedPane var2 = (JTabbedPane)var1.getSource();
                        Integer var3 = (Integer)var2.getClientProperty("Synthetica.MOUSE_OVER");
                        int var4 = var3 == null ? -1 : var3;
                        if (var4 != -1) {
                           var2.putClientProperty("Synthetica.MOUSE_OVER", -1);
                           var2.repaint();
                        }

                     }
                  });
               }

               JTabbedPane var40 = (JTabbedPane)var1;
               if (!this.eventListenerExists(var40.getContainerListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_CONTAINER_LISTENERS");
                  this.storeContainerComponentProperties(var40, "SYCP_OPAQUE");
                  var40.addContainerListener(new ContainerListener() {
                     public void componentAdded(ContainerEvent var1) {
                        StyleFactory.this.storeContainerComponentProperties(var1.getContainer(), "SYCP_OPAQUE");
                        ((JTabbedPane)var1.getContainer()).putClientProperty("Synthetica.childsAreTranslucent", false);
                     }

                     public void componentRemoved(ContainerEvent var1) {
                     }
                  });
               }
            } else if (var2 == Region.TABBED_PANE_CONTENT) {
               var3 = TabbedPaneContentStyle.getStyle(var3, var1, var2);
            } else if (var2 == Region.TEXT_AREA) {
               this.installFocusListener(var1);
               this.installTextComponentPropertyChangeListener(var1);
               var3 = TextAreaStyle.getStyle(var3, var1, var2);
               if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
                  this.setOpaque(var1, false);
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.textArea.hoverSupport.enabled", var1)) {
                  this.installTextComponentHoverSupport(var1);
               }
            } else if (var2 == Region.TEXT_FIELD) {
               this.installFocusListener(var1);
               var3 = TextFieldStyle.getStyle(var3, var1, var2);
               if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
                  this.setOpaque(var1, false);
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.textField.hoverSupport.enabled", var1)) {
                  this.installTextComponentHoverSupport(var1);
               }
            } else if (var2 == Region.TEXT_PANE) {
               this.installFocusListener(var1);
               this.installTextComponentPropertyChangeListener(var1);
               var3 = TextPaneStyle.getStyle(var3, var1, var2);
               if (!SyntheticaLookAndFeel.getBoolean("Synthetica.textComponents.useSwingOpaqueness", var1)) {
                  this.setOpaque(var1, false);
               }

               if (SyntheticaLookAndFeel.getBoolean("Synthetica.textPane.hoverSupport.enabled", var1)) {
                  this.installTextComponentHoverSupport(var1);
               }
            } else if (var2 == Region.TOGGLE_BUTTON) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_BUTTON_MARGIN");
               if (var1.getParent() != null && var1.getParent() instanceof JToolBar) {
                  var3 = ToolBarButtonStyle.getStyle(var3, var1, var2);
               } else if (!SyntheticaLookAndFeel.getBoolean("Synthetica.toolgeButton.useSynthStyle", var1, true)) {
                  var3 = ToggleButtonStyle.getStyle(var3, var1, var2);
               }

               LookAndFeel.installProperty(var1, "iconTextGap", SyntheticaLookAndFeel.getInt("Synthetica.toggleButton.iconTextGap", var1, 4));
            } else if (var2 == Region.TOOL_BAR) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
               var3 = ToolBarStyle.getStyle(var3, var1, var2);
               this.setOpaque(var1, SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.opaque", var1, true));
               if (!this.eventListenerExists(var1.getContainerListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_CONTAINER_LISTENERS");
                  var1.addContainerListener(new ContainerAdapter() {
                     public void componentAdded(ContainerEvent var1) {
                        Component var2 = var1.getChild();
                        Font var3 = var2.getFont();
                        AbstractButton var4;
                        String var5;
                        if (var2 instanceof JButton && var3 instanceof UIResource && SyntheticaLookAndFeel.get("Synthetica.toolBar.button.font.style", var2) != null) {
                           var4 = (AbstractButton)var2;
                           var5 = SyntheticaLookAndFeel.getStyleName(var4);
                           if (var4.getClientProperty("Synthetica.style") != null) {
                              var4.putClientProperty("Synthetica.style", (Object)null);
                              var4.putClientProperty("Synthetica.style", var5);
                           } else {
                              var2.setName((String)null);
                              var2.setName(var5);
                           }
                        } else if (var2 instanceof JToggleButton && var3 instanceof UIResource && SyntheticaLookAndFeel.get("Synthetica.toolBar.toggleButton.font.style", var2) != null) {
                           var4 = (AbstractButton)var2;
                           var5 = SyntheticaLookAndFeel.getStyleName(var2);
                           if (var4.getClientProperty("Synthetica.style") != null) {
                              var4.putClientProperty("Synthetica.style", (Object)null);
                              var4.putClientProperty("Synthetica.style", var5);
                           } else {
                              var2.setName((String)null);
                              var2.setName(var5);
                           }
                        }

                     }
                  });
               }
            } else if (var2 == Region.TOOL_BAR_SEPARATOR) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TOOLBAR_SEPARATOR_SIZE");
               var3 = ToolBarSeparatorStyle.getStyle(var3, var1, var2);
            } else if (var2 == Region.TOOL_TIP) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
               var3 = ToolTipStyle.getStyle(var3, var1, var2);
            } else if (var2 == Region.TREE) {
               this.installFocusListener(var1);
               if (SyntheticaLookAndFeel.get("Synthetica.tree.opaque", (Component)var1) != null) {
                  this.setOpaque(var1, SyntheticaLookAndFeel.getBoolean("Synthetica.tree.opaque", var1, true));
               }

               if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
                  var1.addPropertyChangeListener(new PropertyChangeListener() {
                     public void propertyChange(PropertyChangeEvent var1) {
                        String var2 = var1.getPropertyName();
                        JTree var3 = (JTree)var1.getSource();
                        TreeCellRenderer var4 = var3.getCellRenderer();
                        boolean var5 = SyntheticaLookAndFeel.getBoolean("Synthetica.tree.defaultCellRenderer.enabled", var3, true);
                        String var6 = SyntheticaLookAndFeel.getString("Synthetica.tree.defaultCellRenderer.className", var3);
                        if (var6 == null) {
                           var6 = SyntheticaDefaultTreeCellRenderer.class.getName();
                        }

                        if ((var2.equals("cellRenderer") || var2.equals("name")) && var5 && var4 != null && (var4.getClass().getName().startsWith("javax.swing.plaf.synth.SynthTreeUI") || var4 instanceof SyntheticaDefaultTreeCellRenderer)) {
                           try {
                              if (!var6.equals(var4.getClass().getName())) {
                                 TreeCellRenderer var7 = (TreeCellRenderer)Class.forName(var6).newInstance();
                                 var3.setCellRenderer(var7);
                              }
                           } catch (Exception var8) {
                              throw new RuntimeException(var8);
                           }
                        }

                        if (JavaVersion.JAVA5 && "componentOrientation".equals(var2) && !((ComponentOrientation)var1.getNewValue()).isLeftToRight()) {
                           ((JTree)var1.getSource()).updateUI();
                        }

                     }
                  });
               }

               if (OS.getCurrentOS() == OS.Mac && !this.eventListenerExists(var1.getMouseListeners()) && SyntheticaLookAndFeel.getBoolean("Synthetica.metaKeySupportOnMacEnabled", var1, true) && SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
                  this.installMetaKeySupport(var1);
               }
            } else if (var2 == Region.PANEL) {
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
               this.applyTitledBorderStyle(var1);
            } else if (var2 == Region.POPUP_MENU) {
               if ("ComboPopup.popup".equals(var5) && UIManager.get("Synthetica.comboPopup.insets") != null && !(var1.getBorder() instanceof SyntheticaComboPopupBorder) && SyntheticaLookAndFeel.findComponent((Class)JScrollPane.class, var1) != null) {
                  var1.setBorder(new SyntheticaComboPopupBorder((SyntheticaComboPopupBorder)null));
                  JScrollPane var41 = (JScrollPane)SyntheticaLookAndFeel.findComponent((Class)JScrollPane.class, var1);
                  Dimension var34 = var41.getPreferredSize();
                  var34.width -= var1.getInsets().left + var1.getInsets().right - 2;
                  var41.setPreferredSize(var34);
                  var41.setMinimumSize(var34);
                  var41.setMaximumSize(var34);
               }

               JComponent var42 = (JComponent)SyntheticaLookAndFeel.findComponent((String)"ComboBox.scrollPane", var1);
               Insets var35;
               if (var42 != null) {
                  var42.setBorder(new EmptyBorder(0, 0, 0, 0));
                  var35 = SyntheticaLookAndFeel.getInsets("Synthetica.comboPopup.list.insets", var1);
                  JComponent var28 = (JComponent)SyntheticaLookAndFeel.findComponent((String)"ComboBox.list", var1);
                  if (var35 != null && var28 != null) {
                     var28.setBorder(new EmptyBorder(var35));
                  }
               }

               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
               this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
               var3 = PopupMenuStyle.getStyle(var3, var1, var2);
               var35 = SyntheticaLookAndFeel.getInsets("Synthetica.popupMenu.toplevel.insets", var1);
               if (var35 != null && !this.eventListenerExists(((JPopupMenu)var1).getPopupMenuListeners())) {
                  this.componentPropertyStore.storeComponentProperty(var1, "SYCP_POPUPMENU_LISTENERS");
                  ((JPopupMenu)var1).addPopupMenuListener(new PopupMenuListener() {
                     public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
                        JPopupMenu var2 = (JPopupMenu)var1.getSource();
                        if (SyntheticaLookAndFeel.isToplevelPopupMenu(var2)) {
                           StyleFactory.this.componentPropertyStore.storeComponentProperty(var2, "SYCP_BORDER");
                           BorderUIResource var3 = new BorderUIResource(new EmptyBorder(SyntheticaLookAndFeel.getInsets("Synthetica.popupMenu.toplevel.insets", var2, false)));
                           var2.setBorder(var3);
                        }

                     }

                     public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
                     }

                     public void popupMenuCanceled(PopupMenuEvent var1) {
                     }
                  });
               }
            } else if (var2 != Region.POPUP_MENU_SEPARATOR) {
               if (var2 == Region.RADIO_BUTTON) {
                  if (!this.eventListenerExists(var1.getMouseListeners())) {
                     this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
                     final JRadioButton var43 = (JRadioButton)var1;
                     var1.addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent var1) {
                           var43.putClientProperty("Synthetica.MOUSE_OVER", true);
                        }

                        public void mouseExited(MouseEvent var1) {
                           var43.putClientProperty("Synthetica.MOUSE_OVER", false);
                        }
                     });
                  }

                  LookAndFeel.installProperty(var1, "iconTextGap", SyntheticaLookAndFeel.getInt("Synthetica.radioButton.iconTextGap", var1, 4));
                  var3 = RadioButtonStyle.getStyle(var3, var1, var2);
               } else if (var2 == Region.VIEWPORT) {
                  var3 = ViewportStyle.getStyle(var3, var1, Region.VIEWPORT);
                  if (SyntheticaLookAndFeel.get("Synthetica.viewport.opaque", (Component)var1) != null) {
                     this.setOpaque(var1, SyntheticaLookAndFeel.getBoolean("Synthetica.viewport.opaque", var1, true));
                  }

                  if (!this.eventListenerExists(var1.getContainerListeners())) {
                     this.componentPropertyStore.storeComponentProperty(var1, "SYCP_CONTAINER_LISTENERS");
                     var1.addContainerListener(new ContainerAdapter() {
                        public void componentAdded(ContainerEvent var1) {
                           Component var2 = var1.getChild();
                           boolean var3 = var2.getClass().getName().endsWith("JHEditorPane") && SyntheticaLookAndFeel.getBoolean("Synthetica.javaHelpEditorViewport.opaque", (Component)null, true);
                           if (var2 instanceof JTextComponent && !var3) {
                              StyleFactory.this.setOpaque((JViewport)var1.getContainer(), false);
                           }

                        }
                     });
                  }
               }
            }
         }
      }

      return var3;
   }

   private boolean replaceDefaultComboRenderer(ListCellRenderer var1, String var2) {
      String var3 = var1 == null ? "NULL" : var1.getClass().getName();
      return var2 != null && (var1 == null || var1 instanceof UIResource) && !var3.equals(var2) && !var3.startsWith("org.netbeans.");
   }

   private void tabHoverSupport(MouseEvent var1) {
      JTabbedPane var2 = (JTabbedPane)var1.getSource();
      Integer var3 = (Integer)var2.getClientProperty("Synthetica.MOUSE_OVER");
      int var4 = var3 == null ? -1 : var3;
      int var5 = -1;
      int var6 = var2.getTabCount();

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var2.getBoundsAt(var7).contains(var1.getPoint())) {
            var5 = var7;
            break;
         }
      }

      if (var4 != var5) {
         var2.putClientProperty("Synthetica.MOUSE_OVER", var5);
         if (var4 >= 0 && var2.getTabCount() > var4) {
            var2.repaint(var2.getBoundsAt(var4));
         }

         if (var5 >= 0) {
            var2.repaint(var2.getBoundsAt(var5));
         }
      }

   }

   private void installScrollPaneCorner(JScrollPane var1, String var2) {
      Component var3 = var1.getCorner(var2);
      if ((var3 == null || var3 instanceof UIResource) && !(var3 instanceof SyntheticaScrollPaneCorner)) {
         var1.setCorner(var2, new SyntheticaScrollPaneCorner.UIResource(var2));
      }

   }

   private void applyTitledBorderStyle(JComponent var1) {
      if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
         var1.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent var1) {
               if ("border".equals(var1.getPropertyName())) {
                  JComponent var2 = (JComponent)var1.getSource();
                  TitledBorder var3 = StyleFactory.this.getTitledBorder(var2.getBorder());
                  if (var3 != null && SyntheticaLookAndFeel.get("Synthetica.titledBorder.title.position", (Component)var2) != null) {
                     StyleFactory.this.setTitledBorderTitlePosition(var2, (TitledBorder)var3);
                  }

               }
            }
         });
      }

      TitledBorder var2 = this.getTitledBorder(var1.getBorder());
      if (var2 != null && SyntheticaLookAndFeel.get("Synthetica.titledBorder.title.position", (Component)var1) != null) {
         this.setTitledBorderTitlePosition(var1, (TitledBorder)var2);
      }

   }

   private void setOpaque(JComponent var1, boolean var2) {
      this.componentPropertyStore.storeComponentProperty(var1, "SYCP_OPAQUE");
      var1.setOpaque(var2);
   }

   private boolean eventListenerExists(EventListener[] var1) {
      EventListener[] var5 = var1;
      int var4 = var1.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         EventListener var2 = var5[var3];
         if (var2 != this.styleUpdater && var2.getClass().getName().startsWith(this.getClass().getName())) {
            return true;
         }
      }

      return false;
   }

   private boolean eventListenerExists(EventListener var1, EventListener[] var2) {
      EventListener[] var6 = var2;
      int var5 = var2.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         EventListener var3 = var6[var4];
         if (var3 == var1) {
            return true;
         }
      }

      return false;
   }

   private void updateToolTipTextForChildren(JComponent var1) {
      Component[] var5;
      int var4 = (var5 = var1.getComponents()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Component var2 = var5[var3];
         ((JComponent)var2).setToolTipText(var1.getToolTipText());
         if (var2 instanceof JComponent) {
            this.updateToolTipTextForChildren((JComponent)var2);
         }
      }

   }

   private TitledBorder getTitledBorder(Border var1) {
      if (var1 instanceof TitledBorder) {
         return (TitledBorder)var1;
      } else {
         if (var1 instanceof CompoundBorder) {
            TitledBorder var2 = this.getTitledBorder(((CompoundBorder)var1).getOutsideBorder());
            if (var2 != null) {
               return (TitledBorder)var2;
            }

            TitledBorder var3 = this.getTitledBorder(((CompoundBorder)var1).getInsideBorder());
            if (var3 != null) {
               return (TitledBorder)var3;
            }
         }

         return null;
      }
   }

   private void setTitledBorderTitlePosition(JComponent var1, TitledBorder var2) {
      this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TITLEDBORDER_TITLEPOSITION");
      var2.setTitlePosition(SyntheticaLookAndFeel.getInt("Synthetica.titledBorder.title.position", var1));
   }

   private void installMetaKeySupport(JComponent var1) {
      this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
      var1.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent var1) {
            int var2 = var1.getModifiers();

            try {
               if (var1.isMetaDown()) {
                  int var3 = var2 | 2;
                  Class var4 = Class.forName("java.awt.event.InputEvent");
                  Field var5 = var4.getDeclaredField("modifiers");
                  var5.setAccessible(true);
                  var5.set(var1, var3);
               }

            } catch (Exception var6) {
               throw new RuntimeException(var6);
            }
         }
      });
   }

   private void storeContainerComponentProperties(Container var1, String var2) {
      if (var1 instanceof JComponent) {
         this.componentPropertyStore.storeComponentProperty(var1, var2);
      }

      Component[] var3 = var1.getComponents();
      Component[] var7 = var3;
      int var6 = var3.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         Component var4 = var7[var5];
         if (var4 instanceof Container && !(var4 instanceof Window) && !(var4 instanceof JRootPane)) {
            this.storeContainerComponentProperties((Container)var4, var2);
         }
      }

   }

   void restoreAllComponentProperties() {
      if (this.componentPropertyStore.cleanerThread != null) {
         this.componentPropertyStore.restoreAllComponentProperties();
         this.componentPropertyStore.cleanerThread.interrupt();
      }

   }

   public void prepareMetalLAFSwitch() {
      this.prepareMetalLAFSwitch = true;
   }

   static void reinitialize() {
      menuToolTipEnabled = UIManager.getBoolean("Synthetica.menu.toolTipEnabled");
      menuItemToolTipEnabled = UIManager.getBoolean("Synthetica.menuItem.toolTipEnabled");
   }

   private void setOpaqueDefault4Metal(JComponent var1) {
      if (var1 instanceof JLabel || var1 instanceof JInternalFrame || var1 instanceof JTabbedPane || var1 instanceof JSeparator || var1 instanceof JMenu || var1 instanceof JSlider || var1 instanceof JPanel && this.isGlassPane((JPanel)var1)) {
         var1.setOpaque(false);
      } else {
         if (var1 instanceof JPanel) {
            return;
         }

         var1.setOpaque(true);
      }

   }

   private boolean isGlassPane(JPanel var1) {
      return var1.getParent() instanceof JRootPane && ((JRootPane)var1.getParent()).getGlassPane() == var1;
   }

   private void updateSplitDivider(JSplitPane var1) {
      if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
         BasicSplitPaneUI var2 = (BasicSplitPaneUI)var1.getUI();
         BasicSplitPaneDivider var3 = var2.getDivider();
         int var4 = DefaultLookup.getInt(var1, var2, "SplitPane.oneTouchButtonOffset", 2);

         try {
            Class var5 = Class.forName("javax.swing.plaf.basic.BasicSplitPaneDivider");
            Field var6 = var5.getDeclaredField("oneTouchOffset");
            var6.setAccessible(true);
            if (var3 != null) {
               var6.set(var3, var4);
            }
         } catch (Exception var7) {
            throw new RuntimeException(var7);
         }

         if (var3 != null) {
            var3.getLayout().layoutContainer(var3);
         }

      }
   }

   private void installDefaultTableRenderers(JTable var1) {
      TableCellRenderer var2 = var1.getDefaultRenderer(Object.class);
      String var3 = "Synthetica.table.defaultRenderer.className";
      boolean var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultRenderer", var1, true);
      if ((SyntheticaLookAndFeel.get(var3, (Component)var1) != null || var4) && (var2 == null || var2.getClass().getName().contains("$SynthTableCellRenderer"))) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TABLE_OBJECT_DEFAULT_RENDERER");
         this.installDefaultTableCellRenderer(var1, Object.class, var3, var2);
         var2 = var1.getDefaultRenderer(Object.class);
      }

      var3 = "Synthetica.table.defaultBooleanRenderer.className";
      var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultBooleanRenderer", var1, true);
      TableCellRenderer var5;
      if (SyntheticaLookAndFeel.get(var3, (Component)var1) != null || var4) {
         var5 = var1.getDefaultRenderer(Boolean.class);
         if (var5 == null || var5.getClass().getName().contains("$SynthBooleanTableCellRenderer")) {
            this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TABLE_BOOLEAN_DEFAULT_RENDERER");
            this.installDefaultTableCellRenderer(var1, Boolean.class, var3, var5);
         }
      }

      var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultFloatRenderer", var1);
      if (SyntheticaLookAndFeel.get("Synthetica.table.defaultFloatRenderer.className", (Component)var1) != null || var4) {
         var5 = var1.getDefaultRenderer(Float.class);
         if (var5 == null || var5 instanceof UIResource && var5 == var2) {
            this.installDefaultTableCellRenderer(var1, Float.class, var3, var2);
         }
      }

      var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultDoubleRenderer", var1);
      if (SyntheticaLookAndFeel.get("Synthetica.table.defaultDoubleRenderer.className", (Component)var1) != null || var4) {
         var5 = var1.getDefaultRenderer(Double.class);
         if (var5 == null || var5 instanceof UIResource && var5 == var2) {
            this.installDefaultTableCellRenderer(var1, Double.class, var3, var2);
         }
      }

      var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultNumberRenderer", var1);
      if (SyntheticaLookAndFeel.get("Synthetica.table.defaultNumberRenderer.className", (Component)var1) != null || var4) {
         var5 = var1.getDefaultRenderer(Number.class);
         if (var5 == null || var5 instanceof UIResource && var5 == var2) {
            this.installDefaultTableCellRenderer(var1, Number.class, var3, var2);
         }
      }

      var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultDateRenderer", var1);
      if (SyntheticaLookAndFeel.get("Synthetica.table.defaultDateRenderer.className", (Component)var1) != null || var4) {
         var5 = var1.getDefaultRenderer(Date.class);
         if (var5 == null || var5 instanceof UIResource && var5 == var2) {
            this.installDefaultTableCellRenderer(var1, Date.class, var3, var2);
         }
      }

      var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultIconRenderer", var1);
      if (SyntheticaLookAndFeel.get("Synthetica.table.defaultIconRenderer.className", (Component)var1) != null || var4) {
         var5 = var1.getDefaultRenderer(Icon.class);
         if (var5 == null || var5 instanceof UIResource && var5 == var2) {
            this.installDefaultTableCellRenderer(var1, Icon.class, var3, var2);
         }
      }

      var4 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultImageIconRenderer", var1);
      if (SyntheticaLookAndFeel.get("Synthetica.table.defaultImageIconRenderer.className", (Component)var1) != null || var4) {
         var5 = var1.getDefaultRenderer(ImageIcon.class);
         if (var5 == null || var5 instanceof UIResource && var5 == var2) {
            this.installDefaultTableCellRenderer(var1, ImageIcon.class, var3, var2);
         }
      }

   }

   private void installDefaultTableCellRenderer(JTable var1, Class var2, String var3, TableCellRenderer var4) {
      try {
         String var5 = (String)SyntheticaLookAndFeel.get(var3, (Component)var1);
         if (var5 == null) {
            var5 = var2 == Boolean.class ? SyntheticaDefaultBooleanTableCellRenderer.class.getName() : SyntheticaDefaultTableCellRenderer.class.getName();
         }

         TableCellRenderer var6 = (TableCellRenderer)Class.forName(var5).getConstructor(TableCellRenderer.class).newInstance(var4);
         var1.setDefaultRenderer(var2, var6);
      } catch (Exception var7) {
         throw new RuntimeException(var7);
      }
   }

   private void installDefaultTableEditors(JTable var1) {
      String var2 = "Synthetica.table.defaultEditor.className";
      boolean var3 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultEditor", var1, true);
      TableCellEditor var4;
      if (SyntheticaLookAndFeel.get(var2, (Component)var1) != null || var3) {
         var4 = var1.getDefaultEditor(Object.class);
         if (var4 == null || var4.getClass().getName().contains(".JTable") || var4.getClass().getName().contains(".swingx.JXTable")) {
            this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TABLE_OBJECT_DEFAULT_EDITOR");
            this.installDefaultTableCellEditor(var1, Object.class, var2, var4);
         }
      }

      var2 = "Synthetica.table.defaultEditor.className";
      var3 = SyntheticaLookAndFeel.getBoolean("Synthetica.table.installDefaultNumberEditor", var1, true);
      if (SyntheticaLookAndFeel.get(var2, (Component)var1) != null || var3) {
         var4 = var1.getDefaultEditor(Number.class);
         if (var4 == null || var4.getClass().getName().contains(".JTable") || var4.getClass().getName().contains(".swingx.table.NumberEditorExt")) {
            this.componentPropertyStore.storeComponentProperty(var1, "SYCP_TABLE_NUMBER_DEFAULT_EDITOR");
            this.installDefaultTableCellEditor(var1, Number.class, var2, var4);
         }
      }

   }

   private void installDefaultTableCellEditor(JTable var1, Class var2, String var3, TableCellEditor var4) {
      try {
         String var5 = (String)SyntheticaLookAndFeel.get(var3, (Component)var1);
         if (var5 == null) {
            var5 = SyntheticaDefaultTableCellEditor.class.getName();
         }

         if (var2 == Number.class) {
            var5 = var5 + "$NumberEditor";
         }

         var1.setDefaultEditor(var2, (TableCellEditor)Class.forName(var5).newInstance());
      } catch (Exception var6) {
         throw new RuntimeException(var6);
      }
   }

   private void installCellRendererHoverListener(JComponent var1) {
      this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
      this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_MOTION_LISTENERS");
      if (this.cellRendererHoverListener == null) {
         this.cellRendererHoverListener = new CellRendererHoverListener((CellRendererHoverListener)null);
      }

      var1.addMouseListener(this.cellRendererHoverListener);
      var1.addMouseMotionListener(this.cellRendererHoverListener);
   }

   private void installFocusListener(Component var1) {
      if (var1.isEnabled()) {
         FocusListener[] var5;
         int var4 = (var5 = var1.getFocusListeners()).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            FocusListener var2 = var5[var3];
            if (var2 instanceof RepaintFocusListener) {
               return;
            }
         }

         this.getComponentPropertyStore().storeComponentProperty(var1, "SYCP_FOCUS_LISTENERS");
         var1.addFocusListener(new RepaintFocusListener((RepaintFocusListener)null));
      }
   }

   private void installTextComponentHoverSupport(final JComponent var1) {
      if (!this.eventListenerExists(var1.getMouseListeners())) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_MOUSE_LISTENERS");
         var1.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent var1x) {
               var1.putClientProperty("Synthetica.MOUSE_OVER", true);
               var1.repaint();
            }

            public void mouseExited(MouseEvent var1x) {
               var1.putClientProperty("Synthetica.MOUSE_OVER", false);
               var1.repaint();
            }
         });
      }

   }

   private void installTextComponentPropertyChangeListener(Component var1) {
      if (!this.eventListenerExists(var1.getPropertyChangeListeners())) {
         this.componentPropertyStore.storeComponentProperty(var1, "SYCP_PROPERTY_CHANGE_LISTENERS");
         var1.addPropertyChangeListener(new RepaintTextComponentPropertyChangeListener((RepaintTextComponentPropertyChangeListener)null));
      }

   }

   private static class CellRendererHoverListener extends MouseAdapter {
      private final String HOVER;

      private CellRendererHoverListener() {
         this.HOVER = "Synthetica.hoverIndex";
      }

      public void mouseExited(MouseEvent var1) {
         JComponent var2 = (JComponent)var1.getSource();
         var2.putClientProperty("Synthetica.hoverIndex", -1);
         var2.repaint();
      }

      public void mouseMoved(MouseEvent var1) {
         this.doHover(var1);
      }

      private void doHover(MouseEvent var1) {
         Object var2 = var1.getSource();
         int var4;
         Rectangle var5;
         Rectangle var6;
         int var9;
         if (var2 instanceof JList) {
            JList var3 = (JList)var1.getSource();
            var4 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.hoverIndex", var3, -1);
            if (var4 >= 0 && var4 < var3.getModel().getSize()) {
               var5 = var3.getCellBounds(var4, var4);
               var3.repaint(var5);
            }

            var9 = var3.locationToIndex(var1.getPoint());
            var3.putClientProperty("Synthetica.hoverIndex", var9);
            var6 = var3.getCellBounds(var9, var9);
            if (var6 != null) {
               var3.repaint(var6);
            }
         } else if (var2 instanceof JTable) {
            JTable var8 = (JTable)var1.getSource();
            if (var8.getColumnCount() <= 0) {
               return;
            }

            var4 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.hoverIndex", var8, -1);
            if (var4 >= 0 && var4 < var8.getRowCount()) {
               var5 = var8.getCellRect(var4, 0, true);
               var6 = var8.getCellRect(var4, var8.getColumnCount() - 1, true);
               if (var5 != null && var6 != null) {
                  var5.width = var6.x + var6.width - var5.x;
                  var8.repaint(var5);
               }
            }

            var9 = var8.rowAtPoint(var1.getPoint());
            var8.putClientProperty("Synthetica.hoverIndex", var9);
            var6 = var8.getCellRect(var9, 0, true);
            Rectangle var7 = var8.getCellRect(var9, var8.getColumnCount() - 1, true);
            if (var6 != null && var7 != null) {
               var6.width = var7.x + var7.width - var6.x;
               var8.repaint(var6);
            }
         } else {
            boolean var10000 = var2 instanceof JTree;
         }

      }

      // $FF: synthetic method
      CellRendererHoverListener(CellRendererHoverListener var1) {
         this();
      }
   }

   private class ComponentProperty {
      static final String OPAQUE = "SYCP_OPAQUE";
      static final String INSETS = "SYCP_INSETS";
      static final String BORDER = "SYCP_BORDER";
      static final String BUTTON_MARGIN = "SYCP_BUTTON_MARGIN";
      static final String BUTTON_HALIGN = "SYCP_BUTTON_HALIGN";
      static final String BUTTON_VALIGN = "SYCP_BUTTON_VALIGN";
      static final String TOOLBAR_SEPARATOR_SIZE = "SYCP_TOOLBAR_SEPARATOR_SIZE";
      static final String PROPERTY_CHANGE_LISTENERS = "SYCP_PROPERTY_CHANGE_LISTENERS";
      static final String COMPONENT_LISTENERS = "SYCP_COMPONENT_LISTENERS";
      static final String MOUSE_LISTENERS = "SYCP_MOUSE_LISTENERS";
      static final String MOUSE_MOTION_LISTENERS = "SYCP_MOUSE_MOTION_LISTENERS";
      static final String CONTAINER_LISTENERS = "SYCP_CONTAINER_LISTENERS";
      static final String POPUPMENU_LISTENERS = "SYCP_POPUPMENU_LISTENERS";
      static final String COMBOBOX_POPUPMENU_LISTENERS = "SYCP_COMBOBOX_POPUPMENU_LISTENERS";
      static final String TREE_CELL_RENDERER = "SYCP_TREE_CELL_RENDERER";
      static final String FOCUS_LISTENERS = "SYCP_FOCUS_LISTENERS";
      static final String LAYOUT_MANAGER = "SYCP_LAYOUT_MANAGER";
      static final String DESKTOP_MANAGER = "SYCP_DESKTOP_MANAGER";
      static final String TITLEDBORDER_TITLEPOSITION = "SYCP_TITLEDBORDER_TITLEPOSITION";
      static final String COMBOBOX_DEFAULT_RENDERER = "SYCP_COMBOBOX_DEFAULT_RENDERER";
      static final String COMBOBOX_DEFAULT_LAYOUT = "SYCP_COMBOBOX_DEFAULT_LAYOUT";
      static final String SPINNER_DEFAULT_LAYOUT = "SYCP_SPINNER_DEFAULT_LAYOUT";
      static final String TABLE_OBJECT_DEFAULT_RENDERER = "SYCP_TABLE_OBJECT_DEFAULT_RENDERER";
      static final String TABLE_BOOLEAN_DEFAULT_RENDERER = "SYCP_TABLE_BOOLEAN_DEFAULT_RENDERER";
      static final String TABLE_OBJECT_DEFAULT_EDITOR = "SYCP_TABLE_OBJECT_DEFAULT_EDITOR";
      static final String TABLE_NUMBER_DEFAULT_EDITOR = "SYCP_TABLE_NUMBER_DEFAULT_EDITOR";
      static final String TABLE_COLUMN_MARGIN = "SYCP_TABLE_COLUMN_MARGIN";
      static final String TABLE_ROW_MARGIN = "SYCP_TABLE_ROW_MARGIN";
      private WeakReference component;
      private String propertyName;
      private WeakReference value;
      private int componentHashCode;

      ComponentProperty(Component var2, String var3, Object var4) {
         this.component = new WeakReference(var2);
         this.propertyName = var3;
         if (var2 instanceof JComponent) {
            ((JComponent)var2).putClientProperty(var3, var4);
         } else {
            this.value = new WeakReference(var4);
         }

         int var5 = 0;

         try {
            var5 = var2.hashCode();
         } catch (RuntimeException var7) {
         }

         this.componentHashCode = (var5 + var3).hashCode();
      }

      public boolean equals(Object var1) {
         return this.componentHashCode == var1.hashCode();
      }

      public int hashCode() {
         return this.componentHashCode;
      }
   }

   public class ComponentPropertyStore {
      private HashSet componentProperties;
      private boolean enabled;
      private Thread cleanerThread;

      ComponentPropertyStore() {
         this.enabled = SyntheticaLookAndFeel.getBoolean("Synthetica.propertyStore.enabled", (Component)null, StyleFactory.componentPropertyStoreEnabled);
         if (this.enabled) {
            this.reinit();
         }

      }

      void reinit() {
         this.stop();
         this.componentProperties = new HashSet(500);
         this.cleanerThread = new Thread() {
            public void run() {
               while(true) {
                  if (!this.isInterrupted()) {
                     synchronized(ComponentPropertyStore.this.componentProperties) {
                        Iterator var2 = ComponentPropertyStore.this.componentProperties.iterator();

                        while(var2.hasNext()) {
                           ComponentProperty var3 = (ComponentProperty)var2.next();
                           if (var3.component.get() == null) {
                              var2.remove();
                           }
                        }
                     }

                     if (!this.isInterrupted()) {
                        try {
                           sleep((long)StyleFactory.cleanerThreadDelay);
                        } catch (InterruptedException var4) {
                           this.interrupt();
                        }
                        continue;
                     }
                  }

                  ComponentPropertyStore.this.componentProperties.clear();
                  return;
               }
            }
         };
         this.cleanerThread.setName("SyntheticaCleanerThread");
         this.cleanerThread.setDaemon(true);
         this.cleanerThread.start();
      }

      void stop() {
         if (this.cleanerThread != null) {
            this.cleanerThread.interrupt();
         }

      }

      boolean removeComponentProperty(Component var1, String var2, Object var3) {
         ComponentProperty var4 = StyleFactory.this.new ComponentProperty(var1, var2, var3);
         return this.componentProperties.remove(var4);
      }

      public void storeComponentProperty(Component var1, String var2) {
         if (this.enabled) {
            Object var3 = null;
            if (var2.equals("SYCP_OPAQUE")) {
               var3 = var1.isOpaque();
            } else if (var2.equals("SYCP_INSETS")) {
               var3 = ((JComponent)var1).getInsets();
            } else if (var2.equals("SYCP_BORDER")) {
               var3 = ((JComponent)var1).getBorder();
            } else if (var2.equals("SYCP_BUTTON_MARGIN")) {
               var3 = ((AbstractButton)var1).getMargin();
            } else if (var2.equals("SYCP_BUTTON_HALIGN")) {
               var3 = ((AbstractButton)var1).getHorizontalAlignment();
            } else if (var2.equals("SYCP_BUTTON_VALIGN")) {
               var3 = ((AbstractButton)var1).getVerticalAlignment();
            } else if (var2.equals("SYCP_TOOLBAR_SEPARATOR_SIZE")) {
               var3 = ((JToolBar.Separator)var1).getSeparatorSize();
            } else if (var2.equals("SYCP_TREE_CELL_RENDERER")) {
               var3 = ((JTree)var1).getCellRenderer();
            } else if (var2.equals("SYCP_LAYOUT_MANAGER")) {
               var3 = ((JComponent)var1).getLayout();
            } else if (var2.equals("SYCP_DESKTOP_MANAGER")) {
               var3 = ((JDesktopPane)var1).getDesktopManager();
            } else if (var2.equals("SYCP_TITLEDBORDER_TITLEPOSITION")) {
               var3 = StyleFactory.this.getTitledBorder(((JComponent)var1).getBorder()).getTitlePosition();
            } else if (var2.equals("SYCP_TABLE_OBJECT_DEFAULT_RENDERER")) {
               var3 = ((JTable)var1).getDefaultRenderer(Object.class);
            } else if (var2.equals("SYCP_TABLE_BOOLEAN_DEFAULT_RENDERER")) {
               var3 = ((JTable)var1).getDefaultRenderer(Boolean.class);
            } else if (var2.equals("SYCP_TABLE_OBJECT_DEFAULT_EDITOR")) {
               var3 = ((JTable)var1).getDefaultEditor(Object.class);
            } else if (var2.equals("SYCP_TABLE_NUMBER_DEFAULT_EDITOR")) {
               var3 = ((JTable)var1).getDefaultEditor(Number.class);
            } else if (var2.equals("SYCP_TABLE_COLUMN_MARGIN")) {
               var3 = ((JTable)var1).getColumnModel().getColumnMargin();
            } else if (var2.equals("SYCP_TABLE_ROW_MARGIN")) {
               var3 = ((JTable)var1).getRowMargin();
            }

            ComponentProperty var4 = StyleFactory.this.new ComponentProperty(var1, var2, var3);
            synchronized(this.componentProperties) {
               if (!this.componentProperties.contains(var4)) {
                  this.componentProperties.add(var4);
               }

            }
         }
      }

      void restoreAllComponentProperties() {
         synchronized(this.componentProperties) {
            Iterator var3 = this.componentProperties.iterator();

            while(true) {
               if (!var3.hasNext()) {
                  this.componentProperties.clear();
                  break;
               }

               ComponentProperty var2 = (ComponentProperty)var3.next();
               this.restoreComponentProperty(var2);
            }
         }

         StyleFactory.this.prepareMetalLAFSwitch = false;
      }

      private void restoreComponentProperty(ComponentProperty var1) {
         Component var2 = (Component)var1.component.get();
         if (var2 != null) {
            String var3 = var1.propertyName;
            Object var4 = null;
            if (var2 instanceof JComponent) {
               var4 = ((JComponent)var2).getClientProperty(var1.propertyName);
            } else {
               var4 = var1.value.get();
            }

            if (var3.equals("SYCP_OPAQUE") && StyleFactory.this.prepareMetalLAFSwitch) {
               StyleFactory.this.setOpaqueDefault4Metal((JComponent)var2);
            } else if (var3.equals("SYCP_INSETS")) {
               ((JComponent)var2).setBorder(new EmptyBorder((Insets)var4));
            } else if (var3.equals("SYCP_BUTTON_MARGIN")) {
               ((AbstractButton)var2).setMargin((Insets)var4);
            } else if (var3.equals("SYCP_BUTTON_HALIGN")) {
               ((AbstractButton)var2).setHorizontalAlignment((Integer)var4);
            } else if (var3.equals("SYCP_BUTTON_VALIGN")) {
               ((AbstractButton)var2).setVerticalAlignment((Integer)var4);
            } else if (var3.equals("SYCP_TOOLBAR_SEPARATOR_SIZE")) {
               Dimension var5 = (Dimension)var4;
               if (var5 == null) {
                  var5 = new Dimension(10, 10);
               }

               ((JToolBar.Separator)var2).setSeparatorSize(var5);
            } else if (var3.equals("SYCP_TREE_CELL_RENDERER")) {
               TreeCellRenderer var7 = (TreeCellRenderer)var4;
               ((JTree)var2).setCellRenderer(var7);
            } else {
               int var6;
               if (var3.equals("SYCP_PROPERTY_CHANGE_LISTENERS")) {
                  PropertyChangeListener[] var8 = var2.getPropertyChangeListeners();

                  for(var6 = 0; var6 < var8.length; ++var6) {
                     if (var8[var6].getClass().getName().contains("synthetica")) {
                        var2.removePropertyChangeListener(var8[var6]);
                     }
                  }
               } else if (var3.equals("SYCP_COMPONENT_LISTENERS")) {
                  ComponentListener[] var9 = var2.getComponentListeners();

                  for(var6 = 0; var6 < var9.length; ++var6) {
                     if (var9[var6].getClass().getName().contains("synthetica")) {
                        var2.removeComponentListener(var9[var6]);
                     }
                  }
               } else if (var3.equals("SYCP_CONTAINER_LISTENERS")) {
                  ContainerListener[] var10 = ((JComponent)var2).getContainerListeners();

                  for(var6 = 0; var6 < var10.length; ++var6) {
                     if (var10[var6].getClass().getName().contains("synthetica")) {
                        ((JComponent)var2).removeContainerListener(var10[var6]);
                     }
                  }
               } else if (var3.equals("SYCP_MOUSE_LISTENERS")) {
                  MouseListener[] var11 = var2.getMouseListeners();

                  for(var6 = 0; var6 < var11.length; ++var6) {
                     if (var11[var6].getClass().getName().contains("synthetica")) {
                        var2.removeMouseListener(var11[var6]);
                     }
                  }
               } else if (var3.equals("SYCP_MOUSE_MOTION_LISTENERS")) {
                  MouseMotionListener[] var12 = var2.getMouseMotionListeners();

                  for(var6 = 0; var6 < var12.length; ++var6) {
                     if (var12[var6].getClass().getName().contains("synthetica")) {
                        var2.removeMouseMotionListener(var12[var6]);
                     }
                  }
               } else if (var3.equals("SYCP_FOCUS_LISTENERS")) {
                  FocusListener[] var13 = var2.getFocusListeners();

                  for(var6 = 0; var6 < var13.length; ++var6) {
                     if (var13[var6].getClass().getName().contains("synthetica")) {
                        var2.removeFocusListener(var13[var6]);
                     }
                  }
               } else {
                  PopupMenuListener[] var14;
                  if (var3.equals("SYCP_POPUPMENU_LISTENERS")) {
                     var14 = ((JPopupMenu)var2).getPopupMenuListeners();

                     for(var6 = 0; var6 < var14.length; ++var6) {
                        if (var14[var6].getClass().getName().contains("synthetica")) {
                           ((JPopupMenu)var2).removePopupMenuListener(var14[var6]);
                        }
                     }
                  } else if (var3.equals("SYCP_COMBOBOX_POPUPMENU_LISTENERS")) {
                     var14 = ((JComboBox)var2).getPopupMenuListeners();

                     for(var6 = 0; var6 < var14.length; ++var6) {
                        if (var14[var6].getClass().getName().contains("synthetica")) {
                           ((JComboBox)var2).removePopupMenuListener(var14[var6]);
                        }
                     }
                  } else {
                     LayoutManager var15;
                     if (var3.equals("SYCP_LAYOUT_MANAGER")) {
                        var15 = (LayoutManager)var4;
                        ((JComponent)var2).setLayout(var15);
                     } else if (var3.equals("SYCP_DESKTOP_MANAGER")) {
                        DesktopManager var16 = (DesktopManager)var4;
                        ((JDesktopPane)var2).setDesktopManager(var16);
                     } else {
                        int var17;
                        if (var3.equals("SYCP_TITLEDBORDER_TITLEPOSITION")) {
                           var17 = (Integer)var4;
                           TitledBorder var20 = StyleFactory.this.getTitledBorder(((JComponent)var2).getBorder());
                           if (var20 != null) {
                              var20.setTitlePosition(var17);
                           }
                        } else if (var3.equals("SYCP_COMBOBOX_DEFAULT_RENDERER")) {
                           ListCellRenderer var18 = (ListCellRenderer)var4;
                           ((JComboBox)var2).setRenderer(var18);
                        } else if (var3.equals("SYCP_COMBOBOX_DEFAULT_LAYOUT")) {
                           var15 = (LayoutManager)var4;
                           ((JComboBox)var2).setLayout(var15);
                        } else if (var3.equals("SYCP_SPINNER_DEFAULT_LAYOUT")) {
                           var15 = (LayoutManager)var4;
                           ((JSpinner)var2).setLayout(var15);
                        } else {
                           TableCellRenderer var19;
                           if (var3.equals("SYCP_TABLE_OBJECT_DEFAULT_RENDERER")) {
                              var19 = (TableCellRenderer)var4;
                              ((JTable)var2).setDefaultRenderer(Object.class, var19);
                           } else if (var3.equals("SYCP_TABLE_BOOLEAN_DEFAULT_RENDERER")) {
                              var19 = (TableCellRenderer)var4;
                              ((JTable)var2).setDefaultRenderer(Boolean.class, var19);
                           } else {
                              TableCellEditor var21;
                              if (var3.equals("SYCP_TABLE_OBJECT_DEFAULT_EDITOR")) {
                                 var21 = (TableCellEditor)var4;
                                 ((JTable)var2).setDefaultEditor(Object.class, var21);
                              } else if (var3.equals("SYCP_TABLE_NUMBER_DEFAULT_EDITOR")) {
                                 var21 = (TableCellEditor)var4;
                                 ((JTable)var2).setDefaultEditor(Number.class, var21);
                              } else if (var3.equals("SYCP_TABLE_COLUMN_MARGIN")) {
                                 var17 = (Integer)var4;
                                 ((JTable)var2).getColumnModel().setColumnMargin(var17);
                              } else if (var3.equals("SYCP_TABLE_ROW_MARGIN")) {
                                 var17 = (Integer)var4;
                                 ((JTable)var2).setRowMargin(var17);
                              }
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   private static class RepaintFocusListener implements FocusListener {
      private RepaintFocusListener() {
      }

      public void focusGained(FocusEvent var1) {
         this.repaintComponent(var1.getComponent());
      }

      public void focusLost(FocusEvent var1) {
         this.repaintComponent(var1.getComponent());
      }

      private void repaintComponent(Component var1) {
         String var2 = var1.getName();
         if ("ComboBox.textField".equals(var2) && var1.getParent() != null) {
            var1.getParent().repaint();
         } else if ("Spinner.formattedTextField".equals(var2) && var1.getParent() != null && var1.getParent().getParent() != null) {
            var1.getParent().getParent().repaint();
         } else if (var1.getParent() != null && var1.getParent().getParent() != null && var1.getParent().getParent() instanceof JScrollPane) {
            JScrollPane var3 = (JScrollPane)var1.getParent().getParent();
            var3.repaint();
         } else {
            var1.repaint();
         }

      }

      // $FF: synthetic method
      RepaintFocusListener(RepaintFocusListener var1) {
         this();
      }
   }

   private static class RepaintTextComponentPropertyChangeListener implements PropertyChangeListener {
      private RepaintTextComponentPropertyChangeListener() {
      }

      public void propertyChange(PropertyChangeEvent var1) {
         JComponent var2 = (JComponent)var1.getSource();
         String var3 = var1.getPropertyName();
         if (var2.getParent() != null && var2.getParent() instanceof JViewport && ("enabled".equals(var3) || "background".equals(var3) || "Synthetica.MOUSE_OVER".equals(var3))) {
            var2.getParent().getParent().repaint();
         }

      }

      // $FF: synthetic method
      RepaintTextComponentPropertyChangeListener(RepaintTextComponentPropertyChangeListener var1) {
         this();
      }
   }

   private static class SyntheticaComboPopupBorder implements Border {
      private SyntheticaComboPopupBorder() {
      }

      public Insets getBorderInsets(Component var1) {
         return UIManager.getInsets("Synthetica.comboPopup.insets");
      }

      public boolean isBorderOpaque() {
         return false;
      }

      public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
         Color var7 = UIManager.getColor("Synthetica.comboPopup.border.color");
         if (var7 != null && this.getBorderInsets(var1).equals(new Insets(1, 1, 1, 1))) {
            var2.setColor(var7);
            var2.drawRect(var3, var4, var5 - 1, var6 - 1);
         }

      }

      // $FF: synthetic method
      SyntheticaComboPopupBorder(SyntheticaComboPopupBorder var1) {
         this();
      }
   }
}
