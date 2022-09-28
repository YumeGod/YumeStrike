package org.apache.commons.io;

import java.io.File;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.Vector;

public class FileCleaner {
   static ReferenceQueue q = new ReferenceQueue();
   static Collection trackers = new Vector();
   static volatile boolean exitWhenFinished = false;
   static Thread reaper;

   public static void track(File file, Object marker) {
      track(file, marker, (FileDeleteStrategy)null);
   }

   public static void track(File file, Object marker, FileDeleteStrategy deleteStrategy) {
      if (file == null) {
         throw new NullPointerException("The file must not be null");
      } else {
         addTracker(file.getPath(), marker, deleteStrategy);
      }
   }

   public static void track(String path, Object marker) {
      track(path, marker, (FileDeleteStrategy)null);
   }

   public static void track(String path, Object marker, FileDeleteStrategy deleteStrategy) {
      if (path == null) {
         throw new NullPointerException("The path must not be null");
      } else {
         addTracker(path, marker, deleteStrategy);
      }
   }

   private static synchronized void addTracker(String path, Object marker, FileDeleteStrategy deleteStrategy) {
      if (exitWhenFinished) {
         throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
      } else {
         if (reaper == null) {
            reaper = new Reaper();
            reaper.start();
         }

         trackers.add(new Tracker(path, deleteStrategy, marker, q));
      }
   }

   public static int getTrackCount() {
      return trackers.size();
   }

   public static synchronized void exitWhenFinished() {
      exitWhenFinished = true;
      if (reaper != null) {
         Thread var0 = reaper;
         synchronized(var0) {
            reaper.interrupt();
         }
      }

   }

   static final class Tracker extends PhantomReference {
      private final String path;
      private final FileDeleteStrategy deleteStrategy;

      Tracker(String path, FileDeleteStrategy deleteStrategy, Object marker, ReferenceQueue queue) {
         super(marker, queue);
         this.path = path;
         this.deleteStrategy = deleteStrategy == null ? FileDeleteStrategy.NORMAL : deleteStrategy;
      }

      public boolean delete() {
         return this.deleteStrategy.deleteQuietly(new File(this.path));
      }
   }

   static final class Reaper extends Thread {
      Reaper() {
         super("File Reaper");
         this.setPriority(10);
         this.setDaemon(true);
      }

      public void run() {
         while(!FileCleaner.exitWhenFinished || FileCleaner.trackers.size() > 0) {
            Tracker tracker = null;

            try {
               tracker = (Tracker)FileCleaner.q.remove();
            } catch (Exception var3) {
               continue;
            }

            if (tracker != null) {
               tracker.delete();
               tracker.clear();
               FileCleaner.trackers.remove(tracker);
            }
         }

      }
   }
}
