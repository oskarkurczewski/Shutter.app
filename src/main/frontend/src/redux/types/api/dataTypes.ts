export interface etag {
   version: number;
   etag: string;
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
