package com.spring.batch.utils;

import java.io.*;
import java.util.*;

public class CompareTextFilesPerf{
   public static void main(String args[]){
      try ( BufferedReader reader1 = new BufferedReader(new FileReader("file1.txt"));
            BufferedReader reader2 = new BufferedReader(new FileReader("file2.txt")) ){

            //assuming file1.txt is smaller than file2.txt in terms of no. of lines
            HashSet<String> file1 = new HashSet<String>();

            String s = null;
            while( ( s = reader1.readLine()) != null){
               file1.add(s);
            }

            while( (s = reader2.readLine()) != null ){
               if(file1.contains(s))
                  System.out.println(s);
            }
      }
      catch(IOException e){
         System.out.println(e);
      }

   }
}