package dialog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LightSwitch implements ChangeListener {
   protected JCheckBox the_switch = null;
   protected LinkedList components = new LinkedList();
   protected boolean negate = false;

   public void stateChanged(ChangeEvent var1) {
      this.check();
   }

   public void check() {
      boolean var1 = this.the_switch.isSelected();
      if (this.negate) {
         var1 = !var1;
      }

      Iterator var2 = this.components.iterator();

      while(var2.hasNext()) {
         JComponent var3 = (JComponent)var2.next();
         var3.setEnabled(var1);
      }

   }

   public void set(JCheckBox var1, boolean var2) {
      this.the_switch = var1;
      this.negate = var2;
      this.the_switch.addChangeListener(this);
      this.check();
   }

   public void set(DialogManager.DialogRow var1, boolean var2) {
      this.set((JCheckBox)var1.get(1), var2);
   }

   public void add(DialogManager.DialogRow var1) {
      this.add(var1.get(0));
      this.add(var1.get(1));
      this.add(var1.get(2));
   }

   public void add(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         DialogManager.DialogRow var3 = (DialogManager.DialogRow)var2.next();
         this.add(var3);
      }

   }

   public void add(JComponent var1) {
      if (var1 != null) {
         this.components.add(var1);
      }

   }
}
