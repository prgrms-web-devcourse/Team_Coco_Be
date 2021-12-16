package com.cocodan.triplan.member.dto.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MemberLoginRequest {

    private String email;

    private String password;

    protected MemberLoginRequest() {}

    public MemberLoginRequest(String principal, String credentials) {
        this.email = principal;
        this.password = credentials;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("principal", email)
                .append("credentials", password)
                .toString();
    }
}
