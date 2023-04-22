package org.example.library.models.isbn;

import org.immutables.value.Value;

@Value.Immutable(builder = false)
public abstract class ISBN13 implements ISBN {

    public static final int ISBN13_LEN = 13;

    @Override
    @Value.Parameter
    public abstract String value();

    @Value.Check
    protected void validate() {
        validateLength();
        validateChecksum();
    }

    private void validateLength() {
        final int length = value().length();
        if (length != ISBN13_LEN) {
            throw new IllegalArgumentException(
                    String.format("Invalid ISBN length. Value=%s ExpectedLength=%d, ActualLength=%d",
                            value(), ISBN13_LEN, length));
        }
    }

    private void validateChecksum() {
        final int checksumValue = checksum(value());
        if (checksumValue % 10 != 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid ISBN: Value=%s, Checksum=%d", value(), checksumValue));
        }
    }

    public static int checksum(final String isbnStr) {
        int checksum = 0;
        for (int i = 1; i < isbnStr.length(); i += 2) {
            checksum += Character.getNumericValue(isbnStr.charAt(i));
        }
        checksum *= 3;
        for (int i = 0; i < isbnStr.length(); i += 2) {
            checksum += Character.getNumericValue(isbnStr.charAt(i));
        }
        return checksum;
    }
}
