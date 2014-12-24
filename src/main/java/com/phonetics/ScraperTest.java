package com.phonetics;

import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 24/12/2014.
 */
public class ScraperTest {
    static UserAgent userAgent = new UserAgent();

    public static void main(String[] args) {
        test("absorbent");
    }

    private static void test(String word){
        try{
            //create new userAgent (headless browser).
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


        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.

            e.printStackTrace();
        }
    }
}
