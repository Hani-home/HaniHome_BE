package org.hanihome.hanihomebe.admin.customerservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.member.domain.Member;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
@Table(name = "one_on_one_consults")
@Entity
public class OneOnOneConsult {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "one_on_one_consult_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "customer_id", nullable = false)  // 문의자
    private Member customer;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "admin_id")                       // 답변자
    private Member admin;

    private String content;

    private String email;

    private OneOnOneConsultStatus status;

    public static OneOnOneConsult create(String content, String email, Member customer) {
        return OneOnOneConsult.builder()
                .content(content)
                .email(email)
                .customer(customer)
                .status(OneOnOneConsultStatus.REQUESTED)
                .build();
    }

    public void repliedByEmail(Member repliedBy) {
        this.status = OneOnOneConsultStatus.COMPLETED;
        this.admin = repliedBy;
    }
}
