import { api } from "./api";
import {
   getPhotographerReviewsRequest,
   getPhotographerReviewsResponse,
   getPhotographersListRequest,
   photographerTableEntry,
   reportPhotographerReviewRequest,
   photographerReport,
   reviewReport,
} from "redux/types/api/photographerTypes";
import { getListResponse, getReportListRequest } from "redux/types/api/dataTypes";

const PhotographerManagementService = api.injectEndpoints({
   endpoints: (builder) => ({
      getActivePhotographers: builder.query<
         getListResponse<photographerTableEntry>,
         getPhotographersListRequest
      >({
         query: (data) => ({ url: "reservation/photographers", params: data }),
      }),

      getPhotographerReportList: builder.query<
         getListResponse<photographerReport>,
         getReportListRequest
      >({
         query: (params) => ({ url: `/report/list/photographer`, params }),
      }),

      getReviewReportList: builder.query<
         getListResponse<reviewReport>,
         getReportListRequest
      >({
         query: (params) => ({ url: `/report/list/review`, params }),
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

      resolvePhotographerReport: builder.mutation<void, number>({
         query: (id) => ({ url: `/report/photographer/${id}/resolve`, method: "POST" }),
      }),

      resolveReviewReport: builder.mutation<void, number>({
         query: (id) => ({ url: `/report/review/${id}/resolve`, method: "POST" }),
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
   useGetPhotographerReportListQuery,
   useGetReviewReportListQuery,
   useReportPhotographerReviewMutation,
   useGetPhotographerReviewsQuery,
   useResolvePhotographerReportMutation,
   useResolveReviewReportMutation,
   useGetAvailableSpecializationsQuery,
   useGetOwnSpecializationsQuery,
   useEditOwnSpecializationsMutation,
} = PhotographerManagementService;
