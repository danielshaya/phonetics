package com.phonetics;

import java.io.*;
import java.util.*;

/**
 * Created by daniel on 17/12/2014.
 */
public class Main {

    public static final String DICTIONARY_VERSION = "15";

    public static void main(String[] args) throws Exception{
        InputStreamReader readerReplace = new InputStreamReader(new FileInputStream("src/main/resources/replace.txt"), "UTF-16");
        BufferedReader buffReplace = new BufferedReader(readerReplace);

        Map<String, String>replacements = new HashMap<>();
        String line;

        while((line = buffReplace.readLine()) != null) {
            line = line.trim();
            if(line.isEmpty())continue;
            String[] parts = line.split("\t");
            replacements.put(parts[0], parts[1]);
        }



        InputStreamReader reader = new InputStreamReader(new FileInputStream("src/main/resources/mappedWords1.txt"), "UTF-8");
        BufferedReader buff = new BufferedReader(reader);

        PhoneticsProcessor phoneticsProcessor = new PhoneticsProcessor();
        phoneticsProcessor.setIpas(IPA.createIPAsFromFile("src/main/resources/dictionary" + DICTIONARY_VERSION + ".txt"));


        int valid = 0;
        int error = 0;

        FileWriter fw = new FileWriter("src/main/resources/errors.txt");
        FileWriter fw1 = new FileWriter("src/main/resources/wordlist.txt");

        while((line = buff.readLine()) != null) {
            String word = line.split("\\t")[0];
            String IPAword = line.split("\\t")[1];

            if(replacements.containsKey(word)){
                IPAword = replacements.get(word);
            }
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
