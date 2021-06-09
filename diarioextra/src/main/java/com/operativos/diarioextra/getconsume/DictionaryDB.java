package com.operativos.diarioextra.getconsume;

import java.util.ArrayList;

public class DictionaryDB {
    ArrayList<String> Deportes = new ArrayList<String>();
    ArrayList<String> Economia = new ArrayList<String>();
    ArrayList<String> Entretenimiento = new ArrayList<String>();
    ArrayList<String> Eventos = new ArrayList<String>();
    ArrayList<String> Politica = new ArrayList<String>();
    ArrayList<String> Salud = new ArrayList<String>();
    
    public DictionaryDB(ArrayList<String> deportes, ArrayList<String> economia, ArrayList<String> entretenimiento,
            ArrayList<String> eventos, ArrayList<String> politica, ArrayList<String> salud) {
        Deportes = deportes;
        Economia = economia;
        Entretenimiento = entretenimiento;
        Eventos = eventos;
        Politica = politica;
        Salud = salud;
    }

    public ArrayList<String> getDeportes() {
        return Deportes;
    }

    public void setDeportes(ArrayList<String> deportes) {
        Deportes = deportes;
    }

    public ArrayList<String> getEconomia() {
        return Economia;
    }

    public void setEconomia(ArrayList<String> economia) {
        Economia = economia;
    }

    public ArrayList<String> getEntretenimiento() {
        return Entretenimiento;
    }

    public void setEntretenimiento(ArrayList<String> entretenimiento) {
        Entretenimiento = entretenimiento;
    }

    public ArrayList<String> getEventos() {
        return Eventos;
    }

    public void setEventos(ArrayList<String> eventos) {
        Eventos = eventos;
    }

    public ArrayList<String> getPolitica() {
        return Politica;
    }

    public void setPolitica(ArrayList<String> politica) {
        Politica = politica;
    }

    public ArrayList<String> getSalud() {
        return Salud;
    }

    public void setSalud(ArrayList<String> salud) {
        Salud = salud;
    }

    
}
