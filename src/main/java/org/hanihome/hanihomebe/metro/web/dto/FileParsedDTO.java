package org.hanihome.hanihomebe.metro.web.dto;

import java.util.List;

public record FileParsedDTO(
        List<String> lines,
        String headerLine
) {
    public static FileParsedDTO create(List<String> lines, String headerLine) {
        return new FileParsedDTO(lines, headerLine);
    }
}

