package org.apache.xalan.xsltc.cmdline;

import java.io.File;
import java.net.URL;
import java.util.Vector;
import org.apache.xalan.xsltc.cmdline.getopt.GetOpt;
import org.apache.xalan.xsltc.cmdline.getopt.GetOptsException;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

public final class Compile {
   private static int VERSION_MAJOR = 1;
   private static int VERSION_MINOR = 4;
   private static int VERSION_DELTA = 0;

   public static void printUsage() {
      StringBuffer vers = new StringBuffer("XSLTC version " + VERSION_MAJOR + "." + VERSION_MINOR + (VERSION_DELTA > 0 ? "." + VERSION_DELTA : ""));
      System.err.println(vers + "\n" + new ErrorMsg("COMPILE_USAGE_STR"));
   }

   public static void main(String[] args) {
      try {
         boolean inputIsURL = false;
         boolean useStdIn = false;
         boolean classNameSet = false;
         GetOpt getopt = new GetOpt(args, "o:d:j:p:uxhsinv");
         if (args.length < 1) {
            printUsage();
         }

         XSLTC xsltc = new XSLTC();
         xsltc.init();

         int c;
         while((c = getopt.getNextOption()) != -1) {
            switch (c) {
               case 100:
                  xsltc.setDestDirectory(getopt.getOptionArg());
                  break;
               case 101:
               case 102:
               case 103:
               case 104:
               case 107:
               case 108:
               case 109:
               case 113:
               case 114:
               case 115:
               case 116:
               case 118:
               case 119:
               default:
                  printUsage();
                  break;
               case 105:
                  useStdIn = true;
                  break;
               case 106:
                  xsltc.setJarFileName(getopt.getOptionArg());
                  break;
               case 110:
                  xsltc.setTemplateInlining(true);
                  break;
               case 111:
                  xsltc.setClassName(getopt.getOptionArg());
                  classNameSet = true;
                  break;
               case 112:
                  xsltc.setPackageName(getopt.getOptionArg());
                  break;
               case 117:
                  inputIsURL = true;
                  break;
               case 120:
                  xsltc.setDebug(true);
            }
         }

         boolean compileOK;
         if (useStdIn) {
            if (!classNameSet) {
               System.err.println(new ErrorMsg("COMPILE_STDIN_ERR"));
            }

            compileOK = xsltc.compile(System.in, xsltc.getClassName());
         } else {
            String[] stylesheetNames = getopt.getCmdArgs();
            Vector stylesheetVector = new Vector();

            for(int i = 0; i < stylesheetNames.length; ++i) {
               String name = stylesheetNames[i];
               URL url;
               if (inputIsURL) {
                  url = new URL(name);
               } else {
                  url = (new File(name)).toURL();
               }

               stylesheetVector.addElement(url);
            }

            compileOK = xsltc.compile(stylesheetVector);
         }

         if (compileOK) {
            xsltc.printWarnings();
            if (xsltc.getJarFileName() != null) {
               xsltc.outputToJar();
            }
         } else {
            xsltc.printWarnings();
            xsltc.printErrors();
         }
      } catch (GetOptsException var13) {
         System.err.println(var13);
         printUsage();
      } catch (Exception var14) {
         var14.printStackTrace();
      }

   }
}
