package org.hanihome.hanihomebe.property.web.dto.response.summary;

public record MetaInfo(
        boolean owner,
        boolean wished
) {
    public static MetaInfo create(boolean owner, boolean wished) {
        return new MetaInfo(owner, wished);
    }
}
