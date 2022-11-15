package com.it.fi.itismeucci;

import java.util.ArrayList;

public class Messaggio {
    String nazioneRichiesta;
    ArrayList<Persona> persone = new ArrayList<>();
    public Messaggio() {
    }
    public String getNazioneRichiesta() {
        return nazioneRichiesta;
    }
    public void setNazioneRichiesta(String nazioneRichiesta) {
        this.nazioneRichiesta = nazioneRichiesta;
    }
    public ArrayList<Persona> getPersone() {
        return persone;
    }
    public void setPersone(ArrayList<Persona> persone) {
        this.persone = persone;
    }

    
    
}
