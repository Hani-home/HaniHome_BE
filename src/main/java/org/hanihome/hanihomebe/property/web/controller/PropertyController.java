package org.hanihome.hanihomebe.property.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.application.service.PropertyService;
import org.hanihome.hanihomebe.property.web.dto.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController("/api/v1")
public class PropertyController {
    private final PropertyService propertyService;

    @GetMapping("/properties")
    public List<PropertyResponseDTO> getAll(){
        return propertyService.getAllProperties();
    }



    /**
     * contentType: application/json 아님. !! application/json-patch+json
     * */
    @PatchMapping(
            path = "/properties/{propertyId}",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PropertyResponseDTO> patchProperty(@PathVariable("propertyId") Long propertyId, @RequestBody JsonNode patchDocument) throws JsonPatchException, IOException {
        PropertyResponseDTO updated = propertyService.patch(propertyId, patchDocument);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/properties/{propertyId}")
    public PropertyResponseDTO patch(@PathVariable("propertyId") Long propertyId, @RequestBody PropertyPatchRequestDTO dto) throws JsonPatchException, IOException {
        PropertyResponseDTO responseDTO = propertyService.patch(propertyId, dto);
        return responseDTO;
    }


}
