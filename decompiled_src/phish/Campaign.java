package phish;

import common.AObject;
import common.CommonUtils;
import common.MudgeSanity;
import common.PhishEvents;
import common.Request;
import dialog.DialogUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mail.Eater;
import server.ManageUser;
import server.Phisher;
import server.Resources;
import server.ServerUtils;

public class Campaign extends AObject implements Runnable, SmtpNotify {
   protected Request request;
   protected ManageUser client;
   protected Resources resources;
   protected Map options;
   protected Phisher phisher;
   protected String templated;
   protected String sid;
   protected boolean keepgoing = true;

   public Campaign(Phisher var1, Request var2, ManageUser var3, Resources var4) {
      this.phisher = var1;
      this.request = var2;
      this.client = var3;
      this.resources = var4;
      this.sid = (String)var2.arg(0);
      this.templated = (String)var2.arg(1);
      this.options = (Map)var2.arg(2);
      (new Thread(this, "Phishing Campaign")).start();
   }

   public void update(String var1) {
      this.resources.send((ManageUser)this.client, "phishstatus." + this.sid, var1);
   }

   public void cancel() {
      this.keepgoing = false;
   }

   public void run() {
      try {
         String var1 = DialogUtils.string(this.options, "attachmentr");
         String var2 = DialogUtils.string(this.options, "template");
         String var3 = DialogUtils.string(this.options, "bounce");
         String var4 = DialogUtils.string(this.options, "server");
         String var5 = DialogUtils.string(this.options, "url");
         List var6 = (List)this.options.get("targets");
         PhishEvents var7 = new PhishEvents(this.sid);
         Eater var8 = new Eater(new ByteArrayInputStream(CommonUtils.toBytes(this.templated)));
         String var9 = var8.getSubject();
         if (var1 != null && !"".equals(var1) && (new File(var1)).exists()) {
            var8.attachFile(var1);
         }

         this.resources.sendAndProcess(this.client, "phishlog." + this.sid, var7.SendmailStart(var6.size(), var1, var3, var4, var9, var2, var5));
         Iterator var10 = var6.iterator();

         while(var10.hasNext() && this.keepgoing) {
            Map var11 = (Map)var10.next();
            String var12 = var11.get("To") + "";
            String var13 = var11.get("To_Name") + "";
            String var14 = CommonUtils.ID().substring(24, 36);
            ServerUtils.addToken(this.resources, var14, var12, this.sid);
            SmtpClient var15 = new SmtpClient(this);

            try {
               this.resources.sendAndProcess(this.client, "phishlog." + this.sid, var7.SendmailPre(var12));
               String var16 = CommonUtils.bString(var8.getMessage((String)null, "".equals(var13) ? var12 : var13 + " <" + var12 + ">"));
               var16 = PhishingUtils.updateMessage(var16, var11, var5, var14);
               String var17 = var15.send_email(var4, var3, var12, var16);
               this.resources.sendAndProcess(this.client, "phishlog." + this.sid, var7.SendmailPost(var12, "SUCCESS", var17, var14));
            } catch (Exception var18) {
               MudgeSanity.logException("phish to " + var12 + " via " + var4, var18, false);
               this.resources.sendAndProcess(this.client, "phishlog." + this.sid, var7.SendmailPost(var12, "Failed", var18.getMessage(), var14));
            }

            var15.cleanup();
            this.update("");
         }

         this.resources.sendAndProcess(this.client, "phishlog." + this.sid, var7.SendmailDone());
         this.resources.call("tokens.push");
      } catch (Exception var19) {
         MudgeSanity.logException("Campaign", var19, false);
      }

   }
}
