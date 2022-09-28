package org.apache.xmlgraphics.image.loader.impl;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.java2d.color.DeviceCMYKColorSpace;

public class ImageLoaderRawJPEG extends AbstractImageLoader implements JPEGConstants {
   protected static Log log;

   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.RAW_JPEG;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      if (!"image/jpeg".equals(info.getMimeType())) {
         throw new IllegalArgumentException("ImageInfo must be from a image with MIME type: image/jpeg");
      } else {
         ColorSpace colorSpace = null;
         boolean appeFound = false;
         int sofType = 0;
         ByteArrayOutputStream iccStream = null;
         Source src = session.needSource(info.getOriginalURI());
         ImageInputStream in = ImageUtil.needImageInputStream(src);
         JPEGFile jpeg = new JPEGFile(in);
         in.mark();

         try {
            label508:
            while(true) {
               int segID = jpeg.readMarkerSegment();
               if (log.isTraceEnabled()) {
                  log.trace("Seg Marker: " + Integer.toHexString(segID));
               }

               int reclen;
               byte[] adobeHeader;
               switch (segID) {
                  case 0:
                  case 216:
                     break;
                  case 192:
                  case 193:
                  case 194:
                  case 202:
                     sofType = segID;
                     if (log.isTraceEnabled()) {
                        log.trace("SOF: " + Integer.toHexString(segID));
                     }

                     in.mark();

                     try {
                        reclen = jpeg.readSegmentLength();
                        in.skipBytes(1);
                        in.skipBytes(2);
                        in.skipBytes(2);
                        int numComponents = in.readUnsignedByte();
                        if (numComponents == 1) {
                           colorSpace = ColorSpace.getInstance(1003);
                        } else if (numComponents == 3) {
                           colorSpace = ColorSpace.getInstance(1004);
                        } else {
                           if (numComponents != 4) {
                              throw new ImageException("Unsupported ColorSpace for image " + info + ". The number of components supported are 1, 3 and 4.");
                           }

                           colorSpace = DeviceCMYKColorSpace.getInstance();
                        }
                     } finally {
                        in.reset();
                     }

                     in.skipBytes(reclen);
                     break;
                  case 217:
                     log.trace("EOI found. Stopping.");
                     break label508;
                  case 218:
                     log.trace("SOS found. Stopping early.");
                     break label508;
                  case 226:
                     in.mark();

                     try {
                        reclen = jpeg.readSegmentLength();
                        adobeHeader = new byte[11];
                        in.readFully(adobeHeader);
                        in.skipBytes(1);
                        if ("ICC_PROFILE".equals(new String(adobeHeader, "US-ASCII"))) {
                           in.skipBytes(2);
                           int payloadSize = reclen - 2 - 12 - 2;
                           if (this.ignoreColorProfile(hints)) {
                              log.debug("Ignoring ICC profile data in JPEG");
                              in.skipBytes(payloadSize);
                           } else {
                              byte[] buf = new byte[payloadSize];
                              in.readFully(buf);
                              if (iccStream == null) {
                                 if (log.isDebugEnabled()) {
                                    log.debug("JPEG has an ICC profile");
                                    DataInputStream din = new DataInputStream(new ByteArrayInputStream(buf));
                                    log.debug("Declared ICC profile size: " + din.readInt());
                                 }

                                 iccStream = new ByteArrayOutputStream();
                              }

                              iccStream.write(buf);
                           }
                        }
                     } finally {
                        in.reset();
                     }

                     in.skipBytes(reclen);
                     break;
                  case 238:
                     in.mark();

                     try {
                        reclen = jpeg.readSegmentLength();
                        adobeHeader = new byte[5];
                        in.readFully(adobeHeader);
                        if ("Adobe".equals(new String(adobeHeader, "US-ASCII"))) {
                           appeFound = true;
                        }
                     } finally {
                        in.reset();
                     }

                     in.skipBytes(reclen);
                     break;
                  default:
                     jpeg.skipCurrentMarkerSegment();
               }
            }
         } finally {
            in.reset();
         }

         ICC_Profile iccProfile = this.buildICCProfile(info, (ColorSpace)colorSpace, iccStream);
         if (iccProfile == null && colorSpace == null) {
            throw new ImageException("ColorSpace could not be identified for JPEG image " + info);
         } else {
            boolean invertImage = false;
            if (appeFound && ((ColorSpace)colorSpace).getType() == 9) {
               if (log.isDebugEnabled()) {
                  log.debug("JPEG has an Adobe APPE marker. Note: CMYK Image will be inverted. (" + info.getOriginalURI() + ")");
               }

               invertImage = true;
            }

            ImageRawJPEG rawImage = new ImageRawJPEG(info, ImageUtil.needInputStream(src), sofType, (ColorSpace)colorSpace, iccProfile, invertImage);
            return rawImage;
         }
      }
   }

   private ICC_Profile buildICCProfile(ImageInfo info, ColorSpace colorSpace, ByteArrayOutputStream iccStream) throws IOException, ImageException {
      if (iccStream != null && iccStream.size() > 0) {
         if (log.isDebugEnabled()) {
            log.debug("Effective ICC profile size: " + iccStream.size());
         }

         int alignment = true;
         int padding = (4 - iccStream.size() % 4) % 4;
         if (padding != 0) {
            try {
               iccStream.write(new byte[padding]);
            } catch (IOException var8) {
               throw new IOException("Error while aligning ICC stream: " + var8.getMessage());
            }
         }

         ICC_Profile iccProfile = null;

         try {
            iccProfile = ICC_Profile.getInstance(iccStream.toByteArray());
            if (log.isDebugEnabled()) {
               log.debug("JPEG has an ICC profile: " + iccProfile.toString());
            }
         } catch (IllegalArgumentException var9) {
            log.warn("An ICC profile is present in the JPEG file but it is invalid (" + var9.getMessage() + "). The color profile will be ignored. (" + info.getOriginalURI() + ")");
            return null;
         }

         if (iccProfile.getNumComponents() != colorSpace.getNumComponents()) {
            log.warn("The number of components of the ICC profile (" + iccProfile.getNumComponents() + ") doesn't match the image (" + colorSpace.getNumComponents() + "). Ignoring the ICC color profile.");
            return null;
         } else {
            return iccProfile;
         }
      } else {
         return null;
      }
   }

   static {
      log = LogFactory.getLog(ImageLoaderRawJPEG.class);
   }
}
