package org.example.library.models.isbn;

import org.immutables.value.Value;

@Value.Immutable(builder = false)
public abstract class ISBN10 implements ISBN {

    public static final int ISBN10_LEN = 10;

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
        if (value().length() != ISBN10_LEN) {
            throw new IllegalArgumentException(
                    String.format("Invalid ISBN length. Value=%s ExpectedLength=%d, ActualLength=%d",
                            value(), ISBN10_LEN, length));
        }
    }

    private void validateChecksum() {
        final int checksumValue = checksum(value());
        if (checksumValue % 11 != 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid ISBN: Value=%s, Checksum=%d", value(), checksumValue));
        }
    }

    public static int checksum(final String isbnStr) {
        int checksum = 0;
        for (int i = 0; i < isbnStr.length() - 1; i++) {
            checksum += (i + 1)*Character.getNumericValue(isbnStr.charAt(i));
        }
        final char finalChar = isbnStr.charAt(isbnStr.length() - 1);
        final int finalValue = finalChar == 'X' ? 10 : Character.getNumericValue(finalChar);
        checksum += 10 * finalValue;
        return checksum;
    }
}
