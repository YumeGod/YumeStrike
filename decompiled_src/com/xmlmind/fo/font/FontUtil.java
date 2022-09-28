package com.xmlmind.fo.font;

import java.util.Hashtable;

public final class FontUtil {
   private static final String[] serifFamilies = new String[]{"serif", "american typewriter", "baskerville", "baskerville old face", "bell mt", "belwe", "bembo", "bernhard modern std", "big caslon", "bitstream charter", "bitstream vera serif", "bodoni mt", "bodoni mt black", "bodoni mt condensed", "bodoni mt poster compressed", "book antiqua", "bookman", "bookman old style", "californian fb", "calisto mt", "cambria", "caslon", "centaur", "century schoolbook", "century schoolbook l", "charter", "clearlyu", "constantia", "dejavu lgc serif", "dejavu lgc serif condensed", "dejavu serif", "dejavu serif condensed", "didot", "footlight mt light", "freeserif", "garamond", "georgia", "georgia ref", "gloucester mt extra condensed", "goudy old style", "high tower text", "hoefler text", "itc century", "liberation serif", "linux libertine", "lucidabright", "lucida bright", "lucida fax", "luxi serif", "mgopen canonica", "minion std", "minion web", "ms reference serif", "new century schoolbook", "new york", "niagara engraved", "niagara solid", "nimbus roman no9 l", "onyx", "palatino", "palatino linotype", "perpetua", "rockwell", "rockwell condensed", "runic mt condensed", "sylfaen", "times", "times new roman", "university roman let", "urw antiqua t", "urw bookman l", "urw palladio l", "utopia", "vera serif"};
   private static final String[] sansSerifFamilies = new String[]{"sans-serif", "04b_21", "abadi mt condensed", "abadi mt condensed extra bold", "abadi mt condensed light", "arial", "arial black", "arial narrow", "arial rounded mt bold", "arial unicode ms", "avant garde", "avenir", "berlin sans fb", "berlin sans fb demi", "berthold akzidenz grotesk be", "bitstream vera sans", "calibri", "candara", "century gothic", "charcoal", "chicago", "corbel", "dejavu lgc sans", "dejavu lgc sans condensed", "dejavu sans", "dejavu sans condensed", "dejavu sans extra light", "dejavu sans light", "dustismo", "electron", "eras bold itc", "eras demi itc", "eras light itc", "eras medium itc", "eurostile", "firsthome", "franklin gothic book", "franklin gothic demi", "franklin gothic demi cond", "franklin gothic heavy", "franklin gothic medium", "franklin gothic medium cond", "freesans", "futura", "gadget", "geneva", "gill sans", "gill sans mt", "gill sans mt condensed", "gill sans mt ext condensed bold", "gill sans ultra bold", "gill sans ultra bold condensed", "helvetica", "helvetica narrow", "helveticaneue", "helvetica neue", "interstate", "itc franklin gothic", "kabel ult bt", "kartika", "liberation sans", "lucida", "lucida grande", "lucida sans", "lucida sans unicode", "luxi sans", "metal", "mgopen cosmetica", "mgopen modata", "mgopen moderna", "microsoft sans serif", "ms reference sans serif", "news gothic mt", "nice", "nimbus sans l", "nimbus sans l condensed", "optima", "rotissemisans", "segoe ui", "skia", "tahoma", "techno", "tradegothic", "trebuchet ms", "tw cen mt", "tw cen mt condensed", "tw cen mt condensed extra bold", "univers", "urw gothic l", "vera sans", "verdana", "verdana ref", "vrinda"};
   private static final String[] monospaceFamilies = new String[]{"monospace", "andale mono", "andale mono ipa", "bitstream vera sans mono", "consolas", "courier", "courier 10 pitch", "courier new", "dejavu lgc sans mono", "dejavu sans mono", "fixed", "freemono", "liberation mono", "lucida console", "lucida sans typewriter", "lucida typewriter", "luxi mono", "monaco", "monotype.com", "nimbus mono l", "ocr a extended", "ocrb", "terminal", "vera sans mono"};
   private static final String[] fantasyFamilies = new String[]{"fantasy", "academy engraved let", "agency fb", "alba", "alba matter", "alba super", "algerian", "baby kruffy", "balker", "base 02", "bauhaus 93", "beesknees itc", "bernard mt condensed", "blackletter686 bt", "braggadocio", "britannic bold", "broadway", "broadway bt", "campbell", "capitals", "castellar", "chick", "chiller", "colonna mt", "cooper black", "copperplate", "copperplate gothic bold", "copperplate gothic light", "croobie", "curlz mt", "desdemona", "engravers mt", "felix titling", "freshbot", "frosty", "genuine", "goudy stout", "haettenschweiler", "harlow solid italic", "harrington", "herculanum", "highlight let", "impact", "imprint mt shadow", "isabella", "jokerman", "jokerman let", "jokewood", "juice itc", "junkyard", "kino mt", "linux libertine c", "magneto", "marked fool", "marker felt", "marketpro", "matisse itc", "matura mt script capitals", "mekanik let", "milano let", "misterearl bt", "odessa let", "olddreadfulno7 bt", "old english text mt", "orange let", "papyrus", "penguin attack", "perpetua titling mt", "placard condensed", "playbill", "poornut", "pump demi bold let", "pussycat", "quixley let", "ravie", "rockwell extra bold", "scruff let", "showcard gothic", "snap itc", "square721 bt", "stencil", "victorian let", "weltron urban", "westwood let", "wide latin"};
   private static final String[] cursiveFamilies = new String[]{"cursive", "a charming font", "adobe garamond", "andy", "apple casual", "apple chancery", "bickley script", "blackadder itc", "bradley hand itc", "brush script mt", "budhand", "caflisch script pro", "calligraph421 bt", "casual", "cataneo bt", "champignon", "comic sans ms", "dombold bt", "domestic manners", "edwardian script itc", "fat", "fine hand", "forte", "freestyle script", "french script mt", "gigi", "gloogun", "informal roman", "jenkins v2.0", "john handy let", "kaufmann", "kristen itc", "kunstler script", "la bamba let", "lucida calligraphy", "lucida handwriting", "maiandra gd", "mead bold", "mercurius script mt bold", "mistral", "monotype corsiva", "one stroke script let", "palace script mt", "parkavenue bt", "pepita mt", "porcelain", "pristina", "rage italic", "rage italic let", "ruach let", "sand", "script mt bold", "shelley", "smudger let", "staccato222 bt", "tempus sans itc", "textile", "tiranti solid let", "urw chancery l", "viner hand itc", "vivaldi", "vladimir script", "zapf chancery", "zapfino"};
   private static Hashtable wellKnownFamilies = new Hashtable();

