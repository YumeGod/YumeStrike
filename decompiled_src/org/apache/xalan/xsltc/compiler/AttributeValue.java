package org.apache.xalan.xsltc.compiler;

abstract class AttributeValue extends Expression {
   public static final AttributeValue create(SyntaxTreeNode parent, String text, Parser parser) {
      Object result;
      if (text.indexOf(123) != -1) {
         result = new AttributeValueTemplate(text, parser, parent);
      } else if (text.indexOf(125) != -1) {
         result = new AttributeValueTemplate(text, parser, parent);
      } else {
         result = new SimpleAttributeValue(text);
         ((SyntaxTreeNode)result).setParser(parser);
         ((SyntaxTreeNode)result).setParent(parent);
      }

      return (AttributeValue)result;
   }
}
