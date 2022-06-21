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
         invalidatesTags: (result, error, id) => [
            { type: "Review", id },
            { type: "Review", id: "PARTIAL-LIST" },
         ],
      }),

      unlikeReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/unlike`,
            method: "POST",
         }),
         invalidatesTags: (result, error, id) => [
            { type: "Review", id },
            { type: "Review", id: "PARTIAL-LIST" },
         ],
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
         invalidatesTags: (result, error, id) => [
            { type: "Review", id },
            { type: "Review", id: "PARTIAL-LIST" },
         ],
      }),

      removeSomeonesPhotographerReview: builder.mutation<void, number>({
         query: (reviewId) => ({
            url: `/profile/review/${reviewId}/admin`,
            method: "DELETE",
         }),
         invalidatesTags: (result, error, id) => [
            { type: "Review", id },
            { type: "Review", id: "PARTIAL-LIST" },
         ],
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
            method: "GET",
         }),
         providesTags: (result, error, page) =>
            result
               ? [
                    ...result.list.map(({ id }) => ({ type: "Review" as const, id })),
                    { type: "Review", id: "PARTIAL-LIST" },
                 ]
               : [{ type: "Review", id: "PARTIAL-LIST" }],
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
