package org.example.library.models;

import org.testng.annotations.Test;

import static org.example.library.models.Genre.CRIME;
import static org.example.library.models.Genre.FANTASY;
import static org.example.library.models.Genre.MYSTERY;
import static org.example.library.models.Genre.ROMANCE;
import static org.example.library.models.Genre.SCI_FI;
import static org.testng.Assert.assertEquals;

public class GenreTest {

    @Test
    public void GIVEN_Genre_enum_WHEN_calling_getDisplayName_THEN_return_expected_string() {
        assertEquals(CRIME.getDisplayName(), "Crime");
        assertEquals(FANTASY.getDisplayName(), "Fantasy");
        assertEquals(MYSTERY.getDisplayName(), "Mystery");
        assertEquals(ROMANCE.getDisplayName(), "Romance");
        assertEquals(SCI_FI.getDisplayName(), "Sci-Fi");
    }

    @Test
    public void GIVEN_Genre_enum_WHEN_calling_toString_THEN_return_expected_string() {
        assertEquals(CRIME.toString(), "CRIME");
        assertEquals(FANTASY.toString(), "FANTASY");
        assertEquals(MYSTERY.toString(), "MYSTERY");
        assertEquals(ROMANCE.toString(), "ROMANCE");
        assertEquals(SCI_FI.toString(), "SCI_FI");
    }

    @Test
    public void GIVEN_valid_string_WHEN_calling_fromValue_THEN_return_correct_Genre() {
        assertEquals(Genre.fromString("crime"), CRIME);
        assertEquals(Genre.fromString("fantasy"), FANTASY);
        assertEquals(Genre.fromString("mystery"), MYSTERY);
        assertEquals(Genre.fromString("romance"), ROMANCE);
        assertEquals(Genre.fromString("sci_fi"), SCI_FI);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void GIVEN_invalid_string_WHEN_calling_fromValue_THEN_return_IllegalArgumentException() {
        Genre.fromString("invalid-string");
    }
}
