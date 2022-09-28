package org.apache.xalan.xsltc.compiler;

abstract class RelativeLocationPath extends Expression {
   public abstract int getAxis();

   public abstract void setAxis(int var1);
}
