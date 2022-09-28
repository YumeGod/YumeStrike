package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSResource;

public class DSCCommentBeginResource extends AbstractResourceDSCComment {
   private Integer min;
   private Integer max;

   public DSCCommentBeginResource() {
   }

   public DSCCommentBeginResource(PSResource resource) {
      super(resource);
   }

   public DSCCommentBeginResource(PSResource resource, int min, int max) {
      super(resource);
      this.min = new Integer(min);
      this.max = new Integer(max);
   }

   public Integer getMin() {
      return this.min;
   }

   public Integer getMax() {
      return this.max;
   }

   public String getName() {
      return "BeginResource";
   }

   public void generate(PSGenerator gen) throws IOException {
      if (this.getMin() != null) {
         Object[] params = new Object[]{this.getResource(), this.getMin(), this.getMax()};
         gen.writeDSCComment(this.getName(), params);
      } else {
         super.generate(gen);
      }

   }
}
