package com.girlocal.girapi.shared.infraestructure;

import com.girlocal.girapi.shared.application.port.in.GeographicExpansionPort;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class GeographicExpansionAdapter implements GeographicExpansionPort {

    private static final Pattern CSV_SPLIT_PATTERN = Pattern.compile(",");
    private static final String DEFAULT_FILE_PATH = "/geographic_data.csv";

    private Map<String, List<String>> geoDatabase = Map.of();

    @PostConstruct
    public void init() {
        loadCsvData(DEFAULT_FILE_PATH);
    }

    private void loadCsvData(String filePath) {
        log.info("Loading geographic database from: {}", filePath);
        Map<String, List<String>> tempDatabase = new HashMap<>();

        try (var inputStream = getClass().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                log.warn("Geographic data file not found: {}", filePath);
                return;
            }

            try (var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                boolean isHeader = true;

                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) {
                        continue;
                    }
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }

                    String[] parts = CSV_SPLIT_PATTERN.split(line);
                    if (parts.length >= 5) {
                        String country = parts[0].trim().toUpperCase().intern();
                        String postalCode = parts[1].trim().intern();
                        String region = parts[2].trim().toUpperCase().intern();
                        String province = parts[3].trim().toUpperCase().intern();
                        String city = parts[4].trim();

                        String cacheKey = buildKey(country, postalCode);

                        // Safely split province to avoid IndexOutOfBoundsException
                        String provincePart;
                        if (province.contains("-")) {
                            String[] provinceSplit = province.split("-");
                            if (provinceSplit.length > 1) {
                                provincePart = provinceSplit[1].trim();
                            } else {
                                provincePart = province;
                            }
                        } else {
                            provincePart = province;
                        }

                        String cityPart = city.replace(" ", "_").toUpperCase();

                        // Build and intern tag strings to optimize memory footprint
                        List<String> tags = List.of(
                                ("COUNTRY:" + country).intern(),
                                ("REGION:" + region).intern(),
                                ("PROVINCE:" + province).intern(),
                                ("CITY:" + country + "-" + provincePart + "-" + cityPart).intern(),
                                ("PC:" + country + "-" + postalCode).intern()
                        );

                        tempDatabase.put(cacheKey, tags);
                    }
                }
            }
            this.geoDatabase = Map.copyOf(tempDatabase);
            log.info("Geographic database loaded successfully with {} entries.", geoDatabase.size());
        } catch (Exception e) {
            log.error("Failed to load geographic data from {}", filePath, e);
        }
    }

    private String buildKey(String countryCode, String postalCode) {
        if (countryCode == null || postalCode == null) {
            return "";
        }
        return countryCode.trim().toUpperCase() + ":" + postalCode.trim();
    }

    @Override
    public List<String> expandCoverage(String countryCode, String postalCode) {
        if (countryCode == null || postalCode == null) {
            return List.of();
        }

        String key = buildKey(countryCode, postalCode);
        List<String> tags = geoDatabase.get(key);
        if (tags != null) {
            return tags;
        }

        // High performance fallback with minimal allocations
        String cleanCountry = countryCode.trim().toUpperCase();
        String cleanPostalCode = postalCode.trim();

        return List.of(
                ("COUNTRY:" + cleanCountry).intern(),
                ("PC:" + cleanCountry + "-" + cleanPostalCode).intern()
        );
    }
}
