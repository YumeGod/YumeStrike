package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class UnresolvedRef extends VariableRefBase {
   private QName _variableName = null;
   private VariableRefBase _ref = null;
   private VariableBase _var = null;
   private Stylesheet _sheet = null;

   public UnresolvedRef(QName name) {
      this._variableName = name;
      this._sheet = this.getStylesheet();
   }

   public QName getName() {
      return this._variableName;
   }

   private ErrorMsg reportError() {
      ErrorMsg err = new ErrorMsg("VARIABLE_UNDEF_ERR", this._variableName, this);
      this.getParser().reportError(3, err);
      return err;
   }

   private VariableRefBase resolve(Parser parser, SymbolTable stable) {
      VariableBase ref = parser.lookupVariable(this._variableName);
      if (ref == null) {
         ref = (VariableBase)stable.lookupName(this._variableName);
      }

      if (ref == null) {
         this.reportError();
         return null;
      } else {
         if ((this._var = this.findParentVariable()) != null) {
            this._var.addDependency(ref);
         }

         if (ref instanceof Variable) {
            return new VariableRef((Variable)ref);
         } else {
            return ref instanceof Param ? new ParameterRef((Param)ref) : null;
         }
      }
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (this._ref != null) {
         String name = this._variableName.toString();
         new ErrorMsg("CIRCULAR_VARIABLE_ERR", name, this);
      }

      if ((this._ref = this.resolve(this.getParser(), stable)) != null) {
         return super._type = this._ref.typeCheck(stable);
      } else {
         throw new TypeCheckError(this.reportError());
      }
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      if (this._ref != null) {
         this._ref.translate(classGen, methodGen);
      } else {
         this.reportError();
      }

   }

   public String toString() {
      return "unresolved-ref()";
   }
}
