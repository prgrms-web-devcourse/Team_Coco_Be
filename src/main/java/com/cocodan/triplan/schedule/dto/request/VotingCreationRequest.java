package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Voting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VotingCreationRequest {

    @Length(min = Voting.MIN_LENGTH, max = Voting.MAX_LENGTH)
    private String title;

    @Size(min = Voting.CONTENT_MIN, max = Voting.CONTENT_MAX)
    private List<String> contents;

    private boolean multipleFlag;
}
