package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class BeaconEntry implements Serializable, Loggable {
   public static final int LINK_NONE = 0;
   public static final int LINK_GOOD = 1;
   public static final int LINK_BROKEN = 2;
   protected String id = "";
   protected String pid = "";
   protected String ver = "";
   protected String intz = "";
   protected String comp = "";
   protected String user = "";
   protected String is64 = "0";
   protected String ext = "";
   protected long last = System.currentTimeMillis();
   protected long diff = 0L;
   protected int state = 0;
   protected int hint = 0;
   protected String pbid = "";
   protected String note = "";
   protected String barch = "";
   protected boolean alive = true;
   protected String port = "";
   protected boolean sane = false;
   protected String chst = null;
   protected String proc = "";
   protected String accent = "";
   protected String lname = "";
   public static final int METADATA_FLAG_NOTHING = 1;
   public static final int METADATA_FLAG_X64_AGENT = 2;
   public static final int METADATA_FLAG_X64_SYSTEM = 4;
   public static final int METADATA_FLAG_ADMIN = 8;

   public String getId() {
      return this.id;
   }

   public boolean sane() {
      return this.sane;
   }

   public String getListenerName() {
      return this.lname;
   }

   public String getPort() {
      return this.port;
   }

   public void die() {
      this.alive = false;
   }

   public boolean isAlive() {
      return this.alive;
   }

   public boolean isActive() {
      if (!this.isAlive()) {
         return false;
      } else {
         return !this.isLinked() || this.getLinkState() == 1;
      }
   }

   public String getComputer() {
      return this.comp;
   }

   public boolean isEmpty() {
      return this.intz == null || this.intz.length() == 0;
   }

   public String getUser() {
      return this.user;
   }

   public String getInternal() {
      return this.intz;
   }

   public String getExternal() {
      return this.ext;
   }

   public String getPid() {
      return this.isSSH() ? "" : this.pid;
   }

   public PivotHint getPivotHint() {
      return new PivotHint(this.hint);
   }

   public double getVersion() {
      try {
         if (this.isSSH() && this.ver.startsWith("ssh-CYGWIN_NT-")) {
            return Double.parseDouble(CommonUtils.strip(this.ver, "ssh-CYGWIN_NT-"));
         } else {
            return this.isBeacon() ? Double.parseDouble(this.ver) : 0.0;
         }
      } catch (Exception var2) {
         return 0.0;
      }
   }

   public String getNote() {
      return this.note;
   }

   public String getParentId() {
      return this.pbid;
   }

   public boolean isLinked() {
      return this.pbid.length() > 0;
   }

   public int getLinkState() {
      return this.state;
   }

   public String arch() {
      return this.barch;
   }

   public boolean is64() {
      if (!this.is64.equals("1") && !this.is64.equals("0")) {
         CommonUtils.print_warn("is64 is: '" + this.is64 + "'");
      }

      return this.is64.equals("1");
   }

   public boolean isAdmin() {
      return this.getUser().endsWith(" *");
   }

   public void setExternal(String var1) {
      if (this.checkExt(var1)) {
         this.ext = var1;
      } else {
         CommonUtils.print_error("Refused to assign: '" + var1 + "' [was: '" + this.ext + "'] as external address to Beacon: '" + this.id + "'");
      }

   }

   public void setLastCheckin(long var1) {
      this.last = var1;
   }

   public void setNote(String var1) {
      this.note = var1;
   }

   public void setAccent(String var1) {
      this.accent = var1;
   }

   public String getAccent() {
      return this.accent;
   }

   public boolean idle(long var1) {
      return this.diff >= var1;
   }

   public String getLastCheckin() {
      String var1 = "ms";
      long var2 = this.diff;
      if (var2 > 1000L) {
         var2 /= 1000L;
         var1 = "s";
         if (var2 > 60L) {
            var2 /= 60L;
            var1 = "m";
         }

         if (var2 > 60L) {
            var2 /= 60L;
            var1 = "h";
         }

         return var2 + var1;
      } else {
         return var2 + var1;
      }
   }

   public BeaconEntry(byte[] var1, String var2, String var3, String var4) {
      boolean var5;
      try {
         DataParser var6 = new DataParser(var1);
         var6.big();
         var6.consume(20);
         this.id = Long.toString(CommonUtils.toUnsignedInt(var6.readInt()));
         this.pid = Long.toString(CommonUtils.toUnsignedInt(var6.readInt()));
         this.port = Integer.toString(CommonUtils.toUnsignedShort(var6.readShort()));
         byte var7 = var6.readByte();
         if (CommonUtils.Flag(var7, 1)) {
            this.barch = "";
            this.pid = "";
            this.is64 = "";
         } else if (CommonUtils.Flag(var7, 2)) {
            this.barch = "x64";
         } else {
            this.barch = "x86";
         }

         this.is64 = CommonUtils.Flag(var7, 4) ? "1" : "0";
         var5 = CommonUtils.Flag(var7, 8);
      } catch (IOException var8) {
         MudgeSanity.logException("Could not parse metadata!", var8, false);
         this.sane = false;
         return;
      }

      String var9 = CommonUtils.bString(Arrays.copyOfRange(var1, 31, var1.length), var2);
      String[] var10 = var9.split("\t");
      if (var10.length > 0) {
         this.ver = var10[0];
      }

      if (var10.length > 1) {
         this.intz = var10[1];
      }

      if (var10.length > 2) {
         this.comp = var10[2];
      }

      if (var10.length > 3) {
         this.user = var10[3];
      }

      if (var5) {
         this.user = this.user + " *";
      }

      if (var10.length > 4) {
         this.proc = var10[4];
      }

      this.ext = var3;
      this.chst = var2;
      this.lname = var4;
      this.sane = this.sanity();
   }

   public String getCharset() {
      return this.chst;
   }

   public boolean sanity() {
      LinkedList var1 = new LinkedList();

      try {
         return this._sanity(var1);
      } catch (Exception var3) {
         this.id = "0";
         this.intz = "";
         MudgeSanity.logException("Validator blew up!", var3, false);
         return false;
      }
   }

   public boolean checkExt(String var1) {
      if (var1 == null) {
         return true;
      } else if ("".equals(var1)) {
         return true;
      } else {
         String var2;
         if (var1.endsWith(" ⚯ ⚯") && var1.length() > 5) {
            var2 = var1.substring(0, var1.length() - 4);
         } else if (var1.endsWith(" ⚯⚯") && var1.length() > 4) {
            var2 = var1.substring(0, var1.length() - 3);
         } else {
            var2 = var1;
         }

         return CommonUtils.isIP(var2) || CommonUtils.isIPv6(var2) || "unknown".equals(var2);
      }
   }

   public String getProcess() {
      return this.proc;
   }

   public boolean _sanity(LinkedList var1) {
      if (!CommonUtils.isNumber(this.id)) {
         var1.add("id '" + this.id + "' is not a number");
         this.id = "0";
      }

      if (!"".equals(this.intz) && !CommonUtils.isIP(this.intz) && !CommonUtils.isIPv6(this.intz) && !"unknown".equals(this.intz)) {
         var1.add("internal address '" + this.intz + "' is not an address");
         this.intz = "";
      }

      if (!this.checkExt(this.ext)) {
         var1.add("external address '" + this.ext + "' is not an address");
         this.ext = "";
      }

      if (!"".equals(this.pid) && !CommonUtils.isNumber(this.pid)) {
         var1.add("pid '" + this.pid + "' is not a number");
         this.pid = "0";
      }

      if (!"".equals(this.port) && !CommonUtils.isNumber(this.port)) {
         var1.add("port '" + this.port + "' is not a number");
         this.port = "";
      }

      if (!"".equals(this.is64) && !CommonUtils.isNumber(this.is64)) {
         var1.add("is64 '" + this.is64 + "' is not a number");
         this.is64 = "";
      }

      if (this.comp != null && this.comp.length() > 64) {
         var1.add("comp '" + this.comp + "' is too long. Truncating");
         this.comp = this.comp.substring(0, 63);
      }

      if (this.user != null && this.user.length() > 64) {
         var1.add("user '" + this.user + "' is too long. Truncating");
         this.user = this.user.substring(0, 63);
      }

      if (var1.size() <= 0) {
         return true;
      } else {
         Iterator var2 = var1.iterator();
         CommonUtils.print_error("Beacon entry did not validate");

         while(var2.hasNext()) {
            System.out.println("\t" + var2.next());
         }

         return false;
      }
   }

   public BeaconEntry(String var1) {
      this.id = var1;
      this.sane = this.sanity();
   }

   public void touch() {
      this.diff = System.currentTimeMillis() - this.last;
   }

   public BeaconEntry copy() {
      BeaconEntry var1 = new BeaconEntry(this.id);
      var1.pid = this.pid;
      var1.ver = this.ver;
      var1.intz = this.intz;
      var1.comp = this.comp;
      var1.user = this.user;
      var1.is64 = this.is64;
      var1.ext = this.ext;
      var1.diff = this.diff;
      var1.last = this.last;
      var1.state = this.state;
      var1.pbid = this.pbid;
      var1.note = this.note;
      var1.alive = this.alive;
      var1.barch = this.barch;
      var1.port = this.port;
      var1.chst = this.chst;
      var1.hint = this.hint;
      var1.proc = this.proc;
      var1.accent = this.accent;
      var1.lname = this.lname;
      return var1;
   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      var1.put("external", this.ext);
      var1.put("internal", this.intz);
      var1.put("host", this.intz);
      var1.put("user", this.user);
      var1.put("computer", this.comp);
      var1.put("last", this.diff + "");
      var1.put("lastf", this.getLastCheckin());
      var1.put("id", this.id);
      var1.put("pid", this.getPid());
      var1.put("is64", this.is64);
      var1.put("pbid", this.pbid);
      var1.put("note", this.note);
      var1.put("barch", this.barch);
      var1.put("arch", this.barch);
      var1.put("port", this.getPort());
      var1.put("charset", this.getCharset());
      var1.put("phint", this.hint + "");
      var1.put("process", this.proc);
      var1.put("_accent", this.accent);
      var1.put("listener", this.lname);
      if (this.alive) {
         var1.put("alive", "true");
      } else {
         var1.put("alive", "false");
      }

      if (this.state != 0) {
         if (this.state == 1) {
            var1.put("state", "good");
         } else if (this.state == 2) {
            var1.put("state", "broken");
         }
      }

      var1.put("os", this.getOperatingSystem());
      var1.put("ver", Double.toString(this.getVersion()));
      if (this.isSSH()) {
         var1.put("session", "ssh");
      } else if (this.isBeacon()) {
         var1.put("session", "beacon");
      } else {
         var1.put("session", "unknown");
      }

      return var1;
   }

   public boolean wantsMetadata() {
      return this.user.length() == 0;
   }

   public String title() {
      return this.isBeacon() ? this.title("Beacon") : "SSH " + this.intz;
   }

   public String title(String var1) {
      return var1 + " " + this.intz + "@" + this.pid;
   }

   public String toString() {
      return this.getId() + " -> " + this.title() + ", " + this.getLastCheckin();
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      var1.push(SleepUtils.getHashWrapper(this.toMap()));
      var1.push(SleepUtils.getScalar(this.id));
      return var1;
   }

   public void link(String var1, int var2) {
      this.pbid = var1;
      this.state = 1;
      this.hint = var2;
   }

   public void delink() {
      this.state = 2;
      this.lname = "";
   }

   public String getBeaconId() {
      return this.id;
   }

   public String getLogFile() {
      return this.isSSH() ? "ssh_" + this.id + ".log" : "beacon_" + this.id + ".log";
   }

   public String getLogFolder() {
      return null;
   }

   public boolean isBeacon() {
      return !this.isSSH();
   }

   public boolean isSSH() {
      return this.ver.startsWith("ssh-");
   }

   public String getOperatingSystem() {
      if (this.isBeacon()) {
         return "Windows";
      } else if ("ssh-".equals(this.ver)) {
         return "Unknown";
      } else if ("ssh-Darwin".equals(this.ver)) {
         return "MacOS X";
      } else {
         return this.ver.startsWith("ssh-CYGWIN_NT-") ? "Windows" : this.ver.substring(4);
      }
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.writeBytes(CommonUtils.formatLogDate(System.currentTimeMillis()));
      var1.writeBytes(" ");
      var1.writeBytes("[metadata] ");
      if (this.isLinked()) {
         var1.writeBytes("beacon_" + this.getParentId() + " -> " + this.getInternal() + "; ");
      } else if ("".equals(this.getExternal())) {
         var1.writeBytes("unknown <- " + this.getInternal() + "; ");
      } else {
         var1.writeBytes(this.getExternal() + " <- " + this.getInternal() + "; ");
      }

      if (this.isSSH()) {
         CommonUtils.writeUTF8(var1, "computer: " + this.getComputer() + "; ");
         CommonUtils.writeUTF8(var1, "user: " + this.getUser() + "; ");
         var1.writeBytes("os: " + this.getOperatingSystem() + "; ");
         var1.writeBytes("port: " + this.getPort());
      } else {
         CommonUtils.writeUTF8(var1, "computer: " + this.getComputer() + "; ");
         CommonUtils.writeUTF8(var1, "user: " + this.getUser() + "; ");
         var1.writeBytes("process: " + this.getProcess() + "; ");
         var1.writeBytes("pid: " + this.getPid() + "; ");
         var1.writeBytes("os: " + this.getOperatingSystem() + "; ");
         var1.writeBytes("version: " + this.getVersion() + "; ");
         var1.writeBytes("beacon arch: " + this.barch);
         if (this.is64()) {
            var1.writeBytes(" (x64)");
         }
      }

      var1.writeBytes("\n");
   }
}
