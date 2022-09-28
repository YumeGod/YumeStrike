package org.apache.xpath;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.compiler.XPathParser;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.Node;

public class XPath implements Serializable, ExpressionOwner {
   static final long serialVersionUID = 3976493477939110553L;
   private Expression m_mainExp;
   private transient FunctionTable m_funcTable;
   String m_patternString;
   public static final int SELECT = 0;
   public static final int MATCH = 1;
   private static final boolean DEBUG_MATCHES = false;
   public static final double MATCH_SCORE_NONE = Double.NEGATIVE_INFINITY;
   public static final double MATCH_SCORE_QNAME = 0.0;
   public static final double MATCH_SCORE_NSWILD = -0.25;
   public static final double MATCH_SCORE_NODETEST = -0.5;
   public static final double MATCH_SCORE_OTHER = 0.5;

   private void initFunctionTable() {
      this.m_funcTable = new FunctionTable();
   }

   public Expression getExpression() {
      return this.m_mainExp;
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      this.m_mainExp.fixupVariables(vars, globalsSize);
   }

   public void setExpression(Expression exp) {
      if (null != this.m_mainExp) {
         exp.exprSetParent(this.m_mainExp.exprGetParent());
      }

      this.m_mainExp = exp;
   }

   public SourceLocator getLocator() {
      return this.m_mainExp;
   }

   public String getPatternString() {
      return this.m_patternString;
   }