   private FontUtil() {
   }

   public static String normalizeFamily(String var0) {
      if (var0 != null && (var0 = var0.trim()).length() != 0) {
         StringBuffer var1 = new StringBuffer();
         char var2 = 0;
         int var3 = var0.length();

         for(int var4 = 0; var4 < var3; ++var4) {
            char var5 = var0.charAt(var4);
            if (Character.isWhitespace(var5)) {
               var5 = ' ';
               if (var2 != ' ') {
                  var1.append(var5);
               }
            } else {
               var1.append(var5);
            }

            var2 = var5;
         }

         return var1.toString();
      } else {
         return null;
      }
   }

   public static int toGenericFamily(String var0, boolean var1) {
      int var2 = -1;
      var0 = normalizeFamily(var0);
      if (var0 != null) {
         var0 = var0.toLowerCase();
         Integer var3 = (Integer)wellKnownFamilies.get(var0);
         if (var3 != null) {
            var2 = var3;
         }

         if (var1 && var2 < 0) {
            if (var0.indexOf("mono") < 0 && var0.indexOf("typewriter") < 0 && var0.indexOf("courier") < 0) {
               if (var0.indexOf("sans") < 0 && var0.indexOf("arial") < 0 && var0.indexOf("helvetica") < 0) {
                  if (var0.indexOf("serif") >= 0 || var0.indexOf("roman") >= 0 || var0.indexOf("times") >= 0) {
                     var2 = 1;
                  }
               } else {
                  var2 = 2;
               }
            } else {
               var2 = 3;
            }
         }
      }

      return var2;
   }

   static {
      Integer var0 = new Integer(1);
      int var1 = serifFamilies.length;

      int var2;
      for(var2 = 0; var2 < var1; ++var2) {
         wellKnownFamilies.put(serifFamilies[var2], var0);
      }

      var0 = new Integer(2);
      var1 = sansSerifFamilies.length;

      for(var2 = 0; var2 < var1; ++var2) {
         wellKnownFamilies.put(sansSerifFamilies[var2], var0);
      }

      var0 = new Integer(3);
      var1 = monospaceFamilies.length;

      for(var2 = 0; var2 < var1; ++var2) {
         wellKnownFamilies.put(monospaceFamilies[var2], var0);
      }

      var0 = new Integer(4);
      var1 = fantasyFamilies.length;

      for(var2 = 0; var2 < var1; ++var2) {
         wellKnownFamilies.put(fantasyFamilies[var2], var0);
      }

      var0 = new Integer(5);
      var1 = cursiveFamilies.length;

      for(var2 = 0; var2 < var1; ++var2) {
         wellKnownFamilies.put(cursiveFamilies[var2], var0);
      }

   }
}
