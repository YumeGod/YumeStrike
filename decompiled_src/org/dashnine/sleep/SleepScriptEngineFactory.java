package org.dashnine.sleep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class SleepScriptEngineFactory implements ScriptEngineFactory {
   private static List names = new ArrayList(3);
   private static List extensions;
   private static List mimeTypes;

   public String getEngineName() {
      return "sleep";
   }

   public String getEngineVersion() {
      return "20080530";
   }

   public List getExtensions() {
      return extensions;
   }

   public String getLanguageName() {
      return "Sleep";
   }

   public String getLanguageVersion() {
      return "2.1";
   }

   public String getMethodCallSyntax(String var1, String var2, String... var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append('[');
      var4.append(var1);
      var4.append(' ');
      var4.append(var2);
      var4.append(':');
      if (var3.length != 0) {
         int var5;
         for(var5 = 0; var5 < var3.length - 1; ++var5) {
            var4.append(var3[var5] + ", ");
         }

         var4.append(var3[var5]);
      }

      var4.append(")");
      var4.append(']');
      return var4.toString();
   }

   public List getMimeTypes() {
      return mimeTypes;
   }

   public List getNames() {
      return names;
   }

   public String getOutputStatement(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("print('");
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var1.charAt(var4);
         switch (var5) {
            case '\'':
               var2.append("'");
               break;
            default:
               var2.append(var5);
         }
      }

      var2.append("')");
      return var2.toString();
   }

   public String getParameter(String var1) {
      if (var1.equals("javax.script.engine")) {
         return this.getEngineName();
      } else if (var1.equals("javax.script.engine_version")) {
         return this.getEngineVersion();
      } else if (var1.equals("javax.script.name")) {
         return this.getEngineName();
      } else if (var1.equals("javax.script.language")) {
         return this.getLanguageName();
      } else if (var1.equals("javax.script.language_version")) {
         return this.getLanguageVersion();
      } else {
         return var1.equals("THREADING") ? "MULTITHREADED" : null;
      }
   }

   public String getProgram(String... var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append(var1[var3]);
         var2.append(";\n");
      }

      return var2.toString();
   }

   public ScriptEngine getScriptEngine() {
      SleepScriptEngine var1 = new SleepScriptEngine();
      var1.setFactory(this);
      return var1;
   }

   static {
      names.add("sleep");
      names.add("Sleep");
      names.add("sl");
      names = Collections.unmodifiableList(names);
      extensions = new ArrayList(1);
      extensions.add("sl");
      extensions = Collections.unmodifiableList(extensions);
      mimeTypes = new ArrayList(0);
      mimeTypes = Collections.unmodifiableList(mimeTypes);
   }
}
