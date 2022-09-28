package org.apache.fop.hyphenation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

public class Hyphenator {
   protected static Log log;
   private static HyphenationTreeCache hTreeCache;
   private HyphenationTree hyphenTree = null;
   private int remainCharCount = 2;
   private int pushCharCount = 2;
   private static boolean statisticsDump;

   public Hyphenator(String lang, String country, int leftMin, int rightMin) {
      this.hyphenTree = getHyphenationTree(lang, country);
      this.remainCharCount = leftMin;
      this.pushCharCount = rightMin;
   }

   public static synchronized HyphenationTreeCache getHyphenationTreeCache() {
      if (hTreeCache == null) {
         hTreeCache = new HyphenationTreeCache();
      }

      return hTreeCache;
   }

   public static HyphenationTree getHyphenationTree(String lang, String country) {
      return getHyphenationTree(lang, country, (HyphenationTreeResolver)null);
   }

   public static HyphenationTree getHyphenationTree(String lang, String country, HyphenationTreeResolver resolver) {
      String key = HyphenationTreeCache.constructKey(lang, country);
      HyphenationTreeCache cache = getHyphenationTreeCache();
      if (cache.isMissing(key)) {
         return null;
      } else {
         HyphenationTree hTree = getHyphenationTreeCache().getHyphenationTree(lang, country);
         if (hTree != null) {
            return hTree;
         } else {
            if (resolver != null) {
               hTree = getUserHyphenationTree(key, resolver);
            }

            if (hTree == null) {
               hTree = getFopHyphenationTree(key);
            }

            if (hTree != null) {
               cache.cache(key, hTree);
            } else {
               log.error("Couldn't find hyphenation pattern " + key);
               cache.noteMissing(key);
            }

            return hTree;
         }
      }
   }

   private static InputStream getResourceStream(String key) {
      InputStream is = null;

      try {
         Method getCCL = Thread.class.getMethod("getContextClassLoader");
         if (getCCL != null) {
            ClassLoader contextClassLoader = (ClassLoader)getCCL.invoke(Thread.currentThread());
            is = contextClassLoader.getResourceAsStream("hyph/" + key + ".hyp");
         }
      } catch (Exception var4) {
      }

      if (is == null) {
         is = Hyphenator.class.getResourceAsStream("/hyph/" + key + ".hyp");
      }

      return is;
   }

   private static HyphenationTree readHyphenationTree(InputStream in) {
      HyphenationTree hTree = null;

      try {
         ObjectInputStream ois = new ObjectInputStream(in);
         hTree = (HyphenationTree)ois.readObject();
      } catch (IOException var3) {
         log.error("I/O error while loading precompiled hyphenation pattern file", var3);
      } catch (ClassNotFoundException var4) {
         log.error("Error while reading hyphenation object from file", var4);
      }

      return hTree;
   }

   public static HyphenationTree getFopHyphenationTree(String key) {
      HyphenationTree hTree = null;
      ObjectInputStream ois = null;
      InputStream is = null;

      try {
         is = getResourceStream(key);
         if (is == null) {
            String lang;
            if (key.length() != 5) {
               if (log.isDebugEnabled()) {
                  log.debug("Couldn't find precompiled hyphenation pattern " + key + " in resources");
               }

               lang = null;
               return lang;
            }

            lang = key.substring(0, 2);
            is = getResourceStream(lang);
            if (is == null) {
               if (log.isDebugEnabled()) {
                  log.debug("Couldn't find precompiled hyphenation pattern " + lang + " in resources.");
               }

               Object var5 = null;
               return (HyphenationTree)var5;
            }

            if (log.isDebugEnabled()) {
               log.debug("Couldn't find hyphenation pattern '" + key + "'. Using general language pattern '" + lang + "' instead.");
            }
         }

         hTree = readHyphenationTree(is);
      } finally {
         IOUtils.closeQuietly((InputStream)ois);
      }

      return hTree;
   }

   public static HyphenationTree getUserHyphenationTree(String key, String hyphenDir) {
      final File baseDir = new File(hyphenDir);
      HyphenationTreeResolver resolver = new HyphenationTreeResolver() {
         public Source resolve(String href) {
            File f = new File(baseDir, href);
            return new StreamSource(f);
         }
      };
      return getUserHyphenationTree(key, resolver);
   }

