package hr.fer.bioinf.minimizer.hash;

import hr.fer.bioinf.minimizer.extractor.MinimizerExtractor;
import hr.fer.bioinf.minimizer.fastaparser.Parser;
import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class MinimizerHashTable {

    private Map<String, List<Minimizer>> minimizerMap;

    public MinimizerHashTable(File hashTableFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(hashTableFile))) {
            minimizerMap = new HashMap<>();

            String key = null;
            String seqName = null;
            List<Minimizer> minimizerList = null;

            String line = reader.readLine();
            while (line != null && ! line.isEmpty()) {

                switch (line.charAt(0)) {
                    case ':':
                        if (key != null) minimizerMap.put(key, minimizerList);

                        key = line.substring(1);
                        break;
                    case '>':
                        seqName = line.substring(1);
                        minimizerList = new ArrayList<>();
                        break;
                    case '#':
                        int pos = Integer.parseInt(line.substring(1));
                        minimizerList.add(new Minimizer(key, new Sequence(seqName, ""), pos));
                        break;

                    default:
                        throw new IOException("Misformatted file");
                }
            }

            if (key != null) minimizerMap.put(key, minimizerList);
        }
    }

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

    public void saveToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (Map.Entry<String, List<Minimizer>> entry : minimizerMap.entrySet()) {
                writer.write(":");
                writer.write(entry.getKey());
                writer.newLine();

                for (Minimizer minimizer : entry.getValue()) {
                    writer.write(">");
                    writer.write(minimizer.getSeq().getName());
                    writer.newLine();

                    writer.write("#");
                    writer.write(Integer.toString(minimizer.getPos()));
                    writer.newLine();
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
}
