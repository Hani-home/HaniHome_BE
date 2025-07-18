package org.hanihome.hanihomebe.viewing.web.converter.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.converter.ViewingConverter;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ViewingConvertContext {
    private final Viewing viewing;
    private final Map<String, Object> extra;

    public <V> V getExtra(String key) {
        return (V) extra.get(key);
    }

    public static ViewingConvertContext create(Viewing viewing, Map<String, Object> extra) {
        return new ViewingConvertContext(viewing, extra);
    }
}
