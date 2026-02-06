package com.clock_in.clock.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedEmployeeResponseDTO {

    private List<EmployeeResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static PagedEmployeeResponseDTO from(
            List<EmployeeResponseDTO> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last
    ) {
        PagedEmployeeResponseDTO dto = new PagedEmployeeResponseDTO();
        dto.setContent(content);
        dto.setPage(page);
        dto.setSize(size);
        dto.setTotalElements(totalElements);
        dto.setTotalPages(totalPages);
        dto.setLast(last);
        return dto;
    }
}