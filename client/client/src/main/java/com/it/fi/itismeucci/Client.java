package com.it.fi.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {

    String nomeServer = "localhost";
    int porta = 7777;
    Socket mioSocket;
    BufferedReader tastiera;
    DataOutputStream outVersoIlServer;
    BufferedReader inDalServer;
    

    public Socket connetti(){
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            mioSocket = new Socket(nomeServer, porta);

            outVersoIlServer = new DataOutputStream(mioSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader( mioSocket.getInputStream()));
        } catch (Exception e) {
           
            System.out.println("errore durante la comunicazione");
            System.exit(-1);
        }
        return mioSocket;    

    }

    public void comunica(){
        try {           

            ObjectMapper toJson = new ObjectMapper();           
            
            String messaggioRicevuto;
            String continua = "y";
            String risposta;
            
            while (continua.equals("y")){
                Messaggio m = new Messaggio();
                System.out.println("Cosa vuoi fare? \n -1 aggiungi persone \n -2 visualizza persone di una nazione \n -3 visualizza tutte le persone");
                
                do{
                    System.out.println("Inserisci il numero dell'opzione che hai scelto");
                    risposta = tastiera.readLine();

                }while (risposta.equals("1") == false && risposta.equals("2") == false && risposta.equals("3") == false);

                if (risposta.equals("1")){

                    AggiungiPersone();

                }else if (risposta.equals("2")){
                    
                    ricercaPerNazione();

                }else if (risposta.equals("3")){
                    
                    getelencoPersone();

                }
                System.out.println("vuoi compiere un altra azione?(y/n)");
                continua = tastiera.readLine();

            }
        } catch (Exception e) {           
            System.out.println("errore durante la comunicazione");
        }
    }

    public void AggiungiPersone() throws IOException{
        ObjectMapper toJson = new ObjectMapper();  
        Messaggio m = new Messaggio();         
            
        String messaggioRicevuto;
        String continua = "y";
        String risposta;

        String continuare = "y";
        ArrayList<Persona> personeDaAggiungere = new ArrayList<>();

        while (continuare.equals("y")){
            Persona p = new Persona();
            System.out.println("Inserisci il nome della persona da aggiungere");
            p.setNome(tastiera.readLine());
            System.out.println("Inserisci il cognome della persona da aggiungere");
            p.setCognome(tastiera.readLine());
            System.out.println("Inserisci la nazione ddi appartenenza della persona da aggiungere");
            p.setNazioneDiResidenza(tastiera.readLine());
            personeDaAggiungere.add(p);


            System.out.println("ne vuoi aggiungere un altra (y/n)");
            continuare = tastiera.readLine();
        }

        m.setPersone(personeDaAggiungere);
        m.setNazioneRichiesta(null);
        outVersoIlServer.writeBytes(toJson.writeValueAsString(m) + "\n");

        messaggioRicevuto = inDalServer.readLine();

        Messaggio messaggio = toJson.readValue(messaggioRicevuto, Messaggio.class);
        System.out.println("richiesta accettata correttamente dal server");
    }

    public void ricercaPerNazione() throws IOException{
        ObjectMapper toJson = new ObjectMapper();  
        Messaggio m = new Messaggio();    
        String messaggioRicevuto;

        System.out.println("Inserisci il nome della nazione");                
        m.setPersone(null);
        m.setNazioneRichiesta(tastiera.readLine());
        
        outVersoIlServer.writeBytes(toJson.writeValueAsString(m) + "\n");

        messaggioRicevuto = inDalServer.readLine();

        Messaggio messaggio = toJson.readValue(messaggioRicevuto, Messaggio.class);

        System.out.println("Persone della nazione " + m.getNazioneRichiesta() + ": ");
        if (messaggio.getNazioneRichiesta() != null){
            for (int i = 0; i < messaggio.getPersone().size(); i++){
                System.err.println("-Nome: " + messaggio.getPersone().get(i).getNome() + ",Cognome: " + messaggio.getPersone().get(i).getCognome() + ".");
            }   
        }else{
            System.out.println("nessuna persona della nazione richiesta trovata");
        }
    }

    public void getelencoPersone() throws JsonProcessingException, IOException{

        ObjectMapper toJson = new ObjectMapper();  
        Messaggio m = new Messaggio();    
        String messaggioRicevuto;


        m.setPersone(null);
        m.setNazioneRichiesta(null);
        outVersoIlServer.writeBytes(toJson.writeValueAsString(m) + "\n");
                    
        messaggioRicevuto = inDalServer.readLine();

        Messaggio messaggio = toJson.readValue(messaggioRicevuto, Messaggio.class);

        System.out.println("____________ \n Persone presenti:");
        for (int i = 0; i < messaggio.getPersone().size(); i++){
            System.err.println("-Nome: " + messaggio.getPersone().get(i).getNome() + ",Cognome: " + messaggio.getPersone().get(i).getCognome() + ", Nazione: " + messaggio.getPersone().get(i).getNazioneDiResidenza() + ".");
        }

    }
}