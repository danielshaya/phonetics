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

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/words.txt"));
        String line = null;
        //String[] words = new String[]{"born", "holiday", "seem", "stretch", "bath"};
        int mapped = 0;
        int count = 0;

        FileWriter writer = new FileWriter("src/main/resources/mappedWords2.txt");

        while((line = reader.readLine()) != null){
            line = line.trim();
            List<String> mappedWords = map(line);
            if(mappedWords != null){
                for(String mappedWord : mappedWords) {
                    mapped++;
                    writer.write(mappedWord + "\n");
                }
            }
            if(mapped % 100 == 0){
                System.out.println(mapped);
                writer.flush();
            }
            count++;
        }

        writer.flush();
        writer.close();

        System.out.println("Mapped words " + mapped);
        System.out.println("Original list in file " + count);


    }

    private static List<String> map(String word) {
        try{
            userAgent.visit("http://www.oxfordlearnersdictionaries.com/definition/english/" + word + "_1");

            Element es = userAgent.doc.findFirst("<span class=\"i\"");
            List<Element> drs = userAgent.doc.findEvery("<span class=\"dr\"").toList();
            List<Element> iffs = userAgent.doc.findEvery("<span class=\"if\"").toList();



            List mappedWords = new ArrayList<>();
            //System.out.println(userAgent.doc.innerHTML());
            mappedWords.add(es.getAt("id").split("_")[0] + "\t" + es.getText());

            for(Element dr : drs){
                Element p = dr.getParent();
                Element ch = p.findFirst("<div class=\"ei-g\"");
                Element i = p.findFirst("<span class=\"i\"");

                mappedWords.add(dr.getText() + "\t" + i.getText());
            }

            for(Element iff : iffs){
                Element p = iff.getParent();
                Element ch = p.findFirst("<div class=\"ei-g\"");
                Element i = p.findFirst("<span class=\"i\"");

                mappedWords.add(iff.getText() + "\t" + i.getText());
            }

            System.out.println(mappedWords);


            return mappedWords;
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
            return null;
            //System.err.println(e);
        }
    }


}
