package com.phonetics;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to split a word into all it's components in the following way
 * e.g. for the word hait.
 * [hait, hai:t, ha:it, ha:i:t, h:ait, h:ai:t, h:a:it, h:a:i:t]
 */
public class Spliterator {
    public static void main(String[] args) {
        System.out.println(split("hait"));
    }

    public static List<String> split(String word){
        List<String> list = new ArrayList<String>();

        for(int i=word.length(); i>0; i--){
            String left = word.substring(0,i);
            String right = word.substring(i,word.length());
            if(right.isEmpty())list.add(left);
            List<String> rightList = (split(right));

            for(String r : rightList){
                list.add(left + "~" + r);
            }
        }

        return list;
    }
}
