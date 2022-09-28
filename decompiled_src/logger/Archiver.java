package logger;

import common.ArchiveMap;
import common.CommonUtils;
import common.Informant;
import java.util.Iterator;
import java.util.LinkedList;
import server.PersistentData;
import server.Resources;

public class Archiver extends ProcessBackend {
   protected Resources resources;
   protected PersistentData store;
   protected LinkedList model = null;

   public Archiver(Resources var1) {
      this.resources = var1;
      this.load();
   }

   public void load() {
      this.store = new PersistentData("archives", this);
      this.model = (LinkedList)this.store.getValue(new LinkedList());
      Iterator var1 = this.model.iterator();

      while(var1.hasNext()) {
         ArchiveMap var2 = (ArchiveMap)var1.next();
         this.resources.broadcast("archives", var2);
      }

      this.start("archiver");
   }

   public void reset() {
      synchronized(this) {
         this.tasks = new LinkedList();
         this.model = new LinkedList();
         this.store.save(this.model);
      }
   }

   public void process(Object var1) {
      Informant var2 = (Informant)var1;
      if (var2.hasInformation()) {
         ArchiveMap var3 = new ArchiveMap(var2.archive());
         this.resources.broadcast("archives", var3);
         synchronized(this) {
            this.model.add(var3);

            while(this.model.size() > CommonUtils.limit("archives")) {
               this.model.removeFirst();
            }

            this.store.save(this.model);
         }
      }

   }
}
