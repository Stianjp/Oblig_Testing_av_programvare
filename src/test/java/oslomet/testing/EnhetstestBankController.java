package oslomet.testing;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;


    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void test_hentTransaksjonerOK(){ // ENHETSTEST FOR hentTransaksjoner
        //arrange lager to fiktive transaksjoner
        List<Transaksjon> transaksjoner = new ArrayList<>();

        Transaksjon transaksjon1 = new Transaksjon(1, "123456789100", 1000, "2024-02-07", "Test betaling", "1", "1001123322323");
        Transaksjon transaksjon2 = new Transaksjon(2, "987654321012", 49.9,"2024-02-02", "Grandiosa", "1", "001122334455");

        //Legger de to fiktive transaksjonene i listen
        transaksjoner.add(transaksjon1);
        transaksjoner.add(transaksjon2);
        Konto enKonto = new Konto("123565432", "12345654", 1000.00, "Sparekonto", "NOK", transaksjoner);


        // Setter opp forventede verdier mot mock-objektene
        when(sjekk.loggetInn()).thenReturn("010501213265");

        when(repository.hentTransaksjoner(anyString(), anyString(), anyString())).thenReturn(enKonto);

        //act - utfører handlingen som testes
        Konto resultat = bankController.hentTransaksjoner("1001123322323", "2024-02-07", "2024-02-08");

        //assert - verifiserer
        assertEquals(enKonto, resultat, "Feil i hentTransaksjoner");
    }
    @Test
    public void hentTransaksjoner_IkkeLoggetInn(){ //Enhetstest FOR hentTransaksjoner_IkkeLoggetInn
        //arrange

        // Setter opp forventede verdier mot mock-objektene
        when(sjekk.loggetInn()).thenReturn(null);


        //act - utfører handlingen som testes
        Konto resultat = bankController.hentTransaksjoner(null, null, null);

        //assert - verifiserer
        assertNull(resultat);
    }
    @Test
    public void test_hentSaldi_LoggetInn(){ // ENHETSTEST FOR hentSaldi
        //arrange
        List<Konto> saldi = new ArrayList<>();

        Konto saldi1 = new Konto("12345654", "1234565432", 420, "Lønnskonto", "EUR", null);
        Konto saldi2 = new Konto("12665654", "1234565532", 720, "Sparekonto", "NOK", null);

        //Legger de fiktive transaksjonene i listen
        saldi.add(saldi1);
        saldi.add(saldi2);

        when(sjekk.loggetInn()).thenReturn("345654345654");
        when(repository.hentSaldi(anyString())).thenReturn(saldi);

        // act
        List<Konto> resultat = bankController.hentSaldi();
        // assert
        assertEquals(saldi, resultat, "Feil i hentSaldi");
    }

    @Test
    public void test_hentSaldi_IkkeLoggetInn(){
        //arrange
        when(sjekk.loggetInn()).thenReturn(null);
        //act
        List<Konto> resultat = bankController.hentSaldi();

        //assert
        assertNull(resultat);

    }

    @Test
    public void test_hentRegistrerBetaling_loggetInn(){
        // arrange
        Transaksjon regBetaling = new Transaksjon();
        String personnr = "123234234";
        String forventetResultat = "Betalingen er registrert";

        when(sjekk.loggetInn()).thenReturn(personnr);
        when(repository.registrerBetaling(any(Transaksjon.class))).thenReturn(forventetResultat);

        //act
        String resultat = bankController.registrerBetaling(regBetaling);

        // assert
        assertEquals(forventetResultat, resultat, "Betalingen skulle blitt registrert");
    }

    @Test
    public void test_hentRegistrerBetaling_IkkeLoggetInn(){
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);
        // act
        String resultat = bankController.registrerBetaling(null);
        // assert
        assertNull(resultat);
    }

    @Test
    public void test_hentBetaling_loggetInn(){
        // arrange
        List<Transaksjon> hentBet = new ArrayList<>();
        Transaksjon transaksjon1 = new Transaksjon(1, "234234234", 234.00, "01.01.24", "Test", "1", "2342342344");
        Transaksjon transaksjon2 = new Transaksjon(2, "2342343244", 420.00, "02.02.24", "en-to-en", "2", "234234234324");

        hentBet.add(transaksjon1);
        hentBet.add(transaksjon2);

        when(sjekk.loggetInn()).thenReturn("234234234234");
        when(repository.hentBetalinger(anyString())).thenReturn(hentBet);

        // act
        List<Transaksjon> resultat = bankController.hentBetalinger();
        // assert
        assertEquals(hentBet, resultat, "Feil i hentBetaling");
    }
    @Test
    public void test_hentBetaling_ikkeLoggetInn(){
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);
        // act
        List<Transaksjon> resultat = bankController.hentBetalinger();
        // assert
        assertNull(resultat);
    }

    @Test
    public void test_utforBetaling_loggetInn(){
        // arrange
        List<Transaksjon> utforBet = new ArrayList<>();

        Transaksjon test1 = new Transaksjon(123, "2342343245", 420.01, "01.04.23","hallo", "234234324", "234234234324");
        Transaksjon test2 = new Transaksjon(321, "5534235645", 720.20, "09.04.21","hei", "24", "65464324");
        utforBet.add(test1);
        utforBet.add(test2);


        when(sjekk.loggetInn()).thenReturn("234234234324");
        when(repository.utforBetaling(anyInt())).thenReturn("OK");
        when(repository.hentBetalinger(anyString())).thenReturn(utforBet);

        // act
        List<Transaksjon> resultat = bankController.utforBetaling(123);


        // assert
        assertEquals(utforBet, resultat);
    }

    @Test
    public void test_utforBetaling_ikkeLoggetInn(){
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);
        // act
        List<Transaksjon> resultat = bankController.utforBetaling(0);
        // assert
        assertNull(resultat);
    }

    @Test
    public void endreKundeInfo_loggetInn(){
        // arrange
        Kunde enKunde = new Kunde();
        String personrnr = "2342342341";
        String forventetResultat = "Endring gjennomført";

        when(sjekk.loggetInn()).thenReturn(personrnr);
        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn(forventetResultat);

        // act
        String resultat = bankController.endre(enKunde);

        // assert
        assertEquals(forventetResultat, resultat);
    }

    @Test
    public void endreKundeInfo_ikkeLoggetInn(){
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);
        // act
        String resultat = bankController.endre(null);
        // assert
        assertNull(resultat);
    }

    @Test
    public void hentKundeInfo_loggetInn() {

        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertEquals(enKunde, resultat);
    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentKonti_LoggetInn()  {
        // arrange
        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn()  {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }
}

