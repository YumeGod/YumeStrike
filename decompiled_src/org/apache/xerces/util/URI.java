package org.apache.xerces.util;

import java.io.IOException;
import java.io.Serializable;

public class URI implements Serializable {
   static final long serialVersionUID = 1601921774685357214L;
   private static final byte[] fgLookupTable = new byte[128];
   private static final int RESERVED_CHARACTERS = 1;
   private static final int MARK_CHARACTERS = 2;
   private static final int SCHEME_CHARACTERS = 4;
   private static final int USERINFO_CHARACTERS = 8;
   private static final int ASCII_ALPHA_CHARACTERS = 16;
   private static final int ASCII_DIGIT_CHARACTERS = 32;
   private static final int ASCII_HEX_CHARACTERS = 64;
   private static final int PATH_CHARACTERS = 128;
   private static final int MASK_ALPHA_NUMERIC = 48;
   private static final int MASK_UNRESERVED_MASK = 50;
   private static final int MASK_URI_CHARACTER = 51;
   private static final int MASK_SCHEME_CHARACTER = 52;
   private static final int MASK_USERINFO_CHARACTER = 58;
   private static final int MASK_PATH_CHARACTER = 178;
   private String m_scheme;
   private String m_userinfo;
   private String m_host;
   private int m_port;
   private String m_regAuthority;
   private String m_path;
   private String m_queryString;
   private String m_fragment;
   private static boolean DEBUG;

   public URI() {
      this.m_scheme = null;
      this.m_userinfo = null;
      this.m_host = null;
      this.m_port = -1;
      this.m_regAuthority = null;
      this.m_path = null;
      this.m_queryString = null;
      this.m_fragment = null;
   }

   public URI(URI var1) {
      this.m_scheme = null;
      this.m_userinfo = null;
      this.m_host = null;
      this.m_port = -1;
      this.m_regAuthority = null;
      this.m_path = null;
      this.m_queryString = null;
      this.m_fragment = null;
      this.initialize(var1);
   }

   public URI(String var1) throws MalformedURIException {
      this((URI)null, var1);
   }

   public URI(String var1, boolean var2) throws MalformedURIException {
      this((URI)null, var1, var2);
   }

   public URI(URI var1, String var2) throws MalformedURIException {
      this.m_scheme = null;
      this.m_userinfo = null;
      this.m_host = null;
      this.m_port = -1;
      this.m_regAuthority = null;
      this.m_path = null;
      this.m_queryString = null;
      this.m_fragment = null;
      this.initialize(var1, var2);
   }

   public URI(URI var1, String var2, boolean var3) throws MalformedURIException {
      this.m_scheme = null;
      this.m_userinfo = null;
      this.m_host = null;
      this.m_port = -1;
      this.m_regAuthority = null;
      this.m_path = null;
      this.m_queryString = null;
      this.m_fragment = null;
      this.initialize(var1, var2, var3);
   }

   public URI(String var1, String var2) throws MalformedURIException {
      this.m_scheme = null;
      this.m_userinfo = null;
      this.m_host = null;
      this.m_port = -1;
      this.m_regAuthority = null;
      this.m_path = null;
      this.m_queryString = null;
      this.m_fragment = null;
      if (var1 != null && var1.trim().length() != 0) {
         if (var2 != null && var2.trim().length() != 0) {
            this.setScheme(var1);
            this.setPath(var2);
         } else {
            throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
         }
      } else {
         throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
      }
   }

   public URI(String var1, String var2, String var3, String var4, String var5) throws MalformedURIException {
      this(var1, (String)null, var2, -1, var3, var4, var5);
   }

   public URI(String var1, String var2, String var3, int var4, String var5, String var6, String var7) throws MalformedURIException {
      this.m_scheme = null;
      this.m_userinfo = null;
      this.m_host = null;
      this.m_port = -1;
      this.m_regAuthority = null;
      this.m_path = null;
      this.m_queryString = null;
      this.m_fragment = null;
      if (var1 != null && var1.trim().length() != 0) {
         if (var3 == null) {
            if (var2 != null) {
               throw new MalformedURIException("Userinfo may not be specified if host is not specified!");
            }

            if (var4 != -1) {
               throw new MalformedURIException("Port may not be specified if host is not specified!");
            }
         }

         if (var5 != null) {
            if (var5.indexOf(63) != -1 && var6 != null) {
               throw new MalformedURIException("Query string cannot be specified in path and query string!");
            }

            if (var5.indexOf(35) != -1 && var7 != null) {
               throw new MalformedURIException("Fragment cannot be specified in both the path and fragment!");
            }
         }

         this.setScheme(var1);
         this.setHost(var3);
         this.setPort(var4);
         this.setUserinfo(var2);
         this.setPath(var5);
         this.setQueryString(var6);
         this.setFragment(var7);
      } else {
         throw new MalformedURIException("Scheme is required!");
      }
   }

