package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Factory;

public class PageGroup extends AbstractResourceEnvironmentGroupContainer {
   private int tleSequence = 0;

   public PageGroup(Factory factory, String name, int tleSequence) {
      super(factory, name);
      this.tleSequence = tleSequence;
   }

   public void createTagLogicalElement(String name, String value) {
      TagLogicalElement tle = this.factory.createTagLogicalElement(name, value, this.tleSequence);
      if (!this.getTagLogicalElements().contains(tle)) {
         this.getTagLogicalElements().add(tle);
         ++this.tleSequence;
      }

   }

   public void endPageGroup() {
      this.complete = true;
   }

   protected void writeContent(OutputStream os) throws IOException {
      this.writeObjects(this.tagLogicalElements, os, true);
      super.writeContent(os);
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-83);
      os.write(data);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-83);
      os.write(data);
   }

   public String toString() {
      return this.getName();
   }

   public int getTleSequence() {
      return this.tleSequence;
   }
}
