package org.apache.regexp;

import java.io.PrintWriter;
import java.util.Hashtable;

public class REDebugCompiler extends RECompiler {
   static Hashtable hashOpcode = new Hashtable();

   static {
      hashOpcode.put(new Integer(56), "OP_RELUCTANTSTAR");
      hashOpcode.put(new Integer(61), "OP_RELUCTANTPLUS");
      hashOpcode.put(new Integer(47), "OP_RELUCTANTMAYBE");
      hashOpcode.put(new Integer(69), "OP_END");
      hashOpcode.put(new Integer(94), "OP_BOL");
      hashOpcode.put(new Integer(36), "OP_EOL");
      hashOpcode.put(new Integer(46), "OP_ANY");
      hashOpcode.put(new Integer(91), "OP_ANYOF");
      hashOpcode.put(new Integer(124), "OP_BRANCH");
      hashOpcode.put(new Integer(65), "OP_ATOM");
      hashOpcode.put(new Integer(42), "OP_STAR");
      hashOpcode.put(new Integer(43), "OP_PLUS");
      hashOpcode.put(new Integer(63), "OP_MAYBE");
      hashOpcode.put(new Integer(78), "OP_NOTHING");
      hashOpcode.put(new Integer(71), "OP_GOTO");
      hashOpcode.put(new Integer(92), "OP_ESCAPE");
      hashOpcode.put(new Integer(40), "OP_OPEN");
      hashOpcode.put(new Integer(41), "OP_CLOSE");
      hashOpcode.put(new Integer(35), "OP_BACKREF");
      hashOpcode.put(new Integer(80), "OP_POSIXCLASS");
   }

   String charToString(char var1) {
      return var1 >= ' ' && var1 <= 127 ? String.valueOf(var1) : "\\" + var1;
   }

   public void dumpProgram(PrintWriter var1) {
      for(int var2 = 0; var2 < super.lenInstruction; var1.println("")) {
         char var3 = super.instruction[var2];
         char var4 = super.instruction[var2 + 1];
         short var5 = (short)super.instruction[var2 + 2];
         var1.print(var2 + ". " + this.nodeToString(var2) + ", next = ");
         if (var5 == 0) {
            var1.print("none");
         } else {
            var1.print(var2 + var5);
         }

         var2 += 3;
         int var6;
         if (var3 == '[') {
            var1.print(", [");
            var6 = var4;

            for(int var7 = 0; var7 < var6; ++var7) {
               char var8 = super.instruction[var2++];
               char var9 = super.instruction[var2++];
               if (var8 == var9) {
                  var1.print(this.charToString(var8));
               } else {
                  var1.print(this.charToString(var8) + "-" + this.charToString(var9));
               }
            }

            var1.print("]");
         }

         if (var3 == 'A') {
            var1.print(", \"");
            var6 = var4;

            while(var6-- != 0) {
               var1.print(this.charToString(super.instruction[var2++]));
            }

            var1.print("\"");
         }
      }

   }

   String nodeToString(int var1) {
      char var2 = super.instruction[var1];
      char var3 = super.instruction[var1 + 1];
      return this.opcodeToString(var2) + ", opdata = " + var3;
   }

   String opcodeToString(char var1) {
      String var2 = (String)hashOpcode.get(new Integer(var1));
      if (var2 == null) {
         var2 = "OP_????";
      }

      return var2;
   }
}
