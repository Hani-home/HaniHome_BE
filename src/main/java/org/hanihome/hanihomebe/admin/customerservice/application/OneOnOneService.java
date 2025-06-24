package org.hanihome.hanihomebe.admin.customerservice.application;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsult;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsultStatus;
import org.hanihome.hanihomebe.admin.customerservice.repository.OneOnOneConsultRepository;
import org.hanihome.hanihomebe.admin.customerservice.web.dto.OneOnOneConsultCreateDTO;
import org.hanihome.hanihomebe.admin.customerservice.web.dto.OneOnOneConsultResponseDTO;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OneOnOneService {
    private final OneOnOneConsultRepository oneOnOneConsultRepository;
    private final MemberRepository memberRepository;

    // create
    @Transactional
    public OneOnOneConsultResponseDTO createOneOnOneConsult(OneOnOneConsultCreateDTO dto, Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        OneOnOneConsult oneOnOneConsult = OneOnOneConsult.create(dto.getContent(), dto.getEmail(), findMember);
        oneOnOneConsultRepository.save(oneOnOneConsult);
        return OneOnOneConsultResponseDTO.from(oneOnOneConsult);
    }

    // read
    public List<OneOnOneConsultResponseDTO> getAllOneOnOneConsult() {
        List<OneOnOneConsult> oneOnOneConsults = oneOnOneConsultRepository.findAll();

        List<OneOnOneConsultResponseDTO> dtos = oneOnOneConsults.stream()
                .map(consult -> OneOnOneConsultResponseDTO.from(consult))
                .toList();
        return dtos;
    }

    public List<OneOnOneConsultResponseDTO> getAllByStatus(OneOnOneConsultStatus status) {
        List<OneOnOneConsult> oneOnOneConsults = oneOnOneConsultRepository.findByStatus(status);

        List<OneOnOneConsultResponseDTO> dtos = oneOnOneConsults.stream()
                .map(consult -> OneOnOneConsultResponseDTO.from(consult))
                .toList();
        return dtos;
    }




    // others

    // 문의 답변 완료
    @Transactional
    public void replyByEmail(Long oneOnOneConsultId, Long repliedBy) {
        OneOnOneConsult consult = oneOnOneConsultRepository.findById(oneOnOneConsultId)
                .orElseThrow(() -> new CustomException(ServiceCode.ONE_ON_ONE_CONSULT_NOT_EXISTS));

        Member admin = memberRepository.findById(repliedBy)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        consult.repliedByEmail(admin);
    }
}
