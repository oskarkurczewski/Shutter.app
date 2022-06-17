package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;

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

    public PhotoDto(Photo photo, boolean isLiked) {
        this.id = photo.getId();
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.s3Url = photo.getS3Url();
        this.likeCount = photo.getLikeCount();
        this.createdAt = photo.getCreatedAt();
        this.liked = isLiked;
    }
}
