package org.apache.batik.dom.util;

public abstract class DOMUtilitiesSupport {
   static final String[] BITS = new String[]{"Shift", "Ctrl", "Meta-or-Button3", "Alt-or-Button2", "Button1", "AltGraph", "ShiftDown", "CtrlDown", "MetaDown", "AltDown", "Button1Down", "Button2Down", "Button3Down", "AltGraphDown"};

   protected static String getModifiersList(int var0, int var1) {
      if ((var1 & 8192) != 0) {
         var1 = 16 | var1 >> 6 & 15;
      } else {
         var1 = var1 >> 6 & 15;
      }

      String var2 = DOMUtilities.LOCK_STRINGS[var0 & 15];
      return var2.length() != 0 ? var2 + ' ' + DOMUtilities.MODIFIER_STRINGS[var1] : DOMUtilities.MODIFIER_STRINGS[var1];
   }
}
