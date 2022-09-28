package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.fop.afp.Completable;

public class ResourceEnvironmentGroup extends AbstractEnvironmentGroup implements Completable {
   private static final String DEFAULT_NAME = "REG00001";
   private List preProcessPresentationObjects;
   private boolean complete;

   public ResourceEnvironmentGroup() {
      this("REG00001");
   }

   private List getPreprocessPresentationObjects() {
      if (this.preProcessPresentationObjects == null) {
         this.preProcessPresentationObjects = new ArrayList();
      }

      return this.preProcessPresentationObjects;
   }

   public ResourceEnvironmentGroup(String name) {
      super(name);
      this.preProcessPresentationObjects = null;
      this.complete = false;
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-39);
      os.write(data);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-39);
      os.write(data);
   }

   protected void writeContent(OutputStream os) throws IOException {
      this.writeObjects(this.mapDataResources, os);
      this.writeObjects(this.mapPageOverlays, os);
      this.writeObjects(this.preProcessPresentationObjects, os);
   }

   public void setComplete(boolean complete) {
      this.complete = complete;
   }

   public boolean isComplete() {
      return this.complete;
   }
}
