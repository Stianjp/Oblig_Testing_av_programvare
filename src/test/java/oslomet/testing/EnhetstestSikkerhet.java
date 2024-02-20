package oslomet.testing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhet {
    @Mock
    private BankRepository rep;
    @InjectMocks
    private Sikkerhet sikkerhet;
    @Mock
    private MockHttpSession session;

    @Before
    public void initSession(){
        Map<String,Object> attributes = new HashMap<>();

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(session).getAttribute(anyString());

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(session).setAttribute(anyString(), any());
    }

    @Test
    public void sjekkLoggInn_ValidCredentials_ReturnsOKAndSetsSessionAttribute() {
        // Arrange
        String personnummer = "12345678901";
        String passord = "passord123";
        when(rep.sjekkLoggInn(personnummer, passord)).thenReturn("OK");

        // Act
        String resultat = sikkerhet.sjekkLoggInn(personnummer, passord);

        // Assert
        assertEquals("OK", resultat);
        assertEquals(personnummer, session.getAttribute("Innlogget"));
    }

    @Test
    public void sjekkLoggInn_InvalidPersonnummer_ReturnsFeilIPersonnummer() {
        // Arrange
        String personnummer = "123456789"; // for kort personnummer
        String passord = "passord123";

        // Act
        String resultat = sikkerhet.sjekkLoggInn(personnummer, passord);

        // Assert
        assertEquals("Feil i personnummer", resultat);
        assertNull(session.getAttribute("Innlogget"));
    }

    @Test
    public void sjekkLoggInn_InvalidPassord_ReturnsFeilIPassord() {
        // Arrange
        String personnummer = "12345678901";
        String passord = "pass"; // for kort passord

        // Act
        String resultat = sikkerhet.sjekkLoggInn(personnummer, passord);

        // Assert
        assertEquals("Feil i passord", resultat);
        assertNull(session.getAttribute("Innlogget"));
    }

    @Test
    public void sjekkLoggInn_InvalidCredentials_ReturnsFeilIPersonnummerEllerPassord() {
        // Arrange
        String personnummer = "12345678911";
        String passord = "passord"; // for kort passord og ugyldig personnummer

        when(rep.sjekkLoggInn(anyString(),anyString())).thenReturn("Feil");

        // Act
        String resultat = sikkerhet.sjekkLoggInn(personnummer, passord);

        // Assert
        assertEquals("Feil i personnummer eller passord", resultat);
        assertNull(session.getAttribute("Innlogget"));
    }
    @Test
    public void loggUt_AttributeSetToNull() {
        // Arrange
        session.setAttribute("Innlogget", "12345678901");

        // Act
        sikkerhet.loggUt();

        // Assert
        assertNull(session.getAttribute("Innlogget"));
    }
    @Test
    public void loggInnAdmin_ValidCredentials_ReturnsLoggetInn() {
        // Arrange
        String bruker = "Admin";
        String passord = "Admin";

        // Act
        String resultat = sikkerhet.loggInnAdmin(bruker, passord);

        // Assert
        assertEquals("Logget inn", resultat);
        assertEquals("Admin", session.getAttribute("Innlogget"));
    }

    @Test
    public void loggInnAdmin_InvalidCredentials_ReturnsIkkeLoggetInn() {
        // Arrange
        String bruker = "FeilBruker";
        String passord = "FeilPassord";

        // Act
        String resultat = sikkerhet.loggInnAdmin(bruker, passord);

        // Assert
        assertEquals("Ikke logget inn", resultat);
        assertNull(session.getAttribute("Innlogget"));
    }

    @Test
    public void loggetInn_UserLoggedIn_ReturnsBruker() {
        // Arrange
        session.setAttribute("Innlogget", "Admin");

        // Act
        String resultat = sikkerhet.loggetInn();

        // Assert
        assertEquals("Admin", resultat);
    }

    @Test
    public void loggetInn_NoUserLoggedIn_ReturnsNull() {
        // Act
        String resultat = sikkerhet.loggetInn();

        // Assert
        assertNull(resultat);
    }
}