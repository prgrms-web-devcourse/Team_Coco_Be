package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Voting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VotingRequest {

    @Size(max = Voting.TITLE_MAX_LENGTH)
    Map<Long, Boolean> votingMap;
}
