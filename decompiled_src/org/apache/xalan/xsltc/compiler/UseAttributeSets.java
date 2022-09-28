package org.apache.xalan.xsltc.compiler;

import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class UseAttributeSets extends Instruction {
   private static final String ATTR_SET_NOT_FOUND = "";
   private final Vector _sets = new Vector(2);

   public UseAttributeSets(String setNames, Parser parser) {
      this.setParser(parser);
      this.addAttributeSets(setNames);
   }

   public void addAttributeSets(String setNames) {
      if (setNames != null && !setNames.equals("")) {
         StringTokenizer tokens = new StringTokenizer(setNames);

         while(tokens.hasMoreTokens()) {
            QName qname = this.getParser().getQNameIgnoreDefaultNs(tokens.nextToken());
            this._sets.add(qname);
         }
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      SymbolTable symbolTable = this.getParser().getSymbolTable();

      for(int i = 0; i < this._sets.size(); ++i) {
         QName name = (QName)this._sets.elementAt(i);
         AttributeSet attrs = symbolTable.lookupAttributeSet(name);
         if (attrs != null) {
            String methodName = attrs.getMethodName();
            il.append(classGen.loadTranslet());
            il.append(methodGen.loadDOM());
            il.append(methodGen.loadIterator());
            il.append(methodGen.loadHandler());
            int method = cpg.addMethodref(classGen.getClassName(), methodName, "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/serializer/SerializationHandler;)V");
            il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(method)));
         } else {
            Parser parser = this.getParser();
            String atrs = name.toString();
            this.reportError(this, parser, "ATTRIBSET_UNDEF_ERR", atrs);
         }
      }

   }
}
