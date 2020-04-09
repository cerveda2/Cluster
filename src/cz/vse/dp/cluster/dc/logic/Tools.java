/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.cluster.dc.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author David Červenka
 */
public class Tools {
    
    
    public static void saveToFile(String result, String fileType, int count) throws FileNotFoundException, IOException {
        
        File homeDirectory = new File(System.getProperty("user.home") + "/Desktop/Soubory");
        int temp = 1;
        
        File file = new File(homeDirectory + "/soubor" + count + "." + fileType);
        file.getParentFile().mkdirs();

            try (FileWriter fw = new FileWriter(file);) {
                fw.write(result);
            } catch (IOException e) {
                System.out.print("Exception: " + e);
            }
            temp++;

           /* PrintStream fileStream = new PrintStream(new File(homeDirectory + "/file.txt"));
            fileStream.println(result);
            
            try (PrintWriter out = new PrintWriter(homeDirectory + "/file.txt")) {
            out.println(result);
            }*/
        
        
    }
    
}
