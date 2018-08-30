/* This file tests to see if an Assignment submission zip file contains
the correct files.
It does not have any tests.
Using this file does not guarantee any marks, rather the
file is just provided in case it is helpful.*/


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class CheckZipAssignment1 {

        /* main requires a single argument - a zip file to check.
        * You can run this from the command line
        * java CheckZipAssignment1 myzipfile.zip
        *
        * or through IDEA
        * Make a new project and add this file.
        * Go to Run > Edit Configurations
        * Type the name of the submisison zipfile into "Porgram arguments".
        */

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Please provide zip file as argument.");
            return;
        }

        // the zip file to check is the first argument
        String fileZip = args[0];

        // expected filenames, and whether they have been seen (true / false)
        Map<String, Boolean> expectedFilenames = new HashMap<>();
        expectedFilenames.put("src/Block.java", false);
        expectedFilenames.put("src/BlockWorldException.java", false);
        expectedFilenames.put("src/Builder.java", false);
        expectedFilenames.put("src/GrassBlock.java", false);
        expectedFilenames.put("src/GroundBlock.java", false);
        expectedFilenames.put("src/InvalidBlockException.java", false);
        expectedFilenames.put("src/NoExitException.java", false);
        expectedFilenames.put("src/SoilBlock.java", false);
        expectedFilenames.put("src/StoneBlock.java", false);
        expectedFilenames.put("src/Tile.java", false);
        expectedFilenames.put("src/TooHighException.java", false);
        expectedFilenames.put("src/TooLowException.java", false);
        expectedFilenames.put("src/WoodBlock.java", false);
        expectedFilenames.put("test/GrassBlockTest.java", false);
        expectedFilenames.put("test/TileTest.java", false);

        // optional filenames, and whether they have been seen (true / false)
        Map<String, Boolean> optionalFilenames = new HashMap<>();
        optionalFilenames.put("src/", false);
        optionalFilenames.put("test/", false);

        // store accepted filenames and extra filenames that are seen
        List<String> acceptedFilenames = new ArrayList<>();
        List<String> extraFilenames = new ArrayList<>();

        // open the zip file
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));

        // iterate through all zip file entries
        ZipEntry zipEntry = zis.getNextEntry();

        while(zipEntry != null){
            String filename = zipEntry.getName();


            if (expectedFilenames.containsKey(filename)) {

                    expectedFilenames.put(filename, true);

                // add expected filenames to acceptedFilenames
                    acceptedFilenames.add(filename);

            } else if (optionalFilenames.containsKey(filename)) {
                    // don't worry about optional filenames
                    optionalFilenames.put(filename, true);

                acceptedFilenames.add(filename);
            } else {
                    // add unexpected filenames to extraFilenames
                    extraFilenames.add(filename);
            }

            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        // print lists of accepted files, spurious files (extraFilenames)
        // and missing files (values marked false in expectedFilenames)

        System.out.println("Accepted files:");
        for (String filename : acceptedFilenames) {
                System.out.println("\t" + filename);
        }


        boolean failed = false;
        if (extraFilenames.size() > 0) {
                // if there is at least one extra file
                failed = true;
                System.out.println("\nSpurious files:");
                for (String filename : extraFilenames) {
                        System.out.println("\t" + filename);
                }
        }

        if (expectedFilenames.containsValue(false)) {
                // if there is at least one missing file
                failed = true;
                System.out.println("\nMissing files:");
                for (Map.Entry<String, Boolean> entry : expectedFilenames.entrySet()) {
                        if (entry.getValue().booleanValue() == false) {
                                System.out.println("\t" + entry.getKey());
                        }
                }
        }

        if (failed) {
            System.out.println("Spurious or missing files!");
            System.exit(1);
        }

    }
}
