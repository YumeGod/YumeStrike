package c2profile;

import cloudstrike.Response;
import common.CommonUtils;
import common.MudgeSanity;
import dialog.DialogUtils;
import encoders.Base64;
import encoders.Base64Url;
import encoders.MaskEncoder;
import encoders.NetBIOS;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Program implements Serializable {
   protected boolean sealed = false;
   protected boolean posts = false;
   protected boolean cookie = false;
   protected boolean host = false;
   public static final int APPEND = 1;
   public static final int PREPEND = 2;
   public static final int BASE64 = 3;
   public static final int PRINT = 4;
   public static final int PARAMETER = 5;
   public static final int HEADER = 6;
   public static final int BUILD = 7;
   public static final int NETBIOS = 8;
   public static final int _PARAMETER = 9;
   public static final int _HEADER = 10;
   public static final int NETBIOSU = 11;
   public static final int URI_APPEND = 12;
   public static final int BASE64URL = 13;
   public static final int STRREP = 14;
   public static final int MASK = 15;
   public static final int _HOSTHEADER = 16;
   protected List tsteps = new LinkedList();
   protected LinkedList rsteps = new LinkedList();

   public boolean usesHost() {
      return this.host;
   }

   public boolean usesCookie() {
      return this.cookie;
   }

   public boolean isSealed() {
      return this.sealed;
   }

   public boolean postsData() {
      return this.posts;
   }

   public void addStep(String var1, String var2) {
      Statement var3 = new Statement();
      var3.argument = var2;
      if (var2 != null) {
         var3.alen = var2.length();
      }

      if (var1.equals("append")) {
         var3.action = 1;
      } else if (var1.equals("prepend")) {
         var3.action = 2;
      } else if (var1.equals("base64")) {
         var3.action = 3;
      } else if (var1.equals("print")) {
         var3.action = 4;
         this.sealed = true;
         this.posts = true;
      } else if (var1.equals("parameter")) {
         var3.action = 5;
         this.sealed = true;
      } else if (var1.equals("header")) {
         var3.action = 6;
         this.sealed = true;
      } else if (var1.equals("build")) {
         var3.action = 7;
      } else if (var1.equals("netbios")) {
         var3.action = 8;
      } else if (var1.equals("!parameter")) {
         var3.action = 9;
      } else if (var1.equals("!header")) {
         var3.action = 10;
      } else if (var1.equals("!hostheader")) {
         var3.action = 16;
      } else if (var1.equals("netbiosu")) {
         var3.action = 11;
      } else if (var1.equals("uri-append")) {
         var3.action = 12;
         this.sealed = true;
      } else if (var1.equals("base64url")) {
         var3.action = 13;
      } else if (var1.equals("strrep")) {
         var3.action = 14;
      } else {
         if (!var1.equals("mask")) {
            throw new RuntimeException("Invalid action: " + var1);
         }

         var3.action = 15;
      }

      if (var3.action == 6 && var2 != null && "cookie".equals(var2.toLowerCase())) {
         this.cookie = true;
      } else if (var3.action == 10 && var2 != null && var2.toLowerCase().startsWith("cookie: ")) {
         this.cookie = true;
      } else if (var3.action == 6 && var2 != null && "host".equals(var2.toLowerCase())) {
         this.host = true;
      }

      this.tsteps.add(var3);
      this.rsteps.addFirst(var3);
   }

   public byte[] transform_binary(Profile var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(1024);
      DataOutputStream var3 = new DataOutputStream(var2);
      this.transform_binary(var1, var3);
      return var2.toByteArray();
   }

   public void transform_binary(Profile var1, DataOutputStream var2) throws IOException {
      Iterator var3 = this.tsteps.iterator();

      while(true) {
         label88:
         while(var3.hasNext()) {
            Statement var4 = (Statement)var3.next();
            int var5;
            switch (var4.action) {
               case 1:
                  var2.writeInt(1);
                  var2.writeInt(var4.alen);
                  var5 = 0;

                  while(true) {
                     if (var5 >= var4.argument.length()) {
                        continue label88;
                     }

                     var2.write((byte)var4.argument.charAt(var5));
                     ++var5;
                  }
               case 2:
                  var2.writeInt(2);
                  var2.writeInt(var4.alen);
                  var5 = 0;

                  while(true) {
                     if (var5 >= var4.argument.length()) {
                        continue label88;
                     }

                     var2.write((byte)var4.argument.charAt(var5));
                     ++var5;
                  }
               case 3:
                  var2.writeInt(3);
                  break;
               case 4:
                  var2.writeInt(4);
                  break;
               case 5:
                  var2.writeInt(5);
                  var2.writeInt(var4.alen);
                  var5 = 0;

                  while(true) {
                     if (var5 >= var4.argument.length()) {
                        continue label88;
                     }

                     var2.write((byte)var4.argument.charAt(var5));
                     ++var5;
                  }
               case 6:
                  var2.writeInt(6);
                  var2.writeInt(var4.alen);
                  var5 = 0;

                  while(true) {
                     if (var5 >= var4.argument.length()) {
                        continue label88;
                     }

                     var2.write((byte)var4.argument.charAt(var5));
                     ++var5;
                  }
               case 7:
                  var2.writeInt(7);
                  if (var4.argument.endsWith("metadata")) {
                     var2.writeInt(0);
                  } else if (var4.argument.endsWith("id")) {
                     var2.writeInt(0);
                  } else if (var4.argument.endsWith("output")) {
                     var2.writeInt(1);
                  } else {
                     System.err.println("UNKNOWN DATA ARGUMENT: " + var4.argument);
                  }

                  var1.getProgram(var4.argument).transform_binary(var1, var2);
                  break;
               case 8:
                  var2.writeInt(8);
                  break;
               case 9:
                  var2.writeInt(9);
                  var2.writeInt(var4.alen);
                  var5 = 0;

                  while(true) {
                     if (var5 >= var4.argument.length()) {
                        continue label88;
                     }

                     var2.write((byte)var4.argument.charAt(var5));
                     ++var5;
                  }
               case 10:
                  var2.writeInt(10);
                  var2.writeInt(var4.alen);
                  var5 = 0;

                  while(true) {
                     if (var5 >= var4.argument.length()) {
                        continue label88;
                     }

                     var2.write((byte)var4.argument.charAt(var5));
                     ++var5;
                  }
               case 11:
                  var2.writeInt(11);
                  break;
               case 12:
                  var2.writeInt(12);
                  break;
               case 13:
                  var2.writeInt(13);
               case 14:
               default:
                  break;
               case 15:
                  var2.writeInt(15);
                  break;
               case 16:
                  var2.writeInt(16);
                  var2.writeInt(var4.alen);

                  for(var5 = 0; var5 < var4.argument.length(); ++var5) {
                     var2.write((byte)var4.argument.charAt(var5));
                  }
            }
         }

         return;
      }
   }

   public void transform(Profile var1, Response var2, byte[] var3) {
      SmartBuffer var4 = new SmartBuffer();
      var4.append(var3);
      this.transform(var1, var2, var4);
   }

   public void transform(Profile var1, Response var2, SmartBuffer var3) {
      Iterator var4 = this.tsteps.iterator();

      while(var4.hasNext()) {
         Statement var6 = (Statement)var4.next();
         String var5;
         switch (var6.action) {
            case 1:
               var3.append(toBytes(var6.argument));
               break;
            case 2:
               var3.prepend(toBytes(var6.argument));
               break;
            case 3:
               var5 = Base64.encode(var3.getBytes());
               var3.clear();
               var3.append(toBytes(var5));
               break;
            case 4:
               byte[] var7 = var3.getBytes();
               var2.data = new ByteArrayInputStream(var7);
               var2.size = (long)var7.length;
               var2.offset = (long)var3.getDataOffset();
               var2.addHeader("Content-Length", var7.length + "");
               break;
            case 5:
               var2.addParameter(var6.argument + "=" + toBinaryString(var3.getBytes()));
               break;
            case 6:
               var2.addHeader(var6.argument, toBinaryString(var3.getBytes()));
               break;
            case 7:
               if (".http-post.client.output".equals(var6.argument)) {
                  SmartBuffer var8 = new SmartBuffer();
                  var8.append(CommonUtils.randomData(16));
                  var1.getProgram(var6.argument).transform(var1, var2, var8);
               } else {
                  var1.getProgram(var6.argument).transform(var1, var2, var3);
               }
               break;
            case 8:
               var5 = NetBIOS.encode('a', var3.getBytes());
               var3.clear();
               var3.append(toBytes(var5));
               break;
            case 9:
               var2.addParameter(var6.argument);
               break;
            case 10:
            case 16:
               var2.addHeader(var6.argument);
               break;
            case 11:
               var5 = NetBIOS.encode('A', var3.getBytes());
               var3.clear();
               var3.append(toBytes(var5));
               break;
            case 12:
               var2.uri = toBinaryString(var3.getBytes());
               break;
            case 13:
               var5 = Base64Url.encode(var3.getBytes());
               var3.clear();
               var3.append(toBytes(var5));
               break;
            case 14:
            default:
               System.err.println("Unknown: " + var6);
               break;
            case 15:
               var5 = toBinaryString(MaskEncoder.encode(var3.getBytes()));
               var3.clear();
               var3.append(toBytes(var5));
         }
      }

   }

   public byte[] recover_binary() throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(1024);
      DataOutputStream var2 = new DataOutputStream(var1);
      Iterator var3 = this.rsteps.iterator();

      while(true) {
         label45:
         while(var3.hasNext()) {
            Statement var4 = (Statement)var3.next();
            int var5;
            switch (var4.action) {
               case 1:
                  var2.writeInt(1);
                  var2.writeInt(var4.alen);
                  break;
               case 2:
                  var2.writeInt(2);
                  var2.writeInt(var4.alen);
                  break;
               case 3:
                  var2.writeInt(3);
                  break;
               case 4:
                  var2.writeInt(4);
                  break;
               case 5:
                  var2.writeInt(5);
                  var2.writeInt(var4.alen);
                  var5 = 0;

                  while(true) {
                     if (var5 >= var4.argument.length()) {
                        continue label45;
                     }

                     var2.write(var4.argument.charAt(var5));
                     ++var5;
                  }
               case 6:
                  var2.writeInt(5);
                  var2.writeInt(var4.alen);

                  for(var5 = 0; var5 < var4.argument.length(); ++var5) {
                     var2.write(var4.argument.charAt(var5));
                  }
               case 7:
               case 9:
               case 10:
               case 12:
               case 14:
               case 16:
               default:
                  break;
               case 8:
                  var2.writeInt(8);
                  break;
               case 11:
                  var2.writeInt(11);
                  break;
               case 13:
                  var2.writeInt(13);
                  break;
               case 15:
                  var2.writeInt(15);
            }
         }

         return var1.toByteArray();
      }
   }

   public List collissions(Profile var1) {
      HashSet var2 = new HashSet();
      HashMap var3 = new HashMap();
      LinkedList var4 = new LinkedList();
      this.collissions(var1, (String)null, var2, var3, var4);
      return var4;
   }

   private static String[] split(String var0) {
      String[] var1 = var0.split("[:=]");
      if (var1.length != 2) {
         return new String[]{var0, ""};
      } else {
         var1[1] = var1[1].trim();
         return var1;
      }
   }

   public void collissions(Profile var1, String var2, Set var3, Map var4, List var5) {
      Iterator var6 = this.tsteps.iterator();

      while(var6.hasNext()) {
         String var8 = null;
         String var9 = null;
         Statement var10 = (Statement)var6.next();
         String[] var7;
         switch (var10.action) {
            case 4:
               var8 = "print";
               var9 = "block '" + var2 + "'";
               break;
            case 5:
               var8 = "parameter " + var10.argument;
               var9 = "block '" + var2 + "'";
               break;
            case 6:
               var8 = "header " + var10.argument;
               var9 = "block '" + var2 + "'";
               break;
            case 7:
               var1.getProgram(var10.argument).collissions(var1, var10.argument, var3, var4, var5);
            case 8:
            case 11:
            case 13:
            case 14:
            case 15:
            default:
               break;
            case 9:
               var7 = split(var10.argument);
               var8 = "parameter " + var7[0];
               var9 = "value '" + var7[1] + "'";
               break;
            case 10:
            case 16:
               var7 = split(var10.argument);
               var8 = "header " + var7[0];
               var9 = "value '" + var7[1] + "'";
               break;
            case 12:
               var8 = "uri-append";
               var9 = "block '" + var2 + "'";
         }

         if (var8 != null) {
            if (var3.contains(var8)) {
               var5.add(var8 + ": " + var9 + ", " + var4.get(var8));
            } else {
               var3.add(var8);
               var4.put(var8, var9);
            }
         }
      }

   }

   private static final String toBinaryString(byte[] var0) {
      try {
         return new String(var0, "ISO8859-1");
      } catch (UnsupportedEncodingException var2) {
         return "";
      }
   }

   public static final byte[] toBytes(String var0) {
      int var1 = var0.length();
      byte[] var2 = new byte[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = (byte)var0.charAt(var3);
      }

      return var2;
   }

   public String recover(Map var1, Map var2, String var3, String var4) {
      String var5 = "";
      Iterator var6 = this.rsteps.iterator();

      while(var6.hasNext()) {
         Statement var7 = (Statement)var6.next();
         switch (var7.action) {
            case 1:
               try {
                  var5 = var5.substring(0, var5.length() - var7.alen);
                  break;
               } catch (RuntimeException var12) {
                  MudgeSanity.logException("substr('" + var5.replaceAll("\\P{Print}", ".") + "', 0, " + var5.length() + " - " + var7.alen + ")", var12, false);
                  return "";
               }
            case 2:
               try {
                  var5 = var5.substring(var7.alen, var5.length());
                  break;
               } catch (RuntimeException var11) {
                  MudgeSanity.logException("substr('" + var5.replaceAll("\\P{Print}", ".") + "', " + var7.alen + ", " + var5.length() + ")", var11, false);
                  return "";
               }
            case 3:
               try {
                  var5 = toBinaryString(Base64.decode(var5));
                  break;
               } catch (RuntimeException var10) {
                  MudgeSanity.logException("base64 decode: " + var5, var10, true);
                  return "";
               }
            case 4:
               var5 = var3;
               break;
            case 5:
               var5 = DialogUtils.string(var2, var7.argument);
               break;
            case 6:
               var5 = CommonUtils.getCaseInsensitive(var1, var7.argument, "");
            case 7:
            case 9:
            case 10:
            case 16:
               break;
            case 8:
               var5 = toBinaryString(NetBIOS.decode('a', var5));
               break;
            case 11:
               var5 = toBinaryString(NetBIOS.decode('A', var5));
               break;
            case 12:
               var5 = var4;
               break;
            case 13:
               try {
                  var5 = toBinaryString(Base64Url.decode(var5));
                  break;
               } catch (RuntimeException var9) {
                  MudgeSanity.logException("base64url decode: " + var5, var9, true);
                  return "";
               }
            case 14:
            default:
               System.err.println("Unknown: " + var7);
               break;
            case 15:
               var5 = toBinaryString(MaskEncoder.decode(CommonUtils.toBytes(var5)));
         }
      }

      return var5;
   }

   public byte[] transformData(byte[] var1) {
      if (this.tsteps.size() == 0) {
         return var1;
      } else {
         SmartBuffer var2 = new SmartBuffer();
         var2.append(var1);
         Iterator var3 = this.tsteps.iterator();

         while(var3.hasNext()) {
            Statement var4 = (Statement)var3.next();
            switch (var4.action) {
               case 1:
                  var2.append(toBytes(var4.argument));
                  break;
               case 2:
                  var2.prepend(toBytes(var4.argument));
                  break;
               case 14:
                  String var5 = var4.argument.substring(0, var4.argument.length() / 2);
                  String var6 = var4.argument.substring(var4.argument.length() / 2);
                  var2.strrep(var5, var6);
                  break;
               default:
                  System.err.println("Unknown: " + var4);
            }
         }

         return var2.getBytes();
      }
   }

   public byte[] getPrependedData() {
      if (this.tsteps.size() == 0) {
         return new byte[0];
      } else {
         SmartBuffer var1 = new SmartBuffer();
         Iterator var2 = this.tsteps.iterator();

         while(var2.hasNext()) {
            Statement var3 = (Statement)var2.next();
            switch (var3.action) {
               case 2:
                  var1.prepend(toBytes(var3.argument));
            }
         }

         return var1.getBytes();
      }
   }

   public byte[] getAppendedData() {
      if (this.tsteps.size() == 0) {
         return new byte[0];
      } else {
         SmartBuffer var1 = new SmartBuffer();
         Iterator var2 = this.tsteps.iterator();

         while(var2.hasNext()) {
            Statement var3 = (Statement)var2.next();
            switch (var3.action) {
               case 1:
                  var1.append(toBytes(var3.argument));
            }
         }

         return var1.getBytes();
      }
   }

   public static final class Statement implements Serializable {
      public String argument = "";
      public int action = 0;
      public int alen = 0;

      public String toString() {
         return "(" + this.action + ":" + this.argument + ")";
      }
   }
}
