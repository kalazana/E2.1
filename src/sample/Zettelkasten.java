package sample;

import java.io.Serializable;
import java.util.*;

public class Zettelkasten implements Iterable <Medium>, Serializable {


    private static final long serialVersionUID = 1L;

    private ArrayList<Medium> register= new ArrayList<Medium>();;
    private String bSort = null;
    // stores the state of Sort.


    public ArrayList<Medium> getMedium_Arr() {
        return register;
    }
    /**
     * Adds a new media to the ArrayList (paper box).
     *
     * @param medium contains a new medium (book,CD,magazine) object
     */
    public void addMedium(final Medium medium) {
        try {
            register.add(medium);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /*
//     * Search the paper box (ArrayList media) for an Object with the specific title.
//     *
//     * @param title contains title to search for
//     * @return "Medium"-Object if found else null
//     */
//    public Medium findMedium(final String title) {
//        for (Medium medium : media) {
//            if (medium.getTitle().equals(title)) {
//                return medium;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Removes a specific Object from the paper box (ArrayList) that contains the title.
//     * If no Object can be found an error will be displayed.
//     *
//     * @param title contains title of the Object you want to delete
//     */
//    public void dropMedium(final String title) {
//        Medium medium = findMedium(title);
//        if (medium == null) {
//            System.out.println("Es konnte kein Medium zum angegebenen Title gefunden werden.");
//        } else {
//            media.remove(medium);
//        }
//    }

    /**
     * Removes a specific object that contains the title.
     * An error will be displayed if no object can be found
     * or a "DuplicateEntry" Exception will be thrown if multiply objects are found.
     *
     * @param title contains title of the object you want to delete
     * @param string
     */
    @SuppressWarnings("unlikely-arg-type")
    public void dropMedium(final String title, String string) throws DuplicateException{
        ArrayList<Medium> searchResult = findMedium(title);
        if (searchResult.size() > 1) {
        } else if (searchResult.size() == 0) {
            System.out.println("Es konnte kein Medium zum Title \"" + title + "\" gefunden werden.");
        } else {
            register.remove(searchResult);
            System.out.println("\"" + title + "\" wurde erfolgreich gelöscht.");
        }
    }

    /**
     * Removes all Objects that contains the title and are from the specific class.
     * Or removes specific Objects with the title and another parameter.
     * If no Object can be found an error will be displayed.
     *
     * @param title     contains title of the Object the user want to delete
     * @param mediaType contains class type of the media the user want to delete
     */
    public void dropMedium(final String title, final Class<?> mediaType) {
        try {
            ArrayList<Medium> searchResult = findMedium(title);
            @SuppressWarnings("resource")
            Scanner in = new Scanner(System.in);
            String input = "";
            boolean validInput = false;

            if (searchResult.size() > 1) {
                System.out.println("Es wurden mehrere Einträge zu dem Titel \"" + title + "\" gefunden.");
                System.out.print("Möchten Sie alle Einträge löschen oder nur einzelne? ");
                do {
                    input = in.next().toLowerCase();
                    switch (input) {
                        case "alle":
                            for (Object medium : searchResult) {
                                if (medium.getClass().equals(mediaType)) {
                                    register.remove(medium);
                                }
                            }
                            System.out.println("Alle " + mediaType.getSimpleName() + " Einträge mit dem Titel \"" + title + "\" wurden erfolgreich gelöscht.\n");
                            validInput = true;
                            break;
                        case "einzelne":
                            for (Object medium : searchResult) {
                                System.out.println(((Medium) medium).calculateRepresentation() + "\n");
                            }
                            System.out.println("Eine Liste mit allen gefundenen Einträgen zu dem Titel \"" + title + "\" finden Sie weiter oben.");
                            System.out.println("Bitte machen Sie weitere Angaben zu dem zu löschenden Eintrag.");
                            System.out.print("Geben Sie hierzu die ISBN, ISSN oder das Label an: ");
                            in.nextLine();
                            input = in.nextLine();

                            for (Object medium : searchResult) {
                                if (medium instanceof Buch) {
                                    if (((Buch) medium).getISBN().equals(input)) {
                                        register.remove(medium);
                                        break;
                                    }
                                } else if (medium instanceof CD) {
                                    if (((CD) medium).getLabel().equals(input)) {
                                        register.remove(medium);
                                        break;
                                    }
                                } else if (medium instanceof Zeitschrift) {
                                    if (((Zeitschrift) medium).getISSN().equals(input)) {
                                        register.remove(medium);
                                        break;
                                    }
                                }
                            }
                            System.out.println("Der Eintrag wurde erfolgreich gelöscht.\n");
                            validInput = true;
                            break;
                        default:
                            System.out.print("Ungueltige Auswahl. Bitte wählen Sie zwischen \"Alle\" und \"Einzelne\": ");
                            break;
                    }
                } while (!validInput);
            } else if (searchResult.size() == 0) {
                System.out.println("Es konnte kein Eintrag zum Title \"" + title + "\" gefunden werden.\n");
            } else {
                register.remove(searchResult.get(0));
                System.out.println("\"" + title + "\" wurde erfolgreich gelöscht.\n");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes duplicate objects that contains the same title and are from the same class/type.
     * If no Object can be found an error will be displayed.
     *
     * @param title contains title of the Object you want to delete
     */
    public void removeAllDuplicates(final String title) {
        try {
            ArrayList<Medium> searchResult = findMedium(title);
            int counterBook = 0;
            int counterCD = 0;
            int counterMagazine = 0;
            int searchResultSize = searchResult.size();

            if (searchResultSize > 1) {
                for (Object medium : searchResult) {
                    if (medium instanceof Buch) {
                        if (counterBook == 0) {
                            counterBook++;
                            continue;
                        }
                        register.remove(medium);
                    } else if (medium instanceof CD) {
                        if (counterCD == 0) {
                            counterCD++;
                            continue;
                        }
                        register.remove(medium);
                    } else if (medium instanceof Zeitschrift) {
                        if (counterMagazine == 0) {
                            counterMagazine++;
                            continue;
                        }
                        register.remove(medium);
                    }
                }

                if(searchResultSize == searchResult.size())
                {
                    System.out.println("Es wurden keine genauen Duplikate mit dem Titel \"" + title + "\" gefunden.");
                }else {
                    System.out.println("Alle Duplikate mit dem Titel \"" + title + "\" wurden erfolgreich gelöscht.");
                }

            } else if (searchResult.size() == 0) {
                System.out.println("Es konnte kein Medium zum Titel \"" + title + "\" gefunden werden.");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    /**
     * Search ArrayList "media" for an object with the specific title.
     *
     * @param title contains title to search for
     * @return list of found objects
     */
    public ArrayList<Medium> findMedium(final String title) {
        ArrayList<Medium> searchResults = new ArrayList<>();
        for (Medium medium : register) {
            if (medium.getTitel().equals(title)) {
                searchResults.add(medium);
            }
        }
        return searchResults;
    }

    /**
     * Sorts the ArrayList "media" based on the title from „A“„Z“ (asc) or „Z“„A“ (desc) (select per user input).
     */
    //neue sort-Methode
    public void sort(String _sArt) {
        Comparator<Medium> comp = new MediumComparator();

        if (bSort == null) {
            if (_sArt.contentEquals("ab")) {
                Collections.sort(register, comp);
                Collections.reverse(register);
                bSort = "ab";
            }else if(_sArt.contentEquals("auf")) {
                Collections.sort(register);
                bSort = "auf";
            } else {
                System.out.println("Bitte auf/ab eingeben!");
            }
        }else {
            if (bSort.equals("auf") && _sArt.contentEquals("ab")) {
                Collections.sort(register, comp);
                Collections.reverse(register);
                bSort = "ab";
            }else if(bSort.equals("ab") && _sArt.contentEquals("auf")) {
                Collections.sort(register);
                bSort = "auf";
            } else {
                System.out.println("\n\nMedien bereits sortiert!\n");
            }
        }
    }

    private class MediumComparator implements Comparator<Medium>{

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        /**
         * Bei gleichen Titel wird nach dem Typ verglichen.
         */
        public int compare(Medium _M1, Medium _M2) {
            if(_M1.getTitel().compareTo(_M2.getTitel()) == 0){
                return _M1.getClass().getName().substring(7).toString().compareTo(_M2.getClass().getName().substring(7).toString());
            }else if (_M1.getTitel().compareTo(_M2.getTitel()) < 0) {
                return -1;

            }else
                return 1;
        }

    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Medium> iterator() {
        Iterator<Medium> iterator = new Iterator<Medium>() {
            int counter = 0;
            @Override
            public boolean hasNext() {
                if(counter < register.size())
                    return true;
                else
                    return false;
            }

            @Override
            public Medium next() {
                return register.get(counter++);
            }
        };

        return iterator;
    }
}