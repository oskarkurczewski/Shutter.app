package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;

@Data
@NoArgsConstructor
public class PhotoDto {
    private String title;
    private String description;
    private String s3Url;

    public PhotoDto(Photo photo) {
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.s3Url = photo.getS3Url();
    }
}
