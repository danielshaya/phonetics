package com.phonetics;

import java.io.*;
import java.util.*;

/**
 * Created by daniel on 17/12/2014.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        InputStreamReader reader = new InputStreamReader(new FileInputStream("src/main/resources/mappedWords.txt"), "UTF-8");
        BufferedReader buff = new BufferedReader(reader);

        PhoneticsProcessor phoneticsProcessor = new PhoneticsProcessor();
        phoneticsProcessor.setIpas(IPA.createIPAsFromFile("src/main/resources/dictionary.txt"));

        String line;
        int valid = 0;
        int error = 0;
        while((line = buff.readLine()) != null) {
            String word = line.split("\\t")[0];
            String IPAword = line.split("\\t")[1];

            try {
                phoneticsProcessor.process(IPAword, word);
            }catch(AssertionError e){
                System.out.println(e.getMessage());
                error++;
                continue;

            }
            valid++;

        }
        System.out.println("valid " + valid);
        System.out.println("error " + error);
    }
}
