package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;

@Data
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private String name;
    private String surname;
    private Long score;
    private String content;
    private Long likeCount;
    private boolean liked;

    public ReviewDto(Review review, boolean isLiked) {
        this.id = review.getId();
        this.name = review.getPhotographer().getAccount().getName();
        this.surname = review.getPhotographer().getAccount().getSurname();;
        this.score = review.getScore();
        this.content = review.getContent();
        this.likeCount = review.getLikeCount();
        this.liked = isLiked;
    }
}
