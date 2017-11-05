package hr.fer.bioinf.minimizer.sequence;

public class Sequence {

    private final String name;
    private final String string;

    public Sequence(String name, String string) {
        this.name = name;
        this.string = string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sequence sequence = (Sequence) o;

        if (!name.equals(sequence.name)) return false;
        return string.equals(sequence.string);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + string.hashCode();
        return result;
    }
}
