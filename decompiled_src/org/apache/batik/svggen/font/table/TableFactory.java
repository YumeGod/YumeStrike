package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TableFactory {
   public static Table create(DirectoryEntry var0, RandomAccessFile var1) throws IOException {
      Object var2 = null;
      switch (var0.getTag()) {
         case 1111577413:
         case 1128678944:
         case 1146308935:
         case 1161970772:
         case 1161972803:
         case 1161974595:
         case 1195656518:
         case 1246975046:
         case 1280594760:
         case 1296909912:
         case 1296913220:
         case 1346587732:
         case 1447316824:
         case 1719034226:
         case 1734439792:
         case 1751412088:
         case 1986553185:
         case 1986884728:
         default:
            break;
         case 1196445523:
            var2 = new GposTable(var0, var1);
            break;
         case 1196643650:
            var2 = new GsubTable(var0, var1);
            break;
         case 1330851634:
            var2 = new Os2Table(var0, var1);
            break;
         case 1668112752:
            var2 = new CmapTable(var0, var1);
            break;
         case 1668707360:
            var2 = new CvtTable(var0, var1);
            break;
         case 1718642541:
            var2 = new FpgmTable(var0, var1);
            break;
         case 1735162214:
            var2 = new GlyfTable(var0, var1);
            break;
         case 1751474532:
            var2 = new HeadTable(var0, var1);
            break;
         case 1751672161:
            var2 = new HheaTable(var0, var1);
            break;
         case 1752003704:
            var2 = new HmtxTable(var0, var1);
            break;
         case 1801810542:
            var2 = new KernTable(var0, var1);
            break;
         case 1819239265:
            var2 = new LocaTable(var0, var1);
            break;
         case 1835104368:
            var2 = new MaxpTable(var0, var1);
            break;
         case 1851878757:
            var2 = new NameTable(var0, var1);
            break;
         case 1886352244:
            var2 = new PostTable(var0, var1);
            break;
         case 1886545264:
            var2 = new PrepTable(var0, var1);
      }

      return (Table)var2;
   }
}
