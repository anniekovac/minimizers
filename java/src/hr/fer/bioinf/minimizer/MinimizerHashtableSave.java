package hr.fer.bioinf.minimizer;

import hr.fer.bioinf.minimizer.fastaparser.Parser;
import hr.fer.bioinf.minimizer.hash.MinimizerHashTable;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo program which extracts minimizers from fasta file sequences, builds a hash table of the minimizers
 * and saves the hash table to a file.<br/>
 * Usage is: java MinimizerHashtableSave &lt;w&gt; &lt;k&gt; &lt;path-to-sequences&gt;
 * &lt;path-to-hashtable&gt; <br/>
 * Where &lt;w&gt; is the window size and &lt;k&gt; is the minimizer size.
 */
public class MinimizerHashtableSave {
    private final static String USAGE = "USAGE: java MinimizerHashtableSave <w> <k> <path-of-sequences> <path-to-hashtable>";

    /**
     * Reads a file and adds all sequences found in the file to a list.
     * @param sequences List of sequences where the new sequences will be added.
     * @param file File to read for sequences.
     * @return List of sequences where the new sequences were added.
     */
    private static List<Sequence> addSequencesFromFile(List<Sequence> sequences, Path file) {
        Parser parser = new Parser(file.toString());

        try {
            while (parser.hasNext()) {
                sequences.add(parser.nextSequence());
            }
            parser.close();

        } catch (IOException ex) {
            System.out.println("Error when working with file: " + file.toString());
        }

        return sequences;
    }

    /**
     * Extracts all sequences from a group of files and returns them as a list.
     * @param files Group of filepath to the files. If a path to a directory is given, this method will recurse down
     *              file hierarchy and extract sequences from all files with a .fasta, .fna, .ffn, .faa or .frn
     *              extension.
     * @return List of sequences.
     */
    public static List<Sequence> getSequencesFromFile(String[] files) {
        List<Sequence> sequences = new ArrayList<>();

        FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String filename = file.toString();

                    if (filename.endsWith("fasta")
                            || filename.endsWith("fna")
                            || filename.endsWith("ffn")
                            || filename.endsWith("faa")
                            || filename.endsWith("frn")) {

                        addSequencesFromFile(sequences, file);
                    }

                    return FileVisitResult.CONTINUE;
                }
            };

        for (String fileStr : files) {
            File file = new File(fileStr);
            if (! file.exists()) {
                System.out.println("File " + fileStr + " does not exist");
                continue;
            }

            if (file.isFile()) {
                addSequencesFromFile(sequences, file.toPath());

            } else if (file.isDirectory()) {
                try {
                    Files.walkFileTree(file.toPath(), fileVisitor);
                } catch (IOException e) {
                    System.out.println("Error when working with directory: " + fileStr);
                }
            }
        }

        return sequences;
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println(USAGE);
            return;
        }

        int w = 0;
        int k = 0;
        String sequencesPath = args[2];
        String hashTableFilename = args[3];

        try {
            w = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);

        } catch (NumberFormatException ex) {
            System.out.println(USAGE);
            return;
        }

        List<Sequence> sequences = getSequencesFromFile(new String[] {sequencesPath});
        MinimizerHashTable hashTable = new MinimizerHashTable(sequences, w, k);
        try {
            hashTable.saveToFile(new File(hashTableFilename));
        } catch (IOException e) {
            System.out.println("Could not create hash table in file " + hashTableFilename);
        }
    }
}
