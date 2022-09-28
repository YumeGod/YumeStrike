package org.apache.xmlgraphics.ps;

public class PSProcSet extends PSResource {
   private float version;
   private int revision;

   public PSProcSet(String name) {
      this(name, 1.0F, 0);
   }

   public PSProcSet(String name, float version, int revision) {
      super("procset", name);
      this.version = version;
      this.revision = revision;
   }

   public float getVersion() {
      return this.version;
   }

   public int getRevision() {
      return this.revision;
   }

   public String getResourceSpecification() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.getType()).append(" ").append(PSGenerator.convertStringToDSC(this.getName()));
      sb.append(" ").append(PSGenerator.convertRealToDSC(this.getVersion()));
      sb.append(" ").append(Integer.toString(this.getRevision()));
      return sb.toString();
   }
}
