import { api } from "./api";

const ReviewService = api.injectEndpoints({
   endpoints: (builder) => ({
      likeReview: builder.mutation<void, number>({
         query: (reviewId) => ({ 
            url: `/profile/review/${reviewId}/like`,
            method: "POST"
         }),
      }),
   }),
});

export const { useLikeReviewMutation } = ReviewService;
