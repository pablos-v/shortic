package org.pablos.common.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Data
@RequiredArgsConstructor
public class PageDTO {
    private final List<ClickDTO> clicks;
    private final int totalPages;
    private final LinkUnitDTO linkUnit;
    private final int totalClicks;
}