   private void initialize(URI var1) {
      this.m_scheme = var1.getScheme();
      this.m_userinfo = var1.getUserinfo();
      this.m_host = var1.getHost();
      this.m_port = var1.getPort();
      this.m_regAuthority = var1.getRegBasedAuthority();
      this.m_path = var1.getPath();
      this.m_queryString = var1.getQueryString();
      this.m_fragment = var1.getFragment();
   }

   private void initialize(URI var1, String var2, boolean var3) throws MalformedURIException {
      String var4 = var2;
      int var5 = var2 != null ? var2.length() : 0;
      if (var1 == null && var5 == 0) {
         if (var3) {
            this.m_path = "";
         } else {
            throw new MalformedURIException("Cannot initialize URI with empty parameters.");
         }
      } else if (var5 == 0) {
         this.initialize(var1);
      } else {
         int var6 = 0;
         int var7 = var2.indexOf(58);
         int var8;
         if (var7 != -1) {
            var8 = var7 - 1;
            int var9 = var2.lastIndexOf(47, var8);
            int var10 = var2.lastIndexOf(63, var8);
            int var11 = var2.lastIndexOf(35, var8);
            if (var7 != 0 && var9 == -1 && var10 == -1 && var11 == -1) {
               this.initializeScheme(var2);
               var6 = this.m_scheme.length() + 1;
               if (var7 == var5 - 1 || var2.charAt(var7 + 1) == '#') {
                  throw new MalformedURIException("Scheme specific part cannot be empty.");
               }
            } else if (var7 == 0 || var1 == null && var11 != 0 && !var3) {
               throw new MalformedURIException("No scheme found in URI.");
            }
         } else if (var1 == null && var2.indexOf(35) != 0 && !var3) {
            throw new MalformedURIException("No scheme found in URI.");
         }

         if (var6 + 1 < var5 && var2.charAt(var6) == '/' && var2.charAt(var6 + 1) == '/') {
            var6 += 2;
            var8 = var6;

            for(boolean var12 = false; var6 < var5; ++var6) {
               char var13 = var4.charAt(var6);
               if (var13 == '/' || var13 == '?' || var13 == '#') {
                  break;
               }
            }

            if (var6 > var8) {
               if (!this.initializeAuthority(var4.substring(var8, var6))) {
                  var6 = var8 - 2;
               }
            } else {
               this.m_host = "";
            }
         }

         this.initializePath(var4, var6);
         if (var1 != null) {
            this.absolutize(var1);
         }

      }
   }

   private void initialize(URI var1, String var2) throws MalformedURIException {
      String var3 = var2;
      int var4 = var2 != null ? var2.length() : 0;
      if (var1 == null && var4 == 0) {
         throw new MalformedURIException("Cannot initialize URI with empty parameters.");
      } else if (var4 == 0) {
         this.initialize(var1);
      } else {
         int var5 = 0;
         int var6 = var2.indexOf(58);
         int var7;
         if (var6 != -1) {
            var7 = var6 - 1;
            int var8 = var2.lastIndexOf(47, var7);
            int var9 = var2.lastIndexOf(63, var7);
            int var10 = var2.lastIndexOf(35, var7);
            if (var6 != 0 && var8 == -1 && var9 == -1 && var10 == -1) {
               this.initializeScheme(var2);
               var5 = this.m_scheme.length() + 1;
               if (var6 == var4 - 1 || var2.charAt(var6 + 1) == '#') {
                  throw new MalformedURIException("Scheme specific part cannot be empty.");
               }
            } else if (var6 == 0 || var1 == null && var10 != 0) {
               throw new MalformedURIException("No scheme found in URI.");
            }
         } else if (var1 == null && var2.indexOf(35) != 0) {
            throw new MalformedURIException("No scheme found in URI.");
         }

         if (var5 + 1 < var4 && var2.charAt(var5) == '/' && var2.charAt(var5 + 1) == '/') {
            var5 += 2;
            var7 = var5;

            for(boolean var11 = false; var5 < var4; ++var5) {
               char var12 = var3.charAt(var5);
               if (var12 == '/' || var12 == '?' || var12 == '#') {
                  break;
               }
            }

            if (var5 > var7) {
               if (!this.initializeAuthority(var3.substring(var7, var5))) {
                  var5 = var7 - 2;
               }
            } else {
               this.m_host = "";
            }
         }

         this.initializePath(var3, var5);
         if (var1 != null) {
            this.absolutize(var1);
         }

      }
   }

