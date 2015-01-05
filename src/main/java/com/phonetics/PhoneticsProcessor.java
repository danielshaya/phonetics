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

        //We don't want spaces in the ipaWord
        ipaWord = ipaWord.replaceAll(" ", "");

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

            replaceSingleWithCombined(results, "e", "ɪ");

            replaceSingleWithCombined(results, "d", "ʒ");
            replaceSingleWithCombined(results, "e", "ə");
            replaceSingleWithCombined(results, "e", "ə(r)");
            replaceSingleWithCombined(results, "ɪ", "ə");
            replaceSingleWithCombined(results, "ɪ", "ə(r)");
            replaceSingleWithCombined(results, "jʊ", "ə");
            replaceSingleWithCombined(results, "jʊ", "ə(r)");
            replaceSingleWithCombined(results, "a", "ɪ");
            replaceSingleWithCombined(results, "ə", "ʊ");
            replaceSingleWithCombined(results, "j", "u");
            replaceSingleWithCombined(results, "j", "uː");
            replaceSingleWithCombined(results, "o", "ʊ");
            replaceSingleWithCombined(results, "t", "ʃ");
            replaceSingleWithCombined(results, "ʊ", "ə");
            replaceSingleWithCombined(results, "ʊ", "ə(r)");
            replaceSingleWithCombined(results, "k", "s");
            replaceSingleWithCombined(results, "k", "ʃ");
            replaceSingleWithCombined(results, "j", "ʊ");
            replaceSingleWithCombined(results, "j", "ʊə(r)");
            replaceSingleWithCombined(results, "j", "ə");
            replaceSingleWithCombined(results, "j", "ə(r)");
            replaceSingleWithCombined(results, "n", "j");
            replaceSingleWithCombined(results, "l", "j");
            replaceSingleWithCombined(results, "ə", "m");
            replaceSingleWithCombined(results, "ə", "n");
            replaceSingleWithCombined(results, "ə", "l");
            replaceSingleWithCombined(results, "ə", "r");

            replaceSingleWithCombined(results, "e", "ər");

            replaceCombinedWithSingles(results, "e", "s");
            replaceCombinedWithSingles(results, "b", "iː");
            replaceCombinedWithSingles(results, "v", "iː");
            replaceCombinedWithSingles(results, "s", "iː");
            replaceCombinedWithSingles(results, "d", "iː");
            replaceCombinedWithSingles(results, "t", "iː");
            replaceCombinedWithSingles(results, "p", "iː");
            replaceCombinedWithSingles(results, "e", "ks");
            replaceCombinedWithSingles(results, "e", "f");
            replaceCombinedWithSingles(results, "e", "n");
            replaceCombinedWithSingles(results, "e", "m");
            replaceCombinedWithSingles(results, "e", "l");
            replaceCombinedWithSingles(results, "dʒ", "iː");
            replaceCombinedWithSingles(results, "dʒ", "eɪ");
            replaceCombinedWithSingles(results, "k", "eɪ");
            replaceCombinedWithSingles(results, "w", "aɪ");
            replaceCombinedWithSingles(results, "z", "ed");
            replaceCombinedWithSingles(results, "ze", "d");
            replaceCombinedWithSingles(results, "eɪ", "tʃ");
            replaceCombinedWithSingles(results, "k", "juː");

            favourTwoForTwo(results, "nj", "uː", "n", "juː");
            favourTwoForTwo(results, "nj", "ə(r)", "n", "jə(r)");
            favourTwoForTwo(results, "nj", "ə", "n", "jə");
            favourTwoForTwo(results, "nj", "u", "n", "ju");
            favourTwoForTwo(results, "nj", "ʊ", "n", "jʊ");
            favourTwoForTwo(results, "lj", "ə", "l", "jə");
            favourTwoForTwo(results, "lj", "ə(r)", "l", "jə(r)");
            favourTwoForTwo(results, "lj", "u", "l", "ju");
            favourTwoForTwo(results, "lj", "ʊ", "l", "jʊ");
            favourTwoForTwo(results, "nj", "ʊ", "n", "jʊ");
            favourTwoForTwo(results, "nj", "ʊə(r)", "n", "jʊə(r)");
            favourTwoForTwo(results, "nj", "ʊə", "n", "jʊə");
            favourTwoForTwo(results, "lj", "ʊə", "l", "jʊə");
            favourTwoForTwo(results, "eə", "r", "e", "ər");
            favourTwoForTwo(results, "ɪə", "r", "ɪ", "ər");
            favourTwoForTwo(results, "ɪə", "l", "ɪ", "əl");
            favourTwoForTwo(results, "ʊə", "r", "ʊ", "ər");
            favourTwoForTwo(results, "jʊə", "r", "jʊ", "ər");
            favourTwoForTwo(results, "eɪ", "ə(r)", "e", "ɪə(r)");
            favourTwoForTwo(results, "eɪ", "ə", "e", "ɪə");
            favourTwoForTwo(results, "ɪə", "l", "ɪ", "əl");

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
            if(count==ipas.size())ret = true;
        }
        return ret;
    }


    private void replaceCombinedWithSingles(List<PhonicsResult> inResults, String first, String second){
        if(inResults.size() <= 1)return;


        boolean found = false;
        Iterator<PhonicsResult> it = inResults.iterator();

        int foundIn = -1;
        int count = 0;

        while (it.hasNext()) {
            List<IPA> ipas = it.next().getIpas();
            boolean e = false;
            for (IPA ipa : ipas) {
                if (ipa.getSymbol().equals(first)) {
                    e = true;
                } else if (ipa.getSymbol().equals(second) && e) {
                    //We have eɪ so remove it
                    found=true;
                    foundIn = count;
                    break;
                } else {
                    e = false;
                }
            }
            count++;
        }


        if(found) {
            count=0;
            String combined = first + second;
            it = inResults.iterator();
            while (it.hasNext()) {
                List<IPA> ipas = it.next().getIpas();
                for (IPA ipa : ipas) {
                    if (ipa.getSymbol().equals(combined)) {
                        //make sure not to remove it from the wrong result
                        if(foundIn != count) {
                            it.remove();
                            break;
                        }
                    }
                }
                count++;
            }
        }
    }

    private void replaceSingleWithCombined(List<PhonicsResult> inResults, String first, String second){
        if(inResults.size() <= 1)return;

        String combined = first+second;
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

    private void favourTwoForTwo(List<PhonicsResult> inResults, String replaceFirst, String replaceSecond, String withFirst, String withSecond){
        if(inResults.size() <= 1)return;


        boolean found = false;
        Iterator<PhonicsResult> it = inResults.iterator();

        int foundIn = -1;
        int count = 0;

        //are the first two present
        while (it.hasNext()) {
            List<IPA> ipas = it.next().getIpas();
            boolean e = false;
            for (IPA ipa : ipas) {
                if (ipa.getSymbol().equals(replaceFirst)) {
                    e = true;
                } else if (ipa.getSymbol().equals(replaceSecond) && e) {
                    found=true;
                    foundIn = count;
                    break;
                } else {
                    e = false;
                }
            }
            count++;
        }


        //are the second 2 present
        if(found){
            found = false;
            count =0;
            it = inResults.iterator();
            while (it.hasNext()) {
                List<IPA> ipas = it.next().getIpas();
                boolean e = false;
                for (IPA ipa : ipas) {
                    if (ipa.getSymbol().equals(withFirst)) {
                        e = true;
                    } else if (ipa.getSymbol().equals(withSecond) && e) {
                        found=true;
                        break;
                    } else {
                        e = false;
                    }
                }
                count++;
            }
        }


        //all are present so
        if(found) {
            inResults.remove(foundIn);
        }
    }
}
