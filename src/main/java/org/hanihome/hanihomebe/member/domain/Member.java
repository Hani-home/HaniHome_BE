package org.hanihome.hanihomebe.member.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.global.BaseEntity;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.web.dto.ConsentAgreementDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberCompleteProfileRequestDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberUpdateRequestDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.verification.domain.Verification;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String name;

    @Column(length = 30)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "social_provider", length=20)
    private String socialProvider;

    @Column(name = "google_id", length=100, unique = true)
    private String googleId;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone_number" ,length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "profile_image", length = 1000)
    private String profileImage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Verification> verifications = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishItem> wishItems = new ArrayList<>();

    // 관심지역 필드 일단 스트링으로 해두겠음. 추후 region 클래스 생기면 수정할게요
    @Column(name = "interest_region")
    private String interestRegion;

    //회원가입 여부 확인용
    @Column(nullable = false)
    private boolean isRegistered = false;

    //동의 내역
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consent> consents = new ArrayList<>();

    //광고마케팅 동의는 따로 boolean으로 빼도 좋을 듯. 알림 보낼 때 자주쓰니까


    public static Member createFromGoogleSignUp(String email, String googleId) {
        return Member.builder()
                .email(email)
                .password("GOOGLE")
                .socialProvider("Google")
                .googleId(googleId)
                .role(Role.GUEST)
                .isRegistered(false)
                .build();
    }

    public static Member createFrom(String email, String password, Role role) {
        return Member.builder()
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    //유저 정보 업데이트
    public void updateMember(MemberUpdateRequestDTO memberUpdateRequestDTO) {
        if (memberUpdateRequestDTO.getName() != null) this.name = memberUpdateRequestDTO.getName();
        if (memberUpdateRequestDTO.getNickname() != null) this.nickname = memberUpdateRequestDTO.getNickname();
        if (memberUpdateRequestDTO.getBirthDate() != null) this.birthDate = memberUpdateRequestDTO.getBirthDate();
        if (memberUpdateRequestDTO.getPhoneNumber() != null) this.phoneNumber = memberUpdateRequestDTO.getPhoneNumber();
        if (memberUpdateRequestDTO.getGender() != null) this.gender = memberUpdateRequestDTO.getGender();
        if (memberUpdateRequestDTO.getProfileImage() != null) this.profileImage = memberUpdateRequestDTO.getProfileImage();
    }

    public void completeProfile(MemberCompleteProfileRequestDTO dto) {
        if (this.isRegistered) {
            throw new CustomException(ServiceCode.MEMBER_ALREADY_REGISTERED);
        }

        // 프로필 정보 설정
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
        this.nickname = dto.getNickname();
        this.gender = dto.getGender();
        this.interestRegion = dto.getInterestRegion();
        this.profileImage = dto.getProfileImage();

    }

    public void markAsRegistered() {
        this.isRegistered = true;
    }

    //WishItem 추가, 제거 메서드
    public void addWishItem(WishItem wishItem) {
        //중복 검사(DB 제약으로도 설정하면 좋을 것 같음)
        /*
        stream: 리스트 모든 요소 순회
        anyMatch: 하나라도 있으면 true, 아니면 false

        고민: O(n) 시간 복잡도를 가짐. DB에서 바로 확인하도록하면 O(1)이나 O(logn) 까지는 할 수 있을 거같음
        근데 너무 과한가...? 즐겨찾기를 엄청 많이 할 경우는 적을 거 같네...
         */
        boolean alreadyWished = this.wishItems.stream()
                .anyMatch(wish ->
                                        wish.getTargetType()==wishItem.getTargetType() &&
                                        wish.getTargetId().equals(wishItem.getTargetId())
                );

        if(alreadyWished){
            throw new CustomException(ServiceCode.ALEADY_WISH_EXISTS);
        }

        this.wishItems.add(wishItem);
    }

    public void removeWishItem(WishTargetType targetType, Long targetId) {
        this.wishItems.removeIf(wish ->
                wish.getTargetType() == targetType &&
                        wish.getTargetId().equals(targetId)
        );
    }
}
