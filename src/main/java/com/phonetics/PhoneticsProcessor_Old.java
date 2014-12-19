package com.phonetics;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 17/12/2014.
 */
public class PhoneticsProcessor_Old {
    private Map<String, IPA> ipas = new HashMap<String, IPA>();

    public void setIpas(Map<String, IPA> ipas) {
        this.ipas = ipas;
    }

    public boolean process(Deque<String> stack, String IPAword, String word) {
        stack.push(IPAword + "\t" + word);
        IPAword = IPAword.replace("ˈ", "");
        int letterInWord = 0;
        String unmappedSymbol = "";

        int lengthOfLastMatch = 0;
        String lastSymbol = null;
        boolean tryAgain = false;

        for(int i=0; i<IPAword.length(); i++){
            String symbol = unmappedSymbol + IPAword.substring(i, i+1);
            if(tryAgain){
                symbol = lastSymbol;
            }
            unmappedSymbol = "";
            IPA ipa = ipas.get(symbol);
            //char not shown
            if(ipa==null){
                //The IPA symbol is not in the map - this means that it must be more than one char.
                //hait - the a has not match but ai go together to match.
                unmappedSymbol = symbol;
                continue;
            }

            //try to match against letter in word
            String letterLeft;
            if(tryAgain) {
                letterLeft = word.substring(letterInWord, letterInWord + lengthOfLastMatch);
            }else{
                letterLeft = word.substring(letterInWord, word.length());
            }
            //Maybe the there are more than one letter that map to this word.
            //e.g. ai maps to ei, eigh.
            //The strategy is to match as many as we can and if letters further on
            //don't match roll back.
            String lettersFromSymbol = getLettersFromSymbol(ipa,letterLeft);
            if(lettersFromSymbol!=null){
                //This is the simplest match
                stack.push(ipa.getSymbol() + "\t" + lettersFromSymbol);
                //System.out.println(ipa.getSymbol() + "\t" + lettersFromSymbol);
                letterInWord = letterInWord + lettersFromSymbol.length();
                lengthOfLastMatch = lettersFromSymbol.length();
                lastSymbol = symbol;
                tryAgain = false;
                continue;
            }

            if(lengthOfLastMatch != 1){
                i=i-2;//go round loop again this time matching less letters
                letterInWord = letterInWord - lengthOfLastMatch;
                lengthOfLastMatch--;
                tryAgain = true;
                stack.pop();
                continue;
            }

            //if there is no match for the letter then it could be that we should take more than one symbol together
            //so look back one symbol and continue
            symbol = IPAword.substring(i-1,i+1);
            letterLeft = word.substring(letterInWord-1, word.length());
            ipa = ipas.get(symbol);


            if(ipa!=null){
                lettersFromSymbol = getLettersFromSymbol(ipa,letterLeft);
                stack.pop();
                //System.out.println(ipa.getSymbol() + "\t" + letterLeft);
                stack.push(ipa.getSymbol() + "\t" + lettersFromSymbol);
                continue;
            }

            //now try looking forward
            letterLeft = word.substring(letterInWord, word.length());
            symbol = IPAword.substring(i,IPAword.length());
            ipa = getIPAFromSymbol(symbol,1,letterLeft);


            if(ipa!=null){
                lettersFromSymbol = getLettersFromSymbol(ipa,letterLeft);
                stack.push(ipa.getSymbol() + "\t" + lettersFromSymbol);
                letterInWord = letterInWord - lettersFromSymbol.length();
                continue;
            }

            //No match we have problem
            throw new AssertionError("No match for " + symbol + " in word " + IPAword + "\n" + stack);
        }
        return false;
    }

    private IPA getIPAFromSymbol(String symbol, int l, String letters){
        if(l>symbol.length())return null;
        String key = symbol.substring(0,l);
        IPA ipa = ipas.get(key);
        if(ipa!=null && getLettersFromSymbol(ipa, letters)!=null){
            return ipa;
        }
        return getIPAFromSymbol(symbol,++l,letters);
    }

    private String getLettersFromSymbol(IPA ipa, String letters){
        if(letters.isEmpty())return null;
        if(ipa.getLetters().contains(letters)){
            return letters;
        }

        letters = letters.substring(0, letters.length()-1);
        return getLettersFromSymbol(ipa, letters);
    }

}
