import { api } from "./api";
import {
   basicPhotographerInfo,
   AvailabilityResponse,
   ReservationResponse,
   ReservationRequest,
} from "redux/types/api/photographerTypes";
import { AvailabilityHour } from "types/CalendarTypes";
import { parseToAvailabilityRequest } from "redux/converters";

const PhotographerService = api.injectEndpoints({
   endpoints: (builder) => ({
      getPhotographerDetailedInfo: builder.query<basicPhotographerInfo, string>({
         query: (login) => ({ url: `/photographer/${login}/info` }),
      }),

      getAvailabityHours: builder.query<AvailabilityResponse[], string>({
         query: (login) => ({ url: `/availability/${login}` }),
      }),

      updateAvailabilityHours: builder.mutation<void, AvailabilityHour[]>({
         query: (availability) => ({
            url: "/availability",
            method: "PUT",
            body: parseToAvailabilityRequest(availability),
         }),
      }),

      getJobList: builder.mutation<ReservationResponse[], ReservationRequest>({
         query: (data) => ({
            url: "/reservation/my-jobs",
            params: data,
         }),
      }),

      discardJob: builder.mutation<void, number>({
         query: (id) => ({
            url: `/reservation/${id}/discard`,
            method: "DELETE",
         }),
      }),
   }),
});

export const {
   useDiscardJobMutation,
   useGetPhotographerDetailedInfoQuery,
   useGetAvailabityHoursQuery,
   useUpdateAvailabilityHoursMutation,
   useGetJobListMutation,
} = PhotographerService;
