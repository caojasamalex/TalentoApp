package com.djokic.companyservice.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EmployeeRole {
    @JsonProperty("talent_acquisition")
    TALENT_ACQUISITION,

    @JsonProperty("company_manager")
    COMPANY_MANAGER,
}
