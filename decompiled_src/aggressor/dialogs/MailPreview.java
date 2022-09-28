package aggressor.dialogs;

import common.AObject;
import common.CommonUtils;
import common.MudgeSanity;
import console.Display;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import mail.Eater;
import phish.PhishingUtils;
import ui.ATextField;

public class MailPreview extends AObject implements ActionListener {
   protected JFrame dialog = null;
   protected String templatef = null;
   protected String attachf = null;
   protected LinkedList contacts = null;
   protected String urlv = null;
   protected String cRaw = null;
   protected String cHtml = null;
   protected String cText = null;

   public boolean processOptions() {
      try {
         this._processOptions();
         return true;
      } catch (Exception var2) {
         DialogUtils.showError("Trouble processing " + this.templatef + ":\n" + var2.getMessage());
         MudgeSanity.logException("process phishing preview", var2, false);
         return false;
      }
   }

   public void _processOptions() throws IOException {
      Eater var1 = new Eater(this.templatef);
      if (!"".equals(this.attachf) && this.attachf.length() > 0 && (new File(this.attachf)).exists()) {
         var1.attachFile(this.attachf);
      }

      Map var2 = (Map)CommonUtils.pick((List)this.contacts);
      String var3 = (String)var2.get("To");
      String var4 = (String)var2.get("To_Name") + "";
      byte[] var5 = var1.getMessage((String)null, var4.length() > 0 ? var4 + " <" + var3 + ">" : var3);
      String var6 = PhishingUtils.updateMessage(CommonUtils.bString(var5), var2, this.urlv, "1234567890ab");
      Eater var7 = new Eater(new ByteArrayInputStream(CommonUtils.toBytes(var6)));
      this.cHtml = var7.getMessageEntity("text/html");
      this.cText = var7.getMessageEntity("text/plain");
      this.cRaw = var6;
      var1.done();
      var7.done();
   }

   public MailPreview(Map var1) {
      this.templatef = DialogUtils.string(var1, "template");
      this.attachf = DialogUtils.string(var1, "attachment");
      this.contacts = (LinkedList)var1.get("targets");
      this.urlv = DialogUtils.string(var1, "url");
   }

   public void actionPerformed(ActionEvent var1) {
      this.dialog.setVisible(false);
      this.dialog.dispose();
   }

   public JComponent buildRaw() {
      Display var1 = new Display(new Properties());
      var1.setFont(Font.decode("Monospaced BOLD 14"));
      var1.setForeground(Color.decode("#ffffff"));
      var1.setBackground(Color.decode("#000000"));
      var1.setTextDirect(this.cRaw);
      return var1;
   }

   public byte[] buildHTMLScreenshot() {
      JEditorPane var1 = new JEditorPane();
      var1.setContentType("text/html");
      DialogUtils.workAroundEditorBug(var1);
      var1.setEditable(false);
      var1.setOpaque(true);
      var1.setCaretPosition(0);
      var1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      var1.setText(this.cHtml);
      var1.setSize(new Dimension(640, 480));
      return DialogUtils.screenshot(var1);
   }

   public JComponent buildHTML() {
      final ATextField var1 = new ATextField();
      final JEditorPane var2 = new JEditorPane();
      var2.setContentType("text/html");
      DialogUtils.workAroundEditorBug(var2);
      var2.setEditable(false);
      var2.setOpaque(true);
      var2.setCaretPosition(0);
      var2.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      var2.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent var1x) {
            String var2 = var1x.getEventType() + "";
            if (var2.equals("ENTERED")) {
               var1.setText(var1x.getURL() + "");
               var1.setCaretPosition(0);
            } else if (var2.equals("EXITED")) {
               var1.setText("");
            } else if (var2.equals("ACTIVATED")) {
               DialogUtils.showInput(MailPreview.this.dialog, "You clicked", var1x.getURL() + "");
            }

         }
      });
      (new Thread(new Runnable() {
         public void run() {
            var2.setText(MailPreview.this.cHtml);
         }
      }, "buildHTML")).start();
      JPanel var3 = new JPanel();
      var3.setLayout(new BorderLayout());
      var3.add(new JScrollPane(var2), "Center");
      var3.add(var1, "South");
      return var3;
   }

   public JComponent buildText() {
      JEditorPane var1 = new JEditorPane();
      var1.setContentType("text/plain");
      var1.setText(this.cText);
      var1.setEditable(false);
      var1.setOpaque(true);
      var1.setCaretPosition(0);
      var1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      return var1;
   }

   public void show() {
      if (this.processOptions()) {
         this.dialog = DialogUtils.dialog("Preview", 640, 480);
         this.dialog.setLayout(new BorderLayout());
         JTabbedPane var1 = new JTabbedPane();
         var1.addTab("Raw", this.buildRaw());
         var1.addTab("HTML", this.buildHTML());
         var1.addTab("Text", new JScrollPane(this.buildText()));
         JButton var2 = new JButton("Close");
         var2.addActionListener(this);
         this.dialog.add(var1, "Center");
         this.dialog.add(DialogUtils.center((JComponent)var2), "South");
         this.dialog.setVisible(true);
         this.dialog.show();
      }
   }
}
