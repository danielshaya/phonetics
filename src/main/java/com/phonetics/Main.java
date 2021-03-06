package com.phonetics;

import java.io.*;
import java.util.*;

/**
 * Created by daniel on 17/12/2014.
 */
public class Main {

    public static final String DICTIONARY_VERSION = "18";

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



        InputStreamReader reader = new InputStreamReader(new FileInputStream("src/main/resources/mappedWords090615.txt"), "UTF-8");
        BufferedReader buff = new BufferedReader(reader);

        PhoneticsProcessor phoneticsProcessor = new PhoneticsProcessor();
        phoneticsProcessor.setIpas(IPA.createIPAsFromFile("src/main/resources/dictionary" + DICTIONARY_VERSION + ".txt"));


        int valid = 0;
        int error = 0;
        int missed = 0;

        FileWriter fw = new FileWriter("src/main/resources/errors.txt");
        FileWriter fw1 = new FileWriter("src/main/resources/wordlist.txt");
        FileWriter fw2 = new FileWriter("src/main/resources/missedOut.txt");


        while((line = buff.readLine()) != null) {
           // System.out.println(line);

            String word = line.split("\\t")[0];
            //todo investigate why this crashes
            if(word.startsWith("hydrochlorofluorocarbon")){
                fw2.write(line + "\n");
                missed++;
                continue;
            }

            String IPAword = line.split("\\t")[1];

            if(replacements.containsKey(word)){
                IPAword = replacements.get(word);
            }

            if(word.contains("'")){
                fw2.write(line + "\n");
                missed++;
                continue;
            }

            String[] words = null;
            String[] IPAwords = null;
            String joiningSymbol = null;

            if(word.contains(" ")){
                joiningSymbol = " ";
                words = word.split(" ");
                IPAwords = IPAword.split(" ");
                //only process if 2 words each
                if(words.length!=2 || IPAwords.length!=2){
                    fw2.write(line + "\n");
                    missed++;
                    continue;
                }
            }

            if(word.contains("-")){
                joiningSymbol = "-";
                words = word.split("-");
                IPAwords = IPAword.split(" ");
                //only process if 2 words each
                if(words.length!=2 || IPAwords.length!=2){
                    fw2.write(line + "\n");
                    missed++;
                    continue;
                }
            }

            if(words==null) {
                try {
                    List<PhonicsResult> results = phoneticsProcessor.process(IPAword, word);
                    for (PhonicsResult result : results) {
                        fw1.write(result.getWord() + " -> " + IPA.getSymbolsAsString(result.getIpas()) + " -> " + result.getSplitWords() + "\n");
                    }
                } catch (AssertionError e) {
                    fw.write(e.getMessage() + "\n");
                    System.out.println(e.getMessage());
                    error++;
                    continue;

                }
            }else {
                try {
                    List<PhonicsResult> results1 = phoneticsProcessor.process(IPAwords[0], words[0]);
                    List<PhonicsResult> results2 = phoneticsProcessor.process(IPAwords[1], words[1]);
                    for(int i=0; i<results1.size(); i++) {
                        fw1.write(results1.get(i).getWord() + joiningSymbol + results2.get(i).getWord()
                                + " -> " + IPA.getSymbolsAsString(results1.get(i).getIpas()) + ", " + IPA.getSymbolsAsString(results2.get(i).getIpas())
                                + " -> " + results1.get(i).getSplitWords() + joiningSymbol + results2.get(i).getSplitWords() + "\n");
                    }
                } catch (AssertionError e) {
                    fw.write(e.getMessage() + "\n");
                    System.out.println(e.getMessage());
                    error++;
                    continue;

                }
            }
            valid++;

        }
        fw.write("valid " + valid + "\n");
        fw.write("error " + error + "\n");
        fw.write("missed " + missed+ "\n");

        System.out.println("valid " + valid);
        System.out.println("error " + error);
        System.out.println("missed " + missed);
        fw.flush();
        fw.close();

        fw1.flush();
        fw1.close();

        fw2.flush();
        fw2.close();

    }
}
