package be.vdab.expo.domain;

import be.vdab.expo.exceptions.OnvoldoendeTicketsException;

public class Tickets {
    private int juniorDag;
    private int seniorDag;

    public Tickets(int juniorDag, int seniorDag) {
        this.juniorDag = juniorDag;
        this.seniorDag = seniorDag;
    }

    public int getJuniorDag() {
        return juniorDag;
    }
    public int getSeniorDag() {
        return seniorDag;
    }

    public void bestel(int type){

        switch (type){
            case 1 -> {
                if (juniorDag <= 0) {
                    throw new OnvoldoendeTicketsException("niet voldoende tickets beschikbaar");
                }
                juniorDag--;
            }
            case 2 -> {
                if (seniorDag <= 0) {
                    throw new OnvoldoendeTicketsException("niet voldoende tickets beschikbaar");
                }
                seniorDag--;
            }
            case 3 -> {
                if ((juniorDag <= 0) || (seniorDag <= 0)) {
                    throw new OnvoldoendeTicketsException("niet voldoende tickets beschikbaar");
                }
                juniorDag--;
                seniorDag--;
            }
            default -> throw new IllegalArgumentException();

        }
    }
}
