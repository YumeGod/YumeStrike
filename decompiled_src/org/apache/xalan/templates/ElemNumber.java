package org.apache.xalan.templates;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.CountersTable;
import org.apache.xalan.transformer.DecimalToRoman;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.res.CharArrayWrapper;
import org.apache.xml.utils.res.IntArrayWrapper;
import org.apache.xml.utils.res.LongArrayWrapper;
import org.apache.xml.utils.res.StringArrayWrapper;
import org.apache.xml.utils.res.XResourceBundle;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ElemNumber extends ElemTemplateElement {
   static final long serialVersionUID = 8118472298274407610L;
   private CharArrayWrapper m_alphaCountTable = null;
   private XPath m_countMatchPattern = null;
   private XPath m_fromMatchPattern = null;
   private int m_level = 1;
   private XPath m_valueExpr = null;
   private AVT m_format_avt = null;
   private AVT m_lang_avt = null;
   private AVT m_lettervalue_avt = null;
   private AVT m_groupingSeparator_avt = null;
   private AVT m_groupingSize_avt = null;
   private static final DecimalToRoman[] m_romanConvertTable = new DecimalToRoman[]{new DecimalToRoman(1000L, "M", 900L, "CM"), new DecimalToRoman(500L, "D", 400L, "CD"), new DecimalToRoman(100L, "C", 90L, "XC"), new DecimalToRoman(50L, "L", 40L, "XL"), new DecimalToRoman(10L, "X", 9L, "IX"), new DecimalToRoman(5L, "V", 4L, "IV"), new DecimalToRoman(1L, "I", 1L, "I")};

   public void setCount(XPath v) {
      this.m_countMatchPattern = v;
   }

   public XPath getCount() {
      return this.m_countMatchPattern;
   }

   public void setFrom(XPath v) {
      this.m_fromMatchPattern = v;
   }

   public XPath getFrom() {
      return this.m_fromMatchPattern;
   }

   public void setLevel(int v) {
      this.m_level = v;
   }

   public int getLevel() {
      return this.m_level;
   }

   public void setValue(XPath v) {
      this.m_valueExpr = v;
   }

   public XPath getValue() {
      return this.m_valueExpr;
   }

   public void setFormat(AVT v) {
      this.m_format_avt = v;
   }

   public AVT getFormat() {
      return this.m_format_avt;
   }

   public void setLang(AVT v) {
      this.m_lang_avt = v;
   }

   public AVT getLang() {
      return this.m_lang_avt;
   }

   public void setLetterValue(AVT v) {
      this.m_lettervalue_avt = v;
   }

   public AVT getLetterValue() {
      return this.m_lettervalue_avt;
   }

   public void setGroupingSeparator(AVT v) {
      this.m_groupingSeparator_avt = v;
   }

   public AVT getGroupingSeparator() {
      return this.m_groupingSeparator_avt;
   }

   public void setGroupingSize(AVT v) {
      this.m_groupingSize_avt = v;
   }

   public AVT getGroupingSize() {
      return this.m_groupingSize_avt;
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      Vector vnames = cstate.getVariableNames();
      if (null != this.m_countMatchPattern) {
         this.m_countMatchPattern.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_format_avt) {
         this.m_format_avt.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_fromMatchPattern) {
         this.m_fromMatchPattern.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_groupingSeparator_avt) {
         this.m_groupingSeparator_avt.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_groupingSize_avt) {
         this.m_groupingSize_avt.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_lang_avt) {
         this.m_lang_avt.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_lettervalue_avt) {
         this.m_lettervalue_avt.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_valueExpr) {
         this.m_valueExpr.fixupVariables(vnames, cstate.getGlobalsSize());
      }

   }

   public int getXSLToken() {
      return 35;
   }

   public String getNodeName() {
      return "number";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      int sourceNode = transformer.getXPathContext().getCurrentNode();
      String countString = this.getCountString(transformer, sourceNode);

      try {
         transformer.getResultTreeHandler().characters(countString.toCharArray(), 0, countString.length());
      } catch (SAXException var9) {
         throw new TransformerException(var9);
      } finally {
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

      }

   }

   public ElemTemplateElement appendChild(ElemTemplateElement newChild) {
      this.error("ER_CANNOT_ADD", new Object[]{newChild.getNodeName(), this.getNodeName()});
      return null;
   }

   int findAncestor(XPathContext xctxt, XPath fromMatchPattern, XPath countMatchPattern, int context, ElemNumber namespaceContext) throws TransformerException {
      for(DTM dtm = xctxt.getDTM(context); -1 != context && (null == fromMatchPattern || fromMatchPattern.getMatchScore(xctxt, context) == Double.NEGATIVE_INFINITY) && (null == countMatchPattern || countMatchPattern.getMatchScore(xctxt, context) == Double.NEGATIVE_INFINITY); context = dtm.getParent(context)) {
      }

      return context;
   }

   private int findPrecedingOrAncestorOrSelf(XPathContext xctxt, XPath fromMatchPattern, XPath countMatchPattern, int context, ElemNumber namespaceContext) throws TransformerException {
      DTM dtm = xctxt.getDTM(context);

      while(-1 != context) {
         if (null != fromMatchPattern && fromMatchPattern.getMatchScore(xctxt, context) != Double.NEGATIVE_INFINITY) {
            context = -1;
            break;
         }

         if (null != countMatchPattern && countMatchPattern.getMatchScore(xctxt, context) != Double.NEGATIVE_INFINITY) {
            break;
         }

         int prevSibling = dtm.getPreviousSibling(context);
         if (-1 == prevSibling) {
            context = dtm.getParent(context);
         } else {
            context = dtm.getLastChild(prevSibling);
            if (context == -1) {
               context = prevSibling;
            }
         }
      }

      return context;
   }

   XPath getCountMatchPattern(XPathContext support, int contextNode) throws TransformerException {
      XPath countMatchPattern = this.m_countMatchPattern;
      DTM dtm = support.getDTM(contextNode);
      if (null == countMatchPattern) {
         switch (dtm.getNodeType(contextNode)) {
            case 1:
               MyPrefixResolver resolver;
               if (dtm.getNamespaceURI(contextNode) == null) {
                  resolver = new MyPrefixResolver(dtm.getNode(contextNode), dtm, contextNode, false);
               } else {
                  resolver = new MyPrefixResolver(dtm.getNode(contextNode), dtm, contextNode, true);
               }

               countMatchPattern = new XPath(dtm.getNodeName(contextNode), this, resolver, 1, support.getErrorListener());
               break;
            case 2:
               countMatchPattern = new XPath("@" + dtm.getNodeName(contextNode), this, this, 1, support.getErrorListener());
               break;
            case 3:
            case 4:
               countMatchPattern = new XPath("text()", this, this, 1, support.getErrorListener());
               break;
            case 5:
            case 6:
            default:
               countMatchPattern = null;
               break;
            case 7:
               countMatchPattern = new XPath("pi(" + dtm.getNodeName(contextNode) + ")", this, this, 1, support.getErrorListener());
               break;
            case 8:
               countMatchPattern = new XPath("comment()", this, this, 1, support.getErrorListener());
               break;
            case 9:
               countMatchPattern = new XPath("/", this, this, 1, support.getErrorListener());
         }
      }

      return countMatchPattern;
   }

   String getCountString(TransformerImpl transformer, int sourceNode) throws TransformerException {
      long[] list = null;
      XPathContext xctxt = transformer.getXPathContext();
      CountersTable ctable = transformer.getCountersTable();
      if (null != this.m_valueExpr) {
         XObject countObj = this.m_valueExpr.execute(xctxt, sourceNode, this);
         double d_count = Math.floor(countObj.num() + 0.5);
         if (Double.isNaN(d_count)) {
            return "NaN";
         }

         if (d_count < 0.0 && Double.isInfinite(d_count)) {
            return "-Infinity";
         }

         if (Double.isInfinite(d_count)) {
            return "Infinity";
         }

         if (d_count == 0.0) {
            return "0";
         }

         long count = (long)d_count;
         list = new long[]{count};
      } else if (3 == this.m_level) {
         list = new long[]{(long)ctable.countNode(xctxt, this, sourceNode)};
      } else {
         NodeVector ancestors = this.getMatchingAncestors(xctxt, sourceNode, 1 == this.m_level);
         int lastIndex = ancestors.size() - 1;
         if (lastIndex >= 0) {
            list = new long[lastIndex + 1];

            for(int i = lastIndex; i >= 0; --i) {
               int target = ancestors.elementAt(i);
               list[lastIndex - i] = (long)ctable.countNode(xctxt, this, target);
            }
         }
      }

      return null != list ? this.formatNumberList(transformer, list, sourceNode) : "";
   }

   public int getPreviousNode(XPathContext xctxt, int pos) throws TransformerException {
      XPath countMatchPattern = this.getCountMatchPattern(xctxt, pos);
      DTM dtm = xctxt.getDTM(pos);
      if (3 == this.m_level) {
         XPath fromMatchPattern = this.m_fromMatchPattern;

         while(-1 != pos) {
            int next = dtm.getPreviousSibling(pos);
            if (-1 == next) {
               next = dtm.getParent(pos);
               if (-1 != next && (null != fromMatchPattern && fromMatchPattern.getMatchScore(xctxt, next) != Double.NEGATIVE_INFINITY || dtm.getNodeType(next) == 9)) {
                  pos = -1;
                  break;
               }
            } else {
               int child = next;

               while(-1 != child) {
                  child = dtm.getLastChild(next);
                  if (-1 != child) {
                     next = child;
                  }
               }
            }

            pos = next;
            if (-1 != next && (null == countMatchPattern || countMatchPattern.getMatchScore(xctxt, next) != Double.NEGATIVE_INFINITY)) {
               break;
            }
         }
      } else {
         while(-1 != pos) {
            pos = dtm.getPreviousSibling(pos);
            if (-1 != pos && (null == countMatchPattern || countMatchPattern.getMatchScore(xctxt, pos) != Double.NEGATIVE_INFINITY)) {
               break;
            }
         }
      }

      return pos;
   }

   public int getTargetNode(XPathContext xctxt, int sourceNode) throws TransformerException {
      int target = true;
      XPath countMatchPattern = this.getCountMatchPattern(xctxt, sourceNode);
      int target;
      if (3 == this.m_level) {
         target = this.findPrecedingOrAncestorOrSelf(xctxt, this.m_fromMatchPattern, countMatchPattern, sourceNode, this);
      } else {
         target = this.findAncestor(xctxt, this.m_fromMatchPattern, countMatchPattern, sourceNode, this);
      }

      return target;
   }

   NodeVector getMatchingAncestors(XPathContext xctxt, int node, boolean stopAtFirstFound) throws TransformerException {
      NodeSetDTM ancestors = new NodeSetDTM(xctxt.getDTMManager());
      XPath countMatchPattern = this.getCountMatchPattern(xctxt, node);

      for(DTM dtm = xctxt.getDTM(node); -1 != node && (null == this.m_fromMatchPattern || this.m_fromMatchPattern.getMatchScore(xctxt, node) == Double.NEGATIVE_INFINITY || stopAtFirstFound); node = dtm.getParent(node)) {
         if (null == countMatchPattern) {
            System.out.println("Programmers error! countMatchPattern should never be null!");
         }

         if (countMatchPattern.getMatchScore(xctxt, node) != Double.NEGATIVE_INFINITY) {
            ancestors.addElement(node);
            if (stopAtFirstFound) {
               break;
            }
         }
      }

      return ancestors;
   }

   Locale getLocale(TransformerImpl transformer, int contextNode) throws TransformerException {
      Locale locale = null;
      if (null != this.m_lang_avt) {
         XPathContext xctxt = transformer.getXPathContext();
         String langValue = this.m_lang_avt.evaluate(xctxt, contextNode, this);
         if (null != langValue) {
            locale = new Locale(langValue.toUpperCase(), "");
            if (null == locale) {
               transformer.getMsgMgr().warn(this, (Node)null, xctxt.getDTM(contextNode).getNode(contextNode), "WG_LOCALE_NOT_FOUND", new Object[]{langValue});
               locale = Locale.getDefault();
            }
         }
      } else {
         locale = Locale.getDefault();
      }

      return locale;
   }

   private DecimalFormat getNumberFormatter(TransformerImpl transformer, int contextNode) throws TransformerException {
      Locale locale = (Locale)this.getLocale(transformer, contextNode).clone();
      DecimalFormat formatter = null;
      String digitGroupSepValue = null != this.m_groupingSeparator_avt ? this.m_groupingSeparator_avt.evaluate(transformer.getXPathContext(), contextNode, this) : null;
      if (digitGroupSepValue != null && !this.m_groupingSeparator_avt.isSimple() && digitGroupSepValue.length() != 1) {
         transformer.getMsgMgr().warn(this, "WG_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"name", this.m_groupingSeparator_avt.getName()});
      }

      String nDigitsPerGroupValue = null != this.m_groupingSize_avt ? this.m_groupingSize_avt.evaluate(transformer.getXPathContext(), contextNode, this) : null;
      if (null != digitGroupSepValue && null != nDigitsPerGroupValue && digitGroupSepValue.length() > 0) {
         try {
            formatter = (DecimalFormat)NumberFormat.getNumberInstance(locale);
            formatter.setGroupingSize(Integer.valueOf(nDigitsPerGroupValue));
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(digitGroupSepValue.charAt(0));
            formatter.setDecimalFormatSymbols(symbols);
            formatter.setGroupingUsed(true);
         } catch (NumberFormatException var8) {
            formatter.setGroupingUsed(false);
         }
      }

      return formatter;
   }

   String formatNumberList(TransformerImpl transformer, long[] list, int contextNode) throws TransformerException {
      FastStringBuffer formattedNumber = StringBufferPool.get();

      try {
         int nNumbers = list.length;
         int numberWidth = 1;
         char numberType = '1';
         String lastSepString = null;
         String formatTokenString = null;
         String lastSep = ".";
         boolean isFirstToken = true;
         String formatValue = null != this.m_format_avt ? this.m_format_avt.evaluate(transformer.getXPathContext(), contextNode, this) : null;
         if (null == formatValue) {
            formatValue = "1";
         }

         NumberFormatStringTokenizer formatTokenizer = new NumberFormatStringTokenizer(formatValue);

         String formatToken;
         for(int i = 0; i < nNumbers; ++i) {
            if (formatTokenizer.hasMoreTokens()) {
               formatToken = formatTokenizer.nextToken();
               if (Character.isLetterOrDigit(formatToken.charAt(formatToken.length() - 1))) {
                  numberWidth = formatToken.length();
                  numberType = formatToken.charAt(numberWidth - 1);
               } else if (!formatTokenizer.isLetterOrDigitAhead()) {
                  for(lastSepString = formatToken; formatTokenizer.hasMoreTokens(); lastSepString = lastSepString + formatToken) {
                     formatToken = formatTokenizer.nextToken();
                  }
               } else {
                  for(formatTokenString = formatToken; formatTokenizer.nextIsSep(); formatTokenString = formatTokenString + formatToken) {
                     formatToken = formatTokenizer.nextToken();
                  }

                  if (!isFirstToken) {
                     lastSep = formatTokenString;
                  }

                  formatToken = formatTokenizer.nextToken();
                  numberWidth = formatToken.length();
                  numberType = formatToken.charAt(numberWidth - 1);
               }
            }

            if (null != formatTokenString && isFirstToken) {
               formattedNumber.append(formatTokenString);
            } else if (null != lastSep && !isFirstToken) {
               formattedNumber.append(lastSep);
            }

            this.getFormattedNumber(transformer, contextNode, numberType, numberWidth, list[i], formattedNumber);
            isFirstToken = false;
         }

         while(formatTokenizer.isLetterOrDigitAhead()) {
            formatTokenizer.nextToken();
         }

         if (lastSepString != null) {
            formattedNumber.append(lastSepString);
         }

         while(formatTokenizer.hasMoreTokens()) {
            formatToken = formatTokenizer.nextToken();
            formattedNumber.append(formatToken);
         }

         String numStr = formattedNumber.toString();
         return numStr;
      } finally {
         StringBufferPool.free(formattedNumber);
      }
   }

   private void getFormattedNumber(TransformerImpl transformer, int contextNode, char numberType, int numberWidth, long listElement, FastStringBuffer formattedNumber) throws TransformerException {
      String letterVal = this.m_lettervalue_avt != null ? this.m_lettervalue_avt.evaluate(transformer.getXPathContext(), contextNode, this) : null;
      CharArrayWrapper alphaCountTable = null;
      XResourceBundle thisBundle = null;
      switch (numberType) {
         case 'A':
            if (null == this.m_alphaCountTable) {
               thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", this.getLocale(transformer, contextNode));
               this.m_alphaCountTable = (CharArrayWrapper)thisBundle.getObject("alphabet");
            }

            this.int2alphaCount(listElement, this.m_alphaCountTable, formattedNumber);
            break;
         case 'I':
            formattedNumber.append(this.long2roman(listElement, true));
            break;
         case 'a':
            if (null == this.m_alphaCountTable) {
               thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", this.getLocale(transformer, contextNode));
               this.m_alphaCountTable = (CharArrayWrapper)thisBundle.getObject("alphabet");
            }

            FastStringBuffer stringBuf = StringBufferPool.get();

            try {
               this.int2alphaCount(listElement, this.m_alphaCountTable, stringBuf);
               formattedNumber.append(stringBuf.toString().toLowerCase(this.getLocale(transformer, contextNode)));
               break;
            } finally {
               StringBufferPool.free(stringBuf);
            }
         case 'i':
            formattedNumber.append(this.long2roman(listElement, true).toLowerCase(this.getLocale(transformer, contextNode)));
            break;
         case 'α':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("el", ""));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               this.int2alphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet"), formattedNumber);
            }
            break;
         case 'а':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("cy", ""));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               this.int2alphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet"), formattedNumber);
            }
            break;
         case 'א':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("he", ""));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               this.int2alphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet"), formattedNumber);
            }
            break;
         case '๑':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("th", ""));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               this.int2alphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet"), formattedNumber);
            }
            break;
         case 'ა':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ka", ""));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               this.int2alphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet"), formattedNumber);
            }
            break;
         case 'あ':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "HA"));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               formattedNumber.append(this.int2singlealphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet")));
            }
            break;
         case 'い':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "HI"));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               formattedNumber.append(this.int2singlealphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet")));
            }
            break;
         case 'ア':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "A"));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               formattedNumber.append(this.int2singlealphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet")));
            }
            break;
         case 'イ':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("ja", "JP", "I"));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               formattedNumber.append(this.int2singlealphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet")));
            }
            break;
         case '一':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("zh", "CN"));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               this.int2alphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet"), formattedNumber);
            }
            break;
         case '壹':
            thisBundle = XResourceBundle.loadResourceBundle("org.apache.xml.utils.res.XResources", new Locale("zh", "TW"));
            if (letterVal != null && letterVal.equals("traditional")) {
               formattedNumber.append(this.tradAlphaCount(listElement, thisBundle));
            } else {
               this.int2alphaCount(listElement, (CharArrayWrapper)thisBundle.getObject("alphabet"), formattedNumber);
            }
            break;
         default:
            DecimalFormat formatter = this.getNumberFormatter(transformer, contextNode);
            String padString = formatter == null ? String.valueOf(0) : formatter.format(0L);
            String numString = formatter == null ? String.valueOf(listElement) : formatter.format(listElement);
            int nPadding = numberWidth - numString.length();

            for(int k = 0; k < nPadding; ++k) {
               formattedNumber.append(padString);
            }

            formattedNumber.append(numString);
      }

   }

   String getZeroString() {
      return "0";
   }

   protected String int2singlealphaCount(long val, CharArrayWrapper table) {
      int radix = table.getLength();
      return val > (long)radix ? this.getZeroString() : (new Character(table.getChar((int)val - 1))).toString();
   }

   protected void int2alphaCount(long val, CharArrayWrapper aTable, FastStringBuffer stringBuf) {
      int radix = aTable.getLength();
      char[] table = new char[radix];

      int i;
      for(i = 0; i < radix - 1; ++i) {
         table[i + 1] = aTable.getChar(i);
      }

      table[0] = aTable.getChar(i);
      char[] buf = new char[100];
      int charPos = buf.length - 1;
      int lookupIndex = 1;
      long correction = 0L;

      do {
         correction = lookupIndex != 0 && (correction == 0L || lookupIndex != radix - 1) ? 0L : (long)(radix - 1);
         lookupIndex = (int)(val + correction) % radix;
         val /= (long)radix;
         if (lookupIndex == 0 && val == 0L) {
            break;
         }

         buf[charPos--] = table[lookupIndex];
      } while(val > 0L);

      stringBuf.append(buf, charPos + 1, buf.length - charPos - 1);
   }

   protected String tradAlphaCount(long val, XResourceBundle thisBundle) {
      if (val > Long.MAX_VALUE) {
         this.error("ER_NUMBER_TOO_BIG");
         return "#error";
      } else {
         char[] table = null;
         int lookupIndex = true;
         char[] buf = new char[100];
         int charPos = 0;
         IntArrayWrapper groups = (IntArrayWrapper)thisBundle.getObject("numberGroups");
         StringArrayWrapper tables = (StringArrayWrapper)thisBundle.getObject("tables");
         String numbering = thisBundle.getString("numbering");
         CharArrayWrapper zeroChar;
         int i;
         char[] table;
         int lookupIndex;
         if (numbering.equals("multiplicative-additive")) {
            String mult_order = thisBundle.getString("multiplierOrder");
            LongArrayWrapper multiplier = (LongArrayWrapper)thisBundle.getObject("multiplier");
            zeroChar = (CharArrayWrapper)thisBundle.getObject("zero");

            for(i = 0; i < multiplier.getLength() && val < multiplier.getLong(i); ++i) {
            }

            while(i < multiplier.getLength()) {
               if (val < multiplier.getLong(i)) {
                  if (zeroChar.getLength() == 0) {
                     ++i;
                  } else {
                     if (buf[charPos - 1] != zeroChar.getChar(0)) {
                        buf[charPos++] = zeroChar.getChar(0);
                     }

                     ++i;
                  }
               } else if (val >= multiplier.getLong(i)) {
                  long mult = val / multiplier.getLong(i);
                  val %= multiplier.getLong(i);

                  for(int k = 0; k < groups.getLength(); ++k) {
                     lookupIndex = true;
                     if (mult / (long)groups.getInt(k) > 0L) {
                        CharArrayWrapper THEletters = (CharArrayWrapper)thisBundle.getObject(tables.getString(k));
                        table = new char[THEletters.getLength() + 1];

                        int j;
                        for(j = 0; j < THEletters.getLength(); ++j) {
                           table[j + 1] = THEletters.getChar(j);
                        }

                        table[0] = THEletters.getChar(j - 1);
                        lookupIndex = (int)mult / groups.getInt(k);
                        if (lookupIndex == 0 && mult == 0L) {
                           break;
                        }

                        char multiplierChar = ((CharArrayWrapper)thisBundle.getObject("multiplierChar")).getChar(i);
                        if (lookupIndex >= table.length) {
                           return "#error";
                        }

                        if (mult_order.equals("precedes")) {
                           buf[charPos++] = multiplierChar;
                           buf[charPos++] = table[lookupIndex];
                        } else {
                           if (lookupIndex != 1 || i != multiplier.getLength() - 1) {
                              buf[charPos++] = table[lookupIndex];
                           }

                           buf[charPos++] = multiplierChar;
                        }
                        break;
                     }
                  }

                  ++i;
               }

               if (i >= multiplier.getLength()) {
                  break;
               }
            }
         }

         int count = 0;

         while(count < groups.getLength()) {
            if (val / (long)groups.getInt(count) <= 0L) {
               ++count;
            } else {
               zeroChar = (CharArrayWrapper)thisBundle.getObject(tables.getString(count));
               table = new char[zeroChar.getLength() + 1];

               for(i = 0; i < zeroChar.getLength(); ++i) {
                  table[i + 1] = zeroChar.getChar(i);
               }

               table[0] = zeroChar.getChar(i - 1);
               lookupIndex = (int)val / groups.getInt(count);
               val %= (long)groups.getInt(count);
               if (lookupIndex == 0 && val == 0L) {
                  break;
               }

               if (lookupIndex >= table.length) {
                  return "#error";
               }

               buf[charPos++] = table[lookupIndex];
               ++count;
            }
         }

         return new String(buf, 0, charPos);
      }
   }

   protected String long2roman(long val, boolean prefixesAreOK) {
      if (val <= 0L) {
         return this.getZeroString();
      } else {
         String roman = "";
         int place = 0;
         if (val > 3999L) {
            roman = "#error";
         } else {
            do {
               while(val >= m_romanConvertTable[place].m_postValue) {
                  roman = roman + m_romanConvertTable[place].m_postLetter;
                  val -= m_romanConvertTable[place].m_postValue;
               }

               if (prefixesAreOK && val >= m_romanConvertTable[place].m_preValue) {
                  roman = roman + m_romanConvertTable[place].m_preLetter;
                  val -= m_romanConvertTable[place].m_preValue;
               }

               ++place;
            } while(val > 0L);
         }

         return roman;
      }
   }

   public void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      if (callAttrs) {
         if (null != this.m_countMatchPattern) {
            this.m_countMatchPattern.getExpression().callVisitors(this.m_countMatchPattern, visitor);
         }

         if (null != this.m_fromMatchPattern) {
            this.m_fromMatchPattern.getExpression().callVisitors(this.m_fromMatchPattern, visitor);
         }

         if (null != this.m_valueExpr) {
            this.m_valueExpr.getExpression().callVisitors(this.m_valueExpr, visitor);
         }

         if (null != this.m_format_avt) {
            this.m_format_avt.callVisitors(visitor);
         }

         if (null != this.m_groupingSeparator_avt) {
            this.m_groupingSeparator_avt.callVisitors(visitor);
         }

         if (null != this.m_groupingSize_avt) {
            this.m_groupingSize_avt.callVisitors(visitor);
         }

         if (null != this.m_lang_avt) {
            this.m_lang_avt.callVisitors(visitor);
         }

         if (null != this.m_lettervalue_avt) {
            this.m_lettervalue_avt.callVisitors(visitor);
         }
      }

      super.callChildVisitors(visitor, callAttrs);
   }

   class NumberFormatStringTokenizer {
      private int currentPosition;
      private int maxPosition;
      private String str;

      public NumberFormatStringTokenizer(String str) {
         this.str = str;
         this.maxPosition = str.length();
      }

      public void reset() {
         this.currentPosition = 0;
      }

      public String nextToken() {
         if (this.currentPosition >= this.maxPosition) {
            throw new NoSuchElementException();
         } else {
            int start;
            for(start = this.currentPosition; this.currentPosition < this.maxPosition && Character.isLetterOrDigit(this.str.charAt(this.currentPosition)); ++this.currentPosition) {
            }

            if (start == this.currentPosition && !Character.isLetterOrDigit(this.str.charAt(this.currentPosition))) {
               ++this.currentPosition;
            }

            return this.str.substring(start, this.currentPosition);
         }
      }

      public boolean isLetterOrDigitAhead() {
         for(int pos = this.currentPosition; pos < this.maxPosition; ++pos) {
            if (Character.isLetterOrDigit(this.str.charAt(pos))) {
               return true;
            }
         }

         return false;
      }

      public boolean nextIsSep() {
         return !Character.isLetterOrDigit(this.str.charAt(this.currentPosition));
      }

      public boolean hasMoreTokens() {
         return this.currentPosition < this.maxPosition;
      }

      public int countTokens() {
         int count = 0;

         for(int currpos = this.currentPosition; currpos < this.maxPosition; ++count) {
            int start;
            for(start = currpos; currpos < this.maxPosition && Character.isLetterOrDigit(this.str.charAt(currpos)); ++currpos) {
            }

            if (start == currpos && !Character.isLetterOrDigit(this.str.charAt(currpos))) {
               ++currpos;
            }
         }

         return count;
      }
   }

   private class MyPrefixResolver implements PrefixResolver {
      DTM dtm;
      int handle;
      boolean handleNullPrefix;

      public MyPrefixResolver(Node xpathExpressionContext, DTM dtm, int handle, boolean handleNullPrefix) {
         this.dtm = dtm;
         this.handle = handle;
         this.handleNullPrefix = handleNullPrefix;
      }

      public String getNamespaceForPrefix(String prefix) {
         return this.dtm.getNamespaceURI(this.handle);
      }

      public String getNamespaceForPrefix(String prefix, Node context) {
         return this.getNamespaceForPrefix(prefix);
      }

      public String getBaseIdentifier() {
         return ElemNumber.this.getBaseIdentifier();
      }

      public boolean handlesNullPrefixes() {
         return this.handleNullPrefix;
      }
   }
}
