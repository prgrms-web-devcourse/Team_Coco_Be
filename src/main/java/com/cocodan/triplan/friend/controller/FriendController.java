package com.cocodan.triplan.friend.controller;

import com.cocodan.triplan.friend.dto.request.FriendRequest;
import com.cocodan.triplan.friend.service.FriendService;
import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api("친구")
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    @ApiOperation("친구 추가")
    @PostMapping()
    public ResponseEntity<Void> addFollowing(
            @Valid @RequestBody FriendRequest friendRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        friendService.addFollowing(friendRequest.getMemberId(), authentication.getId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation("나의 친구 목록")
    @GetMapping
    public ResponseEntity<List<MemberSimpleResponse>> getMyFollowing(@AuthenticationPrincipal JwtAuthentication authentication) {
        List<MemberSimpleResponse> memberSimpleResponses = friendService.getMyFollowing(authentication.getId());

        return ResponseEntity.ok(memberSimpleResponses);
    }
}
