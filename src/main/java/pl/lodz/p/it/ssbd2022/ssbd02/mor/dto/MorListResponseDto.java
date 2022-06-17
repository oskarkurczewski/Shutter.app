package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MorListResponseDto<T> {

    @NotNull
    private Integer pageNo;

    @NotNull
    private Integer allPages;

    @NotNull
    private Integer recordsPerPage;

    @NotNull
    private Long allRecords;

    @NotNull
    private List<T> list;
}
