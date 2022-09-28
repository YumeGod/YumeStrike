package org.apache.fop.render;

import javax.xml.transform.Source;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontResolver;

public class DefaultFontResolver implements FontResolver {
   private FOUserAgent userAgent;

   public DefaultFontResolver(FOUserAgent userAgent) {
      this.userAgent = userAgent;
   }

   public Source resolve(String href) {
      return this.userAgent.resolveURI(href, this.userAgent.getFactory().getFontManager().getFontBaseURL());
   }
}
