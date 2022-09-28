package eu.bitwalker.useragentutils;

public enum Application {
   HOTMAIL(Manufacturer.MICROSOFT, 1, "Windows Live Hotmail", new String[]{"mail.live.com", "hotmail.msn"}, ApplicationType.WEBMAIL),
   GMAIL(Manufacturer.GOOGLE, 5, "Gmail", new String[]{"mail.google.com"}, ApplicationType.WEBMAIL),
   YAHOO_MAIL(Manufacturer.YAHOO, 10, "Yahoo Mail", new String[]{"mail.yahoo.com"}, ApplicationType.WEBMAIL),
   COMPUSERVE(Manufacturer.COMPUSERVE, 20, "Compuserve", new String[]{"csmail.compuserve.com"}, ApplicationType.WEBMAIL),
   AOL_WEBMAIL(Manufacturer.AOL, 30, "AOL webmail", new String[]{"webmail.aol.com"}, ApplicationType.WEBMAIL),
   MOBILEME(Manufacturer.APPLE, 40, "MobileMe", new String[]{"www.me.com"}, ApplicationType.WEBMAIL),
   MAIL_COM(Manufacturer.MMC, 50, "Mail.com", new String[]{".mail.com"}, ApplicationType.WEBMAIL),
   HORDE(Manufacturer.OTHER, 50, "horde", new String[]{"horde"}, ApplicationType.WEBMAIL),
   OTHER_WEBMAIL(Manufacturer.OTHER, 60, "Other webmail client", new String[]{"webmail", "webemail"}, ApplicationType.WEBMAIL),
   UNKNOWN(Manufacturer.OTHER, 0, "Unknown", new String[0], ApplicationType.UNKNOWN);

   private final short id;
   private final String name;
   private final String[] aliases;
   private final ApplicationType applicationType;
   private final Manufacturer manufacturer;

   private Application(Manufacturer manufacturer, int versionId, String name, String[] aliases, ApplicationType applicationType) {
      this.id = (short)((manufacturer.getId() << 8) + (byte)versionId);
      this.name = name;
      this.aliases = aliases;
      this.applicationType = applicationType;
      this.manufacturer = manufacturer;
   }

   public short getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public ApplicationType getApplicationType() {
      return this.applicationType;
   }

   public Manufacturer getManufacturer() {
      return this.manufacturer;
   }

   public boolean isInReferrerString(String referrerString) {
      String[] arr$ = this.aliases;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String alias = arr$[i$];
         if (referrerString.toLowerCase().indexOf(alias.toLowerCase()) != -1) {
            return true;
         }
      }

      return false;
   }

   public static Application parseReferrerString(String referrerString) {
      if (referrerString != null && referrerString.length() > 1) {
         Application[] arr$ = values();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Application applicationInList = arr$[i$];
            if (applicationInList.isInReferrerString(referrerString)) {
               return applicationInList;
            }
         }
      }

      return UNKNOWN;
   }

   public static Application valueOf(short id) {
      Application[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Application application = arr$[i$];
         if (application.getId() == id) {
            return application;
         }
      }

      throw new IllegalArgumentException("No enum const for id " + id);
   }
}
