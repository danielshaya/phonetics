package com.phonetics;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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

        FileWriter writer = new FileWriter("src/main/resources/mappedWords.txt");

        while((line = reader.readLine()) != null){
            line = line.trim();
            String mappedWord = map(line);
            if(mappedWord != null){
                mapped ++;
                writer.write(mappedWord + "\n");
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
        System.out.println("Total words " + count);


    }

    private static String map(String word) {
        try{
                                  //create new userAgent (headless browser).
            userAgent.visit("http://www.oxfordlearnersdictionaries.com/definition/english/" + word + "_1");

            List<Element> es = userAgent.doc.findEvery("<span class=\"i\"").toList();

            String mappedWord = null;
            for(Element e : es){
                mappedWord = e.getAt("id").split("_")[0] + " -> " + e.getText();
                continue;
            }

            //print the content as HTML
            //System.out.println(userAgent.doc.innerHTML());              //print the content as HTML
            return mappedWord;
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
            return null;
            //System.err.println(e);
        }
    }


}
