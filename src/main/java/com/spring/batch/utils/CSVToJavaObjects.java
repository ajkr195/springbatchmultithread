package com.spring.batch.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.spring.batch.model.Sales;

public class CSVToJavaObjects {
	private static String MYSEPARATOR = ",";
	private static String INPUTFILEPATH = "path/to/some/csv/file";
	
	@SuppressWarnings("unused")
	private List<Sales> processInputFile(String INPUTFILEPATH) throws FileNotFoundException {
	    List<Sales> inputList = new ArrayList<Sales>();
	    try{
	      File inputF = new File(INPUTFILEPATH);
	      InputStream inputFS = new FileInputStream(INPUTFILEPATH);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      // skip the header of the csv
	      inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
	      br.close();
	    } catch (IOException e) {
	      System.out.println("Catch Exceptions here");
	    }
	    return inputList ;
	}
	private Function<String, Sales> mapToItem = (line) -> {
		  String[] p = line.split(MYSEPARATOR);// a CSV has comma separated lines
		  Sales item = new Sales();
		  item.setRegion(p[0]);//<-- this is the first column in the csv file
		  if (p[3] != null && p[3].trim().length() > 0) {
		    item.setCountry(p[3]);
		  }
		  //more initialization goes here
		  return item;
		};

}
