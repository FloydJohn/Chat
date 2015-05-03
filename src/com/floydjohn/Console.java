package com.floydjohn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class Console {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_CLS = "\u001b[2J";
    public static final String ANSI_HOME = "\u001b[H";
    private static boolean isLog = false;

    public static String getFileName() {
        if(isLog) return fileName;
        else return "No log";
    }

    private static String fileName = "log2.txt";
    private static String OS;

    public static void clearScreen(){
        detectOS();
        if(!OS.startsWith("Windows")) System.out.print(ANSI_CLS+ANSI_HOME);
    }

    public static void initLog(){
        isLog = true;
        addToLog("*************SERVER LOG******************");
    }

    public static void detectOS(){
        if(OS==null) OS = System.getProperty("os.name");
    }



    public static void toggleLog(boolean in){
        isLog=in;
    }

    private static void println(String input, String color) {
        String date = new Date().toString();
        detectOS();
        if(OS.startsWith("Windows")){ //Windows non supporta i colori al terminale, linux si
            System.out.print("["+date+"] ");
            System.out.println(input);
        }
        else {
            print("["+date+"] ",ANSI_PURPLE);
            System.out.println(color + input + ANSI_RESET);
        }
        if(isLog) addToLog("["+date+"] "+input);
        date = null;
    }

    private static void addToLog(String input) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)))){
                writer.println(input);
            } catch (Exception e) {
                printlnPure("Error using file : " + e, ANSI_RED);
            }
    }

    public static void printlnPure(String input, String color) {
        println(deleteEscapes(input), color);
    }

    private static void print(String input, String color){
        System.out.print(color + input + ANSI_RESET);
    }

    private static void printPure(String input, String color){
        input = deleteEscapes(input);
        print(input,color);
    }

    private static String deleteEscapes(String input) {
        String out = input.replaceAll("\n","");
        out = out.replaceAll("\r", "");
        return out;
    }

    public static void help() {
        System.out.println("Chat server created in 2014 by Alessandro Racheli");
        System.out.println("Usage : Chat.jar [port] [log_state(true/false)]");
        System.exit(0);
    }
}
