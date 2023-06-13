package be.vdab.expo.domain;

public class Bestelling {

    private final String naam;
    private final int type;

    public Bestelling(String naam, int type) {
        if(naam.isBlank()){
            throw new IllegalArgumentException("Naam mag niet leeg zijn");
        }

        if((type <=0)||(type > 3)){
            throw new IllegalArgumentException("Dit type ticket is niet beschikbaar");
        }
        this.naam = naam;
        this.type = type;
    }

    public String getNaam() {
        return naam;
    }

    public int getType() {
        return type;
    }
}
