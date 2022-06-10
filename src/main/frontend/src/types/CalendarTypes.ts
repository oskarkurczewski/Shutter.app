export type RenderHourBoxElement = (
   onMouseEnter?: React.MouseEventHandler<HTMLDivElement>
) => React.ReactElement;

export type HourBox = {
   from: moment.Moment;
   weekday: number;
   selected: boolean;
};
