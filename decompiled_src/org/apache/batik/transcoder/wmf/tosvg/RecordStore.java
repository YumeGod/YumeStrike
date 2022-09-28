package org.apache.batik.transcoder.wmf.tosvg;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

/** @deprecated */
public class RecordStore {
   private transient URL url;
   protected transient int numRecords;
   protected transient int numObjects;
   public transient int lastObjectIdx;
   protected transient int vpX;
   protected transient int vpY;
   protected transient int vpW;
   protected transient int vpH;
   protected transient Vector records;
   protected transient Vector objectVector;
   protected transient boolean bReading = false;

   public RecordStore() {
      this.reset();
   }

   public void reset() {
      this.numRecords = 0;
      this.vpX = 0;
      this.vpY = 0;
      this.vpW = 1000;
      this.vpH = 1000;
      this.numObjects = 0;
      this.records = new Vector(20, 20);
      this.objectVector = new Vector();
   }

   synchronized void setReading(boolean var1) {
      this.bReading = var1;
   }

   synchronized boolean isReading() {
      return this.bReading;
   }

   public boolean read(DataInputStream var1) throws IOException {
      this.setReading(true);
      this.reset();
      short var2 = 0;
      this.numRecords = 0;
      this.numObjects = var1.readShort();
      this.objectVector.ensureCapacity(this.numObjects);

      for(int var3 = 0; var3 < this.numObjects; ++var3) {
         this.objectVector.add(new GdiObject(var3, false));
      }

      while(var2 != -1) {
         var2 = var1.readShort();
         if (var2 == -1) {
            break;
         }

         short var4;
         Object var7;
         switch (var2) {
            case 763:
            case 1313:
            case 1583:
            case 2610:
               var4 = var1.readShort();
               byte[] var5 = new byte[var4];

               for(int var6 = 0; var6 < var4; ++var6) {
                  var5[var6] = var1.readByte();
               }

               String var9 = new String(var5);
               var7 = new MetaRecord.StringRecord(var9);
               break;
            default:
               var7 = new MetaRecord();
         }

         var4 = var1.readShort();
         ((MetaRecord)var7).numPoints = var4;
         ((MetaRecord)var7).functionId = var2;

         for(int var8 = 0; var8 < var4; ++var8) {
            ((MetaRecord)var7).AddElement(new Integer(var1.readShort()));
         }

         this.records.add(var7);
         ++this.numRecords;
      }

      this.setReading(false);
      return true;
   }

   public void addObject(int var1, Object var2) {
      for(int var3 = 0; var3 < this.numObjects; ++var3) {
         GdiObject var4 = (GdiObject)this.objectVector.get(var3);
         if (!var4.used) {
            var4.Setup(var1, var2);
            this.lastObjectIdx = var3;
            break;
         }
      }

   }

   public void addObjectAt(int var1, Object var2, int var3) {
      if (var3 != 0 && var3 <= this.numObjects) {
         this.lastObjectIdx = var3;

         for(int var4 = 0; var4 < this.numObjects; ++var4) {
            GdiObject var5 = (GdiObject)this.objectVector.get(var4);
            if (var4 == var3) {
               var5.Setup(var1, var2);
               break;
            }
         }

      } else {
         this.addObject(var1, var2);
      }
   }

   public URL getUrl() {
      return this.url;
   }

   public void setUrl(URL var1) {
      this.url = var1;
   }

   public GdiObject getObject(int var1) {
      return (GdiObject)this.objectVector.get(var1);
   }

   public MetaRecord getRecord(int var1) {
      return (MetaRecord)this.records.get(var1);
   }

   public int getNumRecords() {
      return this.numRecords;
   }

   public int getNumObjects() {
      return this.numObjects;
   }

   public int getVpX() {
      return this.vpX;
   }

   public int getVpY() {
      return this.vpY;
   }

   public int getVpW() {
      return this.vpW;
   }

   public int getVpH() {
      return this.vpH;
   }

   public void setVpX(int var1) {
      this.vpX = var1;
   }

   public void setVpY(int var1) {
      this.vpY = var1;
   }

   public void setVpW(int var1) {
      this.vpW = var1;
   }

   public void setVpH(int var1) {
      this.vpH = var1;
   }
}
