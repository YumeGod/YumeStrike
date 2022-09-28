package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class PDFCMap extends PDFStream {
   public static final String ENC_GB_EUC_H = "GB-EUC-H";
   public static final String ENC_GB_EUC_V = "GB_EUC_V";
   public static final String ENC_GBPC_EUC_H = "GBpc-EUC-H";
   public static final String ENC_GBPC_EUC_V = "GBpc-EUC-V";
   public static final String ENC_GBK_EUC_H = "GBK-EUC-H";
   public static final String ENC_GBK_EUC_V = "GBK-EUC-V";
   public static final String ENC_GBKP_EUC_H = "GBKp-EUC-H";
   public static final String ENC_GBKP_EUC_V = "GBKp-EUC-V";
   public static final String ENC_GBK2K_H = "GBK2K-H";
   public static final String ENC_GBK2K_V = "GBK2K-V";
   public static final String ENC_UNIGB_UCS2_H = "UniGB-UCS2-H";
   public static final String ENC_UNIGB_UCS2_V = "UniGB-UCS2-V";
   public static final String ENC_B5PC_H = "B5pc-H";
   public static final String ENC_B5PC_V = "B5pc-V";
   public static final String ENC_HKSCS_B5_H = "HKscs-B5-H";
   public static final String ENC_HKSCS_B5_V = "HKscs-B5-V";
   public static final String ENC_ETEN_B5_H = "ETen-B5-H";
   public static final String ENC_ETEN_B5_V = "ETen-B5-V";
   public static final String ENC_ETENMS_B5_H = "ETenms-B5-H";
   public static final String ENC_ETENMS_B5_V = "ETenms-B5-V";
   public static final String ENC_CNS_EUC_H = "CNS-EUC-H";
   public static final String ENC_CNS_EUC_V = "CNS-EUC-V";
   public static final String ENC_UNICNS_UCS2_H = "UniCNS-UCS2-H";
   public static final String ENC_UNICNS_UCS2_V = "UniCNS-UCS2-V";
   public static final String ENC_83PV_RKSJ_H = "83pv-RKSJ-H";
   public static final String ENC_90MS_RKSJ_H = "90ms-RKSJ-H";
   public static final String ENC_90MS_RKSJ_V = "90ms-RKSJ-V";
   public static final String ENC_90MSP_RKSJ_H = "90msp-RKSJ-H";
   public static final String ENC_90MSP_RKSJ_V = "90msp-RKSJ-V";
   public static final String ENC_90PV_RKSJ_H = "90pv-RKSJ-H";
   public static final String ENC_ADD_RKSJ_H = "Add-RKSJ-H";
   public static final String ENC_ADD_RKSJ_V = "Add-RKSJ-V";
   public static final String ENC_EUC_H = "EUC-H";
   public static final String ENC_EUC_V = "EUC-V";
   public static final String ENC_EXT_RKSJ_H = "Ext-RKSJ-H";
   public static final String ENC_EXT_RKSJ_V = "Ext-RKSJ-V";
   public static final String ENC_H = "H";
   public static final String ENC_V = "V";
   public static final String ENC_UNIJIS_UCS2_H = "UniJIS-UCS2-H";
   public static final String ENC_UNIJIS_UCS2_V = "UniJIS-UCS2-V";
   public static final String ENC_UNIJIS_UCS2_HW_H = "UniJIS-UCS2-HW-H";
   public static final String ENC_UNIJIS_UCS2_HW_V = "UniJIS-UCS2-HW-V";
   public static final String ENC_KSC_EUC_H = "KSC-EUC-H";
   public static final String ENC_KSC_EUC_V = "KSC-EUC-V";
   public static final String ENC_KSCMS_UHC_H = "KSCms-UHC-H";
   public static final String ENC_KSCMS_UHC_V = "KSCms-UHC-V";
   public static final String ENC_KSCMS_UHC_HW_H = "KSCms-UHC-HW-H";
   public static final String ENC_KSCMS_UHC_HW_V = "KSCms-UHC-HW-V";
   public static final String ENC_KSCPC_EUC_H = "KSCpc-EUC-H";
   public static final String ENC_UNIKSC_UCS2_H = "UniKSC-UCS2-H";
   public static final String ENC_UNIKSC_UCS2_V = "UniKSC-UCS2-V";
   public static final String ENC_IDENTITY_H = "Identity-H";
   public static final String ENC_IDENTTITY_V = "Identity-V";
   protected String name;
   protected PDFCIDSystemInfo sysInfo;
   public static final byte WMODE_HORIZONTAL = 0;
   public static final byte WMODE_VERTICAL = 1;
   protected byte wMode = 0;
   protected Object base;

   public PDFCMap(String name, PDFCIDSystemInfo sysInfo) {
      this.name = name;
      this.sysInfo = sysInfo;
      this.base = null;
   }

   public void setWMode(byte mode) {
      this.wMode = mode;
   }

   public void setUseCMap(String base) {
      this.base = base;
   }

   public void setUseCMap(PDFStream base) {
      this.base = base;
   }

   protected CMapBuilder createCMapBuilder(Writer writer) {
      return new CMapBuilder(writer, this.name);
   }

   protected int output(OutputStream stream) throws IOException {
      CMapBuilder builder = this.createCMapBuilder(this.getBufferWriter());
      builder.writeCMap();
      return super.output(stream);
   }
}
