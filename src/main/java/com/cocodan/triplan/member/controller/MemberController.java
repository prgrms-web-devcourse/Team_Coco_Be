package com.cocodan.triplan.member.controller;

import com.cocodan.triplan.member.dto.request.MemberCreateRequest;
import com.cocodan.triplan.member.dto.request.MemberUpdateRequest;
import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.dto.response.MemberDeleteResponse;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.member.dto.response.MemberUpdateResponse;
import com.cocodan.triplan.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;

@Api(tags = "Member")
@RestController
@RequestMapping(value = "/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ApiOperation("회원(Member) 신규 추가, 성공시 생성된 Member ID 반환")
    @PostMapping()
    public ResponseEntity<MemberCreateResponse> signUp(@Valid @RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = memberService.create(
                request.getEmail(),
                request.getName(),
                request.getPhoneNumber(),
                request.getBirth(),
                request.getGender(),
                request.getNickname(),
                request.getProfileImage()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("회원(Member) 단건 조회, 성공시 Member 정보 반환")
    @GetMapping(value = "/{memberId}")
    public ResponseEntity<MemberGetOneResponse> readSingleData(@PathVariable Long memberId) {
        MemberGetOneResponse response = memberService.getOne(memberId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("모든 회원(Member) 조회, 성공시 Member Page 반환")
    @GetMapping()
    public ResponseEntity<Page<MemberGetOneResponse>> readPage(Pageable pageable) {
        Page<MemberGetOneResponse> responses = memberService.getAll(pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @ApiOperation("회원(Member) 단건 수정, 성공시 수정된 Member 정보 반환")
    @PutMapping(value = "/{memberId}")
    public ResponseEntity<MemberUpdateResponse> editInfo(@PathVariable Long memberId, @Valid @RequestBody MemberUpdateRequest request) {
        MemberUpdateResponse response = memberService.update(
                memberId,
                request.getName(),
                request.getPhoneNumber(),
                request.getNickname(),
                request.getProfileImage()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation("회원(Member) 단건 삭제, 성공시 삭제된 Member ID 반환")
    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<MemberDeleteResponse> delete(@Valid @PathVariable Long memberId) {
        MemberDeleteResponse response = memberService.delete(memberId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
