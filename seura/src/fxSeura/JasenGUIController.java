package fxSeura;

import java.net.URL;
import java.util.ResourceBundle;

//import java.awt.Button;

//import java.net.URL;
//import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs

;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
//import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
//import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logiikka.Jasen;


/** Kontrolleri jäsenen muokkausdialogille
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class JasenGUIController implements ModalControllerInterface<Jasen>, Initializable {

    @FXML private TextField editNimi;
    @FXML private TextField editSyntaika;
    @FXML private TextField editPno;
    @FXML private TextField editSapo;
    @FXML private TextField editOsoite;
    @FXML private TextField editHuoltajanNimi;
    @FXML private TextField editHuoltajanPno;
    @FXML private TextField editHuoltajanSapo;
    @FXML private TextField editLisatietoja;
    @FXML private Label labelVirhe;
    
    private TextField[] edits;
    
    private Jasen klikattuJasen;
    
    
    @Override
    public void setDefault(Jasen oletus) {
        klikattuJasen = oletus;
        naytaJasen(klikattuJasen);
    }
    
    
    @Override
    public void handleShown() {
        //
    }
    
    
    @Override
    public Jasen getResult() { return klikattuJasen; }
    
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    
    /**
     * Tekee tarvittavat alustukset. Mm. kokoaa edit-kentät yhteen taulukkoon ja laittaa kenttien muutokset
     * eteenpäin muutosten käsittelymetodille.
     */
    protected void alusta() {
        edits = new TextField[]{
          editNimi, editSyntaika, editPno, editSapo, editOsoite,
          editHuoltajanNimi, editHuoltajanPno, editHuoltajanSapo,
          editLisatietoja
        };
        
        
        int i = 0;
        for ( TextField edit : edits ) {
            final int k = ++i;
            edit.setOnKeyReleased( e -> kasitteleJasenenMuutos(k, (TextField)e.getSource()) );
        }
        
        
    }
    
    
    /*
     * Metodi käsittelee kaikki tekstikenttiin kirjoitetut muutokset.
     */
    private void kasitteleJasenenMuutos(int k, TextField edit) {
        
        if ( klikattuJasen == null ) return;
        
        String s = edit.getText();
        String virhe = null;
        
        switch (k) {
        case 1 : virhe = klikattuJasen.setNimi(s); break;
        case 2 : virhe = klikattuJasen.setSyntaika(s); break;
        case 3 : virhe = klikattuJasen.setPuhno(s); break;
        case 4 : virhe = klikattuJasen.setSapo(s); break;
        case 5 : virhe = klikattuJasen.setOsoite(s); break;
        case 6 : virhe = klikattuJasen.setVanhemmanNimi(s); break;
        case 7 : virhe = klikattuJasen.setVanhemmanPuhno(s); break;
        case 8 : virhe = klikattuJasen.setVanhemmanSapo(s); break;
        case 9 : virhe = klikattuJasen.setLisatietoja(s); break;
        default:
        }
        
        
        if ( virhe == null ) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        }
        else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
        
        
    }
    
    
    /**
     * tallennusmetodi
     * @return jasen
     */
//    public Jasen tallenna() {
//        //Dialogs.showMessageDialog("Tallennetetaan! Mutta ei toimi vielä");
//        return klikattuJasen;
//        //ModalController.closeStage(editLisatietoja);
//    }
    
    
    /**
     * Ei tallenna muutoksia ja sulkee ikkunan
     */
    public void peruuta() { ModalController.closeStage(editLisatietoja); }
    
    
    /**
     * @param modalityStage mille ollaan modaalisia, null == sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @return null jos painetaan "peruuta", muuten muokattu tietue
     * 
     */
    public static Jasen kysyJasen(Stage modalityStage, Jasen oletus) {
        
        Jasen muokattu = ModalController.showModal(
                JasenGUIController.class.getResource("JasenDialogView.fxml"),
                "Muokkaa", modalityStage, oletus);
        
        return muokattu;
    }
    
    
    /**
     * Näytetään jäsenen tiedot dialogiin
     * @param oletus  ads
     */
    public void naytaJasen(Jasen oletus) {
        
        if ( oletus == null ) return;
        
        
        
        editNimi.setText( klikattuJasen.getNimi() );
        editSyntaika.setText( klikattuJasen.getSyntaika() );
        editPno.setText( klikattuJasen.getPuhno() );
        editSapo.setText( klikattuJasen.getSapo() );
        editOsoite.setText( klikattuJasen.getOsoite() );
        editHuoltajanNimi.setText( klikattuJasen.getVanhemmanNimi() );
        editHuoltajanSapo.setText( klikattuJasen.getVanhemmanSapo() );
        editHuoltajanPno.setText( klikattuJasen.getVanhemmanPuhno() );
        editLisatietoja.setText( klikattuJasen.getLisatietoja() );
        
    }
    
    
    /*
     * Virheilmoituksen käsittely
     */
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
           labelVirhe.setText("");
           labelVirhe.getStyleClass().removeAll("virhe");
           return;
        }
        
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    
    
    
    
    
    //=============================================================
    //-----------------KÄYTTÄLIITTYMÄKUTSUT ALLA-------------------
    //=============================================================
    
    
    
    /*
     * käsitellään peruutusmetodi
     */
    @FXML private void handlePeruuta() { klikattuJasen = null; peruuta(); }
    
    
    /*
     * käsitellään tallennusmetodi
     */
    @FXML private void handleTallenna() {
        if ( klikattuJasen != null && klikattuJasen.getNimi().trim().equals("") ) { naytaVirhe("Nimi ei saa olla tyhjä!"); return; }
        
        ModalController.closeStage(labelVirhe);
    }
    
    
}
