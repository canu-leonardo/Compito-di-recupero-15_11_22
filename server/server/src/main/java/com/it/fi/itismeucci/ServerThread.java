package com.it.fi.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerThread extends Thread{

    ServerSocket server      = null;
    Socket client            = null;
    String stringaRicevuta   = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoIlClient;

    public ServerThread (Socket socket){
        this.client = socket;
    }   

    public void run(){
        try {
            comunica();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void comunica() throws Exception{
        
        while (true){
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoIlClient = new DataOutputStream(client.getOutputStream());

            ObjectMapper toJson = new ObjectMapper();
            Messaggio messaggioRicevuto;
            Messaggio messaggioDaInviare = new Messaggio();

            stringaRicevuta = inDalClient.readLine();
            messaggioRicevuto = toJson.readValue(stringaRicevuta, Messaggio.class);

            if (messaggioRicevuto.getPersone() != null &&  messaggioRicevuto.getNazioneRichiesta() == null){
                for (int i = 0; i < messaggioRicevuto.getPersone().size(); i++){
                    ServerMain.personePresenti.add(messaggioRicevuto.getPersone().get(i));
                }

                messaggioDaInviare.setNazioneRichiesta(null);
                messaggioDaInviare.setPersone(null);

            }else if (messaggioRicevuto.getPersone() == null &&  messaggioRicevuto.getNazioneRichiesta() != null){
                ArrayList<Persona> personeScelte = new ArrayList<>();

                for (int i = 0; i < ServerMain.personePresenti.size(); i++) {
                    if (ServerMain.personePresenti.get(i).getNazioneDiResidenza().equals(messaggioRicevuto.getNazioneRichiesta())){
                        personeScelte.add(ServerMain.personePresenti.get(i));
                    }
                }

                messaggioDaInviare.setNazioneRichiesta(messaggioRicevuto.getNazioneRichiesta());
                messaggioDaInviare.setPersone(personeScelte);

            }else if (messaggioRicevuto.getPersone() == null &&  messaggioRicevuto.getNazioneRichiesta() == null){
                messaggioDaInviare.setNazioneRichiesta(null);
                messaggioDaInviare.setPersone(ServerMain.personePresenti);

            }

            outVersoIlClient.writeBytes(toJson.writeValueAsString(messaggioDaInviare) + "\n");
        }
    }
    
}
