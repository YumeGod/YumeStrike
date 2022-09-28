package org.apache.xpath;

import javax.xml.transform.SourceLocator;

public interface ExpressionNode extends SourceLocator {
   void exprSetParent(ExpressionNode var1);

   ExpressionNode exprGetParent();

   void exprAddChild(ExpressionNode var1, int var2);

   ExpressionNode exprGetChild(int var1);

   int exprGetNumChildren();
}
