package se.osbe.id.enums;

public enum IDType {
    PERSONNUMMER(
            "personnummer",
            "^(?<era>18|19|20)?(?<year>\\d{2})(?<month>\\d{2})(?<day>\\d{2})(?<sign>[\\-+])?(?<last3>\\d{3})(?<checksum>\\d{1})?$"),

    ORGANISATIONSNUMMER(
            "organisationsnummer",
            "^(?<era>18|19|20)?(?<year>\\d{2})(?<month>\\d{2})(?<day>\\d{2})(?<sign>[\\-+])?(?<last3>\\d{3})(?<checksum>\\d{1})?$"),

    SAMORDNINGSNUMMER(
            "samordningsnummer",
            "^(?<era>18|19|20)?(?<year>\\d{2})(?<month>\\d{2})(?<day>\\d{2})(?<sign>[\\-+])?(?<last3>\\d{3})(?<checksum>\\d{1})?$"),

//	ENSKILD(
//			"enskild näringsidkare",
//			"^(?<era>18|19|20)?(?<year>\\d{2})(?<month>\\d{2})(?<day>\\d{2})(?<sign>[\\-+])?(?<last3>\\d{3})(?<checksum>\\d{1})?$")
    ;

    private final String _description;
    private final String _pattern;

    private IDType(final String description, String pattern) {
        _description = description;
        _pattern = pattern;
    }

    public String getDescription() {
        return _description;
    }

    public String getPattern() {
        return _pattern;
    }
}