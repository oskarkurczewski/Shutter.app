package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;

@Data
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private String authorLogin;
    private String name;
    private String surname;
    private String email;
    private Long score;
    private String content;
    private Long likeCount;
    private boolean liked;

    public ReviewDto(Review review, boolean isLiked) {
        this.id = review.getId();
        this.authorLogin = review.getAccount().getLogin();
        this.name = review.getAccount().getName();
        this.surname = review.getAccount().getSurname();
        this.email = review.getAccount().getEmail();
        this.score = review.getScore();
        this.content = review.getContent();
        this.likeCount = review.getLikeCount();
        this.liked = isLiked;
    }
}
