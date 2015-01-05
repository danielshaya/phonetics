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
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 22/12/2014.
 */
public class ScraperMT {
    ArrayBlockingQueue<List<String>> resultsQueue = new ArrayBlockingQueue(200);
    ArrayBlockingQueue<String> workQueue = new ArrayBlockingQueue(200);
    int allwords=0;

    public static void main(String[] args)throws Exception{
        new ScraperMT().init();
    }

    public void init(){

        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 200; i++) {
            Runnable worker = new Fetcher();
            executor.execute(worker);
        }

        new Thread() {
            public void run() {
                BufferedReader reader = null;
                try {
                    //reader = new BufferedReader(new FileReader("src/main/resources/allwords.txt"));
                    reader = new BufferedReader(new FileReader("src/main/resources/words.txt"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                String line = null;

                try {
                    while ((line = reader.readLine()) != null)
                    {
                        line = line.trim();
                        allwords++;
                        if(line.contains(" ") || line.contains("-") || line.contains("'") || line.contains("‡"))continue;
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
            writer = new FileWriter("src/main/resources/mappedWords2.txt");
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
                        System.out.println(mappedWord);
                    }
                }
                if (mapped % 500 == 0) {
                    System.out.println(mapped + " from " + allwords + mappedWords);
                    writer.flush();
                }

                if(workQueue.isEmpty() && resultsQueue.isEmpty()){
                    System.out.println("empty");
                    Thread.sleep(5000);
                    if(resultsQueue.peek()==null){
                        writer.flush();
                        writer.close();
                        System.out.println("exit");
                        System.exit(0);
                    }
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
                        String word = workQueue.poll(1, TimeUnit.SECONDS);
                        if(word==null)return;
                        userAgent.visit("http://www.oxfordlearnersdictionaries.com/definition/english/" + word + "_1");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List mappedWords = new ArrayList<>();

                    Element topg = userAgent.doc.findFirst("<div class=\"top-g\"");

                    //find the ei-g elements
                    List<Element> eigs = userAgent.doc.findEvery("<div class=\"ei-g\"").toList();
                    if(eigs.size()<1){
                        //these words are not found in the dictionary
                        //might be in the American dictionary.
                        continue;
                    }
                    mappedWords.add(eigs.get(0).getAt("id").split("_")[0] + "\t" + getUKPhonetic(eigs.get(0)));

                    //Check for a second level words
                    try {
                        Element h2 = topg.findFirst("<span class=\"h2\"");
                        if(eigs.size()>=2)mappedWords.add(h2.getText() + "\t" + getUKPhonetic(eigs.get(1)));
                    }catch(JauntException e){
                        //No problem there may not be a second level word
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    mapSecondaryItems(mappedWords, "if", userAgent);
                    mapSecondaryItems(mappedWords, "dr", userAgent);

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

    private static void mapSecondaryItems(List mappedWords, String secondary, UserAgent userAgent) {
        try {
            Element ifg = userAgent.doc.findFirst("<span class=\"" + secondary + "-g\"");
            List<Element> children = ifg.getChildElements();

            Element lastIf = null;
            for(Element child: children){
                if(child.getAt("class").equals(secondary)){
                    lastIf = child;
                }else if(child.getAt("class").equals("ei-g")){
                    if(lastIf != null)
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
