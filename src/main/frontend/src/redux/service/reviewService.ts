import {
   ReviewInfo,
   addReviewRequest,
   getPhotographerReviewsResponse,
   getPhotographerReviewsRequest,
} from "redux/types/api";
import { api } from "./api";

const ReviewService = api.injectEndpoints({
   endpoints: (builder) => ({
      likeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/like`,
            method: "POST",
         }),
         invalidatesTags: ["Review"],
      }),

      unlikeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/unlike`,
            method: "POST",
         }),
         invalidatesTags: ["Review"],
      }),

      getReviewById: builder.query<ReviewInfo, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}`,
            method: "GET",
         }),
         providesTags: ["Review"],
      }),

      removeSomeonesPhotographerReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/admin`,
            method: "DELETE",
         }),
         invalidatesTags: ["Review"],
      }),

      addReview: builder.mutation<void, addReviewRequest>({
         query: (data) => ({
            url: `/profile/review`,
            method: "POST",
            body: data,
         }),
         invalidatesTags: ["Review"],
      }),
      getPhotographerReviews: builder.query<
         getPhotographerReviewsResponse,
         getPhotographerReviewsRequest
      >({
         query: (data) => ({
            url: "profile/review/list",
            params: data,
         }),
         providesTags: ["Review"],
      }),
   }),
});

export const {
   useLikeReviewMutation,
   useUnlikeReviewMutation,
   useGetReviewByIdQuery,
   useRemoveSomeonesPhotographerReviewMutation,
   useAddReviewMutation,
   useGetPhotographerReviewsQuery,
} = ReviewService;
