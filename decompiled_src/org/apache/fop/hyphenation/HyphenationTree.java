package org.apache.fop.hyphenation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.InputSource;

public class HyphenationTree extends TernaryTree implements PatternConsumer, Serializable {
   private static final long serialVersionUID = -7842107987915665573L;
   protected ByteVector vspace = new ByteVector();
   protected HashMap stoplist = new HashMap(23);
   protected TernaryTree classmap = new TernaryTree();
   private transient TernaryTree ivalues;

   public HyphenationTree() {
      this.vspace.alloc(1);
   }

   protected int packValues(String values) {
      int n = values.length();
      int m = (n & 1) == 1 ? (n >> 1) + 2 : (n >> 1) + 1;
      int offset = this.vspace.alloc(m);
      byte[] va = this.vspace.getArray();

      for(int i = 0; i < n; ++i) {
         int j = i >> 1;
         byte v = (byte)(values.charAt(i) - 48 + 1 & 15);
         if ((i & 1) == 1) {
            va[j + offset] |= v;
         } else {
            va[j + offset] = (byte)(v << 4);
         }
      }

      va[m - 1 + offset] = 0;
      return offset;
   }

   protected String unpackValues(int k) {
      StringBuffer buf = new StringBuffer();

      for(byte v = this.vspace.get(k++); v != 0; v = this.vspace.get(k++)) {
         char c = (char)((v >>> 4) - 1 + 48);
         buf.append(c);
         c = (char)(v & 15);
         if (c == 0) {
            break;
         }

         c = (char)(c - 1 + 48);
         buf.append(c);
      }

      return buf.toString();
   }

   public void loadPatterns(String filename) throws HyphenationException {
      File f = new File(filename);

      try {
         InputSource src = new InputSource(f.toURI().toURL().toExternalForm());
         this.loadPatterns(src);
      } catch (MalformedURLException var4) {
         throw new HyphenationException("Error converting the File '" + f + "' to a URL: " + var4.getMessage());
      }
   }

   public void loadPatterns(InputSource source) throws HyphenationException {
      PatternParser pp = new PatternParser(this);
      this.ivalues = new TernaryTree();
      pp.parse(source);
      this.trimToSize();
      this.vspace.trimToSize();
      this.classmap.trimToSize();
      this.ivalues = null;
   }

   public String findPattern(String pat) {
      int k = super.find(pat);
      return k >= 0 ? this.unpackValues(k) : "";
   }

   protected int hstrcmp(char[] s, int si, char[] t, int ti) {
      while(s[si] == t[ti]) {
         if (s[si] == 0) {
            return 0;
         }

         ++si;
         ++ti;
      }

      if (t[ti] == 0) {
         return 0;
      } else {
         return s[si] - t[ti];
      }
   }

   protected byte[] getValues(int k) {
      StringBuffer buf = new StringBuffer();

      for(byte v = this.vspace.get(k++); v != 0; v = this.vspace.get(k++)) {
         char c = (char)((v >>> 4) - 1);
         buf.append(c);
         c = (char)(v & 15);
         if (c == 0) {
            break;
         }

         --c;
         buf.append(c);
      }

      byte[] res = new byte[buf.length()];

      for(int i = 0; i < res.length; ++i) {
         res[i] = (byte)buf.charAt(i);
      }

      return res;
   }

