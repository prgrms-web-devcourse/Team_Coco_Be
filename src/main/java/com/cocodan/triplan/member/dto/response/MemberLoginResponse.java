package com.cocodan.triplan.member.dto.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MemberLoginResponse {

    private final String token;

    private final Long id;

    public MemberLoginResponse(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("token", token)
                .append("id", id)
                .toString();
    }
}
