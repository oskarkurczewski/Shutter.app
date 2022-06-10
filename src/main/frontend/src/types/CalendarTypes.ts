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
   from: DateTime;
   to: DateTime;
};

export type Reservation = {
   // photographer: string;
   // client: string;
   from: DateTime;
   to: DateTime;
};
