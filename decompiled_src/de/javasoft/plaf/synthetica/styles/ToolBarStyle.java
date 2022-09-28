package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.JavaVersion;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class ToolBarStyle extends StyleWrapper {
   private static final String ORG_FILLERS = "Synthetica.toolBarStyle.orgFillers";
   private static final String ORG_X_ALIGNMENTS = "Synthetica.toolBarStyle.orgXAlignments";
   private static final String ORG_Y_ALIGNMENTS = "Synthetica.toolBarStyle.orgYAlignments";
   private static final String ORG_MARGIN = "Synthetica.toolBarStyle.orgMargin";
   private static final String ORG_LAYOUT_MANAGER = "Synthetica.toolBarStyle.orgLayoutManager";
   private static final String ORIENTATION_LISTENER = "Synthetica.toolBarStyle.orientationListener";
   private static final String CONTAINER_LISTENER = "Synthetica.toolBarStyle.containerListener";
   private static ToolBarStyle instance = new ToolBarStyle();

   private ToolBarStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ToolBarStyle var3 = new ToolBarStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public void installDefaults(SynthContext var1) {
      this.synthStyle.installDefaults(var1);
      JToolBar var2 = (JToolBar)var1.getComponent();
      if (var2.getClientProperty("Synthetica.toolBarStyle.orgFillers") == null) {
         var2.putClientProperty("Synthetica.toolBarStyle.orgFillers", new HashMap());
      }

      HashMap var3 = (HashMap)var2.getClientProperty("Synthetica.toolBarStyle.orgFillers");
      if (var2.getClientProperty("Synthetica.toolBarStyle.orgXAlignments") == null) {
         var2.putClientProperty("Synthetica.toolBarStyle.orgXAlignments", new HashMap());
      }

      HashMap var4 = (HashMap)var2.getClientProperty("Synthetica.toolBarStyle.orgXAlignments");
      if (var2.getClientProperty("Synthetica.toolBarStyle.orgYAlignments") == null) {
         var2.putClientProperty("Synthetica.toolBarStyle.orgYAlignments", new HashMap());
      }

      HashMap var5 = (HashMap)var2.getClientProperty("Synthetica.toolBarStyle.orgYAlignments");
      var2.putClientProperty("Synthetica.toolBarStyle.orgLayoutManager", var2.getLayout());
      var2.putClientProperty("Synthetica.toolBarStyle.orgMargin", var2.getMargin());
      var2.addPropertyChangeListener(this.getOrientationListener(var2));
      Component[] var6 = var2.getComponents();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         Component var8 = var6[var7];
         var4.put(var8, var8.getAlignmentX());
         var5.put(var8, var8.getAlignmentY());
         if (var8 instanceof Box.Filler) {
            var3.put(var7, var8);
         }

         ((JComponent)var8).setAlignmentX(0.5F);
         ((JComponent)var8).setAlignmentY(0.5F);
      }

      var2.addContainerListener(this.getContainerListener(var2));
      this.setLayoutManager(var2);
   }

   private PropertyChangeListener getOrientationListener(final JToolBar var1) {
      PropertyChangeListener var2 = (PropertyChangeListener)var1.getClientProperty("Synthetica.toolBarStyle.orientationListener");
      if (var2 == null) {
         var2 = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent var1x) {
               String var2 = var1x.getPropertyName();
               if (var2.equals("orientation")) {
                  ToolBarStyle.this.setLayoutManager(var1);
               } else if (var2.equals("floatable")) {
                  ToolBarStyle.this.setMargin(var1, (Boolean)var1x.getNewValue());
               } else if (var2.equals("componentOrientation")) {
                  ToolBarStyle.this.setMargin(var1, var1.isFloatable());
               }

            }
         };
         var1.putClientProperty("Synthetica.toolBarStyle.orientationListener", var2);
      }

      return var2;
   }

   private ContainerListener getContainerListener(final JToolBar var1) {
      ContainerListener var2 = (ContainerListener)var1.getClientProperty("Synthetica.toolBarStyle.containerListener");
      if (var2 == null) {
         var2 = new ContainerListener() {
            public void componentAdded(ContainerEvent var1x) {
               HashMap var2 = (HashMap)var1.getClientProperty("Synthetica.toolBarStyle.orgFillers");
               HashMap var3 = (HashMap)var1.getClientProperty("Synthetica.toolBarStyle.orgXAlignments");
               HashMap var4 = (HashMap)var1.getClientProperty("Synthetica.toolBarStyle.orgYAlignments");
               Component var5 = var1x.getChild();
               var3.put(var5, var5.getAlignmentX());
               var4.put(var5, var5.getAlignmentY());
               if (var5 instanceof Box.Filler) {
                  var2.put(var1.getComponents().length - 1, var5);
               }

               if (var5 instanceof JComponent) {
                  ((JComponent)var5).setAlignmentX(0.5F);
                  ((JComponent)var5).setAlignmentY(0.5F);
               }

            }

            public void componentRemoved(ContainerEvent var1x) {
               HashMap var2 = (HashMap)var1.getClientProperty("Synthetica.toolBarStyle.orgXAlignments");
               var2.remove(var1x.getChild());
               HashMap var3 = (HashMap)var1.getClientProperty("Synthetica.toolBarStyle.orgYAlignments");
               var3.remove(var1x.getChild());
            }
         };
         var1.putClientProperty("Synthetica.toolBarStyle.containerListener", var2);
      }

      return var2;
   }

   public void uninstallDefaults(SynthContext var1) {
      this.synthStyle.uninstallDefaults(var1);
      JToolBar var2 = (JToolBar)var1.getComponent();
      var2.removePropertyChangeListener(this.getOrientationListener(var2));
      var2.removeContainerListener(this.getContainerListener(var2));
      HashMap var3 = (HashMap)var2.getClientProperty("Synthetica.toolBarStyle.orgFillers");
      HashMap var4 = (HashMap)var2.getClientProperty("Synthetica.toolBarStyle.orgXAlignments");
      HashMap var5 = (HashMap)var2.getClientProperty("Synthetica.toolBarStyle.orgYAlignments");
      Iterator var6 = var3.entrySet().iterator();

      Map.Entry var7;
      while(var6.hasNext()) {
         var7 = (Map.Entry)var6.next();
         int var8 = (Integer)var7.getKey();
         Component var9 = (Component)var7.getValue();
         var2.remove(var8);
         var2.add(var9, var8);
      }

      var6 = var4.entrySet().iterator();

      JComponent var10;
      float var11;
      while(var6.hasNext()) {
         var7 = (Map.Entry)var6.next();
         var10 = (JComponent)var7.getKey();
         var11 = (Float)var7.getValue();
         var10.setAlignmentX(var11);
      }

      var6 = var5.entrySet().iterator();

      while(var6.hasNext()) {
         var7 = (Map.Entry)var6.next();
         var10 = (JComponent)var7.getKey();
         var11 = (Float)var7.getValue();
         var10.setAlignmentY(var11);
      }

      var2.setMargin((Insets)var2.getClientProperty("Synthetica.toolBarStyle.orgMargin"));
      var2.setLayout((LayoutManager)var2.getClientProperty("Synthetica.toolBarStyle.orgLayoutManager"));
      var2.putClientProperty("Synthetica.toolBarStyle.orgFillers", (Object)null);
      var2.putClientProperty("Synthetica.toolBarStyle.orgXAlignments", (Object)null);
      var2.putClientProperty("Synthetica.toolBarStyle.orgYAlignments", (Object)null);
      var2.putClientProperty("Synthetica.toolBarStyle.orgMargin", (Object)null);
      var2.putClientProperty("Synthetica.toolBarStyle.orgLayoutManager", (Object)null);
      var2.putClientProperty("Synthetica.toolBarStyle.orientationListener", (Object)null);
      var2.putClientProperty("Synthetica.toolBarStyle.containerListener", (Object)null);
   }

   private void setLayoutManager(JToolBar var1) {
      if (!this.useSynthLayout(var1)) {
         HashMap var2 = (HashMap)var1.getClientProperty("Synthetica.toolBarStyle.orgFillers");
         var1.removeContainerListener(this.getContainerListener(var1));
         Component[] var3 = var1.getComponents();
         SyntheticaToolBarLayout var4 = null;
         int var5;
         Component var6;
         Dimension var7;
         int var8;
         if (var1.getOrientation() == 0) {
            var4 = new SyntheticaToolBarLayout(var1, 2);

            for(var5 = 0; var5 < var3.length; ++var5) {
               if (var3[var5] instanceof Box.Filler && this.fillerIsGlue((Box.Filler)var3[var5])) {
                  var1.remove(var3[var5]);
                  var1.add(Box.createHorizontalGlue(), var5);
               } else if (var3[var5] instanceof Box.Filler && !this.fillerIsGlue((Box.Filler)var3[var5])) {
                  var1.remove(var3[var5]);
                  var6 = (Component)var2.get(var5);
                  var7 = var6.getPreferredSize();
                  var8 = Math.max(var7.width, var7.height);
                  var1.add(Box.createHorizontalStrut(var8), var5);
               }
            }

            this.setMargin(var1, var1.isFloatable());

            for(var5 = 0; var5 < var3.length; ++var5) {
               ((JComponent)var3[var5]).setAlignmentY(0.5F);
            }
         } else {
            var4 = new SyntheticaToolBarLayout(var1, 3);

            for(var5 = 0; var5 < var3.length; ++var5) {
               if (var3[var5] instanceof Box.Filler && this.fillerIsGlue((Box.Filler)var3[var5])) {
                  var1.remove(var3[var5]);
                  var1.add(Box.createVerticalGlue(), var5);
               } else if (var3[var5] instanceof Box.Filler && !this.fillerIsGlue((Box.Filler)var3[var5])) {
                  var1.remove(var3[var5]);
                  var6 = (Component)var2.get(var5);
                  var7 = var6.getPreferredSize();
                  var8 = Math.max(var7.width, var7.height);
                  var1.add(Box.createVerticalStrut(var8), var5);
               }
            }

            this.setMargin(var1, var1.isFloatable());

            for(var5 = 0; var5 < var3.length; ++var5) {
               ((JComponent)var3[var5]).setAlignmentX(0.5F);
            }
         }

         LayoutManager var9 = var1.getLayout();
         if (var9 != null && (var9.getClass().getName().contains("SynthToolBar") || var9 instanceof SyntheticaToolBarLayout)) {
            var1.setLayout(var4);
         }

         var1.addContainerListener(this.getContainerListener(var1));
      }
   }

   private void setMargin(JToolBar var1, boolean var2) {
      if (!this.useSynthLayout(var1)) {
         Insets var3 = null;
         if (var1.getOrientation() == 0) {
            var3 = (Insets)SyntheticaLookAndFeel.getInsets("Synthetica.toolBar.margin.horizontal", var1).clone();
            if (var2 && var1.getComponentOrientation().isLeftToRight()) {
               var3.left += SyntheticaLookAndFeel.getInt("Synthetica.toolBar.handle.size", var1, 0);
            } else if (var2) {
               var3.right += SyntheticaLookAndFeel.getInt("Synthetica.toolBar.handle.size", var1, 0);
            }
         } else {
            var3 = (Insets)SyntheticaLookAndFeel.getInsets("Synthetica.toolBar.margin.vertical", var1).clone();
            if (var2) {
               var3.top += SyntheticaLookAndFeel.getInt("Synthetica.toolBar.handle.size", var1, 0);
            }
         }

         Insets var4 = (Insets)var1.getClientProperty("Synthetica.toolBarStyle.orgMargin");
         if (var4 != null && var3 != null) {
            var3.bottom += var4.bottom;
            var3.left += var4.left;
            var3.top += var4.top;
            var3.right += var4.right;
            var1.setMargin(var3);
         }

      }
   }

   private boolean useSynthLayout(JComponent var1) {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.useSynthLayoutManager", var1, JavaVersion.JAVA5 ? false : false);
   }

   private boolean fillerIsGlue(Box.Filler var1) {
      Dimension var2 = new Dimension(0, 0);
      return var1.getMinimumSize().equals(var2) && var1.getPreferredSize().equals(var2);
   }

   private static class SyntheticaToolBarLayout extends BoxLayout {
      private static final long serialVersionUID = 6806218476946368742L;

      SyntheticaToolBarLayout(Container var1, int var2) {
         super(var1, var2);
      }
   }
}
