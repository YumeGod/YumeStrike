package com.glavsoft.viewer.swing;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyboardConvertor {
   private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
   private static final String PATTERN_STRING_FOR_SCANCODE = "scancode=(\\d+)";
   private Pattern patternForScancode;
   private static final Map keyMap = new HashMap() {
      {
         this.put(192, new CodePair(96, 126));
         this.put(49, new CodePair(49, 33));
         this.put(50, new CodePair(50, 64));
         this.put(51, new CodePair(51, 35));
         this.put(52, new CodePair(52, 36));
         this.put(53, new CodePair(53, 37));
         this.put(54, new CodePair(54, 94));
         this.put(55, new CodePair(55, 38));
         this.put(56, new CodePair(56, 42));
         this.put(57, new CodePair(57, 40));
         this.put(48, new CodePair(48, 41));
         this.put(45, new CodePair(45, 95));
         this.put(61, new CodePair(61, 43));
         this.put(92, new CodePair(92, 124));
         this.put(81, new CodePair(113, 81));
         this.put(87, new CodePair(119, 87));
         this.put(69, new CodePair(101, 69));
         this.put(82, new CodePair(114, 82));
         this.put(84, new CodePair(116, 84));
         this.put(89, new CodePair(121, 89));
         this.put(85, new CodePair(117, 85));
         this.put(73, new CodePair(105, 73));
         this.put(79, new CodePair(111, 79));
         this.put(80, new CodePair(112, 80));
         this.put(91, new CodePair(91, 123));
         this.put(93, new CodePair(93, 125));
         this.put(65, new CodePair(97, 65));
         this.put(83, new CodePair(115, 83));
         this.put(68, new CodePair(100, 68));
         this.put(70, new CodePair(102, 70));
         this.put(71, new CodePair(103, 71));
         this.put(72, new CodePair(104, 72));
         this.put(74, new CodePair(106, 74));
         this.put(75, new CodePair(107, 75));
         this.put(76, new CodePair(108, 76));
         this.put(59, new CodePair(59, 58));
         this.put(222, new CodePair(39, 34));
         this.put(90, new CodePair(122, 90));
         this.put(88, new CodePair(120, 88));
         this.put(67, new CodePair(99, 67));
         this.put(86, new CodePair(118, 86));
         this.put(66, new CodePair(98, 66));
         this.put(78, new CodePair(110, 78));
         this.put(77, new CodePair(109, 77));
         this.put(44, new CodePair(44, 60));
         this.put(46, new CodePair(46, 62));
         this.put(47, new CodePair(47, 63));
         this.put(153, new CodePair(60, 62));
      }
   };
   private static boolean canCheckCapsWithToolkit;

   public KeyboardConvertor() {
      try {
         Toolkit.getDefaultToolkit().getLockingKeyState(20);
         canCheckCapsWithToolkit = true;
      } catch (Exception var2) {
         canCheckCapsWithToolkit = false;
      }

      if (isWindows) {
         this.patternForScancode = Pattern.compile("scancode=(\\d+)");
      }

   }

   public int convert(int keyChar, KeyEvent ev) {
      int keyCode = ev.getKeyCode();
      boolean isShiftDown = ev.isShiftDown();
      CodePair codePair = (CodePair)keyMap.get(keyCode);
      if (null == codePair) {
         return keyChar;
      } else {
         if (isWindows) {
            Matcher matcher = this.patternForScancode.matcher(ev.paramString());
            if (matcher.matches()) {
               try {
                  int scancode = Integer.parseInt(matcher.group(1));
                  if (90 == keyCode && 21 == scancode) {
                     codePair = (CodePair)keyMap.get(89);
                  } else if (89 == keyCode && 44 == scancode) {
                     codePair = (CodePair)keyMap.get(90);
                  }
               } catch (NumberFormatException var9) {
               }
            }
         }

         boolean isCapsLock = false;
         if (Character.isLetter(codePair.code) && canCheckCapsWithToolkit) {
            try {
               isCapsLock = Toolkit.getDefaultToolkit().getLockingKeyState(20);
            } catch (Exception var8) {
            }
         }

         return (!isShiftDown || isCapsLock) && (isShiftDown || !isCapsLock) ? codePair.code : codePair.codeShifted;
      }
   }

   private static class CodePair {
      public int code;
      public int codeShifted;

      public CodePair(int code, int codeShifted) {
         this.code = code;
         this.codeShifted = codeShifted;
      }
   }
}
