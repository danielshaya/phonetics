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
//        for(int i =0; i<10; i++) {
//            map();
//            Thread.sleep(5000);
//        }
    }

    private static String map() {
        try{
            //UserAgent userAgent = new UserAgent();
            userAgent.visit("http://www.oed.com/view/Entry/500");


            userAgent.doc.fillout("Library card number", "20131009065979");       //fill out the component labelled 'Username:' with "tom"
            userAgent.doc.submit();                          //submit the form
            //System.out.println(userAgent.getLocation());

            //print the content as HTML

                userAgent.visit("http://www.oed.com/view/Entry/500");
                //System.out.println(userAgent.doc.innerHTML());              //print the content as HTML
                Element es = userAgent.doc.findFirst("<span class=\"hw\">");
                System.out.println(es.getText());



            return null;
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.

            e.printStackTrace();
            return null;
        }
    }

//    private static String map() {
//        try{
//            userAgent.visit("http://www.oed.com/view/Entry/500");
//            userAgent.doc.fillout("Library card number", "20131009065979");
//            userAgent.doc.submit();
//
//
//            //print the content as HTML
//            for(int i=0; i<300; i++) {
//                userAgent.visit("http://www.oed.com/view/Entry/" +i);
//                //System.out.println(userAgent.doc.innerHTML());              //print the content as HTML
//                Element es = userAgent.doc.findFirst("<span class=\"hw\">");
//                System.out.println(es.getText());
//            }
//            return null;
//        }
//        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
//
//            e.printStackTrace();
//            return null;
//        }
//    }
}
