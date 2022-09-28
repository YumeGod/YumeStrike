package org.apache.xerces.impl.xs.identity;

public class UniqueOrKey extends IdentityConstraint {
   public UniqueOrKey(String var1, String var2, String var3, short var4) {
      super(var1, var2, var3);
      super.type = var4;
   }
}
