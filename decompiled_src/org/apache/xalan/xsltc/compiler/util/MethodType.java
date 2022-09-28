package org.apache.xalan.xsltc.compiler.util;

import java.util.Vector;

public final class MethodType extends Type {
   private final Type _resultType;
   private final Vector _argsType;

   public MethodType(Type resultType) {
      this._argsType = null;
      this._resultType = resultType;
   }

   public MethodType(Type resultType, Type arg1) {
      if (arg1 != Type.Void) {
         this._argsType = new Vector();
         this._argsType.addElement(arg1);
      } else {
         this._argsType = null;
      }

      this._resultType = resultType;
   }

   public MethodType(Type resultType, Type arg1, Type arg2) {
      this._argsType = new Vector(2);
      this._argsType.addElement(arg1);
      this._argsType.addElement(arg2);
      this._resultType = resultType;
   }

   public MethodType(Type resultType, Type arg1, Type arg2, Type arg3) {
      this._argsType = new Vector(3);
      this._argsType.addElement(arg1);
      this._argsType.addElement(arg2);
      this._argsType.addElement(arg3);
      this._resultType = resultType;
   }

   public MethodType(Type resultType, Vector argsType) {
      this._resultType = resultType;
      this._argsType = argsType.size() > 0 ? argsType : null;
   }

   public String toString() {
      StringBuffer result = new StringBuffer("method{");
      if (this._argsType != null) {
         int count = this._argsType.size();

         for(int i = 0; i < count; ++i) {
            result.append(this._argsType.elementAt(i));
            if (i != count - 1) {
               result.append(',');
            }
         }
      } else {
         result.append("void");
      }

      result.append('}');
      return result.toString();
   }

   public String toSignature() {
      return this.toSignature("");
   }

   public String toSignature(String lastArgSig) {
      StringBuffer buffer = new StringBuffer();
      buffer.append('(');
      if (this._argsType != null) {
         int n = this._argsType.size();

         for(int i = 0; i < n; ++i) {
            buffer.append(((Type)this._argsType.elementAt(i)).toSignature());
         }
      }

      return buffer.append(lastArgSig).append(')').append(this._resultType.toSignature()).toString();
   }

   public org.apache.bcel.generic.Type toJCType() {
      return null;
   }

   public boolean identicalTo(Type other) {
      boolean result = false;
      if (other instanceof MethodType) {
         MethodType temp = (MethodType)other;
         if (this._resultType.identicalTo(temp._resultType)) {
            int len = this.argsCount();
            result = len == temp.argsCount();

            for(int i = 0; i < len && result; ++i) {
               Type arg1 = (Type)this._argsType.elementAt(i);
               Type arg2 = (Type)temp._argsType.elementAt(i);
               result = arg1.identicalTo(arg2);
            }
         }
      }

      return result;
   }

   public int distanceTo(Type other) {
      int result = Integer.MAX_VALUE;
      if (other instanceof MethodType) {
         MethodType mtype = (MethodType)other;
         if (this._argsType != null) {
            int len = this._argsType.size();
            if (len == mtype._argsType.size()) {
               result = 0;

               for(int i = 0; i < len; ++i) {
                  Type arg1 = (Type)this._argsType.elementAt(i);
                  Type arg2 = (Type)mtype._argsType.elementAt(i);
                  int temp = arg1.distanceTo(arg2);
                  if (temp == Integer.MAX_VALUE) {
                     result = temp;
                     break;
                  }

                  result += arg1.distanceTo(arg2);
               }
            }
         } else if (mtype._argsType == null) {
            result = 0;
         }
      }

      return result;
   }

   public Type resultType() {
      return this._resultType;
   }

   public Vector argsType() {
      return this._argsType;
   }

   public int argsCount() {
      return this._argsType == null ? 0 : this._argsType.size();
   }
}
