package org.apache.james.mime4j.dom.address;

import java.io.Serializable;
import java.util.List;

public abstract class Address implements Serializable {
   private static final long serialVersionUID = 634090661990433426L;

   final void addMailboxesTo(List results) {
      this.doAddMailboxesTo(results);
   }

   protected abstract void doAddMailboxesTo(List var1);
}
