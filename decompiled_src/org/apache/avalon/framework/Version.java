package org.apache.avalon.framework;

import java.io.Serializable;
import java.util.StringTokenizer;

public final class Version implements Comparable, Serializable {
   private int m_major;
   private int m_minor;
   private int m_micro;

   public static Version getVersion(String version) throws NumberFormatException, IllegalArgumentException {
      if (version == null) {
         throw new NullPointerException("version");
      } else {
         StringTokenizer tokenizer = new StringTokenizer(version, ".");
         String[] levels = new String[tokenizer.countTokens()];

         int major;
         for(major = 0; major < levels.length; ++major) {
            levels[major] = tokenizer.nextToken();
         }

         major = -1;
         if (0 < levels.length) {
            major = Integer.parseInt(levels[0]);
         }

         int minor = 0;
         if (1 < levels.length) {
            minor = Integer.parseInt(levels[1]);
         }

         int micro = 0;
         if (2 < levels.length) {
            micro = Integer.parseInt(levels[2]);
         }

         return new Version(major, minor, micro);
      }
   }

   public Version(int major, int minor, int micro) {
      this.m_major = major;
      this.m_minor = minor;
      this.m_micro = micro;
   }

   public int getMajor() {
      return this.m_major;
   }

   public int getMinor() {
      return this.m_minor;
   }

   public int getMicro() {
      return this.m_micro;
   }

   public boolean equals(Version other) {
      if (other == null) {
         return false;
      } else {
         boolean isEqual = this.getMajor() == other.getMajor();
         if (isEqual) {
            isEqual = this.getMinor() == other.getMinor();
         }

         if (isEqual) {
            isEqual = this.getMicro() == other.getMicro();
         }

         return isEqual;
      }
   }

   public boolean equals(Object other) {
      boolean isEqual = false;
      if (other instanceof Version) {
         isEqual = this.equals((Version)other);
      }

      return isEqual;
   }

   public int hashCode() {
      int hash = this.getMajor();
      hash >>>= 17;
      hash += this.getMinor();
      hash >>>= 17;
      hash += this.getMicro();
      return hash;
   }

   public boolean complies(Version other) {
      if (other == null) {
         return false;
      } else if (other.m_major == -1) {
         return true;
      } else if (this.m_major != other.m_major) {
         return false;
      } else if (this.m_minor < other.m_minor) {
         return false;
      } else {
         return this.m_minor != other.m_minor || this.m_micro >= other.m_micro;
      }
   }

   public String toString() {
      return this.m_major + "." + this.m_minor + "." + this.m_micro;
   }

   public int compareTo(Object o) {
      if (o == null) {
         throw new NullPointerException("o");
      } else {
         Version other = (Version)o;
         int val = 0;
         if (this.getMajor() < other.getMajor()) {
            val = -1;
         }

         if (0 == val && this.getMajor() > other.getMajor()) {
            val = 1;
         }

         if (0 == val && this.getMinor() < other.getMinor()) {
            val = -1;
         }

         if (0 == val && this.getMinor() > other.getMinor()) {
            val = 1;
         }

         if (0 == val && this.getMicro() < other.getMicro()) {
            val = -1;
         }

         if (0 == val && this.getMicro() > other.getMicro()) {
            val = 1;
         }

         return val;
      }
   }
}
