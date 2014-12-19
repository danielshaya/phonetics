package com.phonetics;

import java.io.*;
import java.util.*;

/**
 * Created by daniel on 17/12/2014.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        Deque<String> stack = new ArrayDeque<String>();


        InputStreamReader reader = new InputStreamReader(new FileInputStream("src/main/resources/wordlist.txt"), "UTF-16");
        BufferedReader buff = new BufferedReader(reader);

        PhoneticsProcessor_Old phoneticsProcessor = new PhoneticsProcessor_Old();
        phoneticsProcessor.setIpas(IPA.createIPAsFromFile("src/main/resources/dictionary.txt"));

        String line;
        while((line = buff.readLine()) != null) {
            stack.clear();
            String IPAword = line.split("\\t")[0];
            String word = line.split("\\t")[1];

            if (phoneticsProcessor.process(stack, IPAword, word)) continue;
            Iterator<String> it = stack.descendingIterator();
            while(it.hasNext()){
                System.out.println(it.next());
            }

        }
    }
}
