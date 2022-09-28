package org.apache.batik.util;

public class BatikSecurityManager extends SecurityManager {
   public Class[] getClassContext() {
      return super.getClassContext();
   }
}
