package org.apache.xmlgraphics.image.loader.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.image.loader.ImageProcessingHints;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.ImageSource;
import org.xml.sax.InputSource;

public class ImageUtil {
   private static final byte[] GZIP_MAGIC = new byte[]{31, -117};
   private static final String PAGE_INDICATOR = "page=";

   public static InputStream getInputStream(Source src) {
      if (src instanceof StreamSource) {
         return ((StreamSource)src).getInputStream();
      } else if (src instanceof ImageSource) {
         return new ImageInputStreamAdapter(((ImageSource)src).getImageInputStream());
      } else {
         if (src instanceof SAXSource) {
            InputSource is = ((SAXSource)src).getInputSource();
            if (is != null) {
               return is.getByteStream();
            }
         }

         return null;
      }
   }

   public static ImageInputStream getImageInputStream(Source src) {
      return src instanceof ImageSource ? ((ImageSource)src).getImageInputStream() : null;
   }

   public static InputStream needInputStream(Source src) {
      InputStream in = getInputStream(src);
      if (in != null) {
         return in;
      } else {
         throw new IllegalArgumentException("Source must be a StreamSource with an InputStream or an ImageSource");
      }
   }

   public static ImageInputStream needImageInputStream(Source src) {
      if (src instanceof ImageSource) {
         ImageSource isrc = (ImageSource)src;
         if (isrc.getImageInputStream() == null) {
            throw new IllegalArgumentException("ImageInputStream is null/cleared on ImageSource");
         } else {
            return isrc.getImageInputStream();
         }
      } else {
         throw new IllegalArgumentException("Source must be an ImageSource");
      }
   }

   public static boolean hasInputStream(Source src) {
      if (src instanceof StreamSource) {
         InputStream in = ((StreamSource)src).getInputStream();
         return in != null;
      } else if (src instanceof ImageSource) {
         return hasImageInputStream(src);
      } else {
         if (src instanceof SAXSource) {
            InputSource is = ((SAXSource)src).getInputSource();
            if (is != null) {
               return is.getByteStream() != null;
            }
         }

         return false;
      }
   }

   public static boolean hasReader(Source src) {
      if (src instanceof StreamSource) {
         Reader reader = ((StreamSource)src).getReader();
         return reader != null;
      } else {
         if (src instanceof SAXSource) {
            InputSource is = ((SAXSource)src).getInputSource();
            if (is != null) {
               return is.getCharacterStream() != null;
            }
         }

         return false;
      }
   }

   public static boolean hasImageInputStream(Source src) {
      if (src instanceof ImageSource) {
         ImageInputStream in = ((ImageSource)src).getImageInputStream();
         if (in != null) {
            return true;
         }
      }

      return false;
   }

   public static void removeStreams(Source src) {
      if (src instanceof ImageSource) {
         ImageSource isrc = (ImageSource)src;
         isrc.setImageInputStream((ImageInputStream)null);
      } else if (src instanceof StreamSource) {
         StreamSource ssrc = (StreamSource)src;
         ssrc.setInputStream((InputStream)null);
         ssrc.setReader((Reader)null);
      } else if (src instanceof SAXSource) {
         InputSource is = ((SAXSource)src).getInputSource();
         if (is != null) {
            is.setByteStream((InputStream)null);
            is.setCharacterStream((Reader)null);
         }
      }

   }

   public static void closeQuietly(Source src) {
      if (src != null) {
         if (src instanceof StreamSource) {
            StreamSource streamSource = (StreamSource)src;
            IOUtils.closeQuietly(streamSource.getInputStream());
            streamSource.setInputStream((InputStream)null);
            IOUtils.closeQuietly(streamSource.getReader());
            streamSource.setReader((Reader)null);
         } else if (src instanceof ImageSource) {
            ImageSource imageSource = (ImageSource)src;
            if (imageSource.getImageInputStream() != null) {
               try {
                  imageSource.getImageInputStream().close();
               } catch (IOException var3) {
               }

               imageSource.setImageInputStream((ImageInputStream)null);
            }
         } else if (src instanceof SAXSource) {
            InputSource is = ((SAXSource)src).getInputSource();
            if (is != null) {
               IOUtils.closeQuietly(is.getByteStream());
               is.setByteStream((InputStream)null);
               IOUtils.closeQuietly(is.getCharacterStream());
               is.setCharacterStream((Reader)null);
            }
         }

      }
   }

   public static ImageInputStream ignoreFlushing(final ImageInputStream in) {
      return (ImageInputStream)Proxy.newProxyInstance(in.getClass().getClassLoader(), new Class[]{ImageInputStream.class}, new InvocationHandler() {
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (!methodName.startsWith("flush")) {
               try {
                  return method.invoke(in, args);
               } catch (InvocationTargetException var6) {
                  throw var6.getCause();
               }
            } else {
               return null;
            }
         }
      });
   }

   public static boolean isGZIPCompressed(InputStream in) throws IOException {
      if (!in.markSupported()) {
         throw new IllegalArgumentException("InputStream must support mark()!");
      } else {
         byte[] data = new byte[2];
         in.mark(2);
         in.read(data);
         in.reset();
         return data[0] == GZIP_MAGIC[0] && data[1] == GZIP_MAGIC[1];
      }
   }

   public static InputStream decorateMarkSupported(InputStream in) {
      return (InputStream)(in.markSupported() ? in : new BufferedInputStream(in));
   }

   public static InputStream autoDecorateInputStream(InputStream in) throws IOException {
      in = decorateMarkSupported(in);
      return (InputStream)(isGZIPCompressed(in) ? new GZIPInputStream(in) : in);
   }

   public static Map getDefaultHints(ImageSessionContext session) {
      Map hints = new HashMap();
      hints.put(ImageProcessingHints.SOURCE_RESOLUTION, new Float(session.getParentContext().getSourceResolution()));
      hints.put(ImageProcessingHints.TARGET_RESOLUTION, new Float(session.getTargetResolution()));
      hints.put(ImageProcessingHints.IMAGE_SESSION_CONTEXT, session);
      return hints;
   }

   public static Integer getPageIndexFromURI(String uri) {
      if (uri.indexOf(35) < 0) {
         return null;
      } else {
         try {
            URI u = new URI(uri);
            String fragment = u.getFragment();
            if (fragment != null) {
               int pos = fragment.indexOf("page=");
               if (pos >= 0) {
                  pos += "page=".length();

                  StringBuffer sb;
                  int pageIndex;
                  for(sb = new StringBuffer(); pos < fragment.length(); ++pos) {
                     pageIndex = fragment.charAt(pos);
                     if (pageIndex < 48 || pageIndex > 57) {
                        break;
                     }

                     sb.append((char)pageIndex);
                  }

                  if (sb.length() > 0) {
                     pageIndex = Integer.parseInt(sb.toString()) - 1;
                     pageIndex = Math.max(0, pageIndex);
                     return new Integer(pageIndex);
                  }
               }
            }

            return null;
         } catch (URISyntaxException var6) {
            throw new IllegalArgumentException("URI is invalid: " + var6.getLocalizedMessage());
         }
      }
   }

   public static int needPageIndexFromURI(String uri) {
      Integer res = getPageIndexFromURI(uri);
      return res != null ? res : 0;
   }
}
