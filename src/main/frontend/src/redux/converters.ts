import { DateTime } from "luxon";
import { AvailabilityHour } from "types/CalendarTypes";
import { AvailabilityRequest, AvailabilityResponse } from "./types/api";
import { Weekday } from "./types/api/dataTypes";

export const parseToAvailabilityHour = (data: AvailabilityResponse[]) => {
   return data.map((availability) => ({
      id: availability.id,
      day: availability.day + 1,
      from: DateTime.fromFormat(availability.from, "HH:mm:ss").set({
         weekday: availability.day + 1,
      }),
      to: DateTime.fromFormat(availability.to, "HH:mm:ss").set({
         weekday: availability.day + 1,
      }),
   }));
};

export const parseToAvailabilityRequest = (
   data: AvailabilityHour[]
): AvailabilityRequest[] => {
   return data.map((availability) => ({
      day: Weekday[availability.day],
      from: availability.from.toFormat("HH:mm:ss"),
      to: availability.to.toFormat("HH:mm:ss"),
   }));
};
