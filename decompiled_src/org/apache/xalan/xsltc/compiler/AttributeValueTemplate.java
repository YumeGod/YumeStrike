package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class AttributeValueTemplate extends AttributeValue {
   static final int OUT_EXPR = 0;
   static final int IN_EXPR = 1;
   static final int IN_EXPR_SQUOTES = 2;
   static final int IN_EXPR_DQUOTES = 3;
   static final String DELIMITER = "\ufffe";

   public AttributeValueTemplate(String value, Parser parser, SyntaxTreeNode parent) {
      this.setParent(parent);
      this.setParser(parser);

      try {
         this.parseAVTemplate(value, parser);
      } catch (NoSuchElementException var5) {
         this.reportError(parent, parser, "ATTR_VAL_TEMPLATE_ERR", value);
      }

   }

   private void parseAVTemplate(String text, Parser parser) {
      StringTokenizer tokenizer = new StringTokenizer(text, "{}\"'", true);
      String t = null;
      String lookahead = null;
      StringBuffer buffer = new StringBuffer();
      int state = 0;

      while(tokenizer.hasMoreTokens()) {
         if (lookahead != null) {
            t = lookahead;
            lookahead = null;
         } else {
            t = tokenizer.nextToken();
         }

         if (t.length() == 1) {
            switch (t.charAt(0)) {
               case '"':
                  switch (state) {
                     case 0:
                     case 2:
                     default:
                        break;
                     case 1:
                        state = 3;
                        break;
                     case 3:
                        state = 1;
                  }

                  buffer.append(t);
                  break;
               case '\'':
                  switch (state) {
                     case 0:
                     case 3:
                     default:
                        break;
                     case 1:
                        state = 2;
                        break;
                     case 2:
                        state = 1;
                  }

                  buffer.append(t);
                  break;
               case '{':
                  switch (state) {
                     case 0:
                        lookahead = tokenizer.nextToken();
                        if (lookahead.equals("{")) {
                           buffer.append(lookahead);
                           lookahead = null;
                        } else {
                           buffer.append("\ufffe");
                           state = 1;
                        }
                        continue;
                     case 1:
                     case 2:
                     case 3:
                        this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
                     default:
                        continue;
                  }
               case '}':
                  switch (state) {
                     case 0:
                        lookahead = tokenizer.nextToken();
                        if (lookahead.equals("}")) {
                           buffer.append(lookahead);
                           lookahead = null;
                        } else {
                           this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
                        }
                        continue;
                     case 1:
                        buffer.append("\ufffe");
                        state = 0;
                        continue;
                     case 2:
                     case 3:
                        buffer.append(t);
                     default:
                        continue;
                  }
               default:
                  buffer.append(t);
            }
         } else {
            buffer.append(t);
         }
      }

      if (state != 0) {
         this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
      }

      tokenizer = new StringTokenizer(buffer.toString(), "\ufffe", true);

      while(tokenizer.hasMoreTokens()) {
         t = tokenizer.nextToken();
         if (t.equals("\ufffe")) {
            this.addElement(parser.parseExpression(this, tokenizer.nextToken()));
            tokenizer.nextToken();
         } else {
            this.addElement(new LiteralExpr(t));
         }
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Vector contents = this.getContents();
      int n = contents.size();

      for(int i = 0; i < n; ++i) {
         Expression exp = (Expression)contents.elementAt(i);
         if (!exp.typeCheck(stable).identicalTo(Type.String)) {
            contents.setElementAt(new CastExpr(exp, Type.String), i);
         }
      }

      return super._type = Type.String;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("AVT:[");
      int count = this.elementCount();

      for(int i = 0; i < count; ++i) {
         buffer.append(this.elementAt(i).toString());
         if (i < count - 1) {
            buffer.append(' ');
         }
      }

      return buffer.append(']').toString();
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      if (this.elementCount() == 1) {
         Expression exp = (Expression)this.elementAt(0);
         exp.translate(classGen, methodGen);
      } else {
         ConstantPoolGen cpg = classGen.getConstantPool();
         InstructionList il = methodGen.getInstructionList();
         int initBuffer = cpg.addMethodref("java.lang.StringBuffer", "<init>", "()V");
         org.apache.bcel.generic.Instruction append = new INVOKEVIRTUAL(cpg.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
         int toString = cpg.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
         il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("java.lang.StringBuffer"))));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(initBuffer)));
         Enumeration elements = this.elements();

         while(elements.hasMoreElements()) {
            Expression exp = (Expression)elements.nextElement();
            exp.translate(classGen, methodGen);
            il.append((org.apache.bcel.generic.Instruction)append);
         }

         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(toString)));
      }

   }
}
