package dns;

import common.CommonUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DNSServer implements Runnable {
   protected Thread fred;
   protected DatagramSocket server;
   protected DatagramPacket in = new DatagramPacket(new byte[512], 512);
   protected DatagramPacket out = new DatagramPacket(new byte[512], 512);
   protected Handler listener = null;
   protected int ttl = 1;
   public static final int DNS_TYPE_A = 1;
   public static final int DNS_TYPE_AAAA = 28;
   public static final int DNS_TYPE_CNAME = 5;
   public static final int DNS_TYPE_TXT = 16;
   protected boolean isup = true;

   public void setDefaultTTL(int var1) {
      this.ttl = var1;
   }

   public static Response A(long var0) {
      Response var2 = new Response();
      var2.type = 1;
      var2.addr4 = var0;
      return var2;
   }

   public static Response TXT(byte[] var0) {
      Response var1 = new Response();
      var1.type = 16;
      var1.data = var0;
      return var1;
   }

   public static Response AAAA(byte[] var0) {
      Response var1 = new Response();
      var1.type = 28;
      var1.data = var0;
      return var1;
   }

   public void installHandler(Handler var1) {
      this.listener = var1;
   }

   public byte[] respond(byte[] var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(512);
      DataOutputStream var3 = new DataOutputStream(var2);
      DNSHeader var4 = new DNSHeader(var1);
      var4.flags = (short)var4.flags | '耀';
      ++var4.ancount;
      var4.nscount = 0;
      var4.arcount = 0;
      Iterator var5 = var4.getQuestions().iterator();

      DNSQuestion var6;
      DNSAnswer var7;
      while(var5.hasNext()) {
         var6 = (DNSQuestion)var5.next();
         var7 = new DNSAnswer(var6);
         var6.setAnswer(var7);
      }

      var5 = var4.getQuestions().iterator();

      while(var5.hasNext()) {
         var6 = (DNSQuestion)var5.next();
         var7 = var6.getAnswer();
         if (var6.getType() == 28 && var7.getType() == 1) {
            --var4.ancount;
         }
      }

      var3.writeShort(var4.id);
      var3.writeShort(var4.flags);
      var3.writeShort(var4.qdcount);
      var3.writeShort(var4.ancount);
      var3.writeShort(var4.nscount);
      var3.writeShort(var4.arcount);
      var5 = var4.getQuestions().iterator();

      DNSQuestion var10;
      for(int var9 = 12; var5.hasNext(); var9 += var10.getSize()) {
         var10 = (DNSQuestion)var5.next();
         var3.write(var1, var9, var10.getSize());
      }

      var5 = var4.getQuestions().iterator();

      while(true) {
         while(var5.hasNext()) {
            var10 = (DNSQuestion)var5.next();
            DNSAnswer var8 = var10.getAnswer();
            if (var10.getType() == 28 && var8.getType() == 1) {
               CommonUtils.print_warn("Dropped AAAA request for: " + var10.qname + " (A request expected)");
            } else {
               var3.write(var8.getAnswer());
            }
         }

         var3.close();
         return var2.toByteArray();
      }
   }

   public DNSServer(int var1) throws IOException {
      this.server = new DatagramSocket(var1);
   }

   public void stop() {
      this.isup = false;
      this.fred.interrupt();

      try {
         this.server.close();
      } catch (Exception var2) {
      }

   }

   public void run() {
      while(this.isup) {
         try {
            this.server.receive(this.in);
            new DNSHeader(this.in.getData());
            this.out.setAddress(this.in.getAddress());
            this.out.setPort(this.in.getPort());
            this.out.setData(this.respond(this.in.getData()));
            this.server.send(this.out);
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

      try {
         this.server.close();
      } catch (Exception var3) {
      }

      CommonUtils.print_info("DNS server stopped");
   }

   public void go() {
      this.fred = new Thread(this);
      this.fred.start();
   }

   private static class DNSHeader {
      public int id;
      public int flags;
      public int qdcount;
      public int ancount;
      public int nscount;
      public int arcount;
      protected List questions = new LinkedList();

      public DNSHeader(byte[] var1) throws IOException {
         DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(var1));
         this.id = var2.readUnsignedShort();
         this.flags = var2.readUnsignedShort();
         this.qdcount = var2.readUnsignedShort();
         this.ancount = var2.readUnsignedShort();
         this.nscount = var2.readUnsignedShort();
         this.arcount = var2.readUnsignedShort();

         for(int var3 = 0; var3 < this.qdcount; ++var3) {
            DNSQuestion var4 = new DNSQuestion(var2);
            this.questions.add(var4);
         }

      }

      public List getQuestions() {
         return this.questions;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("DNS Header\n");
         var1.append("ID:      " + Integer.toHexString(this.id) + "\n");
         var1.append("Flags:   " + Integer.toBinaryString(this.flags) + "\n");
         var1.append("QdCount: " + this.qdcount + "\n");
         var1.append("AnCount: " + this.ancount + "\n");
         var1.append("NsCount: " + this.nscount + "\n");
         var1.append("ArCount: " + this.arcount + "\n");
         Iterator var2 = this.questions.iterator();

         while(var2.hasNext()) {
            DNSQuestion var3 = (DNSQuestion)var2.next();
            var1.append(var3);
         }

         return var1.toString();
      }
   }

   private static class DNSQuestion {
      public String qname;
      public int qtype;
      public int qclass;
      public int size = 0;
      public DNSAnswer answer = null;

      public DNSQuestion(DataInputStream var1) throws IOException {
         StringBuffer var2 = new StringBuffer();
         int var3 = var1.readUnsignedByte();
         ++this.size;

         while(var3 > 0) {
            for(int var4 = 0; var4 < var3; ++var4) {
               var2.append((char)var1.readUnsignedByte());
               ++this.size;
            }

            var2.append(".");
            var3 = var1.readUnsignedByte();
            ++this.size;
         }

         this.qname = var2.toString();
         this.qtype = var1.readUnsignedShort();
         this.qclass = var1.readUnsignedShort();
         this.size += 4;
      }

      public DNSAnswer getAnswer() {
         return this.answer;
      }

      public void setAnswer(DNSAnswer var1) {
         this.answer = var1;
      }

      public int getType() {
         return this.qtype;
      }

      public int getSize() {
         return this.size;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("\tQuestion: '" + this.qname + "' size: " + this.size + " bytes\n");
         var1.append("\tQType:    " + Integer.toHexString(this.qtype) + "\n");
         var1.append("\tQClass:   " + Integer.toHexString(this.qclass) + "\n\n");
         return var1.toString();
      }
   }

   private class DNSAnswer {
      protected ByteArrayOutputStream raw = new ByteArrayOutputStream(512);
      protected int type;

      public byte[] getAnswer() {
         return this.raw.toByteArray();
      }

      public int getType() {
         return this.type;
      }

      public DNSAnswer(DNSQuestion var2) throws IOException {
         DataOutputStream var3 = new DataOutputStream(this.raw);
         String[] var4 = var2.qname.split("\\.");

         int var6;
         for(int var5 = 0; var5 < var4.length; ++var5) {
            var3.writeByte(var4[var5].length());

            for(var6 = 0; var6 < var4[var5].length(); ++var6) {
               var3.writeByte(var4[var5].charAt(var6));
            }
         }

         var3.writeByte(0);
         if (DNSServer.this.listener != null) {
            Response var7 = DNSServer.this.listener.respond(var2.qname, var2.getType());
            if (var7 == null) {
               System.err.println("Response for question is null\n" + var2);
               var7 = DNSServer.A(0L);
            }

            var3.writeShort(var7.type);
            var3.writeShort(1);
            var3.writeInt(DNSServer.this.ttl);
            this.type = var7.type;
            label39:
            switch (var7.type) {
               case 1:
                  var3.writeShort(4);
                  var3.writeInt((int)var7.addr4);
                  break;
               case 16:
                  var3.writeShort(var7.data.length + 1);
                  var3.writeByte(var7.data.length);
                  var6 = 0;

                  while(true) {
                     if (var6 >= var7.data.length) {
                        break label39;
                     }

                     var3.writeByte(var7.data[var6]);
                     ++var6;
                  }
               case 28:
                  var3.writeShort(16);

                  for(var6 = 0; var6 < 16; ++var6) {
                     if (var6 < var7.data.length) {
                        var3.writeByte(var7.data[var6]);
                     } else {
                        var3.writeByte(0);
                     }
                  }
            }
         }

         var3.close();
      }
   }

   public interface Handler {
      Response respond(String var1, int var2);
   }

   public static final class Response {
      public int type = 0;
      public long addr4;
      public long[] addr6;
      public byte[] data;
   }
}
