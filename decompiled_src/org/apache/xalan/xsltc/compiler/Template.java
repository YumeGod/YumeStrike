package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NamedMethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

public final class Template extends TopLevelElement {
   private QName _name;
   private QName _mode;
   private Pattern _pattern;
   private double _priority;
   private int _position;
   private boolean _disabled = false;
   private boolean _compiled = false;
   private boolean _simplified = false;
   private boolean _isSimpleNamedTemplate = false;
   private Vector _parameters = new Vector();
   private Stylesheet _stylesheet = null;

   public boolean hasParams() {
      return this._parameters.size() > 0;
   }

   public boolean isSimplified() {
      return this._simplified;
   }

   public void setSimplified() {
      this._simplified = true;
   }

   public boolean isSimpleNamedTemplate() {
      return this._isSimpleNamedTemplate;
   }

   public void addParameter(Param param) {
      this._parameters.addElement(param);
   }

   public Vector getParameters() {
      return this._parameters;
   }

   public void disable() {
      this._disabled = true;
   }

   public boolean disabled() {
      return this._disabled;
   }

   public double getPriority() {
      return this._priority;
   }

   public int getPosition() {
      return this._position;
   }

   public boolean isNamed() {
      return this._name != null;
   }

   public Pattern getPattern() {
      return this._pattern;
   }

   public QName getName() {
      return this._name;
   }

   public void setName(QName qname) {
      if (this._name == null) {
         this._name = qname;
      }

   }

   public QName getModeName() {
      return this._mode;
   }

   public int compareTo(Object template) {
      Template other = (Template)template;
      if (this._priority > other._priority) {
         return 1;
      } else if (this._priority < other._priority) {
         return -1;
      } else if (this._position > other._position) {
         return 1;
      } else {
         return this._position < other._position ? -1 : 0;
      }
   }

   public void display(int indent) {
      Util.println('\n');
      this.indent(indent);
      if (this._name != null) {
         this.indent(indent);
         Util.println("name = " + this._name);
      } else if (this._pattern != null) {
         this.indent(indent);
         Util.println("match = " + this._pattern.toString());
      }

      if (this._mode != null) {
         this.indent(indent);
         Util.println("mode = " + this._mode);
      }

      this.displayContents(indent + 4);
   }

   private boolean resolveNamedTemplates(Template other, Parser parser) {
      if (other == null) {
         return true;
      } else {
         SymbolTable stable = parser.getSymbolTable();
         int us = this.getImportPrecedence();
         int them = other.getImportPrecedence();
         if (us > them) {
            other.disable();
            return true;
         } else if (us < them) {
            stable.addTemplate(other);
            this.disable();
            return true;
         } else {
            return false;
         }
      }
   }

   public Stylesheet getStylesheet() {
      return this._stylesheet;
   }

   public void parseContents(Parser parser) {
      String name = this.getAttribute("name");
      String mode = this.getAttribute("mode");
      String match = this.getAttribute("match");
      String priority = this.getAttribute("priority");
      this._stylesheet = super.getStylesheet();
      ErrorMsg err;
      if (name.length() > 0) {
         if (!XML11Char.isXML11ValidQName(name)) {
            err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
            parser.reportError(3, err);
         }

         this._name = parser.getQNameIgnoreDefaultNs(name);
      }

      if (mode.length() > 0) {
         if (!XML11Char.isXML11ValidQName(mode)) {
            err = new ErrorMsg("INVALID_QNAME_ERR", mode, this);
            parser.reportError(3, err);
         }

         this._mode = parser.getQNameIgnoreDefaultNs(mode);
      }

      if (match.length() > 0) {
         this._pattern = parser.parsePattern(this, "match", (String)null);
      }

      if (priority.length() > 0) {
         this._priority = Double.parseDouble(priority);
      } else if (this._pattern != null) {
         this._priority = this._pattern.getPriority();
      } else {
         this._priority = Double.NaN;
      }

      this._position = parser.getTemplateIndex();
      if (this._name != null) {
         Template other = parser.getSymbolTable().addTemplate(this);
         if (!this.resolveNamedTemplates(other, parser)) {
            ErrorMsg err = new ErrorMsg("TEMPLATE_REDEF_ERR", this._name, this);
            parser.reportError(3, err);
         }

         if (this._pattern == null && this._mode == null) {
            this._isSimpleNamedTemplate = true;
         }
      }

      if (super._parent instanceof Stylesheet) {
         ((Stylesheet)super._parent).addTemplate(this);
      }

      parser.setTemplate(this);
      this.parseChildren(parser);
      parser.setTemplate((Template)null);
   }

   public void parseSimplified(Stylesheet stylesheet, Parser parser) {
      this._stylesheet = stylesheet;
      this.setParent(stylesheet);
      this._name = null;
      this._mode = null;
      this._priority = Double.NaN;
      this._pattern = parser.parsePattern(this, "/");
      Vector contents = this._stylesheet.getContents();
      SyntaxTreeNode root = (SyntaxTreeNode)contents.elementAt(0);
      if (root instanceof LiteralElement) {
         this.addElement(root);
         root.setParent(this);
         contents.set(0, this);
         parser.setTemplate(this);
         root.parseContents(parser);
         parser.setTemplate((Template)null);
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (this._pattern != null) {
         this._pattern.typeCheck(stable);
      }

      return this.typeCheckContents(stable);
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (!this._disabled) {
         String className = classGen.getClassName();
         if (this._compiled && this.isNamed()) {
            String methodName = Util.escape(this._name.toString());
            il.append(classGen.loadTranslet());
            il.append(methodGen.loadDOM());
            il.append(methodGen.loadIterator());
            il.append(methodGen.loadHandler());
            il.append(methodGen.loadCurrentNode());
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref(className, methodName, "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/serializer/SerializationHandler;I)V"))));
         } else if (!this._compiled) {
            this._compiled = true;
            if (this._isSimpleNamedTemplate && methodGen instanceof NamedMethodGenerator) {
               int numParams = this._parameters.size();
               NamedMethodGenerator namedMethodGen = (NamedMethodGenerator)methodGen;

               for(int i = 0; i < numParams; ++i) {
                  Param param = (Param)this._parameters.elementAt(i);
                  param.setLoadInstruction(namedMethodGen.loadParameter(i));
                  param.setStoreInstruction(namedMethodGen.storeParameter(i));
               }
            }

            this.translateContents(classGen, methodGen);
            il.setPositions(true);
         }
      }
   }
}
