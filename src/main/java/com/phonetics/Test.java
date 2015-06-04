package com.phonetics;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by daniel on 24/12/2014.
 */
public class Test {
    ArrayBlockingQueue<String> resultsQueue = new ArrayBlockingQueue(10);
    ArrayBlockingQueue<String> workQueue = new ArrayBlockingQueue(10);

    public static void main(String[] args) throws Exception{
//        Map<String, Double> map = new HashMap<>();
//        map.put("one", 1.0);
//        map.put("two", 2.0);
//        map.put("three", 3.0);
//
//        double sum = map.entrySet().stream().mapToDouble(Map.Entry::getValue).sum();
//
//        double sum = map.entrySet().stream().mapToDouble(e->e.getValue()).sum();

        Map<String, BigDecimal> map = new HashMap<>();
        map.put("one", BigDecimal.ONE);
        map.put("two", BigDecimal.valueOf(2));
        map.put("three", BigDecimal.valueOf(3));

        BigDecimal sum = map.entrySet().stream().map(e->e.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println(sum);
       // new Test().start();
    }

    private void start() throws Exception{
        new Thread(){
            public void run(){
                for(int i=0; i<100; i++){
                    try {
                        workQueue.put("" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread();
            executor.execute(worker);
        }


        while(true){
            String s = resultsQueue.take();
            System.out.println(s);
        }




    }

    private class WorkerThread implements Runnable {

        public WorkerThread() {

        }

        @Override
        public void run() {
            while(true) {
                try {
                    String work = workQueue.take();
                    resultsQueue.put(work + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
