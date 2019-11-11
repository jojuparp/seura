package fxSeura;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import logiikka.Jasen;
import logiikka.JasenJaJoukkue;
import logiikka.Joukkue;
import logiikka.Seura;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author Joni Parpala
 * @version 10.5.2019
 * Luokka seuran käyttöliittymän tapahtumien hoitamiseksi
 * 
 * TODO: tulostuksen käsittely toimivaksi, joukkueen muokkaaminen liittymästä, joukkueen poistaminen liittymästä.
 */
public class SeuraGUIController implements Initializable {
    
    
    private Seura seura;
    //private TextArea areaJasen = new TextArea(); //==================RAKENNUSTELINE===================
    private Jasen klikattuJasen;
    private Joukkue klikattuJoukkue;
    //final JasenGUIController seuraCtrl = (JasenGUIController)ldr.getController();
    
    @FXML private ScrollPane panelJasenTiedot;
    @FXML private ListChooser<Jasen> listJasenet;
    @FXML private ListChooser<Joukkue> listJoukkueet;
    
    @FXML private TextField editNimi;
    @FXML private TextField editSyntaika;
    @FXML private TextField editPno;
    @FXML private TextField editSapo;
    @FXML private TextField editOsoite;
    @FXML private TextField editHuoltaja;
    @FXML private TextField editHuoltajaPno;
    @FXML private TextField editHuoltajaSapo;
    @FXML private TextField editLisatietoja;
    @FXML private TextField editJoukkueet;
    
    @FXML private TextField editHaku;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        //
    }

    
    /**
     * Funktio jäsenen poistamiselle
     * ottaa parametrina poistettavan jäsenen
     * @param j 
     */
    private void poistetaanko(Jasen j) {
        // Kun tarvitsee kysyä Kyllä/Ei tyyppisiä kysymyksiä:

        boolean vastaus = Dialogs.showQuestionDialog("Poisto?",
                           "Poistetaanko jäsen: " + j.getNimi() + "?", "Kyllä", "Ei");
        if ( vastaus ) poistaJasen(j);
    }
    
    
    /**
     * Jäsenen poisto controllerista käsin
     */
    private void poistaJasen(Jasen j) {
        seura.poista(j);        
        haeJasen(-1);
    }
    
    
    /**
     * Tekee uuden relaation valitun jäsenen ja joukkueen välille.
     */
    private void uusiRelaatio() {
        if ( klikattuJasen == null || klikattuJoukkue == null ) return;
        JasenJaJoukkue relaatio = seura.luoRelaatio(klikattuJasen.getId(), klikattuJoukkue.getId());
        if ( seura.onkoOlemassa(relaatio) ) return;

        seura.lisaa(relaatio);
        
        naytaJasen();
    }
    
    
    /**
     * Joukkueen luontimetodi controllerista käsin
     */
    private void uusiJoukkue() {
        Joukkue uusi = new Joukkue();
        uusi.taytaTiedot();
        seura.lisaa(uusi);
        
        haeJoukkue(uusi.getId());
    }
    

    /**
     * Jäsenen luontimetodi controllerista käin
     * @throws CloneNotSupportedException 
     */
    private void uusiJasen() throws CloneNotSupportedException {
        Jasen jasen = new Jasen();
        jasen.taytaTiedot();
        jasen = JasenGUIController.kysyJasen(null, jasen.clone());
        if ( jasen == null ) return;
        jasen.rekisteroi();
        seura.korvaaTaiLisaa(jasen);
        haeJasen(jasen.getId());
        naytaJasen();

    }
    
    
    /**
     * Metodi uuden jäsenen hakemiselle käyttöliittymän listaan.
     * @param jasenId jäsenen id
     */
    protected void haeJasen(int jasenId) {  
        listJasenet.clear();
        
        int indeksi = 0;
        for (int i = 0; i < seura.getJasenia(); i++) {
            Jasen jasen = seura.annaJasen(i);
            if ( jasen.getId() == jasenId ) indeksi = i;
            listJasenet.add(jasen.getNimi(), jasen);
        }
        
        listJasenet.getSelectionModel().select(indeksi);
        
    }
    
    
    /**
     * Metodi uuden joukkueen hakemiselle käyttöliittymän listaan
     * @param joukkueId joukkueen id
     */
    protected void haeJoukkue(int joukkueId) {
        listJoukkueet.clear();
        
        int indeksi = 0;
        for (int i = 0; i < seura.getJoukkueita(); i++) {
            Joukkue joukkue = seura.annaJoukkue(i);
            if ( joukkue.getId() == joukkueId ) indeksi = i;
            listJoukkueet.add(joukkue.getNimi(), joukkue);
        }
        
        listJoukkueet.getSelectionModel().select(indeksi);
    
    }
    
    
    /**
     * Hakumetodi
     */
    private void hae() {
        
        //editHaku.setOnKeyReleased( e -> kasitteleHaku((TextField)e.getSource()));
        
        kasitteleHaku(editHaku);
    }
    
    
    private void kasitteleHaku(TextField hakukentta) {
        listJasenet.clear();
        String ehto = hakukentta.getText();
        
        List<Jasen> tulokset = seura.haeJasenet(ehto);
        if ( tulokset.size() < 1 ) { 
            Dialogs.showMessageDialog("Ei tuloksia!");
            haeJasen(-1); 
            return;
        }
        
        for ( Jasen j : tulokset ) { listJasenet.add(j.getNimi(), j); }
    }
    
    
    /** 
     * Metodi näyttää ohjelman tiedot. Tekijän, versionumeron jne. 
     */
    private void naytaTiedot() {
        ModalController.showModal(SeuraGUIController.class.getResource("TietojaView.fxml"),
                "Tietoja", null, "tuloste");
         
    }
    
    
    /**
     * metodi lopettaa ohjelman
     */
    private void lopeta() {
        boolean vastaus = Dialogs.showQuestionDialog("Lopetetaanko?",
                "Suljetaanko ohjelma?", "Kyllä", "Ei");
        
        if (vastaus)Platform.exit();
    }
    
    
    /**
     * Metodi tulostaa harjoitusty�n suunnitelman www-selaimeen
     */
    private void ohjeet() {
    Desktop desktop = Desktop.getDesktop();
    try {
       URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/jojuparp");
       desktop.browse(uri);
        } 
    catch (URISyntaxException e) {
       return;
    }
    
    catch (IOException e) {
       return;
    }
    
    }
    
    /**
     * Metodi jäsenen tietojen tulostamiselle erilliseen dialogiin
     * TODO: korjaa toimivaksi
     */
    private void tulostaTiedot() {
        Dialogs.showMessageDialog("Tulostus ei toimi vielä!");
    }
    
    
    /**
     * Metodi tulostaa valittujen jäsenten kaikki tiedot
     */
