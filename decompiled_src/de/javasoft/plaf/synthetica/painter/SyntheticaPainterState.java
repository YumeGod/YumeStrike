package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Container;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.text.JTextComponent;

class SyntheticaPainterState extends SyntheticaState {
   public SyntheticaPainterState(SynthContext var1) {
      this(var1, 0, false);
   }

   public SyntheticaPainterState(SynthContext var1, int var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   public SyntheticaPainterState(SynthContext var1, int var2, boolean var3, boolean var4) {
      super(var1.getComponentState() | var2);
      JComponent var5 = var1.getComponent();
      if (var5 instanceof JTextComponent && !((JTextComponent)var5).isEditable()) {
         this.setState(SyntheticaState.State.LOCKED);
      } else if (var5 instanceof JComboBox && !((JComboBox)var5).isEditable()) {
         this.setState(SyntheticaState.State.LOCKED);
      }

      boolean var7;
      if (var3) {
         boolean var6 = var5.getClientProperty("Synthetica.MOUSE_OVER") == null ? false : (Boolean)var5.getClientProperty("Synthetica.MOUSE_OVER");
         if (var6) {
            this.setState(SyntheticaState.State.HOVER);
         }

         var7 = var5.getClientProperty("Synthetica.MOUSE_PRESSED") == null ? false : (Boolean)var5.getClientProperty("Synthetica.MOUSE_PRESSED");
         if (var7) {
            this.setState(SyntheticaState.State.PRESSED);
         }
      }

      if (var4) {
         Container var9 = var5.getParent();
         if (var9 instanceof JComponent) {
            var7 = ((JComponent)var9).getClientProperty("Synthetica.MOUSE_OVER") == null ? false : (Boolean)((JComponent)var9).getClientProperty("Synthetica.MOUSE_OVER");
            if (var7) {
               this.setState(SyntheticaState.State.HOVER);
            }

            boolean var8 = ((JComponent)var9).getClientProperty("Synthetica.MOUSE_PRESSED") == null ? false : (Boolean)((JComponent)var9).getClientProperty("Synthetica.MOUSE_PRESSED");
            if (var8) {
               this.setState(SyntheticaState.State.PRESSED);
            }
         }
      }

   }
}
