/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author David Červenka
 */
public class Tools {


    /**
     * Method to write result into file
     *
     * @param result   text to be written into file
     * @param fileType type of output file
     * @param count    number of iteration
     * @throws IOException handling IOException
     */
    public static void saveToFile(String result, String fileType, int count) throws IOException {

        File homeDirectory = new File(System.getProperty("user.home") + "/Desktop/Soubory");

        File file = new File(homeDirectory + "/soubor" + count + "." + fileType);
        file.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(result);
        } catch (IOException e) {
            System.out.print("Exception: " + e);
            throw new IOException("Unable to save into file", e);
        }

    }

}