//    private void tulosta(PrintStream os, final Jasen jasen) {
//        os.println("--------------------------------------------");
//        jasen.tulosta(os);
//        os.println("--------------------------------------------\nJäsenen joukkueet:\n");
//        List<JasenJaJoukkue> relaatiot = seura.etsiJasenenJoukkueet(jasen.getId());
//        List<Joukkue> joukkueet = new ArrayList<Joukkue>();
//        
//        for ( JasenJaJoukkue r : relaatiot ) {
//            joukkueet.add(seura.etsiJoukkue(r.getJoukkueId()));
//        }
//        
//        for ( Joukkue j : joukkueet ) { j.tulosta(os); }
//        
////        ModalController.showModal(SeuraGUIController.class.getResource("TulostusView.fxml"),
////                "Kerho", null, "tuloste");
//    }   

    


    
    /** Metodi viitteen asettamiselle käytettävään kerhoon 
     * @param seura asetettava seura
     */
    public void setSeura(Seura seura) { this.seura = seura; }
    
    
    /** Metodi alustaa Jäsenen tiedot -alueen pelkäksi tyhj�ksi tekstialueeksi
     * ===================RAKENNUSTELINE========================
     */
    public void alusta() { 
//        panelJasenTiedot.setContent(areaJasen);
//        areaJasen.setFont(new Font("Consolas", 12));
//        panelJasenTiedot.setFitToHeight(true);
        listJasenet.clear();
        listJasenet.addSelectionListener(e -> naytaJasen());
        listJoukkueet.addSelectionListener(e -> asetaJoukkue());
        
    }
    
    
    /**
     * Metodi joukkuevalinnan asettamsielle klikatun joukkueen kohdalle.
     */
    private void asetaJoukkue() { klikattuJoukkue = listJoukkueet.getSelectedObject(); }


    /**
     * Näyttää listasta valitun jäsenen tiedot
     */
    private void naytaJasen() {
        
        klikattuJasen = listJasenet.getSelectedObject();
        if ( klikattuJasen == null ) return;
        
        editNimi.setText( klikattuJasen.getNimi() );
        editSyntaika.setText( klikattuJasen.getSyntaika() );
        editPno.setText( klikattuJasen.getPuhno() );
        editSapo.setText( klikattuJasen.getSapo() );
        editOsoite.setText( klikattuJasen.getOsoite() );
        editHuoltaja.setText( klikattuJasen.getVanhemmanNimi() );
        editHuoltajaSapo.setText( klikattuJasen.getVanhemmanSapo() );
        editHuoltajaPno.setText( klikattuJasen.getVanhemmanPuhno() );
        editLisatietoja.setText( klikattuJasen.getLisatietoja() );
        editJoukkueet.setText( seura.annaJasenenJoukkueet(klikattuJasen) );
        
        
        /*
        areaJasen.setText("");
        try ( PrintStream os = TextAreaOutputStream.getTextPrintStream(areaJasen) ) {
            tulosta(os, jasenKohta);
        
            
        }
        */
    }
    
    
    /**
     * Tiedoston kirjoitus controllerista käsin
     */
    private void tallenna() { seura.kirjoitaTiedosto(); Dialogs.showMessageDialog("Tallennettu!"); }
    
    
    /**
     * Jäsenen muokkaus
    */
   private void muokkaa() {
       
       if ( klikattuJasen == null ) return;
       
        try {
            Jasen jasen;
            jasen = JasenGUIController.kysyJasen(null, klikattuJasen.clone());
            if ( jasen == null ) return;
            seura.korvaaTaiLisaa(jasen);
            haeJasen(jasen.getId());
            naytaJasen();
        } catch (CloneNotSupportedException e) {
            // 
        }
    }

    
    //=================================================================================================================
    //------------------------------------------KÄYTTÖLIITTYMÄKUTSUT ALLA----------------------------------------------
    //=================================================================================================================
    
       
    /**
     * käsitellään hakumetodi
     */
    @FXML private void handleHae() { hae(); }
    
    
    /**
     * Käsitellään hakutulosten peruutus
     */
    @FXML private void handlePeruutaHaku( ) { haeJasen(-1); }
    
    /**
     * Käsitellään versiotietojen näyttö
     */
    @FXML private void handleNaytaTiedot() { naytaTiedot(); }
    
    
    /**
     * käsitellään tulostusmetodi
     */
    @FXML private void handleTulosta() { tulostaTiedot(); }
    
    
    /**
     * käsitellään jäsenen poisto
     */
    @FXML private void handlePoistetaanko() {
        if ( seura.getJasenia() < 1 ) return;
        poistetaanko(klikattuJasen);
    }
    
    
    /**
     * Käsitellään muokkaus
     */
    @FXML private void handleMuokkaa() { muokkaa(); }
    
    
    /**
     * käsitellään ohjeiden näyttö
     */
    @FXML private void handleOhjeet() { ohjeet(); }
    
    
    /**
     * Käsitellään uuden jäsenen lisääminen
     * @throws CloneNotSupportedException 
     */
    @FXML private void handleUusiJasen() throws CloneNotSupportedException { uusiJasen(); }
    
    
    /**
     * Käsitellään uuden joukkueen lisäys
     */
     @FXML private void handleUusiJoukkue() { uusiJoukkue(); }
    
     
    /**
     * Käsitellään tallennus
     */
    @FXML private void handleTallenna() { tallenna(); }

    
    /**
     * Käsitellään uuden jäsen-joukkue -relaation lisäys
     */
    @FXML private void handleUusiRelaatio() { uusiRelaatio(); }
    

    /**
     * Käsitellään lopetusksäky
     */
    @FXML private void handleLopeta() { lopeta(); }

    
}
