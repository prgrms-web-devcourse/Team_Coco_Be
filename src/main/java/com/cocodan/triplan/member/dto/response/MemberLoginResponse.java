package com.cocodan.triplan.member.dto.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MemberLoginResponse {
    private final String token;

    private final Long id;

    private final String group;

    public MemberLoginResponse(String token, Long id, String group) {
        this.token = token;
        this.id = id;
        this.group = group;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("token", token)
                .append("id", id)
                .append("group", group)
                .toString();
    }
}