   protected void searchPatterns(char[] word, int index, byte[] il) {
      int i = index;
      char sp = word[index];
      char p = this.root;

      label79:
      while(p > 0 && p < this.sc.length) {
         byte[] values;
         int j;
         int j;
         if (this.sc[p] == '\uffff') {
            if (this.hstrcmp(word, i, this.kv.getArray(), this.lo[p]) == 0) {
               values = this.getValues(this.eq[p]);
               j = index;

               for(j = 0; j < values.length; ++j) {
                  if (j < il.length && values[j] > il[j]) {
                     il[j] = values[j];
                  }

                  ++j;
               }
            }

            return;
         }

         j = sp - this.sc[p];
         if (j == 0) {
            if (sp == 0) {
               break;
            }

            ++i;
            sp = word[i];
            p = this.eq[p];

            for(char q = p; q > 0 && q < this.sc.length && this.sc[q] != '\uffff'; q = this.lo[q]) {
               if (this.sc[q] == 0) {
                  values = this.getValues(this.eq[q]);
                  j = index;
                  int k = 0;

                  while(true) {
                     if (k >= values.length) {
                        continue label79;
                     }

                     if (j < il.length && values[k] > il[j]) {
                        il[j] = values[k];
                     }

                     ++j;
                     ++k;
                  }
               }
            }
         } else {
            p = j < 0 ? this.lo[p] : this.hi[p];
         }
      }

   }

   public Hyphenation hyphenate(String word, int remainCharCount, int pushCharCount) {
      char[] w = word.toCharArray();
      return this.hyphenate(w, 0, w.length, remainCharCount, pushCharCount);
   }

   public Hyphenation hyphenate(char[] w, int offset, int len, int remainCharCount, int pushCharCount) {
      char[] word = new char[len + 3];
      char[] c = new char[2];
      int iIgnoreAtBeginning = 0;
      int iLength = len;
      boolean bEndOfLetters = false;

      int i;
      for(i = 1; i <= len; ++i) {
         c[0] = w[offset + i - 1];
         int nc = this.classmap.find(c, 0);
         if (nc < 0) {
            if (i == 1 + iIgnoreAtBeginning) {
               ++iIgnoreAtBeginning;
            } else {
               bEndOfLetters = true;
            }

            --iLength;
         } else {
            if (bEndOfLetters) {
               return null;
            }

            word[i - iIgnoreAtBeginning] = (char)nc;
         }
      }

      len = iLength;
      if (iLength < remainCharCount + pushCharCount) {
         return null;
      } else {
         int[] result = new int[iLength + 1];
         int k = 0;
         String sw = new String(word, 1, iLength);
         if (this.stoplist.containsKey(sw)) {
            ArrayList hw = (ArrayList)this.stoplist.get(sw);
            int j = 0;

            for(i = 0; i < hw.size(); ++i) {
               Object o = hw.get(i);
               if (o instanceof String) {
                  j += ((String)o).length();
                  if (j >= remainCharCount && j < len - pushCharCount) {
                     result[k++] = j + iIgnoreAtBeginning;
                  }
               }
            }
         } else {
            word[0] = '.';
            word[iLength + 1] = '.';
            word[iLength + 2] = 0;
            byte[] il = new byte[iLength + 3];

            for(i = 0; i < len + 1; ++i) {
               this.searchPatterns(word, i, il);
            }

            for(i = 0; i < len; ++i) {
               if ((il[i + 1] & 1) == 1 && i >= remainCharCount && i <= len - pushCharCount) {
                  result[k++] = i + iIgnoreAtBeginning;
               }
            }
         }

         if (k > 0) {
            int[] res = new int[k];
            System.arraycopy(result, 0, res, 0, k);
            return new Hyphenation(new String(w, offset, len), res);
         } else {
            return null;
         }
      }
   }

   public void addClass(String chargroup) {
      if (chargroup.length() > 0) {
         char equivChar = chargroup.charAt(0);
         char[] key = new char[]{'\u0000', '\u0000'};

         for(int i = 0; i < chargroup.length(); ++i) {
            key[0] = chargroup.charAt(i);
            this.classmap.insert(key, 0, equivChar);
         }
      }

   }

   public void addException(String word, ArrayList hyphenatedword) {
      this.stoplist.put(word, hyphenatedword);
   }

   public void addPattern(String pattern, String ivalue) {
      int k = this.ivalues.find(ivalue);
      if (k <= 0) {
         k = this.packValues(ivalue);
         this.ivalues.insert(ivalue, (char)k);
      }

      this.insert(pattern, (char)k);
   }

