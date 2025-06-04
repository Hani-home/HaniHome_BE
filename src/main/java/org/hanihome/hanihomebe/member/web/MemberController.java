package org.hanihome.hanihomebe.member.web;

import org.hanihome.hanihomebe.member.service.MemberService;
import org.hanihome.hanihomebe.member.web.dto.MemberResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSignupRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //테스트 유정 생성 및 사용을 위한 회원가입, 로그인 => 추후 일반유저 확장한다면 그대로 써도 됨.
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberSignupRequestDTO memberSignupRequestDTO) {
        memberService.signup(memberSignupRequestDTO);
        return ResponseEntity.ok("회원가입 성공");
    }


    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long memberId) {
        MemberResponseDTO memberResponseDTO = memberService.getMemberById(memberId);
        return ResponseEntity.ok(memberResponseDTO);
    }
}
