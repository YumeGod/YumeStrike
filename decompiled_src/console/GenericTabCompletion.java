package console;

import common.CommonUtils;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import javax.swing.AbstractAction;

public abstract class GenericTabCompletion {
   protected Console window;
   protected String last = null;
   protected Iterator tabs = null;

   public Console getWindow() {
      return this.window;
   }

   public GenericTabCompletion(Console var1) {
      this.window = var1;
      this.window.addActionForKey("pressed TAB", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            GenericTabCompletion.this.tabComplete(var1);
         }
      });
   }

   public abstract Collection getOptions(String var1);

   public String transformText(String var1) {
      return var1;
   }

   private void tabCompleteFirst(String var1) {
      try {
         var1 = this.transformText(var1);
         LinkedHashSet var2 = new LinkedHashSet();
         Collection var3 = this.getOptions(var1);
         if (var3 == null) {
            return;
         }

         String var6;
         String var7;
         for(Iterator var4 = var3.iterator(); var4.hasNext(); var2.add(var6 + var7)) {
            String var5 = var4.next() + "";
            if (var1.length() > var5.length()) {
               var6 = var5;
               var7 = "";
            } else {
               var6 = var5.substring(0, var1.length());
               var7 = var5.substring(var1.length());
            }

            int var8;
            if ((var8 = var7.indexOf(47)) > -1 && var8 + 1 < var7.length()) {
               var7 = var7.substring(0, var8);
            }
         }

         var2.add(var1);
         synchronized(this.window) {
            this.tabs = var2.iterator();
            this.last = (String)this.tabs.next();
         }

         CommonUtils.runSafe(new Runnable() {
            public void run() {
               GenericTabCompletion.this.window.getInput().setText(GenericTabCompletion.this.last);
            }
         });
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public void tabComplete(ActionEvent var1) {
      final String var2 = this.window.getInput().getText();
      if (var2.length() != 0) {
         synchronized(this.window) {
            if (this.tabs != null && this.tabs.hasNext() && var2.equals(this.last)) {
               this.last = (String)this.tabs.next();
               this.window.getInput().setText(this.last);
            } else {
               (new Thread(new Runnable() {
                  public void run() {
                     GenericTabCompletion.this.tabCompleteFirst(var2);
                  }
               })).start();
            }
         }
      }
   }
}
