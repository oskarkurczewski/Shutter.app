import { api } from "./api";
import {
   BasicPhotographerInfo,
   AvailabilityResponse,
} from "redux/types/api/photographerTypes";
import { AvailabilityHour } from "types/CalendarTypes";
import { parseToAvailabilityHour, parseToAvailabilityRequest } from "redux/converters";

const PhotographerService = api.injectEndpoints({
   endpoints: (builder) => ({
      getPhotographerDetailedInfo: builder.query<BasicPhotographerInfo, string>({
         query: (login) => ({ url: `/photographer/${login}/info` }),
      }),

      getAvailabityHours: builder.query<AvailabilityHour[], string>({
         query: (login) => ({ url: `/availability/${login}` }),
         transformResponse(data: AvailabilityResponse[]) {
            return parseToAvailabilityHour(data);
         },
      }),

      updateAvailabilityHours: builder.mutation<void, AvailabilityHour[]>({
         query: (availability) => ({
            url: "/availability",
            method: "PUT",
            body: parseToAvailabilityRequest(availability),
         }),
      }),
   }),
});

export const {
   useGetPhotographerDetailedInfoQuery,
   useGetAvailabityHoursQuery,
   useUpdateAvailabilityHoursMutation,
} = PhotographerService;
