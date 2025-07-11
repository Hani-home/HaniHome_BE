package org.hanihome.hanihomebe.viewing.web.converter;

import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;

public interface ViewingConverter<T> {
    ViewingViewType supports();

    T convert(Viewing viewing);
}
