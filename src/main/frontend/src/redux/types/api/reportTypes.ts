export interface UserReportRequest {
   reportedLogin: string;
   cause: UserReportCause;
}

export enum UserReportCause {
   PAY_ON_TIME = "USER_REPORT_DIDNT_PAY_ON_TIME",
   SHOW_UP = "USER_REPORT_DIDNT_SHOW_UP",
}
