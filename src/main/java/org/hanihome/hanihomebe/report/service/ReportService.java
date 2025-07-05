package org.hanihome.hanihomebe.report.service;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.report.application.ReportHandler;
import org.hanihome.hanihomebe.report.application.domain.Report;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;
import org.hanihome.hanihomebe.report.web.dto.ReportRequestDTO;
import org.hanihome.hanihomebe.report.web.dto.ReportResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {


    private final MemberRepository memberRepository;
    private final Map<ReportTargetType, ReportHandler> reportHandlerMap;

    @Autowired
    public ReportService(List<ReportHandler> handlers, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.reportHandlerMap = handlers.stream()
                .collect(Collectors.toMap(ReportHandler::getTargetType, h-> h));
    }

    //생성
    public ReportResponseDTO createReport(Long reporterId,ReportRequestDTO dto) {
        Member reporter = memberRepository.findById(reporterId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        //해당 타입의 해당 id가 존재하는지 validate하는 거 만들어야함.

        ReportTargetType targetType = dto.getTargetType();
        ReportHandler handler = reportHandlerMap.get(targetType);

        if(!handler.validate(dto)) {
            throw new CustomException(ServiceCode.INVALID_TARGET_TYPE);
        }
        Report report = handler.createAndSave(reporter, dto);


        return ReportResponseDTO.from(report.getTargetType(), report.getTargetId(), report);
    }




}
