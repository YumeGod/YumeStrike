package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.serializer.ElemDesc;
import org.apache.xml.utils.XML11Char;

final class XslAttribute extends Instruction {
   private String _prefix;
   private AttributeValue _name;
   private AttributeValueTemplate _namespace = null;
   private boolean _ignore = false;
   private boolean _isLiteral = false;

   public AttributeValue getName() {
      return this._name;
   }

   public void display(int indent) {
      this.indent(indent);
      Util.println("Attribute " + this._name);
      this.displayContents(indent + 4);
   }

   public void parseContents(Parser parser) {
      boolean generated = false;
      SymbolTable stable = parser.getSymbolTable();
      String name = this.getAttribute("name");
      String namespace = this.getAttribute("namespace");
      QName qname = parser.getQName(name, false);
      String prefix = qname.getPrefix();
      if ((prefix == null || !prefix.equals("xmlns")) && !name.equals("xmlns")) {
         this._isLiteral = Util.isLiteral(name);
         if (this._isLiteral && !XML11Char.isXML11ValidQName(name)) {
            this.reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", name);
         } else {
            SyntaxTreeNode parent = this.getParent();
            Vector siblings = parent.getContents();

            for(int i = 0; i < parent.elementCount(); ++i) {
               SyntaxTreeNode item = (SyntaxTreeNode)siblings.elementAt(i);
               if (item == this) {
                  break;
               }

               if (!(item instanceof XslAttribute) && !(item instanceof UseAttributeSets) && !(item instanceof LiteralAttribute) && !(item instanceof Text) && !(item instanceof If) && !(item instanceof Choose) && !(item instanceof CopyOf) && !(item instanceof VariableBase)) {
                  this.reportWarning(this, parser, "STRAY_ATTRIBUTE_ERR", name);
               }
            }

            if (namespace != null && namespace != "") {
               this._prefix = this.lookupPrefix(namespace);
               this._namespace = new AttributeValueTemplate(namespace, parser, this);
            } else if (prefix != null && prefix != "") {
               this._prefix = prefix;
               namespace = this.lookupNamespace(prefix);
               if (namespace != null) {
                  this._namespace = new AttributeValueTemplate(namespace, parser, this);
               }
            }

            if (this._namespace != null) {
               if (this._prefix != null && this._prefix != "") {
                  if (prefix != null && !prefix.equals(this._prefix)) {
                     this._prefix = prefix;
                  }
               } else if (prefix != null) {
                  this._prefix = prefix;
               } else {
                  this._prefix = stable.generateNamespacePrefix();
                  generated = true;
               }

               name = this._prefix + ":" + qname.getLocalPart();
               if (parent instanceof LiteralElement && !generated) {
                  ((LiteralElement)parent).registerNamespace(this._prefix, namespace, stable, false);
               }
            }

            if (parent instanceof LiteralElement) {
               ((LiteralElement)parent).addAttribute(this);
            }

            this._name = AttributeValue.create(this, name, parser);
            this.parseChildren(parser);
         }
      } else {
         this.reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", name);
      }
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (!this._ignore) {
         this._name.typeCheck(stable);
         if (this._namespace != null) {
            this._namespace.typeCheck(stable);
         }

         this.typeCheckContents(stable);
      }

      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (!this._ignore) {
         this._ignore = true;
         if (this._namespace != null) {
            il.append(methodGen.loadHandler());
            il.append((CompoundInstruction)(new PUSH(cpg, this._prefix)));
            this._namespace.translate(classGen, methodGen);
            il.append(methodGen.namespace());
         }

         int flags;
         if (!this._isLiteral) {
            LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), il.getEnd());
            this._name.translate(classGen, methodGen);
            il.append((org.apache.bcel.generic.Instruction)(new ASTORE(nameValue.getIndex())));
            il.append((org.apache.bcel.generic.Instruction)(new ALOAD(nameValue.getIndex())));
            flags = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkAttribQName", "(Ljava/lang/String;)V");
            il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(flags)));
            il.append(methodGen.loadHandler());
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((org.apache.bcel.generic.Instruction)(new ALOAD(nameValue.getIndex())));
         } else {
            il.append(methodGen.loadHandler());
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            this._name.translate(classGen, methodGen);
         }

         if (this.elementCount() == 1 && this.elementAt(0) instanceof Text) {
            il.append((CompoundInstruction)(new PUSH(cpg, ((Text)this.elementAt(0)).getText())));
         } else {
            il.append(classGen.loadTranslet());
            il.append((org.apache.bcel.generic.Instruction)(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;"))));
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append(methodGen.storeHandler());
            this.translateContents(classGen, methodGen);
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;"))));
         }

         SyntaxTreeNode parent = this.getParent();
         if (parent instanceof LiteralElement && ((LiteralElement)parent).allAttributesUnique()) {
            flags = 0;
            ElemDesc elemDesc = ((LiteralElement)parent).getElemDesc();
            if (elemDesc != null && this._name instanceof SimpleAttributeValue) {
               String attrName = ((SimpleAttributeValue)this._name).toString();
               if (elemDesc.isAttrFlagSet(attrName, 4)) {
                  flags |= 2;
               } else if (elemDesc.isAttrFlagSet(attrName, 2)) {
                  flags |= 4;
               }
            }

            il.append((CompoundInstruction)(new PUSH(cpg, flags)));
            il.append(methodGen.uniqueAttribute());
         } else {
            il.append(methodGen.attribute());
         }

         il.append(methodGen.storeHandler());
      }
   }
}
