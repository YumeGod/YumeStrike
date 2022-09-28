package org.apache.fop.cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.FOUserAgent;

public class Main {
   public static URL[] getJARList() throws MalformedURLException {
      String fopHome = System.getProperty("fop.home");
      File baseDir;
      if (fopHome != null) {
         baseDir = (new File(fopHome)).getAbsoluteFile();
      } else {
         baseDir = (new File(".")).getAbsoluteFile().getParentFile();
      }

      File buildDir;
      if ("build".equals(baseDir.getName())) {
         buildDir = baseDir;
         baseDir = baseDir.getParentFile();
      } else {
         buildDir = new File(baseDir, "build");
      }

      File fopJar = new File(buildDir, "fop.jar");
      if (!fopJar.exists()) {
         fopJar = new File(baseDir, "fop.jar");
      }

      if (!fopJar.exists()) {
         throw new RuntimeException("fop.jar not found in directory: " + baseDir.getAbsolutePath() + " (or below)");
      } else {
         List jars = new ArrayList();
         jars.add(fopJar.toURI().toURL());
         FileFilter filter = new FileFilter() {
            public boolean accept(File pathname) {
               return pathname.getName().endsWith(".jar");
            }
         };
         File libDir = new File(baseDir, "lib");
         if (!libDir.exists()) {
            libDir = baseDir;
         }

         File[] files = libDir.listFiles(filter);
         int i;
         if (files != null) {
            int i = 0;

            for(i = files.length; i < i; ++i) {
               jars.add(files[i].toURI().toURL());
            }
         }

         String optionalLib = System.getProperty("fop.optional.lib");
         if (optionalLib != null) {
            files = (new File(optionalLib)).listFiles(filter);
            if (files != null) {
               i = 0;

               for(int size = files.length; i < size; ++i) {
                  jars.add(files[i].toURI().toURL());
               }
            }
         }

         URL[] urls = (URL[])jars.toArray(new URL[jars.size()]);
         return urls;
      }
   }

   public static boolean checkDependencies() {
      try {
         Class clazz = Class.forName("org.apache.commons.io.IOUtils");
         if (clazz != null) {
            clazz = Class.forName("org.apache.avalon.framework.configuration.Configuration");
         }

         return clazz != null;
      } catch (Exception var1) {
         return false;
      }
   }

   public static void startFOPWithDynamicClasspath(String[] args) {
      try {
         URL[] urls = getJARList();
         ClassLoader loader = new URLClassLoader(urls, (ClassLoader)null);
         Thread.currentThread().setContextClassLoader(loader);
         Class clazz = Class.forName("org.apache.fop.cli.Main", true, loader);
         Method mainMethod = clazz.getMethod("startFOP", String[].class);
         mainMethod.invoke((Object)null, args);
      } catch (Exception var5) {
         System.err.println("Unable to start FOP:");
         var5.printStackTrace();
         System.exit(-1);
      }

   }

   public static void startFOP(String[] args) {
      CommandLineOptions options = null;
      FOUserAgent foUserAgent = null;
      OutputStream out = null;

      try {
         options = new CommandLineOptions();
         if (!options.parse(args)) {
            System.exit(0);
         }

         foUserAgent = options.getFOUserAgent();
         String outputFormat = options.getOutputFormat();

         try {
            if (options.getOutputFile() != null) {
               out = new BufferedOutputStream(new FileOutputStream(options.getOutputFile()));
               foUserAgent.setOutputFile(options.getOutputFile());
            } else if (options.isOutputToStdOut()) {
               out = new BufferedOutputStream(System.out);
            }

            if (!"text/xsl".equals(outputFormat)) {
               options.getInputHandler().renderTo(foUserAgent, outputFormat, out);
            } else {
               options.getInputHandler().transformTo((OutputStream)out);
            }
         } finally {
            IOUtils.closeQuietly((OutputStream)out);
         }

         if (!"application/X-fop-awt-preview".equals(outputFormat)) {
            System.exit(0);
         }
      } catch (Exception var9) {
         if (options != null) {
            options.getLogger().error("Exception", var9);
            if (options.getOutputFile() != null) {
               options.getOutputFile().delete();
            }
         }

         System.exit(1);
      }

   }

   public static void main(String[] args) {
      if (checkDependencies()) {
         startFOP(args);
      } else {
         startFOPWithDynamicClasspath(args);
      }

   }
}
