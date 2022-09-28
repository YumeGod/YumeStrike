package aggressor.bridges;

import common.CommonUtils;
import cortana.Cortana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ToolBarBridge implements Function, Loadable {
   protected JToolBar toolbar;

   public ToolBarBridge(JToolBar var1) {
      this.toolbar = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&image", this);
      Cortana.put(var1, "&image_internal", this);
      Cortana.put(var1, "&toolbar", this);
      Cortana.put(var1, "&toolbar_separator", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var9;
      if (var1.equals("&image")) {
         var9 = BridgeUtilities.getString(var3, "");
         return SleepUtils.getScalar((Object)(new ImageIcon(var9)));
      } else if (var1.equals("&image_internal")) {
         try {
            var9 = BridgeUtilities.getString(var3, "");
            BufferedImage var10 = ImageIO.read(CommonUtils.resource(var9));
            return SleepUtils.getScalar((Object)(new ImageIcon(var10)));
         } catch (IOException var8) {
            throw new RuntimeException(var8);
         }
      } else {
         if (var1.equals("&toolbar")) {
            Icon var4 = (Icon)BridgeUtilities.getObject(var3);
            final String var5 = (String)BridgeUtilities.getObject(var3);
            final SleepClosure var6 = BridgeUtilities.getFunction(var3, var2);
            JButton var7 = new JButton(var4);
            var7.setToolTipText(var5);
            var7.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  Stack var2 = new Stack();
                  var2.push(SleepUtils.getScalar(var5));
                  SleepUtils.runCode((SleepClosure)var6, "toolbar", (ScriptInstance)null, var2);
               }
            });
            this.toolbar.add(var7);
         } else if (var1.equals("&toolbar_separator")) {
            this.toolbar.addSeparator();
         }

         return SleepUtils.getEmptyScalar();
      }
   }
}
