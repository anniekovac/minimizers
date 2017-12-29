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
        try (BufferedReader reader = new BufferedReader(new FileReader(hashTableFile), 65536)) {
            minimizerMap = new HashMap<>(500000);
            Map<String, Sequence> sequenceMap = new HashMap<>();

            String key = null;
            String seqName = null;
            List<Minimizer> minimizerList = null;

            String line = reader.readLine();
            int counter = 0;
            while (line != null && ! line.isEmpty()) {

                switch (line.charAt(0)) {
                    case ':':
                        if (key != null) minimizerMap.put(key, minimizerList);
                        minimizerList = new ArrayList<>();

                        key = line.substring(1);
                        break;
                    case '>':
                        seqName = line.substring(1);
                        break;
                    case '#':
                        int pos = Integer.parseInt(line.substring(1));
                        Sequence seq = sequenceMap.get(seqName);
                        if (seq == null) {
                            seq = new Sequence(seqName, "");
                            sequenceMap.put(seqName, seq);
                        }

                        minimizerList.add(new Minimizer(key, seq, pos));
                        break;

                    default:
                        throw new IOException("Misformatted file");
                }

                line = reader.readLine();
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


}