   public void absolutize(URI var1) {
      if (this.m_path.length() == 0 && this.m_scheme == null && this.m_host == null && this.m_regAuthority == null) {
         this.m_scheme = var1.getScheme();
         this.m_userinfo = var1.getUserinfo();
         this.m_host = var1.getHost();
         this.m_port = var1.getPort();
         this.m_regAuthority = var1.getRegBasedAuthority();
         this.m_path = var1.getPath();
         if (this.m_queryString == null) {
            this.m_queryString = var1.getQueryString();
            if (this.m_fragment == null) {
               this.m_fragment = var1.getFragment();
            }
         }

      } else if (this.m_scheme == null) {
         this.m_scheme = var1.getScheme();
         if (this.m_host == null && this.m_regAuthority == null) {
            this.m_userinfo = var1.getUserinfo();
            this.m_host = var1.getHost();
            this.m_port = var1.getPort();
            this.m_regAuthority = var1.getRegBasedAuthority();
            if (this.m_path.length() <= 0 || !this.m_path.startsWith("/")) {
               String var2 = "";
               String var3 = var1.getPath();
               int var4;
               if (var3 != null && var3.length() > 0) {
                  var4 = var3.lastIndexOf(47);
                  if (var4 != -1) {
                     var2 = var3.substring(0, var4 + 1);
                  }
               } else if (this.m_path.length() > 0) {
                  var2 = "/";
               }

               var2 = var2.concat(this.m_path);

               for(boolean var7 = true; (var4 = var2.indexOf("/./")) != -1; var2 = var2.substring(0, var4 + 1).concat(var2.substring(var4 + 3))) {
               }

               if (var2.endsWith("/.")) {
                  var2 = var2.substring(0, var2.length() - 1);
               }

               var4 = 1;
               boolean var5 = true;
               String var6 = null;

               int var8;
               while((var4 = var2.indexOf("/../", var4)) > 0) {
                  var6 = var2.substring(0, var2.indexOf("/../"));
                  var8 = var6.lastIndexOf(47);
                  if (var8 != -1) {
                     if (!var6.substring(var8).equals("..")) {
                        var2 = var2.substring(0, var8 + 1).concat(var2.substring(var4 + 4));
                        var4 = var8;
                     } else {
                        var4 += 4;
                     }
                  } else {
                     var4 += 4;
                  }
               }

               if (var2.endsWith("/..")) {
                  var6 = var2.substring(0, var2.length() - 3);
                  var8 = var6.lastIndexOf(47);
                  if (var8 != -1) {
                     var2 = var2.substring(0, var8 + 1);
                  }
               }

               this.m_path = var2;
            }
         }
      }
   }

   private void initializeScheme(String var1) throws MalformedURIException {
      int var2 = var1.length();
      int var3 = 0;
      String var4 = null;

      for(boolean var5 = false; var3 < var2; ++var3) {
         char var6 = var1.charAt(var3);
         if (var6 == ':' || var6 == '/' || var6 == '?' || var6 == '#') {
            break;
         }
      }

      var4 = var1.substring(0, var3);
      if (var4.length() == 0) {
         throw new MalformedURIException("No scheme found in URI.");
      } else {
         this.setScheme(var4);
      }
   }

   private boolean initializeAuthority(String var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = var1.length();
      boolean var5 = false;
      String var6 = null;
      if (var1.indexOf(64, var3) != -1) {
         while(true) {
            if (var2 < var4) {
               char var13 = var1.charAt(var2);
               if (var13 != '@') {
                  ++var2;
                  continue;
               }
            }

            var6 = var1.substring(var3, var2);
            ++var2;
            break;
         }
      }

      String var7 = null;
      boolean var8 = false;
      int var9;
      if (var2 < var4) {
         if (var1.charAt(var2) == '[') {
            var9 = var1.indexOf(93, var2);
            var2 = var9 != -1 ? var9 : var4;
            if (var2 + 1 < var4 && var1.charAt(var2 + 1) == ':') {
               ++var2;
               var8 = true;
            } else {
               var2 = var4;
            }
         } else {
            var9 = var1.lastIndexOf(58, var4);
            var2 = var9 > var2 ? var9 : var4;
            var8 = var2 != var4;
         }
      }

      var7 = var1.substring(var2, var2);
      var9 = -1;
      if (var7.length() > 0 && var8) {
         ++var2;

         for(var3 = var2; var2 < var4; ++var2) {
         }

         String var10 = var1.substring(var3, var2);
         if (var10.length() > 0) {
            try {
               var9 = Integer.parseInt(var10);
               if (var9 == -1) {
                  --var9;
               }
            } catch (NumberFormatException var12) {
               var9 = -2;
            }
         }
      }

      if (this.isValidServerBasedAuthority(var7, var9, var6)) {
         this.m_host = var7;
         this.m_port = var9;
         this.m_userinfo = var6;
         return true;
      } else if (this.isValidRegistryBasedAuthority(var1)) {
         this.m_regAuthority = var1;
         return true;
      } else {
         return false;
      }
   }

