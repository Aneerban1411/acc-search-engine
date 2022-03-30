package org.uwindsor.acc.search;
/**
 * 
 * @author raks
 * @reference Advanced Computing Concepts (Winter 2022): Text Processing Assignment -> Source Code 
 *   		  Link: https://blackboard.uwindsor.ca/bbcswebdav/pid-1890968-dt-content-rid-27993045_1/xid-27993045_1
 */
public class KMP
{
   private final int R;       // the radix
   private int[][] dfa;       // the KMP automoton

   private char[] pattern;    // either the character array for the pattern
   private String pat;        // or the pattern string

   // create the DFA from a String
   public KMP(String pat)
   {
       this.R = 256;
       this.pat = pat;

       // build DFA from pattern
       int M = pat.length();
       dfa = new int[R][M]; 
       dfa[pat.charAt(0)][0] = 1; 
       for (int X = 0, j = 1; j < M; j++)
       {
           for (int c = 0; c < R; c++) 
               dfa[c][j] = dfa[c][X];     // Copy mismatch cases. 
           dfa[pat.charAt(j)][j] = j+1;   // Set match case. 
           X = dfa[pat.charAt(j)][X];     // Update restart state. 
       } 
   } 

   // create the DFA from a character array over R-character alphabet
   public KMP(char[] pattern, int R)
   {
       this.R = R;
       this.pattern = new char[pattern.length];
       for (int j = 0; j < pattern.length; j++)
           this.pattern[j] = pattern[j];

       // build DFA from pattern
       int M = pattern.length;
       dfa = new int[R][M]; 
       dfa[pattern[0]][0] = 1; 
       for (int X = 0, j = 1; j < M; j++)
       {
           for (int c = 0; c < R; c++) 
               dfa[c][j] = dfa[c][X];     // Copy mismatch cases. 
           dfa[pattern[j]][j] = j+1;      // Set match case. 
           X = dfa[pattern[j]][X];        // Update restart state. 
       } 
   } 

   // return offset of first match; N if no match
   public int search(String txt)
   {

       // simulate operation of DFA on text
       int M = pat.length();
       int N = txt.length();
       int i, j;
       for (i = 0, j = 0; i < N && j < M; i++)
       {
           j = dfa[txt.charAt(i)][j];
       }
       if (j == M) return i - M;    // found
       return -1;                    // not found
   }


   // return offset of first match; N if no match
   public int search(char[] text)
   {

       // simulate operation of DFA on text
       int M = pattern.length;
       int N = text.length;
       int i, j;
       for (i = 0, j = 0; i < N && j < M; i++)
       {
           j = dfa[text[i]][j];
       }
       if (j == M) return i - M;    // found
       return -1;                    // not found
   }

}
