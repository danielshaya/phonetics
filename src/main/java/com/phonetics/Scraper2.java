package com.phonetics;

import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by daniel on 22/12/2014.
 */
public class Scraper2 {

    static UserAgent userAgent = new UserAgent();

    public static void main(String[] args)throws Exception{

        map();
    }

    private static String map() {
        try{
                                  //create new userAgent (headless browser).
            userAgent.visit("http://www.oed.com/view/Entry/500");


            userAgent.doc.fillout("Library card number", "20131009065979");       //fill out the component labelled 'Username:' with "tom"
            userAgent.doc.submit();                          //submit the form
            System.out.println(userAgent.getLocation());

            //print the content as HTML
            userAgent.visit("http://www.oed.com/view/Entry/500");
            System.out.println(userAgent.doc.innerHTML());              //print the content as HTML
            List<Element> es = userAgent.doc.findEvery("<span class=\"phonetics\">").toList();


            Element root = userAgent.doc.getRoot();
            return null;
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.

            e.printStackTrace();
            return null;
        }
    }
}
