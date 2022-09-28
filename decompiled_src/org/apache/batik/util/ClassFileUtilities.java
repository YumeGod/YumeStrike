package org.apache.batik.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ClassFileUtilities {
   public static final byte CONSTANT_UTF8_INFO = 1;
   public static final byte CONSTANT_INTEGER_INFO = 3;
   public static final byte CONSTANT_FLOAT_INFO = 4;
   public static final byte CONSTANT_LONG_INFO = 5;
   public static final byte CONSTANT_DOUBLE_INFO = 6;
   public static final byte CONSTANT_CLASS_INFO = 7;
   public static final byte CONSTANT_STRING_INFO = 8;
   public static final byte CONSTANT_FIELDREF_INFO = 9;
   public static final byte CONSTANT_METHODREF_INFO = 10;
   public static final byte CONSTANT_INTERFACEMETHODREF_INFO = 11;
   public static final byte CONSTANT_NAMEANDTYPE_INFO = 12;

   protected ClassFileUtilities() {
   }

   public static void main(String[] var0) {
      boolean var1 = false;
      if (var0.length == 1 && var0[0].equals("-f")) {
         var1 = true;
      } else if (var0.length != 0) {
         System.err.println("usage: org.apache.batik.util.ClassFileUtilities [-f]");
         System.err.println();
         System.err.println("  -f    list files that cause each jar file dependency");
         System.exit(1);
      }

      File var2 = new File(".");
      File var3 = null;
      String[] var4 = var2.list();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].startsWith("batik-")) {
            var3 = new File(var4[var5]);
            if (var3.isDirectory()) {
               break;
            }

            var3 = null;
         }
      }

      if (var3 != null && var3.isDirectory()) {
         try {
            HashMap var16 = new HashMap();
            HashMap var6 = new HashMap();
            collectJars(var3, var6, var16);
            HashSet var7 = new HashSet();
            Iterator var8 = var6.values().iterator();

            while(var8.hasNext()) {
               var7.add(((Jar)var8.next()).jarFile);
            }

            var8 = var16.values().iterator();

            ClassFile var9;
            Iterator var11;
            ClassFile var12;
            while(var8.hasNext()) {
               var9 = (ClassFile)var8.next();
               Set var10 = getClassDependencies((InputStream)var9.getInputStream(), var7, false);
               var11 = var10.iterator();

               while(var11.hasNext()) {
                  var12 = (ClassFile)var16.get(var11.next());
                  if (var9 != var12 && var12 != null) {
                     var9.deps.add(var12);
                  }
               }
            }

            var8 = var16.values().iterator();

            Jar var22;
            while(var8.hasNext()) {
               var9 = (ClassFile)var8.next();
               Iterator var18 = var9.deps.iterator();

               while(var18.hasNext()) {
                  ClassFile var21 = (ClassFile)var18.next();
                  var22 = var9.jar;
                  Jar var13 = var21.jar;
                  if (!var9.name.equals(var21.name) && var13 != var22 && !var22.files.contains(var21.name)) {
                     Integer var14 = (Integer)var22.deps.get(var13);
                     if (var14 == null) {
                        var22.deps.put(var13, new Integer(1));
                     } else {
                        var22.deps.put(var13, new Integer(var14 + 1));
                     }
                  }
               }
            }

            ArrayList var17 = new ArrayList(10);
            var8 = var6.values().iterator();

            while(var8.hasNext()) {
               Jar var19 = (Jar)var8.next();
               var11 = var19.deps.keySet().iterator();

               while(var11.hasNext()) {
                  var22 = (Jar)var11.next();
                  Triple var23 = new Triple();
                  var23.from = var19;
                  var23.to = var22;
                  var23.count = (Integer)var19.deps.get(var22);
                  var17.add(var23);
               }
            }

            Collections.sort(var17);
            var8 = var17.iterator();

            while(true) {
               Triple var20;
               do {
                  if (!var8.hasNext()) {
                     return;
                  }

                  var20 = (Triple)var8.next();
                  System.out.println(var20.count + "," + var20.from.name + "," + var20.to.name);
               } while(!var1);

               var11 = var20.from.files.iterator();

               while(var11.hasNext()) {
                  var12 = (ClassFile)var11.next();
                  Iterator var24 = var12.deps.iterator();

                  while(var24.hasNext()) {
                     ClassFile var25 = (ClassFile)var24.next();
                     if (var25.jar == var20.to && !var20.from.files.contains(var25.name)) {
                        System.out.println("\t" + var12.name + " --> " + var25.name);
                     }
                  }
               }
            }
         } catch (IOException var15) {
            var15.printStackTrace();
         }
      } else {
         System.out.println("Directory 'batik-xxx' not found in current directory!");
      }
   }

   private static void collectJars(File var0, Map var1, Map var2) throws IOException {
      File[] var3 = var0.listFiles();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4].getName();
         if (var5.endsWith(".jar") && var3[var4].isFile()) {
            Jar var6 = new Jar();
            var6.name = var3[var4].getPath();
            var6.file = var3[var4];
            var6.jarFile = new JarFile(var3[var4]);
            var1.put(var6.name, var6);
            Enumeration var7 = var6.jarFile.entries();

            while(var7.hasMoreElements()) {
               ZipEntry var8 = (ZipEntry)var7.nextElement();
               String var9 = var8.getName();
               if (var9.endsWith(".class")) {
                  ClassFile var10 = new ClassFile();
                  var10.name = var9;
                  var10.jar = var6;
                  var2.put(var6.name + '!' + var10.name, var10);
                  var6.files.add(var10);
               }
            }
         } else if (var3[var4].isDirectory()) {
            collectJars(var3[var4], var1, var2);
         }
      }

   }

   public static Set getClassDependencies(String var0, Set var1, boolean var2) throws IOException {
      return getClassDependencies((InputStream)(new FileInputStream(var0)), var1, var2);
   }

   public static Set getClassDependencies(InputStream var0, Set var1, boolean var2) throws IOException {
      HashSet var3 = new HashSet();
      HashSet var4 = new HashSet();
      computeClassDependencies(var0, var1, var4, var3, var2);
      return var3;
   }

   private static void computeClassDependencies(InputStream var0, Set var1, Set var2, Set var3, boolean var4) throws IOException {
      Iterator var5 = getClassDependencies(var0).iterator();

      while(true) {
         String var6;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = (String)var5.next();
         } while(var2.contains(var6));

         var2.add(var6);
         Iterator var7 = var1.iterator();

         while(var7.hasNext()) {
            Object var8 = null;
            String var9 = null;
            Object var10 = var7.next();
            if (var10 instanceof JarFile) {
               JarFile var11 = (JarFile)var10;
               String var12 = var6 + ".class";
               ZipEntry var13 = var11.getEntry(var12);
               if (var13 != null) {
                  var9 = var11.getName() + '!' + var12;
                  var8 = var11.getInputStream(var13);
               }
            } else {
               var9 = (String)var10 + '/' + var6 + ".class";
               File var14 = new File(var9);
               if (var14.isFile()) {
                  var8 = new FileInputStream(var14);
               }
            }

            if (var8 != null) {
               var3.add(var9);
               if (var4) {
                  computeClassDependencies((InputStream)var8, var1, var2, var3, var4);
               }
            }
         }
      }
   }

   public static Set getClassDependencies(InputStream var0) throws IOException {
      DataInputStream var1 = new DataInputStream(var0);
      if (var1.readInt() != -889275714) {
         throw new IOException("Invalid classfile");
      } else {
         var1.readInt();
         short var2 = var1.readShort();
         String[] var3 = new String[var2];
         HashSet var4 = new HashSet();
         HashSet var5 = new HashSet();

         for(int var6 = 1; var6 < var2; ++var6) {
            int var7 = var1.readByte() & 255;
            switch (var7) {
               case 1:
                  var3[var6] = var1.readUTF();
                  break;
               case 2:
               default:
                  throw new RuntimeException("unexpected data in constant-pool:" + var7);
               case 3:
               case 4:
               case 9:
               case 10:
               case 11:
                  var1.readInt();
                  break;
               case 5:
               case 6:
                  var1.readLong();
                  ++var6;
                  break;
               case 7:
                  var4.add(new Integer(var1.readShort() & '\uffff'));
                  break;
               case 8:
                  var1.readShort();
                  break;
               case 12:
                  var1.readShort();
                  var5.add(new Integer(var1.readShort() & '\uffff'));
            }
         }

         HashSet var8 = new HashSet();
         Iterator var9 = var4.iterator();

         while(var9.hasNext()) {
            var8.add(var3[(Integer)var9.next()]);
         }

         var9 = var5.iterator();

         while(var9.hasNext()) {
            var8.addAll(getDescriptorClasses(var3[(Integer)var9.next()]));
         }

         return var8;
      }
   }

   protected static Set getDescriptorClasses(String var0) {
      HashSet var1 = new HashSet();
      int var2 = 0;
      char var3 = var0.charAt(var2);
      StringBuffer var4;
      switch (var3) {
         case '(':
            while(true) {
               ++var2;
               var3 = var0.charAt(var2);
               switch (var3) {
                  case ')':
                     ++var2;
                     var3 = var0.charAt(var2);
                     switch (var3) {
                        case 'V':
                        default:
                           return var1;
                        case '[':
                           do {
                              ++var2;
                              var3 = var0.charAt(var2);
                           } while(var3 == '[');

                           if (var3 != 'L') {
                              return var1;
                           }
                        case 'L':
                           ++var2;
                           var3 = var0.charAt(var2);

                           for(var4 = new StringBuffer(); var3 != ';'; var3 = var0.charAt(var2)) {
                              var4.append(var3);
                              ++var2;
                           }

                           var1.add(var4.toString());
                           return var1;
                     }
                  case '[':
                     do {
                        ++var2;
                        var3 = var0.charAt(var2);
                     } while(var3 == '[');

                     if (var3 != 'L') {
                        break;
                     }
                  case 'L':
                     ++var2;
                     var3 = var0.charAt(var2);

                     for(var4 = new StringBuffer(); var3 != ';'; var3 = var0.charAt(var2)) {
                        var4.append(var3);
                        ++var2;
                     }

                     var1.add(var4.toString());
               }
            }
         case '[':
            do {
               ++var2;
               var3 = var0.charAt(var2);
            } while(var3 == '[');

            if (var3 != 'L') {
               break;
            }
         case 'L':
            ++var2;
            var3 = var0.charAt(var2);

            for(var4 = new StringBuffer(); var3 != ';'; var3 = var0.charAt(var2)) {
               var4.append(var3);
               ++var2;
            }

            var1.add(var4.toString());
      }

      return var1;
   }

   protected static class Triple implements Comparable {
      public Jar from;
      public Jar to;
      public int count;

      public int compareTo(Object var1) {
         return ((Triple)var1).count - this.count;
      }
   }

   protected static class Jar {
      public String name;
      public File file;
      public JarFile jarFile;
      public Map deps = new HashMap();
      public Set files = new HashSet();
   }

   protected static class ClassFile {
      public String name;
      public List deps = new ArrayList(10);
      public Jar jar;

      public InputStream getInputStream() throws IOException {
         return this.jar.jarFile.getInputStream(this.jar.jarFile.getEntry(this.name));
      }
   }
}
