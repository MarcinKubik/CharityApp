package pl.coderslab.charity;

import org.junit.Test;
import pl.coderslab.charity.config.Password;
import static org.junit.Assert.*;

public class CharityApplicationTests {

    @Test
    public void testPassword(){
        Password password = new Password();

        assertEquals("Hasło musi zawierać co najmniej 8 znaków", password.testPassword("abc", "vvv"));
        assertEquals("Hasło musi zawierać co najmniej jedną wielką literę", password.testPassword("abcabcab", "vvv"));
        assertEquals("Hasło musi zawierać co najmniej jedną małą literę", password.testPassword("ABCABCAB", "vvv"));
        assertEquals("Hasło musi zawierać co najmniej jedną cyfrę", password.testPassword("abcabcaN", "vvv"));
        assertEquals("Hasło musi zawierać co najmniej jeden znak specjalny", password.testPassword("abcabc1N", "vvv"));
        assertEquals("Hasła nie są takie same", password.testPassword("abcab/1N", "vvv"));
        assertEquals("Hasło OK", password.testPassword("abcab/1N", "abcab/1N"));

    }

}
