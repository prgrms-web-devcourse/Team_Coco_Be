package com.cocodan.triplan.friend.controller;

import com.cocodan.triplan.friend.dto.request.FriendRequest;
import com.cocodan.triplan.friend.service.FriendService;
import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping()
    public ResponseEntity<Void> addFollowing(
            @Valid @RequestBody FriendRequest friendRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        friendService.addFollowing(friendRequest.getMemberId(), authentication.getId());

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberSimpleResponse>> getMyFollowing(@AuthenticationPrincipal JwtAuthentication authentication) {
        List<MemberSimpleResponse> memberSimpleResponses = friendService.getMyFollowing(authentication.getId());

        return ResponseEntity.ok(memberSimpleResponses);
    }
}
