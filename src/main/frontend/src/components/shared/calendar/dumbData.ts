import { DateTime } from "luxon";
import { AvailabilityHour, Reservation } from "types/CalendarTypes";

export const availabilityHours: AvailabilityHour[] = [
   {
      from: DateTime.local(2022, 6, 6, 10, 0),
      to: DateTime.local(2022, 6, 6, 13, 0),
   },
   {
      from: DateTime.local(2022, 6, 6, 14, 0),
      to: DateTime.local(2022, 6, 6, 18, 0),
   },
   {
      from: DateTime.local(2022, 6, 7, 8, 0),
      to: DateTime.local(2022, 6, 7, 10, 0),
   },
   {
      from: DateTime.local(2022, 6, 7, 15, 0),
      to: DateTime.local(2022, 6, 7, 17, 30),
   },
   {
      from: DateTime.local(2022, 6, 9, 10, 0),
      to: DateTime.local(2022, 6, 9, 15, 0),
   },
   {
      from: DateTime.local(2022, 6, 10, 9, 0),
      to: DateTime.local(2022, 6, 10, 14, 0),
   },
   {
      from: DateTime.local(2022, 6, 10, 14, 0),
      to: DateTime.local(2022, 6, 10, 16, 30),
   },
];

export const reservations: Reservation[] = [
   {
      from: DateTime.local(2022, 6, 14, 10, 30),
      to: DateTime.local(2022, 6, 14, 12, 30),
   },
   {
      from: DateTime.local(2022, 6, 17, 11, 0),
      to: DateTime.local(2022, 6, 17, 12, 0),
   },
   {
      from: DateTime.local(2022, 6, 17, 12, 30),
      to: DateTime.local(2022, 6, 17, 14, 30),
   },
];
