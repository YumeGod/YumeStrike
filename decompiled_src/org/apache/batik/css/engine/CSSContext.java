package org.apache.batik.css.engine;

import org.apache.batik.css.engine.value.Value;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;

public interface CSSContext {
   Value getSystemColor(String var1);

   Value getDefaultFontFamily();

   float getLighterFontWeight(float var1);

   float getBolderFontWeight(float var1);

   float getPixelUnitToMillimeter();

   float getPixelToMillimeter();

   float getMediumFontSize();

   float getBlockWidth(Element var1);

   float getBlockHeight(Element var1);

   void checkLoadExternalResource(ParsedURL var1, ParsedURL var2) throws SecurityException;

   boolean isDynamic();

   boolean isInteractive();

   CSSEngine getCSSEngineForElement(Element var1);
}
