package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.JavaVersion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Insets;
import java.lang.reflect.Field;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.JTextComponent;

public class FormattedTextFieldStyle extends StyleWrapper {
   private static FormattedTextFieldStyle instance = new FormattedTextFieldStyle();

   private FormattedTextFieldStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null && ((JTextComponent)var1).isEditable()) {
         instance.setStyle(var0);
         return instance;
      } else {
         FormattedTextFieldStyle var3 = new FormattedTextFieldStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Insets getInsets(SynthContext var1, Insets var2) {
      Insets var3 = this.synthStyle.getInsets(var1, var2);
      String var4 = var1.getComponent().getName();
      if (!"Spinner.formattedTextField".equals(var4)) {
         return "ComboBox.textField".equals(var4) ? new Insets(0, 1, 0, 1) : var3;
      } else {
         if ((var3.left > 10 || var3.right > 10) && (JavaVersion.JAVA7 || JavaVersion.JAVA8) && SyntheticaLookAndFeel.getBoolean("Synthetica.ColorChooserPanel.workaround.enabled", (Component)null, true)) {
            this.colorChooserPanelWorkaround(var1);
         }

         return var1.getComponent().getComponentOrientation().isLeftToRight() ? new Insets(0, 0, 0, var3.right) : new Insets(0, var3.right, 0, 0);
      }
   }

   private void colorChooserPanelWorkaround(final SynthContext var1) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            if (var1 != null) {
               JComponent var1x = var1.getComponent();
               if (var1x != null && var1x.getParent() != null) {
                  Container var2 = var1x.getParent().getParent();
                  if (var2 != null && var2.getClass().getName().equals("javax.swing.colorchooser.ColorChooserPanel")) {
                     try {
                        Class var3 = Class.forName("javax.swing.colorchooser.ColorChooserPanel");
                        Field var4 = var3.getDeclaredField("slider");
                        var4.setAccessible(true);
                        JComponent var5 = (JComponent)var4.get(var2);
                        var5.setBorder(new EmptyBorder(0, 0, 0, 0));
                        var4 = var3.getDeclaredField("diagram");
                        var4.setAccessible(true);
                        JComponent var6 = (JComponent)var4.get(var2);
                        var6.setBorder(new EmptyBorder(0, 0, 0, 0));
                     } catch (Exception var7) {
                        var7.printStackTrace();
                     }
                  }
               }

            }
         }
      });
   }

   public Color getColorForState(SynthContext var1, ColorType var2) {
      JComponent var3 = var1.getComponent();
      if (var2 == ColorType.TEXT_FOREGROUND && var3.getForeground() instanceof ColorUIResource && !((JTextComponent)var3).isEditable()) {
         Color var4 = SyntheticaLookAndFeel.getColor("Synthetica.formattedTextField.locked.textColor", var3);
         return var4 == null ? super.getColorForState(var1, var2) : var4;
      } else {
         return super.getColorForState(var1, var2);
      }
   }
}
