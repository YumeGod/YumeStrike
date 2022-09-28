package eu.bitwalker.useragentutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public enum OperatingSystem {
   WINDOWS(Manufacturer.MICROSOFT, (OperatingSystem)null, 1, "Windows", new String[]{"Windows"}, new String[]{"Palm", "ggpht.com"}, DeviceType.COMPUTER, (String)null),
   WINDOWS_81(Manufacturer.MICROSOFT, WINDOWS, 23, "Windows 8.1", new String[]{"Windows NT 6.3"}, (String[])null, DeviceType.COMPUTER, (String)null),
   WINDOWS_8(Manufacturer.MICROSOFT, WINDOWS, 22, "Windows 8", new String[]{"Windows NT 6.2"}, (String[])null, DeviceType.COMPUTER, (String)null),
   WINDOWS_7(Manufacturer.MICROSOFT, WINDOWS, 21, "Windows 7", new String[]{"Windows NT 6.1"}, (String[])null, DeviceType.COMPUTER, (String)null),
   WINDOWS_VISTA(Manufacturer.MICROSOFT, WINDOWS, 20, "Windows Vista", new String[]{"Windows NT 6"}, (String[])null, DeviceType.COMPUTER, (String)null),
   WINDOWS_2000(Manufacturer.MICROSOFT, WINDOWS, 15, "Windows 2000", new String[]{"Windows NT 5.0"}, (String[])null, DeviceType.COMPUTER, (String)null),
   WINDOWS_XP(Manufacturer.MICROSOFT, WINDOWS, 10, "Windows XP", new String[]{"Windows NT 5"}, new String[]{"ggpht.com"}, DeviceType.COMPUTER, (String)null),
   WINDOWS_PHONE8(Manufacturer.MICROSOFT, WINDOWS, 52, "Windows Phone 8", new String[]{"Windows Phone 8"}, (String[])null, DeviceType.MOBILE, (String)null),
   WINDOWS_MOBILE7(Manufacturer.MICROSOFT, WINDOWS, 51, "Windows Phone 7", new String[]{"Windows Phone OS 7"}, (String[])null, DeviceType.MOBILE, (String)null),
   WINDOWS_MOBILE(Manufacturer.MICROSOFT, WINDOWS, 50, "Windows Mobile", new String[]{"Windows CE"}, (String[])null, DeviceType.MOBILE, (String)null),
   WINDOWS_98(Manufacturer.MICROSOFT, WINDOWS, 5, "Windows 98", new String[]{"Windows 98", "Win98"}, new String[]{"Palm"}, DeviceType.COMPUTER, (String)null),
   ANDROID(Manufacturer.GOOGLE, (OperatingSystem)null, 0, "Android", new String[]{"Android"}, (String[])null, DeviceType.MOBILE, (String)null),
   ANDROID4(Manufacturer.GOOGLE, ANDROID, 4, "Android 4.x", new String[]{"Android 4", "Android-4"}, (String[])null, DeviceType.MOBILE, (String)null),
   ANDROID4_TABLET(Manufacturer.GOOGLE, ANDROID4, 40, "Android 4.x Tablet", new String[]{"Android 4", "Android-4"}, new String[]{"mobile"}, DeviceType.TABLET, (String)null),
   ANDROID3_TABLET(Manufacturer.GOOGLE, ANDROID, 30, "Android 3.x Tablet", new String[]{"Android 3"}, (String[])null, DeviceType.TABLET, (String)null),
   ANDROID2(Manufacturer.GOOGLE, ANDROID, 2, "Android 2.x", new String[]{"Android 2"}, (String[])null, DeviceType.MOBILE, (String)null),
   ANDROID2_TABLET(Manufacturer.GOOGLE, ANDROID2, 20, "Android 2.x Tablet", new String[]{"Kindle Fire", "GT-P1000", "SCH-I800"}, (String[])null, DeviceType.TABLET, (String)null),
   ANDROID1(Manufacturer.GOOGLE, ANDROID, 1, "Android 1.x", new String[]{"Android 1"}, (String[])null, DeviceType.MOBILE, (String)null),
   ANDROID_MOBILE(Manufacturer.GOOGLE, ANDROID, 11, "Android Mobile", new String[]{"Mobile"}, (String[])null, DeviceType.MOBILE, (String)null),
   ANDROID_TABLET(Manufacturer.GOOGLE, ANDROID, 12, "Android Tablet", new String[]{"Tablet"}, (String[])null, DeviceType.TABLET, (String)null),
   WEBOS(Manufacturer.HP, (OperatingSystem)null, 11, "WebOS", new String[]{"webOS"}, (String[])null, DeviceType.MOBILE, (String)null),
   PALM(Manufacturer.HP, (OperatingSystem)null, 10, "PalmOS", new String[]{"Palm"}, (String[])null, DeviceType.MOBILE, (String)null),
   MEEGO(Manufacturer.NOKIA, (OperatingSystem)null, 3, "MeeGo", new String[]{"MeeGo"}, (String[])null, DeviceType.MOBILE, (String)null),
   IOS(Manufacturer.APPLE, (OperatingSystem)null, 2, "iOS", new String[]{"iPhone OS", "like Mac OS X"}, (String[])null, DeviceType.MOBILE, (String)null),
   iOS7_IPHONE(Manufacturer.APPLE, IOS, 44, "iOS 7 (iPhone)", new String[]{"iPhone OS 7"}, (String[])null, DeviceType.MOBILE, (String)null),
   iOS6_IPHONE(Manufacturer.APPLE, IOS, 43, "iOS 6 (iPhone)", new String[]{"iPhone OS 6"}, (String[])null, DeviceType.MOBILE, (String)null),
   iOS5_IPHONE(Manufacturer.APPLE, IOS, 42, "iOS 5 (iPhone)", new String[]{"iPhone OS 5"}, (String[])null, DeviceType.MOBILE, (String)null),
   iOS4_IPHONE(Manufacturer.APPLE, IOS, 41, "iOS 4 (iPhone)", new String[]{"iPhone OS 4"}, (String[])null, DeviceType.MOBILE, (String)null),
   MAC_OS_X_IPAD(Manufacturer.APPLE, IOS, 50, "Mac OS X (iPad)", new String[]{"iPad"}, (String[])null, DeviceType.TABLET, (String)null),
   iOS7_IPAD(Manufacturer.APPLE, MAC_OS_X_IPAD, 52, "iOS 7 (iPad)", new String[]{"OS 7"}, (String[])null, DeviceType.TABLET, (String)null),
   iOS6_IPAD(Manufacturer.APPLE, MAC_OS_X_IPAD, 51, "iOS 6 (iPad)", new String[]{"OS 6"}, (String[])null, DeviceType.TABLET, (String)null),
   MAC_OS_X_IPHONE(Manufacturer.APPLE, IOS, 40, "Mac OS X (iPhone)", new String[]{"iPhone"}, (String[])null, DeviceType.MOBILE, (String)null),
   MAC_OS_X_IPOD(Manufacturer.APPLE, IOS, 30, "Mac OS X (iPod)", new String[]{"iPod"}, (String[])null, DeviceType.MOBILE, (String)null),
   MAC_OS_X(Manufacturer.APPLE, (OperatingSystem)null, 10, "Mac OS X", new String[]{"Mac OS X", "CFNetwork"}, (String[])null, DeviceType.COMPUTER, (String)null),
   MAC_OS(Manufacturer.APPLE, (OperatingSystem)null, 1, "Mac OS", new String[]{"Mac"}, (String[])null, DeviceType.COMPUTER, (String)null),
   MAEMO(Manufacturer.NOKIA, (OperatingSystem)null, 2, "Maemo", new String[]{"Maemo"}, (String[])null, DeviceType.MOBILE, (String)null),
   BADA(Manufacturer.SAMSUNG, (OperatingSystem)null, 2, "Bada", new String[]{"Bada"}, (String[])null, DeviceType.MOBILE, (String)null),
   GOOGLE_TV(Manufacturer.GOOGLE, (OperatingSystem)null, 100, "Android (Google TV)", new String[]{"GoogleTV"}, (String[])null, DeviceType.DMR, (String)null),
   KINDLE(Manufacturer.AMAZON, (OperatingSystem)null, 1, "Linux (Kindle)", new String[]{"Kindle"}, (String[])null, DeviceType.TABLET, (String)null),
   KINDLE3(Manufacturer.AMAZON, KINDLE, 30, "Linux (Kindle 3)", new String[]{"Kindle/3"}, (String[])null, DeviceType.TABLET, (String)null),
   KINDLE2(Manufacturer.AMAZON, KINDLE, 20, "Linux (Kindle 2)", new String[]{"Kindle/2"}, (String[])null, DeviceType.TABLET, (String)null),
   LINUX(Manufacturer.OTHER, (OperatingSystem)null, 2, "Linux", new String[]{"Linux", "CamelHttpStream"}, (String[])null, DeviceType.COMPUTER, (String)null),
   SYMBIAN(Manufacturer.SYMBIAN, (OperatingSystem)null, 1, "Symbian OS", new String[]{"Symbian", "Series60"}, (String[])null, DeviceType.MOBILE, (String)null),
   SYMBIAN9(Manufacturer.SYMBIAN, SYMBIAN, 20, "Symbian OS 9.x", new String[]{"SymbianOS/9", "Series60/3"}, (String[])null, DeviceType.MOBILE, (String)null),
   SYMBIAN8(Manufacturer.SYMBIAN, SYMBIAN, 15, "Symbian OS 8.x", new String[]{"SymbianOS/8", "Series60/2.6", "Series60/2.8"}, (String[])null, DeviceType.MOBILE, (String)null),
   SYMBIAN7(Manufacturer.SYMBIAN, SYMBIAN, 10, "Symbian OS 7.x", new String[]{"SymbianOS/7"}, (String[])null, DeviceType.MOBILE, (String)null),
   SYMBIAN6(Manufacturer.SYMBIAN, SYMBIAN, 5, "Symbian OS 6.x", new String[]{"SymbianOS/6"}, (String[])null, DeviceType.MOBILE, (String)null),
   SERIES40(Manufacturer.NOKIA, (OperatingSystem)null, 1, "Series 40", new String[]{"Nokia6300"}, (String[])null, DeviceType.MOBILE, (String)null),
   SONY_ERICSSON(Manufacturer.SONY_ERICSSON, (OperatingSystem)null, 1, "Sony Ericsson", new String[]{"SonyEricsson"}, (String[])null, DeviceType.MOBILE, (String)null),
   SUN_OS(Manufacturer.SUN, (OperatingSystem)null, 1, "SunOS", new String[]{"SunOS"}, (String[])null, DeviceType.COMPUTER, (String)null),
   PSP(Manufacturer.SONY, (OperatingSystem)null, 1, "Sony Playstation", new String[]{"Playstation"}, (String[])null, DeviceType.GAME_CONSOLE, (String)null),
   WII(Manufacturer.NINTENDO, (OperatingSystem)null, 1, "Nintendo Wii", new String[]{"Wii"}, (String[])null, DeviceType.GAME_CONSOLE, (String)null),
   BLACKBERRY(Manufacturer.BLACKBERRY, (OperatingSystem)null, 1, "BlackBerryOS", new String[]{"BlackBerry"}, (String[])null, DeviceType.MOBILE, (String)null),
   BLACKBERRY7(Manufacturer.BLACKBERRY, BLACKBERRY, 7, "BlackBerry 7", new String[]{"Version/7"}, (String[])null, DeviceType.MOBILE, (String)null),
   BLACKBERRY6(Manufacturer.BLACKBERRY, BLACKBERRY, 6, "BlackBerry 6", new String[]{"Version/6"}, (String[])null, DeviceType.MOBILE, (String)null),
   BLACKBERRY_TABLET(Manufacturer.BLACKBERRY, (OperatingSystem)null, 100, "BlackBerry Tablet OS", new String[]{"RIM Tablet OS"}, (String[])null, DeviceType.TABLET, (String)null),
   ROKU(Manufacturer.ROKU, (OperatingSystem)null, 1, "Roku OS", new String[]{"Roku"}, (String[])null, DeviceType.DMR, (String)null),
   PROXY(Manufacturer.OTHER, (OperatingSystem)null, 10, "Proxy", new String[]{"ggpht.com"}, (String[])null, DeviceType.UNKNOWN, (String)null),
   UNKNOWN_MOBILE(Manufacturer.OTHER, (OperatingSystem)null, 3, "Unknown mobile", new String[]{"Mobile"}, (String[])null, DeviceType.MOBILE, (String)null),
   UNKNOWN_TABLET(Manufacturer.OTHER, (OperatingSystem)null, 4, "Unknown tablet", new String[]{"Tablet"}, (String[])null, DeviceType.TABLET, (String)null),
   UNKNOWN(Manufacturer.OTHER, (OperatingSystem)null, 1, "Unknown", new String[0], (String[])null, DeviceType.UNKNOWN, (String)null);

   private final short id;
   private final String name;
   private final String[] aliases;
   private final String[] excludeList;
   private final Manufacturer manufacturer;
   private final DeviceType deviceType;
   private final OperatingSystem parent;
   private List children;
   private Pattern versionRegEx;
   private static List topLevelOperatingSystems;

   private OperatingSystem(Manufacturer manufacturer, OperatingSystem parent, int versionId, String name, String[] aliases, String[] exclude, DeviceType deviceType, String versionRegexString) {
      this.manufacturer = manufacturer;
      this.parent = parent;
      this.children = new ArrayList();
      this.id = (short)((manufacturer.getId() << 8) + (byte)versionId);
      this.name = name;
      this.aliases = aliases;
      this.excludeList = exclude;
      this.deviceType = deviceType;
      if (versionRegexString != null) {
         this.versionRegEx = Pattern.compile(versionRegexString);
      }

      if (this.parent == null) {
         addTopLevelOperatingSystem(this);
      } else {
         this.parent.children.add(this);
      }

   }

   private static void addTopLevelOperatingSystem(OperatingSystem os) {
      if (topLevelOperatingSystems == null) {
         topLevelOperatingSystems = new ArrayList();
      }

      topLevelOperatingSystems.add(os);
   }

   public short getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public boolean isMobileDevice() {
      return this.deviceType.equals(DeviceType.MOBILE);
   }

   public DeviceType getDeviceType() {
      return this.deviceType;
   }

   public OperatingSystem getGroup() {
      return this.parent != null ? this.parent.getGroup() : this;
   }

   public Manufacturer getManufacturer() {
      return this.manufacturer;
   }

   public boolean isInUserAgentString(String agentString) {
      String[] arr$ = this.aliases;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String alias = arr$[i$];
         if (agentString.toLowerCase().indexOf(alias.toLowerCase()) != -1) {
            return true;
         }
      }

      return false;
   }

   private boolean containsExcludeToken(String agentString) {
      if (this.excludeList != null) {
         String[] arr$ = this.excludeList;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String exclude = arr$[i$];
            if (agentString.toLowerCase().indexOf(exclude.toLowerCase()) != -1) {
               return true;
            }
         }
      }

      return false;
   }

   private OperatingSystem checkUserAgent(String agentString) {
      if (this.isInUserAgentString(agentString)) {
         if (this.children.size() > 0) {
            Iterator i$ = this.children.iterator();

            while(i$.hasNext()) {
               OperatingSystem childOperatingSystem = (OperatingSystem)i$.next();
               OperatingSystem match = childOperatingSystem.checkUserAgent(agentString);
               if (match != null) {
                  return match;
               }
            }
         }

         if (!this.containsExcludeToken(agentString)) {
            return this;
         }
      }

      return null;
   }

   public static OperatingSystem parseUserAgentString(String agentString) {
      return parseUserAgentString(agentString, topLevelOperatingSystems);
   }

   public static OperatingSystem parseUserAgentString(String agentString, List operatingSystems) {
      Iterator i$ = operatingSystems.iterator();

      OperatingSystem match;
      do {
         if (!i$.hasNext()) {
            return UNKNOWN;
         }

         OperatingSystem operatingSystem = (OperatingSystem)i$.next();
         match = operatingSystem.checkUserAgent(agentString);
      } while(match == null);

      return match;
   }

   public static OperatingSystem valueOf(short id) {
      OperatingSystem[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         OperatingSystem operatingSystem = arr$[i$];
         if (operatingSystem.getId() == id) {
            return operatingSystem;
         }
      }

      throw new IllegalArgumentException("No enum const for id " + id);
   }
}
