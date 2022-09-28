package cortana.gui;

import cortana.core.EventManager;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.engine.Block;
import sleep.interfaces.Environment;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;
import ui.DynamicMenu;

public class MenuBridge implements Loadable, Function, Environment {
   protected Stack parents = new Stack();
   protected Map menus = new HashMap();
   protected Stack data = new Stack();
   protected ScriptableApplication application;
   protected MenuBuilder builder = null;

   public Stack getArguments() {
      return (Stack)this.data.peek();
   }

   public MenuBridge(ScriptableApplication var1, MenuBuilder var2) {
      this.application = var1;
      this.builder = var2;
   }

   public void push(JComponent var1, Stack var2) {
      this.parents.push(var1);
      this.data.push(var2);
   }

   public void pop() {
      this.parents.pop();
      this.data.pop();
   }

   public JComponent getTopLevel() {
      if (this.parents.isEmpty()) {
         throw new RuntimeException("menu has no parent");
      } else {
         return (JComponent)this.parents.peek();
      }
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      SleepClosure var5 = new SleepClosure(var1, var4);
      if (var2.equals("menu")) {
         this.createMenu(var3, var5);
      } else if (var2.equals("item")) {
         this.createItem(var3, var5);
      } else if (var2.equals("popup")) {
         this.registerTopLevel(var3, var5);
      }

   }

   public void registerTopLevel(String var1, SleepClosure var2) {
      if (!this.menus.containsKey(var1)) {
         this.menus.put(var1, new LinkedList());
      }

      LinkedList var3 = (LinkedList)this.menus.get(var1);
      var3.add(var2);
   }

   public void clearTopLevel(String var1) {
      this.menus.remove(var1);
   }

   public boolean isPopulated(String var1) {
      return this.menus.containsKey(var1) && ((LinkedList)this.menus.get(var1)).size() > 0;
   }

   public LinkedList getMenus(String var1) {
      return this.menus.containsKey(var1) ? (LinkedList)this.menus.get(var1) : new LinkedList();
   }

   public void createMenu(String var1, SleepClosure var2) {
      JComponent var3 = this.getTopLevel();
      ScriptedMenu var4 = new ScriptedMenu(var1, var2, this);
      var3.add(var4);
   }

   public void createItem(String var1, SleepClosure var2) {
      JComponent var3 = this.getTopLevel();
      ScriptedMenuItem var4 = new ScriptedMenuItem(var1, var2, this);
      var3.add(var4);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&separator")) {
         if (this.getTopLevel() instanceof JMenu) {
            ((JMenu)this.getTopLevel()).addSeparator();
         } else if (this.getTopLevel() instanceof JPopupMenu) {
            ((JPopupMenu)this.getTopLevel()).addSeparator();
         }
      } else {
         String var4;
         if (var1.equals("&show_menu")) {
            var4 = BridgeUtilities.getString(var3, "");
            if (var3.size() > 0) {
               this.builder.setupMenu(this.getTopLevel(), var4, EventManager.shallowCopy(var3));
            } else {
               this.builder.setupMenu(this.getTopLevel(), var4, this.getArguments());
            }
         } else {
            final String var5;
            if (var1.equals("&show_popup")) {
               MouseEvent var11 = (MouseEvent)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               JPopupMenu var6 = new JPopupMenu();
               this.push(var6, EventManager.shallowCopy(var3));
               this.builder.setupMenu(this.getTopLevel(), var5, this.getArguments());
               this.pop();
               var6.show((JComponent)var11.getSource(), var11.getX(), var11.getY());
            } else if (var1.equals("&insert_menu")) {
               var4 = BridgeUtilities.getString(var3, "");
               this.push(this.getTopLevel(), EventManager.shallowCopy(var3));
               this.builder.setupMenu(this.getTopLevel(), var4, this.getArguments());
               this.pop();
            } else if (var1.equals("&insert_component")) {
               Object var12 = BridgeUtilities.getObject(var3);
               if (this.getTopLevel() instanceof JMenu) {
                  ((JMenu)this.getTopLevel()).add((JComponent)var12);
               } else if (this.getTopLevel() instanceof JPopupMenu) {
                  ((JPopupMenu)this.getTopLevel()).add((JComponent)var12);
               }
            } else if (var1.equals("&menubar")) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               int var14 = BridgeUtilities.getInt(var3, 2);
               DynamicMenu var7 = new DynamicMenu("");
               if (var4.indexOf(38) > -1) {
                  var7.setText(var4.substring(0, var4.indexOf(38)) + var4.substring(var4.indexOf(38) + 1, var4.length()));
                  var7.setMnemonic(var4.charAt(var4.indexOf(38) + 1));
               } else {
                  var7.setText(var4);
               }

               var7.setHandler(new DynamicMenu.DynamicMenuHandler() {
                  public void setupMenu(JMenu var1) {
                     MenuBridge.this.builder.setupMenu(var1, var5, new Stack());
                     if (!MenuBridge.this.isPopulated(var5)) {
                        MenuBridge.this.application.getJMenuBar().remove(var1);
                        MenuBridge.this.application.getJMenuBar().validate();
                     }

                  }
               });
               MenuElement[] var8 = this.application.getJMenuBar().getSubElements();

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  JMenu var10 = (JMenu)var8[var9].getComponent();
                  if (var10.getText().equals(var7.getText())) {
                     this.application.getJMenuBar().remove(var10);
                  }
               }

               this.application.getJMenuBar().add(var7);
               this.application.getJMenuBar().validate();
            } else if (var1.equals("&popup_clear")) {
               var4 = BridgeUtilities.getString(var3, "");
               this.clearTopLevel(var4);
            } else {
               var4 = BridgeUtilities.getString(var3, "");
               SleepClosure var13 = BridgeUtilities.getFunction(var3, var2);
               if (var1.equals("&menu")) {
                  this.createMenu(var4, var13);
               } else if (var1.equals("&item")) {
                  this.createItem(var4, var13);
               } else if (var1.equals("&popup")) {
                  this.registerTopLevel(var4, var13);
               }
            }
         }
      }

      return SleepUtils.getEmptyScalar();
   }

   public void scriptLoaded(ScriptInstance var1) {
      var1.getScriptEnvironment().getEnvironment().put("popup", this);
      var1.getScriptEnvironment().getEnvironment().put("&popup", this);
      var1.getScriptEnvironment().getEnvironment().put("menu", this);
      var1.getScriptEnvironment().getEnvironment().put("&menu", this);
      var1.getScriptEnvironment().getEnvironment().put("item", this);
      var1.getScriptEnvironment().getEnvironment().put("&item", this);
      var1.getScriptEnvironment().getEnvironment().put("&separator", this);
      var1.getScriptEnvironment().getEnvironment().put("&menubar", this);
      var1.getScriptEnvironment().getEnvironment().put("&show_menu", this);
      var1.getScriptEnvironment().getEnvironment().put("&insert_menu", this);
      var1.getScriptEnvironment().getEnvironment().put("&show_popup", this);
      var1.getScriptEnvironment().getEnvironment().put("&popup_clear", this);
      var1.getScriptEnvironment().getEnvironment().put("&insert_component", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }
}
