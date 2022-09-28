package org.apache.fop.fonts.substitute;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.apps.FOPException;

public class FontSubstitutionsConfigurator {
   private Configuration cfg = null;

   public FontSubstitutionsConfigurator(Configuration cfg) {
      this.cfg = cfg;
   }

   private static FontQualifier getQualfierFromConfiguration(Configuration cfg) throws FOPException {
      String fontFamily = cfg.getAttribute("font-family", (String)null);
      if (fontFamily == null) {
         throw new FOPException("substitution qualifier must have a font-family");
      } else {
         FontQualifier qualifier = new FontQualifier();
         qualifier.setFontFamily(fontFamily);
         String fontWeight = cfg.getAttribute("font-weight", (String)null);
         if (fontWeight != null) {
            qualifier.setFontWeight(fontWeight);
         }

         String fontStyle = cfg.getAttribute("font-style", (String)null);
         if (fontStyle != null) {
            qualifier.setFontStyle(fontStyle);
         }

         return qualifier;
      }
   }

   public void configure(FontSubstitutions substitutions) throws FOPException {
      Configuration[] substitutionCfgs = this.cfg.getChildren("substitution");

      for(int i = 0; i < substitutionCfgs.length; ++i) {
         Configuration fromCfg = substitutionCfgs[i].getChild("from", false);
         if (fromCfg == null) {
            throw new FOPException("'substitution' element without child 'from' element");
         }

         Configuration toCfg = substitutionCfgs[i].getChild("to", false);
         if (fromCfg == null) {
            throw new FOPException("'substitution' element without child 'to' element");
         }

         FontQualifier fromQualifier = getQualfierFromConfiguration(fromCfg);
         FontQualifier toQualifier = getQualfierFromConfiguration(toCfg);
         FontSubstitution substitution = new FontSubstitution(fromQualifier, toQualifier);
         substitutions.add(substitution);
      }

   }
}
