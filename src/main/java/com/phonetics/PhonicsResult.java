package com.phonetics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by daniel on 19/12/2014.
 */
public class PhonicsResult {
    private List<IPA> ipas;
    private String splitWord;
    private String word;

    public PhonicsResult(List<IPA> ipas, String splitWord, String word) {
        this.ipas = ipas;
        this.splitWord = splitWord;
        this.word = word;
    }

    public List<IPA> getIpas() {
        return ipas;
    }

    public List<String> getSplitWords() {
        return Arrays.asList(splitWord.split("~"));
    }

    public String getWord(){
        return word;
    }

    public String toString(){
        return splitWord + "\t{ " + IPA.getSymbolsAsString(ipas) + " }";
    }
}
