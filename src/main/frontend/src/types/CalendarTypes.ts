import { DateTime } from "luxon";

export type RenderHourBoxElement = (
   onMouseEnter?: React.MouseEventHandler<HTMLDivElement>
) => React.ReactElement;

export type HourBox = {
   from: DateTime;
   weekday: number;
   selected: boolean;
};

export type AvailabilityHour = {
   id: number;
   day: number;
   from: DateTime;
   to: DateTime;
};

export type Reservation = {
   id: number;
   photographer: string;
   client: string;
   from: DateTime;
   to: DateTime;
};
