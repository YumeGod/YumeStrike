package org.apache.batik.css.engine.sac;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.PositionalCondition;

public class CSSConditionFactory implements ConditionFactory {
   protected String classNamespaceURI;
   protected String classLocalName;
   protected String idNamespaceURI;
   protected String idLocalName;

   public CSSConditionFactory(String var1, String var2, String var3, String var4) {
      this.classNamespaceURI = var1;
      this.classLocalName = var2;
      this.idNamespaceURI = var3;
      this.idLocalName = var4;
   }

   public CombinatorCondition createAndCondition(Condition var1, Condition var2) throws CSSException {
      return new CSSAndCondition(var1, var2);
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
      return new CSSAttributeCondition(var1, var2, var3, var4);
   }

   public AttributeCondition createIdCondition(String var1) throws CSSException {
      return new CSSIdCondition(this.idNamespaceURI, this.idLocalName, var1);
   }

   public LangCondition createLangCondition(String var1) throws CSSException {
      return new CSSLangCondition(var1);
   }

   public AttributeCondition createOneOfAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException {
      return new CSSOneOfAttributeCondition(var1, var2, var3, var4);
   }

   public AttributeCondition createBeginHyphenAttributeCondition(String var1, String var2, boolean var3, String var4) throws CSSException {
      return new CSSBeginHyphenAttributeCondition(var1, var2, var3, var4);
   }

   public AttributeCondition createClassCondition(String var1, String var2) throws CSSException {
      return new CSSClassCondition(this.classLocalName, this.classNamespaceURI, var2);
   }

   public AttributeCondition createPseudoClassCondition(String var1, String var2) throws CSSException {
      return new CSSPseudoClassCondition(var1, var2);
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
