package eu.bitwalker.useragentutils;

public enum ApplicationType {
   WEBMAIL("Webmail client"),
   UNKNOWN("unknown");

   private String name;

   private ApplicationType(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}
