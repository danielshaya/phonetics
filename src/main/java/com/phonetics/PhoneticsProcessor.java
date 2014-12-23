package com.phonetics;

import java.util.*;

/**
 * Created by daniel on 19/12/2014.
 */
public class PhoneticsProcessor {
    private Map<String, IPA> ipas = new HashMap<String, IPA>();

    public void setIpas(Map<String, IPA> ipas) {
        this.ipas = ipas;
    }

    public List<PhonicsResult> process(String ipaWord, String word){
        //PRE PROCESS:
        ipaWord = ipaWord.replace("ˈ", "");

        //STEP 1: Spliterate the IPAword
        List<String> ipaParts = Spliterator.split(ipaWord);

        //STEP 2: Map as many as possible to IPA objects
        List<List<IPA>> ipaCombinations = new ArrayList<>();

        for(String ipaPart : ipaParts){
            String[] parts = ipaPart.split("~");
            List<IPA> ipaCombination = new ArrayList<IPA>();
            boolean valid = true;
            for(String part : parts){
                if(ipas.containsKey(part)){
                    ipaCombination.add(ipas.get(part));
                }else{
                    valid = false;
                    break;
                }
            }
            if(valid){
                ipaCombinations.add(ipaCombination);
            }
        }

        if(ipaCombinations.size()==0){
            throw new AssertionError("ipaWord '" + ipaWord + "' for '"+ word + "' can not be mapped to IPA objects");
        }

        //STEP 3: Spliterate the word
        List<String> wordParts = Spliterator.split(word);

        //STep 4: Map the ipaCombinations to the wordParts
        List<PhonicsResult> results = new ArrayList<>();
        for(List<IPA> ipas : ipaCombinations){
            for(String wordPart : wordParts){
                String[] parts = wordPart.split("~");
                if(ipas.size() != parts.length)continue;
                boolean valid = match(ipas, parts);
                if(valid){
                    results.add(new PhonicsResult(ipas, wordPart));
                }
            }
        }
        //System.out.println(results);

        if(results.size()==0){
            throw new AssertionError("No match for " + word + " for these IPA objects " + IPA.getSymbolsAsString(ipaCombinations.get(0)));
        }else if(results.size() >1){
            throw new AssertionError("Warning more than one mapping valid for " + ipaWord);
        }
        return results;
    }

    private boolean match(List<IPA> ipas, String[] parts) {
        int i=0;
        for(IPA ipa : ipas){
            if(!ipa.getLetters().contains(parts[i])){
                return false;
            }
            i++;
        }
        return true;
    }
}
