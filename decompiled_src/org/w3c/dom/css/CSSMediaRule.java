package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.MediaList;

public interface CSSMediaRule extends CSSRule {
   MediaList getMedia();

   CSSRuleList getCssRules();

   int insertRule(String var1, int var2) throws DOMException;

   void deleteRule(int var1) throws DOMException;
}
