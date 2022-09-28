package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.StyleFactory;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.ImageObserver;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.WeakHashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class TabbedPanePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.TabbedPanePainter";
   public static final int UNDEFINED = -1;
   public static final int LEFT = 0;
   public static final int RIGHT = 1;
   public static final int LEFT_RIGHT = 2;
   public static final int TOP = 3;
   public static final int BOTTOM = 4;
   public static final int TOP_BOTTOM = 5;
   private static final String FOCUS_PLACEMENT_KEY = "JTabbedPane.focusPlacement";
   private static WeakHashMap translucentTabbedPanes = new WeakHashMap();
   private static final String TRANSLUCENT_KEY = "Synthetica.childsAreTranslucent";
   private static HashMap imgCache = new HashMap();

   protected TabbedPanePainter() {
   }

   public static TabbedPanePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static TabbedPanePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, TabbedPanePainter.class, "Synthetica.TabbedPanePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, TabbedPanePainter.class, "Synthetica.TabbedPanePainter");
      }

      return (TabbedPanePainter)var1;
   }

   public static void reinitialize() {
      Iterator var1 = translucentTabbedPanes.keySet().iterator();

      while(var1.hasNext()) {
         JComponent var0 = (JComponent)var1.next();
         var0.putClientProperty("Synthetica.childsAreTranslucent", false);
      }

      translucentTabbedPanes = new WeakHashMap();
   }

   public void paintTabbedPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTabbedPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTabbedPaneContentBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JTabbedPane var7 = (JTabbedPane)var1.getComponent();
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.contentBorder.paintBackground", var7, false)) {
         int var8 = var7.getTabPlacement();
         SyntheticaState var9 = new SyntheticaState(var1.getComponentState());
         String var10 = this.getContentBorderImagePath(var7, var9, var8);
         UIKey var11 = new UIKey("tabbedPane.contentBorder.image", var9, -1, var8, -1);
         Insets var12 = (Insets)UIKey.findProperty((JComponent)var7, var11.get(), "sourceInsets", true, 1);
         Insets var13 = (Insets)var12.clone();
         ImagePainter var14 = new ImagePainter(var7, (String)null, -1, -1, -1, -1, var2, var3, var4, var5, var6, var10, var12, var13, 0, 0, this.flipOnRTL(var7), false);
         var14.drawCenter();
      }
   }

   public void paintTabbedPaneContentBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JTabbedPane var7 = (JTabbedPane)var1.getComponent();
      var2.setColor(SyntheticaLookAndFeel.getColor("Synthetica.tabbedPane.hideBorderColor", var7));
      this.modifyChildOpacity(var1);
      this.paintTabbedPaneContentBorder(var7, new SyntheticaState(var1.getComponentState()), var7.getTabPlacement(), true, var2, var3, var4, var5, var6);
   }

   public void paintTabbedPaneContentBorder(JComponent var1, SyntheticaState var2, int var3, boolean var4, Graphics var5, int var6, int var7, int var8, int var9) {
      this.paintTabbedPaneContentBorder(var1, var2, var3, var4, 0, var5, var6, var7, var8, var9);
   }

   public void paintTabbedPaneContentBorder(JComponent var1, SyntheticaState var2, int var3, boolean var4, int var5, Graphics var6, int var7, int var8, int var9, int var10) {
      String var11 = this.getContentBorderImagePath(var1, var2, var3);
      UIKey var12 = new UIKey("tabbedPane.contentBorder.image", var2, -1, var3, -1);
      Insets var13 = (Insets)UIKey.findProperty((JComponent)var1, var12.get(), "sourceInsets", true, 1);
      if (var13 == null) {
         var13 = new Insets(0, 0, 0, 0);
      }

      Insets var14 = (Insets)var13.clone();
      int var15 = SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.contentBorder.horizontalTiled", var1, false) ? 1 : 0;
      int var16 = SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.contentBorder.verticalTiled", var1, false) ? 1 : 0;
      ImagePainter var17 = new ImagePainter(var1, (String)null, -1, -1, -1, -1, var6, var7, var8, var9, var10, var11, var13, var14, var15, var16, this.flipOnRTL(var1), false);
      var17.drawBorder();
      if (var4 && var1 instanceof JTabbedPane && !SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.selected.paintOverContentBorder.disabled", var1)) {
         this.drawContentBorderLine4SelectedTab((JTabbedPane)var1, var3, var5, var6, var7, var8, var9, var10);
      }

   }

   private String getContentBorderImagePath(JComponent var1, SyntheticaState var2, int var3) {
      boolean var4 = var1.getComponentOrientation().isLeftToRight();
      if (var3 == 2 && !var4) {
         var3 = 4;
      } else if (var3 == 4 && !var4) {
         var3 = 2;
      }

      UIKey var5 = new UIKey("tabbedPane.contentBorder.image", var2, -1, var3, -1);
      return (String)UIKey.findProperty((JComponent)var1, var5.get(), (String)null, true, 1);
   }

   protected void modifyChildOpacity(SynthContext var1) {
      JComponent var2 = var1.getComponent();
      boolean var3 = var2.getClientProperty("Synthetica.childsAreTranslucent") == null ? false : (Boolean)var2.getClientProperty("Synthetica.childsAreTranslucent");
      if (!SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.keepOpacity", var2) && !var3 && !var2.getParent().getClass().getName().equals("org.flexdock.view.Viewport")) {
         var2.putClientProperty("Synthetica.childsAreTranslucent", Boolean.TRUE);
         translucentTabbedPanes.put(var2, (Object)null);
         Component[] var4 = var2.getComponents();
         Component[] var8 = var4;
         int var7 = var4.length;

         for(int var6 = 0; var6 < var7; ++var6) {
            Component var5 = var8[var6];
            if (var5 instanceof Container && !(var5 instanceof Window) && !(var5 instanceof JRootPane)) {
               setComponentsTranslucent((Container)var5, true);
            }
         }
      }

   }

   public static void setAllNonOpaque(Container var0) {
      setComponentsTranslucent(var0, false);
   }

   private static void setComponentsTranslucent(Container var0, boolean var1) {
      StyleFactory var2 = var1 ? (StyleFactory)SynthLookAndFeel.getStyleFactory() : null;
      boolean var3 = false;
      boolean var4 = false;
      if (var0 instanceof JComponent) {
         var3 = ((JComponent)var0).getClientProperty("Synthetica.tabbedPane.keepOpacity") == null ? false : (Boolean)((JComponent)var0).getClientProperty("Synthetica.tabbedPane.keepOpacity");
         var4 = ((JComponent)var0).getClientProperty("Synthetica.tabbedPane.keepChildrenOpacity") == null ? false : (Boolean)((JComponent)var0).getClientProperty("Synthetica.tabbedPane.keepChildrenOpacity");
      }

      if ((var0 instanceof JPanel || var0 instanceof JScrollPane || var0 instanceof JViewport) && !var3) {
         if (var2 != null) {
            var2.getComponentPropertyStore().storeComponentProperty(var0, "SYCP_OPAQUE");
         }

         if (!SyntheticaLookAndFeel.isWindowOpacityEnabled((Window)null)) {
            ((JComponent)var0).setDoubleBuffered(false);
         }

         ((JComponent)var0).setOpaque(false);
      } else if (var0 instanceof JComponent && !var3) {
         JComponent var5 = (JComponent)var0;
         if ((var5 instanceof JRadioButton || var5 instanceof JCheckBox || var5 instanceof JTextArea && !(var5.getParent() instanceof JViewport)) && (var5.getBackground() == null || var5.getBackground() instanceof ColorUIResource)) {
            if (var2 != null) {
               var2.getComponentPropertyStore().storeComponentProperty(var5, "SYCP_OPAQUE");
            }

            if (!SyntheticaLookAndFeel.isWindowOpacityEnabled((Window)null)) {
               var5.setDoubleBuffered(false);
            }

            var5.setOpaque(false);
         }
      }

      if (!var4) {
         Component[] var10 = var0.getComponents();
         Component[] var9 = var10;
         int var8 = var10.length;

         for(int var7 = 0; var7 < var8; ++var7) {
            Component var6 = var9[var7];
            if (var6 instanceof Container && !(var6 instanceof Window) && !(var6 instanceof JRootPane)) {
               setComponentsTranslucent((Container)var6, var1);
            }
         }

      }
   }

   public void paintTabbedPaneTabAreaBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTabbedPaneTabAreaBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JTabbedPane var7 = (JTabbedPane)var1.getComponent();
      this.paintTabbedPaneTabAreaBackground(var7, new SyntheticaPainterState(var1), var7.getTabPlacement(), 0, var2, var3, var4, var5, var6);
   }

   public void paintTabbedPaneTabAreaBackground(JComponent var1, SyntheticaState var2, int var3, int var4, Graphics var5, int var6, int var7, int var8, int var9) {
      boolean var10 = var1.getComponentOrientation().isLeftToRight();
      if (var3 == 2 && !var10) {
         var3 = 4;
      } else if (var3 == 4 && !var10) {
         var3 = 2;
      }

      UIKey var11 = new UIKey("tabbedPane.tabArea.background.image", var2, -1, var3, -1);
      Insets var12 = SyntheticaLookAndFeel.getInsets(var11.get("insets"), var1);
      String var14 = SyntheticaLookAndFeel.getString(var11.get(), var1);
      if (var14 != null) {
         int var15 = SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tabArea.background.horizontalTiled", var1, false) ? 1 : 0;
         int var16 = SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tabArea.background.verticalTiled", var1, false) ? 1 : 0;
         ImagePainter var17 = new ImagePainter(var1, (String)null, -1, -1, -1, -1, var5, var6, var7, var8, var9, var14, var12, var12, var15, var16, this.flipOnRTL(var1), false);
         var17.setAngle(var4);
         var17.draw();
      }

   }

   public void paintTabbedPaneTabBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      JTabbedPane var8 = (JTabbedPane)var1.getComponent();
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.selected.bold", var8) && var8.getSelectedIndex() == var7 && var8.hasFocus()) {
         JButton var9 = new JButton();
         SynthStyle var10 = SynthLookAndFeel.getStyle(var9, Region.BUTTON);
         var1 = new SynthContext(var9, Region.BUTTON, var10, 256);
         var10.getPainter(var1).paintButtonBorder(var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintTabbedPaneTab(JComponent var1, SyntheticaState var2, int var3, int var4, int var5, int var6, Graphics var7, int var8, int var9, int var10, int var11) {
      Color var12 = (Color)var1.getClientProperty("Synthetica.background");
      Float var13 = (Float)var1.getClientProperty("Synthetica.background.alpha");
      String var14 = "Synthetica.tabbedPane.tab." + var3 + ".background";
      Color var15 = (Color)var1.getClientProperty(var14);
      if (var15 == null && var1 instanceof JTabbedPane && !(((JTabbedPane)var1).getBackgroundAt(var3) instanceof UIResource)) {
         var15 = ((JTabbedPane)var1).getBackgroundAt(var3);
      }

      if (var15 != null) {
         var1.putClientProperty("Synthetica.background", var15);
         if (var1.getClientProperty(var14 + ".alpha") != null) {
            var1.putClientProperty("Synthetica.background.alpha", var1.getClientProperty(var14 + ".alpha"));
         }
      }

      String var16 = var4 + "/" + var3;
      String var17 = null;
      Insets var18 = null;
      String var19 = "tabbedPane.tab";
      if (var2.isSet(SyntheticaState.State.HOVER) && !var2.isSet(SyntheticaState.State.SELECTED) && !var2.isSet(SyntheticaState.State.DISABLED)) {
         var19 = var19 + ".hover";
      } else if (var2.isSet(SyntheticaState.State.SELECTED)) {
         var19 = var19 + ".selected";
      } else if (var2.isSet(SyntheticaState.State.DISABLED)) {
         var19 = var19 + ".disabled";
      }

      if (var4 == 1) {
         var19 = var19 + ".top";
      } else if (var4 == 2) {
         var19 = var19 + ".left";
      } else if (var4 == 3) {
         var19 = var19 + ".bottom";
      } else if (var4 == 4) {
         var19 = var19 + ".right";
      }

      String var20 = SyntheticaLookAndFeel.getStyleName(var1);
      var17 = SyntheticaLookAndFeel.getString(var19, (String)null, var20, true);
      var18 = SyntheticaLookAndFeel.getInsets(var19, "insets", var20, true);
      if (var17 == null) {
         if (var2.isSet(SyntheticaState.State.SELECTED)) {
            var17 = this.getTabImagePath(var1, var4, var5, ".selected");
            if (var17 == null) {
               if (var4 != 1 && var4 != 3) {
                  var17 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.y.image.selected", var1);
               } else {
                  var17 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.x.image.selected", var1);
               }
            }
         } else if (var2.isSet(SyntheticaState.State.DISABLED)) {
            var17 = this.getTabImagePath(var1, var4, var5, ".disabled");
         } else {
            if (var2.isSet(SyntheticaState.State.HOVER)) {
               var17 = this.getTabImagePath(var1, var4, var5, ".hover");
            }

            if (var17 == null) {
               var17 = this.getTabImagePath(var1, var4, var5, (String)null);
            }
         }
      }

      Insets var21 = SyntheticaLookAndFeel.getInsets("Synthetica.tabbedPane.tab.noneSelected.background.insets", var1);
      if (!var2.isSet(SyntheticaState.State.SELECTED) && var21 != null) {
         if (var4 == 1) {
            var9 += var21.top;
         } else if (var4 == 3) {
            var11 -= var21.bottom;
         } else if (var4 == 2) {
            var8 += var21.left;
         } else if (var4 == 4) {
            if (var6 == 0) {
               var10 -= var21.right;
            } else {
               var11 -= var21.right;
            }
         }
      }

      if (var4 == 3) {
         var18 = new Insets(var18.bottom, var18.left, var18.top, var18.right);
      }

      if (var4 == 4) {
         var18 = new Insets(var18.top, var18.right, var18.bottom, var18.left);
      }

      if (var18 == null) {
         var18 = new Insets(0, 0, 0, 0);
      }

      Insets var22 = (Insets)var18.clone();
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.removeDoubleBorder", var1)) {
         if ((var4 == 1 || var4 == 3) && (var5 == 0 || var5 == -1)) {
            var22.right = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.doubleBorderXCorrection", var1, 0);
         } else if (var4 == 2 && (var5 == 4 || var5 == -1) && var6 != 0) {
            var22.right = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.doubleBorderYCorrection", var1, 0);
         } else if (var4 == 4 && (var5 == 3 || var5 == -1) && var6 != 0) {
            var22.right = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.doubleBorderYCorrection", var1, 0);
         } else if ((var4 == 2 || var4 == 4) && (var5 == 3 || var5 == -1)) {
            var22.bottom = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.doubleBorderYCorrection", var1, 0);
         }
      }

      if (var4 == 1) {
         var22.bottom = 0;
      } else if (var4 == 2) {
         if (var6 == 0) {
            var22.right = 0;
         } else {
            var22.bottom = 0;
         }
      } else if (var4 == 3) {
         var22.top = 0;
      } else if (var4 == 4) {
         if (var6 == 0) {
            var22.left = 0;
         } else {
            var22.bottom = 0;
         }
      }

      boolean var23 = SyntheticaLookAndFeel.getBoolean("Synthetica.tabbePane.tab.imageClippingEnabled", var1, SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.imageClippingEnabled", var1, true));
      ImagePainter var24;
      int var29;
      int var30;
      int var31;
      if (var2.isSet(SyntheticaState.State.HOVER) && !var2.isSet(SyntheticaState.State.DISABLED) && !var2.isSet(SyntheticaState.State.SELECTED) && SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.hover.red", var1, Integer.MIN_VALUE) != Integer.MIN_VALUE) {
         var29 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.hover.red", var1);
         var30 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.hover.green", var1);
         var31 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.hover.blue", var1);
         Insets var28 = SyntheticaLookAndFeel.getInsets("Synthetica.tabbedPane.tab.hover.insets", var1);
         if (var28 == null) {
            var28 = new Insets(0, 0, 0, 0);
         }

         var24 = new ImagePainter(var1, var29, var30, var31, var28, var7, var8, var9, var10, var11, var17, var18, var23 ? var22 : var18, 0, 0, this.flipOnRTL(var1), false);
      } else {
         boolean var25 = false;
         boolean var26 = false;
         boolean var27 = false;
         if (var2.isSet(SyntheticaState.State.HOVER)) {
            var30 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.hover.animation.cycles", var1, 1);
            var29 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.hover.animation.delay", var1, 50);
            var31 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.hover.animation.type", var1, 1);
         } else {
            var30 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.animation.cycles", var1, 1);
            var29 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.animation.delay", var1, 50);
            var31 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tab.animation.type", var1, 2);
         }

         var24 = new ImagePainter(var1, var16, var30, var29, var31, var2.getState(), var7, var8, var9, var10, var11, var17, var18, var23 ? var22 : var18, 0, 0, this.flipOnRTL(var1), false);
      }

      var24.setAngle(var6);
      if (!var23) {
         var24.draw();
      } else {
         if (var4 != 3) {
            var24.drawTopLeft();
            var24.drawTopCenter();
            var24.drawTopRight();
         }

         if (var4 != 4 || var6 != 0) {
            var24.drawLeft();
         }

         if (var4 != 1) {
            var24.drawBottomLeft();
            var24.drawBottomCenter();
            var24.drawBottomRight();
         }

         if (var4 != 2 || var6 != 0) {
            var24.drawRight();
         }

         var24.drawCenter();
      }

      var1.putClientProperty("Synthetica.background", var12);
      var1.putClientProperty("Synthetica.background.alpha", var13);
      if (var2.isSet(SyntheticaState.State.FOCUSED)) {
         var29 = var1.getClientProperty("JTabbedPane.focusPlacement") == null ? -1 : (Integer)var1.getClientProperty("JTabbedPane.focusPlacement");
         if (var29 != -1) {
            var4 = var29;
         }

         String var32 = "";
         if (var4 == 1) {
            var32 = "focus.tabbedPane.tab.top";
         } else if (var4 == 3) {
            var32 = "focus.tabbedPane.tab.bottom";
         } else if (var4 == 2) {
            var32 = "focus.tabbedPane.tab.left";
         } else if (var4 == 4) {
            var32 = "focus.tabbedPane.tab.right";
         }

         FocusPainter.paintFocus(var32, var1, var2.getState(), var16, var6, var7, var8, var9, var10, var11);
      }

   }

   private String getTabImagePath(JComponent var1, int var2, int var3, String var4) {
      String var5 = null;
      if (var4 == null) {
         var4 = "";
      }

      if (var3 == 0) {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.left.image" + var4, var1);
      } else if (var3 == 1) {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.right.image" + var4, var1);
      } else if (var3 == 2) {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.leftRight.image" + var4, var1);
      } else if (var3 == 3) {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.top.image" + var4, var1);
      } else if (var3 == 4) {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.bottom.image" + var4, var1);
      } else if (var3 == 5) {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.topBottom.image" + var4, var1);
      } else if (var2 != 1 && var2 != 3) {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.center.image" + var4, var1);
      } else {
         var5 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.middle.image" + var4, var1);
      }

      return var5;
   }

   public void paintTabbedPaneTabBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      JTabbedPane var8 = (JTabbedPane)var1.getComponent();
      int var9 = var8.getTabPlacement();
      int var10 = this.getTabPosition(var8, var7);
      SyntheticaState var11 = new SyntheticaState(var1.getComponentState());
      Integer var12 = (Integer)var8.getClientProperty("Synthetica.MOUSE_OVER");
      int var13 = var12 == null ? -1 : var12;
      if (var13 == var7) {
         var11.setState(SyntheticaState.State.HOVER);
      }

      if (!var8.isEnabledAt(var7)) {
         var11.setState(SyntheticaState.State.DISABLED);
      }

      this.paintTabbedPaneTab(var8, var11, var7, var9, var10, 0, var2, var3, var4, var5, var6);
   }

   protected int getTabPosition(JTabbedPane var1, int var2) {
      if (var2 < 0) {
         return -1;
      } else {
         boolean var3 = false;
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         TreeMap var7;
         TreeMap var8;
         int var9;
         if (var1.getTabPlacement() != 1 && var1.getTabPlacement() != 3) {
            var7 = new TreeMap(new Comparator() {
               public int compare(Object var1, Object var2) {
                  Rectangle var3 = (Rectangle)var1;
                  Rectangle var4 = (Rectangle)var2;
                  if (var3.y > var4.y) {
                     return 1;
                  } else {
                     return var3.y < var4.y ? -1 : 0;
                  }
               }
            });
            var8 = new TreeMap(new Comparator() {
               public int compare(Object var1, Object var2) {
                  Rectangle var3 = (Rectangle)var1;
                  Rectangle var4 = (Rectangle)var2;
                  if (var3.y + var3.height < var4.y + var4.height) {
                     return 1;
                  } else {
                     return var3.y + var3.height > var4.y + var4.height ? -1 : 0;
                  }
               }
            });

            for(var9 = 0; var9 < var1.getTabCount(); ++var9) {
               var7.put(var1.getBoundsAt(var9), new Integer(var9));
            }

            if (var7.headMap(var1.getBoundsAt(var2)).size() == 0) {
               var5 = true;
            }

            for(var9 = 0; var9 < var1.getTabCount(); ++var9) {
               var8.put(var1.getBoundsAt(var9), new Integer(var9));
            }

            if (var8.headMap(var1.getBoundsAt(var2)).size() == 0) {
               var6 = true;
            }

            if (var5 && var6) {
               return 5;
            }

            if (var5) {
               return 3;
            }

            if (var6) {
               return 4;
            }
         } else {
            var7 = new TreeMap(new Comparator() {
               public int compare(Object var1, Object var2) {
                  Rectangle var3 = (Rectangle)var1;
                  Rectangle var4 = (Rectangle)var2;
                  if (var3.x > var4.x) {
                     return 1;
                  } else {
                     return var3.x < var4.x ? -1 : 0;
                  }
               }
            });
            var8 = new TreeMap(new Comparator() {
               public int compare(Object var1, Object var2) {
                  Rectangle var3 = (Rectangle)var1;
                  Rectangle var4 = (Rectangle)var2;
                  if (var3.x + var3.width < var4.x + var4.width) {
                     return 1;
                  } else {
                     return var3.x + var3.width > var4.x + var4.width ? -1 : 0;
                  }
               }
            });

            for(var9 = 0; var9 < var1.getTabCount(); ++var9) {
               var7.put(var1.getBoundsAt(var9), new Integer(var9));
            }

            if (var7.headMap(var1.getBoundsAt(var2)).size() == 0) {
               var3 = true;
            }

            for(var9 = 0; var9 < var1.getTabCount(); ++var9) {
               var8.put(var1.getBoundsAt(var9), new Integer(var9));
            }

            if (var8.headMap(var1.getBoundsAt(var2)).size() == 0) {
               var4 = true;
            }

            if (var3 && var4) {
               return 2;
            }

            if (var3) {
               return 0;
            }

            if (var4) {
               return 1;
            }
         }

         return -1;
      }
   }

   private void drawContentBorderLine4SelectedTab(JTabbedPane var1, int var2, int var3, Graphics var4, int var5, int var6, int var7, int var8) {
      if (SyntheticaLookAndFeel.get("Synthetica.tabbedPane.rotateTabRuns", (Component)var1) == null || SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.rotateTabRuns", var1) || this.getSelectedRun(var1) == 0) {
         String var9;
         if (var2 == 1 && SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.top", var1) != null) {
            var9 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.top", var1);
         } else if (var2 == 3 && SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.bottom", var1) != null) {
            var9 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.bottom", var1);
         } else if (var2 == 2 && SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.left", var1) != null) {
            var9 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.left", var1);
         } else if (var2 == 4 && SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.right", var1) != null) {
            var9 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.selected.right", var1);
         } else {
            int var10 = this.getTabPosition(var1, var1.getSelectedIndex());
            var9 = this.getTabImagePath(var1, var2, var10, ".selected");
            if (var9 == null) {
               if (var2 != 1 && var2 != 3) {
                  var9 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.y.image.selected", var1);
               } else {
                  var9 = SyntheticaLookAndFeel.getString("Synthetica.tabbedPane.tab.x.image.selected", var1);
               }
            }
         }

         if (var9 != null) {
            Image var19 = (Image)imgCache.get(var9);
            if (var19 == null) {
               var19 = (new ImageIcon(SyntheticaLookAndFeel.class.getResource(var9))).getImage();
               imgCache.put(var9, var19);
            }

            byte var11 = 0;
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.removeDoubleBorder", var1)) {
               boolean var12 = false;
               int var13 = this.getTabPosition(var1, var1.getSelectedIndex());
               if ((var2 == 1 || var2 == 3) && var13 == 1) {
                  var12 = true;
               } else if (var3 != 0 && var2 == 4 && var13 == 4) {
                  var12 = true;
               } else if (var3 != 0 && var2 == 2 && var13 == 3) {
                  var12 = true;
               }

               if (!var12) {
                  var11 = 1;
               }
            }

            int var21 = var1.getSelectedIndex();
            if (var21 != -1) {
               Rectangle var20 = (Rectangle)var1.getClientProperty("JTabbedPane.visibleSelectedTabRect");
               if (var20 == null) {
                  var20 = var1.getBoundsAt(var21);
               }

               String var14 = "tabbedPane.tab.selected";
               if (var2 == 1) {
                  var14 = var14 + ".top";
               } else if (var2 == 2) {
                  var14 = var14 + ".left";
               } else if (var2 == 3) {
                  var14 = var14 + ".bottom";
               } else if (var2 == 4) {
                  var14 = var14 + ".right";
               }

               Insets var15 = SyntheticaLookAndFeel.getInsets(var14, "insets", SyntheticaLookAndFeel.getStyleName(var1), true);
               var15 = (Insets)var15.clone();
               if (var20.width >= var15.left + var15.right && var20.height >= var15.top + var15.bottom) {
                  if (var2 == 3) {
                     var15 = new Insets(var15.bottom, var15.left, var15.top, var15.right);
                  } else if (var2 == 4) {
                     var15 = new Insets(var15.top, var15.right, var15.bottom, var15.left);
                  }

                  Insets var16 = new Insets(0, 0, 0, 0);
                  boolean var17 = SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.selected.useFillAsContentBorder", var1, true);
                  if (var2 == 1) {
                     var15.top = var19.getHeight((ImageObserver)null) + (var17 ? -var15.bottom - 1 : 0);
                     var16.left = var15.left;
                     var16.right = var15.right - var11;
                     var5 = var20.x;
                     var7 = var20.width + var11;
                     var8 = 1;
                  } else if (var2 == 3) {
                     var15.bottom = var19.getHeight((ImageObserver)null) + (var17 ? -var15.top - 1 : 0);
                     var16.left = var15.left;
                     var16.right = var15.right - var11;
                     var5 = var20.x;
                     var6 = var6 + var8 - 1;
                     var7 = var20.width + var11;
                     var8 = 1;
                  } else if (var2 == 2) {
                     if (var3 == 0) {
                        var15.left = var19.getWidth((ImageObserver)null) + (var17 ? -var15.right - 1 : 0);
                        var16.top = var15.top;
                        var16.bottom = var15.bottom - var11;
                     } else {
                        var15.top = var19.getHeight((ImageObserver)null) + (var17 ? -var15.bottom - 1 : 0);
                        var16.left = var15.left;
                        var16.right = var15.right - var11;
                     }

                     var5 = var20.x + var20.width;
                     var6 = var3 == 0 ? var20.y : var20.y - var11;
                     var7 = var3 == 0 ? 1 : var20.height + var11;
                     var8 = var3 == 0 ? var20.height + var11 : 1;
                  } else if (var2 == 4) {
                     if (var3 == 0) {
                        var15.right = var19.getWidth((ImageObserver)null) + (var17 ? -var15.left - 1 : 0);
                        var16.top = var15.top;
                        var16.bottom = var15.bottom - var11;
                     } else {
                        var15.top = var19.getHeight((ImageObserver)null) + (var17 ? -var15.bottom - 1 : 0);
                        var16.left = var15.left;
                        var16.right = var15.right - var11;
                     }

                     var5 = var20.x - 1;
                     var6 = var20.y;
                     var7 = var3 == 0 ? 1 : var20.height + var11;
                     var8 = var3 == 0 ? var20.height + var11 : 1;
                  }

                  ImagePainter var18 = new ImagePainter(var1, (String)null, -1, -1, -1, -1, var4, var5, var6, var7, var8, var9, var15, var16, 0, 0, this.flipOnRTL(var1), false);
                  var18.setAngle(var3);
                  if (var3 == 0 && var2 != 1 && var2 != 3) {
                     var18.drawTopCenter();
                     var18.drawBottomCenter();
                  } else {
                     var18.drawLeft();
                     var18.drawRight();
                  }

                  var18.drawCenter();
               }
            }
         }
      }
   }

   public boolean paintTabbedPaneTabText(SynthContext var1, Graphics var2, String var3, int var4, int var5, int var6) {
      return true;
   }

   protected boolean flipOnRTL(JComponent var1) {
      return var1.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT && SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.horizontalFlipOnRTL.enabled", var1);
   }

   protected int getSelectedRun(JTabbedPane var1) {
      if (!(var1.getUI() instanceof BasicTabbedPaneUI)) {
         return -1;
      } else {
         BasicTabbedPaneUI var2 = (BasicTabbedPaneUI)var1.getUI();
         int var3 = var1.getSelectedIndex();
         int var4 = var1.getTabCount();
         if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
            try {
               Class var5 = Class.forName("javax.swing.plaf.basic.BasicTabbedPaneUI");
               Method var6 = var5.getDeclaredMethod("getRunForTab", Integer.TYPE, Integer.TYPE);
               var6.setAccessible(true);
               return (Integer)var6.invoke(var2, var4, var3);
            } catch (Exception var7) {
               throw new RuntimeException(var7);
            }
         } else {
            return -1;
         }
      }
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return !var1.equals("paintTabbedPaneBorder") && !var1.equals("paintTabbedPaneBackground") && !var1.equals("paintTabbedPaneContentBorder") && !var1.equals("paintTabbedPaneContentBackground") ? super.getCacheScaleType(var1) : Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      if (!var5.equals("paintTabbedPaneBorder") && !var5.equals("paintTabbedPaneBackground") && !var5.equals("paintTabbedPaneContentBorder") && !var5.equals("paintTabbedPaneContentBackground")) {
         JTabbedPane var6 = (JTabbedPane)var1.getComponent();
         int var7 = super.getCacheHash(var1, var2, var3, var4, var5);
         if (var5.equals("paintTabbedPaneTabBackground") || var5.equals("paintTabbedPaneTabBorder")) {
            var7 = var7 * 31 + var6.getTabPlacement();
            var7 = var7 * 31 + this.getTabPosition(var6, var4);
            Integer var8 = (Integer)var6.getClientProperty("Synthetica.MOUSE_OVER");
            var7 = var7 * 31 + (var8 == null ? 1000 : var8);
         }

         return var7;
      } else {
         return -1;
      }
   }
}
