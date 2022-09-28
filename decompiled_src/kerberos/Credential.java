package kerberos;

import common.DataParser;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Credential {
   protected Principal client;
   protected Principal server;
   protected KeyBlock key;
   protected Times time;
   protected byte is_skey;
   protected int tktflags;
   protected List addresses = new LinkedList();
   protected List authdata = new LinkedList();
   protected String ticket;
   protected String second_ticket;

   public Credential(DataParser var1) throws IOException {
      this.client = new Principal(var1);
      this.server = new Principal(var1);
      this.key = new KeyBlock(var1);
      this.time = new Times(var1);
      this.is_skey = (byte)var1.readChar();
      this.tktflags = var1.readInt();
      int var2 = var1.readInt();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         this.addresses.add(new Address(var1));
      }

      var3 = var1.readInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         this.authdata.add(new AuthData(var1));
      }

      this.ticket = var1.readCountedString();
      this.second_ticket = var1.readCountedString();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Credential\n");
      var1.append("\tclient: " + this.client + "\n");
      var1.append("\tserver: " + this.client + "\n");
      var1.append("\tkey:    " + this.key + "\n");
      var1.append("\tticket: " + this.ticket.length() + "\n");
      var1.append("\tsecond: " + this.second_ticket.length() + "\n");
      return var1.toString();
   }
}