   public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type, ErrorListener errorListener) throws TransformerException {
      this.m_funcTable = null;
      this.initFunctionTable();
      if (null == errorListener) {
         errorListener = new DefaultErrorHandler();
      }

      this.m_patternString = exprString;
      XPathParser parser = new XPathParser((ErrorListener)errorListener, locator);
      Compiler compiler = new Compiler((ErrorListener)errorListener, locator, this.m_funcTable);
      if (0 == type) {
         parser.initXPath(compiler, exprString, prefixResolver);
      } else {
         if (1 != type) {
            throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_CANNOT_DEAL_XPATH_TYPE", new Object[]{Integer.toString(type)}));
         }

         parser.initMatchPattern(compiler, exprString, prefixResolver);
      }

      Expression expr = compiler.compile(0);
      this.setExpression(expr);
      if (null != locator && locator instanceof ExpressionNode) {
         expr.exprSetParent((ExpressionNode)locator);
      }

   }

   public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type, ErrorListener errorListener, FunctionTable aTable) throws TransformerException {
      this.m_funcTable = null;
      this.m_funcTable = aTable;
      if (null == errorListener) {
         errorListener = new DefaultErrorHandler();
      }

      this.m_patternString = exprString;
      XPathParser parser = new XPathParser((ErrorListener)errorListener, locator);
      Compiler compiler = new Compiler((ErrorListener)errorListener, locator, this.m_funcTable);
      if (0 == type) {
         parser.initXPath(compiler, exprString, prefixResolver);
      } else {
         if (1 != type) {
            throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_CANNOT_DEAL_XPATH_TYPE", new Object[]{Integer.toString(type)}));
         }

         parser.initMatchPattern(compiler, exprString, prefixResolver);
      }

      Expression expr = compiler.compile(0);
      this.setExpression(expr);
      if (null != locator && locator instanceof ExpressionNode) {
         expr.exprSetParent((ExpressionNode)locator);
      }

   }

   public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type) throws TransformerException {
      this(exprString, locator, prefixResolver, type, (ErrorListener)null);
   }

   public XPath(Expression expr) {
      this.m_funcTable = null;
      this.setExpression(expr);
      this.initFunctionTable();
   }

   public XObject execute(XPathContext xctxt, Node contextNode, PrefixResolver namespaceContext) throws TransformerException {
      return this.execute(xctxt, xctxt.getDTMHandleFromNode(contextNode), namespaceContext);
   }

   public XObject execute(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext) throws TransformerException {
      xctxt.pushNamespaceContext(namespaceContext);
      xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
      XObject xobj = null;

      try {
         xobj = this.m_mainExp.execute(xctxt);
      } catch (TransformerException var15) {
         var15.setLocator(this.getLocator());
         ErrorListener el = xctxt.getErrorListener();
         if (null == el) {
            throw var15;
         }

         el.error(var15);
      } catch (Exception var16) {
         Exception e;
         for(e = var16; e instanceof WrappedRuntimeException; e = ((WrappedRuntimeException)e).getException()) {
         }

         String msg = e.getMessage();
         if (msg == null || msg.length() == 0) {
            msg = XPATHMessages.createXPATHMessage("ER_XPATH_ERROR", (Object[])null);
         }

         TransformerException te = new TransformerException(msg, this.getLocator(), e);
         ErrorListener el = xctxt.getErrorListener();
         if (null == el) {
            throw te;
         }

         el.fatalError(te);
      } finally {
         xctxt.popNamespaceContext();
         xctxt.popCurrentNodeAndExpression();
      }

      return xobj;
   }

   public boolean bool(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext) throws TransformerException {
      xctxt.pushNamespaceContext(namespaceContext);
      xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);

      try {
         TransformerException te = this.m_mainExp.bool(xctxt);
         return te;
      } catch (TransformerException var14) {
         var14.setLocator(this.getLocator());
         ErrorListener el = xctxt.getErrorListener();
         if (null == el) {
            throw var14;
         }

         el.error(var14);
      } catch (Exception var15) {
         Exception e;
         for(e = var15; e instanceof WrappedRuntimeException; e = ((WrappedRuntimeException)e).getException()) {
         }

         String msg = e.getMessage();
         if (msg == null || msg.length() == 0) {
            msg = XPATHMessages.createXPATHMessage("ER_XPATH_ERROR", (Object[])null);
         }

         TransformerException te = new TransformerException(msg, this.getLocator(), e);
         ErrorListener el = xctxt.getErrorListener();
         if (null == el) {
            throw te;
         }

         el.fatalError(te);
      } finally {
         xctxt.popNamespaceContext();
         xctxt.popCurrentNodeAndExpression();
      }

      return false;
   }

   public double getMatchScore(XPathContext xctxt, int context) throws TransformerException {
      xctxt.pushCurrentNode(context);
      xctxt.pushCurrentExpressionNode(context);

      double var4;
      try {
         XObject score = this.m_mainExp.execute(xctxt);
         var4 = score.num();
      } finally {
         xctxt.popCurrentNode();
         xctxt.popCurrentExpressionNode();
      }

      return var4;
   }

   public void warn(XPathContext xctxt, int sourceNode, String msg, Object[] args) throws TransformerException {
      String fmsg = XPATHMessages.createXPATHWarning(msg, args);
      ErrorListener ehandler = xctxt.getErrorListener();
      if (null != ehandler) {
         ehandler.warning(new TransformerException(fmsg, (SAXSourceLocator)xctxt.getSAXLocator()));
      }

   }

   public void assertion(boolean b, String msg) {
      if (!b) {
         String fMsg = XPATHMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{msg});
         throw new RuntimeException(fMsg);
      }
   }

   public void error(XPathContext xctxt, int sourceNode, String msg, Object[] args) throws TransformerException {
      String fmsg = XPATHMessages.createXPATHMessage(msg, args);
      ErrorListener ehandler = xctxt.getErrorListener();
      if (null != ehandler) {
         ehandler.fatalError(new TransformerException(fmsg, (SAXSourceLocator)xctxt.getSAXLocator()));
      } else {
         SourceLocator slocator = xctxt.getSAXLocator();
         System.out.println(fmsg + "; file " + slocator.getSystemId() + "; line " + slocator.getLineNumber() + "; column " + slocator.getColumnNumber());
      }

   }

   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
      this.m_mainExp.callVisitors(this, visitor);
   }
}
