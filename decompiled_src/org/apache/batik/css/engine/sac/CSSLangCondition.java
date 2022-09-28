package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.LangCondition;
import org.w3c.dom.Element;

public class CSSLangCondition implements LangCondition, ExtendedCondition {
   protected String lang;
   protected String langHyphen;

   public CSSLangCondition(String var1) {
      this.lang = var1.toLowerCase();
      this.langHyphen = var1 + '-';
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1.getClass() == this.getClass()) {
         CSSLangCondition var2 = (CSSLangCondition)var1;
         return var2.lang.equals(this.lang);
      } else {
         return false;
      }
   }

   public short getConditionType() {
      return 6;
   }

   public String getLang() {
      return this.lang;
   }

   public int getSpecificity() {
      return 256;
   }

   public boolean match(Element var1, String var2) {
      String var3 = var1.getAttribute("lang").toLowerCase();
      if (!var3.equals(this.lang) && !var3.startsWith(this.langHyphen)) {
         var3 = var1.getAttributeNS("http://www.w3.org/XML/1998/namespace", "lang").toLowerCase();
         return var3.equals(this.lang) || var3.startsWith(this.langHyphen);
      } else {
         return true;
      }
   }

   public void fillAttributeSet(Set var1) {
      var1.add("lang");
   }

   public String toString() {
      return ":lang(" + this.lang + ')';
   }
}
