package de.javasoft.plaf.synthetica.filechooser;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Locale;

class GermanCollator extends RuleBasedCollator {
   private static RuleBasedCollator instance;
   private static final String rulesDIN5007_2 = "& ae , ä & AE , Ä& oe , ö & OE , Ö& ue , ü & UE , ü";

   private GermanCollator() throws ParseException {
      super(((RuleBasedCollator)Collator.getInstance(Locale.GERMAN)).getRules() + "& ae , ä & AE , Ä& oe , ö & OE , Ö& ue , ü & UE , ü");
   }

   public static RuleBasedCollator getInstance() {
      if (instance == null) {
         try {
            instance = new GermanCollator();
         } catch (ParseException var1) {
            throw new RuntimeException(var1);
         }
      }

      return instance;
   }
}
