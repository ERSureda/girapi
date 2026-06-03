package com.girlocal.girapi.shared.application.port.in;

import java.util.List;

public interface GeographicExpansionPort {
    List<String> expandCoverage(String countryCode, String postalCode);
}
