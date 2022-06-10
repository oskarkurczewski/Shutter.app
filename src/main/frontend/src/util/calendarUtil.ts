import moment from "moment";
import { HourBox } from "types/CalendarTypes";

export const getHourRange = () => {
   const items: string[] = [];
   for (let i = 0; i < 24; i++) {
      items.push(moment({ hour: i }).format("HH:mm"));
   }
   return items;
};

export const getWeekRange = (weekStartDay: moment.Moment) => {
   const items: HourBox[][] = new Array(7).fill(0).map(() => new Array(48).fill(null));
   for (let i = 0; i < 336; i++) {
      const from = moment({ ...weekStartDay }).add({
         minutes: 30 * i,
      });

      const weekday = Math.floor(i / 48);
      const hour = i % 48;

      items[weekday][hour] = {
         selected: false,
         from,
         weekday,
      };
   }

   return items;
};

export const formatWeekLabel = (week: HourBox[][]) => {
   const start = week[0][0].from;
   const end = week[6][0].from;

   let res = start.format("DD");
   if (start.month() !== end.month()) {
      res += start.format(" MMM");
      if (start.year() !== end.year()) {
         res += start.format(" YYYY");
      }
      res += start.format(" - ");
   } else {
      res += "-";
   }

   res += end.format("DD MMM YYYY");

   return res;
};
