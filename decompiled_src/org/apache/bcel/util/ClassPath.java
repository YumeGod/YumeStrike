package org.apache.bcel.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassPath {
   private PathEntry[] paths;

   public ClassPath(String class_path) {
      ArrayList vec = new ArrayList();
      StringTokenizer tok = new StringTokenizer(class_path, System.getProperty("path.separator"));

      while(tok.hasMoreTokens()) {
         String path = tok.nextToken();
         if (!path.equals("")) {
            File file = new File(path);

            try {
               if (file.exists()) {
                  if (file.isDirectory()) {
                     vec.add(new Dir(path));
                  } else {
                     vec.add(new Zip(new ZipFile(file)));
                  }
               }
            } catch (IOException var7) {
               System.err.println("CLASSPATH component " + file + ": " + var7);
            }
         }
      }

      this.paths = new PathEntry[vec.size()];
      vec.toArray(this.paths);
   }

   public ClassPath() {
      this(getClassPath());
   }

   private static final void getPathComponents(String path, ArrayList list) {
      if (path != null) {
         StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);

         while(tok.hasMoreTokens()) {
            String name = tok.nextToken();
            File file = new File(name);
            if (file.exists()) {
               list.add(name);
            }
         }
      }

   }

   public static final String getClassPath() {
      String class_path = System.getProperty("java.class.path");
      String boot_path = System.getProperty("sun.boot.class.path");
      String ext_path = System.getProperty("java.ext.dirs");
      ArrayList list = new ArrayList();
      getPathComponents(class_path, list);
      getPathComponents(boot_path, list);
      ArrayList dirs = new ArrayList();
      getPathComponents(ext_path, dirs);
      Iterator e = dirs.iterator();

      while(true) {
         String[] extensions;
         do {
            if (!e.hasNext()) {
               StringBuffer buf = new StringBuffer();
               Iterator e = list.iterator();

               while(e.hasNext()) {
                  buf.append((String)e.next());
                  if (e.hasNext()) {
                     buf.append(File.pathSeparatorChar);
                  }
               }

               return buf.toString();
            }

            File ext_dir = new File((String)e.next());
            extensions = ext_dir.list(new FilenameFilter() {
               public boolean accept(File dir, String name) {
                  name = name.toLowerCase();
                  return name.endsWith(".zip") || name.endsWith(".jar");
               }
            });
         } while(extensions == null);

         for(int i = 0; i < extensions.length; ++i) {
            list.add(ext_path + File.separatorChar + extensions[i]);
         }
      }
   }

   public InputStream getInputStream(String name) throws IOException {
      return this.getInputStream(name, ".class");
   }

   public InputStream getInputStream(String name, String suffix) throws IOException {
      InputStream is = null;

      try {
         is = this.getClass().getClassLoader().getResourceAsStream(name + suffix);
      } catch (Exception var5) {
      }

      return is != null ? is : this.getClassFile(name, suffix).getInputStream();
   }

   public ClassFile getClassFile(String name, String suffix) throws IOException {
      for(int i = 0; i < this.paths.length; ++i) {
         ClassFile cf;
         if ((cf = this.paths[i].getClassFile(name, suffix)) != null) {
            return cf;
         }
      }

      throw new IOException("Couldn't find: " + name + suffix);
   }

   public ClassFile getClassFile(String name) throws IOException {
      return this.getClassFile(name, ".class");
   }

   public byte[] getBytes(String name, String suffix) throws IOException {
      InputStream is = this.getInputStream(name, suffix);
      if (is == null) {
         throw new IOException("Couldn't find: " + name + suffix);
      } else {
         DataInputStream dis = new DataInputStream(is);
         byte[] bytes = new byte[is.available()];
         dis.readFully(bytes);
         dis.close();
         is.close();
         return bytes;
      }
   }

   public byte[] getBytes(String name) throws IOException {
      return this.getBytes(name, ".class");
   }

   public String getPath(String name) throws IOException {
      int index = name.lastIndexOf(46);
      String suffix = "";
      if (index > 0) {
         suffix = name.substring(index);
         name = name.substring(0, index);
      }

      return this.getPath(name, suffix);
   }

   public String getPath(String name, String suffix) throws IOException {
      return this.getClassFile(name, suffix).getPath();
   }

   private static class Zip extends PathEntry {
      private ZipFile zip;

      Zip(ZipFile z) {
         super(null);
         this.zip = z;
      }

      ClassFile getClassFile(String name, String suffix) throws IOException {
         final ZipEntry entry = this.zip.getEntry(name.replace('.', '/') + suffix);
         return entry != null ? new ClassFile() {
            public InputStream getInputStream() throws IOException {
               return Zip.this.zip.getInputStream(entry);
            }

            public String getPath() {
               return entry.toString();
            }

            public long getTime() {
               return entry.getTime();
            }

            public long getSize() {
               return entry.getSize();
            }
         } : null;
      }
   }

   private static class Dir extends PathEntry {
      private String dir;

      Dir(String d) {
         super(null);
         this.dir = d;
      }

      ClassFile getClassFile(String name, String suffix) throws IOException {
         final File file = new File(this.dir + File.separatorChar + name.replace('.', File.separatorChar) + suffix);
         return file.exists() ? new ClassFile() {
            public InputStream getInputStream() throws IOException {
               return new FileInputStream(file);
            }

            public String getPath() {
               try {
                  return file.getCanonicalPath();
               } catch (IOException var2) {
                  return null;
               }
            }

            public long getTime() {
               return file.lastModified();
            }

            public long getSize() {
               return file.length();
            }
         } : null;
      }

      public String toString() {
         return this.dir;
      }
   }

   public abstract static class ClassFile {
      public abstract InputStream getInputStream() throws IOException;

      public abstract String getPath();

      public abstract long getTime();

      public abstract long getSize();
   }

   private abstract static class PathEntry {
      private PathEntry() {
      }

      abstract ClassFile getClassFile(String var1, String var2) throws IOException;

      // $FF: synthetic method
      PathEntry(Object x0) {
         this();
      }
   }
}
