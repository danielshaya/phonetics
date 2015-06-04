package com.phonetics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by daniel on 29/12/2014.
 */
public class WordListCombiner {
    public static void main(String[] args) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/mappedWords3.txt"));

        String line = null;
        TreeSet<String> ts = new TreeSet<>();

        while((line = reader.readLine()) != null){
            ts.add(line);
        }

        System.out.println(ts.size());

        reader = new BufferedReader(new FileReader("src/main/resources/mappedWords4.txt"));

        while((line = reader.readLine()) != null){
            ts.add(line);
        }
        System.out.println(ts.size());

        FileWriter fw = new FileWriter("src/main/resources/mappedWords5.txt");
        Iterator<String>it = ts.iterator();
        while(it.hasNext()){
            fw.write(it.next() + "\n");
        }
        fw.flush();
        fw.close();
    }
}
