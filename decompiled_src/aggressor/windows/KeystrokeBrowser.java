package aggressor.windows;

import aggressor.DataManager;
import aggressor.DataUtils;
import common.AObject;
import common.AdjustData;
import common.BeaconEntry;
import common.CommonUtils;
import common.Keystrokes;
import common.TeamQueue;
import console.Colors;
import console.Display;
import cortana.Cortana;
import dialog.DialogUtils;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import ui.DataBrowser;
import ui.DataSelectionListener;

public class KeystrokeBrowser extends AObject implements AdjustData, DataSelectionListener {
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected DataBrowser browser = null;
   protected Display content = null;
   protected Map sessions = new HashMap();
   protected Colors colors = new Colors(new Properties());

   public KeystrokeBrowser(DataManager var1, Cortana var2, TeamQueue var3) {
      this.engine = var2;
      this.conn = var3;
      this.data = var1;
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("keystrokes", this);
   }

   public JComponent getContent() {
      this.data.populateAndSubscribe("keystrokes", this);
      LinkedList var1 = new LinkedList(this.sessions.values());
      this.content = new Display(new Properties());
      this.browser = DataBrowser.getBeaconDataBrowser(this.engine, "document", this.content, var1);
      this.browser.addDataSelectionListener(this);
      DialogUtils.setupDateRenderer(this.browser.getTable(), "when");
      return this.browser;
   }

   public void selected(Object var1) {
      if (var1 != null) {
         StyledDocument var2 = (StyledDocument)var1;
         this.content.swap(var2);
         this.content.getConsole().setCaretPosition(var2.getLength());
      } else {
         this.content.clear();
      }

   }

   public Map format(String var1, Object var2) {
      final Keystrokes var3 = (Keystrokes)var2;
      if (!this.sessions.containsKey(var3.id())) {
         BeaconEntry var4 = DataUtils.getBeacon(this.data, var3.id());
         if (var4 == null) {
            return null;
         }

         Map var5 = var4.toMap();
         var5.put("_accent", "");
         var5.put("document", new DefaultStyledDocument());
         this.sessions.put(var3.id(), var5);
      }

      Map var7 = (Map)this.sessions.get(var3.id());
      final StyledDocument var6 = (StyledDocument)var7.get("document");
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            KeystrokeBrowser.this.colors.append(var6, var3.getKeystrokes());
            if (KeystrokeBrowser.this.content != null && var6 == KeystrokeBrowser.this.content.getConsole().getDocument()) {
               KeystrokeBrowser.this.content.getConsole().scrollRectToVisible(new Rectangle(0, KeystrokeBrowser.this.content.getConsole().getHeight() + 1, 1, 1));
            }

         }
      });
      var7.put("when", var3.time());
      return var7;
   }

   public void result(String var1, Object var2) {
      this.format(var1, var2);
      if (this.browser != null) {
         this.browser.setTable(this.sessions.values());
      }
   }
}
