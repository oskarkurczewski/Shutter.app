package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;


import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;

import java.time.LocalDateTime;

@Data
public class GetReviewDto {
    private Long id;
    private String photographerLogin;
    private String reviewerLogin;
    private Long score;
    private Long likeCount;
    private String content;
    private Boolean active;
    private LocalDateTime createdAt;

    public GetReviewDto(Review review) {
        this.id = review.getId();
        this.photographerLogin = review.getPhotographer().getAccount().getLogin();
        this.reviewerLogin = review.getAccount().getLogin();
        this.score = review.getScore();
        this.likeCount = review.getLikeCount();
        this.content = review.getContent();
        this.active = review.getActive();
        this.createdAt = review.getCreatedAt();
    }
}
