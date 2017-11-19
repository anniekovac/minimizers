package hr.fer.bioinf.minimizer.hash;

import hr.fer.bioinf.minimizer.extractor.MinimizerExtractor;
import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.util.*;

public class MinimizerHashTable {
    private static final int ESTIMATED_SEQUENCE_SIZE = 10000;

    Map<String, Minimizer> minimizerMap;

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
                minimizerMap.put(minimizer.getString(), minimizer);
            }
        }
    }

    public Minimizer get(String minimizerStr) {
        return minimizerMap.get(minimizerStr);
    }
}
