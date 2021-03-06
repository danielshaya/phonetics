package com.phonetics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

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
        Map<String, IPA> ipas = new HashMap<String, IPA>();


        while((line = buff.readLine()) != null){

            String[] parts = line.split("\\t");


            List<String> letters = new ArrayList<String>();
            for(int i=2; i<parts.length; i++){
                if(parts[i].length() >0){
                    letters.add(parts[i]);
                }
            }

            if(parts.length > 1) {
                IPA ipa = new IPA(parts[0], parts[1], letters);

                ipas.put(ipa.getSymbol(), ipa);
            }
        }
        reader.close();
        return ipas;
    }

    public static String getSymbolsAsString(List<IPA> ipas){
        return ipas.stream().map(IPA::getSymbol).collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IPA ipa = (IPA) o;

        if (symbol != null ? !symbol.equals(ipa.symbol) : ipa.symbol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : 0;
    }
}