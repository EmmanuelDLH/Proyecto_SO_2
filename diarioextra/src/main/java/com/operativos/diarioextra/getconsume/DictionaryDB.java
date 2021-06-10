package com.operativos.diarioextra.getconsume;

import java.util.ArrayList;

public class DictionaryDB {
    ArrayList<String> sports;
    ArrayList<String> economy;
    ArrayList<String> entertainment;
    ArrayList<String> events;
    ArrayList<String> politics;
    ArrayList<String> health;

    public DictionaryDB() {
        super();
    }

    public DictionaryDB(ArrayList<String> sports, ArrayList<String> economy, ArrayList<String> entertainment,
            ArrayList<String> events, ArrayList<String> politics, ArrayList<String> health) {
        super();
        this.sports = sports;
        this.economy = economy;
        this.entertainment = entertainment;
        this.events = events;
        this.politics = politics;
        this.health = health;
    }

    public ArrayList<String> getsports() {
        return sports;
    }

    public void setsports(ArrayList<String> sports) {
        this.sports = sports;
    }

    public ArrayList<String> geteconomy() {
        return economy;
    }

    public void seteconomy(ArrayList<String> economy) {
        this.economy = economy;
    }

    public ArrayList<String> getentertainment() {
        return entertainment;
    }

    public void setentertainment(ArrayList<String> entertainment) {
        this.entertainment = entertainment;
    }

    public ArrayList<String> getevents() {
        return events;
    }

    public void setevents(ArrayList<String> events) {
        this.events = events;
    }

    public ArrayList<String> getpolitics() {
        return politics;
    }

    public void setpolitics(ArrayList<String> politics) {
        this.politics = politics;
    }

    public ArrayList<String> gethealth() {
        return health;
    }

    public void sethealth(ArrayList<String> health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "DictionaryDB [economy=" + economy + ", entertainment=" + entertainment + ", events=" + events
                + ", health=" + health + ", politics=" + politics + ", sports=" + sports + "]";
    }
}
