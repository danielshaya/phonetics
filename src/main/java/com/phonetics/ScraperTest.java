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
        test();
    }

    private static void test(){
        try{
            //create new userAgent (headless browser).
            userAgent.visit("http://www.oxfordlearnersdictionaries.com/definition/english/go_1");

            List<Element> es = userAgent.doc.findEvery("<span class=\"i\"").toList();
            List<Element> dr = userAgent.doc.findEvery("<span class=\"dr\"").toList();
            List<Element> iff = userAgent.doc.findEvery("<span class=\"if\"").toList();

            List mappedWords = new ArrayList<>();
            System.out.println(userAgent.doc.innerHTML());
            int count =0;
            for(Element e : es){
                if(count==0){
                    mappedWords.add(e.getAt("id").split("_")[0] + "\t" + e.getText());
                }else {
                    if(dr.size()>0) {
                        mappedWords.add(dr.get(count - 1).getText() + "\t" + e.getText());
                    }else{
                        mappedWords.add(iff.get(count - 1).getText() + "\t" + e.getText());
                    }
                }
                count++;
            }

            System.out.println(mappedWords);
            //print the content as HTML
                          //print the content as HTML

        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.

            e.printStackTrace();
        }
    }
}
