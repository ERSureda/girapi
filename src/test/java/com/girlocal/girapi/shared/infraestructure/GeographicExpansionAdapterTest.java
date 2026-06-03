package com.girlocal.girapi.shared.infraestructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeographicExpansionAdapterTest {

    private GeographicExpansionAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new GeographicExpansionAdapter();
        adapter.init();
    }

    @Test
    void shouldExpandCoverageForExistingPostalCode() {
        // Spain postal code 07001 (Palma de Mallorca, Baleares, region ES-IB)
        List<String> tags = adapter.expandCoverage("ES", "07001");

        assertNotNull(tags);
        assertEquals(5, tags.size());
        assertTrue(tags.contains("COUNTRY:ES"));
        assertTrue(tags.contains("REGION:ES-IB"));
        assertTrue(tags.contains("PROVINCE:ES-PM"));
        assertTrue(tags.contains("CITY:ES-PM-PALMA_DE_MALLORCA"));
        assertTrue(tags.contains("PC:ES-07001"));
    }

    @Test
    void shouldExpandCoverageForAnotherExistingPostalCode() {
        // Spain postal code 28001 (Madrid)
        List<String> tags = adapter.expandCoverage("ES", "28001");

        assertNotNull(tags);
        assertEquals(5, tags.size());
        assertTrue(tags.contains("COUNTRY:ES"));
        assertTrue(tags.contains("REGION:ES-MD"));
        assertTrue(tags.contains("PROVINCE:ES-M"));
        assertTrue(tags.contains("CITY:ES-M-MADRID"));
        assertTrue(tags.contains("PC:ES-28001"));
    }

    @Test
    void shouldFallbackGracefullyForNonExistingPostalCode() {
        // Non-existing postal code (e.g. 99999)
        List<String> tags = adapter.expandCoverage("ES", "99999");

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.contains("COUNTRY:ES"));
        assertTrue(tags.contains("PC:ES-99999"));
    }

    @Test
    void shouldHandleNullsGracefully() {
        List<String> nullCountry = adapter.expandCoverage(null, "28001");
        List<String> nullPostal = adapter.expandCoverage("ES", null);
        List<String> bothNull = adapter.expandCoverage(null, null);

        assertNotNull(nullCountry);
        assertTrue(nullCountry.isEmpty());

        assertNotNull(nullPostal);
        assertTrue(nullPostal.isEmpty());

        assertNotNull(bothNull);
        assertTrue(bothNull.isEmpty());
    }

    @Test
    void shouldHandleLowercaseInputsCorrectly() {
        List<String> tags = adapter.expandCoverage("es", " 28001 ");

        assertNotNull(tags);
        assertEquals(5, tags.size());
        assertTrue(tags.contains("COUNTRY:ES"));
        assertTrue(tags.contains("PC:ES-28001"));
    }
}
