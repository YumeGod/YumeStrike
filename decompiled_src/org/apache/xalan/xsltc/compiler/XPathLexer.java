package org.apache.xalan.xsltc.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

class XPathLexer implements Scanner {
   private final int YY_BUFFER_SIZE;
   private final int YY_F;
   private final int YY_NO_STATE;
   private final int YY_NOT_ACCEPT;
   private final int YY_START;
   private final int YY_END;
   private final int YY_NO_ANCHOR;
   private final int YY_BOL;
   private final int YY_EOF;
   public final int YYEOF;
   int last;
   private BufferedReader yy_reader;
   private int yy_buffer_index;
   private int yy_buffer_read;
   private int yy_buffer_start;
   private int yy_buffer_end;
   private char[] yy_buffer;
   private boolean yy_at_bol;
   private int yy_lexical_state;
   private boolean yy_eof_done;
   private final int YYINITIAL;
   private final int[] yy_state_dtrans;
   private boolean yy_last_was_cr;
   private final int YY_E_INTERNAL;
   private final int YY_E_MATCH;
   private String[] yy_error_string;
   private int[] yy_acpt;
   private static int[] yy_cmap = unpackFromString(1, 65538, "54:9,27:2,54,27:2,54:18,27,17,53,54,15,54:2,55,25,26,1,3,11,4,13,2,56:10,10,54,18,16,19,54,12,44,57:3,46,57:3,51,57:4,48,52,43,57,47,50,45,57:3,49,57:2,41,54,42,54,58,54,35,38,29,5,21,39,33,36,6,57,20,37,8,28,9,30,57,31,32,23,34,7,40,24,22,57,54,14,54:58,60,54:8,57:23,54,57:31,54,57:58,58:2,57:11,58:2,57:8,58,57:53,58,57:68,58:9,57:36,58:3,57:2,58:4,57:30,58:56,57:89,58:18,57:7,58:62,60:70,54:26,60:2,54:14,58:14,54,58:7,57,58,57:3,58,57,58,57:20,58,57:44,58,57:7,58:3,57,58,57,58,57,58,57,58,57:18,58:13,57:12,58,57:66,58,57:12,58,57:36,58:14,57:53,58:2,57:2,58:2,57:2,58:3,57:28,58:2,57:8,58:2,57:2,58:55,57:38,58:2,57,58:7,57:38,58:73,57:27,58:5,57:3,58:46,57:26,58:6,57:10,58:21,59:10,58:7,57:71,58:2,57:5,58,57:15,58,57:4,58,57,58:15,57:2,58:9,59:10,58:523,57:53,58:3,57,58:26,57:10,58:4,59:10,58:21,57:8,58:2,57:2,58:2,57:22,58,57:7,58,57,58:3,57:4,58:34,57:2,58,57:3,58:4,59:10,57:2,58:19,57:6,58:4,57:2,58:2,57:22,58,57:7,58,57:2,58,57:2,58,57:2,58:31,57:4,58,57,58:7,59:10,58:2,57:3,58:16,57:7,58,57,58,57:3,58,57:22,58,57:7,58,57:2,58,57:5,58:3,57,58:34,57,58:5,59:10,58:21,57:8,58:2,57:2,58:2,57:22,58,57:7,58,57:2,58:2,57:4,58:3,57,58:30,57:2,58,57:3,58:4,59:10,58:21,57:6,58:3,57:3,58,57:4,58:3,57:2,58,57,58,57:2,58:3,57:2,58:3,57:3,58:3,57:8,58,57:3,58:45,59:9,58:21,57:8,58,57:3,58,57:23,58,57:10,58,57:5,58:38,57:2,58:4,59:10,58:21,57:8,58,57:3,58,57:23,58,57:10,58,57:5,58:36,57,58,57:2,58:4,59:10,58:21,57:8,58,57:3,58,57:23,58,57:16,58:38,57:2,58:4,59:10,58:145,57:46,58,57,58,57:2,58:12,57:6,58:10,59:10,58:39,57:2,58,57,58:2,57:2,58,57,58:2,57,58:6,57:4,58,57:7,58,57:3,58,57,58,57,58:2,57:2,58,57:2,58,57,58,57:2,58:9,57,58:2,57:5,58:11,59:10,58:70,59:10,58:22,57:8,58,57:33,58:310,57:38,58:10,57:39,58:9,57,58,57:2,58,57:3,58,57,58,57:2,58,57:5,58:41,57,58,57,58,57,58:11,57,58,57,58,57,58:3,57:2,58:3,57,58:5,57:3,58,57,58,57,58,57,58,57,58:3,57:2,58:3,57:2,58,57,58:40,57,58:9,57,58:2,57,58:2,57:2,58:7,57:2,58,57,58,57:7,58:40,57,58:4,57,58:8,57,58:3078,57:156,58:4,57:90,58:6,57:22,58:2,57:6,58:2,57:38,58:2,57:6,58:2,57:8,58,57,58,57,58,57,58,57:31,58:2,57:53,58,57:7,58,57,58:3,57:3,58,57:7,58:3,57:4,58:2,57:6,58:4,57:13,58:5,57:3,58,57:7,58:3,54:12,58:2,54:98,58:182,57,58:3,57:2,58:2,57,58:81,57:3,58:13,54:2672,58:1008,54:17,58:64,57:84,58:12,57:90,58:10,57:40,58:31443,57:11172,58:92,54:8448,58:1232,54:32,58:526,54:2,0:2")[0];
   private static int[] yy_rmap = unpackFromString(1, 234, "0,1:2,2,1:2,3,4,1,5,6,1:3,7,8,1:5,9,1,10:2,1:3,11,1:5,12,10,1,10:5,1:2,10,1:2,13,1,10,1,14,10,15,16,1:2,10:4,17,1:2,18,19,20,21,22,23,24,25,26,27,1,25,10,28:2,29,5,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,10,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181")[0];
   private static int[][] yy_nxt = unpackFromString(182, 61, "1,2,3,4,5,6,65,184,204,70,7,8,9,10,11,12,13,66,14,15,211,184:2,215,184,16,17,18,218,220,221,184,222,184:2,223,184:3,224,184,19,20,184:10,71,74,77,21,184:2,67,74,-1:63,22,-1:62,184:2,73,184:3,64,-1:2,76,-1:6,184,79,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:10,25,-1:51,26,-1:72,27,-1:42,28,-1:2,28,-1:17,30,-1:26,69,-1:2,72,-1:30,31,-1:57,34,-1:42,21,-1:2,21,-1:5,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:56,28,-1:2,28,-1:57,34,-1:2,34,-1:5,155,184:5,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,209,184:5,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,233,184:5,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,158,184:5,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,122,-1,124,183,184:12,-1:2,184:10,-1:3,76,184,76:3,-1,36,-1:3,103:5,-1:2,80,-1:7,103:5,-1:3,103:13,-1:2,103:10,-1:4,103:3,-1:5,184,23,184:4,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:16,29,-1:48,184:6,64,-1:2,68,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,67,184,76,67,76,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,68,184,76,68,76,-1:44,82,-1:20,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,24,184:9,-1:2,184:10,-1:3,76,184,76:3,-1,75:52,32,75:7,-1:49,84,-1:15,184:3,35,184:2,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1,78:54,33,78:5,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,105,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184,37,184:4,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:45,185,-1:19,184:6,64,-1:2,76,-1:6,184:2,38,184:2,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:43,86,-1:21,184:6,64,-1:2,76,-1:6,184:4,191,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:47,186,-1:17,184,107,184:4,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:46,96,-1:18,184:4,193,184,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:26,42,-1:38,184:2,205,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:25,100,-1,92,-1:37,184:5,192,64,-1:2,76,-1:6,184,228,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:26,43,-1:38,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,206,184:9,-1:2,184:10,-1:3,76,184,76:3,-1:47,104,-1:17,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,111,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:50,190,-1:14,184:6,64,-1:2,76,-1:6,184:3,113,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:26,45,-1:38,184,39,184:4,64,-1:2,76,-1:6,184:5,-1:3,184,212,184:11,-1:2,184:10,-1:3,76,184,76:3,-1:26,46,-1:38,103:6,-1:3,103,-1:6,103:5,-1:3,103:13,-1:2,103:10,-1:3,103:5,-1:48,106,-1:16,184:6,64,-1:2,76,-1:6,184:5,-1:3,184,216,184:11,-1:2,184:10,-1:3,76,184,76:3,-1:19,48,-1:45,184:6,64,-1:2,76,-1:6,184,119,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:51,114,-1:13,184:4,123,184,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:26,50,-1:38,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:11,40,184,-1:2,184:10,-1:3,76,184,76:3,-1:25,116,-1,112,-1:37,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,128,184:9,-1:2,184:10,-1:3,76,184,76:3,-1:52,118,-1:12,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,129,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:26,55,-1:38,184:6,64,-1:2,76,-1:6,184:3,130,184,90,-1,92,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:48,120,-1:16,184:6,64,-1:2,76,-1:6,184,131,184:3,94,-1,188,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:19,56,-1:45,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,132,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:26,62,-1:38,184:6,64,-1:2,76,-1:6,184,208,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:25,126,-1,124,-1:37,184,41,184:4,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:26,63,-1:38,184:6,64,-1:2,76,-1:6,184:5,-1:3,135,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,136,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,138,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,130,184,-1:2,92,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,131,184:3,-1:2,188,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:2,139,184:10,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,197,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184,140,184:4,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,44,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:10,141,184:2,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,142,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:12,225,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:7,143,184:5,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,145,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:6,146,184:6,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,147,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184,148,184:11,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,149,184,110,-1,112,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,150,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,151,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,47,184:9,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,49,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,149,184,-1:2,112,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:5,51,184:7,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,52,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:5,53,184:7,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,54,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:5,156,184:7,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,157,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,159,184:5,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,160,184:9,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,161,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,162,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,213,184:5,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,226,184:9,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,217,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:10,164,184:2,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,167,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,168,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,170,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,171,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,172,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,173,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,174,184:9,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,175,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:11,57,184,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,177,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:6,178,184:6,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:5,58,184:7,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:5,59,184:7,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:11,60,184,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184,179,184:11,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,180,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,181,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,182,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,61,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:2,124,183,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:45,88,-1:61,98,-1:18,184:4,109,184,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:25,102,-1,188,-1:37,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,115,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:50,108,-1:14,184:6,64,-1:2,76,-1:6,184:3,117,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184,195,184:11,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,121,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,137,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,133,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,198,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,229,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184,200,184:4,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,144,184,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:7,210,184:5,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,152,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,163,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,176,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,81,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,125,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,127,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,134,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,199,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,202,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,153,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,83,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,194,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,165,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,154,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,85,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,196,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,166,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,87,64,-1:2,76,-1:6,184:5,-1:3,184:7,89,184:5,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,169,184:8,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,187,64,-1:2,76,-1:6,184:5,-1:3,184:8,91,184:4,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:3,93,184:3,95,184:5,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184,97,184:3,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:3,99,184,-1:3,101,184:12,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:5,189,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,201,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,219,184:5,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,203,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184,207,184:11,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,214,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:9,227,184:3,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:10,230,184:2,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:2,231,184:3,64,-1:2,76,-1:6,184:5,-1:3,184:13,-1:2,184:10,-1:3,76,184,76:3,-1:4,184:6,64,-1:2,76,-1:6,184:5,-1:3,184:4,232,184:8,-1:2,184:10,-1:3,76,184,76:3");

