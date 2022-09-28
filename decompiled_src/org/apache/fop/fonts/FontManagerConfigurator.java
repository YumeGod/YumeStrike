package org.apache.fop.fonts;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.substitute.FontSubstitutions;
import org.apache.fop.fonts.substitute.FontSubstitutionsConfigurator;
import org.apache.fop.util.LogUtil;

public class FontManagerConfigurator {
   private static Log log;
   private Configuration cfg;

   public FontManagerConfigurator(Configuration cfg) {
      this.cfg = cfg;
   }

   public void configure(FontManager fontManager, boolean strict) throws FOPException {
      if (this.cfg.getChild("use-cache", false) != null) {
         try {
            fontManager.setUseCache(this.cfg.getChild("use-cache").getValueAsBoolean());
         } catch (ConfigurationException var8) {
            LogUtil.handleException(log, var8, true);
         }
      }

      if (this.cfg.getChild("font-base", false) != null) {
         try {
            fontManager.setFontBaseURL(this.cfg.getChild("font-base").getValue((String)null));
         } catch (MalformedURLException var7) {
            LogUtil.handleException(log, var7, true);
         }
      }

      Configuration fontsCfg = this.cfg.getChild("fonts", false);
      if (fontsCfg != null) {
         Configuration substitutionsCfg = fontsCfg.getChild("substitutions", false);
         if (substitutionsCfg != null) {
            FontSubstitutionsConfigurator fontSubstitutionsConfigurator = new FontSubstitutionsConfigurator(substitutionsCfg);
            FontSubstitutions substitutions = new FontSubstitutions();
            fontSubstitutionsConfigurator.configure(substitutions);
            fontManager.setFontSubstitutions(substitutions);
         }

         Configuration referencedFontsCfg = fontsCfg.getChild("referenced-fonts", false);
         if (referencedFontsCfg != null) {
            FontTriplet.Matcher matcher = createFontsMatcher(referencedFontsCfg, strict);
            fontManager.setReferencedFontsMatcher(matcher);
         }
      }

   }

   public static FontTriplet.Matcher createFontsMatcher(Configuration cfg, boolean strict) throws FOPException {
      List matcherList = new ArrayList();
      Configuration[] matches = cfg.getChildren("match");

      for(int i = 0; i < matches.length; ++i) {
         try {
            matcherList.add(new FontFamilyRegExFontTripletMatcher(matches[i].getAttribute("font-family")));
         } catch (ConfigurationException var6) {
            LogUtil.handleException(log, var6, strict);
         }
      }

      FontTriplet.Matcher orMatcher = new OrFontTripletMatcher((FontTriplet.Matcher[])matcherList.toArray(new FontTriplet.Matcher[matcherList.size()]));
      return orMatcher;
   }

   static {
      log = LogFactory.getLog(FontManagerConfigurator.class);
   }

   private static class FontFamilyRegExFontTripletMatcher implements FontTriplet.Matcher {
      private Pattern regex;

      public FontFamilyRegExFontTripletMatcher(String regex) {
         this.regex = Pattern.compile(regex);
      }

      public boolean matches(FontTriplet triplet) {
         return this.regex.matcher(triplet.getName()).matches();
      }
   }

   private static class OrFontTripletMatcher implements FontTriplet.Matcher {
      private FontTriplet.Matcher[] matchers;

      public OrFontTripletMatcher(FontTriplet.Matcher[] matchers) {
         this.matchers = matchers;
      }

      public boolean matches(FontTriplet triplet) {
         int i = 0;

         for(int c = this.matchers.length; i < c; ++i) {
            if (this.matchers[i].matches(triplet)) {
               return true;
            }
         }

         return false;
      }
   }
}
