package org.hanihome.hanihomebe.temporaryProperty.application;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryRentProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryShareProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.item.TemporaryPropertyOptionItem;
import org.hanihome.hanihomebe.temporaryProperty.repository.TemporaryPropertyOptionItemRepository;
import org.hanihome.hanihomebe.temporaryProperty.repository.TemporaryPropertyRepository;
import org.hanihome.hanihomebe.temporaryProperty.repository.TemporaryRentPropertyRepository;
import org.hanihome.hanihomebe.temporaryProperty.repository.TemporarySharePropertyRepository;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.response.TemporaryPropertyListResponseDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryRentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporarySharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.response.TemporaryPropertyResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TemporaryPropertyService {

    private final MemberRepository memberRepository;

    private final TemporaryPropertyRepository temporaryPropertyRepository;
    private final TemporaryRentPropertyRepository temporaryRentPropertyRepository;
    private final TemporarySharePropertyRepository temporarySharePropertyRepository;
    private final TemporaryPropertyOptionItemRepository temporaryPropertyOptionItemRepository;


    private final OptionItemRepository optionItemRepository;


    //생성
    public void createTemporaryProperty(Long hostId, TemporaryPropertyCreateRequestDTO dto) {

        //해당 멤버 존재하는지 확인
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));


        /* 개수 제한은 일단 보류
        int savedCount = temporaryPropertyRepository.countByMember(host);
        if(savedCount >= 3) {
            throw new CustomException(ServiceCode.TEMPORARY_LIMIT_EXCEEDED);
        }
         */
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
    }

    //수정
    public void updateTemporaryProperty(Long hostId, TemporaryPropertyCreateRequestDTO dto) {
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        if(dto.id() == null) {
            throw new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_EXISTS);
        }



        TemporaryProperty temporaryProperty;


        if (dto.kind() == PropertySuperType.RENT) {
            TemporaryRentPropertyCreateRequestDTO rentDto = (TemporaryRentPropertyCreateRequestDTO) dto;

            TemporaryRentProperty temporaryRentProperty = temporaryRentPropertyRepository.findById(rentDto.id())
                    .orElseThrow(() -> new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_EXISTS));


            if (!temporaryRentProperty.getMember().getId().equals(hostId)) {
                throw new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_BELONGS_TO_YOU);
            }


            // 실제 필드 업데이트
            temporaryRentProperty.update(rentDto); // 이거 아래에서 설명할게

            // 옵션 아이템 덮어쓰기
            temporaryRentProperty.getOptionItems().clear();
            temporaryPropertyOptionItemRepository.deleteByTemporaryProperty(temporaryRentProperty); // 기존 삭제
            if (rentDto.optionItemIds() != null) {
                saveNewTemporaryPropertyOptionItems(rentDto.optionItemIds(), temporaryRentProperty);

            }

            temporaryRentPropertyRepository.save(temporaryRentProperty);

        } else if (dto.kind() == PropertySuperType.SHARE) {
            TemporarySharePropertyCreateRequestDTO shareDto = (TemporarySharePropertyCreateRequestDTO) dto;

            TemporaryShareProperty temporaryShareProperty = temporarySharePropertyRepository.findById(shareDto.id())
                    .orElseThrow(() -> new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_EXISTS));


            if (!temporaryShareProperty.getMember().getId().equals(hostId)) {
                throw new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_BELONGS_TO_YOU);
            }

            temporaryShareProperty.update(shareDto);

            temporaryShareProperty.getOptionItems().clear();
            temporaryPropertyOptionItemRepository.deleteByTemporaryProperty(temporaryShareProperty);
            if (shareDto.optionItemIds() != null) {
                saveNewTemporaryPropertyOptionItems(shareDto.optionItemIds(), temporaryShareProperty);
            }

            temporarySharePropertyRepository.save(temporaryShareProperty);
        }
    }

    private void addTemporaryPropertyOptionItem(List<Long> optionItemIds, TemporaryProperty temporaryProperty) {
        optionItemIds.forEach(optionItemId -> {
            OptionItem optionItem = optionItemRepository.findById(optionItemId).orElseThrow(() -> new CustomException(ServiceCode.OPTION_ITEM_NOT_EXISTS));

            TemporaryPropertyOptionItem temporaryPropertyOptionItem = TemporaryPropertyOptionItem.builder()
                    .temporaryProperty(temporaryProperty)
                    .optionItem(optionItem)
                    .optionItemName(optionItem.getItemName())
                    .build();
            temporaryProperty.addTemporaryPropertyOptionItem(temporaryPropertyOptionItem);
        });
    }

    private void saveNewTemporaryPropertyOptionItems(List<Long> optionItemIds, TemporaryProperty temporaryProperty) {
        List<TemporaryPropertyOptionItem> newItems = optionItemIds.stream()
                .map(optionItemId -> {
                    OptionItem optionItem = optionItemRepository.findById(optionItemId)
                            .orElseThrow(() -> new CustomException(ServiceCode.OPTION_ITEM_NOT_EXISTS));
                    return TemporaryPropertyOptionItem.builder()
                            .temporaryProperty(temporaryProperty)
                            .optionItem(optionItem)
                            .optionItemName(optionItem.getItemName())
                            .build();
                })
                .toList();

        temporaryPropertyOptionItemRepository.saveAll(newItems);
    }


    //전체 조회
    public List<TemporaryPropertyListResponseDTO> getTemporaryProperties(Long hostId) {
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<TemporaryProperty> temporaryProperties = temporaryPropertyRepository.findAllByMember(host);


        return temporaryProperties.stream()
                .sorted(Comparator.comparing(TemporaryProperty::getCreatedAt).reversed()) //최신순 정렬
                .map(property -> new TemporaryPropertyListResponseDTO(
                        property.getId(),
                        property.getStatus(),
                        property.getCreatedAt()
                ))
                .toList();
    }

    //상세조회
    public TemporaryPropertyResponseDTO getTemporaryProperty(Long hostId, Long propertyId) {
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        TemporaryProperty temporaryProperty = temporaryPropertyRepository.findByIdAndMember(propertyId, host)
                .orElseThrow(() -> new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_EXISTS));

        return temporaryProperty.toResponseDTO();


    }


    public void deleteTemporaryProperty(Long hostId, Long temporaryPropertyId) {

        //걍 temporayPropertyRepository.findByIdAndMemberId() 해서 한 번에 가는 게 나으러나
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        TemporaryProperty temporaryProperty = temporaryPropertyRepository.findByIdAndMember(temporaryPropertyId, host)
                .orElseThrow(() -> new CustomException(ServiceCode.TEMPORARY_PROPERTY_NOT_EXISTS));

        temporaryPropertyRepository.delete(temporaryProperty);
    }
}
