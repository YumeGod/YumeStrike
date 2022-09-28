package org.apache.batik.bridge.svg12;

import java.util.HashMap;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractContentSelector {
   protected ContentManager contentManager;
   protected XBLOMContentElement contentElement;
   protected Element boundElement;
   protected static HashMap selectorFactories = new HashMap();

   public AbstractContentSelector(ContentManager var1, XBLOMContentElement var2, Element var3) {
      this.contentManager = var1;
      this.contentElement = var2;
      this.boundElement = var3;
   }

   public abstract NodeList getSelectedContent();

   abstract boolean update();

   protected boolean isSelected(Node var1) {
      return this.contentManager.getContentElement(var1) != null;
   }

   public static AbstractContentSelector createSelector(String var0, ContentManager var1, XBLOMContentElement var2, Element var3, String var4) {
      ContentSelectorFactory var5 = (ContentSelectorFactory)selectorFactories.get(var0);
      if (var5 == null) {
         throw new RuntimeException("Invalid XBL content selector language '" + var0 + "'");
      } else {
         return var5.createSelector(var1, var2, var3, var4);
      }
   }

   static {
      XPathPatternContentSelectorFactory var0 = new XPathPatternContentSelectorFactory();
      XPathSubsetContentSelectorFactory var1 = new XPathSubsetContentSelectorFactory();
      selectorFactories.put((Object)null, var0);
      selectorFactories.put("XPathPattern", var0);
      selectorFactories.put("XPathSubset", var1);
   }

   protected static class XPathPatternContentSelectorFactory implements ContentSelectorFactory {
      public AbstractContentSelector createSelector(ContentManager var1, XBLOMContentElement var2, Element var3, String var4) {
         return new XPathPatternContentSelector(var1, var2, var3, var4);
      }
   }

   protected static class XPathSubsetContentSelectorFactory implements ContentSelectorFactory {
      public AbstractContentSelector createSelector(ContentManager var1, XBLOMContentElement var2, Element var3, String var4) {
         return new XPathSubsetContentSelector(var1, var2, var3, var4);
      }
   }

   protected interface ContentSelectorFactory {
      AbstractContentSelector createSelector(ContentManager var1, XBLOMContentElement var2, Element var3, String var4);
   }
}
