import { api } from "./api";
import {
   getPhotographerReviewsRequest,
   getPhotographerReviewsResponse,
   getPhotographersListRequest,
   photographerTableEntry,
   reportPhotographerReviewRequest,
} from "redux/types/api/photographerTypes";
import { getListResponse } from "redux/types/api/dataTypes";

const PhotographerManagementService = api.injectEndpoints({
   endpoints: (builder) => ({
      getActivePhotographers: builder.query<
         getListResponse<photographerTableEntry>,
         getPhotographersListRequest
      >({
         query: (data) => ({ url: "reservation/photographers", params: data }),
      }),
      getPhotographerReviews: builder.query<
         getPhotographerReviewsResponse,
         getPhotographerReviewsRequest
      >({
         query: (data) => ({
            url: "profile/review/list",
            params: data,
         }),
      }),
      reportPhotographerReview: builder.mutation<void, reportPhotographerReviewRequest>({
         query: (data) => ({ url: `report/review`, method: "POST", body: data }),
      }),
      getAvailableSpecializations: builder.query<string[], void>({
         query: () => ({ url: "profile/specialization-list" }),
      }),
      getOwnSpecializations: builder.query<string[], void>({
         query: () => ({ url: "profile/specializations" }),
      }),
      editOwnSpecializations: builder.mutation<void, string[]>({
         query: (data) => ({ url: "profile/specializations", method: "PUT", body: data }),
      }),
   }),
});

export const {
   useGetActivePhotographersQuery,
   useReportPhotographerReviewMutation,
   useGetPhotographerReviewsQuery,
   useGetAvailableSpecializationsQuery,
   useGetOwnSpecializationsQuery,
   useEditOwnSpecializationsMutation,
} = PhotographerManagementService;
