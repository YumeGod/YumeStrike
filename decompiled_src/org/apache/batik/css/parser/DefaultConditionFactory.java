package org.apache.batik.css.parser;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.PositionalCondition;

public class DefaultConditionFactory implements ConditionFactory {
   public static final ConditionFactory INSTANCE = new DefaultConditionFactory();

   protected DefaultConditionFactory() {
   }

   public CombinatorCondition createAndCondition(Condition var1, Condition var2) throws CSSException {
      return new DefaultAndCondition(var1, var2);
   }

   public CombinatorCondition createOrCondition(Condition var1, Condition var2) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public NegativeCondition createNegativeCondition(Condition var1) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public PositionalCondition createPositionalCondition(int var1, boolean var2, boolean var3) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public AttributeCondition createAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException {
      return new DefaultAttributeCondition(var1, var2, var3, var4);
   }

   public AttributeCondition createIdCondition(String var1) throws CSSException {
      return new DefaultIdCondition(var1);
   }

   public LangCondition createLangCondition(String var1) throws CSSException {
      return new DefaultLangCondition(var1);
   }

   public AttributeCondition createOneOfAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException {
      return new DefaultOneOfAttributeCondition(var1, var2, var3, var4);
   }

   public AttributeCondition createBeginHyphenAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException {
      return new DefaultBeginHyphenAttributeCondition(var1, var2, var3, var4);
   }

   public AttributeCondition createClassCondition(String var1, String var2) throws CSSException {
      return new DefaultClassCondition(var1, var2);
   }

   public AttributeCondition createPseudoClassCondition(String var1, String var2) throws CSSException {
      return new DefaultPseudoClassCondition(var1, var2);
   }

   public Condition createOnlyChildCondition() throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public Condition createOnlyTypeCondition() throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }

   public ContentCondition createContentCondition(String var1) throws CSSException {
      throw new CSSException("Not implemented in CSS2");
   }
}
