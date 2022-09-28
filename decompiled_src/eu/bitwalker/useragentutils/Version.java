package eu.bitwalker.useragentutils;

public class Version implements Comparable {
   String version;
   String majorVersion;
   String minorVersion;

   public Version(String version, String majorVersion, String minorVersion) {
      this.version = version;
      this.majorVersion = majorVersion;
      this.minorVersion = minorVersion;
   }

   public String getVersion() {
      return this.version;
   }

   public String getMajorVersion() {
      return this.majorVersion;
   }

   public String getMinorVersion() {
      return this.minorVersion;
   }

   public String toString() {
      return this.version;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      result = 31 * result + (this.majorVersion == null ? 0 : this.majorVersion.hashCode());
      result = 31 * result + (this.minorVersion == null ? 0 : this.minorVersion.hashCode());
      result = 31 * result + (this.version == null ? 0 : this.version.hashCode());
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
         Version other = (Version)obj;
         if (this.majorVersion == null) {
            if (other.majorVersion != null) {
               return false;
            }
         } else if (!this.majorVersion.equals(other.majorVersion)) {
            return false;
         }

         if (this.minorVersion == null) {
            if (other.minorVersion != null) {
               return false;
            }
         } else if (!this.minorVersion.equals(other.minorVersion)) {
            return false;
         }

         if (this.version == null) {
            if (other.version != null) {
               return false;
            }
         } else if (!this.version.equals(other.version)) {
            return false;
         }

         return true;
      }
   }

   public int compareTo(Version other) {
      if (other == null) {
         return 1;
      } else {
         String[] versionParts = this.version.split("\\.");
         String[] otherVersionParts = other.version.split("\\.");

         for(int i = 0; i < Math.min(versionParts.length, otherVersionParts.length); ++i) {
            if (versionParts[i].length() != otherVersionParts[i].length()) {
               return versionParts[i].length() > otherVersionParts[i].length() ? 1 : -1;
            }

            int comparisonResult = versionParts[i].compareTo(otherVersionParts[i]);
            if (comparisonResult != 0) {
               return comparisonResult;
            }
         }

         if (versionParts.length > otherVersionParts.length) {
            return 1;
         } else if (versionParts.length < otherVersionParts.length) {
            return -1;
         } else {
            return 0;
         }
      }
   }
}
