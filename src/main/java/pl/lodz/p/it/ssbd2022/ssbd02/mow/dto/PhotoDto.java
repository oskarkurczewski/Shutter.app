package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PhotoDto {
    private Long id;
    private String title;
    private String description;
    private String s3Url;
    private Long likeCount;
    private LocalDateTime createdAt;
    private boolean liked;
    private String photographerLogin;
    private String photographerName;

    public PhotoDto(Photo photo, boolean isLiked, PhotographerInfo photographerInfo) {
        this.id = photo.getId();
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.s3Url = photo.getS3Url();
        this.likeCount = photo.getLikeCount();
        this.createdAt = photo.getCreatedAt();
        this.liked = isLiked;
        this.photographerLogin = photographerInfo.getAccount().getLogin();
        this.photographerName = photographerInfo.getAccount().getName() + " " + photographerInfo.getAccount().getSurname();
    }
}
