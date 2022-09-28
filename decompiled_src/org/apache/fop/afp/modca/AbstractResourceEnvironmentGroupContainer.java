package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Factory;

public abstract class AbstractResourceEnvironmentGroupContainer extends AbstractResourceGroupContainer {
   protected ResourceEnvironmentGroup resourceEnvironmentGroup = null;

   public AbstractResourceEnvironmentGroupContainer(Factory factory, String name) {
      super(factory, name);
   }

   public void addPage(PageObject page) {
      this.addObject(page);
   }

   public void addPageGroup(PageGroup pageGroup) {
      this.addObject(pageGroup);
   }

   public void createInvokeMediumMap(String name) {
      InvokeMediumMap invokeMediumMap = this.factory.createInvokeMediumMap(name);
      this.addObject(invokeMediumMap);
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      if (this.resourceEnvironmentGroup != null) {
         this.resourceEnvironmentGroup.writeToStream(os);
      }

   }

   protected ResourceEnvironmentGroup getResourceEnvironmentGroup() {
      if (this.resourceEnvironmentGroup == null) {
         this.resourceEnvironmentGroup = this.factory.createResourceEnvironmentGroup();
      }

      return this.resourceEnvironmentGroup;
   }
}
