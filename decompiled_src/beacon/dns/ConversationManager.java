package beacon.dns;

import c2profile.Profile;
import common.CommonUtils;
import java.util.HashMap;
import java.util.Map;

public class ConversationManager {
   protected Map conversations = new HashMap();
   protected int maxtxt;
   protected long idlemask;

   public ConversationManager(Profile var1) {
      this.maxtxt = var1.getInt(".dns_max_txt");
      this.idlemask = CommonUtils.ipToLong(var1.getString(".dns_idle"));
   }

   public RecvConversation getRecvConversation(String var1, String var2) {
      return (RecvConversation)this.getConversation(var1, var2, RecvConversation.class);
   }

   public SendConversation getSendConversationA(String var1, String var2) {
      return (SendConversation)this.getConversation(var1, var2, SendConversationA.class);
   }

   public SendConversation getSendConversationAAAA(String var1, String var2) {
      return (SendConversation)this.getConversation(var1, var2, SendConversationAAAA.class);
   }

   public SendConversation getSendConversationTXT(String var1, String var2) {
      return (SendConversation)this.getConversation(var1, var2, SendConversationTXT.class);
   }

   public Map getConversations(String var1) {
      if (!this.conversations.containsKey(var1)) {
         this.conversations.put(var1, new Entry());
      }

      Entry var2 = (Entry)this.conversations.get(var1);
      var2.last = System.currentTimeMillis();
      return var2.convos;
   }

   public Object getConversation(String var1, String var2, Class var3) {
      Map var4 = this.getConversations(var1);
      if (!var4.containsKey(var2)) {
         if (var3 == RecvConversation.class) {
            RecvConversation var8 = new RecvConversation(var1, var2);
            var4.put(var2, var8);
            return var8;
         } else if (var3 == SendConversationA.class) {
            SendConversationA var7 = new SendConversationA(var1, var2, this.idlemask);
            var4.put(var2, var7);
            return var7;
         } else if (var3 == SendConversationAAAA.class) {
            SendConversationAAAA var6 = new SendConversationAAAA(var1, var2, this.idlemask);
            var4.put(var2, var6);
            return var6;
         } else if (var3 == SendConversationTXT.class) {
            SendConversationTXT var5 = new SendConversationTXT(var1, var2, this.idlemask, this.maxtxt);
            var4.put(var2, var5);
            return var5;
         } else {
            return null;
         }
      } else {
         return var4.get(var2);
      }
   }

   public void removeConversation(String var1, String var2) {
      if (this.conversations.containsKey(var1)) {
         Map var3 = this.getConversations(var1);
         var3.remove(var2);
         if (var3.size() == 0) {
            this.conversations.remove(var1);
         }

      }
   }

   public void purge(String var1) {
      if (this.conversations.containsKey(var1)) {
         Entry var2 = (Entry)this.conversations.get(var1);
         if (System.currentTimeMillis() - var2.last <= 15000L && var2.hits <= 256L) {
            CommonUtils.print_warn("Protected " + var2.convos.size() + " open conversation(s) for " + var1 + " (strike " + var2.hits + " of 256)");
            ++var2.hits;
         } else {
            this.conversations.remove(var1);
            CommonUtils.print_error("Purged " + var2.convos.size() + " stalled conversation(s) for " + var1);
         }

      }
   }

   private static class Entry {
      public Map convos;
      public long last;
      public long hits;

      private Entry() {
         this.convos = new HashMap();
         this.last = System.currentTimeMillis();
         this.hits = 0L;
      }

      // $FF: synthetic method
      Entry(Object var1) {
         this();
      }
   }
}
