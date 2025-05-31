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

}
