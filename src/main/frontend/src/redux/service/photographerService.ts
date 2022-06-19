import { api } from "./api";
import {
   basicPhotographerInfo,
   AvailabilityResponse,
   ReservationResponse,
   ReservationRequest,
   AddReservationRequest,
} from "redux/types/api/photographerTypes";
import { AvailabilityHour, Reservation } from "types/CalendarTypes";
import { parseToAvailabilityRequest } from "redux/converters";

const PhotographerService = api.injectEndpoints({
   endpoints: (builder) => ({
      getPhotographerDetailedInfo: builder.query<basicPhotographerInfo, string>({
         query: (login) => ({ url: `/photographer/${login}/info` }),
      }),

      getAvailabityHours: builder.query<AvailabilityResponse[], string>({
         query: (login) => ({ url: `/availability/${login}` }),
      }),

      getReservationsForUser: builder.query<AvailabilityResponse[], ReservationRequest>({
         query: (data) => ({ url: `/reservation/${data.name}?date=${data.date}` }),
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

      createReservation: builder.mutation<void, AddReservationRequest>({
         query: (reservation) => ({
            url: "/reservation",
            method: "POST",
            body: {
               photographerLogin: reservation.photographerLogin,
               from: reservation.from.toFormat("yyyy-MM-dd'T'HH:mm:ss"),
               to: reservation.to.toFormat("yyyy-MM-dd'T'HH:mm:ss"),
            },
         }),
      }),
   }),
});

export const {
   useCreateReservationMutation,
   useGetPhotographerDetailedInfoQuery,
   useGetReservationsForUserQuery,
   useGetAvailabityHoursQuery,
   useUpdateAvailabilityHoursMutation,
   useGetJobListMutation,
} = PhotographerService;
