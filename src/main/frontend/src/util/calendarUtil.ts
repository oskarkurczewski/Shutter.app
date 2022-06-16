import { DateTime, Duration } from "luxon";
import { HourBox } from "types/CalendarTypes";

export const getHourRange = () => {
   const items: string[] = [];
   for (let i = 0; i < 24; i++) {
      items.push(
         Duration.fromObject({
            hours: i,
         }).toFormat("hh:mm")
      );
   }
   return items;
};

export const getWeekRange = (weekStartDay: DateTime) => {
   const items: HourBox[][] = new Array(7).fill(0).map(() => new Array(48).fill(null));
   for (let i = 0; i < 336; i++) {
      const from = weekStartDay.plus({
         minutes: 30 * i,
      });
      const to = from.plus({
         minutes: 30,
      });

      const weekday = Math.floor(i / 48) + 1;
      const hour = i % 48;

      items[weekday - 1][hour] = {
         selected: false,
         from,
         to,
         weekday,
      };
   }

   return items;
};

export const formatWeekLabel = (week: HourBox[][], lang: string) => {
   const start = week[0][0].from;
   const end = week[6][0].from;

   let res = start.toFormat("dd");
   if (!start.hasSame(end, "month")) {
      res += start.setLocale(lang).toFormat(" MMM");
      if (!start.hasSame(end, "year")) {
         res += start.toFormat(" yyyy");
      }
      res += " - ";
   } else {
      res += "-";
   }

   res += end.setLocale(lang).toFormat("dd MMM yyyy");
   return res;
};
