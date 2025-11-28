package com.djokic.companyrequestservice.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CompanyRequestStatus {
    @JsonProperty("pending")
    PENDING,

    @JsonProperty("approved")
    APPROVED,

    @JsonProperty("rejected")
    REJECTED
}
