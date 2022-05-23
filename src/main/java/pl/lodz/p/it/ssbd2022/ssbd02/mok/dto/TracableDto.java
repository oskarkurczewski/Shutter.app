package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TracableDto {
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private LocalDateTime createdAt;
    private String createdBy;

}
