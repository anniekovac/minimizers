package hr.fer.bioinf.minimizer.hash;

import hr.fer.bioinf.minimizer.extractor.MinimizerExtractor;
import hr.fer.bioinf.minimizer.fastaparser.Parser;
import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class MinimizerHashTable {

    private Map<String, ArrayList<Minimizer>> minimizerMap;

    public MinimizerHashTable(Collection<Sequence> sequences, int w, int k) {
        if (sequences == null || sequences.isEmpty()) {
            throw new IllegalArgumentException("sequences must be a non-null, non-empty collection");
        }

        int sizeSum = sequences
                .stream()
                .mapToInt(seq -> seq.getString().length())
                .sum();

        minimizerMap = new HashMap<>(sizeSum * 2 / (w + 1));

        for (Sequence seq : sequences) {
            List<Minimizer> minimizers = MinimizerExtractor.extract(seq, w, k);

            for (Minimizer minimizer : minimizers) {
                String minimizerStr = minimizer.getString();

                if (! minimizerMap.containsKey(minimizerStr)) {
                    ArrayList<Minimizer> newList = new ArrayList<>();
                    newList.add(minimizer);
                    minimizerMap.put(minimizerStr, newList);

                } else {
                    minimizerMap.get(minimizerStr).add(minimizer);
                }
            }
        }
    }

    public List<Minimizer> get(String minimizerStr) {
        return minimizerMap.get(minimizerStr);
    }

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

    private static List<Sequence> getSequencesFromFile(String[] files) {
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
        List<Sequence> sequences = getSequencesFromFile(args);
        Scanner sc = new Scanner(System.in);

        System.out.print("Input window size: ");
        int w = sc.nextInt();

        System.out.print("Input minimizer size: ");
        int k = sc.nextInt();

        MinimizerHashTable hashTable = new MinimizerHashTable(sequences, w, k);

        while (true) {
            System.out.print("Find string: ");
            String queryStr = sc.next();

            if (queryStr.length() < k) {
                System.out.println("String must be longer than k");
                continue;
            }

            List<Minimizer> queryStrMinimizers = MinimizerExtractor.extract(new Sequence("", queryStr), w, k);

            //if (queryStr.equals("exit")) break;

            for (Minimizer m : queryStrMinimizers) {
                List<Minimizer> foundMinimizers = hashTable.get(m.getString());

                if (foundMinimizers == null) continue;

                for (Minimizer foundMinimizer : foundMinimizers) {
                    System.out.println("Found: " + foundMinimizer);
                }
            }

        }
    }
}