   public static HyphenationTree getUserHyphenationTree(String key, HyphenationTreeResolver resolver) {
      HyphenationTree hTree = null;
      String name = key + ".hyp";
      Source source = resolver.resolve(name);
      if (source != null) {
         try {
            InputStream in = null;
            if (source instanceof StreamSource) {
               in = ((StreamSource)source).getInputStream();
            }

            if (in == null) {
               if (source.getSystemId() == null) {
                  throw new UnsupportedOperationException("Cannot load hyphenation pattern file with the supplied Source object: " + source);
               }

               in = (new URL(source.getSystemId())).openStream();
            }

            InputStream in = new BufferedInputStream(in);

            try {
               hTree = readHyphenationTree(in);
            } finally {
               IOUtils.closeQuietly((InputStream)in);
            }

            return hTree;
         } catch (IOException var22) {
            if (log.isDebugEnabled()) {
               log.debug("I/O problem while trying to load " + name, var22);
            }
         }
      }

      name = key + ".xml";
      source = resolver.resolve(name);
      if (source != null) {
         hTree = new HyphenationTree();

         try {
            InputStream in = null;
            if (source instanceof StreamSource) {
               in = ((StreamSource)source).getInputStream();
            }

            if (in == null) {
               if (source.getSystemId() == null) {
                  throw new UnsupportedOperationException("Cannot load hyphenation pattern file with the supplied Source object: " + source);
               }

               in = (new URL(source.getSystemId())).openStream();
            }

            if (!(in instanceof BufferedInputStream)) {
               in = new BufferedInputStream((InputStream)in);
            }

            try {
               InputSource src = new InputSource((InputStream)in);
               src.setSystemId(source.getSystemId());
               hTree.loadPatterns(src);
            } finally {
               IOUtils.closeQuietly((InputStream)in);
            }

            if (statisticsDump) {
               System.out.println("Stats: ");
               hTree.printStats();
            }

            return hTree;
         } catch (HyphenationException var20) {
            log.error("Can't load user patterns from XML file " + source.getSystemId() + ": " + var20.getMessage());
            return null;
         } catch (IOException var21) {
            if (log.isDebugEnabled()) {
               log.debug("I/O problem while trying to load " + name, var21);
            }

            return null;
         }
      } else {
         if (log.isDebugEnabled()) {
            log.debug("Could not load user hyphenation file for '" + key + "'.");
         }

         return null;
      }
   }

   public static Hyphenation hyphenate(String lang, String country, HyphenationTreeResolver resolver, String word, int leftMin, int rightMin) {
      HyphenationTree hTree = getHyphenationTree(lang, country, resolver);
      return hTree == null ? null : hTree.hyphenate(word, leftMin, rightMin);
   }

   public static Hyphenation hyphenate(String lang, String country, String word, int leftMin, int rightMin) {
      return hyphenate(lang, country, (HyphenationTreeResolver)null, word, leftMin, rightMin);
   }

   public static Hyphenation hyphenate(String lang, String country, HyphenationTreeResolver resolver, char[] word, int offset, int len, int leftMin, int rightMin) {
      HyphenationTree hTree = getHyphenationTree(lang, country, resolver);
      return hTree == null ? null : hTree.hyphenate(word, offset, len, leftMin, rightMin);
   }

   public static Hyphenation hyphenate(String lang, String country, char[] word, int offset, int len, int leftMin, int rightMin) {
      return hyphenate(lang, country, (HyphenationTreeResolver)null, word, offset, len, leftMin, rightMin);
   }

   public void setMinRemainCharCount(int min) {
      this.remainCharCount = min;
   }

   public void setMinPushCharCount(int min) {
      this.pushCharCount = min;
   }

   public void setLanguage(String lang, String country) {
      this.hyphenTree = getHyphenationTree(lang, country);
   }

   public Hyphenation hyphenate(char[] word, int offset, int len) {
      return this.hyphenTree == null ? null : this.hyphenTree.hyphenate(word, offset, len, this.remainCharCount, this.pushCharCount);
   }

   public Hyphenation hyphenate(String word) {
      return this.hyphenTree == null ? null : this.hyphenTree.hyphenate(word, this.remainCharCount, this.pushCharCount);
   }

   static {
      log = LogFactory.getLog(Hyphenator.class);
      hTreeCache = null;
      statisticsDump = false;
   }
}
