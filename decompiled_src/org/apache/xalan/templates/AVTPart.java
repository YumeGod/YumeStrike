package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;

public abstract class AVTPart implements Serializable, XSLTVisitable {
   static final long serialVersionUID = -1747749903613916025L;

   public abstract String getSimpleString();

   public abstract void evaluate(XPathContext var1, FastStringBuffer var2, int var3, PrefixResolver var4) throws TransformerException;

   public void setXPathSupport(XPathContext support) {
   }

   public boolean canTraverseOutsideSubtree() {
      return false;
   }

   public abstract void fixupVariables(Vector var1, int var2);

   public abstract void callVisitors(XSLTVisitor var1);
}
