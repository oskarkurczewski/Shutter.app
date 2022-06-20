import { ReviewInfo, addReviewRequest } from "redux/types/api";
import { api } from "./api";

const ReviewService = api.injectEndpoints({
   endpoints: (builder) => ({
      likeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/like`,
            method: "POST",
         }),
      }),

      getReviewById: builder.query<ReviewInfo, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}`,
            method: "GET",
         }),
      }),

      removeOwnPhotographerReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}`,
            method: "DELETE",
         }),
      }),

      removeSomeonesPhotographerReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/admin`,
            method: "DELETE",
         }),
      }),

      addReview: builder.mutation<void, addReviewRequest>({
         query: (data) => ({
            url: `/profile/review`,
            method: "POST",
            body: data,
         }),
      }),
   }),
});

export const {
   useLikeReviewMutation,
   useGetReviewByIdQuery,
   useRemoveOwnPhotographerReviewMutation,
   useRemoveSomeonesPhotographerReviewMutation,
   useAddReviewMutation,
} = ReviewService;
