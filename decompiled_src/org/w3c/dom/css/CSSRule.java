package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public interface CSSRule {
   short UNKNOWN_RULE = 0;
   short STYLE_RULE = 1;
   short CHARSET_RULE = 2;
   short IMPORT_RULE = 3;
   short MEDIA_RULE = 4;
   short FONT_FACE_RULE = 5;
   short PAGE_RULE = 6;

   short getType();

   String getCssText();

   void setCssText(String var1) throws DOMException;

   CSSStyleSheet getParentStyleSheet();

   CSSRule getParentRule();
}
