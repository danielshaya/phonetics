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

    public PhonicsResult(List<IPA> ipas, String splitWord) {
        this.ipas = ipas;
        this.splitWord = splitWord;
    }

    public List<IPA> getIpas() {
        return ipas;
    }

    public List<String> getSplitWords() {
        return Arrays.asList(splitWord.split("~"));
    }

    public String toString(){
        return splitWord + "\t{ " + ipas.stream().map(IPA::getSymbol).collect(Collectors.joining(", ")) + " }";
    }
}
