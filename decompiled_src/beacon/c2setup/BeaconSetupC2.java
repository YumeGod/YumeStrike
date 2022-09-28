package beacon.c2setup;

import beacon.BeaconC2;
import c2profile.Profile;
import common.ScListener;
import server.Resources;

public abstract class BeaconSetupC2 {
   protected Resources resources;
   protected BeaconC2 controller;
   protected ScListener listener;

   public BeaconSetupC2(Resources var1, ScListener var2, BeaconC2 var3) {
      this.resources = var1;
      this.listener = var2;
      this.controller = var3;
   }

   public Resources getResources() {
      return this.resources;
   }

   public BeaconC2 getController() {
      return this.controller;
   }

   public ScListener getListener() {
      return this.listener;
   }

   public Profile getProfile() {
      return this.listener.getProfile();
   }

   public abstract void start() throws Exception;

   public abstract void stop();
}
