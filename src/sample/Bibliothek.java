package sample;

import java.io.IOException;

public class Bibliothek {

//Alles bis einschließlich Aufgabenblatt D von Fuad übernommen !!!

    public static void main(String[] args) throws IOException {

        @SuppressWarnings("unused")
        BinaryPersistency binaryPersistency = new BinaryPersistency();
        @SuppressWarnings("unused")
        HumanReadablePersistency humanReadablePersistency = new HumanReadablePersistency();
        WikiBooksParser w = new WikiBooksParser();
        Zettelkasten zettelkasten = new Zettelkasten();

        if (args.length == 2) {
            if (args[0].equals("parse")) {
                zettelkasten.addMedium(w.parse(args[1]));
            }
        }

        zettelkasten.addMedium(w.parse("Java_Standard"));
        zettelkasten.addMedium(w.parse("_XHTML"));

    }
}