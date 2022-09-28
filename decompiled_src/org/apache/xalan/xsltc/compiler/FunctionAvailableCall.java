package org.apache.xalan.xsltc.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class FunctionAvailableCall extends FunctionCall {
   private Expression _arg;
   private String _nameOfFunct = null;
   private String _namespaceOfFunct = null;
   private boolean _isFunctionAvailable = false;

   public FunctionAvailableCall(QName fname, Vector arguments) {
      super(fname, arguments);
      this._arg = (Expression)arguments.elementAt(0);
      super._type = null;
      if (this._arg instanceof LiteralExpr) {
         LiteralExpr arg = (LiteralExpr)this._arg;
         this._namespaceOfFunct = arg.getNamespace();
         this._nameOfFunct = arg.getValue();
         if (!this.isInternalNamespace()) {
            this._isFunctionAvailable = this.hasMethods();
         }
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (super._type != null) {
         return super._type;
      } else if (this._arg instanceof LiteralExpr) {
         return super._type = Type.Boolean;
      } else {
         ErrorMsg err = new ErrorMsg("NEED_LITERAL_ERR", "function-available", this);
         throw new TypeCheckError(err);
      }
   }

   public Object evaluateAtCompileTime() {
      return this.getResult() ? Boolean.TRUE : Boolean.FALSE;
   }

   private boolean hasMethods() {
      LiteralExpr arg = (LiteralExpr)this._arg;
      String className = this.getClassNameFromUri(this._namespaceOfFunct);
      String methodName = null;
      int colonIndex = this._nameOfFunct.indexOf(":");
      if (colonIndex > 0) {
         String functionName = this._nameOfFunct.substring(colonIndex + 1);
         int lastDotIndex = functionName.lastIndexOf(46);
         if (lastDotIndex > 0) {
            methodName = functionName.substring(lastDotIndex + 1);
            if (className != null && !className.equals("")) {
               className = className + "." + functionName.substring(0, lastDotIndex);
            } else {
               className = functionName.substring(0, lastDotIndex);
            }
         } else {
            methodName = functionName;
         }
      } else {
         methodName = this._nameOfFunct;
      }

      if (className != null && methodName != null) {
         if (methodName.indexOf(45) > 0) {
            methodName = FunctionCall.replaceDash(methodName);
         }

         try {
            Class clazz = ObjectFactory.findProviderClass(className, ObjectFactory.findClassLoader(), true);
            if (clazz == null) {
               return false;
            } else {
               Method[] methods = clazz.getMethods();

               for(int i = 0; i < methods.length; ++i) {
                  int mods = methods[i].getModifiers();
                  if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && methods[i].getName().equals(methodName)) {
                     return true;
                  }
               }

               return false;
            }
         } catch (ClassNotFoundException var9) {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean getResult() {
      if (this._nameOfFunct == null) {
         return false;
      } else {
         if (this.isInternalNamespace()) {
            Parser parser = this.getParser();
            this._isFunctionAvailable = parser.functionSupported(Util.getLocalName(this._nameOfFunct));
         }

         return this._isFunctionAvailable;
      }
   }

   private boolean isInternalNamespace() {
      return this._namespaceOfFunct == null || this._namespaceOfFunct.equals("") || this._namespaceOfFunct.equals("http://xml.apache.org/xalan/xsltc");
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      methodGen.getInstructionList().append((CompoundInstruction)(new PUSH(cpg, this.getResult())));
   }
}
