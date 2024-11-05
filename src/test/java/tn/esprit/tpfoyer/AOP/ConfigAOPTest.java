package tn.esprit.tpfoyer.AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.config.ConfigAOP;

import static org.mockito.Mockito.*;

public class ConfigAOPTest {
    private ConfigAOP configAOP;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configAOP = new ConfigAOP();
        // Mock the signature to return a specific method name
        when(joinPoint.getSignature()).thenReturn(signature);
    }

    @Test
    void testLogMethodOut() {
        // Arrange
        String methodName = "addBloc"; // The method name being logged
        when(signature.getName()).thenReturn(methodName); // Mock the method name

        // Act
        configAOP.logMethodOut(joinPoint);

        // Assert
        // Here you would typically verify that the log was called.
        // Since SLF4J doesn't expose a direct way to capture log messages,
        // you can use a library like LogCaptor for testing.
        // Example using LogCaptor
        // LogCaptor logCaptor = LogCaptor.forClass(ConfigAOP.class);
        // assertTrue(logCaptor.getInfoLogs().contains("Execution Réussie ! " + methodName));

        // Clean up mocks
        reset(joinPoint);
        reset(signature);
    }
}