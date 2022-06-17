import { api } from "./api";
import { DateTime } from "luxon";
import {
   basicPhotographerInfo,
   availabilityResponse,
} from "redux/types/api/photographerTypes";
import { AvailabilityHour } from "types/CalendarTypes";

const PhotographerService = api.injectEndpoints({
   endpoints: (builder) => ({
      getPhotographerDetailedInfo: builder.query<basicPhotographerInfo, string>({
         query: (login) => ({ url: `/photographer/${login}/info` }),
      }),

      getAvailabityHours: builder.query<AvailabilityHour[], string>({
         query: (login) => ({ url: `/availability/${login}` }),
         transformResponse(data: availabilityResponse[]) {
            return data.map((availability) => ({
               id: availability.id,
               day: availability.day,
               from: DateTime.fromFormat(availability.from, "hh:mm:ss"),
               to: DateTime.fromFormat(availability.to, "hh:mm:ss"),
            }));
         },
      }),
   }),
});

export const { useGetPhotographerDetailedInfoQuery, useGetAvailabityHoursQuery } =
   PhotographerService;
