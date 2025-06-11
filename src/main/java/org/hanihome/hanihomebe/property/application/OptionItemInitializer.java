/*
package org.hanihome.hanihomebe.property.application;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.domain.option.CategoryCode;
import org.hanihome.hanihomebe.property.domain.option.OptionCategory;
import org.hanihome.hanihomebe.property.domain.option.OptionItem;
import org.hanihome.hanihomebe.property.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.property.repository.OptionItemRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
*/
/** NOT USED
 * Custom
 * *//*

@RequiredArgsConstructor
@Component
public class OptionItemInitializer {
    private final OptionItemRepository itemRepository;
    private final OptionCategoryRepository categoryRepository;

    @Transactional
    @PostConstruct
    public void init() {
        */
/**
         * 공통항목
         * *//*

        /// 빌에 포함된 항목
        OptionCategory categoryListIncludedInBill = OptionCategory.create(CategoryCode.CAT1);
        categoryRepository.save(categoryListIncludedInBill);

        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryListIncludedInBill, "수도세"),
                OptionItem.createDefault(categoryListIncludedInBill, "전기세"),
                OptionItem.createDefault(categoryListIncludedInBill, "인터넷비"),
                OptionItem.createDefault(categoryListIncludedInBill, "가스비"),
                OptionItem.createDefault(categoryListIncludedInBill, "청소비"),
                OptionItem.createDefault(categoryListIncludedInBill, "주차비"),
                OptionItem.createDefault(categoryListIncludedInBill, "직접입력")
        ));
        
        ///  가능 불가능 여부
        OptionCategory categoryPossibleNotPossible = OptionCategory.create(CategoryCode.CAT2);
        categoryRepository.save(categoryPossibleNotPossible);

        // 흡연자
        OptionItem smoker = itemRepository.save(
                OptionItem.createDefault(categoryPossibleNotPossible, "흡연자")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryPossibleNotPossible, smoker, "가능"),
                OptionItem.createDefault(categoryPossibleNotPossible, smoker, "불가능")
        ));
        
        // 반려동물
        OptionItem pet = itemRepository.save(
                OptionItem.createDefault(categoryPossibleNotPossible, "반려동물")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryPossibleNotPossible, pet, "가능"),
                OptionItem.createDefault(categoryPossibleNotPossible, pet, "불가능")
        ));
        
        // 외부인 방문
        OptionItem visiter = itemRepository.save(
                OptionItem.createDefault(categoryPossibleNotPossible, "외부인 방문")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryPossibleNotPossible, visiter, "가능"),
                OptionItem.createDefault(categoryPossibleNotPossible, visiter, "불가능")
        ));
        
        // 주차
        OptionItem parkingOptions = itemRepository.save(
                OptionItem.createDefault(categoryPossibleNotPossible, "주차")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryPossibleNotPossible, parkingOptions, "전용공간"),
                OptionItem.createDefault(categoryPossibleNotPossible, parkingOptions, "StreetParking"),
                OptionItem.createDefault(categoryPossibleNotPossible, parkingOptions, "불가능")
        ));
        
        // 주방
        OptionItem kitchen = itemRepository.save(
                OptionItem.createDefault(categoryPossibleNotPossible, "주방")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryPossibleNotPossible, kitchen, "가능"),
                OptionItem.createDefault(categoryPossibleNotPossible, kitchen, "불가능")
        ));
        
        ///  기본 제공 가전, 가구
        OptionCategory categoryFreeTools = OptionCategory.create(CategoryCode.CAT3);
        categoryRepository.save(categoryFreeTools);
        
        // 침실
        OptionItem bedroom = itemRepository.save(
                OptionItem.createDefault(categoryFreeTools, "침실")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryFreeTools, bedroom, "침대 프레임"),
                OptionItem.createDefault(categoryFreeTools, bedroom, "책상"),
                OptionItem.createDefault(categoryFreeTools, bedroom, "침구류"),
                OptionItem.createDefault(categoryFreeTools, bedroom, "옷장"),
                OptionItem.createDefault(categoryFreeTools, bedroom, "수납장"),
                OptionItem.createDefault(categoryFreeTools, bedroom, "의자")
        ));
        
        // 주방
        OptionItem kitchenInFree = itemRepository.save(
                OptionItem.createDefault(categoryFreeTools, "주방")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryFreeTools, kitchenInFree, "전자렌지"),
                OptionItem.createDefault(categoryFreeTools, kitchenInFree, "냉장고"),
                OptionItem.createDefault(categoryFreeTools, kitchenInFree, "가스렌지"),
                OptionItem.createDefault(categoryFreeTools, kitchenInFree, "식기류"),
                OptionItem.createDefault(categoryFreeTools, kitchenInFree, "조리도구")
        ));
        
        // 거실
        OptionItem livingRoom = itemRepository.save(
                OptionItem.createDefault(categoryFreeTools, "거실")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryFreeTools, livingRoom, "TV"),
                OptionItem.createDefault(categoryFreeTools, livingRoom, "소파"),
                OptionItem.createDefault(categoryFreeTools, livingRoom, "커피테이블")
        ));
        
        // 기타
        OptionItem others = itemRepository.save(
                OptionItem.createDefault(categoryFreeTools, "기타")
        );
        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryFreeTools, others, "Wifi"),
                OptionItem.createDefault(categoryFreeTools, others, "청소기")
        ));

        ///  이 매물의 장점
        OptionCategory categoryPropertyAdvantage = OptionCategory.create(CategoryCode.CAT4);
        categoryRepository.save(categoryPropertyAdvantage);

        itemRepository.saveAll(Arrays.asList(
                OptionItem.createDefault(categoryPropertyAdvantage, "햇빛이 잘들어요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "주변 편의시설이 많아요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "전망이 좋아요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "주변보다 저렴해요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "테라스가 있어요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "교통이 편리해요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "커뮤니티 시설이 좋아요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "집상태가 깨끗해요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "방음이 잘돼요"),
                OptionItem.createDefault(categoryPropertyAdvantage, "치안이 좋아요")
        ));

        */
/**
         * Rent Property 고유 아이템
         *//*


        */
/**
         * Share Property 고유 아이템
         *//*

    }
}*/
