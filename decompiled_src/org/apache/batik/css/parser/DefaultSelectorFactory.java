package org.apache.batik.css.parser;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

public class DefaultSelectorFactory implements SelectorFactory {
   public static final SelectorFactory INSTANCE = new DefaultSelectorFactory();

   protected DefaultSelectorFactory() {
   }

   public ConditionalSelector createConditionalSelector(SimpleSelector var1, Condition var2) throws CSSException {
      return new DefaultConditionalSelector(var1, var2);
   }

   public SimpleSelector createAnyNodeSelector() throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public SimpleSelector createRootNodeSelector() throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public NegativeSelector createNegativeSelector(SimpleSelector var1) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public ElementSelector createElementSelector(String var1, String var2) throws CSSException {
      return new DefaultElementSelector(var1, var2);
   }

   public CharacterDataSelector createTextNodeSelector(String var1) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public CharacterDataSelector createCDataSectionSelector(String var1) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public ProcessingInstructionSelector createProcessingInstructionSelector(String var1, String var2) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public CharacterDataSelector createCommentSelector(String var1) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public ElementSelector createPseudoElementSelector(String var1, String var2) throws CSSException {
      return new DefaultPseudoElementSelector(var1, var2);
   }

   public DescendantSelector createDescendantSelector(Selector var1, SimpleSelector var2) throws CSSException {
      return new DefaultDescendantSelector(var1, var2);
   }

   public DescendantSelector createChildSelector(Selector var1, SimpleSelector var2) throws CSSException {
      return new DefaultChildSelector(var1, var2);
   }

   public SiblingSelector createDirectAdjacentSelector(short var1, Selector var2, SimpleSelector var3) throws CSSException {
      return new DefaultDirectAdjacentSelector(var1, var2, var3);
   }
}
