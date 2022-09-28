package org.apache.batik.css.parser;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

public abstract class AbstractSiblingSelector implements SiblingSelector {
   protected short nodeType;
   protected Selector selector;
   protected SimpleSelector simpleSelector;

   protected AbstractSiblingSelector(short var1, Selector var2, SimpleSelector var3) {
      this.nodeType = var1;
      this.selector = var2;
      this.simpleSelector = var3;
   }

   public short getNodeType() {
      return this.nodeType;
   }

   public Selector getSelector() {
      return this.selector;
   }

   public SimpleSelector getSiblingSelector() {
      return this.simpleSelector;
   }
}
