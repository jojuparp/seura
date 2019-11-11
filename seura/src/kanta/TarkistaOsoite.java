package kanta;

/**
 * Luokassa suoritetaan oikeellisuustarkistuksia osoitteelle.
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class TarkistaOsoite {
    
    /** Funktio tarkistamaan osoitteein oikeellisuus. Sama toiminnallisuus kuin nimen tarkistuksessa.
     * Osoitteen oltava v�hint��n kaksiosainen, muuten palauttaa false.
     * @param os osoite
     * @return 'true', jos osoite v�hint��n kaksiosainen. Muuten 'false'
     * @example
     * <pre name="test">
     * tarkistaOsoite("Lakkatie 8") === true;
     * tarkistaOsoite("Mustikkakatu 9 b 27") === true;
     * tarkistaOsoite("jyv�skyl�") === false;
     * tarkistaOsoite("") === false;
     * </pre>
     */
    public static boolean tarkistaOsoite(String os) {
        
        
        return TarkistaNimi.nimiTarkistus(os);
    }

    /**
     * @param args ei k�yt�ss�
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
    
    }

}
