export interface basicUserInfoResponse {
   email: string;
   name: string;
   surname: string;
}

export interface registerAccountRequest {
   login: string;
   password: string;
   email: string;
   name: string;
   surname: string;
}

export interface registerAccountAsAdminRequest extends registerAccountRequest {
   registered: boolean;
   active: boolean;
}

export interface getListRequest {
   pageNo: number;
   recordsPerPage: number;
   columnName: string;
   order: string;
   login?: string;
   email?: string;
   name?: string;
   surname?: string;
   registered?: boolean;
   active?: boolean;
}
export interface getListResponse {
   allPages: number;
   allRecords: number;
   list: string[];
   pageNo: number;
   recordsPerPage: number;
}
