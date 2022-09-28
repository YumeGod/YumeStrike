package ui;

import common.CommonUtils;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyBindings implements KeyEventDispatcher {
   protected Map bindings = new HashMap();

   public void bind(String var1, KeyHandler var2) {
      synchronized(this) {
         this.bindings.put(var1, var2);
      }
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      StringBuffer var2 = new StringBuffer();
      if (var1.getModifiers() != 0) {
         var2.append(getKeyModifiers(var1));
      }

      var2.append(getKeyText(var1));
      synchronized(this) {
         if (this.bindings.containsKey(var2.toString())) {
            var1.consume();
            if (var1.getID() != 401) {
               return false;
            } else {
               CommonUtils.runSafe(new ExecuteBinding(var2.toString(), (KeyHandler)this.bindings.get(var2.toString())));
               return true;
            }
         } else {
            return false;
         }
      }
   }

   private static String getKeyModifiers(KeyEvent var0) {
      StringBuffer var1 = new StringBuffer();
      if (var0.isShiftDown()) {
         var1.append("Shift+");
      }

      if (var0.isControlDown()) {
         var1.append("Ctrl+");
      }

      if (var0.isAltDown()) {
         var1.append("Alt+");
      }

      if (var0.isMetaDown()) {
         var1.append("Meta+");
      }

      return var1.toString();
   }

   private static String getKeyText(KeyEvent var0) {
      switch (var0.getKeyCode()) {
         case 8:
            return "Backspace";
         case 9:
            return "Tab";
         case 10:
            return "Enter";
         case 12:
            return "Clear";
         case 19:
            return "Pause";
         case 20:
            return "Caps_Lock";
         case 24:
            return "Final";
         case 27:
            return "Escape";
         case 28:
            return "Convert";
         case 30:
            return "Accept";
         case 32:
            return "Space";
         case 33:
            return "Page_Up";
         case 34:
            return "Page_Down";
         case 35:
            return "End";
         case 36:
            return "Home";
         case 37:
            return "Left";
         case 38:
            return "Up";
         case 39:
            return "Right";
         case 40:
            return "Down";
         case 44:
            return "NumPad_,";
         case 46:
            return "Period";
         case 47:
            return "NumPad_/";
         case 106:
            return "NumPad_*";
         case 109:
            return "NumPad_-";
         case 112:
            return "F1";
         case 113:
            return "F2";
         case 114:
            return "F3";
         case 115:
            return "F4";
         case 116:
            return "F5";
         case 117:
            return "F6";
         case 118:
            return "F7";
         case 119:
            return "F8";
         case 120:
            return "F9";
         case 121:
            return "F10";
         case 122:
            return "F11";
         case 123:
            return "F12";
         case 127:
            return "Delete";
         case 144:
            return "Num_Lock";
         case 145:
            return "Scroll_Lock";
         case 154:
            return "Print_Screen";
         case 155:
            return "Insert";
         case 156:
            return "Help";
         case 192:
            return "Back_Quote";
         case 222:
            return "Quote";
         case 521:
            return "NumPad_+";
         default:
            return KeyEvent.getKeyText(var0.getKeyCode());
      }
   }

   private static class ExecuteBinding implements Runnable {
      protected String binding;
      protected KeyHandler handler;

      public ExecuteBinding(String var1, KeyHandler var2) {
         this.binding = var1;
         this.handler = var2;
      }

      public void run() {
         this.handler.key_pressed(this.binding);
      }
   }
}
