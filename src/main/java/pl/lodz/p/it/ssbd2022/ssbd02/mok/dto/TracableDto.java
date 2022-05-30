package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TracableDto {
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private LocalDateTime createdAt;
    private String createdBy;

}
