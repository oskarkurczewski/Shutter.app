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
    Integer pageNo;

    @NotNull
    Integer allPages;

    @NotNull
    Integer recordsPerPage;

    @NotNull
    Long allRecords;

    @NotNull
    List<T> list;


}
