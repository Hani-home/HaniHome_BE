package org.hanihome.hanihomebe.item.application;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ViewingItemInitializer extends OptionItemInitializer {

    public ViewingItemInitializer(OptionCategoryRepository categoryRepository, OptionItemRepository itemRepository) {
        super(categoryRepository, itemRepository);
    }

    @Override
    void initialize() {
        initializeCancelReasonItems();  // 카테고리1: 취소 이유
        initializeViewingChecklist();   // 카테고리2: 체크리스트
    }
    /// 취소 이유
    private void initializeCancelReasonItems() {
        if(super.isAlreadyInitialized(CategoryCode.VIEWING_CAT1)) return;

        // save category
        OptionCategory optionCategory = super.saveCategory(CategoryCode.VIEWING_CAT1);

        // save items
        List<String> cancelReasons = Arrays.asList(
                "다른 매물로 결정했어요",
                "일정 변경을 원해요",
                "교통이 불편해요",
                "예산이 맞지 않아요",
                "입주 가능 날짜가 달라요",
                "기타"
        );

        cancelReasons.forEach(reason ->
                super.saveItemIfNotExists(optionCategory, reason)
        );
    }


    /// 뷰잉 체크리스트
    private void initializeViewingChecklist() {
        if(super.isAlreadyInitialized(CategoryCode.VIEWING_CAT2)) return;

        // save category
        OptionCategory checklistCategory = super.saveCategory(CategoryCode.VIEWING_CAT2);

        // save items
        // 상위 카테고리 항목들 생성
        OptionItem locationParent = super.saveItemIfNotExists(checklistCategory, "위치 및 생활 인프라");
        OptionItem contractParent = super.saveItemIfNotExists(checklistCategory, "계약 조건 및 공과금 포함 여부");
        OptionItem housebaseParent = super.saveItemIfNotExists(checklistCategory, "집 구조 및 상태");
        OptionItem furnitureParent = super.saveItemIfNotExists(checklistCategory, "가구 및 가전 상태");

        // 각 상위 카테고리 별 하위 아이템 추가
        initializeLocationItems(checklistCategory, locationParent);
        initializeContractItems(checklistCategory, contractParent);
        initializeStructureItems(checklistCategory, housebaseParent);
        initializeFurnitureItems(checklistCategory, furnitureParent);
    }

    /// 위치 및 생활 인프라
    private void initializeLocationItems(OptionCategory category, OptionItem parent) {
        List<String> locationItems = Arrays.asList(
            "대중교통 접근성 (역/버스 정류장 도보 10분 이내)",
            "주변 대형 마트 또는 편의점 있음",
            "근처 병원, 약국 있음",
            "동네 치안 양호 (밤길 안전)",
            "Asian-Friendly 상권 있음 (아시안 마트, 음식점 등)"
        );

        locationItems.forEach(item -> 
            super.saveParentChildItems(category, parent.getItemName(), Arrays.asList(item))
        );
    }

    /// 계약 조건 및 공과금 포함 여부
    private void initializeContractItems(OptionCategory category, OptionItem parent) {
        List<String> contractItems = Arrays.asList(
            "전기요금 포함",
            "수도요금 포함",
            "가스요금 포함",
            "인터넷 포함",
            "무보인, 무보증 수수료 없음",
            "주차 공간 포함",
            "반려동물 허용"
        );

        contractItems.forEach(item -> 
            super.saveParentChildItems(category, parent.getItemName(), Arrays.asList(item))
        );
    }

    /// 집 구조 및 상태
    private void initializeStructureItems(OptionCategory category, OptionItem parent) {
        List<String> structureItems = Arrays.asList(
            "창문 열림 가능",
            "창문 방향, 채광 양호 (햇빛 잘 듦)",
            "수압 양호 (샤워기 및 세면대)",
            "곰팡이 흔적 없음",
            "누수 흔적 없음",
            "24시간 온수 사용 가능",
            "블라인드 및 커튼 설치되어 있음",
            "바닥 상태 양호 (스크래치 없음, 카펫 청결)"
        );

        structureItems.forEach(item -> 
            super.saveParentChildItems(category, parent.getItemName(), Arrays.asList(item))
        );
    }

    /// 가구 및 가전 상태
    private void initializeFurnitureItems(OptionCategory category, OptionItem parent) {
        List<String> furnitureItems = Arrays.asList(
            "냉장고 제공",
            "세탁기 제공",
            "건조기 제공",
            "오븐 제공",
            "전자레인지 제공",
            "에어컨 작동 가능",
            "히터 작동 가능",
            "기본 가구(침대, 책상, 의자 등) 포함"
        );

        furnitureItems.forEach(item -> 
            super.saveParentChildItems(category, parent.getItemName(), Arrays.asList(item))
        );
    }
}