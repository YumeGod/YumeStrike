package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import org.apache.fop.afp.Completable;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.Streamable;

public abstract class AbstractResourceGroupContainer extends AbstractPageObject implements Streamable {
   protected boolean started = false;
   protected ResourceGroup resourceGroup = null;

   public AbstractResourceGroupContainer(Factory factory) {
      super(factory);
   }

   public AbstractResourceGroupContainer(Factory factory, String name) {
      super(factory, name);
   }

   public AbstractResourceGroupContainer(Factory factory, String name, int width, int height, int rotation, int widthRes, int heightRes) {
      super(factory, name, width, height, rotation, widthRes, heightRes);
   }

   protected int getResourceCount() {
      return this.resourceGroup != null ? this.resourceGroup.getResourceCount() : 0;
   }

   protected boolean hasResources() {
      return this.resourceGroup != null && this.resourceGroup.getResourceCount() > 0;
   }

   public ResourceGroup getResourceGroup() {
      if (this.resourceGroup == null) {
         this.resourceGroup = this.factory.createResourceGroup();
      }

      return this.resourceGroup;
   }

   public void writeToStream(OutputStream os) throws IOException {
      if (!this.started) {
         this.writeStart(os);
         this.started = true;
      }

      this.writeContent(os);
      if (this.complete) {
         this.writeEnd(os);
      }

   }

   protected void writeObjects(Collection objects, OutputStream os) throws IOException {
      this.writeObjects(objects, os, false);
   }

   protected void writeObjects(Collection objects, OutputStream os, boolean forceWrite) throws IOException {
      if (objects != null && objects.size() > 0) {
         Iterator it = objects.iterator();

         while(it.hasNext()) {
            AbstractAFPObject ao = (AbstractAFPObject)it.next();
            if (!forceWrite && !this.canWrite(ao)) {
               break;
            }

            ao.writeToStream(os);
            it.remove();
         }
      }

   }

   protected boolean canWrite(AbstractAFPObject obj) {
      return obj instanceof AbstractPageObject ? ((Completable)obj).isComplete() : this.isComplete();
   }
}
