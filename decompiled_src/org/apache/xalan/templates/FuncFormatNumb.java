package org.apache.xalan.templates;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function3Args;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncFormatNumb extends Function3Args {
   static final long serialVersionUID = -8869935264870858636L;

   public XObject execute(XPathContext xctxt) throws TransformerException {
      ElemTemplateElement templElem = (ElemTemplateElement)xctxt.getNamespaceContext();
      StylesheetRoot ss = templElem.getStylesheetRoot();
      DecimalFormat formatter = null;
      DecimalFormatSymbols dfs = null;
      double num = this.getArg0().execute(xctxt).num();
      String patternStr = this.getArg1().execute(xctxt).str();
      if (patternStr.indexOf(164) > 0) {
         ss.error("ER_CURRENCY_SIGN_ILLEGAL");
      }

      try {
         Expression arg2Expr = this.getArg2();
         if (null != arg2Expr) {
            String dfName = arg2Expr.execute(xctxt).str();
            QName qname = new QName(dfName, xctxt.getNamespaceContext());
            dfs = ss.getDecimalFormatComposed(qname);
            if (null == dfs) {
               this.warn(xctxt, "WG_NO_DECIMALFORMAT_DECLARATION", new Object[]{dfName});
            } else {
               formatter = new DecimalFormat();
               formatter.setDecimalFormatSymbols(dfs);
               formatter.applyLocalizedPattern(patternStr);
            }
         }

         if (null == formatter) {
            dfs = ss.getDecimalFormatComposed(new QName(""));
            if (dfs != null) {
               formatter = new DecimalFormat();
               formatter.setDecimalFormatSymbols(dfs);
               formatter.applyLocalizedPattern(patternStr);
            } else {
               dfs = new DecimalFormatSymbols(Locale.US);
               dfs.setInfinity("Infinity");
               dfs.setNaN("NaN");
               formatter = new DecimalFormat();
               formatter.setDecimalFormatSymbols(dfs);
               if (null != patternStr) {
                  formatter.applyLocalizedPattern(patternStr);
               }
            }
         }

         return new XString(formatter.format(num));
      } catch (Exception var12) {
         templElem.error("ER_MALFORMED_FORMAT_STRING", new Object[]{patternStr});
         return XString.EMPTYSTRING;
      }
   }

   public void warn(XPathContext xctxt, String msg, Object[] args) throws TransformerException {
      String formattedMsg = XSLMessages.createWarning(msg, args);
      ErrorListener errHandler = xctxt.getErrorListener();
      errHandler.warning(new TransformerException(formattedMsg, (SAXSourceLocator)xctxt.getSAXLocator()));
   }

   public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
      if (argNum > 3 || argNum < 2) {
         this.reportWrongNumberArgs();
      }

   }

   protected void reportWrongNumberArgs() throws WrongNumberArgsException {
      throw new WrongNumberArgsException(XSLMessages.createMessage("ER_TWO_OR_THREE", (Object[])null));
   }
}
