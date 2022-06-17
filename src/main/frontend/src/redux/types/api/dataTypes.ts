export interface etag {
   version: number;
   etag: string;
}

export enum Weekday {
   MONDAY = 1,
   TUESDAY,
   WEDNESDAY,
   THURSDAY,
   FRIDAY,
   SATURDAY,
   SUNDAY,
}

export interface EtagData<T> {
   data: T;
   etag: etag;
}

export interface getListResponse<T> {
   allPages: number;
   allRecords: number;
   list: T[];
   pageNo: number;
   recordsPerPage: number;
}

export interface getReportListRequest {
   reviewed?: boolean;
   order: string;
   page: number;
   recordsPerPage: number;
}
