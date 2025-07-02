package org.hanihome.hanihomebe.member.web;

import org.hanihome.hanihomebe.member.service.MemberService;
import org.hanihome.hanihomebe.member.web.dto.MemberCompleteProfileRequestDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberNicknameCheckResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSignupRequestDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberUpdateRequestDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.security.auth.user.detail.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public MemberController(MemberService memberService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.memberService = memberService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    //테스트 유정 생성 및 사용을 위한 회원가입, 로그인 => 추후 일반유저 확장한다면 그대로 써도 됨.
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberSignupRequestDTO memberSignupRequestDTO) {
        memberService.signup(memberSignupRequestDTO);
        return ResponseEntity.ok("회원가입 성공");
    }


    //내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDTO> getMyMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getUserId();
        MemberResponseDTO memberResponseDTO = memberService.getMemberById(memberId); //요정도는 재사용해도 되겠죠..? id로 조회하는 거랑 service가 같아서..
        return ResponseEntity.ok(memberResponseDTO);
    }

    //유저 정보 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long memberId) {
        MemberResponseDTO memberResponseDTO = memberService.getMemberById(memberId);
        return ResponseEntity.ok(memberResponseDTO);
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<?> completeProfile(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MemberCompleteProfileRequestDTO dto) {
        memberService.completeProfile(userDetails.getUserId(), dto);
        return ResponseEntity.ok("회원 등록 성공");
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<MemberNicknameCheckResponseDTO> checkNickname(@AuthenticationPrincipal CustomUserDetails userDetails , @RequestParam String nickname) {
        MemberNicknameCheckResponseDTO dto = memberService.checkNickname(nickname);
        return ResponseEntity.ok(dto);
    }

    /*
    지금은 멤버 아이디만 있으면 다 가져올 수 있지만,
    나중엔 memberId와 userDetails에 담긴 id와 비교하여 자신의 정보만 수정할 수 있게 수정
    주석을 통해 구현
     */
    //프로필 수정
    @PatchMapping("/me")
    public ResponseEntity<?> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO) {

        Long memberId = userDetails.getUserId();

        memberService.updateMember(memberId, memberUpdateRequestDTO);
        return ResponseEntity.ok("회원정보가 수정되었습니다.");
    }

    /*
    이것도 updateMember와 마찬가지로 추후에 자신의 계정만 삭제할 수 있게 수정하겠습니다
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getUserId();


        memberService.deleteMember(memberId);
        return ResponseEntity.ok("회원 삭제 처리 되었습니다.");
    }
}