   void initialize() {
      this.last = -1;
   }

   static boolean isWhitespace(int c) {
      return c == 32 || c == 9 || c == 13 || c == 10 || c == 12;
   }

   Symbol disambiguateAxisOrFunction(int ss) throws Exception {
      int index;
      for(index = this.yy_buffer_index; index < this.yy_buffer_read && isWhitespace(this.yy_buffer[index]); ++index) {
      }

      if (index >= this.yy_buffer_read) {
         return new Symbol(ss);
      } else {
         return (this.yy_buffer[index] != ':' || this.yy_buffer[index + 1] != ':') && this.yy_buffer[index] != '(' ? this.newSymbol(27, (String)this.yytext()) : this.newSymbol(ss);
      }
   }

   Symbol disambiguateOperator(int ss) throws Exception {
      switch (this.last) {
         case -1:
         case 2:
         case 4:
         case 6:
         case 7:
         case 9:
         case 10:
         case 12:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 32:
         case 33:
            return this.newSymbol(27, (String)this.yytext());
         case 0:
         case 1:
         case 3:
         case 5:
         case 8:
         case 11:
         case 13:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         default:
            return this.newSymbol(ss);
      }
   }

   Symbol newSymbol(int ss) {
      this.last = ss;
      return new Symbol(ss);
   }