   public void printStats() {
      System.out.println("Value space size = " + Integer.toString(this.vspace.length()));
      super.printStats();
   }

   public static void main(String[] argv) throws Exception {
      HyphenationTree ht = null;
      int minCharCount = 2;
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      while(true) {
         while(true) {
            while(true) {
               while(true) {
                  while(true) {
                     while(true) {
                        while(true) {
                           System.out.print("l:\tload patterns from XML\nL:\tload patterns from serialized object\ns:\tset minimum character count\nw:\twrite hyphenation tree to object file\nh:\thyphenate\nf:\tfind pattern\nb:\tbenchmark\nq:\tquit\n\nCommand:");
                           String token = in.readLine().trim();
                           if (!token.equals("f")) {
                              if (!token.equals("s")) {
                                 if (!token.equals("l")) {
                                    if (!token.equals("L")) {
                                       if (!token.equals("w")) {
                                          if (!token.equals("h")) {
                                             if (!token.equals("b")) {
                                                if (token.equals("q")) {
                                                   return;
                                                }
                                             } else {
                                                if (ht == null) {
                                                   System.out.println("No patterns have been loaded.");
                                                   return;
                                                }

                                                System.out.print("Word list filename: ");
                                                token = in.readLine().trim();
                                                long starttime = 0L;
                                                int counter = 0;

                                                try {
                                                   BufferedReader reader = new BufferedReader(new FileReader(token));

                                                   String line;
                                                   for(starttime = System.currentTimeMillis(); (line = reader.readLine()) != null; ++counter) {
                                                      Hyphenation hyp = ht.hyphenate(line, minCharCount, minCharCount);
                                                      if (hyp != null) {
                                                         String var11 = hyp.toString();
                                                      }
                                                   }
                                                } catch (Exception var44) {
                                                   System.out.println("Exception " + var44);
                                                   var44.printStackTrace();
                                                }

                                                long endtime = System.currentTimeMillis();
                                                long result = endtime - starttime;
                                                System.out.println(counter + " words in " + result + " Milliseconds hyphenated");
                                             }
                                          } else {
                                             System.out.print("Word: ");
                                             token = in.readLine().trim();
                                             System.out.print("Hyphenation points: ");
                                             System.out.println(ht.hyphenate(token, minCharCount, minCharCount));
                                          }
                                       } else {
                                          System.out.print("Object file name: ");
                                          token = in.readLine().trim();
                                          ObjectOutputStream oos = null;

                                          try {
                                             oos = new ObjectOutputStream(new FileOutputStream(token));
                                             oos.writeObject(ht);
                                          } catch (Exception var40) {
                                             var40.printStackTrace();
                                          } finally {
                                             if (oos != null) {
                                                try {
                                                   oos.flush();
                                                } catch (IOException var39) {
                                                }

                                                try {
                                                   oos.close();
                                                } catch (IOException var38) {
                                                }
                                             }

                                          }
                                       }
                                    } else {
                                       ObjectInputStream ois = null;
                                       System.out.print("Object file name: ");
                                       token = in.readLine().trim();

                                       try {
                                          ois = new ObjectInputStream(new FileInputStream(token));
                                          ht = (HyphenationTree)ois.readObject();
                                       } catch (Exception var41) {
                                          var41.printStackTrace();
                                       } finally {
                                          if (ois != null) {
                                             try {
                                                ois.close();
                                             } catch (IOException var37) {
                                             }
                                          }

                                       }
                                    }
                                 } else {
                                    ht = new HyphenationTree();
                                    System.out.print("XML file name: ");
                                    token = in.readLine().trim();
                                    ht.loadPatterns(token);
                                 }
                              } else {
                                 System.out.print("Minimun value: ");
                                 token = in.readLine().trim();
                                 minCharCount = Integer.parseInt(token);
                              }
                           } else {
                              System.out.print("Pattern: ");
                              token = in.readLine().trim();
                              System.out.println("Values: " + ht.findPattern(token));
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
