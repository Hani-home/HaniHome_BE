package org.hanihome.hanihomebe.temporaryProperty.application;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;
import org.hanihome.hanihomebe.temporaryProperty.application.validator.StepValidationManager;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryRentProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryShareProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.item.TemporaryPropertyOptionItem;
import org.hanihome.hanihomebe.temporaryProperty.repository.TemporaryPropertyRepository;
import org.hanihome.hanihomebe.temporaryProperty.repository.TemporaryRentPropertyRepository;
import org.hanihome.hanihomebe.temporaryProperty.repository.TemporarySharePropertyRepository;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ConditionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ContractDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.DetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryRentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporarySharePropertyCreateRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class TemporaryPropertyService {

    private final MemberRepository memberRepository;
    private final StepValidationManager stepValidationManager;

    private final TemporaryPropertyRepository temporaryPropertyRepository;
    private final TemporaryRentPropertyRepository temporaryRentPropertyRepository;
    private final TemporarySharePropertyRepository temporarySharePropertyRepository;

    private final OptionItemRepository optionItemRepository;



    public void temporaryPropertyCheckAndSave(Long hostId, TemporaryPropertyCreateRequestDTO dto) {

        //해당 멤버 존재하는지 확인
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));


        /* 개수 제한은 일단 보류
        int savedCount = temporaryPropertyRepository.countByMember(host);
        if(savedCount >= 3) {
            throw new CustomException(ServiceCode.TEMPORARY_LIMIT_EXCEEDED);
        }
         */


        //검증

        //stepValidationManager.validateUpToStep(dto);

        //생성 or update. 으악 근데 if-else가 너무 많아....

        TemporaryProperty temporaryProperty;

        if(dto.kind() == PropertySuperType.RENT) {
            TemporaryRentPropertyCreateRequestDTO rentDto = (TemporaryRentPropertyCreateRequestDTO) dto;
            temporaryProperty = TemporaryRentProperty.create(rentDto, host);
            temporaryRentPropertyRepository.save((TemporaryRentProperty) temporaryProperty);

            //수정해야함
            if (dto.optionItemIds() != null) {
                addTemporaryPropertyOptionItem(dto.optionItemIds(), temporaryProperty);
            }



        } else if (dto.kind() == PropertySuperType.SHARE) {
            TemporarySharePropertyCreateRequestDTO shareDto = (TemporarySharePropertyCreateRequestDTO) dto;
            temporaryProperty  = TemporaryShareProperty.create(shareDto, host);
            temporarySharePropertyRepository.save((TemporaryShareProperty) temporaryProperty);

            //여기도 옵션아이템
            if (dto.optionItemIds() != null) {
                addTemporaryPropertyOptionItem(dto.optionItemIds(), temporaryProperty);
            }


        }

        /*
            TemporaryProperty temporaryProperty;

            if(dto.getKind() == PropertySuperType.RENT) {
                temporaryProperty = TemporaryRentProperty.create(dto, host);
                temporaryRentPropertyRepository.save((TemporaryRentProperty) temporaryProperty);

                addTemporaryPropertyOptionItem(dto, temporaryProperty);

            } else if (dto.getKind() == PropertySuperType.SHARE) {
                //
                temporaryProperty  = TemporaryShareProperty.create(dto, host);
                temporarySharePropertyRepository.save((TemporaryShareProperty) temporaryProperty);
                //여기도 옵션아이템
                addTemporaryPropertyOptionItem(dto, temporaryProperty);
            }
        }
         */

    }

    //OptionItem 우야노
    private void addTemporaryPropertyOptionItem(List<Long> optionItemIds, TemporaryProperty temporaryProperty) {
        optionItemIds.forEach(optionItemId -> {
            OptionItem optionItem = optionItemRepository.findById(optionItemId).orElseThrow(() -> new RuntimeException("해당하는 선택목록 식별자가 없습니다."));

            TemporaryPropertyOptionItem temporaryPropertyOptionItem = TemporaryPropertyOptionItem.builder()
                    .temporaryProperty(temporaryProperty)
                    .optionItem(optionItem)
                    .optionItemName(optionItem.getItemName())
                    .build();
            temporaryProperty.addTemporaryPropertyOptionItem(temporaryPropertyOptionItem);
        });
    }

    /*
    private void addTemporaryPropertyOptionItem(TemporaryPropertyCreateRequestDTO dto, TemporaryProperty temporaryProperty) {
        //여기서 위에 주석에 있는 애들 데리고 와서 TemporaryPropertyOptionItem 테이블에 넣어야함
        List<Long> optionItemIds = new ArrayList<>();
        //2단계

        //중복이 생길 수 있을까...?
        Set<Long> uniqueIds = new HashSet<>(optionItemIds);
        List<OptionItem> optionItems = optionItemRepository.findAllById(uniqueIds);

        optionItems.forEach(optionItem -> {
            TemporaryPropertyOptionItem propertyOptionItem = TemporaryPropertyOptionItem.builder()
                    .temporaryProperty(temporaryProperty)
                    .optionItem(optionItem)
                    .optionItemName(optionItem.getItemName())
                    .build();
            temporaryProperty.addTemporaryPropertyOptionItem(propertyOptionItem);
        });
    }
     */

    public void deleteTemporaryProperty(Long hostId, Long temporaryPropertyId) {

        //걍 temporayPropertyRepository.findByIdAndMemberId() 해서 한 번에 가는 게 나으러나
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        TemporaryProperty temporaryProperty = temporaryPropertyRepository.findByIdAndMember(temporaryPropertyId, host)
                .orElseThrow(() -> new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_EXISTS));

        temporaryPropertyRepository.delete(temporaryProperty);
    }
}
