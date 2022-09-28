package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DCONST;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Variable extends VariableBase {
   public int getIndex() {
      return super._local != null ? super._local.getIndex() : -1;
   }

   public void parseContents(Parser parser) {
      super.parseContents(parser);
      SyntaxTreeNode parent = this.getParent();
      if (parent instanceof Stylesheet) {
         super._isLocal = false;
         Variable var = parser.getSymbolTable().lookupVariable(super._name);
         if (var != null) {
            int us = this.getImportPrecedence();
            int them = var.getImportPrecedence();
            if (us == them) {
               String name = super._name.toString();
               this.reportError(this, parser, "VARIABLE_REDEF_ERR", name);
            } else {
               if (them > us) {
                  super._ignore = true;
                  return;
               }

               var.disable();
            }
         }

         ((Stylesheet)parent).addVariable(this);
         parser.getSymbolTable().addVariable(this);
      } else {
         super._isLocal = true;
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (super._select != null) {
         super._type = super._select.typeCheck(stable);
      } else if (this.hasContents()) {
         this.typeCheckContents(stable);
         super._type = Type.ResultTree;
      } else {
         super._type = Type.Reference;
      }

      return Type.Void;
   }

   public void initialize(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (this.isLocal() && !super._refs.isEmpty()) {
         if (super._local == null) {
            super._local = methodGen.addLocalVariable2(this.getEscapedName(), super._type.toJCType(), il.getEnd());
         }

         if (!(super._type instanceof IntType) && !(super._type instanceof NodeType) && !(super._type instanceof BooleanType)) {
            if (super._type instanceof RealType) {
               il.append((org.apache.bcel.generic.Instruction)(new DCONST(0.0)));
            } else {
               il.append((org.apache.bcel.generic.Instruction)(new ACONST_NULL()));
            }
         } else {
            il.append((org.apache.bcel.generic.Instruction)(new ICONST(0)));
         }

         il.append(super._type.STORE(super._local.getIndex()));
      }

   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (super._refs.isEmpty()) {
         super._ignore = true;
      }

      if (!super._ignore) {
         super._ignore = true;
         String name = this.getEscapedName();
         if (this.isLocal()) {
            this.translateValue(classGen, methodGen);
            if (super._local == null) {
               this.mapRegister(methodGen);
            }

            il.append(super._type.STORE(super._local.getIndex()));
         } else {
            String signature = super._type.toSignature();
            if (classGen.containsField(name) == null) {
               classGen.addField(new Field(1, cpg.addUtf8(name), cpg.addUtf8(signature), (org.apache.bcel.classfile.Attribute[])null, cpg.getConstantPool()));
               il.append(classGen.loadTranslet());
               this.translateValue(classGen, methodGen);
               il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(cpg.addFieldref(classGen.getClassName(), name, signature))));
            }
         }

      }
   }
}
