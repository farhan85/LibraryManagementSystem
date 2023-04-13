package org.example.library.models;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class EmailTest {

    @Test
    public void test() {
        final Email email = ImmutableEmail.of("me@test.com");
        assertEquals(email.value(), "me@test.com");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test2() {
        ImmutableEmail.of("me@test.com..");
    }
}
