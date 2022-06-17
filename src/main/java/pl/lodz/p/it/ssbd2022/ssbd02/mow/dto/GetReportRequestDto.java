package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetReportRequestDto {
    private Boolean reviewed;
    private String order;
    private int page;
    private int recordsPerPage;
}
