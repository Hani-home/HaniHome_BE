package org.hanihome.hanihomebe.property.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.application.PropertyMapper;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.option.OptionItem;
import org.hanihome.hanihomebe.property.domain.option.PropertyOptionItem;
import org.hanihome.hanihomebe.property.repository.OptionItemRepository;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.*;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.SharePropertyResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final PropertyMapper propertyMapper;
    private final Validator validator;
    private final OptionItemRepository optionItemRepository;

    //create
    /**
     * 1) RentProperty 생성 (부모 레포지토리로 저장)
     */
    @Transactional
    public PropertyResponseDTO createProperty(PropertyCreateRequestDTO dto){
        log.info("property 생성 로직 진입");
        Member findMember = memberRepository.findById(dto.memberId()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        if(dto instanceof RentPropertyCreateRequestDTO){

            RentProperty rentProperty = RentProperty.create((RentPropertyCreateRequestDTO) dto, findMember);

            // create PropertyOptionItem
            addPropertyOptionItem(dto.optionItemIds(), rentProperty);

            RentProperty save = propertyRepository.save(rentProperty);//RentProperty 테이블에도 JPA가 insert
            log.info("RentPrperty 생성 저장 성공");
            return propertyMapper.toResponseDto(save);
        } else if (dto instanceof SharePropertyCreateRequestDTO){
            ShareProperty shareProperty = ShareProperty.create((SharePropertyCreateRequestDTO) dto, findMember);

            // create PropertyOptionItem
            addPropertyOptionItem(dto.optionItemIds(), shareProperty);

            ShareProperty save = propertyRepository.save(shareProperty);
            log.info("SharaProperty 생성 저장 성공");
            return propertyMapper.toResponseDto(save);
        }

        throw new RuntimeException("매물 생성 실패: 해당하는 Property Subtype이 없습니다.");
    }




    // read
    /**
     * 3) 전체 Property 조회
     *    - 모든 서브타입(RentProperty, ShareProperty)을 섞어서 반환합니다.
     */
    public List<PropertyResponseDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(property -> propertyMapper.toResponseDto(property))
                .collect(Collectors.toList());
    }

    /**
     * 4) 단일 Property 조회 (부모 타입으로 조회)
     */
    public PropertyResponseDTO getPropertyById(Long id) {
        Property findProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found: " + id));

        return propertyMapper.toResponseDto(findProperty);
    }

    //update
    /* json-patch(매물이 더 복잡해질 경우 고려 가능)
    @Transactional
    public PropertyResponseDTO patch(Long propertyId, JsonNode patchDocument) throws JsonPatchException, IOException {
        Property findProperty = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found: " + propertyId));

        if(findProperty instanceof RentProperty entity) {
            // ① 스냅샷 → JsonNode
            RentPropertyPatchRequestDTO dtoSnapshot = propertyMapper.toPatchDTO(entity);
            JsonNode original = objectMapper.valueToTree(dtoSnapshot);

            // ② JSON Patch 적용
            JsonPatch patch = JsonPatch.fromJson(patchDocument);
            JsonNode patchedNode = patch.apply(original);
            RentPropertyPatchRequestDTO patchedDto =
                    objectMapper.treeToValue(patchedNode, RentPropertyPatchRequestDTO.class);   // 해당 DTO에 null값은 없음. 이유: 원본 스냅샷을 DTO로 변환한 것에 patch를 적용했으므로.
                                                                                                // 만약 remove OP를 해서 키가 없는경우, ObjectMapper는 null을 넣어준다
            // ③ (옵션) Bean Validation
//            validator.validate(patchedDto);

            // ④ 엔티티 부분 업데이트
            propertyMapper.applyPatch(patchedDto, entity);

            // ⑤ 응답 변환
            return propertyMapper.toResponseDto(entity);
        } else if (findProperty instanceof ShareProperty entity) {
            // ① 스냅샷 → JsonNode
            SharePropertyPatchRequestDTO dtoSnapshot = propertyMapper.toPatchDTO(entity);
            JsonNode original = objectMapper.valueToTree(dtoSnapshot);

            // ② JSON Patch 적용
            JsonPatch patch = JsonPatch.fromJson(patchDocument);
            JsonNode patchedNode = patch.apply(original);
            SharePropertyPatchRequestDTO patchedDto =
                    objectMapper.treeToValue(patchedNode, SharePropertyPatchRequestDTO.class);

            // ③ (옵션) Bean Validation
//            validator.validate(patchedDto);

            // ④ 엔티티 부분 업데이트
            propertyMapper.applyPatch(patchedDto, entity);

            // ⑤ 응답 변환
            return propertyMapper.toResponseDto(findProperty);
        }

        throw new RuntimeException("해당하는 매물 종류가 없습니다");
    }
    */

    @Transactional
    public PropertyResponseDTO patch(Long propertyId, PropertyPatchRequestDTO dto) {
        Property findProperty = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found: " + propertyId));

        Property updated = findProperty.update(dto);

        PropertyResponseDTO responseDTO = (updated instanceof ShareProperty) ? SharePropertyResponseDTO.from((ShareProperty) updated)
                : (updated instanceof RentProperty) ? RentPropertyResponseDTO.from((RentProperty) updated)
                : null;

        return responseDTO;
    }

    /**
     * 6) 삭제 예시
     */
    @Transactional
    public void deletePropertyById(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found: " + id);
        }
        propertyRepository.deleteById(id);
    }


    private void addPropertyOptionItem(List<Long> optionItemIds, Property property) {
        optionItemIds.forEach(optionItemId -> {
            OptionItem optionItem = optionItemRepository.findById(optionItemId).orElseThrow(() -> new RuntimeException("해당하는 선택목록 식별자가 없습니다."));
            PropertyOptionItem propertyOptionItem = PropertyOptionItem.builder()
                    .property(property)
                    .optionItem(optionItem)
                    .optionItemName(optionItem.getItemName())
                    .build();
            property.addPropertyOptionItem(propertyOptionItem);
        });
    }

}

