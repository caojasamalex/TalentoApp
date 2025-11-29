package com.djokic.companyservice.enumeration.companyrequestserviceenumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CompanyRequestStatus {
    @JsonProperty("pending")
    PENDING,

    @JsonProperty("approved")
    APPROVED,

    @JsonProperty("rejected")
    REJECTED
}
