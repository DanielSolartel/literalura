package com.alura.literalura.model;

public enum Tongue {
    ES("es"),
    EN("en"),
    FR("fr"),
    IT("it"),
    PT("pt");

    private String dialect;

    Tongue(String dialect) {
        this.dialect = dialect;
    }

    public static Tongue fromString(String text) {
        for (Tongue tongue : Tongue.values()) {
            if (tongue.dialect.equalsIgnoreCase(text)) {
                return tongue;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + text);
    }

    public String getDialect() {
        return this.dialect;
    }
}
