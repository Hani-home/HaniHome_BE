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
import org.hanihome.hanihomebe.global.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String name;

    @Column(length = 30)
    private String nickname;

    @Column(length = 100, nullable = true) //TODO: 지금 당장은 password가 필수 값은 아니니까 일단 nullable 허용하겠습니다
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

}
