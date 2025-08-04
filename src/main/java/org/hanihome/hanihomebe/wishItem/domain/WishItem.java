package org.hanihome.hanihomebe.wishItem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hanihome.hanihomebe.member.domain.Member;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class WishItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable=false)
    protected Member member;

    @Column(nullable=false)
    protected LocalDateTime createdAt = LocalDateTime.now();
}
