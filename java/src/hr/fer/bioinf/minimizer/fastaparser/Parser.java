package hr.fer.bioinf.minimizer.fastaparser;

import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.io.*;

/**
 * Parser for FASTA file format. Requires sequence names and supports comments.
 * Files are parsed lazily, sequence by sequence.
 * Parser should be closed with the 'close' method after use.
 */
public class Parser {
    private static final int SB_INIT_CAPACITY = 1000;

    private final String filename;
    private BufferedReader reader;
    private int lastChar;

    /**
     * Constructs a Parser which will parse the file of the given file name. No parsing takes place until 'nextSequence'
     * is called.
     * @param filename Name of the file which will be parsed.
     */
    public Parser(String filename) {
        this.filename = filename;
    }

    /**
     * Parses one Sequence from the file. If it is called after the 'close' method, it will reopen the file and start
     * the parsing process from the beginning.
     * @return Parsed sequence.
     * @throws IOException If the file could not have been read for any reason.
     * @throws IllegalStateException If the end of file has already been reached.
     */
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

    /**
     * Checks whether the end of file has been reached.
     * @return true if the end of file has not yet been reached, false otherwise
     */
    public boolean hasNext() {
        return lastChar != -1;
    }

    /**
     * Closes the file which was parsed.
     * @throws IOException If the underlying stream could not be closed.
     */
    public void close() throws IOException {
        reader.close();
        reader = null;
    }

    private void init() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(filename));
    }
}
