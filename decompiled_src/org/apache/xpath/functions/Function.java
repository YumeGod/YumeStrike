package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;

public abstract class Function extends Expression {
   static final long serialVersionUID = 6927661240854599768L;

   public void setArg(Expression arg, int argNum) throws WrongNumberArgsException {
      this.reportWrongNumberArgs();
   }

   public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
      if (argNum != 0) {
         this.reportWrongNumberArgs();
      }

   }

   protected void reportWrongNumberArgs() throws WrongNumberArgsException {
      throw new WrongNumberArgsException(XPATHMessages.createXPATHMessage("zero", (Object[])null));
   }

   public XObject execute(XPathContext xctxt) throws TransformerException {
      System.out.println("Error! Function.execute should not be called!");
      return null;
   }

   public void callArgVisitors(XPathVisitor visitor) {
   }

   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
      if (visitor.visitFunction(owner, this)) {
         this.callArgVisitors(visitor);
      }

   }

   public boolean deepEquals(Expression expr) {
      return this.isSameClass(expr);
   }

   public void postCompileStep(Compiler compiler) {
   }
}
