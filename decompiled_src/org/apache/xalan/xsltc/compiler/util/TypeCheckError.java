package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;

public class TypeCheckError extends Exception {
   static final long serialVersionUID = 3246224233917854640L;
   ErrorMsg _error = null;
   SyntaxTreeNode _node = null;

   public TypeCheckError(SyntaxTreeNode node) {
      this._node = node;
   }

   public TypeCheckError(ErrorMsg error) {
      this._error = error;
   }

   public TypeCheckError(String code, Object param) {
      this._error = new ErrorMsg(code, param);
   }

   public TypeCheckError(String code, Object param1, Object param2) {
      this._error = new ErrorMsg(code, param1, param2);
   }

   public ErrorMsg getErrorMsg() {
      return this._error;
   }

   public String getMessage() {
      return this.toString();
   }

   public String toString() {
      if (this._error == null) {
         if (this._node != null) {
            this._error = new ErrorMsg("TYPE_CHECK_ERR", this._node.toString());
         } else {
            this._error = new ErrorMsg("TYPE_CHECK_UNK_LOC_ERR");
         }
      }

      return this._error.toString();
   }
}
