## Ohjelman käyttö

Ohjelmaa käytetään antamalla komentoriville seuraava komento:
javaw --module-path c:\devel\javafx\lib --add-modules javafx.controls,javafx.fxml -jar rekisteri.jar 

### Pääikkuna

Kun ohjelma käynnistyy, on näkyvillä ohjelman pääikkuna.

![Paaikkuna](https://github.com/jojuparp/seura/blob/master/dokumentaatio/kuvat/paaikkuna.png)

### Käyttö

Navigoidaan vasemmasta sivupalkista halutun joukkueen kohdalle, ja valitaan se hiirellä. Harrastajia voi poistaa/lisätä valitsemalla joukkue ja käyttämällä ylävalikon vaihtoehtoja. Oikeassa paneelissa näkyy harrastajan tiedot. Mahdollisuus käyttää hakutoimintoa ja etsiä harrastajia nimen perusteella.


### Muokkaaminen

Jäsenen tietoja muokataan klikkaamalla harrastajaa ja valitsemalla joko hiiren oikealla näppäimellä tai ylävalikosta (muokkaa). Tällöin avautuu uuteen ikkunaan harrastajan tiedot muokattavaksi. Tallentaminen (tallenna) painikkeella. Mikäli jäsen poistetaan, muokkausdialogi sulkeutuu ja jäsen poistuu listasta. Poistettaessa näytetään varmistusdialogi.

![muokkaa](https://github.com/jojuparp/seura/blob/master/dokumentaatio/kuvat/muokkaa.png)

![poisto](https://github.com/jojuparp/seura/blob/master/dokumentaatio/kuvat/poisto.png)
