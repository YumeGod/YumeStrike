package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.MsTranslator;

public class PageReference extends Field {
   public PageReference(String var1, Context var2) {
      super("PAGEREF " + ((MsTranslator)var2.translator).checkBookmark(var1), "0");
   }
}
