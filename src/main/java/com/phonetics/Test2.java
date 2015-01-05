package com.phonetics;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by daniel on 01/01/2015.
 */
public class Test2 {
    public static void main(String[] args) {
        final AtomicBoolean suspended = new AtomicBoolean(false);

        new Thread() {
            public void run() {
                while (true)
                {
                    Scanner sc = new Scanner(System.in);
                    boolean b = sc.nextBoolean();
                    suspended.set(b);
                }
            }
        }.start();


        while(true){
            if(!suspended.get()){
                System.out.println("working");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
