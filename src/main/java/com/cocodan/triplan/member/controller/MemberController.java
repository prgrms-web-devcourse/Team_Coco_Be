package com.cocodan.triplan.member.controller;

import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.exception.common.NotIncludeException;
import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.jwt.JwtAuthenticationToken;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.request.MemberCreateRequest;
import com.cocodan.triplan.member.dto.request.MemberLoginRequest;
import com.cocodan.triplan.member.dto.response.MemberLoginResponse;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;

@Api(tags = "Member")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @ApiOperation("회원(Member) 신규 추가, 성공시 생성된 Member ID 반환")
    @PostMapping("/register")
    public ResponseEntity<MemberCreateResponse> signUp(@Valid @RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = memberService.create(
                request.getEmail(),
                request.getName(),
                request.getPhoneNumber(),
                request.getBirth(),
                request.getGender(),
                request.getNickname(),
                request.getProfileImage(),
                request.getPassword(),
                request.getGroupId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiOperation("회원(Member) 단건 조회, 성공시 Member 정보 반환")
    @GetMapping(value = "/users/{memberId}")
    public ResponseEntity<MemberGetOneResponse> readSingleData(@AuthenticationPrincipal JwtAuthentication authentication) {
        Long memberId = authentication.getId();
        if (memberId == 0)
        {
            throw new NotFoundException(Member.class, memberId);
        }
        MemberGetOneResponse response = memberService.getOne(memberId);

        return ResponseEntity.ok(response);
    }

    @ApiOperation("모든 회원(Member) 조회, 성공시 Member Page 반환")
    @GetMapping("/users")
    public ResponseEntity<Page<MemberGetOneResponse>> readPage(Pageable pageable) {
        Page<MemberGetOneResponse> responses = memberService.getAll(pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @ApiOperation("회원(Member) 단건 수정, 성공시 수정된 Member 정보 반환")
    @PutMapping(value = "/users/{memberId}")
    public ResponseEntity<MemberUpdateResponse> editInfo(@PathVariable Long memberId, @Valid @RequestBody MemberUpdateRequest request) {
        MemberUpdateResponse response = memberService.update(
                memberId,
                request.getName(),
                request.getPhoneNumber(),
                request.getNickname(),
                request.getProfileImage()
        );

        return ResponseEntity.ok(response);
    }

    @ApiOperation("회원(Member) 단건 삭제, 성공시 삭제된 Member ID 반환")
    @DeleteMapping(value = "/users/{memberId}")
    public ResponseEntity<MemberDeleteResponse> delete(@Valid @PathVariable Long memberId) {
        MemberDeleteResponse response = memberService.delete(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody MemberLoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        Member member = (Member) resultToken.getDetails();

        MemberLoginResponse response = new MemberLoginResponse(authentication.getToken(), authentication.getId(), member.getGroup().getName());

        if (response == null) throw new NotIncludeException(Member.class, Member.class, authentication.getId(), authentication.getId());
        return ResponseEntity.ok(response);
    }
}
