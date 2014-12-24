package com.phonetics;

import java.io.*;
import java.util.*;

/**
 * Created by daniel on 17/12/2014.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        InputStreamReader reader = new InputStreamReader(new FileInputStream("src/main/resources/mappedWords3.txt"), "UTF-8");
        BufferedReader buff = new BufferedReader(reader);

        PhoneticsProcessor phoneticsProcessor = new PhoneticsProcessor();
        phoneticsProcessor.setIpas(IPA.createIPAsFromFile("src/main/resources/dictionary.txt"));

        String line;
        int valid = 0;
        int error = 0;

        FileWriter fw = new FileWriter("src/main/resources/output.txt");

        while((line = buff.readLine()) != null) {
            String word = line.split("\\t")[0];
            String IPAword = line.split("\\t")[1];

            try {
                phoneticsProcessor.process(IPAword, word);
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
    }
}
