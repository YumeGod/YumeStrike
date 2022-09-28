package org.apache.fop.tools.anttasks;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class RunTest extends Task {
   private String basedir;
   private String testsuite = "";
   private String referenceJar = "";
   private String refVersion = "";

   public void setTestSuite(String str) {
      this.testsuite = str;
   }

   public void setBasedir(String str) {
      this.basedir = str;
   }

   public void setReference(String str) {
      this.referenceJar = str;
   }

   public void setRefVersion(String str) {
      this.refVersion = str;
   }

   public void execute() throws BuildException {
      this.runReference();
      this.testNewBuild();
   }

   protected void testNewBuild() {
      try {
         ClassLoader loader = new URLClassLoader(this.createUrls("build/fop.jar"));
         Map diff = this.runConverter(loader, "areatree", "reference/output/");
         if (diff != null && !diff.isEmpty()) {
            System.out.println("====================================");
            System.out.println("The following files differ:");
            boolean broke = false;
            Iterator keys = diff.keySet().iterator();

            while(keys.hasNext()) {
               Object fname = keys.next();
               Boolean pass = (Boolean)diff.get(fname);
               System.out.println("file: " + fname + " - reference success: " + pass);
               if (pass) {
                  broke = true;
               }
            }

            if (broke) {
               throw new BuildException("Working tests have been changed.");
            }
         }
      } catch (MalformedURLException var7) {
         var7.printStackTrace();
      }

   }

   protected void runReference() throws BuildException {
      File f = new File(this.basedir + "/reference/output/");

      try {
         ClassLoader loader = new URLClassLoader(this.createUrls(this.referenceJar));
         boolean failed = false;

         try {
            Class cla = Class.forName("org.apache.fop.apps.Fop", true, loader);
            Method get = cla.getMethod("getVersion");
            if (!get.invoke((Object)null).equals(this.refVersion)) {
               throw new BuildException("Reference jar is not correct version it must be: " + this.refVersion);
            }
         } catch (IllegalAccessException var6) {
            failed = true;
         } catch (IllegalArgumentException var7) {
            failed = true;
         } catch (InvocationTargetException var8) {
            failed = true;
         } catch (ClassNotFoundException var9) {
            failed = true;
         } catch (NoSuchMethodException var10) {
            failed = true;
         }

         if (failed) {
            throw new BuildException("Reference jar could not be found in: " + this.basedir + "/reference/");
         }

         f.mkdirs();
         this.runConverter(loader, "reference/output/", (String)null);
      } catch (MalformedURLException var11) {
         var11.printStackTrace();
      }

   }

   protected Map runConverter(ClassLoader loader, String dest, String compDir) {
      String converter = "org.apache.fop.tools.TestConverter";
      Map diff = null;

      try {
         Class cla = Class.forName(converter, true, loader);
         Object tc = cla.newInstance();
         Method meth = cla.getMethod("setBaseDir", String.class);
         meth.invoke(tc, this.basedir);
         meth = cla.getMethod("runTests", String.class, String.class, String.class);
         diff = (Map)meth.invoke(tc, this.testsuite, dest, compDir);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return diff;
   }

   private URL[] createUrls(String mainJar) throws MalformedURLException {
      ArrayList urls = new ArrayList();
      urls.add((new File(mainJar)).toURI().toURL());
      File[] libFiles = (new File("lib")).listFiles();

      for(int i = 0; i < libFiles.length; ++i) {
         if (libFiles[i].getPath().endsWith(".jar")) {
            urls.add(libFiles[i].toURI().toURL());
         }
      }

      return (URL[])urls.toArray(new URL[urls.size()]);
   }
}
