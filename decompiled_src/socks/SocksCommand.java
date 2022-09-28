package socks;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SocksCommand {
   public static final int COMMAND_CONNECT = 1;
   public static final int COMMAND_BIND = 2;
   public static final int REQUEST_GRANTED = 90;
   public static final int REQUEST_FAILED = 91;
   protected int version;
   protected int command;
   protected int dstport;
   protected int dstraw;
   protected String dstip;
   protected String userid;

   public static String toHost(long var0) {
      long var2 = ((var0 & -16777216L) >>> 24) % 255L;
      long var4 = ((var0 & 16711680L) >>> 16) % 255L;
      long var6 = ((var0 & 65280L) >>> 8) % 255L;
      long var8 = (var0 & 255L) % 255L;
      return var2 + "." + var4 + "." + var6 + "." + var8;
   }

   public void reply(OutputStream var1, int var2) throws IOException {
      if (var2 != 90 && var2 != 91) {
         throw new IllegalArgumentException("invalid SOCKS reply: " + var2);
      } else {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream(1024);
         DataOutputStream var4 = new DataOutputStream(var3);
         var4.writeByte(0);
         var4.writeByte(var2);
         var4.writeShort(this.dstport);
         if (this.getCommand() == 2) {
            var4.writeInt(0);
         } else {
            var4.writeInt(this.dstraw);
         }

         var1.write(var3.toByteArray());
      }
   }

   public String toString() {
      return "[version: " + this.version + ", command: " + this.command + ", dstip: " + this.dstip + ", dstport: " + this.dstport + ", userid: " + this.userid + "]";
   }

   public int getVersion() {
      return this.version;
   }

   public int getCommand() {
      return this.command;
   }

   public String getHost() {
      return this.dstip;
   }

   public int getPort() {
      return this.dstport;
   }

   protected String readString(DataInputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();

      while(true) {
         byte var3 = var1.readByte();
         if (var3 == 0) {
            return var2.toString();
         }

         var2.append((char)var3);
      }
   }

   public SocksCommand(InputStream var1) throws IOException {
      DataInputStream var2 = new DataInputStream(var1);
      this.version = var2.readUnsignedByte();
      if (this.version != 4) {
         throw new IOException("invalid SOCKS version: " + this.version);
      } else {
         this.command = var2.readUnsignedByte();
         if (this.command != 1 && this.command != 2) {
            throw new IOException("invalid SOCKS command: " + this.command);
         } else {
            this.dstport = var2.readUnsignedShort();
            this.dstraw = var2.readInt();
            this.userid = this.readString(var2);
            if ((this.dstraw & -256) == 0) {
               this.dstip = this.readString(var2);
            } else {
               this.dstip = toHost((long)this.dstraw);
            }

         }
      }
   }
}
