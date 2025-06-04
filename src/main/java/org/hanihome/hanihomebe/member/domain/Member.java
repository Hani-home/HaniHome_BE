package org.hanihome.hanihomebe.member.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.member.web.dto.MemberUpdateRequestDTO;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(length = 10)
    private String gender;

    @Column(name = "profile_image", length = 1000)
    private String profileImage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at" )
    private LocalDateTime updatedAt;

    public static Member createFromGoogleSignUp(String email, String googleId) {
        return Member.builder()
                .email(email)
                .password("GOOGLE")
                .socialProvider("Google")
                .googleId(googleId)
                .role(Role.GUEST)
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
}
