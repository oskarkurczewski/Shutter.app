import {
   ReviewInfo,
   addReviewRequest,
   GetPhotographerReviewsRequest,
   GetPhotographerReviewsResponse,
} from "redux/types/api";
import { api } from "./api";

const ReviewService = api.injectEndpoints({
   endpoints: (builder) => ({
      likeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/like`,
            method: "POST",
         }),
      }),

      unlikeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/unlike`,
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

      getPhotographerReviews: builder.query<
         GetPhotographerReviewsResponse,
         GetPhotographerReviewsRequest
      >({
         query: (data) => ({
            url: "profile/review/list",
            params: data,
         }),
      }),
   }),
});

export const {
   useLikeReviewMutation,
   useUnlikeReviewMutation,
   useGetReviewByIdQuery,
   useRemoveOwnPhotographerReviewMutation,
   useRemoveSomeonesPhotographerReviewMutation,
   useAddReviewMutation,
   useGetPhotographerReviewsQuery,
} = ReviewService;
