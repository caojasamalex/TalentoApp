package com.djokic.apiauthgateway.enumeration.userserviceenumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlatformRole {
    @JsonProperty("platform_superadmin")
    PLATFORM_SUPERADMIN,

    @JsonProperty("platform_admin")
    PLATFORM_ADMIN,

    @JsonProperty("platform_user")
    PLATFORM_USER
}
