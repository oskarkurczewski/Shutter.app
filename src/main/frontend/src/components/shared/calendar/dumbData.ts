import { DateTime } from "luxon";
import { AvailabilityHour, Reservation } from "types/CalendarTypes";

export const availabilityHours: AvailabilityHour[] = [
   {
      id: 1,
      day: 0,
      from: DateTime.local(2022, 6, 6, 10, 0),
      to: DateTime.local(2022, 6, 6, 13, 0),
   },
   {
      id: 2,
      day: 0,
      from: DateTime.local(2022, 6, 6, 14, 0),
      to: DateTime.local(2022, 6, 6, 18, 0),
   },
   {
      id: 3,
      day: 1,
      from: DateTime.local(2022, 6, 7, 8, 0),
      to: DateTime.local(2022, 6, 7, 10, 0),
   },
   {
      id: 4,
      day: 1,
      from: DateTime.local(2022, 6, 7, 15, 0),
      to: DateTime.local(2022, 6, 7, 17, 30),
   },
   {
      id: 5,
      day: 3,
      from: DateTime.local(2022, 6, 9, 10, 0),
      to: DateTime.local(2022, 6, 9, 15, 0),
   },
   {
      id: 6,
      day: 4,
      from: DateTime.local(2022, 6, 10, 9, 0),
      to: DateTime.local(2022, 6, 10, 14, 0),
   },
   {
      id: 7,
      day: 4,
      from: DateTime.local(2022, 6, 10, 14, 0),
      to: DateTime.local(2022, 6, 10, 16, 30),
   },
];

export const reservations: Reservation[] = [
   {
      id: 0,
      photographer: "majster2",
      client: "majster3",
      from: DateTime.local(2022, 6, 14, 10, 30),
      to: DateTime.local(2022, 6, 14, 12, 30),
   },
   {
      id: 1,
      photographer: "majster2",
      client: "majster4",
      from: DateTime.local(2022, 6, 17, 11, 0),
      to: DateTime.local(2022, 6, 17, 12, 0),
   },
   {
      id: 2,
      photographer: "majster2",
      client: "majster3",
      from: DateTime.local(2022, 6, 17, 12, 30),
      to: DateTime.local(2022, 6, 17, 14, 30),
   },
   {
      id: 3,
      photographer: "majster2",
      client: "majster3",
      from: DateTime.local(2022, 6, 17, 16, 30),
      to: DateTime.local(2022, 6, 17, 20, 0),
   },
];
