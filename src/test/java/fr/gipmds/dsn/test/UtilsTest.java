package fr.gipmds.dsn.test;

import fr.gipmds.dsn.test.resources.TestData;
import fr.gipmds.dsn.utils.Base64Utils;
import fr.gipmds.dsn.utils.DateUtils;
import fr.gipmds.dsn.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Utils Test")
class UtilsTest {

    @Test
    @DisplayName("Base 64 Test")
    void base64Test() {
        // Given
        String original = "TOTO";
        // When
        String encoded = Base64Utils.encode(original);
        String actual = Base64Utils.decode(encoded);
        // Then
        assertNotNull(actual);
        assertEquals("TOTO", actual);
    }

    @Test
    @DisplayName("Date Test")
    void dateTest() {
        // Given
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.JUNE, 22, 1, 20, 0);
        // When
        String actual = DateUtils.format(cal.getTime());
        // Then
        assertNotNull(actual);
        assertEquals("20130622012000", actual);
    }

    @Test
    @DisplayName("Token Parser Test")
    void tokenParserTest() {
        // Given
        String jeton = TestData.declarantInscrit.getFauxJeton();
        jeton = Base64Utils.decode(jeton);
        // When
        Map<String, String> map = SecurityUtils.parseToken(jeton);
        // Then
        assertNotNull(map.get("siret"));
        assertNotNull(map.get("nom"));
        assertNotNull(map.get("prenom"));
    }

    @Test
    void shortDateTest() {
        Assertions.assertThrows(ParseException.class, () -> {
            final Date date = DateUtils.parseShort("2013010");
            System.out.println("date = " + date);
        });


    }
}
