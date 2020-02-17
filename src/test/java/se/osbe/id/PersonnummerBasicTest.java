package se.osbe.id;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.osbe.id.enums.GenderType;
import se.osbe.id.enums.PnrZodiacType;
import se.osbe.id.exception.PersonnummerException;
import se.osbe.id.helper.PersonnummerBuilder;
import se.osbe.id.helper.PersonnummerHelper;
import se.osbe.id.pnr.Personnummer;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PersonnummerBasicTest {

    private List<String> _pnrOKList;
    private List<String> _pnrNOKList;
    final int FORMER_ERA = (LocalDate.now().getCenturyOfEra() - 1);

    public PersonnummerBasicTest() {
        _pnrOKList = new LinkedList<String>();
    }

    @Before
    public void before() {
        // OK
        _pnrOKList = new LinkedList<String>();
        _pnrOKList.add("1212121212");
        _pnrOKList.add("121212-1212");
        _pnrOKList.add("121212+1212");
        _pnrOKList.add("191212121212");
        _pnrOKList.add("19121212-1212");
        _pnrOKList.add("4604300014");
        _pnrOKList.add("460430-0014");
        _pnrOKList.add("460430+0014");
        _pnrOKList.add("194604300014");
        _pnrOKList.add("19460430-0014");
        _pnrOKList.add("18180112-0302");
        _pnrOKList.add("040229-0308"); // Leap year

        // NOK
        _pnrNOKList = new LinkedList<String>();
        _pnrNOKList.add("1801170000"); // last4 digits all zero
        _pnrNOKList.add("180117-0000"); // last4 digits all zero
        _pnrNOKList.add("180117+0000"); // last4 digits all zero
        _pnrNOKList.add("18180117-0000"); // last4 digits all zero
        _pnrNOKList.add("191801170000"); // last4 digits all zero
        _pnrNOKList.add("19180117-0000"); // last4 digits all zero
        _pnrNOKList.add("201801170000"); // last4 digits all zero
        _pnrNOKList.add("20180117-0000"); // last4 digits all zero

        _pnrNOKList.add("abc"); // no digits at all
        _pnrNOKList.add("123"); // too short
        _pnrNOKList.add("x1212121212"); // char
        _pnrNOKList.add("161212121212"); // Org. nr (not Pnr)
        _pnrNOKList.add("171212121212"); // Era is wrong
        _pnrNOKList.add("211212121212"); // Future
        _pnrNOKList.add("050229-0307"); // Not a Leap year
        _pnrNOKList.add("1212121213"); // Wrong checksum
        _pnrNOKList.add("121212-1213"); // Wrong checksum
        _pnrNOKList.add("121212+1213"); // Wrong checksum
        _pnrNOKList.add("191212121213"); // Wrong checksum
        _pnrNOKList.add("19121212-1213"); // Wrong checksum
        _pnrNOKList.add("201212121213"); // Wrong checksum
        _pnrNOKList.add("20121212-1213"); // Wrong checksum
        _pnrNOKList.add("21991212-1218"); // In the future
        _pnrNOKList.add("219912121218"); // In the future
        _pnrNOKList.add("21991212-1219"); // In the future, wrong checksum
    }

    /*
     * Basic parse validations
     */

    @Test
    public void testSsnParseOK() {
        // OK test
        for (String okPnr : _pnrOKList) {
            Personnummer pnr = Personnummer.parse(okPnr).get();
            Assert.assertNotNull("Ssn " + okPnr + " is null after parse ", pnr);
            int len = okPnr.length();
            Assert.assertEquals("Calculated checksum is wrong compared to original", okPnr.substring(len - 1),
                    ("" + pnr.getChecksum()));
        }
    }

    @Test
    public void testPnrParsePlusHundredYearsOldOK() {
        Assert.assertTrue(Personnummer.parse("010101+0106").get().isHundredYears());
        Assert.assertTrue(Personnummer.parse("010101+0106").get().getBirthDate().isBefore(LocalDate.now().minusYears(100)));
    }

    @Test
    public void testPnrParseEmptyNOK() {
        // NOK test
        Assert.assertFalse(Personnummer.parse(null).isPresent());
        Assert.assertFalse(Personnummer.parse(null, false).isPresent());
        Assert.assertFalse(Personnummer.parse(null, true).isPresent());
        Assert.assertFalse(Personnummer.parse("").isPresent());
        Assert.assertFalse(Personnummer.parse("", false).isPresent());
        Assert.assertFalse(Personnummer.parse("", true).isPresent());
    }

    @Test
    public void testPnrWithEraAndPlusNOK() {
        // illegal combination, can not have both era AND a plus sign at the
        // same time
        Assert.assertFalse(Personnummer.parse("19010101+0106").isPresent());
    }

    @Test
    public void testPnrNoChecksumAndNotForgivingNOK() {
        Assert.assertFalse(Personnummer.parse("010101010", false).isPresent());
        Assert.assertFalse(Personnummer.parse("19010101010", false).isPresent());
    }

    @Test
    public void testPnrMonthNOK() {
        // Month = 20
        Assert.assertFalse(Personnummer.parse("012003-0012").isPresent());
        Assert.assertFalse(Personnummer.parse("012003-0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("012003-0012", true).isPresent());

        Assert.assertFalse(Personnummer.parse("012003+0012").isPresent());
        Assert.assertFalse(Personnummer.parse("012003+0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("012003+0012", true).isPresent());

        Assert.assertFalse(Personnummer.parse("19012003-0012").isPresent());
        Assert.assertFalse(Personnummer.parse("19012003-0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("19012003-0012", true).isPresent());

        Assert.assertFalse(Personnummer.parse("20012003-0012").isPresent());
        Assert.assertFalse(Personnummer.parse("20012003-0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("20012003-0012", true).isPresent());

        // Month = 99
        Assert.assertFalse(Personnummer.parse("019903-0012").isPresent());
        Assert.assertFalse(Personnummer.parse("019903-0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("019903-0012", true).isPresent());

        Assert.assertFalse(Personnummer.parse("019903+0012").isPresent());
        Assert.assertFalse(Personnummer.parse("019903+0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("019903+0012", true).isPresent());

        Assert.assertFalse(Personnummer.parse("19019903-0012").isPresent());
        Assert.assertFalse(Personnummer.parse("19019903-0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("19019903-0012", true).isPresent());

        Assert.assertFalse(Personnummer.parse("20019903-0012").isPresent());
        Assert.assertFalse(Personnummer.parse("20019903-0012", false).isPresent());
        Assert.assertFalse(Personnummer.parse("20019903-0012", true).isPresent());
    }

    @Test
    public void testPnrParseNOK() {
        _pnrNOKList.stream()
                .forEach(p -> Assert.assertFalse("False candidates for Pnr should return null when trying to parse",
                        Personnummer.parse(p).isPresent()));
    }

    /*
     * Equals tests
     */

    @Test
    public void testSsn10Equals() {
        for (String okPnr : _pnrOKList) {
            Personnummer pnr = Personnummer.parse(okPnr).get();
            if (pnr.isHundredYears()) {
                /*
                 * Skipping equals check when '+' sign is present, and we
                 * extract 10 digits, we loose the '+' sign and born date more
                 * than 100 years back is no longer detectable by any parse
                 * method.
                 */
                continue;
            }
            Personnummer pnr10 = Personnummer.parse(pnr.toString10()).get();
            Assert.assertEquals(pnr, pnr10);
        }
    }

    @Test
    public void testSsn11Equals() {
        _pnrOKList.stream().forEach(p -> {
            Assert.assertEquals(Personnummer.parse(p).get().toString(), Personnummer.parse(p).get().toString11());
        });

        _pnrOKList.stream().forEach(p -> {
            Personnummer pnr = Personnummer.parse(p).get();
            Personnummer pnr11 = Personnummer.parse(pnr.toString11()).get();
            if (p.length() == 13 && Integer.parseInt(p.substring(0, 2)) < FORMER_ERA) {
                Assert.assertNotSame(pnr, pnr11);
            }
        });
    }

    @Test
    public void testSsn12Equals() {
        _pnrOKList.stream().forEach(p -> {
            Assert.assertEquals(Personnummer.parse(p).get(),
                    Personnummer.parse(Personnummer.parse(p).get().toString12()).get());
        });
    }

    @Test
    public void testSsn13Equals() {
        _pnrOKList.stream().forEach(p -> {
            Assert.assertEquals(Personnummer.parse(p).get(),
                    Personnummer.parse(Personnummer.parse(p).get().toString13()).get());
        });
    }

    /*
     * Test Helper class features
     */

    @Test
    public void testZodiacSign() {
        String[][] pnrArr = {
                // start // middle // end
                {"CAPRICORNUS", "120101-0000", "120115-0000", "120119-0000"},
                {"AQUARIUS", "120120-0000", "120215-0000", "120218-0000"},
                {"PISCES", "120219-0000", "120315-0000", "120320-0000"},

                {"ARIES", "120321-0000", "120415-0000", "120419-0000"},
                {"TAURUS", "120420-0000", "120515-0000", "120521-0000"},
                {"GEMINI", "120522-0000", "120615-0000", "120621-0000"},

                {"CANCER", "120622-0000", "120715-0000", "120722-0000"},
                {"LEO", "120723-0000", "120815-0000", "120822-0000"},
                {"VIRGO", "120823-0000", "120915-0000", "120922-0000"},

                {"LIBRA", "120923-0000", "121015-0000", "121022-0000"},
                {"SCORPIO", "121023-0000", "121115-0000", "121121-0000"},
                {"SAGITTARIUS", "121222-0000", "121225-0000", "121231-0000"}};
        for (int x = 0; x < pnrArr.length; x++) {
            PnrZodiacType zodiacSign = PnrZodiacType.valueOf(PnrZodiacType.class, pnrArr[x][0]);
            for (int y = 1; y < 3; y++) {
                Optional<Personnummer> tempPnr = Personnummer.parse(pnrArr[x][y], true);
                if (tempPnr.isPresent()) {
                    Assert.assertEquals(zodiacSign, PersonnummerHelper.getZodiacSign(Personnummer.parse(pnrArr[x][y], true).get()).get());
                }
            }
        }
    }

    @Test
    public void testGenderOK() {
        for (String okPnr : _pnrOKList) {
            Personnummer pnr = Personnummer.parse(okPnr).get();
            boolean isEven = (Integer.parseInt(pnr.getLastFour().substring(2, 3)) % 2 == 0);
            Assert.assertEquals((isEven ? "woman" : "man"), pnr.getGender("woman", "man"));
            Assert.assertTrue(pnr.getGender() == GenderType.MAN || pnr.getGender() == GenderType.WOMAN);
        }
    }

    @Test
    public void testPnrToString() {
        for (String okPnr : _pnrOKList) {
            Personnummer pnr = Personnummer.parse(okPnr).get();
            Assert.assertTrue(pnr.getAgeNow() >= 0);
            Assert.assertEquals(11, pnr.toString().length());
            Assert.assertEquals(10, pnr.toString10().length());
            Assert.assertEquals(11, pnr.toString11().length());
            Assert.assertEquals(12, pnr.toString12().length());
            Assert.assertEquals(13, pnr.toString13().length());

            Assert.assertTrue(pnr.toString11().contains("-") || pnr.toString11().contains("+"));
        }
    }

    @Test
    public void testSsnLast4Digits() {
        for (String okPnr : _pnrOKList) {
            Personnummer pnr = Personnummer.parse(okPnr).get();
            Assert.assertEquals(4, pnr.getLastFour().length());
            int last4 = Integer.parseInt(pnr.getLastFour());
            Assert.assertTrue(last4 >= 0 && last4 <= 9999);
        }
    }

    @Test
    public void testSsnAge() {
        _pnrOKList.stream()
                .forEach(p -> Assert.assertTrue(Personnummer.parse(p).get().getAgeNow() >= 0));
    }

    @Test
    public void testGetDaysFromBirth() {
        try {
            int rand = PersonnummerHelper.dice(1, 130);
            LocalDate earlier = LocalDate.now().minusDays(rand);
            new PersonnummerBuilder().setStartAndEndDate(earlier, earlier).build().forEach(p -> Assert.assertEquals(rand, p.getDaysFromBirth()));
        } catch (PersonnummerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSsnPlusSign() {
        Personnummer pnr1 = Personnummer.parse("121212+1212").get();
        Assert.assertTrue(pnr1.isHundredYears());
        Assert.assertTrue(pnr1.toString().contains("+"));
        Assert.assertTrue(pnr1.toString11().contains("+"));

        Personnummer pnr2 = Personnummer.parse("19121212-1212").get();
        Assert.assertTrue(pnr2.isHundredYears());
        Assert.assertTrue(pnr2.toString().contains("+"));
        Assert.assertTrue(pnr2.toString11().contains("+"));

        Personnummer pnr3 = Personnummer.parse("191212121212").get();
        Assert.assertTrue(pnr3.isHundredYears());
        Assert.assertTrue(pnr3.toString().contains("+"));
        Assert.assertTrue(pnr3.toString11().contains("+"));
    }

    @Test
    public void testSsnBirthDate() {
        _pnrOKList.forEach(p -> Assert.assertNotNull(Personnummer.parse(p).get().getBirthDate()));
    }
}