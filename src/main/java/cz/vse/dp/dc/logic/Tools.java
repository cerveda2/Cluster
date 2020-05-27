/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author David Červenka
 */
public class Tools {


    public static void saveToFile(String result, String fileType, int count) throws IOException {

        File homeDirectory = new File(System.getProperty("user.home") + "/Desktop/Soubory");

        File file = new File(homeDirectory + "/soubor" + count + "." + fileType);
        file.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(result);
        } catch (IOException e) {
            System.out.print("Exception: " + e);
        }


    }

}
