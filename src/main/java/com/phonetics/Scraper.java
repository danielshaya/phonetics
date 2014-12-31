package com.phonetics;

import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 22/12/2014.
 */
public class Scraper {

    static UserAgent userAgent = new UserAgent();

    public static void main(String[] args)throws Exception{

//        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/words.txt"));
//        String line = null;
//        //String[] words = new String[]{"born", "holiday", "seem", "stretch", "bath"};
//        int mapped = 0;
//        int count = 0;
//
//        FileWriter writer = new FileWriter("src/main/resources/mappedWords2.txt");
//
//        while((line = reader.readLine()) != null){
//            line = line.trim();
//            List<String> mappedWords = map(line);
//            if(mappedWords != null){
//                for(String mappedWord : mappedWords) {
//                    mapped++;
//                    writer.write(mappedWord + "\n");
//                }
//            }
//            if(mapped % 100 == 0){
//                System.out.println(mapped);
//                writer.flush();
//            }
//            count++;
//        }
//
//        writer.flush();
//        writer.close();
//
//        System.out.println("Mapped words " + mapped);
//        System.out.println("Original list in file " + count);

//        System.out.println(map("airman"));
//        System.out.println(map("abate"));
//        System.out.println(map("seat"));
//        System.out.println(map("beat"));
//        System.out.println(map("bespeak"));
//        System.out.println(map("come"));
       // System.out.println(map("absent"));
        System.out.println(map("aa"));


    }

    private static List<String> map(String word) {
        try{
            List mappedWords = new ArrayList<>();
            userAgent.visit("http://www.oxfordlearnersdictionaries.com/definition/english/" + word + "_1");


            Element topg = userAgent.doc.findFirst("<div class=\"top-g\"");

            //find the ei-g elements
            List<Element> eigs = userAgent.doc.findEvery("<div class=\"ei-g\"").toList();
            List<Element> iis = userAgent.doc.findEvery("<span class=\"i\"").toList();

            //Must always be a first level word
            mappedWords.add(eigs.get(0).getAt("id").split("_")[0] + "\t" + getUKPhonetic(eigs.get(0)));

            //Check for a second level words
            try {
                Element h2 = topg.findFirst("<span class=\"h2\"");
                mappedWords.add(h2.getText() + "\t" + getUKPhonetic(eigs.get(1)));
            }catch(JauntException e){
                //No problem there may not be a second level word
            }


            mapSecondaryItems(mappedWords, "if");
            mapSecondaryItems(mappedWords, "dr");


            return mappedWords;
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.

            System.err.println(e);
            return null;
        }
    }

    private static void mapSecondaryItems(List mappedWords, String secondary) {
        try {
            Element ifg = userAgent.doc.findFirst("<span class=\"" + secondary + "-g\"");
            List<Element> children = ifg.getChildElements();

            Element lastIf = null;
            for(Element child: children){
                if(child.getAt("class").equals(secondary)){
                    lastIf = child;
                }else if(child.getAt("class").equals("ei-g")){
                    mappedWords.add(lastIf.getText() + "\t" + getUKPhonetic(child));
                }
            }
        }catch(JauntException e){
            //Not a problem may not be iffs
        }
    }

    private static String getUKPhonetic(Element eig) throws JauntException{
        Element i = eig.findFirst("<span class=\"i\"");
        return i.getText();
    }
}
