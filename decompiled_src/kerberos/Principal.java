package kerberos;

import common.DataParser;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Principal {
   protected int name_type;
   protected String realm;
   protected List components = new LinkedList();

   public Principal(int var1, String var2) {
      this.name_type = var1;
      this.realm = var2;
   }

   public Principal(DataParser var1) throws IOException {
      this.name_type = var1.readInt();
      int var2 = var1.readInt();
      this.realm = var1.readCountedString();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.components.add(var1.readCountedString());
      }

   }

   public String toString() {
      return "Principal(" + this.name_type + ") " + this.realm + " " + this.components;
   }
}
