package org.apache.batik.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class PreferenceManager {
   protected Properties internal;
   protected Map defaults;
   protected String prefFileName;
   protected String fullName;
   protected static final String USER_HOME = getSystemProperty("user.home");
   protected static final String USER_DIR = getSystemProperty("user.dir");
   protected static final String FILE_SEP = getSystemProperty("file.separator");
   private static String PREF_DIR = null;

   protected static String getSystemProperty(String var0) {
      try {
         return System.getProperty(var0);
      } catch (AccessControlException var2) {
         return "";
      }
   }

   public PreferenceManager(String var1) {
      this(var1, (Map)null);
   }

   public PreferenceManager(String var1, Map var2) {
      this.internal = null;
      this.defaults = null;
      this.prefFileName = null;
      this.fullName = null;
      this.prefFileName = var1;
      this.defaults = var2;
      this.internal = new Properties();
   }

   public static void setPreferenceDirectory(String var0) {
      PREF_DIR = var0;
   }

   public static String getPreferenceDirectory() {
      return PREF_DIR;
   }

   public void load() throws IOException {
      FileInputStream var1 = null;
      if (this.fullName != null) {
         try {
            var1 = new FileInputStream(this.fullName);
         } catch (IOException var14) {
            this.fullName = null;
         }
      }

      if (this.fullName == null) {
         if (PREF_DIR != null) {
            try {
               var1 = new FileInputStream(this.fullName = PREF_DIR + FILE_SEP + this.prefFileName);
            } catch (IOException var13) {
               this.fullName = null;
            }
         }

         if (this.fullName == null) {
            try {
               var1 = new FileInputStream(this.fullName = USER_HOME + FILE_SEP + this.prefFileName);
            } catch (IOException var12) {
               try {
                  var1 = new FileInputStream(this.fullName = USER_DIR + FILE_SEP + this.prefFileName);
               } catch (IOException var11) {
                  this.fullName = null;
               }
            }
         }
      }

      if (this.fullName != null) {
         try {
            this.internal.load(var1);
         } finally {
            var1.close();
         }
      }

   }

   public void save() throws IOException {
      FileOutputStream var1 = null;
      if (this.fullName != null) {
         try {
            var1 = new FileOutputStream(this.fullName);
         } catch (IOException var11) {
            this.fullName = null;
         }
      }

      if (this.fullName == null) {
         if (PREF_DIR != null) {
            try {
               var1 = new FileOutputStream(this.fullName = PREF_DIR + FILE_SEP + this.prefFileName);
            } catch (IOException var10) {
               this.fullName = null;
            }
         }

         if (this.fullName == null) {
            try {
               var1 = new FileOutputStream(this.fullName = USER_HOME + FILE_SEP + this.prefFileName);
            } catch (IOException var9) {
               this.fullName = null;
               throw var9;
            }
         }
      }

      try {
         this.internal.store(var1, this.prefFileName);
      } finally {
         var1.close();
      }

   }

   private Object getDefault(String var1) {
      return this.defaults != null ? this.defaults.get(var1) : null;
   }

   public Rectangle getRectangle(String var1) {
      Rectangle var2 = (Rectangle)this.getDefault(var1);
      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         Rectangle var4 = new Rectangle();

         try {
            StringTokenizer var10 = new StringTokenizer(var3, " ", false);
            if (!var10.hasMoreTokens()) {
               this.internal.remove(var1);
               return var2;
            } else {
               String var9 = var10.nextToken();
               int var5 = Integer.parseInt(var9);
               if (!var10.hasMoreTokens()) {
                  this.internal.remove(var1);
                  return var2;
               } else {
                  var9 = var10.nextToken();
                  int var6 = Integer.parseInt(var9);
                  if (!var10.hasMoreTokens()) {
                     this.internal.remove(var1);
                     return var2;
                  } else {
                     var9 = var10.nextToken();
                     int var7 = Integer.parseInt(var9);
                     if (!var10.hasMoreTokens()) {
                        this.internal.remove(var1);
                        return var2;
                     } else {
                        var9 = var10.nextToken();
                        int var8 = Integer.parseInt(var9);
                        var4.setBounds(var5, var6, var7, var8);
                        return var4;
                     }
                  }
               }
            }
         } catch (NumberFormatException var11) {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public Dimension getDimension(String var1) {
      Dimension var2 = (Dimension)this.getDefault(var1);
      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         Dimension var4 = new Dimension();

         try {
            StringTokenizer var8 = new StringTokenizer(var3, " ", false);
            if (!var8.hasMoreTokens()) {
               this.internal.remove(var1);
               return var2;
            } else {
               String var7 = var8.nextToken();
               int var5 = Integer.parseInt(var7);
               if (!var8.hasMoreTokens()) {
                  this.internal.remove(var1);
                  return var2;
               } else {
                  var7 = var8.nextToken();
                  int var6 = Integer.parseInt(var7);
                  var4.setSize(var5, var6);
                  return var4;
               }
            }
         } catch (NumberFormatException var9) {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public Point getPoint(String var1) {
      Point var2 = (Point)this.getDefault(var1);
      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         Point var4 = new Point();

         try {
            StringTokenizer var8 = new StringTokenizer(var3, " ", false);
            if (!var8.hasMoreTokens()) {
               this.internal.remove(var1);
               return var2;
            } else {
               String var7 = var8.nextToken();
               int var5 = Integer.parseInt(var7);
               if (!var8.hasMoreTokens()) {
                  this.internal.remove(var1);
                  return var2;
               } else {
                  var7 = var8.nextToken();
                  int var6 = Integer.parseInt(var7);
                  if (!var8.hasMoreTokens()) {
                     this.internal.remove(var1);
                     return var2;
                  } else {
                     var4.setLocation(var5, var6);
                     return var4;
                  }
               }
            }
         } catch (NumberFormatException var9) {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public Color getColor(String var1) {
      Color var2 = (Color)this.getDefault(var1);
      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         try {
            StringTokenizer var9 = new StringTokenizer(var3, " ", false);
            if (!var9.hasMoreTokens()) {
               this.internal.remove(var1);
               return var2;
            } else {
               String var8 = var9.nextToken();
               int var4 = Integer.parseInt(var8);
               if (!var9.hasMoreTokens()) {
                  this.internal.remove(var1);
                  return var2;
               } else {
                  var8 = var9.nextToken();
                  int var5 = Integer.parseInt(var8);
                  if (!var9.hasMoreTokens()) {
                     this.internal.remove(var1);
                     return var2;
                  } else {
                     var8 = var9.nextToken();
                     int var6 = Integer.parseInt(var8);
                     if (!var9.hasMoreTokens()) {
                        this.internal.remove(var1);
                        return var2;
                     } else {
                        var8 = var9.nextToken();
                        int var7 = Integer.parseInt(var8);
                        return new Color(var4, var5, var6, var7);
                     }
                  }
               }
            }
         } catch (NumberFormatException var10) {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public Font getFont(String var1) {
      Font var2 = (Font)this.getDefault(var1);
      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         try {
            StringTokenizer var8 = new StringTokenizer(var3, " ", false);
            if (!var8.hasMoreTokens()) {
               this.internal.remove(var1);
               return var2;
            } else {
               String var6 = var8.nextToken();
               if (!var8.hasMoreTokens()) {
                  this.internal.remove(var1);
                  return var2;
               } else {
                  String var7 = var8.nextToken();
                  int var4 = Integer.parseInt(var7);
                  if (!var8.hasMoreTokens()) {
                     this.internal.remove(var1);
                     return var2;
                  } else {
                     var7 = var8.nextToken();
                     int var5 = Integer.parseInt(var7);
                     return new Font(var6, var5, var4);
                  }
               }
            }
         } catch (NumberFormatException var9) {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public String getString(String var1) {
      String var2 = this.internal.getProperty(var1);
      if (var2 == null) {
         var2 = (String)this.getDefault(var1);
      }

      return var2;
   }

   public String[] getStrings(String var1) {
      int var3 = 0;
      ArrayList var4 = new ArrayList();

      while(true) {
         String var2 = this.getString(var1 + var3);
         ++var3;
         if (var2 == null) {
            if (var4.size() != 0) {
               String[] var5 = new String[var4.size()];
               return (String[])var4.toArray(var5);
            } else {
               return (String[])this.getDefault(var1);
            }
         }

         var4.add(var2);
      }
   }

   public URL getURL(String var1) {
      URL var2 = (URL)this.getDefault(var1);
      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         URL var4 = null;

         try {
            var4 = new URL(var3);
            return var4;
         } catch (MalformedURLException var6) {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public URL[] getURLs(String var1) {
      int var3 = 0;
      ArrayList var4 = new ArrayList();

      while(true) {
         URL var2 = this.getURL(var1 + var3);
         ++var3;
         if (var2 == null) {
            if (var4.size() != 0) {
               URL[] var5 = new URL[var4.size()];
               return (URL[])var4.toArray(var5);
            } else {
               return (URL[])this.getDefault(var1);
            }
         }

         var4.add(var2);
      }
   }

   public File getFile(String var1) {
      File var2 = (File)this.getDefault(var1);
      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         File var4 = new File(var3);
         if (var4.exists()) {
            return var4;
         } else {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public File[] getFiles(String var1) {
      int var3 = 0;
      ArrayList var4 = new ArrayList();

      while(true) {
         File var2 = this.getFile(var1 + var3);
         ++var3;
         if (var2 == null) {
            if (var4.size() != 0) {
               File[] var5 = new File[var4.size()];
               return (File[])var4.toArray(var5);
            } else {
               return (File[])this.getDefault(var1);
            }
         }

         var4.add(var2);
      }
   }

   public int getInteger(String var1) {
      int var2 = 0;
      if (this.getDefault(var1) != null) {
         var2 = (Integer)this.getDefault(var1);
      }

      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         try {
            int var4 = Integer.parseInt(var3);
            return var4;
         } catch (NumberFormatException var6) {
            this.internal.remove(var1);
            return var2;
         }
      }
   }

   public float getFloat(String var1) {
      float var2 = 0.0F;
      if (this.getDefault(var1) != null) {
         var2 = (Float)this.getDefault(var1);
      }

      String var3 = this.internal.getProperty(var1);
      if (var3 == null) {
         return var2;
      } else {
         try {
            float var4 = Float.parseFloat(var3);
            return var4;
         } catch (NumberFormatException var6) {
            this.setFloat(var1, var2);
            return var2;
         }
      }
   }

   public boolean getBoolean(String var1) {
      if (this.internal.getProperty(var1) != null) {
         return this.internal.getProperty(var1).equals("true");
      } else {
         return this.getDefault(var1) != null ? (Boolean)this.getDefault(var1) : false;
      }
   }

   public void setRectangle(String var1, Rectangle var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2.x + " " + var2.y + " " + var2.width + ' ' + var2.height);
      } else {
         this.internal.remove(var1);
      }

   }

   public void setDimension(String var1, Dimension var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2.width + " " + var2.height);
      } else {
         this.internal.remove(var1);
      }

   }

   public void setPoint(String var1, Point var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2.x + " " + var2.y);
      } else {
         this.internal.remove(var1);
      }

   }

   public void setColor(String var1, Color var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2.getRed() + " " + var2.getGreen() + " " + var2.getBlue() + " " + var2.getAlpha());
      } else {
         this.internal.remove(var1);
      }

   }

   public void setFont(String var1, Font var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2.getName() + " " + var2.getSize() + " " + var2.getStyle());
      } else {
         this.internal.remove(var1);
      }

   }

   public void setString(String var1, String var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2);
      } else {
         this.internal.remove(var1);
      }

   }

   public void setStrings(String var1, String[] var2) {
      int var3 = 0;
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != null) {
               this.setString(var1 + var3, var2[var4]);
               ++var3;
            }
         }
      }

      while(true) {
         String var5 = this.getString(var1 + var3);
         if (var5 == null) {
            return;
         }

         this.setString(var1 + var3, (String)null);
         ++var3;
      }
   }

   public void setURL(String var1, URL var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2.toString());
      } else {
         this.internal.remove(var1);
      }

   }

   public void setURLs(String var1, URL[] var2) {
      int var3 = 0;
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != null) {
               this.setURL(var1 + var3, var2[var4]);
               ++var3;
            }
         }
      }

      while(true) {
         String var5 = this.getString(var1 + var3);
         if (var5 == null) {
            return;
         }

         this.setString(var1 + var3, (String)null);
         ++var3;
      }
   }

   public void setFile(String var1, File var2) {
      if (var2 != null && !var2.equals(this.getDefault(var1))) {
         this.internal.setProperty(var1, var2.getAbsolutePath());
      } else {
         this.internal.remove(var1);
      }

   }

   public void setFiles(String var1, File[] var2) {
      int var3 = 0;
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != null) {
               this.setFile(var1 + var3, var2[var4]);
               ++var3;
            }
         }
      }

      while(true) {
         String var5 = this.getString(var1 + var3);
         if (var5 == null) {
            return;
         }

         this.setString(var1 + var3, (String)null);
         ++var3;
      }
   }

   public void setInteger(String var1, int var2) {
      if (this.getDefault(var1) != null && (Integer)this.getDefault(var1) != var2) {
         this.internal.setProperty(var1, Integer.toString(var2));
      } else {
         this.internal.remove(var1);
      }

   }

   public void setFloat(String var1, float var2) {
      if (this.getDefault(var1) != null && (Float)this.getDefault(var1) != var2) {
         this.internal.setProperty(var1, Float.toString(var2));
      } else {
         this.internal.remove(var1);
      }

   }

   public void setBoolean(String var1, boolean var2) {
      if (this.getDefault(var1) != null && (Boolean)this.getDefault(var1) != var2) {
         this.internal.setProperty(var1, var2 ? "true" : "false");
      } else {
         this.internal.remove(var1);
      }

   }
}
