package com.cocodan.triplan.member.controller;

import com.cocodan.triplan.common.ApiResponse;
import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.jwt.JwtAuthenticationToken;
import com.cocodan.triplan.member.dto.request.MemberCreateRequest;
import com.cocodan.triplan.member.dto.request.MemberLoginRequest;
import com.cocodan.triplan.member.dto.request.MemberUpdateRequest;
import com.cocodan.triplan.member.dto.response.*;
import com.cocodan.triplan.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "Member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    private static final long GROUP_ID = 1L;

    @ApiOperation("회원(Member) 신규 추가, 성공시 생성된 Member ID 반환")
    @PostMapping("/register")
    public ApiResponse<MemberCreateResponse> signUp(@Valid @RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = memberService.validCreate(
                request.getEmail(),
                request.getName(),
                request.getBirth(),
                request.getGender(),
                request.getNickname(),
                request.getProfileImage(),
                request.getPassword(),
                GROUP_ID
        );
        return ApiResponse.ok(response);
    }

    @ApiOperation("회원(Member) 단건 조회, 성공시 Member 정보 반환")
    @GetMapping(value = "/users/{memberId}")
    public ApiResponse<MemberGetOneResponse> readSingleData(@PathVariable Long memberId, @AuthenticationPrincipal JwtAuthentication authentication) {
        MemberGetOneResponse response = memberService.getOne(memberId);
        return ApiResponse.ok(response);
    }

    @ApiOperation("닉네임으로 회원 조회")
    @GetMapping("/users")
    public ApiResponse<List<MemberSimpleResponse>> findMemberByNickname(@RequestParam String nickname) {
        List<MemberSimpleResponse> responses = memberService.findMemberByNickname(nickname);

        return ApiResponse.ok(responses);
    }

    @ApiOperation("회원(Member) 단건 수정, 성공시 수정된 Member 정보 반환")
    @PutMapping(value = "/users/{memberId}")
    public ApiResponse<MemberUpdateResponse> editInfo(@PathVariable Long memberId, @Valid @RequestBody MemberUpdateRequest request) {
        MemberUpdateResponse response = memberService.update(
                memberId,
                request.getName(),
                request.getNickname(),
                request.getProfileImage()
        );
        return ApiResponse.ok(response);
    }

    @ApiOperation("회원(Member) 단건 삭제, 성공시 삭제된 Member ID 반환")
    @DeleteMapping(value = "/users/{memberId}")
    public ApiResponse<MemberDeleteResponse> delete(@Valid @PathVariable Long memberId) {
        MemberDeleteResponse response = memberService.delete(memberId);
        return ApiResponse.ok(response);
    }

    @PostMapping(path = "/login")
    public ApiResponse<MemberLoginResponse> login(@RequestBody MemberLoginRequest request, HttpServletResponse httpServletResponse) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        httpServletResponse.setHeader("token", authentication.getToken());
        MemberLoginResponse response = new MemberLoginResponse(authentication.getToken(), authentication.getId());
        return ApiResponse.ok(response);
    }

    @ApiOperation("내 프로필 조회")
    @GetMapping("/profiles")
    public ApiResponse<MemberGetOneResponse> readProfile(@AuthenticationPrincipal JwtAuthentication authentication) {
        Long memberId = authentication.getId();
        MemberGetOneResponse response = memberService.getOne(memberId);
        return ApiResponse.ok(response);
    }
}
