package com.cocodan.triplan.jwt;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
public class JwtAuthentication {

    private final String token;

    private final Long id;

    public JwtAuthentication(String token, Long id) {
        checkArgument(isNotEmpty(token), "token must be provided.");
        checkArgument(id != 0, "id must be provided.");

        this.token = token;
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("token", token)
                .append("id", id)
                .toString();
    }

}