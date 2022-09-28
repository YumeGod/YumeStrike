package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.AObject;
import common.Callback;
import common.CommonUtils;
import common.ListenerUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScListenerDialog extends AObject implements DialogListener, Callback, ItemListener {
   protected JFrame dialog;
   protected Map options;
   protected String title;
   protected AggressorClient client;
   protected Observer observer;
   protected JPanel cards;
   protected CardLayout cardl;
   protected JComboBox box;
   protected String[] variants;

   public void setObserver(Observer var1) {
      this.observer = var1;
   }

   public ScListenerDialog(AggressorClient var1) {
      this(var1, new HashMap());
      this.title = "New Listener";
      this.options.put("payload", "Beacon HTTP");
      this.options.put("http_port", "80");
      this.options.put("http_host", DataUtils.getLocalIP(var1.getData()));
      this.options.put("https_port", "443");
      this.options.put("https_host", DataUtils.getLocalIP(var1.getData()));
      this.options.put("http_f_port", "80");
      this.options.put("https_f_port", "443");
      this.options.put("smb_pipe", DataUtils.getDefaultPipeName(var1.getData(), ".").substring(9));
      this.options.put("tcp_port", DataUtils.getProfile(var1.getData()).getInt(".tcp_port") + "");
      this.options.put("extc2_port", "2222");
      this.options.put("http_profile", "default");
      this.options.put("https_profile", "default");
      this.variants = DataUtils.getProfile(var1.getData()).getVariants();
   }

   public ScListenerDialog(AggressorClient var1, Map var2) {
      this.dialog = null;
      this.options = new HashMap();
      this.title = "Edit Listener";
      this.client = null;
      this.observer = null;
      this.cards = null;
      this.cardl = null;
      this.box = null;
      this.variants = new String[0];
      this.client = var1;
      this.options = ListenerUtils.mapToDialog(var2);
      this.variants = DataUtils.getProfile(var1.getData()).getVariants();
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = ListenerUtils.dialogToMap(var2);
      String var3 = (String)this.options.get("name");
      if (!this.title.equals("Edit Listener") && ListenerUtils.isLocalListener(this.client, var3)) {
         DialogUtils.showError("Listener '" + var3 + "' already exists.");
      } else if (ListenerUtils.validate(this.options)) {
         if (!DialogUtils.isShift(var1)) {
            DialogUtils.close(this.dialog);
         }

         this.client.getConnection().call("listeners.stop", CommonUtils.args(var3));
         this.client.getConnection().call("listeners.create", CommonUtils.args(var3, this.options), this);
      }
   }

   public void result(String var1, Object var2) {
      String var3 = (String)this.options.get("name");
      String var4 = var2 + "";
      if ("".equals(var4)) {
         if (this.observer != null) {
            this.observer.update((Observable)null, var3);
         }
      } else {
         if (!var4.equals("success")) {
            DialogUtils.showError("Could not start listener: \n" + var4);
            return;
         }

         if (this.observer != null) {
            this.observer.update((Observable)null, var3);
         } else {
            DialogUtils.showInfo("Started Listener");
         }
      }

   }

   public void itemStateChanged(ItemEvent var1) {
      String var2 = (String)this.box.getSelectedItem();
      if ("Beacon DNS".equals(var2)) {
         this.cardl.show(this.cards, "dns");
      } else if ("Beacon SMB".equals(var2)) {
         this.cardl.show(this.cards, "smb");
      } else if ("Beacon TCP".equals(var2)) {
         this.cardl.show(this.cards, "tcp");
      } else if ("Beacon HTTP".equals(var2)) {
         this.cardl.show(this.cards, "http");
      } else if ("Beacon HTTPS".equals(var2)) {
         this.cardl.show(this.cards, "https");
      } else if ("External C2".equals(var2)) {
         this.cardl.show(this.cards, "externalc2");
      } else if ("Foreign HTTP".equals(var2)) {
         this.cardl.show(this.cards, "http_foreign");
      } else if ("Foreign HTTPS".equals(var2)) {
         this.cardl.show(this.cards, "https_foreign");
      }

   }

   public void show_top(DialogManager var1) {
      var1.startGroup("top");
      String[] var2 = CommonUtils.toArray("Beacon DNS, Beacon HTTP, Beacon HTTPS, Beacon SMB, Beacon TCP, External C2, Foreign HTTP, Foreign HTTPS");
      DialogManager.DialogRow var3 = var1.text("name", "Name:", 30);
      DialogManager.DialogRow var4 = var1.combobox("payload", "Payload:", var2);
      this.box = (JComboBox)var4.get(1);
      this.box.addItemListener(this);
      if (this.title.equals("Edit Listener")) {
         this.box.setEnabled(false);
         var3.get(1).setEnabled(false);
      }

      var1.endGroup();
   }

   public void show_http_foreign(DialogManager var1) {
      var1.startGroup("http_foreign");
      var1.text("http_f_host", "HTTP Host (Stager):");
      var1.text("http_f_port", "HTTP Port (Stager):");
      var1.endGroup();
   }

   public void show_https_foreign(DialogManager var1) {
      var1.startGroup("https_foreign");
      var1.text("https_f_host", "HTTPS Host (Stager):");
      var1.text("https_f_port", "HTTPS Port (Stager):");
      var1.endGroup();
   }

   public void show_https(DialogManager var1) {
      var1.startGroup("https");
      var1.list_csv("https_hosts", "HTTPS Hosts:", "Add a callback host", DataUtils.getLocalIP(this.client.getData()), 120);
      var1.text("https_host", "HTTPS Host (Stager):");
      var1.combobox("https_profile", "Profile:", this.variants);
      var1.text("https_port", "HTTPS Port (C2):");
      var1.text("https_bind", "HTTPS Port (Bind):");
      var1.text("https_hosth", "HTTPS Host Header:");
      var1.proxyserver("https_proxy", "HTTPS Proxy:", this.client);
      var1.endGroup();
   }

   public void show_http(DialogManager var1) {
      var1.startGroup("http");
      var1.list_csv("http_hosts", "HTTP Hosts:", "Add a callback host", DataUtils.getLocalIP(this.client.getData()), 120);
      var1.text("http_host", "HTTP Host (Stager):");
      var1.combobox("http_profile", "Profile:", this.variants);
      var1.text("http_port", "HTTP Port (C2):");
      var1.text("http_bind", "HTTP Port (Bind):");
      var1.text("http_hosth", "HTTP Host Header:");
      var1.proxyserver("http_proxy", "HTTP Proxy:", this.client);
      var1.endGroup();
   }

   public void show_dns(DialogManager var1) {
      var1.startGroup("dns");
      var1.list_csv("dns_hosts", "DNS Hosts:", "Add a callback host", "", 120);
      var1.text("dns_host", "DNS Host (Stager):");
      var1.text("dns_bind", "DNS Port (Bind):");
      var1.endGroup();
   }

   public void show_externalc2(DialogManager var1) {
      var1.startGroup("externalc2");
      var1.text("extc2_port", "Port (Bind):");
      var1.checkbox_add("extc2_local", "", "Bind to localhost only");
      var1.endGroup();
   }

   public void show_tcp(DialogManager var1) {
      var1.startGroup("tcp");
      var1.text("tcp_port", "Port (C2):");
      var1.checkbox_add("tcp_local", "", "Bind to localhost only");
      var1.endGroup();
   }

   public void show_smb(DialogManager var1) {
      var1.startGroup("smb");
      var1.text("smb_pipe", "Pipename (C2):");
      var1.endGroup();
   }

   public void show() {
      this.dialog = DialogUtils.dialog(this.title, 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set(this.options);
      this.show_top(var1);
      this.show_http(var1);
      this.show_https(var1);
      this.show_dns(var1);
      this.show_tcp(var1);
      this.show_smb(var1);
      this.show_externalc2(var1);
      this.show_http_foreign(var1);
      this.show_https_foreign(var1);
      this.cards = new JPanel();
      this.cardl = new CardLayout();
      this.cards.setLayout(this.cardl);
      this.cards.add(var1.layout("http"), "http");
      this.cards.add(var1.layout("https"), "https");
      this.cards.add(DialogUtils.top(var1.layout("dns")), "dns");
      this.cards.add(DialogUtils.top(var1.layout("tcp")), "tcp");
      this.cards.add(DialogUtils.top(var1.layout("smb")), "smb");
      this.cards.add(DialogUtils.top(var1.layout("externalc2")), "externalc2");
      this.cards.add(DialogUtils.top(var1.layout("http_foreign")), "http_foreign");
      this.cards.add(DialogUtils.top(var1.layout("https_foreign")), "https_foreign");
      this.cards.setBorder(BorderFactory.createTitledBorder("Payload Options"));
      JPanel var2 = new JPanel();
      var2.setLayout(new BorderLayout());
      var2.add(var1.layout("top"), "North");
      var2.add(this.cards, "Center");
      JButton var3 = var1.action_noclose("Save");
      JButton var4 = var1.help("https://www.cobaltstrike.com/help-listener-management");
      this.dialog.add(DialogUtils.description("Create a listener."), "North");
      this.dialog.add(var2, "Center");
      this.dialog.add(DialogUtils.center(var3, var4), "South");
      this.itemStateChanged((ItemEvent)null);
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
