package cortana.gui;

import javax.swing.JMenuBar;
import ui.KeyHandler;

public interface ScriptableApplication {
   void bindKey(String var1, KeyHandler var2);

   JMenuBar getJMenuBar();

   boolean isHeadless();
}
