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
        ipaWord = ipaWord.replaceAll("ˈ", "");
        ipaWord = ipaWord.replaceAll("ˌ", "");

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
                    boolean equal = checkForEqualIPALists(results, ipas);
                    if(!equal){
                        results.add(new PhonicsResult(ipas, wordPart, word));
                    }
                }
            }
        }

        try {
            replaceCombinedWithSingles(results, "e", "ɪ");
            replaceCombinedWithSingles(results, "d", "ʒ");
            replaceCombinedWithSingles(results, "e", "ə");
            replaceCombinedWithSingles(results, "eə", "(r)");
            replaceCombinedWithSingles(results, "e", "ə(r)");
            replaceCombinedWithSingles(results, "ɪ", "ə");
            replaceCombinedWithSingles(results, "ɪ", "ə(r)");
            replaceCombinedWithSingles(results, "jʊ", "ə");
            replaceCombinedWithSingles(results, "jʊ", "ə(r)");
            replaceCombinedWithSingles(results, "a", "ɪ");
            replaceCombinedWithSingles(results, "ə", "ʊ");
            replaceCombinedWithSingles(results, "j", "u");
            replaceCombinedWithSingles(results, "j", "uː");
            replaceCombinedWithSingles(results, "o", "ʊ");
            replaceCombinedWithSingles(results, "t", "ʃ");
            replaceCombinedWithSingles(results, "ʊ", "ə");
            replaceCombinedWithSingles(results, "ʊ", "ə(r)");
            replaceCombinedWithSingles(results, "k", "s");
            replaceCombinedWithSingles(results, "k", "ʃ");
            replaceCombinedWithSingles(results, "j", "ʊ");
            replaceCombinedWithSingles(results, "j", "ə");
            replaceCombinedWithSingles(results, "j", "ə(r)");
            replaceCombinedWithSingles(results, "n", "j");
            replaceCombinedWithSingles(results, "l", "j");
            replaceCombinedWithSingles(results, "ə", "m");
            replaceCombinedWithSingles(results, "ə", "n");
            replaceCombinedWithSingles(results, "ə", "l");
            replaceCombinedWithSingles(results, "ə", "r");

            replaceSingleWithCombined(results, "e", "ər", "eər");


        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println(results);

        if(results.size()==0){
            throw new AssertionError("No match for " + word + " for these IPA objects " + IPA.getSymbolsAsString(ipaCombinations.get(0)));
        }else if(results.size() >1){
            String combinations = "";
            for(PhonicsResult result : results){
                combinations += IPA.getSymbolsAsString(result.getIpas()) + "\n";
            }
            combinations = combinations.substring(0,combinations.length()-1);//remove the last newLine
            throw new AssertionError("Warning more than one mapping valid for " + word + ":" + ipaWord + "\n" + combinations);
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

    private boolean checkForEqualIPALists(List<PhonicsResult> results, List<IPA> ipas){
        boolean ret = false;
        for(PhonicsResult result : results){
            List<IPA> ipas1 = result.getIpas();
            if(ipas1.size() != ipas.size())continue;

            int count=0;
            for(IPA ipa : ipas1){
                if(!ipa.equals(ipas.get(count)))break;
                count++;
            }
            ret = true;
            break;
        }
        return ret;
    }

    private void replaceCombinedWithSingles(List<PhonicsResult> inResults, String first, String second){
        if(inResults.size() <= 1)return;


        boolean found = false;
        Iterator<PhonicsResult> it = inResults.iterator();

        while (it.hasNext()) {
            List<IPA> ipas = it.next().getIpas();
            boolean e = false;
            for (IPA ipa : ipas) {
                if (ipa.getSymbol().equals(first)) {
                    e = true;
                } else if (ipa.getSymbol().equals(second) && e) {
                    //We have eɪ so remove it
                    found=true;
                    break;
                } else {
                    e = false;
                }
            }
        }


        if(found) {
            String combined = first + second;
            it = inResults.iterator();
            while (it.hasNext()) {
                List<IPA> ipas = it.next().getIpas();
                for (IPA ipa : ipas) {
                    if (ipa.getSymbol().equals(combined)) {
                        it.remove();
                        break;
                    }
                }
            }
        }
    }

    private void replaceSingleWithCombined(List<PhonicsResult> inResults, String first, String second, String combined){
        if(inResults.size() <= 1)return;

        boolean found = false;
        Iterator<PhonicsResult>  it = inResults.iterator();
        while(it.hasNext()){
            List<IPA> ipas = it.next().getIpas();
            for(IPA ipa : ipas){
                if(ipa.getSymbol().equals(combined)){
                    found=true;
                    break;
                }
            }
        }

        it = inResults.iterator();
        if(found){
            while(it.hasNext()){
                List<IPA> ipas = it.next().getIpas();
                boolean e = false;
                for(IPA ipa : ipas){
                    if(ipa.getSymbol().equals(first)){
                        e=true;
                    } else if(ipa.getSymbol().equals(second) && e){
                        //We have eɪ so remove it
                        it.remove();
                        break;
                    }else {
                        e = false;
                    }
                }
            }
        }
    }
}
