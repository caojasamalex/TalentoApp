package com.djokic.userservice.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlatformRole {

    @JsonProperty("platform_admin")
    PLATFORM_ADMIN,

    @JsonProperty("platform_user")
    PLATFORM_USER
}
