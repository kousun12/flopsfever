package com.illinois.ncsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import framework.FileIO;

public class Settings {
    public static boolean soundEnabled = true;

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    files.readFile(".ffever")));
            soundEnabled = Boolean.parseBoolean(in.readLine());

        } catch (IOException e) {
            // :( It's ok we have defaults
        } 
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile(".ffever")));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");


        } catch (IOException e) {
        }
    }


}