import { ReviewInfo, addReviewRequest } from "redux/types/api";
import { api } from "./api";

const ReviewService = api.injectEndpoints({
   endpoints: (builder) => ({
      likeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/like`,
            method: "POST",
         }),
         invalidatesTags: (result, error, arg) => [{ type: "Review", id: arg }],
      }),

      unlikeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/unlike`,
            method: "POST",
         }),
         invalidatesTags: (result, error, arg) => [{ type: "Review", id: arg }],
      }),

      getReviewById: builder.query<ReviewInfo, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}`,
            method: "GET",
         }),
         providesTags: (result, error, arg) => [{ type: "Review", id: result.id }],
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
   useUnlikeReviewMutation,
   useGetReviewByIdQuery,
   useRemoveSomeonesPhotographerReviewMutation,
   useAddReviewMutation,
} = ReviewService;
