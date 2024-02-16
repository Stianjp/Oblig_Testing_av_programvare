package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Repository;
import oslomet.testing.API.AdminKontoController;
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

public class EnhetstestAdminKontoController {

    @InjectMocks
    // denne skal testes
    private AdminKontoController adminKontoController;

    @Mock
    // denne skal Mock'es
    private AdminRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void hentAlleKonti_LoggetInn() {
        // arrange
        List<Konto> alleKonti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        alleKonti.add(konto1);
        alleKonti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentAlleKonti()).thenReturn(alleKonti);

        // act
        List<Konto> resultat = adminKontoController.hentAlleKonti();

        // assert
        assertEquals(alleKonti, resultat);
    }
    @Test
    public void hentAlleKonti_IkkeLoggetInn()  {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = adminKontoController.hentAlleKonti();

        // assert
        assertNull(resultat);
    }
    @Test
    public void registrerKonto_LoggetInn() {
        // Arrange
        String personnummer = "123456789";
        when(sjekk.loggetInn()).thenReturn(personnummer);
        Konto konto = new Konto();

        when(repository.registrerKonto(any(Konto.class))).thenReturn("OK");

        // Act
        String resultat = adminKontoController.registrerKonto(konto);

        // Assert
        assertEquals("OK", resultat);
    }
    @Test
    public void registrerKonto_IkkeLoggetInn() {
        // Arrange
        when(sjekk.loggetInn()).thenReturn(null);
        Konto konto = new Konto();

        // Act
        String resultat = adminKontoController.registrerKonto(konto);

        // Assert
        assertEquals("Ikke innlogget", resultat);
    }
    @Test
    public void endreKonto_LoggetInn() {
        // Arrange
        String personnummer = "123456789";
        when(sjekk.loggetInn()).thenReturn(personnummer);
        Konto konto = new Konto();

        when(repository.endreKonto(any(Konto.class))).thenReturn("OK");

        // Act
        String resultat = adminKontoController.endreKonto(konto);

        // Assert
        assertEquals("OK", resultat);
    }
    @Test
    public void endreKonto_IkkeLoggetInn() {
        // Arrange
        when(sjekk.loggetInn()).thenReturn(null);
        Konto konto = new Konto();

        // Act
        String resultat = adminKontoController.endreKonto(konto);

        // Assert
        assertEquals("Ikke innlogget", resultat);
    }
    @Test
    public void slettKonto_LoggetInn() {
        // Arrange
        String personnummer = "123456789";
        when(sjekk.loggetInn()).thenReturn(personnummer);
        String kontonummer = "123456";

        when(repository.slettKonto(anyString())).thenReturn("OK");

        // Act
        String resultat = adminKontoController.slettKonto(kontonummer);

        // Assert
        assertEquals("OK", resultat);
    }
    @Test
    public void slettKonto_IkkeLoggetInn() {
        // Arrange
        when(sjekk.loggetInn()).thenReturn(null);
        String kontonummer = "123456";

        // Act
        String resultat = adminKontoController.slettKonto(kontonummer);

        // Assert
        assertEquals("Ikke innlogget", resultat);
    }
}
