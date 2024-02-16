package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKundeController;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKundeController {

    @InjectMocks
    // denne skal testes
    private AdminKundeController adminKundeController;

    @Mock
    // denne skal Mock'es
    private AdminRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;



    @Test
    public void hentAlle_loggetInn(){
        List<Kunde> kunder = new ArrayList<>();
        Kunde kunde1 = new Kunde("24060185987", "Dennis" , "Janssen" , "galgeberg", "2034" , "Oslo" , "98429456" , "123456789");
        Kunde kunde2 = new Kunde("24060185988" , "Denise" , "Jensen" , "grønland" , "2034" , "Gjerdrum" , "46694798" , "987654321");

        kunder.add(kunde1);
        kunder.add(kunde2);

        when(sjekk.loggetInn()).thenReturn("24060185987");
        when(repository.hentAlleKunder()).thenReturn(kunder);

        List<Kunde> resultat = adminKundeController.hentAlle();

        assertEquals(kunder, resultat);
    }

    @Test
    public void hentAlle_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        List<Kunde> resultat = adminKundeController.hentAlle();

        // assert
        assertNull(resultat);
    }

    @Test
    public void lagreKunde_loggetInn(){
        Kunde kunde1 = new Kunde("24060185987", "Dennis" , "Janssen" , "galgeberg", "2034" , "Oslo" , "98429456" , "123456789");

        when(sjekk.loggetInn()).thenReturn("10101010101");

        when(repository.registrerKunde(any(Kunde.class))).thenReturn("OK");

        String resultat = adminKundeController.lagreKunde(kunde1);

        assertEquals("OK",resultat);

    }

    @Test
    public void lagreKunde_IkkeloggetInn() {

        Kunde kunde1 = new Kunde();

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = adminKundeController.lagreKunde(kunde1);

        // assert
        assertEquals("Ikke logget inn", resultat);
    }

    @Test
    public void endreKunde_loggetInn(){
        Kunde kunde1 = new Kunde("24060185987", "Dennis" , "Janssen" , "galgeberg", "2034" , "Oslo" , "98429456" , "123456789");

        when(sjekk.loggetInn()).thenReturn("10101010101");

        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("OK");

        String resultat = adminKundeController.endre(kunde1);

        assertEquals("OK",resultat);

    }

    @Test
    public void endreKunde_IkkeloggetInn() {

        Kunde kunde1 = new Kunde();

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = adminKundeController.endre(kunde1);

        // assert
        assertEquals("Ikke logget inn", resultat);
    }

    @Test
    public void slett_loggetInn(){

        String personnr ="1010101010";

        when(sjekk.loggetInn()).thenReturn("10101010101");

        when(repository.slettKunde(anyString())).thenReturn("OK");

        String resultat = adminKundeController.slett(personnr);

        assertEquals("OK",resultat);

    }

    @Test
    public void slettKunde_IkkeloggetInn() {

        String personnr = "1010101010";

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = adminKundeController.slett(personnr);

        // assert
        assertEquals("Ikke logget inn", resultat);
    }

}