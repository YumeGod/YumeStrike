package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.StyleFactory;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.JavaVersion;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class ScrollPaneStyle extends StyleWrapper {
   private static ScrollPaneStyle instance = new ScrollPaneStyle();
   private static String comboBoxViewportInsetsKey = "Synthetica.comboBox.viewport.border.insets";

   private ScrollPaneStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      String var3 = (String)SyntheticaLookAndFeel.get("Synthetica.scrollPane.layoutManager.className", (Component)var1);
      if (var3 != null && var1.getLayout() != null) {
         try {
            Class var4 = Class.forName(var3);
            if (!var4.isAssignableFrom(var1.getLayout().getClass())) {
               ((StyleFactory)SynthLookAndFeel.getStyleFactory()).getComponentPropertyStore().storeComponentProperty(var1, "SYCP_LAYOUT_MANAGER");
               var1.setLayout((LayoutManager)var4.newInstance());
            }
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }
      }

      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ScrollPaneStyle var6 = new ScrollPaneStyle();
         var6.setStyle(var0);
         return var6;
      }
   }

   public Object get(SynthContext var1, Object var2) {
      JScrollPane var3 = (JScrollPane)var1.getComponent();
      return !JavaVersion.JAVA5 && "ScrollPane.viewportBorderInsets".equals(var2) && UIManager.get(comboBoxViewportInsetsKey) != null && var3.getViewport().getView() != null && "ComboBox.list".equals(var3.getViewport().getView().getName()) ? UIManager.getInsets(comboBoxViewportInsetsKey) : super.get(var1, var2);
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      JScrollPane var3 = (JScrollPane)var1.getComponent();
      Component var4 = var3.getViewport().getView();
      if (var4 == null) {
         return this.synthStyle.getColor(var1, var2);
      } else {
         Color var5 = var4.getBackground();
         if (var4.getBackground() == null || var4.getBackground() instanceof ColorUIResource) {
            var5 = Color.WHITE;
         }

         if (var4 instanceof JEditorPane && var4.getBackground() instanceof ColorUIResource) {
            JEditorPane var7 = (JEditorPane)var4;
            if (!var7.isEditable()) {
               var5 = UIManager.getColor("Synthetica.editorPane.lockedColor");
            }

            if (!var7.isEnabled()) {
               var5 = UIManager.getColor("Synthetica.editorPane.disabledColor");
            }
         } else if (var4 instanceof JTextArea && var4.getBackground() instanceof ColorUIResource) {
            JTextArea var6 = (JTextArea)var4;
            if (!var6.isEditable()) {
               var5 = UIManager.getColor("Synthetica.textArea.lockedColor");
            } else if (!var6.isEnabled()) {
               var5 = UIManager.getColor("Synthetica.textArea.disabledColor");
            }
         }

         return (Color)(var5 != null ? new ColorUIResource(var5) : super.getColor(var1, var2));
      }
   }
}
