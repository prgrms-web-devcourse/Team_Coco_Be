package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoRequest {

    @Length(min = Memo.TITLE_MIN_LENGTH, max = Memo.TITLE_MAX_LENGTH)
    private String title;

    @Length(min = Memo.CONTENT_MIN_LENGTH, max = Memo.CONTENT_MAX_LENGTH)
    private String content;
}