   private boolean isValidServerBasedAuthority(String var1, int var2, String var3) {
      if (!isWellFormedAddress(var1)) {
         return false;
      } else if (var2 >= -1 && var2 <= 65535) {
         if (var3 != null) {
            int var4 = 0;
            int var5 = var3.length();

            for(boolean var6 = false; var4 < var5; ++var4) {
               char var7 = var3.charAt(var4);
               if (var7 == '%') {
                  if (var4 + 2 >= var5 || !isHex(var3.charAt(var4 + 1)) || !isHex(var3.charAt(var4 + 2))) {
                     return false;
                  }

                  var4 += 2;
               } else if (!isUserinfoCharacter(var7)) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isValidRegistryBasedAuthority(String var1) {
      int var2 = 0;

      for(int var3 = var1.length(); var2 < var3; ++var2) {
         char var4 = var1.charAt(var2);
         if (var4 == '%') {
            if (var2 + 2 >= var3 || !isHex(var1.charAt(var2 + 1)) || !isHex(var1.charAt(var2 + 2))) {
               return false;
            }

            var2 += 2;
         } else if (!isPathCharacter(var4)) {
            return false;
         }
      }

      return true;
   }

   private void initializePath(String var1, int var2) throws MalformedURIException {
      if (var1 == null) {
         throw new MalformedURIException("Cannot initialize path from null string!");
      } else {
         int var3 = var2;
         int var5 = var1.length();
         char var6 = 0;
         if (var2 < var5) {
            if (this.getScheme() != null && var1.charAt(var2) != '/') {
               for(; var3 < var5; ++var3) {
                  var6 = var1.charAt(var3);
                  if (var6 == '?' || var6 == '#') {
                     break;
                  }

                  if (var6 == '%') {
                     if (var3 + 2 >= var5 || !isHex(var1.charAt(var3 + 1)) || !isHex(var1.charAt(var3 + 2))) {
                        throw new MalformedURIException("Opaque part contains invalid escape sequence!");
                     }

                     var3 += 2;
                  } else if (!isURICharacter(var6)) {
                     throw new MalformedURIException("Opaque part contains invalid character: " + var6);
                  }
               }
            } else {
               for(; var3 < var5; ++var3) {
                  var6 = var1.charAt(var3);
                  if (var6 == '%') {
                     if (var3 + 2 >= var5 || !isHex(var1.charAt(var3 + 1)) || !isHex(var1.charAt(var3 + 2))) {
                        throw new MalformedURIException("Path contains invalid escape sequence!");
                     }

                     var3 += 2;
                  } else if (!isPathCharacter(var6)) {
                     if (var6 != '?' && var6 != '#') {
                        throw new MalformedURIException("Path contains invalid character: " + var6);
                     }
                     break;
                  }
               }
            }
         }

         this.m_path = var1.substring(var2, var3);
         int var4;
         if (var6 == '?') {
            ++var3;
            var4 = var3;

            while(true) {
               if (var3 < var5) {
                  var6 = var1.charAt(var3);
                  if (var6 != '#') {
                     if (var6 == '%') {
                        if (var3 + 2 >= var5 || !isHex(var1.charAt(var3 + 1)) || !isHex(var1.charAt(var3 + 2))) {
                           throw new MalformedURIException("Query string contains invalid escape sequence!");
                        }

                        var3 += 2;
                     } else if (!isURICharacter(var6)) {
                        throw new MalformedURIException("Query string contains invalid character: " + var6);
                     }

                     ++var3;
                     continue;
                  }
               }

               this.m_queryString = var1.substring(var4, var3);
               break;
            }
         }

         if (var6 == '#') {
            ++var3;
            var4 = var3;

            while(true) {
               if (var3 >= var5) {
                  this.m_fragment = var1.substring(var4, var3);
                  break;
               }

               var6 = var1.charAt(var3);
               if (var6 == '%') {
                  if (var3 + 2 >= var5 || !isHex(var1.charAt(var3 + 1)) || !isHex(var1.charAt(var3 + 2))) {
                     throw new MalformedURIException("Fragment contains invalid escape sequence!");
                  }

                  var3 += 2;
               } else if (!isURICharacter(var6)) {
                  throw new MalformedURIException("Fragment contains invalid character: " + var6);
               }

               ++var3;
            }
         }

      }
   }

   public String getScheme() {
      return this.m_scheme;
   }

   public String getSchemeSpecificPart() {
      StringBuffer var1 = new StringBuffer();
      if (this.m_host != null || this.m_regAuthority != null) {
         var1.append("//");
         if (this.m_host != null) {
            if (this.m_userinfo != null) {
               var1.append(this.m_userinfo);
               var1.append('@');
            }

            var1.append(this.m_host);
            if (this.m_port != -1) {
               var1.append(':');
               var1.append(this.m_port);
            }
         } else {
            var1.append(this.m_regAuthority);
         }
      }

      if (this.m_path != null) {
         var1.append(this.m_path);
      }

      if (this.m_queryString != null) {
         var1.append('?');
         var1.append(this.m_queryString);
      }

      if (this.m_fragment != null) {
         var1.append('#');
         var1.append(this.m_fragment);
      }

      return var1.toString();
   }

   public String getUserinfo() {
      return this.m_userinfo;
   }

   public String getHost() {
      return this.m_host;
   }

   public int getPort() {
      return this.m_port;
   }

   public String getRegBasedAuthority() {
      return this.m_regAuthority;
   }

   public String getAuthority() {
      StringBuffer var1 = new StringBuffer();
      if (this.m_host != null || this.m_regAuthority != null) {
         var1.append("//");
         if (this.m_host != null) {
            if (this.m_userinfo != null) {
               var1.append(this.m_userinfo);
               var1.append('@');
            }

            var1.append(this.m_host);
            if (this.m_port != -1) {
               var1.append(':');
               var1.append(this.m_port);
            }
         } else {
            var1.append(this.m_regAuthority);
         }
      }

      return var1.toString();
   }

   public String getPath(boolean var1, boolean var2) {
      StringBuffer var3 = new StringBuffer(this.m_path);
      if (var1 && this.m_queryString != null) {
         var3.append('?');
         var3.append(this.m_queryString);
      }

      if (var2 && this.m_fragment != null) {
         var3.append('#');
         var3.append(this.m_fragment);
      }

      return var3.toString();
   }

   public String getPath() {
      return this.m_path;
   }

   public String getQueryString() {
      return this.m_queryString;
   }

   public String getFragment() {
      return this.m_fragment;
   }

   public void setScheme(String var1) throws MalformedURIException {
      if (var1 == null) {
         throw new MalformedURIException("Cannot set scheme from null string!");
      } else if (!isConformantSchemeName(var1)) {
         throw new MalformedURIException("The scheme is not conformant.");
      } else {
         this.m_scheme = var1.toLowerCase();
      }
   }

   public void setUserinfo(String var1) throws MalformedURIException {
      if (var1 == null) {
         this.m_userinfo = null;
      } else if (this.m_host == null) {
         throw new MalformedURIException("Userinfo cannot be set when host is null!");
      } else {
         int var2 = 0;
         int var3 = var1.length();

         for(boolean var4 = false; var2 < var3; ++var2) {
            char var5 = var1.charAt(var2);
            if (var5 == '%') {
               if (var2 + 2 >= var3 || !isHex(var1.charAt(var2 + 1)) || !isHex(var1.charAt(var2 + 2))) {
                  throw new MalformedURIException("Userinfo contains invalid escape sequence!");
               }
            } else if (!isUserinfoCharacter(var5)) {
               throw new MalformedURIException("Userinfo contains invalid character:" + var5);
            }
         }

         this.m_userinfo = var1;
      }
   }

   public void setHost(String var1) throws MalformedURIException {
      if (var1 != null && var1.length() != 0) {
         if (!isWellFormedAddress(var1)) {
            throw new MalformedURIException("Host is not a well formed address!");
         } else {
            this.m_host = var1;
            this.m_regAuthority = null;
         }
      } else {
         if (var1 != null) {
            this.m_regAuthority = null;
         }

         this.m_host = var1;
         this.m_userinfo = null;
         this.m_port = -1;
      }
   }

   public void setPort(int var1) throws MalformedURIException {
      if (var1 >= 0 && var1 <= 65535) {
         if (this.m_host == null) {
            throw new MalformedURIException("Port cannot be set when host is null!");
         }
      } else if (var1 != -1) {
         throw new MalformedURIException("Invalid port number!");
      }

      this.m_port = var1;
   }

   public void setRegBasedAuthority(String var1) throws MalformedURIException {
      if (var1 == null) {
         this.m_regAuthority = null;
      } else if (var1.length() >= 1 && this.isValidRegistryBasedAuthority(var1) && var1.indexOf(47) == -1) {
         this.m_regAuthority = var1;
         this.m_host = null;
         this.m_userinfo = null;
         this.m_port = -1;
      } else {
         throw new MalformedURIException("Registry based authority is not well formed.");
      }
   }

   public void setPath(String var1) throws MalformedURIException {
      if (var1 == null) {
         this.m_path = null;
         this.m_queryString = null;
         this.m_fragment = null;
      } else {
         this.initializePath(var1, 0);
      }

   }

   public void appendPath(String var1) throws MalformedURIException {
      if (var1 != null && var1.trim().length() != 0) {
         if (!isURIString(var1)) {
            throw new MalformedURIException("Path contains invalid character!");
         } else {
            if (this.m_path != null && this.m_path.trim().length() != 0) {
               if (this.m_path.endsWith("/")) {
                  if (var1.startsWith("/")) {
                     this.m_path = this.m_path.concat(var1.substring(1));
                  } else {
                     this.m_path = this.m_path.concat(var1);
                  }
               } else if (var1.startsWith("/")) {
                  this.m_path = this.m_path.concat(var1);
               } else {
                  this.m_path = this.m_path.concat("/" + var1);
               }
            } else if (var1.startsWith("/")) {
               this.m_path = var1;
            } else {
               this.m_path = "/" + var1;
            }

         }
      }
   }

   public void setQueryString(String var1) throws MalformedURIException {
      if (var1 == null) {
         this.m_queryString = null;
      } else {
         if (!this.isGenericURI()) {
            throw new MalformedURIException("Query string can only be set for a generic URI!");
         }

         if (this.getPath() == null) {
            throw new MalformedURIException("Query string cannot be set when path is null!");
         }

         if (!isURIString(var1)) {
            throw new MalformedURIException("Query string contains invalid character!");
         }

         this.m_queryString = var1;
      }

   }

   public void setFragment(String var1) throws MalformedURIException {
      if (var1 == null) {
         this.m_fragment = null;
      } else {
         if (!this.isGenericURI()) {
            throw new MalformedURIException("Fragment can only be set for a generic URI!");
         }

         if (this.getPath() == null) {
            throw new MalformedURIException("Fragment cannot be set when path is null!");
         }

         if (!isURIString(var1)) {
            throw new MalformedURIException("Fragment contains invalid character!");
         }

         this.m_fragment = var1;
      }

   }

   public boolean equals(Object var1) {
      if (var1 instanceof URI) {
         URI var2 = (URI)var1;
         if ((this.m_scheme == null && var2.m_scheme == null || this.m_scheme != null && var2.m_scheme != null && this.m_scheme.equals(var2.m_scheme)) && (this.m_userinfo == null && var2.m_userinfo == null || this.m_userinfo != null && var2.m_userinfo != null && this.m_userinfo.equals(var2.m_userinfo)) && (this.m_host == null && var2.m_host == null || this.m_host != null && var2.m_host != null && this.m_host.equals(var2.m_host)) && this.m_port == var2.m_port && (this.m_path == null && var2.m_path == null || this.m_path != null && var2.m_path != null && this.m_path.equals(var2.m_path)) && (this.m_queryString == null && var2.m_queryString == null || this.m_queryString != null && var2.m_queryString != null && this.m_queryString.equals(var2.m_queryString)) && (this.m_fragment == null && var2.m_fragment == null || this.m_fragment != null && var2.m_fragment != null && this.m_fragment.equals(var2.m_fragment))) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.m_scheme != null) {
         var1.append(this.m_scheme);
         var1.append(':');
      }

      var1.append(this.getSchemeSpecificPart());
      return var1.toString();
   }

   public boolean isGenericURI() {
      return this.m_host != null;
   }

   public boolean isAbsoluteURI() {
      return this.m_scheme != null;
   }

   public static boolean isConformantSchemeName(String var0) {
      if (var0 != null && var0.trim().length() != 0) {
         if (!isAlpha(var0.charAt(0))) {
            return false;
         } else {
            int var2 = var0.length();

            for(int var3 = 1; var3 < var2; ++var3) {
               char var1 = var0.charAt(var3);
               if (!isSchemeCharacter(var1)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean isWellFormedAddress(String var0) {
      if (var0 == null) {
         return false;
      } else {
         int var1 = var0.length();
         if (var1 == 0) {
            return false;
         } else if (var0.startsWith("[")) {
            return isWellFormedIPv6Reference(var0);
         } else if (!var0.startsWith(".") && !var0.startsWith("-") && !var0.endsWith("-")) {
            int var2 = var0.lastIndexOf(46);
            if (var0.endsWith(".")) {
               var2 = var0.substring(0, var2).lastIndexOf(46);
            }

            if (var2 + 1 < var1 && isDigit(var0.charAt(var2 + 1))) {
               return isWellFormedIPv4Address(var0);
            } else if (var1 > 255) {
               return false;
            } else {
               int var4 = 0;

               for(int var5 = 0; var5 < var1; ++var5) {
                  char var3 = var0.charAt(var5);
                  if (var3 == '.') {
                     if (!isAlphanum(var0.charAt(var5 - 1))) {
                        return false;
                     }

                     if (var5 + 1 < var1 && !isAlphanum(var0.charAt(var5 + 1))) {
                        return false;
                     }

                     var4 = 0;
                  } else {
                     if (!isAlphanum(var3) && var3 != '-') {
                        return false;
                     }

                     ++var4;
                     if (var4 > 63) {
                        return false;
                     }
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   public static boolean isWellFormedIPv4Address(String var0) {
      int var1 = var0.length();
      int var3 = 0;
      int var4 = 0;

      for(int var5 = 0; var5 < var1; ++var5) {
         char var2 = var0.charAt(var5);
         if (var2 != '.') {
            if (!isDigit(var2)) {
               return false;
            }

            ++var4;
            if (var4 > 3) {
               return false;
            }

            if (var4 == 3) {
               char var6 = var0.charAt(var5 - 2);
               char var7 = var0.charAt(var5 - 1);
               if (var6 >= '2' && (var6 != '2' || var7 >= '5' && (var7 != '5' || var2 > '5'))) {
                  return false;
               }
            }
         } else {
            if (var5 > 0 && !isDigit(var0.charAt(var5 - 1)) || var5 + 1 < var1 && !isDigit(var0.charAt(var5 + 1))) {
               return false;
            }

            var4 = 0;
            ++var3;
            if (var3 > 3) {
               return false;
            }
         }
      }

      return var3 == 3;
   }

   public static boolean isWellFormedIPv6Reference(String var0) {
      int var1 = var0.length();
      int var2 = 1;
      int var3 = var1 - 1;
      if (var1 > 2 && var0.charAt(0) == '[' && var0.charAt(var3) == ']') {
         int[] var4 = new int[1];
         var2 = scanHexSequence(var0, var2, var3, var4);
         if (var2 == -1) {
            return false;
         } else if (var2 == var3) {
            return var4[0] == 8;
         } else if (var2 + 1 < var3 && var0.charAt(var2) == ':') {
            if (var0.charAt(var2 + 1) == ':') {
               if (++var4[0] > 8) {
                  return false;
               } else {
                  var2 += 2;
                  if (var2 == var3) {
                     return true;
                  } else {
                     int var5 = var4[0];
                     var2 = scanHexSequence(var0, var2, var3, var4);
                     return var2 == var3 || var2 != -1 && isWellFormedIPv4Address(var0.substring(var4[0] > var5 ? var2 + 1 : var2, var3));
                  }
               }
            } else {
               return var4[0] == 6 && isWellFormedIPv4Address(var0.substring(var2 + 1, var3));
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static int scanHexSequence(String var0, int var1, int var2, int[] var3) {
      int var5 = 0;

      for(int var6 = var1; var1 < var2; ++var1) {
         char var4 = var0.charAt(var1);
         if (var4 == ':') {
            if (var5 > 0 && ++var3[0] > 8) {
               return -1;
            }

            if (var5 == 0 || var1 + 1 < var2 && var0.charAt(var1 + 1) == ':') {
               return var1;
            }

            var5 = 0;
         } else {
            if (!isHex(var4)) {
               if (var4 == '.' && var5 < 4 && var5 > 0 && var3[0] <= 6) {
                  int var7 = var1 - var5 - 1;
                  return var7 >= var6 ? var7 : var7 + 1;
               }

               return -1;
            }

            ++var5;
            if (var5 > 4) {
               return -1;
            }
         }
      }

      return var5 > 0 && ++var3[0] <= 8 ? var2 : -1;
   }

   private static boolean isDigit(char var0) {
      return var0 >= '0' && var0 <= '9';
   }

   private static boolean isHex(char var0) {
      return var0 <= 'f' && (fgLookupTable[var0] & 64) != 0;
   }

   private static boolean isAlpha(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z';
   }

   private static boolean isAlphanum(char var0) {
      return var0 <= 'z' && (fgLookupTable[var0] & 48) != 0;
   }

   private static boolean isReservedCharacter(char var0) {
      return var0 <= ']' && (fgLookupTable[var0] & 1) != 0;
   }

   private static boolean isUnreservedCharacter(char var0) {
      return var0 <= '~' && (fgLookupTable[var0] & 50) != 0;
   }

   private static boolean isURICharacter(char var0) {
      return var0 <= '~' && (fgLookupTable[var0] & 51) != 0;
   }

   private static boolean isSchemeCharacter(char var0) {
      return var0 <= 'z' && (fgLookupTable[var0] & 52) != 0;
   }

   private static boolean isUserinfoCharacter(char var0) {
      return var0 <= 'z' && (fgLookupTable[var0] & 58) != 0;
   }

   private static boolean isPathCharacter(char var0) {
      return var0 <= '~' && (fgLookupTable[var0] & 178) != 0;
   }

   private static boolean isURIString(String var0) {
      if (var0 == null) {
         return false;
      } else {
         int var1 = var0.length();
         boolean var2 = false;

         for(int var3 = 0; var3 < var1; ++var3) {
            char var4 = var0.charAt(var3);
            if (var4 == '%') {
               if (var3 + 2 >= var1 || !isHex(var0.charAt(var3 + 1)) || !isHex(var0.charAt(var3 + 2))) {
                  return false;
               }

               var3 += 2;
            } else if (!isURICharacter(var4)) {
               return false;
            }
         }

         return true;
      }
   }

   static {
      byte[] var10000;
      for(int var0 = 48; var0 <= 57; ++var0) {
         var10000 = fgLookupTable;
         var10000[var0] = (byte)(var10000[var0] | 96);
      }

      for(int var1 = 65; var1 <= 70; ++var1) {
         var10000 = fgLookupTable;
         var10000[var1] = (byte)(var10000[var1] | 80);
         var10000 = fgLookupTable;
         var10000[var1 + 32] = (byte)(var10000[var1 + 32] | 80);
      }

      for(int var2 = 71; var2 <= 90; ++var2) {
         var10000 = fgLookupTable;
         var10000[var2] = (byte)(var10000[var2] | 16);
         var10000 = fgLookupTable;
         var10000[var2 + 32] = (byte)(var10000[var2 + 32] | 16);
      }

      var10000 = fgLookupTable;
      var10000[59] = (byte)(var10000[59] | 1);
      var10000 = fgLookupTable;
      var10000[47] = (byte)(var10000[47] | 1);
      var10000 = fgLookupTable;
      var10000[63] = (byte)(var10000[63] | 1);
      var10000 = fgLookupTable;
      var10000[58] = (byte)(var10000[58] | 1);
      var10000 = fgLookupTable;
      var10000[64] = (byte)(var10000[64] | 1);
      var10000 = fgLookupTable;
      var10000[38] = (byte)(var10000[38] | 1);
      var10000 = fgLookupTable;
      var10000[61] = (byte)(var10000[61] | 1);
      var10000 = fgLookupTable;
      var10000[43] = (byte)(var10000[43] | 1);
      var10000 = fgLookupTable;
      var10000[36] = (byte)(var10000[36] | 1);
      var10000 = fgLookupTable;
      var10000[44] = (byte)(var10000[44] | 1);
      var10000 = fgLookupTable;
      var10000[91] = (byte)(var10000[91] | 1);
      var10000 = fgLookupTable;
      var10000[93] = (byte)(var10000[93] | 1);
      var10000 = fgLookupTable;
      var10000[45] = (byte)(var10000[45] | 2);
      var10000 = fgLookupTable;
      var10000[95] = (byte)(var10000[95] | 2);
      var10000 = fgLookupTable;
      var10000[46] = (byte)(var10000[46] | 2);
      var10000 = fgLookupTable;
      var10000[33] = (byte)(var10000[33] | 2);
      var10000 = fgLookupTable;
      var10000[126] = (byte)(var10000[126] | 2);
      var10000 = fgLookupTable;
      var10000[42] = (byte)(var10000[42] | 2);
      var10000 = fgLookupTable;
      var10000[39] = (byte)(var10000[39] | 2);
      var10000 = fgLookupTable;
      var10000[40] = (byte)(var10000[40] | 2);
      var10000 = fgLookupTable;
      var10000[41] = (byte)(var10000[41] | 2);
      var10000 = fgLookupTable;
      var10000[43] = (byte)(var10000[43] | 4);
      var10000 = fgLookupTable;
      var10000[45] = (byte)(var10000[45] | 4);
      var10000 = fgLookupTable;
      var10000[46] = (byte)(var10000[46] | 4);
      var10000 = fgLookupTable;
      var10000[59] = (byte)(var10000[59] | 8);
      var10000 = fgLookupTable;
      var10000[58] = (byte)(var10000[58] | 8);
      var10000 = fgLookupTable;
      var10000[38] = (byte)(var10000[38] | 8);
      var10000 = fgLookupTable;
      var10000[61] = (byte)(var10000[61] | 8);
      var10000 = fgLookupTable;
      var10000[43] = (byte)(var10000[43] | 8);
      var10000 = fgLookupTable;
      var10000[36] = (byte)(var10000[36] | 8);
      var10000 = fgLookupTable;
      var10000[44] = (byte)(var10000[44] | 8);
      var10000 = fgLookupTable;
      var10000[59] = (byte)(var10000[59] | 128);
      var10000 = fgLookupTable;
      var10000[47] = (byte)(var10000[47] | 128);
      var10000 = fgLookupTable;
      var10000[58] = (byte)(var10000[58] | 128);
      var10000 = fgLookupTable;
      var10000[64] = (byte)(var10000[64] | 128);
      var10000 = fgLookupTable;
      var10000[38] = (byte)(var10000[38] | 128);
      var10000 = fgLookupTable;
      var10000[61] = (byte)(var10000[61] | 128);
      var10000 = fgLookupTable;
      var10000[43] = (byte)(var10000[43] | 128);
      var10000 = fgLookupTable;
      var10000[36] = (byte)(var10000[36] | 128);
      var10000 = fgLookupTable;
      var10000[44] = (byte)(var10000[44] | 128);
      DEBUG = false;
   }

   public static class MalformedURIException extends IOException {
      static final long serialVersionUID = -6695054834342951930L;

      public MalformedURIException() {
      }

      public MalformedURIException(String var1) {
         super(var1);
      }
   }
}
