package de.javasoft.plaf.synthetica;

import de.javasoft.util.OS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.UIDefaults;

class SyntheticaInputDefaults {
   public static Object[] getInputMapDefaults() {
      ArrayList var0 = new ArrayList();
      Map var1 = getInputMap();
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var2 = (Map.Entry)var3.next();
         var0.add(var2.getKey());
         Map var4 = (Map)var2.getValue();
         ArrayList var5 = new ArrayList();
         Iterator var7 = var4.entrySet().iterator();

         while(var7.hasNext()) {
            Map.Entry var6 = (Map.Entry)var7.next();
            var5.add(var6.getKey());
            var5.add(var6.getValue());
         }

         var0.add(new UIDefaults.LazyInputMap(var5.toArray()));
      }

      return var0.toArray();
   }

   private static Map getInputMap() {
      boolean var0 = OS.getCurrentOS() == OS.Mac;
      boolean var1 = OS.getCurrentOS() == OS.Linux;
      HashMap var2 = new HashMap();
      Map var3 = getBaseMap4JTable();
      if (var0) {
         var3.putAll(getMacMap4JTable());
      } else if (var1) {
         var3.putAll(getLinuxMap4JTable());
      }

      var2.put("Table.ancestorInputMap", var3);
      var3 = getBaseMap4JTableRTL();
      var2.put("Table.ancestorInputMap.RightToLeft", var3);
      var3 = getBaseMap4JTree();
      if (var0) {
         var3.putAll(getMacMap4JTree());
      } else if (var1) {
         var3.putAll(getLinuxMap4JTree());
      }

      var2.put("Tree.ancestorInputMap", var3);
      var3 = getBaseMap4JComboBox();
      var2.put("ComboBox.ancestorInputMap", var3);
      var3 = getBaseMap4JRootPane();
      var2.put("RootPane.ancestorInputMap", var3);
      return var2;
   }

   private static Map getBaseMap4JTable() {
      HashMap var0 = new HashMap();
      Object[] var1 = getBaseValues4JTable();

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         var0.put(var1[var2], var1[var2 + 1]);
      }

      return var0;
   }

   private static Map getMacMap4JTable() {
      HashMap var0 = new HashMap();
      var0.put("meta A", "selectAll");
      return var0;
   }

   private static Map getLinuxMap4JTable() {
      HashMap var0 = new HashMap();
      return var0;
   }

   private static Object[] getBaseValues4JTable() {
      return new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"};
   }

   private static Map getBaseMap4JTableRTL() {
      HashMap var0 = new HashMap();
      Object[] var1 = getBaseValues4JTableRTL();

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         var0.put(var1[var2], var1[var2 + 1]);
      }

      return var0;
   }

   private static Object[] getBaseValues4JTableRTL() {
      return new Object[]{"RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "shift RIGHT", "selectPreviousColumnChangeLead", "shift KP_RIGHT", "selectPreviousColumnChangeLead", "LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", "ctrl KP_LEFT", "selectNextColumnChangeLead", "ctrl PAGE_UP", "scrollRightChangeSelection", "ctrl PAGE_DOWN", "scrollLeftChangeSelection", "ctrl shift PAGE_UP", "scrollRightExtendSelection", "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection"};
   }

   private static Map getBaseMap4JTree() {
      HashMap var0 = new HashMap();
      return var0;
   }

   private static Map getMacMap4JTree() {
      HashMap var0 = new HashMap();
      var0.put("meta A", "selectAll");
      return var0;
   }

   private static Map getLinuxMap4JTree() {
      HashMap var0 = new HashMap();
      return var0;
   }

   private static Map getBaseMap4JComboBox() {
      HashMap var0 = new HashMap();
      Object[] var1 = getBaseValues4JComboBox();

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         var0.put(var1[var2], var1[var2 + 1]);
      }

      return var0;
   }

   private static Object[] getBaseValues4JComboBox() {
      return new Object[]{"ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext", "KP_DOWN", "selectNext", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup", "SPACE", "spacePopup", "ENTER", "enterPressed", "UP", "selectPrevious", "KP_UP", "selectPrevious"};
   }

   private static Map getBaseMap4JRootPane() {
      HashMap var0 = new HashMap();
      Object[] var1 = getBaseValues4JRootPane();

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         var0.put(var1[var2], var1[var2 + 1]);
      }

      return var0;
   }

   private static Object[] getBaseValues4JRootPane() {
      return new Object[]{"shift F10", "postPopup", "CONTEXT_MENU", "postPopup"};
   }
}
