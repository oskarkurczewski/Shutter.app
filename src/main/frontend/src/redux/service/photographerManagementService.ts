import { api } from "./api";
import {
   GetPhotographerReviewsRequest,
   GetPhotographerReviewsResponse,
   GetPhotographersListRequest,
   PhotographerTableEntry,
   ReportPhotographerReviewRequest,
} from "redux/types/api/photographerTypes";
import { getListResponse } from "redux/types/api/dataTypes";

const PhotographerManagementService = api.injectEndpoints({
   endpoints: (builder) => ({
      getActivePhotographers: builder.mutation<
         getListResponse<PhotographerTableEntry>,
         GetPhotographersListRequest
      >({
         query: (data) => ({ url: "reservation/photographers", params: data }),
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
      reportPhotographerReview: builder.mutation<void, ReportPhotographerReviewRequest>({
         query: (data) => ({ url: `report/review`, method: "POST", body: data }),
      }),
   }),
});

export const {
   useGetActivePhotographersMutation,
   useReportPhotographerReviewMutation,
   useGetPhotographerReviewsQuery,
} = PhotographerManagementService;
