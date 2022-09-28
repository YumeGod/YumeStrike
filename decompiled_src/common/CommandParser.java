package common;

import java.io.File;
import java.util.HashSet;
import java.util.Stack;

public class CommandParser {
   protected StringStack parse;
   protected String command;
   protected Stack args = new Stack();
   protected String error = null;
   protected String text;
   protected boolean missing = false;

   public CommandParser(String var1) {
      this.text = var1;
      this.reset();
   }

   public boolean is(String var1) {
      return this.command.equals(var1);
   }

   public String getCommand() {
      return this.command;
   }

   public String getArguments() {
      return this.parse.toString();
   }

   public String error() {
      return this.command + " error: " + this.error;
   }

   public boolean isMissingArguments() {
      boolean var1 = this.missing;
      if (this.missing) {
         this.reset();
      }

      return var1;
   }

   public void error(String var1) {
      this.error = var1;
   }

   public boolean empty() {
      return this.parse.isEmpty();
   }

   public boolean hasError() {
      return this.error != null;
   }

   public boolean reset() {
      this.parse = new StringStack(this.text);
      this.command = this.parse.shift();
      this.error = null;
      this.args = new Stack();
      this.missing = false;
      return false;
   }

   public boolean verify(String var1) {
      char[] var2 = var1.toCharArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (this.parse.isEmpty()) {
            this.error = "not enough arguments";
            this.missing = true;
            return false;
         }

         if (var2[var3] == 'A') {
            this.args.push(this.parse.shift());
         } else {
            String var4;
            if (var2[var3] == 'C') {
               var4 = this.parse.shift();
               if (!var4.equals("dns") && !var4.equals("dns-txt") && !var4.equals("dns6")) {
                  this.error = "argument '" + var4 + "' is not 'dns', 'dns6', or 'dns-txt'";
                  return false;
               }

               this.args.push(var4);
            } else if (var2[var3] == 'D') {
               var4 = this.parse.shift();
               if (!var4.equals("icmp") && !var4.equals("arp") && !var4.equals("none")) {
                  this.error = "argument '" + var4 + "' is not 'arp', 'icmp', or 'none'";
                  return false;
               }

               this.args.push(var4);
            } else if (var2[var3] == 'g') {
               var4 = this.parse.shift();
               if (!var4.equals("query") && !var4.equals("queryv")) {
                  this.error = "argument '" + var4 + "' is not 'query or queryv'";
                  return false;
               }

               this.args.push(var4);
            } else if (var2[var3] == 'H') {
               var4 = this.parse.shift();
               if (var4.length() == 65 && var4.charAt(32) == ':') {
                  var4 = var4.substring(33);
               }

               if (var4.length() != 32) {
                  this.error = "argument '" + var4 + "' is not an NTLM hash";
                  return false;
               }

               this.args.push(var4);
            } else {
               int var5;
               if (var2[var3] == 'I') {
                  var4 = this.parse.shift();

                  try {
                     var5 = Integer.parseInt(var4);
                     this.args.push(new Integer(var5));
                  } catch (Exception var6) {
                     this.error = "'" + var4 + "' is not a number";
                     return false;
                  }
               } else {
                  File var11;
                  if (var2[var3] == 'f') {
                     var11 = new File(this.parse.toString());
                     if (!var11.exists()) {
                        this.error = "'" + var11.getAbsolutePath() + "' does not exist";
                        return false;
                     }

                     if (!var11.canRead()) {
                        this.error = "'" + var11.getAbsolutePath() + "' is not readable";
                        return false;
                     }

                     if (var11.isDirectory()) {
                        this.error = "'" + var11.getAbsolutePath() + "' is a directory";
                        return false;
                     }

                     this.args.push(var11.getAbsolutePath());
                  } else if (var2[var3] == 'F') {
                     var11 = new File(this.parse.toString());
                     if (!var11.exists()) {
                        this.error = "'" + var11.getAbsolutePath() + "' does not exist";
                        return false;
                     }

                     if (!var11.canRead()) {
                        this.error = "'" + var11.getAbsolutePath() + "' is not readable";
                        return false;
                     }

                     if (var11.isDirectory()) {
                        this.error = "'" + var11.getAbsolutePath() + "' is a directory";
                        return false;
                     }

                     if (var11.length() > 1048576L) {
                        this.error = "max upload size is 1MB";
                        return false;
                     }

                     this.args.push(var11.getAbsolutePath());
                  } else if (var2[var3] == 'L') {
                     var4 = this.parse.toString();
                     if (!ListenerUtils.isListener(var4)) {
                        this.error = "Listener '" + var4 + "' does not exist";
                        return false;
                     }

                     this.args.push(var4);
                  } else if (var2[var3] == 'p') {
                     var11 = new File(this.parse.shift());
                     if (!var11.exists()) {
                        this.error = "'" + var11.getAbsolutePath() + "' does not exist";
                        return false;
                     }

                     if (!var11.canRead()) {
                        this.error = "'" + var11.getAbsolutePath() + "' is not readable";
                        return false;
                     }

                     if (var11.isDirectory()) {
                        this.error = "'" + var11.getAbsolutePath() + "' is a directory";
                        return false;
                     }

                     this.args.push(var11.getAbsolutePath());
                  } else if (var2[var3] == 'Q') {
                     var4 = this.parse.shift();
                     if (!var4.equals("high") && !var4.equals("low")) {
                        this.error = "argument '" + var4 + "' is not 'high' or 'low'";
                        return false;
                     }

                     this.args.push(var4);
                  } else if (var2[var3] == 'R') {
                     var4 = this.parse.shift();
                     PortFlipper var10 = new PortFlipper(var4);
                     var10.parse();
                     if (var10.hasError()) {
                        this.error = var10.getError();
                        return false;
                     }

                     this.args.push(var4);
                  } else if (var2[var3] == 'T') {
                     var4 = this.parse.shift();
                     AddressList var9 = new AddressList(var4);
                     if (var9.hasError()) {
                        this.error = var9.getError();
                        return false;
                     }

                     this.args.push(var4);
                  } else if (var2[var3] == 'U') {
                     var4 = this.parse.shift();
                     if (!var4.startsWith("\\\\")) {
                        this.error = "argument '" + var4 + "' is not a \\\\target";
                        return false;
                     }

                     this.args.push(var4.substring(2));
                  } else if (var2[var3] == 'V') {
                     var4 = this.parse.shift();
                     HashSet var8 = new HashSet(CommonUtils.getNetCommands());
                     if (!var8.contains(var4)) {
                        this.error = "argument '" + var4 + "' is not a net command";
                        return false;
                     }

                     this.args.push(var4);
                  } else if (var2[var3] == 'X') {
                     var4 = this.parse.shift();
                     if (!var4.equals("x86") && !var4.equals("x64")) {
                        this.error = "argument '" + var4 + "' is not 'x86' or 'x64'";
                        return false;
                     }

                     this.args.push(var4);
                  } else if (var2[var3] == 'Z') {
                     this.args.push(this.parse.toString());
                  } else if (var2[var3] == '%') {
                     var4 = this.parse.shift();

                     try {
                        var5 = Integer.parseInt(var4);
                        if (var5 < 0 || var5 > 99) {
                           this.error = "argument " + var5 + " is not a value 0-99";
                           return false;
                        }

                        this.args.push(new Integer(var5));
                     } catch (Exception var7) {
                        this.error = "'" + var4 + "' is not a number";
                        return false;
                     }
                  } else if (var2[var3] == '?') {
                     var4 = this.parse.shift();
                     if (!var4.equals("start") && !var4.equals("on") && !var4.equals("true")) {
                        if (!var4.equals("stop") && !var4.equals("off") && !var4.equals("false")) {
                           this.error = "'" + var4 + "' is not a boolean value";
                           return false;
                        }

                        this.args.push(Boolean.FALSE);
                     } else {
                        this.args.push(Boolean.TRUE);
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   public int popInt() {
      Integer var1 = (Integer)this.args.pop();
      return var1;
   }

   public String popString() {
      return this.args.pop() + "";
   }

   public boolean popBoolean() {
      Boolean var1 = (Boolean)this.args.pop();
      return var1;
   }
}
