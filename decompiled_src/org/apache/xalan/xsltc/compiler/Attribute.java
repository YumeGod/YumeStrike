package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Util;

final class Attribute extends Instruction {
   private QName _name;

   public void display(int indent) {
      this.indent(indent);
      Util.println("Attribute " + this._name);
      this.displayContents(indent + 4);
   }

   public void parseContents(Parser parser) {
      this._name = parser.getQName(this.getAttribute("name"));
      this.parseChildren(parser);
   }
}
