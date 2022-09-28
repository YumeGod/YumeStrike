package eu.bitwalker.useragentutils;

import java.io.Serializable;

public class UserAgent implements Serializable {
   private static final long serialVersionUID = 7025462762784240212L;
   private OperatingSystem operatingSystem;
   private Browser browser;
   private int id;
   private String userAgentString;

   public UserAgent(OperatingSystem operatingSystem, Browser browser) {
      this.operatingSystem = OperatingSystem.UNKNOWN;
      this.browser = Browser.UNKNOWN;
      this.operatingSystem = operatingSystem;
      this.browser = browser;
      this.id = (operatingSystem.getId() << 16) + browser.getId();
   }

   public UserAgent(String userAgentString) {
      this.operatingSystem = OperatingSystem.UNKNOWN;
      this.browser = Browser.UNKNOWN;
      Browser browser = Browser.parseUserAgentString(userAgentString);
      OperatingSystem operatingSystem = OperatingSystem.UNKNOWN;
      if (browser != Browser.BOT) {
         operatingSystem = OperatingSystem.parseUserAgentString(userAgentString);
      }

      this.operatingSystem = operatingSystem;
      this.browser = browser;
      this.id = (operatingSystem.getId() << 16) + browser.getId();
      this.userAgentString = userAgentString;
   }

   public static UserAgent parseUserAgentString(String userAgentString) {
      return new UserAgent(userAgentString);
   }

   public Version getBrowserVersion() {
      return this.browser.getVersion(this.userAgentString);
   }

   public OperatingSystem getOperatingSystem() {
      return this.operatingSystem;
   }

   public Browser getBrowser() {
      return this.browser;
   }

   public int getId() {
      return this.id;
   }

   public String toString() {
      return this.operatingSystem.toString() + "-" + this.browser.toString();
   }

   public static UserAgent valueOf(int id) {
      OperatingSystem operatingSystem = OperatingSystem.valueOf((short)(id >> 16));
      Browser browser = Browser.valueOf((short)(id & '\uffff'));
      return new UserAgent(operatingSystem, browser);
   }

   public static UserAgent valueOf(String name) {
      if (name == null) {
         throw new NullPointerException("Name is null");
      } else {
         String[] elements = name.split("-");
         if (elements.length == 2) {
            OperatingSystem operatingSystem = OperatingSystem.valueOf(elements[0]);
            Browser browser = Browser.valueOf(elements[1]);
            return new UserAgent(operatingSystem, browser);
         } else {
            throw new IllegalArgumentException("Invalid string for userAgent " + name);
         }
      }
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      result = 31 * result + (this.browser == null ? 0 : this.browser.hashCode());
      result = 31 * result + this.id;
      result = 31 * result + (this.operatingSystem == null ? 0 : this.operatingSystem.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         UserAgent other = (UserAgent)obj;
         if (this.browser == null) {
            if (other.browser != null) {
               return false;
            }
         } else if (!this.browser.equals(other.browser)) {
            return false;
         }

         if (this.id != other.id) {
            return false;
         } else {
            if (this.operatingSystem == null) {
               if (other.operatingSystem != null) {
                  return false;
               }
            } else if (!this.operatingSystem.equals(other.operatingSystem)) {
               return false;
            }

            return true;
         }
      }
   }
}
