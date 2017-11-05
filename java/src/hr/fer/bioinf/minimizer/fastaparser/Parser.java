package hr.fer.bioinf.minimizer.fastaparser;

import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.io.*;

public class Parser {
    private static final int SB_INIT_CAPACITY = 1000;

    private final String filename;
    private BufferedReader reader;
    private int lastChar;

    public Parser(String filename) {
        this.filename = filename;
    }

    public Sequence nextSequence() throws IOException {
        if (reader == null) {
            init();
        }

        if (! hasNext()) {
            throw new IllegalStateException("End of stream has been reached");
        }

        String name = "";
        if (lastChar != '>') {
            lastChar = reader.read();
        }

        if (lastChar == '>') {
            name = reader.readLine().trim();
        }

        StringBuilder stringBuilder = new StringBuilder(SB_INIT_CAPACITY);

        lastChar = reader.read();
        while (lastChar != '>' && lastChar != -1) {
            if (lastChar == ';') {
                reader.readLine();
                lastChar = reader.read();
            }

            if (! Character.isWhitespace(lastChar)) {
                stringBuilder.append((char) lastChar);
            }

            lastChar = reader.read();
        }

        return new Sequence(name, stringBuilder.toString());
    }

    public boolean hasNext() {
        return lastChar != -1;
    }

    public void close() throws IOException {
        reader.close();
        reader = null;
    }

    private void init() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(filename));
    }
}
