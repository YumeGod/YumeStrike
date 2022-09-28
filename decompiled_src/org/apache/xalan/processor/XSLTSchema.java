package org.apache.xalan.processor;

import java.util.Hashtable;
import org.apache.xml.utils.QName;

public class XSLTSchema extends XSLTElementDef {
   private Hashtable m_availElems = new Hashtable();
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemTextLiteral;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemLiteralResult;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemUnknown;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemValueOf;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemCopyOf;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemNumber;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemSort;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemWithParam;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemApplyTemplates;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemApplyImport;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemForEach;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemIf;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemWhen;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemOtherwise;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemChoose;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemAttribute;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemCallTemplate;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemVariable;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemParam;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemText;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemPI;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemElement;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemComment;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemCopy;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemMessage;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemFallback;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemExsltFunction;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemExsltFuncResult;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemTemplate;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemExtensionScript;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemExtensionDecl;

   XSLTSchema() {
      this.build();
   }

   void build() {
      XSLTAttributeDef hrefAttr = new XSLTAttributeDef((String)null, "href", 2, true, false, 1);
      XSLTAttributeDef elementsAttr = new XSLTAttributeDef((String)null, "elements", 12, true, false, 1);
      XSLTAttributeDef methodAttr = new XSLTAttributeDef((String)null, "method", 9, false, false, 1);
      XSLTAttributeDef versionAttr = new XSLTAttributeDef((String)null, "version", 13, false, false, 1);
      XSLTAttributeDef encodingAttr = new XSLTAttributeDef((String)null, "encoding", 1, false, false, 1);
      XSLTAttributeDef omitXmlDeclarationAttr = new XSLTAttributeDef((String)null, "omit-xml-declaration", 8, false, false, 1);
      XSLTAttributeDef standaloneAttr = new XSLTAttributeDef((String)null, "standalone", 8, false, false, 1);
      XSLTAttributeDef doctypePublicAttr = new XSLTAttributeDef((String)null, "doctype-public", 1, false, false, 1);
      XSLTAttributeDef doctypeSystemAttr = new XSLTAttributeDef((String)null, "doctype-system", 1, false, false, 1);
      XSLTAttributeDef cdataSectionElementsAttr = new XSLTAttributeDef((String)null, "cdata-section-elements", 19, false, false, 1);
      XSLTAttributeDef indentAttr = new XSLTAttributeDef((String)null, "indent", 8, false, false, 1);
      XSLTAttributeDef mediaTypeAttr = new XSLTAttributeDef((String)null, "media-type", 1, false, false, 1);
      XSLTAttributeDef nameAttrRequired = new XSLTAttributeDef((String)null, "name", 9, true, false, 1);
      XSLTAttributeDef nameAVTRequired = new XSLTAttributeDef((String)null, "name", 18, true, true, 2);
      XSLTAttributeDef nameAVT_NCNAMERequired = new XSLTAttributeDef((String)null, "name", 17, true, true, 2);
      XSLTAttributeDef nameAttrOpt_ERROR = new XSLTAttributeDef((String)null, "name", 9, false, false, 1);
      XSLTAttributeDef useAttr = new XSLTAttributeDef((String)null, "use", 5, true, false, 1);
      XSLTAttributeDef namespaceAVTOpt = new XSLTAttributeDef((String)null, "namespace", 2, false, true, 2);
      XSLTAttributeDef decimalSeparatorAttr = new XSLTAttributeDef((String)null, "decimal-separator", 6, false, 1, ".");
      XSLTAttributeDef infinityAttr = new XSLTAttributeDef((String)null, "infinity", 1, false, 1, "Infinity");
      XSLTAttributeDef minusSignAttr = new XSLTAttributeDef((String)null, "minus-sign", 6, false, 1, "-");
      XSLTAttributeDef NaNAttr = new XSLTAttributeDef((String)null, "NaN", 1, false, 1, "NaN");
      XSLTAttributeDef percentAttr = new XSLTAttributeDef((String)null, "percent", 6, false, 1, "%");
      XSLTAttributeDef perMilleAttr = new XSLTAttributeDef((String)null, "per-mille", 6, false, false, 1);
      XSLTAttributeDef zeroDigitAttr = new XSLTAttributeDef((String)null, "zero-digit", 6, false, 1, "0");
      XSLTAttributeDef digitAttr = new XSLTAttributeDef((String)null, "digit", 6, false, 1, "#");
      XSLTAttributeDef patternSeparatorAttr = new XSLTAttributeDef((String)null, "pattern-separator", 6, false, 1, ";");
      XSLTAttributeDef groupingSeparatorAttr = new XSLTAttributeDef((String)null, "grouping-separator", 6, false, 1, ",");
      XSLTAttributeDef useAttributeSetsAttr = new XSLTAttributeDef((String)null, "use-attribute-sets", 10, false, false, 1);
      XSLTAttributeDef testAttrRequired = new XSLTAttributeDef((String)null, "test", 5, true, false, 1);
      XSLTAttributeDef selectAttrRequired = new XSLTAttributeDef((String)null, "select", 5, true, false, 1);
      XSLTAttributeDef selectAttrOpt = new XSLTAttributeDef((String)null, "select", 5, false, false, 1);
      XSLTAttributeDef selectAttrDefNode = new XSLTAttributeDef((String)null, "select", 5, false, 1, "node()");
      XSLTAttributeDef selectAttrDefDot = new XSLTAttributeDef((String)null, "select", 5, false, 1, ".");
      XSLTAttributeDef matchAttrRequired = new XSLTAttributeDef((String)null, "match", 4, true, false, 1);
      XSLTAttributeDef matchAttrOpt = new XSLTAttributeDef((String)null, "match", 4, false, false, 1);
      XSLTAttributeDef priorityAttr = new XSLTAttributeDef((String)null, "priority", 7, false, false, 1);
      XSLTAttributeDef modeAttr = new XSLTAttributeDef((String)null, "mode", 9, false, false, 1);
      XSLTAttributeDef spaceAttr = new XSLTAttributeDef("http://www.w3.org/XML/1998/namespace", "space", false, false, false, 2, "default", 2, "preserve", 1);
      XSLTAttributeDef spaceAttrLiteral = new XSLTAttributeDef("http://www.w3.org/XML/1998/namespace", "space", 2, false, true, 1);
      XSLTAttributeDef stylesheetPrefixAttr = new XSLTAttributeDef((String)null, "stylesheet-prefix", 1, true, false, 1);
      XSLTAttributeDef resultPrefixAttr = new XSLTAttributeDef((String)null, "result-prefix", 1, true, false, 1);
      XSLTAttributeDef disableOutputEscapingAttr = new XSLTAttributeDef((String)null, "disable-output-escaping", 8, false, false, 1);
      XSLTAttributeDef levelAttr = new XSLTAttributeDef((String)null, "level", false, false, false, 1, "single", 1, "multiple", 2, "any", 3);
      levelAttr.setDefault("single");
      XSLTAttributeDef countAttr = new XSLTAttributeDef((String)null, "count", 4, false, false, 1);
      XSLTAttributeDef fromAttr = new XSLTAttributeDef((String)null, "from", 4, false, false, 1);
      XSLTAttributeDef valueAttr = new XSLTAttributeDef((String)null, "value", 5, false, false, 1);
      XSLTAttributeDef formatAttr = new XSLTAttributeDef((String)null, "format", 1, false, true, 1);
      formatAttr.setDefault("1");
      XSLTAttributeDef langAttr = new XSLTAttributeDef((String)null, "lang", 13, false, true, 1);
      XSLTAttributeDef letterValueAttr = new XSLTAttributeDef((String)null, "letter-value", false, true, false, 1, "alphabetic", 1, "traditional", 2);
      XSLTAttributeDef groupingSeparatorAVT = new XSLTAttributeDef((String)null, "grouping-separator", 6, false, true, 1);
      XSLTAttributeDef groupingSizeAttr = new XSLTAttributeDef((String)null, "grouping-size", 7, false, true, 1);
      XSLTAttributeDef dataTypeAttr = new XSLTAttributeDef((String)null, "data-type", false, true, true, 1, "text", 1, "number", 1);
      dataTypeAttr.setDefault("text");
      XSLTAttributeDef orderAttr = new XSLTAttributeDef((String)null, "order", false, true, false, 1, "ascending", 1, "descending", 2);
      orderAttr.setDefault("ascending");
      XSLTAttributeDef caseOrderAttr = new XSLTAttributeDef((String)null, "case-order", false, true, false, 1, "upper-first", 1, "lower-first", 2);
      XSLTAttributeDef terminateAttr = new XSLTAttributeDef((String)null, "terminate", 8, false, false, 1);
      terminateAttr.setDefault("no");
      XSLTAttributeDef xslExcludeResultPrefixesAttr = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "exclude-result-prefixes", 20, false, false, 1);
      XSLTAttributeDef xslExtensionElementPrefixesAttr = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "extension-element-prefixes", 15, false, false, 1);
      XSLTAttributeDef xslUseAttributeSetsAttr = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "use-attribute-sets", 10, false, false, 1);
      XSLTAttributeDef xslVersionAttr = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "version", 13, false, false, 1);
      XSLTElementDef charData = new XSLTElementDef(this, (String)null, "text()", (String)null, (XSLTElementDef[])null, (XSLTAttributeDef[])null, new ProcessorCharacters(), class$org$apache$xalan$templates$ElemTextLiteral == null ? (class$org$apache$xalan$templates$ElemTextLiteral = class$("org.apache.xalan.templates.ElemTextLiteral")) : class$org$apache$xalan$templates$ElemTextLiteral);
      charData.setType(2);
      XSLTElementDef whiteSpaceOnly = new XSLTElementDef(this, (String)null, "text()", (String)null, (XSLTElementDef[])null, (XSLTAttributeDef[])null, (XSLTElementProcessor)null, class$org$apache$xalan$templates$ElemTextLiteral == null ? (class$org$apache$xalan$templates$ElemTextLiteral = class$("org.apache.xalan.templates.ElemTextLiteral")) : class$org$apache$xalan$templates$ElemTextLiteral);
      charData.setType(2);
      XSLTAttributeDef resultAttr = new XSLTAttributeDef((String)null, "*", 3, false, true, 2);
      XSLTAttributeDef xslResultAttr = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "*", 1, false, false, 2);
      XSLTElementDef[] templateElements = new XSLTElementDef[23];
      XSLTElementDef[] templateElementsAndParams = new XSLTElementDef[24];
      XSLTElementDef[] templateElementsAndSort = new XSLTElementDef[24];
      XSLTElementDef[] exsltFunctionElements = new XSLTElementDef[24];
      XSLTElementDef[] charTemplateElements = new XSLTElementDef[15];
      XSLTElementDef resultElement = new XSLTElementDef(this, (String)null, "*", (String)null, templateElements, new XSLTAttributeDef[]{spaceAttrLiteral, xslExcludeResultPrefixesAttr, xslExtensionElementPrefixesAttr, xslUseAttributeSetsAttr, xslVersionAttr, xslResultAttr, resultAttr}, new ProcessorLRE(), class$org$apache$xalan$templates$ElemLiteralResult == null ? (class$org$apache$xalan$templates$ElemLiteralResult = class$("org.apache.xalan.templates.ElemLiteralResult")) : class$org$apache$xalan$templates$ElemLiteralResult, 20, true);
      XSLTElementDef unknownElement = new XSLTElementDef(this, "*", "unknown", (String)null, templateElementsAndParams, new XSLTAttributeDef[]{xslExcludeResultPrefixesAttr, xslExtensionElementPrefixesAttr, xslUseAttributeSetsAttr, xslVersionAttr, xslResultAttr, resultAttr}, new ProcessorUnknown(), class$org$apache$xalan$templates$ElemUnknown == null ? (class$org$apache$xalan$templates$ElemUnknown = class$("org.apache.xalan.templates.ElemUnknown")) : class$org$apache$xalan$templates$ElemUnknown, 20, true);
      XSLTElementDef xslValueOf = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "value-of", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{selectAttrRequired, disableOutputEscapingAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemValueOf == null ? (class$org$apache$xalan$templates$ElemValueOf = class$("org.apache.xalan.templates.ElemValueOf")) : class$org$apache$xalan$templates$ElemValueOf, 20, true);
      XSLTElementDef xslCopyOf = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "copy-of", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{selectAttrRequired}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemCopyOf == null ? (class$org$apache$xalan$templates$ElemCopyOf = class$("org.apache.xalan.templates.ElemCopyOf")) : class$org$apache$xalan$templates$ElemCopyOf, 20, true);
      XSLTElementDef xslNumber = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "number", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{levelAttr, countAttr, fromAttr, valueAttr, formatAttr, langAttr, letterValueAttr, groupingSeparatorAVT, groupingSizeAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemNumber == null ? (class$org$apache$xalan$templates$ElemNumber = class$("org.apache.xalan.templates.ElemNumber")) : class$org$apache$xalan$templates$ElemNumber, 20, true);
      XSLTElementDef xslSort = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "sort", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{selectAttrDefDot, langAttr, dataTypeAttr, orderAttr, caseOrderAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemSort == null ? (class$org$apache$xalan$templates$ElemSort = class$("org.apache.xalan.templates.ElemSort")) : class$org$apache$xalan$templates$ElemSort, 19, true);
      XSLTElementDef xslWithParam = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "with-param", (String)null, templateElements, new XSLTAttributeDef[]{nameAttrRequired, selectAttrOpt}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemWithParam == null ? (class$org$apache$xalan$templates$ElemWithParam = class$("org.apache.xalan.templates.ElemWithParam")) : class$org$apache$xalan$templates$ElemWithParam, 19, true);
      XSLTElementDef xslApplyTemplates = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "apply-templates", (String)null, new XSLTElementDef[]{xslSort, xslWithParam}, new XSLTAttributeDef[]{selectAttrDefNode, modeAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemApplyTemplates == null ? (class$org$apache$xalan$templates$ElemApplyTemplates = class$("org.apache.xalan.templates.ElemApplyTemplates")) : class$org$apache$xalan$templates$ElemApplyTemplates, 20, true);
      XSLTElementDef xslApplyImports = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "apply-imports", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[0], new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemApplyImport == null ? (class$org$apache$xalan$templates$ElemApplyImport = class$("org.apache.xalan.templates.ElemApplyImport")) : class$org$apache$xalan$templates$ElemApplyImport);
      XSLTElementDef xslForEach = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "for-each", (String)null, templateElementsAndSort, new XSLTAttributeDef[]{selectAttrRequired, spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemForEach == null ? (class$org$apache$xalan$templates$ElemForEach = class$("org.apache.xalan.templates.ElemForEach")) : class$org$apache$xalan$templates$ElemForEach, true, false, true, 20, true);
      XSLTElementDef xslIf = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "if", (String)null, templateElements, new XSLTAttributeDef[]{testAttrRequired, spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemIf == null ? (class$org$apache$xalan$templates$ElemIf = class$("org.apache.xalan.templates.ElemIf")) : class$org$apache$xalan$templates$ElemIf, 20, true);
      XSLTElementDef xslWhen = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "when", (String)null, templateElements, new XSLTAttributeDef[]{testAttrRequired, spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemWhen == null ? (class$org$apache$xalan$templates$ElemWhen = class$("org.apache.xalan.templates.ElemWhen")) : class$org$apache$xalan$templates$ElemWhen, false, true, 1, true);
      XSLTElementDef xslOtherwise = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "otherwise", (String)null, templateElements, new XSLTAttributeDef[]{spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemOtherwise == null ? (class$org$apache$xalan$templates$ElemOtherwise = class$("org.apache.xalan.templates.ElemOtherwise")) : class$org$apache$xalan$templates$ElemOtherwise, false, false, 2, false);
      XSLTElementDef xslChoose = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "choose", (String)null, new XSLTElementDef[]{xslWhen, xslOtherwise}, new XSLTAttributeDef[]{spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemChoose == null ? (class$org$apache$xalan$templates$ElemChoose = class$("org.apache.xalan.templates.ElemChoose")) : class$org$apache$xalan$templates$ElemChoose, true, false, true, 20, true);
      XSLTElementDef xslAttribute = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "attribute", (String)null, charTemplateElements, new XSLTAttributeDef[]{nameAVTRequired, namespaceAVTOpt, spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemAttribute == null ? (class$org$apache$xalan$templates$ElemAttribute = class$("org.apache.xalan.templates.ElemAttribute")) : class$org$apache$xalan$templates$ElemAttribute, 20, true);
      XSLTElementDef xslCallTemplate = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "call-template", (String)null, new XSLTElementDef[]{xslWithParam}, new XSLTAttributeDef[]{nameAttrRequired}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemCallTemplate == null ? (class$org$apache$xalan$templates$ElemCallTemplate = class$("org.apache.xalan.templates.ElemCallTemplate")) : class$org$apache$xalan$templates$ElemCallTemplate, 20, true);
      XSLTElementDef xslVariable = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "variable", (String)null, templateElements, new XSLTAttributeDef[]{nameAttrRequired, selectAttrOpt}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemVariable == null ? (class$org$apache$xalan$templates$ElemVariable = class$("org.apache.xalan.templates.ElemVariable")) : class$org$apache$xalan$templates$ElemVariable, 20, true);
      XSLTElementDef xslParam = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "param", (String)null, templateElements, new XSLTAttributeDef[]{nameAttrRequired, selectAttrOpt}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemParam == null ? (class$org$apache$xalan$templates$ElemParam = class$("org.apache.xalan.templates.ElemParam")) : class$org$apache$xalan$templates$ElemParam, 19, true);
      XSLTElementDef xslText = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "text", (String)null, new XSLTElementDef[]{charData}, new XSLTAttributeDef[]{disableOutputEscapingAttr}, new ProcessorText(), class$org$apache$xalan$templates$ElemText == null ? (class$org$apache$xalan$templates$ElemText = class$("org.apache.xalan.templates.ElemText")) : class$org$apache$xalan$templates$ElemText, 20, true);
      XSLTElementDef xslProcessingInstruction = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "processing-instruction", (String)null, charTemplateElements, new XSLTAttributeDef[]{nameAVT_NCNAMERequired, spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemPI == null ? (class$org$apache$xalan$templates$ElemPI = class$("org.apache.xalan.templates.ElemPI")) : class$org$apache$xalan$templates$ElemPI, 20, true);
      XSLTElementDef xslElement = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "element", (String)null, templateElements, new XSLTAttributeDef[]{nameAVTRequired, namespaceAVTOpt, useAttributeSetsAttr, spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemElement == null ? (class$org$apache$xalan$templates$ElemElement = class$("org.apache.xalan.templates.ElemElement")) : class$org$apache$xalan$templates$ElemElement, 20, true);
      XSLTElementDef xslComment = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "comment", (String)null, charTemplateElements, new XSLTAttributeDef[]{spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemComment == null ? (class$org$apache$xalan$templates$ElemComment = class$("org.apache.xalan.templates.ElemComment")) : class$org$apache$xalan$templates$ElemComment, 20, true);
      XSLTElementDef xslCopy = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "copy", (String)null, templateElements, new XSLTAttributeDef[]{spaceAttr, useAttributeSetsAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemCopy == null ? (class$org$apache$xalan$templates$ElemCopy = class$("org.apache.xalan.templates.ElemCopy")) : class$org$apache$xalan$templates$ElemCopy, 20, true);
      XSLTElementDef xslMessage = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "message", (String)null, templateElements, new XSLTAttributeDef[]{terminateAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemMessage == null ? (class$org$apache$xalan$templates$ElemMessage = class$("org.apache.xalan.templates.ElemMessage")) : class$org$apache$xalan$templates$ElemMessage, 20, true);
      XSLTElementDef xslFallback = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "fallback", (String)null, templateElements, new XSLTAttributeDef[]{spaceAttr}, new ProcessorTemplateElem(), class$org$apache$xalan$templates$ElemFallback == null ? (class$org$apache$xalan$templates$ElemFallback = class$("org.apache.xalan.templates.ElemFallback")) : class$org$apache$xalan$templates$ElemFallback, 20, true);
      XSLTElementDef exsltFunction = new XSLTElementDef(this, "http://exslt.org/functions", "function", (String)null, exsltFunctionElements, new XSLTAttributeDef[]{nameAttrRequired}, new ProcessorExsltFunction(), class$org$apache$xalan$templates$ElemExsltFunction == null ? (class$org$apache$xalan$templates$ElemExsltFunction = class$("org.apache.xalan.templates.ElemExsltFunction")) : class$org$apache$xalan$templates$ElemExsltFunction);
      XSLTElementDef exsltResult = new XSLTElementDef(this, "http://exslt.org/functions", "result", (String)null, templateElements, new XSLTAttributeDef[]{selectAttrOpt}, new ProcessorExsltFuncResult(), class$org$apache$xalan$templates$ElemExsltFuncResult == null ? (class$org$apache$xalan$templates$ElemExsltFuncResult = class$("org.apache.xalan.templates.ElemExsltFuncResult")) : class$org$apache$xalan$templates$ElemExsltFuncResult);
      int i = 0;
      templateElements[i++] = charData;
      templateElements[i++] = xslApplyTemplates;
      templateElements[i++] = xslCallTemplate;
      templateElements[i++] = xslApplyImports;
      templateElements[i++] = xslForEach;
      templateElements[i++] = xslValueOf;
      templateElements[i++] = xslCopyOf;
      templateElements[i++] = xslNumber;
      templateElements[i++] = xslChoose;
      templateElements[i++] = xslIf;
      templateElements[i++] = xslText;
      templateElements[i++] = xslCopy;
      templateElements[i++] = xslVariable;
      templateElements[i++] = xslMessage;
      templateElements[i++] = xslFallback;
      templateElements[i++] = xslProcessingInstruction;
      templateElements[i++] = xslComment;
      templateElements[i++] = xslElement;
      templateElements[i++] = xslAttribute;
      templateElements[i++] = resultElement;
      templateElements[i++] = unknownElement;
      templateElements[i++] = exsltFunction;
      templateElements[i++] = exsltResult;

      int k;
      for(k = 0; k < i; ++k) {
         templateElementsAndParams[k] = templateElements[k];
         templateElementsAndSort[k] = templateElements[k];
         exsltFunctionElements[k] = templateElements[k];
      }

      templateElementsAndParams[k] = xslParam;
      templateElementsAndSort[k] = xslSort;
      exsltFunctionElements[k] = xslParam;
      int i = 0;
      i = i + 1;
      charTemplateElements[i] = charData;
      charTemplateElements[i++] = xslApplyTemplates;
      charTemplateElements[i++] = xslCallTemplate;
      charTemplateElements[i++] = xslApplyImports;
      charTemplateElements[i++] = xslForEach;
      charTemplateElements[i++] = xslValueOf;
      charTemplateElements[i++] = xslCopyOf;
      charTemplateElements[i++] = xslNumber;
      charTemplateElements[i++] = xslChoose;
      charTemplateElements[i++] = xslIf;
      charTemplateElements[i++] = xslText;
      charTemplateElements[i++] = xslCopy;
      charTemplateElements[i++] = xslVariable;
      charTemplateElements[i++] = xslMessage;
      charTemplateElements[i++] = xslFallback;
      XSLTElementDef importDef = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "import", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{hrefAttr}, new ProcessorImport(), (Class)null, 1, true);
      XSLTElementDef includeDef = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "include", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{hrefAttr}, new ProcessorInclude(), (Class)null, 20, true);
      XSLTAttributeDef[] scriptAttrs = new XSLTAttributeDef[]{new XSLTAttributeDef((String)null, "lang", 13, true, false, 2), new XSLTAttributeDef((String)null, "src", 2, false, false, 2)};
      XSLTAttributeDef[] componentAttrs = new XSLTAttributeDef[]{new XSLTAttributeDef((String)null, "prefix", 13, true, false, 2), new XSLTAttributeDef((String)null, "elements", 14, false, false, 2), new XSLTAttributeDef((String)null, "functions", 14, false, false, 2)};
      XSLTElementDef[] topLevelElements = new XSLTElementDef[]{includeDef, importDef, whiteSpaceOnly, unknownElement, new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "strip-space", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{elementsAttr}, new ProcessorStripSpace(), (Class)null, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "preserve-space", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{elementsAttr}, new ProcessorPreserveSpace(), (Class)null, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "output", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{methodAttr, versionAttr, encodingAttr, omitXmlDeclarationAttr, standaloneAttr, doctypePublicAttr, doctypeSystemAttr, cdataSectionElementsAttr, indentAttr, mediaTypeAttr, XSLTAttributeDef.m_foreignAttr}, new ProcessorOutputElem(), (Class)null, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "key", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{nameAttrRequired, matchAttrRequired, useAttr}, new ProcessorKey(), (Class)null, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "decimal-format", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{nameAttrOpt_ERROR, decimalSeparatorAttr, groupingSeparatorAttr, infinityAttr, minusSignAttr, NaNAttr, percentAttr, perMilleAttr, zeroDigitAttr, digitAttr, patternSeparatorAttr}, new ProcessorDecimalFormat(), (Class)null, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "attribute-set", (String)null, new XSLTElementDef[]{xslAttribute}, new XSLTAttributeDef[]{nameAttrRequired, useAttributeSetsAttr}, new ProcessorAttributeSet(), (Class)null, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "variable", (String)null, templateElements, new XSLTAttributeDef[]{nameAttrRequired, selectAttrOpt}, new ProcessorGlobalVariableDecl(), class$org$apache$xalan$templates$ElemVariable == null ? (class$org$apache$xalan$templates$ElemVariable = class$("org.apache.xalan.templates.ElemVariable")) : class$org$apache$xalan$templates$ElemVariable, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "param", (String)null, templateElements, new XSLTAttributeDef[]{nameAttrRequired, selectAttrOpt}, new ProcessorGlobalParamDecl(), class$org$apache$xalan$templates$ElemParam == null ? (class$org$apache$xalan$templates$ElemParam = class$("org.apache.xalan.templates.ElemParam")) : class$org$apache$xalan$templates$ElemParam, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "template", (String)null, templateElementsAndParams, new XSLTAttributeDef[]{matchAttrOpt, nameAttrOpt_ERROR, priorityAttr, modeAttr, spaceAttr}, new ProcessorTemplate(), class$org$apache$xalan$templates$ElemTemplate == null ? (class$org$apache$xalan$templates$ElemTemplate = class$("org.apache.xalan.templates.ElemTemplate")) : class$org$apache$xalan$templates$ElemTemplate, true, 20, true), new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "namespace-alias", (String)null, (XSLTElementDef[])null, new XSLTAttributeDef[]{stylesheetPrefixAttr, resultPrefixAttr}, new ProcessorNamespaceAlias(), (Class)null, 20, true), new XSLTElementDef(this, "http://xml.apache.org/xalan", "component", (String)null, new XSLTElementDef[]{new XSLTElementDef(this, "http://xml.apache.org/xalan", "script", (String)null, new XSLTElementDef[]{charData}, scriptAttrs, new ProcessorLRE(), class$org$apache$xalan$templates$ElemExtensionScript == null ? (class$org$apache$xalan$templates$ElemExtensionScript = class$("org.apache.xalan.templates.ElemExtensionScript")) : class$org$apache$xalan$templates$ElemExtensionScript, 20, true)}, componentAttrs, new ProcessorLRE(), class$org$apache$xalan$templates$ElemExtensionDecl == null ? (class$org$apache$xalan$templates$ElemExtensionDecl = class$("org.apache.xalan.templates.ElemExtensionDecl")) : class$org$apache$xalan$templates$ElemExtensionDecl), new XSLTElementDef(this, "http://xml.apache.org/xslt", "component", (String)null, new XSLTElementDef[]{new XSLTElementDef(this, "http://xml.apache.org/xslt", "script", (String)null, new XSLTElementDef[]{charData}, scriptAttrs, new ProcessorLRE(), class$org$apache$xalan$templates$ElemExtensionScript == null ? (class$org$apache$xalan$templates$ElemExtensionScript = class$("org.apache.xalan.templates.ElemExtensionScript")) : class$org$apache$xalan$templates$ElemExtensionScript, 20, true)}, componentAttrs, new ProcessorLRE(), class$org$apache$xalan$templates$ElemExtensionDecl == null ? (class$org$apache$xalan$templates$ElemExtensionDecl = class$("org.apache.xalan.templates.ElemExtensionDecl")) : class$org$apache$xalan$templates$ElemExtensionDecl), exsltFunction};
      XSLTAttributeDef excludeResultPrefixesAttr = new XSLTAttributeDef((String)null, "exclude-result-prefixes", 20, false, false, 2);
      XSLTAttributeDef extensionElementPrefixesAttr = new XSLTAttributeDef((String)null, "extension-element-prefixes", 15, false, false, 2);
      XSLTAttributeDef idAttr = new XSLTAttributeDef((String)null, "id", 1, false, false, 2);
      XSLTAttributeDef versionAttrRequired = new XSLTAttributeDef((String)null, "version", 13, true, false, 2);
      XSLTElementDef stylesheetElemDef = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "stylesheet", "transform", topLevelElements, new XSLTAttributeDef[]{extensionElementPrefixesAttr, excludeResultPrefixesAttr, idAttr, versionAttrRequired, spaceAttr}, new ProcessorStylesheetElement(), (Class)null, true, -1, false);
      importDef.setElements(new XSLTElementDef[]{stylesheetElemDef, resultElement, unknownElement});
      includeDef.setElements(new XSLTElementDef[]{stylesheetElemDef, resultElement, unknownElement});
      this.build((String)null, (String)null, (String)null, new XSLTElementDef[]{stylesheetElemDef, whiteSpaceOnly, resultElement, unknownElement}, (XSLTAttributeDef[])null, new ProcessorStylesheetDoc(), (Class)null);
   }

   public Hashtable getElemsAvailable() {
      return this.m_availElems;
   }

   void addAvailableElement(QName elemName) {
      this.m_availElems.put(elemName, elemName);
   }

   public boolean elementAvailable(QName elemName) {
      return this.m_availElems.containsKey(elemName);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