   Symbol newSymbol(int ss, String value) {
      this.last = ss;
      return new Symbol(ss, value);
   }

   Symbol newSymbol(int ss, Long value) {
      this.last = ss;
      return new Symbol(ss, value);
   }

   Symbol newSymbol(int ss, Double value) {
      this.last = ss;
      return new Symbol(ss, value);
   }

   XPathLexer(Reader reader) {
      this();
      if (null == reader) {
         throw new Error("Error: Bad input stream initializer.");
      } else {
         this.yy_reader = new BufferedReader(reader);
      }
   }

   XPathLexer(InputStream instream) {
      this();
      if (null == instream) {
         throw new Error("Error: Bad input stream initializer.");
      } else {
         this.yy_reader = new BufferedReader(new InputStreamReader(instream));
      }
   }

   private XPathLexer() {
      this.YY_BUFFER_SIZE = 512;
      this.YY_F = -1;
      this.YY_NO_STATE = -1;
      this.YY_NOT_ACCEPT = 0;
      this.YY_START = 1;
      this.YY_END = 2;
      this.YY_NO_ANCHOR = 4;
      this.YY_BOL = 65536;
      this.YY_EOF = 65537;
      this.YYEOF = -1;
      this.yy_eof_done = false;
      this.YYINITIAL = 0;
      this.yy_state_dtrans = new int[]{0};
      this.yy_last_was_cr = false;
      this.YY_E_INTERNAL = 0;
      this.YY_E_MATCH = 1;
      this.yy_error_string = new String[]{"Error: Internal error.\n", "Error: Unmatched input.\n"};
      this.yy_acpt = new int[]{0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 4, 0, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
      this.yy_buffer = new char[512];
      this.yy_buffer_read = 0;
      this.yy_buffer_index = 0;
      this.yy_buffer_start = 0;
      this.yy_buffer_end = 0;
      this.yy_at_bol = true;
      this.yy_lexical_state = 0;
   }

   private void yybegin(int state) {
      this.yy_lexical_state = state;
   }

   private int yy_advance() throws IOException {
      if (this.yy_buffer_index < this.yy_buffer_read) {
         return this.yy_buffer[this.yy_buffer_index++];
      } else {
         int next_read;
         if (0 != this.yy_buffer_start) {
            int i = this.yy_buffer_start;

            int j;
            for(j = 0; i < this.yy_buffer_read; ++j) {
               this.yy_buffer[j] = this.yy_buffer[i];
               ++i;
            }

            this.yy_buffer_end -= this.yy_buffer_start;
            this.yy_buffer_start = 0;
            this.yy_buffer_read = j;
            this.yy_buffer_index = j;
            next_read = this.yy_reader.read(this.yy_buffer, this.yy_buffer_read, this.yy_buffer.length - this.yy_buffer_read);
            if (-1 == next_read) {
               return 65537;
            }

            this.yy_buffer_read += next_read;
         }

         while(this.yy_buffer_index >= this.yy_buffer_read) {
            if (this.yy_buffer_index >= this.yy_buffer.length) {
               this.yy_buffer = this.yy_double(this.yy_buffer);
            }

            next_read = this.yy_reader.read(this.yy_buffer, this.yy_buffer_read, this.yy_buffer.length - this.yy_buffer_read);
            if (-1 == next_read) {
               return 65537;
            }

            this.yy_buffer_read += next_read;
         }

         return this.yy_buffer[this.yy_buffer_index++];
      }
   }

   private void yy_move_end() {
      if (this.yy_buffer_end > this.yy_buffer_start && '\n' == this.yy_buffer[this.yy_buffer_end - 1]) {
         --this.yy_buffer_end;
      }

      if (this.yy_buffer_end > this.yy_buffer_start && '\r' == this.yy_buffer[this.yy_buffer_end - 1]) {
         --this.yy_buffer_end;
      }

   }

   private void yy_mark_start() {
      this.yy_buffer_start = this.yy_buffer_index;
   }

   private void yy_mark_end() {
      this.yy_buffer_end = this.yy_buffer_index;
   }

   private void yy_to_mark() {
      this.yy_buffer_index = this.yy_buffer_end;
      this.yy_at_bol = this.yy_buffer_end > this.yy_buffer_start && ('\r' == this.yy_buffer[this.yy_buffer_end - 1] || '\n' == this.yy_buffer[this.yy_buffer_end - 1] || 2028 == this.yy_buffer[this.yy_buffer_end - 1] || 2029 == this.yy_buffer[this.yy_buffer_end - 1]);
   }

   private String yytext() {
      return new String(this.yy_buffer, this.yy_buffer_start, this.yy_buffer_end - this.yy_buffer_start);
   }

   private int yylength() {
      return this.yy_buffer_end - this.yy_buffer_start;
   }

   private char[] yy_double(char[] buf) {
      char[] newbuf = new char[2 * buf.length];

      for(int i = 0; i < buf.length; ++i) {
         newbuf[i] = buf[i];
      }

      return newbuf;
   }

   private void yy_error(int code, boolean fatal) {
      System.out.print(this.yy_error_string[code]);
      System.out.flush();
      if (fatal) {
         throw new Error("Fatal Error.\n");
      }
   }

   private static int[][] unpackFromString(int size1, int size2, String st) {
      int colonIndex = true;
      int sequenceLength = 0;
      int sequenceInteger = 0;
      int[][] res = new int[size1][size2];

      for(int i = 0; i < size1; ++i) {
         for(int j = 0; j < size2; ++j) {
            if (sequenceLength != 0) {
               res[i][j] = sequenceInteger;
               --sequenceLength;
            } else {
               int commaIndex = st.indexOf(44);
               String workString = commaIndex == -1 ? st : st.substring(0, commaIndex);
               st = st.substring(commaIndex + 1);
               int colonIndex = workString.indexOf(58);
               if (colonIndex == -1) {
                  res[i][j] = Integer.parseInt(workString);
               } else {
                  String lengthString = workString.substring(colonIndex + 1);
                  sequenceLength = Integer.parseInt(lengthString);
                  workString = workString.substring(0, colonIndex);
                  sequenceInteger = Integer.parseInt(workString);
                  res[i][j] = sequenceInteger;
                  --sequenceLength;
               }
            }
         }
      }

      return res;
   }

   public Symbol next_token() throws IOException, Exception {
      int yy_anchor = true;
      int yy_state = this.yy_state_dtrans[this.yy_lexical_state];
      int yy_next_state = true;
      int yy_last_accept_state = -1;
      boolean yy_initial = true;
      this.yy_mark_start();
      int yy_this_accept = this.yy_acpt[yy_state];
      if (0 != yy_this_accept) {
         yy_last_accept_state = yy_state;
         this.yy_mark_end();
      }

      while(true) {
         int yy_lookahead;
         if (yy_initial && this.yy_at_bol) {
            yy_lookahead = 65536;
         } else {
            yy_lookahead = this.yy_advance();
         }

         yy_next_state = true;
         int yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
         if (65537 == yy_lookahead && yy_initial) {
            return this.newSymbol(0);
         }

         if (-1 != yy_next_state) {
            yy_state = yy_next_state;
            yy_initial = false;
            yy_this_accept = this.yy_acpt[yy_next_state];
            if (0 != yy_this_accept) {
               yy_last_accept_state = yy_next_state;
               this.yy_mark_end();
            }
         } else {
            if (-1 == yy_last_accept_state) {
               throw new Error("Lexical Error: Unmatched Input.");
            }

            int yy_anchor = this.yy_acpt[yy_last_accept_state];
            if (0 != (2 & yy_anchor)) {
               this.yy_move_end();
            }

            this.yy_to_mark();
            switch (yy_last_accept_state) {
               case -201:
               case -200:
               case -199:
               case -198:
               case -197:
               case -196:
               case -195:
               case -194:
               case -193:
               case -192:
               case -191:
               case -190:
               case -189:
               case -188:
               case -187:
               case -186:
               case -185:
               case -184:
               case -183:
               case -182:
               case -181:
               case -180:
               case -179:
               case -178:
               case -177:
               case -176:
               case -175:
               case -174:
               case -173:
               case -172:
               case -171:
               case -170:
               case -169:
               case -168:
               case -167:
               case -166:
               case -165:
               case -164:
               case -163:
               case -162:
               case -161:
               case -160:
               case -159:
               case -158:
               case -157:
               case -156:
               case -155:
               case -154:
               case -153:
               case -152:
               case -151:
               case -150:
               case -149:
               case -148:
               case -147:
               case -146:
               case -145:
               case -144:
               case -143:
               case -142:
               case -141:
               case -140:
               case -139:
               case -138:
               case -137:
               case -136:
               case -135:
               case -134:
               case -133:
               case -132:
               case -131:
               case -130:
               case -129:
               case -128:
               case -127:
               case -126:
               case -125:
               case -124:
               case -123:
               case -122:
               case -121:
               case -120:
               case -119:
               case -118:
               case -117:
               case -116:
               case -115:
               case -114:
               case -113:
               case -112:
               case -111:
               case -110:
               case -109:
               case -108:
               case -107:
               case -106:
               case -105:
               case -104:
               case -103:
               case -102:
               case -101:
               case -100:
               case -99:
               case -98:
               case -97:
               case -96:
               case -95:
               case -94:
               case -93:
               case -92:
               case -91:
               case -90:
               case -89:
               case -88:
               case -87:
               case -86:
               case -85:
               case -84:
               case -83:
               case -82:
               case -81:
               case -80:
               case -79:
               case -78:
               case -77:
               case -76:
               case -75:
               case -74:
               case -73:
               case -72:
               case -71:
               case -70:
               case -69:
               case -68:
               case -67:
               case -66:
               case -65:
               case -64:
               case -63:
               case -62:
               case -61:
               case -60:
               case -59:
               case -58:
               case -57:
               case -56:
               case -55:
               case -54:
               case -53:
               case -52:
               case -51:
               case -50:
               case -49:
               case -48:
               case -47:
               case -46:
               case -45:
               case -44:
               case -43:
               case -42:
               case -41:
               case -40:
               case -39:
               case -38:
               case -37:
               case -36:
               case -35:
               case -34:
               case -33:
               case -32:
               case -31:
               case -30:
               case -29:
               case -28:
               case -27:
               case -26:
               case -25:
               case -24:
               case -23:
               case -22:
               case -21:
               case -20:
               case -19:
               case -18:
               case -17:
               case -16:
               case -15:
               case -14:
               case -13:
               case -12:
               case -11:
               case -10:
               case -9:
               case -8:
               case -7:
               case -6:
               case -5:
               case -4:
               case -3:
               case -2:
               case -1:
               case 1:
               case 18:
                  break;
               case 0:
               case 64:
               case 69:
               case 72:
               case 75:
               case 78:
               case 80:
               case 82:
               case 84:
               case 86:
               case 88:
               case 90:
               case 92:
               case 94:
               case 96:
               case 98:
               case 100:
               case 102:
               case 104:
               case 106:
               case 108:
               case 110:
               case 112:
               case 114:
               case 116:
               case 118:
               case 120:
               case 122:
               case 124:
               case 126:
               case 185:
               case 186:
               case 188:
               case 190:
               default:
                  this.yy_error(0, false);
                  break;
               case 2:
                  return this.newSymbol(9);
               case 3:
                  return this.newSymbol(2);
               case 4:
                  return this.newSymbol(22);
               case 5:
                  return this.newSymbol(23);
               case 6:
                  return this.newSymbol(27, (String)this.yytext());
               case 7:
                  throw new Exception(this.yytext());
               case 8:
                  return this.newSymbol(10);
               case 9:
                  return this.newSymbol(12);
               case 10:
                  return this.newSymbol(3);
               case 11:
                  return this.newSymbol(6);
               case 12:
                  return this.newSymbol(11);
               case 13:
                  return this.newSymbol(16);
               case 14:
                  return this.newSymbol(18);
               case 15:
                  return this.newSymbol(19);
               case 16:
                  return this.newSymbol(7);
               case 17:
                  return this.newSymbol(8);
               case 19:
                  return this.newSymbol(4);
               case 20:
                  return this.newSymbol(5);
               case 21:
                  return this.newSymbol(51, (Long)(new Long(this.yytext())));
               case 22:
                  return this.newSymbol(15);
               case 23:
                  return this.disambiguateAxisOrFunction(28);
               case 24:
                  return this.disambiguateOperator(32);
               case 25:
                  return this.newSymbol(14);
               case 26:
                  return this.newSymbol(27, (String)this.yytext());
               case 27:
                  return this.newSymbol(13);
               case 28:
                  return this.newSymbol(50, (Double)(new Double(this.yytext())));
               case 29:
                  return this.newSymbol(17);
               case 30:
                  return this.newSymbol(20);
               case 31:
                  return this.newSymbol(21);
               case 32:
                  return this.newSymbol(26, (String)this.yytext().substring(1, this.yytext().length() - 1));
               case 33:
                  return this.newSymbol(26, (String)this.yytext().substring(1, this.yytext().length() - 1));
               case 34:
                  return this.newSymbol(50, (Double)(new Double(this.yytext())));
               case 35:
                  return this.disambiguateOperator(24);
               case 36:
                  return this.newSymbol(27, (String)this.yytext());
               case 37:
                  return this.disambiguateOperator(25);
               case 38:
                  return this.disambiguateAxisOrFunction(29);
               case 39:
                  return this.disambiguateOperator(33);
               case 40:
                  return this.disambiguateAxisOrFunction(38);
               case 41:
                  return this.disambiguateAxisOrFunction(40);
               case 42:
                  return this.newSymbol(30);
               case 43:
                  return this.newSymbol(31);
               case 44:
                  return this.disambiguateAxisOrFunction(39);
               case 45:
                  return this.newSymbol(30);
               case 46:
                  return this.newSymbol(31);
               case 47:
                  return this.disambiguateAxisOrFunction(42);
               case 48:
                  this.initialize();
                  return new Symbol(52);
               case 49:
                  return this.disambiguateAxisOrFunction(48);
               case 50:
                  return this.newSymbol(34);
               case 51:
                  return this.disambiguateAxisOrFunction(49);
               case 52:
                  return this.disambiguateAxisOrFunction(41);
               case 53:
                  return this.disambiguateAxisOrFunction(46);
               case 54:
                  return this.disambiguateAxisOrFunction(44);
               case 55:
                  return this.newSymbol(34);
               case 56:
                  this.initialize();
                  return new Symbol(53);
               case 57:
                  return this.disambiguateAxisOrFunction(43);
               case 58:
                  return this.disambiguateAxisOrFunction(37);
               case 59:
                  return this.disambiguateAxisOrFunction(47);
               case 60:
                  return this.disambiguateAxisOrFunction(45);
               case 61:
                  return this.disambiguateAxisOrFunction(36);
               case 62:
                  return this.newSymbol(35);
               case 63:
                  return this.newSymbol(35);
               case 65:
                  return this.newSymbol(27, (String)this.yytext());
               case 66:
                  throw new Exception(this.yytext());
               case 67:
                  return this.newSymbol(51, (Long)(new Long(this.yytext())));
               case 68:
                  return this.newSymbol(50, (Double)(new Double(this.yytext())));
               case 70:
                  return this.newSymbol(27, (String)this.yytext());
               case 71:
                  throw new Exception(this.yytext());
               case 73:
                  return this.newSymbol(27, (String)this.yytext());
               case 74:
                  throw new Exception(this.yytext());
               case 76:
                  return this.newSymbol(27, (String)this.yytext());
               case 77:
                  throw new Exception(this.yytext());
               case 79:
                  return this.newSymbol(27, (String)this.yytext());
               case 81:
                  return this.newSymbol(27, (String)this.yytext());
               case 83:
                  return this.newSymbol(27, (String)this.yytext());
               case 85:
                  return this.newSymbol(27, (String)this.yytext());
               case 87:
                  return this.newSymbol(27, (String)this.yytext());
               case 89:
                  return this.newSymbol(27, (String)this.yytext());
               case 91:
                  return this.newSymbol(27, (String)this.yytext());
               case 93:
                  return this.newSymbol(27, (String)this.yytext());
               case 95:
                  return this.newSymbol(27, (String)this.yytext());
               case 97:
                  return this.newSymbol(27, (String)this.yytext());
               case 99:
                  return this.newSymbol(27, (String)this.yytext());
               case 101:
                  return this.newSymbol(27, (String)this.yytext());
               case 103:
                  return this.newSymbol(27, (String)this.yytext());
               case 105:
                  return this.newSymbol(27, (String)this.yytext());
               case 107:
                  return this.newSymbol(27, (String)this.yytext());
               case 109:
                  return this.newSymbol(27, (String)this.yytext());
               case 111:
                  return this.newSymbol(27, (String)this.yytext());
               case 113:
                  return this.newSymbol(27, (String)this.yytext());
               case 115:
                  return this.newSymbol(27, (String)this.yytext());
               case 117:
                  return this.newSymbol(27, (String)this.yytext());
               case 119:
                  return this.newSymbol(27, (String)this.yytext());
               case 121:
                  return this.newSymbol(27, (String)this.yytext());
               case 123:
                  return this.newSymbol(27, (String)this.yytext());
               case 125:
                  return this.newSymbol(27, (String)this.yytext());
               case 127:
                  return this.newSymbol(27, (String)this.yytext());
               case 128:
                  return this.newSymbol(27, (String)this.yytext());
               case 129:
                  return this.newSymbol(27, (String)this.yytext());
               case 130:
                  return this.newSymbol(27, (String)this.yytext());
               case 131:
                  return this.newSymbol(27, (String)this.yytext());
               case 132:
                  return this.newSymbol(27, (String)this.yytext());
               case 133:
                  return this.newSymbol(27, (String)this.yytext());
               case 134:
                  return this.newSymbol(27, (String)this.yytext());
               case 135:
                  return this.newSymbol(27, (String)this.yytext());
               case 136:
                  return this.newSymbol(27, (String)this.yytext());
               case 137:
                  return this.newSymbol(27, (String)this.yytext());
               case 138:
                  return this.newSymbol(27, (String)this.yytext());
               case 139:
                  return this.newSymbol(27, (String)this.yytext());
               case 140:
                  return this.newSymbol(27, (String)this.yytext());
               case 141:
                  return this.newSymbol(27, (String)this.yytext());
               case 142:
                  return this.newSymbol(27, (String)this.yytext());
               case 143:
                  return this.newSymbol(27, (String)this.yytext());
               case 144:
                  return this.newSymbol(27, (String)this.yytext());
               case 145:
                  return this.newSymbol(27, (String)this.yytext());
               case 146:
                  return this.newSymbol(27, (String)this.yytext());
               case 147:
                  return this.newSymbol(27, (String)this.yytext());
               case 148:
                  return this.newSymbol(27, (String)this.yytext());
               case 149:
                  return this.newSymbol(27, (String)this.yytext());
               case 150:
                  return this.newSymbol(27, (String)this.yytext());
               case 151:
                  return this.newSymbol(27, (String)this.yytext());
               case 152:
                  return this.newSymbol(27, (String)this.yytext());
               case 153:
                  return this.newSymbol(27, (String)this.yytext());
               case 154:
                  return this.newSymbol(27, (String)this.yytext());
               case 155:
                  return this.newSymbol(27, (String)this.yytext());
               case 156:
                  return this.newSymbol(27, (String)this.yytext());
               case 157:
                  return this.newSymbol(27, (String)this.yytext());
               case 158:
                  return this.newSymbol(27, (String)this.yytext());
               case 159:
                  return this.newSymbol(27, (String)this.yytext());
               case 160:
                  return this.newSymbol(27, (String)this.yytext());
               case 161:
                  return this.newSymbol(27, (String)this.yytext());
               case 162:
                  return this.newSymbol(27, (String)this.yytext());
               case 163:
                  return this.newSymbol(27, (String)this.yytext());
               case 164:
                  return this.newSymbol(27, (String)this.yytext());
               case 165:
                  return this.newSymbol(27, (String)this.yytext());
               case 166:
                  return this.newSymbol(27, (String)this.yytext());
               case 167:
                  return this.newSymbol(27, (String)this.yytext());
               case 168:
                  return this.newSymbol(27, (String)this.yytext());
               case 169:
                  return this.newSymbol(27, (String)this.yytext());
               case 170:
                  return this.newSymbol(27, (String)this.yytext());
               case 171:
                  return this.newSymbol(27, (String)this.yytext());
               case 172:
                  return this.newSymbol(27, (String)this.yytext());
               case 173:
                  return this.newSymbol(27, (String)this.yytext());
               case 174:
                  return this.newSymbol(27, (String)this.yytext());
               case 175:
                  return this.newSymbol(27, (String)this.yytext());
               case 176:
                  return this.newSymbol(27, (String)this.yytext());
               case 177:
                  return this.newSymbol(27, (String)this.yytext());
               case 178:
                  return this.newSymbol(27, (String)this.yytext());
               case 179:
                  return this.newSymbol(27, (String)this.yytext());
               case 180:
                  return this.newSymbol(27, (String)this.yytext());
               case 181:
                  return this.newSymbol(27, (String)this.yytext());
               case 182:
                  return this.newSymbol(27, (String)this.yytext());
               case 183:
                  return this.newSymbol(27, (String)this.yytext());
               case 184:
                  return this.newSymbol(27, (String)this.yytext());
               case 187:
                  return this.newSymbol(27, (String)this.yytext());
               case 189:
                  return this.newSymbol(27, (String)this.yytext());
               case 191:
                  return this.newSymbol(27, (String)this.yytext());
               case 192:
                  return this.newSymbol(27, (String)this.yytext());
               case 193:
                  return this.newSymbol(27, (String)this.yytext());
               case 194:
                  return this.newSymbol(27, (String)this.yytext());
               case 195:
                  return this.newSymbol(27, (String)this.yytext());
               case 196:
                  return this.newSymbol(27, (String)this.yytext());
               case 197:
                  return this.newSymbol(27, (String)this.yytext());
               case 198:
                  return this.newSymbol(27, (String)this.yytext());
               case 199:
                  return this.newSymbol(27, (String)this.yytext());
               case 200:
                  return this.newSymbol(27, (String)this.yytext());
               case 201:
                  return this.newSymbol(27, (String)this.yytext());
               case 202:
                  return this.newSymbol(27, (String)this.yytext());
               case 203:
                  return this.newSymbol(27, (String)this.yytext());
               case 204:
                  return this.newSymbol(27, (String)this.yytext());
               case 205:
                  return this.newSymbol(27, (String)this.yytext());
               case 206:
                  return this.newSymbol(27, (String)this.yytext());
               case 207:
                  return this.newSymbol(27, (String)this.yytext());
               case 208:
                  return this.newSymbol(27, (String)this.yytext());
               case 209:
                  return this.newSymbol(27, (String)this.yytext());
               case 210:
                  return this.newSymbol(27, (String)this.yytext());
               case 211:
                  return this.newSymbol(27, (String)this.yytext());
               case 212:
                  return this.newSymbol(27, (String)this.yytext());
               case 213:
                  return this.newSymbol(27, (String)this.yytext());
               case 214:
                  return this.newSymbol(27, (String)this.yytext());
               case 215:
                  return this.newSymbol(27, (String)this.yytext());
               case 216:
                  return this.newSymbol(27, (String)this.yytext());
               case 217:
                  return this.newSymbol(27, (String)this.yytext());
               case 218:
                  return this.newSymbol(27, (String)this.yytext());
               case 219:
                  return this.newSymbol(27, (String)this.yytext());
               case 220:
                  return this.newSymbol(27, (String)this.yytext());
               case 221:
                  return this.newSymbol(27, (String)this.yytext());
               case 222:
                  return this.newSymbol(27, (String)this.yytext());
               case 223:
                  return this.newSymbol(27, (String)this.yytext());
               case 224:
                  return this.newSymbol(27, (String)this.yytext());
               case 225:
                  return this.newSymbol(27, (String)this.yytext());
               case 226:
                  return this.newSymbol(27, (String)this.yytext());
               case 227:
                  return this.newSymbol(27, (String)this.yytext());
               case 228:
                  return this.newSymbol(27, (String)this.yytext());
               case 229:
                  return this.newSymbol(27, (String)this.yytext());
               case 230:
                  return this.newSymbol(27, (String)this.yytext());
               case 231:
                  return this.newSymbol(27, (String)this.yytext());
               case 232:
                  return this.newSymbol(27, (String)this.yytext());
               case 233:
                  return this.newSymbol(27, (String)this.yytext());
            }

            yy_initial = true;
            yy_state = this.yy_state_dtrans[this.yy_lexical_state];
            yy_next_state = true;
            yy_last_accept_state = -1;
            this.yy_mark_start();
            yy_this_accept = this.yy_acpt[yy_state];
            if (0 != yy_this_accept) {
               yy_last_accept_state = yy_state;
               this.yy_mark_end();
            }
         }
      }
   }
}
