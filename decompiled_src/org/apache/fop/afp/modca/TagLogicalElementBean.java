package org.apache.fop.afp.modca;

public class TagLogicalElementBean {
   private String key;
   private String value;

   public TagLogicalElementBean(String key, String value) {
      this.key = key;
      this.value = value;
   }

   public String getKey() {
      return this.key;
   }

   public String getValue() {
      return this.value;
   }
}
