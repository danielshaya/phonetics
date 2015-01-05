package com.phonetics;

import java.io.*;
import java.util.*;

/**
 * Created by daniel on 17/12/2014.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        InputStreamReader readerExclude = new InputStreamReader(new FileInputStream("src/main/resources/exclusions.txt"), "UTF-8");
        BufferedReader buffexclude = new BufferedReader(readerExclude);

        List<String>exclusions = new ArrayList<>();
        String line;

        while((line = buffexclude.readLine()) != null) {
            line = line.trim();
            exclusions.add(line);
        }



        InputStreamReader reader = new InputStreamReader(new FileInputStream("src/main/resources/mappedWords1.txt"), "UTF-8");
        BufferedReader buff = new BufferedReader(reader);

        PhoneticsProcessor phoneticsProcessor = new PhoneticsProcessor();
        phoneticsProcessor.setIpas(IPA.createIPAsFromFile("src/main/resources/dictionary12.txt"));


        int valid = 0;
        int error = 0;

        FileWriter fw = new FileWriter("src/main/resources/errors.txt");
        FileWriter fw1 = new FileWriter("src/main/resources/wordlist.txt");

        while((line = buff.readLine()) != null) {
            String word = line.split("\\t")[0];
            String IPAword = line.split("\\t")[1];

            //if(exclusions.contains(word))continue;
            if(word.contains(" "))continue;
            if(word.contains("-"))continue;
            if(word.contains("'"))continue;


            try {
                List<PhonicsResult> results = phoneticsProcessor.process(IPAword, word);
                for(PhonicsResult result : results){
                    fw1.write(result.getWord() + " -> " +  IPA.getSymbolsAsString(result.getIpas()) + " -> " + result.getSplitWords() + "\n");
                }
            }catch(AssertionError e){
                fw.write(e.getMessage() + "\n");
                System.out.println(e.getMessage());
                error++;
                continue;

            }
            valid++;

        }
        fw.write("valid " + valid + "\n");
        fw.write("error " + error + "\n");

        System.out.println("valid " + valid);
        System.out.println("error " + error);
        fw.flush();
        fw.close();

        fw1.flush();
        fw1.close();
    }
}
