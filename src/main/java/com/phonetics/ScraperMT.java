package com.phonetics;

import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by daniel on 22/12/2014.
 */
public class ScraperMT {
    ArrayBlockingQueue<List<String>> resultsQueue = new ArrayBlockingQueue(10);
    ArrayBlockingQueue<String> workQueue = new ArrayBlockingQueue(10);

    public static void main(String[] args)throws Exception{
        new ScraperMT().init();
    }

    public void init(){
        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            Runnable worker = new Fetcher();
            executor.execute(worker);
        }

        new Thread() {
            public void run() {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader("src/main/resources/words.txt"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                String line = null;

                try {
                    while ((line = reader.readLine()) != null)
                    {
                        line = line.trim();
                        workQueue.put(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        int mapped = 0;


        FileWriter writer = null;
        try {
            writer = new FileWriter("src/main/resources/mappedWords3.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                List<String> mappedWords = resultsQueue.take();
                if (mappedWords != null) {
                    for (String mappedWord : mappedWords) {
                        mapped++;
                        writer.write(mappedWord + "\n");
                    }
                }
                if (mapped % 100 == 0) {
                    System.out.println(mapped);
                    writer.flush();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private class Fetcher implements Runnable {
        UserAgent userAgent = new UserAgent();
        public void run() {
            while (true) {
                try {
                    try {
                        userAgent.visit("http://www.oxfordlearnersdictionaries.com/definition/english/" + workQueue.take() + "_1");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Element es = userAgent.doc.findFirst("<span class=\"i\"");
                    List<Element> drs = userAgent.doc.findEvery("<span class=\"dr\"").toList();
                    List<Element> iffs = userAgent.doc.findEvery("<span class=\"if\"").toList();


                    List mappedWords = new ArrayList<>();
                    //System.out.println(userAgent.doc.innerHTML());
                    mappedWords.add(es.getAt("id").split("_")[0] + "\t" + es.getText());

                    for (Element dr : drs) {
                        Element p = dr.getParent();
                        Element ch = p.findFirst("<div class=\"ei-g\"");
                        Element i = p.findFirst("<span class=\"i\"");

                        mappedWords.add(dr.getText() + "\t" + i.getText());
                    }

                    for (Element iff : iffs) {
                        Element p = iff.getParent();
                        Element ch = p.findFirst("<div class=\"ei-g\"");
                        Element i = p.findFirst("<span class=\"i\"");

                        mappedWords.add(iff.getText() + "\t" + i.getText());
                    }

                    System.out.println(mappedWords);

                    try {
                        resultsQueue.put(mappedWords);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (JauntException e) {         //if an HTTP/connection error occurs, handle JauntException.

                    //System.err.println(e);
                }
            }
        }
    }


}
