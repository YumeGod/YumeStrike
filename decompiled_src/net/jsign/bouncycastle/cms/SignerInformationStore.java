package net.jsign.bouncycastle.cms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.jsign.bouncycastle.util.Iterable;

public class SignerInformationStore implements Iterable {
   private List all = new ArrayList();
   private Map table = new HashMap();

   public SignerInformationStore(SignerInformation var1) {
      this.all = new ArrayList(1);
      this.all.add(var1);
      SignerId var2 = var1.getSID();
      this.table.put(var2, this.all);
   }

   public SignerInformationStore(Collection var1) {
      SignerInformation var3;
      ArrayList var5;
      for(Iterator var2 = var1.iterator(); var2.hasNext(); var5.add(var3)) {
         var3 = (SignerInformation)var2.next();
         SignerId var4 = var3.getSID();
         var5 = (ArrayList)this.table.get(var4);
         if (var5 == null) {
            var5 = new ArrayList(1);
            this.table.put(var4, var5);
         }
      }

      this.all = new ArrayList(var1);
   }

   public SignerInformation get(SignerId var1) {
      Collection var2 = this.getSigners(var1);
      return var2.size() == 0 ? null : (SignerInformation)var2.iterator().next();
   }

   public int size() {
      return this.all.size();
   }

   public Collection getSigners() {
      return new ArrayList(this.all);
   }

   public Collection getSigners(SignerId var1) {
      ArrayList var2;
      if (var1.getIssuer() != null && var1.getSubjectKeyIdentifier() != null) {
         var2 = new ArrayList();
         Collection var3 = this.getSigners(new SignerId(var1.getIssuer(), var1.getSerialNumber()));
         if (var3 != null) {
            var2.addAll(var3);
         }

         Collection var4 = this.getSigners(new SignerId(var1.getSubjectKeyIdentifier()));
         if (var4 != null) {
            var2.addAll(var4);
         }

         return var2;
      } else {
         var2 = (ArrayList)this.table.get(var1);
         return var2 == null ? new ArrayList() : new ArrayList(var2);
      }
   }

   public Iterator iterator() {
      return this.getSigners().iterator();
   }
}
