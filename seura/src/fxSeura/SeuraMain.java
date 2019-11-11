package fxSeura;
	

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

import logiikka.Seura;


/** Ohjelman pääluokka. Käynnistää ohjelman ja alustaa tiedostojen lukemisen. 
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class SeuraMain extends Application {
    
	@Override
	public void start(Stage primaryStage) throws IOException {

    
		try {
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("SeuraGUIView.fxml"));
		    final Pane root = (Pane)ldr.load();
			final SeuraGUIController seuraCtrl = (SeuraGUIController)ldr.getController();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("seura.css").toExternalForm());
			primaryStage.setScene(scene);
			Seura seura = new Seura();
			seuraCtrl.alusta();
			seuraCtrl.setSeura(seura);
			
			seura.lueTiedosto(); seuraCtrl.haeJasen(-1); seuraCtrl.haeJoukkue(-1);
			
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param args ei käytössä
	 * 
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
