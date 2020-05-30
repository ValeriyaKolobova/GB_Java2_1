package geekbrains.java_level2;

import java.util.Arrays;

public class Main {
    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;

    public static void method1(){
        float[] arr = new float[SIZE];
        Arrays.fill(arr, 1);
        long a = System.currentTimeMillis();
        for (int i = 0; i < arr.length ; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Time for method 1: " +(System.currentTimeMillis() - a));
    }

    public static void method2(){
        float[] arr = new float[SIZE];
        Arrays.fill(arr, 1);

        float[] arr1 = new float[HALF];
        float[] arr2 = new float[HALF];

        long b = System.currentTimeMillis();
        System.arraycopy(arr, 0, arr1, 0, HALF);
        System.arraycopy(arr, HALF, arr2, 0, HALF);

        Thread arr1Calc = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < arr1.length; i++) {
                    arr1[i] = (float)(arr1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        }) ;
        arr1Calc.start();

        Thread arr2Calc = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < arr2.length; i++) {
                    arr2[i] = (float)(arr2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        arr2Calc.start();

        try{
            arr1Calc.join();
            arr2Calc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.arraycopy(arr1, 0, arr, 0, HALF);
        System.arraycopy(arr2, 0, arr, HALF, HALF);
        System.out.println("Time for method 2: " + (System.currentTimeMillis() - b));
    }


    public static void main(String[] args) {
        method1();
        method2();
    }
}
