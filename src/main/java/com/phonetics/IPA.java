package com.phonetics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

class IPA{
    final String symbol;
    final String example;
    final List<String> letters;

    public IPA(String symbol, String example, List<String> letters) {
        this.symbol = symbol;
        this.example = example;
        this.letters = letters;
    }

    public String getSymbol() {
        return symbol;
    }


    public List<String> getLetters() {
        return letters;
    }


    @Override
    public String toString() {
        return "IPA{" +
                "symbol='" + symbol + '\'' +
                ", example='" + example + '\'' +
                ", letters=" + letters +
                '}';
    }

    public static Map<String, IPA> createIPAsFromFile(String file) throws Exception{
        Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-16");
        BufferedReader buff = new BufferedReader(reader);
        String line;
        int counter =0;
        Map<String, IPA> ipas = new HashMap<String, IPA>();


        while((line = buff.readLine()) != null){
            counter ++;
            if(counter ==1)continue;

            String[] parts = line.split("\\t");


            List<String> letters = new ArrayList<String>();
            for(int i=2; i<parts.length; i++){
                if(!parts[i].isEmpty()){
                    letters.add(parts[i]);
                }
            }

            IPA ipa = new IPA(parts[0], parts[1], letters);

            ipas.put(ipa.getSymbol(), ipa);
        }
        reader.close();
        return ipas;
    }
}