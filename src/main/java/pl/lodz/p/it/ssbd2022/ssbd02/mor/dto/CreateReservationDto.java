package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Reservation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Reservation
public class CreateReservationDto {
    @NotNull(message = "validator.incorrect.photographer_login.null")
    @Login
    private String photographerLogin;
    @NotNull(message = "validator.incorrect.time_from.null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime from;
    @NotNull(message = "validator.incorrect.time_to.null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime to;
}
